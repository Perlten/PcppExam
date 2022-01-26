package readWriterProblem;

import java.util.concurrent.locks.ReentrantLock;

public class TestLocking0 {
    public static void main(String[] args) throws Exception {
        final int count = 1_000_000;

        Mystery m = new Mystery();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < count; i++)
                m.addInstance(1);
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < count; i++)
                m.addStatic(1);
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.printf("Sum is %f and should be %f%n", m.sum(), 2.0 * count);
    }
}

class Mystery {
    private static double sum = 0;
    private static ReentrantLock lock = new ReentrantLock();

    public static synchronized void addStatic(double x) {
        try {
            Mystery.lock.lock();
            sum += x;
        } finally {
            lock.unlock();
        }
    }

    public synchronized void addInstance(double x) {
        try {
            Mystery.lock.lock();
            sum += x;
        } finally {
            lock.unlock();
        }
    }

    public static synchronized double sum() {
        return sum;
    }
}
