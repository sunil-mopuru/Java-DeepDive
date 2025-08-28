# Joda-Time - Quick Notes

## \ud83c\udfaf **Core Classes**

### **Main Date/Time Classes**
```java
// DateTime - main class with timezone
DateTime now = DateTime.now();
DateTime specific = new DateTime(2024, 6, 15, 10, 30, 0);

// LocalDate - date only
LocalDate today = LocalDate.now();
LocalDate birthday = new LocalDate(1990, 6, 15);

// LocalTime - time only
LocalTime currentTime = LocalTime.now();
LocalTime noon = new LocalTime(12, 0, 0);

// LocalDateTime - date and time, no timezone
LocalDateTime dateTime = LocalDateTime.now();
LocalDateTime meeting = new LocalDateTime(2024, 6, 15, 14, 30);

// Instant - point in time from epoch
Instant instant = Instant.now();
long millis = instant.getMillis();
```

---

## \ud83d\udee0\ufe0f **Common Operations**

### **Creating Dates**
```java
// Current date/time
DateTime now = DateTime.now();
DateTime nowUtc = DateTime.now(DateTimeZone.UTC);

// Specific date/time
DateTime specific = new DateTime(2024, 6, 15, 10, 30, 0);

// From string
DateTime parsed = DateTime.parse(\"2024-06-15T10:30:00\");

// From milliseconds
DateTime fromMillis = new DateTime(System.currentTimeMillis());
```

### **Date Arithmetic**
```java
DateTime date = DateTime.now();

// Addition
DateTime tomorrow = date.plusDays(1);
DateTime nextWeek = date.plusWeeks(1);
DateTime nextMonth = date.plusMonths(1);
DateTime nextYear = date.plusYears(1);
DateTime inTwoHours = date.plusHours(2);

// Subtraction
DateTime yesterday = date.minusDays(1);
DateTime lastWeek = date.minusWeeks(1);
DateTime lastMonth = date.minusMonths(1);
DateTime lastYear = date.minusYears(1);

// Withers (change specific field)
DateTime newYear = date.withYear(2025);
DateTime firstOfMonth = date.withDayOfMonth(1);
DateTime startOfDay = date.withTimeAtStartOfDay();
```

---

## \ud83c\udf0d **Time Zones**

### **Working with Time Zones**
```java
// Get time zone
DateTimeZone utc = DateTimeZone.UTC;
DateTimeZone newYork = DateTimeZone.forID(\"America/New_York\");
DateTimeZone tokyo = DateTimeZone.forID(\"Asia/Tokyo\");
DateTimeZone system = DateTimeZone.getDefault();

// Create date with timezone
DateTime nyTime = new DateTime(2024, 6, 15, 10, 0, 0, newYork);
DateTime utcTime = DateTime.now(DateTimeZone.UTC);

// Convert between time zones
DateTime localTime = DateTime.now();
DateTime utcEquivalent = localTime.withZone(DateTimeZone.UTC);
DateTime nyEquivalent = localTime.withZone(newYork);
```

### **Common Time Zone IDs**
```java
\"UTC\"                    // Coordinated Universal Time
\"America/New_York\"       // US Eastern Time
\"America/Chicago\"        // US Central Time
\"America/Denver\"         // US Mountain Time
\"America/Los_Angeles\"    // US Pacific Time
\"Europe/London\"          // British Time
\"Europe/Paris\"           // Central European Time
\"Asia/Tokyo\"             // Japan Standard Time
\"Asia/Shanghai\"          // China Standard Time
\"Australia/Sydney\"       // Australian Eastern Time
```

---

## \ud83d\udcdd **Formatting and Parsing**

