package K2.ex16;

import java.util.*;
import java.util.stream.Collectors;

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for (int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for (int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде

class BlockContainer<T extends Comparable<T>> {
    private int size;
    List<TreeSet<T>> blocks;

    public BlockContainer(int size) {
        this.blocks = new ArrayList<>();
        blocks.add(new TreeSet<>());
        this.size = size;
    }

    public void add(T element) {
        TreeSet<T> lastBlock = blocks.get(blocks.size() - 1);
        if (lastBlock.size() == size) {
            lastBlock = new TreeSet<>();
            blocks.add(lastBlock);
        }
        lastBlock.add(element);
    }

    public void remove(T element) {
        TreeSet<T> lastBlock = blocks.get(blocks.size() - 1);
        lastBlock.remove(element);

        if (lastBlock.isEmpty()) {
            blocks.remove(lastBlock);
        }

    }

    public void sort() {
        List<T> all = new ArrayList<>();
        for (TreeSet<T> set : blocks) {
            all.addAll(set);
        }
        all = all.stream().sorted().collect(Collectors.toList());
        BlockContainer<T> sortedContainer = new BlockContainer<>(size);
        all.forEach(sortedContainer::add);
        this.blocks = sortedContainer.blocks;
    }

    @Override
    public String toString() {
        return blocks.stream()
                .map(block -> block.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining("],[", "[", "]"));
    }
}

