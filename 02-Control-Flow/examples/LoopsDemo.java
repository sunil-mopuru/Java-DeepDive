/**
 * LoopsDemo.java - Comprehensive Loops Demonstration
 * 
 * This program demonstrates all types of loops in Java: for, enhanced for,
 * while, and do-while loops with practical examples and best practices.
 */

import java.util.Scanner;
import java.util.Random;

public class LoopsDemo {
    
    public static void main(String[] args) {
        
        System.out.println("=== JAVA LOOPS COMPREHENSIVE DEMO ===\n");
        
        // 1. FOR LOOP BASICS
        System.out.println("1. FOR LOOP BASICS:");
        System.out.println("--------------------");
        
        // Simple counting loop
        System.out.println("Counting from 1 to 5:");
        for (int i = 1; i <= 5; i++) {
            System.out.printf("Count: %d%n", i);
        }
        
        // Counting backwards
        System.out.println("\nCountdown from 10 to 1:");
        for (int i = 10; i >= 1; i--) {
            System.out.printf("T-minus %d%n", i);
        }
        System.out.println("🚀 Blast off!\n");
        
        // Step by different amounts
        System.out.println("Even numbers from 0 to 20:");
        for (int i = 0; i <= 20; i += 2) {
            System.out.printf("%d ", i);
        }
        System.out.println("\n");
        
        // Multiple variables in for loop
        System.out.println("Multiple variables demonstration:");
        for (int i = 0, j = 10; i < j; i++, j--) {
            System.out.printf("i = %d, j = %d, sum = %d%n", i, j, i + j);
        }
        System.out.println();
        
        // 2. FOR LOOP PRACTICAL APPLICATIONS
        System.out.println("2. FOR LOOP APPLICATIONS:");
        System.out.println("--------------------------");
        
        // Sum of numbers
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.printf("Sum of numbers 1 to 100: %d%n", sum);
        
        // Factorial calculation
        int number = 5;
        long factorial = 1;
        for (int i = 1; i <= number; i++) {
            factorial *= i;
        }
        System.out.printf("Factorial of %d: %d%n", number, factorial);
        
        // Multiplication table
        int tableFor = 7;
        System.out.printf("\nMultiplication table for %d:%n", tableFor);
        for (int i = 1; i <= 10; i++) {
            System.out.printf("%d × %d = %d%n", tableFor, i, tableFor * i);
        }
        System.out.println();
        
        // 3. ENHANCED FOR LOOP (for-each)
        System.out.println("3. ENHANCED FOR LOOP:");
        System.out.println("----------------------");
        
        // Array iteration
        int[] numbers = {10, 25, 30, 45, 50};
        System.out.println("Array elements:");
        for (int num : numbers) {
            System.out.printf("Number: %d%n", num);
        }
        
        // String array iteration
        String[] fruits = {"Apple", "Banana", "Orange", "Mango", "Grapes"};
        System.out.println("\nFruit inventory:");
        for (String fruit : fruits) {
            System.out.printf("- %s%n", fruit);
        }
        
        // Character iteration in string
        String word = "HELLO";
        System.out.printf("\nCharacters in '%s':%n", word);
        for (char ch : word.toCharArray()) {
            System.out.printf("'%c' ", ch);
        }
        System.out.println("\n");
        
        // Finding maximum in array
        int[] scores = {85, 92, 78, 96, 88, 73, 99};
        int maxScore = scores[0];
        for (int score : scores) {
            if (score > maxScore) {
                maxScore = score;
            }
        }
        System.out.printf("Highest score: %d%n%n", maxScore);
        
        // 4. WHILE LOOP
        System.out.println("4. WHILE LOOP:");
        System.out.println("---------------");
        
        // Basic while loop
        int count = 1;
        System.out.println("While loop counting:");
        while (count <= 5) {
            System.out.printf("Iteration %d%n", count);
            count++;
        }
        
        // Sum until condition
        int total = 0;
        int currentNum = 1;
        while (total < 50) {
            total += currentNum;
            System.out.printf("Added %d, total now: %d%n", currentNum, total);
            currentNum++;
        }
        System.out.println();
        
        // Input validation example
        Scanner scanner = new Scanner(System.in);
        int age = -1;
        
        System.out.println("Input validation with while loop:");
        while (age < 0 || age > 120) {
            System.out.print("Enter a valid age (0-120): ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                if (age < 0 || age > 120) {
                    System.out.println("Invalid age! Please try again.");
                }
            } else {
                System.out.println("Please enter a number!");
                scanner.next(); // Clear invalid input
            }
        }
        System.out.printf("Thank you! Age %d is valid.%n%n", age);
        
