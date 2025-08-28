# String Handling - Practice Exercises

## 📝 Instructions
- Focus on string manipulation, regular expressions, and text processing
- Practice with StringBuilder/StringBuffer for performance
- Work with pattern matching and validation
- Handle different character encodings and formats

---

## Exercise 1: Text Analyzer Pro
**Difficulty: Beginner-Intermediate**

Create a comprehensive text analysis tool that processes text files and provides detailed statistics.

### Requirements:
- **Word Analysis**: Count words, characters, sentences, paragraphs
- **Reading Level**: Calculate Flesch-Kincaid readability score
- **Frequency Analysis**: Most/least common words, word length distribution
- **Pattern Detection**: Find email addresses, phone numbers, URLs
- **Language Features**: Detect repeated words, longest/shortest sentences

### Input/Output:
```
Input: "sample_text.txt"
Output:
=== TEXT ANALYSIS REPORT ===
File: sample_text.txt
Characters: 1,245 (1,089 without spaces)
Words: 234
Sentences: 18
Paragraphs: 5
Average sentence length: 13.0 words
Readability score: 65.2 (Standard)

Most common words:
  the: 15 occurrences
  and: 12 occurrences
  in: 9 occurrences

Contact information found:
  Emails: john@example.com, support@company.org
  Phone numbers: (555) 123-4567, 555-987-6543
  URLs: https://www.example.com
```

### Bonus Features:
- Support multiple file formats (txt, csv, basic HTML)
- Generate word clouds data (word -> frequency mapping)
- Detect potential plagiarism by comparing with other texts

---

## Exercise 2: Advanced String Processor
**Difficulty: Intermediate**

Build a string processing utility with various transformation and validation capabilities.

### Requirements:
- **Case Conversions**: camelCase, PascalCase, snake_case, kebab-case
- **Text Cleaning**: Remove extra spaces, normalize punctuation
- **Format Validation**: Email, phone, credit card, SSN validation
- **Text Generation**: Lorem ipsum generator, password generator
- **Encoding Handling**: Base64 encoding/decoding, URL encoding

### Example Usage:
```java
StringProcessor processor = new StringProcessor();

// Case conversions
processor.toCamelCase("hello world test"); // "helloWorldTest"
processor.toSnakeCase("HelloWorldTest");   // "hello_world_test"

// Validation
processor.isValidEmail("user@domain.com"); // true
processor.isValidPhone("(555) 123-4567");  // true

// Text cleaning
processor.cleanText("  Too    many   spaces!  "); // "Too many spaces!"

// Generation
processor.generatePassword(12, true, true, true); // "K9m#nP2x$wQ8"
processor.generateLoremIpsum(50); // 50 words of Lorem Ipsum
```

---

## Exercise 3: CSV/JSON Data Processor
**Difficulty: Intermediate-Advanced**

Create a robust data processor that can parse, validate, and transform CSV and JSON-like data.

### Requirements:
- **CSV Parser**: Handle quoted fields, escaped characters, different delimiters
- **JSON Parser**: Parse simple JSON structures (objects and arrays)
- **Data Validation**: Type checking, required fields, format validation
- **Data Transformation**: Convert between CSV and JSON formats
- **Error Handling**: Detailed error reporting with line numbers

### Features:
```java
CSVProcessor csv = new CSVProcessor();
List<Map<String, String>> records = csv.parseCSV("employees.csv");

JSONProcessor json = new JSONProcessor();
String jsonString = json.convertToJSON(records);

DataValidator validator = new DataValidator();
validator.addRule("email", ValidationRule.EMAIL);
validator.addRule("age", ValidationRule.INTEGER_RANGE, 18, 100);
List<ValidationError> errors = validator.validate(records);
```

---

## Exercise 4: Log File Analyzer
**Difficulty: Advanced**

Build a sophisticated log file analysis tool that can process various log formats and extract meaningful insights.

### Requirements:
- **Multi-Format Support**: Apache access logs, application logs, system logs
- **Pattern Extraction**: IP addresses, timestamps, error codes, user agents
- **Statistical Analysis**: Request frequency, error rates, response times
- **Alert Generation**: Detect suspicious patterns, performance issues
- **Report Generation**: Generate summary reports in multiple formats

### Log Format Examples:
```
Apache Access Log:
192.168.1.1 - - [25/Dec/2023:10:00:23 +0000] "GET /index.html HTTP/1.1" 200 1234

Application Log:
2023-12-25 10:00:23 ERROR [UserService] Failed login attempt for user: john@example.com

System Log:
Dec 25 10:00:23 server01 kernel: Out of memory: Kill process 1234 (java)
```

