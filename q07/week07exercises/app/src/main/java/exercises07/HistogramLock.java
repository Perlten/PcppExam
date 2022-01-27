package exercises07;

public class HistogramLock implements Histogram {
    private final int[] counts;
    private volatile int total = 0;

    public HistogramLock(int span) {
        this.counts = new int[span];
    }

    public synchronized void increment(int bin) {
        counts[bin] = counts[bin] + 1;
        total++;
    }

    public synchronized int getCount(int bin) {
        return counts[bin];
    }

    public synchronized float getPercentage(int bin) {
        return getCount(bin) / getTotal() * 100;
    }

    public synchronized int getSpan() {
        return counts.length;
    }

    @Override
    public synchronized int getAndClear(int bin) {
        int before = counts[bin];
        counts[bin] = 0;
        return before;

    }

    public synchronized int getTotal() {
        return total;
    }
}
