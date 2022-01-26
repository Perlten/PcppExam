package exercises04;

import java.util.concurrent.atomic.AtomicInteger;

public class Person {
    private final long id;
    private String name;
    private int zip;
    private String address;
    private volatile static AtomicInteger idCounter = new AtomicInteger(0);
    private volatile static boolean firstPerson = true;

    public Person() {
            firstPerson = false;
            this.id = idCounter.getAndIncrement();
    }

    public Person(int initialIds) {
        synchronized (Person.class) {
            if (firstPerson) {
                idCounter = new AtomicInteger(initialIds);
                firstPerson = false;
            }
            id = idCounter.getAndIncrement();
        }
    }

    public synchronized void setZipAndAddress(int zip, String address) {
        this.zip = zip;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized int getZip() {
        return zip;
    }

    public synchronized String getAddress() {
        return address;
    }

}
