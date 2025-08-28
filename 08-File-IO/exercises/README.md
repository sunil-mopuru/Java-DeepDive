# File I/O - Practice Exercises

## 📝 Instructions
- Practice with different stream types (byte vs character)
- Learn proper resource management with try-with-resources
- Work with both traditional I/O and NIO.2
- Handle different file formats and encodings
- Focus on performance and error handling

---

## Exercise 1: File Manager Utility
**Difficulty: Beginner-Intermediate**

Create a comprehensive file management utility with various file operations.

### Requirements:
- **Basic Operations**: Copy, move, delete, rename files and directories
- **File Information**: Size, permissions, creation/modification dates
- **Directory Operations**: List contents, create directory trees
- **Search Functionality**: Find files by name, size, type, content
- **Batch Operations**: Process multiple files with patterns

### Features:
```java
FileManager manager = new FileManager();

// Basic operations
manager.copyFile("source.txt", "destination.txt");
manager.moveFile("old_location.txt", "new_location.txt");
manager.deleteFile("unwanted.txt");

// Directory operations
manager.createDirectoryTree("path/to/nested/dirs");
List<FileInfo> contents = manager.listDirectory(".", true); // recursive

// Search operations  
List<File> results = manager.findFiles("*.txt", "/documents");
List<File> largeFiles = manager.findFilesBySize(">1MB", "/downloads");
```

### Output Example:
```
=== FILE MANAGER REPORT ===
Operation: Directory Listing
Path: /documents
Files found: 25
Total size: 15.2 MB

Files:
  document1.txt       1.2 KB    2023-12-25 14:30
  presentation.pptx   2.5 MB    2023-12-24 09:15  
  spreadsheet.xlsx    856 KB    2023-12-23 16:45
  
Directories:
  projects/          (15 files, 8.3 MB)
  archives/          (8 files, 3.1 MB)
```

---

## Exercise 2: Data File Processor
**Difficulty: Intermediate**

Build a robust data file processor that can handle various file formats and perform transformations.

### Requirements:
- **CSV Processing**: Read, write, validate CSV files with custom delimiters
- **JSON Handling**: Parse and generate JSON files (basic implementation)
- **XML Processing**: Read and write simple XML structures  
- **Binary Files**: Handle custom binary formats with DataInputStream/DataOutputStream
- **Configuration Files**: Properties files, INI files, YAML-like formats

### Supported Operations:
```java
DataProcessor processor = new DataProcessor();

// CSV operations
List<Map<String, String>> csvData = processor.readCSV("employees.csv");
processor.writeCSV(csvData, "output.csv");

// Format conversion
processor.convertCSVToJSON("input.csv", "output.json");
processor.convertJSONToXML("input.json", "output.xml");

// Data validation and cleaning
ValidationReport report = processor.validateCSV("data.csv", schema);
CleanedData cleaned = processor.cleanData(csvData, cleaningRules);
```

### Example Data Processing:
```
Input CSV (employees.csv):
ID,Name,Email,Department,Salary
1,John Doe,john@company.com,IT,75000
2,Jane Smith,jane@company.com,HR,65000

Output JSON:
[
  {
    "id": "1",
    "name": "John Doe", 
    "email": "john@company.com",
    "department": "IT",
    "salary": "75000"
  },
  {
    "id": "2",
    "name": "Jane Smith",
    "email": "jane@company.com", 
    "department": "HR",
    "salary": "65000"
  }
]
```

---

## Exercise 3: Log File Processing System
**Difficulty: Intermediate-Advanced**

Create a sophisticated log file processing system that can handle large files efficiently.

### Requirements:
- **Large File Handling**: Process files larger than available RAM
- **Real-time Monitoring**: Watch files for changes and process new entries
- **Pattern Matching**: Extract structured data from unstructured logs
- **Aggregation**: Group and summarize log entries by various criteria
- **Output Generation**: Create reports in multiple formats

### Features:
```java
LogProcessor processor = new LogProcessor();

// Configure log format
processor.setLogFormat("${timestamp} ${level} [${thread}] ${logger}: ${message}");

// Process large files efficiently
processor.processLargeFile("application.log", 
    entry -> {
        if (entry.getLevel() == LogLevel.ERROR) {
            errorHandler.handle(entry);
        }
    });

// Real-time monitoring
processor.watchFile("live.log", 
    newEntry -> alertService.checkForAlerts(newEntry));

// Generate reports
LogReport report = processor.generateReport("logs/", 
    LocalDate.now().minusDays(7), LocalDate.now());
```

### Sample Log Analysis Output:
```
=== LOG ANALYSIS REPORT ===
Period: 2023-12-18 to 2023-12-25
Files processed: 7
Total entries: 156,423

Summary by Level:
  INFO:  98,234 (62.8%)
  WARN:  45,123 (28.8%) 
  ERROR: 12,456 (8.0%)
  DEBUG:    610 (0.4%)

Top Error Categories:
  DatabaseConnectionException: 3,456 occurrences
  NullPointerException: 2,134 occurrences
  TimeoutException: 1,876 occurrences

Performance Issues:
  ⚠️  High error rate on 2023-12-22 (15.2%)
  ⚠️  Response time spike at 14:30-15:00
```

---

## Exercise 4: Backup and Synchronization Tool
**Difficulty: Advanced**

Develop a backup and file synchronization tool with incremental backup capabilities.

### Requirements:
- **Incremental Backups**: Only backup changed files since last backup
- **Compression**: Compress backup archives to save space
- **Integrity Checking**: Verify backup integrity using checksums
- **Restoration**: Restore files from backups with version history
- **Scheduling**: Schedule automatic backups

