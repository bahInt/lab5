package lab5;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.HashMap;
import java.util.Map;

public class CasherActor extends AbstractActor {
    private final Map<String, Float> cash = new HashMap<>();

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(String.class, )
                .build();
    }
}
