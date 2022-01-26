package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import mobilepayment.Messages.BankCommands;
import mobilepayment.Messages.Deposite;

public class Account extends AbstractBehavior<BankCommands> {

    /* --- State ---------------------------------------- */
    private int balance = 0;

    /* --- Constructor ---------------------------------- */
    private Account(ActorContext<BankCommands> context) {
        super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<BankCommands> create() {
        return Behaviors.setup(Account::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<BankCommands> createReceive() {
        return newReceiveBuilder().onMessage(Deposite.class, this::onDeposite).build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<BankCommands> onDeposite(Deposite deposite) {
        getContext().getLog().info("Account {} has a balance of {}", getContext().getSelf().path().name(),
                this.balance);
                
        this.balance += deposite.amount;

        getContext().getLog().info("Account {} NOW has a balance of {}", getContext().getSelf().path().name(),
                this.balance);

        return this;
    }
}
