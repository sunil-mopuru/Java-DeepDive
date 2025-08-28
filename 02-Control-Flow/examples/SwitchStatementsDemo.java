/**
 * SwitchStatementsDemo.java - Comprehensive Switch Statement Examples
 * 
 * This program demonstrates traditional and modern switch statements in Java,
 * including fall-through behavior, switch expressions, and practical applications.
 */

import java.util.Scanner;

public class SwitchStatementsDemo {
    
    public static void main(String[] args) {
        
        System.out.println("=== SWITCH STATEMENTS COMPREHENSIVE DEMO ===\n");
        
        // TRADITIONAL SWITCH STATEMENT
        System.out.println("1. TRADITIONAL SWITCH STATEMENT:");
        System.out.println("----------------------------------");
        
        // Day of week example
        int dayNumber = 4;
        String dayName;
        
        switch (dayNumber) {
            case 1:
                dayName = "Monday";
                break;
            case 2:
                dayName = "Tuesday";
                break;
            case 3:
                dayName = "Wednesday";
                break;
            case 4:
                dayName = "Thursday";
                break;
            case 5:
                dayName = "Friday";
                break;
            case 6:
                dayName = "Saturday";
                break;
            case 7:
                dayName = "Sunday";
                break;
            default:
                dayName = "Invalid day";
                break;
        }
        
        System.out.printf("Day %d is %s%n%n", dayNumber, dayName);
        
        // SWITCH WITH STRINGS (Java 7+)
        System.out.println("2. SWITCH WITH STRINGS:");
        System.out.println("------------------------");
        
        String month = "March";
        String season;
        int days;
        
        switch (month) {
            case "December":
            case "January":
            case "February":
                season = "Winter";
                days = (month.equals("February")) ? 28 : 31;
                break;
            case "March":
            case "April":
            case "May":
                season = "Spring";
                days = (month.equals("April")) ? 30 : 31;
                break;
            case "June":
            case "July":
            case "August":
                season = "Summer";
                days = (month.equals("June")) ? 30 : 31;
                break;
            case "September":
            case "October":
            case "November":
                season = "Autumn";
                days = (month.equals("September") || month.equals("November")) ? 30 : 31;
                break;
            default:
                season = "Unknown";
                days = 0;
                break;
        }
        
        System.out.printf("Month: %s → Season: %s, Days: %d%n%n", month, season, days);
        
        // FALL-THROUGH BEHAVIOR DEMONSTRATION
        System.out.println("3. FALL-THROUGH BEHAVIOR:");
        System.out.println("--------------------------");
        
        int score = 85;
        String feedback = "Score " + score + ": ";
        
        // Intentional fall-through for cumulative feedback
        switch (score / 10) {
            case 10:
            case 9:
                feedback += "Excellent! ";
                // Fall through intentionally
            case 8:
                feedback += "Great work! ";
                // Fall through intentionally
            case 7:
                feedback += "Good job! ";
                break;
            case 6:
                feedback += "Satisfactory, but can improve.";
                break;
            default:
                feedback += "Needs significant improvement.";
                break;
        }
        
        System.out.println(feedback + "\n");
        
        // SWITCH WITH ENUM
        System.out.println("4. SWITCH WITH ENUM:");
        System.out.println("---------------------");
        
        Priority taskPriority = Priority.HIGH;
        String urgencyLevel;
        int maxDays;
        
        switch (taskPriority) {
            case CRITICAL:
                urgencyLevel = "🔴 URGENT - Handle immediately!";
                maxDays = 0;
                break;
            case HIGH:
                urgencyLevel = "🟠 HIGH - Complete within 24 hours";
                maxDays = 1;
                break;
            case MEDIUM:
                urgencyLevel = "🟡 MEDIUM - Complete within 3 days";
                maxDays = 3;
                break;
            case LOW:
                urgencyLevel = "🟢 LOW - Complete within a week";
                maxDays = 7;
                break;
            default:
                urgencyLevel = "⚪ UNKNOWN priority";
                maxDays = -1;
                break;
        }
        
        System.out.printf("Task priority: %s%n", taskPriority);
        System.out.printf("Action required: %s%n", urgencyLevel);
        System.out.printf("Maximum days: %d%n%n", maxDays);
        
        // ENHANCED SWITCH (Java 14+) - Expression Form
        System.out.println("5. ENHANCED SWITCH EXPRESSIONS (Java 14+):");
        System.out.println("--------------------------------------------");
        
        // Arrow syntax - no fall-through, returns value
        int dayNum = 5;
        String dayType = switch (dayNum) {
            case 1, 2, 3, 4, 5 -> "Weekday";
            case 6, 7 -> "Weekend";
            default -> "Invalid day";
        };
        System.out.printf("Day %d is a %s%n", dayNum, dayType);
        
        // Multi-statement blocks with yield
        String monthName = "July";
        String monthInfo = switch (monthName) {
            case "December", "January", "February" -> {
                System.out.println("  Processing winter month...");
                yield "Winter month - cold season";
            }
            case "March", "April", "May" -> {
                System.out.println("  Processing spring month...");
                yield "Spring month - flowers bloom";
            }
            case "June", "July", "August" -> {
                System.out.println("  Processing summer month...");
                yield "Summer month - vacation time";
            }
            case "September", "October", "November" -> {
                System.out.println("  Processing autumn month...");
                yield "Autumn month - leaves fall";
            }
            default -> "Unknown month";
        };
        System.out.printf("%s: %s%n%n", monthName, monthInfo);
        
        // CALCULATOR USING SWITCH
        System.out.println("6. PRACTICAL APPLICATION - Calculator:");
        System.out.println("---------------------------------------");
        
        double num1 = 15.5;
        double num2 = 4.2;
        char operator = '*';
        
        String calculation = switch (operator) {
            case '+' -> {
                double result = num1 + num2;
                yield String.format("%.1f + %.1f = %.2f", num1, num2, result);
            }
            case '-' -> {
                double result = num1 - num2;
                yield String.format("%.1f - %.1f = %.2f", num1, num2, result);
            }
            case '*' -> {
                double result = num1 * num2;
                yield String.format("%.1f × %.1f = %.2f", num1, num2, result);
            }
            case '/' -> {
                if (num2 != 0) {
                    double result = num1 / num2;
                    yield String.format("%.1f ÷ %.1f = %.2f", num1, num2, result);
                } else {
                    yield "Error: Division by zero!";
                }
            }
            case '%' -> {
                if (num2 != 0) {
                    double result = num1 % num2;
                    yield String.format("%.1f %% %.1f = %.2f", num1, num2, result);
                } else {
                    yield "Error: Modulus by zero!";
                }
            }
            default -> "Error: Unknown operator '" + operator + "'";
        };
        
        System.out.println(calculation + "\n");
        
        // GRADE LETTER TO GPA CONVERSION
        System.out.println("7. GRADE TO GPA CONVERTER:");
        System.out.println("---------------------------");
        
        char letterGrade = 'B';
        double gpa = switch (letterGrade) {
            case 'A' -> 4.0;
            case 'B' -> 3.0;
            case 'C' -> 2.0;
            case 'D' -> 1.0;
            case 'F' -> 0.0;
            default -> {
                System.out.println("Invalid grade: " + letterGrade);
                yield -1.0;
            }
        };
        
        if (gpa >= 0) {
            System.out.printf("Grade %c corresponds to GPA: %.1f%n", letterGrade, gpa);
        }
        
        // HTTP STATUS CODE HANDLER
        System.out.println("\n8. HTTP STATUS CODE HANDLER:");
        System.out.println("-----------------------------");
        
        int statusCode = 404;
        String statusMessage = switch (statusCode / 100) {
            case 2 -> switch (statusCode) {
                case 200 -> "OK - Request successful";
                case 201 -> "Created - Resource created successfully";
                case 204 -> "No Content - Request successful, no content returned";
                default -> "Success - 2xx status code";
            };
            case 3 -> "Redirection - 3xx status code";
            case 4 -> switch (statusCode) {
                case 400 -> "Bad Request - Invalid request syntax";
                case 401 -> "Unauthorized - Authentication required";
                case 403 -> "Forbidden - Access denied";
                case 404 -> "Not Found - Resource not found";
                default -> "Client Error - 4xx status code";
            };
            case 5 -> "Server Error - 5xx status code";
            default -> "Unknown status code";
        };
        
        System.out.printf("HTTP %d: %s%n", statusCode, statusMessage);
        
        // INTERACTIVE MENU SYSTEM
        System.out.println("\n9. INTERACTIVE MENU SYSTEM:");
        System.out.println("-----------------------------");
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("📱 PHONE MENU SIMULATOR");
        System.out.println("1. Call");
        System.out.println("2. Text");
        System.out.println("3. Internet");
        System.out.println("4. Settings");
        System.out.println("0. Exit");
        System.out.print("Enter your choice (0-4): ");
        
        int choice = scanner.nextInt();
        
        String menuAction = switch (choice) {
            case 1 -> {
                System.out.println("📞 Opening call application...");
                yield "Call feature activated";
            }
            case 2 -> {
                System.out.println("💬 Opening messaging app...");
                yield "Text messaging activated";
            }
            case 3 -> {
                System.out.println("🌐 Opening web browser...");
                yield "Internet browsing activated";
            }
            case 4 -> {
                System.out.println("⚙️ Opening settings menu...");
                yield "Settings panel opened";
            }
            case 0 -> {
                System.out.println("👋 Goodbye! Phone shutting down...");
                yield "Exit requested";
            }
            default -> {
                System.out.println("❌ Invalid option selected!");
                yield "Error: Invalid choice";
            }
        };
        
        System.out.println("Result: " + menuAction + "\n");
        
        // COMPARISON: SWITCH vs IF-ELSE-IF
        System.out.println("10. SWITCH vs IF-ELSE-IF COMPARISON:");
        System.out.println("-------------------------------------");
        
        String userRole = "admin";
        
        // Using switch
        String switchPermissions = switch (userRole.toLowerCase()) {
            case "admin" -> "Full access - can read, write, delete, and manage users";
            case "editor" -> "Edit access - can read and write content";
            case "viewer" -> "Read access - can only view content";
            case "guest" -> "Limited access - can view public content only";
            default -> "No access - unknown role";
        };
        
        // Using if-else-if (equivalent logic)
        String ifElsePermissions;
        if (userRole.toLowerCase().equals("admin")) {
            ifElsePermissions = "Full access - can read, write, delete, and manage users";
        } else if (userRole.toLowerCase().equals("editor")) {
            ifElsePermissions = "Edit access - can read and write content";
        } else if (userRole.toLowerCase().equals("viewer")) {
            ifElsePermissions = "Read access - can only view content";
        } else if (userRole.toLowerCase().equals("guest")) {
            ifElsePermissions = "Limited access - can view public content only";
        } else {
            ifElsePermissions = "No access - unknown role";
        }
        
        System.out.printf("User role: %s%n", userRole);
        System.out.printf("Switch result: %s%n", switchPermissions);
        System.out.printf("If-else result: %s%n", ifElsePermissions);
        System.out.printf("Results match: %b%n", switchPermissions.equals(ifElsePermissions));
        
        scanner.close();
    }
    
