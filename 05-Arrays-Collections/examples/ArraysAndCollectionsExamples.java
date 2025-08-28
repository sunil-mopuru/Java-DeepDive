/**
 * ArraysAndCollectionsExamples.java - Comprehensive Examples
 * 
 * This program demonstrates arrays, collections framework, and their
 * practical applications with real-world scenarios.
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ArraysAndCollectionsExamples {
    
    public static void main(String[] args) {
        
        System.out.println("=== ARRAYS AND COLLECTIONS EXAMPLES ===\n");
        
        // 1. ARRAY OPERATIONS
        arrayOperationsDemo();
        
        // 2. LIST EXAMPLES
        listExamplesDemo();
        
        // 3. SET EXAMPLES
        setExamplesDemo();
        
        // 4. MAP EXAMPLES
        mapExamplesDemo();
        
        // 5. QUEUE EXAMPLES
        queueExamplesDemo();
        
        // 6. PRACTICAL APPLICATIONS
        practicalApplicationsDemo();
    }
    
    // ==================== ARRAY OPERATIONS ====================
    
    public static void arrayOperationsDemo() {
        System.out.println("1. ARRAY OPERATIONS:");
        System.out.println("--------------------");
        
        // Creating and initializing arrays
        int[] numbers = {10, 5, 8, 3, 9, 1, 7};
        String[] fruits = {"apple", "banana", "orange", "grape"};
        
        System.out.println("Original array: " + Arrays.toString(numbers));
        
        // Array searching
        int target = 8;
        int index = linearSearch(numbers, target);
        System.out.println("Linear search for " + target + ": index " + index);
        
        // Array sorting
        int[] sortedNumbers = numbers.clone();
        Arrays.sort(sortedNumbers);
        System.out.println("Sorted array: " + Arrays.toString(sortedNumbers));
        
        // Binary search (requires sorted array)
        int binaryIndex = Arrays.binarySearch(sortedNumbers, target);
        System.out.println("Binary search for " + target + ": index " + binaryIndex);
        
        // Array statistics
        ArrayStats stats = calculateArrayStats(numbers);
        System.out.println("Array statistics: " + stats);
        
        // 2D array example
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        System.out.println("Matrix sum: " + calculateMatrixSum(matrix));
        System.out.println();
    }
    
    public static int linearSearch(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static ArrayStats calculateArrayStats(int[] array) {
        if (array.length == 0) return new ArrayStats(0, 0, 0, 0);
        
        int sum = 0;
        int min = array[0];
        int max = array[0];
        
        for (int num : array) {
            sum += num;
            min = Math.min(min, num);
            max = Math.max(max, num);
        }
        
        return new ArrayStats(sum, sum / (double) array.length, min, max);
    }
    
    public static int calculateMatrixSum(int[][] matrix) {
        int sum = 0;
        for (int[] row : matrix) {
            for (int value : row) {
                sum += value;
            }
        }
        return sum;
    }
    
    // ==================== LIST EXAMPLES ====================
    
    public static void listExamplesDemo() {
        System.out.println("2. LIST EXAMPLES:");
        System.out.println("-----------------");
        
        // ArrayList - dynamic resizing, fast random access
        List<String> cities = new ArrayList<>();
        cities.add("New York");
        cities.add("London");
        cities.add("Tokyo");
        cities.add("Paris");
        
        System.out.println("Cities: " + cities);
        
        // List operations
        cities.add(1, "Sydney");  // Insert at index
        cities.set(0, "NYC");     // Replace element
        System.out.println("After modifications: " + cities);
        
        // LinkedList - efficient insertion/deletion at ends
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addFirst(1);
        queue.addLast(2);
        queue.addLast(3);
        
        System.out.println("Queue: " + queue);
        System.out.println("Remove first: " + queue.removeFirst());
        System.out.println("Queue after removal: " + queue);
        
        // List searching and filtering
        List<Student> students = Arrays.asList(
            new Student("Alice", 85),
            new Student("Bob", 92),
            new Student("Charlie", 78),
            new Student("Diana", 96)
        );
        
        // Find high performers
        List<Student> highPerformers = students.stream()
            .filter(s -> s.getGrade() >= 90)
            .collect(Collectors.toList());
        
        System.out.println("High performers: " + highPerformers);
        
        // Sort students by grade
        students.sort((s1, s2) -> Integer.compare(s2.getGrade(), s1.getGrade()));
        System.out.println("Students by grade (desc): " + students);
        System.out.println();
    }
    
    // ==================== SET EXAMPLES ====================
    
    public static void setExamplesDemo() {
        System.out.println("3. SET EXAMPLES:");
        System.out.println("----------------");
        
        // HashSet - fast operations, no ordering
        Set<String> uniqueWords = new HashSet<>();
        String text = "the quick brown fox jumps over the lazy dog";
        
        for (String word : text.split(" ")) {
            uniqueWords.add(word);
        }
        
        System.out.println("Unique words: " + uniqueWords);
        System.out.println("Total unique words: " + uniqueWords.size());
        
        // TreeSet - sorted order
        Set<Integer> sortedNumbers = new TreeSet<>();
        sortedNumbers.addAll(Arrays.asList(5, 2, 8, 1, 9, 3));
        System.out.println("Sorted numbers: " + sortedNumbers);
        
        // LinkedHashSet - maintains insertion order
        Set<String> orderedColors = new LinkedHashSet<>();
        orderedColors.addAll(Arrays.asList("red", "green", "blue", "yellow"));
        System.out.println("Ordered colors: " + orderedColors);
        
        // Set operations
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C", "D"));
        Set<String> set2 = new HashSet<>(Arrays.asList("C", "D", "E", "F"));
        
        // Union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);
        
        // Intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);
        
        // Difference
        Set<String> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (set1 - set2): " + difference);
        System.out.println();
    }
    
    // ==================== MAP EXAMPLES ====================
    
    public static void mapExamplesDemo() {
        System.out.println("4. MAP EXAMPLES:");
        System.out.println("----------------");
        
        // HashMap - fast operations, no ordering
        Map<String, Integer> studentGrades = new HashMap<>();
        studentGrades.put("Alice", 85);
        studentGrades.put("Bob", 92);
        studentGrades.put("Charlie", 78);
        studentGrades.put("Diana", 96);
        
        System.out.println("Student grades: " + studentGrades);
        
        // Map operations
        System.out.println("Bob's grade: " + studentGrades.get("Bob"));
        System.out.println("Eve's grade: " + studentGrades.getOrDefault("Eve", 0));
        
        studentGrades.putIfAbsent("Eve", 88);
        System.out.println("After adding Eve: " + studentGrades);
        
        // TreeMap - sorted by keys
        Map<String, String> sortedCountries = new TreeMap<>();
        sortedCountries.put("USA", "Washington DC");
        sortedCountries.put("France", "Paris");
        sortedCountries.put("Japan", "Tokyo");
        sortedCountries.put("Australia", "Canberra");
        
        System.out.println("Countries (sorted): " + sortedCountries);
        
        // Frequency counting with Map
        String sentence = "hello world hello java world";
        Map<String, Integer> wordCount = new HashMap<>();
        
        for (String word : sentence.split(" ")) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        
        System.out.println("Word frequencies: " + wordCount);
        
        // Group by functionality
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Engineering"),
            new Employee("Bob", "Marketing"),
            new Employee("Charlie", "Engineering"),
            new Employee("Diana", "Sales"),
            new Employee("Eve", "Engineering")
        );
        
        Map<String, List<Employee>> employeesByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::getDepartment));
        
        System.out.println("Employees by department:");
        employeesByDept.forEach((dept, empList) -> 
            System.out.println("  " + dept + ": " + empList.size() + " employees"));
        System.out.println();
    }
    
    // ==================== QUEUE EXAMPLES ====================
    
    public static void queueExamplesDemo() {
        System.out.println("5. QUEUE EXAMPLES:");
        System.out.println("------------------");
        
        // LinkedList as Queue (FIFO)
        Queue<String> taskQueue = new LinkedList<>();
        taskQueue.offer("Task 1");
        taskQueue.offer("Task 2");
        taskQueue.offer("Task 3");
        
        System.out.println("Task queue: " + taskQueue);
        System.out.println("Processing: " + taskQueue.poll());
        System.out.println("Queue after processing: " + taskQueue);
        
        // PriorityQueue - elements ordered by priority
        PriorityQueue<Task> priorityTasks = new PriorityQueue<>();
        priorityTasks.offer(new Task("Low priority task", 3));
        priorityTasks.offer(new Task("High priority task", 1));
        priorityTasks.offer(new Task("Medium priority task", 2));
        
        System.out.println("Priority queue processing:");
        while (!priorityTasks.isEmpty()) {
            System.out.println("  Processing: " + priorityTasks.poll());
        }
        
        // ArrayDeque - double-ended queue
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("First");
        deque.addLast("Last");
        deque.addFirst("New First");
        
        System.out.println("Deque: " + deque);
        System.out.println("Remove from front: " + deque.removeFirst());
        System.out.println("Remove from back: " + deque.removeLast());
        System.out.println("Final deque: " + deque);
        System.out.println();
    }
    
    // ==================== PRACTICAL APPLICATIONS ====================
    
    public static void practicalApplicationsDemo() {
        System.out.println("6. PRACTICAL APPLICATIONS:");
        System.out.println("--------------------------");
        
        // Shopping cart implementation
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Product("Laptop", 999.99), 1);
        cart.addItem(new Product("Mouse", 29.99), 2);
        cart.addItem(new Product("Keyboard", 79.99), 1);
        
        System.out.println("Shopping Cart:");
        cart.displayCart();
        System.out.println("Total: $" + String.format("%.2f", cart.getTotal()));
        
        // Inventory management
        InventoryManager inventory = new InventoryManager();
        inventory.addStock("LAPTOP001", 50);
        inventory.addStock("MOUSE001", 200);
        inventory.addStock("KEYBOARD001", 100);
        
        System.out.println("\nInventory Status:");
        inventory.displayInventory();
        
        // Process an order
        inventory.processOrder("LAPTOP001", 5);
        inventory.processOrder("MOUSE001", 250); // Should fail - insufficient stock
        
        System.out.println("\nUpdated Inventory:");
        inventory.displayInventory();
        
        // Cache implementation example
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4"); // Should evict key1
        
        System.out.println("\nCache contents:");
        System.out.println("key1: " + cache.get("key1")); // null
        System.out.println("key2: " + cache.get("key2")); // value2
        System.out.println();
    }
}

// ==================== SUPPORTING CLASSES ====================

class ArrayStats {
    private final int sum;
    private final double average;
    private final int min;
    private final int max;
    
    public ArrayStats(int sum, double average, int min, int max) {
        this.sum = sum;
        this.average = average;
        this.min = min;
        this.max = max;
    }
    
    @Override
    public String toString() {
        return String.format("Sum: %d, Average: %.2f, Min: %d, Max: %d", 
                           sum, average, min, max);
    }
}

class Student {
    private final String name;
    private final int grade;
    
    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }
    
    public String getName() { return name; }
    public int getGrade() { return grade; }
    
    @Override
    public String toString() {
        return name + "(" + grade + ")";
    }
}

class Employee {
    private final String name;
    private final String department;
    
    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    
    @Override
    public String toString() {
        return name;
    }
}

class Task implements Comparable<Task> {
    private final String description;
    private final int priority;
    
    public Task(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }
    
    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
    
    @Override
    public String toString() {
        return description + " (Priority: " + priority + ")";
    }
}

class Product {
    private final String name;
    private final double price;
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}

class ShoppingCart {
    private final Map<Product, Integer> items = new HashMap<>();
    
    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }
    
    public void removeItem(Product product) {
        items.remove(product);
    }
    
    public double getTotal() {
        return items.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }
    
    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("  Cart is empty");
            return;
        }
        
        items.forEach((product, quantity) -> 
            System.out.printf("  %s x%d = $%.2f%n", 
                            product.getName(), quantity, 
                            product.getPrice() * quantity));
    }
}

class InventoryManager {
    private final Map<String, Integer> stock = new ConcurrentHashMap<>();
    
    public void addStock(String productId, int quantity) {
        stock.put(productId, stock.getOrDefault(productId, 0) + quantity);
    }
    
    public boolean processOrder(String productId, int quantity) {
        int currentStock = stock.getOrDefault(productId, 0);
        if (currentStock >= quantity) {
            stock.put(productId, currentStock - quantity);
            System.out.println("Order processed: " + productId + " x" + quantity);
            return true;
        } else {
            System.out.println("Insufficient stock for " + productId + 
                             " (requested: " + quantity + ", available: " + currentStock + ")");
            return false;
        }
    }
    
    public void displayInventory() {
        stock.forEach((productId, quantity) -> 
            System.out.println("  " + productId + ": " + quantity + " units"));
    }
}

class LRUCache<K, V> {
    private final int capacity;
    private final LinkedHashMap<K, V> cache;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<K, V>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }
    
    public V get(K key) {
        return cache.get(key);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
    }
}