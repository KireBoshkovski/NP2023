package K2.ex23;

import java.util.*;
import java.util.stream.Collectors;

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}

class Student {
    private String index;
    private List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public double sumPoints() {
        return (double) points.stream().mapToInt(Integer::intValue).sum() / 10;
    }

    public boolean isPassed() {
        return points.size() >= 8;
    }

    @Override
    public String toString() {
        if (isPassed()) return String.format("%s YES %.2f", getIndex(), sumPoints());
        return String.format("%s NO %.2f", getIndex(), sumPoints());
    }
}

class LabExercises {
    HashSet<Student> studentsByIndex;

    public LabExercises() {
        this.studentsByIndex = new HashSet<>();
    }

    public void addStudent(Student student) {
        studentsByIndex.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        if (ascending) {
            studentsByIndex.stream()
                    .sorted(
                            Comparator.comparing(Student::sumPoints)
                                    .thenComparing(Student::getIndex)
                    ).limit(n)
                    .forEach(System.out::println);
        } else {
            studentsByIndex.stream()
                    .sorted(
                            Comparator.comparing(Student::sumPoints)
                                    .thenComparing(Student::getIndex)
                                    .reversed()

                    ).limit(n)
                    .forEach(System.out::println);
        }
    }

    public List<Student> failedStudents() {
        return studentsByIndex.stream()
                .filter(student -> !student.isPassed())
                .sorted(
                        Comparator.comparing(Student::getIndex)
                                .thenComparing(Student::sumPoints)
                ).collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        return studentsByIndex.stream()
                .filter(Student::isPassed)
                .collect(Collectors.groupingBy(
                        student -> 20 - Integer.parseInt(student.getIndex().substring(0, 2)),
                        Collectors.averagingDouble(Student::sumPoints)
                ));
    }
}