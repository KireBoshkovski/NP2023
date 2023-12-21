package K2.ex27;

import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde
class Names {
    TreeMap<String, Integer> names;

    public Names() {
        this.names = new TreeMap<>();
    }

    public void addName(String name) {
        names.putIfAbsent(name, 0);
        if (names.containsKey(name)) {
            int n = names.get(name);
            names.put(name, n + 1);
        }
    }

    public void printN(int n) {
        names.entrySet().stream()
                .filter(entry -> entry.getValue() >= n)
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println(entry.getKey() + " (" + entry.getValue() + ") " + uniqueLetters(entry.getKey())));
    }

    public String findName(int len, int x) {
        List<String> filteredNames = names.keySet()
                .stream()
                .filter(name -> name.length() < len)
                .sorted()
                .collect(Collectors.toList());

        return filteredNames.get(x % filteredNames.size());
    }

    private int uniqueLetters(String s) {
        return (int) s.toLowerCase().chars().distinct().count();

    }
}