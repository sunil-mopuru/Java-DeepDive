# Module 14.8: Java 16 Features

## 🎯 **Overview**

Java 16 (March 2021) standardized **Records** and **Pattern Matching for instanceof**, bringing these powerful features to production use. It also introduced **Sealed Classes** as a second preview and various JVM improvements for better performance and tooling.

---

## 🚀 **Key Features**

### **🔥 Major Features**
- **Records (Standard)**: Immutable data carriers are now production-ready
- **Pattern Matching for instanceof (Standard)**: Type testing with pattern variable binding
- **Sealed Classes (Second Preview)**: Enhanced control over inheritance hierarchies
- **Stream.toList()**: Convenient method to collect streams to lists

### **📈 Performance & JVM**
- **Vector API (Incubator)**: SIMD programming for better performance
- **Foreign Linker API (Incubator)**: Native code integration
- **ZGC Concurrent Thread Processing**: Improved garbage collection
- **Elastic Metaspace**: Better memory management

---

## 📚 **Detailed Feature Guide**

### **1. Records (Production Ready)**

Records are now a standard feature, providing a concise way to create immutable data carriers.

#### **Basic Record Usage**
```java
// Simple data record
public record Person(String name, int age, String email) {
    // Compact constructor for validation
    public Person {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // Normalize the name
        name = name.trim();
    }
    
    // Custom methods
    public boolean isAdult() {
        return age >= 18;
    }
    
    public String getDisplayName() {
        return name + " (" + age + ")";
    }
}

// Usage
Person person = new Person("John Doe", 30, "john@example.com");
System.out.println(person.name()); // John Doe
System.out.println(person.isAdult()); // true
```

#### **Advanced Record Features**
```java
// Record with static methods and nested types
public record Product(String id, String name, Price price, Category category) {
    
    // Nested record types
    public record Price(double amount, String currency) {
        public Price {
            if (amount < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (currency == null) {
                currency = "USD";
            }
        }
        
        public String formatted() {
            return String.format("%.2f %s", amount, currency);
        }
    }
    
    public enum Category {
        ELECTRONICS, CLOTHING, BOOKS, HOME, SPORTS
    }
    
    // Static factory methods
    public static Product electronics(String id, String name, double price) {
        return new Product(id, name, new Price(price, "USD"), Category.ELECTRONICS);
    }
    
    public static Product book(String id, String name, double price) {
        return new Product(id, name, new Price(price, "USD"), Category.BOOKS);
    }
    
    // Custom methods
    public boolean isExpensive() {
        return price.amount() > 100.0;
    }
    
    public Product withDiscount(double percentage) {
        double newAmount = price.amount() * (1 - percentage / 100);
        return new Product(id, name, new Price(newAmount, price.currency()), category);
    }
}
```

### **2. Pattern Matching for instanceof (Standard)**

Pattern matching with instanceof is now a standard feature, eliminating explicit casting.

```java
public class PatternMatchingExamples {
    
    // Basic pattern matching
    public String processObject(Object obj) {
        if (obj instanceof String str) {
            return "String of length " + str.length() + ": " + str.toUpperCase();
        } else if (obj instanceof Integer num) {
            return "Integer: " + num + " (squared: " + (num * num) + ")";
        } else if (obj instanceof List<?> list) {
            return "List with " + list.size() + " elements";
        } else if (obj instanceof Person person) {
            return "Person: " + person.getDisplayName() + 
                   (person.isAdult() ? " (adult)" : " (minor)");
        }
        return "Unknown type: " + obj.getClass().getSimpleName();
    }
    
    // Complex pattern matching with conditions
    public double calculateArea(Object shape) {
        return switch (shape) {
            case Circle circle && circle.radius() > 0 -> 
                Math.PI * circle.radius() * circle.radius();
            case Rectangle rect && rect.width() > 0 && rect.height() > 0 -> 
                rect.width() * rect.height();
            case Triangle tri && tri.base() > 0 && tri.height() > 0 -> 
                0.5 * tri.base() * tri.height();
            default -> 0.0;
        };
    }
    
    // Pattern matching in method chains
    public String analyzeData(Object data) {
        if (data instanceof Collection<?> collection && !collection.isEmpty()) {
            if (collection instanceof List<?> list) {
                return "List analysis: first=" + list.get(0) + ", last=" + list.get(list.size()-1);
            } else if (collection instanceof Set<?> set) {
                return "Set analysis: unique elements=" + set.size();
            }
            return "Collection with " + collection.size() + " elements";
        } else if (data instanceof String str && str.length() > 10) {
            return "Long string: " + str.substring(0, 10) + "...";
        } else if (data instanceof Number num && num.doubleValue() > 1000) {
            return "Large number: " + num;
        }
        return "No significant analysis available";
    }
}

// Supporting records for examples
record Circle(double radius) {}
record Rectangle(double width, double height) {}
record Triangle(double base, double height) {}
```

