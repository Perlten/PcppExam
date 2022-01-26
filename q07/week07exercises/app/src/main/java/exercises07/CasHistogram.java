// For week 7
// raup@itu.dk * 10/10/2021
package exercises07;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class CasHistogram implements Histogram {
  public static void main(String[] args) throws InterruptedException {
    CasHistogram histogram = new CasHistogram(8);

    ExecutorService pool = Executors.newFixedThreadPool(4);

    for (int i = 0; i < 100; i++) {
      final int j = i;
      pool.execute(() -> {
        int res = j % 8;
        histogram.increment(res);
      });
    }

    pool.shutdown();

    Thread.sleep(100);
    histogram.print();
  }

  private AtomicInteger[] histogram;

  public CasHistogram(int span) {
    this.histogram = new AtomicInteger[span];
    Arrays.fill(this.histogram, new AtomicInteger(0));
  }

  @Override
  public void increment(int bin) {
    int value;
    int newValue;

    do {
      value = histogram[bin].get();
      newValue = value + 1;
    } while (!histogram[bin].compareAndSet(value, newValue));
  }

  @Override
  public int getCount(int bin) {
    return this.histogram[bin].get();
  }

  @Override
  public int getSpan() {
    return this.histogram.length;
  }

  @Override
  public int getAndClear(int bin) {
    int value;
    do {
      value = this.histogram[bin].get();
    } while (!histogram[bin].compareAndSet(value, 0));
    return value;
  }

  public void print() {
    for (int i = 0; i < histogram.length; i++) {
      System.out.println(histogram[i].get());
    }
  }
}
