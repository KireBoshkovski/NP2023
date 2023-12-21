package K2.ex15;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}
class Flight implements Comparable<Flight> {
    private final String from;
    private final String to;
    private final int time;
    private final int duration;
    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }
    public String getTo() {
        return to;
    }
    public int getTime() {
        return time;
    }
    private String getDurationTime() {
        Duration duration = Duration.ofMinutes(this.duration);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%dh%02dm", hours, minutes);
    }
    public String getDirection(){
        return String.format("%s-%s", from, to);
    }
    private String getClock() {
        LocalTime departureTime = LocalTime.MIN.plusMinutes(this.time);
        LocalTime arrivalTime = departureTime.plusMinutes(this.duration);
        long days = (this.time + this.duration) / 1440;
        if (days > 0) {
            return String.format("%s-%s +%dd", departureTime, arrivalTime, days);
        }
        return String.format("%s-%s", departureTime, arrivalTime);
    }
    @Override
    public String toString() {
        return String.format("%s %s %s", getDirection(), getClock(), getDurationTime());
    }

    @Override
    public int compareTo(Flight o) {
        return this.getDirection().compareTo(o.getDirection());
    }
}
class Airport {
    private final String name;
    private final String country;
    private final String code;
    private final int passengers;
    //all flights in the lists are direct
    private final TreeSet<Flight> arrivingFlights;
    private final TreeSet<Flight> departingFlights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        this.arrivingFlights = new TreeSet<>(Comparator.comparing(Flight::getTime).thenComparing(Flight::getDirection));
        this.departingFlights = new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime));
    }

    public void addArrivingFlight(Flight arriving) {
        arrivingFlights.add(arriving);
    }

    public void addDepartingFlight(Flight departing) {
        departingFlights.add(departing);
    }

    public TreeSet<Flight> getArrivingFlights() {
        return arrivingFlights;
    }

    public TreeSet<Flight> getDepartingFlights() {
        return departingFlights;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(code).append(")\n").append(country).append("\n").append(passengers);
        int i = 0;
        for (Flight departingFlight : departingFlights) {
            sb.append("\n").append(++i).append(". ").append(departingFlight);
        }
        return sb.toString();
    }
}

class Airports {
    private final Map<String, Airport> airports;

    public Airports() {
        this.airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        Airport airport = new Airport(name, country, code, passengers);
        airports.put(code, airport);
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        airports.get(from).addDepartingFlight(flight);
        airports.get(to).addArrivingFlight(flight);
    }

    public void showFlightsFromAirport(String code) {
        System.out.println(airports.get(code));
    }

    public void showDirectFlightsFromTo(String from, String to) {

        long c = airports.get(from).getDepartingFlights().stream().filter(flight -> flight.getTo().equals(to)).count();
        if (c == 0) {
            System.out.printf("No flights from %s to %s%n", from, to);
        }
        airports.get(from).getDepartingFlights().stream().filter(flight -> flight.getTo().equals(to)).forEach(System.out::println);
    }

    public void showDirectFlightsTo(String to) {
        airports.get(to).getArrivingFlights().forEach(System.out::println);
    }
}
