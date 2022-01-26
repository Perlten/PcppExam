package exercises01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DashPrinter {

  Lock lock = new ReentrantLock();

  public void print() {
    lock.lock();
    try {
      System.out.println("-");
      try {
        Thread.sleep(50);
      } catch (Exception e) {
      }
      System.out.println("|");

    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) {
    try {
      DashPrinter p = new DashPrinter();

      Thread t1 = new Thread(() -> {
        while (true)
          p.print();
      });

      Thread t2 = new Thread(() -> {
        while (true)
          p.print();
      });

      t1.start();
      t2.start();
      t1.join();
      t2.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}