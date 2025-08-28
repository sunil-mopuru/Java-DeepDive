/**
 * Joda-Time Examples - Beginner to Advanced
 * 
 * Demonstrates Joda-Time API usage from basic concepts to advanced features.
 * This covers date/time creation, manipulation, formatting, and time zones.
 */

import org.joda.time.*;
import org.joda.time.format.*;
import java.util.*;

public class JodaTimeExamples {
    
    public static void main(String[] args) {
        System.out.println("=== Joda-Time Examples: Beginner to Advanced ===");
        
        // BEGINNER LEVEL
        demonstrateBasicDateCreation();
        showDateArithmetic();
        
        // INTERMEDIATE LEVEL
        exploreFormatting();
        workWithTimeZones();
        
        // ADVANCED LEVEL
        calculateDurationsAndPeriods();
        showPracticalExamples();
    }
    
    public static void demonstrateBasicDateCreation() {
        System.out.println("\n1. Basic Date and Time Creation");
        System.out.println("===============================");
        
        // Current date and time
        DateTime now = DateTime.now();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        
        System.out.println("Current DateTime: " + now);
        System.out.println("Today's Date: " + today);
        System.out.println("Current Time: " + currentTime);
        
        // Specific dates
        DateTime birthday = new DateTime(1990, 6, 15, 14, 30, 0);
        LocalDate christmas = new LocalDate(2024, 12, 25);
        LocalTime meetingTime = new LocalTime(14, 30, 0);
        
        System.out.println("\nSpecific dates:");
        System.out.println("Birthday: " + birthday);
        System.out.println("Christmas: " + christmas);
        System.out.println("Meeting time: " + meetingTime);
    }
    
    public static void showDateArithmetic() {
        System.out.println("\n2. Date Arithmetic Operations");
        System.out.println("==============================");
        
        DateTime baseDate = new DateTime(2024, 6, 15, 10, 30, 0);
        System.out.println("Base date: " + baseDate);
        
        // Addition operations
        DateTime tomorrow = baseDate.plusDays(1);
        DateTime nextWeek = baseDate.plusWeeks(1);
        DateTime nextMonth = baseDate.plusMonths(1);
        DateTime nextYear = baseDate.plusYears(1);
        
        System.out.println("\nAdditions:");
        System.out.println("Tomorrow: " + tomorrow);
        System.out.println("Next week: " + nextWeek);
        System.out.println("Next month: " + nextMonth);
        System.out.println("Next year: " + nextYear);
        
        // Subtraction operations
        DateTime yesterday = baseDate.minusDays(1);
        DateTime lastMonth = baseDate.minusMonths(1);
        
        System.out.println("\nSubtractions:");
        System.out.println("Yesterday: " + yesterday);
        System.out.println("Last month: " + lastMonth);
        
        // Withers (changing specific fields)
        DateTime newYear = baseDate.withYear(2025);
        DateTime startOfMonth = baseDate.withDayOfMonth(1);
        DateTime noon = baseDate.withHourOfDay(12).withMinuteOfHour(0);
        
        System.out.println("\nWithers (field changes):");
        System.out.println("Changed to 2025: " + newYear);
        System.out.println("First of month: " + startOfMonth);
        System.out.println("At noon: " + noon);
    }
    
    public static void exploreFormatting() {
        System.out.println("\n3. Date and Time Formatting");
        System.out.println("============================");
        
        DateTime dateTime = new DateTime(2024, 6, 15, 14, 30, 45);
        
        // Built-in formatters
        System.out.println("Built-in formats:");
        System.out.println("ISO format: " + dateTime.toString());
        System.out.println("Date only: " + dateTime.toLocalDate());
        System.out.println("Time only: " + dateTime.toLocalTime());
        
        // Custom formatting
        DateTimeFormatter customFormat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter readableFormat = DateTimeFormat.forPattern("EEEE, MMMM dd, yyyy 'at' h:mm a");
        
        System.out.println("\nCustom formats:");
        System.out.println("DD/MM/YYYY: " + customFormat.print(dateTime));
        System.out.println("Readable: " + readableFormat.print(dateTime));
        
        // Parsing from strings
        try {
            DateTime parsed = customFormat.parseDateTime("25/12/2024 15:30:00");
            System.out.println("\nParsed from string: " + parsed);
        } catch (IllegalArgumentException e) {
            System.out.println("Parse error: " + e.getMessage());
        }
    }
    
    public static void workWithTimeZones() {
        System.out.println("\n4. Time Zone Operations");
        System.out.println("=======================");
        
        // Different time zones
        DateTimeZone utc = DateTimeZone.UTC;
        DateTimeZone newYork = DateTimeZone.forID("America/New_York");
        DateTimeZone tokyo = DateTimeZone.forID("Asia/Tokyo");
        DateTimeZone london = DateTimeZone.forID("Europe/London");
        
        // Create dates in different time zones
        DateTime utcTime = DateTime.now(utc);
        DateTime nyTime = DateTime.now(newYork);
        DateTime tokyoTime = DateTime.now(tokyo);
        
        System.out.println("Same moment in different time zones:");
        System.out.println("UTC: " + utcTime);
        System.out.println("New York: " + nyTime);
        System.out.println("Tokyo: " + tokyoTime);
        
        // Convert between time zones
        DateTime meetingUTC = new DateTime(2024, 6, 15, 14, 0, 0, utc);
        DateTime meetingNY = meetingUTC.withZone(newYork);
        DateTime meetingTokyo = meetingUTC.withZone(tokyo);
        
        System.out.println("\nMeeting time conversions:");
        System.out.println("UTC: " + meetingUTC);
        System.out.println("New York: " + meetingNY);
        System.out.println("Tokyo: " + meetingTokyo);
    }
    
