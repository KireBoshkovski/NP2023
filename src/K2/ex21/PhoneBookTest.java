package K2.ex21;

import java.util.*;

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }
}

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class Contact implements Comparable<Contact> {
    private String name;
    private String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }

    @Override
    public int compareTo(Contact o) {
        if (this.name.compareTo(o.name) == 0) {
            return this.number.compareTo(o.number);
        }
        return this.name.compareTo(o.name);
    }
}

class PhoneBook {
    // key -> name
    // value -> number
    private Set<String> allNumbers;
    private Map<String, Set<Contact>> byName;
    private Map<String, Set<Contact>> byNumber;


    public PhoneBook() {
        this.allNumbers = new HashSet<>();
        this.byName = new HashMap<>();
        this.byNumber = new TreeMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (allNumbers.contains(number)) {
            throw new DuplicateNumberException(number);
        }

        Contact contact = new Contact(name, number);

        allNumbers.add(number);

        byName.putIfAbsent(name, new TreeSet<>());
        byName.get(name).add(contact);

        byNumber.putIfAbsent(number, new TreeSet<>());
        byNumber.get(number).add(contact);

    }

    public void contactsByNumber(String number) {
        TreeSet<Contact> sortedContacts = new TreeSet<>();
        for (String num : byNumber.keySet()) {
            if (num.contains(number)) {
                sortedContacts.addAll(byNumber.get(num));
            }
        }

        if (sortedContacts.isEmpty()){
            System.out.println("NOT FOUND");
        } else {
            sortedContacts.forEach(System.out::println);
        }
    }

    public void contactsByName(String name) {
        if (byName.containsKey(name)) {
            for (Contact contact : byName.get(name)) {
                System.out.println(contact);
            }
        } else {
            System.out.println("NOT FOUND");
        }
    }
}



