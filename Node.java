import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;

public class Node extends AbstractBehavior<Node.M> {
    public Node(ActorContext context) {
        super(context);
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder()
                .onMessage(Ping.class, this::onPing)
                .onMessage(FindNode.class, this::onFind)
                .build();
    }

    public Behavior<M> onPing(Ping p) {
        return this;
    }

    public Behavior<M> onFind(FindNode p) {
        return this;
    }

    public record Ping() implements M {}
    public record FindNode() implements M {}
    public interface M { }
}
