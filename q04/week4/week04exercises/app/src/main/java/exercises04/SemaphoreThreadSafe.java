package exercises04;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreThreadSafe {

    private final int semaphoreSize;
    private volatile int currentSize = 0;

    public SemaphoreThreadSafe(int semaphoreSize) {
        this.semaphoreSize = semaphoreSize;
    }

    public synchronized void acquireLockOrSleep() {
        if (currentSize >= semaphoreSize) {
            try {
                System.out.println(Thread.currentThread().getId() + " we full yo, gotta wait");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            increAmountOfThreads();
        }
    }

    public synchronized void releaseLock() {
        this.notify();
        decreaseAmountOfThreads();
    }

    private synchronized void increAmountOfThreads() {
        currentSize++;
    }

    private synchronized void decreaseAmountOfThreads() {
        currentSize++;
    }


}
