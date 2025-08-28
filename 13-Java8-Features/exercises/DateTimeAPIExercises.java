/**
 * Date and Time API Exercises - Beginner to Advanced
 * 
 * Practice exercises for mastering Java 8 Date and Time API (java.time package),
 * working with LocalDate, LocalTime, LocalDateTime, ZonedDateTime, and more.
 */

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import java.util.stream.Collectors;

public class DateTimeAPIExercises {
    
    public static void main(String[] args) {
        System.out.println("=== Date and Time API Exercises ===");
        
        basicDateTimeExercises();
        formattingAndParsingExercises();
        dateArithmeticExercises();
        timeZoneExercises();
        advancedDateTimeExercises();
    }
    
    public static void basicDateTimeExercises() {
        System.out.println("\n1. Basic Date and Time Exercises");
        System.out.println("================================");
        
        // Exercise 1a: Creating date and time objects
        System.out.println("Exercise 1a: Creating date and time objects");
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalDateTime now = LocalDateTime.now();
        
        System.out.println("Today's date: " + today);
        System.out.println("Current time: " + currentTime);
        System.out.println("Current date-time: " + now);
        
        // Exercise 1b: Creating specific dates and times
        System.out.println("\nExercise 1b: Creating specific dates and times");
        LocalDate birthday = LocalDate.of(1990, Month.JUNE, 15);
        LocalTime meetingTime = LocalTime.of(14, 30, 0);
        LocalDateTime appointment = LocalDateTime.of(2024, 6, 15, 10, 30);
        
        System.out.println("Birthday: " + birthday);
        System.out.println("Meeting time: " + meetingTime);
        System.out.println("Appointment: " + appointment);
        
        // Exercise 1c: Getting components from date/time objects
        System.out.println("\nExercise 1c: Getting components from date/time objects");
        System.out.println("Year: " + today.getYear());
        System.out.println("Month: " + today.getMonth());
        System.out.println("Day of month: " + today.getDayOfMonth());
        System.out.println("Day of week: " + today.getDayOfWeek());
        System.out.println("Day of year: " + today.getDayOfYear());
        
        System.out.println("Hour: " + currentTime.getHour());
        System.out.println("Minute: " + currentTime.getMinute());
        System.out.println("Second: " + currentTime.getSecond());
        
        // Exercise 1d: Instant and Clock
        System.out.println("\nExercise 1d: Instant and Clock");
        Instant instant = Instant.now();
        System.out.println("Current instant (UTC): " + instant);
        System.out.println("Epoch seconds: " + instant.getEpochSecond());
        
        Clock systemClock = Clock.systemDefaultZone();
        LocalDateTime clockTime = LocalDateTime.now(systemClock);
        System.out.println("Time from system clock: " + clockTime);
    }
    
    public static void formattingAndParsingExercises() {
        System.out.println("\n2. Formatting and Parsing Exercises");
        System.out.println("===================================");
        
        LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 14, 30, 45);
        