### **Built-in Formatters**
```java
DateTime dt = DateTime.now();

// ISO formats
String iso = dt.toString();                    // 2024-06-15T10:30:00.000+01:00
String isoDate = dt.toLocalDate().toString();  // 2024-06-15
String isoTime = dt.toLocalTime().toString();  // 10:30:00.000

// Custom patterns
String custom = dt.toString(\"yyyy-MM-dd HH:mm:ss\");     // 2024-06-15 10:30:00
String readable = dt.toString(\"EEEE, MMMM dd, yyyy\");   // Saturday, June 15, 2024
String time12 = dt.toString(\"h:mm a\");                  // 10:30 AM
```

### **Custom Formatters**
```java
// Create formatter
DateTimeFormatter fmt = DateTimeFormat.forPattern(\"dd/MM/yyyy HH:mm\");

// Format to string
String formatted = fmt.print(DateTime.now());

// Parse from string
DateTime parsed = fmt.parseDateTime(\"15/06/2024 10:30\");

// Locale-aware formatting
DateTimeFormatter localeFmt = DateTimeFormat.forPattern(\"EEEE, MMMM dd\")
    .withLocale(Locale.FRENCH);
String frenchDate = localeFmt.print(DateTime.now()); // \"samedi, juin 15\"
```

---

## \u23f1\ufe0f **Duration and Period**

### **Duration (Time-based)**
```java
DateTime start = new DateTime(2024, 6, 15, 9, 0, 0);
DateTime end = new DateTime(2024, 6, 15, 17, 30, 0);

// Create duration
Duration duration = new Duration(start, end);

// Get components
long millis = duration.getMillis();           // Total milliseconds
long seconds = duration.getStandardSeconds();  // Total seconds
long minutes = duration.getStandardMinutes();  // Total minutes
long hours = duration.getStandardHours();      // Total hours
long days = duration.getStandardDays();        // Total days

// Create duration from values
Duration twoHours = Duration.standardHours(2);
Duration thirtyMinutes = Duration.standardMinutes(30);
Duration oneWeek = Duration.standardDays(7);
```

### **Period (Date-based)**
```java
LocalDate birth = new LocalDate(1990, 6, 15);
LocalDate today = LocalDate.now();

// Create period
Period age = new Period(birth, today);

// Get components
int years = age.getYears();
int months = age.getMonths();
int days = age.getDays();

// Create period from values
Period twoYears = Period.years(2);
Period sixMonths = Period.months(6);
Period tenDays = Period.days(10);
Period complex = new Period(2, 6, 0, 15, 0, 0, 0, 0); // 2 years, 6 months, 15 days
```

---

## \u2696\ufe0f **Comparison Operations**

### **Comparing Dates**
```java
DateTime date1 = new DateTime(2024, 6, 15, 10, 0, 0);
DateTime date2 = new DateTime(2024, 6, 15, 14, 0, 0);

// Comparison methods
boolean isBefore = date1.isBefore(date2);      // true
boolean isAfter = date1.isAfter(date2);        // false
boolean isEqual = date1.isEqual(date2);        // false

// Compare to
int comparison = date1.compareTo(date2);       // -1 (negative = before)

// Check if same day
boolean sameDay = date1.toLocalDate().equals(date2.toLocalDate());

// Null-safe comparison
boolean safeComparison = Objects.equals(date1, date2);
```

---

## \ud83d\udcc5 **Common Patterns**

### **Age Calculation**
```java
public static int calculateAge(LocalDate birthDate) {
    return new Period(birthDate, LocalDate.now()).getYears();
}

// Usage
LocalDate birth = new LocalDate(1990, 6, 15);
int age = calculateAge(birth);
```

### **Business Days**
```java
public static boolean isBusinessDay(LocalDate date) {
    int dayOfWeek = date.getDayOfWeek();
    return dayOfWeek >= DateTimeConstants.MONDAY && 
           dayOfWeek <= DateTimeConstants.FRIDAY;
}

public static LocalDate addBusinessDays(LocalDate date, int days) {
    LocalDate result = date;
    int added = 0;
    
    while (added < days) {
        result = result.plusDays(1);
        if (isBusinessDay(result)) {
            added++;
        }
    }
    return result;
}
```

