package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import mobilepayment.Messages.BankCommands;
import mobilepayment.Messages.Transaction;
import mobilepayment.Messages.Deposite;;

public class Bank extends AbstractBehavior<BankCommands> {

    /* --- State ---------------------------------------- */

    /* --- Constructor ---------------------------------- */
    private Bank(ActorContext<BankCommands> context) {
        super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<BankCommands> create() {
        return Behaviors.setup(Bank::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<BankCommands> createReceive() {
        return newReceiveBuilder().onMessage(Transaction.class, this::onTransaction).build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<BankCommands> onTransaction(Transaction transaction) {
        final int amount = transaction.amount;
        transaction.from.tell(new Deposite(-amount));
        transaction.to.tell(new Deposite(amount));

        return this;
    }
}
