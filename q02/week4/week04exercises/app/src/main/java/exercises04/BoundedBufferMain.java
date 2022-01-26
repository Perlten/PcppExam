package exercises04;

import java.util.Random;

public class BoundedBufferMain {

    public static void main(String[] args) {

        BoundedBuffer<Integer> que = new BoundedBuffer<Integer>(10);

        //Reader threads, reads 5 times each then dies
        for (int i = 0; i < 3; i++) {
            Thread t = new Thread(() -> {
                int counter = 0;
                while (counter != 5) {
                    try {
                        System.out.println(Thread.currentThread().getId() + " <- id is now taking element " + que.take());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    counter++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getId() + " is done");
            });
            t.start();
        }

        //Writer threads
        for (int i = 0; i < 200; i++) {
            Thread t = new Thread(() -> {
                int counter = 0;
                while (counter != 5) {
                    try {
                        Random ran = new Random();
                        int x = ran.nextInt(100) + 5;
                        que.insert(x);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter++;
                }
                System.out.println(Thread.currentThread().getId() + " is done");
            });
            t.start();
        }


    }

}
