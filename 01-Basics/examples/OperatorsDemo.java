/**
 * OperatorsDemo.java - Comprehensive Operators Demonstration
 * 
 * This program demonstrates all types of operators in Java with
 * practical examples and detailed explanations of operator precedence.
 */
public class OperatorsDemo {
    
    public static void main(String[] args) {
        
        System.out.println("=== JAVA OPERATORS COMPREHENSIVE DEMO ===\n");
        
        // ARITHMETIC OPERATORS
        System.out.println("1. ARITHMETIC OPERATORS:");
        System.out.println("-------------------------");
        
        int a = 15, b = 4;
        double x = 15.0, y = 4.0;
        
        System.out.printf("Given: a = %d, b = %d%n", a, b);
        System.out.printf("Given: x = %.1f, y = %.1f%n%n", x, y);
        
        // Basic arithmetic
        System.out.println("Basic Operations:");
        System.out.printf("a + b = %d + %d = %d%n", a, b, (a + b));
        System.out.printf("a - b = %d - %d = %d%n", a, b, (a - b));
        System.out.printf("a * b = %d * %d = %d%n", a, b, (a * b));
        System.out.printf("a / b = %d / %d = %d (integer division)%n", a, b, (a / b));
        System.out.printf("x / y = %.1f / %.1f = %.2f (floating-point division)%n", x, y, (x / y));
        System.out.printf("a %% b = %d %% %d = %d (modulus/remainder)%n%n", a, b, (a % b));
        
        // Increment and Decrement
        System.out.println("Increment/Decrement Operators:");
        int counter = 10;
        System.out.printf("Initial counter: %d%n", counter);
        System.out.printf("counter++: %d (post-increment, use then increment)%n", counter++);
        System.out.printf("After post-increment: %d%n", counter);
        System.out.printf("++counter: %d (pre-increment, increment then use)%n", ++counter);
        System.out.printf("counter--: %d (post-decrement, use then decrement)%n", counter--);
        System.out.printf("After post-decrement: %d%n", counter);
        System.out.printf("--counter: %d (pre-decrement, decrement then use)%n%n", --counter);
        
        // ASSIGNMENT OPERATORS
        System.out.println("2. ASSIGNMENT OPERATORS:");
        System.out.println("--------------------------");
        
        int value = 20;
        System.out.printf("Initial value: %d%n", value);
        
        value += 5;  // value = value + 5
        System.out.printf("value += 5: %d%n", value);
        
        value -= 3;  // value = value - 3
        System.out.printf("value -= 3: %d%n", value);
        
        value *= 2;  // value = value * 2
        System.out.printf("value *= 2: %d%n", value);
        
        value /= 4;  // value = value / 4
        System.out.printf("value /= 4: %d%n", value);
        
        value %= 7;  // value = value % 7
        System.out.printf("value %%= 7: %d%n%n", value);
        
        // COMPARISON OPERATORS
        System.out.println("3. COMPARISON OPERATORS:");
        System.out.println("-------------------------");
        
        int num1 = 10, num2 = 20, num3 = 10;
        
        System.out.printf("num1 = %d, num2 = %d, num3 = %d%n%n", num1, num2, num3);
        
        System.out.printf("num1 == num2: %d == %d → %b%n", num1, num2, (num1 == num2));
        System.out.printf("num1 == num3: %d == %d → %b%n", num1, num3, (num1 == num3));
        System.out.printf("num1 != num2: %d != %d → %b%n", num1, num2, (num1 != num2));
        System.out.printf("num1 < num2:  %d < %d → %b%n", num1, num2, (num1 < num2));
        System.out.printf("num1 > num2:  %d > %d → %b%n", num1, num2, (num1 > num2));
        System.out.printf("num1 <= num3: %d <= %d → %b%n", num1, num3, (num1 <= num3));
        System.out.printf("num2 >= num1: %d >= %d → %b%n%n", num2, num1, (num2 >= num1));
        
        // LOGICAL OPERATORS
        System.out.println("4. LOGICAL OPERATORS:");
        System.out.println("----------------------");
        
        boolean p = true, q = false;
        
        System.out.printf("p = %b, q = %b%n%n", p, q);
        
        System.out.printf("p && q (AND): %b && %b → %b%n", p, q, (p && q));
        System.out.printf("p || q (OR):  %b || %b → %b%n", p, q, (p || q));
        System.out.printf("!p (NOT):     !%b → %b%n", p, !p);
        System.out.printf("!q (NOT):     !%b → %b%n%n", q, !q);
        
        // Short-circuit evaluation demonstration
        System.out.println("Short-circuit Evaluation:");
        int i = 0;
        boolean result1 = (false && (++i > 0)); // ++i is not executed
        System.out.printf("false && (++i > 0): i = %d (not incremented)%n", i);
        
        i = 0;
        boolean result2 = (true || (++i > 0)); // ++i is not executed
        System.out.printf("true || (++i > 0): i = %d (not incremented)%n%n", i);
        
        // BITWISE OPERATORS
        System.out.println("5. BITWISE OPERATORS:");
        System.out.println("----------------------");
        
        int m = 12; // Binary: 1100
        int n = 7;  // Binary: 0111
        
        System.out.printf("m = %d (Binary: %s)%n", m, Integer.toBinaryString(m));
        System.out.printf("n = %d (Binary: %s)%n%n", n, Integer.toBinaryString(n));
        
        System.out.printf("m & n  (AND): %d & %d = %d (Binary: %s)%n", 
                         m, n, (m & n), Integer.toBinaryString(m & n));
        System.out.printf("m | n  (OR):  %d | %d = %d (Binary: %s)%n", 
                         m, n, (m | n), Integer.toBinaryString(m | n));
        System.out.printf("m ^ n  (XOR): %d ^ %d = %d (Binary: %s)%n", 
                         m, n, (m ^ n), Integer.toBinaryString(m ^ n));
        System.out.printf("~m     (NOT): ~%d = %d%n%n", m, ~m);
        
        // Shift operators
        System.out.println("Shift Operators:");
        System.out.printf("m << 2 (Left shift):  %d << 2 = %d (Binary: %s)%n", 
                         m, (m << 2), Integer.toBinaryString(m << 2));
        System.out.printf("m >> 2 (Right shift): %d >> 2 = %d (Binary: %s)%n", 
                         m, (m >> 2), Integer.toBinaryString(m >> 2));
        System.out.printf("m >>> 2 (Unsigned right): %d >>> 2 = %d%n%n", 
                         m, (m >>> 2));
        
        // TERNARY OPERATOR
        System.out.println("6. TERNARY OPERATOR:");
        System.out.println("---------------------");
        
        int age = 20;
        String category = (age >= 18) ? "Adult" : "Minor";
        System.out.printf("Age: %d → Category: %s%n", age, category);
        
        int score = 85;
        String grade = (score >= 90) ? "A" : 
                      (score >= 80) ? "B" : 
                      (score >= 70) ? "C" : 
                      (score >= 60) ? "D" : "F";
        System.out.printf("Score: %d → Grade: %s%n%n", score, grade);
        
        // OPERATOR PRECEDENCE DEMONSTRATION
        System.out.println("7. OPERATOR PRECEDENCE:");
        System.out.println("------------------------");
        
        int result;
        
        // Without parentheses
        result = 2 + 3 * 4;  // Multiplication first: 2 + 12 = 14
        System.out.printf("2 + 3 * 4 = %d (multiplication first)%n", result);
        
        // With parentheses
        result = (2 + 3) * 4;  // Addition first: 5 * 4 = 20
        System.out.printf("(2 + 3) * 4 = %d (addition first)%n", result);
        
        // Complex expression
        result = 10 + 5 * 2 - 8 / 4;  // 10 + 10 - 2 = 18
        System.out.printf("10 + 5 * 2 - 8 / 4 = %d%n", result);
        
        // Boolean precedence
        boolean complexBoolean = true || false && false;  // && before ||: true || false = true
        System.out.printf("true || false && false = %b (&& has higher precedence)%n%n", complexBoolean);
        
        // PRACTICAL EXAMPLES
        System.out.println("8. PRACTICAL EXAMPLES:");
        System.out.println("-----------------------");
        
        // Calculate compound interest
        double principal = 1000.0;
        double rate = 0.05;  // 5%
        int time = 3;        // years
        double amount = principal * Math.pow(1 + rate, time);
        double interest = amount - principal;
        
        System.out.printf("Compound Interest Calculation:%n");
        System.out.printf("Principal: $%.2f%n", principal);
        System.out.printf("Rate: %.1f%%%n", rate * 100);
        System.out.printf("Time: %d years%n", time);
        System.out.printf("Amount: $%.2f%n", amount);
        System.out.printf("Interest: $%.2f%n%n", interest);
        
        // Temperature conversion
        double celsius = 25.0;
        double fahrenheit = (celsius * 9.0 / 5.0) + 32;
        System.out.printf("Temperature Conversion:%n");
        System.out.printf("%.1f°C = %.1f°F%n%n", celsius, fahrenheit);
        
        // Even/Odd check using modulus
        int number = 17;
        String evenOdd = (number % 2 == 0) ? "Even" : "Odd";
        System.out.printf("Number %d is %s%n%n", number, evenOdd);
        
        // Area calculations
        double radius = 5.0;
        double circleArea = Math.PI * radius * radius;
        
        double length = 8.0;
        double width = 6.0;
        double rectangleArea = length * width;
        
        System.out.printf("Circle (radius=%.1f): Area = %.2f%n", radius, circleArea);
        System.out.printf("Rectangle (%.1f x %.1f): Area = %.1f%n", length, width, rectangleArea);
    }
}

/*
 * Operator Precedence Summary (Highest to Lowest):
 * 1. Postfix: x++, x--
 * 2. Unary: ++x, --x, !, ~, +, -
 * 3. Multiplicative: *, /, %
 * 4. Additive: +, -
 * 5. Shift: <<, >>, >>>
 * 6. Relational: <, >, <=, >=, instanceof
 * 7. Equality: ==, !=
 * 8. Bitwise AND: &
 * 9. Bitwise XOR: ^
 * 10. Bitwise OR: |
 * 11. Logical AND: &&
 * 12. Logical OR: ||
 * 13. Ternary: ? :
 * 14. Assignment: =, +=, -=, *=, /=, %=, etc.
 */