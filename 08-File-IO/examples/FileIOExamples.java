/**
 * FileIOExamples.java - Comprehensive File I/O Examples
 * 
 * This program demonstrates various file operations including reading,
 * writing, stream processing, and practical file handling scenarios.
 */

import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class FileIOExamples {
    
    private static final String SAMPLE_DIR = "file_examples";
    
    public static void main(String[] args) {
        
        System.out.println("=== FILE I/O COMPREHENSIVE EXAMPLES ===\n");
        
        // Create sample directory
        createSampleDirectory();
        
        try {
            // 1. BASIC FILE OPERATIONS
            basicFileOperations();
            
            // 2. BYTE STREAMS
            byteStreamOperations();
            
            // 3. CHARACTER STREAMS
            characterStreamOperations();
            
            // 4. BUFFERED I/O
            bufferedIOOperations();
            
            // 5. NIO.2 (NEW I/O)
            nioOperations();
            
            // 6. PRACTICAL APPLICATIONS
            practicalApplications();
            
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cleanup
            cleanup();
        }
    }
    
    // ==================== SETUP AND CLEANUP ====================
    
    private static void createSampleDirectory() {
        try {
            Path dir = Paths.get(SAMPLE_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
                System.out.println("Created sample directory: " + SAMPLE_DIR);
            }
        } catch (IOException e) {
            System.err.println("Failed to create sample directory: " + e.getMessage());
        }
    }
    
    private static void cleanup() {
        try {
            Path dir = Paths.get(SAMPLE_DIR);
            if (Files.exists(dir)) {
                // Delete all files in directory
                try (Stream<Path> paths = Files.walk(dir)) {
                    paths.sorted(Comparator.reverseOrder())
                         .map(Path::toFile)
                         .forEach(File::delete);
                }
                System.out.println("Cleaned up sample directory");
            }
        } catch (IOException e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }
    
    // ==================== BASIC FILE OPERATIONS ====================
    
    public static void basicFileOperations() throws IOException {
        System.out.println("1. BASIC FILE OPERATIONS:");
        System.out.println("--------------------------");
        
        // Create files
        File textFile = new File(SAMPLE_DIR, "sample.txt");
        File dataFile = new File(SAMPLE_DIR, "data.bin");
        
        // File information
        System.out.printf("File exists: %b%n", textFile.exists());
        System.out.printf("Is file: %b%n", textFile.isFile());
        System.out.printf("Is directory: %b%n", textFile.isDirectory());
        System.out.printf("Can read: %b%n", textFile.canRead());
        System.out.printf("Can write: %b%n", textFile.canWrite());
        
        // Create new file
        if (textFile.createNewFile()) {
            System.out.println("Created new file: " + textFile.getName());
        }
        
        // File properties
        System.out.printf("Absolute path: %s%n", textFile.getAbsolutePath());
        System.out.printf("File name: %s%n", textFile.getName());
        System.out.printf("Parent directory: %s%n", textFile.getParent());
        
        // Directory operations
        File sampleDir = new File(SAMPLE_DIR);
        System.out.printf("Directory exists: %b%n", sampleDir.exists());
        System.out.printf("Is directory: %b%n", sampleDir.isDirectory());
        
        // List directory contents
        String[] contents = sampleDir.list();
        if (contents != null) {
            System.out.println("Directory contents:");
            for (String item : contents) {
                System.out.println("  " + item);
            }
        }
        
        System.out.println();
    }
    
    // ==================== BYTE STREAMS ====================
    
    public static void byteStreamOperations() throws IOException {
        System.out.println("2. BYTE STREAMS:");
        System.out.println("----------------");
        
        File binaryFile = new File(SAMPLE_DIR, "binary_data.bin");
        
        // Writing binary data
        try (FileOutputStream fos = new FileOutputStream(binaryFile)) {
            // Write different types of data
            byte[] data = {65, 66, 67, 68, 69}; // ASCII: A, B, C, D, E
            fos.write(data);
            
            // Write integers as bytes
            for (int i = 1; i <= 5; i++) {
                fos.write(i);
            }
            
            System.out.println("Written binary data to file");
        }
        
        // Reading binary data
        try (FileInputStream fis = new FileInputStream(binaryFile)) {
            System.out.println("Reading binary data:");
            
            int byteData;
            int count = 0;
            while ((byteData = fis.read()) != -1 && count < 10) {
                System.out.printf("Byte %d: %d (char: %c)%n", 
                                count++, byteData, (char)byteData);
            }
        }
        
        // DataOutputStream for structured data
        File structuredFile = new File(SAMPLE_DIR, "structured_data.bin");
        try (DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(structuredFile))) {
            
            dos.writeInt(42);
            dos.writeDouble(3.14159);
            dos.writeUTF("Hello World");
            dos.writeBoolean(true);
            
            System.out.println("Written structured binary data");
        }
        
        // DataInputStream for reading structured data
        try (DataInputStream dis = new DataInputStream(
                new FileInputStream(structuredFile))) {
            
            int intValue = dis.readInt();
            double doubleValue = dis.readDouble();
            String stringValue = dis.readUTF();
            boolean boolValue = dis.readBoolean();
            
            System.out.printf("Read structured data:%n");
            System.out.printf("  Integer: %d%n", intValue);
            System.out.printf("  Double: %.5f%n", doubleValue);
            System.out.printf("  String: %s%n", stringValue);
            System.out.printf("  Boolean: %b%n", boolValue);
        }
        
        System.out.println();
    }
    
    // ==================== CHARACTER STREAMS ====================
    
    public static void characterStreamOperations() throws IOException {
        System.out.println("3. CHARACTER STREAMS:");
        System.out.println("---------------------");
        
        File textFile = new File(SAMPLE_DIR, "text_data.txt");
        
        // Writing text with FileWriter
        try (FileWriter writer = new FileWriter(textFile)) {
            writer.write("Line 1: Hello World\n");
            writer.write("Line 2: Java File I/O\n");
            writer.write("Line 3: Character Streams\n");
            writer.write("Line 4: Unicode Support: café, naïve, résumé\n");
            
            System.out.println("Written text data to file");
        }
        
        // Reading text with FileReader
        try (FileReader reader = new FileReader(textFile)) {
            System.out.println("Reading character by character:");
            
            int charData;
            int count = 0;
            while ((charData = reader.read()) != -1 && count < 50) {
                char c = (char) charData;
                System.out.printf("%c", c);
                count++;
            }
            System.out.println("\n");
        }
        
        // PrintWriter for formatted output
        File formattedFile = new File(SAMPLE_DIR, "formatted_output.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(formattedFile))) {
            pw.println("=== EMPLOYEE REPORT ===");
            pw.printf("Name: %-15s Age: %3d%n", "John Doe", 30);
            pw.printf("Name: %-15s Age: %3d%n", "Jane Smith", 25);
            pw.printf("Name: %-15s Age: %3d%n", "Bob Johnson", 35);
            pw.println("========================");
            
            // Check for errors
            if (pw.checkError()) {
                System.out.println("Error occurred during writing");
            } else {
                System.out.println("Formatted output written successfully");
            }
        }
        
        // Scanner for reading formatted input
        try (Scanner scanner = new Scanner(textFile)) {
            System.out.println("Reading with Scanner:");
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("  " + line);
            }
        }
        
        System.out.println();
    }
    
    // ==================== BUFFERED I/O ====================
    
    public static void bufferedIOOperations() throws IOException {
        System.out.println("4. BUFFERED I/O:");
        System.out.println("----------------");
        
        File largeFile = new File(SAMPLE_DIR, "large_text.txt");
        
        // BufferedWriter for efficient writing
        long startTime = System.currentTimeMillis();
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(largeFile))) {
            for (int i = 1; i <= 10000; i++) {
                bw.write(String.format("Line %05d: This is a sample line with some text content%n", i));
            }
            bw.flush(); // Ensure all data is written
        }
        
        long writeTime = System.currentTimeMillis() - startTime;
        System.out.printf("Written 10,000 lines in %d ms%n", writeTime);
        
        // BufferedReader for efficient reading
        startTime = System.currentTimeMillis();
        int lineCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(largeFile))) {
            while (br.readLine() != null) {
                lineCount++;
            }
        }
        
        long readTime = System.currentTimeMillis() - startTime;
        System.out.printf("Read %d lines in %d ms%n", lineCount, readTime);
        
        // Demonstrate readline with processing
        try (BufferedReader br = new BufferedReader(new FileReader(largeFile))) {
            System.out.println("First 5 lines:");
            String line;
            int count = 0;
            while ((line = br.readLine()) != null && count < 5) {
                System.out.printf("  %s%n", line);
                count++;
            }
        }
        
        // File size information
        long fileSize = largeFile.length();
        System.out.printf("File size: %d bytes (%.2f KB)%n", fileSize, fileSize / 1024.0);
        
        System.out.println();
    }
    
    // ==================== NIO.2 OPERATIONS ====================
    
    public static void nioOperations() throws IOException {
        System.out.println("5. NIO.2 (NEW I/O):");
        System.out.println("-------------------");
        
        Path nioFile = Paths.get(SAMPLE_DIR, "nio_example.txt");
        
        // Writing with NIO.2
        List<String> lines = Arrays.asList(
            "NIO.2 File Operations",
            "Modern Java File I/O",
            "Path-based operations",
            "Atomic file operations"
        );
        
        Files.write(nioFile, lines, StandardCharsets.UTF_8);
        System.out.println("Written file using NIO.2");
        
        // Reading with NIO.2
        List<String> readLines = Files.readAllLines(nioFile, StandardCharsets.UTF_8);
        System.out.println("Read lines using NIO.2:");
        readLines.forEach(line -> System.out.println("  " + line));
        
        // File attributes
        System.out.printf("File size: %d bytes%n", Files.size(nioFile));
        System.out.printf("Is regular file: %b%n", Files.isRegularFile(nioFile));
        System.out.printf("Is readable: %b%n", Files.isReadable(nioFile));
        System.out.printf("Is writable: %b%n", Files.isWritable(nioFile));
        
        // File times
        System.out.printf("Last modified: %s%n", Files.getLastModifiedTime(nioFile));
        
        // Copy operations
        Path copyPath = Paths.get(SAMPLE_DIR, "nio_copy.txt");
        Files.copy(nioFile, copyPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File copied successfully");
        
        // Walking directory tree
        System.out.println("Directory contents:");
        try (Stream<Path> paths = Files.walk(Paths.get(SAMPLE_DIR))) {
            paths.filter(Files::isRegularFile)
                 .forEach(path -> System.out.println("  " + path.getFileName()));
        }
        
        // Reading file as Stream
        System.out.println("Reading file as stream:");
        try (Stream<String> stream = Files.lines(nioFile)) {
            stream.filter(line -> line.contains("NIO"))
                  .forEach(line -> System.out.println("  Found: " + line));
        }
        
        System.out.println();
    }
    
    // ==================== PRACTICAL APPLICATIONS ====================
    
    public static void practicalApplications() throws IOException {
        System.out.println("6. PRACTICAL APPLICATIONS:");
        System.out.println("--------------------------");
        
        // Configuration file manager
        ConfigFileManager configManager = new ConfigFileManager(SAMPLE_DIR);
        
        // Create sample configuration
        Properties config = new Properties();
        config.setProperty("database.url", "jdbc:mysql://localhost:3306/mydb");
        config.setProperty("database.username", "admin");
        config.setProperty("database.password", "secret");
        config.setProperty("app.debug", "true");
        config.setProperty("app.version", "1.0.0");
        
        configManager.saveConfiguration(config, "app.properties");
        System.out.println("Saved configuration file");
        
        // Load and display configuration
        Properties loadedConfig = configManager.loadConfiguration("app.properties");
        System.out.println("Loaded configuration:");
        loadedConfig.forEach((key, value) -> 
            System.out.printf("  %s = %s%n", key, value));
        
        // CSV file processor
        CSVFileProcessor csvProcessor = new CSVFileProcessor();
        
        // Create sample CSV data
        List<Employee> employees = Arrays.asList(
            new Employee(1, "John Doe", "Developer", 75000),
            new Employee(2, "Jane Smith", "Manager", 85000),
            new Employee(3, "Bob Johnson", "Designer", 65000),
            new Employee(4, "Alice Brown", "Analyst", 70000)
        );
        
        Path csvFile = Paths.get(SAMPLE_DIR, "employees.csv");
        csvProcessor.writeEmployeesToCSV(employees, csvFile);
        System.out.println("Created CSV file with employee data");
        
        // Read and process CSV
        List<Employee> loadedEmployees = csvProcessor.readEmployeesFromCSV(csvFile);
        System.out.println("Loaded employees from CSV:");
        loadedEmployees.forEach(emp -> System.out.printf("  %s%n", emp));
        
        // Log file analyzer
        LogFileAnalyzer logAnalyzer = new LogFileAnalyzer();
        
        // Create sample log file
        Path logFile = Paths.get(SAMPLE_DIR, "application.log");
        createSampleLogFile(logFile);
        
        // Analyze log file
        LogStatistics stats = logAnalyzer.analyzeLogFile(logFile);
        System.out.println("Log file analysis:");
        System.out.printf("  Total entries: %d%n", stats.getTotalEntries());
        System.out.printf("  Error entries: %d%n", stats.getErrorCount());
        System.out.printf("  Warning entries: %d%n", stats.getWarningCount());
        System.out.printf("  Info entries: %d%n", stats.getInfoCount());
        
        // File backup utility
        FileBackupUtility backupUtility = new FileBackupUtility();
        
        Path sourceDir = Paths.get(SAMPLE_DIR);
        Path backupDir = Paths.get(SAMPLE_DIR + "_backup");
        
        backupUtility.createBackup(sourceDir, backupDir);
        System.out.println("Created backup of files");
        
        System.out.println();
    }
    
    private static void createSampleLogFile(Path logFile) throws IOException {
        List<String> logEntries = Arrays.asList(
            "2023-12-25 10:00:00 INFO Application started",
            "2023-12-25 10:01:15 INFO User login: john.doe",
            "2023-12-25 10:05:30 WARN Database connection slow",
            "2023-12-25 10:10:45 ERROR Failed to process payment",
            "2023-12-25 10:15:20 INFO User logout: john.doe",
            "2023-12-25 10:20:10 ERROR Database connection lost",
            "2023-12-25 10:25:55 INFO Database reconnected",
            "2023-12-25 10:30:00 INFO Application shutdown"
        );
        
        Files.write(logFile, logEntries, StandardCharsets.UTF_8);
    }
}

