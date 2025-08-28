/**
 * ClassesAndObjectsDemo.java - Comprehensive OOP Basics Examples
 * 
 * This program demonstrates fundamental OOP concepts including classes,
 * objects, methods, constructors, and encapsulation with practical examples.
 */

public class ClassesAndObjectsDemo {
    
    public static void main(String[] args) {
        
        System.out.println("=== OBJECT-ORIENTED PROGRAMMING BASICS DEMO ===\n");
        
        // 1. CREATING AND USING OBJECTS
        System.out.println("1. CREATING AND USING OBJECTS:");
        System.out.println("-------------------------------");
        
        // Create Student objects using different constructors
        Student student1 = new Student();  // Default constructor
        Student student2 = new Student("Alice Johnson", 12345);
        Student student3 = new Student("Bob Smith", 12346, "Computer Science", 3.8);
        
        // Display initial information
        System.out.println("Initial student information:");
        student1.displayInfo();
        student2.displayInfo();
        student3.displayInfo();
        
        // 2. METHOD CALLS AND OBJECT BEHAVIOR
        System.out.println("\n2. METHOD CALLS AND BEHAVIOR:");
        System.out.println("------------------------------");
        
        // Modify student1 using setter methods
        student1.setName("Charlie Brown");
        student1.setStudentId(12347);
        student1.setMajor("Mathematics");
        student1.setGpa(3.5);
        
        System.out.println("After setting student1 information:");
        student1.displayInfo();
        
        // Add grades to student2
        student2.addGrade(85.5);
        student2.addGrade(92.0);
        student2.addGrade(78.5);
        student2.addGrade(88.0);
        
        System.out.println("After adding grades to student2:");
        student2.displayInfo();
        
        // 3. BANK ACCOUNT SYSTEM DEMONSTRATION
        System.out.println("\n3. BANK ACCOUNT SYSTEM:");
        System.out.println("------------------------");
        
        // Create bank accounts
        BankAccount account1 = new BankAccount("John Doe", 1500.00);
        BankAccount account2 = new BankAccount("Jane Smith", 2500.00);
        
        System.out.println("Initial account information:");
        account1.displayAccountInfo();
        account2.displayAccountInfo();
        
        // Perform transactions
        System.out.println("\nPerforming transactions:");
        account1.deposit(500.00);
        account1.withdraw(200.00);
        account1.withdraw(3000.00);  // Should fail - insufficient funds
        
        account2.deposit(1000.00);
        account2.withdraw(1500.00);
        account2.applyInterest();
        
        System.out.println("\nFinal account information:");
        account1.displayAccountInfo();
        account2.displayAccountInfo();
        
        // Display bank statistics
        System.out.printf("Total accounts created: %d%n", BankAccount.getTotalAccounts());
        System.out.printf("Current interest rate: %.2f%%%n", BankAccount.getInterestRate() * 100);
        
        // 4. CAR MANAGEMENT SYSTEM
        System.out.println("\n4. CAR MANAGEMENT SYSTEM:");
        System.out.println("--------------------------");
        
        // Create different types of cars
        Car sedan = new Car("Toyota", "Camry", 2022, "Blue", 4);
        Car suv = new Car("Honda", "CR-V", 2023, "Red", 5);
        Car truck = new Car("Ford", "F-150", 2022, "Black", 2);
        
        System.out.println("Vehicle fleet:");
        sedan.displayInfo();
        suv.displayInfo();
        truck.displayInfo();
        
        // Demonstrate car operations
        System.out.println("\nCar operations demonstration:");
        sedan.start();
        sedan.accelerate(60);
        sedan.brake(20);
        sedan.displayStatus();
        
        suv.start();
        suv.accelerate(80);
        suv.accelerate(30);  // Speed limit check
        suv.displayStatus();
        suv.stop();
        
        // 5. LIBRARY BOOK SYSTEM
        System.out.println("\n5. LIBRARY BOOK SYSTEM:");
        System.out.println("------------------------");
        
        // Create books
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 2004);
        Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "978-0446310789", 1988);
        Book book3 = new Book("1984", "George Orwell", "978-0451524935", 1949);
        
        System.out.println("Library inventory:");
        book1.displayInfo();
        book2.displayInfo();
        book3.displayInfo();
        
        // Demonstrate book operations
        System.out.println("\nBook checkout system:");
        book1.checkOut("Alice Johnson");
        book1.checkOut("Bob Smith");  // Should fail - already checked out
        book1.returnBook();
        book1.checkOut("Carol Davis");
        
        book2.checkOut("Dave Wilson");
        
        System.out.println("\nCurrent book status:");
        book1.displayStatus();
        book2.displayStatus();
        book3.displayStatus();
        
        // 6. RECTANGLE GEOMETRY CALCULATOR
        System.out.println("\n6. RECTANGLE GEOMETRY:");
        System.out.println("-----------------------");
        
        // Create rectangles using different constructors
        Rectangle square = new Rectangle(5);          // Square
        Rectangle rect1 = new Rectangle(8, 6);        // Rectangle
        Rectangle rect2 = new Rectangle();            // Default 1x1
        
        System.out.println("Rectangle calculations:");
        square.displayInfo();
        System.out.printf("Square area: %.2f, perimeter: %.2f%n%n", 
                         square.calculateArea(), square.calculatePerimeter());
        
        rect1.displayInfo();
        System.out.printf("Rectangle area: %.2f, perimeter: %.2f%n%n", 
                         rect1.calculateArea(), rect1.calculatePerimeter());
        
        rect2.displayInfo();
        rect2.setDimensions(12, 8);
        System.out.println("After resizing:");
        rect2.displayInfo();
        
        // 7. COUNTER SYSTEM (STATIC VS INSTANCE)
        System.out.println("7. COUNTER SYSTEM:");
        System.out.println("-------------------");
        
        // Demonstrate static vs instance variables
        Counter counter1 = new Counter("Counter A");
        Counter counter2 = new Counter("Counter B");
        Counter counter3 = new Counter("Counter C");
        
        // Increment different counters
        counter1.increment();
        counter1.increment();
        counter1.increment();
        
        counter2.increment();
        counter2.increment();
        
        counter3.increment();
        
        // Display results
        System.out.println("Counter results:");
        counter1.displayStatus();
        counter2.displayStatus();
        counter3.displayStatus();
        
        System.out.printf("Total counter objects created: %d%n", Counter.getTotalCounters());
        System.out.printf("Global increment count: %d%n", Counter.getGlobalCount());
        
        // 8. CALCULATOR WITH METHOD OVERLOADING
        System.out.println("\n8. CALCULATOR WITH OVERLOADING:");
        System.out.println("--------------------------------");
        
        Calculator calc = new Calculator();
        
        // Demonstrate method overloading
        System.out.println("Method overloading examples:");
        System.out.printf("add(5, 3) = %d%n", calc.add(5, 3));
        System.out.printf("add(5, 3, 2) = %d%n", calc.add(5, 3, 2));
        System.out.printf("add(5.5, 3.2) = %.2f%n", calc.add(5.5, 3.2));
        System.out.printf("add(5.5, 3.2, 1.8) = %.2f%n", calc.add(5.5, 3.2, 1.8));
        
        System.out.printf("multiply(4, 6) = %d%n", calc.multiply(4, 6));
        System.out.printf("multiply(4.5, 2.5) = %.2f%n", calc.multiply(4.5, 2.5));
        
        // 9. PERSON CLASS WITH THIS KEYWORD DEMO
        System.out.println("\n9. 'THIS' KEYWORD DEMONSTRATION:");
        System.out.println("----------------------------------");
        
        Person person1 = new Person("John", 30, "Engineer");
        Person person2 = new Person("Mary", 25);
        Person person3 = new Person("David");
        
        System.out.println("Person objects created:");
        person1.displayInfo();
        person2.displayInfo();
        person3.displayInfo();
        
        // Method chaining example
        person3.setName("David Johnson")
               .setAge(35)
               .setOccupation("Manager");
        
        System.out.println("After method chaining:");
        person3.displayInfo();
    }
}

