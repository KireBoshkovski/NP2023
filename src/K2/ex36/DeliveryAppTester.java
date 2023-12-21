package K2.ex36;

import java.util.*;

class User {
    private String id;
    private String name;
    private Map<String, Location> addressList;
    private int numOrders;
    private float totalSpending;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.addressList = new HashMap<>();
        this.numOrders = 0;
        this.totalSpending = 0;
    }

    public void addAddress(String addressName, Location location) {
        addressList.putIfAbsent(addressName, location);
    }

    public Location getAddressByName(String name) {
        return addressList.get(name);
    }

    public String getName() {
        return name;
    }

    public float getTotalSpending() {
        return totalSpending;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f"
                , id, name, numOrders, totalSpending, numOrders == 0 ? 0 : totalSpending / numOrders);
    }

    public void addOrder(float cost) {
        this.numOrders ++;
        this.totalSpending += cost;
    }
}

class DeliveryGuy {
    private String id;
    private String name;
    private Location currentLocation;
    private int numDeliveries;
    private int totalDeliveryFee;

    public DeliveryGuy(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.numDeliveries = 0;
        this.totalDeliveryFee = 0;
    }

    public void addDelivery(int points){

        this.totalDeliveryFee+= 90 + ((points/10) * 10);
        this.numDeliveries++;
    }

    public String getName() {
        return name;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public float getAverageDeliveryFee(){
        if(numDeliveries == 0) {
            return 0;
        }
        return (float) totalDeliveryFee / numDeliveries;
    }

    public float getTotalDeliveryFee() {
        return totalDeliveryFee;
    }

    public int compareDistanceToRestaurant (DeliveryGuy other, Location restaurantLocation) {
        int currentDistance = currentLocation.distance(restaurantLocation);
        int otherDistance = other.currentLocation.distance(restaurantLocation);
        if(currentDistance == otherDistance){
            return Integer.compare(this.numDeliveries, other.numDeliveries);
        }
        return currentDistance - otherDistance;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id, name, numDeliveries, getTotalDeliveryFee(), getAverageDeliveryFee());
    }
}

class Restaurant {
    private String id;
    private String name;
    private Location location;
    private int numOrders;
    private float totalEarnings;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.numOrders = 0;
        this.totalEarnings = 0;
    }

    public void addOrder(float cost) {
        this.numOrders++;
        this.totalEarnings+=cost;
    }

    public float getAverageOrder() {
        if (numOrders == 0) {
            return 0;
        }
        return totalEarnings / numOrders;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id, name, numOrders, totalEarnings, getAverageOrder());
    }
}

class DeliveryApp {
    private String name;
    //keys are id's
    private Map<String, DeliveryGuy> deliveryGuys;
    private Map<String, Restaurant> restaurants;
    private Map<String, User> users;

    public DeliveryApp(String appName) {
        this.name = appName;
        this.deliveryGuys = new TreeMap<>();
        this.restaurants = new TreeMap<>();
        this.users = new TreeMap<>();
    }

    public void registerDeliveryPerson(String id, String name, Location location) {
        deliveryGuys.putIfAbsent(id, new DeliveryGuy(id, name, location));
    }

    public void addRestaurant(String id, String name, Location location) {
        restaurants.putIfAbsent(id, new Restaurant(id, name, location));
    }

    public void addUser(String id, String name) {
        users.putIfAbsent(id, new User(id, name));
    }

    public void addAddress(String id, String addressName, Location location) {
        users.get(id).addAddress(addressName, location);
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        Location userLocation = users.get(userId).getAddressByName(userAddressName);
        Location restaurantLocation = restaurants.get(restaurantId).getLocation();
        //find the closest
        DeliveryGuy closestDelivery = deliveryGuys.values().stream()
                .min((l,r) -> l.compareDistanceToRestaurant(r, restaurantLocation))
                .orElse(null);
        int points = restaurantLocation.distance(closestDelivery.getCurrentLocation());
        closestDelivery.setCurrentLocation(userLocation);
        closestDelivery.addDelivery(points);
        restaurants.get(restaurantId).addOrder(cost);
        users.get(userId).addOrder(cost);
    }

    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::getTotalSpending).thenComparing(User::getName).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream()
                .sorted(Comparator.comparing(Restaurant::getAverageOrder).thenComparing(Restaurant::getName).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryGuys.values().stream()
                .sorted(Comparator.comparing(DeliveryGuy::getTotalDeliveryFee).thenComparing(DeliveryGuy::getName).reversed())
                .forEach(System.out::println);
    }
}


interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
