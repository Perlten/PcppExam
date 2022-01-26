package exercises07;

class SyncHistogram implements Histogram {

    private final int[] counts;

    public SyncHistogram(int span) {
        this.counts = new int[span];
    }

    @Override
    public synchronized void increment(int bin) {
        counts[bin] = counts[bin] + 1;
    }

    @Override
    public int getCount(int bin) {
        return counts[bin];
    }

    @Override
    public int getSpan() {
        return counts.length;
    }

    @Override
    public synchronized int getAndClear(int bin) {
        int value = this.counts[bin];
        this.counts[bin] = 0;
        return value;
    }

    public void print() {
    }
}
