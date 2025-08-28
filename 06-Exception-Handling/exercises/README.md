# Exception Handling - Practice Exercises

## 📝 Instructions
- Create custom exception hierarchies for specific domains
- Practice proper resource management with try-with-resources
- Implement comprehensive error handling strategies
- Focus on graceful degradation and recovery mechanisms
- Learn exception chaining and meaningful error messages

---

## Exercise 1: Banking System Exception Hierarchy
**Difficulty: Beginner-Intermediate**

Create a comprehensive exception system for a banking application with proper inheritance and error handling.

### Requirements:
- **Custom Exception Hierarchy**: Create domain-specific exceptions
- **Exception Chaining**: Link related exceptions together
- **Error Codes**: Implement standardized error codes
- **Recovery Mechanisms**: Handle recoverable vs non-recoverable errors
- **Audit Logging**: Log all exceptions with context information

### Exception Hierarchy:
```java
// Base banking exception
public abstract class BankingException extends Exception {
    private final String errorCode;
    private final String userMessage;
    private final boolean recoverable;
    
    protected BankingException(String errorCode, String message, 
                             String userMessage, boolean recoverable) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.recoverable = recoverable;
    }
    
    protected BankingException(String errorCode, String message,
                             String userMessage, boolean recoverable, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
        this.recoverable = recoverable;
    }
    
    // Getters...
}

// Account-related exceptions
public class AccountException extends BankingException {
    private final String accountNumber;
    
    public AccountException(String errorCode, String message, 
                          String userMessage, String accountNumber) {
        super(errorCode, message, userMessage, false);
        this.accountNumber = accountNumber;
    }
}

public class InsufficientFundsException extends AccountException {
    private final double requestedAmount;
    private final double availableBalance;
    
    public InsufficientFundsException(String accountNumber, 
                                    double requestedAmount, double availableBalance) {
        super("ACC_001", 
              String.format("Insufficient funds in account %s. Requested: %.2f, Available: %.2f", 
                           accountNumber, requestedAmount, availableBalance),
              "Insufficient funds for this transaction",
              accountNumber);
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }
}

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException(String accountNumber) {
        super("ACC_002",
              "Account not found: " + accountNumber,
              "The specified account could not be found",
              accountNumber);
    }
}

// Transaction-related exceptions
public class TransactionException extends BankingException {
    private final String transactionId;
    
    protected TransactionException(String errorCode, String message,
                                 String userMessage, String transactionId, 
                                 boolean recoverable) {
        super(errorCode, message, userMessage, recoverable);
        this.transactionId = transactionId;
    }
}

public class DuplicateTransactionException extends TransactionException {
    public DuplicateTransactionException(String transactionId) {
        super("TXN_001",
              "Duplicate transaction detected: " + transactionId,
              "This transaction has already been processed",
              transactionId,
              false);
    }
}
```

### Usage Example:
```java
public class BankingService {
    public void transfer(String fromAccount, String toAccount, double amount) 
            throws BankingException {
        try {
            Account from = getAccount(fromAccount);
            Account to = getAccount(toAccount);
            
            if (from.getBalance() < amount) {
                throw new InsufficientFundsException(fromAccount, amount, from.getBalance());
            }
            
            // Perform transfer
            from.withdraw(amount);
            to.deposit(amount);
            
        } catch (DatabaseException e) {
            throw new TransactionException("TXN_002", 
                "Database error during transfer", 
                "Transaction temporarily unavailable", 
                UUID.randomUUID().toString(), 
                true, e);
        }
    }
}
```

---

## Exercise 2: Robust File Processing System
**Difficulty: Intermediate**

Build a comprehensive file processing system that handles various I/O exceptions and provides recovery mechanisms.

### Requirements:
- **Multiple File Operations**: Read, write, copy, move, delete
- **Exception Recovery**: Retry mechanisms for temporary failures
- **Resource Management**: Proper cleanup using try-with-resources
- **Batch Processing**: Handle partial failures in batch operations
- **Progress Reporting**: Report progress and handle interruptions

