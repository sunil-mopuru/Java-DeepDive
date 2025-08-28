# File I/O - Quick Notes

## 🎯 **Key Concepts**

### **Stream Types**
- **Byte Streams**: Handle raw binary data (InputStream, OutputStream)
- **Character Streams**: Handle text data with encoding (Reader, Writer)
- **Buffered Streams**: Improve performance by reducing system calls
- **NIO.2**: Modern file operations using Path and Files classes

### **Stream Hierarchy**
```
InputStream/OutputStream (bytes)
├── FileInputStream/FileOutputStream
├── BufferedInputStream/BufferedOutputStream
└── DataInputStream/DataOutputStream

Reader/Writer (characters)  
├── FileReader/FileWriter
├── BufferedReader/BufferedWriter
└── PrintWriter
```

---

## 🛠️ **Basic File Operations**

### **File Class**
```java
File file = new File("data.txt");
boolean exists = file.exists();
boolean isFile = file.isFile();
boolean isDir = file.isDirectory();
long size = file.length();
String path = file.getAbsolutePath();
boolean created = file.createNewFile();
boolean deleted = file.delete();
```

### **Directory Operations**
```java
File dir = new File("myFolder");
boolean created = dir.mkdir();        // Create single directory
boolean createdAll = dir.mkdirs();    // Create directory tree
String[] contents = dir.list();       // List file names
File[] files = dir.listFiles();       // List File objects
```

---

## 📖 **Reading Files**

### **Byte Streams**
```java
try (FileInputStream fis = new FileInputStream("data.bin")) {
    int byteData;
    while ((byteData = fis.read()) != -1) {
        // Process byte
    }
}
```

### **Character Streams**
```java
try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        // Process line
    }
}
```

### **NIO.2 (Modern Approach)**
```java
// Read all lines
List<String> lines = Files.readAllLines(Paths.get("data.txt"));

// Read as stream
try (Stream<String> stream = Files.lines(Paths.get("data.txt"))) {
    stream.forEach(System.out::println);
}
```

---

## ✏️ **Writing Files**

### **Character Streams**
```java
try (PrintWriter writer = new PrintWriter(new FileWriter("output.txt"))) {
    writer.println("Line 1");
    writer.printf("Formatted: %s = %d%n", "count", 42);
}
```

### **Buffered Writing**
```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
    writer.write("Hello World");
    writer.newLine();
    writer.flush(); // Ensure data is written
}
```

### **NIO.2 Writing**
```java
List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
Files.write(Paths.get("output.txt"), lines, StandardCharsets.UTF_8);
```

---

## 🔄 **Resource Management**

### **Try-with-Resources (Recommended)**
```java
try (FileReader file = new FileReader("data.txt");
     BufferedReader reader = new BufferedReader(file)) {
    
    return reader.readLine();
} // Automatic resource cleanup
```

### **Traditional Try-Finally**
```java
FileReader file = null;
try {
    file = new FileReader("data.txt");
    // Use file
} finally {
    if (file != null) {
        file.close();
    }
}
```

---

## 🚀 **NIO.2 Path Operations**

### **Path Manipulation**
```java
Path path = Paths.get("folder", "subfolder", "file.txt");
Path parent = path.getParent();
Path filename = path.getFileName();
Path absolute = path.toAbsolutePath();
Path normalized = path.normalize();
```

### **File Operations**
```java
// Check existence and properties
boolean exists = Files.exists(path);
boolean readable = Files.isReadable(path);
long size = Files.size(path);

// Copy and move
Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);

// Create and delete
Files.createFile(path);
Files.createDirectories(path.getParent());
Files.delete(path);
Files.deleteIfExists(path);
```

---

## 📊 **Data Streams**

### **Writing Structured Data**
```java
try (DataOutputStream dos = new DataOutputStream(
        new FileOutputStream("data.bin"))) {
    dos.writeInt(42);
    dos.writeDouble(3.14);
    dos.writeUTF("Hello");
    dos.writeBoolean(true);
}
```

### **Reading Structured Data**
```java
try (DataInputStream dis = new DataInputStream(
        new FileInputStream("data.bin"))) {
    int intVal = dis.readInt();
    double doubleVal = dis.readDouble();
    String stringVal = dis.readUTF();
    boolean boolVal = dis.readBoolean();
}
```

---

## ⚡ **Quick Reference**

| Operation | Old I/O | NIO.2 |
|-----------|---------|-------|
| **Read all text** | `BufferedReader` + loop | `Files.readAllLines()` |
| **Write text** | `PrintWriter` | `Files.write()` |
| **Copy file** | Manual stream copying | `Files.copy()` |
| **Check existence** | `file.exists()` | `Files.exists()` |
| **File size** | `file.length()` | `Files.size()` |
| **Create directory** | `file.mkdirs()` | `Files.createDirectories()` |

---

## 💡 **Best Practices**

1. **Always use try-with-resources** for automatic cleanup
2. **Use buffered streams** for better performance
3. **Choose appropriate stream type**: bytes vs characters
4. **Prefer NIO.2** for new applications (Path/Files)
5. **Handle exceptions properly** (IOException)
6. **Use correct character encoding** (UTF-8)
7. **Check file permissions** before operations

---

## 🔧 **Common Patterns**

### **Configuration File Reader**
```java
Properties props = new Properties();
try (FileInputStream fis = new FileInputStream("config.properties")) {
    props.load(fis);
    String value = props.getProperty("key");
}
```

### **CSV Processing**
```java
try (BufferedReader reader = Files.newBufferedReader(path)) {
    return reader.lines()
                 .skip(1) // Skip header
                 .map(line -> line.split(","))
                 .collect(Collectors.toList());
}
```

---

**Next:** [Multithreading](../09-Multithreading/README.md)