// ==================== UTILITY CLASSES ====================

class ConfigFileManager {
    private final String baseDirectory;
    
    public ConfigFileManager(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
    
    public void saveConfiguration(Properties config, String filename) throws IOException {
        Path configPath = Paths.get(baseDirectory, filename);
        
        try (FileOutputStream fos = new FileOutputStream(configPath.toFile())) {
            config.store(fos, "Application Configuration");
        }
    }
    
    public Properties loadConfiguration(String filename) throws IOException {
        Path configPath = Paths.get(baseDirectory, filename);
        Properties config = new Properties();
        
        if (Files.exists(configPath)) {
            try (FileInputStream fis = new FileInputStream(configPath.toFile())) {
                config.load(fis);
            }
        }
        
        return config;
    }
}

class Employee {
    private final int id;
    private final String name;
    private final String department;
    private final double salary;
    
    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    
    @Override
    public String toString() {
        return String.format("Employee{id=%d, name='%s', department='%s', salary=%.2f}", 
                           id, name, department, salary);
    }
    
    public String toCSV() {
        return String.format("%d,\"%s\",\"%s\",%.2f", id, name, department, salary);
    }
    
    public static Employee fromCSV(String csvLine) {
        String[] parts = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        int id = Integer.parseInt(parts[0]);
        String name = parts[1].replaceAll("\"", "");
        String department = parts[2].replaceAll("\"", "");
        double salary = Double.parseDouble(parts[3]);
        
        return new Employee(id, name, department, salary);
    }
}

class CSVFileProcessor {
    
