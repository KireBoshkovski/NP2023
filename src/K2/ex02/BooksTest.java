package K2.ex02;

import java.util.*;

public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

class Book {
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }
}

class BookCollection {
    //key -> category
    private HashMap<String, TreeSet<Book>> books;

    public BookCollection() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) {
        String category = book.getCategory();
        books.putIfAbsent(category, new TreeSet<>(Comparator.comparing(Book::getTitle).thenComparing(Book::getPrice)));
        books.get(category).add(book);
    }

    public void printByCategory(String category) {
        books.get(category).forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        PriorityQueue<Book> queue = new PriorityQueue<>(Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle));

        for (TreeSet<Book> set : books.values()) {
            queue.addAll(set);
        }

        List<Book> cheapest = new ArrayList<>();

        for (int i = 0; i < n && !queue.isEmpty(); i++) {
            cheapest.add(queue.poll());
        }

        return cheapest;
    }
}