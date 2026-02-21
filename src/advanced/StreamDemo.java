package advanced;

import java.util.*;
import java.util.stream.*;

/**
 * Stream API学习
 * 学习目标：
 * 1. 理解Stream的概念
 * 2. 掌握Stream的创建方式
 * 3. 学习中间操作和终端操作
 * 4. 掌握常用的Stream操作
 */
public class StreamDemo {
    public static void main(String[] args) {
        System.out.println("=== Stream API 学习 ===\n");

        // 示例1：Stream创建
        demonstrateStreamCreation();

        // 示例2：过滤和映射
        demonstrateFilterAndMap();

        // 示例3：排序和去重
        demonstrateSortAndDistinct();

        // 示例4：聚合操作
        demonstrateAggregation();

        // 示例5：收集操作
        demonstrateCollectors();

        // 示例6：实战案例
        demonstratePracticalExample();
    }

    // Stream创建
    public static void demonstrateStreamCreation() {
        System.out.println("1. Stream创建方式\n");

        // 从集合创建
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> stream1 = list.stream();
        System.out.println("从List创建: " + stream1.count() + "个元素");

        // 从数组创建
        String[] array = {"x", "y", "z"};
        Stream<String> stream2 = Arrays.stream(array);
        System.out.println("从数组创建: " + stream2.count() + "个元素");

        // 使用Stream.of创建
        Stream<Integer> stream3 = Stream.of(1, 2, 3, 4, 5);
        System.out.println("使用Stream.of创建: " + stream3.count() + "个元素");

        // 创建无限流
        Stream<Integer> stream4 = Stream.iterate(0, n -> n + 2).limit(5);
        System.out.print("无限流(前5个偶数): ");
        stream4.forEach(n -> System.out.print(n + " "));
        System.out.println("\n");
    }

    // 过滤和映射
    public static void demonstrateFilterAndMap() {
        System.out.println("2. 过滤和映射\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter: 过滤偶数
        System.out.println("偶数:");
        numbers.stream()
               .filter(n -> n % 2 == 0)
               .forEach(n -> System.out.print(n + " "));

        // map: 数字平方
        System.out.println("\n\n每个数字的平方:");
        numbers.stream()
               .map(n -> n * n)
               .forEach(n -> System.out.print(n + " "));

        // 链式操作：过滤偶数然后平方
        System.out.println("\n\n偶数的平方:");
        numbers.stream()
               .filter(n -> n % 2 == 0)
               .map(n -> n * n)
               .forEach(n -> System.out.print(n + " "));

        System.out.println("\n");
    }

    // 排序和去重
    public static void demonstrateSortAndDistinct() {
        System.out.println("3. 排序和去重\n");

        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9, 2, 5, 3);

        // sorted: 排序
        System.out.println("排序后:");
        numbers.stream()
               .sorted()
               .forEach(n -> System.out.print(n + " "));

        // distinct: 去重
        System.out.println("\n\n去重后:");
        numbers.stream()
               .distinct()
               .forEach(n -> System.out.print(n + " "));

        // 组合：去重并排序
        System.out.println("\n\n去重并排序:");
        numbers.stream()
               .distinct()
               .sorted()
               .forEach(n -> System.out.print(n + " "));

        // 倒序排序
        System.out.println("\n\n倒序排序:");
        numbers.stream()
               .sorted(Comparator.reverseOrder())
               .forEach(n -> System.out.print(n + " "));

        System.out.println("\n");
    }

    // 聚合操作
    public static void demonstrateAggregation() {
        System.out.println("4. 聚合操作\n");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // count: 计数
        long count = numbers.stream().filter(n -> n > 5).count();
        System.out.println("大于5的数字个数: " + count);

        // max: 最大值
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        max.ifPresent(m -> System.out.println("最大值: " + m));

        // min: 最小值
        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        min.ifPresent(m -> System.out.println("最小值: " + m));

        // reduce: 求和
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("总和: " + sum);

        // 平均值
        double average = numbers.stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0.0);
        System.out.println("平均值: " + average);

        // anyMatch: 是否有任意元素匹配
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        System.out.println("\n是否包含偶数: " + hasEven);

        // allMatch: 是否所有元素都匹配
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        System.out.println("是否都是正数: " + allPositive);

        System.out.println();
    }

    // 收集操作
    public static void demonstrateCollectors() {
        System.out.println("5. 收集操作\n");

        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");

        // 收集到List
        List<String> filteredList = words.stream()
                                         .filter(w -> w.length() > 5)
                                         .collect(Collectors.toList());
        System.out.println("长度>5的单词: " + filteredList);

        // 收集到Set
        Set<Integer> lengths = words.stream()
                                    .map(String::length)
                                    .collect(Collectors.toSet());
        System.out.println("单词长度集合: " + lengths);

        // 连接字符串
        String joined = words.stream()
                            .collect(Collectors.joining(", "));
        System.out.println("连接字符串: " + joined);

        // 分组
        Map<Integer, List<String>> grouped = words.stream()
                                                  .collect(Collectors.groupingBy(String::length));
        System.out.println("\n按长度分组:");
        grouped.forEach((len, list) ->
            System.out.println("  长度" + len + ": " + list));

        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("6. 实战案例：学生成绩分析\n");

        List<StreamStudent> students = Arrays.asList(
            new StreamStudent("张三", 85, "男"),
            new StreamStudent("李四", 92, "女"),
            new StreamStudent("王五", 78, "男"),
            new StreamStudent("赵六", 95, "女"),
            new StreamStudent("孙七", 88, "男")
        );

        // 找出成绩最高的学生
        students.stream()
                .max(Comparator.comparing(StreamStudent::getScore))
                .ifPresent(s -> System.out.println("最高分学生: " + s.getName() + ", 分数: " + s.getScore()));

        // 计算平均分
        double avgScore = students.stream()
                                  .mapToInt(StreamStudent::getScore)
                                  .average()
                                  .orElse(0.0);
        System.out.println("平均分: " + avgScore);

        // 按性别分组
        Map<String, List<StreamStudent>> byGender = students.stream()
                                                      .collect(Collectors.groupingBy(StreamStudent::getGender));
        System.out.println("\n按性别分组:");
        byGender.forEach((gender, list) -> {
            System.out.println("  " + gender + ":");
            list.forEach(s -> System.out.println("    - " + s.getName() + ", " + s.getScore() + "分"));
        });

        // 找出80分以上的学生名单
        List<String> excellentStudents = students.stream()
                                                 .filter(s -> s.getScore() >= 80)
                                                 .map(StreamStudent::getName)
                                                 .collect(Collectors.toList());
        System.out.println("\n80分以上的学生: " + excellentStudents);
    }
}

// StreamStudent类
class StreamStudent {
    private String name;
    private int score;
    private String gender;

    public StreamStudent(String name, int score, String gender) {
        this.name = name;
        this.score = score;
        this.gender = gender;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public String getGender() { return gender; }
}