        // Exercise 2a: Built-in formatters
        System.out.println("Exercise 2a: Built-in formatters");
        System.out.println("ISO_LOCAL_DATE: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("ISO_LOCAL_TIME: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.out.println("ISO_LOCAL_DATE_TIME: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Exercise 2b: Custom formatters
        System.out.println("\nExercise 2b: Custom formatters");
        DateTimeFormatter customFormatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter customFormatter2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' h:mm a");
        DateTimeFormatter customFormatter3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        
        System.out.println("Custom format 1: " + dateTime.format(customFormatter1));
        System.out.println("Custom format 2: " + dateTime.format(customFormatter2));
        System.out.println("Custom format 3: " + dateTime.format(customFormatter3));
        
        // Exercise 2c: Parsing from strings
        System.out.println("\nExercise 2c: Parsing from strings");
        try {
            LocalDate parsedDate = LocalDate.parse("2024-12-25", DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDateTime parsedDateTime = LocalDateTime.parse("15/06/2024 14:30:45", customFormatter1);
            
            System.out.println("Parsed date: " + parsedDate);
            System.out.println("Parsed date-time: " + parsedDateTime);
        } catch (DateTimeParseException e) {
            System.out.println("Parse error: " + e.getMessage());
        }
        
        // Exercise 2d: Locale-specific formatting
        System.out.println("\nExercise 2d: Locale-specific formatting");
        DateTimeFormatter frenchFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.FRENCH);
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("EEEE, dd. MMMM yyyy", Locale.GERMAN);
        
        System.out.println("French format: " + dateTime.format(frenchFormatter));
        System.out.println("German format: " + dateTime.format(germanFormatter));
    }
    
    public static void dateArithmeticExercises() {
        System.out.println("\n3. Date Arithmetic Exercises");
        System.out.println("============================");
        
        LocalDate baseDate = LocalDate.of(2024, 6, 15);
        LocalDateTime baseDateTime = LocalDateTime.of(2024, 6, 15, 10, 30, 0);
        
        // Exercise 3a: Adding and subtracting time units
        System.out.println("Exercise 3a: Adding and subtracting time units");
        System.out.println("Base date: " + baseDate);
        System.out.println("Plus 1 day: " + baseDate.plusDays(1));
        System.out.println("Plus 1 week: " + baseDate.plusWeeks(1));
        System.out.println("Plus 1 month: " + baseDate.plusMonths(1));
        System.out.println("Plus 1 year: " + baseDate.plusYears(1));
        System.out.println("Minus 10 days: " + baseDate.minusDays(10));
        
        // Exercise 3b: With methods (setting specific values)
        System.out.println("\nExercise 3b: With methods (setting specific values)");
        System.out.println("Base date-time: " + baseDateTime);
        System.out.println("With year 2025: " + baseDateTime.withYear(2025));
        System.out.println("With month December: " + baseDateTime.withMonth(12));
        System.out.println("With day 1st: " + baseDateTime.withDayOfMonth(1));
        System.out.println("With hour 15: " + baseDateTime.withHour(15));
        
        // Exercise 3c: Period calculations
        System.out.println("\nExercise 3c: Period calculations");
        LocalDate birthDate = LocalDate.of(1990, 3, 15);
        LocalDate currentDate = LocalDate.now();
        
        Period age = Period.between(birthDate, currentDate);
        System.out.println("Birth date: " + birthDate);
        System.out.println("Current date: " + currentDate);
        System.out.printf("Age: %d years, %d months, %d days%n", 
                         age.getYears(), age.getMonths(), age.getDays());
        
        // Exercise 3d: Duration calculations
        System.out.println("\nExercise 3d: Duration calculations");
        LocalTime workStart = LocalTime.of(9, 0);
        LocalTime workEnd = LocalTime.of(17, 30);
        
        Duration workDuration = Duration.between(workStart, workEnd);
        System.out.println("Work start: " + workStart);
        System.out.println("Work end: " + workEnd);
        System.out.println("Work duration: " + workDuration.toHours() + " hours " + 
                          (workDuration.toMinutes() % 60) + " minutes");
        
        // Exercise 3e: Business day calculations
        System.out.println("\nExercise 3e: Business day calculations");
        LocalDate startDate = LocalDate.of(2024, 6, 14); // Friday
        LocalDate businessDayResult = addBusinessDays(startDate, 5);
        System.out.println("5 business days after " + startDate + " (" + startDate.getDayOfWeek() + ") is: " + 
                          businessDayResult + " (" + businessDayResult.getDayOfWeek() + ")");
    }
    
    public static void timeZoneExercises() {
        System.out.println("\n4. Time Zone Exercises");
        System.out.println("======================");
        
        // Exercise 4a: Working with different time zones
        System.out.println("Exercise 4a: Working with different time zones");
        ZoneId utc = ZoneId.of("UTC");
        ZoneId newYork = ZoneId.of("America/New_York");
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        ZoneId london = ZoneId.of("Europe/London");
        
        LocalDateTime localDateTime = LocalDateTime.of(2024, 6, 15, 12, 0);
        
        ZonedDateTime utcTime = localDateTime.atZone(utc);
        ZonedDateTime nyTime = utcTime.withZoneSameInstant(newYork);
        ZonedDateTime tokyoTime = utcTime.withZoneSameInstant(tokyo);
        ZonedDateTime londonTime = utcTime.withZoneSameInstant(london);
        
        System.out.println("UTC time: " + utcTime);
        System.out.println("New York time: " + nyTime);
        System.out.println("Tokyo time: " + tokyoTime);
        System.out.println("London time: " + londonTime);
        
        // Exercise 4b: Meeting scheduler across time zones
        System.out.println("\nExercise 4b: Global meeting scheduler");
        scheduleGlobalMeeting();
        
        // Exercise 4c: Daylight Saving Time handling
        System.out.println("\nExercise 4c: Daylight Saving Time handling");
        handleDaylightSavingTime();
        
        // Exercise 4d: Converting between time zones
        System.out.println("\nExercise 4d: Time zone conversion utility");
        ZonedDateTime meetingTime = ZonedDateTime.of(2024, 3, 15, 10, 0, 0, 0, newYork);
        
        System.out.println("Meeting scheduled for (NY): " + meetingTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")));
        
        List<String> officeZones = Arrays.asList("Europe/London", "Asia/Tokyo", "Australia/Sydney");
        for (String zone : officeZones) {
            ZonedDateTime officeTime = meetingTime.withZoneSameInstant(ZoneId.of(zone));
            System.out.println("  " + zone + ": " + officeTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")));
        }
    }
    
    public static void advancedDateTimeExercises() {
        System.out.println("\n5. Advanced Date and Time Exercises");
        System.out.println("===================================");
        
        // Exercise 5a: Temporal adjusters
        System.out.println("Exercise 5a: Temporal adjusters");
        LocalDate today = LocalDate.now();
        
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear());
        LocalDate nextWorkingDay = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        
        System.out.println("Today: " + today);
        System.out.println("Next Monday: " + nextMonday);
        System.out.println("Last day of month: " + lastDayOfMonth);
        System.out.println("First day of year: " + firstDayOfYear);
        
        // Exercise 5b: Custom temporal adjuster
        System.out.println("\nExercise 5b: Custom temporal adjuster - Next working day");
        TemporalAdjuster nextWorkingDayAdjuster = temporal -> {
            LocalDate date = LocalDate.from(temporal);
            do {
                date = date.plusDays(1);
            } while (date.getDayOfWeek() == DayOfWeek.SATURDAY || 
                     date.getDayOfWeek() == DayOfWeek.SUNDAY);
            return date;
        };
        
        LocalDate fridayDate = LocalDate.of(2024, 6, 14); // Friday
        LocalDate nextWorking = fridayDate.with(nextWorkingDayAdjuster);
        System.out.println("After Friday " + fridayDate + ", next working day is: " + nextWorking);
        
        // Exercise 5c: Age calculation with different methods
        System.out.println("\nExercise 5c: Precise age calculation");
        LocalDate birthDate = LocalDate.of(1990, 2, 29); // Leap year birthday
        calculatePreciseAge(birthDate);
        
        // Exercise 5d: Working with fiscal years
        System.out.println("\nExercise 5d: Fiscal year calculations");
        calculateFiscalYear(LocalDate.of(2024, 3, 15));
        calculateFiscalYear(LocalDate.of(2024, 10, 15));
        
        // Exercise 5e: Holiday calculations
        System.out.println("\nExercise 5e: Holiday calculations");
        calculateHolidays(2024);
        
        // Exercise 5f: Working hours calculator
        System.out.println("\nExercise 5f: Working hours in date range");
        LocalDate startPeriod = LocalDate.of(2024, 6, 1);
        LocalDate endPeriod = LocalDate.of(2024, 6, 30);
        long workingHours = calculateWorkingHours(startPeriod, endPeriod);
        System.out.printf("Working hours from %s to %s: %d hours%n", startPeriod, endPeriod, workingHours);
    }
    
    // Helper methods
    
    private static LocalDate addBusinessDays(LocalDate startDate, int businessDaysToAdd) {
        LocalDate result = startDate;
        int added = 0;
        
        while (added < businessDaysToAdd) {
            result = result.plusDays(1);
            if (result.getDayOfWeek() != DayOfWeek.SATURDAY && 
                result.getDayOfWeek() != DayOfWeek.SUNDAY) {
                added++;
            }
        }
        return result;
    }
    
    private static void scheduleGlobalMeeting() {
        // Meeting at 10 AM Eastern Time
        ZonedDateTime meetingET = ZonedDateTime.of(2024, 6, 15, 10, 0, 0, 0, ZoneId.of("America/New_York"));
        
        Map<String, ZoneId> offices = Map.of(
            "New York", ZoneId.of("America/New_York"),
            "London", ZoneId.of("Europe/London"),
            "Tokyo", ZoneId.of("Asia/Tokyo"),
            "Sydney", ZoneId.of("Australia/Sydney"),
            "San Francisco", ZoneId.of("America/Los_Angeles")
        );
        
        System.out.println("Global meeting scheduled for 10:00 AM Eastern Time:");
        offices.forEach((city, zone) -> {
            ZonedDateTime cityTime = meetingET.withZoneSameInstant(zone);
            System.out.printf("  %-15s: %s%n", city, 
                cityTime.format(DateTimeFormatter.ofPattern("HH:mm (EEE, MMM dd)")));
        });
    }
    
    private static void handleDaylightSavingTime() {
        // Spring forward example (US)
        ZoneId eastern = ZoneId.of("America/New_York");
        
        // Before DST starts (2024 DST starts March 10, 2:00 AM -> 3:00 AM)
        LocalDateTime beforeDST = LocalDateTime.of(2024, 3, 10, 1, 30);
        LocalDateTime duringDST = LocalDateTime.of(2024, 3, 10, 2, 30); // This time doesn't exist!
        
        try {
            ZonedDateTime beforeZoned = beforeDST.atZone(eastern);
            System.out.println("Before DST: " + beforeZoned);
            
            ZonedDateTime duringZoned = duringDST.atZone(eastern);
            System.out.println("During DST gap (auto-adjusted): " + duringZoned);
        } catch (Exception e) {
            System.out.println("DST handling: " + e.getMessage());
        }
    }
    
    private static void calculatePreciseAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        long totalDays = ChronoUnit.DAYS.between(birthDate, currentDate);
        
        System.out.println("Birth date: " + birthDate);
        System.out.println("Current date: " + currentDate);
        System.out.printf("Age: %d years, %d months, %d days%n", 
                         period.getYears(), period.getMonths(), period.getDays());
        System.out.println("Total days lived: " + totalDays);
        
        // Handle leap year birthdays
        if (birthDate.getMonthDay().equals(MonthDay.of(2, 29))) {
            System.out.println("Note: Born on leap day (Feb 29)");
        }
    }
    
    private static void calculateFiscalYear(LocalDate date) {
        // Assume fiscal year starts April 1st
        int fiscalYear;
        if (date.getMonth().getValue() >= 4) {
            fiscalYear = date.getYear() + 1; // FY 2024-25 if date is Apr 2024 or later
        } else {
            fiscalYear = date.getYear(); // FY 2023-24 if date is Jan-Mar 2024
        }
        
        LocalDate fiscalStart = LocalDate.of(fiscalYear - 1, 4, 1);
        LocalDate fiscalEnd = LocalDate.of(fiscalYear, 3, 31);
        
        System.out.printf("Date %s is in FY %d-%d (Apr 1 %d - Mar 31 %d)%n", 
                         date, fiscalYear - 1, fiscalYear % 100, fiscalYear - 1, fiscalYear);
    }
    
    private static void calculateHolidays(int year) {
        // Calculate some US holidays for the year
        LocalDate newYear = LocalDate.of(year, 1, 1);
        
        // Martin Luther King Day - 3rd Monday in January
        LocalDate mlkDay = LocalDate.of(year, 1, 1)
            .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.MONDAY));
        
