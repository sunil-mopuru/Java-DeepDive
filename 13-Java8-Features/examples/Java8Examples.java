/**
 * Java 8 Features - Comprehensive Examples
 * 
 * This file demonstrates Java 8 features from beginner to advanced:
 * BEGINNER: Basic lambda expressions and method references
 * INTERMEDIATE: Stream API operations and Optional usage
 * ADVANCED: Custom functional interfaces and complex stream operations
 * 
 * Each example includes detailed explanations and practical use cases.
 */

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;

public class Java8Examples {
    
    public static void main(String[] args) {
        System.out.println(\"=== Java 8 Features: Beginner to Advanced ===\");
        
        // BEGINNER LEVEL
        System.out.println(\"\n\\u001B[32m=== BEGINNER LEVEL ===\\u001B[0m\");
        demonstrateLambdaBasics();
        showMethodReferences();
        
        // INTERMEDIATE LEVEL
        System.out.println(\"\n\\u001B[33m=== INTERMEDIATE LEVEL ===\\u001B[0m\");
        exploreStreamAPI();
        masterOptionalUsage();
        
        // ADVANCED LEVEL
        System.out.println(\"\n\\u001B[31m=== ADVANCED LEVEL ===\\u001B[0m\");
        advancedStreamOperations();
        workWithDateTimeAPI();
    }
    
    /**
     * BEGINNER: Lambda Expression fundamentals
     */
    public static void demonstrateLambdaBasics() {
        System.out.println(\"\n1. Lambda Expression Basics\");
        System.out.println(\"============================\");
        
        // Before Java 8: Anonymous classes
        System.out.println(\"Before Java 8 (Anonymous Classes):\");
        
        List<String> names = Arrays.asList(\"Alice\", \"Bob\", \"Charlie\", \"David\");
        
        // Old way - Anonymous class
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        System.out.println(\"Sorted names (old way): \" + names);
        
        // Java 8 way - Lambda expressions
        System.out.println(\"\nJava 8 Lambda Expressions:\");
        
        List<String> names2 = Arrays.asList(\"Alice\", \"Bob\", \"Charlie\", \"David\");
        
        // Lambda expression - much shorter!
        Collections.sort(names2, (s1, s2) -> s1.compareTo(s2));
        System.out.println(\"Sorted names (lambda): \" + names2);
        
        // Even shorter with method reference
        Collections.sort(names2, String::compareTo);
        System.out.println(\"Sorted names (method ref): \" + names2);
        
        // Different lambda syntax examples
        System.out.println(\"\nLambda Syntax Examples:\");
        
        // No parameters
        Runnable sayHello = () -> System.out.println(\"Hello from lambda!\");
        sayHello.run();
        
        // One parameter (parentheses optional)
        Function<String, Integer> getLength = s -> s.length();
        System.out.println(\"Length of 'Hello': \" + getLength.apply(\"Hello\"));
        
        // Multiple parameters
        BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println(\"5 + 3 = \" + add.apply(5, 3));
        
        // Block body
        Function<String, String> processString = (str) -> {
            String result = str.trim().toLowerCase();
            return \"Processed: \" + result;
        };
        System.out.println(processString.apply(\"  HELLO WORLD  \"));
        
        System.out.println(\"\nLambda Benefits:\");
        System.out.println(\"\u2713 More concise code\");
        System.out.println(\"\u2713 Better readability\");
        System.out.println(\"\u2713 Functional programming support\");
    }
    
    /**
     * BEGINNER: Method References
     */
    public static void showMethodReferences() {
        System.out.println(\"\n2. Method References\");
        System.out.println(\"====================\");
        
        List<String> words = Arrays.asList(\"hello\", \"world\", \"java\", \"programming\");
        
        System.out.println(\"Original list: \" + words);
        
        // Static method reference
        System.out.println(\"\nStatic Method References:\");
        List<Integer> lengths = words.stream()
            .map(String::length)  // Same as: s -> s.length()
            .collect(Collectors.toList());
        System.out.println(\"String lengths: \" + lengths);
        
        // Instance method reference
        System.out.println(\"\nInstance Method References:\");
        words.stream()
            .map(String::toUpperCase)  // Same as: s -> s.toUpperCase()
            .forEach(System.out::println);  // Same as: s -> System.out.println(s)
        
        // Constructor reference
        System.out.println(\"\nConstructor References:\");
        List<StringBuilder> builders = words.stream()
            .map(StringBuilder::new)  // Same as: s -> new StringBuilder(s)
            .collect(Collectors.toList());
        
        System.out.println(\"StringBuilder objects created: \" + builders.size());
        
        // Method reference with object instance
        System.out.println(\"\nInstance Method of Specific Object:\");
        String prefix = \"PREFIX: \";
        List<String> prefixed = words.stream()
            .map(prefix::concat)  // Same as: s -> prefix.concat(s)
            .collect(Collectors.toList());
        System.out.println(\"Prefixed: \" + prefixed);
        
        System.out.println(\"\nMethod Reference Types:\");
        System.out.println(\"\u2713 Static: Class::staticMethod\");
        System.out.println(\"\u2713 Instance: object::instanceMethod\");
        System.out.println(\"\u2713 Constructor: Class::new\");
        System.out.println(\"\u2713 Arbitrary object: Class::instanceMethod\");
    }
    
    /**
     * INTERMEDIATE: Stream API Operations
     */
    public static void exploreStreamAPI() {
        System.out.println(\"\n3. Stream API Exploration\");
        System.out.println(\"==========================\");
        
        // Sample data
        List<Employee> employees = createEmployeeData();
        
        System.out.println(\"Sample employees: \" + employees.size() + \" total\");
        employees.forEach(emp -> 
            System.out.println(\"  \" + emp.getName() + \" - \" + emp.getDepartment() + 
                             \" - $\" + emp.getSalary()));
        
        // Basic filtering
        System.out.println(\"\nFiltering Operations:\");
        
        List<Employee> highEarners = employees.stream()
            .filter(emp -> emp.getSalary() > 60000)
            .collect(Collectors.toList());
        
        System.out.println(\"Employees earning > $60,000: \" + highEarners.size());
        highEarners.forEach(emp -> 
            System.out.println(\"  \" + emp.getName() + \": $\" + emp.getSalary()));
        
        // Mapping operations
        System.out.println(\"\nMapping Operations:\");
        
        List<String> employeeNames = employees.stream()
            .map(Employee::getName)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        
        System.out.println(\"Employee names (uppercase): \" + employeeNames);
        
        // Grouping operations
        System.out.println(\"\nGrouping Operations:\");
        
        Map<String, List<Employee>> byDepartment = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        
        byDepartment.forEach((dept, empList) -> {
            System.out.println(dept + \" Department: \" + empList.size() + \" employees\");
            empList.forEach(emp -> System.out.println(\"  - \" + emp.getName()));
        });
        
        // Statistical operations
        System.out.println(\"\nStatistical Operations:\");
        
        DoubleSummaryStatistics salaryStats = employees.stream()
            .mapToDouble(Employee::getSalary)
            .summaryStatistics();
        
        System.out.printf(\"Salary Statistics:\n\");
        System.out.printf(\"  Count: %d\n\", salaryStats.getCount());
        System.out.printf(\"  Average: $%.2f\n\", salaryStats.getAverage());
        System.out.printf(\"  Min: $%.2f\n\", salaryStats.getMin());
        System.out.printf(\"  Max: $%.2f\n\", salaryStats.getMax());
        System.out.printf(\"  Total: $%.2f\n\", salaryStats.getSum());
        
        System.out.println(\"\nStream Benefits:\");
        System.out.println(\"\u2713 Declarative programming style\");
        System.out.println(\"\u2713 Easy parallel processing\");
        System.out.println(\"\u2713 Lazy evaluation for performance\");
        System.out.println(\"\u2713 Functional composition\");
    }
    
    /**
     * INTERMEDIATE: Optional Usage
     */
    public static void masterOptionalUsage() {
        System.out.println(\"\n4. Optional Pattern Mastery\");
        System.out.println(\"============================\");
        
        List<Employee> employees = createEmployeeData();
        
        // Finding with Optional
        System.out.println(\"Finding Operations with Optional:\");
        
        Optional<Employee> foundEmployee = employees.stream()
            .filter(emp -> emp.getName().startsWith(\"A\"))
            .findFirst();
        
        // Safe way to handle Optional
        if (foundEmployee.isPresent()) {
            System.out.println(\"Found employee: \" + foundEmployee.get().getName());
        } else {
            System.out.println(\"No employee found starting with 'A'\");
        }
        
        // Better functional approach
        System.out.println(\"\nFunctional Optional Usage:\");
        
        String employeeName = foundEmployee
            .map(Employee::getName)
            .map(String::toUpperCase)
            .orElse(\"NO EMPLOYEE FOUND\");
        
        System.out.println(\"Employee name: \" + employeeName);
        
        // Optional chaining
        System.out.println(\"\nOptional Chaining:\");
        
        Optional<String> department = employees.stream()
            .filter(emp -> emp.getSalary() > 100000)
            .findFirst()
            .map(Employee::getDepartment)
            .filter(dept -> dept.contains(\"Engineering\"));
        
        department.ifPresentOrElse(
            dept -> System.out.println(\"High-earning engineer in: \" + dept),
            () -> System.out.println(\"No high-earning engineers found\")
        );
        
        // Optional with default values
        System.out.println(\"\nOptional with Defaults:\");
        
        String defaultName = Optional.ofNullable(getEmployeeName(null))
            .orElse(\"Unknown Employee\");
        
        System.out.println(\"Employee name with default: \" + defaultName);
        
        String computedDefault = Optional.ofNullable(getEmployeeName(null))
            .orElseGet(() -> \"Computed Default: \" + System.currentTimeMillis());
        
        System.out.println(\"Computed default: \" + computedDefault);
        
        // Exception handling with Optional
        try {
            String requiredName = Optional.ofNullable(getEmployeeName(null))
                .orElseThrow(() -> new IllegalStateException(\"Employee name is required\"));
        } catch (IllegalStateException e) {
            System.out.println(\"Caught exception: \" + e.getMessage());
        }
        
        System.out.println(\"\nOptional Best Practices:\");
        System.out.println(\"\u2713 Use Optional for return types, not parameters\");
        System.out.println(\"\u2713 Prefer functional style over isPresent() + get()\");
        System.out.println(\"\u2713 Use orElse() for simple defaults, orElseGet() for computed defaults\");
        System.out.println(\"\u2713 Chain Optional operations with map() and flatMap()\");
    }
    
    /**
     * ADVANCED: Complex Stream Operations
     */
    public static void advancedStreamOperations() {
        System.out.println(\"\n5. Advanced Stream Operations\");
        System.out.println(\"==============================\");
        
        List<Employee> employees = createEmployeeData();
        
        // Custom collectors
        System.out.println(\"Custom Collectors:\");
        
        String employeeReport = employees.stream()
            .collect(Collector.of(
                StringBuilder::new,
                (sb, emp) -> sb.append(emp.getName()).append(\"(\")
                               .append(emp.getDepartment()).append(\"), \"),
                (sb1, sb2) -> sb1.append(sb2),
                sb -> sb.length() > 0 ? sb.substring(0, sb.length() - 2) : \"\"
            ));
        
        System.out.println(\"Employee Report: \" + employeeReport);
        
        // Parallel processing
        System.out.println(\"\nParallel Processing:\");
        
        long startTime = System.nanoTime();
        
        double avgSalarySequential = employees.stream()
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);
        
        long sequentialTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        
        double avgSalaryParallel = employees.parallelStream()
            .mapToDouble(Employee::getSalary)
            .average()
            .orElse(0.0);
        
        long parallelTime = System.nanoTime() - startTime;
        
        System.out.printf(\"Sequential average: $%.2f (took %d ns)\n\", 
                         avgSalarySequential, sequentialTime);
        System.out.printf(\"Parallel average: $%.2f (took %d ns)\n\", 
                         avgSalaryParallel, parallelTime);
        
        // Complex grouping
        System.out.println(\"\nComplex Grouping Operations:\");
        
        Map<String, Map<Boolean, List<Employee>>> complexGrouping = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.partitioningBy(emp -> emp.getSalary() > 70000)
            ));
        
        complexGrouping.forEach((dept, salaryGroups) -> {
            System.out.println(dept + \" Department:\");
            System.out.println(\"  High earners: \" + salaryGroups.get(true).size());
            System.out.println(\"  Regular earners: \" + salaryGroups.get(false).size());
        });
        
        System.out.println(\"\nAdvanced Stream Features:\");
        System.out.println(\"\u2713 Custom collectors for complex aggregations\");
        System.out.println(\"\u2713 Parallel streams for performance\");
        System.out.println(\"\u2713 Multi-level grouping and partitioning\");
        System.out.println(\"\u2713 Functional composition and reusability\");
    }
    
    /**
     * ADVANCED: Date and Time API
     */
    public static void workWithDateTimeAPI() {
        System.out.println(\"\n6. Modern Date and Time API\");
        System.out.println(\"============================\");
        
        // Basic date/time operations
        System.out.println(\"Basic Date/Time Operations:\");
        
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalDateTime dateTime = LocalDateTime.now();
        
        System.out.println(\"Today: \" + today);
        System.out.println(\"Current time: \" + currentTime);
        System.out.println(\"Date and time: \" + dateTime);
        
        // Date manipulation
        System.out.println(\"\nDate Manipulation:\");
        
        LocalDate nextWeek = today.plusWeeks(1);
        LocalDate lastMonth = today.minusMonths(1);
        LocalDate specificDate = LocalDate.of(2024, 12, 25);
        
        System.out.println(\"Next week: \" + nextWeek);
        System.out.println(\"Last month: \" + lastMonth);
        System.out.println(\"Christmas 2024: \" + specificDate);
        
        // Formatting and parsing
        System.out.println(\"\nFormatting and Parsing:\");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"dd/MM/yyyy HH:mm:ss\");
        String formattedDateTime = dateTime.format(formatter);
        System.out.println(\"Formatted: \" + formattedDateTime);
        
        LocalDateTime parsed = LocalDateTime.parse(\"25/12/2024 15:30:00\", formatter);
        System.out.println(\"Parsed: \" + parsed);
        
        // Duration and Period
        System.out.println(\"\nDuration and Period Calculations:\");
        
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 17, 30);
        
        Duration workDuration = Duration.between(start, end);
        System.out.println(\"Work duration: \" + workDuration.toHours() + \" hours \" +
                          (workDuration.toMinutes() % 60) + \" minutes\");
        
        Period yearPeriod = Period.between(LocalDate.of(2020, 1, 1), today);
        System.out.println(\"Years since 2020: \" + yearPeriod.getYears() + \" years \" +
                          yearPeriod.getMonths() + \" months \" + yearPeriod.getDays() + \" days\");
        
        // Time zones
        System.out.println(\"\nTime Zone Operations:\");
        
        ZonedDateTime nowInNY = ZonedDateTime.now(ZoneId.of(\"America/New_York\"));
        ZonedDateTime nowInTokyo = ZonedDateTime.now(ZoneId.of(\"Asia/Tokyo\"));
        
        System.out.println(\"New York time: \" + nowInNY.format(formatter));
        System.out.println(\"Tokyo time: \" + nowInTokyo.format(formatter));
        
        System.out.println(\"\nDate/Time API Benefits:\");
        System.out.println(\"\u2713 Immutable and thread-safe\");
        System.out.println(\"\u2713 Clear API design\");
        System.out.println(\"\u2713 Better performance than old Date/Calendar\");
        System.out.println(\"\u2713 Comprehensive time zone support\");
    }
    
    // Helper Methods
    
    private static List<Employee> createEmployeeData() {
        return Arrays.asList(
            new Employee(\"Alice Johnson\", \"Engineering\", 75000),
            new Employee(\"Bob Smith\", \"Marketing\", 65000),
            new Employee(\"Charlie Brown\", \"Engineering\", 85000),
            new Employee(\"Diana Prince\", \"Sales\", 55000),
            new Employee(\"Edward Norton\", \"Engineering\", 95000),
            new Employee(\"Fiona Apple\", \"Marketing\", 60000),
            new Employee(\"George Lucas\", \"Sales\", 50000),
            new Employee(\"Helen Hunt\", \"HR\", 70000)
        );
    }
    
    private static String getEmployeeName(Employee employee) {
        return employee != null ? employee.getName() : null;
    }
}

/**
 * Simple Employee class for demonstration
 */
class Employee {
    private String name;
    private String department;
    private double salary;
    
    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    
    @Override
    public String toString() {
        return String.format(\"Employee{name='%s', dept='%s', salary=%.0f}\", 
                           name, department, salary);
    }
}

/**
 * Functional Interface Examples
 */
interface StringProcessor {
    String process(String input);
    
    // Default method in interface (Java 8 feature)
    default String processWithPrefix(String input, String prefix) {
        return prefix + process(input);
    }
    
    // Static method in interface (Java 8 feature)
    static StringProcessor createUpperCaseProcessor() {
        return String::toUpperCase;
    }
}

/**
 * Demonstration of functional interfaces and lambda usage
 */
class FunctionalInterfaceDemo {
    
    public static void demonstrateFunctionalInterfaces() {
        System.out.println(\"\n=== Functional Interface Demo ===\");
        
        // Using lambda with custom functional interface
        StringProcessor processor = (str) -> str.trim().toLowerCase();
        
        String result = processor.process(\"  HELLO WORLD  \");
        System.out.println(\"Processed: '\" + result + \"'\");
        
        // Using default method
        String prefixed = processor.processWithPrefix(\"test\", \"PREFIX: \");
        System.out.println(\"With prefix: \" + prefixed);
        
        // Using static method
        StringProcessor upperProcessor = StringProcessor.createUpperCaseProcessor();
        System.out.println(\"Uppercase: \" + upperProcessor.process(\"hello\"));
        
        // Built-in functional interfaces
        System.out.println(\"\nBuilt-in Functional Interfaces:\");
        
        // Predicate - test condition
        Predicate<String> isLong = str -> str.length() > 5;
        System.out.println(\"'programming' is long: \" + isLong.test(\"programming\"));
        
        // Function - transform input to output
        Function<String, Integer> getLength = String::length;
        System.out.println(\"Length of 'Java': \" + getLength.apply(\"Java\"));
        
        // Consumer - consume input, no return
        Consumer<String> printer = System.out::println;
        printer.accept(\"Hello from Consumer!\");
        
        // Supplier - supply value, no input
        Supplier<String> timeSupplier = () -> \"Current time: \" + System.currentTimeMillis();
        System.out.println(timeSupplier.get());
    }
}"