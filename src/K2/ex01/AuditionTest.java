package K2.ex01;

import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Participant{
    private String city;
    private String code;
    private String name;
    private int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }
}

class Audition {
    private HashMap<String, HashMap<String ,Participant>> participantsByCity;

    public Audition() {
        this.participantsByCity = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        participantsByCity.putIfAbsent(city, new HashMap<>());

        Participant p = new Participant(city, code, name, age);

        participantsByCity.get(city).putIfAbsent(code, p);

    }

    public void listByCity(String city) {
        List<Participant> participants = new ArrayList<>(participantsByCity.get(city).values());

        participants.stream()
                .sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge))
                .forEach(System.out::println);
    }
}