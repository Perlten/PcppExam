### 4.1
Implemtend

### 4.1.2

The current implementation is "working" over a run of 10 times
However when analysing my code a few days later, it seems that certain elements of it possibly may not be thread safe, such as when i poll or read from my que
I could easily fix this by synchronizing more of my code, but then it kinda kills the point of a semaphore as only one thread would access the "critical section"(its not a critical section atm)? Which scenario would you ever need to use a semaphore? Would you ever be okay with 9 threads in a "critical section" but not 10? This is rather confusing


### 4.1.3

### 4.1.4
It is a weak fairness, meaning that the thread which has been waiting the longest gets to go next. In this scenario it does not matter, as each thread does roughly the same work(Insert a number or read a number) in our case order has no importance.. But if we were making a que to get into Tivoli, the customer who had waited the longest should probaly get in first



### 4.2


### 4.2.2
The person constructor uses an volatile static AtomicInt in order to be readable from main memory and shared between all instances and safe of race conditions.. Likewise we use a synchronize on the Person.class for the firstPerson boolean in order to set the initial value of the id's

All the fields are visible to outside reads upon creation of the object, as they are all either synchronized or volatile


### 4.2.4
No, you need to review your code and scenarios in order to determine thread safeness, you may run your code 1000 times without finding an error.. But trust me! It can be there ;)


### 4.3
Have implemented them, thought they were pretty tricky, but i feel like the versions are working, but not perfect


Thread safety

Identify state variables, state variables must not cause race condditions

Mutable class state must not escape (Dont return a reference to an object which then can be modified) - instead make methods which can be syncronized which possibly modify an object for you(such as a list etc!)

safe publication.. Aka, things must be readable/visible to other threads, you dont want an object to be published with null values for some threads for example, you want it to be read from memory!

if possible, make ur class immutable

if a class has mutables, ensure mutex(mutual exclusion!)