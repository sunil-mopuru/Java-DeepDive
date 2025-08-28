/**
 * JVM Fundamentals - Beginner to Advanced Examples
 * 
 * This file demonstrates JVM concepts in a progressive manner:
 * BEGINNER: Understanding basic JVM concepts and memory
 * INTERMEDIATE: Object lifecycle and garbage collection basics
 * ADVANCED: Performance monitoring and optimization
 * 
 * Run with: java JVMExamples
 * Optional JVM flags to see more details:
 * java -XX:+PrintGC -verbose:gc JVMExamples
 */

import java.lang.management.*;
import java.util.*;

public class JVMExamples {
    public static void main(String[] args) throws Exception {
        System.out.println("=== JVM Learning Journey: Beginner to Advanced ===");
        
        // BEGINNER LEVEL - Understanding basics
        System.out.println("\n``=== BEGINNER LEVEL ===``");
        explainWhatIsJVM();
        demonstrateBasicMemory();
        
        // INTERMEDIATE LEVEL - Memory and GC
        System.out.println("\n``=== INTERMEDIATE LEVEL ===``");
        demonstrateObjectLifecycle();
        explainGarbageCollection();
        
        // ADVANCED LEVEL - Monitoring and tuning
        System.out.println("\n``=== ADVANCED LEVEL ===``");
        monitorJVMPerformance();
        demonstrateMemoryPools();
    }
    
    /**
     * BEGINNER: What is JVM and how does Java code run?
     */
    public static void explainWhatIsJVM() {
        System.out.println("\n1. What is JVM? How does Java code run?");
        System.out.println("===========================================");
        
        // Step 1: Explain the process
        System.out.println("Java Execution Process:");
        System.out.println("1. Write Java code (.java files)");
        System.out.println("2. Compile to bytecode (.class files)");
        System.out.println("3. JVM loads and executes bytecode");
        System.out.println("4. JVM translates to machine code");
        
        // Step 2: Show JVM information
        System.out.println("\nCurrent JVM Information:");
        System.out.println("JVM Name: " + 
            ManagementFactory.getRuntimeMXBean().getVmName());
        System.out.println("JVM Version: " + 
            ManagementFactory.getRuntimeMXBean().getVmVersion());
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Operating System: " + System.getProperty("os.name"));
        
        // Step 3: Show why JVM is important
        System.out.println("\nWhy JVM matters:");
        System.out.println("``\u2713 Platform independence - same code runs everywhere``");
        System.out.println("``\u2713 Automatic memory management - no manual malloc/free``");
        System.out.println("``\u2713 Performance optimization - gets faster over time``");
        System.out.println("``\u2713 Security - sandboxed execution environment``");
    }
    
    /**
     * BEGINNER: Understanding Stack vs Heap memory
     */
    public static void demonstrateBasicMemory() {
        System.out.println("\n2. Memory Basics - Stack vs Heap");
        System.out.println("===================================");
        
        // Show current memory usage
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();
        
        System.out.println("Current Memory Status:");
        System.out.printf("Max Memory:   %4d MB (JVM can use up to this)\n", 
                         maxMemory / 1024 / 1024);
        System.out.printf("Total Memory: %4d MB (Currently allocated by JVM)\n", 
                         totalMemory / 1024 / 1024);
        System.out.printf("Used Memory:  %4d MB (Actually being used)\n", 
                         usedMemory / 1024 / 1024);
        System.out.printf("Free Memory:  %4d MB (Available for allocation)\n", 
                         freeMemory / 1024 / 1024);
        
        // Demonstrate Stack vs Heap
        System.out.println("\nStack vs Heap Examples:");
        
        // Stack variables (method parameters and local variables)
        int stackInt = 42;              // Primitive - stored in stack
        boolean stackBoolean = true;    // Primitive - stored in stack
        double stackDouble = 3.14;      // Primitive - stored in stack
        
        // Heap objects (all objects go to heap)
        String heapString = new String("Hello JVM!");  // Object in heap
        ArrayList<Integer> heapList = new ArrayList<>(); // Object in heap
        int[] heapArray = new int[100]; // Array in heap
        
        System.out.println("Stack Variables (fast, automatic cleanup):");
        System.out.println("  stackInt = " + stackInt);
        System.out.println("  stackBoolean = " + stackBoolean);
        System.out.println("  stackDouble = " + stackDouble);
        
        System.out.println("\nHeap Objects (slower, garbage collected):");
        System.out.println("  heapString = " + heapString);
        System.out.println("  heapList size = " + heapList.size());
        System.out.println("  heapArray length = " + heapArray.length);
        
        System.out.println("\nKey Differences:");
        System.out.println("``\u2713 Stack: Fast access, automatic cleanup, thread-specific``");
        System.out.println("``\u2713 Heap: Shared memory, garbage collected, slower access``");
    }
    
