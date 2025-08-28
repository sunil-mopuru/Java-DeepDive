/**
 * VariablesAndDataTypes.java - Comprehensive Data Types Demo
 * 
 * This program demonstrates all primitive data types in Java,
 * their ranges, default values, and practical usage examples.
 */
public class VariablesAndDataTypes {
    
    // Class-level variables (fields) - have default values
    static byte defaultByte;           // Default: 0
    static short defaultShort;         // Default: 0
    static int defaultInt;             // Default: 0
    static long defaultLong;           // Default: 0L
    static float defaultFloat;         // Default: 0.0f
    static double defaultDouble;       // Default: 0.0
    static char defaultChar;           // Default: '\u0000'
    static boolean defaultBoolean;     // Default: false
    static String defaultString;       // Default: null
    
    public static void main(String[] args) {
        
        System.out.println("=== JAVA PRIMITIVE DATA TYPES DEMO ===\n");
        
        // INTEGER TYPES
        System.out.println("1. INTEGER TYPES:");
        System.out.println("-------------------");
        
        // byte: 8-bit signed integer
        byte studentAge = 21;
        byte minByte = -128;        // Minimum value
        byte maxByte = 127;         // Maximum value
        System.out.printf("byte - Size: 8 bits, Range: %d to %d%n", minByte, maxByte);
        System.out.printf("Student age: %d%n%n", studentAge);
        
        // short: 16-bit signed integer
        short yearFounded = 1995;
        short minShort = -32768;    // Minimum value
        short maxShort = 32767;     // Maximum value
        System.out.printf("short - Size: 16 bits, Range: %d to %d%n", minShort, maxShort);
        System.out.printf("Java founded in: %d%n%n", yearFounded);
        
        // int: 32-bit signed integer (most commonly used)
        int population = 331900000;
        int minInt = Integer.MIN_VALUE;  // -2,147,483,648
        int maxInt = Integer.MAX_VALUE;  //  2,147,483,647
        System.out.printf("int - Size: 32 bits, Range: %,d to %,d%n", minInt, maxInt);
        System.out.printf("US Population (approx): %,d%n%n", population);
        
        // long: 64-bit signed integer
        long distanceToMoon = 384400000L; // Note the 'L' suffix
        long minLong = Long.MIN_VALUE;
        long maxLong = Long.MAX_VALUE;
        System.out.printf("long - Size: 64 bits, Range: %,d to %,d%n", minLong, maxLong);
        System.out.printf("Distance to Moon (km): %,d%n%n", distanceToMoon);
        
        // FLOATING-POINT TYPES
        System.out.println("2. FLOATING-POINT TYPES:");
        System.out.println("--------------------------");
        
        // float: 32-bit IEEE 754 floating point
        float productPrice = 19.99f;    // Note the 'f' suffix
        float piFloat = 3.14159f;
        System.out.printf("float - Size: 32 bits, Precision: ~7 decimal digits%n");
        System.out.printf("Product price: $%.2f%n", productPrice);
        System.out.printf("Pi (float): %.5f%n%n", piFloat);
        
        // double: 64-bit IEEE 754 floating point (default for decimal numbers)
        double salary = 75000.50;
        double piDouble = 3.141592653589793;
        System.out.printf("double - Size: 64 bits, Precision: ~15 decimal digits%n");
        System.out.printf("Annual salary: $%,.2f%n", salary);
        System.out.printf("Pi (double): %.15f%n%n", piDouble);
        
        // CHARACTER TYPE
        System.out.println("3. CHARACTER TYPE:");
        System.out.println("-------------------");
        
        char grade = 'A';              // Single character
        char unicodeChar = '\u0041';   // Unicode for 'A'
        char asciiCode = 65;           // ASCII code for 'A'
        char newline = '\n';           // Escape sequence
        char tab = '\t';               // Tab character
        
        System.out.printf("char - Size: 16 bits, Range: 0 to 65,535 (Unicode)%n");
        System.out.printf("Grade: %c%n", grade);
        System.out.printf("Unicode \\u0041: %c%n", unicodeChar);
        System.out.printf("ASCII 65: %c%n", asciiCode);
        System.out.printf("Special characters: newline:%c tab:%c%n%n", newline, tab);
        
        // BOOLEAN TYPE
        System.out.println("4. BOOLEAN TYPE:");
        System.out.println("-----------------");
        
        boolean isJavaFun = true;
        boolean isProgrammingEasy = false;
        boolean testResult = (5 > 3);  // Expression result
        
        System.out.printf("boolean - Size: JVM dependent, Values: true or false%n");
        System.out.printf("Is Java fun? %b%n", isJavaFun);
        System.out.printf("Is programming easy? %b%n", isProgrammingEasy);
        System.out.printf("Is 5 > 3? %b%n%n", testResult);
        
        // REFERENCE TYPES
        System.out.println("5. REFERENCE TYPES:");
        System.out.println("--------------------");
        
        String courseName = "Java Fundamentals";
        String instructor = "AI Assistant";
        int[] scores = {95, 87, 92, 78, 88};
        
        System.out.printf("String (Reference type): %s%n", courseName);
        System.out.printf("Instructor: %s%n", instructor);
        System.out.printf("Array (Reference type): ");
        for (int i = 0; i < scores.length; i++) {
            System.out.printf("%d%s", scores[i], (i < scores.length - 1) ? ", " : "%n%n");
        }
        
        // TYPE CONVERSION AND CASTING
        System.out.println("6. TYPE CONVERSION:");
        System.out.println("--------------------");
        
        // Implicit conversion (widening)
        int intValue = 100;
        long longValue = intValue;      // int → long (automatic)
        float floatValue = intValue;    // int → float (automatic)
        double doubleValue = floatValue; // float → double (automatic)
        
        System.out.println("Implicit conversions (widening):");
        System.out.printf("int %d → long %d%n", intValue, longValue);
        System.out.printf("int %d → float %.1f%n", intValue, floatValue);
        System.out.printf("float %.1f → double %.1f%n", floatValue, doubleValue);
        
        // Explicit conversion (casting)
        double largeDouble = 99.99;
        int castToInt = (int) largeDouble;      // double → int (explicit)
        float castToFloat = (float) largeDouble; // double → float (explicit)
        
        System.out.println("\nExplicit conversions (casting):");
        System.out.printf("double %.2f → int %d (truncated)%n", largeDouble, castToInt);
        System.out.printf("double %.2f → float %.2f%n", largeDouble, castToFloat);
        
        // DEFAULT VALUES DEMONSTRATION
        System.out.println("\n7. DEFAULT VALUES:");
        System.out.println("-------------------");
        System.out.printf("Default byte: %d%n", defaultByte);
        System.out.printf("Default short: %d%n", defaultShort);
        System.out.printf("Default int: %d%n", defaultInt);
        System.out.printf("Default long: %d%n", defaultLong);
        System.out.printf("Default float: %.1f%n", defaultFloat);
        System.out.printf("Default double: %.1f%n", defaultDouble);
        System.out.printf("Default char: '%c' (Unicode: \\u%04X)%n", 
                         defaultChar, (int)defaultChar);
        System.out.printf("Default boolean: %b%n", defaultBoolean);
        System.out.printf("Default String: %s%n", defaultString);
        
        // CONSTANTS
        System.out.println("\n8. CONSTANTS:");
        System.out.println("---------------");
        
        final double PI = 3.141592653589793;    // Mathematical constant
        final int DAYS_IN_WEEK = 7;             // Fixed value
        final String COMPANY_NAME = "TechCorp"; // Unchangeable string
        
        System.out.printf("PI (final): %.15f%n", PI);
        System.out.printf("Days in week (final): %d%n", DAYS_IN_WEEK);
        System.out.printf("Company name (final): %s%n", COMPANY_NAME);
        
        // Attempting to change final variable would cause compilation error
        // PI = 3.14; // Error: Cannot assign a value to final variable PI
    }
}

/*
 * Key Learning Points:
 * 1. Java has 8 primitive data types + reference types
 * 2. Each type has specific size, range, and purpose
 * 3. Local variables must be initialized before use
 * 4. Class-level variables have default values
 * 5. Use appropriate type for your data requirements
 * 6. Be aware of type conversion rules (implicit vs explicit)
 * 7. Use 'final' keyword for constants
 */