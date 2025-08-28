# Control Flow - Quick Reference Notes

## 🎯 Decision Making

### if Statement
```java
if (condition) {
    // Execute if true
}
```

### if-else
```java
if (condition) {
    // Execute if true
} else {
    // Execute if false
}
```

### if-else-if Ladder
```java
if (condition1) {
    // Code for condition1
} else if (condition2) {
    // Code for condition2
} else {
    // Default code
}
```

### Ternary Operator
```java
result = (condition) ? valueIfTrue : valueIfFalse;
```

## 🔄 Switch Statements

### Traditional Switch
```java
switch (variable) {
    case value1:
        // Code
        break;
    case value2:
        // Code
        break;
    default:
        // Default code
        break;
}
```

### Enhanced Switch (Java 14+)
```java
String result = switch (variable) {
    case value1 -> "Result1";
    case value2, value3 -> "Result2";
    default -> "Default";
};
```

## 🔁 Loops

### for Loop
```java
for (initialization; condition; update) {
    // Loop body
}
```

### Enhanced for (for-each)
```java
for (dataType item : collection) {
    // Use item
}
```

### while Loop
```java
while (condition) {
    // Loop body
    // Update condition variable
}
```

### do-while Loop
```java
do {
    // Loop body (executes at least once)
} while (condition);
```

## 🎮 Loop Control

### break Statement
```java
for (int i = 0; i < 10; i++) {
    if (i == 5) break; // Exit loop
}
```

### continue Statement
```java
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) continue; // Skip even numbers
    System.out.println(i);
}
```

### Labeled break/continue
```java
outer: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (condition) break outer; // Break from outer loop
    }
}
```

## 🧠 Decision Guide

| Use | When |
|-----|------|
| **if-else** | Boolean conditions, ranges, complex logic |
| **switch** | Discrete values, multiple options |
| **for** | Known iterations, counting |
| **enhanced for** | Collection iteration (read-only) |
| **while** | Unknown iterations, conditions |
| **do-while** | At least one execution needed |

## ⚡ Best Practices

### Readability
- Use meaningful condition names
- Avoid deep nesting (max 3-4 levels)
- Use early returns to reduce nesting
- Group related conditions

### Performance
- Calculate loop bounds once
- Use break/continue to avoid unnecessary iterations
- Choose appropriate loop type
- Consider algorithm complexity

### Common Patterns

#### Input Validation
```java
while (input < min || input > max) {
    System.out.print("Invalid! Enter again: ");
    input = scanner.nextInt();
}
```

#### Menu System
```java
do {
    displayMenu();
    choice = getChoice();
    processChoice(choice);
} while (choice != EXIT);
```

#### Array Processing
```java
// With index
for (int i = 0; i < array.length; i++) {
    // Use array[i] and i
}

// Without index  
for (int element : array) {
    // Use element only
}
```

#### Search Pattern
```java
boolean found = false;
for (int item : collection) {
    if (item.equals(target)) {
        found = true;
        break;
    }
}
```

## 🚨 Common Mistakes

1. **Missing break in switch**
   ```java
   switch (value) {
       case 1: 
           doSomething();
           // Missing break - falls through!
       case 2:
           doSomethingElse();
           break;
   }
   ```

2. **Infinite loops**
   ```java
   int i = 0;
   while (i < 10) {
       System.out.println(i);
       // Missing i++ - infinite loop!
   }
   ```

3. **Off-by-one errors**
   ```java
   for (int i = 0; i <= array.length; i++) { // Should be <
       System.out.println(array[i]); // IndexOutOfBounds!
   }
   ```

4. **Modifying collection during enhanced for**
   ```java
   for (String item : list) {
       if (condition) {
           list.remove(item); // ConcurrentModificationException!
       }
   }
   ```

## 📊 Complexity Guide

| Structure | Time Complexity | Use Case |
|-----------|-----------------|----------|
| if-else | O(1) | Simple decisions |
| switch | O(1) | Multiple discrete values |
| for loop | O(n) | Linear processing |
| nested loops | O(n²) | 2D processing |
| while | Variable | Condition-based |

## 🔧 Debugging Tips

1. **Add print statements** to trace execution
2. **Check boundary conditions** (0, 1, max values)
3. **Verify loop conditions** don't change unexpectedly
4. **Test with edge cases** (empty arrays, null values)
5. **Use debugger** to step through complex logic

---
**Next Topic:** [OOP Basics →](../../03-OOP-Basics/Notes.md)