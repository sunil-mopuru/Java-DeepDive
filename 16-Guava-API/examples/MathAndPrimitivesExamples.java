/**
 * Math and Primitives Examples - Mathematical Operations and Primitive Collections
 * 
 * Demonstrates Guava's mathematical utilities, primitive array operations,
 * hashing functions, and performance-optimized primitive collections.
 */

import com.google.common.math.*;
import com.google.common.primitives.*;
import com.google.common.hash.*;
import com.google.common.collect.*;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;

public class MathAndPrimitivesExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava Math and Primitives Examples ===");
        
        demonstrateMathUtilities();
        demonstratePrimitiveArrays();
        demonstrateHashingUtilities();
        demonstrateAdvancedMathOperations();
    }
    
    /**
     * Mathematical utilities demonstrations
     */
    public static void demonstrateMathUtilities() {
        System.out.println("\n1. Mathematical Utilities");
        System.out.println("=========================");
        
        // IntMath operations
        demonstrateIntMath();
        
        // LongMath operations  
        demonstrateLongMath();
        
        // DoubleMath operations
        demonstrateDoubleMath();
    }
    
    public static void demonstrateIntMath() {
        System.out.println("IntMath Operations:");
        
        // Basic operations
        System.out.println("GCD(48, 18) = " + IntMath.gcd(48, 18));
        System.out.println("Factorial(5) = " + IntMath.factorial(5));
        System.out.println("2^10 = " + IntMath.pow(2, 10));
        
        // Power of two operations
        System.out.println("Is 16 power of 2? " + IntMath.isPowerOfTwo(16));
        System.out.println("Is 15 power of 2? " + IntMath.isPowerOfTwo(15));
        
        // Logarithms
        System.out.println("log2(1024) = " + IntMath.log2(1024, RoundingMode.EXACT));
        System.out.println("log10(1000) = " + IntMath.log10(1000, RoundingMode.EXACT));
        
        // Square roots
        System.out.println("sqrt(144) = " + IntMath.sqrt(144, RoundingMode.EXACT));
        System.out.println("sqrt(150) = " + IntMath.sqrt(150, RoundingMode.DOWN));
        
        // Division utilities
        System.out.println("Divide 17 by 5 (UP) = " + IntMath.divide(17, 5, RoundingMode.UP));
        System.out.println("Divide 17 by 5 (DOWN) = " + IntMath.divide(17, 5, RoundingMode.DOWN));
        
        // Safe arithmetic (throws on overflow)
        try {
            int result = IntMath.checkedAdd(Integer.MAX_VALUE, 1);
            System.out.println("Addition result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("✓ Overflow detected: " + e.getMessage());
        }
        
        try {
            int result = IntMath.checkedMultiply(100000, 100000);
            System.out.println("Multiplication result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("✓ Overflow detected in multiplication");
        }
    }
    
    public static void demonstrateLongMath() {
        System.out.println("\nLongMath Operations:");
        
        // Large factorials
        System.out.println("Factorial(20) = " + LongMath.factorial(20));
        
        // Large powers
        System.out.println("3^20 = " + LongMath.pow(3, 20));
        
        // Binomial coefficients
        System.out.println("C(10,3) = " + LongMath.binomial(10, 3));
        System.out.println("C(50,2) = " + LongMath.binomial(50, 2));
        
        // Safe operations with large numbers
        try {
            long result = LongMath.checkedPow(10, 19);
            System.out.println("10^19 = " + result);
        } catch (ArithmeticException e) {
            System.out.println("✓ Overflow in power calculation");
        }
        
        // GCD for large numbers
        System.out.println("GCD(123456789, 987654321) = " + 
                         LongMath.gcd(123456789L, 987654321L));
    }
    
    public static void demonstrateDoubleMath() {
        System.out.println("\nDoubleMath Operations:");
        
        double[] values = {3.14159, 2.71828, 1.41421, Double.NaN, Double.POSITIVE_INFINITY};
        
        for (double value : values) {
            System.out.printf("Value: %8.5f", value);
            System.out.printf(" | Finite: %5s", DoubleMath.isFinite(value));
            
            if (DoubleMath.isFinite(value)) {
                System.out.printf(" | Rounded (HALF_UP): %3d", 
                                DoubleMath.roundToInt(value, RoundingMode.HALF_UP));
                System.out.printf(" | Rounded (DOWN): %3d", 
                                DoubleMath.roundToInt(value, RoundingMode.DOWN));
                
                if (value > 0) {
                    System.out.printf(" | Log2: %6.3f", DoubleMath.log2(value));
                }
            }
            System.out.println();
        }
        
        // Mathematical constants and functions
        System.out.println("\nMean calculation:");
        double[] dataset = {1.5, 2.3, 3.7, 4.1, 5.9, 6.2, 7.8};
        double sum = Arrays.stream(dataset).sum();
        double mean = sum / dataset.length;
        System.out.printf("Dataset mean: %.3f%n", mean);
    }
    
    /**
     * Primitive array operations
     */
    public static void demonstratePrimitiveArrays() {
        System.out.println("\n2. Primitive Array Operations");
        System.out.println("==============================");
        
        // Integer arrays
        demonstrateIntArrays();
        
        // Long arrays
        demonstrateLongArrays();
        
        // Double arrays
        demonstrateDoubleArrays();
        
        // Byte arrays
        demonstrateByteArrays();
    }
    
    public static void demonstrateIntArrays() {
        System.out.println("Integer Array Operations:");
        
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5, 6};
        int[] array3 = {7, 8, 9};
        
        // Concatenation
        int[] combined = Ints.concat(array1, array2, array3);
        System.out.println("Concatenated: " + Arrays.toString(combined));
        
        // Conversion between arrays and collections
        List<Integer> list = Ints.asList(combined);
        System.out.println("As List: " + list);
        
        int[] backToArray = Ints.toArray(list);
        System.out.println("Back to array: " + Arrays.toString(backToArray));
        
        // Statistical operations
        System.out.println("Min value: " + Ints.min(combined));
        System.out.println("Max value: " + Ints.max(combined));
        
        // Search operations
        System.out.println("Contains 5: " + Ints.contains(combined, 5));
        System.out.println("Index of 7: " + Ints.indexOf(combined, 7));
        System.out.println("Last index of 3: " + Ints.lastIndexOf(combined, 3));
        
        // Join as string
        System.out.println("Joined: " + Ints.join(", ", combined));
        
        // Lexicographic comparison
        int[] other = {1, 2, 3, 4};
        System.out.println("Compare arrays: " + Ints.lexicographicalComparator().compare(array1, other));
    }
    
    public static void demonstrateLongArrays() {
        System.out.println("\nLong Array Operations:");
        
        long[] timestamps = {1693276800L, 1693363200L, 1693449600L}; // Sample timestamps
        long[] moreTimestamps = {1693536000L, 1693622400L};
        
        long[] allTimestamps = Longs.concat(timestamps, moreTimestamps);
        System.out.println("All timestamps: " + Arrays.toString(allTimestamps));
        
        System.out.println("Min timestamp: " + Longs.min(allTimestamps));
        System.out.println("Max timestamp: " + Longs.max(allTimestamps));
        
        // Convert to list and sort
        List<Long> timestampList = Longs.asList(allTimestamps);
        Collections.sort(timestampList);
        System.out.println("Sorted timestamps: " + timestampList);
    }
    
    public static void demonstrateDoubleArrays() {
        System.out.println("\nDouble Array Operations:");
        
        double[] measurements = {3.14, 2.71, 1.41, 0.57, 1.61};
        double[] moreMeasurements = {2.23, 4.66, 1.32};
        
        double[] allMeasurements = Doubles.concat(measurements, moreMeasurements);
        System.out.printf("All measurements: %s%n", Arrays.toString(allMeasurements));
        
        System.out.printf("Min: %.2f%n", Doubles.min(allMeasurements));
        System.out.printf("Max: %.2f%n", Doubles.max(allMeasurements));
        
        // Statistical analysis
        double sum = Arrays.stream(allMeasurements).sum();
        double average = sum / allMeasurements.length;
        System.out.printf("Average: %.3f%n", average);
        
        // Check for special values
        double[] withSpecial = Doubles.concat(allMeasurements, new double[]{Double.NaN, Double.POSITIVE_INFINITY});
        System.out.println("Contains NaN: " + Doubles.contains(withSpecial, Double.NaN));
        System.out.println("Contains Infinity: " + Doubles.contains(withSpecial, Double.POSITIVE_INFINITY));
    }
    
    public static void demonstrateByteArrays() {
        System.out.println("\nByte Array Operations:");
        
        byte[] data1 = {0x48, 0x65, 0x6C, 0x6C, 0x6F}; // "Hello"
        byte[] data2 = {0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64}; // " World"
        
        byte[] combined = Bytes.concat(data1, data2);
        System.out.println("Combined bytes: " + Arrays.toString(combined));
        System.out.println("As string: " + new String(combined, StandardCharsets.UTF_8));
        
        // Search in byte arrays
        System.out.println("Contains 0x6C: " + Bytes.contains(combined, (byte) 0x6C));
        System.out.println("Index of 0x57: " + Bytes.indexOf(combined, (byte) 0x57));
    }
    
    /**
     * Hashing utilities demonstrations  
     */
    public static void demonstrateHashingUtilities() {
        System.out.println("\n3. Hashing Utilities");
        System.out.println("====================");
        
        demonstrateBasicHashing();
        demonstrateIncrementalHashing();
        demonstrateHashingObjects();
    }
    
    public static void demonstrateBasicHashing() {
        System.out.println("Basic Hashing:");
        
        String text = "Hello, Guava Hashing!";
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        
        // Different hash functions
        HashCode md5 = Hashing.md5().hashString(text, StandardCharsets.UTF_8);
        HashCode sha1 = Hashing.sha1().hashString(text, StandardCharsets.UTF_8);
        HashCode sha256 = Hashing.sha256().hashString(text, StandardCharsets.UTF_8);
        
        System.out.println("Text: " + text);
        System.out.println("MD5:    " + md5);
        System.out.println("SHA1:   " + sha1);
        System.out.println("SHA256: " + sha256);
        
        // Hash bytes directly
        HashCode bytesHash = Hashing.sha256().hashBytes(data);
        System.out.println("Bytes hash: " + bytesHash);
        
        // Non-cryptographic hashes (faster)
        HashCode murmur3 = Hashing.murmur3_32_fixed().hashString(text, StandardCharsets.UTF_8);
        System.out.println("Murmur3: " + murmur3);
    }
    
    public static void demonstrateIncrementalHashing() {
        System.out.println("\nIncremental Hashing:");
        
        // Build hash incrementally
        Hasher hasher = Hashing.sha256().newHasher();
        
        hasher.putString("User: ", StandardCharsets.UTF_8);
        hasher.putString("Alice", StandardCharsets.UTF_8);
        hasher.putChar(',');
        hasher.putString(" Age: ", StandardCharsets.UTF_8);
        hasher.putInt(30);
        hasher.putChar(',');
        hasher.putString(" Active: ", StandardCharsets.UTF_8);
        hasher.putBoolean(true);
        
        HashCode incrementalHash = hasher.hash();
        System.out.println("Incremental hash: " + incrementalHash);
        
        // Compare with direct hashing
        String directString = "User: Alice, Age: 30, Active: true";
        HashCode directHash = Hashing.sha256().hashString(directString, StandardCharsets.UTF_8);
        System.out.println("Direct hash: " + directHash);
        System.out.println("Hashes equal: " + incrementalHash.equals(directHash));
    }
    
    public static void demonstrateHashingObjects() {
        System.out.println("\nHashing Objects:");
        
        // Create sample objects
        Person person1 = new Person("Alice", 30, "alice@example.com");
        Person person2 = new Person("Bob", 25, "bob@example.com");
        Person person3 = new Person("Alice", 30, "alice@example.com"); // Same as person1
        
        // Hash objects consistently
        HashCode hash1 = hashPerson(person1);
        HashCode hash2 = hashPerson(person2);
        HashCode hash3 = hashPerson(person3);
        
        System.out.println("Person 1 hash: " + hash1);
        System.out.println("Person 2 hash: " + hash2);
        System.out.println("Person 3 hash: " + hash3);
        
        System.out.println("Person 1 == Person 3: " + hash1.equals(hash3));
        System.out.println("Person 1 == Person 2: " + hash1.equals(hash2));
        
        // Demonstrate hash collision handling
        demonstrateHashCollisions();
    }
    
    private static HashCode hashPerson(Person person) {
        return Hashing.sha256().newHasher()
            .putString(person.getName(), StandardCharsets.UTF_8)
            .putInt(person.getAge())
            .putString(person.getEmail(), StandardCharsets.UTF_8)
            .hash();
    }
    
    public static void demonstrateHashCollisions() {
        System.out.println("\nHash Distribution Analysis:");
        
        // Generate many hash codes and analyze distribution
        HashFunction hashFunction = Hashing.murmur3_32_fixed();
        Map<Integer, Integer> bucketCounts = new HashMap<>();
        int numBuckets = 100;
        int numItems = 10000;
        
        for (int i = 0; i < numItems; i++) {
            String item = "Item" + i;
            int hash = hashFunction.hashString(item, StandardCharsets.UTF_8).asInt();
            int bucket = Math.abs(hash) % numBuckets;
            bucketCounts.merge(bucket, 1, Integer::sum);
        }
        
        // Analyze distribution
        int minCount = bucketCounts.values().stream().min(Integer::compareTo).orElse(0);
        int maxCount = bucketCounts.values().stream().max(Integer::compareTo).orElse(0);
        double avgCount = (double) numItems / numBuckets;
        
        System.out.printf("Hash distribution (%d items, %d buckets):%n", numItems, numBuckets);
        System.out.printf("Min items per bucket: %d%n", minCount);
        System.out.printf("Max items per bucket: %d%n", maxCount);
        System.out.printf("Average items per bucket: %.1f%n", avgCount);
        System.out.printf("Distribution quality: %.2f%% (closer to 100%% is better)%n", 
                         (avgCount / maxCount) * 100);
    }
    
    /**
     * Advanced mathematical operations and algorithms
     */
    public static void demonstrateAdvancedMathOperations() {
        System.out.println("\n4. Advanced Mathematical Operations");
        System.out.println("===================================");
        
        demonstrateStatisticalOperations();
        demonstrateNumberTheory();
        demonstratePrecisionHandling();
    }
    
    public static void demonstrateStatisticalOperations() {
        System.out.println("Statistical Operations:");
        
        int[] dataset = IntStream.rangeClosed(1, 100).toArray();
        
        // Basic statistics using Guava utilities
        int sum = Arrays.stream(dataset).sum();
        double mean = (double) sum / dataset.length;
        
        System.out.println("Dataset: 1 to 100");
        System.out.println("Sum: " + sum);
        System.out.printf("Mean: %.2f%n", mean);
        System.out.println("Min: " + Ints.min(dataset));
        System.out.println("Max: " + Ints.max(dataset));
        
        // Combinatorics
        System.out.println("\nCombinatorics:");
        System.out.println("C(52,5) = " + LongMath.binomial(52, 5)); // Poker hands
        System.out.println("C(49,6) = " + LongMath.binomial(49, 6)); // Lottery combinations
        
        // Factorials for probability calculations
        System.out.println("10! = " + LongMath.factorial(10));
        System.out.println("13! = " + LongMath.factorial(13));
    }
    
    public static void demonstrateNumberTheory() {
        System.out.println("\nNumber Theory:");
        
        // Prime-related operations (using GCD)
        int[] numbers = {15, 28, 35, 42, 56};
        System.out.println("Numbers: " + Arrays.toString(numbers));
        
        // Find GCD of multiple numbers
        int gcd = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            gcd = IntMath.gcd(gcd, numbers[i]);
        }
        System.out.println("GCD of all numbers: " + gcd);
        
        // Powers of 2 analysis
        System.out.println("\nPowers of 2 analysis:");
        for (int i = 1; i <= 1024; i *= 2) {
            System.out.printf("%4d is power of 2: %s | log2(%4d) = %2d%n", 
                            i, IntMath.isPowerOfTwo(i), i, 
                            IntMath.log2(i, RoundingMode.EXACT));
        }
    }
    
    public static void demonstratePrecisionHandling() {
        System.out.println("\nPrecision and Rounding:");
        
        double[] values = {3.14159, 2.71828, 1.41421, 0.57721, 1.61803};
        RoundingMode[] modes = {
            RoundingMode.UP, RoundingMode.DOWN, 
            RoundingMode.HALF_UP, RoundingMode.HALF_DOWN,
            RoundingMode.CEILING, RoundingMode.FLOOR
        };
        
        System.out.printf("%-10s", "Value");
        for (RoundingMode mode : modes) {
            System.out.printf("%-12s", mode.toString());
        }
        System.out.println();
        
        for (double value : values) {
            System.out.printf("%-10.5f", value);
            for (RoundingMode mode : modes) {
                try {
                    int rounded = DoubleMath.roundToInt(value, mode);
                    System.out.printf("%-12d", rounded);
                } catch (ArithmeticException e) {
                    System.out.printf("%-12s", "ERROR");
                }
            }
            System.out.println();
        }
    }
    
    // Supporting class for hashing examples
    static class Person {
        private final String name;
        private final int age;
        private final String email;
        
        public Person(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
        
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getEmail() { return email; }
        
        @Override
        public String toString() {
            return String.format("Person{name='%s', age=%d, email='%s'}", name, age, email);
        }
    }
}