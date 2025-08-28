# Java Basics - Practice Exercises

## 📝 Instructions
- Solve each exercise step by step
- Test your solutions with different inputs
- Focus on understanding concepts, not just getting the right answer
- Try to solve exercises without looking at solutions first

---

## Exercise 1: Personal Information Display
**Difficulty: Beginner**

Write a Java program that:
1. Declares variables for personal information (name, age, height, isStudent)
2. Initializes them with your information
3. Displays the information in a formatted manner

**Expected Output:**
```
=== Personal Information ===
Name: John Doe
Age: 25 years old
Height: 1.75 meters
Student Status: true
```

**Learning Goals:** Variable declaration, initialization, data types, formatted output

---

## Exercise 2: Simple Calculator
**Difficulty: Beginner**

Create a program that:
1. Takes two numbers and an operator (+, -, *, /) as input
2. Performs the calculation
3. Displays the result with proper formatting
4. Handles division by zero

**Sample Run:**
```
Enter first number: 15.5
Enter operator (+, -, *, /): *
Enter second number: 2.5
Result: 15.5 * 2.5 = 38.75
```

**Learning Goals:** Input/output, operators, conditional logic

---

## Exercise 3: Data Type Conversions
**Difficulty: Beginner-Intermediate**

Write a program that demonstrates:
1. Implicit type conversions (widening)
2. Explicit type conversions (casting)
3. Shows potential data loss during casting
4. Converts between different numeric types

**Requirements:**
- Start with a double value: 123.789
- Convert to int, float, long, short, byte
- Display original and converted values
- Show what happens with overflow (byte range: -128 to 127)

**Learning Goals:** Type casting, data loss, numeric ranges

---

## Exercise 4: Operator Precedence Challenge
**Difficulty: Intermediate**

Predict the output of these expressions WITHOUT running the code:
```java
int a = 5, b = 10, c = 15;
```

1. `a + b * c`
2. `(a + b) * c`
3. `a + b * c / 3`
4. `a++ + ++b - c--`
5. `a < b && b < c || a > c`
6. `!false || true && false`

Then write a program to verify your answers and explain why each result occurs.

**Learning Goals:** Operator precedence, logical thinking, debugging skills

---

## Exercise 5: Temperature Converter
**Difficulty: Intermediate**

Create a comprehensive temperature converter that:
1. Converts between Celsius, Fahrenheit, and Kelvin
2. Takes input for temperature and source unit
3. Displays conversions to all other units
4. Validates that Kelvin is not below absolute zero (-273.15°C)

**Formulas:**
- Celsius to Fahrenheit: F = (C × 9/5) + 32
- Celsius to Kelvin: K = C + 273.15
- Fahrenheit to Celsius: C = (F - 32) × 5/9

**Sample Run:**
```
Enter temperature: 25
Enter unit (C/F/K): C
25.0°C = 77.0°F = 298.15K
```

**Learning Goals:** Mathematical operations, input validation, formatting

---

## Exercise 6: Grade Calculator
**Difficulty: Intermediate**

Write a program that:
1. Takes student scores for 5 subjects
2. Calculates average, highest, and lowest scores
3. Determines letter grade based on average
4. Uses appropriate data types for all calculations

**Grading Scale:**
- A: 90-100
- B: 80-89
- C: 70-79
- D: 60-69
- F: Below 60

**Learning Goals:** Arrays (basic), mathematical operations, conditional logic

---

## Exercise 7: Bitwise Operations Explorer
**Difficulty: Advanced**

Create a program that:
1. Takes two integers as input
2. Displays their binary representations
3. Shows results of all bitwise operations (AND, OR, XOR, NOT)
4. Demonstrates left and right shift operations
5. Explains what each operation does in practical terms

**Sample Output:**
```
Enter first number: 12
Enter second number: 7

12 in binary: 1100
7 in binary:  0111

12 & 7 = 4   (0100) - Bits set in both numbers
12 | 7 = 15  (1111) - Bits set in either number
12 ^ 7 = 11  (1011) - Bits set in one number only
~12 = -13    - All bits flipped

12 << 2 = 48  (110000) - Multiply by 4
12 >> 2 = 3   (0011)   - Divide by 4
```

**Learning Goals:** Bitwise operations, binary representation, advanced operators

---

## Exercise 8: Input Validation System
**Difficulty: Advanced**

Build a robust input system that:
1. Prompts for different data types with validation
2. Handles invalid input gracefully
3. Provides clear error messages
4. Continues prompting until valid input is received

**Requirements:**
- Age: integer between 0-150
- Name: non-empty string, no numbers
- Salary: positive double
- Grade: character A, B, C, D, or F

**Learning Goals:** Input validation, exception handling basics, user experience

---

## Exercise 9: Number Base Converter
**Difficulty: Advanced**

Create a program that:
1. Takes a decimal number as input
2. Converts it to binary, octal, and hexadecimal
3. Also converts from any base to decimal
4. Uses Java's built-in methods and manual conversion

**Features:**
- Support numbers up to Integer.MAX_VALUE
- Show step-by-step conversion process
- Handle negative numbers correctly

**Learning Goals:** Number systems, algorithms, Java utility methods

---

## Exercise 10: Mini Banking System
**Difficulty: Challenge**

Design a simple banking calculator that:
1. Calculates simple and compound interest
2. Determines loan payments
3. Converts between currencies (use fixed rates)
4. Validates all financial inputs
5. Formats monetary output appropriately

**Features:**
- Simple Interest: SI = (P × R × T) / 100
- Compound Interest: CI = P(1 + R/100)^T - P
- Monthly Payment: M = P[r(1+r)^n]/[(1+r)^n-1]

**Learning Goals:** Real-world applications, mathematical formulas, data formatting

---

## 🎯 Challenge Projects

### Project A: Personal Finance Calculator
Combine multiple exercises to create a comprehensive financial calculator with:
- Income/expense tracking
- Savings goal calculator
- Investment growth projections
- Budget recommendations

### Project B: Unit Converter Suite
Build a complete unit conversion system covering:
- Length, weight, volume measurements
- Temperature scales
- Time units
- Data storage units (bytes, KB, MB, GB)

### Project C: Math Learning Assistant
Create an educational tool that:
- Generates math problems
- Checks answers
- Tracks progress
- Provides hints for incorrect answers

---

## 📚 Study Tips

1. **Start Simple:** Begin with basic exercises and gradually increase complexity
2. **Debug Actively:** When code doesn't work, use print statements to understand flow
3. **Experiment:** Try modifying exercises to see different outcomes
4. **Read Errors:** Java compiler errors contain helpful information
5. **Practice Daily:** Consistency is key to mastering programming concepts

---

## ✅ Solution Guidelines

When checking your solutions:
- ✅ Code compiles without errors
- ✅ Handles edge cases (empty input, zero values, etc.)
- ✅ Uses appropriate data types
- ✅ Follows Java naming conventions
- ✅ Includes meaningful comments
- ✅ Produces expected output format

**Next:** After completing these exercises, move to [Control Flow](../../02-Control-Flow/exercises/) exercises.