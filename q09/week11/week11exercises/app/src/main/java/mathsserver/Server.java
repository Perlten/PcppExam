package mathsserver;

// Hint: The imports below may give you hints for solving the exercise.
//       But feel free to change them.

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.ChildFailed;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.*;

import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.IntStream;

import mathsserver.Task;
import mathsserver.Task.BinaryOperation;
import mathsserver.Worker.ComputeTask;
import mathsserver.Worker.Stop;
import mathsserver.Worker.WorkerCommand;

public class Server extends AbstractBehavior<Server.ServerCommand> {
	/* --- Messages ------------------------------------- */
	public interface ServerCommand {
	}

	public static final class ComputeTasks implements ServerCommand {
		public final List<Task> tasks;
		public final ActorRef<Client.ClientCommand> client;

		public ComputeTasks(List<Task> tasks, ActorRef<Client.ClientCommand> client) {
			this.tasks = tasks;
			this.client = client;
		}
	}

	public static final class WorkDone implements ServerCommand {
		ActorRef<Worker.WorkerCommand> worker;
		ActorRef<Client.ClientCommand> client;

		public WorkDone(ActorRef<Worker.WorkerCommand> worker, ActorRef<Client.ClientCommand> client) {
			this.worker = worker;
			this.client = client;
		}
	}

	/* --- State ---------------------------------------- */

	private int minWorker = 0;
	private int maxWorker = 0;

	private List<ActorRef<WorkerCommand>> idleWorkers = new LinkedList<>();
	private List<ActorRef<WorkerCommand>> busyWorkers = new LinkedList<>();

	private List<Task> pendingTasks = new LinkedList<>();

	/* --- Constructor ---------------------------------- */
	private Server(ActorContext<ServerCommand> context, int minWorkers, int maxWorkers) {
		super(context);
		this.minWorker = minWorkers;
		this.maxWorker = maxWorkers;

		IntStream.range(0, this.minWorker).forEach(i -> {
			ActorRef<WorkerCommand> worker = context.spawn(Worker.create(this.getContext().getSelf()), "worker-" + i);
			idleWorkers.add(worker);
		});
	}

	/* --- Actor initial state -------------------------- */
	public static Behavior<ServerCommand> create(int minWorkers, int maxWorkers) {
		return Behaviors.setup(context -> new Server(context, minWorkers, maxWorkers));
	}

	/* --- Message handling ----------------------------- */
	@Override
	public Receive<ServerCommand> createReceive() {
		return newReceiveBuilder().onMessage(ComputeTasks.class, this::onComputeTasks)
				.onMessage(WorkDone.class, this::onWorkDone).onSignal(ChildFailed.class, this::onChildFailed).build();
	}

	/* --- Handlers ------------------------------------- */
	public Behavior<ServerCommand> onComputeTasks(ComputeTasks msg) {
		msg.tasks.forEach(task -> {
			if (!idleWorkers.isEmpty()) {
				ActorRef<WorkerCommand> worker = idleWorkers.remove(0);
				busyWorkers.add(worker);
				worker.tell(new ComputeTask(task, msg.client));
			} else if (busyWorkers.size() < maxWorker) {
				ActorRef<WorkerCommand> worker = this.getContext().spawn(Worker.create(this.getContext().getSelf()),
						"worker-" + busyWorkers.size());
				busyWorkers.add(worker);
				worker.tell(new ComputeTask(task, msg.client));
			} else {
				pendingTasks.add(task);
			}
		});

		return this;
	}
	
	public Behavior<ServerCommand> onChildFailed(ChildFailed msg) {
		ActorRef<WorkerCommand> worker = this.getContext().spawn(Worker.create(this.getContext().getSelf()),
				"worker-" + busyWorkers.size());
		idleWorkers.add(worker);

		return this;
	}

	public Behavior<ServerCommand> onWorkDone(WorkDone msg) {
		if (!pendingTasks.isEmpty()) {
			ActorRef<WorkerCommand> worker = msg.worker;
			Task task = pendingTasks.remove(0);
			worker.tell(new ComputeTask(task, msg.client));
		} else if (idleWorkers.size() >= minWorker) {
			ActorRef<WorkerCommand> worker = msg.worker;
			worker.tell(new Stop());
		} else {
			busyWorkers.remove(msg.worker);
			idleWorkers.add(msg.worker);
		}
		getContext().getLog().info("Idle workers: {}", idleWorkers.size());
		return this;
	}
}
