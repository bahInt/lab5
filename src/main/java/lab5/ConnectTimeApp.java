package lab5;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Query;
import akka.japi.Pair;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Source;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ConnectTimeApp {
    private static final String SYS_NAME = "webtimechecker";
    private static final String HOST = "localhost";
    private static final String URL = "connect";
    private static final String COUNT = "repeat";
    private static final int PORT = 8080;
    private static final Duration TIMEOUT = Duration.ofSeconds(5);


    public static void main(String[] args) throws IOException {
        ActorSystem system = ActorSystem.create(SYS_NAME);
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        ActorRef actor = system.actorOf(Props.create(CasherActor.class));
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = createFlow(http, system, materializer, actor);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer
        );
        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    public static Flow<HttpRequest, HttpResponse, NotUsed> createFlow(Http http, ActorSystem system, ActorMaterializer materializer, ActorRef casher){
        return Flow.of(HttpRequest.class)
                .map((r) -> {
                    Query query = r.getUri().query();
                    String url = query.getOrElse(URL, HOST);
                    int count = Integer.parseInt(query.getOrElse(COUNT, "1"));
                    return new Pair<>(url, count);
                })
                .mapAsync(2, (Pair<String, Integer> p) ->
                        Patterns.ask(casher, p.first(), TIMEOUT).thenCompose((Object t) -> {
                            if((float) t >= 0) return CompletableFuture.completedFuture(new Pair<>(p.first(), (float) t));
                            return Source.from(Collections.singletonList(p))
                                    .toMat(testSink, Keep.right())
                                    .run(materializer)
                        }))
                .map();

    }
}
