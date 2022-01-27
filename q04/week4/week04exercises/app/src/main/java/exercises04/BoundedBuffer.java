package exercises04;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BoundedBuffer<T> implements BoundedBufferInterface<T> {

    private LinkedList<T> buffer = new LinkedList<>();
    private Semaphore insertSemaphore = null;
    private Semaphore takeSemaphore = null;

    public BoundedBuffer(int bufferSize) throws InterruptedException {
        this.insertSemaphore = new Semaphore(bufferSize);
        this.takeSemaphore = new Semaphore(0);
    }

    @Override
    public T take() throws Exception {
        this.takeSemaphore.acquire();

        T data = this.buffer.pop();

        System.out.println("take: " + data);

        this.insertSemaphore.release();
        return data;

    }

    @Override
    public void insert(T elem) throws Exception {
        this.insertSemaphore.acquire();

        System.out.println("Insert: " + elem);
        this.buffer.add(elem);

        this.takeSemaphore.release();
    }

    public static void main(String[] args) throws Exception {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(5);

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    bb.insert(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                try {
                    bb.take();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // new Thread(() -> {
        // try {
        // for (int i = 0; i < 5; i++) {
        // bb.insert(i);
        // }

        // TimeUnit.SECONDS.sleep(10);

        // for (int i = 0; i < 5; i++) {
        // System.out.println("About to insert: " + (i + 10));
        // bb.insert(i + 10);
        // }

        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }).start();

        // new Thread(() -> {
        // try {
        // TimeUnit.SECONDS.sleep(5);
        // for (int i = 0; i < 10; i++) {
        // System.out.println("About to take");
        // bb.take();
        // }

        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // }).start();

    }

}
