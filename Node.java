import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class Node extends AbstractBehavior<Node.M> {
    private Node(ActorContext context) {
        super(context);
    }

    public static Behavior<M> create() {
        return Behaviors.setup(Node::new);
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder()
                .onMessage(Ping.class, this::onPing)
                .onMessage(FindNode.class, this::onFind)
                .build();
    }

    public Behavior<M> onPing(Ping p) {
        p.replyTo().tell(new Pong());
        return this;
    }

    public Behavior<M> onFind(FindNode p) {
        return this;
    }

    public record Ping(ActorRef<M> replyTo) implements M {}
    public record Pong() implements M {}
    public interface M { }
}
