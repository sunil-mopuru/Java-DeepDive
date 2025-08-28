# Chapter 1: Java Basics

## 📚 Table of Contents
1. [Introduction to Java](#introduction-to-java)
2. [Java Environment Setup](#java-environment-setup)
3. [Java Syntax and Structure](#java-syntax-and-structure)
4. [Variables and Data Types](#variables-and-data-types)
5. [Operators](#operators)
6. [Input and Output](#input-and-output)
7. [Comments and Documentation](#comments-and-documentation)
8. [Practical Examples](#practical-examples)
9. [Key Takeaways](#key-takeaways)

---

## Introduction to Java

### What is Java?
Java is a **high-level, object-oriented, platform-independent** programming language developed by Sun Microsystems (now Oracle) in 1995.

### Key Features of Java:

#### 1. **Platform Independence**
- **"Write Once, Run Anywhere" (WORA)**
- Java code compiles to bytecode, which runs on the Java Virtual Machine (JVM)
- The same Java program can run on Windows, Linux, macOS without modification

#### 2. **Object-Oriented Programming (OOP)**
- Everything in Java is an object (except primitives)
- Supports encapsulation, inheritance, polymorphism, and abstraction

#### 3. **Automatic Memory Management**
- Garbage Collection automatically manages memory
- No need for manual memory allocation/deallocation

#### 4. **Strong Type System**
- Variables must be declared with specific types
- Type checking happens at compile-time

#### 5. **Multithreading Support**
- Built-in support for concurrent programming
- Easy to create and manage multiple threads

---

## Java Environment Setup

### Components of Java Development:

#### 1. **JDK (Java Development Kit)**
- Complete development environment
- Includes JRE + development tools (compiler, debugger)

#### 2. **JRE (Java Runtime Environment)**
- Runtime environment for Java applications
- Includes JVM + standard libraries

#### 3. **JVM (Java Virtual Machine)**
- Executes Java bytecode
- Platform-specific implementation

```
Source Code (.java) → Compiler (javac) → Bytecode (.class) → JVM → Machine Code
```

---

## Java Syntax and Structure

### Basic Java Program Structure:

```java
// Package declaration (optional)
package com.example;

// Import statements (optional)
import java.util.Scanner;

// Class declaration (required)
public class MyProgram {
    
    // Main method (entry point)
    public static void main(String[] args) {
        // Program logic goes here
        System.out.println("Hello, World!");
    }
}
```

### Key Syntax Rules:

#### 1. **Case Sensitivity**
- Java is case-sensitive: `Hello` ≠ `hello`

#### 2. **Class Names**
- Must start with uppercase letter (PascalCase)
- Example: `MyClass`, `StudentRecord`

#### 3. **Method Names**
- Start with lowercase letter (camelCase)
- Example: `getName()`, `calculateTotal()`

#### 4. **Statements**
- End with semicolon (`;`)
- Example: `int x = 5;`

#### 5. **Blocks**
- Code blocks enclosed in curly braces `{ }`

---

## Variables and Data Types

### What is a Variable?
A variable is a **named storage location** in memory that holds data.

### Variable Declaration Syntax:
```java
dataType variableName = value;
```

### Java Data Types:

#### 1. **Primitive Data Types** (8 types)

##### **Integer Types:**
```java
byte age = 25;           // 8-bit: -128 to 127
short year = 2024;       // 16-bit: -32,768 to 32,767
int population = 50000;  // 32-bit: -2^31 to 2^31-1
long distance = 123456789L; // 64-bit: -2^63 to 2^63-1
```

##### **Floating-Point Types:**
```java
float price = 19.99f;    // 32-bit: ~7 decimal places
double salary = 75000.50; // 64-bit: ~15 decimal places
```

##### **Character Type:**
```java
char grade = 'A';        // 16-bit: Unicode characters
char symbol = '\u0041';  // Unicode for 'A'
```

##### **Boolean Type:**
```java
boolean isActive = true;  // true or false only
boolean isComplete = false;
```

#### 2. **Reference Data Types**
```java
String name = "John Doe";        // String object
int[] numbers = {1, 2, 3, 4, 5}; // Array object
Scanner input = new Scanner(System.in); // Custom object
```

### Memory Allocation:

#### **Stack Memory:**
- Stores primitive variables and references
- Fast access, limited size
- Automatically managed

#### **Heap Memory:**
- Stores objects and arrays
- Larger size, slower access
- Managed by Garbage Collector

### Variable Initialization:

#### **Default Values:**
```java
public class DefaultValues {
    static byte b;       // 0
    static short s;      // 0
    static int i;        // 0
    static long l;       // 0L
    static float f;      // 0.0f
    static double d;     // 0.0
    static char c;       // '\u0000' (null character)
    static boolean bool; // false
    static String str;   // null
}
```

#### **Local Variables:**
- Must be initialized before use
- No default values

---

## Operators

### 1. **Arithmetic Operators**

```java
int a = 10, b = 3;

int addition = a + b;        // 13
int subtraction = a - b;     // 7
int multiplication = a * b;  // 30
int division = a / b;        // 3 (integer division)
int remainder = a % b;       // 1 (modulus)

// Increment/Decrement
int x = 5;
x++;    // Post-increment: use value, then increment
++x;    // Pre-increment: increment, then use value
x--;    // Post-decrement
--x;    // Pre-decrement
```

### 2. **Assignment Operators**

```java
int x = 10;

x += 5;  // x = x + 5  → x = 15
x -= 3;  // x = x - 3  → x = 12
x *= 2;  // x = x * 2  → x = 24
x /= 4;  // x = x / 4  → x = 6
x %= 3;  // x = x % 3  → x = 0
```

### 3. **Comparison Operators**

```java
int a = 10, b = 20;

boolean isEqual = (a == b);      // false
boolean isNotEqual = (a != b);   // true
boolean isGreater = (a > b);     // false
boolean isLess = (a < b);        // true
boolean isGreaterEqual = (a >= b); // false
boolean isLessEqual = (a <= b);  // true
```

### 4. **Logical Operators**

```java
boolean x = true, y = false;

boolean and = x && y;  // false (Logical AND)
boolean or = x || y;   // true  (Logical OR)
boolean not = !x;      // false (Logical NOT)

// Short-circuit evaluation
boolean result = (x && someMethod()); // someMethod() called only if x is true
boolean result2 = (y || someMethod()); // someMethod() called only if y is false
```

### 5. **Bitwise Operators**

```java
int a = 12; // Binary: 1100
int b = 7;  // Binary: 0111

int bitwiseAnd = a & b;  // 4  (0100)
int bitwiseOr = a | b;   // 15 (1111)
int bitwiseXor = a ^ b;  // 11 (1011)
int bitwiseNot = ~a;     // -13 (two's complement)

int leftShift = a << 2;  // 48 (110000)
int rightShift = a >> 2; // 3  (0011)
```

### 6. **Ternary Operator**

```java
// Syntax: condition ? value_if_true : value_if_false
int age = 20;
String category = (age >= 18) ? "Adult" : "Minor";

// Nested ternary
int score = 85;
String grade = (score >= 90) ? "A" : 
               (score >= 80) ? "B" : 
               (score >= 70) ? "C" : "F";
```

### **Operator Precedence** (Highest to Lowest):
1. Postfix: `x++`, `x--`
2. Unary: `++x`, `--x`, `!`, `~`
3. Multiplicative: `*`, `/`, `%`
4. Additive: `+`, `-`
5. Shift: `<<`, `>>`, `>>>`
6. Relational: `<`, `>`, `<=`, `>=`
7. Equality: `==`, `!=`
8. Bitwise AND: `&`
9. Bitwise XOR: `^`
10. Bitwise OR: `|`
11. Logical AND: `&&`
12. Logical OR: `||`
13. Ternary: `? :`
14. Assignment: `=`, `+=`, `-=`, etc.

---

## Input and Output

### **Output to Console:**

#### **System.out Methods:**
```java
System.out.print("Hello ");     // No newline
System.out.println("World!");   // With newline
System.out.printf("Age: %d, Score: %.2f%n", 25, 87.5); // Formatted output
```

#### **Print Formatting:**
```java
int age = 25;
double salary = 75000.50;
String name = "John";

// Printf formatting
System.out.printf("Name: %s, Age: %d, Salary: $%.2f%n", name, age, salary);

// String formatting
String formatted = String.format("Hello %s, you are %d years old", name, age);
System.out.println(formatted);
```

### **Input from Console:**

#### **Using Scanner Class:**
```java
import java.util.Scanner;

public class InputExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();  // Read entire line
        
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();       // Read integer
        
        System.out.print("Enter your salary: ");
        double salary = scanner.nextDouble(); // Read double
        
        System.out.printf("Hello %s, Age: %d, Salary: $%.2f%n", 
                         name, age, salary);
        
        scanner.close(); // Always close scanner
    }
}
```

#### **Scanner Methods:**
- `nextLine()` - Read entire line (including spaces)
- `next()` - Read single word
- `nextInt()` - Read integer
- `nextDouble()` - Read double
- `nextFloat()` - Read float
- `nextBoolean()` - Read boolean
- `hasNext()` - Check if input available

---

## Comments and Documentation

### **Types of Comments:**

#### 1. **Single-line Comments:**
```java
// This is a single-line comment
int x = 5; // Comment at end of line
```

#### 2. **Multi-line Comments:**
```java
/*
 * This is a multi-line comment
 * It can span multiple lines
 * Useful for longer explanations
 */
```

#### 3. **Javadoc Comments:**
```java
/**
 * This class demonstrates basic Java concepts
 * 
 * @author John Doe
 * @version 1.0
 * @since 2024
 */
public class BasicConcepts {
    
    /**
     * Calculates the area of a rectangle
     * 
     * @param length the length of rectangle
     * @param width the width of rectangle
     * @return the area as double
     */
    public static double calculateArea(double length, double width) {
        return length * width;
    }
}
```

### **Documentation Best Practices:**
- Comment **why**, not **what**
- Keep comments up-to-date with code changes
- Use meaningful variable and method names
- Document complex algorithms and business logic

---

## Practical Examples

See the [examples](./examples/) directory for complete working programs demonstrating these concepts.

---

## Key Takeaways

### **Essential Concepts Mastered:**
✅ **Java Environment** - Understanding JDK, JRE, and JVM  
✅ **Program Structure** - Basic syntax and organization  
✅ **Variables** - Declaration, initialization, and scope  
✅ **Data Types** - Primitives vs. references, memory allocation  
✅ **Operators** - Arithmetic, logical, comparison, and precedence  
✅ **Input/Output** - Console interaction using Scanner  
✅ **Documentation** - Comments and Javadoc standards  

### **Next Steps:**
- Practice with [exercises](./exercises/) 
- Move to [Control Flow](../02-Control-Flow/README.md) concepts
- Start building small programs combining these concepts

---

**Continue to: [Chapter 2: Control Flow →](../02-Control-Flow/README.md)**