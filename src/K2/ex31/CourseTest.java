package K2.ex31;


import java.util.*;
import java.util.stream.Collectors;


public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

class Student {
    private String index;
    private String name;
    private int midterm1;
    private int midterm2;
    private int labs;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.midterm1 = 0;
        this.midterm2 = 0;
        this.labs = 0;
    }

    public String getIndex() {
        return index;
    }

    public void setMidterm1(int midterm1) {
        this.midterm1 = midterm1;
    }

    public void setMidterm2(int midterm2) {
        this.midterm2 = midterm2;
    }

    public void setLabs(int labs) {
        this.labs = labs;
    }

    public float getFinalPoints() {
        return (float) (midterm1 * 0.45 + midterm2 * 0.45 + labs);
    }

    public int getGrade() {
        return Math.max((int) Math.ceil(getFinalPoints() / 10.0), 5);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                index, name, midterm1, midterm2, labs, getFinalPoints(), getGrade());
    }
}

class AdvancedProgrammingCourse {
    //key -> index
    private HashMap<String, Student> students;

    public AdvancedProgrammingCourse() {
        this.students = new HashMap<>();
    }

    public void addStudent(Student student) {
        students.putIfAbsent(student.getIndex(), student);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        Student student = students.get(idNumber);
        if (activity.equals("midterm1")) {
            student.setMidterm1(points);
        } else if (activity.equals("midterm2")) {
            student.setMidterm2(points);
        } else if (activity.equals("labs")) {
            student.setLabs(points);
        } else {
            //System.out.println("Exception");
        }
    }

    public List<Student> getFirstNStudents(int n) {
        List<Student> sort = new ArrayList<>(students.values());
        return sort.stream()
                .sorted(
                        Comparator.comparing(Student::getFinalPoints)
                                .reversed()
                ).limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, Integer> grades = new HashMap<>();
        grades.put(5, 0);
        grades.put(6, 0);
        grades.put(7, 0);
        grades.put(8, 0);
        grades.put(9, 0);
        grades.put(10, 0);
        students.values().forEach(
                student -> {
                    int grade = student.getGrade();
                    int n = grades.get(grade);
                    grades.put(grade, n + 1);
                }
        );

        return grades;
    }

    public void printStatistics() {
        List<Student> passed = new ArrayList<>(
                students.values()
                        .stream()
                        .filter(student -> student.getGrade() > 5)
                        .collect(Collectors.toList())
        );
        double average = passed.stream()
                .mapToDouble(Student::getFinalPoints)
                .average()
                .orElse(0.0);
        double min = passed.stream()
                .mapToDouble(Student::getFinalPoints)
                .min()
                .orElse(0.0);
        double max = passed.stream()
                .mapToDouble(Student::getFinalPoints)
                .max()
                .orElse(0.0);
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                passed.size(), min, (float) average, max));
    }
}