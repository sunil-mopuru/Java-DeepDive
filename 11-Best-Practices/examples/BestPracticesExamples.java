/**
 * BestPracticesExamples.java - Comprehensive Best Practices Examples
 * 
 * This program demonstrates design patterns, SOLID principles,
 * and professional Java development practices.
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BestPracticesExamples {
    
    public static void main(String[] args) {
        
        System.out.println("=== JAVA BEST PRACTICES COMPREHENSIVE EXAMPLES ===\n");
        
        // 1. DESIGN PATTERNS
        designPatternsDemo();
        
        // 2. SOLID PRINCIPLES
        solidPrinciplesDemo();
        
        // 3. CODE QUALITY PRACTICES
        codeQualityDemo();
        
        // 4. PERFORMANCE BEST PRACTICES
        performanceBestPractices();
    }
    
    // ==================== DESIGN PATTERNS ====================
    
    public static void designPatternsDemo() {
        System.out.println("1. DESIGN PATTERNS:");
        System.out.println("-------------------");
        
        // Singleton Pattern
        System.out.println("Singleton Pattern:");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        System.out.printf("Same instance: %b%n", db1 == db2);
        
        // Factory Pattern
        System.out.println("\nFactory Pattern:");
        ShapeFactory factory = new ShapeFactory();
        Shape circle = factory.createShape("CIRCLE");
        Shape rectangle = factory.createShape("RECTANGLE");
        
        circle.draw();
        rectangle.draw();
        
        // Builder Pattern
        System.out.println("\nBuilder Pattern:");
        Computer computer = new Computer.Builder("Intel i7", "16GB")
                .setStorage("1TB SSD")
                .setGraphicsCard("RTX 4080")
                .setWifi(true)
                .build();
        
        System.out.println("Built computer: " + computer);
        
        // Observer Pattern
        System.out.println("\nObserver Pattern:");
        WeatherStation station = new WeatherStation();
        
        Observer display1 = new CurrentConditionsDisplay("Display-1");
        Observer display2 = new CurrentConditionsDisplay("Display-2");
        
        station.addObserver(display1);
        station.addObserver(display2);
        
        station.setTemperature(25.5f);
        station.setTemperature(30.0f);
        
        System.out.println();
    }
    
    // ==================== SOLID PRINCIPLES ====================
    
    public static void solidPrinciplesDemo() {
        System.out.println("2. SOLID PRINCIPLES:");
        System.out.println("--------------------");
        
        // Single Responsibility Principle
        System.out.println("Single Responsibility Principle:");
        Employee emp = new Employee("John Doe", 75000);
        EmployeeRepository repo = new EmployeeRepository();
        SalaryCalculator calc = new SalaryCalculator();
        
        repo.save(emp);
        double netSalary = calc.calculateNetSalary(emp.getSalary());
        System.out.printf("Employee: %s, Net Salary: $%.2f%n", emp.getName(), netSalary);
        
        // Open/Closed Principle
        System.out.println("\nOpen/Closed Principle:");
        List<Shape> shapes = Arrays.asList(
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 4, 5)
        );
        
        AreaCalculator areaCalc = new AreaCalculator();
        double totalArea = areaCalc.calculateTotalArea(shapes);
        System.out.printf("Total area: %.2f%n", totalArea);
        
        // Liskov Substitution Principle
        System.out.println("\nLiskov Substitution Principle:");
        Bird[] birds = {new Sparrow(), new Penguin()};
        
        for (Bird bird : birds) {
            bird.eat();
            if (bird instanceof Flyable) {
                ((Flyable) bird).fly();
            }
        }
        
        // Interface Segregation Principle
        System.out.println("\nInterface Segregation Principle:");
        Printer printer = new SimplePrinter();
        Scanner scanner = new SimpleScanner();
        
        printer.print("Hello World");
        scanner.scan();
        
        // Dependency Inversion Principle
        System.out.println("\nDependency Inversion Principle:");
        EmailService emailService = new EmailService();
        NotificationService notification = new NotificationService(emailService);
        notification.sendNotification("Welcome to our service!");
        
        System.out.println();
    }
    
    // ==================== CODE QUALITY PRACTICES ====================
    
    public static void codeQualityDemo() {
        System.out.println("3. CODE QUALITY PRACTICES:");
        System.out.println("---------------------------");
        
        // Proper exception handling
        System.out.println("Exception Handling:");
        FileProcessor processor = new FileProcessor();
        
        try {
            processor.processFile("nonexistent.txt");
        } catch (FileProcessingException e) {
            System.out.println("Handled exception: " + e.getMessage());
        }
        
        // Null safety
        System.out.println("\nNull Safety:");
        String nullString = null;
        String result = StringUtils.safeToUpperCase(nullString);
        System.out.println("Safe result: " + result);
        
        // Input validation
        System.out.println("\nInput Validation:");
        UserValidator validator = new UserValidator();
        
        try {
            User validUser = new User("john.doe@email.com", "SecurePass123!");
            validator.validate(validUser);
            System.out.println("User validation passed");
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    // ==================== PERFORMANCE BEST PRACTICES ====================
    
    public static void performanceBestPractices() {
        System.out.println("4. PERFORMANCE BEST PRACTICES:");
        System.out.println("-------------------------------");
        
        // String concatenation
        System.out.println("String Performance:");
        
        // Bad approach
        long start = System.currentTimeMillis();
        String result1 = "";
        for (int i = 0; i < 1000; i++) {
            result1 += "item" + i;
        }
        long time1 = System.currentTimeMillis() - start;
        
        // Good approach
        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("item").append(i);
        }
        String result2 = sb.toString();
        long time2 = System.currentTimeMillis() - start;
        
        System.out.printf("String concatenation: %d ms vs StringBuilder: %d ms%n", time1, time2);
        
        // Collection performance
        System.out.println("\nCollection Performance:");
        
        // ArrayList vs LinkedList for random access
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        
        for (int i = 0; i < 10000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            arrayList.get(i * 5);
        }
        time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            linkedList.get(i * 5);
        }
        time2 = System.currentTimeMillis() - start;
        
        System.out.printf("ArrayList access: %d ms vs LinkedList: %d ms%n", time1, time2);
        
        System.out.println();
    }
}

// ==================== DESIGN PATTERN IMPLEMENTATIONS ====================

// Singleton Pattern
class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    
    private DatabaseConnection() {
        // Private constructor
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}

// Factory Pattern
interface Shape {
    void draw();
    double area();
}

class Circle implements Shape {
    private double radius;
    
    public Circle() { this.radius = 1.0; }
    public Circle(double radius) { this.radius = radius; }
    
    @Override
    public void draw() {
        System.out.println("Drawing a circle with radius: " + radius);
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    private double width, height;
    
    public Rectangle() { this.width = 1.0; this.height = 1.0; }
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing rectangle: %.1f x %.1f%n", width, height);
    }
    
    @Override
    public double area() {
        return width * height;
    }
}

class Triangle implements Shape {
    private double a, b, c;
    
    public Triangle(double a, double b, double c) {
        this.a = a; this.b = b; this.c = c;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing triangle with sides: %.1f, %.1f, %.1f%n", a, b, c);
    }
    
    @Override
    public double area() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}

class ShapeFactory {
    public Shape createShape(String type) {
        switch (type.toUpperCase()) {
            case "CIRCLE": return new Circle();
            case "RECTANGLE": return new Rectangle();
            default: throw new IllegalArgumentException("Unknown shape: " + type);
        }
    }
}

// Builder Pattern
class Computer {
    private final String cpu;
    private final String ram;
    private final String storage;
    private final String graphicsCard;
    private final boolean hasWifi;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.graphicsCard = builder.graphicsCard;
        this.hasWifi = builder.hasWifi;
    }
    
    public static class Builder {
        private final String cpu;
        private final String ram;
        private String storage = "500GB HDD";
        private String graphicsCard = "Integrated";
        private boolean hasWifi = false;
        
        public Builder(String cpu, String ram) {
            this.cpu = cpu;
            this.ram = ram;
        }
        
        public Builder setStorage(String storage) {
            this.storage = storage;
            return this;
        }
        
        public Builder setGraphicsCard(String card) {
            this.graphicsCard = card;
            return this;
        }
        
        public Builder setWifi(boolean hasWifi) {
            this.hasWifi = hasWifi;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format("Computer{cpu='%s', ram='%s', storage='%s', gpu='%s', wifi=%b}", 
                           cpu, ram, storage, graphicsCard, hasWifi);
    }
}

// Observer Pattern
interface Observer {
    void update(float temperature);
}

class WeatherStation {
    private List<Observer> observers = new ArrayList<>();
    private float temperature;
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void setTemperature(float temp) {
        this.temperature = temp;
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature);
        }
    }
}

class CurrentConditionsDisplay implements Observer {
    private String name;
    
    public CurrentConditionsDisplay(String name) {
        this.name = name;
    }
    
    @Override
    public void update(float temperature) {
        System.out.printf("%s: Current temperature is %.1f°C%n", name, temperature);
    }
}

// ==================== SOLID PRINCIPLES EXAMPLES ====================

// Single Responsibility
class Employee {
    private String name;
    private double salary;
    
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public double getSalary() { return salary; }
}

class EmployeeRepository {
    public void save(Employee employee) {
        System.out.println("Saving employee: " + employee.getName());
    }
}

class SalaryCalculator {
    public double calculateNetSalary(double grossSalary) {
        return grossSalary * 0.8; // 20% tax
    }
}

// Open/Closed Principle
class AreaCalculator {
    public double calculateTotalArea(List<Shape> shapes) {
        return shapes.stream().mapToDouble(Shape::area).sum();
    }
}

// Liskov Substitution Principle
abstract class Bird {
    public abstract void eat();
}

interface Flyable {
    void fly();
}

class Sparrow extends Bird implements Flyable {
    @Override
    public void eat() {
        System.out.println("Sparrow eating seeds");
    }
    
    @Override
    public void fly() {
        System.out.println("Sparrow flying");
    }
}

class Penguin extends Bird {
    @Override
    public void eat() {
        System.out.println("Penguin eating fish");
    }
}

// Interface Segregation Principle
interface Printer {
    void print(String document);
}

interface Scanner {
    void scan();
}

class SimplePrinter implements Printer {
    @Override
    public void print(String document) {
        System.out.println("Printing: " + document);
    }
}

class SimpleScanner implements Scanner {
    @Override
    public void scan() {
        System.out.println("Scanning document");
    }
}

// Dependency Inversion Principle
interface MessageService {
    void sendMessage(String message);
}

class EmailService implements MessageService {
    @Override
    public void sendMessage(String message) {
        System.out.println("Email sent: " + message);
    }
}

class NotificationService {
    private final MessageService messageService;
    
    public NotificationService(MessageService messageService) {
        this.messageService = messageService;
    }
    
    public void sendNotification(String message) {
        messageService.sendMessage(message);
    }
}

// ==================== CODE QUALITY EXAMPLES ====================

class FileProcessingException extends Exception {
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

class FileProcessor {
    public void processFile(String filename) throws FileProcessingException {
        try {
            // Simulate file processing
            if (!filename.endsWith(".txt")) {
                throw new IllegalArgumentException("Only .txt files supported");
            }
            // Processing logic here
            System.out.println("Processing file: " + filename);
        } catch (Exception e) {
            throw new FileProcessingException("Failed to process file: " + filename, e);
        }
    }
}

class StringUtils {
    public static String safeToUpperCase(String input) {
        return input == null ? null : input.toUpperCase();
    }
    
    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}

class User {
    private final String email;
    private final String password;
    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}

class UserValidator {
    public void validate(User user) throws ValidationException {
        if (StringUtils.isNullOrEmpty(user.getEmail())) {
            throw new ValidationException("Email is required");
        }
        
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format");
        }
        
        if (user.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
    }
}