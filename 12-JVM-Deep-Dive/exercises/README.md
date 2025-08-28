# JVM Deep Dive - Practice Exercises

## 📝 Instructions
- Use JVM monitoring tools to analyze behavior
- Experiment with different JVM parameters and observe effects
- Practice memory analysis and garbage collection tuning
- Learn to identify and resolve performance bottlenecks
- Focus on real-world JVM optimization scenarios

---

## Exercise 1: JVM Memory Analysis Lab
**Difficulty: Beginner-Intermediate**

Build a comprehensive memory analysis tool that monitors heap usage patterns and identifies potential issues.

### Requirements:
- **Memory Monitoring**: Track heap, non-heap, and memory pool usage
- **Allocation Tracking**: Monitor object allocation rates and patterns
- **Leak Detection**: Implement basic memory leak detection algorithms
- **Report Generation**: Create detailed memory usage reports
- **Threshold Alerting**: Alert when memory usage exceeds thresholds

### Implementation Tasks:
```java
public class MemoryAnalyzer {
    public void analyzeMemoryUsage() {
        // Monitor all memory pools
        // Track allocation rates
        // Detect unusual patterns
        // Generate comprehensive reports
    }
    
    public void detectMemoryLeaks() {
        // Compare heap dumps over time
        // Identify growing object collections
        // Report potential leak candidates
    }
}
```

### Expected Output:
```
=== Memory Analysis Report ===
Heap Usage: 1,234 MB / 2,048 MB (60.3%)
Allocation Rate: 45.2 MB/sec
GC Frequency: Every 12.5 seconds
Potential Issues:
  ⚠️  High allocation rate in package com.app.cache
  ⚠️  String objects growing consistently
```

---

## Exercise 2: Garbage Collection Performance Tuner
**Difficulty: Intermediate**

Create a GC performance analysis and tuning tool that compares different garbage collectors.

### Requirements:
- **GC Algorithm Testing**: Compare Serial, Parallel, G1, ZGC performance
- **Benchmark Suite**: Run standardized workloads with different GC settings
- **Metrics Collection**: Measure pause times, throughput, memory overhead
- **Tuning Recommendations**: Suggest optimal GC parameters for workloads
- **Performance Visualization**: Generate charts and performance reports

### Test Scenarios:
```java
public class GCPerformanceTuner {
    public void runGCBenchmarks() {
        // Test different allocation patterns:
        // 1. High allocation rate (streaming data)
        // 2. Long-lived objects (caching)
        // 3. Mixed workload (typical application)
        // 4. Batch processing (large data sets)
    }
    
    public void compareGCAlgorithms() {
        // Run same workload with different GC algorithms
        // Measure and compare:
        // - Average pause time
        // - 95th percentile pause time
        // - Throughput (ops/sec)
        // - Memory overhead
    }
}
```

### Deliverables:
- Performance comparison matrix
- Tuning parameter recommendations
- Workload-specific GC selection guide

---

## Exercise 3: JIT Compilation Analyzer
**Difficulty: Intermediate-Advanced**

Build a tool to analyze JIT compilation behavior and optimize hotspot performance.

### Requirements:
- **Hotspot Detection**: Identify methods that benefit from JIT compilation
- **Compilation Tracking**: Monitor compilation events and optimization levels
- **Performance Correlation**: Correlate compilation with performance improvements
- **Optimization Analysis**: Analyze inlining decisions and loop optimizations
- **Benchmarking**: Compare interpreted vs compiled performance

### Analysis Features:
```java
public class JITAnalyzer {
    public void trackCompilationEvents() {
        // Monitor compilation queue
        // Track compilation time and levels
        // Identify deoptimization events
    }
    
    public void analyzeHotspots() {
        // Find frequently executed methods
        // Measure compilation impact on performance
        // Suggest JIT tuning parameters
    }
}
```

---

## Exercise 4: Production JVM Monitor
**Difficulty: Advanced**

Develop a production-ready JVM monitoring system with alerting and automated responses.

### Requirements:
- **Real-time Monitoring**: Continuous tracking of JVM health metrics
- **Alert System**: Configurable thresholds and notification channels
- **Automated Actions**: Automatic responses to common issues
- **Historical Analysis**: Trend analysis and capacity planning
- **Dashboard Integration**: REST API for external monitoring systems

