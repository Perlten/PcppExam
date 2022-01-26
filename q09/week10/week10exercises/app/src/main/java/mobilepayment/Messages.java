package mobilepayment;

import akka.actor.Actor;
import akka.actor.typed.ActorRef;

public class Messages {

    public interface BankCommands {
    };

    public static final class Kickoff implements BankCommands {
    }

    public static final class Deposite implements BankCommands {
        public final int amount;

        public Deposite(int amount) {
            this.amount = amount;
        }
    }

    public static final class Transaction implements BankCommands {
        public final ActorRef<BankCommands> from;
        public final ActorRef<BankCommands> to;
        public final int amount;

        public Transaction(ActorRef<BankCommands> from, ActorRef<BankCommands> to, int amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }
    }

    public static final class MakePayment implements BankCommands {
        public final ActorRef<BankCommands> bank;
        public final ActorRef<BankCommands> fromAccount;
        public final ActorRef<BankCommands> toAccount;

        public MakePayment(ActorRef<BankCommands> bank, ActorRef<BankCommands> fromAccount,
                ActorRef<BankCommands> toAccount) {
            this.bank = bank;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
        }

    }

}
