package advanced;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;

/**
 * Lambda表达式学习
 * 学习目标：
 * 1. 理解Lambda表达式的语法
 * 2. 掌握函数式接口
 * 3. 学习方法引用
 * 4. 理解Lambda的实际应用
 */
public class LambdaDemo {
    public static void main(String[] args) {
        System.out.println("=== Lambda表达式基础 ===\n");

        // 示例1：传统方式 vs Lambda方式
        demonstrateBasicLambda();

        // 示例2：常用函数式接口
        demonstrateFunctionalInterfaces();

        // 示例3：方法引用
        demonstrateMethodReference();

        // 示例4：实际应用
        demonstratePracticalUsage();
    }

    // 基础Lambda示例
    public static void demonstrateBasicLambda() {
        System.out.println("1. 传统方式 vs Lambda方式\n");

        // 传统方式：使用匿名内部类
        Calculator add = new Calculator() {
            @Override
            public int calculate(int a, int b) {
                return a + b;
            }
        };
        System.out.println("传统方式 - 10 + 5 = " + add.calculate(10, 5));

        // Lambda方式：简洁明了
        Calculator subtract = (a, b) -> a - b;
        System.out.println("Lambda方式 - 10 - 5 = " + subtract.calculate(10, 5));

        // 多行Lambda
        Calculator multiply = (a, b) -> {
            int result = a * b;
            return result;
        };
        System.out.println("多行Lambda - 10 * 5 = " + multiply.calculate(10, 5));
        System.out.println();
    }

    // 常用函数式接口
    public static void demonstrateFunctionalInterfaces() {
        System.out.println("2. 常用函数式接口\n");

        // Predicate<T>: 判断条件，返回boolean
        Predicate<Integer> isEven = num -> num % 2 == 0;
        System.out.println("10是偶数吗？" + isEven.test(10));
        System.out.println("7是偶数吗？" + isEven.test(7));

        // Function<T, R>: 接收T类型，返回R类型
        Function<String, Integer> stringLength = str -> str.length();
        System.out.println("\n\"Hello\"的长度: " + stringLength.apply("Hello"));

        // Consumer<T>: 接收T类型，无返回值
        Consumer<String> printer = msg -> System.out.println("消息: " + msg);
        printer.accept("这是一条测试消息");

        // Supplier<T>: 无参数，返回T类型
        Supplier<Double> randomSupplier = () -> Math.random();
        System.out.println("\n随机数: " + randomSupplier.get());

        // BiFunction<T, U, R>: 接收两个参数，返回结果
        BiFunction<Integer, Integer, Integer> power = (base, exp) ->
            (int) Math.pow(base, exp);
        System.out.println("2的3次方: " + power.apply(2, 3));
        System.out.println();
    }

    // 方法引用
    public static void demonstrateMethodReference() {
        System.out.println("3. 方法引用\n");

        List<String> names = Arrays.asList("张三", "李四", "王五", "赵六");

        // Lambda方式
        System.out.println("Lambda方式:");
        names.forEach(name -> System.out.println("- " + name));

        // 方法引用：静态方法引用
        System.out.println("\n方法引用（静态方法）:");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.forEach(LambdaDemo::printNumber);

        // 方法引用：实例方法引用
        System.out.println("\n方法引用（实例方法）:");
        names.forEach(System.out::println);

        // 构造器引用
        Supplier<StringBuilder> sbSupplier = StringBuilder::new;
        StringBuilder sb = sbSupplier.get();
        sb.append("使用构造器引用创建");
        System.out.println("\n" + sb.toString());
        System.out.println();
    }

    // 实际应用
    public static void demonstratePracticalUsage() {
        System.out.println("4. 实际应用场景\n");

        List<LambdaPerson> people = Arrays.asList(
            new LambdaPerson("张三", 25),
            new LambdaPerson("李四", 30),
            new LambdaPerson("王五", 22),
            new LambdaPerson("赵六", 35)
        );

        // 场景1：过滤年龄大于25的人
        System.out.println("年龄大于25的人:");
        people.stream()
              .filter(p -> p.getAge() > 25)
              .forEach(p -> System.out.println("- " + p.getName() + ", " + p.getAge() + "岁"));

        // 场景2：排序
        System.out.println("\n按年龄排序:");
        people.stream()
              .sorted((p1, p2) -> p1.getAge() - p2.getAge())
              .forEach(p -> System.out.println("- " + p.getName() + ", " + p.getAge() + "岁"));

        // 场景3：转换
        System.out.println("\n所有人的名字:");
        people.stream()
              .map(LambdaPerson::getName)
              .forEach(name -> System.out.println("- " + name));
    }

    // 辅助方法
    public static void printNumber(int num) {
        System.out.println("数字: " + num);
    }
}

// 函数式接口示例
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

// LambdaPerson类
class LambdaPerson {
    private String name;
    private int age;

    public LambdaPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}
