/**
 * Optional Exercises - Beginner to Advanced
 * 
 * Practice exercises for mastering Java 8 Optional class for null-safe programming,
 * avoiding NullPointerException, and writing cleaner code.
 */

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class OptionalExercises {
    
    public static void main(String[] args) {
        System.out.println("=== Optional Exercises ===");
        
        basicOptionalExercises();
        optionalMethodsExercises();
        optionalChainingExercises();
        practicalOptionalExercises();
        advancedOptionalPatterns();
    }
    
    public static void basicOptionalExercises() {
        System.out.println("\n1. Basic Optional Operations");
        System.out.println("============================");
        
        // Exercise 1a: Creating Optional objects
        System.out.println("Exercise 1a: Creating Optional objects");
        Optional<String> emptyOptional = Optional.empty();
        Optional<String> nonEmptyOptional = Optional.of("Hello World");
        Optional<String> nullableOptional = Optional.ofNullable(getNullableValue());
        
        System.out.println("Empty Optional: " + emptyOptional);
        System.out.println("Non-empty Optional: " + nonEmptyOptional);
        System.out.println("Nullable Optional: " + nullableOptional);
        
        // Exercise 1b: Checking if Optional has value
        System.out.println("\nExercise 1b: Checking if Optional has value");
        System.out.println("Empty has value: " + emptyOptional.isPresent());
        System.out.println("Non-empty has value: " + nonEmptyOptional.isPresent());
        
        // Exercise 1c: Getting values safely
        System.out.println("\nExercise 1c: Getting values safely");
        System.out.println("Value with default: " + emptyOptional.orElse("Default Value"));
        System.out.println("Value with supplier: " + emptyOptional.orElseGet(() -> "Supplied Default"));
        
        // Exercise 1d: Exception handling with Optional
        System.out.println("\nExercise 1d: Exception handling with Optional");
        try {
            String value = nonEmptyOptional.orElseThrow(() -> new RuntimeException("No value present"));
            System.out.println("Retrieved value: " + value);
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
    
    public static void optionalMethodsExercises() {
        System.out.println("\n2. Optional Methods Exercises");
        System.out.println("=============================");
        
        // Exercise 2a: map() transformation
        System.out.println("Exercise 2a: map() transformation");
        Optional<String> name = Optional.of("john doe");
        Optional<String> upperName = name.map(String::toUpperCase);
        Optional<Integer> nameLength = name.map(String::length);
        
        System.out.println("Original: " + name.orElse("N/A"));
        System.out.println("Upper case: " + upperName.orElse("N/A"));
        System.out.println("Length: " + nameLength.orElse(0));
        
        // Exercise 2b: filter() with predicates
        System.out.println("\nExercise 2b: filter() with predicates");
        Optional<Integer> number = Optional.of(42);
        Optional<Integer> evenNumber = number.filter(n -> n % 2 == 0);
        Optional<Integer> largeNumber = number.filter(n -> n > 100);
        
        System.out.println("Original number: " + number.orElse(0));
        System.out.println("Even number: " + evenNumber.orElse(0));
        System.out.println("Large number: " + largeNumber.orElse(0) + " (empty if not > 100)");
        
        // Exercise 2c: flatMap() for nested Optional
        System.out.println("\nExercise 2c: flatMap() for nested Optional");
        Optional<Person> person = Optional.of(new Person("Alice", Optional.of(new Address("123 Main St"))));
        Optional<String> street = person.flatMap(Person::getAddress)
                                       .map(Address::getStreet);
        
        System.out.println("Person's street: " + street.orElse("No address"));
        
        // Exercise 2d: ifPresent() for side effects
        System.out.println("\nExercise 2d: ifPresent() for side effects");
        List<String> results = new ArrayList<>();
        Optional<String> value1 = Optional.of("Found Value");
        Optional<String> value2 = Optional.empty();
        
        value1.ifPresent(v -> results.add("Value1: " + v));
        value2.ifPresent(v -> results.add("Value2: " + v));
        
        System.out.println("Results collected: " + results);
    }
    
    public static void optionalChainingExercises() {
        System.out.println("\n3. Optional Chaining Exercises");
        System.out.println("==============================");
        
        // Exercise 3a: Service layer with Optional chaining
        System.out.println("Exercise 3a: Service layer with Optional chaining");
        UserService userService = new UserService();
        
        Long userId = 1L;
        String displayName = userService.findUser(userId)
            .filter(User::isActive)
            .map(User::getProfile)
            .filter(Objects::nonNull)
            .map(UserProfile::getDisplayName)
            .filter(name -> !name.trim().isEmpty())
            .orElse("Anonymous User");
        
        System.out.println("Display name for user " + userId + ": " + displayName);
        
        // Exercise 3b: Configuration chain
        System.out.println("\nExercise 3b: Configuration chain");
        Config config = new Config();
        String serverUrl = config.getServerConfig()
            .flatMap(ServerConfig::getUrl)
            .orElseGet(() -> {
                System.out.println("Using default server URL");
                return "http://localhost:8080";
            });
        
        System.out.println("Server URL: " + serverUrl);
        
        // Exercise 3c: Validation chain
        System.out.println("\nExercise 3c: Validation chain");
        String email = "user@example.com";
        Optional<String> validEmail = Optional.of(email)
            .filter(e -> e.contains("@"))
            .filter(e -> e.length() > 5)
            .filter(e -> !e.startsWith("@"))
            .map(String::toLowerCase);
        
        System.out.println("Valid email: " + validEmail.orElse("Invalid email format"));
    }
    
    public static void practicalOptionalExercises() {
        System.out.println("\n4. Practical Optional Exercises");
        System.out.println("===============================");
        
        // Exercise 4a: Database-like operations
        System.out.println("Exercise 4a: Database-like operations");
        Repository<User> userRepo = new Repository<>();
        userRepo.save(new User(1L, "alice", true, new UserProfile("Alice Smith")));
        userRepo.save(new User(2L, "bob", false, new UserProfile("Bob Jones")));
        
        // Find active user by username
        Optional<User> activeUser = userRepo.findByUsername("alice")
            .filter(User::isActive);
        
        activeUser.ifPresentOrElse(
            user -> System.out.println("Found active user: " + user.getProfile().getDisplayName()),
            () -> System.out.println("No active user found with that username")
        );
        
        // Exercise 4b: Cache-like operations with Optional
        System.out.println("\nExercise 4b: Cache-like operations");
        Cache<String, String> cache = new Cache<>();
        
        String cachedValue = cache.get("key1")
            .orElseGet(() -> {
                String computedValue = "Computed Value for key1";
                cache.put("key1", computedValue);
                System.out.println("Cache miss - computed value");
                return computedValue;
            });
        
        System.out.println("Retrieved value: " + cachedValue);
        
        // Second access should hit cache
        String cachedValue2 = cache.get("key1")
            .orElseGet(() -> {
                System.out.println("This shouldn't print - cache hit expected");
                return "Fallback";
            });
        
        System.out.println("Second retrieval: " + cachedValue2);
        
        // Exercise 4c: Optional in collections
        System.out.println("\nExercise 4c: Optional in collections");
        List<Optional<String>> optionalList = Arrays.asList(
            Optional.of("Apple"),
            Optional.empty(),
            Optional.of("Banana"),
            Optional.empty(),
            Optional.of("Cherry")
        );
        
        List<String> presentValues = optionalList.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        
        System.out.println("Present values: " + presentValues);
        
        // Better approach using flatMap
        List<String> presentValues2 = optionalList.stream()
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
        
        System.out.println("Present values (flatMap): " + presentValues2);
    }
    
    public static void advancedOptionalPatterns() {
        System.out.println("\n5. Advanced Optional Patterns");
        System.out.println("=============================");
        
        // Exercise 5a: Optional with multiple conditions
        System.out.println("Exercise 5a: Optional with multiple conditions");
        Optional<User> user = Optional.of(new User(1L, "alice", true, new UserProfile("Alice Smith")));
        
        boolean canPerformAction = user
            .filter(User::isActive)
            .map(User::getProfile)
            .filter(profile -> profile.getDisplayName().length() > 5)
            .isPresent();
        
        System.out.println("Can perform action: " + canPerformAction);
        
        // Exercise 5b: Converting exceptions to Optional
        System.out.println("\nExercise 5b: Converting exceptions to Optional");
        Function<String, Optional<Integer>> safeParseInt = str -> {
            try {
                return Optional.of(Integer.parseInt(str));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        };
        
        List<String> numberStrings = Arrays.asList("123", "abc", "456", "xyz", "789");
        List<Integer> parsedNumbers = numberStrings.stream()
            .map(safeParseInt)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        
        System.out.println("Successfully parsed numbers: " + parsedNumbers);
        
        // Exercise 5c: Optional combining with Stream reduce
        System.out.println("\nExercise 5c: Optional combining with Stream reduce");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        Optional<Integer> product = numbers.stream()
            .reduce((a, b) -> a * b);
        
        product.ifPresentOrElse(
            p -> System.out.println("Product of all numbers: " + p),
            () -> System.out.println("Empty list - no product")
        );
        
        // Exercise 5d: Optional as return type best practices
        System.out.println("\nExercise 5d: Optional return type best practices");
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        Optional<String> longestName = findLongestName(names);
        longestName.ifPresentOrElse(
            name -> System.out.println("Longest name: " + name),
            () -> System.out.println("No names provided")
        );
        
        Optional<String> nameStartingWithZ = findFirstNameStartingWith(names, "Z");
        nameStartingWithZ.ifPresentOrElse(
            name -> System.out.println("Name starting with Z: " + name),
            () -> System.out.println("No name starting with Z found")
        );
    }
    
    // Helper methods and classes
    
    private static String getNullableValue() {
        return Math.random() > 0.5 ? "Random Value" : null;
    }
    
    private static Optional<String> findLongestName(List<String> names) {
        return names.stream()
            .max(Comparator.comparing(String::length));
    }
    
    private static Optional<String> findFirstNameStartingWith(List<String> names, String prefix) {
        return names.stream()
            .filter(name -> name.startsWith(prefix))
            .findFirst();
    }
    
    // Supporting classes
    static class Person {
        private String name;
        private Optional<Address> address;
        
        public Person(String name, Optional<Address> address) {
            this.name = name;
            this.address = address;
        }
        
        public Optional<Address> getAddress() { return address; }
    }
    
    static class Address {
        private String street;
        
        public Address(String street) { this.street = street; }
        public String getStreet() { return street; }
    }
    
    static class User {
        private Long id;
        private String username;
        private boolean active;
        private UserProfile profile;
        
        public User(Long id, String username, boolean active, UserProfile profile) {
            this.id = id;
            this.username = username;
            this.active = active;
            this.profile = profile;
        }
        
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public boolean isActive() { return active; }
        public UserProfile getProfile() { return profile; }
    }
    
    static class UserProfile {
        private String displayName;
        
        public UserProfile(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }
    
    static class UserService {
        private Repository<User> userRepository = new Repository<>();
        
        public UserService() {
            // Initialize with some test data
            userRepository.save(new User(1L, "alice", true, new UserProfile("Alice Smith")));
            userRepository.save(new User(2L, "bob", false, new UserProfile("Bob Jones")));
        }
        
        public Optional<User> findUser(Long id) {
            return userRepository.findById(id);
        }
    }
    
    static class Repository<T extends User> {
        private Map<Long, T> data = new HashMap<>();
        
        public void save(T entity) {
            data.put(entity.getId(), entity);
        }
        
        public Optional<T> findById(Long id) {
            return Optional.ofNullable(data.get(id));
        }
        
        public Optional<T> findByUsername(String username) {
            return data.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
        }
    }
    
    static class Config {
        public Optional<ServerConfig> getServerConfig() {
            // Simulate configuration that might not be present
            return Math.random() > 0.3 ? 
                Optional.of(new ServerConfig()) : 
                Optional.empty();
        }
    }
    
    static class ServerConfig {
        public Optional<String> getUrl() {
            return Math.random() > 0.5 ? 
                Optional.of("https://api.example.com") : 
                Optional.empty();
        }
    }
    
    static class Cache<K, V> {
        private Map<K, V> cache = new HashMap<>();
        
        public Optional<V> get(K key) {
            return Optional.ofNullable(cache.get(key));
        }
        
        public void put(K key, V value) {
            cache.put(key, value);
        }
    }
}