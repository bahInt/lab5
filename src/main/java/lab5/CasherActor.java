package lab5;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;

public class CasherActor extends AbstractActor {
    private final Map<String, Float> cash = new HashMap<>();

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(String.class, r -> sender().tell(cash.getOrDefault(r, (float)-1.0), ActorRef.noSender()))
                .match(StorageMessage.class, r -> cash.put(r.getUrl(), r.getAvgTime()))
                .build();
    }
}
