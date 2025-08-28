# Multithreading - Quick Notes

## 🎯 **Key Concepts**

### **Thread Basics**
- **Thread**: Independent execution path within a process
- **Concurrency**: Multiple threads executing seemingly simultaneously  
- **Parallelism**: True simultaneous execution on multiple cores
- **Race Condition**: Multiple threads accessing shared data unsafely
- **Deadlock**: Threads waiting for each other indefinitely

### **Thread States**
```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

---

## 🛠️ **Creating Threads**

### **Extending Thread Class**
```java
class MyThread extends Thread {
    @Override
    public void run() {
        // Thread execution code
    }
}

MyThread thread = new MyThread();
thread.start(); // Don't call run() directly!
```

### **Implementing Runnable Interface**
```java
class MyTask implements Runnable {
    @Override
    public void run() {
        // Task code
    }
}

Thread thread = new Thread(new MyTask());
thread.start();
```

### **Lambda Expression (Java 8+)**
```java
Thread thread = new Thread(() -> {
    // Task code here
});
thread.start();
```

---

## 🔒 **Synchronization**

### **Synchronized Methods**
```java
public synchronized void method() {
    // Only one thread can execute this at a time
}
```

### **Synchronized Blocks**
```java
synchronized(this) {
    // Critical section
    sharedVariable++;
}
```

### **Atomic Variables**
```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet(); // Thread-safe increment
counter.compareAndSet(5, 10); // Compare and swap
```

---

## 🎛️ **Thread Control**

### **Basic Control Methods**
```java
thread.join();          // Wait for thread to finish
thread.interrupt();     // Request thread interruption
Thread.sleep(1000);     // Sleep for 1 second
Thread.yield();         // Hint to scheduler to yield
```

### **Thread States Check**
```java
thread.isAlive();       // Is thread running?
thread.isInterrupted(); // Has thread been interrupted?
Thread.currentThread(); // Get current thread
```

---

## 🏭 **Thread Pools**

### **ExecutorService**
```java
ExecutorService executor = Executors.newFixedThreadPool(5);

// Submit tasks
executor.submit(() -> {
    // Task code
});

// Shutdown properly
executor.shutdown();
executor.awaitTermination(10, TimeUnit.SECONDS);
```

### **Common Thread Pools**
```java
// Fixed number of threads
Executors.newFixedThreadPool(10);

// Single thread executor
Executors.newSingleThreadExecutor();

// Cached thread pool (creates threads as needed)
Executors.newCachedThreadPool();

// Scheduled tasks
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
```

---

## 🔄 **Producer-Consumer Pattern**

### **Using BlockingQueue**
```java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

// Producer
queue.put("item"); // Blocks if queue is full

// Consumer  
String item = queue.take(); // Blocks if queue is empty
```

### **Wait/Notify Pattern**
```java
synchronized(object) {
    while (condition) {
        object.wait(); // Release lock and wait
    }
    // Process
    object.notifyAll(); // Wake up waiting threads
}
```

---

## 📦 **Concurrent Collections**

### **Thread-Safe Collections**
```java
// Concurrent HashMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Copy-on-write ArrayList (good for read-heavy scenarios)
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

// Concurrent LinkedQueue
ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
```

### **Synchronization Utilities**
```java
// CountDownLatch - wait for events
CountDownLatch latch = new CountDownLatch(3);
latch.countDown(); // Decrement counter
latch.await();     // Wait until counter reaches 0

// Semaphore - control access to resource
Semaphore semaphore = new Semaphore(5); // 5 permits
semaphore.acquire(); // Get permit
semaphore.release(); // Release permit
```

---

## 🚀 **CompletableFuture**

### **Async Programming**
```java
// Async task
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Hello";
});

// Chain operations
CompletableFuture<String> result = future
    .thenApply(s -> s + " World")
    .thenApply(String::toUpperCase);

String value = result.get(); // "HELLO WORLD"
```

### **Combining Futures**
```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

CompletableFuture<String> combined = future1.thenCombine(future2, 
    (s1, s2) -> s1 + " " + s2);
```

---

## ⚡ **Quick Reference**

| Concept | Implementation | Use Case |
|---------|---------------|----------|
| **Basic Thread** | `Thread`, `Runnable` | Simple async tasks |
| **Thread Pool** | `ExecutorService` | Managing multiple tasks |
| **Synchronization** | `synchronized`, `AtomicXxx` | Shared resource access |
| **Producer-Consumer** | `BlockingQueue` | Task queuing |
| **Future** | `CompletableFuture` | Async computation chains |

---

## 💡 **Best Practices**

### **Do's**
✅ **Use thread pools** instead of creating threads manually  
✅ **Prefer concurrent collections** over synchronized collections  
✅ **Use atomic variables** for simple counters  
✅ **Handle InterruptedException** properly  
✅ **Use try-with-resources** with locks  

### **Don'ts**  
❌ **Don't call run() directly** - use start()  
❌ **Don't ignore InterruptedException**  
❌ **Avoid unnecessary synchronization** (performance cost)  
❌ **Don't hold locks for long periods**  
❌ **Avoid nested synchronized blocks** (deadlock risk)  

---

## 🔧 **Common Patterns**

### **Thread-Safe Singleton**
```java
public class Singleton {
    private static volatile Singleton instance;
    
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### **Producer-Consumer with Custom Buffer**
```java
class Buffer<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;
    
    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.offer(item);
        notifyAll();
    }
    
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.poll();
        notifyAll();
        return item;
    }
}
```

---

**Next:** [Advanced Topics](../10-Advanced-Topics/README.md)