/**
 * Java 12 Features Examples - Beginner to Advanced
 * 
 * Demonstrates Java 12 preview and standard features including Switch Expressions,
 * enhanced String methods (indent, transform), Collectors.teeing(), and JVM improvements.
 */

import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.time.*;

public class Java12Examples {
    
    public static void main(String[] args) {
        System.out.println("=== Java 12 Features Examples ===");
        
        // BEGINNER LEVEL
        demonstrateSwitchExpressions();
        showStringMethods();
        
        // INTERMEDIATE LEVEL
        exploreTeeingCollector();
        showPracticalSwitchUsage();
        
        // ADVANCED LEVEL
        buildComplexExamples();
        performanceComparisons();
    }
    
    public static void demonstrateSwitchExpressions() {
        System.out.println("\n1. Switch Expressions (Preview)");
        System.out.println("===============================");
        
        // Basic switch expression with arrow syntax
        DayOfWeek today = DayOfWeek.MONDAY;
        
        String dayType = switch (today) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
            case SATURDAY, SUNDAY -> "Weekend";
        };
        
        System.out.println("Today (" + today + ") is a: " + dayType);
        
        // Switch with multiple values
        Month currentMonth = Month.JUNE;
        int quarter = switch (currentMonth) {
            case JANUARY, FEBRUARY, MARCH -> 1;
            case APRIL, MAY, JUNE -> 2;
            case JULY, AUGUST, SEPTEMBER -> 3;
            case OCTOBER, NOVEMBER, DECEMBER -> 4;
        };
        
        System.out.println("Month " + currentMonth + " is in quarter: " + quarter);
        
        // Switch expression with block syntax
        String weather = "SUNNY";
        String recommendation = switch (weather) {
            case "SUNNY" -> {
                System.out.println("Processing sunny weather...");
                yield "Wear sunglasses and light clothes";
            }
            case "RAINY" -> {
                System.out.println("Processing rainy weather...");
                yield "Take an umbrella and wear waterproof jacket";
            }
            case "SNOWY" -> {
                System.out.println("Processing snowy weather...");
                yield "Dress warmly and wear boots";
            }
            default -> "Check weather forecast";
        };
        
        System.out.println("Weather recommendation: " + recommendation);
        
