import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node extends AbstractBehavior<Node.M> {
    private final int a;
    private Map<Integer, ActorRef<M>> kBuckets;

    private Node(ActorContext context, int a) {
        super(context);
        this.a = a;
        this.kBuckets = new HashMap<>();
    }

    public static Behavior<M> create(int a) {
        return Behaviors.setup(context -> new Node(context, a));
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder()
                .onMessage(Ping.class, this::onPing)
                .onMessage(FindNode.class, this::onFind)
                .build();
    }

    private Behavior<M> onPing(Ping p) {
        p.replyTo().tell(new Pong());
        return this;
    }

    private Behavior<M> onFind(FindNode n) {
        List<Map.Entry<Integer, ActorRef<M>>> sortedByDistance = kBuckets.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey()^n.id())).toList();
        n.replyTo().tell(new NodeList(sortedByDistance.subList(0, a)));
        return this;
    }

    public record Ping(ActorRef<M> replyTo) implements M { }
    public record Pong() implements M { }
    public record FindNode(int id, ActorRef<M> replyTo) implements M { }
    public record NodeList(List<Map.Entry<Integer, ActorRef<M>>> ids) implements M { }

    public interface M { }
}