### **3. Stream.toList() Method**

Convenient method to collect streams directly to lists.

```java
public class StreamToListExamples {
    
    public void demonstrateToList() {
        // Before Java 16 (verbose)
        List<String> oldWay = Stream.of("apple", "banana", "cherry")
                                   .filter(s -> s.length() > 5)
                                   .collect(Collectors.toList());
        
        // Java 16 way (concise)
        List<String> newWay = Stream.of("apple", "banana", "cherry")
                                   .filter(s -> s.length() > 5)
                                   .toList();
        
        System.out.println("Filtered fruits: " + newWay);
        
        // Note: toList() returns an immutable list
        // This would throw UnsupportedOperationException:
        // newWay.add("grape");
        
        // Complex example
        List<Product> expensiveElectronics = products.stream()
                                                   .filter(p -> p.category() == Product.Category.ELECTRONICS)
                                                   .filter(Product::isExpensive)
                                                   .sorted((p1, p2) -> Double.compare(p2.price().amount(), p1.price().amount()))
                                                   .toList();
        
        System.out.println("Expensive electronics: " + expensiveElectronics);
    }
    
    // Practical usage examples
    public List<String> extractEmails(List<Person> people) {
        return people.stream()
                    .filter(Person::isAdult)
                    .map(Person::email)
                    .distinct()
                    .sorted()
                    .toList();
    }
    
    public List<Product> searchProducts(String query, Product.Category category, double maxPrice) {
        return products.stream()
                      .filter(p -> p.name().toLowerCase().contains(query.toLowerCase()))
                      .filter(p -> category == null || p.category() == category)
                      .filter(p -> p.price().amount() <= maxPrice)
                      .limit(20)
                      .toList();
    }
}
```

### **4. Practical Applications**

Real-world usage combining Java 16 features for robust applications.

#### **E-commerce Order System**
```java
public record Order(
    String orderId,
    Customer customer,
    List<OrderItem> items,
    OrderStatus status,
    LocalDateTime createdAt,
    ShippingAddress shippingAddress
) {
    // Validation in compact constructor
    public Order {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if (status == null) {
            status = OrderStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Business methods
    public double getTotalAmount() {
        return items.stream()
                   .mapToDouble(item -> item.quantity() * item.unitPrice())
                   .sum();
    }
    
    public List<OrderItem> getItemsByCategory(Product.Category category) {
        return items.stream()
                   .filter(item -> item.product().category() == category)
                   .toList();
    }
    
    public boolean hasExpensiveItems() {
        return items.stream()
                   .anyMatch(item -> item.product().isExpensive());
    }
    
    public Order updateStatus(OrderStatus newStatus) {
        return new Order(orderId, customer, items, newStatus, createdAt, shippingAddress);
    }
}

public record OrderItem(Product product, int quantity, double unitPrice) {
    public OrderItem {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
    }
    
    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}

public record Customer(String id, String name, String email, CustomerType type) {
    public enum CustomerType { REGULAR, PREMIUM, VIP }
    
    public double getDiscountRate() {
        return switch (type) {
            case REGULAR -> 0.0;
            case PREMIUM -> 0.05;
            case VIP -> 0.10;
        };
    }
}

public enum OrderStatus { PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED }

public record ShippingAddress(String street, String city, String state, String zipCode, String country) {}

// Order processing with pattern matching
public class OrderProcessor {
    
    public String processPayment(Object paymentMethod, double amount) {
        return switch (paymentMethod) {
            case CreditCard(var number, var expiry, var cvv) -> 
                processCreditCardPayment(number, amount);
            case PayPal(var email) -> 
                processPayPalPayment(email, amount);
            case BankTransfer(var accountNumber, var routingNumber) -> 
                processBankTransfer(accountNumber, routingNumber, amount);
            default -> "Unsupported payment method";
        };
    }
    
    public List<Order> findOrdersByStatus(List<Order> orders, OrderStatus status) {
        return orders.stream()
                    .filter(order -> order.status() == status)
                    .toList();
    }
    
    public Map<Product.Category, List<OrderItem>> groupItemsByCategory(Order order) {
        return order.items().stream()
                   .collect(Collectors.groupingBy(
                       item -> item.product().category(),
                       Collectors.toList()
                   ));
    }
}

// Payment method records
record CreditCard(String number, String expiry, String cvv) {}
record PayPal(String email) {}
record BankTransfer(String accountNumber, String routingNumber) {}
```