    public static void calculateDurationsAndPeriods() {
        System.out.println("\n5. Durations and Periods");
        System.out.println("========================");
        
        // Period calculation (date-based)
        LocalDate birthDate = new LocalDate(1990, 6, 15);
        LocalDate currentDate = LocalDate.now();
        Period age = new Period(birthDate, currentDate);
        
        System.out.println("Age calculation:");
        System.out.println("Born: " + birthDate);
        System.out.println("Today: " + currentDate);
        System.out.printf("Age: %d years, %d months, %d days\n", 
                         age.getYears(), age.getMonths(), age.getDays());
        
        // Duration calculation (time-based)
        DateTime workStart = new DateTime(2024, 6, 15, 9, 0, 0);
        DateTime workEnd = new DateTime(2024, 6, 15, 17, 30, 0);
        Duration workDay = new Duration(workStart, workEnd);
        
        System.out.println("\nWork day duration:");
        System.out.println("Start: " + workStart.toString("HH:mm"));
        System.out.println("End: " + workEnd.toString("HH:mm"));
        System.out.println("Duration: " + workDay.getStandardHours() + " hours " + 
                          (workDay.getStandardMinutes() % 60) + " minutes");
        
        // Project timeline
        DateTime projectStart = new DateTime(2024, 1, 1, 0, 0, 0);
        DateTime projectEnd = new DateTime(2024, 6, 30, 0, 0, 0);
        Period projectDuration = new Period(projectStart.toLocalDate(), projectEnd.toLocalDate());
        
        System.out.println("\nProject timeline:");
        System.out.println("Start: " + projectStart.toLocalDate());
        System.out.println("End: " + projectEnd.toLocalDate());
        System.out.printf("Duration: %d months, %d days\n", 
                         projectDuration.getMonths(), projectDuration.getDays());
    }
    
    public static void showPracticalExamples() {
        System.out.println("\n6. Practical Real-World Examples");
        System.out.println("================================");
        
        // Business day calculation
        LocalDate startDate = new LocalDate(2024, 6, 14); // Friday
        LocalDate businessDay = addBusinessDays(startDate, 5);
        System.out.println("5 business days after " + startDate + " is: " + businessDay);
        
        // Meeting scheduler across time zones
        scheduleGlobalMeeting();
        
        // Age verification
        boolean canVote = isOldEnough(new LocalDate(2000, 6, 15), 18);
        System.out.println("\nCan vote (born 2000-06-15): " + canVote);
        
        // Working hours check
        LocalTime currentTime = new LocalTime(14, 30, 0);
        boolean duringWork = isDuringWorkingHours(currentTime);
        System.out.println("During working hours (" + currentTime + "): " + duringWork);
    }
    
    // Helper methods for practical examples
    public static LocalDate addBusinessDays(LocalDate startDate, int businessDaysToAdd) {
        LocalDate result = startDate;
        int added = 0;
        
        while (added < businessDaysToAdd) {
            result = result.plusDays(1);
            if (result.getDayOfWeek() >= DateTimeConstants.MONDAY && 
                result.getDayOfWeek() <= DateTimeConstants.FRIDAY) {
                added++;
            }
        }
        return result;
    }
    
    public static void scheduleGlobalMeeting() {
        System.out.println("\nGlobal meeting scheduler:");
        
        // Meeting at 10 AM New York time
        DateTimeZone nyTz = DateTimeZone.forID("America/New_York");
        DateTime meetingNY = new DateTime(2024, 6, 15, 10, 0, 0, nyTz);
        
        // Convert to other time zones
        DateTimeZone londonTz = DateTimeZone.forID("Europe/London");
        DateTimeZone tokyoTz = DateTimeZone.forID("Asia/Tokyo");
        DateTimeZone sydneyTz = DateTimeZone.forID("Australia/Sydney");
        
        System.out.println("Meeting time (10 AM NY):");
        System.out.println("New York: " + meetingNY.toString("HH:mm"));
        System.out.println("London: " + meetingNY.withZone(londonTz).toString("HH:mm"));
        System.out.println("Tokyo: " + meetingNY.withZone(tokyoTz).toString("HH:mm"));
        System.out.println("Sydney: " + meetingNY.withZone(sydneyTz).toString("HH:mm"));
    }
    
    public static boolean isOldEnough(LocalDate birthDate, int requiredAge) {
        Period age = new Period(birthDate, LocalDate.now());
        return age.getYears() >= requiredAge;
    }
    
    public static boolean isDuringWorkingHours(LocalTime time) {
        LocalTime workStart = new LocalTime(9, 0, 0);
        LocalTime workEnd = new LocalTime(17, 0, 0);
        return !time.isBefore(workStart) && !time.isAfter(workEnd);
    }
}