# JVM Deep Dive - Complete Quick Reference Guide

## 🎯 **JVM Fundamentals (Basic Concepts)**

### **What is JVM?**
```
JVM = Java Virtual Machine
• A runtime environment that executes Java bytecode
• Provides platform independence ("Write Once, Run Anywhere")
• Manages memory automatically (Garbage Collection)
• Optimizes code at runtime (Just-In-Time compilation)
```

### **Java Execution Process**
```
1. Source Code (.java)    → Human-readable Java code
2. Javac Compiler         → Compiles to bytecode
3. Bytecode (.class)      → Platform-independent instructions
4. JVM Class Loader       → Loads classes into memory
5. JVM Execution Engine   → Executes bytecode
6. Output                 → Your program runs!

Commands:
javac HelloWorld.java     # Compile to bytecode
java HelloWorld           # Execute via JVM
```

### **JVM vs JRE vs JDK**
```
JDK (Java Development Kit)
├── JRE (Java Runtime Environment)
│   ├── JVM (Java Virtual Machine)
│   ├── Core Libraries (java.lang, java.util)
│   └── Supporting Files
└── Development Tools (javac, jar, javadoc)

• JVM: Executes Java bytecode
• JRE: JVM + libraries to run Java apps
• JDK: JRE + tools to develop Java apps
```

---

## 💾 **Memory Management Basics**

### **Stack vs Heap Memory**
```
┏━━━━━━━━━━━━━━━━━━━━━━━┓   ┏━━━━━━━━━━━━━━━━━━━━━━━┓
┃     STACK MEMORY        ┃   ┃     HEAP MEMORY         ┃
┃   (Per Thread)          ┃   ┃    (Shared)             ┃
┃                         ┃   ┃                         ┃
┃ • Method calls          ┃   ┃ • Objects               ┃
┃ • Local variables       ┃   ┃ • Instance variables    ┃
┃ • Fast access           ┃   ┃ • Arrays                ┃
┃ • LIFO (Last In First)  ┃   ┃ • Garbage collected     ┃
┃ • Limited size (~1MB)   ┃   ┃ • Larger size (GBs)     ┃
┃ • Auto cleanup          ┃   ┃ • Slower access         ┃
┗━━━━━━━━━━━━━━━━━━━━━━━┛   ┗━━━━━━━━━━━━━━━━━━━━━━━┛
```

### **Detailed JVM Memory Layout**
```
JVM Memory Areas:

1. HEAP MEMORY (Shared among threads)
   ├── Young Generation
   │   ├── Eden Space       (New objects created here)
   │   ├── Survivor S0      (Objects that survived 1 GC)
   │   └── Survivor S1      (Objects that survived multiple GC)
   └── Old Generation       (Long-lived objects)

2. STACK MEMORY (Per thread)
   └── Stack Frames         (Method calls + local variables)

3. METHOD AREA (Shared)
   ├── Class Metadata       (Class information)
   ├── Constant Pool        (String literals, constants)
   └── Static Variables     (Class-level variables)

4. PC REGISTER (Per thread)
   └── Program Counter      (Current instruction pointer)

5. NATIVE METHOD STACK (Per thread)
   └── Native method calls  (JNI calls)
```

### **Object Lifecycle**
```
1. BIRTH (Creation)
   new Object()  →  Allocated in Eden space (Young Generation)
   
2. LIFE (Usage)
   Object methods called, fields accessed
   
3. SURVIVAL (Garbage Collection)
   Object survives GC → Moved to Survivor space
   Survives multiple GCs → Promoted to Old Generation
   
4. DEATH (Cleanup)
   No references → Eligible for garbage collection
   Memory reclaimed by GC
```

### **Memory Allocation Example**
```java
public class MemoryExample {
    static int staticVar = 100;        // Method Area
    
    public static void main(String[] args) {     // main() frame → Stack
        int localVar = 42;             // Stack
        String text = "Hello";         // "Hello" → Heap, text → Stack
        
        createObjects();               // New frame → Stack
    }
    
    static void createObjects() {      // createObjects() frame → Stack
        List<String> list = new ArrayList<>();  // ArrayList → Heap
        list.add("World");             // "World" → Heap
    }  // Frame removed from Stack, but objects stay in Heap
}
```

