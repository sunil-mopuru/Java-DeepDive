# String Handling - Quick Notes

## 🎯 **Key Concepts**

### **String Characteristics**
- **Immutable**: Cannot be changed after creation
- **String Pool**: Literal strings are cached for memory efficiency
- **Reference vs Value**: Use `.equals()` for content comparison

### **String vs StringBuilder vs StringBuffer**
- **String**: Immutable, good for few operations
- **StringBuilder**: Mutable, fast, not thread-safe
- **StringBuffer**: Mutable, thread-safe, slower than StringBuilder

---

## 🛠️ **Essential Methods**

### **Basic Operations**
```java
String str = "Hello World";
int len = str.length();              // 11
char ch = str.charAt(0);             // 'H'
String sub = str.substring(0, 5);    // "Hello"
int index = str.indexOf("World");    // 6
boolean contains = str.contains("llo"); // true
```

### **Case and Formatting**
```java
str.toLowerCase();      // "hello world"
str.toUpperCase();      // "HELLO WORLD"
str.trim();            // Remove leading/trailing spaces
str.strip();           // Better Unicode whitespace removal (Java 11+)
```

### **Replacement and Splitting**
```java
str.replace("World", "Java");        // "Hello Java"
str.replaceAll("\\d+", "X");        // Replace digits with X
String[] parts = str.split(" ");     // ["Hello", "World"]
String joined = String.join(", ", parts); // "Hello, World"
```

---

## 📝 **String Comparison**

### **Comparison Methods**
```java
String s1 = "Hello", s2 = "hello";
s1.equals(s2);                  // false (case-sensitive)
s1.equalsIgnoreCase(s2);        // true
s1.compareTo(s2);               // < 0 (lexicographical)
s1.compareToIgnoreCase(s2);     // 0
```

### **Null-Safe Comparison**
```java
Objects.equals(str1, str2);     // Handles null values
```

---

## 🔧 **Performance Tips**

### **StringBuilder for Multiple Operations**
```java
// Bad - creates many string objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += "item" + i;
}

// Good - efficient string building
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append("item").append(i);
}
String result = sb.toString();
```

### **String Formatting**
```java
// printf-style
String formatted = String.format("Name: %s, Age: %d", name, age);

// printf to console
System.out.printf("Value: %.2f%n", 3.14159);
```

---

## 🎨 **Regular Expressions**

### **Common Patterns**
```java
String email = "user@domain.com";
boolean isValid = email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

String phone = "1234567890";
String formatted = phone.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "($1) $2-$3");

// Pattern compilation for reuse
Pattern pattern = Pattern.compile("\\d+");
Matcher matcher = pattern.matcher("abc123def456");
while (matcher.find()) {
    System.out.println("Found: " + matcher.group());
}
```

---

## ⚡ **Quick Reference**

| Operation | Method | Example |
|-----------|---------|---------|
| Length | `length()` | `"Hello".length()` → 5 |
| Character at | `charAt(i)` | `"Hello".charAt(1)` → 'e' |
| Substring | `substring(start, end)` | `"Hello".substring(1, 3)` → "el" |
| Index of | `indexOf(str)` | `"Hello".indexOf("ll")` → 2 |
| Replace | `replace(old, new)` | `"Hello".replace("l", "x")` → "Hexxo" |
| Split | `split(regex)` | `"a,b,c".split(",")` → ["a","b","c"] |
| Join | `String.join(sep, arr)` | `String.join(",", arr)` → "a,b,c" |
| Trim | `trim()` | `" Hello ".trim()` → "Hello" |

---

## 🔍 **Common Use Cases**

- **Validation**: Email, phone number, password patterns
- **Text Processing**: Parsing CSV, log files, user input
- **URL/Path Manipulation**: Building URLs, file paths
- **Data Formatting**: Currency, dates, numbers
- **Template Processing**: String interpolation, placeholders

---

**Next:** [File I/O](../08-File-IO/README.md)