package lab5;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;

import java.util.Map;

public class CasherActor extends AbstractActor {
    private final Map<String, Float> cash = new

    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match()
                .build();
    }
}