### **Time Range Check**
```java
public static boolean isInRange(DateTime dateTime, DateTime start, DateTime end) {
    return !dateTime.isBefore(start) && !dateTime.isAfter(end);
}

// Usage
DateTime meeting = new DateTime(2024, 6, 15, 14, 0, 0);
DateTime workStart = new DateTime(2024, 6, 15, 9, 0, 0);
DateTime workEnd = new DateTime(2024, 6, 15, 17, 0, 0);

boolean duringWorkHours = isInRange(meeting, workStart, workEnd);
```

---

## \ud83d\udd04 **Migration to java.time**

### **Class Mapping**
```java
// Joda-Time \u2192 java.time
DateTime \u2192 ZonedDateTime / OffsetDateTime
LocalDate \u2192 LocalDate
LocalTime \u2192 LocalTime
LocalDateTime \u2192 LocalDateTime
Instant \u2192 Instant
Duration \u2192 Duration
Period \u2192 Period
DateTimeZone \u2192 ZoneId
DateTimeFormatter \u2192 DateTimeFormatter
```

### **Common Conversions**
```java
// Joda-Time to java.time
DateTime jodaDateTime = DateTime.now();
ZonedDateTime javaDateTime = jodaDateTime.toGregorianCalendar().toZonedDateTime();

// java.time to Joda-Time
ZonedDateTime javaDateTime = ZonedDateTime.now();
DateTime jodaDateTime = new DateTime(javaDateTime.toInstant().toEpochMilli());

// Simple field mapping
DateTime joda = DateTime.now();
ZonedDateTime java = ZonedDateTime.of(
    joda.getYear(),
    joda.getMonthOfYear(),
    joda.getDayOfMonth(),
    joda.getHourOfDay(),
    joda.getMinuteOfHour(),
    joda.getSecondOfMinute(),
    joda.getMillisOfSecond() * 1_000_000,
    ZoneId.of(joda.getZone().getID())
);
```

---

## \u26a0\ufe0f **Common Pitfalls**

### **Immutability**
```java
// WRONG - Joda-Time objects are immutable
DateTime date = DateTime.now();
date.plusDays(1);  // This doesn't change 'date'!

// CORRECT
DateTime date = DateTime.now();
DateTime tomorrow = date.plusDays(1);  // Create new object
```

### **Time Zone Confusion**
```java
// Be careful with time zones
DateTime local = DateTime.now();                    // Local timezone
DateTime utc = DateTime.now(DateTimeZone.UTC);     // UTC timezone

// Converting vs Creating
DateTime converted = local.withZone(DateTimeZone.UTC);  // Convert existing time
DateTime created = new DateTime(DateTimeZone.UTC);      // Create new UTC time
```

### **Month Numbers**
```java
// Joda-Time uses human-friendly month numbers (1-12)
DateTime june = new DateTime(2024, 6, 15, 0, 0, 0);  // June = 6 (correct!)

// Unlike java.util.Calendar where June = 5
Calendar cal = Calendar.getInstance();
cal.set(2024, 5, 15);  // June = 5 (confusing!)
```

---

## \ud83d\udcc8 **Performance Tips**

### **Formatter Reuse**
```java
// GOOD - Reuse formatters
private static final DateTimeFormatter FORMATTER = 
    DateTimeFormat.forPattern(\"yyyy-MM-dd HH:mm:ss\");

public String formatDate(DateTime date) {
    return FORMATTER.print(date);
}

// AVOID - Creating new formatters repeatedly
public String formatDate(DateTime date) {
    return date.toString(\"yyyy-MM-dd HH:mm:ss\");  // Creates new formatter each time
}
```

### **Time Zone Caching**
```java
// GOOD - Cache time zone objects
private static final DateTimeZone NYC_TZ = DateTimeZone.forID(\"America/New_York\");

// AVOID - Looking up time zones repeatedly
DateTime nyTime = DateTime.now(DateTimeZone.forID(\"America/New_York\"));  // Lookup each time
```

---

**Next:** [Back to Course Overview](../README.md)"