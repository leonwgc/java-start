package advanced;

import java.util.*;
import java.util.function.*;

/**
 * 函数式接口详解（Java 8+）
 * 学习目标：
 * 1. 理解函数式接口的定义和作用
 * 2. 掌握Java内置的函数式接口（Function、Predicate、Consumer等）
 * 3. 学习自定义函数式接口
 * 4. 了解方法引用和构造器引用
 *
 * 函数式接口是什么？
 * - 只有一个抽象方法的接口（@FunctionalInterface注解）
 * - Lambda表达式的基础
 * - Java提供了大量内置函数式接口
 * - 支持方法引用，代码更简洁
 *
 * Spring应用：
 * - Stream API操作
 * - 异步回调处理
 * - 策略模式实现
 * - 事件监听器
 */
public class FunctionalInterfacesDemo {

    public static void main(String[] args) {
        System.out.println("=== 函数式接口详解 ===\n");

        demonstrateFunction();
        demonstratePredicate();
        demonstrateConsumer();
        demonstrateSupplier();
        demonstrateBiFunctions();
        demonstrateMethodReferences();
        demonstrateCustomFunctionalInterface();
        demonstrateComposition();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. Function<T, R> - 接收参数返回结果
     */
    private static void demonstrateFunction() {
        System.out.println("1. Function<T, R> - 转换函数\n");

        // Lambda表达式
        Function<String, Integer> stringLength = s -> s.length();
        Function<Integer, Integer> square = x -> x * x;
        Function<String, String> toUpperCase = String::toUpperCase;

        System.out.println("字符串长度: " + stringLength.apply("Hello"));
        System.out.println("平方: " + square.apply(5));
        System.out.println("大写: " + toUpperCase.apply("hello world"));

        // Function组合：andThen和compose
        Function<String, Integer> lengthThenSquare = stringLength.andThen(square);
        System.out.println("先求长度再平方: " + lengthThenSquare.apply("Java"));  // 4的平方=16
        System.out.println();
    }

    /**
     * 2. Predicate<T> - 条件判断
     */
    private static void demonstratePredicate() {
        System.out.println("2. Predicate<T> - 断言/条件判断\n");

        Predicate<Integer> isEven = x -> x % 2 == 0;
        Predicate<Integer> isPositive = x -> x > 0;
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> startsWithA = s -> s.startsWith("A");

        System.out.println("10是偶数? " + isEven.test(10));
        System.out.println("-5是正数? " + isPositive.test(-5));
        System.out.println("空字符串? " + isEmpty.test(""));

        // Predicate组合：and、or、negate
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        System.out.println("10是偶数且正数? " + isEvenAndPositive.test(10));
        System.out.println("-4是偶数且正数? " + isEvenAndPositive.test(-4));

        // 实际应用：过滤列表
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("\n过滤偶数: " + filter(numbers, isEven));
        System.out.println("过滤正偶数: " + filter(numbers, isEvenAndPositive));
        System.out.println();
    }

    /**
     * 3. Consumer<T> - 消费数据（无返回值）
     */
    private static void demonstrateConsumer() {
        System.out.println("3. Consumer<T> - 消费者（无返回值）\n");

        Consumer<String> printUpperCase = s -> System.out.println(s.toUpperCase());
        Consumer<Integer> printSquare = x -> System.out.println("平方: " + (x * x));
        Consumer<List<String>> printList = list -> list.forEach(System.out::println);

        System.out.println("消费字符串:");
        printUpperCase.accept("hello");

        System.out.println("\n消费数字:");
        printSquare.accept(5);

        // Consumer组合：andThen
        Consumer<String> logger = s -> System.out.println("[LOG] " + s);
        Consumer<String> saver = s -> System.out.println("[SAVE] " + s);
        Consumer<String> logAndSave = logger.andThen(saver);

        System.out.println("\n链式消费:");
        logAndSave.accept("用户登录");
        System.out.println();
    }

    /**
     * 4. Supplier<T> - 提供数据（无参数）
     */
    private static void demonstrateSupplier() {
        System.out.println("4. Supplier<T> - 供应者（无参数）\n");

        Supplier<Double> randomDouble = Math::random;
        Supplier<String> timestampSupplier = () -> "时间戳: " + System.currentTimeMillis();
        Supplier<List<String>> listSupplier = ArrayList::new;

        System.out.println("随机数: " + randomDouble.get());
        System.out.println(timestampSupplier.get());
        System.out.println("新列表: " + listSupplier.get());

        // 实际应用：懒加载
        System.out.println("\n懒加载示例:");
        String result = getOrDefault(null, () -> {
            System.out.println("  执行昂贵的计算...");
            return "默认值";
        });
        System.out.println("结果: " + result);
        System.out.println();
    }

    /**
     * 5. 双参数函数式接口
     */
    private static void demonstrateBiFunctions() {
        System.out.println("5. 双参数函数式接口\n");

        // BiFunction<T, U, R> - 两个参数，一个返回值
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + " " + s2;

        System.out.println("加法: 10 + 20 = " + add.apply(10, 20));
        System.out.println("拼接: " + concat.apply("Hello", "World"));

        // BiPredicate<T, U> - 两个参数，返回boolean
        BiPredicate<String, Integer> longerThan = (s, len) -> s.length() > len;
        System.out.println("\"Java\"长度>5? " + longerThan.test("Java", 5));

        // BiConsumer<T, U> - 两个参数，无返回值
        BiConsumer<String, Integer> printInfo = (name, age) ->
            System.out.println(name + " 的年龄是 " + age);
        printInfo.accept("张三", 25);
        System.out.println();
    }

    /**
     * 6. 方法引用
     */
    private static void demonstrateMethodReferences() {
        System.out.println("6. 方法引用\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        System.out.println("1) 静态方法引用: Integer::parseInt");
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println("   解析: " + parser.apply("123"));

        System.out.println("\n2) 实例方法引用: String::toUpperCase");
        names.stream()
            .map(String::toUpperCase)
            .forEach(System.out::println);

        System.out.println("\n3) 对象方法引用: System.out::println");
        names.forEach(System.out::println);

        System.out.println("\n4) 构造器引用: ArrayList::new");
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> newList = listFactory.get();
        System.out.println("   创建列表: " + newList);
        System.out.println();
    }

    /**
     * 7. 自定义函数式接口
     */
    private static void demonstrateCustomFunctionalInterface() {
        System.out.println("7. 自定义函数式接口\n");

        // 使用自定义函数式接口
        Calculator add = (a, b) -> a + b;
        Calculator multiply = (a, b) -> a * b;

        System.out.println("加法: 10 + 5 = " + add.calculate(10, 5));
        System.out.println("乘法: 10 * 5 = " + multiply.calculate(10, 5));

        // 字符串处理器
        StringProcessor reverser = s -> new StringBuilder(s).reverse().toString();
        StringProcessor upperCase = String::toUpperCase;

        System.out.println("\n字符串处理:");
        System.out.println("反转: " + reverser.process("Hello"));
        System.out.println("大写: " + upperCase.process("hello"));

        // 验证器
        Validator emailValidator = email -> email.contains("@") && email.contains(".");
        System.out.println("\n邮箱验证:");
        System.out.println("test@example.com: " + emailValidator.validate("test@example.com"));
        System.out.println("invalid: " + emailValidator.validate("invalid"));
        System.out.println();
    }

    /**
     * 8. 函数组合
     */
    private static void demonstrateComposition() {
        System.out.println("8. 函数组合\n");

        // Function组合
        Function<String, String> addPrefix = s -> "【" + s + "】";
        Function<String, String> toUpper = String::toUpperCase;
        Function<String, String> composed = addPrefix.andThen(toUpper);

        System.out.println("组合结果: " + composed.apply("hello"));

        // Predicate组合
        Predicate<String> notEmpty = s -> !s.isEmpty();
        Predicate<String> shorterThan10 = s -> s.length() < 10;
        Predicate<String> validString = notEmpty.and(shorterThan10);

        System.out.println("\n字符串验证:");
        System.out.println("\"Hello\": " + validString.test("Hello"));
        System.out.println("\"\": " + validString.test(""));
        System.out.println("\"很长的字符串超过十个字\": " + validString.test("很长的字符串超过十个字"));
        System.out.println();
    }

    /**
     * 9. 实际应用场景
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("9. 实际应用场景\n");

        // 场景1：数据转换管道
        System.out.println("场景1: 数据转换管道");
        List<String> rawData = Arrays.asList("1", "2", "3", "4", "5");

        Function<String, Integer> parse = Integer::parseInt;
        Function<Integer, Integer> square = x -> x * x;
        Function<Integer, String> format = x -> "结果: " + x;

        Function<String, String> pipeline = parse.andThen(square).andThen(format);

        rawData.stream()
            .map(pipeline)
            .forEach(System.out::println);

        // 场景2：条件过滤器
        System.out.println("\n场景2: 用户过滤");
        List<User> users = Arrays.asList(
            new User("Alice", 25, "alice@example.com"),
            new User("Bob", 17, "bob@example.com"),
            new User("Charlie", 30, "charlie@example.com")
        );

        Predicate<User> isAdult = u -> u.age >= 18;
        Predicate<User> hasEmail = u -> u.email != null && !u.email.isEmpty();
        Predicate<User> validUser = isAdult.and(hasEmail);

        System.out.println("有效用户:");
        users.stream()
            .filter(validUser)
            .forEach(u -> System.out.println("  " + u.name + ", " + u.age));

        // 场景3：事件处理器
        System.out.println("\n场景3: 事件处理链");
        Consumer<String> logger = msg -> System.out.println("[LOG] " + msg);
        Consumer<String> notifier = msg -> System.out.println("[NOTIFY] 发送通知: " + msg);
        Consumer<String> saver = msg -> System.out.println("[DB] 保存到数据库: " + msg);

        Consumer<String> eventHandler = logger.andThen(notifier).andThen(saver);
        eventHandler.accept("用户下单成功");
        System.out.println();
    }

    // ==================== 辅助方法和类 ====================

    private static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    private static <T> T getOrDefault(T value, Supplier<T> defaultSupplier) {
        return value != null ? value : defaultSupplier.get();
    }

    // 自定义函数式接口
    @FunctionalInterface
    interface Calculator {
        int calculate(int a, int b);
    }

    @FunctionalInterface
    interface StringProcessor {
        String process(String input);
    }

    @FunctionalInterface
    interface Validator {
        boolean validate(String input);
    }

    // 用户类
    static class User {
        String name;
        int age;
        String email;

        User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }
    }
}
