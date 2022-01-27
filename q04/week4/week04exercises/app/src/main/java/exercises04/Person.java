package exercises04;

import java.util.concurrent.atomic.AtomicLong;

public class Person {

    private final long id;
    private volatile String name;
    private volatile int zip;
    private volatile String address;

    private static AtomicLong globalId = new AtomicLong(0);

    public Person() {
        this.id = Person.globalId.getAndIncrement();
    }

    public Person(String name, int zip, String address) {
        this();
        this.name = name;
        this.zip = zip;
        this.address = address;
    }

    public synchronized void changeLocation(String address, int zip) {
        this.address = address;
        this.zip = zip;
    }

    public synchronized String getAddress() {
        return address;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized int getZip() {
        return zip;
    }

    public static void main(String[] args) {
        new Thread(Person::test).start();
        new Thread(Person::test).start();
        new Thread(Person::test).start();
        new Thread(Person::test).start();
    }

    public static void test() {
        for (int i = 0; i < 1000; i++) {
            var p = new Person();
            System.out.println(p.id);
        }
    }

}
