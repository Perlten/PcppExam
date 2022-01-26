package exercises03;


public class MeasureVolatile {


    public interface Thunk {
        int apply();
    }
    

    private volatile int vctr;
    private int ctr;

    public int vInc() {
        return vctr++;
    }

    public int inc() {
        return ctr++;
    }

    public static double Mark7(String msg, Thunk f) {
        int n = 10, count = 1, totalCount = 0;
        double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
        do {
            count *= 2;
            st = sst = 0.0;
            for (int j = 0; j < n; j++) {
                Timer t = new Timer();
                for (int i = 0; i < count; i++)
                    dummy += f.apply();
                ;
                runningTime = t.check();
                double time = runningTime * 1e9 / count;
                st += time;
                sst += time * time;
                totalCount += count;
            }
        } while (runningTime < 0.25 && count < Integer.MAX_VALUE / 2);
        double mean = st / n, sdev = Math.sqrt((sst - mean * mean * n) / (n - 1));
        System.out.printf("%-25s %15.1f ns %10.2f %10d%n", msg, mean, sdev, count);
        return dummy / totalCount;
    }

    public static void main(String[] args) {
        MeasureVolatile mv = new MeasureVolatile();
        Mark7("Volatile test", () -> mv.vInc());

        Mark7("Regular test", () -> mv.inc());
    }

}
