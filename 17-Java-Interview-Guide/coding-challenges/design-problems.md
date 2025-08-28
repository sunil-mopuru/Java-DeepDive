# Object-Oriented Design Problems (10+ Years Experience)

## 🏗️ **System Design Challenges**

### **Problem 1: Parking Lot Management System**
**Difficulty**: ⭐⭐⭐⭐
**Scenario**: Design a parking lot management system for a multi-level facility with different vehicle types.

**Requirements**:
- Support cars, motorcycles, buses with different spot requirements
- Handle hourly billing with dynamic pricing
- Manage reservations and real-time availability
- Support admin operations and reporting

**Solution**:
```java
// Domain model with proper abstraction
public abstract class Vehicle {
    protected String licensePlate;
    protected String color;
    protected VehicleType type;
    
    public abstract int getRequiredSpots();
    public abstract double getHourlyRate();
}

public class Car extends Vehicle {
    @Override
    public int getRequiredSpots() { return 1; }
    
    @Override
    public double getHourlyRate() { return 2.0; }
}

public class Motorcycle extends Vehicle {
    @Override
    public int getRequiredSpots() { return 1; }
    
    @Override
    public double getHourlyRate() { return 1.0; }
}

public class Bus extends Vehicle {
    @Override
    public int getRequiredSpots() { return 5; }
    
    @Override
    public double getHourlyRate() { return 3.0; }
}

// Parking spot hierarchy
public abstract class ParkingSpot {
    protected String spotId;
    protected boolean available;
    protected Vehicle parkedVehicle;
    
    public abstract boolean canFitVehicle(Vehicle vehicle);
    public abstract double calculatePrice(Duration duration);
}

public class CompactSpot extends ParkingSpot {
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.CAR || 
               vehicle.getType() == VehicleType.MOTORCYCLE;
    }
    
    @Override
    public double calculatePrice(Duration duration) {
        return duration.toHours() * 2.0;
    }
}

public class LargeSpot extends ParkingSpot {
    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true; // Can fit any vehicle
    }
    
    @Override
    public double calculatePrice(Duration duration) {
        return duration.toHours() * 3.0;
    }
}

// Parking floor with composite pattern
public class ParkingFloor {
    private String floorId;
    private List<ParkingSpot> spots;
    private Map<String, ParkingTicket> activeTickets;
    
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        List<ParkingSpot> availableSpots = findAvailableSpots(vehicle);
        
        if (availableSpots.size() < vehicle.getRequiredSpots()) {
            throw new ParkingFullException("No available spots for " + vehicle.getType());
        }
        
        // Reserve spots
        for (int i = 0; i < vehicle.getRequiredSpots(); i++) {
            ParkingSpot spot = availableSpots.get(i);
            spot.parkVehicle(vehicle);
        }
        
        // Create ticket
        ParkingTicket ticket = new ParkingTicket(vehicle, availableSpots);
        activeTickets.put(ticket.getTicketId(), ticket);
        
        return ticket;
    }
    
    public Receipt unparkVehicle(String ticketId) {
        ParkingTicket ticket = activeTickets.remove(ticketId);
        if (ticket == null) {
            throw new InvalidTicketException("Invalid ticket ID: " + ticketId);
        }
        
        Duration duration = Duration.between(ticket.getEntryTime(), Instant.now());
        double totalAmount = ticket.getSpots().stream()
            .mapToDouble(spot -> spot.calculatePrice(duration))
            .sum();
            
        // Free spots
        ticket.getSpots().forEach(ParkingSpot::removeVehicle);
        
        return new Receipt(ticket, duration, totalAmount);
    }
}

// Main parking lot system
@Service
public class ParkingLotSystem {
    
    private List<ParkingFloor> floors;
    private Map<String, ParkingTicket> allTickets;
    
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            try {
                return floor.parkVehicle(vehicle);
            } catch (ParkingFullException e) {
                // Try next floor
                continue;
            }
        }
        
        throw new ParkingFullException("All floors are full");
    }
    
    public Receipt unparkVehicle(String ticketId) {
        ParkingTicket ticket = allTickets.get(ticketId);
        if (ticket == null) {
            throw new InvalidTicketException("Ticket not found");
        }
        
        ParkingFloor floor = findFloorForTicket(ticket);
        return floor.unparkVehicle(ticketId);
    }
    
    // Reservation system
    public Reservation createReservation(VehicleType type, LocalDateTime startTime, 
                                       Duration duration) {
        // Find available spot for the time period
        ParkingSpot spot = findAvailableSpot(type, startTime, duration);
        if (spot == null) {
            throw new NoSpotAvailableException("No spots available for reservation");
        }
        
        Reservation reservation = new Reservation(spot, type, startTime, duration);
        spot.reserve(reservation);
        
        return reservation;
    }
}
```

