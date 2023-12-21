package K2.ex6;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });
    }
}

abstract class Employee {
    private final String Id;
    private final String level;

    public Employee(String id, String level) {
        Id = id;
        this.level = level;
    }

    public String getId() {
        return Id;
    }

    abstract double getSalary();

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f ", Id, level, getSalary());
    }
}

class HourlyEmployee extends Employee {
    private final float hours;

    public HourlyEmployee(String id, String level, float hours) {
        super(id, level);
        this.hours = hours;
    }

    @Override
    double getSalary() {
        double m = PayrollSystem.hourlyRateByLevel.get(getLevel());
        return m * (Math.min(40, hours) + (Math.max(0, hours - 40) * 1.5)) + 0.01;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Regular hours: %.2f Overtime hours: %.2f", Math.min(40, hours), Math.max(0, hours - 40));
    }
}

class FreelanceEmployee extends Employee {
    private final ArrayList<Integer> points;

    public FreelanceEmployee(String id, String level, ArrayList<Integer> points) {
        super(id, level);
        this.points = points;
    }

    public int getTotalPoints() {
        return points.stream().mapToInt(i -> i).sum();
    }

    @Override
    double getSalary() {
        return (float) (getTotalPoints() * PayrollSystem.ticketRateByLevel.get(getLevel()));
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Tickets count: %d Tickets points: %d", points.size(), getTotalPoints());
    }
}


class PayrollSystem {
    static Map<String, Double> hourlyRateByLevel;
    static Map<String, Double> ticketRateByLevel;
    //key -> level
    private final Map<String, Set<Employee>> employees;

    PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        PayrollSystem.hourlyRateByLevel = hourlyRateByLevel;
        PayrollSystem.ticketRateByLevel = ticketRateByLevel;
        this.employees = new TreeMap<>(Comparator.naturalOrder());
    }

    public void readEmployees(InputStream in) {
        Scanner scanner = new Scanner(in);
        //F;e911a6;level6;3;4;5;8;3;8;7;5;5
        //H;157f3d;level10;63.14
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(";");
            String t = parts[0];
            String id = parts[1];
            String level = parts[2];
            Employee employee;
            if (t.equals("H")) {
                float hours = Float.parseFloat(parts[3]);
                employee = new HourlyEmployee(id, level, hours);
            } else {
                //0;1;2; |||| 3;4;5;8;3;8;7;5;5
                ArrayList<Integer> points = new ArrayList<>();
                for (int i = 3; i < parts.length; i++) {
                    points.add(Integer.parseInt(parts[i]));
                }
                employee = new FreelanceEmployee(id, level, points);
            }
            employees.putIfAbsent(level, new TreeSet<>(Comparator.comparing(Employee::getSalary).thenComparing(Employee::getId).reversed()));
            employees.get(level).add(employee);
        }
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(PrintStream out, Set<String> levels) {
        Map<String, Set<Employee>> result = new TreeMap<>(Comparator.naturalOrder());

        levels.forEach(level -> {
            if (employees.containsKey(level))
                result.put(level, employees.get(level));
        });

        return result;
    }

}