---

## 🗑️ **Garbage Collection (GC) Fundamentals**

### **Why Garbage Collection?**
```
Problem without GC:
• Manual memory management (malloc/free in C)
• Memory leaks (forgot to free memory)
• Dangling pointers (using freed memory)
• Double-free errors

Solution with GC:
• Automatic memory management
• Safer code (no manual allocation/deallocation)
• Reduced bugs and security issues
• Developer productivity
```

### **How GC Works (3 Phases)**
```
1. MARK PHASE
   • Start from "root" references (stack, static variables)
   • Follow all object references
   • Mark all reachable objects as "alive"
   
   Example:
   Stack → ObjectA → ObjectB
   Static → ObjectC
   
   ObjectD (no references - unreachable!)

2. SWEEP PHASE  
   • Scan through heap memory
   • Delete all unmarked (unreachable) objects
   • Reclaim memory for future use
   
   Result: ObjectA, ObjectB, ObjectC kept; ObjectD deleted

3. COMPACT PHASE (Optional)
   • Move objects to reduce fragmentation
   • Group live objects together
   • Create larger free memory blocks
   
   Before: [ObjectA][FREE][ObjectB][FREE][ObjectC]
   After:  [ObjectA][ObjectB][ObjectC][  FREE SPACE  ]
```

### **Generational Garbage Collection**
```
Key Insight: Most objects die young!

┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃              YOUNG GENERATION                    ┃
┃  ┏━━━━━━━━┓  ┏━━━━━━━┓  ┏━━━━━━━┓  ┃
┃  ┃ EDEN   ┃  ┃  S0   ┃  ┃  S1   ┃  ┃
┃  ┃ SPACE  ┃  ┃      ┃  ┃      ┃  ┃
┃  ┗━━━━━━━━┛  ┗━━━━━━━┛  ┗━━━━━━━┛  ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
┃            OLD GENERATION                     ┃
┃         (Tenured Space)                    ┃
┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛

• Eden: Where new objects are created
• S0/S1: Survivor spaces for objects that survived GC
• Old: Long-lived objects promoted from Young

• Minor GC: Cleans Young Generation (fast, frequent)
• Major/Full GC: Cleans Old Generation (slower, less frequent)
```

### **GC Algorithms - Which to Choose?**

| Algorithm | When to Use | Pause Time | Throughput | Best For |
|-----------|-------------|------------|------------|-----------|
| **Serial GC** | Small apps, single CPU | High | Low | Development, testing |
| **Parallel GC** | Batch processing | Medium | High | Server apps, batch jobs |
| **G1 GC** | Large heap (>6GB) | Low | Medium | Interactive applications |
| **ZGC** | Ultra-low latency needs | Ultra-low | Medium | Real-time systems |
| **Shenandoah** | Low pause requirements | Ultra-low | Medium | Latency-sensitive apps |

**Simple Decision Guide:**
- **Small app or development?** → Serial GC
- **Batch processing or high throughput?** → Parallel GC  
- **Interactive app with large heap?** → G1 GC
- **Real-time or ultra-low latency?** → ZGC or Shenandoah

### **GC Tuning Parameters**
```bash
# Heap sizing
-Xms<size>          # Initial heap size
-Xmx<size>          # Maximum heap size
-Xmn<size>          # Young generation size

# GC selection
-XX:+UseG1GC        # G1 Garbage Collector
-XX:+UseZGC         # ZGC (Java 11+)
-XX:+UseShenandoahGC # Shenandoah GC

# GC tuning
-XX:MaxGCPauseMillis=<ms>     # Target pause time (G1)
-XX:G1HeapRegionSize=<size>   # G1 region size
-XX:NewRatio=<ratio>          # Old/Young generation ratio
```

