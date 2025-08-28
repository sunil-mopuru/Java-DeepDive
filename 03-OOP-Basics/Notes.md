# OOP Basics - Quick Reference Notes

## 🏗️ Class Structure

### Basic Class Template
```java
public class ClassName {
    // Fields (attributes)
    private dataType fieldName;
    
    // Constructors
    public ClassName(parameters) {
        this.fieldName = parameter;
    }
    
    // Methods
    public returnType methodName(parameters) {
        return value;
    }
    
    // Getters and Setters
    public dataType getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(dataType fieldName) {
        this.fieldName = fieldName;
    }
}
```

## 🔐 Access Modifiers

| Modifier | Class | Package | Subclass | World |
|----------|-------|---------|----------|-------|
| `public` | ✓ | ✓ | ✓ | ✓ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| default | ✓ | ✓ | ✗ | ✗ |
| `private` | ✓ | ✗ | ✗ | ✗ |

## 🏗️ Constructors

### Types of Constructors
```java
public class Person {
    private String name;
    private int age;
    
    // Default constructor
    public Person() {
        this("Unknown", 0);
    }
    
    // Parameterized constructor
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

### Constructor Rules
- Same name as class
- No return type
- Called with `new` keyword
- Can be overloaded
- Use `this()` for constructor chaining

## ⚡ Static vs Instance

### Instance Members
```java
public class Counter {
    private int count = 0;        // Instance variable
    
    public void increment() {     // Instance method
        count++;
    }
}
```

### Static Members
```java
public class MathUtils {
    public static final double PI = 3.14159;  // Static constant
    private static int calculations = 0;       // Static variable
    
    public static double circleArea(double r) { // Static method
        calculations++;
        return PI * r * r;
    }
}
```

## 🔄 Method Overloading

### Rules for Overloading
- Same method name
- Different parameter lists (number, type, or order)
- Return type can differ

### Examples
```java
public class Calculator {
    public int add(int a, int b) { return a + b; }
    public int add(int a, int b, int c) { return a + b + c; }
    public double add(double a, double b) { return a + b; }
}
```

## 🎯 The `this` Keyword

### Uses of `this`
```java
public class Person {
    private String name;
    
    public Person(String name) {
        this.name = name;        // 1. Field disambiguation
    }
    
    public Person() {
        this("Unknown");         // 2. Constructor chaining
    }
    
    public Person setName(String name) {
        this.name = name;
        return this;             // 3. Method chaining
    }
}
```

## 📦 Encapsulation Best Practices

### Data Protection
```java
public class BankAccount {
    private double balance;      // Private field
    
    public double getBalance() { // Controlled read access
        return balance;
    }
    
    public void deposit(double amount) { // Controlled modification
        if (amount > 0) {
            balance += amount;
        }
    }
}
```

## 🎨 Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Class | PascalCase | `BankAccount` |
| Method | camelCase | `calculateInterest()` |
| Variable | camelCase | `accountNumber` |
| Constant | UPPER_CASE | `MAX_BALANCE` |

## 🔍 Object Creation and Usage

### Object Lifecycle
```java
// 1. Declaration
Student student;

// 2. Instantiation  
student = new Student("John", 123);

// 3. Usage
student.setGrade(85);
String name = student.getName();

// 4. Garbage collection (automatic)
student = null; // Remove reference
```

## ⚠️ Common Mistakes

### 1. Not Using `this` When Needed
```java
// Wrong
public void setName(String name) {
    name = name; // Assigns parameter to itself
}

// Correct
public void setName(String name) {
    this.name = name; // Assigns to field
}
```

### 2. Missing Validation
```java
// Better
public void setAge(int age) {
    if (age >= 0 && age <= 150) {
        this.age = age;
    } else {
        throw new IllegalArgumentException("Invalid age");
    }
}
```

### 3. Breaking Encapsulation
```java
// Wrong
public int[] scores; // Direct access

// Better  
private int[] scores;
public int[] getScores() {
    return scores.clone(); // Return copy
}
```

## 🎯 Design Guidelines

### Single Responsibility
- Each class should have one clear purpose
- Methods should do one thing well

### Encapsulation
- Make fields private
- Provide public methods for controlled access
- Validate all inputs

### Naming
- Use descriptive names
- Follow Java conventions
- Be consistent throughout code

## 📊 Memory Management

### Stack vs Heap
```java
Student student = new Student("John"); 
// 'student' reference → Stack
// Student object → Heap
```

### Garbage Collection
- Objects eligible when no references exist
- Automatic memory management
- Use `null` to remove references when done

---
**Next Topic:** [Advanced OOP →](../../04-OOP-Advanced/Notes.md)