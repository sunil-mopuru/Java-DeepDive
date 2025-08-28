# Chapter 4: Advanced Object-Oriented Programming

## 📚 Table of Contents
1. [Introduction to Advanced OOP](#introduction-to-advanced-oop)
2. [Inheritance](#inheritance)
3. [Polymorphism](#polymorphism)
4. [Encapsulation Advanced](#encapsulation-advanced)
5. [Abstraction](#abstraction)
6. [Interface vs Abstract Classes](#interface-vs-abstract-classes)
7. [Method Overriding](#method-overriding)
8. [The `super` Keyword](#the-super-keyword)
9. [Final Keyword](#final-keyword)
10. [Practical Applications](#practical-applications)
11. [Key Takeaways](#key-takeaways)

---

## Introduction to Advanced OOP

### **The Four Pillars of OOP - Advanced Level:**

Now that we understand basic classes and objects, let's dive deep into the four fundamental principles that make OOP powerful:

#### **1. Inheritance** - "IS-A" Relationship
- Creating new classes based on existing classes
- Code reuse and establishing hierarchical relationships
- Parent-child class relationships

#### **2. Polymorphism** - "Same Interface, Different Behavior"
- Objects can take multiple forms
- Same method name, different implementations
- Runtime method resolution

#### **3. Encapsulation** - "Data Hiding and Protection"
- Advanced data protection techniques
- Controlled access through interfaces
- Information hiding principles

#### **4. Abstraction** - "Essential Features Only"
- Hiding implementation complexity
- Focus on what an object does, not how
- Abstract classes and interfaces

---

## Inheritance

### **What is Inheritance?**
Inheritance allows a class (child/subclass) to inherit properties and methods from another class (parent/superclass).

### **Benefits of Inheritance:**
- **Code Reusability** - Don't repeat yourself (DRY principle)
- **Method Overriding** - Customize behavior in child classes
- **Hierarchical Classification** - Model real-world relationships
- **Extensibility** - Add new features without modifying existing code

### **Inheritance Syntax:**
```java
public class ParentClass {
    // Parent class members
}

public class ChildClass extends ParentClass {
    // Child class inherits all non-private members from ParentClass
    // Can add new members and override existing ones
}
```

### **Real-World Example: Vehicle Hierarchy**

#### **Base Class (Parent):**
```java
public class Vehicle {
    // Protected members can be accessed by subclasses
    protected String brand;
    protected String model;
    protected int year;
    protected double speed;
    protected boolean isRunning;
    
    // Constructor
    public Vehicle(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.speed = 0.0;
        this.isRunning = false;
    }
    
    // Methods that can be inherited
    public void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.println(brand + " " + model + " started.");
        }
    }
    
    public void stop() {
        isRunning = false;
        speed = 0.0;
        System.out.println(brand + " " + model + " stopped.");
    }
    
    public void accelerate(double amount) {
        if (isRunning) {
            speed += amount;
            System.out.printf("Speed: %.1f km/h%n", speed);
        }
    }
    
    // Method to be overridden
    public void displayInfo() {
        System.out.printf("%d %s %s%n", year, brand, model);
    }
    
    // Getters
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getSpeed() { return speed; }
    public boolean isRunning() { return isRunning; }
}
```

#### **Derived Classes (Children):**

##### **Car Class:**
```java
public class Car extends Vehicle {
    private int doors;
    private String fuelType;
    private boolean hasAirConditioning;
    
    // Constructor calls parent constructor
    public Car(String brand, String model, int year, int doors, String fuelType) {
        super(brand, model, year);  // Call parent constructor
        this.doors = doors;
        this.fuelType = fuelType;
        this.hasAirConditioning = true;
    }
    
    // New method specific to Car
    public void honk() {
        System.out.println("Beep beep!");
    }
    
    public void toggleAirConditioning() {
        hasAirConditioning = !hasAirConditioning;
        System.out.println("Air conditioning " + (hasAirConditioning ? "ON" : "OFF"));
    }
    
    // Override parent method
    @Override
    public void displayInfo() {
        super.displayInfo();  // Call parent version
        System.out.printf("  Type: Car, Doors: %d, Fuel: %s%n", doors, fuelType);
    }
    
    // Override accelerate with car-specific logic
    @Override
    public void accelerate(double amount) {
        if (isRunning) {
            double maxSpeed = fuelType.equals("Electric") ? 200.0 : 180.0;
            speed = Math.min(speed + amount, maxSpeed);
            System.out.printf("Car accelerated to %.1f km/h%n", speed);
        }
    }
}
```

##### **Motorcycle Class:**
```java
public class Motorcycle extends Vehicle {
    private int engineSize;  // in cc
    private boolean hasSidecar;
    
    public Motorcycle(String brand, String model, int year, int engineSize) {
        super(brand, model, year);
        this.engineSize = engineSize;
        this.hasSidecar = false;
    }
    
    // Motorcycle-specific methods
    public void wheelie() {
        if (isRunning && speed > 20) {
            System.out.println("Performing a wheelie!");
        } else {
            System.out.println("Need more speed for a wheelie!");
        }
    }
    
    public void attachSidecar() {
        hasSidecar = true;
        System.out.println("Sidecar attached!");
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("  Type: Motorcycle, Engine: %d cc%n", engineSize);
    }
    
    @Override
    public void accelerate(double amount) {
        if (isRunning) {
            // Motorcycles accelerate faster but have lower top speed
            double acceleration = hasSidecar ? amount * 0.7 : amount * 1.2;
            speed = Math.min(speed + acceleration, 160.0);
            System.out.printf("Motorcycle accelerated to %.1f km/h%n", speed);
        }
    }
}
```

##### **Truck Class:**
```java
public class Truck extends Vehicle {
    private double cargoCapacity;  // in tons
    private double currentLoad;
    private int axles;
    
    public Truck(String brand, String model, int year, double cargoCapacity, int axles) {
        super(brand, model, year);
        this.cargoCapacity = cargoCapacity;
        this.currentLoad = 0.0;
        this.axles = axles;
    }
    
    public void loadCargo(double weight) {
        if (currentLoad + weight <= cargoCapacity) {
            currentLoad += weight;
            System.out.printf("Loaded %.1f tons. Current load: %.1f/%.1f tons%n", 
                             weight, currentLoad, cargoCapacity);
        } else {
            System.out.println("Cannot load: exceeds capacity!");
        }
    }
    
    public void unloadCargo(double weight) {
        if (weight <= currentLoad) {
            currentLoad -= weight;
            System.out.printf("Unloaded %.1f tons. Remaining load: %.1f tons%n", 
                             weight, currentLoad);
        }
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf("  Type: Truck, Capacity: %.1f tons, Load: %.1f tons%n", 
                         cargoCapacity, currentLoad);
    }
    
    @Override
    public void accelerate(double amount) {
        if (isRunning) {
            // Trucks accelerate slower, especially when loaded
            double loadFactor = 1.0 - (currentLoad / cargoCapacity * 0.5);
            double acceleration = amount * loadFactor;
            speed = Math.min(speed + acceleration, 120.0);
            System.out.printf("Truck accelerated to %.1f km/h%n", speed);
        }
    }
}
```

### **Types of Inheritance:**

#### **1. Single Inheritance:**
```java
class Animal { }
class Dog extends Animal { }  // Dog inherits from Animal
```

#### **2. Multilevel Inheritance:**
```java
class Animal { }
class Mammal extends Animal { }
class Dog extends Mammal { }  // Dog → Mammal → Animal
```

#### **3. Hierarchical Inheritance:**
```java
class Animal { }
class Dog extends Animal { }     // Both inherit from Animal
class Cat extends Animal { }
```

**Note:** Java does NOT support multiple inheritance (one class extending multiple classes) to avoid the "Diamond Problem." However, interfaces provide similar functionality.

---

## Polymorphism

### **What is Polymorphism?**
Polymorphism allows objects of different classes to be treated as objects of a common base class, while each object behaves according to its actual class.

### **Types of Polymorphism:**

#### **1. Compile-Time Polymorphism (Method Overloading)**
```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public double add(double a, double b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
}
```

#### **2. Runtime Polymorphism (Method Overriding)**
```java
// Using Vehicle hierarchy
public class PolymorphismDemo {
    public static void main(String[] args) {
        // Polymorphic references
        Vehicle[] fleet = {
            new Car("Toyota", "Camry", 2022, 4, "Gasoline"),
            new Motorcycle("Harley", "Street 750", 2021, 750),
            new Truck("Ford", "F-150", 2023, 2.5, 2)
        };
        
        // Same method call, different behaviors
        for (Vehicle vehicle : fleet) {
            vehicle.start();          // Inherited method
            vehicle.accelerate(30);   // Overridden method - different for each type
            vehicle.displayInfo();    // Overridden method
            vehicle.stop();
            System.out.println();
        }
    }
}
```

### **Dynamic Method Dispatch:**
```java
public class PolymorphicBehavior {
    public static void demonstratePolymorphism(Vehicle vehicle) {
        // The actual method called depends on the object type at runtime
        vehicle.displayInfo();  // Could be Car, Motorcycle, or Truck version
        
        // Type checking and casting when needed
        if (vehicle instanceof Car) {
            Car car = (Car) vehicle;
            car.honk();  // Car-specific method
        } else if (vehicle instanceof Motorcycle) {
            Motorcycle bike = (Motorcycle) vehicle;
            bike.wheelie();  // Motorcycle-specific method
        } else if (vehicle instanceof Truck) {
            Truck truck = (Truck) vehicle;
            truck.loadCargo(1.0);  // Truck-specific method
        }
    }
}
```

### **Benefits of Polymorphism:**
- **Flexibility** - Same code works with different object types
- **Extensibility** - Add new classes without changing existing code
- **Maintainability** - Changes in implementations don't affect client code
- **Code Reuse** - Write once, use with multiple types

---

## Encapsulation Advanced

### **Advanced Encapsulation Techniques:**

#### **1. Data Validation in Setters:**
```java
public class BankAccount {
    private double balance;
    private String accountNumber;
    private AccountStatus status;
    
    public enum AccountStatus {
        ACTIVE, FROZEN, CLOSED
    }
    
    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }
    
    public void withdraw(double amount) {
        if (status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account is not active");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
    }
}
```

#### **2. Immutable Objects:**
```java
public final class ImmutablePerson {
    private final String name;
    private final int age;
    private final List<String> hobbies;
    
    public ImmutablePerson(String name, int age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        // Create defensive copy
        this.hobbies = new ArrayList<>(hobbies);
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    public List<String> getHobbies() {
        // Return defensive copy
        return new ArrayList<>(hobbies);
    }
    
    // No setters - object is immutable
}
```

#### **3. Builder Pattern for Complex Objects:**
```java
public class Computer {
    private final String cpu;
    private final int ram;
    private final int storage;
    private final String gpu;
    private final boolean hasWifi;
    
    // Private constructor
    private Computer(ComputerBuilder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.gpu = builder.gpu;
        this.hasWifi = builder.hasWifi;
    }
    
    // Static inner builder class
    public static class ComputerBuilder {
        private String cpu;
        private int ram;
        private int storage;
        private String gpu = "Integrated";
        private boolean hasWifi = true;
        
        public ComputerBuilder(String cpu, int ram, int storage) {
            this.cpu = cpu;
            this.ram = ram;
            this.storage = storage;
        }
        
        public ComputerBuilder setGpu(String gpu) {
            this.gpu = gpu;
            return this;
        }
        
        public ComputerBuilder setWifi(boolean hasWifi) {
            this.hasWifi = hasWifi;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
}

// Usage:
Computer gaming = new Computer.ComputerBuilder("Intel i9", 32, 1000)
    .setGpu("RTX 4080")
    .setWifi(true)
    .build();
```

---

## Abstraction

### **What is Abstraction?**
Abstraction hides implementation details while showing only essential features to the user.

### **Abstract Classes:**

#### **Syntax:**
```java
public abstract class AbstractClassName {
    // Can have concrete methods
    public void concreteMethod() {
        // Implementation
    }
    
    // Must have at least one abstract method
    public abstract void abstractMethod();
}
```

#### **Example: Shape Hierarchy**
```java
public abstract class Shape {
    protected String color;
    protected boolean filled;
    
    // Concrete constructor
    public Shape(String color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }
    
    // Concrete methods
    public String getColor() { return color; }
    public boolean isFilled() { return filled; }
    
    public void displayInfo() {
        System.out.printf("Shape: Color=%s, Filled=%b%n", color, filled);
    }
    
    // Abstract methods - must be implemented by subclasses
    public abstract double calculateArea();
    public abstract double calculatePerimeter();
    public abstract void draw();
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(String color, boolean filled, double radius) {
        super(color, filled);
        this.radius = radius;
    }
    
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing a %s circle with radius %.2f%n", color, radius);
    }
}

public class Rectangle extends Shape {
    private double length;
    private double width;
    
    public Rectangle(String color, boolean filled, double length, double width) {
        super(color, filled);
        this.length = length;
        this.width = width;
    }
    
    @Override
    public double calculateArea() {
        return length * width;
    }
    
    @Override
    public double calculatePerimeter() {
        return 2 * (length + width);
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing a %s rectangle %.2f x %.2f%n", color, length, width);
    }
}
```

### **Interfaces:**

#### **Syntax:**
```java
public interface InterfaceName {
    // Constants (public, static, final by default)
    int CONSTANT_VALUE = 100;
    
    // Abstract methods (public, abstract by default)
    void method1();
    int method2(String parameter);
    
    // Default methods (Java 8+)
    default void defaultMethod() {
        System.out.println("Default implementation");
    }
    
    // Static methods (Java 8+)
    static void staticMethod() {
        System.out.println("Static method in interface");
    }
}
```

#### **Example: Animal Behavior Interface**
```java
public interface Animal {
    // Constants
    int MAX_AGE = 100;
    
    // Abstract methods
    void eat();
    void sleep();
    void makeSound();
    
    // Default method
    default void breathe() {
        System.out.println("Breathing...");
    }
}

public interface Flyable {
    void fly();
    void land();
    
    default double getMaxAltitude() {
        return 10000.0; // meters
    }
}

public interface Swimmable {
    void swim();
    void dive();
}

// Multiple interface implementation
public class Duck implements Animal, Flyable, Swimmable {
    private String name;
    
    public Duck(String name) {
        this.name = name;
    }
    
    @Override
    public void eat() {
        System.out.println(name + " is eating bread crumbs");
    }
    
    @Override
    public void sleep() {
        System.out.println(name + " is sleeping with head tucked under wing");
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " says: Quack quack!");
    }
    
    @Override
    public void fly() {
        System.out.println(name + " is flying");
    }
    
    @Override
    public void land() {
        System.out.println(name + " is landing gracefully");
    }
    
    @Override
    public void swim() {
        System.out.println(name + " is swimming in the pond");
    }
    
    @Override
    public void dive() {
        System.out.println(name + " is diving for food");
    }
}
```

---

## Interface vs Abstract Classes

### **When to Use Each:**

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| **Multiple Inheritance** | ✓ (implements multiple) | ✗ (extends one only) |
| **Constructor** | ✗ | ✓ |
| **Instance Variables** | ✗ (only constants) | ✓ |
| **Method Implementation** | Default/Static only | ✓ All types |
| **Access Modifiers** | public only | All modifiers |
| **Use Case** | Contract/Capability | Common base with shared code |

### **Example Comparison:**
```java
// Interface - defines a contract
public interface Drawable {
    void draw();
    void erase();
}

// Abstract class - provides common implementation
public abstract class GraphicalObject implements Drawable {
    protected int x, y;
    protected String color;
    
    public GraphicalObject(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    // Common implementation
    public void moveTo(int newX, int newY) {
        x = newX;
        y = newY;
    }
    
    // Abstract method
    public abstract void resize(double factor);
    
    // Concrete method
    public void displayPosition() {
        System.out.printf("Position: (%d, %d)%n", x, y);
    }
}

public class Button extends GraphicalObject {
    private String text;
    
    public Button(int x, int y, String color, String text) {
        super(x, y, color);
        this.text = text;
    }
    
    @Override
    public void draw() {
        System.out.printf("Drawing %s button '%s' at (%d, %d)%n", color, text, x, y);
    }
    
    @Override
    public void erase() {
        System.out.printf("Erasing button '%s'%n", text);
    }
    
    @Override
    public void resize(double factor) {
        System.out.printf("Resizing button by factor %.2f%n", factor);
    }
}
```

---

## Method Overriding

### **Rules for Method Overriding:**
1. **Same signature** - name, parameters, return type
2. **Cannot reduce visibility** - can increase (private → protected → public)
3. **Cannot throw broader exceptions**
4. **Use `@Override` annotation** for compile-time checking

### **Example with Different Override Scenarios:**
```java
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    // Method to be overridden
    public void makeSound() {
        System.out.println(name + " makes a sound");
    }
    
    // Method with protected access
    protected void sleep() {
        System.out.println(name + " is sleeping");
    }
    
    // Final method - cannot be overridden
    public final void breathe() {
        System.out.println(name + " is breathing");
    }
}

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println(name + " barks: Woof woof!");
    }
    
    @Override
    public void sleep() {  // Can change protected to public
        System.out.println(name + " curls up and sleeps");
    }
    
    // Cannot override breathe() - it's final
    // public void breathe() { } // Compiler error
}
```

### **Covariant Return Types:**
```java
public class Animal {
    public Animal reproduce() {
        return new Animal("Baby");
    }
}

public class Dog extends Animal {
    @Override
    public Dog reproduce() {  // Covariant return type
        return new Dog("Puppy");
    }
}
```

---

## The `super` Keyword

### **Uses of `super`:**

#### **1. Call Parent Constructor:**
```java
public class Employee {
    protected String name;
    protected double salary;
    
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
}

public class Manager extends Employee {
    private int teamSize;
    
    public Manager(String name, double salary, int teamSize) {
        super(name, salary);  // Call parent constructor
        this.teamSize = teamSize;
    }
}
```

#### **2. Call Parent Method:**
```java
public class Vehicle {
    public void start() {
        System.out.println("Vehicle starting...");
    }
}

public class Car extends Vehicle {
    @Override
    public void start() {
        super.start();  // Call parent method first
        System.out.println("Car engine started!");
    }
}
```

#### **3. Access Parent Fields:**
```java
public class Person {
    protected String name;
}

public class Student extends Person {
    private String name; // Hides parent field
    
    public void displayNames() {
        System.out.println("Student name: " + this.name);
        System.out.println("Person name: " + super.name);
    }
}
```

---

## Final Keyword

### **Uses of `final`:**

#### **1. Final Variables (Constants):**
```java
public class Constants {
    public static final double PI = 3.14159;
    public static final int MAX_SIZE = 100;
    
    private final String id;  // Must be initialized
    
    public Constants(String id) {
        this.id = id;  // Can initialize in constructor
    }
}
```

#### **2. Final Methods (Cannot be Overridden):**
```java
public class Security {
    public final void authenticate() {
        // Critical security method - cannot be overridden
        System.out.println("Secure authentication process");
    }
}
```

#### **3. Final Classes (Cannot be Extended):**
```java
public final class String {  // String class is final
    // Cannot be subclassed
}

public final class UtilityClass {
    // Utility class that shouldn't be extended
    public static void utilityMethod() {
        // Static utility methods
    }
}
```

---

## Practical Applications

### **Example: Employee Management System with Full OOP**
```java
// Abstract base class
public abstract class Employee {
    protected String name;
    protected int id;
    protected double baseSalary;
    protected Department department;
    
    public Employee(String name, int id, double baseSalary, Department department) {
        this.name = name;
        this.id = id;
        this.baseSalary = baseSalary;
        this.department = department;
    }
    
    // Abstract method - each employee type calculates salary differently
    public abstract double calculateSalary();
    
    // Concrete method
    public void displayInfo() {
        System.out.printf("Employee: %s (ID: %d), Department: %s%n", 
                         name, id, department);
    }
    
    // Template method pattern
    public final double processPayroll() {
        double salary = calculateSalary();
        double tax = calculateTax(salary);
        double netSalary = salary - tax;
        
        System.out.printf("Gross: $%.2f, Tax: $%.2f, Net: $%.2f%n", 
                         salary, tax, netSalary);
        return netSalary;
    }
    
    private double calculateTax(double salary) {
        return salary * 0.2; // 20% tax
    }
}

// Concrete implementations
public class FullTimeEmployee extends Employee implements Payable {
    private double bonus;
    
    public FullTimeEmployee(String name, int id, double baseSalary, Department dept) {
        super(name, id, baseSalary, dept);
        this.bonus = 0.0;
    }
    
    @Override
    public double calculateSalary() {
        return baseSalary + bonus;
    }
    
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    
    @Override
    public void processPayment() {
        double net = processPayroll();
        System.out.println("Processing full-time employee payment: $" + net);
    }
}

public class Contractor extends Employee implements Payable {
    private int hoursWorked;
    private double hourlyRate;
    
    public Contractor(String name, int id, double hourlyRate, Department dept) {
        super(name, id, 0, dept);  // No base salary for contractors
        this.hourlyRate = hourlyRate;
        this.hoursWorked = 0;
    }
    
    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }
    
    public void logHours(int hours) {
        this.hoursWorked += hours;
    }
    
    @Override
    public void processPayment() {
        double net = processPayroll();
        System.out.println("Processing contractor payment: $" + net);
        hoursWorked = 0; // Reset for next period
    }
}

// Interface for payment processing
public interface Payable {
    void processPayment();
}

// Enum for departments
public enum Department {
    ENGINEERING, MARKETING, SALES, HR, FINANCE
}
```

---

## Key Takeaways

### **Essential Advanced OOP Concepts Mastered:**
✅ **Inheritance** - Code reuse and hierarchical relationships  
✅ **Polymorphism** - Same interface, different behaviors  
✅ **Encapsulation** - Advanced data protection and immutability  
✅ **Abstraction** - Abstract classes and interfaces  
✅ **Method Overriding** - Customizing inherited behavior  
✅ **The `super` keyword** - Accessing parent class members  
✅ **Final keyword** - Preventing inheritance and modification  

### **Design Principles Applied:**
- **DRY (Don't Repeat Yourself)** - Inheritance reduces code duplication
- **Open/Closed Principle** - Open for extension, closed for modification
- **Liskov Substitution Principle** - Subclasses should be substitutable
- **Interface Segregation** - Use specific interfaces, not large ones

### **Best Practices:**
- **Favor composition over inheritance** when possible
- **Use interfaces to define contracts**
- **Make classes final if not designed for inheritance**
- **Override equals(), hashCode(), and toString() when needed**
- **Use abstract classes for common implementation**

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Design your own class hierarchies
- Move to [Arrays and Collections](../05-Arrays-Collections/README.md)

---

**Continue to: [Chapter 5: Arrays and Collections →](../05-Arrays-Collections/README.md)**