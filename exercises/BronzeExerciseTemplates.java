/**
 * Bronze Level Exercise Templates
 * 
 * These starter templates provide the basic structure for Bronze level exercises.
 * Students can use these as starting points for their implementations.
 */

// ==================== EXERCISE B1: PERSONAL FINANCE TRACKER ====================

import java.time.LocalDate;
import java.util.*;

/**
 * Template for Personal Finance Tracker Exercise
 * 
 * TODO: Complete the implementation of all methods
 * TODO: Add input validation and error handling
 * TODO: Implement report generation functionality
 */

class Transaction {
    private LocalDate date;
    private double amount;
    private String description;
    private String category;
    
    // TODO: Implement constructor
    public Transaction(LocalDate date, double amount, String description, String category) {
        // Your implementation here
    }
    
    // TODO: Implement getters and setters
    public LocalDate getDate() { return date; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    
    // TODO: Implement toString method for display
    @Override
    public String toString() {
        // Your implementation here
        return "";
    }
}

class Account {
    private String accountName;
    private double initialBalance;
    private List<Transaction> transactions;
    
    // TODO: Implement constructor
    public Account(String accountName, double initialBalance) {
        // Your implementation here
    }
    
    // TODO: Add a transaction to the account
    public void addTransaction(Transaction transaction) {
        // Your implementation here
        // Remember to validate the transaction
    }
    
    // TODO: Calculate current balance
    public double getBalance() {
        // Your implementation here
        // Start with initial balance and add/subtract transactions
        return 0.0;
    }
    
    // TODO: Get transactions by category
    public List<Transaction> getTransactionsByCategory(String category) {
        // Your implementation here
        return new ArrayList<>();
    }
    
    // TODO: Get transactions for a specific month
    public List<Transaction> getTransactionsForMonth(int year, int month) {
        // Your implementation here
        return new ArrayList<>();
    }
    
    // TODO: Generate spending report by category
    public void generateCategoryReport() {
        // Your implementation here
        // Group transactions by category and sum amounts
    }
}

// ==================== EXERCISE B2: LIBRARY MANAGEMENT SYSTEM ====================

/**
 * Template for Library Management System Exercise
 * 
 * TODO: Implement inheritance hierarchy
 * TODO: Add search functionality
 * TODO: Implement checkout/return system
 */

abstract class Item {
    protected String id;
    protected String title;
    protected boolean checkedOut;
    protected LocalDate dueDate;
    
    // TODO: Implement constructor
    public Item(String id, String title) {
        // Your implementation here
    }
    
    // TODO: Abstract method for item-specific display
    public abstract String getItemType();
    
    // TODO: Check out item
    public void checkOut(int daysToReturn) {
        // Your implementation here
    }
    
    // TODO: Return item
    public void returnItem() {
        // Your implementation here
    }
    
    // TODO: Check if item is overdue
    public boolean isOverdue() {
        // Your implementation here
        return false;
    }
    
    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCheckedOut() { return checkedOut; }
    public LocalDate getDueDate() { return dueDate; }
}

class Book extends Item {
    private String author;
    private String isbn;
    private int pages;
    
    // TODO: Implement constructor
    public Book(String id, String title, String author, String isbn, int pages) {
        // Your implementation here
    }
    
    @Override
    public String getItemType() {
        return "Book";
    }
    
    // TODO: Implement getters for book-specific fields
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPages() { return pages; }
    
    @Override
    public String toString() {
        // Your implementation here
        return "";
    }
}

class Magazine extends Item {
    private String publisher;
    private int issueNumber;
    private LocalDate publicationDate;
    
    // TODO: Implement constructor and methods
    public Magazine(String id, String title, String publisher, int issueNumber, LocalDate publicationDate) {
        // Your implementation here
    }
    
    @Override
    public String getItemType() {
        return "Magazine";
    }
    
    // TODO: Implement getters and toString
}

class DVD extends Item {
    private String director;
    private int duration; // in minutes
    private String genre;
    
    // TODO: Implement constructor and methods
    public DVD(String id, String title, String director, int duration, String genre) {
        // Your implementation here
    }
    
    @Override
    public String getItemType() {
        return "DVD";
    }
    
    // TODO: Implement getters and toString
}

class Library {
    private String libraryName;
    private List<Item> items;
    
    // TODO: Implement constructor
    public Library(String libraryName) {
        // Your implementation here
    }
    
    // TODO: Add item to library
    public void addItem(Item item) {
        // Your implementation here
    }
    
    // TODO: Search items by title (partial match)
    public List<Item> searchByTitle(String title) {
        // Your implementation here
        return new ArrayList<>();
    }
    
    // TODO: Search books by author
    public List<Book> searchByAuthor(String author) {
        // Your implementation here
        return new ArrayList<>();
    }
    
    // TODO: Get all checked out items
    public List<Item> getCheckedOutItems() {
        // Your implementation here
        return new ArrayList<>();
    }
    
    // TODO: Get overdue items
    public List<Item> getOverdueItems() {
        // Your implementation here
        return new ArrayList<>();
    }
    
    // TODO: Generate inventory report
    public void generateInventoryReport() {
        // Your implementation here
        // Show total items by type, checked out vs available
    }
}

// ==================== EXERCISE B3: STUDENT GRADE CALCULATOR ====================

/**
 * Template for Student Grade Calculator Exercise
 * 
 * TODO: Implement CSV reading functionality
 * TODO: Add grade calculation methods
 * TODO: Create statistical analysis features
 */

class StudentGrade {
    private String studentName;
    private Map<String, Double> scores; // assignment name -> score
    private double finalGrade;
    private String letterGrade;
    
    // TODO: Implement constructor
    public StudentGrade(String studentName) {
        // Your implementation here
    }
    
