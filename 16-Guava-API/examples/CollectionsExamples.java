/**
 * Guava Collections Examples - Advanced Collection Utilities
 * 
 * This class demonstrates Guava's advanced collection utilities including
 * Multimap, BiMap, Table, Range, and various collection transformations.
 */

import com.google.common.collect.*;
import com.google.common.base.*;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionsExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Guava Collections Examples ===");
        
        // Advanced collection types
        demonstrateMultimap();
        demonstrateBiMap();
        demonstrateTable();
        demonstrateRange();
        
        // Collection transformations
        demonstrateCollectionTransformations();
        
        // Iterables and Iterators
        demonstrateIterables();
        
        // Comparison and Ordering
        demonstrateOrdering();
        
        // Real-world examples
        demonstrateRealWorldScenarios();
    }
    
    /**
     * Demonstrates Multimap - a collection that maps keys to multiple values
     */
    public static void demonstrateMultimap() {
        System.out.println("\n1. Multimap Examples");
        System.out.println("====================");
        
        // Creating different types of Multimaps
        System.out.println("Creating Multimaps:");
        
        // ListMultimap - allows duplicate values
        ListMultimap<String, String> listMultimap = ArrayListMultimap.create();
        listMultimap.put("fruits", "apple");
        listMultimap.put("fruits", "banana");
        listMultimap.put("fruits", "apple"); // duplicate allowed
        listMultimap.put("vegetables", "carrot");
        listMultimap.put("vegetables", "broccoli");
        
        System.out.println("ListMultimap: " + listMultimap);
        System.out.println("Fruits: " + listMultimap.get("fruits"));
        
        // SetMultimap - no duplicate values
        SetMultimap<String, String> setMultimap = HashMultimap.create();
        setMultimap.put("colors", "red");
        setMultimap.put("colors", "blue");
        setMultimap.put("colors", "red"); // duplicate ignored
        setMultimap.put("shapes", "circle");
        setMultimap.put("shapes", "square");
        
        System.out.println("SetMultimap: " + setMultimap);
        System.out.println("Colors: " + setMultimap.get("colors"));
        
        // Practical example: Student courses
        demonstrateStudentCourses();
        
        // Multimap operations
        System.out.println("\nMultimap Operations:");
        System.out.println("All keys: " + listMultimap.keySet());
        System.out.println("All values: " + listMultimap.values());
        System.out.println("Size: " + listMultimap.size());
        System.out.println("Contains key 'fruits': " + listMultimap.containsKey("fruits"));
        System.out.println("Contains entry 'fruits'->'apple': " + 
                         listMultimap.containsEntry("fruits", "apple"));
        
        // Remove operations
        listMultimap.remove("fruits", "apple"); // Remove one occurrence
        System.out.println("After removing one apple: " + listMultimap.get("fruits"));
        
        listMultimap.removeAll("vegetables"); // Remove all vegetables
        System.out.println("After removing all vegetables: " + listMultimap);
    }
    
    /**
     * Demonstrates student courses using Multimap
     */
    public static void demonstrateStudentCourses() {
        System.out.println("\nStudent Courses Example:");
        
        ListMultimap<String, String> studentCourses = ArrayListMultimap.create();
        
        // Add courses for students
        studentCourses.put("Alice", "Mathematics");
        studentCourses.put("Alice", "Physics");
        studentCourses.put("Alice", "Chemistry");
        studentCourses.put("Bob", "Computer Science");
        studentCourses.put("Bob", "Mathematics");
        studentCourses.put("Charlie", "Literature");
        studentCourses.put("Charlie", "History");
        
        // Display student courses
        for (String student : studentCourses.keySet()) {
            System.out.println(student + " is enrolled in: " + 
                             studentCourses.get(student));
        }
        
        // Find students taking Mathematics
        System.out.println("\nStudents taking Mathematics:");
        for (Map.Entry<String, String> entry : studentCourses.entries()) {
            if (entry.getValue().equals("Mathematics")) {
                System.out.println("  " + entry.getKey());
            }
        }
    }
    
    /**
     * Demonstrates BiMap - bidirectional map
     */
    public static void demonstrateBiMap() {
        System.out.println("\n2. BiMap Examples");
        System.out.println("=================");
        
        // Creating BiMap
        BiMap<String, Integer> userIds = HashBiMap.create();
        userIds.put("Alice", 1001);
        userIds.put("Bob", 1002);
        userIds.put("Charlie", 1003);
        
        System.out.println("BiMap: " + userIds);
        
        // Forward lookup (key to value)
        Integer aliceId = userIds.get("Alice");
        System.out.println("Alice's ID: " + aliceId);
        
        // Reverse lookup (value to key)
        BiMap<Integer, String> idToUser = userIds.inverse();
        String userWith1002 = idToUser.get(1002);
        System.out.println("User with ID 1002: " + userWith1002);
        
        // Demonstrate uniqueness constraint
        try {
            userIds.put("David", 1001); // This will fail - value already exists
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Force put (removes existing mapping)
        userIds.forcePut("David", 1001);
        System.out.println("After force put: " + userIds);
        System.out.println("Inverse after force put: " + userIds.inverse());
        
        // Real-world example: Country codes
        demonstrateCountryCodes();
    }
    
    /**
     * Demonstrates country codes using BiMap
     */
    public static void demonstrateCountryCodes() {
        System.out.println("\nCountry Codes Example:");
        
        BiMap<String, String> countryCodes = HashBiMap.create();
        countryCodes.put("United States", "US");
        countryCodes.put("United Kingdom", "UK");
        countryCodes.put("Germany", "DE");
        countryCodes.put("France", "FR");
        countryCodes.put("Japan", "JP");
        
        // Lookup by country name
        System.out.println("Germany code: " + countryCodes.get("Germany"));
        
        // Lookup by code
        BiMap<String, String> codeToCountry = countryCodes.inverse();
        System.out.println("JP is: " + codeToCountry.get("JP"));
        
        // Display all mappings
        System.out.println("All country mappings:");
        for (Map.Entry<String, String> entry : countryCodes.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }
    
    /**
     * Demonstrates Table - two-dimensional map
     */
    public static void demonstrateTable() {
        System.out.println("\n3. Table Examples");
        System.out.println("=================");
        
        // Creating Table for sales data
        Table<String, String, Double> salesTable = HashBasedTable.create();
        
        // Add sales data (Salesperson, Product, Amount)
        salesTable.put("Alice", "Laptop", 1200.00);
        salesTable.put("Alice", "Mouse", 25.00);
        salesTable.put("Alice", "Keyboard", 80.00);
        salesTable.put("Bob", "Laptop", 1100.00);
        salesTable.put("Bob", "Monitor", 300.00);
        salesTable.put("Charlie", "Mouse", 30.00);
        salesTable.put("Charlie", "Keyboard", 90.00);
        
        System.out.println("Sales Table: " + salesTable);
        
        // Access individual cell
        Double aliceLaptopSale = salesTable.get("Alice", "Laptop");
        System.out.println("Alice's laptop sale: $" + aliceLaptopSale);
        
        // Get all sales by Alice
        Map<String, Double> aliceSales = salesTable.row("Alice");
        System.out.println("Alice's all sales: " + aliceSales);
        
        // Get all laptop sales
        Map<String, Double> laptopSales = salesTable.column("Laptop");
        System.out.println("All laptop sales: " + laptopSales);
        
        // Calculate totals
        System.out.println("\nSales Analysis:");
        
        // Total sales per person
        for (String salesperson : salesTable.rowKeySet()) {
            double totalSales = salesTable.row(salesperson).values()
                                         .stream()
                                         .mapToDouble(Double::doubleValue)
                                         .sum();
            System.out.println(salesperson + " total sales: $" + totalSales);
        }
        
        // Total sales per product
        for (String product : salesTable.columnKeySet()) {
            double totalProductSales = salesTable.column(product).values()
                                                 .stream()
                                                 .mapToDouble(Double::doubleValue)
                                                 .sum();
            System.out.println(product + " total sales: $" + totalProductSales);
        }
        
        // Demonstrate matrix-like operations
        demonstrateTableAsMatrix();
    }
    
    /**
     * Demonstrates Table used as a matrix
     */
    public static void demonstrateTableAsMatrix() {
        System.out.println("\nTable as Matrix Example:");
        
        // Distance matrix between cities
        Table<String, String, Integer> distanceMatrix = HashBasedTable.create();
        
        // Add distances (symmetric matrix)
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston"};
        
        // Add some sample distances
        distanceMatrix.put("New York", "Los Angeles", 2445);
        distanceMatrix.put("Los Angeles", "New York", 2445);
        distanceMatrix.put("New York", "Chicago", 790);
        distanceMatrix.put("Chicago", "New York", 790);
        distanceMatrix.put("Chicago", "Houston", 940);
        distanceMatrix.put("Houston", "Chicago", 940);
        distanceMatrix.put("Los Angeles", "Houston", 1374);
        distanceMatrix.put("Houston", "Los Angeles", 1374);
        
        // Same city distances
        for (String city : cities) {
            distanceMatrix.put(city, city, 0);
        }
        
        // Display distance matrix
        System.out.println("Distance Matrix (miles):");
        System.out.printf("%-12s", "");
        for (String city : cities) {
            System.out.printf("%-12s", city.substring(0, Math.min(city.length(), 8)));
        }
        System.out.println();
        
        for (String fromCity : cities) {
            System.out.printf("%-12s", fromCity.substring(0, Math.min(fromCity.length(), 8)));
            for (String toCity : cities) {
                Integer distance = distanceMatrix.get(fromCity, toCity);
                System.out.printf("%-12s", distance != null ? distance : "-");
            }
            System.out.println();
        }
    }
    
    /**
     * Demonstrates Range - continuous span of comparable values
     */
    public static void demonstrateRange() {
        System.out.println("\n4. Range Examples");
        System.out.println("=================");
        
        // Creating different types of ranges
        System.out.println("Creating Ranges:");
        
        Range<Integer> closedRange = Range.closed(1, 10);     // [1, 10]
        Range<Integer> openRange = Range.open(1, 10);         // (1, 10)
        Range<Integer> closedOpenRange = Range.closedOpen(1, 10); // [1, 10)
        Range<Integer> openClosedRange = Range.openClosed(1, 10); // (1, 10]
        
        System.out.println("Closed range [1, 10]: " + closedRange);
        System.out.println("Open range (1, 10): " + openRange);
        System.out.println("Closed-open [1, 10): " + closedOpenRange);
        System.out.println("Open-closed (1, 10]: " + openClosedRange);
        
        // Unbounded ranges
        Range<Integer> atLeast5 = Range.atLeast(5);       // [5, +∞)
        Range<Integer> lessThan10 = Range.lessThan(10);   // (-∞, 10)
        Range<Integer> all = Range.all();                 // (-∞, +∞)
        
        System.out.println("At least 5: " + atLeast5);
        System.out.println("Less than 10: " + lessThan10);
        
        // Range operations
        System.out.println("\nRange Operations:");
        
        System.out.println("Contains 5: " + closedRange.contains(5));
        System.out.println("Contains 10: " + openRange.contains(10));
        System.out.println("Contains all [2,8]: " + closedRange.encloses(Range.closed(2, 8)));
        
        // Range intersections
        Range<Integer> range1 = Range.closed(1, 8);
        Range<Integer> range2 = Range.closed(5, 12);
        
        if (range1.isConnected(range2)) {
            Range<Integer> intersection = range1.intersection(range2);
            System.out.println("Intersection of " + range1 + " and " + range2 + ": " + intersection);
        }
        
        // Range span
        Range<Integer> span = range1.span(range2);
        System.out.println("Span of " + range1 + " and " + range2 + ": " + span);
        
        // Practical examples
        demonstrateAgeRanges();
        demonstratePriceRanges();
    }
    
    /**
     * Demonstrates age ranges for different categories
     */
    public static void demonstrateAgeRanges() {
        System.out.println("\nAge Categories Example:");
        
        Range<Integer> child = Range.closedOpen(0, 13);
        Range<Integer> teenager = Range.closedOpen(13, 20);
        Range<Integer> adult = Range.closedOpen(20, 65);
        Range<Integer> senior = Range.atLeast(65);
        
        int[] testAges = {5, 16, 25, 70, 12, 19, 64, 65};
        
        for (int age : testAges) {
            String category;
            if (child.contains(age)) {
                category = "Child";
            } else if (teenager.contains(age)) {
                category = "Teenager";
            } else if (adult.contains(age)) {
                category = "Adult";
            } else if (senior.contains(age)) {
                category = "Senior";
            } else {
                category = "Unknown";
            }
            System.out.println("Age " + age + " -> " + category);
        }
    }
    
    /**
     * Demonstrates price ranges for product categories
     */
    public static void demonstratePriceRanges() {
        System.out.println("\nPrice Range Filtering:");
        
        List<Product> products = Arrays.asList(
            new Product("Budget Phone", 199.99),
            new Product("Mid-range Phone", 599.99),
            new Product("Premium Phone", 1099.99),
            new Product("Basic Laptop", 399.99),
            new Product("Gaming Laptop", 1599.99),
            new Product("Tablet", 299.99)
        );
        
        Range<Double> budgetRange = Range.lessThan(500.0);
        Range<Double> midRange = Range.closedOpen(500.0, 1000.0);
        Range<Double> premiumRange = Range.atLeast(1000.0);
        
        System.out.println("Budget products (<$500):");
        products.stream()
               .filter(p -> budgetRange.contains(p.getPrice()))
               .forEach(p -> System.out.println("  " + p));
        
        System.out.println("Mid-range products ([$500, $1000)):");
        products.stream()
               .filter(p -> midRange.contains(p.getPrice()))
               .forEach(p -> System.out.println("  " + p));
        
        System.out.println("Premium products ($1000+):");
        products.stream()
               .filter(p -> premiumRange.contains(p.getPrice()))
               .forEach(p -> System.out.println("  " + p));
    }
    
    /**
     * Demonstrates collection transformations
     */
    public static void demonstrateCollectionTransformations() {
        System.out.println("\n5. Collection Transformations");
        System.out.println("=============================");
        
        List<String> words = Arrays.asList("hello", "world", "guava", "collections");
        
        // Filter collections
        System.out.println("Original words: " + words);
        
        Predicate<String> longWords = word -> word.length() > 5;
        Collection<String> filteredWords = Collections2.filter(words, longWords);
        System.out.println("Long words: " + filteredWords);
        
        // Transform collections
        Function<String, String> capitalize = word -> 
            word.substring(0, 1).toUpperCase() + word.substring(1);
        
        Collection<String> capitalizedWords = Collections2.transform(words, capitalize);
        System.out.println("Capitalized: " + capitalizedWords);
        
        // Chain transformations
        Collection<String> result = Collections2.transform(
            Collections2.filter(words, longWords),
            capitalize
        );
        System.out.println("Long and capitalized: " + result);
        
        // Transform maps
        demonstrateMapTransformations();
    }
    
    /**
     * Demonstrates map transformations
     */
    public static void demonstrateMapTransformations() {
        System.out.println("\nMap Transformations:");
        
        Map<String, Integer> ages = ImmutableMap.of(
            "Alice", 30,
            "Bob", 25,
            "Charlie", 35
        );
        
        System.out.println("Original ages: " + ages);
        
        // Transform values
        Map<String, String> ageDescriptions = Maps.transformValues(ages, 
            age -> age + " years old");
        System.out.println("Age descriptions: " + ageDescriptions);
        
        // Transform keys
        Map<String, Integer> upperKeys = Maps.transformEntries(ages,
            (key, value) -> key.toUpperCase());
        System.out.println("Upper case keys: " + upperKeys);
        
        // Filter maps
        Map<String, Integer> youngPeople = Maps.filterValues(ages, age -> age < 30);
        System.out.println("Young people: " + youngPeople);
        
        Map<String, Integer> shortNames = Maps.filterKeys(ages, name -> name.length() < 5);
        System.out.println("Short names: " + shortNames);
    }
    
    /**
     * Demonstrates Iterables utilities
     */
    public static void demonstrateIterables() {
        System.out.println("\n6. Iterables Utilities");
        System.out.println("======================");
        
        List<String> list1 = Arrays.asList("A", "B", "C");
        List<String> list2 = Arrays.asList("D", "E", "F");
        List<String> list3 = Arrays.asList("G", "H", "I");
        
        // Concatenate iterables
        Iterable<String> concatenated = Iterables.concat(list1, list2, list3);
        System.out.println("Concatenated: " + Lists.newArrayList(concatenated));
        
        // Get elements
        String first = Iterables.getFirst(concatenated, "DEFAULT");
        String last = Iterables.getLast(concatenated);
        System.out.println("First: " + first + ", Last: " + last);
        
        // Get by index
        String element = Iterables.get(concatenated, 4, "NOT_FOUND");
        System.out.println("Element at index 4: " + element);
        
        // Size and empty checks
        int size = Iterables.size(concatenated);
        boolean isEmpty = Iterables.isEmpty(concatenated);
        System.out.println("Size: " + size + ", Is empty: " + isEmpty);
        
        // Frequency
        List<String> withDuplicates = Arrays.asList("A", "B", "A", "C", "B", "A");
        int frequency = Iterables.frequency(withDuplicates, "A");
        System.out.println("Frequency of 'A': " + frequency);
        
        // Partition
        Iterable<List<String>> partitioned = Iterables.partition(withDuplicates, 2);
        System.out.println("Partitioned (size 2): " + Lists.newArrayList(partitioned));
        
        // Cycle (infinite iterator - be careful!)
        Iterable<String> cycled = Iterables.cycle("X", "Y", "Z");
        System.out.println("First 10 from cycle: " + 
                         Lists.newArrayList(Iterables.limit(cycled, 10)));
    }
    
    /**
     * Demonstrates Ordering and Comparison
     */
    public static void demonstrateOrdering() {
        System.out.println("\n7. Ordering and Comparison");
        System.out.println("==========================");
        
        List<String> names = Arrays.asList("Alice", "bob", "Charlie", "alice", "BOB");
        System.out.println("Original names: " + names);
        
        // Natural ordering
        Ordering<String> natural = Ordering.natural();
        System.out.println("Natural order: " + natural.sortedCopy(names));
        
        // Case-insensitive ordering
        Ordering<String> caseInsensitive = Ordering.from(String.CASE_INSENSITIVE_ORDER);
        System.out.println("Case insensitive: " + caseInsensitive.sortedCopy(names));
        
        // Reverse ordering
        System.out.println("Reversed: " + natural.reverse().sortedCopy(names));
        
        // Ordering by length
        Ordering<String> byLength = Ordering.natural().onResultOf(String::length);
        System.out.println("By length: " + byLength.sortedCopy(names));
        
        // Complex object ordering
        demonstratePersonOrdering();
    }
    
    /**
     * Demonstrates ordering complex objects
     */
    public static void demonstratePersonOrdering() {
        System.out.println("\nPerson Ordering:");
        
        List<Person> people = Arrays.asList(
            new Person("Alice", "Smith", 30),
            new Person("Bob", "Johnson", 25),
            new Person("Alice", "Johnson", 28),
            new Person("Charlie", "Brown", 35)
        );
        
        System.out.println("Original people: " + people);
        
        // Order by last name, then first name, then age
        Ordering<Person> byLastName = Ordering.natural().onResultOf(Person::getLastName);
        Ordering<Person> byFirstName = Ordering.natural().onResultOf(Person::getFirstName);
        Ordering<Person> byAge = Ordering.natural().onResultOf(Person::getAge);
        
        Ordering<Person> compound = byLastName.compound(byFirstName).compound(byAge);
        
        System.out.println("Ordered by last name, first name, age:");
        for (Person person : compound.sortedCopy(people)) {
            System.out.println("  " + person);
        }
        
        // Find min and max
        Person youngest = byAge.min(people);
        Person oldest = byAge.max(people);
        System.out.println("Youngest: " + youngest);
        System.out.println("Oldest: " + oldest);
        
        // Top/bottom K
        List<Person> youngest2 = byAge.leastOf(people, 2);
        System.out.println("2 youngest: " + youngest2);
    }
    
    /**
     * Demonstrates real-world scenarios using Guava collections
     */
    public static void demonstrateRealWorldScenarios() {
        System.out.println("\n8. Real-World Scenarios");
        System.out.println("=======================");
        
        // Scenario 1: Book library management
        demonstrateLibraryManagement();
        
        // Scenario 2: Web analytics
        demonstrateWebAnalytics();
    }
    
    /**
     * Library management system using various Guava collections
     */
    public static void demonstrateLibraryManagement() {
        System.out.println("Library Management System:");
        
        // Books by author (Multimap)
        ListMultimap<String, String> booksByAuthor = ArrayListMultimap.create();
        booksByAuthor.put("J.K. Rowling", "Harry Potter and the Philosopher's Stone");
        booksByAuthor.put("J.K. Rowling", "Harry Potter and the Chamber of Secrets");
        booksByAuthor.put("George Orwell", "1984");
        booksByAuthor.put("George Orwell", "Animal Farm");
        
        // ISBN to title mapping (BiMap)
        BiMap<String, String> isbnToTitle = HashBiMap.create();
        isbnToTitle.put("978-0439708180", "Harry Potter and the Philosopher's Stone");
        isbnToTitle.put("978-0451524935", "1984");
        isbnToTitle.put("978-0451526342", "Animal Farm");
        
        // Book availability by branch and title (Table)
        Table<String, String, Integer> availability = HashBasedTable.create();
        availability.put("Main Branch", "1984", 3);
        availability.put("Main Branch", "Animal Farm", 2);
        availability.put("North Branch", "1984", 1);
        availability.put("South Branch", "Animal Farm", 4);
        
        // Query examples
        System.out.println("Books by J.K. Rowling: " + booksByAuthor.get("J.K. Rowling"));
        
        String isbn = "978-0451524935";
        String title = isbnToTitle.get(isbn);
        System.out.println("ISBN " + isbn + " -> " + title);
        
        System.out.println("'1984' availability at Main Branch: " + 
                         availability.get("Main Branch", "1984"));
        
        // Find total copies of "Animal Farm"
        int totalAnimalFarm = availability.column("Animal Farm").values()
                                        .stream()
                                        .mapToInt(Integer::intValue)
                                        .sum();
        System.out.println("Total 'Animal Farm' copies: " + totalAnimalFarm);
    }
    
    /**
     * Web analytics using Guava collections
     */
    public static void demonstrateWebAnalytics() {
        System.out.println("\nWeb Analytics System:");
        
        // Page views by hour (simulated data)
        ListMultimap<Integer, String> pageViewsByHour = ArrayListMultimap.create();
        
        // Simulate some page views
        for (int hour = 9; hour <= 17; hour++) {
            int viewsThisHour = 10 + (int)(Math.random() * 20);
            for (int i = 0; i < viewsThisHour; i++) {
                String[] pages = {"/home", "/products", "/about", "/contact", "/blog"};
                String page = pages[(int)(Math.random() * pages.length)];
                pageViewsByHour.put(hour, page);
            }
        }
        
        // Analyze peak hours
        System.out.println("Page views by hour:");
        for (Integer hour : pageViewsByHour.keySet()) {
            int views = pageViewsByHour.get(hour).size();
            System.out.println("  " + hour + ":00 - " + views + " views");
        }
        
        // Find most popular pages
        Multiset<String> pageCounts = HashMultiset.create();
        pageCounts.addAll(pageViewsByHour.values());
        
        System.out.println("\nMost popular pages:");
        for (Multiset.Entry<String> entry : pageCounts.entrySet()) {
            System.out.println("  " + entry.getElement() + ": " + entry.getCount() + " views");
        }
        
        // Find peak hour
        Optional<Integer> peakHour = pageViewsByHour.keySet().stream()
            .max(Comparator.comparing(hour -> pageViewsByHour.get(hour).size()));
        
        if (peakHour.isPresent()) {
            System.out.println("Peak hour: " + peakHour.get() + ":00 with " + 
                             pageViewsByHour.get(peakHour.get()).size() + " views");
        }
    }
    
    // Supporting classes
    static class Product {
        private final String name;
        private final double price;
        
        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
        
        public String getName() { return name; }
        public double getPrice() { return price; }
        
        @Override
        public String toString() {
            return name + " - $" + price;
        }
    }
    
    static class Person {
        private final String firstName;
        private final String lastName;
        private final int age;
        
        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAge() { return age; }
        
        @Override
        public String toString() {
            return firstName + " " + lastName + " (" + age + ")";
        }
    }
}