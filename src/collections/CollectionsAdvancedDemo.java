package collections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 集合框架高级操作详解
 * 学习目标：
 * 1. 掌握Collectors高级收集器
 * 2. 学习分组、分区、聚合操作
 * 3. 理解并发集合的使用
 * 4. 了解集合性能优化技巧
 *
 * 集合高级操作包括：
 * - Collectors收集器（groupingBy、partitioningBy等）
 * - 集合排序和比较器链式调用
 * - 集合转换和合并操作
 * - 并发安全集合（ConcurrentHashMap等）
 *
 * Spring应用：
 * - 数据分组统计
 * - 批量数据处理
 * - 缓存实现
 * - 并发场景数据共享
 */
public class CollectionsAdvancedDemo {

    public static void main(String[] args) {
        System.out.println("=== 集合框架高级操作详解 ===\n");

        demonstrateGroupingBy();
        demonstratePartitioningBy();
        demonstrateCollectorsCombining();
        demonstrateComparatorChaining();
        demonstrateConcurrentCollections();
        demonstrateImmutableCollections();
        demonstrateCollectionConversions();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. groupingBy - 分组操作
     */
    private static void demonstrateGroupingBy() {
        System.out.println("1. groupingBy - 分组操作\n");

        List<Person> people = Arrays.asList(
            new Person("张三", 25, "北京", 8000),
            new Person("李四", 30, "上海", 12000),
            new Person("王五", 25, "北京", 9000),
            new Person("赵六", 35, "上海", 15000),
            new Person("钱七", 30, "深圳", 11000)
        );

        // 按年龄分组
        Map<Integer, List<Person>> byAge = people.stream()
            .collect(Collectors.groupingBy(Person::getAge));

        System.out.println("按年龄分组:");
        byAge.forEach((age, persons) -> {
            System.out.println("  " + age + "岁: " +
                persons.stream().map(Person::getName).collect(Collectors.joining(", ")));
        });

        // 按城市分组，统计人数
        Map<String, Long> countByCity = people.stream()
            .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));

        System.out.println("\n按城市统计人数:");
        countByCity.forEach((city, count) ->
            System.out.println("  " + city + ": " + count + "人"));

        // 按城市分组，计算平均工资
        Map<String, Double> avgSalaryByCity = people.stream()
            .collect(Collectors.groupingBy(
                Person::getCity,
                Collectors.averagingDouble(Person::getSalary)
            ));

        System.out.println("\n按城市统计平均工资:");
        avgSalaryByCity.forEach((city, avgSalary) ->
            System.out.printf("  %s: %.2f元\n", city, avgSalary));

        // 多级分组：先按城市，再按年龄
        Map<String, Map<Integer, List<Person>>> byCityAndAge = people.stream()
            .collect(Collectors.groupingBy(
                Person::getCity,
                Collectors.groupingBy(Person::getAge)
            ));

