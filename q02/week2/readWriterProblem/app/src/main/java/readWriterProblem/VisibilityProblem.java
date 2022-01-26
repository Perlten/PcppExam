package readWriterProblem;

public class VisibilityProblem {

    private volatile int value = 0;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static void main(String[] args) throws Exception {
        VisibilityProblem mi = new VisibilityProblem();

        Thread t = new Thread(() -> {
            while (mi.getValue() == 0) {
                // Do stuff
            }
            System.out.println("I completed, mi = " + mi.getValue());
        });
        t.start();

        Thread.sleep(500);

        mi.setValue(42);

        System.out.println("mi set to 42");

        t.join();

        System.out.println("Thread and main has executed");
    }
}
