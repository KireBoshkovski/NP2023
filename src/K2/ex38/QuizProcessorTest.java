package K2.ex38;

import java.io.InputStream;
import java.util.*;

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}

class QuizProcessor {
    public static Map<String, Double> processAnswers(InputStream is) {
        Scanner scanner = new Scanner(is);
        Map<String, Double> map = new TreeMap<>();
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(";");
            String id = parts[0];
            String[] correct = parts[1].split(", ");
            String[] answers = parts[2].split(", ");
            if (correct.length != answers.length) {
                System.out.println("A quiz must have same number of correct and selected answers");
            } else {
                double points = 0;
                for (int i = 0; i < answers.length; i++) {
                    if (answers[i].equals(correct[i])) {
                        points++;
                    } else {
                        points -= 0.25;
                    }
                }
                map.putIfAbsent(id, points);
            }
        }
        return map;
    }
}