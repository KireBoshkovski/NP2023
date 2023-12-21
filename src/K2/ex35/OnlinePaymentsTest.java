package K2.ex35;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}

class Item {
    private String name;
    private int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;

    }

    @Override
    public String toString() {
        return String.format("%s %d", name, price);
    }
}

class Student {
    private String id;
    private Set<Item> items;

    public Student(String id) {
        this.items = new TreeSet<>(Comparator.comparing(Item::getPrice).thenComparing(Item::getName).reversed());
        this.id = id;
    }

    public void addItems(Item item) {
        items.add(item);
    }

    private int getNet() {
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    private int totalFee() {
        return (int) Math.min(300, Math.max(3, Math.round(items.stream().mapToInt(Item::getPrice).sum() * 0.0114)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(id);
        sb.append(" Net: ").append(getNet());
        sb.append(" Fee: ").append(totalFee());
        sb.append(" Total: ").append(totalFee() + getNet()).append("\n").append("Items:");
        int i = 1;
        for (Item item : items) {
            sb.append("\n").append(i++).append(". ").append(item);
        }

        return sb.append("\n").toString();
    }
}

class OnlinePayments {
    HashMap<String, Student> students;

    public OnlinePayments() {
        this.students = new HashMap<>();
    }

    public void readItems(InputStream in) {
        Scanner scanner = new Scanner(in);
        while (scanner.hasNextLine()) {
            String [] parts = scanner.nextLine().split(";");
            String id = parts[0];
            String itemName = parts[1];
            int price = Integer.parseInt(parts[2]);
            Item item = new Item(itemName, price);
            Student student = new Student(id);
            students.putIfAbsent(id, student);
            students.get(id).addItems(item);
        }
    }

    public void printStudentReport(String id, PrintStream out) {
        if(!students.containsKey(id)){
            System.out.println("Student " + id + " not found!");
        }else {
            PrintWriter pw = new PrintWriter(out);

            pw.print(students.get(id));

            pw.flush();
        }
    }
}