    /**
     * INTERMEDIATE: How objects are created, used, and cleaned up
     */
    public static void demonstrateObjectLifecycle() {
        System.out.println("\n3. Object Lifecycle - Birth to Death");
        System.out.println("=====================================");
        
        // Phase 1: Object Creation (Birth)
        System.out.println("Phase 1: Object Creation");
        showMemoryBefore("Before creating objects");
        
        List<Student> students = new ArrayList<>();
        
        // Create many objects to see memory allocation
        for (int i = 0; i < 5000; i++) {
            students.add(new Student("Student" + i, 18 + (i % 10)));
        }
        
        showMemoryBefore("After creating " + students.size() + " student objects");
        
        // Phase 2: Object Usage (Life)
        System.out.println("\nPhase 2: Object Usage");
        long adultStudents = students.stream()
            .filter(s -> s.getAge() >= 21)
            .count();
        
        String oldestStudent = students.stream()
            .max(Comparator.comparingInt(Student::getAge))
            .map(Student::getName)
            .orElse("None");
        
        System.out.println("Adult students (21+): " + adultStudents);
        System.out.println("Oldest student: " + oldestStudent);
        
        // Phase 3: Object Death (Garbage Collection)
        System.out.println("\nPhase 3: Making Objects Eligible for Garbage Collection");
        
        // Remove references to make objects eligible for GC
        students.clear();  // Clear the list
        students = null;   // Remove reference to the list itself
        
        // Suggest garbage collection (JVM decides whether to do it)
        System.out.println("Requesting garbage collection...");
        System.gc();
        
        // Give GC some time to run
        try { Thread.sleep(200); } catch (InterruptedException e) {}
        
        showMemoryBefore("After clearing objects and requesting GC");
        
        System.out.println("\nObject Lifecycle Summary:");
        System.out.println("``\u2713 Birth: new keyword allocates memory in heap``");
        System.out.println("``\u2713 Life: Objects are used by your program``");
        System.out.println("``\u2713 Death: No references \u2192 eligible for garbage collection``");
    }
    
    /**
     * INTERMEDIATE: Understanding Garbage Collection basics
     */
    public static void explainGarbageCollection() {
        System.out.println("\n4. Garbage Collection Fundamentals");
        System.out.println("===================================");
        
        // Get garbage collector information
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        
        System.out.println("Available Garbage Collectors:");
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.printf("- %s\n", gcBean.getName());
            System.out.printf("  Collections so far: %d\n", gcBean.getCollectionCount());
            System.out.printf("  Total time spent: %d ms\n", gcBean.getCollectionTime());
        }
        
        System.out.println("\nHow Garbage Collection Works:");
        System.out.println("1. Mark Phase: Find all reachable objects");
        System.out.println("2. Sweep Phase: Clean up unreachable objects");
        System.out.println("3. Compact Phase: Reduce memory fragmentation");
        
        // Demonstrate triggering GC
        System.out.println("\nDemonstrating GC trigger:");
        
        long gcCountBefore = getTotalGCCount();
        
