package K2.ex9;

import java.util.*;

public class StadiumTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

class SeatNotAllowedException extends Exception {
}

class SeatTakenException extends Exception {
}

class Sector {
    private String code;
    private int numSeats;
    // value -> 0 - neutral, 1 - home, 2 - away
    private int type;
    private Set<Integer> takenSeats;

    public Sector(String code, int numSeats) {
        this.code = code;
        this.numSeats = numSeats;
        this.takenSeats = new HashSet<>();
        this.type = 0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public Set<Integer> getTakenSeats() {
        return takenSeats;
    }

    public void takeSeat(int seat) {
        takenSeats.add(seat);
    }

    public double getPercentage() {
        return ((double) (takenSeats.size() * 100) / numSeats );
    }

    @Override
    public String toString() {
        //H	88/100	12.0%
        return String.format("%s\t%d/%d\t%.1f%%", code, numSeats - takenSeats.size(), numSeats, getPercentage());
    }
}

class Stadium {
    private String name;
    // key -> sectorName
    private HashMap<String, Sector> sectors;

    public Stadium(String name) {
        this.name = name;
        this.sectors = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for (int i = 0; i < sectorSizes.length; i++) {
            Sector sector = new Sector(sectorNames[i], sectorSizes[i]);
            sectors.put(sectorNames[i], sector);
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (sectors.get(sectorName).getTakenSeats().contains(seat)) {
            throw new SeatTakenException();
        }
        if (type != 0 && sectors.get(sectorName).getType() != 0 && sectors.get(sectorName).getType() != type) {
            throw new SeatNotAllowedException();
        }
        if (type != 0) {
            sectors.get(sectorName).setType(type);
        }
        sectors.get(sectorName).takeSeat(seat);
    }

    public void showSectors() {
        sectors.values()
                .stream()
                .sorted(
                        Comparator.comparing(Sector::getPercentage)
                                .thenComparing(Sector::getCode)
                )
                .forEach(System.out::println);
    }
}