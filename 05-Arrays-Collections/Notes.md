# Arrays and Collections - Quick Reference Notes

## 📋 Arrays

### Declaration and Initialization
```java
// Declaration
int[] numbers;
String[] names;

// With size
int[] scores = new int[5];

// With values
int[] ages = {25, 30, 35};
String[] colors = {"red", "green", "blue"};

// 2D arrays
int[][] matrix = new int[3][4];
int[][] table = {{1, 2}, {3, 4}};
```

### Common Operations
```java
// Access
int first = arr[0];
int last = arr[arr.length - 1];

// Modify
arr[2] = 100;

// Length
int size = arr.length;

// Iteration
for (int i = 0; i < arr.length; i++) { }
for (int value : arr) { }
```

## 📚 Collections Framework

### List Interface
```java
List<String> list = new ArrayList<>();

// Add elements
list.add("item");
list.add(0, "first");

// Access
String item = list.get(0);
list.set(1, "updated");

// Remove
list.remove(0);
list.remove("item");

// Size and checks
int size = list.size();
boolean empty = list.isEmpty();
boolean contains = list.contains("item");
```

### Set Interface
```java
Set<String> set = new HashSet<>();

// Add (no duplicates)
set.add("unique");

// Check membership
if (set.contains("item")) { }

// Set operations
set1.addAll(set2);      // Union
set1.retainAll(set2);   // Intersection
set1.removeAll(set2);   // Difference
```

### Map Interface
```java
Map<String, Integer> map = new HashMap<>();

// Add/Update
map.put("key", 42);
map.putIfAbsent("key", 0);

// Access
Integer value = map.get("key");
Integer safe = map.getOrDefault("key", 0);

// Remove
map.remove("key");

// Iteration
for (String key : map.keySet()) { }
for (Integer value : map.values()) { }
for (Map.Entry<String, Integer> entry : map.entrySet()) { }
```

## 🔄 Queue Interface
```java
Queue<String> queue = new LinkedList<>();

// Add
queue.offer("item");

// Access head
String head = queue.peek();  // Don't remove
String item = queue.poll();  // Remove and return

// Deque (double-ended)
Deque<String> deque = new ArrayDeque<>();
deque.addFirst("front");
deque.addLast("back");
deque.removeFirst();
deque.removeLast();
```

## 🎯 Collection Selection Guide

### Choose ArrayList when:
- Need indexed access
- Mostly reading data
- Size doesn't change frequently

### Choose LinkedList when:
- Frequent insertion/deletion
- Don't need random access
- Using as queue/stack

### Choose HashSet when:
- Need unique elements
- Fast lookup required
- Order doesn't matter

### Choose TreeSet when:
- Need unique elements
- Want sorted order
- Need range operations

### Choose HashMap when:
- Key-value mapping
- Fast lookup by key
- Order doesn't matter

### Choose TreeMap when:
- Key-value mapping
- Need sorted keys
- Range queries needed

## ⚡ Performance Cheatsheet

| Collection | Get | Add | Remove | Contains |
|------------|-----|-----|--------|----------|
| ArrayList | O(1) | O(1)* | O(n) | O(n) |
| LinkedList | O(n) | O(1) | O(1) | O(n) |
| HashSet | N/A | O(1) | O(1) | O(1) |
| TreeSet | N/A | O(log n) | O(log n) | O(log n) |
| HashMap | O(1) | O(1) | O(1) | O(1) |
| TreeMap | O(log n) | O(log n) | O(log n) | O(log n) |

*Amortized; O(n) when resizing

## 🛠️ Collections Utility Methods
```java
// Sorting
Collections.sort(list);
Collections.sort(list, Collections.reverseOrder());

// Searching (sorted list required)
int index = Collections.binarySearch(list, item);

// Min/Max
T min = Collections.min(collection);
T max = Collections.max(collection);

// Shuffle
Collections.shuffle(list);

// Fill
Collections.fill(list, value);

// Immutable views
List<T> readonly = Collections.unmodifiableList(list);
```

## 🔍 Iteration Best Practices

### Enhanced for loop (when no modification needed)
```java
for (String item : collection) {
    System.out.println(item);
}
```

### Iterator (when modification needed)
```java
Iterator<String> it = collection.iterator();
while (it.hasNext()) {
    String item = it.next();
    if (condition) {
        it.remove(); // Safe removal
    }
}
```

### Stream API (Java 8+)
```java
collection.stream()
    .filter(item -> condition)
    .map(String::toUpperCase)
    .forEach(System.out::println);
```

## ⚠️ Common Pitfalls

### 1. ConcurrentModificationException
```java
// Wrong
for (String item : list) {
    if (condition) {
        list.remove(item); // Exception!
    }
}

// Correct
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (condition) {
        it.remove(); // Safe
    }
}
```

### 2. Using == instead of equals()
```java
// Wrong
if (str1 == str2) { }

// Correct
if (str1.equals(str2)) { }
```

### 3. Modifying during enhanced for loop
```java
// Wrong - will throw exception
for (String item : list) {
    list.add("new item");
}

// Correct - use regular for loop or iterator
```

## 💡 Best Practices

### 1. Use interface types
```java
List<String> list = new ArrayList<>();    // Good
ArrayList<String> list = new ArrayList<>(); // Avoid
```

### 2. Initialize with capacity if known
```java
List<String> list = new ArrayList<>(1000);
```

### 3. Use diamond operator
```java
Map<String, List<Integer>> map = new HashMap<>(); // Java 7+
```

### 4. Prefer immutable collections when possible
```java
List<String> items = List.of("a", "b", "c"); // Java 9+
```

---
**Next Topic:** [Exception Handling →](../../06-Exception-Handling/Notes.md)