        // Create temporary objects that will need cleanup
        for (int batch = 0; batch < 10; batch++) {
            List<byte[]> tempData = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                tempData.add(new byte[1024]); // 1KB each
            }
            // tempData goes out of scope here and becomes eligible for GC
        }
        
        long gcCountAfter = getTotalGCCount();
        
        if (gcCountAfter > gcCountBefore) {
            System.out.println("``\u2713 Garbage collection was triggered!``");
            System.out.println("GC cycles increased from " + gcCountBefore + 
                             " to " + gcCountAfter);
        } else {
            System.out.println("? GC might not have run yet (that's normal)");
            System.out.println("JVM decides when to run GC based on memory pressure");
        }
        
        System.out.println("\nGC Benefits:");
        System.out.println("``\u2713 Automatic memory management - no memory leaks``");
        System.out.println("``\u2713 No manual malloc/free like C/C++``");
        System.out.println("``\u2713 Prevents most memory-related crashes``");
    }
    
    /**
     * ADVANCED: Monitor JVM performance metrics
     */
    public static void monitorJVMPerformance() {
        System.out.println("\n5. JVM Performance Monitoring");
        System.out.println("==============================");
        
        // CPU and System Information
        System.out.println("System Information:");
        System.out.printf("Available CPU cores: %d\n", 
                         Runtime.getRuntime().availableProcessors());
        
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        System.out.printf("System load average: %.2f\n", osBean.getSystemLoadAverage());
        
        // Thread Information
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        System.out.printf("\nThread Information:\n");
        System.out.printf("Current threads: %d\n", threadBean.getThreadCount());
        System.out.printf("Peak threads: %d\n", threadBean.getPeakThreadCount());
        System.out.printf("Total threads created: %d\n", 
                         threadBean.getTotalStartedThreadCount());
        
        // Class Loading Information
        ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
        System.out.printf("\nClass Loading Information:\n");
        System.out.printf("Currently loaded classes: %d\n", 
                         classBean.getLoadedClassCount());
        System.out.printf("Total classes loaded: %d\n", 
                         classBean.getTotalLoadedClassCount());
        System.out.printf("Classes unloaded: %d\n", 
                         classBean.getUnloadedClassCount());
        
        // JIT Compilation Information
        CompilationMXBean compilationBean = ManagementFactory.getCompilationMXBean();
        if (compilationBean != null) {
            System.out.printf("\nJIT Compilation:\n");
            System.out.printf("Compiler: %s\n", compilationBean.getName());
            if (compilationBean.isCompilationTimeMonitoringSupported()) {
                System.out.printf("Total compilation time: %d ms\n", 
                                 compilationBean.getTotalCompilationTime());
            }
        }
        
        System.out.println("\nWhy Monitor Performance?");
        System.out.println("``\u2713 Identify memory usage patterns``");
        System.out.println("``\u2713 Detect performance bottlenecks``");
        System.out.println("``\u2713 Optimize application configuration``");
        System.out.println("``\u2713 Plan for production capacity``");
    }
    
    /**
     * ADVANCED: Understanding different memory pools
     */
    public static void demonstrateMemoryPools() {
        System.out.println("\n6. Advanced Memory Pool Analysis");
        System.out.println("==================================");
        
        System.out.println("JVM Memory Pools:");
        
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            MemoryUsage usage = pool.getUsage();
            if (usage != null) {
                String poolName = pool.getName();
                long used = usage.getUsed() / 1024 / 1024;  // Convert to MB
                long committed = usage.getCommitted() / 1024 / 1024;
                long max = usage.getMax() / 1024 / 1024;
                
                double usagePercent = max > 0 ? 
                    (double) usage.getUsed() / usage.getMax() * 100 : 0;
                
                System.out.printf("\n%-25s:\n", poolName);
                System.out.printf("  Used:      %4d MB\n", used);
                System.out.printf("  Committed: %4d MB\n", committed);
                System.out.printf("  Maximum:   %4d MB\n", max);
                if (max > 0) {
                    System.out.printf("  Usage:     %5.1f%%\n", usagePercent);
                }
                
                // Explain what each pool is for
                explainMemoryPool(poolName);
            }
        }
        
        System.out.println("\nMemory Pool Summary:");
        System.out.println("``\u2713 Eden Space: Where new objects are created``");
        System.out.println("``\u2713 Survivor Spaces: Objects that survived one GC cycle``");
        System.out.println("``\u2713 Old Generation: Long-lived objects``");
        System.out.println("``\u2713 Metaspace: Class metadata (Java 8+)``");
    }
    
    // Helper Methods
    
    private static void showMemoryBefore(String phase) {
        Runtime runtime = Runtime.getRuntime();
        long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
        System.out.printf("%s: %d MB used\n", phase, used);
    }
    
    private static long getTotalGCCount() {
        return ManagementFactory.getGarbageCollectorMXBeans().stream()
            .mapToLong(GarbageCollectorMXBean::getCollectionCount)
            .sum();
    }
    
    private static void explainMemoryPool(String poolName) {
        if (poolName.contains("Eden")) {
            System.out.println("  \u2192 Eden Space: Where new objects are born");
        } else if (poolName.contains("Survivor")) {
            System.out.println("  \u2192 Survivor Space: Objects that survived initial GC");
        } else if (poolName.contains("Old") || poolName.contains("Tenured")) {
            System.out.println("  \u2192 Old Generation: Long-lived objects");
        } else if (poolName.contains("Metaspace") || poolName.contains("Perm")) {
            System.out.println("  \u2192 Metaspace: Class definitions and metadata");
        } else if (poolName.contains("Code")) {
            System.out.println("  \u2192 Code Cache: Compiled native code from JIT");
        }
    }
}

/**
 * Simple class for demonstrating object lifecycle
 */
class Student {
    private String name;
    private int age;
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }
}

/**
 * Beginner-friendly memory demonstration
 */
class BeginnerMemoryDemo {
    
    public static void runBasicDemo() {
        System.out.println("\n=== Beginner Memory Demo ===");
        
        // 1. Primitive variables (stored in stack)
        System.out.println("\n1. Stack Memory (Primitives):");
        int number = 42;
        double price = 19.99;
        boolean isValid = true;
        char grade = 'A';
        
        System.out.println("These are stored in STACK memory:");
        System.out.println("int number = " + number);
        System.out.println("double price = " + price);
        System.out.println("boolean isValid = " + isValid);
        System.out.println("char grade = " + grade);
        
        // 2. Object variables (stored in heap)
        System.out.println("\n2. Heap Memory (Objects):");
        String message = "Hello World";
        StringBuilder builder = new StringBuilder("Building...");
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        
        System.out.println("These are stored in HEAP memory:");
        System.out.println("String message = " + message);
        System.out.println("StringBuilder = " + builder);
        System.out.println("ArrayList = " + numbers);
        
        // 3. Show memory usage
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / 1024 / 1024; // MB
        long freeMemory = runtime.freeMemory() / 1024 / 1024;   // MB
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("\n3. Current Memory Usage:");
        System.out.println("Total JVM Memory: " + totalMemory + " MB");
        System.out.println("Used Memory: " + usedMemory + " MB");
        System.out.println("Free Memory: " + freeMemory + " MB");
        
        System.out.println("\n``\u2713 Stack: Fast, automatic cleanup, thread-specific``");
        System.out.println("``\u2713 Heap: Slower, garbage collected, shared between threads``");
    }
}
