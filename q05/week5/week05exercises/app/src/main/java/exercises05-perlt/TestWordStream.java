// Week 3
// sestoft@itu.dk * 2015-09-09
package exercises05;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import java.util.function.Supplier;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.Length;
import org.checkerframework.common.reflection.qual.GetClass;

public class TestWordStream {
  public static void main(String[] args) throws Exception {

    // Stream<String> stream = readWords(fileName);
    String fileName = "C:\\Users\\Nikolai\\Documents\\perlt\\ITU\\1.semester\\pcpp\\lectures\\week05\\code-exercises\\week05exercises\\app\\src\\main\\resources\\english-words.txt";
    Supplier<Stream<String>> streamSupplier = () -> {
      try {
        return readWords(fileName);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    };

    // 5.2

    // 1
    // System.out.println(streamSupplier.get().count());

    // // 2
    // streamSupplier.get().limit(100).forEach(System.out::println);

    // // 3
    // streamSupplier.get().filter(e -> e.length() >=
    // 22).forEach(System.out::println);
    // // 4
    // streamSupplier.get().filter(e -> e.length() >=
    // 22).limit(1).forEach(System.out::println);

    // // 5
    // // real 0m1.456s
    // streamSupplier.get().filter(TestWordStream::isPalindrom).forEach(System.out::println);

    // // 6
    // // real 0m1.379s
    // streamSupplier.get().parallel().filter(TestWordStream::isPalindrom).forEach(System.out::println);

    // System.out.println(streamSupplier.get().mapToInt(String::length).average().getAsDouble());
    // System.out.println(streamSupplier.get().mapToInt(String::length).min().getAsInt());
    // System.out.println(streamSupplier.get().mapToInt(String::length).max().getAsInt());

    // // 8
    // Map<Integer, List<String>> grouping =
    // streamSupplier.get().collect(Collectors.groupingBy(String::length));

    // // 9
    // streamSupplier.get().map(TestWordStream::letters).limit(100).forEach(System.out::println);

    // 10
    // System.out.println(streamSupplier.get().map(TestWordStream::letters).reduce(0,
    // (z, e) -> z + e.getOrDefault('e', 0),
    // Integer::sum));

    // 11
    // real 0m11.129s
    System.out.println(streamSupplier.get().map(TestWordStream::letters).collect(Collectors.groupingBy(e -> e)).values()
        .stream().filter(e -> e.size() > 1).count());

    // 12
    // real 1m5.923s
    System.out.println(streamSupplier.get().parallel().map(TestWordStream::letters)
        .collect(Collectors.groupingBy(e -> e)).values().stream().filter(e -> e.size() > 1).count());

    // 13
    // real 0m3.734s
    System.out.println(streamSupplier.get().parallel().map(TestWordStream::letters)
        .collect(Collectors.groupingByConcurrent(e -> e)).values().stream().filter(e -> e.size() > 1).count());

    // 5.3

    // 1
    final int N = 10_000_001;

    int[] a = new int[N];
    Arrays.parallelSetAll(a, e -> isPrime(e) ? 1 : 0);

    // // 2
    Arrays.parallelPrefix(a, Integer::sum);

    int debug = 1;
  }

  public static boolean isPalindrom(String s) {
    int n = s.length();
    for (int i = 0; i < (n / 2); ++i) {
      if (s.charAt(i) != s.charAt(n - i - 1)) {
        return false;
      }
    }
    return true;
  }

  public static Stream<String> readWords(String filename) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    return reader.lines();
  }

  public static Map<Character, Integer> letters(String s) {
    Map<Character, Integer> res = new TreeMap<>();
    s = s.toLowerCase();

    for (char c : s.toCharArray()) {
      res.put(c, res.getOrDefault(c, 0) + 1);
    }

    return res;
  }

  public static boolean isPrime(int num) {
    if (num <= 1) {
      return false;
    }
    for (int i = 2; i <= Math.sqrt(num); i++) {
      if (num % i == 0) {
        return false;
      }
    }
    return true;
  }
}
