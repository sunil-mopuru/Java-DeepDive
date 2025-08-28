# Chapter 2: Control Flow

## 📚 Table of Contents
1. [Introduction to Control Flow](#introduction-to-control-flow)
2. [Conditional Statements](#conditional-statements)
3. [Switch Statements](#switch-statements)
4. [Loops](#loops)
5. [Break and Continue](#break-and-continue)
6. [Nested Structures](#nested-structures)
7. [Practical Applications](#practical-applications)
8. [Best Practices](#best-practices)
9. [Key Takeaways](#key-takeaways)

---

## Introduction to Control Flow

### What is Control Flow?
**Control Flow** determines the order in which program statements are executed. By default, Java executes statements sequentially from top to bottom, but control flow statements allow us to:

- **Make decisions** (conditional execution)
- **Repeat actions** (loops)
- **Jump to different parts** of code
- **Handle multiple cases** efficiently

### Types of Control Flow:

#### 1. **Sequential Flow** (Default)
```java
System.out.println("First");  // Executes first
System.out.println("Second"); // Executes second
System.out.println("Third");  // Executes third
```

#### 2. **Conditional Flow** (Decision Making)
```java
if (condition) {
    // Execute if true
} else {
    // Execute if false
}
```

#### 3. **Repetitive Flow** (Loops)
```java
for (int i = 0; i < 5; i++) {
    // Repeat this block
}
```

#### 4. **Selective Flow** (Multiple Choices)
```java
switch (variable) {
    case 1: // Option 1
    case 2: // Option 2
    default: // Default option
}
```

---

## Conditional Statements

### 1. **if Statement**

#### **Syntax:**
```java
if (condition) {
    // Code to execute if condition is true
}
```

#### **Example:**
```java
int age = 18;
if (age >= 18) {
    System.out.println("You are eligible to vote!");
}
```

#### **Key Points:**
- Condition must be a `boolean` expression
- Curly braces `{}` are optional for single statements but recommended
- Use parentheses `()` around the condition

### 2. **if-else Statement**

#### **Syntax:**
```java
if (condition) {
    // Code if condition is true
} else {
    // Code if condition is false
}
```

#### **Example:**
```java
int temperature = 25;
if (temperature > 30) {
    System.out.println("It's hot outside!");
} else {
    System.out.println("It's pleasant weather.");
}
```

### 3. **if-else-if Ladder**

#### **Syntax:**
```java
if (condition1) {
    // Code for condition1
} else if (condition2) {
    // Code for condition2
} else if (condition3) {
    // Code for condition3
} else {
    // Default code (optional)
}
```

#### **Example:**
```java
int score = 85;

if (score >= 90) {
    System.out.println("Grade: A");
} else if (score >= 80) {
    System.out.println("Grade: B");
} else if (score >= 70) {
    System.out.println("Grade: C");
} else if (score >= 60) {
    System.out.println("Grade: D");
} else {
    System.out.println("Grade: F");
}
```

### 4. **Nested if Statements**

#### **Example:**
```java
boolean isLoggedIn = true;
String userRole = "admin";

if (isLoggedIn) {
    if (userRole.equals("admin")) {
        System.out.println("Welcome, Administrator!");
    } else if (userRole.equals("user")) {
        System.out.println("Welcome, User!");
    } else {
        System.out.println("Welcome, Guest!");
    }
} else {
    System.out.println("Please log in first.");
}
```

### **Conditional Operators in Detail:**

#### **Comparison Operators:**
```java
int a = 10, b = 20;

// Equality
boolean equal = (a == b);        // false
boolean notEqual = (a != b);     // true

// Relational
boolean greater = (a > b);       // false
boolean less = (a < b);          // true
boolean greaterEqual = (a >= b); // false
boolean lessEqual = (a <= b);    // true
```

#### **Logical Operators:**
```java
boolean sunny = true;
boolean warm = false;

// AND: both conditions must be true
boolean perfect = sunny && warm;  // false

// OR: at least one condition must be true
boolean okay = sunny || warm;     // true

// NOT: inverts the boolean value
boolean cloudy = !sunny;         // false
```

#### **Short-Circuit Evaluation:**
```java
// && stops evaluating if first condition is false
boolean result1 = (false && expensiveMethod()); // expensiveMethod() not called

// || stops evaluating if first condition is true
boolean result2 = (true || expensiveMethod());  // expensiveMethod() not called
```

---

## Switch Statements

### **Traditional Switch (Java 7 and earlier)**

#### **Syntax:**
```java
switch (expression) {
    case value1:
        // Code for value1
        break;
    case value2:
        // Code for value2
        break;
    default:
        // Default code (optional)
        break;
}
```

#### **Example:**
```java
int dayOfWeek = 3;
String dayName;

switch (dayOfWeek) {
    case 1:
        dayName = "Monday";
        break;
    case 2:
        dayName = "Tuesday";
        break;
    case 3:
        dayName = "Wednesday";
        break;
    case 4:
        dayName = "Thursday";
        break;
    case 5:
        dayName = "Friday";
        break;
    case 6:
        dayName = "Saturday";
        break;
    case 7:
        dayName = "Sunday";
        break;
    default:
        dayName = "Invalid day";
        break;
}
System.out.println("Day: " + dayName);
```

### **Switch with Strings (Java 7+)**
```java
String month = "January";

switch (month) {
    case "December":
    case "January":
    case "February":
        System.out.println("Winter");
        break;
    case "March":
    case "April":
    case "May":
        System.out.println("Spring");
        break;
    case "June":
    case "July":
    case "August":
        System.out.println("Summer");
        break;
    case "September":
    case "October":
    case "November":
        System.out.println("Autumn");
        break;
    default:
        System.out.println("Invalid month");
}
```

### **Enhanced Switch (Java 14+)**

#### **Switch Expressions:**
```java
String dayType = switch (dayOfWeek) {
    case 1, 2, 3, 4, 5 -> "Weekday";
    case 6, 7 -> "Weekend";
    default -> "Invalid";
};
```

#### **Switch with yield:**
```java
String season = switch (month) {
    case "December", "January", "February" -> {
        System.out.println("Cold season");
        yield "Winter";
    }
    case "March", "April", "May" -> "Spring";
    case "June", "July", "August" -> "Summer";
    case "September", "October", "November" -> "Autumn";
    default -> "Unknown";
};
```

### **Fall-Through Behavior:**
```java
int number = 2;

switch (number) {
    case 1:
        System.out.println("One");
        // No break - falls through to next case
    case 2:
        System.out.println("Two or after One");
        break;
    case 3:
        System.out.println("Three");
        break;
}
// Output: "Two or after One"
```

---

## Loops

### 1. **for Loop**

#### **Syntax:**
```java
for (initialization; condition; update) {
    // Loop body
}
```

#### **Example:**
```java
// Print numbers 1 to 5
for (int i = 1; i <= 5; i++) {
    System.out.println("Number: " + i);
}
```

#### **Loop Components:**
1. **Initialization:** `int i = 1` - executed once at the beginning
2. **Condition:** `i <= 5` - checked before each iteration
3. **Update:** `i++` - executed after each iteration

#### **Variations:**
```java
// Multiple variables
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.println(i + " " + j);
}

// Infinite loop (be careful!)
for (;;) {
    // This runs forever unless broken
    break; // Exit condition needed
}

// Empty sections
int i = 0;
for (; i < 5;) {
    System.out.println(i);
    i++; // Update inside loop body
}
```

### 2. **Enhanced for Loop (for-each)**

#### **Syntax:**
```java
for (dataType variable : collection) {
    // Use variable
}
```

#### **Example:**
```java
int[] numbers = {1, 2, 3, 4, 5};

// Enhanced for loop
for (int number : numbers) {
    System.out.println("Number: " + number);
}

// String iteration
String text = "Hello";
for (char ch : text.toCharArray()) {
    System.out.println("Character: " + ch);
}
```

#### **Benefits:**
- **Simpler syntax** - no index management
- **Less error-prone** - no bounds checking needed
- **Readable** - intent is clearer

#### **Limitations:**
- **Read-only** - cannot modify the collection
- **No index access** - don't know current position
- **Forward only** - cannot iterate backwards

### 3. **while Loop**

#### **Syntax:**
```java
while (condition) {
    // Loop body
    // Update condition variable
}
```

#### **Example:**
```java
int count = 1;
while (count <= 5) {
    System.out.println("Count: " + count);
    count++; // Important: update the condition variable
}
```

#### **Use Cases:**
- **Unknown iterations** - don't know how many times to loop
- **Event-driven loops** - wait for user input or file end
- **Game loops** - continue until game over

#### **Example - Input Validation:**
```java
Scanner scanner = new Scanner(System.in);
int age = -1;

while (age < 0 || age > 150) {
    System.out.print("Enter valid age (0-150): ");
    age = scanner.nextInt();
    if (age < 0 || age > 150) {
        System.out.println("Invalid age! Try again.");
    }
}
System.out.println("Age accepted: " + age);
```

### 4. **do-while Loop**

#### **Syntax:**
```java
do {
    // Loop body (executes at least once)
} while (condition);
```

#### **Example:**
```java
int number;
Scanner scanner = new Scanner(System.in);

do {
    System.out.print("Enter a positive number: ");
    number = scanner.nextInt();
    if (number <= 0) {
        System.out.println("That's not positive! Try again.");
    }
} while (number <= 0);

System.out.println("You entered: " + number);
```

#### **Key Difference:**
- **while:** Check condition first, then execute (0+ times)
- **do-while:** Execute first, then check condition (1+ times)

### **Loop Comparison:**

| Loop Type | When to Use | Iterations | Best For |
|-----------|-------------|------------|----------|
| `for` | Known count | 0+ times | Counters, arrays by index |
| `enhanced for` | Iterate collections | 0+ times | Arrays, lists (read-only) |
| `while` | Unknown count | 0+ times | Conditions, input validation |
| `do-while` | At least once | 1+ times | Menus, user input |

---

## Break and Continue

### 1. **break Statement**

#### **Purpose:** Exit the nearest enclosing loop or switch

#### **In Loops:**
```java
// Find first even number
for (int i = 1; i <= 10; i++) {
    if (i % 2 == 0) {
        System.out.println("First even: " + i);
        break; // Exit loop immediately
    }
}
```

#### **In Switch:**
```java
switch (choice) {
    case 1:
        System.out.println("Option 1");
        break; // Prevent fall-through
    case 2:
        System.out.println("Option 2");
        break;
}
```

### 2. **continue Statement**

#### **Purpose:** Skip rest of current iteration and move to next

#### **Example:**
```java
// Print only odd numbers
for (int i = 1; i <= 10; i++) {
    if (i % 2 == 0) {
        continue; // Skip even numbers
    }
    System.out.println("Odd: " + i);
}
```

### 3. **Labeled break and continue**

#### **For Nested Loops:**
```java
outer: for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (i == 2 && j == 2) {
            break outer; // Break from both loops
        }
        System.out.println(i + "," + j);
    }
}
```

#### **Labeled continue:**
```java
outer: for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        if (j == 2) {
            continue outer; // Skip to next i iteration
        }
        System.out.println(i + "," + j);
    }
}
```

---

## Nested Structures

### **Nested if Statements:**
```java
int age = 25;
boolean hasLicense = true;
boolean hasInsurance = true;

if (age >= 18) {
    if (hasLicense) {
        if (hasInsurance) {
            System.out.println("You can drive!");
        } else {
            System.out.println("Get insurance first.");
        }
    } else {
        System.out.println("Get a license first.");
    }
} else {
    System.out.println("Too young to drive.");
}
```

### **Nested Loops:**
```java
// Multiplication table
for (int i = 1; i <= 5; i++) {
    for (int j = 1; j <= 5; j++) {
        System.out.print((i * j) + "\t");
    }
    System.out.println(); // New line after each row
}
```

### **Pattern Example:**
```java
// Right triangle pattern
int rows = 5;
for (int i = 1; i <= rows; i++) {
    for (int j = 1; j <= i; j++) {
        System.out.print("* ");
    }
    System.out.println();
}
/*
Output:
* 
* * 
* * * 
* * * * 
* * * * * 
*/
```

---

## Practical Applications

### 1. **Menu-Driven Program:**
```java
Scanner scanner = new Scanner(System.in);
int choice;

do {
    System.out.println("\n=== CALCULATOR MENU ===");
    System.out.println("1. Add");
    System.out.println("2. Subtract");
    System.out.println("3. Multiply");
    System.out.println("4. Divide");
    System.out.println("0. Exit");
    System.out.print("Enter choice: ");
    
    choice = scanner.nextInt();
    
    switch (choice) {
        case 1:
            // Addition logic
            break;
        case 2:
            // Subtraction logic
            break;
        case 3:
            // Multiplication logic
            break;
        case 4:
            // Division logic
            break;
        case 0:
            System.out.println("Goodbye!");
            break;
        default:
            System.out.println("Invalid choice!");
    }
} while (choice != 0);
```

### 2. **Number Guessing Game:**
```java
import java.util.Random;

Random random = new Random();
int secretNumber = random.nextInt(100) + 1;
int attempts = 0;
boolean guessed = false;

System.out.println("Guess the number between 1 and 100!");

while (!guessed) {
    System.out.print("Enter your guess: ");
    int guess = scanner.nextInt();
    attempts++;
    
    if (guess == secretNumber) {
        System.out.println("Congratulations! You guessed it in " + attempts + " attempts!");
        guessed = true;
    } else if (guess < secretNumber) {
        System.out.println("Too low! Try higher.");
    } else {
        System.out.println("Too high! Try lower.");
    }
}
```

### 3. **Prime Number Checker:**
```java
public static boolean isPrime(int number) {
    if (number <= 1) return false;
    if (number <= 3) return true;
    if (number % 2 == 0 || number % 3 == 0) return false;
    
    for (int i = 5; i * i <= number; i += 6) {
        if (number % i == 0 || number % (i + 2) == 0) {
            return false;
        }
    }
    return true;
}
```

---

## Best Practices

### 1. **Code Readability:**
```java
// Good: Clear and readable
if (age >= 18 && hasValidID && !isBanned) {
    allowEntry();
}

// Bad: Hard to understand
if (a >= 18 && b && !c) {
    d();
}
```

### 2. **Avoid Deep Nesting:**
```java
// Bad: Deep nesting
if (user != null) {
    if (user.isActive()) {
        if (user.hasPermission()) {
            // Do something
        }
    }
}

// Good: Early returns
if (user == null) return;
if (!user.isActive()) return;
if (!user.hasPermission()) return;
// Do something
```

### 3. **Loop Optimization:**
```java
// Good: Calculate once
int length = array.length;
for (int i = 0; i < length; i++) {
    // Process array[i]
}

// Bad: Recalculate every iteration
for (int i = 0; i < array.length; i++) {
    // Process array[i]
}
```

### 4. **Use Enhanced for Loop When Possible:**
```java
// Good: Enhanced for loop for read-only access
for (String name : names) {
    System.out.println(name);
}

// Use traditional for when you need index
for (int i = 0; i < names.length; i++) {
    System.out.println(i + ": " + names[i]);
}
```

---

## Key Takeaways

### **Essential Concepts Mastered:**
✅ **Conditional Logic** - Making decisions with if/else statements  
✅ **Switch Statements** - Handling multiple options efficiently  
✅ **Loops** - Repeating actions with for, while, do-while  
✅ **Loop Control** - Using break and continue appropriately  
✅ **Nesting** - Combining structures for complex logic  
✅ **Best Practices** - Writing clean, maintainable control flow  

### **Decision Making Guide:**

| Use | When |
|-----|------|
| **if-else** | 2-3 conditions, complex boolean logic |
| **switch** | Multiple discrete values, cleaner than many if-else |
| **for loop** | Known number of iterations |
| **while loop** | Unknown iterations, condition-based |
| **do-while** | At least one execution needed |
| **enhanced for** | Iterating collections (read-only) |

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Build programs combining different control structures
- Move to [Object-Oriented Programming Basics](../03-OOP-Basics/README.md)

---

**Continue to: [Chapter 3: OOP Basics →](../03-OOP-Basics/README.md)**