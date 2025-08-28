/**
 * ComprehensiveJavaDemo.java - Integrated Java Fundamentals Example
 * 
 * This comprehensive demo integrates concepts from all modules:
 * - Basic syntax and OOP principles
 * - Collections and data structures
 * - File I/O and exception handling
 * - Multithreading and streams
 * - Best practices and design patterns
 */

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class ComprehensiveJavaDemo {
    
    public static void main(String[] args) {
        System.out.println("=== COMPREHENSIVE JAVA FUNDAMENTALS DEMO ===\n");
        
        try {
            // Initialize the student management system
            StudentManagementSystem sms = new StudentManagementSystem();
            
            // Run the comprehensive demo
            sms.runDemo();
            
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// ==================== MAIN SYSTEM CLASS ====================

class StudentManagementSystem {
    private final Map<Integer, Student> students;
    private final List<Course> courses;
    private final GradeProcessor gradeProcessor;
    private final ReportGenerator reportGenerator;
    private final ExecutorService executorService;
    
    public StudentManagementSystem() {
        this.students = new ConcurrentHashMap<>();
        this.courses = Collections.synchronizedList(new ArrayList<>());
        this.gradeProcessor = new GradeProcessor();
        this.reportGenerator = new ReportGenerator();
        this.executorService = Executors.newFixedThreadPool(4);
    }
    
    public void runDemo() throws Exception {
        System.out.println("🎓 STUDENT MANAGEMENT SYSTEM DEMO");
        System.out.println("==================================\n");
        
        // 1. Setup initial data
        setupInitialData();
        
        // 2. Demonstrate collections and streams
        demonstrateCollectionsAndStreams();
        
        // 3. File I/O operations
        demonstrateFileOperations();
        
        // 4. Multithreading with concurrent processing
        demonstrateMultithreading();
        
        // 5. Generate comprehensive reports
        generateReports();
        
        // Cleanup
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("✅ Demo completed successfully!");
    }
    
    private void setupInitialData() {
        System.out.println("📚 Setting up initial data...");
        
        // Create courses
        courses.addAll(Arrays.asList(
            new Course("CS101", "Introduction to Programming", 3, "Dr. Smith"),
            new Course("CS102", "Data Structures", 4, "Prof. Johnson"),
            new Course("CS201", "Algorithms", 4, "Dr. Brown"),
            new Course("CS301", "Software Engineering", 3, "Prof. Davis")
        ));
        
        // Create students with random data
        Random random = new Random();
        for (int i = 1; i <= 20; i++) {
            Student student = new Student(
                1000 + i,
                generateRandomName(),
                String.format("student%d@university.edu", i),
                LocalDate.of(2000 + random.nextInt(5), 1 + random.nextInt(12), 1 + random.nextInt(28))
            );
            
            // Enroll students in random courses with random grades
            courses.forEach(course -> {
                if (random.nextDouble() > 0.3) { // 70% chance of enrollment
                    double grade = 60 + random.nextDouble() * 40; // Grade between 60-100
                    student.enrollInCourse(course.getCourseId(), grade);
                }
            });
            
            students.put(student.getStudentId(), student);
        }
        
        System.out.printf("✅ Created %d students and %d courses%n%n", students.size(), courses.size());
    }
    
    private void demonstrateCollectionsAndStreams() {
        System.out.println("🔍 COLLECTIONS AND STREAMS DEMONSTRATION");
        System.out.println("=========================================");
        
        // Stream operations with method chaining
        System.out.println("📊 Student Statistics:");
        
        // Find top performing students
        List<Student> topStudents = students.values().stream()
            .filter(student -> !student.getEnrollments().isEmpty())
            .sorted(Comparator.comparing(Student::getGPA).reversed())
            .limit(5)
            .collect(Collectors.toList());
        
        System.out.println("\n🏆 Top 5 Students by GPA:");
        topStudents.forEach(student -> 
            System.out.printf("  %s - GPA: %.2f%n", student.getName(), student.getGPA()));
        
        // Group students by GPA range
        Map<String, List<Student>> gradeCategories = students.values().stream()
            .filter(s -> !s.getEnrollments().isEmpty())
            .collect(Collectors.groupingBy(student -> {
                double gpa = student.getGPA();
                if (gpa >= 90) return "Excellent (90+)";
                if (gpa >= 80) return "Good (80-89)";
                if (gpa >= 70) return "Average (70-79)";
                return "Below Average (<70)";
            }));
        
        System.out.println("\n📈 Students by Performance Category:");
        gradeCategories.forEach((category, studentList) ->
            System.out.printf("  %s: %d students%n", category, studentList.size()));
        
        // Course enrollment statistics
        Map<String, Long> courseEnrollments = students.values().stream()
            .flatMap(student -> student.getEnrollments().keySet().stream())
            .collect(Collectors.groupingBy(courseId -> 
                courses.stream()
                       .filter(c -> c.getCourseId().equals(courseId))
                       .findFirst()
                       .map(Course::getCourseName)
                       .orElse("Unknown"), 
                Collectors.counting()));
        
        System.out.println("\n📚 Course Enrollment Numbers:");
        courseEnrollments.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("  %s: %d students%n", entry.getKey(), entry.getValue()));
        
        System.out.println();
    }
    
    private void demonstrateFileOperations() throws IOException {
        System.out.println("💾 FILE I/O OPERATIONS");
        System.out.println("======================");
        
        // Create data directory
        Path dataDir = Paths.get("student_data");
        Files.createDirectories(dataDir);
        
        // Export student data to CSV
        Path csvFile = dataDir.resolve("students.csv");
        exportStudentsToCSV(csvFile);
        
        // Export course data to JSON-like format
        Path jsonFile = dataDir.resolve("courses.json");
        exportCoursesToJSON(jsonFile);
        
        // Read and validate files
        long csvLines = Files.lines(csvFile).count();
        long jsonLines = Files.lines(jsonFile).count();
        
        System.out.printf("✅ Exported %d lines to CSV file%n", csvLines);
        System.out.printf("✅ Exported %d lines to JSON file%n", jsonLines);
        System.out.printf("📁 Files saved in: %s%n%n", dataDir.toAbsolutePath());
    }
    
    private void demonstrateMultithreading() throws InterruptedException {
        System.out.println("🧵 MULTITHREADING DEMONSTRATION");
        System.out.println("===============================");
        
        // Process grades concurrently for different courses
        List<CompletableFuture<CourseStatistics>> futures = courses.stream()
            .map(course -> CompletableFuture.supplyAsync(() -> 
                gradeProcessor.processCourseStatistics(course, students.values()), 
                executorService))
            .collect(Collectors.toList());
        
        // Wait for all processing to complete
        CompletableFuture<List<CourseStatistics>> allStats = 
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        
        List<CourseStatistics> statistics = allStats.join();
        
        System.out.println("📊 Course Statistics (processed concurrently):");
        statistics.forEach(stat -> {
            System.out.printf("  %s:%n", stat.getCourseName());
            System.out.printf("    Enrolled: %d, Avg Grade: %.1f, Pass Rate: %.1f%%%n",
                stat.getEnrolledCount(), stat.getAverageGrade(), stat.getPassRate());
        });
        
        System.out.println();
    }
    
    private void generateReports() throws IOException {
        System.out.println("📋 GENERATING COMPREHENSIVE REPORTS");
        System.out.println("==================================");
        
        // Generate different types of reports
        reportGenerator.generateStudentReport(students.values());
        reportGenerator.generateCourseReport(courses, students.values());
        reportGenerator.generateSummaryReport(students.values(), courses);
        
        System.out.println("✅ All reports generated successfully!\n");
    }
    
    // Helper methods
    private void exportStudentsToCSV(Path csvFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(csvFile))) {
            writer.println("Student_ID,Name,Email,Birth_Date,GPA,Courses_Enrolled");
            
            students.values().forEach(student -> {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",%.2f,%d%n",
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getBirthDate(),
                    student.getGPA(),
                    student.getEnrollments().size()
                );
            });
        }
    }
    
    private void exportCoursesToJSON(Path jsonFile) throws IOException {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(jsonFile))) {
            writer.println("[");
            
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                long enrollmentCount = students.values().stream()
                    .mapToLong(s -> s.getEnrollments().containsKey(course.getCourseId()) ? 1 : 0)
                    .sum();
                
                writer.printf("  {%n");
                writer.printf("    \"courseId\": \"%s\",%n", course.getCourseId());
                writer.printf("    \"courseName\": \"%s\",%n", course.getCourseName());
                writer.printf("    \"credits\": %d,%n", course.getCredits());
                writer.printf("    \"instructor\": \"%s\",%n", course.getInstructor());
                writer.printf("    \"enrolledStudents\": %d%n", enrollmentCount);
                writer.printf("  }%s%n", i < courses.size() - 1 ? "," : "");
            }
            
            writer.println("]");
        }
    }
    
    private String generateRandomName() {
        String[] firstNames = {"Alice", "Bob", "Charlie", "Diana", "Eva", "Frank", "Grace", "Henry", "Ivy", "Jack"};
        String[] lastNames = {"Anderson", "Brown", "Clark", "Davis", "Evans", "Garcia", "Harris", "Johnson", "Lee", "Martinez"};
        
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + 
               lastNames[random.nextInt(lastNames.length)];
    }
}