---

## ⚡ **JIT Compilation - How Java Gets Faster**

### **What is JIT (Just-In-Time) Compilation?**
```
Java's Secret to Speed:

1. START: Bytecode runs in Interpreter (slow but works immediately)
2. MONITOR: JVM watches which methods are called frequently 
3. COMPILE: "Hot" methods get compiled to optimized machine code
4. EXECUTE: Compiled code runs much faster than interpreted
5. OPTIMIZE: JVM continues optimizing based on runtime behavior

Result: Java programs get faster the longer they run!
```

### **Compilation Levels (Tiered Compilation)**
```
Level 0: Interpreter
         • Executes bytecode directly
         • Slow but starts immediately
         • Collects profiling data

Level 1: C1 Compiler (Client)
         • Fast compilation
         • Basic optimizations
         • Good for short-running apps

Level 2: C1 with limited profiling
         • Some performance data collection
         • Preparation for higher levels

Level 3: C1 with full profiling
         • Detailed performance information
         • Identifies optimization opportunities

Level 4: C2 Compiler (Server)
         • Aggressive optimizations
         • Produces fastest code
         • Takes time to compile
```

### **JIT Optimization Techniques**
```
• Method Inlining:     Replace method calls with actual code
• Dead Code Elimination: Remove code that never executes
• Loop Optimization:   Unroll loops, move invariants outside
• Escape Analysis:     Stack-allocate objects that don't "escape"
• Constant Folding:    Compute constants at compile time
• Branch Prediction:   Optimize for common execution paths
```

### **Hotspot Detection**
```
When does JIT kick in?

• Method Invocation Threshold:  Default ~10,000 calls
• Loop Back Edge Threshold:     Default ~10,700 iterations

Example:
for (int i = 0; i < 50000; i++) {
    calculateSomething();  // This will trigger JIT compilation!
}
```


---

## 🔧 **JVM Monitoring & Troubleshooting**

### **Essential JVM Commands (Every Developer Should Know)**

#### **Memory Analysis**
```bash
# Check what's in memory (object histogram)
jmap -histo <pid>                    # Shows object counts and sizes
jmap -histo:live <pid>               # Only live objects (triggers GC)

# Create heap dump for detailed analysis
jmap -dump:format=b,file=heap.hprof <pid>
jmap -dump:live,format=b,file=heap.hprof <pid>  # Only live objects

# Example output:
# Class Name                Objects    Size
# java.lang.String          1234567    89MB
# char[]                    987654     45MB
# java.util.HashMap         456789     23MB
```

#### **Garbage Collection Monitoring**
```bash
# Real-time GC statistics
jstat -gc <pid>                      # Current GC stats
jstat -gc <pid> 1s                   # Updates every second  
jstat -gcutil <pid> 1s               # GC utilization percentages

# Example jstat -gc output:
# S0C     S1C     S0U     S1U      EC       EU        OC         OU       MC     MU
# 8704.0  8704.0  0.0    8704.0   69952.0  2048.0   175104.0   0.0     4864.0  2432.4

# What these mean:
# S0C/S1C = Survivor space 0/1 capacity
# S0U/S1U = Survivor space 0/1 utilization  
# EC/EU   = Eden space capacity/utilization
# OC/OU   = Old generation capacity/utilization
# MC/MU   = Metaspace capacity/utilization
```

#### **Thread Analysis**
```bash
# Thread dumps (what all threads are doing)
jstack <pid>                         # All thread stacks
jcmd <pid> Thread.print             # Alternative method

# Look for these thread states:
# RUNNABLE    - Thread is executing
# BLOCKED     - Waiting for synchronized lock
# WAITING     - Waiting indefinitely (wait(), join())
# TIMED_WAIT  - Waiting with timeout (sleep(), wait(timeout))
```

