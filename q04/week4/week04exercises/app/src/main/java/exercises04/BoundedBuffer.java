package exercises04;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBuffer<T> implements BoundedBufferInteface<T> {

    private final Semaphore readerLock = new Semaphore(10);
    private final Semaphore writerLock = new Semaphore(10);
    private final Queue<T> que;
    private final int size;

    public BoundedBuffer(int size) {
        this.que = new LinkedList<>();
        this.size = size;
    }

    @Override
    public T take() throws Exception {
        readerLock.acquire();

        //If there are no elements left, we go to sleep and let the writers know its time to get to work!
        if(que.peek() == null){
            synchronized (writerLock){
                writerLock.notify();
            }
            synchronized (readerLock){
                readerLock.wait();
            }
        }

        //If we have been woken from sleep or not been put to sleep, we are ready to pick up an element
        T element = que.poll();
        System.out.println("Yo we did it ! " + element);

        readerLock.release();

        return element;
    }

    @Override
    public void insert(T elem) throws Exception {

        writerLock.acquire();

        if(que.size() >= size){
            System.out.println("Going to sleep and notifying readers " + Thread.currentThread().getId() + " Ele:" + elem);
            synchronized (readerLock){
                readerLock.notify();
            }
            synchronized (writerLock){
                System.out.println("SLEEEEEEEEP TIIIIIIIIME NIKOOPPP!!!");
                writerLock.wait();

            }
        }

        System.out.println(Thread.currentThread().getId() + " <- id is now inserting element " + elem);
        System.out.println("");
        que.offer(elem);
        //Letting readers know we've inserted a new element to be read
        synchronized (readerLock){
            readerLock.notify();
        }
        writerLock.release();
    }


}
