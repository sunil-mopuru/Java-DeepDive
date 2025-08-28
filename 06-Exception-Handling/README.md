# Chapter 6: Exception Handling

## 📚 Table of Contents
1. [Introduction to Exceptions](#introduction-to-exceptions)
2. [Exception Hierarchy](#exception-hierarchy)
3. [Try-Catch-Finally](#try-catch-finally)
4. [Throw and Throws](#throw-and-throws)
5. [Custom Exceptions](#custom-exceptions)
6. [Best Practices](#best-practices)

---

## Introduction to Exceptions

### **What are Exceptions?**
Exceptions are events that occur during program execution that disrupt the normal flow of instructions.

### **Types of Exceptions:**
- **Checked Exceptions** - Must be handled or declared
- **Unchecked Exceptions** - Runtime exceptions
- **Errors** - System-level problems

---

## Exception Hierarchy

```
Throwable
├── Exception (Checked)
│   ├── IOException
│   ├── SQLException
│   └── RuntimeException (Unchecked)
│       ├── NullPointerException
│       ├── IllegalArgumentException
│       └── ArrayIndexOutOfBoundsException
└── Error
    ├── OutOfMemoryError
    └── StackOverflowError
```

---

## Try-Catch-Finally

### **Basic Syntax:**
```java
try {
    // Risky code
    int result = 10 / 0;
} catch (ArithmeticException e) {
    // Handle specific exception
    System.out.println("Division by zero: " + e.getMessage());
} catch (Exception e) {
    // Handle any other exception
    System.out.println("General error: " + e.getMessage());
} finally {
    // Always executes
    System.out.println("Cleanup code");
}
```

### **Multiple Catch Blocks:**
```java
try {
    String str = null;
    int length = str.length();  // NullPointerException
    int[] arr = new int[5];
    arr[10] = 100;             // ArrayIndexOutOfBoundsException
} catch (NullPointerException e) {
    System.out.println("Null reference error");
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Array index error");
} catch (Exception e) {
    System.out.println("Other error: " + e.getClass().getSimpleName());
}
```

### **Try-with-Resources (Java 7+):**
```java
try (FileReader file = new FileReader("data.txt");
     BufferedReader reader = new BufferedReader(file)) {
    
    String line = reader.readLine();
    System.out.println(line);
    
} catch (IOException e) {
    System.out.println("File error: " + e.getMessage());
}
// Resources automatically closed
```

---

## Throw and Throws

### **Throw Statement:**
```java
public void validateAge(int age) {
    if (age < 0 || age > 150) {
        throw new IllegalArgumentException("Invalid age: " + age);
    }
}
```

### **Throws Declaration:**
```java
public void readFile(String filename) throws IOException {
    FileReader file = new FileReader(filename);
    // Method declares it might throw IOException
}

public void caller() {
    try {
        readFile("data.txt");
    } catch (IOException e) {
        System.out.println("File not found");
    }
}
```

---

## Custom Exceptions

### **Creating Custom Exception:**
```java
public class InsufficientFundsException extends Exception {
    private double amount;
    private double balance;
    
    public InsufficientFundsException(double amount, double balance) {
        super("Insufficient funds: Required " + amount + ", Available " + balance);
        this.amount = amount;
        this.balance = balance;
    }
    
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
}

// Usage
public class BankAccount {
    private double balance;
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }
        balance -= amount;
    }
}
```

---

## Best Practices

### **1. Be Specific:**
```java
// Good - specific exception
catch (FileNotFoundException e) {
    // Handle file not found
}

// Avoid - too general
catch (Exception e) {
    // Catches everything
}
```

### **2. Don't Ignore Exceptions:**
```java
// Bad
try {
    riskyOperation();
} catch (Exception e) {
    // Silently ignore - very bad!
}

// Good
try {
    riskyOperation();
} catch (Exception e) {
    logger.error("Operation failed", e);
    throw new ServiceException("Unable to complete operation", e);
}
```

### **3. Use Finally for Cleanup:**
```java
FileInputStream file = null;
try {
    file = new FileInputStream("data.txt");
    // Process file
} catch (IOException e) {
    System.out.println("File error");
} finally {
    if (file != null) {
        try {
            file.close();
        } catch (IOException e) {
            System.out.println("Error closing file");
        }
    }
}
```

### **4. Validate Input:**
```java
public void processData(String data) {
    if (data == null) {
        throw new IllegalArgumentException("Data cannot be null");
    }
    if (data.trim().isEmpty()) {
        throw new IllegalArgumentException("Data cannot be empty");
    }
    // Process valid data
}
```

---

## Key Takeaways

### **Essential Concepts:**
✅ **Exception Types** - Checked vs unchecked exceptions  
✅ **Try-Catch-Finally** - Proper exception handling structure  
✅ **Throw/Throws** - Creating and declaring exceptions  
✅ **Custom Exceptions** - Building domain-specific exceptions  
✅ **Best Practices** - Proper exception handling techniques  

### **Exception Handling Rules:**
- **Handle specific exceptions** before general ones
- **Always clean up resources** in finally or try-with-resources
- **Don't catch and ignore** exceptions silently
- **Document exceptions** your methods can throw
- **Use exceptions for exceptional conditions**, not control flow

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Learn about logging frameworks for better error tracking
- Move to [String Handling](../07-String-Handling/README.md)

---

**Continue to: [Chapter 7: String Handling →](../07-String-Handling/README.md)**