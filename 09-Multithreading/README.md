# Chapter 9: Multithreading and Concurrency

## 📚 Table of Contents
1. [Introduction to Multithreading](#introduction-to-multithreading)
2. [Creating Threads](#creating-threads)
3. [Thread Synchronization](#thread-synchronization)
4. [Executor Framework](#executor-framework)
5. [Concurrent Collections](#concurrent-collections)
6. [Common Concurrency Problems](#common-concurrency-problems)
7. [Best Practices](#best-practices)

---

## Introduction to Multithreading

### **What is Multithreading?**
Multithreading allows multiple threads of execution to run concurrently within a single program.

### **Benefits:**
- **Improved Performance** - Utilize multiple CPU cores
- **Responsiveness** - UI remains responsive during long operations
- **Resource Sharing** - Threads share memory space
- **Parallel Processing** - Execute multiple tasks simultaneously

### **Thread Lifecycle:**
```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

---

## Creating Threads

### **Method 1: Extending Thread Class:**
```java
class MyThread extends Thread {
    private String name;
    
    public MyThread(String name) {
        this.name = name;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + " - Count: " + i);
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

// Usage
MyThread thread1 = new MyThread("Thread-1");
MyThread thread2 = new MyThread("Thread-2");

thread1.start(); // Don't call run() directly!
thread2.start();
```

### **Method 2: Implementing Runnable Interface:**
```java
class MyTask implements Runnable {
    private String name;
    
    public MyTask(String name) {
        this.name = name;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(name + " - Count: " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

// Usage
Thread thread1 = new Thread(new MyTask("Task-1"));
Thread thread2 = new Thread(new MyTask("Task-2"));

thread1.start();
thread2.start();
```

### **Method 3: Lambda Expressions (Java 8+):**
```java
// Simple task
Thread thread = new Thread(() -> {
    System.out.println("Running in: " + Thread.currentThread().getName());
});

// With parameters
Runnable task = () -> {
    for (int i = 0; i < 10; i++) {
        System.out.println("Count: " + i);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
        }
    }
};

new Thread(task, "Lambda-Thread").start();
```

---

## Thread Synchronization

### **The Problem - Race Conditions:**
```java
class Counter {
    private int count = 0;
    
    public void increment() {
        count++; // Not atomic! Three operations: read, increment, write
    }
    
    public int getCount() {
        return count;
    }
}

// Race condition example
Counter counter = new Counter();
Runnable task = () -> {
    for (int i = 0; i < 1000; i++) {
        counter.increment(); // Multiple threads accessing simultaneously
    }
};

Thread t1 = new Thread(task);
Thread t2 = new Thread(task);
t1.start();
t2.start();
// Final count might not be 2000 due to race condition!
```

### **Solution 1: Synchronized Methods:**
```java
class SynchronizedCounter {
    private int count = 0;
    
    public synchronized void increment() {
        count++; // Only one thread can execute this at a time
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

### **Solution 2: Synchronized Blocks:**
```java
class Counter {
    private int count = 0;
    private final Object lock = new Object();
    
    public void increment() {
        synchronized (lock) {
            count++;
        }
    }
    
    public void decrement() {
        synchronized (lock) {
            count--;
        }
    }
}
```

### **Solution 3: Atomic Classes:**
```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    private AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet(); // Thread-safe atomic operation
    }
    
    public int getCount() {
        return count.get();
    }
}
```

### **Wait/Notify Pattern:**
```java
class ProducerConsumer {
    private final Object lock = new Object();
    private boolean dataReady = false;
    private String data;
    
    public void produce(String newData) {
        synchronized (lock) {
            data = newData;
            dataReady = true;
            lock.notify(); // Wake up waiting consumer
        }
    }
    
    public String consume() {
        synchronized (lock) {
            while (!dataReady) {
                try {
                    lock.wait(); // Wait for data to be ready
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            dataReady = false;
            return data;
        }
    }
}
```

---

## Executor Framework

### **Thread Pool Benefits:**
- **Resource Management** - Limit number of threads
- **Performance** - Reuse threads instead of creating new ones
- **Control** - Better control over thread execution

### **ExecutorService:**
```java
import java.util.concurrent.*;

// Fixed thread pool
ExecutorService executor = Executors.newFixedThreadPool(4);

// Submit tasks
for (int i = 0; i < 10; i++) {
    final int taskId = i;
    executor.submit(() -> {
        System.out.println("Task " + taskId + " executed by " + 
                          Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    });
}

// Shutdown executor
executor.shutdown();
try {
    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
        executor.shutdownNow();
    }
} catch (InterruptedException e) {
    executor.shutdownNow();
}
```

### **Different Executor Types:**
```java
// Fixed thread pool - fixed number of threads
ExecutorService fixed = Executors.newFixedThreadPool(4);

// Cached thread pool - creates threads as needed
ExecutorService cached = Executors.newCachedThreadPool();

// Single thread executor - one thread for all tasks
ExecutorService single = Executors.newSingleThreadExecutor();

// Scheduled executor - for delayed/periodic tasks
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

// Schedule one-time task
scheduled.schedule(() -> System.out.println("Delayed task"), 5, TimeUnit.SECONDS);

// Schedule recurring task
scheduled.scheduleAtFixedRate(() -> System.out.println("Periodic task"), 
                              0, 2, TimeUnit.SECONDS);
```

### **Future and Callable:**
```java
import java.util.concurrent.*;

// Callable returns a result (unlike Runnable)
Callable<Integer> calculation = () -> {
    Thread.sleep(2000);
    return 42;
};

ExecutorService executor = Executors.newSingleThreadExecutor();

// Submit callable and get Future
Future<Integer> future = executor.submit(calculation);

System.out.println("Task submitted...");

try {
    // Get result (blocks until complete)
    Integer result = future.get(3, TimeUnit.SECONDS);
    System.out.println("Result: " + result);
} catch (TimeoutException e) {
    System.out.println("Task timed out");
    future.cancel(true);
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause());
}

executor.shutdown();
```

### **CompletableFuture (Java 8+):**
```java
// Asynchronous computation
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    return "Hello World";
});

// Chain operations
CompletableFuture<String> result = future
    .thenApply(s -> s.toUpperCase())
    .thenApply(s -> s + "!");

// Non-blocking get with timeout
try {
    String finalResult = result.get(3, TimeUnit.SECONDS);
    System.out.println(finalResult); // "HELLO WORLD!"
} catch (Exception e) {
    e.printStackTrace();
}
```

---

## Concurrent Collections

### **Thread-Safe Collections:**
```java
import java.util.concurrent.*;

// Concurrent HashMap - thread-safe Map
ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();
concurrentMap.put("key1", 100);
concurrentMap.putIfAbsent("key2", 200);

// Blocking Queue - thread-safe Queue with blocking operations
BlockingQueue<String> queue = new LinkedBlockingQueue<>();

// Producer
new Thread(() -> {
    try {
        queue.put("item1");
        queue.put("item2");
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}).start();

// Consumer
new Thread(() -> {
    try {
        String item = queue.take(); // Blocks if queue is empty
        System.out.println("Consumed: " + item);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}).start();

// Copy on Write ArrayList - optimized for read-heavy scenarios
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
cowList.add("item1");
cowList.add("item2");
```

---

## Common Concurrency Problems

### **1. Deadlock:**
```java
// Deadlock example (avoid this!)
class DeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {
            System.out.println("Method1: acquired lock1");
            synchronized (lock2) {
                System.out.println("Method1: acquired lock2");
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {
            System.out.println("Method2: acquired lock2");
            synchronized (lock1) { // Different order - potential deadlock!
                System.out.println("Method2: acquired lock1");
            }
        }
    }
}

// Prevention: Always acquire locks in the same order
```

### **2. Livelock:**
```java
// Threads keep responding to each other but make no progress
// Solution: Add randomization or different strategies
```

### **3. Starvation:**
```java
// Some threads never get CPU time
// Solution: Use fair locks or different scheduling
ReentrantLock fairLock = new ReentrantLock(true); // Fair lock
```

---

## Best Practices

### **1. Use Higher-Level Concurrency Utilities:**
```java
// Prefer ExecutorService over raw threads
ExecutorService executor = Executors.newFixedThreadPool(4);

// Prefer concurrent collections over synchronized collections
ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

// Prefer atomic classes over synchronized blocks for simple operations
AtomicInteger counter = new AtomicInteger();
```

### **2. Minimize Shared Mutable State:**
```java
// Prefer immutable objects
public final class ImmutablePoint {
    private final int x, y;
    
    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
}

// Use thread-local variables when possible
ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(
    () -> new SimpleDateFormat("yyyy-MM-dd")
);
```

### **3. Handle Interruption Properly:**
```java
public void interruptibleTask() {
    while (!Thread.currentThread().isInterrupted()) {
        try {
            // Do work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
            break;
        }
    }
}
```

### **4. Use Try-with-Resources for Locks:**
```java
class AutoCloseableLock implements AutoCloseable {
    private final ReentrantLock lock = new ReentrantLock();
    
    public AutoCloseableLock acquire() {
        lock.lock();
        return this;
    }
    
    @Override
    public void close() {
        lock.unlock();
    }
}

// Usage
try (AutoCloseableLock ignored = lockObj.acquire()) {
    // Critical section
} // Lock automatically released
```

---

## Key Takeaways

### **Essential Concepts:**
✅ **Thread Creation** - Extending Thread vs implementing Runnable  
✅ **Synchronization** - Preventing race conditions  
✅ **Executor Framework** - Managing thread pools  
✅ **Concurrent Collections** - Thread-safe data structures  
✅ **Common Problems** - Deadlock, livelock, starvation  
✅ **Best Practices** - Using higher-level utilities  

### **Synchronization Mechanisms:**

| Mechanism | Use Case | Performance |
|-----------|----------|-------------|
| **synchronized** | Simple mutual exclusion | Good |
| **ReentrantLock** | Advanced features needed | Good |
| **Atomic classes** | Simple numeric operations | Excellent |
| **volatile** | Simple flag variables | Excellent |
| **ConcurrentHashMap** | Thread-safe map operations | Excellent |

### **Thread Safety Guidelines:**
- **Immutable objects** are inherently thread-safe
- **Stateless objects** are thread-safe
- **Use concurrent collections** instead of synchronizing regular collections
- **Minimize scope** of synchronized blocks
- **Avoid nested locks** to prevent deadlock

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Learn about reactive programming and async patterns
- Move to [Advanced Topics](../10-Advanced-Topics/README.md)

---

**Continue to: [Chapter 10: Advanced Topics →](../10-Advanced-Topics/README.md)**