// ==================== SUPPORTING CLASSES ====================

/**
 * Student class demonstrating basic OOP concepts
 */
class Student {
    // Private instance variables (encapsulation)
    private String name;
    private int studentId;
    private String major;
    private double gpa;
    private int gradeCount;
    private double totalPoints;
    
    // Default constructor
    public Student() {
        this.name = "Unknown";
        this.studentId = 0;
        this.major = "Undeclared";
        this.gpa = 0.0;
        this.gradeCount = 0;
        this.totalPoints = 0.0;
    }
    
    // Constructor with name and ID
    public Student(String name, int studentId) {
        this.name = name;
        this.studentId = studentId;
        this.major = "Undeclared";
        this.gpa = 0.0;
        this.gradeCount = 0;
        this.totalPoints = 0.0;
    }
    
    // Full constructor
    public Student(String name, int studentId, String major, double gpa) {
        this.name = name;
        this.studentId = studentId;
        this.major = major;
        this.gpa = gpa;
        this.gradeCount = 0;
        this.totalPoints = 0.0;
    }
    
    // Getter methods
    public String getName() { return name; }
    public int getStudentId() { return studentId; }
    public String getMajor() { return major; }
    public double getGpa() { return gpa; }
    
    // Setter methods with validation
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }
    
    public void setStudentId(int studentId) {
        if (studentId > 0) {
            this.studentId = studentId;
        }
    }
    
    public void setMajor(String major) {
        if (major != null && !major.trim().isEmpty()) {
            this.major = major;
        }
    }
    
    public void setGpa(double gpa) {
        if (gpa >= 0.0 && gpa <= 4.0) {
            this.gpa = gpa;
        }
    }
    
    // Method to add a grade and recalculate GPA
    public void addGrade(double grade) {
        if (grade >= 0.0 && grade <= 100.0) {
            totalPoints += grade;
            gradeCount++;
            gpa = (totalPoints / gradeCount) / 25.0;  // Convert to 4.0 scale
            if (gpa > 4.0) gpa = 4.0;
        }
    }
    
    public void displayInfo() {
        System.out.printf("Student: %s (ID: %d) - Major: %s, GPA: %.2f%n", 
                         name, studentId, major, gpa);
    }
}