// ==================== DOMAIN CLASSES ====================

class Student {
    private final int studentId;
    private final String name;
    private final String email;
    private final LocalDate birthDate;
    private final Map<String, Double> enrollments; // courseId -> grade
    
    public Student(int studentId, String name, String email, LocalDate birthDate) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.enrollments = new HashMap<>();
    }
    
    public void enrollInCourse(String courseId, double grade) {
        enrollments.put(courseId, grade);
    }
    
    public double getGPA() {
        if (enrollments.isEmpty()) return 0.0;
        
        return enrollments.values().stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);
    }
    
    // Getters
    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public Map<String, Double> getEnrollments() { return new HashMap<>(enrollments); }
    
    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', gpa=%.2f}", 
            studentId, name, getGPA());
    }
}

class Course {
    private final String courseId;
    private final String courseName;
    private final int credits;
    private final String instructor;
    
    public Course(String courseId, String courseName, int credits, String instructor) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
    }
    
    // Getters
    public String getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getInstructor() { return instructor; }
    
    @Override
    public String toString() {
        return String.format("Course{%s: %s (%d credits)}", courseId, courseName, credits);
    }
}

// ==================== PROCESSOR CLASSES ====================

class GradeProcessor {
    
    public CourseStatistics processCourseStatistics(Course course, Collection<Student> allStudents) {
        // Simulate some processing time
        try {
            Thread.sleep(100 + (int)(Math.random() * 200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        List<Double> grades = allStudents.stream()
            .filter(student -> student.getEnrollments().containsKey(course.getCourseId()))
            .map(student -> student.getEnrollments().get(course.getCourseId()))
            .collect(Collectors.toList());
        
        if (grades.isEmpty()) {
            return new CourseStatistics(course.getCourseName(), 0, 0.0, 0.0);
        }
        
        double averageGrade = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double passRate = grades.stream().mapToDouble(grade -> grade >= 70.0 ? 1.0 : 0.0).average().orElse(0.0) * 100;
        
        return new CourseStatistics(course.getCourseName(), grades.size(), averageGrade, passRate);
    }
}

class CourseStatistics {
    private final String courseName;
    private final int enrolledCount;
    private final double averageGrade;
    private final double passRate;
    
    public CourseStatistics(String courseName, int enrolledCount, double averageGrade, double passRate) {
        this.courseName = courseName;
        this.enrolledCount = enrolledCount;
        this.averageGrade = averageGrade;
        this.passRate = passRate;
    }
    
    // Getters
    public String getCourseName() { return courseName; }
    public int getEnrolledCount() { return enrolledCount; }
    public double getAverageGrade() { return averageGrade; }
    public double getPassRate() { return passRate; }
}

class ReportGenerator {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public void generateStudentReport(Collection<Student> students) throws IOException {
        Path reportFile = Paths.get("student_data", "student_report.txt");
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportFile))) {
            writer.println("STUDENT PERFORMANCE REPORT");
            writer.println("Generated: " + LocalDateTime.now().format(formatter));
            writer.println("=".repeat(50));
            writer.println();
            
            students.stream()
                .filter(s -> !s.getEnrollments().isEmpty())
                .sorted(Comparator.comparing(Student::getGPA).reversed())
                .forEach(student -> {
                    writer.printf("Student: %s (ID: %d)%n", student.getName(), student.getStudentId());
                    writer.printf("  GPA: %.2f%n", student.getGPA());
                    writer.printf("  Courses: %d%n", student.getEnrollments().size());
                    writer.println();
                });
        }
        
        System.out.println("📄 Student report saved to: " + reportFile);
    }
    
