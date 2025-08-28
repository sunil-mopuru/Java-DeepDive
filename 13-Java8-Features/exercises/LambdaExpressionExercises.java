/**
 * Lambda Expression Exercises - Beginner to Advanced
 * 
 * Practice exercises for mastering lambda expressions, functional interfaces,
 * and method references in Java 8.
 */

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class LambdaExpressionExercises {
    
    public static void main(String[] args) {
        System.out.println("=== Lambda Expression Exercises ===");
        
        // Run exercises
        basicLambdaExercises();
        functionalInterfaceExercises();
        methodReferenceExercises();
        advancedLambdaExercises();
    }
    
    public static void basicLambdaExercises() {
        System.out.println("\n1. Basic Lambda Exercises");
        System.out.println("=========================");
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // Exercise 1a: Filter names with length > 3
        System.out.println("Exercise 1a: Filter names with length > 3");
        List<String> longNames = names.stream()
            .filter(name -> name.length() > 3)
            .collect(Collectors.toList());
        System.out.println("Long names: " + longNames);
        
        // Exercise 1b: Convert all names to uppercase
        System.out.println("\nExercise 1b: Convert all names to uppercase");
        List<String> upperNames = names.stream()
            .map(name -> name.toUpperCase())
            .collect(Collectors.toList());
        System.out.println("Upper case names: " + upperNames);
        
        // Exercise 1c: Sort names by length
        System.out.println("\nExercise 1c: Sort names by length");
        List<String> sortedByLength = names.stream()
            .sorted((name1, name2) -> Integer.compare(name1.length(), name2.length()))
            .collect(Collectors.toList());
        System.out.println("Sorted by length: " + sortedByLength);
        
        // Exercise 1d: Find first name starting with 'A'
        System.out.println("\nExercise 1d: Find first name starting with 'A'");
        Optional<String> nameStartingWithA = names.stream()
            .filter(name -> name.startsWith("A"))
            .findFirst();
        System.out.println("First name with 'A': " + nameStartingWithA.orElse("None"));
    }
    
    public static void functionalInterfaceExercises() {
        System.out.println("\n2. Functional Interface Exercises");
        System.out.println("=================================");
        
        // Exercise 2a: Using Predicate
        System.out.println("Exercise 2a: Using Predicate functional interface");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> greaterThan5 = n -> n > 5;
        Predicate<Integer> evenAndGreaterThan5 = isEven.and(greaterThan5);
        
        List<Integer> filtered = numbers.stream()
            .filter(evenAndGreaterThan5)
            .collect(Collectors.toList());
        System.out.println("Numbers that are even AND > 5: " + filtered);
        
        // Exercise 2b: Using Function
        System.out.println("\nExercise 2b: Using Function functional interface");
        Function<String, Integer> stringLength = s -> s.length();
        Function<Integer, String> numberToString = n -> "Number: " + n;
        Function<String, String> composed = stringLength.andThen(numberToString);
        
        List<String> words = Arrays.asList("hello", "world", "java", "lambda");
        List<String> results = words.stream()
            .map(composed)
            .collect(Collectors.toList());
        System.out.println("Composed function results: " + results);
        
        // Exercise 2c: Using Consumer
        System.out.println("\nExercise 2c: Using Consumer functional interface");
        Consumer<String> printer = s -> System.out.println("Processing: " + s);
        Consumer<String> upperCasePrinter = s -> System.out.println("UPPER: " + s.toUpperCase());
        Consumer<String> combined = printer.andThen(upperCasePrinter);
        
        words.forEach(combined);
        
        // Exercise 2d: Using Supplier
        System.out.println("\nExercise 2d: Using Supplier functional interface");
        Supplier<Double> randomSupplier = () -> Math.random();
        List<Double> randomNumbers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            randomNumbers.add(randomSupplier.get());
        }
        System.out.println("Random numbers: " + randomNumbers);
    }
    
    public static void methodReferenceExercises() {
        System.out.println("\n3. Method Reference Exercises");
        System.out.println("=============================");
        
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 3);
        
        // Exercise 3a: Static method references
        System.out.println("Exercise 3a: Static method references");
        List<Integer> lengths = words.stream()
            .map(String::length)  // Method reference to instance method
            .collect(Collectors.toList());
        System.out.println("Word lengths: " + lengths);
        
        List<String> stringNumbers = numbers.stream()
            .map(String::valueOf)  // Static method reference
            .collect(Collectors.toList());
        System.out.println("Numbers as strings: " + stringNumbers);
        
        // Exercise 3b: Instance method references
        System.out.println("\nExercise 3b: Instance method references");
        List<String> upperWords = words.stream()
            .map(String::toUpperCase)  // Instance method reference
            .collect(Collectors.toList());
        System.out.println("Upper case words: " + upperWords);
        
        // Exercise 3c: Constructor references
        System.out.println("\nExercise 3c: Constructor references");
        List<StringBuilder> builders = words.stream()
            .map(StringBuilder::new)  // Constructor reference
            .collect(Collectors.toList());
        System.out.println("StringBuilders created: " + builders.size());
        
        // Exercise 3d: Method references on arbitrary objects
        System.out.println("\nExercise 3d: Method references on arbitrary objects");
        words.stream()
            .sorted(String::compareToIgnoreCase)  // Method reference on arbitrary object
            .forEach(System.out::println);
    }
    
    public static void advancedLambdaExercises() {
        System.out.println("\n4. Advanced Lambda Exercises");
        System.out.println("============================");
        
        // Exercise 4a: Custom functional interface
        System.out.println("Exercise 4a: Custom functional interface");
        TriFunction<Integer, Integer, Integer, Integer> addThree = (a, b, c) -> a + b + c;
        int sum = addThree.apply(1, 2, 3);
        System.out.println("Sum of 1, 2, 3: " + sum);
        
        // Exercise 4b: Higher-order functions
        System.out.println("\nExercise 4b: Higher-order functions");
        Function<Integer, Function<Integer, Integer>> adder = x -> y -> x + y;
        Function<Integer, Integer> add5 = adder.apply(5);
        int result = add5.apply(10);
        System.out.println("5 + 10 = " + result);
        
        // Exercise 4c: Lambda with exception handling
        System.out.println("\nExercise 4c: Lambda with exception handling");
        List<String> stringNumbers = Arrays.asList("1", "2", "invalid", "4", "5");
        List<Integer> parsedNumbers = stringNumbers.stream()
            .map(this::safeParseInt)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        System.out.println("Safely parsed numbers: " + parsedNumbers);
        
        // Exercise 4d: Complex data processing
        System.out.println("\nExercise 4d: Complex data processing with Employee objects");
        List<Employee> employees = createEmployeeList();
        
        // Find highest paid employee in each department
        Map<String, Optional<Employee>> highestPaidByDept = employees.stream()
            .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.maxBy(Comparator.comparing(Employee::getSalary))
            ));
        
        highestPaidByDept.forEach((dept, emp) -> 
            System.out.println(dept + ": " + emp.map(Employee::getName).orElse("None"))
        );
    }
    
    // Helper methods
    private Optional<Integer> safeParseInt(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    private static List<Employee> createEmployeeList() {
        return Arrays.asList(
            new Employee("Alice", "Engineering", 75000),
            new Employee("Bob", "Engineering", 80000),
            new Employee("Charlie", "Marketing", 60000),
            new Employee("Diana", "Marketing", 65000),
            new Employee("Eve", "HR", 55000),
            new Employee("Frank", "HR", 58000)
        );
    }
    
    // Custom functional interface
    @FunctionalInterface
    interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
    
    // Employee class for exercises
    static class Employee {
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
            return String.format("Employee{name='%s', department='%s', salary=%.0f}", 
                               name, department, salary);
        }
    }
}