### **Problem 2: Online Shopping Cart System**
**Expected Answer**:
```java
// Shopping cart with composite pattern for items
public class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();
    private Customer customer;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    
    public void addItem(Product product, int quantity) {
        CartItem existingItem = findItem(product.getId());
        
        if (existingItem != null) {
            existingItem.updateQuantity(existingItem.getQuantity() + quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
        
        lastModified = LocalDateTime.now();
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        lastModified = LocalDateTime.now();
    }
    
    public Money calculateTotal() {
        return items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(Money.ZERO, Money::add);
    }
    
    public Money calculateTotalWithDiscounts() {
        Money subtotal = calculateTotal();
        List<Discount> applicableDiscounts = getDiscountEngine()
            .getApplicableDiscounts(this);
            
        Money totalDiscount = applicableDiscounts.stream()
            .map(discount -> discount.calculate(subtotal, this))
            .reduce(Money.ZERO, Money::add);
            
        return subtotal.subtract(totalDiscount);
    }
}

// Strategy pattern for pricing
public interface PricingStrategy {
    Money calculatePrice(Product product, int quantity);
}

public class RegularPricingStrategy implements PricingStrategy {
    @Override
    public Money calculatePrice(Product product, int quantity) {
        return product.getPrice().multiply(quantity);
    }
}

public class BulkPricingStrategy implements PricingStrategy {
    private int bulkQuantity;
    private double discountRate;
    
    @Override
    public Money calculatePrice(Product product, int quantity) {
        if (quantity >= bulkQuantity) {
            Money regularPrice = product.getPrice().multiply(quantity);
            return regularPrice.multiply(1 - discountRate);
        }
        return product.getPrice().multiply(quantity);
    }
}

// Observer pattern for inventory updates
@Component
public class InventoryService {
    
    private List<InventoryObserver> observers = new ArrayList<>();
    
    public void addObserver(InventoryObserver observer) {
        observers.add(observer);
    }
    
    public void updateInventory(String productId, int newQuantity) {
        // Update inventory
        inventoryRepository.update(productId, newQuantity);
        
        // Notify observers
        observers.forEach(observer -> observer.onInventoryUpdate(productId, newQuantity));
    }
}

public class CartInventoryObserver implements InventoryObserver {
    
    private ShoppingCart cart;
    
    @Override
    public void onInventoryUpdate(String productId, int newQuantity) {
        CartItem item = cart.findItem(productId);
        if (item != null && item.getQuantity() > newQuantity) {
            // Adjust cart quantity to match available inventory
            item.updateQuantity(newQuantity);
            notifyCustomerOfAdjustment(item, newQuantity);
        }
    }
}
```

## 🎯 **Design Principles Applied**

### **SOLID Principles Implementation**:
1. **Single Responsibility**: Each class has one reason to change
2. **Open/Closed**: Extensible through interfaces and inheritance
3. **Liskov Substitution**: Subtypes can replace base types
4. **Interface Segregation**: Clients don't depend on unused methods
5. **Dependency Inversion**: High-level modules depend on abstractions

### **Design Patterns Used**:
- **Strategy**: For pricing and discount calculations
- **Observer**: For inventory updates
- **Composite**: For hierarchical structures
- **Factory**: For object creation
- **Singleton**: For system-wide services

### **Scalability Considerations**:
- **Database sharding** for large datasets
- **Caching strategies** for performance
- **Event-driven architecture** for loose coupling
- **Microservices decomposition** for independent scaling
- **Asynchronous processing** for non-blocking operations

### **Error Handling**:
- **Custom exceptions** for domain-specific errors
- **Validation at boundaries** to maintain consistency
- **Graceful degradation** for partial failures
- **Retry mechanisms** for transient failures
- **Circuit breaker pattern** for external dependencies