### Features:
```java
BackupManager backup = new BackupManager();

// Configure backup
BackupConfig config = new BackupConfig()
    .setSourceDirectory("/documents")
    .setBackupDirectory("/backups")
    .setCompressionEnabled(true)
    .setIncrementalMode(true);

// Perform backup
BackupResult result = backup.performBackup(config);

// Restore operations
backup.listBackupVersions("/documents/important.txt");
backup.restoreFile("/documents/important.txt", "2023-12-20");
backup.restoreDirectory("/documents/projects", "2023-12-25");
```

### Backup Report Example:
```
=== BACKUP REPORT ===
Backup ID: BKP_20231225_143022
Type: Incremental
Source: /documents (1,234 files, 2.5 GB)
Destination: /backups/BKP_20231225_143022.zip

Files processed:
  New: 15 files (125 MB)
  Modified: 8 files (45 MB) 
  Deleted: 2 files
  Unchanged: 1,209 files (skipped)

Compression: 2.3 GB → 856 MB (62.8% reduction)
Duration: 2 minutes 34 seconds
Status: ✅ Completed successfully
```

---

## Exercise 5: File Format Converter
**Difficulty: Advanced**

Build a universal file format converter that can transform data between different file formats.

### Requirements:
- **Text Formats**: Convert between CSV, TSV, JSON, XML, YAML
- **Encoding Conversion**: Handle different character encodings (UTF-8, UTF-16, ASCII)
- **Binary Formats**: Custom binary serialization and deserialization
- **Schema Validation**: Validate data against predefined schemas
- **Batch Processing**: Convert multiple files with consistent formatting

### Converter Features:
```java
FormatConverter converter = new FormatConverter();

// Register custom formats
converter.registerFormat("csv", new CSVHandler());
converter.registerFormat("json", new JSONHandler()); 
converter.registerFormat("xml", new XMLHandler());

// Convert between formats
ConversionResult result = converter.convert(
    "employees.csv", "csv",
    "employees.json", "json"
);

// Batch conversion
BatchConversionJob job = new BatchConversionJob()
    .setInputDirectory("/data/csv")
    .setOutputDirectory("/data/json")
    .setInputFormat("csv")
    .setOutputFormat("json");
    
converter.executeBatch(job);
```

### Schema Validation Example:
```java
// Define schema for employee data
Schema employeeSchema = new Schema()
    .addField("id", FieldType.INTEGER, true)      // required
    .addField("name", FieldType.STRING, true)     // required
    .addField("email", FieldType.EMAIL, true)     // required, must be valid email
    .addField("salary", FieldType.DECIMAL, false) // optional
    .addConstraint("salary", value -> value >= 0); // salary must be non-negative

ValidationResult validation = converter.validate("employees.csv", employeeSchema);
```

---

## Exercise 6: Configuration Management System
**Difficulty: Intermediate-Advanced**

Create a flexible configuration management system that supports multiple file formats and environments.

### Requirements:
- **Multi-Format Support**: Properties, JSON, YAML, XML, INI files
- **Environment Profiles**: Development, testing, production configurations
- **Variable Substitution**: Support for environment variables and placeholders
- **Hot Reloading**: Detect and reload configuration changes at runtime
- **Validation**: Validate configuration against schemas

### Configuration Features:
```java
ConfigManager config = new ConfigManager();

// Load configurations with priority
config.addSource("application.properties")     // lowest priority
      .addSource("application-dev.properties") // medium priority  
      .addSource("system.env")                 // highest priority
      .enableHotReload();

// Access configuration values
String dbUrl = config.getString("database.url");
int maxConnections = config.getInt("database.maxConnections", 10); // with default
boolean debugEnabled = config.getBoolean("debug.enabled");

// Configuration with validation
ConfigSchema schema = new ConfigSchema()
    .requireString("database.url")
    .requireInt("database.maxConnections", 1, 100)
    .optionalBoolean("debug.enabled", false);

config.validateAgainst(schema);
```

### Environment Configuration Example:
```properties
# application.properties (base configuration)
database.url=jdbc:h2:mem:testdb
database.maxConnections=10
logging.level=INFO

# application-prod.properties (production overrides) 
database.url=jdbc:postgresql://prod-server:5432/myapp
database.maxConnections=50
logging.level=WARN
```

---

## 🎯 Challenge Projects

### Project A: File System Crawler
Build a multi-threaded file system crawler that:
- Indexes files and directories across multiple drives
- Extracts metadata (size, dates, file types, checksums)
- Creates searchable index with full-text search
- Monitors file system changes in real-time
- Generates file system reports and statistics

### Project B: Database Import/Export Tool
Create a tool that can:
- Export database tables to various file formats
- Import data from files into database tables
- Handle large datasets with streaming processing
- Validate data integrity during import/export
- Support multiple database systems (MySQL, PostgreSQL, SQLite)

### Project C: Document Processing Pipeline
Develop a document processing system that:
- Processes various document formats (PDF, Word, Excel)
- Extracts text content and metadata
- Performs content analysis and classification
- Generates searchable index
- Creates document summaries and reports

---

## 📚 Testing Guidelines

### Performance Testing:
- Test with large files (>1GB)
- Measure memory usage during processing
- Test concurrent file access scenarios
- Monitor file handle leaks

### Error Handling:
- File not found scenarios
- Permission denied errors
- Disk space limitations
- Network drive disconnections
- Corrupted file handling

### Platform Compatibility:
- Test on different operating systems
- Handle different file path separators
- Test with various file systems (NTFS, ext4, HFS+)
- Unicode filename support

### Resource Management:
- Verify all streams are properly closed
- Test with try-with-resources patterns
- Monitor system resource usage
- Test cleanup in error scenarios

---

**Next:** [Multithreading Exercises](../../09-Multithreading/exercises/)