#### **General JVM Information**
```bash
# JVM flags and system properties
jcmd <pid> VM.flags                 # JVM command-line flags
jcmd <pid> VM.system_properties     # System properties
jcmd <pid> VM.version               # JVM version info

# Performance counters
jcmd <pid> PerfCounter.print        # Internal performance metrics
```

### **Reading JVM Logs Like a Pro**

#### **Understanding GC Logs**
```bash
# Enable GC logging (Java 8)
java -Xloggc:gc.log -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps MyApp

# Enable GC logging (Java 9+)
java -Xlog:gc:gc.log:time MyApp

# Sample GC log entry:
# 2023-08-28T10:30:15.123+0000: [GC (Allocation Failure) 
# [PSYoungGen: 131072K->10240K(153600K)] 
# 245760K->124928K(502784K), 0.0123456 secs]

# Translation:
# - Timestamp: 2023-08-28T10:30:15.123+0000
# - Cause: Allocation Failure (Eden space full)
# - Young Gen: 131MB -> 10MB (out of 150MB capacity)
# - Total Heap: 240MB -> 122MB (out of 491MB capacity)
# - Duration: 12.3 milliseconds
```

### **Common JVM Problems & Quick Fixes**

#### **Memory Issues**
```
🔴 OutOfMemoryError: Java heap space
Cause:    Heap too small OR memory leak
Solution: 
  • Increase heap size: -Xmx8g
  • Analyze heap dump for memory leaks
  • Check for large object retention

🔴 OutOfMemoryError: Metaspace
Cause:    Too many classes loaded (common in dev with hot reload)
Solution:
  • Increase metaspace: -XX:MaxMetaspaceSize=512m
  • Check for class loader leaks

🔴 High GC overhead (>5% time in GC)
Cause:    Heap too small or wrong GC algorithm
Solution:
  • Increase heap size
  • Try different GC: -XX:+UseG1GC
  • Tune GC parameters
```

#### **Performance Issues**
```
🔴 Slow startup time
Cause:    Class loading overhead, large initialization
Solution:
  • Use Application Class-Data Sharing: -Xshare:on
  • Reduce classpath size
  • Lazy initialization patterns

🔴 Poor throughput after warmup
Cause:    Insufficient JIT compilation or memory pressure
Solution:
  • Allow more warmup time
  • Lower JIT thresholds: -XX:CompileThreshold=1500
  • Check memory allocation patterns

🔴 High CPU usage
Cause:    Inefficient algorithms, excessive GC, or infinite loops
Solution:
  • Profile with Java Flight Recorder
  • Check GC frequency and duration
  • Look for thread contention
```


---

## 🚀 **Production JVM Tuning Guide**

### **Essential JVM Parameters (Copy-Paste Ready)**

#### **Basic Heap Sizing**
```bash
# Small application (< 2GB heap)
java -Xms1g -Xmx2g -XX:+UseG1GC MyApp

# Medium application (2-8GB heap)
java -Xms4g -Xmx8g -XX:+UseG1GC -XX:MaxGCPauseMillis=200 MyApp

# Large application (> 8GB heap)
java -Xms8g -Xmx16g -XX:+UseG1GC -XX:MaxGCPauseMillis=100 MyApp

# Ultra-low latency (if available)
java -Xms4g -Xmx8g -XX:+UseZGC MyApp
```

#### **Production-Ready Template**
```bash
java \
  # Memory settings
  -Xms4g -Xmx8g \
  
  # Garbage Collection
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:G1HeapRegionSize=16m \
  
  # GC Logging (CRITICAL for production)
  -Xloggc:/var/log/myapp/gc.log \
  -XX:+PrintGC \
  -XX:+PrintGCDetails \
  -XX:+PrintGCTimeStamps \
  -XX:+UseGCLogFileRotation \
  -XX:NumberOfGCLogFiles=5 \
  -XX:GCLogFileSize=100M \
  
  # JIT Compilation
  -XX:+TieredCompilation \
  -XX:CompileThreshold=1500 \
  
  # Memory leak detection
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/dumps/ \
  
  # Performance monitoring  
  -XX:+FlightRecorder \
  -XX:StartFlightRecording=duration=300s,filename=/var/log/myapp/startup.jfr \
  
  MyApplication
```