---

## ⚡ **Best Practices**

### **Records Guidelines**
- ✅ **Use for immutable data**: Perfect for DTOs, value objects, and data transfer
- ✅ **Add validation in compact constructors**: Ensure data integrity
- ✅ **Combine with other features**: Use with sealed classes and pattern matching
- ❌ **Don't use for mutable state**: Records are designed to be immutable

### **Pattern Matching Best Practices**
- ✅ **Combine with conditions**: Use `&&` for additional checks
- ✅ **Handle all cases**: Ensure exhaustive coverage in switch expressions
- ✅ **Keep patterns simple**: Complex logic should be in separate methods
- ❌ **Don't overuse**: Simple instanceof checks might be clearer

### **Stream.toList() Usage**
- ✅ **Use for immutable results**: When you don't need to modify the list
- ✅ **Prefer over collect(toList())**: More concise and intention-revealing
- ❌ **Don't use for mutable lists**: Use collect(Collectors.toCollection())

---

## 🧪 **Testing with Java 16 Features**

```java
@Test
public void testRecords() {
    Person person = new Person("John Doe", 25, "john@example.com");
    
    assertEquals("John Doe", person.name());
    assertEquals(25, person.age());
    assertTrue(person.isAdult());
    
    // Test validation
    assertThrows(IllegalArgumentException.class, () -> 
        new Person("", 25, "john@example.com"));
}

@Test
public void testPatternMatching() {
    PatternMatchingExamples examples = new PatternMatchingExamples();
    
    String result = examples.processObject("Hello World");
    assertTrue(result.contains("String of length 11"));
    
    double area = examples.calculateArea(new Circle(5.0));
    assertEquals(Math.PI * 25, area, 0.001);
}

@Test
public void testStreamToList() {
    List<Integer> numbers = Stream.of(1, 2, 3, 4, 5)
                                 .filter(n -> n % 2 == 0)
                                 .toList();
    
    assertEquals(List.of(2, 4), numbers);
    
    // Verify immutability
    assertThrows(UnsupportedOperationException.class, () -> 
        numbers.add(6));
}
```

---

## 🎯 **What You'll Learn**

After completing this module, you'll understand:
- **Records**: Building immutable data classes with validation and custom methods
- **Pattern Matching**: Type-safe instanceof checks with pattern variables
- **Stream Processing**: Converting streams to immutable lists efficiently
- **Modern Java Design**: Combining features for clean, maintainable code

---

## 📚 **Prerequisites**

- **Java 15 Features**: Sealed classes and text blocks
- **Java 14 Features**: Records preview and pattern matching basics
- **Stream API**: Understanding of stream operations and collectors
- **Object-Oriented Programming**: Classes, inheritance, and polymorphism

---

**🚀 Ready to build robust data-driven applications? Let's master Java 16's production-ready features!**

**Next:** [Java 17 LTS Features](../14.9-Java17/README.md) | **Previous:** [Java 15 Features](../14.7-Java15/README.md)