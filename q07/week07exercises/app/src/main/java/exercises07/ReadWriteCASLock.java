// For week 7
// raup@itu.dk * 10/10/2021
package exercises07;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import org.checkerframework.checker.units.qual.A;

class ReadWriteCASLock implements SimpleRWTryLockInterface {

    ReaderList readerList = new ReaderList();
    AtomicReference<Holders> holder = new AtomicReference<>(readerList);

    public static void main(String[] args) throws Exception {

        ReadWriteCASLock lockingSystem = new ReadWriteCASLock();

        Executor executor = Executors.newFixedThreadPool(2);

        executor.execute(() -> {
            try {
                System.out.println(lockingSystem.readerTryLock() + " " +
                        Thread.currentThread().getId());
                lockingSystem.readerUnlock();
                System.out.println(lockingSystem.readerTryLock() + " " +
                        Thread.currentThread().getId());
                Thread.sleep(2000);
                lockingSystem.readerUnlock();
                Thread.sleep(8000);
                System.out.println(lockingSystem.readerTryLock() + " " +
                        Thread.currentThread().getId());
                System.out.println(lockingSystem.readerTryLock() + " " +
                        Thread.currentThread().getId());
                lockingSystem.readerUnlock();
                lockingSystem.readerUnlock();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        executor.execute(() -> {
            try {
                Thread.sleep(2000);
                System.out.println(lockingSystem.writerTryLock() + " writer " +
                        Thread.currentThread().getId());
                Thread.sleep(1000);
                System.out.println(lockingSystem.writerTryLock() + " writer " +
                        Thread.currentThread().getId());
                lockingSystem.writerUnlock();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        // System.out.println(lockingSystem.readerTryLock() + " " +
        // Thread.currentThread().getId());
        // System.out.println(lockingSystem.readerTryLock() + " " +
        // Thread.currentThread().getId());
        // System.out.println(lockingSystem.readerTryLock() + " " +
        // Thread.currentThread().getId()); lockingSystem.readerUnlock();
        // lockingSystem.readerUnlock();
        // lockingSystem.readerUnlock();

    }
    public boolean readerTryLock() {

        final Thread current = Thread.currentThread();

        List<Thread> readers = readerList.readers;
        readers.add(current);

        Holders h = holder.get();

        while (true) {
            if (h instanceof ReaderList) {
                boolean success = holder.compareAndSet(h, readerList);
                if (success) {
                    return true;
                }
            } else {
                readerList.readers = new LinkedList<>();
                return false;
            }
        }
    }

    // Challenging 7.2.7: You may add new methods
    public void readerUnlock() throws Exception {

        final Thread current = Thread.currentThread();

        if (holder == null) {
            throw new Exception("Not even sure how you got here, holder is null!");
        }
        if (!readerList.contains(current)) {
            throw new Exception(
                    "You cant unluck, youre not even in the pool my man!");
        } else {
            readerList.remove(current);
        }
        if (readerList.readers.isEmpty()) {
            while (true) {
                Holders h = holder.get();
                boolean success = holder.compareAndSet(h, readerList);
                if (success)
                    break;
            }
        }

        System.out.println("unlocked by thread " + current.getId());
    }

    public boolean writerTryLock() {
        final Thread current = Thread.currentThread();

        Writer w = new Writer(current.getId());

        Holders h = holder.get();

        if (h instanceof ReaderList) {
            if (((ReaderList) h).readers.isEmpty()) {
                return holder.compareAndSet(h, w);
            }
        }

        return false;
    }

    public void writerUnlock() throws Exception {

        final Thread current = Thread.currentThread();

        long id = current.getId();

        Holders h = holder.get();

        if (h == null) {
            throw new Exception(
                    "damn, u cant call this, when holders is null, then u do not own the lock mister");
        } else if (!(h instanceof Writer)) {
            throw new Exception(
                    "Youre not even a Writer, how are you here mr reader!");
        } else if (h instanceof Writer) {
            Writer x = (Writer) h;
            if (x.id != id) {
                throw new Exception("Thread isnt owner, wrong writer!!");
            } else {
                holder.compareAndSet(h, readerList);
            }
        }
    }

    private static abstract class Holders {
    }

    private static class ReaderList extends Holders {

        public List<Thread> readers = new LinkedList<>();

        public ReaderList() {
        }

        public void add(Thread t) {
            synchronized (ReaderList.class) {
                readers.add(t);
            }
        }

        public boolean contains(Thread t) {
            return readers.contains(t);
        }

        public void remove(Thread t) {
            synchronized (ReaderList.class) {
                readers.remove(t);
            }
        }
    }

    private static class Writer extends Holders {

        long id;

        private Writer(long id) {
            this.id = id;
        }
    }
}
