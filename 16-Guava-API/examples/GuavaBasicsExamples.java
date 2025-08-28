/**
 * Guava Basics Examples - Introduction to Google Guava
 * 
 * This class demonstrates the fundamental concepts and basic usage of Google Guava,
 * providing a foundation for understanding this powerful utility library.
 */

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.primitives.*;
import java.util.*;
import java.util.concurrent.*;

public class GuavaBasicsExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Google Guava Basics Examples ===");
        
        // Basic collection utilities
        demonstrateBasicCollections();
        
        // String processing basics
        demonstrateStringProcessing();
        
        // Null safety with Optional
        demonstrateNullSafety();
        
        // Basic functional programming
        demonstrateFunctionalBasics();
        
        // Primitive utilities
        demonstratePrimitiveUtilities();
        
        // Preconditions and validation
        demonstratePreconditions();
    }
    
    /**
     * Demonstrates basic collection creation and utilities
     */
    public static void demonstrateBasicCollections() {
        System.out.println("\n1. Basic Collections");
        System.out.println("====================");
        
        // Creating lists with Guava
        System.out.println("Creating Lists:");
        List<String> emptyList = Lists.newArrayList();
        List<String> initializedList = Lists.newArrayList("Apple", "Banana", "Cherry");
        List<String> capacityList = Lists.newArrayListWithCapacity(100);
        
        System.out.println("Empty list: " + emptyList);
        System.out.println("Initialized list: " + initializedList);
        System.out.println("Capacity list size: " + capacityList.size());
        
        // Creating sets with Guava
        System.out.println("\nCreating Sets:");
        Set<String> hashSet = Sets.newHashSet("Red", "Green", "Blue");
        Set<String> linkedHashSet = Sets.newLinkedHashSet();
        Set<String> treeSet = Sets.newTreeSet();
        
        linkedHashSet.addAll(Arrays.asList("Third", "First", "Second"));
        treeSet.addAll(Arrays.asList("Zebra", "Apple", "Monkey"));
        
        System.out.println("Hash set: " + hashSet);
        System.out.println("Linked hash set (insertion order): " + linkedHashSet);
        System.out.println("Tree set (sorted): " + treeSet);
        
        // Creating maps with Guava
        System.out.println("\nCreating Maps:");
        Map<String, Integer> hashMap = Maps.newHashMap();
        Map<String, Integer> linkedHashMap = Maps.newLinkedHashMap();
        Map<String, Integer> treeMap = Maps.newTreeMap();
        
        // Populate maps
        hashMap.put("Alice", 30);
        hashMap.put("Bob", 25);
        hashMap.put("Charlie", 35);
        
        System.out.println("Hash map: " + hashMap);
        
        // Set operations
        demonstrateSetOperations();
    }
    
    /**
     * Demonstrates set operations using Guava utilities
     */
    public static void demonstrateSetOperations() {
        System.out.println("\nSet Operations:");
        
        Set<Integer> set1 = Sets.newHashSet(1, 2, 3, 4, 5);
        Set<Integer> set2 = Sets.newHashSet(4, 5, 6, 7, 8);
        
        System.out.println("Set 1: " + set1);
        System.out.println("Set 2: " + set2);
        
        // Union: all elements from both sets
        Sets.SetView<Integer> union = Sets.union(set1, set2);
        System.out.println("Union: " + union);
        
        // Intersection: common elements
        Sets.SetView<Integer> intersection = Sets.intersection(set1, set2);
        System.out.println("Intersection: " + intersection);
        
        // Difference: elements in set1 but not in set2
        Sets.SetView<Integer> difference = Sets.difference(set1, set2);
        System.out.println("Difference (set1 - set2): " + difference);
        
        // Symmetric difference: elements in either set but not in both
        Sets.SetView<Integer> symmetricDiff = Sets.symmetricDifference(set1, set2);
        System.out.println("Symmetric difference: " + symmetricDiff);
    }
    
    /**
     * Demonstrates basic string processing with Joiner and Splitter
     */
    public static void demonstrateStringProcessing() {
        System.out.println("\n2. Basic String Processing");
        System.out.println("===========================");
        
        // Joiner examples
        System.out.println("Joiner Examples:");
        
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry", "Date");
        String joined = Joiner.on(", ").join(fruits);
        System.out.println("Joined fruits: " + joined);
        
        // Handling nulls with Joiner
        List<String> fruitsWithNulls = Arrays.asList("Apple", null, "Cherry", "Date");
        String joinedSkipNulls = Joiner.on(", ").skipNulls().join(fruitsWithNulls);
        System.out.println("Joined (skip nulls): " + joinedSkipNulls);
        
        String joinedReplaceNulls = Joiner.on(", ").useForNull("Unknown").join(fruitsWithNulls);
        System.out.println("Joined (replace nulls): " + joinedReplaceNulls);
        
        // Map joining
        Map<String, Integer> scores = Maps.newLinkedHashMap();
        scores.put("Alice", 95);
        scores.put("Bob", 87);
        scores.put("Charlie", 92);
        
        String mapJoined = Joiner.on(", ").withKeyValueSeparator(" -> ").join(scores);
        System.out.println("Map joined: " + mapJoined);
        
        // Splitter examples
        System.out.println("\nSplitter Examples:");
        
        String csvData = "Apple,Banana,Cherry,Date,Elderberry";
        Iterable<String> splitResult = Splitter.on(',').split(csvData);
        System.out.println("Split CSV: " + Lists.newArrayList(splitResult));
        
        // Handling empty strings and whitespace
        String messyData = "Apple, ,Cherry,  Date  , ,Elderberry,";
        Iterable<String> cleanSplit = Splitter.on(',')
            .trimResults()
            .omitEmptyStrings()
            .split(messyData);
        System.out.println("Clean split: " + Lists.newArrayList(cleanSplit));
        
        // Limit splits
        String longString = "One,Two,Three,Four,Five,Six";
        Iterable<String> limitedSplit = Splitter.on(',').limit(3).split(longString);
        System.out.println("Limited split (3): " + Lists.newArrayList(limitedSplit));
        
        // Split into map
        String keyValueData = "name=Alice,age=30,city=NewYork,country=USA";
        Map<String, String> dataMap = Splitter.on(',')
            .withKeyValueSeparator('=')
            .split(keyValueData);
        System.out.println("Split to map: " + dataMap);
    }
    
    /**
     * Demonstrates null safety using Guava's Optional
     */
    public static void demonstrateNullSafety() {
        System.out.println("\n3. Null Safety with Optional");
        System.out.println("=============================");
        
        // Creating Optional values
        System.out.println("Creating Optional values:");
        
        Optional<String> presentValue = Optional.of("Hello, Guava!");
        Optional<String> absentValue = Optional.absent();
        Optional<String> nullableValue = Optional.fromNullable(getString(true));
        Optional<String> nullValue = Optional.fromNullable(getString(false));
        
        System.out.println("Present optional: " + presentValue);
        System.out.println("Absent optional: " + absentValue);
        System.out.println("Nullable (non-null): " + nullableValue);
        System.out.println("Nullable (null): " + nullValue);
        
        // Using Optional values
        System.out.println("\nUsing Optional values:");
        
        // Check if present
        if (presentValue.isPresent()) {
            System.out.println("Present value: " + presentValue.get());
        }
        
        // Provide default values
        String value1 = absentValue.or("Default Value");
        System.out.println("Absent with default: " + value1);
        
        String value2 = nullValue.or("Fallback");
        System.out.println("Null with fallback: " + value2);
        
        // Transform values
        Optional<Integer> length = presentValue.transform(String::length);
        System.out.println("Transformed length: " + length);
        
        Optional<String> upperCase = presentValue.transform(String::toUpperCase);
        System.out.println("Transformed to upper: " + upperCase);
        
        // Chain transformations
        Optional<String> result = Optional.of("  Hello World  ")
            .transform(String::trim)
            .transform(String::toLowerCase);
        System.out.println("Chained transformation: " + result);
        
        // Demonstrate safe user data processing
        demonstrateSafeUserProcessing();
    }
    
    /**
     * Helper method for Optional demonstration
     */
    private static String getString(boolean returnValue) {
        return returnValue ? "Valid String" : null;
    }
    
    /**
     * Demonstrates safe user data processing with Optional
     */
    public static void demonstrateSafeUserProcessing() {
        System.out.println("\nSafe User Data Processing:");
        
        // Simulate user data that might be null
        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("Bob", null);
        User user3 = null;
        
        List<User> users = Arrays.asList(user1, user2, user3);
        
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println("User " + (i + 1) + ":");
            
            // Safe name processing
            String displayName = Optional.fromNullable(user)
                .transform(User::getName)
                .or("Anonymous User");
            System.out.println("  Display name: " + displayName);
            
            // Safe email processing
            String emailDomain = Optional.fromNullable(user)
                .transform(User::getEmail)
                .transform(email -> email.substring(email.indexOf('@') + 1))
                .or("no-domain.com");
            System.out.println("  Email domain: " + emailDomain);
        }
    }
    
    /**
     * Demonstrates basic functional programming concepts with Guava
     */
    public static void demonstrateFunctionalBasics() {
        System.out.println("\n4. Basic Functional Programming");
        System.out.println("===============================");
        
        // Predicates
        System.out.println("Predicates:");
        
        List<String> words = Arrays.asList("Hello", "World", "Guava", "Java", "Programming");
        
        // Filter with predicates
        Predicate<String> isLongWord = word -> word.length() > 4;
        Predicate<String> startsWithJ = word -> word.startsWith("J");
        
        Collection<String> longWords = Collections2.filter(words, isLongWord);
        System.out.println("Long words (>4 chars): " + longWords);
        
        Collection<String> jWords = Collections2.filter(words, startsWithJ);
        System.out.println("Words starting with 'J': " + jWords);
        
        // Combine predicates
        Predicate<String> longJWords = Predicates.and(isLongWord, startsWithJ);
        Collection<String> longJavaWords = Collections2.filter(words, longJWords);
        System.out.println("Long words starting with 'J': " + longJavaWords);
        
        // Functions and transformations
        System.out.println("\nFunctions and Transformations:");
        
        Function<String, Integer> stringLength = String::length;
        Function<String, String> toUpperCase = String::toUpperCase;
        
        // Transform collection
        Collection<String> upperWords = Collections2.transform(words, toUpperCase);
        System.out.println("Uppercase words: " + upperWords);
        
        Collection<Integer> wordLengths = Collections2.transform(words, stringLength);
        System.out.println("Word lengths: " + wordLengths);
        
        // Function composition
        Function<String, String> upperAndExclaim = Functions.compose(
            s -> s + "!",
            String::toUpperCase
        );
        
        Collection<String> excitedWords = Collections2.transform(words, upperAndExclaim);
        System.out.println("Excited words: " + excitedWords);
    }
    
    /**
     * Demonstrates primitive utilities
     */
    public static void demonstratePrimitiveUtilities() {
        System.out.println("\n5. Primitive Utilities");
        System.out.println("======================");
        
        // Integer array utilities
        System.out.println("Integer Arrays:");
        
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5, 6};
        
        // Concatenate arrays
        int[] combined = Ints.concat(array1, array2);
        System.out.println("Combined arrays: " + Arrays.toString(combined));
        
        // Convert between arrays and lists
        List<Integer> intList = Ints.asList(combined);
        System.out.println("Array as list: " + intList);
        
        int[] backToArray = Ints.toArray(intList);
        System.out.println("List back to array: " + Arrays.toString(backToArray));
        
        // Find min and max
        int min = Ints.min(combined);
        int max = Ints.max(combined);
        System.out.println("Min: " + min + ", Max: " + max);
        
        // Check contains
        boolean containsThree = Ints.contains(combined, 3);
        System.out.println("Contains 3: " + containsThree);
        
        // Join as string
        String joinedInts = Ints.join(", ", combined);
        System.out.println("Joined as string: " + joinedInts);
        
        // String array utilities
        System.out.println("\nString Arrays:");
        String[] names = {"Alice", "Bob", "Charlie"};
        List<String> nameList = Arrays.asList(names);
        System.out.println("Name list: " + nameList);
        
        // Reverse arrays
        Collections.reverse(nameList);
        System.out.println("Reversed names: " + nameList);
    }
    
    /**
     * Demonstrates preconditions and validation
     */
    public static void demonstratePreconditions() {
        System.out.println("\n6. Preconditions and Validation");
        System.out.println("===============================");
        
        System.out.println("Validation Examples:");
        
        try {
            // Validate arguments
            validateAge(25);
            System.out.println("✓ Valid age: 25");
            
            validateEmail("user@example.com");
            System.out.println("✓ Valid email: user@example.com");
            
            // This will throw an exception
            validateAge(-5);
            
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Validation failed: " + e.getMessage());
        }
        
        try {
            // Validate state
            BankAccount account = new BankAccount(100.0);
            account.withdraw(50.0);
            System.out.println("✓ Withdrawal successful. Balance: " + account.getBalance());
            
            // This will throw an exception
            account.withdraw(100.0);
            
        } catch (IllegalStateException e) {
            System.out.println("✗ State validation failed: " + e.getMessage());
        }
        
        try {
            // Validate not null
            processUser(new User("Alice", "alice@example.com"));
            System.out.println("✓ User processed successfully");
            
            // This will throw an exception
            processUser(null);
            
        } catch (NullPointerException e) {
            System.out.println("✗ Null validation failed: " + e.getMessage());
        }
    }
    
    // Helper methods for preconditions demonstration
    private static void validateAge(int age) {
        Preconditions.checkArgument(age >= 0 && age <= 120, 
            "Age must be between 0 and 120, but was: %s", age);
    }
    
    private static void validateEmail(String email) {
        Preconditions.checkArgument(email != null && email.contains("@"), 
            "Invalid email format: %s", email);
    }
    
    private static void processUser(User user) {
        User validUser = Preconditions.checkNotNull(user, "User cannot be null");
        System.out.println("Processing user: " + validUser.getName());
    }
    
    // Supporting classes
    static class User {
        private final String name;
        private final String email;
        
        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
        
        public String getName() {
            return name;
        }
        
        public String getEmail() {
            return email;
        }
        
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("email", email)
                .toString();
        }
    }
    
    static class BankAccount {
        private double balance;
        
        public BankAccount(double initialBalance) {
            Preconditions.checkArgument(initialBalance >= 0, 
                "Initial balance cannot be negative");
            this.balance = initialBalance;
        }
        
        public void withdraw(double amount) {
            Preconditions.checkArgument(amount > 0, "Withdrawal amount must be positive");
            Preconditions.checkState(balance >= amount, 
                "Insufficient funds. Balance: %s, Requested: %s", balance, amount);
            
            balance -= amount;
        }
        
        public double getBalance() {
            return balance;
        }
    }
}