import java.util.Scanner;

/**
 * InputOutputDemo.java - Comprehensive Input/Output Operations
 * 
 * This program demonstrates various input and output techniques in Java,
 * including console I/O, formatted output, and input validation.
 */
public class InputOutputDemo {
    
    public static void main(String[] args) {
        
        System.out.println("=== JAVA INPUT/OUTPUT COMPREHENSIVE DEMO ===\n");
        
        // OUTPUT DEMONSTRATIONS
        System.out.println("1. OUTPUT METHODS:");
        System.out.println("-------------------");
        
        // Basic output methods
        System.out.print("print() - no newline ");
        System.out.print("continues on same line ");
        System.out.println("println() - adds newline");
        System.out.println("Next line starts here\n");
        
        // Formatted output with printf()
        String name = "Alice";
        int age = 25;
        double salary = 75000.50;
        boolean isEmployed = true;
        char grade = 'A';
        
        System.out.println("2. FORMATTED OUTPUT (printf):");
        System.out.println("------------------------------");
        
        // String formatting
        System.out.printf("String: %%s → %s%n", name);
        System.out.printf("String with width: %%10s → '%10s'%n", name);
        System.out.printf("String left-aligned: %%-10s → '%-10s'%n%n", name, name);
        
        // Integer formatting
        System.out.printf("Integer: %%d → %d%n", age);
        System.out.printf("Integer with width: %%5d → '%5d'%n", age);
        System.out.printf("Integer zero-padded: %%05d → %05d%n", age);
        System.out.printf("Integer with commas: %%,d → %,d%n%n", 1234567);
        
        // Floating-point formatting
        System.out.printf("Double: %%f → %f%n", salary);
        System.out.printf("Double 2 decimals: %%.2f → %.2f%n", salary);
        System.out.printf("Double scientific: %%e → %e%n", salary);
        System.out.printf("Double with commas: %%,.2f → %,.2f%n%n", salary);
        
        // Boolean and character formatting
        System.out.printf("Boolean: %%b → %b%n", isEmployed);
        System.out.printf("Character: %%c → %c%n%n", grade);
        
        // Escape sequences
        System.out.println("3. ESCAPE SEQUENCES:");
        System.out.println("---------------------");
        System.out.println("\\n → Newline\nSee?");
        System.out.println("\\t → Tab\tSeparated\tValues");
        System.out.println("\\\\ → Backslash: C:\\\\Users\\\\Name");
        System.out.println("\\\" → Quote: She said \"Hello\"");
        System.out.println("\\' → Apostrophe: It\\'s working");
        System.out.println("\\r → Carriage return (rare in Java)");
        System.out.println("\\b → Backspace (removes previous char)");
        System.out.println("\\f → Form feed (page break)\n");
        
        // INPUT DEMONSTRATIONS
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("4. INPUT METHODS (Interactive):");
        System.out.println("--------------------------------");
        
        try {
            // String input
            System.out.print("Enter your full name: ");
            String fullName = scanner.nextLine();
            System.out.printf("Hello, %s!%n%n", fullName);
            
            // Integer input
            System.out.print("Enter your age: ");
            int userAge = scanner.nextInt();
            System.out.printf("You are %d years old.%n%n", userAge);
            
            // Double input
            System.out.print("Enter your height in meters (e.g., 1.75): ");
            double height = scanner.nextDouble();
            System.out.printf("Your height is %.2f meters.%n%n", height);
            
            // Character input (first character of string)
            System.out.print("Enter your favorite letter: ");
            char favoriteLetter = scanner.next().charAt(0);
            System.out.printf("Your favorite letter is '%c'.%n%n", favoriteLetter);
            
            // Boolean input
            System.out.print("Are you a student? (true/false): ");
            boolean isStudent = scanner.nextBoolean();
            System.out.printf("Student status: %b%n%n", isStudent);
            
            // Clear the scanner buffer
            scanner.nextLine(); // Consume remaining newline
            
            // Multiple inputs on one line
            System.out.print("Enter three numbers separated by spaces: ");
            String numberLine = scanner.nextLine();
            String[] numbers = numberLine.split(" ");
            System.out.println("You entered:");
            for (int i = 0; i < numbers.length && i < 3; i++) {
                System.out.printf("  Number %d: %s%n", i + 1, numbers[i]);
            }
            System.out.println();
            
            // INPUT VALIDATION EXAMPLE
            System.out.println("5. INPUT VALIDATION:");
            System.out.println("---------------------");
            
            int validNumber = 0;
            boolean validInput = false;
            
            while (!validInput) {
                System.out.print("Enter a number between 1 and 100: ");
                if (scanner.hasNextInt()) {
                    validNumber = scanner.nextInt();
                    if (validNumber >= 1 && validNumber <= 100) {
                        validInput = true;
                        System.out.printf("Great! You entered: %d%n%n", validNumber);
                    } else {
                        System.out.println("Error: Number must be between 1 and 100. Try again.");
                    }
                } else {
                    System.out.println("Error: Please enter a valid integer.");
                    scanner.next(); // Clear invalid input
                }
            }
            
            // PRACTICAL APPLICATION: Simple Calculator
            System.out.println("6. PRACTICAL APPLICATION - Simple Calculator:");
            System.out.println("----------------------------------------------");
            
            scanner.nextLine(); // Clear buffer
            
            System.out.print("Enter first number: ");
            double num1 = scanner.nextDouble();
            
            System.out.print("Enter operator (+, -, *, /): ");
            char operator = scanner.next().charAt(0);
            
            System.out.print("Enter second number: ");
            double num2 = scanner.nextDouble();
            
            double result = 0;
            boolean validOperation = true;
            
            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    if (num2 != 0) {
                        result = num1 / num2;
                    } else {
                        System.out.println("Error: Division by zero!");
                        validOperation = false;
                    }
                    break;
                default:
                    System.out.println("Error: Invalid operator!");
                    validOperation = false;
            }
            
            if (validOperation) {
                System.out.printf("Result: %.2f %c %.2f = %.2f%n%n", num1, operator, num2, result);
            }
            
            // TABLE FORMATTING EXAMPLE
            System.out.println("7. TABLE FORMATTING:");
            System.out.println("---------------------");
            
            System.out.printf("%-10s %-5s %-10s %-12s%n", "Name", "Age", "Salary", "Department");
            System.out.println("---------------------------------------------");
            System.out.printf("%-10s %-5d $%-9,.2f %-12s%n", "Alice", 28, 65000.0, "Engineering");
            System.out.printf("%-10s %-5d $%-9,.2f %-12s%n", "Bob", 35, 75000.0, "Marketing");
            System.out.printf("%-10s %-5d $%-9,.2f %-12s%n", "Carol", 29, 70000.0, "Sales");
            System.out.printf("%-10s %-5d $%-9,.2f %-12s%n%n", "David", 42, 95000.0, "Management");
            
            // PROGRESS BAR EXAMPLE
            System.out.println("8. PROGRESS BAR SIMULATION:");
            System.out.println("-----------------------------");
            
            System.out.print("Processing");
            for (int i = 0; i <= 100; i += 10) {
                System.out.printf("\rProgress: [");
                int bars = i / 10;
                for (int j = 0; j < bars; j++) {
                    System.out.print("█");
                }
                for (int j = bars; j < 10; j++) {
                    System.out.print("░");
                }
                System.out.printf("] %d%%", i);
                
                try {
                    Thread.sleep(300); // Simulate processing time
                } catch (InterruptedException e) {
                    // Handle interruption
                }
            }
            System.out.println("\nProcessing complete!\n");
            
        } catch (Exception e) {
            System.out.println("An error occurred during input: " + e.getMessage());
        } finally {
            scanner.close();
        }
        
