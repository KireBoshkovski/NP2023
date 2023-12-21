package K2.ex22;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

class WrongDateException extends Exception {

    public WrongDateException(Date date) {
        super("Wrong date: " + date.toString());
    }
}

class Event {
    private String name;
    private String location;
    private Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyy HH:mm");
        String strDate = formatter.format(date);
        return String.format("%s at %s, %s", strDate, location, name);
    }
}

class EventCalendar {
    private Map<Date, Set<Event>> events;
    private int year;

    public EventCalendar(int year) {
        this.year = year;
        this.events = new HashMap<>();
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        if (year != Integer.parseInt(formatter.format(date))) {
            throw new WrongDateException(date);
        }
        events.putIfAbsent(date, new TreeSet<>(Comparator.comparing(Event::getDate).thenComparing(Event::getName).thenComparing(Event::getLocation)));
        events.get(date).add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        boolean contains = false;
        for (Date dates : events.keySet()) {
            if (formatter.format(date).equals(formatter.format(dates))){
                events.get(dates).forEach(System.out::println);
                contains = true;
            }
        }
        if (!contains) {
            System.out.println("No events on this day!");
        }
    }

    public void listByMonth() {
        HashMap<Integer, Integer> byMonth = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            byMonth.put(i, 0);
        }
        events.keySet().stream().forEach(date -> {
            SimpleDateFormat formatter = new SimpleDateFormat("M");
            Integer month = Integer.parseInt(formatter.format(date));
            int value = byMonth.get(month);
            byMonth.put(month, 1+value);
        });

        for (int i: byMonth.keySet()) {
            System.out.println(i + " : " + byMonth.get(i));
        }

        System.out.println();
        System.out.println(events.values().size());
    }
}