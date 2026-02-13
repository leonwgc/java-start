package advanced;

import java.time.LocalDate;
import java.util.List;

/**
 * Java Records（记录类）详解
 * 学习目标：
 * 1. 理解Records的作用和优势（Java 14引入，Java 17正式版）
 * 2. 掌握Records的定义和使用
 * 3. 了解Records与传统类的区别
 * 4. 学习Records的高级特性（紧凑构造器、自定义方法）
 *
 * Records是什么？
 * - Records是Java提供的一种特殊类，专门用于表示不可变数据
 * - 自动生成构造器、getter、equals()、hashCode()、toString()
 * - 代码更简洁，意图更明确
 *
 * Spring应用：
 * - DTO（数据传输对象）定义
 * - API请求/响应对象
 * - 配置属性类
 * - 事件对象
 */
public class RecordsDemo {

    public static void main(String[] args) {
        System.out.println("=== Java Records 详解 ===\n");

        demonstrateBasicRecord();
        demonstrateRecordFeatures();
        demonstrateCompactConstructor();
        demonstrateCustomMethods();
        demonstrateRecordWithCollections();
        demonstrateRecordComparison();
    }

    /**
     * 1. 基础Records定义和使用
     */
    private static void demonstrateBasicRecord() {
        System.out.println("1. 基础Records定义和使用\n");

        // 创建Record实例
        Person person = new Person("张三", 25);

        System.out.println("创建Person: " + person);
        System.out.println("姓名: " + person.name());  // 自动生成的访问方法
        System.out.println("年龄: " + person.age());
        System.out.println();

        // Record自动实现了equals和hashCode
        Person person2 = new Person("张三", 25);
        System.out.println("person.equals(person2): " + person.equals(person2));
        System.out.println("person.hashCode(): " + person.hashCode());
        System.out.println("person2.hashCode(): " + person2.hashCode());
        System.out.println();
    }

    /**
     * 2. Records的特性
     */
    private static void demonstrateRecordFeatures() {
        System.out.println("2. Records的特性\n");

        System.out.println("Records自动提供的功能：");
        System.out.println("✓ 私有final字段");
        System.out.println("✓ 公共构造器");
        System.out.println("✓ 公共访问方法（name()而不是getName()）");
        System.out.println("✓ equals()方法");
        System.out.println("✓ hashCode()方法");
        System.out.println("✓ toString()方法");
        System.out.println();

        System.out.println("Records的限制：");
        System.out.println("✗ 不能被继承（隐式final）");
        System.out.println("✗ 不能声明实例字段（除了组件字段）");
        System.out.println("✓ 可以实现接口");
        System.out.println("✓ 可以有静态字段和方法");
        System.out.println("✓ 可以有实例方法");
        System.out.println();
    }

    /**
     * 3. 紧凑构造器（Compact Constructor）
     */
    private static void demonstrateCompactConstructor() {
        System.out.println("3. 紧凑构造器 - 参数验证\n");

        try {
            // 正常创建
            Employee emp1 = new Employee("李四", 50000.0);
            System.out.println("创建员工: " + emp1);

            // 触发验证失败
            Employee emp2 = new Employee("", -1000.0);
        } catch (IllegalArgumentException e) {
            System.out.println("验证失败: " + e.getMessage());
        }
        System.out.println();
    }

    /**
     * 4. Records可以有自定义方法
     */
    private static void demonstrateCustomMethods() {
        System.out.println("4. Records的自定义方法\n");

        Book book = new Book("Java核心技术", "Cay Horstmann", 129.0);
        System.out.println(book);
        System.out.println("书籍信息: " + book.getFullInfo());
        System.out.println("是否昂贵: " + book.isExpensive());
        System.out.println("打折价格: " + book.getDiscountedPrice(0.8));
        System.out.println();
    }

    /**
     * 5. Records与集合配合使用
     */
    private static void demonstrateRecordWithCollections() {
        System.out.println("5. Records与集合配合使用\n");

        // Records非常适合作为Map的key或Set的元素
        List<Point> points = List.of(
            new Point(0, 0),
            new Point(3, 4),
            new Point(-1, 2)
        );

        System.out.println("所有点:");
        points.forEach(System.out::println);

        System.out.println("\n计算每个点到原点的距离:");
        points.forEach(p ->
            System.out.printf("%s 到原点距离: %.2f\n", p, p.distanceFromOrigin())
        );
        System.out.println();
    }

    /**
     * 6. Records vs 传统类对比
     */
    private static void demonstrateRecordComparison() {
        System.out.println("6. Records vs 传统类对比\n");

        System.out.println("传统类（PersonClass）代码行数: ~30行");
        System.out.println("Records（Person）代码行数: 1行");
        System.out.println();

        System.out.println("Spring Boot中的应用场景：");
        System.out.println("1. REST API的请求对象:");
        System.out.println("   record CreateUserRequest(String username, String email) {}");
        System.out.println();

        System.out.println("2. REST API的响应对象:");
        System.out.println("   record UserResponse(Long id, String username, LocalDate createdAt) {}");
        System.out.println();

        System.out.println("3. 配置属性:");
        System.out.println("   @ConfigurationProperties(\"app\")");
        System.out.println("   record AppConfig(String name, int timeout) {}");
        System.out.println();
    }

    // ==================== Records定义 ====================

    /**
     * 基础Record示例 - 一行代码定义完整的不可变数据类
     */
    record Person(String name, int age) {
        // Record会自动生成:
        // - 私有final字段: private final String name; private final int age;
        // - 构造器: public Person(String name, int age)
        // - 访问方法: public String name() 和 public int age()
        // - equals()、hashCode()、toString()
    }

    /**
     * 带验证的Record - 使用紧凑构造器
     */
    record Employee(String name, double salary) {
        // 紧凑构造器：参数验证
        public Employee {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("姓名不能为空");
            }
            if (salary < 0) {
                throw new IllegalArgumentException("薪水不能为负数");
            }
            // 不需要赋值语句，Java会自动赋值
        }
    }

    /**
     * 带自定义方法的Record
     */
    record Book(String title, String author, double price) {
        // 自定义方法
        public String getFullInfo() {
            return String.format("《%s》 作者: %s", title, author);
        }

        public boolean isExpensive() {
            return price > 100.0;
        }

        public double getDiscountedPrice(double discount) {
            return price * discount;
        }
    }

    /**
     * 实现接口的Record
     */
    record Point(int x, int y) {
        // 自定义方法：计算到原点的距离
        public double distanceFromOrigin() {
            return Math.sqrt(x * x + y * y);
        }

        @Override
        public String toString() {
            return String.format("Point(%d, %d)", x, y);
        }
    }

    // ==================== 传统类对比 ====================

    /**
     * 传统类写法 - 需要大量模板代码
     */
    static class PersonClass {
        private final String name;
        private final int age;

        public PersonClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PersonClass that = (PersonClass) o;
            return age == that.age && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode() * 31 + age;
        }

        @Override
        public String toString() {
            return "PersonClass{name='" + name + "', age=" + age + "}";
        }
    }
}
