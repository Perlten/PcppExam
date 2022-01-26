package exercises07;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleRWTryLock implements SimpleRWTryLockInterface {

    public static void main(String[] args) {

    }

    // 0: Empty 1: taken by reader 2: taken by writer 
    private AtomicInteger readerHolder = new AtomicInteger(0);
    private AtomicReference<Thread> writerHolder = new AtomicReference<Thread>(null);

    @Override
    public boolean readerTryLock() {
        if (readerHolder.compareAndSet(1, 1)) {
            return true;
        } else if (!readerHolder.compareAndSet(2, 2)) {
            return readerHolder.compareAndSet(0, 1);
        }
        return false;
    }

    @Override
    public void readerUnlock() {
        if (readerHolder.compareAndSet(1, 0)) {
            return;
        }
        throw new RuntimeException("HEY STUPID YOU DONT HOLD THE LOCK");

    }

    @Override
    public boolean writerTryLock() {
        Thread current = Thread.currentThread();
        if (this.readerHolder.compareAndSet(0, 2)) {
            return this.writerHolder.compareAndSet(null, current);
        }
        return false;
    }

    @Override
    public void writerUnlock() {
        Thread current = Thread.currentThread();
        if (!this.readerHolder.compareAndSet(2, 0) && !this.writerHolder.compareAndSet(current, null)) {
            throw new RuntimeException("HEY STUPID YOU DONT HOLD THE LOCK");
        }

    }

}