    public void generateCourseReport(List<Course> courses, Collection<Student> students) throws IOException {
        Path reportFile = Paths.get("student_data", "course_report.txt");
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportFile))) {
            writer.println("COURSE ENROLLMENT REPORT");
            writer.println("Generated: " + LocalDateTime.now().format(formatter));
            writer.println("=" .repeat(50));
            writer.println();
            
            courses.forEach(course -> {
                long enrollmentCount = students.stream()
                    .mapToLong(s -> s.getEnrollments().containsKey(course.getCourseId()) ? 1 : 0)
                    .sum();
                
                writer.printf("Course: %s%n", course.getCourseName());
                writer.printf("  ID: %s%n", course.getCourseId());
                writer.printf("  Instructor: %s%n", course.getInstructor());
                writer.printf("  Credits: %d%n", course.getCredits());
                writer.printf("  Enrolled Students: %d%n", enrollmentCount);
                writer.println();
            });
        }
        
        System.out.println("📄 Course report saved to: " + reportFile);
    }
    
    public void generateSummaryReport(Collection<Student> students, List<Course> courses) throws IOException {
        Path reportFile = Paths.get("student_data", "summary_report.txt");
        
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(reportFile))) {
            writer.println("SYSTEM SUMMARY REPORT");
            writer.println("Generated: " + LocalDateTime.now().format(formatter));
            writer.println("=" .repeat(50));
            writer.println();
            
            // Overall statistics
            long activeStudents = students.stream().filter(s -> !s.getEnrollments().isEmpty()).count();
            double systemGPA = students.stream()
                .filter(s -> !s.getEnrollments().isEmpty())
                .mapToDouble(Student::getGPA)
                .average().orElse(0.0);
            
            writer.printf("Total Students: %d%n", students.size());
            writer.printf("Active Students: %d%n", activeStudents);
            writer.printf("Total Courses: %d%n", courses.size());
            writer.printf("System Average GPA: %.2f%n", systemGPA);
            writer.println();
            
            // Performance distribution
            Map<String, Long> performanceDistribution = students.stream()
                .filter(s -> !s.getEnrollments().isEmpty())
                .collect(Collectors.groupingBy(student -> {
                    double gpa = student.getGPA();
                    if (gpa >= 90) return "Excellent";
                    if (gpa >= 80) return "Good";
                    if (gpa >= 70) return "Average";
                    return "Below Average";
                }, Collectors.counting()));
            
            writer.println("Performance Distribution:");
            performanceDistribution.forEach((category, count) ->
                writer.printf("  %s: %d students%n", category, count));
        }
        
        System.out.println("📄 Summary report saved to: " + reportFile);
    }
}