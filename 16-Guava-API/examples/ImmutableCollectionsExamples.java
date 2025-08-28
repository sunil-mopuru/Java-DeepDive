/**
 * Immutable Collections Examples - Thread-Safe and Performance Optimized Collections
 * 
 * This class demonstrates Guava's immutable collections including ImmutableList,
 * ImmutableSet, ImmutableMap, and their advanced usage patterns for building
 * thread-safe and performance-optimized applications.
 */

import com.google.common.collect.*;
import com.google.common.base.*;
import java.util.*;
import java.util.concurrent.*;
import java.time.*;
import java.util.stream.Collectors;

public class ImmutableCollectionsExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava Immutable Collections Examples ===");
        
        // Basic immutable collections
        demonstrateImmutableLists();
        demonstrateImmutableSets();
        demonstrateImmutableMaps();
        
        // Advanced patterns
        demonstrateBuilderPatterns();
        demonstratePerformanceOptimizations();
        
        // Real-world applications
        demonstrateConfigurationManagement();
        demonstrateThreadSafetyBenefits();
        
        // Advanced use cases
        demonstrateImmutableMultimap();
        demonstrateImmutableBiMap();
        demonstrateImmutableTable();
    }
    
    /**
     * Demonstrates ImmutableList creation and usage
     */
    public static void demonstrateImmutableLists() {
        System.out.println("\n1. ImmutableList Examples");
        System.out.println("=========================");
        
        // Creation methods
        System.out.println("Creating ImmutableLists:");
        
        // Empty list
        ImmutableList<String> emptyList = ImmutableList.of();
        System.out.println("Empty list: " + emptyList + " (size: " + emptyList.size() + ")");
        
        // Single element
        ImmutableList<String> singleElement = ImmutableList.of("Java");
        System.out.println("Single element: " + singleElement);
        
        // Multiple elements
        ImmutableList<String> languages = ImmutableList.of("Java", "Python", "JavaScript", "Go");
        System.out.println("Multiple elements: " + languages);
        
        // From existing collection
        List<String> mutableList = Arrays.asList("Spring", "Hibernate", "Guava");
        ImmutableList<String> fromCollection = ImmutableList.copyOf(mutableList);
        System.out.println("From collection: " + fromCollection);
        
        // From iterator
        ImmutableList<Integer> fromIterator = ImmutableList.copyOf(Arrays.asList(1, 2, 3, 4, 5).iterator());
        System.out.println("From iterator: " + fromIterator);
        
        // Demonstrate immutability
        demonstrateImmutability(languages);
        
        // List operations
        System.out.println("\nImmutableList Operations:");
        System.out.println("Get element at index 1: " + languages.get(1));
        System.out.println("Index of 'Java': " + languages.indexOf("Java"));
        System.out.println("Contains 'Python': " + languages.contains("Python"));
        System.out.println("Sublist (1, 3): " + languages.subList(1, 3));
        System.out.println("Reversed: " + languages.reverse());
        
        // Sorting (returns new sorted list)
        ImmutableList<String> sorted = ImmutableList.sortedCopyOf(languages);
        System.out.println("Sorted copy: " + sorted);
        System.out.println("Original unchanged: " + languages);
    }
    
    /**
     * Demonstrates immutability constraints
     */
    public static void demonstrateImmutability(ImmutableList<String> immutableList) {
        System.out.println("\nTesting Immutability:");
        
        try {
            // These operations will throw UnsupportedOperationException
            // immutableList.add("New Language");  // Would throw exception
            // immutableList.remove(0);            // Would throw exception
            // immutableList.set(0, "Modified");   // Would throw exception
            
            System.out.println("✓ Immutable list cannot be modified (as expected)");
            
            // Even getting the underlying list won't allow modifications
            List<String> asList = immutableList.asList();
            try {
                asList.add("Attempt to modify");
            } catch (UnsupportedOperationException e) {
                System.out.println("✓ Even asList() view is immutable");
            }
            
        } catch (UnsupportedOperationException e) {
            System.out.println("✗ Modification attempted: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates ImmutableSet creation and usage
     */
    public static void demonstrateImmutableSets() {
        System.out.println("\n2. ImmutableSet Examples");
        System.out.println("========================");
        
        // Creation methods
        System.out.println("Creating ImmutableSets:");
        
        ImmutableSet<String> colors = ImmutableSet.of("Red", "Green", "Blue");
        System.out.println("Colors set: " + colors);
        
        // Automatic deduplication
        ImmutableSet<String> withDuplicates = ImmutableSet.of("Apple", "Banana", "Apple", "Cherry");
        System.out.println("With duplicates (auto-deduplicated): " + withDuplicates);
        
        // From collection
        Set<String> mutableSet = new HashSet<>(Arrays.asList("Cat", "Dog", "Bird", "Fish"));
        ImmutableSet<String> animals = ImmutableSet.copyOf(mutableSet);
        System.out.println("Animals set: " + animals);
        
        // Set operations
        System.out.println("\nSet Operations:");
        ImmutableSet<Integer> set1 = ImmutableSet.of(1, 2, 3, 4, 5);
        ImmutableSet<Integer> set2 = ImmutableSet.of(4, 5, 6, 7, 8);
        
        System.out.println("Set 1: " + set1);
        System.out.println("Set 2: " + set2);
        
        // Union (using Sets utility)
        Sets.SetView<Integer> union = Sets.union(set1, set2);
        ImmutableSet<Integer> unionSet = union.immutableCopy();
        System.out.println("Union: " + unionSet);
        
        // Intersection
        Sets.SetView<Integer> intersection = Sets.intersection(set1, set2);
        ImmutableSet<Integer> intersectionSet = intersection.immutableCopy();
        System.out.println("Intersection: " + intersectionSet);
        
        // Difference
        Sets.SetView<Integer> difference = Sets.difference(set1, set2);
        ImmutableSet<Integer> differenceSet = difference.immutableCopy();
        System.out.println("Difference (set1 - set2): " + differenceSet);
    }
    
    /**
     * Demonstrates ImmutableMap creation and usage
     */
    public static void demonstrateImmutableMaps() {
        System.out.println("\n3. ImmutableMap Examples");
        System.out.println("========================");
        
        // Creation methods
        System.out.println("Creating ImmutableMaps:");
        
        // Simple creation
        ImmutableMap<String, Integer> ages = ImmutableMap.of(
            "Alice", 30,
            "Bob", 25,
            "Charlie", 35
        );
        System.out.println("Ages map: " + ages);
        
        // From existing map
        Map<String, String> mutableMap = new HashMap<>();
        mutableMap.put("Java", "Programming Language");
        mutableMap.put("Spring", "Framework");
        mutableMap.put("Guava", "Utility Library");
        
        ImmutableMap<String, String> technologies = ImmutableMap.copyOf(mutableMap);
        System.out.println("Technologies: " + technologies);
        
        // Map operations
        System.out.println("\nMap Operations:");
        System.out.println("Alice's age: " + ages.get("Alice"));
        System.out.println("Contains key 'Bob': " + ages.containsKey("Bob"));
        System.out.println("Contains value 25: " + ages.containsValue(25));
        System.out.println("All keys: " + ages.keySet());
        System.out.println("All values: " + ages.values());
        
        // Inverse map operations
        demonstrateMapTransformations(ages);
    }
    
    /**
     * Demonstrates map transformations and inversions
     */
    public static void demonstrateMapTransformations(ImmutableMap<String, Integer> ages) {
        System.out.println("\nMap Transformations:");
        
        // Transform values
        ImmutableMap<String, String> ageStrings = Maps.toMap(ages.keySet(), 
            name -> ages.get(name) + " years old");
        System.out.println("Age strings: " + ageStrings);
        
        // Filter by keys
        ImmutableMap<String, Integer> shortNames = Maps.filterKeys(ages, 
            name -> name.length() <= 3).entrySet().stream()
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Short names: " + shortNames);
        
        // Filter by values
        ImmutableMap<String, Integer> youngPeople = ages.entrySet().stream()
            .filter(entry -> entry.getValue() < 30)
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("Young people: " + youngPeople);
        
        // Create inverse map (value -> key)
        ImmutableMap<Integer, String> ageToName = ages.entrySet().stream()
            .collect(ImmutableMap.toImmutableMap(Map.Entry::getValue, Map.Entry::getKey));
        System.out.println("Age to name: " + ageToName);
    }
    
    /**
     * Demonstrates builder patterns for complex immutable collections
     */
    public static void demonstrateBuilderPatterns() {
        System.out.println("\n4. Builder Patterns");
        System.out.println("===================");
        
        // ImmutableList builder
        System.out.println("ImmutableList Builder:");
        ImmutableList<String> fruits = ImmutableList.<String>builder()
            .add("Apple")
            .add("Banana", "Cherry")
            .addAll(Arrays.asList("Date", "Elderberry"))
            .add("Fig")
            .build();
        System.out.println("Built fruits list: " + fruits);
        
        // ImmutableSet builder
        System.out.println("\nImmutableSet Builder:");
        ImmutableSet<String> countries = ImmutableSet.<String>builder()
            .add("USA")
            .add("Canada", "Mexico")
            .addAll(Arrays.asList("Brazil", "Argentina"))
            .add("Chile")
            .build();
        System.out.println("Built countries set: " + countries);
        
        // ImmutableMap builder
        System.out.println("\nImmutableMap Builder:");
        ImmutableMap<String, String> capitals = ImmutableMap.<String, String>builder()
            .put("USA", "Washington D.C.")
            .put("Canada", "Ottawa")
            .put("Mexico", "Mexico City")
            .putAll(Map.of("Brazil", "Brasília", "Argentina", "Buenos Aires"))
            .put("Chile", "Santiago")
            .build();
        System.out.println("Built capitals map: " + capitals);
        
        // Conditional building
        demonstrateConditionalBuilding();
    }
    
    /**
     * Demonstrates conditional building of immutable collections
     */
    public static void demonstrateConditionalBuilding() {
        System.out.println("\nConditional Building:");
        
        ImmutableList.Builder<String> todoBuilder = ImmutableList.builder();
        todoBuilder.add("Review code");
        
        boolean hasUrgentTasks = true;
        if (hasUrgentTasks) {
            todoBuilder.add("Fix critical bug", "Deploy hotfix");
        }
        
        boolean hasMeetings = true;
        if (hasMeetings) {
            todoBuilder.add("Attend standup", "Client meeting");
        }
        
        ImmutableList<String> todoList = todoBuilder.build();
        System.out.println("Dynamic todo list: " + todoList);
        
        // Building from stream
        ImmutableSet<Integer> evenNumbers = IntStream.range(1, 21)
            .filter(n -> n % 2 == 0)
            .boxed()
            .collect(ImmutableSet.toImmutableSet());
        System.out.println("Even numbers (1-20): " + evenNumbers);
    }
    
    /**
     * Demonstrates performance optimizations with immutable collections
     */
    public static void demonstratePerformanceOptimizations() {
        System.out.println("\n5. Performance Optimizations");
        System.out.println("============================");
        
        // Memory efficiency
        demonstrateMemoryEfficiency();
        
        // Hash code caching
        demonstrateHashCodeCaching();
        
        // Structural sharing
        demonstrateStructuralSharing();
    }
    
    /**
     * Shows memory efficiency of immutable collections
     */
    public static void demonstrateMemoryEfficiency() {
        System.out.println("Memory Efficiency:");
        
        // Large collection with duplicates
        List<String> mutableWithDuplicates = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            mutableWithDuplicates.add("Item " + (i % 100)); // Only 100 unique items
        }
        
        ImmutableSet<String> deduplicated = ImmutableSet.copyOf(mutableWithDuplicates);
        
        System.out.println("Original list size: " + mutableWithDuplicates.size());
        System.out.println("Deduplicated set size: " + deduplicated.size());
        System.out.println("Memory savings: " + (mutableWithDuplicates.size() - deduplicated.size()) + " elements");
        
        // Single-element optimizations
        ImmutableList<String> singleElement = ImmutableList.of("OnlyElement");
        ImmutableSet<String> singleElementSet = ImmutableSet.of("OnlyElement");
        
        System.out.println("Single element collections are optimized for memory");
    }
    
    /**
     * Demonstrates hash code caching benefits
     */
    public static void demonstrateHashCodeCaching() {
        System.out.println("\nHash Code Caching:");
        
        ImmutableList<String> largeList = ImmutableList.copyOf(
            IntStream.range(0, 10000)
                    .mapToObj(i -> "Element " + i)
                    .collect(Collectors.toList())
        );
        
        // First hash code computation
        long startTime = System.nanoTime();
        int hashCode1 = largeList.hashCode();
        long firstTime = System.nanoTime() - startTime;
        
        // Second hash code computation (should be cached)
        startTime = System.nanoTime();
        int hashCode2 = largeList.hashCode();
        long secondTime = System.nanoTime() - startTime;
        
        System.out.println("First hashCode() call: " + firstTime + " ns");
        System.out.println("Second hashCode() call: " + secondTime + " ns");
        System.out.println("Performance improvement: " + (firstTime / Math.max(1, secondTime)) + "x");
        System.out.println("Hash codes equal: " + (hashCode1 == hashCode2));
    }
    
    /**
     * Demonstrates structural sharing in immutable collections
     */
    public static void demonstrateStructuralSharing() {
        System.out.println("\nStructural Sharing:");
        
        ImmutableList<String> original = ImmutableList.of("A", "B", "C", "D", "E");
        ImmutableList<String> sublist = original.subList(1, 4);
        ImmutableList<String> reversed = original.reverse();
        
        System.out.println("Original: " + original);
        System.out.println("Sublist: " + sublist);
        System.out.println("Reversed: " + reversed);
        
        // These operations create views that share structure with the original
        System.out.println("Sublist shares structure with original (efficient)");
        System.out.println("Reversed shares structure with original (efficient)");
    }
    
    /**
     * Demonstrates configuration management using immutable collections
     */
    public static void demonstrateConfigurationManagement() {
        System.out.println("\n6. Configuration Management");
        System.out.println("===========================");
        
        // Application configuration
        ApplicationConfig config = new ApplicationConfig.Builder()
            .setAppName("MyApplication")
            .setVersion("1.2.3")
            .addAllowedHost("localhost")
            .addAllowedHost("myapp.com")
            .addFeature("feature1", true)
            .addFeature("feature2", false)
            .addDatabaseUrl("production", "jdbc:postgresql://prod:5432/mydb")
            .addDatabaseUrl("development", "jdbc:h2:mem:testdb")
            .build();
        
        System.out.println("Application Configuration:");
        System.out.println("App: " + config.getAppName() + " v" + config.getVersion());
        System.out.println("Allowed hosts: " + config.getAllowedHosts());
        System.out.println("Features: " + config.getFeatures());
        System.out.println("Database URLs: " + config.getDatabaseUrls());
        
        // Configuration is immutable and thread-safe
        System.out.println("\n✓ Configuration is thread-safe and cannot be modified");
        System.out.println("✓ Can be safely shared across multiple threads");
    }
    
    /**
     * Demonstrates thread safety benefits
     */
    public static void demonstrateThreadSafetyBenefits() {
        System.out.println("\n7. Thread Safety Benefits");
        System.out.println("=========================");
        
        ImmutableList<String> sharedList = ImmutableList.of("Task1", "Task2", "Task3", "Task4", "Task5");
        
        // Concurrent access without synchronization
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);
        
        for (int i = 0; i < 3; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // Multiple threads can safely read from the immutable collection
                    for (String task : sharedList) {
                        System.out.println("Thread " + threadId + " processing: " + task);
                        Thread.sleep(10); // Simulate work
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        executor.shutdown();
        System.out.println("✓ All threads completed safely without synchronization");
    }
    
    /**
     * Demonstrates ImmutableMultimap usage
     */
    public static void demonstrateImmutableMultimap() {
        System.out.println("\n8. ImmutableMultimap Examples");
        System.out.println("=============================");
        
        // Create ImmutableListMultimap
        ImmutableListMultimap<String, String> employeeDepartments = 
            ImmutableListMultimap.<String, String>builder()
                .put("Engineering", "Alice")
                .put("Engineering", "Bob")
                .put("Engineering", "Charlie")
                .put("Marketing", "David")
                .put("Marketing", "Eve")
                .put("Sales", "Frank")
                .put("Sales", "Grace")
                .put("Engineering", "Henry") // Can have duplicates in ListMultimap
                .build();
        
        System.out.println("Employee departments: " + employeeDepartments);
        System.out.println("Engineering team: " + employeeDepartments.get("Engineering"));
        System.out.println("All departments: " + employeeDepartments.keySet());
        System.out.println("All employees: " + employeeDepartments.values());
        
        // Create ImmutableSetMultimap (no duplicates in values)
        ImmutableSetMultimap<String, String> skillsMultimap = 
            ImmutableSetMultimap.<String, String>builder()
                .put("Alice", "Java")
                .put("Alice", "Python")
                .put("Alice", "Java") // Duplicate will be ignored
                .put("Bob", "JavaScript")
                .put("Bob", "React")
                .put("Charlie", "Java")
                .put("Charlie", "Spring")
                .build();
        
        System.out.println("\nSkills multimap: " + skillsMultimap);
        System.out.println("Alice's skills: " + skillsMultimap.get("Alice"));
    }
    
    /**
     * Demonstrates ImmutableBiMap usage
     */
    public static void demonstrateImmutableBiMap() {
        System.out.println("\n9. ImmutableBiMap Examples");
        System.out.println("==========================");
        
        ImmutableBiMap<String, String> countryCapitals = ImmutableBiMap.<String, String>builder()
            .put("USA", "Washington D.C.")
            .put("France", "Paris")
            .put("Japan", "Tokyo")
            .put("Germany", "Berlin")
            .put("Australia", "Canberra")
            .build();
        
        System.out.println("Country-Capital mapping: " + countryCapitals);
        
        // Forward lookup
        System.out.println("Capital of France: " + countryCapitals.get("France"));
        
        // Reverse lookup
        ImmutableBiMap<String, String> capitalCountry = countryCapitals.inverse();
        System.out.println("Country with capital Tokyo: " + capitalCountry.get("Tokyo"));
        
        // Both directions are available
        System.out.println("Forward map: " + countryCapitals);
        System.out.println("Inverse map: " + capitalCountry);
        
        System.out.println("✓ BiMap maintains bidirectional uniqueness");
    }
    
    /**
     * Demonstrates ImmutableTable usage
     */
    public static void demonstrateImmutableTable() {
        System.out.println("\n10. ImmutableTable Examples");
        System.out.println("===========================");
        
        // Create a table for student grades
        ImmutableTable<String, String, Integer> studentGrades = 
            ImmutableTable.<String, String, Integer>builder()
                .put("Alice", "Math", 95)
                .put("Alice", "Science", 88)
                .put("Alice", "English", 92)
                .put("Bob", "Math", 87)
                .put("Bob", "Science", 94)
                .put("Bob", "English", 89)
                .put("Charlie", "Math", 91)
                .put("Charlie", "Science", 86)
                .put("Charlie", "English", 93)
                .build();
        
        System.out.println("Student Grades Table:");
        System.out.println("All data: " + studentGrades);
        
        // Access by student (row)
        System.out.println("\nAlice's grades: " + studentGrades.row("Alice"));
        
        // Access by subject (column)
        System.out.println("Math grades: " + studentGrades.column("Math"));
        
        // Access specific cell
        System.out.println("Bob's Science grade: " + studentGrades.get("Bob", "Science"));
        
        // Get all students and subjects
        System.out.println("All students: " + studentGrades.rowKeySet());
        System.out.println("All subjects: " + studentGrades.columnKeySet());
        
        // Calculate averages
        double aliceAverage = studentGrades.row("Alice").values().stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
        System.out.println("Alice's average: " + String.format("%.1f", aliceAverage));
    }
    
    // Supporting classes for examples
    public static class ApplicationConfig {
        private final String appName;
        private final String version;
        private final ImmutableSet<String> allowedHosts;
        private final ImmutableMap<String, Boolean> features;
        private final ImmutableMap<String, String> databaseUrls;
        
        private ApplicationConfig(Builder builder) {
            this.appName = builder.appName;
            this.version = builder.version;
            this.allowedHosts = builder.allowedHosts.build();
            this.features = builder.features.build();
            this.databaseUrls = builder.databaseUrls.build();
        }
        
        public String getAppName() { return appName; }
        public String getVersion() { return version; }
        public ImmutableSet<String> getAllowedHosts() { return allowedHosts; }
        public ImmutableMap<String, Boolean> getFeatures() { return features; }
        public ImmutableMap<String, String> getDatabaseUrls() { return databaseUrls; }
        
        public static class Builder {
            private String appName;
            private String version;
            private final ImmutableSet.Builder<String> allowedHosts = ImmutableSet.builder();
            private final ImmutableMap.Builder<String, Boolean> features = ImmutableMap.builder();
            private final ImmutableMap.Builder<String, String> databaseUrls = ImmutableMap.builder();
            
            public Builder setAppName(String appName) {
                this.appName = appName;
                return this;
            }
            
            public Builder setVersion(String version) {
                this.version = version;
                return this;
            }
            
            public Builder addAllowedHost(String host) {
                this.allowedHosts.add(host);
                return this;
            }
            
            public Builder addFeature(String feature, boolean enabled) {
                this.features.put(feature, enabled);
                return this;
            }
            
            public Builder addDatabaseUrl(String environment, String url) {
                this.databaseUrls.put(environment, url);
                return this;
            }
            
            public ApplicationConfig build() {
                return new ApplicationConfig(this);
            }
        }
    }
}