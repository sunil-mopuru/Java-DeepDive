# Exception Handling - Quick Notes

## 🎯 **Key Concepts**

### **Exception Types**
- **Checked Exceptions**: Must be handled (IOException, SQLException)
- **Unchecked Exceptions**: Runtime exceptions (NullPointerException, ArrayIndexOutOfBoundsException)
- **Errors**: System-level problems (OutOfMemoryError, StackOverflowError)

### **Exception Hierarchy**
```
Throwable
├── Error (unchecked)
└── Exception
    ├── RuntimeException (unchecked)
    └── Other exceptions (checked)
```

---

## 🛠️ **Basic Syntax**

### **Try-Catch-Finally**
```java
try {
    // risky code
} catch (SpecificException e) {
    // handle specific exception
} catch (Exception e) {
    // handle general exception
} finally {
    // always executes
}
```

### **Try-with-Resources**
```java
try (FileReader file = new FileReader("data.txt")) {
    // use file
} catch (IOException e) {
    // handle exception
}
// file automatically closed
```

### **Throwing Exceptions**
```java
public void method() throws IOException {
    if (condition) {
        throw new IOException("Error message");
    }
}
```

---

## 📝 **Best Practices**

1. **Catch Specific Exceptions** first, then general ones
2. **Use try-with-resources** for automatic resource management  
3. **Don't catch and ignore** - at least log the exception
4. **Create custom exceptions** for business logic
5. **Document exceptions** in method signatures
6. **Clean up resources** in finally block (if not using try-with-resources)

---

## 🔧 **Common Patterns**

### **Custom Exception**
```java
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}
```

### **Exception Chaining**
```java
catch (SQLException e) {
    throw new ServiceException("Service failed", e);
}
```

### **Multiple Catch (Java 7+)**
```java
catch (IOException | SQLException e) {
    // handle both exception types
}
```

---

## ⚡ **Quick Reference**

| Pattern | Use Case |
|---------|----------|
| `try-catch` | Handle exceptions |
| `try-finally` | Cleanup resources |
| `try-with-resources` | Auto-close resources |
| `throws` | Declare exceptions |
| `throw` | Manually throw |
| `printStackTrace()` | Debug information |
| `getMessage()` | Exception message |

---

**Next:** [String Handling](../07-String-Handling/README.md)