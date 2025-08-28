/**
 * ExceptionHandlingExamples.java - Comprehensive Exception Handling Examples
 * 
 * This program demonstrates various exception handling techniques,
 * custom exceptions, and best practices for error management.
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ExceptionHandlingExamples {
    
    public static void main(String[] args) {
        
        System.out.println("=== EXCEPTION HANDLING EXAMPLES ===\n");
        
        // 1. BASIC EXCEPTION HANDLING
        basicExceptionHandling();
        
        // 2. MULTIPLE CATCH BLOCKS
        multipleCatchBlocks();
        
        // 3. TRY-WITH-RESOURCES
        tryWithResourcesExample();
        
        // 4. CUSTOM EXCEPTIONS
        customExceptionsDemo();
        
        // 5. EXCEPTION PROPAGATION
        exceptionPropagationDemo();
        
        // 6. PRACTICAL APPLICATIONS
        practicalApplicationsDemo();
    }
    
    // ==================== BASIC EXCEPTION HANDLING ====================
    
    public static void basicExceptionHandling() {
        System.out.println("1. BASIC EXCEPTION HANDLING:");
        System.out.println("-----------------------------");
        
        // Division by zero example
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: Cannot divide by zero - " + e.getMessage());
        } finally {
            System.out.println("Division operation completed");
        }
        
        // Array index out of bounds
        try {
            int[] numbers = {1, 2, 3, 4, 5};
            System.out.println("Element at index 10: " + numbers[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Array index out of bounds - " + e.getMessage());
        }
        
        // Null pointer exception
        try {
            String str = null;
            int length = str.length();
        } catch (NullPointerException e) {
            System.out.println("Error: Null pointer access - " + e.getMessage());
        }
        
        System.out.println();
    }
    
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return a / b;
    }
    
    // ==================== MULTIPLE CATCH BLOCKS ====================
    
    public static void multipleCatchBlocks() {
        System.out.println("2. MULTIPLE CATCH BLOCKS:");
        System.out.println("--------------------------");
        
        Scanner scanner = new Scanner(System.in);
        
        try {
            System.out.print("Enter a number: ");
            String input = "abc"; // Simulating invalid input
            int number = Integer.parseInt(input);
            
            int[] array = {1, 2, 3};
            System.out.println("Element: " + array[number]);
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        } finally {
            System.out.println("Input processing completed");
        }
        
        // Multi-catch block (Java 7+)
        try {
            performRiskyOperation();
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Expected error occurred: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    public static void performRiskyOperation() throws IOException {
        // Simulate an IOException
        throw new IOException("Network connection failed");
    }
    
    // ==================== TRY-WITH-RESOURCES ====================
    
    public static void tryWithResourcesExample() {
        System.out.println("3. TRY-WITH-RESOURCES:");
        System.out.println("-----------------------");
        
        // File reading with automatic resource management
        String filename = "sample.txt";
        
        // Create a sample file first
        try {
            Files.write(Paths.get(filename), "Hello World\nJava Exception Handling\nTry-with-resources".getBytes());
        } catch (IOException e) {
            System.out.println("Failed to create sample file: " + e.getMessage());
        }
        
        // Read file with try-with-resources
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            System.out.println("File contents:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("  " + line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        // BufferedReader is automatically closed here
        
        // Multiple resources
        try (FileInputStream fis = new FileInputStream(filename);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            
            System.out.println("First line: " + br.readLine());
            
        } catch (IOException e) {
            System.out.println("Error with multiple resources: " + e.getMessage());
        }
        // All resources are automatically closed
        
        // Clean up
        try {
            Files.deleteIfExists(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Failed to delete sample file: " + e.getMessage());
        }
        
        System.out.println();
    }
    
    // ==================== CUSTOM EXCEPTIONS ====================
    
    public static void customExceptionsDemo() {
        System.out.println("4. CUSTOM EXCEPTIONS:");
        System.out.println("----------------------");
        
        BankAccount account = new BankAccount("12345", 1000.0);
        
        try {
            account.withdraw(500.0);
            System.out.println("Withdrawal successful. New balance: $" + account.getBalance());
            
            account.withdraw(600.0); // This should fail
            
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            System.out.println("Requested: $" + e.getRequestedAmount());
            System.out.println("Available: $" + e.getAvailableBalance());
        } catch (InvalidAccountException e) {
            System.out.println("Account error: " + e.getMessage());
        }
        
        // Age validation example
        try {
            Person person = new Person("John", -5); // Invalid age
        } catch (InvalidAgeException e) {
            System.out.println("Person creation failed: " + e.getMessage());
            System.out.println("Invalid age: " + e.getAge());
        }
        
        System.out.println();
    }
    
    // ==================== EXCEPTION PROPAGATION ====================
    
    public static void exceptionPropagationDemo() {
        System.out.println("5. EXCEPTION PROPAGATION:");
        System.out.println("--------------------------");
        
        try {
            methodA();
        } catch (CustomBusinessException e) {
            System.out.println("Caught in main: " + e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
            
            // Print stack trace for debugging
            System.out.println("Stack trace:");
            e.printStackTrace();
        }
        
        // Exception chaining example
        try {
            processData("invalid_data");
        } catch (DataProcessingException e) {
            System.out.println("Processing failed: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
        
        System.out.println();
    }
    
    public static void methodA() throws CustomBusinessException {
        methodB();
    }
    
    public static void methodB() throws CustomBusinessException {
        methodC();
    }
    
    public static void methodC() throws CustomBusinessException {
        throw new CustomBusinessException("Error occurred in methodC", "ERR_001");
    }
    
    public static void processData(String data) throws DataProcessingException {
        try {
            // Simulate data processing that might fail
            if ("invalid_data".equals(data)) {
                throw new IllegalArgumentException("Data format is invalid");
            }
            // Process data...
        } catch (IllegalArgumentException e) {
            // Wrap and re-throw with more context
            throw new DataProcessingException("Failed to process data: " + data, e);
        }
    }
    
    // ==================== PRACTICAL APPLICATIONS ====================
    
    public static void practicalApplicationsDemo() {
        System.out.println("6. PRACTICAL APPLICATIONS:");
        System.out.println("--------------------------");
        
        // Configuration loader with error handling
        ConfigurationLoader loader = new ConfigurationLoader();
        
        try {
            Properties config = loader.loadConfiguration("config.properties");
            System.out.println("Configuration loaded successfully");
        } catch (ConfigurationException e) {
            System.out.println("Configuration error: " + e.getMessage());
            
            // Use default configuration
            Properties defaultConfig = loader.getDefaultConfiguration();
            System.out.println("Using default configuration with " + defaultConfig.size() + " properties");
        }
        
        // Database connection simulator
        DatabaseManager dbManager = new DatabaseManager();
        
        try {
            dbManager.connect();
            dbManager.executeQuery("SELECT * FROM users");
        } catch (DatabaseConnectionException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            System.out.println("Retry attempt: " + e.getRetryAttempt());
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            System.out.println("Error code: " + e.getErrorCode());
        } finally {
            try {
                dbManager.disconnect();
            } catch (DatabaseConnectionException e) {
                System.out.println("Warning: Failed to close database connection: " + e.getMessage());
            }
        }
        
        // Input validation framework
        InputValidator validator = new InputValidator();
        
        try {
            validator.validateEmail("invalid-email");
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
            System.out.println("Field: " + e.getFieldName());
            System.out.println("Value: " + e.getFieldValue());
        }
        
        System.out.println();
    }
}

// ==================== CUSTOM EXCEPTION CLASSES ====================

// Custom checked exception for insufficient funds
class InsufficientFundsException extends Exception {
    private final double requestedAmount;
    private final double availableBalance;
    
    public InsufficientFundsException(double requested, double available) {
        super(String.format("Insufficient funds: requested $%.2f, available $%.2f", 
                          requested, available));
        this.requestedAmount = requested;
        this.availableBalance = available;
    }
    
    public double getRequestedAmount() { return requestedAmount; }
    public double getAvailableBalance() { return availableBalance; }
}

// Custom unchecked exception for invalid account
class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(String message) {
        super(message);
    }
}

// Custom exception with error codes
class CustomBusinessException extends Exception {
    private final String errorCode;
    
    public CustomBusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() { return errorCode; }
}

// Exception chaining example
class DataProcessingException extends Exception {
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Age validation exception
class InvalidAgeException extends RuntimeException {
    private final int age;
    
    public InvalidAgeException(int age) {
        super("Invalid age: " + age + ". Age must be between 0 and 150.");
        this.age = age;
    }
    
    public int getAge() { return age; }
}

// Configuration exception
class ConfigurationException extends Exception {
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Database exceptions
class DatabaseConnectionException extends Exception {
    private final int retryAttempt;
    
    public DatabaseConnectionException(String message, int retryAttempt) {
        super(message);
        this.retryAttempt = retryAttempt;
    }
    
    public int getRetryAttempt() { return retryAttempt; }
}

class SQLException extends Exception {
    private final int errorCode;
    
    public SQLException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public int getErrorCode() { return errorCode; }
}

// Validation exception
class ValidationException extends Exception {
    private final String fieldName;
    private final String fieldValue;
    
    public ValidationException(String fieldName, String fieldValue, String message) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    public String getFieldName() { return fieldName; }
    public String getFieldValue() { return fieldValue; }
}

// ==================== SUPPORTING CLASSES ====================

class BankAccount {
    private final String accountNumber;
    private double balance;
    
    public BankAccount(String accountNumber, double initialBalance) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new InvalidAccountException("Account number cannot be null or empty");
        }
        if (initialBalance < 0) {
            throw new InvalidAccountException("Initial balance cannot be negative");
        }
        
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }
        
        balance -= amount;
    }
    
    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
}

class Person {
    private final String name;
    private final int age;
    
    public Person(String name, int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException(age);
        }
        
        this.name = name;
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
}

class ConfigurationLoader {
    
    public Properties loadConfiguration(String filename) throws ConfigurationException {
        try {
            Properties props = new Properties();
            // Simulate configuration loading that might fail
            throw new FileNotFoundException("Configuration file not found: " + filename);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load configuration from " + filename, e);
        }
    }
    
    public Properties getDefaultConfiguration() {
        Properties defaults = new Properties();
        defaults.setProperty("app.name", "MyApp");
        defaults.setProperty("app.version", "1.0.0");
        defaults.setProperty("debug.enabled", "false");
        return defaults;
    }
}

class DatabaseManager {
    private boolean connected = false;
    
    public void connect() throws DatabaseConnectionException {
        // Simulate connection that might fail
        if (Math.random() < 0.7) { // 70% chance of failure for demo
            throw new DatabaseConnectionException("Unable to connect to database server", 1);
        }
        connected = true;
        System.out.println("Database connected successfully");
    }
    
    public void executeQuery(String query) throws SQLException {
        if (!connected) {
            throw new SQLException("Not connected to database", 1001);
        }
        
        // Simulate query execution
        if (query.contains("invalid")) {
            throw new SQLException("Invalid SQL syntax", 2001);
        }
        
        System.out.println("Query executed: " + query);
    }
    
    public void disconnect() throws DatabaseConnectionException {
        if (connected) {
            connected = false;
            System.out.println("Database disconnected");
        }
    }
}

class InputValidator {
    
    public void validateEmail(String email) throws ValidationException {
        if (email == null || !email.contains("@")) {
            throw new ValidationException("email", email, "Invalid email format");
        }
    }
    
    public void validateAge(int age) throws ValidationException {
        if (age < 0 || age > 150) {
            throw new ValidationException("age", String.valueOf(age), "Age must be between 0 and 150");
        }
    }
    
    public void validatePhoneNumber(String phone) throws ValidationException {
        if (phone == null || phone.length() < 10) {
            throw new ValidationException("phone", phone, "Phone number must be at least 10 digits");
        }
    }
}