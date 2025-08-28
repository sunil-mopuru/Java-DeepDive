/**
 * Guava Functional Programming Examples - Advanced Functional Utilities
 * 
 * This class demonstrates Guava's functional programming capabilities including
 * Functions, Predicates, Optional, and functional transformations.
 */

import com.google.common.base.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.stream.Collectors;

public class FunctionalExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava Functional Programming Examples ===");
        
        // Basic functional concepts
        demonstratePredicates();
        demonstrateFunctions();
        demonstrateOptional();
        
        // Advanced functional patterns
        demonstrateFunctionalTransformations();
        demonstrateFunctionalComposition();
        
        // Real-world functional programming
        demonstrateDataProcessingPipeline();
        demonstrateBusinessRules();
    }
    
    /**
     * Demonstrates Predicates for filtering and validation
     */
    public static void demonstratePredicates() {
        System.out.println("\n1. Predicates for Filtering and Validation");
        System.out.println("===========================================");
        
        List<String> words = Arrays.asList(
            "Hello", "World", "Java", "Guava", "Programming", 
            "Functional", "Code", "Example", "", null
        );
        
        System.out.println("Original words: " + words);
        
        // Basic predicates
        Predicate<String> isNotNull = Predicates.notNull();
        Predicate<String> isNotEmpty = s -> s != null && !s.isEmpty();
        Predicate<String> isLongWord = s -> s != null && s.length() > 5;
        Predicate<String> startsWithJ = s -> s != null && s.startsWith("J");
        
        // Apply predicates individually
        System.out.println("\nFiltering with individual predicates:");
        System.out.println("Not null: " + Lists.newArrayList(Collections2.filter(words, isNotNull)));
        System.out.println("Not empty: " + Lists.newArrayList(Collections2.filter(words, isNotEmpty)));
        System.out.println("Long words: " + Lists.newArrayList(Collections2.filter(words, isLongWord)));
        System.out.println("Starts with 'J': " + Lists.newArrayList(Collections2.filter(words, startsWithJ)));
        
        // Combine predicates
        System.out.println("\nCombining predicates:");
        
        // AND combination
        Predicate<String> longAndStartsWithJ = Predicates.and(isLongWord, startsWithJ);
        Collection<String> longJWords = Collections2.filter(words, longAndStartsWithJ);
        System.out.println("Long AND starts with 'J': " + longJWords);
        
        // OR combination
        Predicate<String> shortOrStartsWithJ = Predicates.or(
            s -> s != null && s.length() <= 4,
            startsWithJ
        );
        Collection<String> shortOrJ = Collections2.filter(words, shortOrStartsWithJ);
        System.out.println("Short OR starts with 'J': " + shortOrJ);
        
        // NOT combination
        Predicate<String> notLong = Predicates.not(isLongWord);
        Collection<String> shortWords = Collections2.filter(words, Predicates.and(isNotNull, notLong));
        System.out.println("Not long (short words): " + shortWords);
        
        // Demonstrate user validation
        demonstrateUserValidation();
    }
    
    /**
     * Demonstrates user validation using predicates
     */
    public static void demonstrateUserValidation() {
        System.out.println("\nUser Validation Example:");
        
        List<User> users = Arrays.asList(
            new User("Alice", "alice@example.com", 25),
            new User("", "bob@example.com", 30),
            new User("Charlie", "invalid-email", 20),
            new User("Diana", "diana@example.com", 17),
            new User("Eve", "eve@example.com", 35)
        );
        
        // Define validation predicates
        Predicate<User> hasValidName = user -> 
            user.getName() != null && !user.getName().trim().isEmpty();
        
        Predicate<User> hasValidEmail = user -> 
            user.getEmail() != null && user.getEmail().contains("@") && user.getEmail().contains(".");
        
        Predicate<User> isAdult = user -> user.getAge() >= 18;
        
        Predicate<User> isValidUser = Predicates.and(hasValidName, hasValidEmail, isAdult);
        
        System.out.println("All users: " + users.size());
        
        Collection<User> validUsers = Collections2.filter(users, isValidUser);
        System.out.println("Valid users: " + validUsers);
        
        Collection<User> invalidUsers = Collections2.filter(users, Predicates.not(isValidUser));
        System.out.println("Invalid users: " + invalidUsers);
    }
    
    /**
     * Demonstrates Functions for transformations
     */
    public static void demonstrateFunctions() {
        System.out.println("\n2. Functions for Transformations");
        System.out.println("=================================");
        
        List<String> names = Arrays.asList("alice", "bob", "charlie", "diana");
        System.out.println("Original names: " + names);
        
        // Basic functions
        Function<String, String> toUpperCase = String::toUpperCase;
        Function<String, Integer> getLength = String::length;
        Function<String, String> addGreeting = name -> "Hello, " + name + "!";
        
        // Apply functions
        Collection<String> upperNames = Collections2.transform(names, toUpperCase);
        System.out.println("Uppercase names: " + upperNames);
        
        Collection<Integer> nameLengths = Collections2.transform(names, getLength);
        System.out.println("Name lengths: " + nameLengths);
        
        Collection<String> greetings = Collections2.transform(names, addGreeting);
        System.out.println("Greetings: " + greetings);
        
        // Function composition
        System.out.println("\nFunction Composition:");
        
        Function<String, String> capitalizeAndGreet = Functions.compose(addGreeting, toUpperCase);
        Collection<String> capitalGreetings = Collections2.transform(names, capitalizeAndGreet);
        System.out.println("Capitalized greetings: " + capitalGreetings);
        
        // Chain multiple transformations
        Function<String, String> processName = Functions.compose(
            Functions.compose(addGreeting, toUpperCase),
            name -> name.trim()
        );
        
        List<String> messyNames = Arrays.asList(" alice ", " bob ", " charlie ");
        Collection<String> processedNames = Collections2.transform(messyNames, processName);
        System.out.println("Processed messy names: " + processedNames);
        
        // Demonstrate map transformations
        demonstrateMapTransformations();
    }
    
    /**
     * Demonstrates map transformations using functions
     */
    public static void demonstrateMapTransformations() {
        System.out.println("\nMap Transformations:");
        
        Map<String, Integer> ages = ImmutableMap.of(
            "Alice", 25,
            "Bob", 30,
            "Charlie", 35,
            "Diana", 28
        );
        
        System.out.println("Original ages: " + ages);
        
        // Transform values
        Map<String, String> ageDescriptions = Maps.transformValues(ages, 
            age -> age + " years old");
        System.out.println("Age descriptions: " + ageDescriptions);
        
        // Transform keys
        Function<String, String> keyTransformer = name -> name.toLowerCase();
        Map<String, Integer> lowerCaseKeys = Maps.transformEntries(ages,
            (key, value) -> key.toLowerCase());
        System.out.println("Lowercase keys: " + lowerCaseKeys);
        
        // Complex transformations
        Map<String, String> statusMap = Maps.transformEntries(ages, (name, age) -> {
            if (age < 25) return "Young";
            if (age < 30) return "Adult";
            return "Mature";
        });
        System.out.println("Age status: " + statusMap);
    }
    
    /**
     * Demonstrates Guava's Optional for null safety
     */
    public static void demonstrateOptional() {
        System.out.println("\n3. Optional for Null Safety");
        System.out.println("============================");
        
        // Creating Optional values
        System.out.println("Creating Optional values:");
        
        Optional<String> present = Optional.of("Hello");
        Optional<String> absent = Optional.absent();
        Optional<String> fromNullable = Optional.fromNullable(getNullableString(true));
        Optional<String> fromNull = Optional.fromNullable(getNullableString(false));
        
        System.out.println("Present: " + present);
        System.out.println("Absent: " + absent);
        System.out.println("From nullable (non-null): " + fromNullable);
        System.out.println("From nullable (null): " + fromNull);
        
        // Using Optional values
        System.out.println("\nUsing Optional values:");
        
        // Check presence
        System.out.println("Present.isPresent(): " + present.isPresent());
        System.out.println("Absent.isPresent(): " + absent.isPresent());
        
        // Get values with defaults
        String value1 = present.or("default");
        String value2 = absent.or("default");
        System.out.println("Present with default: " + value1);
        System.out.println("Absent with default: " + value2);
        
        // Transform values
        Optional<Integer> length = present.transform(String::length);
        Optional<String> upper = present.transform(String::toUpperCase);
        System.out.println("Transformed to length: " + length);
        System.out.println("Transformed to upper: " + upper);
        
        // Chain transformations safely
        Optional<String> result = Optional.of("  Hello World  ")
            .transform(String::trim)
            .transform(String::toLowerCase)
            .transform(s -> s.replace(" ", "_"));
        System.out.println("Chained transformations: " + result);
        
        // Demonstrate safe navigation
        demonstrateSafeNavigation();
    }
    
    /**
     * Demonstrates safe navigation using Optional
     */
    public static void demonstrateSafeNavigation() {
        System.out.println("\nSafe Navigation Example:");
        
        // Mock data with potential nulls
        Company company1 = new Company("TechCorp", 
            new Address("123 Tech St", "Silicon Valley"));
        Company company2 = new Company("StartupInc", null);
        Company company3 = null;
        
        List<Company> companies = Arrays.asList(company1, company2, company3);
        
        for (int i = 0; i < companies.size(); i++) {
            Company company = companies.get(i);
            
            // Safe navigation using Optional
            String city = Optional.fromNullable(company)
                .transform(Company::getAddress)
                .transform(Address::getCity)
                .or("Unknown City");
            
            String companyName = Optional.fromNullable(company)
                .transform(Company::getName)
                .or("Unknown Company");
            
            System.out.println("Company " + (i + 1) + ": " + companyName + " in " + city);
        }
    }
    
    /**
     * Demonstrates functional transformations for data processing
     */
    public static void demonstrateFunctionalTransformations() {
        System.out.println("\n4. Functional Data Transformations");
        System.out.println("==================================");
        
        List<Product> products = Arrays.asList(
            new Product("Laptop", "Electronics", 999.99, 50),
            new Product("Book", "Education", 29.99, 100),
            new Product("Coffee", "Food", 4.99, 200),
            new Product("Phone", "Electronics", 699.99, 75),
            new Product("Desk", "Furniture", 299.99, 25)
        );
        
        System.out.println("All products: " + products.size());
        
        // Filter expensive products
        Predicate<Product> isExpensive = product -> product.getPrice() > 100;
        Collection<Product> expensiveProducts = Collections2.filter(products, isExpensive);
        System.out.println("Expensive products: " + expensiveProducts.size());
        
        // Transform to product names
        Function<Product, String> getName = Product::getName;
        Collection<String> productNames = Collections2.transform(expensiveProducts, getName);
        System.out.println("Expensive product names: " + productNames);
        
        // Calculate total value by category
        System.out.println("\nValue by category:");
        Multimap<String, Product> productsByCategory = Multimaps.index(products, Product::getCategory);
        
        for (String category : productsByCategory.keySet()) {
            Collection<Product> categoryProducts = productsByCategory.get(category);
            double totalValue = categoryProducts.stream()
                .mapToDouble(product -> product.getPrice() * product.getStock())
                .sum();
            System.out.println("  " + category + ": $" + String.format("%.2f", totalValue));
        }
        
        // Complex filtering and transformation
        System.out.println("\nLow stock, high value items:");
        Collection<String> criticalItems = Collections2.transform(
            Collections2.filter(products, product -> 
                product.getStock() < 30 && product.getPrice() > 200),
            product -> product.getName() + " (Stock: " + product.getStock() + ")"
        );
        System.out.println(criticalItems);
    }
    
    /**
     * Demonstrates function composition for complex operations
     */
    public static void demonstrateFunctionalComposition() {
        System.out.println("\n5. Functional Composition");
        System.out.println("=========================");
        
        List<String> rawData = Arrays.asList(
            "  John Doe  ", " jane.smith ", "  BOB_JONES  ", " alice.wonderland ", "  "
        );
        
        System.out.println("Raw data: " + rawData);
        
        // Create individual transformation functions
        Function<String, String> trim = String::trim;
        Function<String, String> toLowerCase = String::toLowerCase;
        Function<String, String> replaceUnderscores = s -> s.replace("_", ".");
        Function<String, String> capitalizeFirst = s -> {
            if (s.isEmpty()) return s;
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        };
        
        // Compose functions for data cleaning pipeline
        Function<String, String> cleanName = Functions.compose(
            Functions.compose(capitalizeFirst, replaceUnderscores),
            Functions.compose(toLowerCase, trim)
        );
        
        // Apply the composed function
        Predicate<String> isNotEmpty = s -> !s.trim().isEmpty();
        Collection<String> cleanedNames = Collections2.transform(
            Collections2.filter(rawData, isNotEmpty),
            cleanName
        );
        
        System.out.println("Cleaned names: " + cleanedNames);
        
        // Demonstrate function chaining for validation and transformation
        demonstrateValidationPipeline();
    }
    
    /**
     * Demonstrates validation and transformation pipeline
     */
    public static void demonstrateValidationPipeline() {
        System.out.println("\nValidation and Transformation Pipeline:");
        
        List<String> emails = Arrays.asList(
            "  alice@example.com  ", "bob@test", "charlie@domain.com", 
            "", "diana@company.org", "invalid.email", "eve@site.co.uk"
        );
        
        System.out.println("Raw emails: " + emails);
        
        // Validation predicates
        Predicate<String> isNotEmpty = s -> s != null && !s.trim().isEmpty();
        Predicate<String> hasAtSymbol = s -> s.contains("@");
        Predicate<String> hasDot = s -> s.contains(".");
        Predicate<String> isValidEmail = Predicates.and(isNotEmpty, hasAtSymbol, hasDot);
        
        // Transformation function
        Function<String, String> normalizeEmail = email -> email.trim().toLowerCase();
        
        // Apply pipeline
        Collection<String> validEmails = Collections2.transform(
            Collections2.filter(emails, isValidEmail),
            normalizeEmail
        );
        
        System.out.println("Valid normalized emails: " + validEmails);
    }
    
    /**
     * Demonstrates data processing pipeline using functional programming
     */
    public static void demonstrateDataProcessingPipeline() {
        System.out.println("\n6. Data Processing Pipeline");
        System.out.println("============================");
        
        // Sample log data
        List<LogEntry> logEntries = Arrays.asList(
            new LogEntry("ERROR", "Database connection failed", "2023-12-01 10:30:00"),
            new LogEntry("INFO", "User logged in", "2023-12-01 10:31:00"),
            new LogEntry("WARN", "High memory usage detected", "2023-12-01 10:32:00"),
            new LogEntry("ERROR", "Payment processing failed", "2023-12-01 10:33:00"),
            new LogEntry("INFO", "Cache cleared", "2023-12-01 10:34:00"),
            new LogEntry("DEBUG", "SQL query executed", "2023-12-01 10:35:00")
        );
        
        System.out.println("Total log entries: " + logEntries.size());
        
        // Filter critical entries (ERROR and WARN)
        Predicate<LogEntry> isCritical = entry -> 
            "ERROR".equals(entry.getLevel()) || "WARN".equals(entry.getLevel());
        
        Collection<LogEntry> criticalEntries = Collections2.filter(logEntries, isCritical);
        System.out.println("Critical entries: " + criticalEntries.size());
        
        // Transform to alerts
        Function<LogEntry, Alert> toAlert = entry -> new Alert(
            entry.getLevel(),
            "ALERT: " + entry.getMessage(),
            entry.getTimestamp()
        );
        
        Collection<Alert> alerts = Collections2.transform(criticalEntries, toAlert);
        System.out.println("Generated alerts:");
        alerts.forEach(System.out::println);
        
        // Group by severity
        System.out.println("\nAlerts by severity:");
        Multimap<String, Alert> alertsBySeverity = Multimaps.index(alerts, Alert::getSeverity);
        for (String severity : alertsBySeverity.keySet()) {
            System.out.println("  " + severity + ": " + alertsBySeverity.get(severity).size() + " alerts");
        }
    }
    
    /**
     * Demonstrates business rules using functional programming
     */
    public static void demonstrateBusinessRules() {
        System.out.println("\n7. Business Rules Engine");
        System.out.println("=========================");
        
        List<Customer> customers = Arrays.asList(
            new Customer("C001", "Alice Johnson", 25000, true, 2),
            new Customer("C002", "Bob Smith", 75000, false, 5),
            new Customer("C003", "Charlie Brown", 45000, true, 3),
            new Customer("C004", "Diana Prince", 120000, true, 8),
            new Customer("C005", "Eve Adams", 35000, false, 1)
        );
        
        System.out.println("Evaluating customers for premium status...");
        
        // Define business rules as predicates
        Predicate<Customer> highIncome = customer -> customer.getAnnualIncome() > 50000;
        Predicate<Customer> loyalCustomer = customer -> customer.getYearsWithCompany() >= 3;
        Predicate<Customer> hasPremiumAccount = Customer::isPremiumAccount;
        
        // Premium eligibility rule
        Predicate<Customer> isPremiumEligible = Predicates.and(
            highIncome,
            Predicates.or(loyalCustomer, hasPremiumAccount)
        );
        
        // Discount eligibility rule
        Predicate<Customer> isDiscountEligible = Predicates.or(
            customer -> customer.getYearsWithCompany() >= 5,
            Predicates.and(hasPremiumAccount, highIncome)
        );
        
        // Apply rules and generate results
        for (Customer customer : customers) {
            boolean premium = isPremiumEligible.apply(customer);
            boolean discount = isDiscountEligible.apply(customer);
            
            System.out.println(customer.getName() + ":");
            System.out.println("  Premium eligible: " + premium);
            System.out.println("  Discount eligible: " + discount);
            
            if (premium) {
                System.out.println("  → Upgrade to premium account");
            }
            if (discount) {
                System.out.println("  → Apply 10% discount");
            }
            System.out.println();
        }
    }
    
    // Helper method for Optional demonstration
    private static String getNullableString(boolean returnValue) {
        return returnValue ? "Valid String" : null;
    }
    
    // Supporting classes
    static class User {
        private final String name;
        private final String email;
        private final int age;
        
        public User(String name, String email, int age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }
        
        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return "User{name='" + name + "', email='" + email + "', age=" + age + "}";
        }
    }
    
    static class Company {
        private final String name;
        private final Address address;
        
        public Company(String name, Address address) {
            this.name = name;
            this.address = address;
        }
        
        public String getName() { return name; }
        public Address getAddress() { return address; }
    }
    
    static class Address {
        private final String street;
        private final String city;
        
        public Address(String street, String city) {
            this.street = street;
            this.city = city;
        }
        
        public String getStreet() { return street; }
        public String getCity() { return city; }
    }
    
    static class Product {
        private final String name;
        private final String category;
        private final double price;
        private final int stock;
        
        public Product(String name, String category, double price, int stock) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.stock = stock;
        }
        
        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }
        
        @Override
        public String toString() {
            return name + " (" + category + ") - $" + price;
        }
    }
    
    static class LogEntry {
        private final String level;
        private final String message;
        private final String timestamp;
        
        public LogEntry(String level, String message, String timestamp) {
            this.level = level;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public String getLevel() { return level; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }
    }
    
    static class Alert {
        private final String severity;
        private final String message;
        private final String timestamp;
        
        public Alert(String severity, String message, String timestamp) {
            this.severity = severity;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public String getSeverity() { return severity; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return "[" + severity + "] " + message + " at " + timestamp;
        }
    }
    
    static class Customer {
        private final String id;
        private final String name;
        private final double annualIncome;
        private final boolean premiumAccount;
        private final int yearsWithCompany;
        
        public Customer(String id, String name, double annualIncome, boolean premiumAccount, int yearsWithCompany) {
            this.id = id;
            this.name = name;
            this.annualIncome = annualIncome;
            this.premiumAccount = premiumAccount;
            this.yearsWithCompany = yearsWithCompany;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public double getAnnualIncome() { return annualIncome; }
        public boolean isPremiumAccount() { return premiumAccount; }
        public int getYearsWithCompany() { return yearsWithCompany; }
    }
}