        // FORMAT SPECIFIERS REFERENCE
        System.out.println("9. FORMAT SPECIFIERS REFERENCE:");
        System.out.println("--------------------------------");
        System.out.println("%s  - String");
        System.out.println("%d  - Decimal integer");
        System.out.println("%f  - Floating point");
        System.out.println("%e  - Scientific notation");
        System.out.println("%g  - General floating point");
        System.out.println("%c  - Character");
        System.out.println("%b  - Boolean");
        System.out.println("%x  - Hexadecimal");
        System.out.println("%o  - Octal");
        System.out.println("%%  - Literal % character");
        System.out.println("%n  - Platform-specific newline");
        System.out.println("\nWidth and precision examples:");
        System.out.println("%10s   - Right-aligned string, width 10");
        System.out.println("%-10s  - Left-aligned string, width 10");
        System.out.println("%05d   - Zero-padded integer, width 5");
        System.out.println("%.2f   - Float with 2 decimal places");
        System.out.println("%10.2f - Float, width 10, 2 decimals");
    }
}

/*
 * Key Learning Points:
 * 
 * 1. OUTPUT:
 *    - System.out.print() - no newline
 *    - System.out.println() - with newline
 *    - System.out.printf() - formatted output
 * 
 * 2. INPUT:
 *    - Scanner class for reading input
 *    - Different methods for different data types
 *    - Always close Scanner when done
 * 
 * 3. FORMATTING:
 *    - Use format specifiers (%s, %d, %f, etc.)
 *    - Control width and alignment
 *    - Handle precision for floating-point numbers
 * 
 * 4. BEST PRACTICES:
 *    - Validate user input
 *    - Handle exceptions gracefully
 *    - Clear scanner buffer when mixing input types
 *    - Use try-with-resources or finally to close Scanner
 */