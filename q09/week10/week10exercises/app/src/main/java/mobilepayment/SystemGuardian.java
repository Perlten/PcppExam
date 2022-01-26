package mobilepayment;

import java.util.ArrayList;
import java.util.List;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import mobilepayment.Messages.BankCommands;
import mobilepayment.Messages.Kickoff;
import mobilepayment.Messages.MakePayment;

public class SystemGuardian extends AbstractBehavior<BankCommands> {

    /* --- State ---------------------------------------- */
    ActorRef<BankCommands> mobileApp;

    List<ActorRef<BankCommands>> accounts = new ArrayList<>();
    List<ActorRef<BankCommands>> banks = new ArrayList<>();

    /* --- Constructor ---------------------------------- */
    private SystemGuardian(ActorContext<BankCommands> context) {
        super(context);

        mobileApp = getContext().spawn(MobileApp.create(), "mobileApp");

        banks.add(getContext().spawn(Bank.create(), "bank"));

        for (int i = 0; i < 2; i++) {
            accounts.add(getContext().spawn(Account.create(), "account_" + i));
        }

    }

    /* --- Actor initial behavior ----------------------- */
    public static Behavior<BankCommands> create() {
        return Behaviors.setup(SystemGuardian::new);
    }

    /* --- Message handling ----------------------------- */
    @Override
    public Receive<BankCommands> createReceive() {
        return newReceiveBuilder().onMessage(Kickoff.class, this::onKickoff).build();
    }

    /* --- Handlers ------------------------------------- */
    public Behavior<BankCommands> onKickoff(Kickoff kickoff) {
        var bank = this.banks.get(0);
        var a1 = this.accounts.get(0);
        var a2 = this.accounts.get(1);

        mobileApp.tell(new MakePayment(bank, a1, a2));
        return this;
    }

}