/**
 * BankAccount class demonstrating encapsulation and static members
 */
class BankAccount {
    // Static variables (class-level)
    private static int nextAccountNumber = 1000;
    private static double interestRate = 0.02;  // 2%
    private static int totalAccounts = 0;
    
    // Instance variables
    private int accountNumber;
    private String holderName;
    private double balance;
    
    // Constructor
    public BankAccount(String holderName, double initialBalance) {
        this.accountNumber = nextAccountNumber++;
        this.holderName = holderName;
        this.balance = initialBalance >= 0 ? initialBalance : 0;
        totalAccounts++;
    }
    
    // Instance methods
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Deposited $%.2f to account %d%n", amount, accountNumber);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.printf("Withdrew $%.2f from account %d%n", amount, accountNumber);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds");
        }
    }
    
    public void applyInterest() {
        double interest = balance * interestRate;
        balance += interest;
        System.out.printf("Interest applied: $%.2f to account %d%n", interest, accountNumber);
    }
    
    // Getters
    public int getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    
    // Static methods
    public static int getTotalAccounts() { return totalAccounts; }
    public static double getInterestRate() { return interestRate; }
    public static void setInterestRate(double rate) {
        if (rate >= 0 && rate <= 1.0) {
            interestRate = rate;
        }
    }
    
    public void displayAccountInfo() {
        System.out.printf("Account %d: %s - Balance: $%.2f%n", 
                         accountNumber, holderName, balance);
    }
}

/**
 * Car class demonstrating object state and behavior
 */
class Car {
    private String make;
    private String model;
    private int year;
    private String color;
    private int doors;
    private double speed;
    private boolean isRunning;
    private static final double MAX_SPEED = 120.0; // km/h
    
    public Car(String make, String model, int year, String color, int doors) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.doors = doors;
        this.speed = 0.0;
        this.isRunning = false;
    }
    
    public void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.printf("%s %s started%n", make, model);
        } else {
            System.out.printf("%s %s is already running%n", make, model);
        }
    }
    
    public void stop() {
        isRunning = false;
        speed = 0.0;
        System.out.printf("%s %s stopped%n", make, model);
    }
    
    public void accelerate(double amount) {
        if (!isRunning) {
            System.out.println("Start the car first!");
            return;
        }
        
        double newSpeed = speed + amount;
        if (newSpeed > MAX_SPEED) {
            System.out.printf("Speed limited to %.0f km/h%n", MAX_SPEED);
            speed = MAX_SPEED;
        } else {
            speed = newSpeed;
            System.out.printf("Accelerated to %.0f km/h%n", speed);
        }
    }
    
    public void brake(double amount) {
        if (isRunning && speed > 0) {
            speed = Math.max(0, speed - amount);
            System.out.printf("Braked to %.0f km/h%n", speed);
        }
    }
    
    public void displayInfo() {
        System.out.printf("%d %s %s (%s, %d doors)%n", year, make, model, color, doors);
    }
    
    public void displayStatus() {
        System.out.printf("Status: %s, Speed: %.0f km/h%n", 
                         isRunning ? "Running" : "Stopped", speed);
    }
}

