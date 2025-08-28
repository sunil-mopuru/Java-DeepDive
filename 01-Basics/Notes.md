# Java Basics - Quick Reference Notes

## 📋 Basic Program Structure
```java
public class ClassName {
    public static void main(String[] args) {
        // Your code here
    }
}
```

## 🔢 Primitive Data Types

| Type    | Size    | Range                    | Default | Example          |
|---------|---------|--------------------------|---------|------------------|
| `byte`  | 8 bits  | -128 to 127             | 0       | `byte b = 100;`  |
| `short` | 16 bits | -32,768 to 32,767       | 0       | `short s = 1000;`|
| `int`   | 32 bits | -2³¹ to 2³¹-1           | 0       | `int i = 50000;` |
| `long`  | 64 bits | -2⁶³ to 2⁶³-1           | 0L      | `long l = 123L;` |
| `float` | 32 bits | ~7 decimal digits       | 0.0f    | `float f = 3.14f;`|
| `double`| 64 bits | ~15 decimal digits      | 0.0     | `double d = 3.14;`|
| `char`  | 16 bits | 0 to 65,535 (Unicode)  | '\u0000'| `char c = 'A';`  |
| `boolean`| JVM     | true or false          | false   | `boolean b = true;`|

## ➕ Operators

### Arithmetic
- `+` Addition
- `-` Subtraction  
- `*` Multiplication
- `/` Division
- `%` Modulus (remainder)
- `++` Increment
- `--` Decrement

### Assignment
- `=` Assign
- `+=` Add and assign
- `-=` Subtract and assign
- `*=` Multiply and assign
- `/=` Divide and assign
- `%=` Modulus and assign

### Comparison
- `==` Equal to
- `!=` Not equal to
- `>` Greater than
- `<` Less than
- `>=` Greater than or equal
- `<=` Less than or equal

### Logical
- `&&` Logical AND
- `||` Logical OR
- `!` Logical NOT

### Bitwise
- `&` Bitwise AND
- `|` Bitwise OR
- `^` Bitwise XOR
- `~` Bitwise NOT
- `<<` Left shift
- `>>` Right shift
- `>>>` Unsigned right shift

## 📥📤 Input/Output

### Output
```java
System.out.print("No newline");
System.out.println("With newline");
System.out.printf("Formatted: %s is %d years old%n", name, age);
```

### Input with Scanner
```java
import java.util.Scanner;

Scanner scanner = new Scanner(System.in);
String name = scanner.nextLine();      // Read line
String word = scanner.next();          // Read word
int number = scanner.nextInt();        // Read integer
double decimal = scanner.nextDouble(); // Read double
boolean flag = scanner.nextBoolean();  // Read boolean
char ch = scanner.next().charAt(0);    // Read character
scanner.close(); // Always close!
```

## 🎯 Format Specifiers

| Specifier | Type | Example |
|-----------|------|---------|
| `%s` | String | `printf("%s", "Hello")` |
| `%d` | Integer | `printf("%d", 42)` |
| `%f` | Float/Double | `printf("%.2f", 3.14159)` |
| `%c` | Character | `printf("%c", 'A')` |
| `%b` | Boolean | `printf("%b", true)` |
| `%x` | Hexadecimal | `printf("%x", 255)` |
| `%o` | Octal | `printf("%o", 8)` |
| `%e` | Scientific | `printf("%e", 1234.5)` |
| `%n` | Newline | `printf("Line 1%nLine 2")` |

### Width and Precision
- `%10s` - Right-aligned, width 10
- `%-10s` - Left-aligned, width 10  
- `%05d` - Zero-padded, width 5
- `%.2f` - 2 decimal places
- `%10.2f` - Width 10, 2 decimals

## 🔄 Type Conversion

### Implicit (Widening)
```java
int i = 100;
long l = i;        // int → long ✓
float f = i;       // int → float ✓
double d = f;      // float → double ✓
```

### Explicit (Casting)
```java
double d = 99.99;
int i = (int) d;   // 99 (truncated)
float f = (float) d;
byte b = (byte) 300; // Overflow: 44
```

## 📝 Comments

```java
// Single-line comment

/*
 * Multi-line comment
 * Spans multiple lines
 */

/**
 * Javadoc comment
 * @param parameter description
 * @return return description
 */
```

## 🎨 Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Class | PascalCase | `MyClass` |
| Method | camelCase | `calculateArea()` |
| Variable | camelCase | `studentName` |
| Constant | UPPER_CASE | `MAX_SIZE` |
| Package | lowercase | `com.example` |

## 🚨 Common Mistakes

1. **Uninitialized local variables**
   ```java
   int x;
   System.out.println(x); // ❌ Error!
   ```

2. **Integer division**
   ```java
   int result = 5 / 2; // 2, not 2.5!
   double result = 5.0 / 2; // ✓ 2.5
   ```

3. **Scanner buffer issues**
   ```java
   int num = scanner.nextInt();
   String text = scanner.nextLine(); // ❌ Gets empty string
   scanner.nextLine(); // ✓ Clear buffer first
   ```

4. **Comparing strings**
   ```java
   String a = "hello";
   if (a == "hello") // ❌ Don't use ==
   if (a.equals("hello")) // ✓ Use equals()
   ```

## ⚡ Quick Tips

- **Always initialize variables** before use
- **Use meaningful names** for variables and methods
- **Close Scanner** when finished
- **Use parentheses** to clarify operator precedence
- **Choose appropriate data types** for your needs
- **Validate user input** to prevent errors
- **Comment your code** but not excessively
- **Follow Java naming conventions**

## 🔗 Memory Aid

**BODMAS for Operators:**
1. **B**rackets `()`
2. **O**rders (exponents) - not in basic Java
3. **D**ivision `/`, **M**ultiplication `*`
4. **A**ddition `+`, **S**ubtraction `-`

**Scanner Methods:**
- `nextLine()` - whole line
- `next()` - single word
- `nextInt()` - integer
- `nextDouble()` - decimal
- `nextBoolean()` - true/false

## 📖 Practice Checklist

- [ ] Create a "Hello World" program
- [ ] Declare variables of all primitive types
- [ ] Use all arithmetic operators
- [ ] Practice formatted output with printf
- [ ] Read different types of input with Scanner
- [ ] Convert between data types (casting)
- [ ] Write meaningful comments
- [ ] Handle basic input validation

---
**Next Topic:** [Control Flow →](../../02-Control-Flow/Notes.md)