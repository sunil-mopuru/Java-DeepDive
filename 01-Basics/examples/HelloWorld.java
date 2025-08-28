/**
 * HelloWorld.java - Your First Java Program
 * 
 * This program demonstrates the basic structure of a Java application
 * and introduces fundamental concepts like classes, methods, and output.
 * 
 * @author Java Fundamentals Course
 * @version 1.0
 */
public class HelloWorld {
    
    /**
     * The main method - Entry point of every Java application
     * 
     * @param args Command line arguments (array of strings)
     */
    public static void main(String[] args) {
        // Single-line comment: This prints a message to the console
        System.out.println("Hello, World!");
        
        /*
         * Multi-line comment:
         * The following lines demonstrate different ways to output text
         */
        System.out.print("Hello ");  // Print without newline
        System.out.print("Java ");   // Print without newline
        System.out.println("World!"); // Print with newline
        
        // Using escape sequences
        System.out.println("Welcome to \"Java Programming\"");
        System.out.println("Path: C:\\Programming\\Java");
        System.out.println("Line 1\nLine 2\nLine 3");
        System.out.println("Tab\tSeparated\tValues");
        
        // Formatted output
        System.out.printf("Today is: %s %d, %d%n", "August", 28, 2025);
    }
}

/*
 * Program Output:
 * ---------------
 * Hello, World!
 * Hello Java World!
 * Welcome to "Java Programming"
 * Path: C:\Programming\Java
 * Line 1
 * Line 2
 * Line 3
 * Tab	Separated	Values
 * Today is: August 28, 2025
 */