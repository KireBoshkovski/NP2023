package K2.ex22;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        events.putIfAbsent(date, new TreeSet<>(Comparator.comparing(Event::getDate).thenComparing(Event::getName)));
        events.get(date).add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        boolean contains = false;
        for (Date dates : events.keySet()) {
            if (formatter.format(date).equals(formatter.format(dates))) {
                events.get(dates).forEach(System.out::println);
                contains = true;
            }
        }
        if (!contains) {
            System.out.println("No events on this day!");
        }
    }

    public void listByMonth() {
        int[] monthCount = new int[12];
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        for (Date date : events.keySet()) {
            int month = Integer.parseInt(monthFormat.format(date)) - 1;
            monthCount[month] += events.get(date).size();
        }
        for (int i = 0; i < 12; i++) {
            System.out.println((i + 1) + " : " + monthCount[i]);
        }
    }
}