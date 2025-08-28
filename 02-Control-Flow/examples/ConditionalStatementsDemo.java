/**
 * ConditionalStatementsDemo.java - Comprehensive Conditional Logic Examples
 * 
 * This program demonstrates various conditional statements in Java with
 * practical real-world scenarios and decision-making logic.
 */

import java.util.Scanner;

public class ConditionalStatementsDemo {
    
    public static void main(String[] args) {
        
        System.out.println("=== CONDITIONAL STATEMENTS COMPREHENSIVE DEMO ===\n");
        
        // SIMPLE IF STATEMENTS
        System.out.println("1. SIMPLE IF STATEMENTS:");
        System.out.println("-------------------------");
        
        int temperature = 35;
        int humidity = 80;
        boolean isSunny = true;
        
        System.out.printf("Current conditions: %d°C, %d%% humidity, sunny: %b%n%n", 
                         temperature, humidity, isSunny);
        
        if (temperature > 30) {
            System.out.println("✓ It's hot outside!");
        }
        
        if (humidity > 70) {
            System.out.println("✓ It's quite humid today.");
        }
        
        if (isSunny) {
            System.out.println("✓ Don't forget your sunglasses!");
        }
        
        // IF-ELSE STATEMENTS
        System.out.println("\n2. IF-ELSE STATEMENTS:");
        System.out.println("-----------------------");
        
        int age = 17;
        System.out.printf("Age: %d → ", age);
        
        if (age >= 18) {
            System.out.println("You are an adult and can vote!");
        } else {
            System.out.println("You are a minor. " + (18 - age) + " years until you can vote.");
        }
        
        // Account balance example
        double balance = 250.75;
        double withdrawAmount = 300.00;
        
        System.out.printf("\nAccount balance: $%.2f, Withdrawal: $%.2f → ", balance, withdrawAmount);
        
        if (balance >= withdrawAmount) {
            balance -= withdrawAmount;
            System.out.printf("Withdrawal successful! New balance: $%.2f%n", balance);
        } else {
            System.out.printf("Insufficient funds! Need $%.2f more.%n", withdrawAmount - balance);
        }
        
        // IF-ELSE-IF LADDER
        System.out.println("\n3. IF-ELSE-IF LADDER:");
        System.out.println("-----------------------");
        
        // Grade calculation
        int score = 87;
        char grade;
        String performance;
        
        System.out.printf("Score: %d → ", score);
        
        if (score >= 90) {
            grade = 'A';
            performance = "Excellent";
        } else if (score >= 80) {
            grade = 'B';
            performance = "Good";
        } else if (score >= 70) {
            grade = 'C';
            performance = "Average";
        } else if (score >= 60) {
            grade = 'D';
            performance = "Below Average";
        } else {
            grade = 'F';
            performance = "Failing";
        }
        
        System.out.printf("Grade: %c (%s)%n", grade, performance);
        
        // BMI Calculator with categories
        double height = 1.75; // meters
        double weight = 70.0;  // kg
        double bmi = weight / (height * height);
        
        System.out.printf("\nBMI Calculation: %.1f kg / %.2f m² = %.1f → ", weight, height, bmi);
        
        if (bmi < 18.5) {
            System.out.println("Underweight");
        } else if (bmi < 25) {
            System.out.println("Normal weight");
        } else if (bmi < 30) {
            System.out.println("Overweight");
        } else {
            System.out.println("Obese");
        }
        
        // NESTED IF STATEMENTS
        System.out.println("\n4. NESTED IF STATEMENTS:");
        System.out.println("-------------------------");
        
        // University admission system
        boolean hasHighSchoolDiploma = true;
        int satScore = 1350;
        double gpa = 3.7;
        boolean hasRecommendations = true;
        
        System.out.println("University Admission Evaluation:");
        System.out.printf("- High school diploma: %b%n", hasHighSchoolDiploma);
        System.out.printf("- SAT score: %d%n", satScore);
        System.out.printf("- GPA: %.1f%n", gpa);
        System.out.printf("- Has recommendations: %b%n%n", hasRecommendations);
        
        if (hasHighSchoolDiploma) {
            if (satScore >= 1200) {
                if (gpa >= 3.5) {
                    if (hasRecommendations) {
                        System.out.println("🎉 ACCEPTED with scholarship eligibility!");
                    } else {
                        System.out.println("✅ ACCEPTED (recommend getting recommendation letters)");
                    }
                } else if (gpa >= 3.0) {
                    System.out.println("✅ CONDITIONALLY ACCEPTED (probationary admission)");
                } else {
                    System.out.println("❌ REJECTED (GPA too low)");
                }
            } else if (satScore >= 1000) {
                System.out.println("⏳ WAITLISTED (consider retaking SAT)");
            } else {
                System.out.println("❌ REJECTED (SAT score too low)");
            }
        } else {
            System.out.println("❌ REJECTED (high school diploma required)");
        }
        
        // LOGICAL OPERATORS IN CONDITIONS
        System.out.println("\n5. LOGICAL OPERATORS:");
        System.out.println("----------------------");
        
        // Weather activity recommendation
        boolean isWeekend = true;
        int temp = 25;
        boolean isRaining = false;
        int windSpeed = 15; // km/h
        
        System.out.printf("Weekend: %b, Temp: %d°C, Raining: %b, Wind: %d km/h%n%n", 
                         isWeekend, temp, isRaining, windSpeed);
        
        // AND operator
        if (isWeekend && temp > 20 && !isRaining) {
            System.out.println("🏖️ Perfect day for a picnic!");
        }
        
        // OR operator
        if (isRaining || windSpeed > 20 || temp < 5) {
            System.out.println("🏠 Better to stay indoors today.");
        }
        
        // Complex conditions
        if ((isWeekend || temp > 25) && !isRaining && windSpeed < 20) {
            System.out.println("🚴 Great day for cycling!");
        }
        
        // SHORT-CIRCUIT EVALUATION DEMO
        System.out.println("\n6. SHORT-CIRCUIT EVALUATION:");
        System.out.println("------------------------------");
        
        int x = 5;
        int y = 0;
        
        // Safe division using short-circuit AND
        if (y != 0 && x / y > 2) {
            System.out.println("Division result is greater than 2");
        } else {
            System.out.println("Cannot divide by zero or result <= 2");
        }
        
        // Demonstrating short-circuit behavior
        System.out.println("\nShort-circuit demonstration:");
        boolean result1 = false && (++x > 0); // x not incremented
        System.out.printf("After 'false && (++x > 0)': x = %d%n", x);
        
        boolean result2 = true || (++x > 0);  // x not incremented
        System.out.printf("After 'true || (++x > 0)': x = %d%n", x);
        
        // TERNARY OPERATOR
        System.out.println("\n7. TERNARY OPERATOR:");
        System.out.println("---------------------");
        
        int number = 15;
        String evenOdd = (number % 2 == 0) ? "even" : "odd";
        System.out.printf("Number %d is %s%n", number, evenOdd);
        
        // Nested ternary (use sparingly)
        int testScore = 75;
        String result = (testScore >= 80) ? "Pass" : 
                       (testScore >= 60) ? "Conditional Pass" : "Fail";
        System.out.printf("Score %d: %s%n", testScore, result);
        
        // Maximum of three numbers
        int a = 10, b = 25, c = 15;
        int max = (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
        System.out.printf("Maximum of %d, %d, %d is: %d%n", a, b, c, max);
        
        // PRACTICAL APPLICATIONS
        System.out.println("\n8. PRACTICAL APPLICATIONS:");
        System.out.println("---------------------------");
        
        // Password strength checker
        String password = "MyPass123!";
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        
        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (!Character.isLetterOrDigit(ch)) hasSpecial = true;
        }
        
        System.out.printf("Password: \"%s\"%n", password);
        System.out.printf("Length: %d, Has lowercase: %b, Has uppercase: %b%n", 
                         password.length(), hasLower, hasUpper);
        System.out.printf("Has digit: %b, Has special char: %b%n", hasDigit, hasSpecial);
        
        if (password.length() >= 8 && hasLower && hasUpper && hasDigit && hasSpecial) {
            System.out.println("🔒 Password strength: STRONG");
        } else if (password.length() >= 6 && ((hasLower && hasUpper) || (hasDigit && hasSpecial))) {
            System.out.println("🔓 Password strength: MEDIUM");
        } else {
            System.out.println("🚨 Password strength: WEAK");
        }
        
        // Time-based greeting
        int hour = 14; // 24-hour format
        String greeting;
        
        if (hour >= 5 && hour < 12) {
            greeting = "Good morning";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good afternoon";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good evening";
        } else {
            greeting = "Good night";
        }
        
        System.out.printf("\nTime: %d:00 → %s!%n", hour, greeting);
        
        // Shipping cost calculator
        double orderAmount = 75.50;
        double shippingCost;
        
        if (orderAmount >= 100) {
            shippingCost = 0.0; // Free shipping
        } else if (orderAmount >= 50) {
            shippingCost = 5.99; // Reduced shipping
        } else {
            shippingCost = 9.99; // Standard shipping
        }
        
        System.out.printf("\nOrder amount: $%.2f%n", orderAmount);
        System.out.printf("Shipping cost: $%.2f%n", shippingCost);
        System.out.printf("Total: $%.2f%n", orderAmount + shippingCost);
        
        if (shippingCost == 0) {
            System.out.println("🎉 You qualified for free shipping!");
        } else if (orderAmount >= 50) {
            System.out.println("📦 You get reduced shipping rates!");
        } else {
            System.out.printf("💡 Add $%.2f more for reduced shipping!%n", 50 - orderAmount);
        }
    }
}

/*
 * Key Learning Points:
 * 
 * 1. IF STATEMENTS:
 *    - Use for simple true/false decisions
 *    - Always use braces for readability
 *    - Combine with logical operators for complex conditions
 * 
 * 2. IF-ELSE-IF:
 *    - Perfect for multiple mutually exclusive conditions
 *    - Order matters - check most specific conditions first
 *    - Always include final else for completeness
 * 
 * 3. NESTED CONDITIONS:
 *    - Use for hierarchical decision making
 *    - Keep nesting levels reasonable (max 3-4 levels)
 *    - Consider using early returns to reduce nesting
 * 
 * 4. LOGICAL OPERATORS:
 *    - && (AND): Both conditions must be true
 *    - || (OR): At least one condition must be true
 *    - ! (NOT): Inverts boolean value
 *    - Short-circuit evaluation saves computation
 * 
 * 5. TERNARY OPERATOR:
 *    - Great for simple assignments
 *    - Avoid complex nested ternary operators
 *    - Use when it improves code readability
 * 
 * 6. BEST PRACTICES:
 *    - Use meaningful variable names
 *    - Group related conditions logically
 *    - Comment complex conditional logic
 *    - Test edge cases and boundary conditions
 */