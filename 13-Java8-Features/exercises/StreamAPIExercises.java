/**
 * Stream API Exercises - Beginner to Advanced
 * 
 * Practice exercises for mastering Java 8 Stream API operations,
 * collectors, and parallel processing.
 */

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StreamAPIExercises {
    
    public static void main(String[] args) {
        System.out.println("=== Stream API Exercises ===");
        
        basicStreamExercises();
        intermediateStreamExercises();
        collectorsExercises();
        advancedStreamExercises();
        parallelStreamExercises();
    }
    
    public static void basicStreamExercises() {
        System.out.println("\n1. Basic Stream Operations");
        System.out.println("==========================");
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Exercise 1a: Filter even numbers
        System.out.println("Exercise 1a: Filter even numbers");
        List<Integer> evenNumbers = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("Even numbers: " + evenNumbers);
        
        // Exercise 1b: Square all numbers
        System.out.println("\nExercise 1b: Square all numbers");
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("Squares: " + squares);
        
        // Exercise 1c: Sum of all numbers
        System.out.println("\nExercise 1c: Sum of all numbers");
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        
        // Exercise 1d: Find maximum number
        System.out.println("\nExercise 1d: Find maximum number");
        OptionalInt max = numbers.stream()
            .mapToInt(Integer::intValue)
            .max();
        System.out.println("Maximum: " + max.orElse(-1));
        
        // Exercise 1e: Count numbers greater than 5
        System.out.println("\nExercise 1e: Count numbers greater than 5");
        long count = numbers.stream()
            .filter(n -> n > 5)
            .count();
        System.out.println("Count > 5: " + count);
    }
    
    public static void intermediateStreamExercises() {
        System.out.println("\n2. Intermediate Stream Operations");
        System.out.println("=================================");
        
        List<String> words = Arrays.asList("apple", "banana", "apricot", "cherry", "avocado", "blueberry");
        
        // Exercise 2a: Group words by first letter
        System.out.println("Exercise 2a: Group words by first letter");
        Map<Character, List<String>> wordsByFirstLetter = words.stream()
            .collect(Collectors.groupingBy(word -> word.charAt(0)));
        wordsByFirstLetter.forEach((letter, wordList) -> 
            System.out.println(letter + ": " + wordList));
        
        // Exercise 2b: Find longest word
        System.out.println("\nExercise 2b: Find longest word");
        Optional<String> longestWord = words.stream()
            .max(Comparator.comparing(String::length));
        System.out.println("Longest word: " + longestWord.orElse("None"));
        
        // Exercise 2c: Sort words by length, then alphabetically
        System.out.println("\nExercise 2c: Sort words by length, then alphabetically");
        List<String> sortedWords = words.stream()
            .sorted(Comparator.comparing(String::length)
                   .thenComparing(String::compareTo))
            .collect(Collectors.toList());
        System.out.println("Sorted words: " + sortedWords);
        
        // Exercise 2d: Get unique characters from all words
        System.out.println("\nExercise 2d: Get unique characters from all words");
        Set<Character> uniqueChars = words.stream()
            .flatMapToInt(String::chars)
            .mapToObj(c -> (char) c)
            .collect(Collectors.toSet());
        System.out.println("Unique characters: " + uniqueChars);
        
        // Exercise 2e: Check if any word starts with 'a'
        System.out.println("\nExercise 2e: Check if any word starts with 'a'");
        boolean anyStartsWithA = words.stream()
            .anyMatch(word -> word.startsWith("a"));
        System.out.println("Any word starts with 'a': " + anyStartsWithA);
    }
    
    public static void collectorsExercises() {
        System.out.println("\n3. Collectors Exercises");
        System.out.println("=======================");
        
        List<Student> students = createStudentList();
        
        // Exercise 3a: Group students by grade
        System.out.println("Exercise 3a: Group students by grade");
        Map<Character, List<Student>> studentsByGrade = students.stream()
            .collect(Collectors.groupingBy(Student::getGrade));
        studentsByGrade.forEach((grade, studentList) -> {
            System.out.println("Grade " + grade + ": " + 
                studentList.stream().map(Student::getName).collect(Collectors.toList()));
        });
        
        // Exercise 3b: Calculate average score by subject
        System.out.println("\nExercise 3b: Calculate average score by subject");
        Map<String, Double> avgScoreBySubject = students.stream()
            .collect(Collectors.groupingBy(
                Student::getSubject,
                Collectors.averagingDouble(Student::getScore)
            ));
        avgScoreBySubject.forEach((subject, avg) -> 
            System.out.printf("%s: %.2f%n", subject, avg));
        
        // Exercise 3c: Count students by subject
        System.out.println("\nExercise 3c: Count students by subject");
        Map<String, Long> studentCountBySubject = students.stream()
            .collect(Collectors.groupingBy(
                Student::getSubject,
                Collectors.counting()
            ));
        studentCountBySubject.forEach((subject, count) -> 
            System.out.println(subject + ": " + count + " students"));
        
        // Exercise 3d: Get statistics for all scores
        System.out.println("\nExercise 3d: Get statistics for all scores");
        DoubleSummaryStatistics scoreStats = students.stream()
            .collect(Collectors.summarizingDouble(Student::getScore));
        System.out.println("Score Statistics:");
        System.out.printf("Count: %d, Average: %.2f, Min: %.1f, Max: %.1f%n",
            scoreStats.getCount(), scoreStats.getAverage(), 
            scoreStats.getMin(), scoreStats.getMax());
        
        // Exercise 3e: Partition students by passing score (>= 70)
        System.out.println("\nExercise 3e: Partition students by passing score (>= 70)");
        Map<Boolean, List<Student>> passingPartition = students.stream()
            .collect(Collectors.partitioningBy(s -> s.getScore() >= 70));
        System.out.println("Passing students: " + 
            passingPartition.get(true).stream()
                .map(Student::getName).collect(Collectors.toList()));
        System.out.println("Failing students: " + 
            passingPartition.get(false).stream()
                .map(Student::getName).collect(Collectors.toList()));
    }
    
    public static void advancedStreamExercises() {
        System.out.println("\n4. Advanced Stream Operations");
        System.out.println("=============================");
        
        // Exercise 4a: Custom collector to join names with grades
        System.out.println("Exercise 4a: Custom collector to join names with grades");
        List<Student> students = createStudentList();
        
        String nameGradeString = students.stream()
            .collect(Collector.of(
                StringBuilder::new,
                (sb, student) -> sb.append(student.getName())
                                   .append("(").append(student.getGrade()).append("), "),
                StringBuilder::append,
                sb -> sb.length() > 2 ? sb.substring(0, sb.length() - 2) : sb.toString()
            ));
        System.out.println("Names with grades: " + nameGradeString);
        
        // Exercise 4b: Find top 3 students by score in each subject
        System.out.println("\nExercise 4b: Find top 3 students by score in each subject");
        Map<String, List<Student>> top3BySubject = students.stream()
            .collect(Collectors.groupingBy(
                Student::getSubject,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> list.stream()
                               .sorted(Comparator.comparing(Student::getScore).reversed())
                               .limit(3)
                               .collect(Collectors.toList())
                )
            ));
        
        top3BySubject.forEach((subject, topStudents) -> {
            System.out.println(subject + " top 3:");
            topStudents.forEach(s -> 
                System.out.printf("  %s: %.1f%n", s.getName(), s.getScore()));
        });
        
        // Exercise 4c: Calculate weighted average (assume equal weights)
        System.out.println("\nExercise 4c: Calculate weighted average for each student");
        Map<String, Double> studentAverages = students.stream()
            .collect(Collectors.groupingBy(
                Student::getName,
                Collectors.averagingDouble(Student::getScore)
            ));
        
        studentAverages.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> 
                System.out.printf("%s: %.2f%n", entry.getKey(), entry.getValue()));
        
        // Exercise 4d: Generate infinite stream and process
        System.out.println("\nExercise 4d: Generate fibonacci sequence (first 10 numbers)");
        Stream.iterate(new int[]{0, 1}, fib -> new int[]{fib[1], fib[0] + fib[1]})
            .limit(10)
            .map(fib -> fib[0])
            .forEach(n -> System.out.print(n + " "));
        System.out.println();
    }
    
    public static void parallelStreamExercises() {
        System.out.println("\n5. Parallel Stream Exercises");
        System.out.println("============================");
        
        // Exercise 5a: Performance comparison - sequential vs parallel
        System.out.println("Exercise 5a: Performance comparison - sum of squares");
        
        List<Integer> largeList = IntStream.rangeClosed(1, 1_000_000)
            .boxed()
            .collect(Collectors.toList());
        
        // Sequential processing
        long startTime = System.currentTimeMillis();
        long sequentialSum = largeList.stream()
            .mapToLong(n -> (long) n * n)
            .sum();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        // Parallel processing
        startTime = System.currentTimeMillis();
        long parallelSum = largeList.parallelStream()
            .mapToLong(n -> (long) n * n)
            .sum();
        long parallelTime = System.currentTimeMillis() - startTime;
        
        System.out.println("Sequential sum: " + sequentialSum + " (Time: " + sequentialTime + "ms)");
        System.out.println("Parallel sum: " + parallelSum + " (Time: " + parallelTime + "ms)");
        System.out.println("Results match: " + (sequentialSum == parallelSum));
        
        // Exercise 5b: Parallel processing with custom thread pool
        System.out.println("\nExercise 5b: Word processing with parallel streams");
        List<String> texts = Arrays.asList(
            "The quick brown fox jumps over the lazy dog",
            "Java 8 introduced many functional programming features",
            "Stream API makes data processing much easier and more readable",
            "Parallel streams can improve performance for CPU-intensive operations"
        );
        
        Map<String, Long> wordCounts = texts.parallelStream()
            .flatMap(text -> Arrays.stream(text.split("\\s+")))
            .map(String::toLowerCase)
            .collect(Collectors.groupingByConcurrent(
                Function.identity(),
                Collectors.counting()
            ));
        
        wordCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> 
                System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
    
    // Helper method to create test data
    private static List<Student> createStudentList() {
        return Arrays.asList(
            new Student("Alice", "Math", 85.5, 'B'),
            new Student("Bob", "Math", 92.0, 'A'),
            new Student("Charlie", "Science", 78.5, 'C'),
            new Student("Diana", "Science", 88.0, 'B'),
            new Student("Eve", "English", 76.5, 'C'),
            new Student("Frank", "English", 95.0, 'A'),
            new Student("Grace", "Math", 89.5, 'B'),
            new Student("Henry", "Science", 82.0, 'B'),
            new Student("Ivy", "English", 91.5, 'A'),
            new Student("Jack", "Math", 65.0, 'D')
        );
    }
    
    // Student class for exercises
    static class Student {
        private String name;
        private String subject;
        private double score;
        private char grade;
        
        public Student(String name, String subject, double score, char grade) {
            this.name = name;
            this.subject = subject;
            this.score = score;
            this.grade = grade;
        }
        
        public String getName() { return name; }
        public String getSubject() { return subject; }
        public double getScore() { return score; }
        public char getGrade() { return grade; }
        
        @Override
        public String toString() {
            return String.format("Student{name='%s', subject='%s', score=%.1f, grade='%c'}", 
                               name, subject, score, grade);
        }
    }
}