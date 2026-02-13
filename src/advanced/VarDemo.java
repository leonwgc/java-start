package advanced;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Stream;

/**
 * var类型推断详解（Java 10+）
 * 学习目标：
 * 1. 理解var关键字的作用和使用场景
 * 2. 掌握var的语法规则和限制
 * 3. 学习何时应该使用var，何时应该避免
 * 4. 了解var对代码可读性的影响
 *
 * var是什么？
 * - var是局部变量类型推断（Local Variable Type Inference）
 * - 编译器根据右侧表达式自动推断变量类型
 * - 只能用于局部变量，不是动态类型
 * - 提高代码简洁性，减少冗余类型声明
 *
 * Spring应用：
 * - 简化复杂泛型类型声明
 * - Lambda表达式中的变量
 * - Stream链式调用
 * - 构建器模式
 */
public class VarDemo {

    public static void main(String[] args) {
        System.out.println("=== var类型推断详解 ===\n");

        demonstrateBasicVar();
        demonstrateVarWithCollections();
        demonstrateVarWithStreams();
        demonstrateVarBestPractices();
        demonstrateVarLimitations();
        demonstrateVarInLoops();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. var基础用法
     */
    private static void demonstrateBasicVar() {
        System.out.println("1. var基础用法\n");

        // 传统写法 vs var
        String name1 = "张三";
        var name2 = "李四";  // 推断为String

        int age1 = 25;
        var age2 = 30;  // 推断为int

        List<String> list1 = new ArrayList<>();
        var list2 = new ArrayList<String>();  // 推断为ArrayList<String>

        System.out.println("传统写法: String name1 = \"张三\"");
        System.out.println("var写法:   var name2 = \"李四\"");
        System.out.println();

        System.out.println("name2的实际类型: " + name2.getClass().getSimpleName());
        System.out.println("age2的实际类型: " + ((Object)age2).getClass().getSimpleName());
        System.out.println("list2的实际类型: " + list2.getClass().getSimpleName());
        System.out.println();

        System.out.println("重点：var不是动态类型！");
        System.out.println("  • 类型在编译期确定");
        System.out.println("  • 一旦确定就无法改变");
        System.out.println("  • 编译后与显式类型声明完全相同");
        System.out.println();
    }

    /**
     * 2. var与集合类型
     */
    private static void demonstrateVarWithCollections() {
        System.out.println("2. var与集合类型\n");

        // 简化复杂的泛型声明
        var list = new ArrayList<String>();
        list.add("Java");
        list.add("Python");
        list.add("Go");

        var map = new HashMap<String, List<Integer>>();
        map.put("scores", List.of(90, 85, 88));

        var set = new HashSet<String>();
        set.add("Apple");
        set.add("Banana");

        System.out.println("简化前: HashMap<String, List<Integer>> map = new HashMap<>();");
        System.out.println("简化后: var map = new HashMap<String, List<Integer>>();");
        System.out.println();

        System.out.println("List内容: " + list);
        System.out.println("Map内容: " + map);
        System.out.println("Set内容: " + set);
        System.out.println();

        // 对于复杂嵌套类型特别有用
        var complexMap = new HashMap<String, Map<Integer, List<String>>>();
        System.out.println("超复杂类型也很简洁！");
        System.out.println();
    }

    /**
     * 3. var与Stream API
     */
    private static void demonstrateVarWithStreams() {
        System.out.println("3. var与Stream API\n");

        var numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 使用var简化Stream链式调用
        var evenNumbers = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * 2)
            .toList();

        System.out.println("原始数字: " + numbers);
        System.out.println("偶数×2: " + evenNumbers);
        System.out.println();

        // for-each中使用var
        System.out.println("遍历结果:");
        for (var num : evenNumbers) {
            System.out.print(num + " ");
        }
        System.out.println("\n");
    }

    /**
     * 4. var的最佳实践
     */
    private static void demonstrateVarBestPractices() {
        System.out.println("4. var的最佳实践\n");

        System.out.println("【适合使用var的场景】");

        // 1. 右侧类型明显
        var message = "Hello, World!";  // ✓ 明显是String
        var count = 100;  // ✓ 明显是int
        var price = 99.99;  // ✓ 明显是double
        System.out.println("✓ 右侧类型明显时使用var");

        // 2. 简化冗长的泛型声明
        var userList = new ArrayList<User>();  // ✓ 避免重复ArrayList<User>
        System.out.println("✓ 简化复杂泛型声明");

        // 3. 构建器模式
        var user = User.builder()
            .name("张三")
            .age(25)
            .email("zhangsan@example.com")
            .build();
        System.out.println("✓ 构建器模式返回类型明确");
        System.out.println();

        System.out.println("【不适合使用var的场景】");

        // 1. 右侧类型不明显
        // var data = getData();  // ✗ 返回值类型不明确
        System.out.println("✗ 方法返回值类型不明显");

        // 2. 使用字面量时需要特定类型
        // var result = 1;  // ✗ 推断为int，可能需要long
        long result = 1L;  // ✓ 明确指定long
        System.out.println("✗ 需要特定数值类型时");

        // 3. 接口类型赋值
        List<String> items = new ArrayList<>();  // ✓ 使用接口类型
        // var items = new ArrayList<String>();  // ✗ 推断为ArrayList而不是List
        System.out.println("✗ 想要使用接口类型时");
        System.out.println();
    }