### Expected Output:
```
=== LOG ANALYSIS REPORT ===
Analysis Period: 2023-12-25 00:00:00 to 2023-12-25 23:59:59
Total Entries: 10,456

TOP STATISTICS:
Most active IPs:
  192.168.1.100: 1,234 requests
  10.0.0.5: 987 requests

Error Analysis:
  404 Not Found: 45 occurrences
  500 Internal Error: 12 occurrences
  403 Forbidden: 8 occurrences

Security Alerts:
  ⚠️  Suspicious login attempts from 192.168.1.50 (25 failed attempts)
  ⚠️  High error rate detected between 14:00-15:00 (15% error rate)
```

---

## Exercise 5: Template Engine
**Difficulty: Advanced**

Create a simple template engine that can process text templates with variables and basic control structures.

### Requirements:
- **Variable Substitution**: Replace {{variable}} placeholders
- **Conditional Blocks**: {{#if condition}} ... {{/if}}
- **Loops**: {{#each items}} ... {{/each}}
- **Functions**: Built-in functions like {{date}}, {{format}}
- **Error Handling**: Meaningful error messages for template syntax errors

### Template Example:
```html
<!DOCTYPE html>
<html>
<head><title>{{title}}</title></head>
<body>
    <h1>Welcome {{user.name}}!</h1>
    
    {{#if user.isAdmin}}
        <p>Admin Panel: <a href="/admin">Manage System</a></p>
    {{/if}}
    
    <h2>Recent Orders:</h2>
    <ul>
    {{#each orders}}
        <li>Order #{{id}} - {{format amount "currency"}} on {{format date "short"}}</li>
    {{/each}}
    </ul>
    
    <p>Generated on: {{date "yyyy-MM-dd HH:mm:ss"}}</p>
</body>
</html>
```

### Usage:
```java
TemplateEngine engine = new TemplateEngine();

Map<String, Object> context = new HashMap<>();
context.put("title", "Dashboard");
context.put("user", Map.of("name", "John Doe", "isAdmin", true));
context.put("orders", Arrays.asList(
    Map.of("id", "1001", "amount", 29.99, "date", LocalDate.now()),
    Map.of("id", "1002", "amount", 15.50, "date", LocalDate.now().minusDays(1))
));

String result = engine.process(template, context);
```

---

## Exercise 6: Regular Expression Toolkit
**Difficulty: Intermediate-Advanced**

Build a comprehensive toolkit for working with regular expressions and pattern matching.

### Requirements:
- **Pattern Library**: Pre-built patterns for common validations
- **Pattern Tester**: Interactive regex testing with match highlighting
- **Text Extractor**: Extract data using named capture groups
- **Batch Processor**: Apply patterns to multiple files
- **Performance Analyzer**: Measure regex performance and optimization

### Features:
```java
RegexToolkit toolkit = new RegexToolkit();

// Pre-built patterns
Pattern emailPattern = toolkit.getPattern("email");
Pattern phonePattern = toolkit.getPattern("phone");
Pattern urlPattern = toolkit.getPattern("url");

// Advanced extraction
Map<String, List<String>> extracted = toolkit.extractAll(
    "Contact us at support@company.com or call (555) 123-4567",
    Arrays.asList("email", "phone")
);

// Pattern testing
RegexTester tester = new RegexTester();
tester.setPattern("\\b\\w+@\\w+\\.\\w+\\b");
tester.setTestString("Email me at john@example.com");
TestResult result = tester.test(); // Shows matches, groups, positions
```

---

## 🎯 Challenge Projects

### Project A: Markdown to HTML Converter
Create a converter that transforms Markdown syntax to HTML:
- Headers (#, ##, ###)
- Bold (**text**) and italic (*text*)
- Links [text](url) and images ![alt](url)
- Code blocks and inline code
- Lists (ordered and unordered)

### Project B: Simple Compiler/Interpreter
Build a basic interpreter for a simple expression language:
- Variables and assignments
- Mathematical operations (+, -, *, /, %)
- String operations (concatenation, substring)
- Conditional expressions
- Function calls

### Project C: Data Migration Tool
Create a tool that converts data between different formats:
- CSV ↔ JSON ↔ XML ↔ YAML
- Schema validation and transformation
- Data cleansing and normalization
- Batch processing capabilities

---

## 📚 Testing Guidelines

### Input Validation Testing:
- Test with empty strings, null values
- Test with very long strings (performance)
- Test with special characters and Unicode
- Test with malformed input data

### Edge Cases:
- Memory efficiency with large text files
- Performance with complex regular expressions
- Handling different character encodings
- Processing streaming data

### Error Scenarios:
- File reading permissions
- Invalid regex patterns
- Malformed data formats
- Out of memory conditions

---

**Next:** [File I/O Exercises](../../08-File-IO/exercises/)