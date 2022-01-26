package testingconcurrency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrentSetTest {

    ConcurrentIntegerSet set;
    private CyclicBarrier barrier;
    private final static ExecutorService pool = Executors.newCachedThreadPool();

    @BeforeEach
    public void initialize() {
        // init set
        // set = new ConcurrentIntegerSetBuggy();
        // set = new ConcurrentIntegerSetSync();
        set = new ConcurrentIntegerSetLibrary();
    }

    @ParameterizedTest
    // @Disabled
    @DisplayName("Add test")
    @MethodSource("argsGeneration")
    public void testingAddParallel(int nrThreads, int N) {
        System.out.printf("Parallel counter tests with %d threads and %d iterations",
                nrThreads, N);

        // init barrier
        barrier = new CyclicBarrier(nrThreads + 1);

        for (int i = 0; i < nrThreads; i++) {
            pool.execute(new Counter(N));
        }

        try {
            barrier.await();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        assertEquals(N, set.size(), "size == " + set.size() + ", but we expected " + set.size());
    }

    @ParameterizedTest
    // @Disabled
    @DisplayName("Remove test")
    @MethodSource("argsGeneration")
    public void testingRemoveParallel(int nrThreads, int N) {
        System.out.printf("Parallel counter tests with %d threads and %d iterations",
                nrThreads, N);

        // init barrier
        barrier = new CyclicBarrier(nrThreads + 1);

        for (int i = 0; i < N; i++) {
            set.add(i);
        }

        for (int i = 0; i < nrThreads; i++) {
            pool.execute(new Remover(N));
        }

        try {
            barrier.await();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        assertEquals(0, set.size(), "size == " + set.size() + ", but we expected " + 0);
    }

    public class Counter extends Thread {

        int n;

        public Counter(int n) {
            this.n = n;
        }

        public void run() {
            try {
                barrier.await();
                IntStream
                        .range(0, n)
                        .forEach(x -> set.add(x));
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

    public class Remover extends Thread {

        int n;

        public Remover(int n) {
            this.n = n;
        }

        public void run() {
            try {
                barrier.await();
                IntStream
                        .range(0, n)
                        .forEach(x -> {
                            // Removing the same element x3 wont do anything bad in this case
                            // But each thread trying to remove the same element will
                            set.remove(x);
                            set.remove(x);
                            set.remove(x);
                        });
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

    private static List<Arguments> argsGeneration() {

        // Max number of increments
        final int I = 50_000;
        final int iInit = 10_000;
        final int iIncrement = 10_000;

        // Max exponent number of threads (2^J)
        final int J = 6;
        final int jInit = 1;
        final int jIncrement = 1;

        // List to add each parameters entry
        List<Arguments> list = new ArrayList<Arguments>();

        // Loop to generate each parameter entry
        // (2^j, i) for j \in {100,200,...,J} and j \in {1,..,I}
        for (int i = iInit; i <= I; i += iIncrement) {
            for (int j = jInit; j < J; j += jIncrement) {
                list.add(Arguments.of((int) Math.pow(2, j), i));
            }
        }

        // Return the list
        return list;
    }
}
