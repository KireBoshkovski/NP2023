package K2.ex4;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

class File implements Comparable<File> {
    private final String name;
    private final Integer size;
    private final LocalDateTime createdAt;

    public File(String name, Integer size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }

    @Override
    public int compareTo(File o) {
        int created = this.createdAt.compareTo(o.createdAt);
        if (created == 0) {
            int name = this.name.compareTo(o.name);
            if (name == 0) {
                return this.size.compareTo(o.size);
            }
            return name;
        }
        return created;
    }
}

class FileSystem {
    private final Map<Character, Set<File>> filesByFolder;

    public FileSystem() {
        this.filesByFolder = new HashMap<>();
    }

    public void addFile(char folder, String fileName, int fileSize, LocalDateTime dateTime) {
        File file = new File(fileName, fileSize, dateTime);
        filesByFolder.putIfAbsent(folder, new TreeSet<>());
        filesByFolder.get(folder).add(file);
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        List<File> result = new ArrayList<>();

        filesByFolder.values()
                .forEach(set -> set.stream() //stream of files from each set
                        .filter(file -> file.getName().startsWith("."))
                        .filter(file -> file.getSize() < size)
                        .forEach(result::add)
                );

        return result;
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders) {
        return folders.stream().mapToInt(folder -> filesByFolder.get(folder).stream().mapToInt(File::getSize).sum()).sum();
    }

    public Map<Integer, Set<File>> byYear() {
        Map<Integer, Set<File>> byYear = new TreeMap<>();

        filesByFolder.values().forEach(set -> set.forEach(file -> {
                    byYear.putIfAbsent(file.getCreatedAt().getYear(), new TreeSet<>());
                    byYear.get(file.getCreatedAt().getYear()).add(file);
                }
        ));

        return byYear;
    }

    public Map<String, Long> sizeByMonthAndDay() {
        Map<String, Long> sizeByMonthAndDay = new TreeMap<>(Comparator.naturalOrder());

        filesByFolder.values().forEach(set -> set.forEach(file -> {
            Long size = Long.valueOf(file.getSize());
            String date = file.getCreatedAt().getMonth().name() + "-" + file.getCreatedAt().getDayOfMonth();
            sizeByMonthAndDay.putIfAbsent(date, 0L);
            sizeByMonthAndDay.put(date, size + sizeByMonthAndDay.get(date));
        }));

        return sizeByMonthAndDay;
    }
}


