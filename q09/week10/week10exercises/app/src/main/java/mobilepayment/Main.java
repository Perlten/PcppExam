package mobilepayment;

import akka.actor.typed.ActorSystem;
import mobilepayment.Messages.BankCommands;
import mobilepayment.Messages.Kickoff;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {

		final ActorSystem<BankCommands> guardian = ActorSystem.create(SystemGuardian.create(), "system_guardian");

		guardian.tell(new Kickoff());

		try {
			System.out.println(">>> Press ENTER to exit <<<");
			System.in.read();
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
