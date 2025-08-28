/**
 * AdvancedTopicsExamples.java - Comprehensive Advanced Java Features
 * 
 * This program demonstrates advanced Java features including generics,
 * lambda expressions, streams, Optional, annotations, and modern Java patterns.
 */

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class AdvancedTopicsExamples {
    
    public static void main(String[] args) {
        
        System.out.println("=== ADVANCED JAVA TOPICS COMPREHENSIVE EXAMPLES ===\n");
        
        // 1. GENERICS
        genericsExamples();
        
        // 2. LAMBDA EXPRESSIONS
        lambdaExpressions();
        
        // 3. STREAMS API
        streamsAPI();
        
        // 4. OPTIONAL CLASS
        optionalExamples();
        
        // 5. ANNOTATIONS
        annotationExamples();
        
        // 6. MODERN JAVA FEATURES
        modernJavaFeatures();
    }
    
    // ==================== GENERICS ====================
    
    public static void genericsExamples() {
        System.out.println("1. GENERICS:");
        System.out.println("------------");
        
        // Generic class usage
        Box<String> stringBox = new Box<>();
        stringBox.setValue("Hello Generics");
        System.out.println("String Box: " + stringBox.getValue());
        
        Box<Integer> intBox = new Box<>();
        intBox.setValue(42);
        System.out.println("Integer Box: " + intBox.getValue());
        
        // Generic method usage
        String[] stringArray = {"apple", "banana", "cherry"};
        Integer[] intArray = {1, 2, 3, 4, 5};
        
        System.out.println("String array: " + Arrays.toString(stringArray));
        swap(stringArray, 0, 2);
        System.out.println("After swap: " + Arrays.toString(stringArray));
        
        System.out.println("Integer array: " + Arrays.toString(intArray));
        swap(intArray, 1, 3);
        System.out.println("After swap: " + Arrays.toString(intArray));
        
        // Bounded type parameters
        NumberContainer<Integer> intContainer = new NumberContainer<>(100);
        NumberContainer<Double> doubleContainer = new NumberContainer<>(3.14);
        
        System.out.println("Integer container double value: " + intContainer.getDoubleValue());
        System.out.println("Double container double value: " + doubleContainer.getDoubleValue());
        
        // Wildcard usage
        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
        
        System.out.println("Sum of integers: " + sumNumbers(intList));
        System.out.println("Sum of doubles: " + sumNumbers(doubleList));
        
        // Generic interface implementation
        Pair<String, Integer> nameAge = new Pair<>("John", 30);
        System.out.printf("Name-Age pair: %s, %d%n", nameAge.getFirst(), nameAge.getSecond());
        
        Pair<String, String> coordinates = new Pair<>("10.5", "20.3");
        System.out.printf("Coordinates: %s, %s%n", coordinates.getFirst(), coordinates.getSecond());
        
        System.out.println();
    }
    
    // Generic method
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    // Wildcard method
    public static double sumNumbers(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number num : numbers) {
            sum += num.doubleValue();
        }
        return sum;
    }
    
    // ==================== LAMBDA EXPRESSIONS ====================
    
    public static void lambdaExpressions() {
        System.out.println("2. LAMBDA EXPRESSIONS:");
        System.out.println("----------------------");
        
        // Basic lambda expressions
        List<String> names = Arrays.asList("John", "Jane", "Bob", "Alice", "Charlie");
        
        // Lambda for sorting
        System.out.println("Original names: " + names);
        
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort((a, b) -> a.compareTo(b));
        System.out.println("Sorted names: " + sortedNames);
        
        // Lambda with Comparator
        List<String> sortedByLength = new ArrayList<>(names);
        sortedByLength.sort(Comparator.comparing(String::length));
        System.out.println("Sorted by length: " + sortedByLength);
        
        // Functional interfaces
        System.out.println("\nFunctional Interfaces:");
        
        // Predicate
        Predicate<String> longName = name -> name.length() > 4;
        names.stream()
             .filter(longName)
             .forEach(name -> System.out.println("Long name: " + name));
        
        // Function
        Function<String, Integer> nameLength = String::length;
        names.stream()
             .map(nameLength)
             .forEach(length -> System.out.println("Length: " + length));
        
        // Consumer
        Consumer<String> printer = name -> System.out.println("Processing: " + name);
        names.forEach(printer);
        
        // Supplier
        Supplier<String> randomName = () -> {
            String[] randomNames = {"Alex", "Emma", "Ryan", "Sophie"};
            return randomNames[(int)(Math.random() * randomNames.length)];
        };
        
        System.out.println("Random name: " + randomName.get());
        System.out.println("Another random name: " + randomName.get());
        
        // BiFunction
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        
        System.out.printf("Add 5 + 3 = %d%n", add.apply(5, 3));
        System.out.printf("Multiply 4 * 6 = %d%n", multiply.apply(4, 6));
        
        // Method references
        System.out.println("\nMethod References:");
        
        // Static method reference
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
               .map(String::valueOf)  // Static method reference
               .forEach(System.out::println);  // Instance method reference
        
        // Constructor reference
        List<String> stringNumbers = Arrays.asList("1", "2", "3", "4", "5");
        List<Integer> parsedNumbers = stringNumbers.stream()
                                                  .map(Integer::new)  // Constructor reference
                                                  .collect(Collectors.toList());
        System.out.println("Parsed numbers: " + parsedNumbers);
        
        System.out.println();
    }
    
    // ==================== STREAMS API ====================
    
    public static void streamsAPI() {
        System.out.println("3. STREAMS API:");
        System.out.println("---------------");
        
        // Sample data
        List<Person> people = Arrays.asList(
            new Person("John", 30, "Developer", 75000),
            new Person("Jane", 25, "Designer", 65000),
            new Person("Bob", 35, "Manager", 85000),
            new Person("Alice", 28, "Developer", 70000),
            new Person("Charlie", 32, "Analyst", 60000),
            new Person("Diana", 29, "Developer", 72000)
        );
        
        System.out.println("Original people list:");
        people.forEach(System.out::println);
        
        // Filter and map operations
        System.out.println("\nDevelopers with salary > 70000:");
        people.stream()
              .filter(p -> "Developer".equals(p.getJobTitle()))
              .filter(p -> p.getSalary() > 70000)
              .forEach(System.out::println);
        
        // Collect to different collections
        List<String> developerNames = people.stream()
            .filter(p -> "Developer".equals(p.getJobTitle()))
            .map(Person::getName)
            .collect(Collectors.toList());
        
        System.out.println("\nDeveloper names: " + developerNames);
        
        // Group by job title
        Map<String, List<Person>> groupedByJob = people.stream()
            .collect(Collectors.groupingBy(Person::getJobTitle));
        
        System.out.println("\nGrouped by job title:");
        groupedByJob.forEach((job, persons) -> {
            System.out.printf("%s: %s%n", job, 
                persons.stream().map(Person::getName).collect(Collectors.toList()));
        });
        
        // Statistical operations
        OptionalDouble averageSalary = people.stream()
            .mapToDouble(Person::getSalary)
            .average();
        
        System.out.printf("Average salary: $%.2f%n", averageSalary.orElse(0.0));
        
        DoubleSummaryStatistics salaryStats = people.stream()
            .collect(Collectors.summarizingDouble(Person::getSalary));
        
        System.out.printf("Salary statistics: Min=%.0f, Max=%.0f, Avg=%.2f, Count=%d%n",
            salaryStats.getMin(), salaryStats.getMax(), 
            salaryStats.getAverage(), salaryStats.getCount());
        
        // Parallel streams
        System.out.println("\nParallel stream processing:");
        
        List<Integer> largeList = IntStream.rangeClosed(1, 1000000)
                                          .boxed()
                                          .collect(Collectors.toList());
        
        long startTime = System.currentTimeMillis();
        long sequentialSum = largeList.stream()
                                     .mapToLong(Integer::longValue)
                                     .sum();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        long parallelSum = largeList.parallelStream()
                                   .mapToLong(Integer::longValue)
                                   .sum();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.printf("Sequential: %d ms, Parallel: %d ms%n", 
                         sequentialTime, parallelTime);
        System.out.printf("Results equal: %b%n", sequentialSum == parallelSum);
        
        // Complex stream operations
        System.out.println("\nComplex stream operations:");
        
        String result = people.stream()
            .filter(p -> p.getAge() > 25)
            .sorted(Comparator.comparing(Person::getSalary).reversed())
            .limit(3)
            .map(p -> p.getName() + "($" + p.getSalary() + ")")
            .collect(Collectors.joining(", ", "Top earners: ", ""));
        
        System.out.println(result);
        
        System.out.println();
    }
    
    // ==================== OPTIONAL CLASS ====================
    
    public static void optionalExamples() {
        System.out.println("4. OPTIONAL CLASS:");
        System.out.println("------------------");
        
        // Creating Optional objects
        Optional<String> emptyOptional = Optional.empty();
        Optional<String> nonEmptyOptional = Optional.of("Hello Optional");
        Optional<String> nullableOptional = Optional.ofNullable(null);
        
        System.out.printf("Empty optional present: %b%n", emptyOptional.isPresent());
        System.out.printf("Non-empty optional present: %b%n", nonEmptyOptional.isPresent());
        System.out.printf("Nullable optional present: %b%n", nullableOptional.isPresent());
        
        // Using Optional safely
        nonEmptyOptional.ifPresent(value -> System.out.println("Value: " + value));
        
        String value1 = nonEmptyOptional.orElse("Default value");
        String value2 = emptyOptional.orElse("Default value");
        
        System.out.println("Value1: " + value1);
        System.out.println("Value2: " + value2);
        
        // Optional with supplier
        String value3 = emptyOptional.orElseGet(() -> "Computed default value");
        System.out.println("Value3: " + value3);
        
        // Optional chaining
        PersonRepository repository = new PersonRepository();
        
        String personInfo = repository.findById(1)
            .map(person -> person.getName().toUpperCase())
            .filter(name -> name.length() > 3)
            .orElse("Person not found or name too short");
        
        System.out.println("Person info: " + personInfo);
        
        // Optional with exceptions
        try {
            String requiredValue = emptyOptional.orElseThrow(
                () -> new IllegalStateException("Required value is missing"));
        } catch (IllegalStateException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
        
        // FlatMap with Optional
        Optional<Address> address = repository.findById(1)
            .flatMap(Person::getAddress);
        
        address.ifPresent(addr -> System.out.println("Address: " + addr.getCity()));
        
        // Optional in streams
        List<Optional<String>> optionals = Arrays.asList(
            Optional.of("apple"),
            Optional.empty(),
            Optional.of("banana"),
            Optional.empty(),
            Optional.of("cherry")
        );
        
        List<String> presentValues = optionals.stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        
        System.out.println("Present values: " + presentValues);
        
        // Java 9+ Optional methods (if available)
        try {
            // or() method
            Optional<String> result = emptyOptional.or(() -> Optional.of("Alternative value"));
            System.out.println("Or result: " + result.get());
            
            // ifPresentOrElse() method
            nonEmptyOptional.ifPresentOrElse(
                value -> System.out.println("Present: " + value),
                () -> System.out.println("Not present")
            );
            
        } catch (NoSuchMethodError e) {
            System.out.println("Java 9+ Optional methods not available");
        }
        
        System.out.println();
    }
    
    // ==================== ANNOTATIONS ====================
    
    public static void annotationExamples() {
        System.out.println("5. ANNOTATIONS:");
        System.out.println("---------------");
        
        // Reflection with annotations
        Class<AnnotatedClass> clazz = AnnotatedClass.class;
        
        // Class-level annotations
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            System.out.printf("Entity name: %s, table: %s%n", 
                             entity.name(), entity.table());
        }
        
        // Method-level annotations
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Deprecated.class)) {
                System.out.printf("Deprecated method: %s%n", method.getName());
            }
            
            if (method.isAnnotationPresent(Validate.class)) {
                Validate validate = method.getAnnotation(Validate.class);
                System.out.printf("Validation method: %s, required: %b%n", 
                                 method.getName(), validate.required());
            }
        }
        
        // Field-level annotations
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                System.out.printf("Column field: %s -> %s (nullable: %b)%n", 
                                 field.getName(), column.name(), column.nullable());
            }
        }
        
        // Using annotated class
        AnnotatedClass instance = new AnnotatedClass();
        instance.setName("Test Entity");
        instance.setId(123L);
        
        try {
            instance.validateData();
            System.out.println("Validation passed");
        } catch (Exception e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    // ==================== MODERN JAVA FEATURES ====================
    
    public static void modernJavaFeatures() {
        System.out.println("6. MODERN JAVA FEATURES:");
        System.out.println("------------------------");
        
        // Local variable type inference (var) - Java 10+
        try {
            // This would work in Java 10+
            // var list = List.of("apple", "banana", "cherry");
            // var map = Map.of("key1", "value1", "key2", "value2");
            
            // Fallback for older Java versions
            List<String> list = Arrays.asList("apple", "banana", "cherry");
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            map.put("key2", "value2");
            
            System.out.println("List: " + list);
            System.out.println("Map: " + map);
        } catch (Exception e) {
            System.out.println("Modern collection creation not available");
        }
        
        // Text blocks (Java 15+) - simulated
        String jsonLikeString = "{\n" +
                               "  \"name\": \"John Doe\",\n" +
                               "  \"age\": 30,\n" +
                               "  \"email\": \"john@example.com\"\n" +
                               "}";
        
        System.out.println("JSON-like string:");
        System.out.println(jsonLikeString);
        
        // Switch expressions (Java 14+) - simulated with traditional switch
        DayOfWeek today = DayOfWeek.MONDAY;
        String dayType = getDayType(today);
        System.out.printf("Today (%s) is a %s%n", today, dayType);
        
        // Record-like behavior (simulated)
        Point point = new Point(10, 20);
        System.out.printf("Point: x=%d, y=%d%n", point.getX(), point.getY());
        
        Point anotherPoint = new Point(10, 20);
        System.out.printf("Points equal: %b%n", point.equals(anotherPoint));
        System.out.printf("Point hash: %d%n", point.hashCode());
        
        // Date/Time API (Java 8)
        System.out.println("\nDate/Time API:");
        
        LocalDate today2 = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalDateTime dateTime = LocalDateTime.now();
        
        System.out.printf("Today: %s%n", today2);
        System.out.printf("Now: %s%n", now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.printf("DateTime: %s%n", 
                         dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // Date calculations
        LocalDate nextWeek = today2.plusWeeks(1);
        LocalDate lastMonth = today2.minusMonths(1);
        
        System.out.printf("Next week: %s%n", nextWeek);
        System.out.printf("Last month: %s%n", lastMonth);
        
        // Duration and Period
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        
        System.out.printf("Duration since 2023-01-01: %d days%n", duration.toDays());
        
        System.out.println();
    }
    
    // Simulate switch expression for older Java versions
    private static String getDayType(DayOfWeek day) {
        switch (day) {
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
            case FRIDAY:
                return "weekday";
            case SATURDAY:
            case SUNDAY:
                return "weekend";
            default:
                return "unknown";
        }
    }
}

// ==================== GENERIC CLASSES ====================

class Box<T> {
    private T value;
    
    public void setValue(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
}

class NumberContainer<T extends Number> {
    private T number;
    
    public NumberContainer(T number) {
        this.number = number;
    }
    
    public double getDoubleValue() {
        return number.doubleValue();
    }
}

class Pair<T, U> {
    private T first;
    private U second;
    
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    public T getFirst() { return first; }
    public U getSecond() { return second; }
}

// ==================== PERSON AND RELATED CLASSES ====================

class Person {
    private String name;
    private int age;
    private String jobTitle;
    private double salary;
    private Optional<Address> address;
    
    public Person(String name, int age, String jobTitle, double salary) {
        this.name = name;
        this.age = age;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.address = Optional.empty();
    }
    
    // Getters
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getJobTitle() { return jobTitle; }
    public double getSalary() { return salary; }
    public Optional<Address> getAddress() { return address; }
    
    public void setAddress(Address address) {
        this.address = Optional.ofNullable(address);
    }
    
    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, job='%s', salary=%.0f}", 
                           name, age, jobTitle, salary);
    }
}

