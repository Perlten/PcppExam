package exercises04;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class CustomSemaphore {

    private int capacity = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private int count = 0;

    public CustomSemaphore(int capacity, int startCount) {
        this.count = startCount;
        this.capacity = capacity;
    }

    public CustomSemaphore(int capacity) {
        this(capacity, 0);
    }

    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (this.count > capacity) {
                condition.await();
            }
            this.count++;
        } finally {
            lock.unlock();
        }
        
    }

    public void release() {
        this.lock.lock();
        try {
            this.count--;
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public void releaseAll(){
        this.lock.lock();
        try {
            this.count = 0;
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CustomSemaphore semaphore = new CustomSemaphore(5);

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    System.out.println("Sema acquired: " + i );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    semaphore.release();
                    System.out.println("Sema released: " + i );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
