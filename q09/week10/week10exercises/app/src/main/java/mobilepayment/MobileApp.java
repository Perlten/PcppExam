package mobilepayment;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import mobilepayment.Messages.BankCommands;
import mobilepayment.Messages.MakePayment;
import mobilepayment.Messages.Transaction;

import java.util.ArrayList;
import java.util.List;
// Hint: You may generate random numbers using Random::ints
import java.util.Random;

public class MobileApp extends AbstractBehavior<BankCommands> {

    /* --- State ---------------------------------------- */
    
    /* --- Constructor ---------------------------------- */
    private MobileApp(ActorContext<BankCommands> context) {
        super(context);
    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<BankCommands> create() {
        return Behaviors.setup(MobileApp::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<BankCommands> createReceive() {
        return newReceiveBuilder().onMessage(MakePayment.class, this::onMakePayment).build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<BankCommands> onMakePayment(MakePayment makePayment) {
        Random rand = new Random();

        for (int i = 0; i < 100; i++) {

            int amount = rand.nextInt(1000);
            
            makePayment.bank.tell(new Transaction(makePayment.fromAccount, makePayment.toAccount, amount));
        }

        return this;
    }
}
