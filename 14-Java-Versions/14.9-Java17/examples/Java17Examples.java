/**
 * Java 17 Features Examples - LTS Release
 * 
 * Demonstrates Java 17 LTS features including Sealed Classes (Standard), 
 * Pattern Matching enhancements, Switch Expressions improvements, and new APIs.
 */

import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.util.random.*;

public class Java17Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 17 LTS Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateSealedClasses();
        showPatternMatching();
        
        // INTERMEDIATE LEVEL
        exploreNewAPIs();
        showSwitchEnhancements();
        
        // ADVANCED LEVEL
        buildComplexExamples();
        performanceImprovements();
    }
    
    public static void demonstrateSealedClasses() {
        System.out.println("\n1. Sealed Classes (Standard in Java 17)");
        System.out.println("========================================");
        
        // Basic sealed class usage
        List<Vehicle> fleet = List.of(
            new Car("Toyota Camry", 4, "Sedan"),
            new Truck("Ford F-150", 2000, "Pickup"),
            new Motorcycle("Harley Davidson", 1200, "Cruiser")
        );
        
        System.out.println("Fleet information:");
        fleet.forEach(vehicle -> {
            String info = switch (vehicle) {
                case Car car -> String.format("Car: %s (%d doors, %s type)", 
                                            car.model(), car.doors(), car.type());
                case Truck truck -> String.format("Truck: %s (%d kg capacity, %s type)", 
                                                 truck.model(), truck.capacity(), truck.type());
                case Motorcycle bike -> String.format("Motorcycle: %s (%d cc, %s type)", 
                                                     bike.model(), bike.engineSize(), bike.type());
            };
            System.out.println(info);
        });
        
        // Complex hierarchy example
        processPayments();
    }
    
    public static void processPayments() {
        System.out.println("\nPayment processing with sealed classes:");
        
        List<PaymentMethod> methods = List.of(
            new CreditCard("1234-****-****-5678", "12/25", "Visa"),
            new DebitCard("9876-****-****-4321", "1234", "Mastercard"),
            new DigitalWallet("user@example.com", "PayPal", 500.00),
            new BankTransfer("BANK123", "ACC456789", "Swift Transfer")
        );
        
        methods.forEach(method -> {
            String status = processPayment(method, 100.0);
            System.out.println(status);
        });
    }
    
    public static String processPayment(PaymentMethod method, double amount) {
        return switch (method) {
            case CreditCard cc -> {
                boolean valid = validateCreditCard(cc);
                yield valid ? 
                    String.format("✓ Credit card payment processed: $%.2f (%s)", amount, cc.brand()) :
                    "✗ Credit card validation failed";
            }
            case DebitCard dc -> {
                boolean hasBalance = checkBalance(dc, amount);
                yield hasBalance ?
                    String.format("✓ Debit card payment processed: $%.2f (%s)", amount, dc.brand()) :
                    "✗ Insufficient balance";
            }
            case DigitalWallet dw -> {
                boolean sufficient = dw.balance() >= amount;
                yield sufficient ?
                    String.format("✓ Digital wallet payment: $%.2f (%s)", amount, dw.provider()) :
                    "✗ Insufficient wallet balance";
            }
            case BankTransfer bt -> 
                String.format("✓ Bank transfer initiated: $%.2f (Bank: %s)", amount, bt.bankCode());
        };
    }
    
    public static void showPatternMatching() {
        System.out.println("\n2. Enhanced Pattern Matching");
        System.out.println("=============================");
        
        List<Object> data = List.of(
            "Java 17 LTS",
            42,
            List.of("sealed", "classes", "pattern", "matching"),
            Map.of("version", 17, "lts", true),
            Optional.of("Present value"),
            Optional.empty()
        );
        
        System.out.println("Processing mixed data types:");
        data.forEach(Java17Examples::processData);
    }
    
    public static void processData(Object obj) {
        String result = switch (obj) {
            case String str when str.length() > 10 -> 
                "Long string: " + str + " (" + str.length() + " chars)";
            case String str -> 
                "Short string: " + str;
            case Integer num when num > 100 -> 
                "Large number: " + num;
            case Integer num -> 
                "Small number: " + num + " (even: " + (num % 2 == 0) + ")";
            case List<?> list when list.size() > 3 -> 
                "Large list with " + list.size() + " items: " + list;
            case List<?> list -> 
                "Small list: " + list;
            case Map<?, ?> map -> 
                "Map with " + map.size() + " entries: " + map;
            case Optional<?> opt when opt.isPresent() -> 
                "Present optional: " + opt.get();
            case Optional<?> opt -> 
                "Empty optional";
            default -> 
                "Unknown type: " + obj.getClass().getSimpleName();
        };
        System.out.println(result);
    }
    
    public static void exploreNewAPIs() {
        System.out.println("\n3. New Java 17 APIs");
        System.out.println("====================");
        
        // Random generators
        demonstrateRandomGenerators();
        
        // Stream enhancements
        demonstrateStreamEnhancements();
        
        // Time API improvements
        demonstrateTimeAPI();
    }
    
    public static void demonstrateRandomGenerators() {
        System.out.println("Enhanced Random generators:");
        
        // Different random generators
        RandomGenerator defaultRng = RandomGenerator.getDefault();
        RandomGenerator secureRng = RandomGenerator.of("SecureRandom");
        RandomGenerator fastRng = RandomGenerator.of("Xoshiro256PlusPlus");
        
        System.out.println("Default RNG: " + Arrays.toString(
            defaultRng.ints(5, 1, 100).toArray()));
        
        System.out.println("Fast RNG doubles: " + Arrays.toString(
            fastRng.doubles(5, 0.0, 1.0).toArray()));
        
        // Stream of random values
        List<String> randomWords = defaultRng.ints(10, 65, 91)
                                           .mapToObj(i -> String.valueOf((char) i))
                                           .collect(Collectors.joining());
        System.out.println("Random word: " + randomWords);
    }
    
    public static void demonstrateStreamEnhancements() {
        System.out.println("\nStream enhancements:");
        
        List<Integer> numbers = IntStream.rangeClosed(1, 20).boxed().toList();
        
        // Enhanced stream operations
        List<Integer> evenSquares = numbers.stream()
                                          .filter(n -> n % 2 == 0)
                                          .map(n -> n * n)
                                          .toList();
        System.out.println("Even squares: " + evenSquares);
        
        // Parallel processing improvements
        long sum = numbers.parallelStream()
                         .mapToLong(n -> n * n)
                         .sum();
        System.out.println("Sum of squares: " + sum);
    }
    
    public static void demonstrateTimeAPI() {
        System.out.println("\nTime API improvements:");
        
        Instant now = Instant.now();
        System.out.println("Current time: " + now);
        
        // Duration formatting improvements
        Duration oneHour = Duration.ofHours(1);
        Duration complexDuration = Duration.ofDays(2).plusHours(3).plusMinutes(45);
        
        System.out.println("One hour: " + oneHour);
        System.out.println("Complex duration: " + complexDuration);
        
        // Zone handling
        ZonedDateTime zonedTime = ZonedDateTime.now(ZoneId.of("America/New_York"));
        System.out.println("NY time: " + zonedTime.format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")));
    }
    
    public static void showSwitchEnhancements() {
        System.out.println("\n4. Switch Expression Enhancements");
        System.out.println("===================================");
        
        // Advanced switch with sealed classes
        List<Shape> shapes = List.of(
            new Circle(5.0),
            new Rectangle(4.0, 6.0),
            new Triangle(3.0, 4.0, 5.0)
        );
        
        System.out.println("Shape calculations:");
        shapes.forEach(shape -> {
            double area = calculateArea(shape);
            double perimeter = calculatePerimeter(shape);
            String type = getShapeType(shape);
            
            System.out.printf("%s - Area: %.2f, Perimeter: %.2f%n", 
                            type, area, perimeter);
        });
        
        // Day type classification
        demonstrateDayClassification();
    }
    
    public static double calculateArea(Shape shape) {
        return switch (shape) {
            case Circle(var radius) -> Math.PI * radius * radius;
            case Rectangle(var width, var height) -> width * height;
            case Triangle(var a, var b, var c) -> {
                double s = (a + b + c) / 2;
                yield Math.sqrt(s * (s - a) * (s - b) * (s - c));
            }
        };
    }
    
    public static double calculatePerimeter(Shape shape) {
        return switch (shape) {
            case Circle(var radius) -> 2 * Math.PI * radius;
            case Rectangle(var width, var height) -> 2 * (width + height);
            case Triangle(var a, var b, var c) -> a + b + c;
        };
    }
    
    public static String getShapeType(Shape shape) {
        return switch (shape) {
            case Circle(var radius) when radius < 3 -> "Small Circle";
            case Circle(var radius) when radius >= 10 -> "Large Circle";
            case Circle(var radius) -> "Medium Circle";
            case Rectangle(var w, var h) when w == h -> "Square";
            case Rectangle(var w, var h) when w > 2 * h || h > 2 * w -> "Long Rectangle";
            case Rectangle(var w, var h) -> "Regular Rectangle";
            case Triangle(var a, var b, var c) when a == b && b == c -> "Equilateral Triangle";
            case Triangle(var a, var b, var c) when a == b || b == c || a == c -> "Isosceles Triangle";
            case Triangle(var a, var b, var c) -> "Scalene Triangle";
        };
    }
    
    public static void demonstrateDayClassification() {
        System.out.println("\nDay classification:");
        
        for (DayOfWeek day : DayOfWeek.values()) {
            String classification = switch (day) {
                case MONDAY -> "Start of work week";
                case TUESDAY, WEDNESDAY, THURSDAY -> "Mid-week";
                case FRIDAY -> "End of work week";
                case SATURDAY, SUNDAY -> "Weekend";
            };
            
            String mood = switch (day) {
                case MONDAY -> "😴 Monday Blues";
                case TUESDAY -> "😐 Getting Started";
                case WEDNESDAY -> "😊 Hump Day";
                case THURSDAY -> "😄 Almost There";
                case FRIDAY -> "🎉 TGIF!";
                case SATURDAY -> "🏖️ Relaxation";
                case SUNDAY -> "😌 Rest Day";
            };
            
            System.out.printf("%s: %s - %s%n", day, classification, mood);
        }
    }
    
    public static void buildComplexExamples() {
        System.out.println("\n5. Complex Real-World Examples");
        System.out.println("==============================");
        
        // Order processing system
        OrderProcessor processor = new OrderProcessor();
        processor.demonstrateOrderProcessing();
        
        // State machine
        StateMachineDemo.demonstrateStateMachine();
        
        // Event processing
        EventProcessor.demonstrateEventProcessing();
    }
    
    public static void performanceImprovements() {
        System.out.println("\n6. Performance Improvements");
        System.out.println("============================");
        
        performSealedClassBenchmark();
        performPatternMatchingBenchmark();
    }
    
    public static void performSealedClassBenchmark() {
        System.out.println("Sealed class performance:");
        
        List<Vehicle> vehicles = IntStream.range(0, 100_000)
            .mapToObj(i -> switch (i % 3) {
                case 0 -> new Car("Model" + i, 4, "Sedan");
                case 1 -> new Truck("Truck" + i, 2000, "Heavy");
                default -> new Motorcycle("Bike" + i, 1000, "Sport");
            })
            .toList();
        
        long startTime = System.nanoTime();
        long fuelConsumption = vehicles.stream()
            .mapToLong(vehicle -> switch (vehicle) {
                case Car car -> 8; // L/100km
                case Truck truck -> 15; // L/100km
                case Motorcycle bike -> 5; // L/100km
            })
            .sum();
        long endTime = System.nanoTime();
        
        System.out.printf("Processed %d vehicles in %.2f ms%n", 
                         vehicles.size(), (endTime - startTime) / 1_000_000.0);
        System.out.printf("Total estimated fuel consumption: %d L/100km%n", fuelConsumption);
    }
    
    public static void performPatternMatchingBenchmark() {
        System.out.println("\nPattern matching performance:");
        
        List<Object> mixedData = IntStream.range(0, 50_000)
            .mapToObj(i -> switch (i % 4) {
                case 0 -> "String" + i;
                case 1 -> i;
                case 2 -> List.of(i, i + 1, i + 2);
                default -> Map.of("key", i);
            })
            .toList();
        
        long startTime = System.nanoTime();
        long result = mixedData.stream()
            .mapToLong(obj -> switch (obj) {
                case String str -> str.length();
                case Integer num -> num;
                case List<?> list -> list.size();
                case Map<?, ?> map -> map.size();
                default -> 0;
            })
            .sum();
        long endTime = System.nanoTime();
        
        System.out.printf("Pattern matched %d objects in %.2f ms%n", 
                         mixedData.size(), (endTime - startTime) / 1_000_000.0);
        System.out.printf("Total result: %d%n", result);
    }
    
    // Helper methods
    private static boolean validateCreditCard(CreditCard cc) {
        return cc.number().length() >= 16 && !cc.expiry().equals("00/00");
    }
    
    private static boolean checkBalance(DebitCard dc, double amount) {
        return true; // Simplified for demo
    }
    
    // Sealed class definitions
    public sealed interface Vehicle permits Car, Truck, Motorcycle {}
    
    public record Car(String model, int doors, String type) implements Vehicle {}
    public record Truck(String model, int capacity, String type) implements Vehicle {}
    public record Motorcycle(String model, int engineSize, String type) implements Vehicle {}
    
    public sealed interface PaymentMethod permits CreditCard, DebitCard, DigitalWallet, BankTransfer {}
    
    public record CreditCard(String number, String expiry, String brand) implements PaymentMethod {}
    public record DebitCard(String number, String pin, String brand) implements PaymentMethod {}
    public record DigitalWallet(String email, String provider, double balance) implements PaymentMethod {}
    public record BankTransfer(String bankCode, String accountNumber, String type) implements PaymentMethod {}
    
    public sealed interface Shape permits Circle, Rectangle, Triangle {}
    
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(double a, double b, double c) implements Shape {}
    
    // Supporting classes
    static class OrderProcessor {
        public void demonstrateOrderProcessing() {
            System.out.println("Order processing with sealed classes:");
            
            List<Order> orders = List.of(
                new StandardOrder("ORD001", 99.99, "Standard"),
                new ExpressOrder("ORD002", 149.99, "Express", Duration.ofHours(2)),
                new SubscriptionOrder("ORD003", 29.99, "Monthly", 12)
            );
            
            orders.forEach(order -> {
                String processing = switch (order) {
                    case StandardOrder std -> 
                        String.format("Processing standard order %s: $%.2f", std.id(), std.amount());
                    case ExpressOrder exp -> 
                        String.format("Rush processing order %s: $%.2f (delivery in %s)", 
                                    exp.id(), exp.amount(), exp.deliveryTime());
                    case SubscriptionOrder sub -> 
                        String.format("Setting up subscription %s: $%.2f x %d months", 
                                    sub.id(), sub.amount(), sub.duration());
                };
                System.out.println(processing);
            });
        }
    }
    
    sealed interface Order permits StandardOrder, ExpressOrder, SubscriptionOrder {
        String id();
        double amount();
    }
    
    record StandardOrder(String id, double amount, String type) implements Order {}
    record ExpressOrder(String id, double amount, String type, Duration deliveryTime) implements Order {}
    record SubscriptionOrder(String id, double amount, String type, int duration) implements Order {}
    
    static class StateMachineDemo {
        public static void demonstrateStateMachine() {
            System.out.println("\nState machine with sealed classes:");
            
            ConnectionState state = new Disconnected();
            System.out.println("Initial: " + describeState(state));
            
            state = state.connect();
            System.out.println("After connect: " + describeState(state));
            
            state = state.authenticate("user123");
            System.out.println("After auth: " + describeState(state));
            
            state = state.disconnect();
            System.out.println("After disconnect: " + describeState(state));
        }
        
        private static String describeState(ConnectionState state) {
            return switch (state) {
                case Disconnected() -> "Not connected";
                case Connected(var timestamp) -> "Connected at " + timestamp;
                case Authenticated(var user, var timestamp) -> 
                    String.format("Authenticated as %s at %s", user, timestamp);
            };
        }
    }
    
    sealed interface ConnectionState permits Disconnected, Connected, Authenticated {
        default ConnectionState connect() {
            return switch (this) {
                case Disconnected() -> new Connected(Instant.now());
                case Connected(var time), Authenticated(var user, var time2) -> this;
            };
        }
        
        default ConnectionState authenticate(String user) {
            return switch (this) {
                case Connected(var time) -> new Authenticated(user, Instant.now());
                case Disconnected(), Authenticated(var u, var t) -> this;
            };
        }
        
        default ConnectionState disconnect() {
            return new Disconnected();
        }
    }
    
    record Disconnected() implements ConnectionState {}
    record Connected(Instant timestamp) implements ConnectionState {}
    record Authenticated(String user, Instant timestamp) implements ConnectionState {}
    
    static class EventProcessor {
        public static void demonstrateEventProcessing() {
            System.out.println("\nEvent processing:");
            
            List<Event> events = List.of(
                new UserEvent("user.login", "user123", Instant.now(), Map.of("ip", "192.168.1.1")),
                new SystemEvent("system.startup", "server01", Instant.now(), Map.of("version", "1.0")),
                new ErrorEvent("error.database", "db01", Instant.now(), Map.of("error", "timeout"))
            );
            
            events.forEach(event -> {
                String processing = switch (event) {
                    case UserEvent(var type, var userId, var time, var data) ->
                        String.format("User event: %s by %s at %s", type, userId, time);
                    case SystemEvent(var type, var component, var time, var data) ->
                        String.format("System event: %s from %s at %s", type, component, time);
                    case ErrorEvent(var type, var source, var time, var data) ->
                        String.format("Error event: %s from %s at %s - %s", type, source, time, data.get("error"));
                };
                System.out.println(processing);
            });
        }
    }
    
    sealed interface Event permits UserEvent, SystemEvent, ErrorEvent {}
    
    record UserEvent(String type, String userId, Instant timestamp, Map<String, Object> data) implements Event {}
    record SystemEvent(String type, String component, Instant timestamp, Map<String, Object> data) implements Event {}
    record ErrorEvent(String type, String source, Instant timestamp, Map<String, Object> data) implements Event {}
}