    public void writeEmployeesToCSV(List<Employee> employees, Path csvFile) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("ID,Name,Department,Salary"); // Header
        
        for (Employee emp : employees) {
            lines.add(emp.toCSV());
        }
        
        Files.write(csvFile, lines, StandardCharsets.UTF_8);
    }
    
    public List<Employee> readEmployeesFromCSV(Path csvFile) throws IOException {
        List<Employee> employees = new ArrayList<>();
        List<String> lines = Files.readAllLines(csvFile, StandardCharsets.UTF_8);
        
        // Skip header line
        for (int i = 1; i < lines.size(); i++) {
            try {
                Employee emp = Employee.fromCSV(lines.get(i));
                employees.add(emp);
            } catch (Exception e) {
                System.err.println("Error parsing line " + (i + 1) + ": " + e.getMessage());
            }
        }
        
        return employees;
    }
}

class LogFileAnalyzer {
    
    public LogStatistics analyzeLogFile(Path logFile) throws IOException {
        int totalEntries = 0;
        int errorCount = 0;
        int warningCount = 0;
        int infoCount = 0;
        
        try (Stream<String> lines = Files.lines(logFile)) {
            for (String line : (Iterable<String>) lines::iterator) {
                totalEntries++;
                
                if (line.contains(" ERROR ")) {
                    errorCount++;
                } else if (line.contains(" WARN ")) {
                    warningCount++;
                } else if (line.contains(" INFO ")) {
                    infoCount++;
                }
            }
        }
        
        return new LogStatistics(totalEntries, errorCount, warningCount, infoCount);
    }
}

class LogStatistics {
    private final int totalEntries;
    private final int errorCount;
    private final int warningCount;
    private final int infoCount;
    
    public LogStatistics(int totalEntries, int errorCount, int warningCount, int infoCount) {
        this.totalEntries = totalEntries;
        this.errorCount = errorCount;
        this.warningCount = warningCount;
        this.infoCount = infoCount;
    }
    
    public int getTotalEntries() { return totalEntries; }
    public int getErrorCount() { return errorCount; }
    public int getWarningCount() { return warningCount; }
    public int getInfoCount() { return infoCount; }
}

class FileBackupUtility {
    
    public void createBackup(Path sourceDir, Path backupDir) throws IOException {
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }
        
        try (Stream<Path> paths = Files.walk(sourceDir)) {
            paths.filter(Files::isRegularFile)
                 .forEach(source -> {
                     try {
                         Path target = backupDir.resolve(sourceDir.relativize(source));
                         Files.createDirectories(target.getParent());
                         Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                     } catch (IOException e) {
                         System.err.println("Failed to backup " + source + ": " + e.getMessage());
                     }
                 });
        }
    }
}