    // Enum for priority demonstration
    enum Priority {
        CRITICAL, HIGH, MEDIUM, LOW
    }
}

/*
 * Key Learning Points:
 * 
 * 1. TRADITIONAL SWITCH:
 *    - Works with int, byte, short, char, String, enum
 *    - Requires break statements to prevent fall-through
 *    - Default case is optional but recommended
 * 
 * 2. FALL-THROUGH:
 *    - Execution continues to next case without break
 *    - Can be intentional (useful) or accidental (bug)
 *    - Use comments to indicate intentional fall-through
 * 
 * 3. ENHANCED SWITCH (Java 14+):
 *    - Arrow syntax -> eliminates fall-through
 *    - Can return values (switch expressions)
 *    - Multiple values per case: case 1, 2, 3 ->
 *    - Use yield for multi-statement blocks
 * 
 * 4. WHEN TO USE SWITCH:
 *    - Multiple discrete values to compare
 *    - Cleaner than long if-else-if chains
 *    - When you need fall-through behavior
 *    - Performance can be better for many cases
 * 
 * 5. WHEN TO USE IF-ELSE:
 *    - Complex boolean conditions
 *    - Range comparisons (>, <, >=, <=)
 *    - Combining multiple variables
 *    - When conditions are not discrete values
 * 
 * 6. BEST PRACTICES:
 *    - Always include default case
 *    - Use break statements (traditional switch)
 *    - Group related cases together
 *    - Keep case logic simple
 *    - Consider using enums for type safety
 */