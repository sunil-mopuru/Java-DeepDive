# Chapter 7: String Handling

## 📚 Table of Contents
1. [Introduction to Strings](#introduction-to-strings)
2. [String Class Methods](#string-class-methods)
3. [StringBuilder and StringBuffer](#stringbuilder-and-stringbuffer)
4. [String Formatting](#string-formatting)
5. [Regular Expressions](#regular-expressions)
6. [Best Practices](#best-practices)

---

## Introduction to Strings

### **String Characteristics:**
- **Immutable** - Cannot be changed after creation
- **Reference Type** - Objects stored in heap
- **String Pool** - JVM optimization for string literals
- **UTF-16 Encoding** - Supports Unicode characters

### **String Creation:**
```java
// String literals (preferred)
String str1 = "Hello World";
String str2 = "Hello World";  // Same object in string pool

// Using new keyword (creates new object)
String str3 = new String("Hello World");  // Different object

// From char array
char[] chars = {'H', 'e', 'l', 'l', 'o'};
String str4 = new String(chars);

// From bytes
byte[] bytes = {72, 101, 108, 108, 111};
String str5 = new String(bytes);
```

---

## String Class Methods

### **Common String Operations:**
```java
String text = "  Hello World  ";

// Length and character access
int length = text.length();                    // 15
char ch = text.charAt(7);                      // 'W'
int index = text.indexOf('W');                 // 8
int lastIndex = text.lastIndexOf('l');         // 11

// Case operations
String upper = text.toUpperCase();             // "  HELLO WORLD  "
String lower = text.toLowerCase();             // "  hello world  "

// Trimming whitespace
String trimmed = text.trim();                  // "Hello World"
String stripped = text.strip();               // Java 11+, handles Unicode

// Substring operations
String sub1 = text.substring(2);               // "Hello World  "
String sub2 = text.substring(2, 7);           // "Hello"

// Replacement
String replaced = text.replace(" ", "_");      // "__Hello_World__"
String regex = text.replaceAll("\\s+", " ");  // " Hello World "
String first = text.replaceFirst("l", "L");   // "  HeLlo World  "

// Splitting and joining
String[] words = "apple,banana,cherry".split(",");
String joined = String.join(", ", words);     // "apple, banana, cherry"

// Comparison
boolean equals = "hello".equals("hello");      // true
boolean equalsIgnoreCase = "HELLO".equalsIgnoreCase("hello"); // true
int comparison = "apple".compareTo("banana");  // negative (apple < banana)

// Checking content
boolean starts = text.startsWith("  Hello");  // true
boolean ends = text.endsWith("World  ");      // true
boolean contains = text.contains("World");    // true
boolean empty = "".isEmpty();                 // true
boolean blank = "   ".isBlank();              // true (Java 11+)
```

### **String Searching:**
```java
String data = "The quick brown fox jumps over the lazy dog";

// Find positions
int pos1 = data.indexOf("fox");               // 16
int pos2 = data.indexOf("the", 10);           // 31 (search from index 10)
int pos3 = data.lastIndexOf("o");             // 41

// Check existence
boolean hasFox = data.contains("fox");        // true
boolean hasPattern = data.matches(".*fox.*"); // true (regex)
```

---

## StringBuilder and StringBuffer

### **Why Use StringBuilder?**
Strings are immutable, so concatenation creates new objects:
```java
// Inefficient - creates many intermediate objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i + ", ";  // Creates new string each iteration
}

// Efficient - uses mutable buffer
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(", ");
}
String result = sb.toString();
```

### **StringBuilder Methods:**
```java
StringBuilder sb = new StringBuilder("Hello");

// Append operations
sb.append(" World");                    // "Hello World"
sb.append('!');                        // "Hello World!"
sb.append(123);                        // "Hello World!123"

// Insert operations
sb.insert(5, " Beautiful");            // "Hello Beautiful World!123"
sb.insert(0, "Say: ");                 // "Say: Hello Beautiful World!123"

// Delete operations
sb.delete(5, 15);                      // "Say: Hello World!123"
sb.deleteCharAt(sb.length() - 1);      // "Say: Hello World!12"

// Replace operations
sb.replace(6, 11, "Java");             // "Say: Java World!12"

// Reverse
sb.reverse();                          // "21!dlroW avaJ :yaS"

// Capacity management
int capacity = sb.capacity();           // Current capacity
sb.ensureCapacity(100);                // Ensure at least 100 chars
sb.trimToSize();                       // Minimize capacity

// Convert to string
String final = sb.toString();
```

### **StringBuilder vs StringBuffer:**

| Feature | StringBuilder | StringBuffer |
|---------|---------------|--------------|
| **Thread Safety** | Not synchronized | Synchronized |
| **Performance** | Faster | Slower |
| **Use Case** | Single-threaded | Multi-threaded |

```java
// StringBuilder - single-threaded applications
StringBuilder sb = new StringBuilder();

// StringBuffer - multi-threaded applications
StringBuffer sbf = new StringBuffer();
```

---

## String Formatting

### **printf-style Formatting:**
```java
String name = "Alice";
int age = 30;
double salary = 75000.50;

// Using String.format()
String formatted = String.format("Name: %s, Age: %d, Salary: $%.2f", 
                                  name, age, salary);

// Using System.out.printf()
System.out.printf("Employee: %s (%d years old) earns $%,.2f%n", 
                  name, age, salary);

// Common format specifiers
String.format("%d", 42);              // "42" (integer)
String.format("%f", 3.14159);         // "3.141590" (float)
String.format("%.2f", 3.14159);       // "3.14" (2 decimal places)
String.format("%10s", "hello");       // "     hello" (width 10)
String.format("%-10s", "hello");      // "hello     " (left-aligned)
String.format("%05d", 42);            // "00042" (zero-padded)
String.format("%,d", 1234567);        // "1,234,567" (with commas)
```

### **Text Blocks (Java 15+):**
```java
String html = """
    <html>
        <body>
            <h1>Welcome to %s</h1>
            <p>Today is %s</p>
        </body>
    </html>
    """.formatted("Java", "Wednesday");

String json = """
    {
        "name": "%s",
        "age": %d,
        "active": %b
    }
    """.formatted("John", 30, true);
```

---

## Regular Expressions

### **Pattern Matching:**
```java
String pattern = "\\d+";  // One or more digits
String text = "The price is 25 dollars";

// Check if matches
boolean matches = text.matches(".*\\d+.*");  // true

// Find matches
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher(text);
if (m.find()) {
    System.out.println("Found: " + m.group()); // "25"
}

// Replace with regex
String replaced = text.replaceAll("\\d+", "XX"); // "The price is XX dollars"
```

### **Common Regex Patterns:**
```java
// Email validation
String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
boolean isValidEmail = "user@example.com".matches(emailPattern);

// Phone number
String phonePattern = "\\(?\\d{3}\\)?[-.]?\\d{3}[-.]?\\d{4}";
boolean isValidPhone = "(555) 123-4567".matches(phonePattern);

// Extract all words
String text = "Hello World 123";
Pattern wordPattern = Pattern.compile("\\b[A-Za-z]+\\b");
Matcher matcher = wordPattern.matcher(text);
while (matcher.find()) {
    System.out.println("Word: " + matcher.group()); // "Hello", "World"
}
```

### **String Validation Examples:**
```java
public class StringValidator {
    
    public static boolean isValidPassword(String password) {
        // At least 8 chars, one uppercase, one lowercase, one digit
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$");
    }
    
    public static boolean isValidIPAddress(String ip) {
        String pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(pattern);
    }
    
    public static String extractNumbers(String text) {
        return text.replaceAll("[^\\d]", ""); // Keep only digits
    }
}
```

---

## Best Practices

### **1. Use StringBuilder for Multiple Concatenations:**
```java
// Avoid
String result = "";
for (String item : items) {
    result += item + ", ";
}

// Prefer
StringBuilder sb = new StringBuilder();
for (String item : items) {
    sb.append(item).append(", ");
}
String result = sb.toString();
```

### **2. Use String.join() for Simple Joining:**
```java
List<String> items = Arrays.asList("apple", "banana", "cherry");
String joined = String.join(", ", items); // "apple, banana, cherry"
```

### **3. Be Careful with String Comparison:**
```java
// Wrong - compares references
if (str1 == str2) { }

// Correct - compares content
if (str1.equals(str2)) { }

// Null-safe comparison
if (Objects.equals(str1, str2)) { }
```

### **4. Use String.format() for Complex Formatting:**
```java
// Readable and maintainable
String message = String.format("User %s (ID: %d) has %d unread messages", 
                               name, id, count);
```

### **5. Validate and Sanitize Input:**
```java
public String sanitizeInput(String input) {
    if (input == null) return "";
    
    return input.trim()
               .replaceAll("[<>\"'&]", "")  // Remove dangerous characters
               .substring(0, Math.min(input.length(), 100)); // Limit length
}
```

---

## Key Takeaways

### **Essential Concepts:**
✅ **String Immutability** - Strings cannot be modified  
✅ **String Pool** - JVM optimization for string literals  
✅ **StringBuilder** - Mutable string buffer for efficiency  
✅ **String Methods** - Rich API for string manipulation  
✅ **Regular Expressions** - Pattern matching and validation  
✅ **String Formatting** - Creating formatted output  

### **Performance Guidelines:**
- **Use StringBuilder** for multiple concatenations
- **Use String literals** instead of new String()
- **Use String.join()** for simple concatenations
- **Compile regex patterns** once if used repeatedly
- **Consider string interning** for frequently used strings

### **Security Considerations:**
- **Validate all input** strings
- **Sanitize user data** before processing
- **Use proper encoding** when working with files/network
- **Be careful with SQL injection** when building queries

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Learn about internationalization (i18n) for multi-language support
- Move to [File I/O](../08-File-IO/README.md)

---

**Continue to: [Chapter 8: File I/O →](../08-File-IO/README.md)**