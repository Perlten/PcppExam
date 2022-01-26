package exercises04;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PersonTest {
    public static List<Person> list = new ArrayList<>();

    private static Lock l = new ReentrantLock();



    public static void main(String[] args) throws InterruptedException {

        Random ran = new Random();
        int x = ran.nextInt(100) + 5;

        ExecutorService es = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {

            es.execute(new Runnable() {
                @Override
                public void run() {
                    int counter = 0;
                    while (counter != 3) {
                        Person p = new Person(x);
                        l.lock();
                        list.add(p);
                        l.unlock();
                        counter++;
                    }
                    System.out.println(Thread.currentThread().getId() + " is done");
                }
            });
        }

        es.shutdown();
        es.awaitTermination(1, TimeUnit.MINUTES);

        for(Person p: list){
            System.out.println(p.getId());
        }
        System.out.println("size" + list.size());
    }
}