    // TODO: Add score for an assignment
    public void addScore(String assignmentName, double score) {
        // Your implementation here
        // Validate score range (0-100)
    }
    
    // TODO: Calculate final grade based on weights
    public void calculateFinalGrade(Map<String, Double> assignmentWeights) {
        // Your implementation here
        // Example: Exams 60%, Assignments 30%, Final 10%
    }
    
    // TODO: Convert numeric grade to letter grade
    private void calculateLetterGrade() {
        // Your implementation here
        // A: 90+, B: 80-89, C: 70-79, D: 60-69, F: <60
    }
    
    // Getters
    public String getStudentName() { return studentName; }
    public double getFinalGrade() { return finalGrade; }
    public String getLetterGrade() { return letterGrade; }
    public Map<String, Double> getScores() { return new HashMap<>(scores); }
}

class GradeCalculator {
    private List<StudentGrade> students;
    private Map<String, Double> assignmentWeights;
    
    // TODO: Implement constructor
    public GradeCalculator() {
        // Your implementation here
    }
    
    // TODO: Read student data from CSV file
    public void loadStudentDataFromCSV(String filename) {
        // Your implementation here
        // Parse CSV file and create StudentGrade objects
        // Handle file not found and parsing errors
    }
    
    // TODO: Set assignment weights for grade calculation
    public void setAssignmentWeights(Map<String, Double> weights) {
        // Your implementation here
        // Validate that weights sum to 1.0 (100%)
    }
    
    // TODO: Calculate grades for all students
    public void calculateAllGrades() {
        // Your implementation here
    }
    
    // TODO: Calculate class statistics
    public ClassStatistics calculateClassStatistics() {
        // Your implementation here
        return new ClassStatistics();
    }
    
    // TODO: Generate grade report
    public void generateGradeReport(String filename) {
        // Your implementation here
        // Export results to formatted text file
    }
    
    // TODO: Get students by letter grade
    public List<StudentGrade> getStudentsByLetterGrade(String letterGrade) {
        // Your implementation here
        return new ArrayList<>();
    }
}

class ClassStatistics {
    private double averageGrade;
    private double highestGrade;
    private double lowestGrade;
    private double standardDeviation;
    private Map<String, Integer> letterGradeDistribution;
    
    // TODO: Implement constructor and calculation methods
    public ClassStatistics() {
        // Your implementation here
    }
    
    // TODO: Calculate statistics from list of student grades
    public void calculateStatistics(List<StudentGrade> students) {
        // Your implementation here
    }
    
    // TODO: Generate statistics report
    public void printStatistics() {
        // Your implementation here
    }
    
    // Getters
    public double getAverageGrade() { return averageGrade; }
    public double getHighestGrade() { return highestGrade; }
    public double getLowestGrade() { return lowestGrade; }
    public double getStandardDeviation() { return standardDeviation; }
    public Map<String, Integer> getLetterGradeDistribution() { 
        return new HashMap<>(letterGradeDistribution); 
    }
}

// ==================== MAIN CLASS FOR TESTING ====================

public class BronzeExerciseTemplates {
    
    public static void main(String[] args) {
        System.out.println("=== BRONZE LEVEL EXERCISE TEMPLATES ===\n");
        
        // TODO: Uncomment and test each exercise as you implement them
        
        // testPersonalFinanceTracker();
        // testLibraryManagement();
        // testGradeCalculator();
    }
    
    // TODO: Implement test method for Exercise B1
    private static void testPersonalFinanceTracker() {
        System.out.println("Testing Personal Finance Tracker...");
        
        // Create account
        Account account = new Account("My Checking", 1000.0);
        
        // Add some transactions
        account.addTransaction(new Transaction(LocalDate.now(), -50.0, "Groceries", "Food"));
        account.addTransaction(new Transaction(LocalDate.now(), -25.0, "Gas", "Transportation"));
        account.addTransaction(new Transaction(LocalDate.now(), 500.0, "Salary", "Income"));
        
        // Display results
        System.out.printf("Current Balance: $%.2f%n", account.getBalance());
        account.generateCategoryReport();
        
        System.out.println();
    }
    
    // TODO: Implement test method for Exercise B2
    private static void testLibraryManagement() {
        System.out.println("Testing Library Management...");
        
        Library library = new Library("City Library");
        
        // Add items
        library.addItem(new Book("B001", "Java Programming", "John Doe", "978-0123456789", 500));
        library.addItem(new Magazine("M001", "Tech Today", "Tech Publishers", 45, LocalDate.now()));
        library.addItem(new DVD("D001", "Learning Java", "Jane Smith", 120, "Educational"));
        
        // Test search
        List<Item> results = library.searchByTitle("Java");
        System.out.printf("Found %d items matching 'Java'%n", results.size());
        
        library.generateInventoryReport();
        
        System.out.println();
    }
    
    // TODO: Implement test method for Exercise B3
    private static void testGradeCalculator() {
        System.out.println("Testing Grade Calculator...");
        
        GradeCalculator calculator = new GradeCalculator();
        
        // Set up grading weights
        Map<String, Double> weights = new HashMap<>();
        weights.put("Exam1", 0.25);
        weights.put("Exam2", 0.25);
        weights.put("Assignment1", 0.20);
        weights.put("Assignment2", 0.20);
        weights.put("Final", 0.10);
        
        calculator.setAssignmentWeights(weights);
        
        // Load data (you'll need to create a sample CSV file)
        // calculator.loadStudentDataFromCSV("sample_grades.csv");
        
        // Calculate and display results
        calculator.calculateAllGrades();
        ClassStatistics stats = calculator.calculateClassStatistics();
        stats.printStatistics();
        
        System.out.println();
    }
}