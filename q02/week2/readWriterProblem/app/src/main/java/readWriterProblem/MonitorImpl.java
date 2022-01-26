package readWriterProblem;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorImpl {

    private int readLockAcquired = 0;
    private int readLockReleased = 0;

    private boolean write = false;

    private ReentrantLock lock = new ReentrantLock(true);
    private Condition condition = lock.newCondition();

    public void lockRead() {
        lock.lock();
        try {

            while (write) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                }
            }
            readLockAcquired++;
        } finally {
            lock.unlock();
        }
    }

    public void releaseLockRead() {
        lock.lock();
        try {

            readLockReleased++;
            if (readLockAcquired == readLockReleased) {
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void lockWrite() {
        lock.lock();
        try {
            while (write) {
                condition.await();
            }

            write = true;

            while (readLockAcquired != readLockReleased) {
                condition.await();
            }
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    public void releaseLockWrite() {
        lock.lock();
        try {

            write = false;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}