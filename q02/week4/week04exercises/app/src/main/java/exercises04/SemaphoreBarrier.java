package exercises04;

import java.util.Currency;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SemaphoreBarrier {

    private final int parties;
    private volatile int[] shared_array;
    SemaphoreThreadSafe sts = new SemaphoreThreadSafe(10);
    ExecutorService es = Executors.newCachedThreadPool();

    public SemaphoreBarrier(int parties) {
        this.parties = parties;
        shared_array = new int[parties];
    }

    public void run() {
        for (int i = 0; i < parties; i++) {
            final int foo = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    sts.acquireLockOrSleep();
                    System.out.println(Thread.currentThread().getId() + " got myself a lock man woo");
                    shared_array[foo] = (int) Thread.currentThread().getId();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getId() + " Letting it go");
                    sts.releaseLock();
                }
            });
        }
    }

    public void await() {
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void getShared_array() {
        for (int i = 0; i < shared_array.length; i++) {
            System.out.println(shared_array[i]);
        }
    }

    public void setShared_array(int[] shared_array) {
        this.shared_array = shared_array;
    }

    public static void main(String[] args) {
        SemaphoreBarrier sb = new SemaphoreBarrier(10);
        sb.run();
        sb.await();

        //Printing after the barrier is over
        sb.getShared_array();
    }


//Tested my semaphore here
//    public static void main(String[] args) {
//        SemaphoreThreadSafe sts = new SemaphoreThreadSafe(3);
//
//        ExecutorService es = Executors.newCachedThreadPool();
//        for (int i = 0; i < 5; i++) {
//            es.execute(new Runnable() {
//                @Override
//                public void run() {
//                    sts.acquireLockOrSleep();
//                    System.out.println(Thread.currentThread().getId() + " got myself a lock man woo");
//                    try {
//                        Thread.sleep(4000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread().getId() + " Letting it go");
//                    sts.releaseLock();
//                }
//            });
//        }
//        es.shutdown();
//        try {
//            es.awaitTermination(1, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}
