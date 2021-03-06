/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package readWriterProblem;

public class App {

    public static void main(String[] args) {

        MonitorImpl monitor = new MonitorImpl();

        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(() -> {
                monitor.lockRead();
                System.out.println(" Reader " + Thread.currentThread().getId() + " started reading");
                // reading
                System.out.println(" Reader " + Thread.currentThread().getId() + " stopped reading");
                monitor.releaseLockRead();
            });
            t1.start();

            if (i % 2 == 0) {
                Thread t2 = new Thread(() -> {
                    monitor.lockWrite();
                    System.out.println(" Writer " + Thread.currentThread().getId() + " started writing");
                    // writing
                    System.out.println(" Writer " + Thread.currentThread().getId() + " stopped writing");
                    monitor.releaseLockWrite();
                });
                t2.start();
            }
        }
    }
}