        System.out.println("\n多级分组（城市→年龄）:");
        byCityAndAge.forEach((city, ageMap) -> {
            System.out.println("  " + city + ":");
            ageMap.forEach((age, persons) -> {
                System.out.println("    " + age + "岁: " +
                    persons.stream().map(Person::getName).collect(Collectors.joining(", ")));
            });
        });
        System.out.println();
    }

    /**
     * 2. partitioningBy - 分区操作
     */
    private static void demonstratePartitioningBy() {
        System.out.println("2. partitioningBy - 分区操作\n");

        List<Person> people = Arrays.asList(
            new Person("张三", 25, "北京", 8000),
            new Person("李四", 30, "上海", 12000),
            new Person("王五", 17, "北京", 5000),
            new Person("赵六", 35, "上海", 15000)
        );

        // 按是否成年分区
        Map<Boolean, List<Person>> partitionedByAge = people.stream()
            .collect(Collectors.partitioningBy(p -> p.getAge() >= 18));

        System.out.println("按是否成年分区:");
        System.out.println("  成年人: " +
            partitionedByAge.get(true).stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ")));
        System.out.println("  未成年: " +
            partitionedByAge.get(false).stream()
                .map(Person::getName)
                .collect(Collectors.joining(", ")));

        // 按工资是否超过1万分区，并统计人数
        Map<Boolean, Long> salaryPartition = people.stream()
            .collect(Collectors.partitioningBy(
                p -> p.getSalary() > 10000,
                Collectors.counting()
            ));

        System.out.println("\n按工资分区统计:");
        System.out.println("  工资>10000: " + salaryPartition.get(true) + "人");
        System.out.println("  工资≤10000: " + salaryPartition.get(false) + "人");
        System.out.println();
    }

    /**
     * 3. Collectors组合使用
     */
    private static void demonstrateCollectorsCombining() {
        System.out.println("3. Collectors组合使用\n");

        List<Person> people = Arrays.asList(
            new Person("张三", 25, "北京", 8000),
            new Person("李四", 30, "上海", 12000),
            new Person("王五", 25, "北京", 9000),
            new Person("赵六", 35, "上海", 15000)
        );

        // joining - 字符串拼接
        String names = people.stream()
            .map(Person::getName)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("所有姓名: " + names);

        // summarizingDouble - 统计摘要
        DoubleSummaryStatistics salaryStats = people.stream()
            .collect(Collectors.summarizingDouble(Person::getSalary));

        System.out.println("\n工资统计:");
        System.out.println("  总数: " + salaryStats.getCount());
        System.out.println("  总和: " + salaryStats.getSum());
        System.out.println("  平均: " + salaryStats.getAverage());
        System.out.println("  最小: " + salaryStats.getMin());
        System.out.println("  最大: " + salaryStats.getMax());

        // toMap - 转换为Map
        Map<String, Integer> nameAgeMap = people.stream()
            .collect(Collectors.toMap(
                Person::getName,
                Person::getAge
            ));
        System.out.println("\n姓名-年龄映射: " + nameAgeMap);

        // collectingAndThen - 收集后再转换
        List<String> topTwoNames = people.stream()
            .sorted(Comparator.comparing(Person::getSalary).reversed())
            .limit(2)
            .map(Person::getName)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                Collections::unmodifiableList
            ));
        System.out.println("工资前2名: " + topTwoNames);
        System.out.println();
    }

    /**
     * 4. 比较器链式调用
     */
    private static void demonstrateComparatorChaining() {
        System.out.println("4. 比较器链式调用\n");

        List<Person> people = Arrays.asList(
            new Person("张三", 25, "北京", 9000),
            new Person("李四", 30, "上海", 12000),
            new Person("王五", 25, "北京", 8000),
            new Person("赵六", 30, "上海", 15000)
        );

        System.out.println("原始顺序:");
        people.forEach(p -> System.out.println("  " + p));

        // 按年龄升序，年龄相同按工资降序
        List<Person> sorted1 = people.stream()
            .sorted(Comparator.comparing(Person::getAge)
                    .thenComparing(Comparator.comparing(Person::getSalary).reversed()))
            .collect(Collectors.toList());

        System.out.println("\n按年龄升序，工资降序:");
        sorted1.forEach(p -> System.out.println("  " + p));

        // 按城市，然后按姓名
        List<Person> sorted2 = people.stream()
            .sorted(Comparator.comparing(Person::getCity)
                    .thenComparing(Person::getName))
            .collect(Collectors.toList());

        System.out.println("\n按城市，然后按姓名:");
        sorted2.forEach(p -> System.out.println("  " + p));

        // 使用nullsFirst处理null值
        List<Person> peopleWithNull = new ArrayList<>(people);
        peopleWithNull.add(new Person(null, 28, "广州", 10000));

        List<Person> sorted3 = peopleWithNull.stream()
            .sorted(Comparator.comparing(Person::getName,
                    Comparator.nullsFirst(String::compareTo)))
            .collect(Collectors.toList());

        System.out.println("\nnull值排在前面:");
        sorted3.forEach(p -> System.out.println("  " + p));
        System.out.println();
    }

    /**
     * 5. 并发集合
     */
    private static void demonstrateConcurrentCollections() {
        System.out.println("5. 并发集合\n");

        // ConcurrentHashMap - 线程安全的HashMap
        Map<String, Integer> concurrentMap = new java.util.concurrent.ConcurrentHashMap<>();
        concurrentMap.put("A", 1);
        concurrentMap.put("B", 2);

        // 原子操作
        concurrentMap.computeIfAbsent("C", k -> 3);
        concurrentMap.merge("A", 10, Integer::sum);  // A的值变为11

        System.out.println("ConcurrentHashMap: " + concurrentMap);

        // CopyOnWriteArrayList - 写时复制列表
        List<String> cowList = new java.util.concurrent.CopyOnWriteArrayList<>();
        cowList.add("Java");
        cowList.add("Python");
        System.out.println("CopyOnWriteArrayList: " + cowList);

        // ConcurrentLinkedQueue - 无界并发队列
        Queue<String> concurrentQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
        concurrentQueue.offer("任务1");
        concurrentQueue.offer("任务2");
        System.out.println("ConcurrentLinkedQueue: " + concurrentQueue.poll());
        System.out.println();
    }

    /**
     * 6. 不可变集合（Java 9+）
     */
    private static void demonstrateImmutableCollections() {
        System.out.println("6. 不可变集合\n");

        // List.of - 创建不可变列表
        List<String> immutableList = List.of("A", "B", "C");
        System.out.println("不可变列表: " + immutableList);

        // Set.of - 创建不可变集合
        Set<Integer> immutableSet = Set.of(1, 2, 3, 4, 5);
        System.out.println("不可变集合: " + immutableSet);

        // Map.of - 创建不可变映射
        Map<String, Integer> immutableMap = Map.of(
            "one", 1,
            "two", 2,
            "three", 3
        );
        System.out.println("不可变映射: " + immutableMap);

        // 尝试修改会抛出UnsupportedOperationException
        try {
            immutableList.add("D");
        } catch (UnsupportedOperationException e) {
            System.out.println("无法修改不可变集合");
        }
        System.out.println();
    }

    /**
     * 7. 集合转换
     */
    private static void demonstrateCollectionConversions() {
        System.out.println("7. 集合转换\n");

        // List to Set
        List<String> list = Arrays.asList("A", "B", "C", "A");
        Set<String> set = new HashSet<>(list);
        System.out.println("List转Set（去重）: " + set);

        // Set to List
        List<String> listFromSet = new ArrayList<>(set);
        System.out.println("Set转List: " + listFromSet);

        // Array to List
        String[] array = {"X", "Y", "Z"};
        List<String> listFromArray = Arrays.asList(array);
        System.out.println("数组转List: " + listFromArray);

        // List to Array
        String[] arrayFromList = list.toArray(new String[0]);
        System.out.println("List转数组: " + Arrays.toString(arrayFromList));

        // Map to List
        Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);
        List<String> keys = new ArrayList<>(map.keySet());
        List<Integer> values = new ArrayList<>(map.values());
        System.out.println("Map的键: " + keys);
        System.out.println("Map的值: " + values);
        System.out.println();
    }

    /**
     * 8. 实际应用场景
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("8. 实际应用场景\n");

        // 模拟订单数据
        List<Order> orders = Arrays.asList(
            new Order("O001", "张三", "北京", 299.0, "已完成"),
            new Order("O002", "李四", "上海", 599.0, "已发货"),
            new Order("O003", "张三", "北京", 199.0, "已完成"),
            new Order("O004", "王五", "深圳", 899.0, "待支付"),
            new Order("O005", "李四", "上海", 399.0, "已完成")
        );

        // 场景1：按用户统计总消费
        System.out.println("场景1: 按用户统计总消费");
        Map<String, Double> totalByUser = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getUser,
                Collectors.summingDouble(Order::getAmount)
            ));
        totalByUser.forEach((user, total) ->
            System.out.printf("  %s: %.2f元\n", user, total));

        // 场景2：按城市统计订单数量和总额
        System.out.println("\n场景2: 按城市统计");
        Map<String, DoubleSummaryStatistics> statsByCity = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getCity,
                Collectors.summarizingDouble(Order::getAmount)
            ));
        statsByCity.forEach((city, stats) -> {
            System.out.printf("  %s: %d笔订单，总额%.2f元，平均%.2f元\n",
                city, stats.getCount(), stats.getSum(), stats.getAverage());
        });

        // 场景3：找出每个城市的最大订单
        System.out.println("\n场景3: 每个城市的最大订单");
        Map<String, Optional<Order>> maxOrderByCity = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getCity,
                Collectors.maxBy(Comparator.comparing(Order::getAmount))
            ));
        maxOrderByCity.forEach((city, optOrder) -> {
            optOrder.ifPresent(order ->
                System.out.printf("  %s: %s (%.2f元)\n",
                    city, order.getId(), order.getAmount()));
        });

        // 场景4：按状态分组，获取订单ID列表
        System.out.println("\n场景4: 按状态分组");
        Map<String, List<String>> orderIdsByStatus = orders.stream()
            .collect(Collectors.groupingBy(
                Order::getStatus,
                Collectors.mapping(Order::getId, Collectors.toList())
            ));
        orderIdsByStatus.forEach((status, ids) ->
            System.out.println("  " + status + ": " + ids));
        System.out.println();
    }

    // ==================== 辅助类 ====================

    static class Person {
        private String name;
        private int age;
        private String city;
        private double salary;

        public Person(String name, int age, String city, double salary) {
            this.name = name;
            this.age = age;
            this.city = city;
            this.salary = salary;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public String getCity() { return city; }
        public double getSalary() { return salary; }

        @Override
        public String toString() {
            return String.format("%s (%d岁, %s, %.0f元)", name, age, city, salary);
        }
    }

    static class Order {
        private String id;
        private String user;
        private String city;
        private double amount;
        private String status;

        public Order(String id, String user, String city, double amount, String status) {
            this.id = id;
            this.user = user;
            this.city = city;
            this.amount = amount;
            this.status = status;
        }

        public String getId() { return id; }
        public String getUser() { return user; }
        public String getCity() { return city; }
        public double getAmount() { return amount; }
        public String getStatus() { return status; }
    }
}
