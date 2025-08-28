/**
 * OOPAdvancedExamples.java - Advanced OOP Concepts
 * 
 * Demonstrates inheritance, polymorphism, abstraction, and encapsulation
 * with practical examples and real-world scenarios.
 */

import java.util.*;

public class OOPAdvancedExamples {
    
    public static void main(String[] args) {
        System.out.println("=== ADVANCED OOP CONCEPTS ===\n");
        
        inheritanceDemo();
        polymorphismDemo();
        abstractionDemo();
        encapsulationDemo();
    }
    
    // ==================== INHERITANCE ====================
    
    public static void inheritanceDemo() {
        System.out.println("1. INHERITANCE:");
        System.out.println("---------------");
        
        Car car = new Car("Toyota", "Camry", 2023, 4);
        Motorcycle bike = new Motorcycle("Honda", "CBR", 2022, 600);
        
        Vehicle[] vehicles = {car, bike};
        
        for (Vehicle v : vehicles) {
            v.start();
            v.accelerate(50);
            v.displayInfo();
            System.out.println();
        }
        
        // Specific methods
        car.honk();
        bike.wheelie();
        System.out.println();
    }
    
    // ==================== POLYMORPHISM ====================
    
    public static void polymorphismDemo() {
        System.out.println("2. POLYMORPHISM:");
        System.out.println("----------------");
        
        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(3, 4, 5)
        };
        
        double totalArea = 0;
        for (Shape shape : shapes) {
            shape.draw();
            double area = shape.calculateArea();
            System.out.printf("Area: %.2f%n", area);
            totalArea += area;
        }
        
        System.out.printf("Total area: %.2f%n%n", totalArea);
    }
    
    // ==================== ABSTRACTION ====================
    
    public static void abstractionDemo() {
        System.out.println("3. ABSTRACTION:");
        System.out.println("---------------");
        
        Employee[] employees = {
            new FullTimeEmployee("Alice", 101, 75000),
            new PartTimeEmployee("Bob", 102, 25, 80)
        };
        
        for (Employee emp : employees) {
            emp.displayInfo();
            System.out.printf("Salary: $%.2f%n%n", emp.calculateSalary());
        }
        
        // Interface example
        Drawable[] items = {new Circle(3), new Button("OK")};
        for (Drawable item : items) {
            item.draw();
            item.resize(1.5);
        }
        System.out.println();
    }
    
    // ==================== ENCAPSULATION ====================
    
    public static void encapsulationDemo() {
        System.out.println("4. ENCAPSULATION:");
        System.out.println("-----------------");
        
        BankAccount account = new BankAccount("ACC123", 1000);
        
        System.out.printf("Balance: $%.2f%n", account.getBalance());
        account.deposit(500);
        account.withdraw(200);
        account.withdraw(2000); // Should fail
        
        System.out.printf("Final balance: $%.2f%n", account.getBalance());
    }
}

// ==================== INHERITANCE HIERARCHY ====================

abstract class Vehicle {
    protected String brand, model;
    protected int year;
    protected double speed;
    protected boolean running;
    
    public Vehicle(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.speed = 0;
        this.running = false;
    }
    
    public void start() {
        running = true;
        System.out.printf("%s %s started%n", brand, model);
    }
    
    public abstract void accelerate(double amount);
    
    public void displayInfo() {
        System.out.printf("%d %s %s - Speed: %.1f km/h%n", year, brand, model, speed);
    }
}

class Car extends Vehicle {
    private int doors;
    
    public Car(String brand, String model, int year, int doors) {
        super(brand, model, year);
        this.doors = doors;
    }
    
    @Override
    public void accelerate(double amount) {
        if (running) {
            speed = Math.min(speed + amount, 200);
            System.out.printf("Car speed: %.1f km/h%n", speed);
        }
    }
    
    public void honk() {
        System.out.println("Beep beep!");
    }
}

class Motorcycle extends Vehicle {
    private int engineSize;
    
    public Motorcycle(String brand, String model, int year, int engineSize) {
        super(brand, model, year);
        this.engineSize = engineSize;
    }
    
    @Override
    public void accelerate(double amount) {
        if (running) {
            speed = Math.min(speed + amount * 1.2, 180);
            System.out.printf("Motorcycle speed: %.1f km/h%n", speed);
        }
    }
    
    public void wheelie() {
        if (running && speed > 20) {
            System.out.println("Doing a wheelie!");
        } else {
            System.out.println("Need more speed for wheelie");
        }
    }
}

// ==================== SHAPES FOR POLYMORPHISM ====================

abstract class Shape {
    public abstract double calculateArea();
    public abstract void draw();
}

class Circle extends Shape implements Drawable {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing circle (radius: %.1f)%n", radius);
    }
    
    @Override
    public void resize(double factor) {
        radius *= factor;
        System.out.printf("Circle resized to radius %.1f%n", radius);
    }
}

class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double calculateArea() {
        return width * height;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing rectangle (%.1f x %.1f)%n", width, height);
    }
}

class Triangle extends Shape {
    private double a, b, c;
    
    public Triangle(double a, double b, double c) {
        this.a = a; this.b = b; this.c = c;
    }
    
    @Override
    public double calculateArea() {
        double s = (a + b + c) / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing triangle (sides: %.1f, %.1f, %.1f)%n", a, b, c);
    }
}

// ==================== EMPLOYEE HIERARCHY ====================

abstract class Employee {
    protected String name;
    protected int id;
    
    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public abstract double calculateSalary();
    
    public void displayInfo() {
        System.out.printf("Employee: %s (ID: %d)%n", name, id);
    }
}

class FullTimeEmployee extends Employee {
    private double annualSalary;
    
    public FullTimeEmployee(String name, int id, double annualSalary) {
        super(name, id);
        this.annualSalary = annualSalary;
    }
    
    @Override
    public double calculateSalary() {
        return annualSalary / 12;
    }
}

class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;
    
    public PartTimeEmployee(String name, int id, double hourlyRate, int hoursWorked) {
        super(name, id);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }
    
    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }
}

// ==================== INTERFACES ====================

interface Drawable {
    void draw();
    void resize(double factor);
}

class Button implements Drawable {
    private String text;
    private int width, height;
    
    public Button(String text) {
        this.text = text;
        this.width = 100;
        this.height = 30;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing button: '%s' (%dx%d)%n", text, width, height);
    }
    
    @Override
    public void resize(double factor) {
        width = (int)(width * factor);
        height = (int)(height * factor);
        System.out.printf("Button resized to %dx%d%n", width, height);
    }
}

// ==================== ENCAPSULATION ====================

class BankAccount {
    private final String accountNumber;
    private double balance;
    
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = Math.max(0, initialBalance);
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Deposited $%.2f%n", amount);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.printf("Withdrew $%.2f%n", amount);
            return true;
        } else {
            System.out.println("Invalid withdrawal or insufficient funds");
            return false;
        }
    }
    
    public double getBalance() {
        return balance;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
}