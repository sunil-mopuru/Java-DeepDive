/**
 * Java 16 Features Examples - Beginner to Advanced
 * 
 * Demonstrates Java 16 standard features including Records, Pattern Matching 
 * for instanceof, Stream.toList(), and Sealed Classes (Second Preview).
 */

import java.time.*;
import java.util.*;
import java.util.stream.*;

public class Java16Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 16 Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateRecords();
        showPatternMatching();
        exploreStreamToList();
        
        // INTERMEDIATE LEVEL
        buildDataModels();
        createBusinessLogic();
        
        // ADVANCED LEVEL
        buildCompleteSystem();
        performanceAnalysis();
    }
    
    public static void demonstrateRecords() {
        System.out.println("\n1. Records (Production Ready)");
        System.out.println("=============================");
        
        // Basic record usage
        Customer customer = new Customer("CUST001", "Alice Johnson", "alice@example.com", Customer.Type.PREMIUM);
        System.out.println("Customer: " + customer);
        System.out.println("Discount rate: " + customer.getDiscountRate() * 100 + "%");
        
        // Product records
        Product laptop = Product.electronics("LAPTOP001", "Gaming Laptop", 1299.99);
        Product book = Product.book("BOOK001", "Java Programming", 49.99);
        
        System.out.println("\nProducts:");
        System.out.println("Laptop: " + laptop.name() + " - " + laptop.price().formatted());
        System.out.println("Is expensive: " + laptop.isExpensive());
        
        Product discountedLaptop = laptop.withDiscount(15.0);
        System.out.println("After 15% discount: " + discountedLaptop.price().formatted());
        
        // Address records
        Address address = new Address("123 Main St", "Anytown", "CA", "90210", "USA");
        System.out.println("\nAddress: " + address.getFormattedAddress());
        
        // Working with collections of records
        List<Product> products = List.of(
            Product.electronics("TV001", "Smart TV", 899.99),
            Product.book("BOOK002", "Design Patterns", 59.99),
            Product.electronics("PHONE001", "Smartphone", 799.99)
        );
        
        System.out.println("\nProduct catalog:");
        products.forEach(p -> System.out.printf("%-15s %-20s %s%n", 
                                               p.category(), p.name(), p.price().formatted()));
    }
    
    public static void showPatternMatching() {
        System.out.println("\n2. Pattern Matching for instanceof");
        System.out.println("===================================");
        
        List<Object> objects = List.of(
            "Hello World",
            42,
            List.of(1, 2, 3, 4, 5),
            new Customer("CUST002", "Bob Smith", "bob@example.com", Customer.Type.REGULAR),
            Product.electronics("TABLET001", "Tablet", 299.99),
            Arrays.asList("apple", "banana", "cherry")
        );
        
        System.out.println("Processing objects with pattern matching:");
        objects.forEach(obj -> {
            String result = processWithPatternMatching(obj);
            System.out.println("- " + result);
        });
        
        // Shape calculations
        System.out.println("\nShape area calculations:");
        List<Object> shapes = List.of(
            new Circle(5.0),
            new Rectangle(10.0, 6.0),
            new Triangle(8.0, 4.0),
            "Not a shape"
        );
        
        shapes.forEach(shape -> {
            double area = calculateAreaWithPatternMatching(shape);
            String shapeInfo = getShapeInfo(shape);
            System.out.printf("%-20s Area: %.2f%n", shapeInfo, area);
        });
    }
    
    public static String processWithPatternMatching(Object obj) {
        if (obj instanceof String str && str.length() > 5) {
            return "Long string: '" + str + "' (length: " + str.length() + ")";
        } else if (obj instanceof String str) {
            return "Short string: '" + str + "'";
        } else if (obj instanceof Integer num && num > 10) {
            return "Large integer: " + num + " (factorial: " + factorial(num) + ")";
        } else if (obj instanceof Integer num) {
            return "Small integer: " + num;
        } else if (obj instanceof List<?> list && !list.isEmpty()) {
            Object first = list.get(0);
            return "List with " + list.size() + " elements, first: " + first;
        } else if (obj instanceof List<?> list) {
            return "Empty list";
        } else if (obj instanceof Customer customer && customer.type() == Customer.Type.PREMIUM) {
            return "Premium customer: " + customer.name() + " (discount: " + customer.getDiscountRate() * 100 + "%)";
        } else if (obj instanceof Customer customer) {
            return "Customer: " + customer.name() + " (" + customer.type() + ")";
        } else if (obj instanceof Product product && product.isExpensive()) {
            return "Expensive product: " + product.name() + " - " + product.price().formatted();
        } else if (obj instanceof Product product) {
            return "Affordable product: " + product.name() + " - " + product.price().formatted();
        }
        return "Unknown object type: " + obj.getClass().getSimpleName();
    }
    
    public static double calculateAreaWithPatternMatching(Object shape) {
        if (shape instanceof Circle circle && circle.radius() > 0) {
            return Math.PI * circle.radius() * circle.radius();
        } else if (shape instanceof Rectangle rect && rect.width() > 0 && rect.height() > 0) {
            return rect.width() * rect.height();
        } else if (shape instanceof Triangle tri && tri.base() > 0 && tri.height() > 0) {
            return 0.5 * tri.base() * tri.height();
        }
        return 0.0;
    }
    
    public static String getShapeInfo(Object shape) {
        if (shape instanceof Circle circle) {
            return "Circle (r=" + circle.radius() + ")";
        } else if (shape instanceof Rectangle rect) {
            return "Rectangle (" + rect.width() + "x" + rect.height() + ")";
        } else if (shape instanceof Triangle tri) {
            return "Triangle (b=" + tri.base() + ", h=" + tri.height() + ")";
        }
        return "Unknown shape";
    }
    
    public static void exploreStreamToList() {
        System.out.println("\n3. Stream.toList() Method");
        System.out.println("=========================");
        
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Basic toList() usage
        List<Integer> evenNumbers = numbers.stream()
                                          .filter(n -> n % 2 == 0)
                                          .toList();
        
        List<Integer> squares = numbers.stream()
                                      .map(n -> n * n)
                                      .toList();
        
        System.out.println("Original numbers: " + numbers);
        System.out.println("Even numbers: " + evenNumbers);
        System.out.println("Squares: " + squares);
        
        // Complex processing
        List<String> words = List.of("apple", "banana", "cherry", "date", "elderberry", "fig");
        
        List<String> processedWords = words.stream()
                                         .filter(word -> word.length() > 4)
                                         .map(String::toUpperCase)
                                         .sorted()
                                         .toList();
        
        System.out.println("\nOriginal words: " + words);
        System.out.println("Processed words (length > 4, uppercase, sorted): " + processedWords);
        
        // Working with custom objects
        List<Product> products = List.of(
            Product.electronics("LAPTOP001", "Gaming Laptop", 1299.99),
            Product.book("BOOK001", "Java Guide", 49.99),
            Product.electronics("PHONE001", "Smartphone", 799.99),
            Product.book("BOOK002", "Python Basics", 39.99)
        );
        
        List<String> expensiveProductNames = products.stream()
                                                   .filter(Product::isExpensive)
                                                   .map(Product::name)
                                                   .sorted()
                                                   .toList();
        
        System.out.println("\nExpensive products: " + expensiveProductNames);
        
        // Demonstrating immutability
        try {
            evenNumbers.add(12); // This will throw UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("\n✓ Confirmed: toList() returns immutable list");
        }
        
        // When you need a mutable list
        List<Integer> mutableList = numbers.stream()
                                          .filter(n -> n % 2 == 0)
                                          .collect(Collectors.toCollection(ArrayList::new));
        mutableList.add(12);
        System.out.println("Mutable list after adding 12: " + mutableList);
    }
    
    public static void buildDataModels() {
        System.out.println("\n4. Building Data Models with Records");
        System.out.println("====================================");
        
        // Employee management system
        Employee emp1 = new Employee("EMP001", "John Doe", "Software Engineer", 
                                   Department.ENGINEERING, 75000.0, LocalDate.of(2022, 1, 15));
        Employee emp2 = new Employee("EMP002", "Jane Smith", "Product Manager", 
                                   Department.PRODUCT, 85000.0, LocalDate.of(2021, 6, 10));
        Employee emp3 = new Employee("EMP003", "Bob Johnson", "Sales Rep", 
                                   Department.SALES, 60000.0, LocalDate.of(2023, 3, 1));
        
        List<Employee> employees = List.of(emp1, emp2, emp3);
        
        System.out.println("Employee Information:");
        employees.forEach(emp -> {
            System.out.printf("%-12s %-20s %-18s %s %s%n", 
                            emp.id(), emp.name(), emp.title(), 
                            emp.department(), emp.getSalaryFormatted());
            System.out.printf("             Years of service: %.1f, Bonus eligible: %s%n", 
                            emp.getYearsOfService(), emp.isBonusEligible() ? "Yes" : "No");
        });
        
        // Financial records
        BankAccount account = new BankAccount("ACC123456", "John Doe", 5000.0, BankAccount.Type.CHECKING);
        
        System.out.println("\nBanking operations:");
        System.out.println("Initial account: " + account);
        
        BankAccount afterDeposit = account.deposit(1000.0);
        System.out.println("After $1000 deposit: " + afterDeposit);
        
        BankAccount afterWithdrawal = afterDeposit.withdraw(750.0);
        System.out.println("After $750 withdrawal: " + afterWithdrawal);
        
        try {
            afterWithdrawal.withdraw(10000.0); // Should fail
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Withdrawal prevented: " + e.getMessage());
        }
    }
    
    public static void createBusinessLogic() {
        System.out.println("\n5. Business Logic with Pattern Matching");
        System.out.println("=======================================");
        
        // Order processing
        List<Order> orders = createSampleOrders();
        
        System.out.println("Order Processing Results:");
        orders.forEach(order -> {
            String status = processOrder(order);
            System.out.println("Order " + order.id() + ": " + status);
        });
        
        // Payment processing
        System.out.println("\nPayment Processing:");
        List<Object> paymentMethods = List.of(
            new CreditCard("**** 1234", "12/25", "123"),
            new PayPal("user@example.com"),
            new BankTransfer("123456789", "987654321"),
            "Cash" // Unsupported
        );
        
        paymentMethods.forEach(method -> {
            String result = processPayment(method, 100.0);
            System.out.println("- " + result);
        });
        
        // Inventory management
        InventoryManager manager = new InventoryManager();
        System.out.println("\nInventory Analysis:");
        System.out.println(manager.generateInventoryReport());
    }
    
    public static void buildCompleteSystem() {
        System.out.println("\n6. Complete E-Commerce System");
        System.out.println("=============================");
        
        // Create a shopping cart system
        ShoppingCart cart = new ShoppingCart("CART001", "CUST001");
        
        // Add items to cart
        cart = cart.addItem(Product.electronics("LAPTOP001", "Gaming Laptop", 1299.99), 1);
        cart = cart.addItem(Product.book("BOOK001", "Java Programming", 49.99), 2);
        cart = cart.addItem(Product.electronics("MOUSE001", "Gaming Mouse", 79.99), 1);
        
        System.out.println("Shopping Cart Contents:");
        cart.items().forEach(item -> 
            System.out.printf("- %dx %s @ %s each = %s%n",
                            item.quantity(), item.product().name(),
                            item.product().price().formatted(),
                            new Product.Price(item.getTotalPrice(), "USD").formatted()));
        
        System.out.printf("Cart Total: %s%n", new Product.Price(cart.getTotalAmount(), "USD").formatted());
        
        // Process checkout
        Customer customer = new Customer("CUST001", "John Doe", "john@example.com", Customer.Type.PREMIUM);
        Address shippingAddress = new Address("123 Main St", "Anytown", "CA", "90210", "USA");
        
        Order order = cart.checkout(customer, shippingAddress);
        System.out.println("\nOrder created: " + order.id());
        System.out.println("Customer: " + order.customer().name());
        System.out.println("Items: " + order.items().size());
        System.out.printf("Total: %s%n", new Product.Price(order.getTotalAmount(), "USD").formatted());
        
        // Order fulfillment simulation
        Order confirmedOrder = order.updateStatus(Order.Status.CONFIRMED);
        Order shippedOrder = confirmedOrder.updateStatus(Order.Status.SHIPPED);
        
        System.out.println("Order status progression: " + 
                         order.status() + " → " + 
                         confirmedOrder.status() + " → " + 
                         shippedOrder.status());
    }
    
    public static void performanceAnalysis() {
        System.out.println("\n7. Performance Analysis");
        System.out.println("=======================");
        
        // toList() vs collect(toList()) performance
        List<Integer> largeList = IntStream.rangeClosed(1, 1_000_000).boxed().toList();
        
        // Measure toList() performance
        long startTime = System.nanoTime();
        List<Integer> result1 = largeList.stream()
                                        .filter(n -> n % 2 == 0)
                                        .map(n -> n * n)
                                        .toList();
        long toListTime = System.nanoTime() - startTime;
        
        // Measure collect(toList()) performance
        startTime = System.nanoTime();
        List<Integer> result2 = largeList.stream()
                                        .filter(n -> n % 2 == 0)
                                        .map(n -> n * n)
                                        .collect(Collectors.toList());
        long collectTime = System.nanoTime() - startTime;
        
        System.out.printf("toList() time: %.2f ms (result size: %d)%n", 
                         toListTime / 1_000_000.0, result1.size());
        System.out.printf("collect(toList()) time: %.2f ms (result size: %d)%n", 
                         collectTime / 1_000_000.0, result2.size());
        
        // Pattern matching vs traditional instanceof
        comparePatternMatchingPerformance();
        
        // Record vs class performance
        compareRecordVsClassPerformance();
    }
    
    public static void comparePatternMatchingPerformance() {
        System.out.println("\nPattern matching vs traditional instanceof:");
        
        List<Object> testData = IntStream.range(0, 100_000)
                                       .mapToObj(i -> switch (i % 4) {
                                           case 0 -> "String" + i;
                                           case 1 -> i;
                                           case 2 -> List.of(i);
                                           default -> new Circle(i);
                                       })
                                       .toList();
        
        // Pattern matching approach
        long startTime = System.nanoTime();
        long sum1 = 0;
        for (Object obj : testData) {
            if (obj instanceof String str) sum1 += str.length();
            else if (obj instanceof Integer num) sum1 += num;
            else if (obj instanceof List<?> list) sum1 += list.size();
            else if (obj instanceof Circle circle) sum1 += (long) circle.radius();
        }
        long patternTime = System.nanoTime() - startTime;
        
        // Traditional approach
        startTime = System.nanoTime();
        long sum2 = 0;
        for (Object obj : testData) {
            if (obj instanceof String) {
                String str = (String) obj;
                sum2 += str.length();
            } else if (obj instanceof Integer) {
                Integer num = (Integer) obj;
                sum2 += num;
            } else if (obj instanceof List<?>) {
                List<?> list = (List<?>) obj;
                sum2 += list.size();
            } else if (obj instanceof Circle) {
                Circle circle = (Circle) obj;
                sum2 += (long) circle.radius();
            }
        }
        long traditionalTime = System.nanoTime() - startTime;
        
        System.out.printf("Pattern matching: %.2f ms (sum: %d)%n", patternTime / 1_000_000.0, sum1);
        System.out.printf("Traditional instanceof: %.2f ms (sum: %d)%n", traditionalTime / 1_000_000.0, sum2);
    }
    
    public static void compareRecordVsClassPerformance() {
        System.out.println("\nRecord vs class performance:");
        
        int iterations = 1_000_000;
        
        // Record creation and access
        long startTime = System.nanoTime();
        long checksum1 = 0;
        for (int i = 0; i < iterations; i++) {
            Point record = new Point(i, i * 2);
            checksum1 += record.x() + record.y();
        }
        long recordTime = System.nanoTime() - startTime;
        
        // Traditional class creation and access
        startTime = System.nanoTime();
        long checksum2 = 0;
        for (int i = 0; i < iterations; i++) {
            PointClass traditional = new PointClass(i, i * 2);
            checksum2 += traditional.getX() + traditional.getY();
        }
        long classTime = System.nanoTime() - startTime;
        
        System.out.printf("Record: %.2f ms (checksum: %d)%n", recordTime / 1_000_000.0, checksum1);
        System.out.printf("Class: %.2f ms (checksum: %d)%n", classTime / 1_000_000.0, checksum2);
    }
    
    // Helper methods
    private static long factorial(int n) {
        return n <= 1 ? 1 : n * factorial(n - 1);
    }
    
    private static List<Order> createSampleOrders() {
        Customer customer1 = new Customer("CUST001", "Alice", "alice@example.com", Customer.Type.PREMIUM);
        Customer customer2 = new Customer("CUST002", "Bob", "bob@example.com", Customer.Type.REGULAR);
        
        return List.of(
            new Order("ORD001", customer1, List.of(
                new Order.Item(Product.electronics("LAPTOP001", "Laptop", 999.99), 1, 999.99)
            ), Order.Status.PENDING, LocalDateTime.now()),
            
            new Order("ORD002", customer2, List.of(
                new Order.Item(Product.book("BOOK001", "Java Book", 49.99), 2, 49.99),
                new Order.Item(Product.electronics("MOUSE001", "Mouse", 29.99), 1, 29.99)
            ), Order.Status.CONFIRMED, LocalDateTime.now())
        );
    }
    
    private static String processOrder(Order order) {
        return switch (order.status()) {
            case PENDING -> "Order pending - awaiting confirmation";
            case CONFIRMED -> "Order confirmed - preparing for shipment";
            case SHIPPED -> "Order shipped - tracking number: " + generateTrackingNumber();
            case DELIVERED -> "Order delivered - thank you for your purchase";
            case CANCELLED -> "Order cancelled - refund processed";
        };
    }
    
    private static String processPayment(Object paymentMethod, double amount) {
        if (paymentMethod instanceof CreditCard card) {
            return "Credit card payment: $" + amount + " charged to " + card.number();
        } else if (paymentMethod instanceof PayPal paypal) {
            return "PayPal payment: $" + amount + " from " + paypal.email();
        } else if (paymentMethod instanceof BankTransfer transfer) {
            return "Bank transfer: $" + amount + " from account " + transfer.accountNumber();
        } else if (paymentMethod instanceof String method) {
            return "Unsupported payment method: " + method;
        }
        return "Unknown payment method";
    }
    
    private static String generateTrackingNumber() {
        return "TRK" + System.currentTimeMillis();
    }
    
    // Record definitions
    public record Customer(String id, String name, String email, Type type) {
        public enum Type { REGULAR, PREMIUM, VIP }
        
        public double getDiscountRate() {
            return switch (type) {
                case REGULAR -> 0.0;
                case PREMIUM -> 0.05;
                case VIP -> 0.10;
            };
        }
    }
    
    public record Product(String id, String name, Price price, Category category) {
        
        public record Price(double amount, String currency) {
            public Price {
                if (amount < 0) throw new IllegalArgumentException("Price cannot be negative");
                if (currency == null) currency = "USD";
            }
            
            public String formatted() {
                return String.format("%.2f %s", amount, currency);
            }
        }
        
        public enum Category { ELECTRONICS, BOOKS, CLOTHING, HOME, SPORTS }
        
        public static Product electronics(String id, String name, double price) {
            return new Product(id, name, new Price(price, "USD"), Category.ELECTRONICS);
        }
        
        public static Product book(String id, String name, double price) {
            return new Product(id, name, new Price(price, "USD"), Category.BOOKS);
        }
        
        public boolean isExpensive() {
            return price.amount() > 100.0;
        }
        
        public Product withDiscount(double percentage) {
            double newAmount = price.amount() * (1 - percentage / 100);
            return new Product(id, name, new Price(newAmount, price.currency()), category);
        }
    }
    
    public record Address(String street, String city, String state, String zipCode, String country) {
        public String getFormattedAddress() {
            return String.format("%s, %s, %s %s, %s", street, city, state, zipCode, country);
        }
    }
    
    // Shape records for pattern matching examples
    public record Circle(double radius) {}
    public record Rectangle(double width, double height) {}
    public record Triangle(double base, double height) {}
    public record Point(int x, int y) {}
    
    // Employee management records
    public record Employee(String id, String name, String title, Department department, 
                          double salary, LocalDate hireDate) {
        
        public double getYearsOfService() {
            return Period.between(hireDate, LocalDate.now()).toTotalMonths() / 12.0;
        }
        
        public boolean isBonusEligible() {
            return getYearsOfService() >= 1.0;
        }
        
        public String getSalaryFormatted() {
            return String.format("$%,.2f", salary);
        }
    }
    
    public enum Department { ENGINEERING, PRODUCT, SALES, MARKETING, HR }
    
    // Banking records
    public record BankAccount(String accountNumber, String ownerName, double balance, Type type) {
        public enum Type { CHECKING, SAVINGS, INVESTMENT }
        
        public BankAccount deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Deposit amount must be positive");
            return new BankAccount(accountNumber, ownerName, balance + amount, type);
        }
        
        public BankAccount withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Withdrawal amount must be positive");
            if (amount > balance) throw new IllegalArgumentException("Insufficient funds");
            return new BankAccount(accountNumber, ownerName, balance - amount, type);
        }
    }
    
    // Order system records
    public record Order(String id, Customer customer, List<Item> items, Status status, LocalDateTime createdAt) {
        
        public enum Status { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }
        
        public record Item(Product product, int quantity, double unitPrice) {
            public double getTotalPrice() {
                return quantity * unitPrice;
            }
        }
        
        public double getTotalAmount() {
            return items.stream().mapToDouble(Item::getTotalPrice).sum();
        }
        
        public Order updateStatus(Status newStatus) {
            return new Order(id, customer, items, newStatus, createdAt);
        }
    }
    
    // Shopping cart
    public record ShoppingCart(String id, String customerId, List<CartItem> items) {
        
        public record CartItem(Product product, int quantity) {
            public double getTotalPrice() {
                return quantity * product.price().amount();
            }
        }
        
        public ShoppingCart(String id, String customerId) {
            this(id, customerId, new ArrayList<>());
        }
        
        public ShoppingCart addItem(Product product, int quantity) {
            List<CartItem> newItems = new ArrayList<>(items);
            newItems.add(new CartItem(product, quantity));
            return new ShoppingCart(id, customerId, newItems);
        }
        
        public double getTotalAmount() {
            return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
        }
        
        public Order checkout(Customer customer, Address shippingAddress) {
            List<Order.Item> orderItems = items.stream()
                .map(item -> new Order.Item(item.product(), item.quantity(), item.product().price().amount()))
                .toList();
            
            return new Order("ORD" + System.currentTimeMillis(), customer, orderItems, 
                           Order.Status.PENDING, LocalDateTime.now());
        }
    }
    
    // Payment method records
    public record CreditCard(String number, String expiry, String cvv) {}
    public record PayPal(String email) {}
    public record BankTransfer(String accountNumber, String routingNumber) {}
    
    // Traditional class for performance comparison
    static class PointClass {
        private final int x, y;
        public PointClass(int x, int y) { this.x = x; this.y = y; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    // Inventory manager
    static class InventoryManager {
        public String generateInventoryReport() {
            List<Product> inventory = List.of(
                Product.electronics("LAPTOP001", "Gaming Laptop", 1299.99),
                Product.book("BOOK001", "Java Programming", 49.99),
                Product.electronics("PHONE001", "Smartphone", 799.99)
            );
            
            List<String> expensiveItems = inventory.stream()
                .filter(Product::isExpensive)
                .map(Product::name)
                .toList();
            
            return "Expensive items in inventory: " + expensiveItems;
        }
    }
}