### **JVM Tuning Checklist**

#### **Phase 1: Basic Setup**
```
✅ Set initial heap size (-Xms) same as max (-Xmx)
✅ Choose appropriate GC algorithm for workload
✅ Enable GC logging with rotation
✅ Set heap dump on OOM
✅ Enable basic performance monitoring
```

#### **Phase 2: Monitoring & Analysis**
```
✅ Monitor GC frequency and pause times
✅ Check heap utilization patterns
✅ Analyze allocation rates
✅ Monitor JIT compilation activity
✅ Track application performance metrics
```

#### **Phase 3: Optimization**
```
✅ Tune GC parameters based on observed behavior
✅ Adjust heap generations if needed
✅ Optimize application code for GC-friendly patterns
✅ Consider off-heap storage for large datasets
✅ Implement application-level monitoring
```

### **Quick Decision Matrix**

| Application Type | Heap Size | GC Algorithm | Key Parameters |
|------------------|-----------|--------------|----------------|
| **Web API** | 2-8GB | G1GC | `-XX:MaxGCPauseMillis=200` |
| **Batch Processing** | 4-16GB | ParallelGC | `-XX:+UseParallelGC` |
| **Real-time System** | 1-4GB | ZGC | `-XX:+UseZGC` |
| **Microservice** | 512MB-2GB | G1GC | `-XX:+UseContainerSupport` |
| **Big Data** | 8GB+ | G1GC or ZGC | `-XX:MaxGCPauseMillis=100` |

### **Emergency Debugging Commands**

```bash
# Application hanging? Get thread dump
jstack <pid> > threads_$(date +%H%M%S).txt

# Memory issues? Quick heap analysis
jmap -histo <pid> | head -20

# High GC? Check current GC stats
jstat -gc <pid>

# Force garbage collection (last resort!)
jcmd <pid> GC.run

# Check JVM flags currently in use
jcmd <pid> VM.flags | grep -E "UseG1GC|Xmx|Xms"
```

### **Performance Monitoring Code**

```java
// Monitor memory usage programmatically
public class JVMMonitor {
    public static void printMemoryStatus() {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memory.getHeapMemoryUsage();
        
        long usedMB = heapUsage.getUsed() / 1024 / 1024;
        long maxMB = heapUsage.getMax() / 1024 / 1024;
        double usage = (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
        
        System.out.printf("Heap: %d/%d MB (%.1f%%)\n", usedMB, maxMB, usage);
    }
    
    public static void printGCStats() {
        List<GarbageCollectorMXBean> gcBeans = 
            ManagementFactory.getGarbageCollectorMXBeans();
        
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("%s: %d collections, %d ms total\n",
                gcBean.getName(),
                gcBean.getCollectionCount(),
                gcBean.getCollectionTime());
        }
    }
}
```

---

## 📚 **Quick Reference Summary**

### **JVM Memory Areas**
- **Heap**: Objects, garbage collected, shared
- **Stack**: Method calls, local variables, per-thread
- **Method Area**: Class metadata, static variables, shared
- **PC Register**: Current instruction, per-thread

### **Garbage Collection**
- **Young GC**: Fast, frequent, cleans new objects
- **Old GC**: Slower, less frequent, cleans long-lived objects
- **Full GC**: Cleans entire heap, most expensive

### **Performance Monitoring**
- **jstat**: GC statistics and memory usage
- **jmap**: Heap dumps and object histograms  
- **jstack**: Thread dumps and deadlock detection
- **jcmd**: Swiss army knife for JVM diagnostics

### **Common Issues**
- **OOM**: Increase heap or find memory leaks
- **Long GC pauses**: Wrong GC or heap too small
- **High CPU**: Check for GC thrashing or inefficient code
- **Slow startup**: Reduce classpath or use CDS

---

