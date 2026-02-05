package basics;

import java.util.Random;
import java.util.StringJoiner;

enum Weekday {
    SUN(1), MON(2), TUE(3), WED(4), THU(5), FRI(6), SAT(7);

    public final int value;

    private Weekday(int value) {
        this.value = value;
    }

}

public class Test {
    public static void main(String[] args) {
        var str = "hello, world";
        System.out.println("This is a test class.");
        System.out.println(str);
        // StringBuilder和StringJoiner
        var strBuilder = new StringBuilder();
        for (var i : str.toCharArray()) {
            strBuilder.append(i);
        }
        var sj = new StringJoiner("-");
        for (var i : str.toCharArray()) {
            sj.add(String.valueOf(i));
        }
        System.out.println(str.length() + "" + strBuilder.toString());
        System.out.println(sj.toString());

        System.out.println(String.join("-", "aa", "bb", "cc"));

        // Enum
        var wk = Weekday.MON;
        System.out.println(wk.value);
        System.out.println(wk.name());

        // Math
        System.out.println(Math.max(10, 20));
        System.out.println(Math.min(10, 20));
        System.out.println(Math.sqrt(16));
        System.out.println(Math.pow(2, 3));
        System.out.println(Math.abs(-5));
        System.out.println(Math.round(3.14));
        System.out.println(Math.ceil(3.14));
        System.out.println(Math.floor(3.14));

        // Random
        var rd = new Random();
        System.out.println(rd.nextInt(100)); // 0-99
        System.out.println(rd.nextDouble()); // 0.0-1.0
        System.out.println(rd.nextBoolean()); // true/false


    }
}
