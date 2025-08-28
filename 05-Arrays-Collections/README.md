# Chapter 5: Arrays and Collections Framework

## 📚 Table of Contents
1. [Introduction to Arrays](#introduction-to-arrays)
2. [Java Collections Framework](#java-collections-framework)
3. [Lists](#lists)
4. [Sets](#sets)
5. [Maps](#maps)
6. [Queues and Deques](#queues-and-deques)
7. [Iterating Collections](#iterating-collections)
8. [Collections Utility Class](#collections-utility-class)
9. [Best Practices](#best-practices)
10. [Key Takeaways](#key-takeaways)

---

## Introduction to Arrays

### **What are Arrays?**
Arrays are containers that hold multiple values of the same data type in contiguous memory locations.

### **Array Characteristics:**
- **Fixed size** - Cannot be resized after creation
- **Homogeneous** - All elements must be same type
- **Zero-indexed** - First element at index 0
- **Reference types** - Arrays are objects in Java

### **Array Declaration and Initialization:**
```java
// Declaration
int[] numbers;
String[] names;

// Declaration with initialization
int[] scores = new int[5];           // Array of 5 integers (all 0)
String[] cities = new String[3];     // Array of 3 strings (all null)

// Declaration with values
int[] ages = {25, 30, 35, 40};      // Array literal
String[] colors = {"red", "green", "blue"};

// Alternative syntax (less common)
int ages[] = new int[4];
```

### **Multi-dimensional Arrays:**
```java
// 2D Array (matrix)
int[][] matrix = new int[3][4];     // 3 rows, 4 columns
int[][] table = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// 3D Array
int[][][] cube = new int[2][3][4];

// Jagged arrays (different row sizes)
int[][] jagged = {
    {1, 2},
    {3, 4, 5, 6},
    {7}
};
```

### **Array Operations:**
```java
int[] numbers = {10, 20, 30, 40, 50};

// Access elements
int first = numbers[0];              // 10
int last = numbers[numbers.length - 1]; // 50

// Modify elements
numbers[2] = 35;                     // Change 30 to 35

// Array length
int size = numbers.length;           // 5

// Array traversal
for (int i = 0; i < numbers.length; i++) {
    System.out.println("Index " + i + ": " + numbers[i]);
}

// Enhanced for loop
for (int num : numbers) {
    System.out.println("Number: " + num);
}
```

---

## Java Collections Framework

### **Why Collections?**
Arrays have limitations:
- **Fixed size** - Cannot grow or shrink
- **No built-in methods** - No add, remove, search operations
- **Type safety issues** - Can store wrong types (before generics)

Collections provide:
- **Dynamic sizing** - Grow and shrink as needed
- **Rich API** - Built-in methods for common operations
- **Type safety** - Generics ensure type correctness
- **Different data structures** - Choose the right one for your needs

### **Collections Hierarchy:**
```
                    Collection<E>
                   /      |      \
                  /       |       \
             List<E>   Set<E>   Queue<E>
            /   |   \     |        |
      ArrayList Vector LinkedList HashSet ArrayDeque
      LinkedList Stack           TreeSet PriorityQueue
                                 LinkedHashSet
```

### **Map Interface** (separate hierarchy):
```
        Map<K,V>
       /    |    \
  HashMap TreeMap LinkedHashMap
  Hashtable
```

---

## Lists

### **List Interface Characteristics:**
- **Ordered collection** - Elements have positions
- **Allows duplicates** - Same element can appear multiple times
- **Indexed access** - Get/set elements by index
- **Dynamic size** - Can grow and shrink

### **ArrayList:**
```java
import java.util.*;

// Creation
List<String> fruits = new ArrayList<>();
ArrayList<Integer> numbers = new ArrayList<>();

// Adding elements
fruits.add("apple");
fruits.add("banana");
fruits.add("cherry");
fruits.add(1, "orange");  // Insert at index 1

// Accessing elements
String first = fruits.get(0);           // "apple"
String second = fruits.get(1);          // "orange"

// Modifying elements
fruits.set(2, "grape");                 // Replace "banana" with "grape"

// Removing elements
fruits.remove("cherry");                // Remove by value
fruits.remove(0);                       // Remove by index

// Size and checks
int size = fruits.size();               // Current size
boolean isEmpty = fruits.isEmpty();     // Check if empty
boolean contains = fruits.contains("apple"); // Check if contains element

// Bulk operations
List<String> moreFruits = Arrays.asList("mango", "kiwi");
fruits.addAll(moreFruits);              // Add all elements
```

### **LinkedList:**
```java
LinkedList<String> list = new LinkedList<>();

// LinkedList-specific methods
list.addFirst("first");
list.addLast("last");
list.add("middle");

String head = list.getFirst();
String tail = list.getLast();

list.removeFirst();
list.removeLast();
```

### **ArrayList vs LinkedList:**

| Feature | ArrayList | LinkedList |
|---------|-----------|------------|
| **Access Time** | O(1) | O(n) |
| **Insertion/Deletion** | O(n) | O(1) |
| **Memory Usage** | Less | More (node overhead) |
| **Use Case** | Random access | Frequent insertion/deletion |

---

## Sets

### **Set Interface Characteristics:**
- **No duplicates** - Each element appears once
- **May be ordered** - Depends on implementation
- **Mathematical set operations** - Union, intersection

### **HashSet:**
```java
Set<String> uniqueNames = new HashSet<>();

uniqueNames.add("John");
uniqueNames.add("Jane");
uniqueNames.add("John");    // Duplicate - won't be added

System.out.println(uniqueNames.size()); // 2, not 3

// Check membership
if (uniqueNames.contains("John")) {
    System.out.println("John is in the set");
}

// Set operations
Set<String> set1 = new HashSet<>(Arrays.asList("A", "B", "C"));
Set<String> set2 = new HashSet<>(Arrays.asList("B", "C", "D"));

// Union
Set<String> union = new HashSet<>(set1);
union.addAll(set2);         // {A, B, C, D}

// Intersection
Set<String> intersection = new HashSet<>(set1);
intersection.retainAll(set2); // {B, C}

// Difference
Set<String> difference = new HashSet<>(set1);
difference.removeAll(set2);  // {A}
```

### **TreeSet (Sorted Set):**
```java
Set<Integer> sortedNumbers = new TreeSet<>();
sortedNumbers.addAll(Arrays.asList(5, 2, 8, 1, 9));

System.out.println(sortedNumbers); // [1, 2, 5, 8, 9] - automatically sorted

// NavigableSet methods
TreeSet<Integer> treeSet = (TreeSet<Integer>) sortedNumbers;
Integer first = treeSet.first();    // 1
Integer last = treeSet.last();      // 9
Integer lower = treeSet.lower(5);   // 2 (largest element < 5)
Integer higher = treeSet.higher(5); // 8 (smallest element > 5)
```

### **LinkedHashSet (Insertion Order):**
```java
Set<String> orderedSet = new LinkedHashSet<>();
orderedSet.add("third");
orderedSet.add("first");
orderedSet.add("second");

// Maintains insertion order: [third, first, second]
```

---

## Maps

### **Map Interface Characteristics:**
- **Key-Value pairs** - Each key maps to a value
- **Unique keys** - No duplicate keys allowed
- **Values can be duplicated**
- **No ordering** (unless specified by implementation)

### **HashMap:**
```java
Map<String, Integer> ageMap = new HashMap<>();

// Adding entries
ageMap.put("John", 25);
ageMap.put("Jane", 30);
ageMap.put("Bob", 35);

// Accessing values
Integer johnAge = ageMap.get("John");    // 25
Integer unknownAge = ageMap.get("Mike"); // null (key doesn't exist)

// Safe access with default
Integer mikeAge = ageMap.getOrDefault("Mike", 0); // 0

// Updating values
ageMap.put("John", 26);                  // Update existing
ageMap.putIfAbsent("Alice", 28);         // Add only if key doesn't exist

// Removing entries
ageMap.remove("Bob");                    // Remove by key
ageMap.remove("Jane", 30);               // Remove only if key-value matches

// Checking contents
boolean hasJohn = ageMap.containsKey("John");
boolean has25 = ageMap.containsValue(25);
```

### **TreeMap (Sorted by Keys):**
```java
Map<String, String> sortedMap = new TreeMap<>();
sortedMap.put("Charlie", "Manager");
sortedMap.put("Alice", "Developer");
sortedMap.put("Bob", "Designer");

// Keys are automatically sorted: Alice, Bob, Charlie
```

### **Iterating Maps:**
```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Math", 95);
scores.put("Science", 87);
scores.put("English", 92);

// Iterate over keys
for (String subject : scores.keySet()) {
    System.out.println(subject + ": " + scores.get(subject));
}

// Iterate over values
for (Integer score : scores.values()) {
    System.out.println("Score: " + score);
}

// Iterate over entries (most efficient)
for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Java 8 forEach
scores.forEach((subject, score) -> 
    System.out.println(subject + ": " + score));
```

---

## Queues and Deques

### **Queue Interface:**
```java
Queue<String> queue = new LinkedList<>();

// Adding elements
queue.offer("first");   // Add to rear
queue.offer("second");
queue.offer("third");

// Accessing elements
String head = queue.peek();  // Get front element without removing
String front = queue.poll(); // Remove and return front element

// Queue operations
boolean isEmpty = queue.isEmpty();
int size = queue.size();
```

### **PriorityQueue (Heap):**
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(5);
minHeap.offer(2);
minHeap.offer(8);
minHeap.offer(1);

while (!minHeap.isEmpty()) {
    System.out.println(minHeap.poll()); // Prints: 1, 2, 5, 8
}

// Custom comparator for max heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
```

### **ArrayDeque (Double-ended Queue):**
```java
Deque<String> deque = new ArrayDeque<>();

// Add to both ends
deque.addFirst("front");
deque.addLast("back");

// Remove from both ends
String first = deque.removeFirst();
String last = deque.removeLast();

// Use as stack
deque.push("item1");    // Add to front
deque.push("item2");
String top = deque.pop(); // Remove from front
```

---

## Iterating Collections

### **Iterator Pattern:**
```java
List<String> list = Arrays.asList("a", "b", "c", "d");

// Iterator (forward only)
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String item = it.next();
    if (item.equals("b")) {
        it.remove(); // Safe removal during iteration
    }
}

// ListIterator (bidirectional)
ListIterator<String> listIt = list.listIterator();
while (listIt.hasNext()) {
    String item = listIt.next();
    listIt.set(item.toUpperCase()); // Modify during iteration
}

// Reverse iteration
while (listIt.hasPrevious()) {
    System.out.println(listIt.previous());
}
```

### **Enhanced for Loop vs Iterator:**
```java
List<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));

// Enhanced for - clean but can't modify collection
for (String item : items) {
    System.out.println(item);
    // items.remove(item); // ConcurrentModificationException!
}

// Iterator - can safely modify
Iterator<String> it = items.iterator();
while (it.hasNext()) {
    String item = it.next();
    if (item.equals("b")) {
        it.remove(); // Safe
    }
}
```

---

## Collections Utility Class

### **Static Utility Methods:**
```java
List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));

// Sorting
Collections.sort(numbers);                    // Natural order
Collections.sort(numbers, Collections.reverseOrder()); // Reverse order

// Searching (requires sorted list)
int index = Collections.binarySearch(numbers, 4);

// Min/Max
Integer min = Collections.min(numbers);
Integer max = Collections.max(numbers);

// Shuffle
Collections.shuffle(numbers);

// Fill
Collections.fill(numbers, 0);                 // Fill all with 0

// Copy
List<Integer> copy = new ArrayList<>(Collections.nCopies(5, 10)); // 5 copies of 10

// Frequency
int count = Collections.frequency(numbers, 1); // Count occurrences of 1

// Immutable collections
List<String> immutableList = Collections.unmodifiableList(Arrays.asList("a", "b"));
Set<String> immutableSet = Collections.unmodifiableSet(new HashSet<>());
Map<String, Integer> immutableMap = Collections.unmodifiableMap(new HashMap<>());

// Empty collections
List<String> emptyList = Collections.emptyList();
Set<String> emptySet = Collections.emptySet();
Map<String, Integer> emptyMap = Collections.emptyMap();
```

---

## Best Practices

### **1. Use Interface Types:**
```java
// Good - using interface type
List<String> list = new ArrayList<>();
Set<Integer> set = new HashSet<>();
Map<String, Object> map = new HashMap<>();

// Avoid - using concrete types
ArrayList<String> list = new ArrayList<>(); // Less flexible
```

### **2. Initialize with Capacity:**
```java
// When you know approximate size
List<String> list = new ArrayList<>(1000);
Set<Integer> set = new HashSet<>(500);
Map<String, Object> map = new HashMap<>(200);
```

### **3. Use Appropriate Collection:**
```java
// Frequent random access
List<String> list = new ArrayList<>();

// Frequent insertion/deletion at ends
List<String> list = new LinkedList<>();

// Unique elements, fast lookup
Set<String> set = new HashSet<>();

// Unique elements, sorted
Set<String> set = new TreeSet<>();

// Key-value mapping, fast lookup
Map<String, Integer> map = new HashMap<>();

// Key-value mapping, sorted by keys
Map<String, Integer> map = new TreeMap<>();
```

### **4. Null Handling:**
```java
// Check for null before operations
if (list != null && !list.isEmpty()) {
    // Process list
}

// Use Optional for better null handling
Optional<String> first = list.stream().findFirst();
```

---

## Key Takeaways

### **Essential Concepts Mastered:**
✅ **Arrays** - Fixed-size, indexed collections  
✅ **Lists** - Dynamic, ordered collections (ArrayList, LinkedList)  
✅ **Sets** - Unique element collections (HashSet, TreeSet)  
✅ **Maps** - Key-value pair collections (HashMap, TreeMap)  
✅ **Queues** - FIFO and priority-based collections  
✅ **Iteration** - Safe traversal and modification techniques  
✅ **Collections Utilities** - Sorting, searching, and manipulation  

### **Collection Selection Guide:**

| Need | Use |
|------|-----|
| **Indexed access** | ArrayList |
| **Frequent insertion/deletion** | LinkedList |
| **Unique elements** | HashSet |
| **Sorted unique elements** | TreeSet |
| **Key-value mapping** | HashMap |
| **Sorted key-value mapping** | TreeMap |
| **FIFO processing** | Queue (LinkedList) |
| **Priority processing** | PriorityQueue |

### **Performance Characteristics:**

| Operation | ArrayList | LinkedList | HashSet | TreeSet | HashMap |
|-----------|-----------|------------|---------|---------|---------|
| **Access** | O(1) | O(n) | O(1) | O(log n) | O(1) |
| **Insert** | O(1)* | O(1) | O(1) | O(log n) | O(1) |
| **Delete** | O(n) | O(1) | O(1) | O(log n) | O(1) |
| **Search** | O(n) | O(n) | O(1) | O(log n) | O(1) |

*Amortized time; O(n) when resizing needed

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Learn about Java 8 Streams for collection processing
- Move to [Exception Handling](../06-Exception-Handling/README.md)

---

**Continue to: [Chapter 6: Exception Handling →](../06-Exception-Handling/README.md)**