        // 5. DO-WHILE LOOP
        System.out.println("5. DO-WHILE LOOP:");
        System.out.println("-------------------");
        
        // Menu system example
        int choice;
        do {
            System.out.println("\n📱 MENU OPTIONS:");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Settings");
            System.out.println("3. Help");
            System.out.println("0. Exit");
            System.out.print("Enter your choice (0-3): ");
            
            choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    System.out.println("👤 Displaying user profile...");
                    break;
                case 2:
                    System.out.println("⚙️ Opening settings...");
                    break;
                case 3:
                    System.out.println("❓ Displaying help information...");
                    break;
                case 0:
                    System.out.println("👋 Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        } while (choice != 0);
        
        // Password attempts
        String correctPassword = "secret123";
        String enteredPassword;
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        System.out.println("\nPassword protection system:");
        do {
            attempts++;
            System.out.printf("Enter password (Attempt %d/%d): ", attempts, MAX_ATTEMPTS);
            enteredPassword = scanner.next();
            
            if (!enteredPassword.equals(correctPassword)) {
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("❌ Incorrect password! Try again.");
                } else {
                    System.out.println("🔒 Account locked! Too many failed attempts.");
                }
            } else {
                System.out.println("✅ Access granted!");
            }
        } while (!enteredPassword.equals(correctPassword) && attempts < MAX_ATTEMPTS);
        
        // 6. NESTED LOOPS
        System.out.println("\n6. NESTED LOOPS:");
        System.out.println("------------------");
        
        // Multiplication table (complete)
        System.out.println("Complete multiplication table (1-5):");
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                System.out.printf("%2d ", i * j);
            }
            System.out.println(); // New line after each row
        }
        
        // Star patterns
        System.out.println("\nStar pattern - Right triangle:");
        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= row; col++) {
                System.out.print("* ");
            }
            System.out.println();
        }
        
        System.out.println("\nStar pattern - Pyramid:");
        int rows = 4;
        for (int i = 1; i <= rows; i++) {
            // Print spaces
            for (int j = 1; j <= rows - i; j++) {
                System.out.print(" ");
            }
            // Print stars
            for (int k = 1; k <= 2 * i - 1; k++) {
                System.out.print("*");
            }
            System.out.println();
        }
        
        // 7. BREAK AND CONTINUE
        System.out.println("\n7. BREAK AND CONTINUE:");
        System.out.println("-----------------------");
        
        // Break example - find first even number > 10
        System.out.println("Finding first even number greater than 10:");
        for (int i = 11; i <= 20; i++) {
            if (i % 2 == 0) {
                System.out.printf("Found: %d%n", i);
                break; // Exit loop immediately
            }
            System.out.printf("Checking %d... not even%n", i);
        }
        
        // Continue example - print only odd numbers
        System.out.println("\nPrinting only odd numbers from 1 to 10:");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue; // Skip rest of this iteration
            }
            System.out.printf("Odd: %d%n", i);
        }
        
        // 8. ADVANCED LOOP TECHNIQUES
        System.out.println("\n8. ADVANCED TECHNIQUES:");
        System.out.println("------------------------");
        
        // Labeled break for nested loops
        System.out.println("Labeled break in nested loops:");
        outer: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) {
                    System.out.printf("Breaking at (%d,%d)%n", i, j);
                    break outer; // Break from both loops
                }
                System.out.printf("(%d,%d) ", i, j);
            }
            System.out.println();
        }
        
        // Array search with for loop
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve"};
        String searchName = "Charlie";
        boolean found = false;
        int position = -1;
        
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(searchName)) {
                found = true;
                position = i;
                break;
            }
        }
        
        if (found) {
            System.out.printf("\n'%s' found at position %d%n", searchName, position);
        } else {
            System.out.printf("\n'%s' not found in the array%n", searchName);
        }
        
        // 9. PERFORMANCE CONSIDERATIONS
        System.out.println("\n9. PERFORMANCE TIPS:");
        System.out.println("---------------------");
        
        // Efficient loop - calculate length once
        String[] largeArray = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int arrayLength = largeArray.length; // Calculate once
        
        System.out.println("Efficient array iteration:");
        for (int i = 0; i < arrayLength; i++) {
            System.out.printf("Element %d: %s%n", i, largeArray[i]);
        }
        
        // 10. PRACTICAL APPLICATIONS
        System.out.println("\n10. PRACTICAL APPLICATIONS:");
        System.out.println("----------------------------");
        
        // Prime number checker
        int checkNumber = 17;
        boolean isPrime = true;
        
        if (checkNumber <= 1) {
            isPrime = false;
        } else {
            for (int i = 2; i <= Math.sqrt(checkNumber); i++) {
                if (checkNumber % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        
        System.out.printf("%d is %s%n", checkNumber, isPrime ? "prime" : "not prime");
        
        // Number guessing game
        Random random = new Random();
        int secretNumber = random.nextInt(10) + 1;
        int guess;
        int guessCount = 0;
        final int MAX_GUESSES = 3;
        
        System.out.printf("\n🎮 Number guessing game! (1-10, %d attempts)%n", MAX_GUESSES);
        
        do {
            guessCount++;
            System.out.printf("Attempt %d: Enter your guess: ", guessCount);
            guess = scanner.nextInt();
            
            if (guess == secretNumber) {
                System.out.printf("🎉 Congratulations! You guessed it in %d attempts!%n", guessCount);
                break;
            } else if (guess < secretNumber) {
                System.out.println("📈 Too low!");
            } else {
                System.out.println("📉 Too high!");
            }
            
            if (guessCount == MAX_GUESSES) {
                System.out.printf("😢 Game over! The number was %d%n", secretNumber);
            }
            
        } while (guessCount < MAX_GUESSES);
        
        // Statistics calculator
        int[] dataSet = {75, 82, 90, 67, 88, 95, 73, 89, 91, 85};
        
        System.out.println("\nDataset statistics:");
        
        // Calculate sum and average
        int dataSum = 0;
        for (int value : dataSet) {
            dataSum += value;
        }
        double average = (double) dataSum / dataSet.length;
        
        // Find min and max
        int min = dataSet[0];
        int max = dataSet[0];
        for (int value : dataSet) {
            if (value < min) min = value;
            if (value > max) max = value;
        }
        
        System.out.printf("Sum: %d%n", dataSum);
        System.out.printf("Average: %.2f%n", average);
        System.out.printf("Minimum: %d%n", min);
        System.out.printf("Maximum: %d%n", max);
        System.out.printf("Range: %d%n", max - min);
        
        scanner.close();
    }
}

/*
 * Key Learning Points:
 * 
 * 1. FOR LOOP:
 *    - Best for known number of iterations
 *    - Three parts: initialization, condition, update
 *    - Can have multiple variables and complex updates
 *    - Most common loop for counting and arrays
 * 
 * 2. ENHANCED FOR LOOP:
 *    - Simplifies iteration over collections/arrays
 *    - Read-only access (cannot modify collection)
 *    - Cleaner syntax for simple traversals
 *    - No index information available
 * 
 * 3. WHILE LOOP:
 *    - Best for unknown number of iterations
 *    - Condition checked before each iteration
 *    - Perfect for input validation and event loops
 *    - Can result in zero iterations
 * 
 * 4. DO-WHILE LOOP:
 *    - Guarantees at least one execution
 *    - Condition checked after each iteration
 *    - Great for menus and user input
 *    - Less commonly used than while
 * 
 * 5. BREAK AND CONTINUE:
 *    - break: Exit loop completely
 *    - continue: Skip to next iteration
 *    - Use labeled break/continue for nested loops
 *    - Improve efficiency by avoiding unnecessary iterations
 * 
 * 6. BEST PRACTICES:
 *    - Choose the right loop type for your needs
 *    - Keep loop bodies simple and focused
 *    - Avoid infinite loops (always have exit condition)
 *    - Use meaningful variable names
 *    - Consider performance implications
 *    - Test edge cases (empty collections, zero iterations)
 * 
 * 7. COMMON PATTERNS:
 *    - Counting loops: for(int i = 0; i < n; i++)
 *    - Collection iteration: for(Type item : collection)
 *    - Input validation: while(invalid input)
 *    - Menu systems: do-while with switch
 *    - Search patterns: loop with break when found
 */