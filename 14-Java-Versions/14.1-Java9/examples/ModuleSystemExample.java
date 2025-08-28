/**
 * Java 9 Module System Example
 * 
 * This example demonstrates the basic concepts of the Java Module System
 * introduced in Java 9. In a real project, this would be split across
 * multiple modules with separate module-info.java files.
 */

// Example module-info.java content for a typical application module
/*
module com.example.userservice {
    // Dependencies on other modules
    requires java.base;           // Implicit - always required
    requires java.logging;        // For logging functionality
    requires java.net.http;      // For HTTP client (Java 11+)
    requires transitive java.sql; // Transitive - anyone requiring this module gets java.sql too
    
    // What this module exposes to other modules
    exports com.example.userservice.api;
    exports com.example.userservice.model;
    
    // Conditional exports (only to specific modules)
    exports com.example.userservice.internal to com.example.userservice.test;
    
    // Services this module provides
    provides com.example.userservice.api.UserService 
        with com.example.userservice.impl.DatabaseUserService,
             com.example.userservice.impl.CacheUserService;
    
    // Services this module consumes
    uses com.example.logging.Logger;
    uses com.example.database.ConnectionProvider;
    
    // Reflection access (if needed)
    opens com.example.userservice.model to java.base;
}
*/

public class ModuleSystemExample {
    
    public static void main(String[] args) {
        System.out.println("=== Java 9 Module System Concepts ===");
        
        explainModuleBasics();
        demonstrateModularStructure();
        showModuleCommands();
        explainBenefits();
    }
    
    public static void explainModuleBasics() {
        System.out.println("\n1. Module System Basics");
        System.out.println("=======================");
        
        System.out.println("A module is a named, self-describing collection of code and data.");
        System.out.println("Key components:");
        System.out.println("• module-info.java - Module descriptor");
        System.out.println("• requires - Dependencies on other modules");
        System.out.println("• exports - Packages visible to other modules");
        System.out.println("• provides/uses - Service provider interface");
        System.out.println("• opens - Reflection access");
        
        // Demonstrate accessing module information at runtime
        Module currentModule = ModuleSystemExample.class.getModule();
        System.out.println("\nCurrent module info:");
        System.out.println("Module name: " + currentModule.getName());
        System.out.println("Is named: " + currentModule.isNamed());
        System.out.println("Descriptor: " + currentModule.getDescriptor());
    }
    
    public static void demonstrateModularStructure() {
        System.out.println("\n2. Typical Modular Application Structure");
        System.out.println("=======================================");
        
        System.out.println("""
            Example multi-module project structure:
            
            my-app/
            ├── app-core/
            │   ├── src/main/java/
            │   │   ├── module-info.java
            │   │   └── com/example/core/
            │   └── target/classes/
            ├── app-api/
            │   ├── src/main/java/
            │   │   ├── module-info.java
            │   │   └── com/example/api/
            │   └── target/classes/
            ├── app-impl/
            │   ├── src/main/java/
            │   │   ├── module-info.java
            │   │   └── com/example/impl/
            │   └── target/classes/
            └── app-main/
                ├── src/main/java/
                │   ├── module-info.java
                │   └── com/example/main/
                └── target/classes/
            """);
        
        // Show example module relationships
        System.out.println("Module dependencies:");
        System.out.println("app-main → app-core → app-api");
        System.out.println("app-main → app-impl → app-api");
        System.out.println("app-core ↔ app-impl (both use app-api)");
    }
    
    public static void showModuleCommands() {
        System.out.println("\n3. Module System Commands");
        System.out.println("=========================");
        
        System.out.println("Compilation commands:");
        System.out.println("javac --module-path mods -d mods/app.core src/app.core/module-info.java src/app.core/com/example/core/*.java");
        
        System.out.println("\nRunning modular applications:");
        System.out.println("java --module-path mods --module app.main/com.example.main.Main");
        
        System.out.println("\nCreating modular JARs:");
        System.out.println("jar --create --file mods/app.core.jar -C mods/app.core .");
        
        System.out.println("\nListing module dependencies:");
        System.out.println("java --module-path mods --list-modules");
        System.out.println("java --module-path mods --describe-module app.core");
        
        System.out.println("\nCreating custom runtime image:");
        System.out.println("jlink --module-path mods:$JAVA_HOME/jmods --add-modules app.main --output myapp-runtime");
    }
    
    public static void explainBenefits() {
        System.out.println("\n4. Module System Benefits");
        System.out.println("=========================");
        
        System.out.println("✅ Strong Encapsulation:");
        System.out.println("   • Only exported packages are accessible");
        System.out.println("   • Internal implementation details are hidden");
        
        System.out.println("\n✅ Reliable Configuration:");
        System.out.println("   • Missing dependencies detected at startup");
        System.out.println("   • No more ClassNotFoundException at runtime");
        
        System.out.println("\n✅ Scalable Development:");
        System.out.println("   • Clear module boundaries");
        System.out.println("   • Easier team collaboration");
        System.out.println("   • Better separation of concerns");
        
        System.out.println("\n✅ Smaller Runtime:");
        System.out.println("   • Include only needed modules");
        System.out.println("   • Create custom runtime images with jlink");
        System.out.println("   • Reduced memory footprint");
        
        System.out.println("\n✅ Better Security:");
        System.out.println("   • Access control at module level");
        System.out.println("   • Prevent unauthorized access to internals");
        
        // Demonstrate module visibility
        demonstrateEncapsulation();
    }
    
    private static void demonstrateEncapsulation() {
        System.out.println("\n5. Encapsulation Example");
        System.out.println("========================");
        
        // In a modular system, this would demonstrate how internal
        // classes are not accessible from other modules
        
        System.out.println("Without modules (traditional JAR):");
        System.out.println("• All public classes are accessible");
        System.out.println("• Internal utilities can be misused");
        System.out.println("• Tight coupling between components");
        
        System.out.println("\nWith modules:");
        System.out.println("• Only exported packages are accessible");
        System.out.println("• Internal utilities remain hidden");
        System.out.println("• Loose coupling enforced by module system");
        
        // Example of what would be hidden vs. exposed
        PublicAPI publicAPI = new PublicAPI();
        publicAPI.performOperation();
        
        // This would NOT be accessible from another module:
        // InternalUtility utility = new InternalUtility(); // Compilation error
        
        System.out.println("✓ Public API accessible");
        System.out.println("✗ Internal utilities hidden from other modules");
    }
}

// This would be in an exported package
class PublicAPI {
    public void performOperation() {
        System.out.println("Performing public operation using internal utilities");
        // Can use internal utilities within the same module
        InternalUtility.doInternalWork();
    }
}

// This would be in a non-exported (internal) package
class InternalUtility {
    // This class would not be accessible from other modules
    static void doInternalWork() {
        System.out.println("Doing internal work - hidden from other modules");
    }
    
    // Internal helper methods
    private static void secretAlgorithm() {
        System.out.println("Secret algorithm implementation");
    }
}

// Service Provider Interface example
interface DatabaseService {
    void connect();
    void disconnect();
    void executeQuery(String query);
}

// Implementation that would be provided via 'provides...with' directive
class PostgreSQLService implements DatabaseService {
    @Override
    public void connect() {
        System.out.println("Connecting to PostgreSQL database");
    }
    
    @Override
    public void disconnect() {
        System.out.println("Disconnecting from PostgreSQL database");
    }
    
    @Override
    public void executeQuery(String query) {
        System.out.println("Executing query: " + query);
    }
}