**Next Steps:** 
- 📚 [Complete JVM Guide (README.md)](./README.md)
- 💻 [Hands-on Examples](./examples/)
- 🎯 [Practice Exercises](./exercises/)
- ➡️ [Java 8 Features](../13-Java8-Features/README.md)

### **Memory Analysis**
```bash
# Heap analysis
jmap -histo <pid>                    # Object histogram
jmap -dump:format=b,file=heap.hprof <pid>  # Heap dump

# Memory usage
jstat -gc <pid> 1s                   # GC statistics
jstat -gccapacity <pid>              # Memory capacities
jstat -gcutil <pid>                  # GC utilization percentage
```

### **Thread Analysis**
```bash
jstack <pid>                         # Thread dump
jcmd <pid> Thread.print              # Alternative thread dump
jcmd <pid> VM.classloader_stats      # Class loader statistics
```

### **Performance Monitoring**
```bash
# Java Flight Recorder
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=app.jfr

# GC logging
java -Xloggc:gc.log -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps

# JIT monitoring
java -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining
```

---

## 📊 **Performance Metrics**

### **Memory Metrics**
```java
// Get memory usage programmatically
MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();

long used = heapUsage.getUsed();     // Bytes used
long committed = heapUsage.getCommitted();  // Bytes committed
long max = heapUsage.getMax();       // Maximum bytes
```

### **GC Metrics**
```java
// Monitor garbage collection
List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

for (GarbageCollectorMXBean gcBean : gcBeans) {
    long collections = gcBean.getCollectionCount();
    long time = gcBean.getCollectionTime();
    System.out.printf("%s: %d collections, %d ms\n", 
                     gcBean.getName(), collections, time);
}
```

---

## 🚀 **Performance Tuning Checklist**

### **Initial Setup**
✅ Set appropriate heap size (`-Xms`, `-Xmx`)  
✅ Choose right GC algorithm for workload  
✅ Enable GC logging for monitoring  
✅ Set up performance monitoring tools  

### **Memory Optimization**
✅ Monitor heap utilization trends  
✅ Analyze object allocation patterns  
✅ Check for memory leaks  
✅ Optimize object lifecycle management  

### **GC Tuning**
✅ Measure GC pause times and frequency  
✅ Adjust generation sizes if needed  
✅ Tune GC parameters based on workload  
✅ Monitor GC overhead percentage  

### **JIT Optimization**
✅ Allow sufficient warmup time  
✅ Monitor compilation events  
✅ Check for deoptimization events  
✅ Verify method inlining behavior  

---

## ⚠️ **Common Issues**

### **Memory Problems**
- **OutOfMemoryError**: Heap too small or memory leak
- **High GC overhead**: > 5% time in GC indicates tuning needed
- **Memory fragmentation**: Can cause premature OOM in heap

### **Performance Issues**
- **Long GC pauses**: Wrong GC algorithm or heap sizing
- **Slow startup**: Class loading overhead or large initialization
- **Thread contention**: Monitor with thread dumps and profiling

### **Configuration Mistakes**
- **Oversized heap**: Can increase GC pause times
- **Wrong GC algorithm**: Match algorithm to workload characteristics
- **Missing monitoring**: Always enable logging in production

---

## 🔍 **Debugging Commands**

### **Emergency Diagnostics**
```bash
# Get immediate memory usage
jcmd <pid> GC.run_finalization
jcmd <pid> VM.memory_usage

# Thread dump on hanging process  
kill -3 <pid>  # Linux/Mac
jstack <pid>   # Cross-platform

# Force garbage collection
jcmd <pid> GC.run
```

### **Advanced Analysis**
```bash
# Class histogram with counts
jmap -histo:live <pid> | head -20

# Check for memory leaks
jmap -dump:live,format=b,file=heap_live.hprof <pid>

# JIT compiler statistics
jcmd <pid> Compiler.queue
jcmd <pid> VM.compiler_codecache
```

---

**Next:** [Java 8 Features Deep Dive](../13-Java8-Features/README.md)