### System Architecture:
```java
@Component
public class JVMMonitoringService {
    
    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void collectMetrics() {
        // Collect memory, GC, thread, and compilation metrics
        // Store in time-series database
        // Check alert conditions
    }
    
    public void handleMemoryAlert(MemoryAlert alert) {
        if (alert.getSeverity() == AlertSeverity.CRITICAL) {
            // Force garbage collection
            // Generate heap dump
            // Send emergency notification
        }
    }
}
```

### Features to Implement:
- Memory usage trending and forecasting
- GC pause time SLA monitoring
- Thread deadlock detection
- CPU usage correlation with JIT compilation
- Automatic heap dump generation on OutOfMemoryError

---

## Exercise 5: JVM Performance Optimization Challenge
**Difficulty: Expert**

Optimize a poorly performing application using JVM tuning and code improvements.

### Scenario:
You're given a Java application with severe performance issues:
- High GC overhead (>10% of execution time)
- Frequent OutOfMemoryErrors
- Poor response time (>5 second average)
- High CPU usage with low throughput

### Optimization Tasks:

#### Phase 1: Diagnosis
```bash
# Use these tools to identify issues:
jstat -gc <pid> 1s          # Monitor GC behavior
jstack <pid>                # Check for deadlocks
jmap -histo <pid>          # Find memory usage patterns
jcmd <pid> VM.compiler_codecache  # Check code cache usage
```

#### Phase 2: JVM Tuning
```bash
# Experiment with different JVM parameters:
-Xms2g -Xmx8g                    # Heap sizing
-XX:+UseG1GC                     # GC algorithm
-XX:MaxGCPauseMillis=200        # Pause time target
-XX:+UseStringDeduplication     # Memory optimization
-XX:CompileThreshold=5000       # JIT tuning
```

#### Phase 3: Code Optimization
- Identify memory allocation hotspots
- Optimize object lifecycle management
- Implement object pooling where appropriate
- Reduce object churn in critical paths

### Success Criteria:
- Reduce GC overhead to <3%
- Eliminate OutOfMemoryErrors
- Achieve <1 second average response time
- Increase throughput by 300%

---

## 🎯 Challenge Projects

### Project A: Multi-JVM Performance Comparison
Compare JVM performance across different vendors:
- Oracle HotSpot vs OpenJDK vs GraalVM
- Different Java versions (8, 11, 17, 21)
- Various workload types and characteristics
- Create comprehensive performance matrix

### Project B: Container-Aware JVM Tuning
Optimize JVM for containerized environments:
- Docker memory limit detection
- Kubernetes resource constraint handling
- Auto-scaling JVM parameters
- Container-specific GC tuning

### Project C: JVM Security and Performance
Balance security features with performance:
- Security manager overhead analysis
- Module system performance impact
- JIT compilation security implications
- Performance cost of various security features

---

## 📚 Learning Resources

### Essential JVM Tools
```bash
# Memory analysis
jmap, jhat, Eclipse MAT, VisualVM

# Performance profiling
JProfiler, Java Flight Recorder, async-profiler

# GC analysis
GCViewer, GCPlot, CRaC (Coordinated Restore at Checkpoint)

# Thread analysis
jstack, jcmd, FastThread.io
```

### Key Performance Metrics
- **Latency**: P50, P95, P99 response times
- **Throughput**: Requests per second, transactions per second
- **Resource Utilization**: CPU, memory, GC overhead
- **Scalability**: Performance under increasing load

### Monitoring Best Practices
1. **Establish Baselines**: Measure normal operation before optimization
2. **Monitor Continuously**: Use production monitoring tools
3. **Test Realistic Workloads**: Use representative data and usage patterns
4. **Measure End-to-End**: Include all system components in analysis
5. **Document Changes**: Keep records of tuning parameters and results

---

## 🔧 Practical Tips

### Memory Optimization
- Size heap appropriately (start with 25% of system memory)
- Use memory profilers to identify allocation hotspots
- Implement object pooling for frequently allocated objects
- Monitor for memory leaks using heap dump analysis

### GC Tuning
- Start with G1GC for most applications
- Set reasonable pause time targets (200ms or less)
- Monitor GC logs for patterns and anomalies
- Test different algorithms with your specific workload

### JIT Optimization
- Allow sufficient warmup time before measuring performance
- Use -XX:+PrintCompilation to monitor compilation events
- Consider tiered compilation for mixed workloads
- Profile method hotspots and optimize accordingly

---

**Next:** [Java 8 Features Deep Dive](../../13-Java8-Features/exercises/)"