        // Switch expressions for calculations
        demonstrateCalculatorSwitch();
    }
    
    public static void demonstrateCalculatorSwitch() {
        System.out.println("\nCalculator using switch expressions:");
        
        double result1 = calculate(10, 5, "+");
        double result2 = calculate(10, 5, "*");
        double result3 = calculate(10, 5, "/");
        
        System.out.println("10 + 5 = " + result1);
        System.out.println("10 * 5 = " + result2);
        System.out.println("10 / 5 = " + result3);
    }
    
    public static double calculate(double a, double b, String operator) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    System.err.println("Division by zero!");
                    yield Double.NaN;
                }
                yield a / b;
            }
            case "%" -> a % b;
            case "^" -> Math.pow(a, b);
            default -> {
                System.err.println("Unknown operator: " + operator);
                yield Double.NaN;
            }
        };
    }
    
    public static void showStringMethods() {
        System.out.println("\n2. Enhanced String Methods");
        System.out.println("==========================");
        
        // String.indent() method
        String codeBlock = "public class Hello {\n" +
                          "public static void main(String[] args) {\n" +
                          "System.out.println(\"Hello World!\");\n" +
                          "}\n" +
                          "}";
        
        System.out.println("Original code:");
        System.out.println(codeBlock);
        
        System.out.println("\nIndented by 4 spaces:");
        String indentedCode = codeBlock.indent(4);
        System.out.println(indentedCode);
        
        System.out.println("Indented by 8 spaces:");
        String moreIndented = codeBlock.indent(8);
        System.out.println(moreIndented);
        
        // Negative indent (dedent)
        String preIndented = "    Line 1\n    Line 2\n    Line 3";
        System.out.println("\nPre-indented text:");
        System.out.println("'" + preIndented + "'");
        
        String dedented = preIndented.indent(-2);
        System.out.println("\nAfter dedenting by 2:");
        System.out.println("'" + dedented + "'");
        
        // String.transform() method
        demonstrateTransform();
    }
    
    public static void demonstrateTransform() {
        System.out.println("\nString.transform() examples:");
        
        String name = "john doe smith";
        
        // Chain of transformations
        String processedName = name
            .transform(String::toUpperCase)
            .transform(s -> s.replace(" ", "_"))
            .transform(s -> "USER_" + s);
        
        System.out.println("Original: " + name);
        System.out.println("Transformed: " + processedName);
        
        // Custom transformation functions
        Function<String, String> addTimestamp = s -> 
            "[" + LocalDateTime.now().toString() + "] " + s;
            
        Function<String, String> addPrefix = s -> "LOG: " + s;
        
        String message = "Application started successfully";
        String logEntry = message
            .transform(addPrefix)
            .transform(addTimestamp);
        
        System.out.println("\nLog entry: " + logEntry);
        
        // Practical example: Data sanitization
        String userInput = "  Hello, World! @#$%^&*()  ";
        String sanitized = userInput
            .transform(String::trim)
            .transform(s -> s.replaceAll("[^a-zA-Z0-9\\s,!.]", ""))
            .transform(String::toLowerCase)
            .transform(s -> s.substring(0, Math.min(s.length(), 50)));
        
        System.out.println("User input: '" + userInput + "'");
        System.out.println("Sanitized: '" + sanitized + "'");
    }
    
    public static void exploreTeeingCollector() {
        System.out.println("\n3. Collectors.teeing()");
        System.out.println("======================");
        
        List<Student> students = createStudentList();
        
        // Basic teeing example: average and count
        var avgAndCount = students.stream()
            .collect(Collectors.teeing(
                Collectors.averagingDouble(Student::getGrade),
                Collectors.counting(),
                (avg, count) -> new GradeStatistics(avg, count)
            ));
        
        System.out.println("Grade Statistics:");
        System.out.printf("Average: %.2f, Count: %d%n", 
                         avgAndCount.getAverage(), avgAndCount.getCount());
        
        // More complex teeing: min/max with summary
        var minMaxSummary = students.stream()
            .collect(Collectors.teeing(
                Collectors.minBy(Comparator.comparing(Student::getGrade)),
                Collectors.maxBy(Comparator.comparing(Student::getGrade)),
                (min, max) -> new MinMaxResult(
                    min.orElse(null), 
                    max.orElse(null)
                )
            ));
        
        System.out.println("\nMin/Max Results:");
        if (minMaxSummary.getMin() != null) {
            System.out.println("Lowest grade: " + minMaxSummary.getMin().getName() + 
                              " (" + minMaxSummary.getMin().getGrade() + ")");
        }
        if (minMaxSummary.getMax() != null) {
            System.out.println("Highest grade: " + minMaxSummary.getMax().getName() + 
                              " (" + minMaxSummary.getMax().getGrade() + ")");
        }
        
        // Triple statistics using nested teeing
        demonstrateNestedTeeing(students);
    }
    
    public static void demonstrateNestedTeeing(List<Student> students) {
        System.out.println("\nNested teeing for comprehensive statistics:");
        
        var comprehensiveStats = students.stream()
            .collect(Collectors.teeing(
                // First collector: basic stats
                Collectors.teeing(
                    Collectors.averagingDouble(Student::getGrade),
                    Collectors.counting(),
                    BasicStats::new
                ),
                
                // Second collector: grade distribution
                Collectors.groupingBy(
                    student -> getLetterGrade(student.getGrade()),
                    Collectors.counting()
                ),
                
                // Combiner: create comprehensive result
                ComprehensiveStatistics::new
            ));
        
        System.out.println("Comprehensive Statistics:");
        System.out.printf("Average: %.2f, Total Students: %d%n",
                         comprehensiveStats.getBasicStats().getAverage(),
                         comprehensiveStats.getBasicStats().getCount());
        
        System.out.println("Grade Distribution:");
        comprehensiveStats.getGradeDistribution().forEach((grade, count) ->
            System.out.println("  " + grade + ": " + count + " students"));
    }
    
    public static void showPracticalSwitchUsage() {
        System.out.println("\n4. Practical Switch Expression Usage");
        System.out.println("====================================");
        
        // HTTP response status handler
        int statusCode = 404;
        String responseMessage = switch (statusCode) {
            case 200 -> "OK - Request successful";
            case 201 -> "Created - Resource created successfully";
            case 400 -> "Bad Request - Invalid request format";
            case 401 -> "Unauthorized - Authentication required";
            case 403 -> "Forbidden - Access denied";
            case 404 -> "Not Found - Resource not found";
            case 500 -> "Internal Server Error - Server error occurred";
            default -> "Unknown status code: " + statusCode;
        };
        
        System.out.println("Status " + statusCode + ": " + responseMessage);
        
        // File type processor
        processFilesByType();
        
        // Order status processor
        processOrderStatuses();
    }
    
    public static void processFilesByType() {
        System.out.println("\nFile type processing:");
        
        List<String> files = Arrays.asList(
            "document.pdf", "image.jpg", "data.csv", 
            "script.js", "style.css", "unknown.xyz"
        );
        
        files.forEach(file -> {
            String extension = getFileExtension(file);
            String processor = switch (extension.toLowerCase()) {
                case "pdf" -> "PDF Reader";
                case "jpg", "jpeg", "png", "gif" -> "Image Viewer";
                case "csv", "xlsx" -> "Spreadsheet Application";
                case "js", "ts" -> "JavaScript Engine";
                case "css" -> "Style Processor";
                case "html", "htm" -> "Web Browser";
                default -> "Default Application";
            };
            
            System.out.println(file + " -> " + processor);
        });
    }
    
    public static void processOrderStatuses() {
        System.out.println("\nOrder status processing:");
        
        List<Order> orders = Arrays.asList(
            new Order("ORD001", OrderStatus.PENDING),
            new Order("ORD002", OrderStatus.PROCESSING),
            new Order("ORD003", OrderStatus.SHIPPED),
            new Order("ORD004", OrderStatus.DELIVERED),
            new Order("ORD005", OrderStatus.CANCELLED)
        );
        
        orders.forEach(order -> {
            String action = switch (order.getStatus()) {
                case PENDING -> "Send confirmation email";
                case PROCESSING -> "Update inventory and prepare shipment";
                case SHIPPED -> "Send tracking information";
                case DELIVERED -> "Request feedback and process payment";
                case CANCELLED -> "Process refund and update inventory";
            };
            
            System.out.println("Order " + order.getId() + " (" + order.getStatus() + "): " + action);
        });
    }
    
    public static void buildComplexExamples() {
        System.out.println("\n5. Complex Real-World Examples");
        System.out.println("==============================");
        
        // Configuration file generator
        generateConfigurationFile();
        
        // Data transformation pipeline
        buildTransformationPipeline();
        
        // Report generator with statistics
        generateAnalyticsReport();
    }
    
    public static void generateConfigurationFile() {
        System.out.println("\nConfiguration file generation:");
        
        Map<String, Object> config = Map.of(
            "app.name", "MyApplication",
            "app.version", "1.2.0",
            "server.port", 8080,
            "database.url", "jdbc:mysql://localhost:3306/mydb",
            "logging.level", "INFO"
        );
        
        String configContent = config.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("\n"))
            .transform(content -> "# Application Configuration\n" + content)
            .transform(content -> content.indent(0)); // Normalize line endings
        
        System.out.println("Generated configuration:");
        System.out.println(configContent);
    }
    
    public static void buildTransformationPipeline() {
        System.out.println("\nData transformation pipeline:");
        
        List<String> rawData = Arrays.asList(
            "  Alice Johnson  ", "  bob smith ", "CHARLIE BROWN",
            "", "  diana PRINCE", "   ", "eve adams  "
        );
        
        // Processing pipeline using transform and switch
        List<String> processedData = rawData.stream()
            .filter(s -> !s.isBlank())
            .map(s -> s.transform(String::strip))
            .map(s -> s.transform(String::toLowerCase))
            .map(s -> s.transform(this::capitalizeWords))
            .collect(Collectors.toList());
        
        System.out.println("Raw data: " + rawData);
        System.out.println("Processed data: " + processedData);
    }
    
    public String capitalizeWords(String input) {
        return Arrays.stream(input.split(" "))
            .map(word -> word.isEmpty() ? word : 
                 Character.toUpperCase(word.charAt(0)) + 
                 word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));
    }
    
    public static void generateAnalyticsReport() {
        System.out.println("\nAnalytics report generation:");
        
        List<Sale> sales = createSalesList();
        
        var salesAnalysis = sales.stream()
            .collect(Collectors.teeing(
                // Revenue statistics
                Collectors.teeing(
                    Collectors.summingDouble(Sale::getAmount),
                    Collectors.averagingDouble(Sale::getAmount),
                    RevenueStats::new
                ),
                
                // Regional breakdown
                Collectors.groupingBy(
                    Sale::getRegion,
                    Collectors.summingDouble(Sale::getAmount)
                ),
                
                SalesAnalysis::new
            ));
        
        System.out.println("Sales Analysis Report:");
        System.out.printf("Total Revenue: $%.2f%n", salesAnalysis.getRevenueStats().getTotal());
        System.out.printf("Average Sale: $%.2f%n", salesAnalysis.getRevenueStats().getAverage());
        
        System.out.println("\nRevenue by Region:");
        salesAnalysis.getRegionalRevenue().entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> System.out.printf("  %s: $%.2f%n", 
                                               entry.getKey(), entry.getValue()));
    }
    
    public static void performanceComparisons() {
        System.out.println("\n6. Performance Comparisons");
        System.out.println("==========================");
        
        // Switch expression vs if-else performance
        compareSwitchPerformance();
        
        // String method performance
        compareStringMethods();
        
        // Teeing vs multiple passes
        compareTeeingPerformance();
    }
    
    public static void compareSwitchPerformance() {
        System.out.println("\nSwitch expression vs if-else performance:");
        
        List<DayOfWeek> days = Arrays.asList(DayOfWeek.values());
        int iterations = 1_000_000;
        
        // Switch expression timing
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DayOfWeek day = days.get(i % days.size());
            String result = switch (day) {
                case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Weekday";
                case SATURDAY, SUNDAY -> "Weekend";
            };
        }
        long switchTime = System.nanoTime() - startTime;
        
        // If-else timing
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            DayOfWeek day = days.get(i % days.size());
            String result;
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                result = "Weekend";
            } else {
                result = "Weekday";
            }
        }
        long ifElseTime = System.nanoTime() - startTime;
        
        System.out.printf("Switch expression: %.2f ms%n", switchTime / 1_000_000.0);
        System.out.printf("If-else chain: %.2f ms%n", ifElseTime / 1_000_000.0);
        System.out.printf("Performance ratio: %.2fx%n", (double) ifElseTime / switchTime);
    }
    
    public static void compareStringMethods() {
        System.out.println("\nString method performance comparison:");
        
        String text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
        int iterations = 100_000;
        
        // Using indent()
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String result = text.indent(4);
        }
        long indentTime = System.nanoTime() - startTime;
        
        // Manual indentation
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String result = text.replaceAll("(?m)^", "    ");
        }
        long manualTime = System.nanoTime() - startTime;
        
        System.out.printf("String.indent(): %.2f ms%n", indentTime / 1_000_000.0);
        System.out.printf("Manual indent: %.2f ms%n", manualTime / 1_000_000.0);
    }
    
    public static void compareTeeingPerformance() {
        System.out.println("\nTeeing vs multiple passes performance:");
        
        List<Integer> numbers = IntStream.rangeClosed(1, 1_000_000)
                                       .boxed()
                                       .collect(Collectors.toList());
        
        // Using teeing (single pass)
        long startTime = System.nanoTime();
        var teeingResult = numbers.stream()
            .collect(Collectors.teeing(
                Collectors.averagingDouble(Integer::doubleValue),
                Collectors.counting(),
                (avg, count) -> new Object[] {avg, count}
            ));
        long teeingTime = System.nanoTime() - startTime;
        
        // Multiple passes
        startTime = System.nanoTime();
        double avg = numbers.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        long count = numbers.stream().count();
        Object[] multiPassResult = {avg, count};
        long multiPassTime = System.nanoTime() - startTime;
        
        System.out.printf("Teeing (single pass): %.2f ms%n", teeingTime / 1_000_000.0);
        System.out.printf("Multiple passes: %.2f ms%n", multiPassTime / 1_000_000.0);
    }
    
    // Helper methods and classes
    private static String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot + 1);
    }
    
    private static String getLetterGrade(double numericGrade) {
        return switch ((int) numericGrade / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            default -> "F";
        };
    }
    
    private static List<Student> createStudentList() {
        return Arrays.asList(
            new Student("Alice", 92.5),
            new Student("Bob", 87.0),
            new Student("Charlie", 78.5),
            new Student("Diana", 95.0),
            new Student("Eve", 83.5),
            new Student("Frank", 72.0)
        );
    }
    
    private static List<Sale> createSalesList() {
        return Arrays.asList(
            new Sale("North", 1250.00),
            new Sale("South", 980.50),
            new Sale("East", 1100.75),
            new Sale("West", 1350.25),
            new Sale("North", 890.00),
            new Sale("South", 1200.00)
        );
    }
    
    // Supporting classes
    static class Student {
        private String name;
        private double grade;
        
        public Student(String name, double grade) {
            this.name = name;
            this.grade = grade;
        }
        
        public String getName() { return name; }
        public double getGrade() { return grade; }
    }
    
    static class GradeStatistics {
        private double average;
        private long count;
        
        public GradeStatistics(double average, long count) {
            this.average = average;
            this.count = count;
        }
        
        public double getAverage() { return average; }
        public long getCount() { return count; }
    }
    
    static class MinMaxResult {
        private Student min;
        private Student max;
        
        public MinMaxResult(Student min, Student max) {
            this.min = min;
            this.max = max;
        }
        
        public Student getMin() { return min; }
        public Student getMax() { return max; }
    }
    
    static class BasicStats {
        private double average;
        private long count;
        
        public BasicStats(double average, long count) {
            this.average = average;
            this.count = count;
        }
        
        public double getAverage() { return average; }
        public long getCount() { return count; }
    }
    
    static class ComprehensiveStatistics {
        private BasicStats basicStats;
        private Map<String, Long> gradeDistribution;
        
        public ComprehensiveStatistics(BasicStats basicStats, Map<String, Long> gradeDistribution) {
            this.basicStats = basicStats;
            this.gradeDistribution = gradeDistribution;
        }
        
        public BasicStats getBasicStats() { return basicStats; }
        public Map<String, Long> getGradeDistribution() { return gradeDistribution; }
    }
    
    static class Order {
        private String id;
        private OrderStatus status;
        
        public Order(String id, OrderStatus status) {
            this.id = id;
            this.status = status;
        }
        
        public String getId() { return id; }
        public OrderStatus getStatus() { return status; }
    }
    
    enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
    
    static class Sale {
        private String region;
        private double amount;
        
        public Sale(String region, double amount) {
            this.region = region;
            this.amount = amount;
        }
        
        public String getRegion() { return region; }
        public double getAmount() { return amount; }
    }
    
    static class RevenueStats {
        private double total;
        private double average;
        
        public RevenueStats(double total, double average) {
            this.total = total;
            this.average = average;
        }
        
        public double getTotal() { return total; }
        public double getAverage() { return average; }
    }
    
    static class SalesAnalysis {
        private RevenueStats revenueStats;
        private Map<String, Double> regionalRevenue;
        
        public SalesAnalysis(RevenueStats revenueStats, Map<String, Double> regionalRevenue) {
            this.revenueStats = revenueStats;
            this.regionalRevenue = regionalRevenue;
        }
        
        public RevenueStats getRevenueStats() { return revenueStats; }
        public Map<String, Double> getRegionalRevenue() { return regionalRevenue; }
    }
}