        // Memorial Day - Last Monday in May
        LocalDate memorialDay = LocalDate.of(year, 5, 31)
            .with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));
        
        // Independence Day
        LocalDate independenceDay = LocalDate.of(year, 7, 4);
        
        // Labor Day - First Monday in September
        LocalDate laborDay = LocalDate.of(year, 9, 1)
            .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        
        // Thanksgiving - 4th Thursday in November
        LocalDate thanksgiving = LocalDate.of(year, 11, 1)
            .with(TemporalAdjusters.dayOfWeekInMonth(4, DayOfWeek.THURSDAY));
        
        System.out.println("US Federal Holidays for " + year + ":");
        System.out.println("  New Year's Day: " + newYear.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        System.out.println("  Martin Luther King Day: " + mlkDay.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        System.out.println("  Memorial Day: " + memorialDay.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        System.out.println("  Independence Day: " + independenceDay.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        System.out.println("  Labor Day: " + laborDay.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        System.out.println("  Thanksgiving: " + thanksgiving.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
    }
    
    private static long calculateWorkingHours(LocalDate startDate, LocalDate endDate) {
        long workingDays = startDate.datesUntil(endDate.plusDays(1))
            .filter(date -> date.getDayOfWeek() != DayOfWeek.SATURDAY && 
                           date.getDayOfWeek() != DayOfWeek.SUNDAY)
            .count();
        
        return workingDays * 8; // Assuming 8-hour work days
    }
}