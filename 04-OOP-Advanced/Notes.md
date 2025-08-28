# Advanced OOP - Quick Reference Notes

## 🧬 Inheritance

### Syntax
```java
public class Parent {
    protected int value;
    public void method() { }
}

public class Child extends Parent {
    @Override
    public void method() {
        super.method(); // Call parent
    }
}
```

### Key Points
- Use `extends` keyword
- Single inheritance only
- `protected` members accessible in subclasses
- Constructor chaining with `super()`

## 🎭 Polymorphism

### Runtime Polymorphism
```java
Animal[] animals = {
    new Dog("Rex"),
    new Cat("Whiskers")
};

for (Animal animal : animals) {
    animal.makeSound(); // Different sounds
}
```

### Method Overriding Rules
- Same signature as parent method
- Cannot reduce visibility
- Use `@Override` annotation
- Cannot override `final` methods

## 🏗️ Abstract Classes

### Syntax
```java
public abstract class Shape {
    protected String color;
    
    // Concrete method
    public void setColor(String color) {
        this.color = color;
    }
    
    // Abstract method
    public abstract double getArea();
}
```

### Key Points
- Cannot be instantiated
- Can have concrete and abstract methods
- Use when sharing common implementation

## 🔌 Interfaces

### Modern Interface
```java
public interface Drawable {
    // Constant (public static final)
    double MAX_SIZE = 1000.0;
    
    // Abstract method (public abstract)
    void draw();
    
    // Default method (Java 8+)
    default void highlight() {
        System.out.println("Highlighting...");
    }
    
    // Static method (Java 8+)
    static void reset() {
        System.out.println("Reset complete");
    }
}
```

### Implementation
```java
public class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}
```

## 🔐 Encapsulation Advanced

### Immutable Objects
```java
public final class ImmutablePerson {
    private final String name;
    private final List<String> hobbies;
    
    public ImmutablePerson(String name, List<String> hobbies) {
        this.name = name;
        this.hobbies = new ArrayList<>(hobbies); // Defensive copy
    }
    
    public List<String> getHobbies() {
        return new ArrayList<>(hobbies); // Return copy
    }
}
```

## 🏗️ Builder Pattern
```java
public class Computer {
    private final String cpu;
    private final int ram;
    
    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
    }
    
    public static class Builder {
        private String cpu;
        private int ram;
        
        public Builder setCpu(String cpu) {
            this.cpu = cpu;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
}
```

## ⬆️ The `super` Keyword

### Uses
```java
public class Child extends Parent {
    public Child(String name) {
        super(name); // Call parent constructor
    }
    
    @Override
    public void method() {
        super.method(); // Call parent method
        // Additional child logic
    }
    
    public void accessParentField() {
        super.parentField; // Access parent field
    }
}
```

## 🔒 Final Keyword

### Applications
```java
// Final class - cannot be extended
public final class Utility { }

// Final method - cannot be overridden
public final void criticalMethod() { }

// Final variable - cannot be reassigned
public final int CONSTANT = 100;
private final List<String> list = new ArrayList<>();
```

## 🆚 Interface vs Abstract Class

| Feature | Interface | Abstract Class |
|---------|-----------|----------------|
| Multiple inheritance | ✓ | ✗ |
| Constructor | ✗ | ✓ |
| Instance fields | ✗ | ✓ |
| Method implementation | Default/Static | All types |
| Access modifiers | Public only | All |

## 🎯 When to Use What

### Use Interface When:
- Defining a contract
- Multiple inheritance needed
- Unrelated classes share behavior
- Future flexibility important

### Use Abstract Class When:
- Sharing code among related classes
- Need constructors or instance fields
- Want to provide default implementation
- Control access with different modifiers

## ⚠️ Common Mistakes

### 1. Not Using @Override
```java
// Wrong - typo won't be caught
public void tostring() { } 

// Correct
@Override
public String toString() { }
```

### 2. Breaking Encapsulation
```java
// Wrong - exposing mutable reference
public List<String> getItems() {
    return items; // Direct reference
}

// Correct - defensive copy
public List<String> getItems() {
    return new ArrayList<>(items);
}
```

### 3. Improper Inheritance
```java
// Wrong - violates IS-A relationship
public class Square extends Rectangle { }

// Better - composition
public class Square {
    private Rectangle rectangle;
}
```

## 🔍 `instanceof` and Casting

### Type Checking and Casting
```java
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;
    dog.bark();
}

// Pattern matching (Java 16+)
if (animal instanceof Dog dog) {
    dog.bark();
}
```

## 📐 Design Principles

### SOLID Principles
- **S**ingle Responsibility
- **O**pen/Closed
- **L**iskov Substitution
- **I**nterface Segregation
- **D**ependency Inversion

### Composition over Inheritance
```java
// Prefer this
public class Car {
    private Engine engine;
    private Transmission transmission;
}

// Over deep inheritance hierarchies
```

---
**Next Topic:** [Arrays and Collections →](../../05-Arrays-Collections/Notes.md)