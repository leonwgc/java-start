package advanced;

import java.util.*;

/**
 * Java泛型学习
 * 学习目标：
 * 1. 理解泛型的概念和作用
 * 2. 掌握泛型类和泛型方法
 * 3. 理解泛型通配符
 * 4. 掌握类型擦除的概念
 */
public class GenericsDemo {
    public static void main(String[] args) {
        System.out.println("=== 泛型学习 ===\n");

        // 示例1：泛型的必要性
        demonstrateWhyGenerics();

        // 示例2：泛型类
        demonstrateGenericClass();

        // 示例3：泛型方法
        demonstrateGenericMethod();

        // 示例4：泛型通配符
        demonstrateWildcards();

        // 示例5：多类型参数
        demonstrateMultipleTypes();
    }

    // 为什么需要泛型
    public static void demonstrateWhyGenerics() {
        System.out.println("1. 为什么需要泛型？\n");

        // 没有泛型：类型不安全
        List list = new ArrayList();
        list.add("Hello");
        list.add(100);
        list.add(true);

        // 取出时需要类型转换，容易出错
        try {
            String str = (String) list.get(1);  // 运行时错误！
        } catch (ClassCastException e) {
            System.out.println("类型转换错误：" + e.getMessage());
        }

        // 使用泛型：类型安全
        List<String> stringList = new ArrayList<>();
        stringList.add("Hello");
        // stringList.add(100);  // 编译错误，类型检查

        String str = stringList.get(0);  // 不需要类型转换
        System.out.println("安全获取：" + str);
        System.out.println();
    }

    // 泛型类
    public static void demonstrateGenericClass() {
        System.out.println("2. 泛型类\n");

        // 创建不同类型的Box
        Box<String> stringBox = new Box<>("Hello World");
        Box<Integer> intBox = new Box<>(123);
        Box<GenericPerson> personBox = new Box<>(new GenericPerson("张三", 25));

        System.out.println("String Box: " + stringBox.get());
        System.out.println("Integer Box: " + intBox.get());
        System.out.println("Person Box: " + personBox.get().getName());
        System.out.println();
    }

    // 泛型方法
    public static void demonstrateGenericMethod() {
        System.out.println("3. 泛型方法\n");

        // 泛型方法可以接受不同类型
        printArray(new String[]{"Java", "Python", "Go"});
        printArray(new Integer[]{1, 2, 3, 4, 5});
        printArray(new Double[]{1.1, 2.2, 3.3});

        // 泛型方法返回值
        String[] names = {"Alice", "Bob", "Charlie"};
        String first = getFirst(names);
        System.out.println("\n第一个元素: " + first);

        // 类型推断
        Integer max = getMax(10, 20);
        System.out.println("最大值: " + max);
        System.out.println();
    }

    // 泛型通配符
    public static void demonstrateWildcards() {
        System.out.println("4. 泛型通配符\n");

        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
        List<String> stringList = Arrays.asList("A", "B", "C");

        // ? 通配符：可以接受任何类型
        System.out.println("Integer List 总数: " + countElements(intList));
        System.out.println("Double List 总数: " + countElements(doubleList));
        System.out.println("String List 总数: " + countElements(stringList));

        // ? extends Number：上界通配符
        System.out.println("\nInteger List 总和: " + sumList(intList));
        System.out.println("Double List 总和: " + sumList(doubleList));
        // System.out.println(sumList(stringList));  // 编译错误

        // ? super Integer：下界通配符
        List<Number> numbers = new ArrayList<>();
        addIntegers(numbers);
        System.out.println("\n添加后的数字: " + numbers);
        System.out.println();
    }

    // 多类型参数
    public static void demonstrateMultipleTypes() {
        System.out.println("5. 多类型参数\n");

        Pair<String, Integer> pair1 = new Pair<>("Age", 25);
        Pair<String, String> pair2 = new Pair<>("Name", "张三");

        System.out.println(pair1.getKey() + ": " + pair1.getValue());
        System.out.println(pair2.getKey() + ": " + pair2.getValue());

        // 使用泛型方法创建Pair
        Pair<String, Double> pair3 = makePair("Salary", 50000.0);
        System.out.println(pair3.getKey() + ": " + pair3.getValue());
    }

    // ========== 工具方法 ==========

    // 泛型方法：打印数组
    public static <T> void printArray(T[] array) {
        System.out.print("数组元素: ");
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    // 泛型方法：获取第一个元素
    public static <T> T getFirst(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    // 泛型方法：获取最大值（有限制）
    public static <T extends Comparable<T>> T getMax(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    // 通配符：? 接受任何类型
    public static int countElements(List<?> list) {
        return list.size();
    }

    // 上界通配符：? extends Number
    public static double sumList(List<? extends Number> list) {
        double sum = 0;
        for (Number num : list) {
            sum += num.doubleValue();
        }
        return sum;
    }

    // 下界通配符：? super Integer
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }

    // 多类型参数方法
    public static <K, V> Pair<K, V> makePair(K key, V value) {
        return new Pair<>(key, value);
    }
}

// ========== 泛型类定义 ==========

// 泛型类：Box<T>
class Box<T> {
    private T content;

    public Box(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }

    public void set(T content) {
        this.content = content;
    }
}

// 多类型参数泛型类：Pair<K, V>
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}

// 简单GenericPerson类
class GenericPerson {
    private String name;
    private int age;

    public GenericPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}