### Implementation:
```java
public class RobustFileProcessor {
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 1000;
    
    public ProcessingResult processFiles(List<File> files, FileOperation operation) {
        List<FileProcessingError> errors = new ArrayList<>();
        List<File> successfullyProcessed = new ArrayList<>();
        
        for (File file : files) {
            try {
                processFileWithRetry(file, operation);
                successfullyProcessed.add(file);
            } catch (FileProcessingException e) {
                errors.add(new FileProcessingError(file, e));
                if (!e.isContinueOnError()) {
                    break; // Stop processing if critical error
                }
            }
        }
        
        return new ProcessingResult(successfullyProcessed, errors);
    }
    
    private void processFileWithRetry(File file, FileOperation operation) 
            throws FileProcessingException {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                operation.execute(file);
                return; // Success
            } catch (IOException e) {
                lastException = e;
                attempts++;
                
                if (isRetryableException(e) && attempts < MAX_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempts);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new FileProcessingException("Processing interrupted", ie, false);
                    }
                } else {
                    break;
                }
            }
        }
        
        throw new FileProcessingException(
            String.format("Failed to process file %s after %d attempts", 
                         file.getName(), attempts),
            lastException,
            true);
    }
    
    private boolean isRetryableException(IOException e) {
        return e instanceof FileNotFoundException == false &&
               e instanceof AccessDeniedException == false;
    }
}

// File operation interface
public interface FileOperation {
    void execute(File file) throws IOException;
}

// Example implementations
public class FileCopyOperation implements FileOperation {
    private final Path destinationDir;
    
    public FileCopyOperation(Path destinationDir) {
        this.destinationDir = destinationDir;
    }
    
    @Override
    public void execute(File file) throws IOException {
        Path source = file.toPath();
        Path target = destinationDir.resolve(file.getName());
        
        try (InputStream in = Files.newInputStream(source);
             OutputStream out = Files.newOutputStream(target)) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
```

---

## Exercise 3: Input Validation Framework
**Difficulty: Intermediate-Advanced**

Create a comprehensive validation framework that throws specific exceptions for different validation failures.

### Requirements:
- **Validation Rules**: Email, phone, age, password strength validation
- **Custom Validators**: Allow custom validation logic
- **Batch Validation**: Validate multiple fields and collect all errors
- **Internationalization**: Support multiple languages for error messages
- **Performance**: Efficient validation with caching

### Framework Design:
```java
// Base validation exception
public class ValidationException extends Exception {
    private final String fieldName;
    private final Object invalidValue;
    private final String validationRule;
    
    public ValidationException(String fieldName, Object invalidValue, 
                             String validationRule, String message) {
        super(message);
        this.fieldName = fieldName;
        this.invalidValue = invalidValue;
        this.validationRule = validationRule;
    }
}

// Specific validation exceptions
public class EmailValidationException extends ValidationException {
    public EmailValidationException(String email) {
        super("email", email, "EMAIL_FORMAT", 
              "Invalid email format: " + email);
    }
}

public class PasswordValidationException extends ValidationException {
    private final List<PasswordRequirement> failedRequirements;
    
    public PasswordValidationException(String password, 
                                     List<PasswordRequirement> failedRequirements) {
        super("password", "[HIDDEN]", "PASSWORD_STRENGTH",
              "Password does not meet requirements: " + 
              failedRequirements.stream()
                  .map(PasswordRequirement::getDescription)
                  .collect(Collectors.joining(", ")));
        this.failedRequirements = failedRequirements;
    }
}

// Validation framework
public class Validator {
    private final Map<String, List<ValidationRule>> rules = new HashMap<>();
    private final MessageResolver messageResolver;
    
    public Validator(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }
    
    public void addRule(String fieldName, ValidationRule rule) {
        rules.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(rule);
    }
    
    public void validate(Map<String, Object> data) throws ValidationException {
        List<ValidationException> errors = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();
            
            List<ValidationRule> fieldRules = rules.get(fieldName);
            if (fieldRules != null) {
                for (ValidationRule rule : fieldRules) {
                    try {
                        rule.validate(fieldName, value);
                    } catch (ValidationException e) {
                        errors.add(e);
                    }
                }
            }
        }
        
        if (!errors.isEmpty()) {
            throw new MultipleValidationException(errors);
        }
    }
}

// Validation rules
public interface ValidationRule {
    void validate(String fieldName, Object value) throws ValidationException;
}

public class EmailValidationRule implements ValidationRule {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    @Override
    public void validate(String fieldName, Object value) throws ValidationException {
        if (value != null && !EMAIL_PATTERN.matcher(value.toString()).matches()) {
            throw new EmailValidationException(value.toString());
        }
    }
}
```

---

