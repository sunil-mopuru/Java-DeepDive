# Chapter 3: Object-Oriented Programming Basics

## 📚 Table of Contents
1. [Introduction to OOP](#introduction-to-oop)
2. [Classes and Objects](#classes-and-objects)
3. [Methods and Constructors](#methods-and-constructors)
4. [Instance Variables and Class Variables](#instance-variables-and-class-variables)
5. [Access Modifiers](#access-modifiers)
6. [The `this` Keyword](#the-this-keyword)
7. [Method Overloading](#method-overloading)
8. [Static Members](#static-members)
9. [Practical Applications](#practical-applications)
10. [Key Takeaways](#key-takeaways)

---

## Introduction to OOP

### What is Object-Oriented Programming?
**Object-Oriented Programming (OOP)** is a programming paradigm based on the concept of "objects" that contain:
- **Data** (attributes/fields)
- **Code** (methods/functions)

### **Why OOP?**
- **Modularity** - Code is organized in separate, interchangeable components
- **Reusability** - Objects can be reused across different programs
- **Maintainability** - Easier to modify and debug
- **Scalability** - Better suited for large applications
- **Real-world Modeling** - Natural way to represent real-world entities

### **Four Pillars of OOP:**

#### 1. **Encapsulation**
- Bundling data and methods that operate on that data
- Hiding internal implementation details
- Controlled access through public interfaces

#### 2. **Inheritance**
- Creating new classes based on existing classes
- Reusing code and establishing relationships
- "Is-a" relationship (covered in Advanced OOP)

#### 3. **Polymorphism**
- Same interface, different implementations
- Objects can take multiple forms
- Method overriding and overloading (covered in Advanced OOP)

#### 4. **Abstraction**
- Hiding complex implementation details
- Showing only essential features
- Focus on what an object does, not how (covered in Advanced OOP)

---

## Classes and Objects

### **What is a Class?**
A **class** is a blueprint or template for creating objects. It defines:
- **Attributes** (what the object has)
- **Methods** (what the object can do)

### **What is an Object?**
An **object** is an instance of a class - a concrete realization of the class blueprint.

### **Class Syntax:**
```java
public class ClassName {
    // Instance variables (attributes)
    private dataType variableName;
    
    // Constructor
    public ClassName(parameters) {
        // Initialize object
    }
    
    // Methods
    public returnType methodName(parameters) {
        // Method body
        return value; // if returnType is not void
    }
}
```

### **Real-World Example: Car Class**
```java
public class Car {
    // Instance variables (attributes)
    private String brand;
    private String model;
    private int year;
    private String color;
    private double speed;
    private boolean isRunning;
    
    // Constructor
    public Car(String brand, String model, int year, String color) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.color = color;
        this.speed = 0.0;
        this.isRunning = false;
    }
    
    // Methods (what the car can do)
    public void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.println(brand + " " + model + " is starting...");
        } else {
            System.out.println("Car is already running!");
        }
    }
    
    public void stop() {
        if (isRunning) {
            isRunning = false;
            speed = 0.0;
            System.out.println(brand + " " + model + " has stopped.");
        } else {
            System.out.println("Car is already stopped!");
        }
    }
    
    public void accelerate(double amount) {
        if (isRunning) {
            speed += amount;
            System.out.printf("Accelerating... Current speed: %.1f km/h%n", speed);
        } else {
            System.out.println("Start the car first!");
        }
    }
    
    // Getter methods (accessors)
    public String getBrand() {
        return brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    // Method to display car information
    public void displayInfo() {
        System.out.printf("Car: %d %s %s (%s) - Speed: %.1f km/h, Running: %b%n",
                          year, brand, model, color, speed, isRunning);
    }
}
```

### **Creating and Using Objects:**
```java
public class CarDemo {
    public static void main(String[] args) {
        // Creating objects (instances) of Car class
        Car myCar = new Car("Toyota", "Camry", 2022, "Blue");
        Car friendsCar = new Car("Honda", "Civic", 2021, "Red");
        
        // Using the objects
        myCar.displayInfo();
        myCar.start();
        myCar.accelerate(50);
        myCar.displayInfo();
        
        friendsCar.displayInfo();
        friendsCar.start();
        friendsCar.accelerate(30);
        friendsCar.stop();
        friendsCar.displayInfo();
    }
}
```

---

## Methods and Constructors

### **Methods**
Methods define the behavior of objects - what they can do.

#### **Method Syntax:**
```java
accessModifier returnType methodName(parameterType parameterName) {
    // Method body
    return value; // if returnType is not void
}
```

#### **Method Components:**
1. **Access Modifier** - Controls visibility (public, private, protected)
2. **Return Type** - Data type of returned value (void if no return)
3. **Method Name** - Identifier following camelCase convention
4. **Parameters** - Input values (optional)
5. **Method Body** - Code to execute

#### **Types of Methods:**

##### **1. Instance Methods**
```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public double multiply(double x, double y) {
        return x * y;
    }
    
    public void displayResult(double result) {
        System.out.println("Result: " + result);
    }
}
```

##### **2. Getter Methods (Accessors)**
```java
public class Person {
    private String name;
    private int age;
    
    public String getName() {  // Getter
        return name;
    }
    
    public int getAge() {      // Getter
        return age;
    }
}
```

##### **3. Setter Methods (Mutators)**
```java
public class Person {
    private String name;
    private int age;
    
    public void setName(String name) {  // Setter
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }
    
    public void setAge(int age) {       // Setter
        if (age >= 0 && age <= 150) {
            this.age = age;
        }
    }
}
```

### **Constructors**
Constructors are special methods used to initialize objects when they are created.

#### **Constructor Characteristics:**
- **Same name** as the class
- **No return type** (not even void)
- **Called automatically** when object is created with `new`
- **Can be overloaded** (multiple versions)

#### **Types of Constructors:**

##### **1. Default Constructor**
```java
public class Student {
    private String name;
    private int id;
    
    // Default constructor (no parameters)
    public Student() {
        name = "Unknown";
        id = 0;
        System.out.println("Default constructor called");
    }
}
```

##### **2. Parameterized Constructor**
```java
public class Student {
    private String name;
    private int id;
    
    // Parameterized constructor
    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        System.out.println("Parameterized constructor called");
    }
}
```

##### **3. Multiple Constructors (Constructor Overloading)**
```java
public class BankAccount {
    private String accountNumber;
    private String holderName;
    private double balance;
    
    // Default constructor
    public BankAccount() {
        this.accountNumber = "000000";
        this.holderName = "Unknown";
        this.balance = 0.0;
    }
    
    // Constructor with account number only
    public BankAccount(String accountNumber) {
        this.accountNumber = accountNumber;
        this.holderName = "Unknown";
        this.balance = 0.0;
    }
    
    // Constructor with account number and holder name
    public BankAccount(String accountNumber, String holderName) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = 0.0;
    }
    
    // Full constructor
    public BankAccount(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }
}
```

#### **Constructor Chaining:**
```java
public class Rectangle {
    private double length;
    private double width;
    
    // Default constructor calls parameterized constructor
    public Rectangle() {
        this(1.0, 1.0);  // Calls Rectangle(double, double)
    }
    
    // Square constructor (same length and width)
    public Rectangle(double side) {
        this(side, side); // Calls Rectangle(double, double)
    }
    
    // Full constructor
    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
}
```

---

## Instance Variables and Class Variables

### **Instance Variables (Non-static)**
- **Belong to individual objects**
- **Each object has its own copy**
- **Stored in heap memory**
- **Accessed through object reference**

### **Class Variables (Static)**
- **Belong to the class itself**
- **Shared by all instances**
- **Stored in method area**
- **Accessed through class name**

### **Example:**
```java
public class Counter {
    // Instance variable - each object has its own copy
    private int instanceCount;
    
    // Class variable - shared by all objects
    private static int totalObjects = 0;
    
    public Counter() {
        instanceCount = 0;
        totalObjects++;  // Increment shared counter
    }
    
    public void increment() {
        instanceCount++;
    }
    
    public int getInstanceCount() {
        return instanceCount;
    }
    
    public static int getTotalObjects() {
        return totalObjects;
    }
}

// Usage example:
Counter c1 = new Counter();
Counter c2 = new Counter();

c1.increment();
c1.increment();

c2.increment();

System.out.println("C1 count: " + c1.getInstanceCount());  // 2
System.out.println("C2 count: " + c2.getInstanceCount());  // 1
System.out.println("Total objects: " + Counter.getTotalObjects()); // 2
```

---

## Access Modifiers

### **Access Control in Java:**

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | ✓ | ✓ | ✓ | ✓ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| default (package) | ✓ | ✓ | ✗ | ✗ |
| `private` | ✓ | ✗ | ✗ | ✗ |

### **1. Public**
```java
public class PublicExample {
    public String publicVariable = "Accessible everywhere";
    
    public void publicMethod() {
        System.out.println("This method can be called from anywhere");
    }
}
```

### **2. Private**
```java
public class PrivateExample {
    private String secretData = "Only accessible within this class";
    
    private void secretMethod() {
        System.out.println("Only this class can call this method");
    }
    
    // Public method to access private data
    public String getSecretData() {
        return secretData;
    }
}
```

### **3. Protected**
```java
public class ProtectedExample {
    protected String protectedData = "Accessible in package and subclasses";
    
    protected void protectedMethod() {
        System.out.println("Package and subclass access");
    }
}
```

### **4. Package (Default)**
```java
class PackageExample {  // No access modifier = package access
    String packageData = "Accessible within same package";
    
    void packageMethod() {
        System.out.println("Same package access only");
    }
}
```

### **Encapsulation Best Practice:**
```java
public class EncapsulatedClass {
    // Private data (encapsulated)
    private String name;
    private int age;
    
    // Public constructors
    public EncapsulatedClass(String name, int age) {
        setName(name);  // Use setter for validation
        setAge(age);    // Use setter for validation
    }
    
    // Public getters (controlled read access)
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    // Public setters (controlled write access with validation)
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }
    
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }
}
```

---

## The `this` Keyword

### **What is `this`?**
The `this` keyword refers to the current object instance.

### **Uses of `this`:**

#### **1. Resolve Name Conflicts**
```java
public class Person {
    private String name;
    private int age;
    
    public void setName(String name) {
        this.name = name;  // this.name refers to instance variable
                          // name refers to parameter
    }
    
    public void setAge(int age) {
        this.age = age;   // Distinguishes parameter from instance variable
    }
}
```

#### **2. Call Other Constructors**
```java
public class Rectangle {
    private double length;
    private double width;
    
    public Rectangle() {
        this(1.0, 1.0);  // Call Rectangle(double, double)
    }
    
    public Rectangle(double side) {
        this(side, side); // Call Rectangle(double, double)
    }
    
    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
}
```

#### **3. Pass Current Object as Parameter**
```java
public class Car {
    private String brand;
    
    public Car(String brand) {
        this.brand = brand;
    }
    
    public void registerCar(CarRegistry registry) {
        registry.addCar(this);  // Pass current object
    }
}

public class CarRegistry {
    public void addCar(Car car) {
        // Register the car
        System.out.println("Car registered: " + car.getBrand());
    }
}
```

#### **4. Return Current Object (Method Chaining)**
```java
public class StringBuilder {
    private String content = "";
    
    public StringBuilder append(String text) {
        content += text;
        return this;  // Return current object for chaining
    }
    
    public StringBuilder reverse() {
        content = new StringBuilder(content).reverse().toString();
        return this;  // Enable method chaining
    }
}

// Usage:
StringBuilder sb = new StringBuilder();
sb.append("Hello").append(" ").append("World").reverse();
```

---

## Method Overloading

### **What is Method Overloading?**
Method overloading allows multiple methods with the same name but different parameters.

### **Rules for Method Overloading:**
1. **Same method name**
2. **Different parameter lists** (number, type, or order)
3. **Return type can be different** (but not the only difference)
4. **Access modifiers can be different**

### **Examples:**

#### **1. Different Number of Parameters**
```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public int add(int a, int b, int c) {
        return a + b + c;
    }
    
    public int add(int a, int b, int c, int d) {
        return a + b + c + d;
    }
}
```

#### **2. Different Parameter Types**
```java
public class Display {
    public void show(int number) {
        System.out.println("Integer: " + number);
    }
    
    public void show(double number) {
        System.out.println("Double: " + number);
    }
    
    public void show(String text) {
        System.out.println("String: " + text);
    }
    
    public void show(boolean flag) {
        System.out.println("Boolean: " + flag);
    }
}
```

#### **3. Different Parameter Order**
```java
public class PersonInfo {
    public void displayInfo(String name, int age) {
        System.out.println("Name: " + name + ", Age: " + age);
    }
    
    public void displayInfo(int age, String name) {
        System.out.println("Age: " + age + ", Name: " + name);
    }
}
```

#### **4. Constructor Overloading**
```java
public class Student {
    private String name;
    private int id;
    private String email;
    
    // Default constructor
    public Student() {
        this("Unknown", 0, "no-email@example.com");
    }
    
    // Name only
    public Student(String name) {
        this(name, 0, "no-email@example.com");
    }
    
    // Name and ID
    public Student(String name, int id) {
        this(name, id, "no-email@example.com");
    }
    
    // Full constructor
    public Student(String name, int id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }
}
```

---

## Static Members

### **Static Variables (Class Variables)**
```java
public class MathUtils {
    public static final double PI = 3.14159;  // Static constant
    private static int calculationCount = 0;   // Static variable
    
    // Static method
    public static double calculateCircleArea(double radius) {
        calculationCount++;  // Modify static variable
        return PI * radius * radius;
    }
    
    public static int getCalculationCount() {
        return calculationCount;
    }
}

// Usage - no object creation needed
double area = MathUtils.calculateCircleArea(5.0);
System.out.println("Calculations performed: " + MathUtils.getCalculationCount());
```

### **Static Methods**
```java
public class StringUtils {
    // Static utility method
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public static int countWords(String str) {
        if (isEmpty(str)) return 0;
        return str.trim().split("\\s+").length;
    }
}

// Usage:
String text = "hello world";
System.out.println(StringUtils.capitalize(text));  // "Hello world"
System.out.println(StringUtils.countWords(text));  // 2
```

### **Static Block (Static Initializer)**
```java
public class DatabaseConfig {
    private static String databaseUrl;
    private static String username;
    
    // Static block - executed when class is first loaded
    static {
        System.out.println("Loading database configuration...");
        databaseUrl = "jdbc:mysql://localhost:3306/mydb";
        username = "admin";
        System.out.println("Database configuration loaded.");
    }
    
    public static String getDatabaseUrl() {
        return databaseUrl;
    }
}
```

### **Rules for Static Members:**
1. **Static methods cannot access instance variables directly**
2. **Static methods cannot call instance methods directly**
3. **Instance methods can access static members**
4. **`this` keyword cannot be used in static context**
5. **Static members belong to class, not instances**

---

## Practical Applications

### **Example 1: Bank Account Management System**
```java
public class BankAccount {
    // Static variables
    private static int nextAccountNumber = 1000;
    private static double interestRate = 0.03;
    
    // Instance variables
    private int accountNumber;
    private String holderName;
    private double balance;
    
    // Constructor
    public BankAccount(String holderName, double initialDeposit) {
        this.accountNumber = nextAccountNumber++;
        this.holderName = holderName;
        this.balance = initialDeposit;
    }
    
    // Instance methods
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, balance);
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }
    
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        System.out.printf("Interest applied: $%.2f. New balance: $%.2f%n", interest, balance);
    }
    
    // Getters
    public int getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    
    // Static methods
    public static void setInterestRate(double rate) {
        if (rate >= 0 && rate <= 1.0) {
            interestRate = rate;
        }
    }
    
    public static double getInterestRate() {
        return interestRate;
    }
}
```

---

## Key Takeaways

### **Essential Concepts Mastered:**
✅ **Classes and Objects** - Blueprint vs instances, object creation  
✅ **Methods** - Behavior definition, parameters, return values  
✅ **Constructors** - Object initialization, overloading  
✅ **Instance vs Static** - Object-level vs class-level members  
✅ **Access Modifiers** - Encapsulation and data protection  
✅ **The `this` keyword** - Self-reference and disambiguation  
✅ **Method Overloading** - Same name, different signatures  

### **OOP Benefits Realized:**
- **Encapsulation** - Data hiding and controlled access
- **Modularity** - Organized, reusable code components
- **Maintainability** - Easier to update and debug
- **Real-world Modeling** - Natural representation of entities

### **Best Practices:**
- Use **private** for data, **public** for interface
- Provide **getters/setters** for controlled access
- **Validate inputs** in setters and constructors
- Use **meaningful names** for classes, methods, variables
- Follow **single responsibility principle** - one class, one purpose

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Build complete programs using classes and objects
- Move to [Advanced OOP](../04-OOP-Advanced/README.md) concepts

---

**Continue to: [Chapter 4: Advanced OOP →](../04-OOP-Advanced/README.md)**