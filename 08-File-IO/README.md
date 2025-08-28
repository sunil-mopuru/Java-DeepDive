# Chapter 8: File I/O and Streams

## 📚 Table of Contents
1. [Introduction to File I/O](#introduction-to-file-io)
2. [File and Path Classes](#file-and-path-classes)
3. [Byte Streams](#byte-streams)
4. [Character Streams](#character-streams)
5. [NIO.2 (New I/O)](#nio2-new-io)
6. [Serialization](#serialization)
7. [Best Practices](#best-practices)

---

## Introduction to File I/O

### **I/O Streams Overview:**
- **Input Streams** - Read data from source
- **Output Streams** - Write data to destination
- **Byte Streams** - Handle binary data (8-bit bytes)
- **Character Streams** - Handle text data (16-bit Unicode)

### **Stream Hierarchy:**
```
InputStream/OutputStream (Byte Streams)
├── FileInputStream/FileOutputStream
├── BufferedInputStream/BufferedOutputStream
└── ObjectInputStream/ObjectOutputStream

Reader/Writer (Character Streams)
├── FileReader/FileWriter
├── BufferedReader/BufferedWriter
└── StringReader/StringWriter
```

---

## File and Path Classes

### **File Class (Legacy):**
```java
import java.io.File;

File file = new File("data.txt");
File dir = new File("documents");

// File information
boolean exists = file.exists();
boolean isFile = file.isFile();
boolean isDirectory = file.isDirectory();
long size = file.length();
long lastModified = file.lastModified();

// File operations
boolean created = file.createNewFile();
boolean deleted = file.delete();
boolean renamed = file.renameTo(new File("newname.txt"));

// Directory operations
boolean dirCreated = dir.mkdir();
boolean dirsCreated = dir.mkdirs();
File[] files = dir.listFiles();
```

### **Path Class (NIO.2):**
```java
import java.nio.file.*;

Path path = Paths.get("data.txt");
Path absolutePath = path.toAbsolutePath();
Path parent = path.getParent();
Path fileName = path.getFileName();

// Path operations
Path resolved = path.resolve("subfolder/file.txt");
Path relative = path1.relativize(path2);
Path normalized = path.normalize();

// File operations with Path
boolean exists = Files.exists(path);
boolean isRegularFile = Files.isRegularFile(path);
long size = Files.size(path);
FileTime modified = Files.getLastModifiedTime(path);
```

---

## Byte Streams

### **FileInputStream/FileOutputStream:**
```java
// Reading bytes
try (FileInputStream fis = new FileInputStream("input.bin")) {
    int byteData;
    while ((byteData = fis.read()) != -1) {
        System.out.print((char) byteData);
    }
} catch (IOException e) {
    e.printStackTrace();
}

// Writing bytes
try (FileOutputStream fos = new FileOutputStream("output.bin")) {
    String data = "Hello World";
    fos.write(data.getBytes());
} catch (IOException e) {
    e.printStackTrace();
}
```

### **Buffered Streams:**
```java
// Buffered reading (more efficient)
try (BufferedInputStream bis = new BufferedInputStream(
        new FileInputStream("large-file.bin"))) {
    
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        // Process buffer
        System.out.write(buffer, 0, bytesRead);
    }
}
```

---

## Character Streams

### **FileReader/FileWriter:**
```java
// Reading characters
try (FileReader reader = new FileReader("text.txt")) {
    int charData;
    while ((charData = reader.read()) != -1) {
        System.out.print((char) charData);
    }
}

// Writing characters
try (FileWriter writer = new FileWriter("output.txt")) {
    writer.write("Hello World\n");
    writer.write("Java File I/O");
}
```

### **BufferedReader/BufferedWriter:**
```java
// Reading lines efficiently
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println("Line: " + line);
    }
}

// Writing lines efficiently
try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    writer.write("Line 1");
    writer.newLine();
    writer.write("Line 2");
    writer.newLine();
}
```

### **PrintWriter (Formatted Output):**
```java
try (PrintWriter writer = new PrintWriter("formatted.txt")) {
    writer.println("Name: John Doe");
    writer.printf("Age: %d%n", 30);
    writer.printf("Salary: $%.2f%n", 75000.50);
}
```

---

## NIO.2 (New I/O)

### **Files Class Utility Methods:**
```java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

// Reading entire file
Path path = Paths.get("data.txt");
byte[] bytes = Files.readAllBytes(path);
List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
String content = Files.readString(path); // Java 11+

// Writing entire file
Files.write(path, "Hello World".getBytes());
Files.write(path, Arrays.asList("Line 1", "Line 2"));
Files.writeString(path, "Hello World"); // Java 11+

// Copy, move, delete
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
Files.delete(path);
Files.deleteIfExists(path);
```

### **Directory Operations:**
```java
// Create directories
Files.createDirectory(Paths.get("newdir"));
Files.createDirectories(Paths.get("parent/child/grandchild"));

// List directory contents
try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("."))) {
    for (Path entry : stream) {
        System.out.println(entry.getFileName());
    }
}

// Walk file tree (Java 8+)
Files.walk(Paths.get("."))
     .filter(Files::isRegularFile)
     .filter(path -> path.toString().endsWith(".java"))
     .forEach(System.out::println);
```

### **File Attributes:**
```java
Path file = Paths.get("data.txt");

// Basic attributes
BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
System.out.println("Size: " + attrs.size());
System.out.println("Created: " + attrs.creationTime());
System.out.println("Modified: " + attrs.lastModifiedTime());

// Set attributes
Files.setLastModifiedTime(file, FileTime.fromMillis(System.currentTimeMillis()));
```

---

## Serialization

### **Object Serialization:**
```java
import java.io.*;

// Serializable class
class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    private transient String password; // Won't be serialized
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // getters and setters
}

// Writing object to file
Person person = new Person("John", 30);
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("person.ser"))) {
    oos.writeObject(person);
}

// Reading object from file
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("person.ser"))) {
    Person loaded = (Person) ois.readObject();
    System.out.println("Loaded: " + loaded.getName());
}
```

### **Custom Serialization:**
```java
class CustomSerialization implements Serializable {
    private String data;
    private transient String secret;
    
    // Custom serialization method
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // Serialize non-transient fields
        oos.writeObject(encrypt(secret)); // Custom handling for secret
    }
    
    // Custom deserialization method
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Deserialize non-transient fields
        secret = decrypt((String) ois.readObject()); // Custom handling
    }
    
    private String encrypt(String data) { return data; } // Implement encryption
    private String decrypt(String data) { return data; } // Implement decryption
}
```

---

## Best Practices

### **1. Always Use Try-with-Resources:**
```java
// Good - automatic resource management
try (BufferedReader reader = Files.newBufferedReader(path)) {
    return reader.readLine();
} // Automatically closed

// Avoid - manual resource management
BufferedReader reader = null;
try {
    reader = Files.newBufferedReader(path);
    return reader.readLine();
} finally {
    if (reader != null) reader.close();
}
```

### **2. Use Appropriate Stream Types:**
```java
// Binary data - use byte streams
try (InputStream in = new FileInputStream("image.jpg")) {
    // Process binary data
}

// Text data - use character streams
try (Reader reader = new FileReader("document.txt")) {
    // Process text data
}
```

### **3. Buffer for Performance:**
```java
// Unbuffered - slow for large files
try (FileReader reader = new FileReader("large.txt")) {
    int ch;
    while ((ch = reader.read()) != -1) {
        // Process character by character - slow!
    }
}

// Buffered - much faster
try (BufferedReader reader = new BufferedReader(new FileReader("large.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        // Process line by line - fast!
    }
}
```

### **4. Handle Exceptions Properly:**
```java
public String readFileContent(String filename) {
    try {
        return Files.readString(Paths.get(filename));
    } catch (NoSuchFileException e) {
        System.err.println("File not found: " + filename);
        return null;
    } catch (AccessDeniedException e) {
        System.err.println("Access denied: " + filename);
        return null;
    } catch (IOException e) {
        System.err.println("I/O error reading " + filename + ": " + e.getMessage());
        return null;
    }
}
```

### **5. Use NIO.2 for New Code:**
```java
// Prefer NIO.2 (newer, more features)
Path path = Paths.get("data.txt");
List<String> lines = Files.readAllLines(path);

// Over legacy File I/O
File file = new File("data.txt");
// Manual reading required
```

---

## Key Takeaways

### **Essential Concepts:**
✅ **Stream Types** - Byte vs character streams  
✅ **File Operations** - Reading, writing, copying, moving  
✅ **NIO.2** - Modern file I/O API with Path and Files  
✅ **Buffering** - Performance optimization for I/O  
✅ **Serialization** - Converting objects to byte streams  
✅ **Resource Management** - Try-with-resources for automatic cleanup  

### **Stream Selection Guide:**

| Use Case | Recommended Stream |
|----------|-------------------|
| **Read text files** | BufferedReader |
| **Write text files** | BufferedWriter/PrintWriter |
| **Read binary files** | BufferedInputStream |
| **Write binary files** | BufferedOutputStream |
| **Object persistence** | ObjectInputStream/ObjectOutputStream |
| **Large files** | NIO.2 with streaming |

### **Performance Tips:**
- **Always use buffered streams** for large files
- **Read/write in chunks** rather than byte-by-byte
- **Use NIO.2** for better performance and features
- **Consider memory mapping** for very large files
- **Use appropriate buffer sizes** (typically 8KB-64KB)

### **Next Steps:**
- Practice with [exercises](./exercises/)
- Learn about concurrent file access and locking
- Move to [Multithreading](../09-Multithreading/README.md)

---

**Continue to: [Chapter 9: Multithreading →](../09-Multithreading/README.md)**