## Exercise 4: Exception Logging and Monitoring System
**Difficulty: Advanced**

Implement a comprehensive exception logging and monitoring system with alerting capabilities.

### Requirements:
- **Structured Logging**: Log exceptions with context and metadata
- **Error Categorization**: Classify errors by severity and type
- **Alerting**: Send alerts for critical errors or error rate spikes
- **Metrics Collection**: Track error rates, response times, recovery success
- **Dashboard Integration**: Provide data for monitoring dashboards

### Implementation:
```java
public class ExceptionMonitor {
    private final Logger logger = LoggerFactory.getLogger(ExceptionMonitor.class);
    private final MetricsCollector metricsCollector;
    private final AlertManager alertManager;
    private final ErrorClassifier errorClassifier;
    
    public void recordException(Exception exception, String context, 
                              Map<String, Object> metadata) {
        ErrorInfo errorInfo = createErrorInfo(exception, context, metadata);
        
        // Log the exception
        logException(errorInfo);
        
        // Update metrics
        updateMetrics(errorInfo);
        
        // Check for alerting conditions
        checkAlertConditions(errorInfo);
    }
    
    private ErrorInfo createErrorInfo(Exception exception, String context,
                                    Map<String, Object> metadata) {
        return ErrorInfo.builder()
            .timestamp(Instant.now())
            .exceptionClass(exception.getClass().getSimpleName())
            .message(exception.getMessage())
            .stackTrace(getStackTrace(exception))
            .context(context)
            .severity(errorClassifier.classifySeverity(exception))
            .category(errorClassifier.classifyCategory(exception))
            .metadata(metadata)
            .correlationId(generateCorrelationId())
            .build();
    }
    
    private void updateMetrics(ErrorInfo errorInfo) {
        metricsCollector.incrementErrorCount(errorInfo.getCategory());
        metricsCollector.recordErrorRate(errorInfo.getSeverity());
        
        if (errorInfo.getMetadata().containsKey("responseTime")) {
            long responseTime = (Long) errorInfo.getMetadata().get("responseTime");
            metricsCollector.recordResponseTime(responseTime);
        }
    }
}
```

---

## Exercise 5: API Error Handling Framework
**Difficulty: Advanced**

Design a REST API error handling framework with proper HTTP status codes and standardized error responses.

### Requirements:
- **HTTP Status Mapping**: Map exceptions to appropriate HTTP status codes
- **Error Response Format**: Standardized JSON error response structure
- **Client-Friendly Messages**: Sanitize internal errors for external clients
- **Error Documentation**: Generate API documentation for error responses
- **Monitoring Integration**: Integration with monitoring and alerting systems

### Framework Implementation:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        ErrorResponse error = ErrorResponse.builder()
            .code("VALIDATION_FAILED")
            .message("Request validation failed")
            .details(e.getValidationErrors())
            .timestamp(Instant.now())
            .build();
            
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(InsufficientFundsException e) {
        ErrorResponse error = ErrorResponse.builder()
            .code("INSUFFICIENT_FUNDS")
            .message("Transaction cannot be completed due to insufficient funds")
            .details(Map.of(
                "availableBalance", e.getAvailableBalance(),
                "requestedAmount", e.getRequestedAmount()))
            .timestamp(Instant.now())
            .build();
            
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(error);
    }
}

// Standardized error response
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, Object> details;
    private Instant timestamp;
    private String traceId;
    
    // Builder pattern and getters...
}
```

---

## 🎯 Challenge Projects

### Project A: Distributed Exception Handling
Build an exception handling system for microservices:
- Cross-service error correlation
- Centralized error aggregation
- Distributed tracing integration
- Circuit breaker pattern implementation

### Project B: Self-Healing Application Framework
Create a framework that automatically recovers from common failures:
- Automatic retry with backoff strategies
- Fallback mechanism implementation
- Resource pool recovery
- Configuration hot-reloading

---

## 📚 Testing Guidelines

### Exception Testing:
- Test both expected and unexpected exception scenarios
- Verify exception messages are meaningful and actionable
- Test exception chaining preserves original cause
- Validate resource cleanup in finally blocks

### Recovery Testing:
- Test retry mechanisms under various failure conditions
- Verify fallback behaviors work correctly
- Test system behavior under cascading failures
- Validate monitoring and alerting trigger correctly

---

**Next:** [String Handling Exercises](../../07-String-Handling/exercises/)