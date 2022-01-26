# 2.1 

## 3 
There is no guarantee that a thread will ever be picked from the entry queue. Therefore there is no fairness when using the synchronized keyword. When we however use ReentrantLock and Condition we can set the fairness flag and thereby ensure fairness. 

## 4 
I have found no examples of intrinsic or ReentrantLock being able to be strong fairness 

# 2.2

## 1 
the loops never ends because the value is cached inside the main threads register, therefore when the value is changed it is not visible to other threads


## 2
When using intrinsic locks we now ensure a happens before relation ship and the jvm no longer cash the value in a local register but instead in shared memory 

## 3
If the getter is not sync the jit compiler might do some weird reordering making the program act in weird ways

```
while (myBean.getValue() > 1.0) {
  // perform some action
  Thread.sleep(1);
}
JIT compiles:

if (myBean.getValue() > 1.0) 
  while (true) {
    // perform some action
    Thread.sleep(1);
  }
```
  
## 4
Now the program will always terminate. This is because the value is forced to be stored in main memory thereby avoiding being chased in a local register

# 2.3


# 1,2
There are a race condition. This is because one method is static and the other is not. This means that we are actually working with two different locks. The static locks uses the class 
synchronized(Mystery.class) and the other uses the instance of the object synchronized(this). 


# 3
I created a new static ReentrantLock that they both can share. Because they now use a single lock, the race condition is no longer present.