    /**
     * 5. var的限制
     */
    private static void demonstrateVarLimitations() {
        System.out.println("5. var的限制\n");

        System.out.println("var不能用于：");
        System.out.println("✗ 类字段（成员变量）");
        System.out.println("✗ 方法参数");
        System.out.println("✗ 方法返回值");
        System.out.println("✗ 没有初始化的变量");
        System.out.println("✗ null初始化");
        System.out.println("✗ Lambda表达式（需要目标类型）");
        System.out.println("✗ 数组初始化器");
        System.out.println();

        // 正确用法示例
        var x = 10;  // ✓ 局部变量
        var y = "Hello";  // ✓ 有初始化值
        var list = List.of(1, 2, 3);  // ✓ 类型明确

        // 错误用法示例（注释掉，因为无法编译）
        // var z;  // ✗ 没有初始化
        // var n = null;  // ✗ 无法推断类型

        System.out.println("只能用于局部变量，且必须立即初始化！");
        System.out.println();
    }

    /**
     * 6. var在循环中的使用
     */
    private static void demonstrateVarInLoops() {
        System.out.println("6. var在循环中的使用\n");

        var numbers = List.of(1, 2, 3, 4, 5);

        // for-each循环
        System.out.println("for-each循环:");
        for (var num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println("\n");

        // 传统for循环
        System.out.println("传统for循环:");
        for (var i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println("\n");

        // Map遍历
        var map = Map.of("Java", 17, "Python", 3, "Go", 1);
        System.out.println("Map遍历:");
        for (var entry : map.entrySet()) {
            System.out.println("  " + entry.getKey() + " → " + entry.getValue());
        }
        System.out.println();
    }

    /**
     * 7. 实际应用示例
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("7. 实际应用示例\n");

        // 示例1: 数据处理
        System.out.println("【示例1】数据处理流水线:");
        var data = List.of("apple", "banana", "cherry", "date");
        var result = data.stream()
            .filter(s -> s.length() > 5)
            .map(String::toUpperCase)
            .toList();
        System.out.println("处理结果: " + result);
        System.out.println();

        // 示例2: 配置构建
        System.out.println("【示例2】配置对象构建:");
        var config = DatabaseConfig.builder()
            .host("localhost")
            .port(3306)
            .database("mydb")
            .username("root")
            .build();
        System.out.println(config);
        System.out.println();

        // 示例3: 资源管理
        System.out.println("【示例3】Try-with-resources:");
        System.out.println("  var reader = new BufferedReader(new FileReader(\"file.txt\"))");
        System.out.println("  类型推断为: BufferedReader");
        System.out.println();

        System.out.println("Spring Boot中的应用：");
        System.out.println("1. Controller方法中简化局部变量");
        System.out.println("   var user = userService.findById(id);");
        System.out.println();
        System.out.println("2. Service层复杂查询结果");
        System.out.println("   var results = repository.findByComplexCriteria(...);");
        System.out.println();
        System.out.println("3. 配置类中的Bean构建");
        System.out.println("   var mapper = new ObjectMapper();");
        System.out.println();
    }

    // ==================== 辅助类 ====================

    /**
     * 用户类（使用Builder模式）
     */
    static class User {
        private String name;
        private int age;
        private String email;

        public static UserBuilder builder() {
            return new UserBuilder();
        }

        static class UserBuilder {
            private String name;
            private int age;
            private String email;

            public UserBuilder name(String name) {
                this.name = name;
                return this;
            }

            public UserBuilder age(int age) {
                this.age = age;
                return this;
            }

            public UserBuilder email(String email) {
                this.email = email;
                return this;
            }

            public User build() {
                User user = new User();
                user.name = this.name;
                user.age = this.age;
                user.email = this.email;
                return user;
            }
        }

        @Override
        public String toString() {
            return String.format("User{name='%s', age=%d, email='%s'}", name, age, email);
        }
    }

    /**
     * 数据库配置类
     */
    static class DatabaseConfig {
        private String host;
        private int port;
        private String database;
        private String username;

        public static DatabaseConfigBuilder builder() {
            return new DatabaseConfigBuilder();
        }

        static class DatabaseConfigBuilder {
            private String host;
            private int port;
            private String database;
            private String username;

            public DatabaseConfigBuilder host(String host) {
                this.host = host;
                return this;
            }

            public DatabaseConfigBuilder port(int port) {
                this.port = port;
                return this;
            }

            public DatabaseConfigBuilder database(String database) {
                this.database = database;
                return this;
            }

            public DatabaseConfigBuilder username(String username) {
                this.username = username;
                return this;
            }

            public DatabaseConfig build() {
                DatabaseConfig config = new DatabaseConfig();
                config.host = this.host;
                config.port = this.port;
                config.database = this.database;
                config.username = this.username;
                return config;
            }
        }

        @Override
        public String toString() {
            return String.format("DatabaseConfig{host='%s', port=%d, database='%s', username='%s'}",
                    host, port, database, username);
        }
    }
}