/**
 * Book class for library management system
 */
class Book {
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private boolean isCheckedOut;
    private String borrowerName;
    
    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.isCheckedOut = false;
        this.borrowerName = null;
    }
    
    public boolean checkOut(String borrowerName) {
        if (!isCheckedOut) {
            this.isCheckedOut = true;
            this.borrowerName = borrowerName;
            System.out.printf("'%s' checked out to %s%n", title, borrowerName);
            return true;
        } else {
            System.out.printf("'%s' is already checked out to %s%n", title, this.borrowerName);
            return false;
        }
    }
    
    public void returnBook() {
        if (isCheckedOut) {
            System.out.printf("'%s' returned by %s%n", title, borrowerName);
            this.isCheckedOut = false;
            this.borrowerName = null;
        } else {
            System.out.printf("'%s' is not currently checked out%n", title);
        }
    }
    
    public void displayInfo() {
        System.out.printf("'%s' by %s (%d) - ISBN: %s%n", 
                         title, author, publicationYear, isbn);
    }
    
    public void displayStatus() {
        if (isCheckedOut) {
            System.out.printf("'%s' - CHECKED OUT to %s%n", title, borrowerName);
        } else {
            System.out.printf("'%s' - AVAILABLE%n", title);
        }
    }
}

/**
 * Rectangle class demonstrating constructor overloading
 */
class Rectangle {
    private double length;
    private double width;
    
    // Default constructor (1x1 square)
    public Rectangle() {
        this(1.0, 1.0);
    }
    
    // Square constructor
    public Rectangle(double side) {
        this(side, side);
    }
    
    // Full constructor
    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
    
    public double calculateArea() {
        return length * width;
    }
    
    public double calculatePerimeter() {
        return 2 * (length + width);
    }
    
    public void setDimensions(double length, double width) {
        this.length = length;
        this.width = width;
    }
    
    public void displayInfo() {
        System.out.printf("Rectangle: %.1f x %.1f%n", length, width);
    }
}

/**
 * Counter class demonstrating static vs instance variables
 */
class Counter {
    private String name;
    private int count;
    private static int totalCounters = 0;
    private static int globalCount = 0;
    
    public Counter(String name) {
        this.name = name;
        this.count = 0;
        totalCounters++;
    }
    
    public void increment() {
        count++;
        globalCount++;
    }
    
    public void displayStatus() {
        System.out.printf("%s: %d counts%n", name, count);
    }
    
    public static int getTotalCounters() { return totalCounters; }
    public static int getGlobalCount() { return globalCount; }
}

/**
 * Calculator class demonstrating method overloading
 */
class Calculator {
    // Integer addition - two parameters
    public int add(int a, int b) {
        return a + b;
    }
    
    // Integer addition - three parameters
    public int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Double addition - two parameters
    public double add(double a, double b) {
        return a + b;
    }
    
    // Double addition - three parameters
    public double add(double a, double b, double c) {
        return a + b + c;
    }
    
    // Integer multiplication
    public int multiply(int a, int b) {
        return a * b;
    }
    
    // Double multiplication
    public double multiply(double a, double b) {
        return a * b;
    }
}

/**
 * Person class demonstrating 'this' keyword and method chaining
 */
class Person {
    private String name;
    private int age;
    private String occupation;
    
    // Constructor using 'this' for constructor chaining
    public Person(String name) {
        this(name, 0, "Unknown");
    }
    
    public Person(String name, int age) {
        this(name, age, "Unknown");
    }
    
    public Person(String name, int age, String occupation) {
        this.name = name;      // 'this' distinguishes parameter from field
        this.age = age;
        this.occupation = occupation;
    }
    
    // Method chaining - returning 'this'
    public Person setName(String name) {
        this.name = name;
        return this;  // Return current object for chaining
    }
    
    public Person setAge(int age) {
        this.age = age;
        return this;
    }
    
    public Person setOccupation(String occupation) {
        this.occupation = occupation;
        return this;
    }
    
    public void displayInfo() {
        System.out.printf("Person: %s, Age: %d, Occupation: %s%n", 
                         name, age, occupation);
    }
}