import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

public class Test {
    public static void main(String[] args) {

        var arr = List.of("1", "2", "3");

        arr.stream().filter(s -> s.equals("2")).forEach(System.out::println);

        Map<String, Object> map = new java.util.HashMap<>();
        map.put("leon", 1);
        map.put("wang", 2);

        map.forEach((k, v) -> System.out.printf("%s %s%n", k, v));

        System.out.println();

        var a = Optional.of(5);

        System.out.println(a.isPresent());
        System.out.println(a.isEmpty());
        System.out.println(a.get());
        System.out.println(a.orElse(10));

        a.ifPresentOrElse((value) -> System.out.println("YES"), () -> System.out.println("NO"));

        a.ifPresent(v -> System.out.println(v));

        System.out.println(Objects.equal("a", "b"));

        System.out.println(Joiner.on(',').join("a", "b", "c"));

        System.out.println(Splitter.on(',').trimResults().omitEmptyStrings().split("a, b   , c"));

        Preconditions.checkArgument(false, "should not be false");
    }
}
