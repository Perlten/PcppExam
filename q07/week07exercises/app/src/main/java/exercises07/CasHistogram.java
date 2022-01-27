package exercises07;

import java.util.concurrent.atomic.AtomicInteger;

public class CasHistogram implements Histogram {

    AtomicInteger[] counts;

    public CasHistogram(int span) {
        this.counts = new AtomicInteger[span];
        for (int i = 0; i < span; i++) {
            counts[i] = new AtomicInteger(0);
        }
    }

    @Override
    public void increment(int bin) {
        while (true) {
            int before = counts[bin].get();
            boolean weMadeIt = counts[bin].compareAndSet(before, before + 1);
            if (weMadeIt)
                break;
        }
    }

    @Override
    public int getCount(int bin) {
        return counts[bin].get();
    }

    @Override
    public int getSpan() {
        return counts.length;
    }

    @Override
    public int getAndClear(int bin) {
        int before = 0;
        while (true) {
            before = counts[bin].get();
            boolean weMadeIt = counts[bin].compareAndSet(before, 0);
            if (weMadeIt)
                break;
        }

        return before;
    }
}