class Address {
    private String street;
    private String city;
    private String zipCode;
    
    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZipCode() { return zipCode; }
}

class PersonRepository {
    private List<Person> people;
    
    public PersonRepository() {
        people = Arrays.asList(
            new Person("John Doe", 30, "Developer", 75000),
            new Person("Jane Smith", 25, "Designer", 65000)
        );
        
        // Set address for first person
        people.get(0).setAddress(new Address("123 Main St", "Anytown", "12345"));
    }
    
    public Optional<Person> findById(int id) {
        if (id > 0 && id <= people.size()) {
            return Optional.of(people.get(id - 1));
        }
        return Optional.empty();
    }
}

// ==================== ANNOTATION DEFINITIONS ====================

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Entity {
    String name() default "";
    String table() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name() default "";
    boolean nullable() default true;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Validate {
    boolean required() default true;
}

// ==================== ANNOTATED CLASS ====================

@Entity(name = "TestEntity", table = "test_entities")
class AnnotatedClass {
    
    @Column(name = "entity_id", nullable = false)
    private Long id;
    
    @Column(name = "entity_name")
    private String name;
    
    @Column(name = "description", nullable = true)
    private String description;
    
    @Validate(required = true)
    public void validateData() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Name is required");
        }
    }
    
    @Deprecated
    public void oldMethod() {
        System.out.println("This method is deprecated");
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

// ==================== RECORD-LIKE CLASS (FOR COMPATIBILITY) ====================

class Point {
    private final int x;
    private final int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return String.format("Point{x=%d, y=%d}", x, y);
    }
}