import java.util.Optional;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

public class Test {
    public static void main(String[] args) {
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

        Preconditions.checkArgument(false,"should not be false");
    }
}
