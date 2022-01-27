package exercises04;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBarrier {

    private int parties = 0;
    private int partiesWaiting = 0;
    private CustomSemaphore semaphore = null;
    private Lock lock = new ReentrantLock();

    public CustomBarrier(int parties) {
        this.parties = parties;
        this.semaphore = new CustomSemaphore(parties, parties + 1);
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            partiesWaiting++;
            if (partiesWaiting >= parties) {
                semaphore.releaseAll();
            }
        } finally {
            lock.unlock();
        }

        semaphore.acquire();
    }

    public static void main(String[] args) throws InterruptedException {
        CustomBarrier barrier = new CustomBarrier(3);
        startThread(1, barrier);
        startThread(2, barrier);
        startThread(3, barrier);
    }

    public static void startThread(int seconds, CustomBarrier barrier) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
                System.out.println(seconds + " Start");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(seconds + " Done");
        }).start();

    }

}
