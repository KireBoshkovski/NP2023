package Labs.lab1.ex2;

import java.util.*;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    public static String toRoman(int n) {
        StringBuilder sb = new StringBuilder();

        Map<Integer, String> integerToRoman = new TreeMap<>(Comparator.reverseOrder());
        integerToRoman.put(1,"I");
        integerToRoman.put(4,"IV");
        integerToRoman.put(5,"V");
        integerToRoman.put(9,"IX");
        integerToRoman.put(10,"X");
        integerToRoman.put(40,"XL");
        integerToRoman.put(50,"L");
        integerToRoman.put(90,"XC");
        integerToRoman.put(100,"C");
        integerToRoman.put(400,"CD");
        integerToRoman.put(500,"D");
        integerToRoman.put(900,"CM");
        integerToRoman.put(1000,"M");

        for (Integer integer : integerToRoman.keySet()) {
            while (n >= integer){
                sb.append(integerToRoman.get(integer));
                n-= integer;
            }
        }
        return sb.toString();
    }

}

