package advanced;

import java.util.*;

/**
 * Optional类学习
 * 学习目标：
 * 1. 理解Optional的作用（避免空指针异常）
 * 2. 掌握Optional的创建和使用
 * 3. 学习Optional的常用方法
 * 4. 理解Optional在Spring中的应用
 */
public class OptionalDemo {
    public static void main(String[] args) {
        System.out.println("=== Optional类学习 ===\n");

        // 示例1：Optional的创建
        demonstrateCreation();

        // 示例2：获取Optional的值
        demonstrateGetValue();

        // 示例3：Optional的判断和处理
        demonstrateCheckAndHandle();

        // 示例4：Optional的转换
        demonstrateTransformation();

        // 示例5：实战案例
        demonstratePracticalExample();
    }

    // Optional的创建
    public static void demonstrateCreation() {
        System.out.println("1. Optional的创建\n");
        System.out.println("作用：提供一个可能包含或不包含值的容器");
        System.out.println("Spring应用：Repository查询结果、配置值获取等\n");

        // 创建包含值的Optional
        Optional<String> optional1 = Optional.of("Hello");
        System.out.println("Optional.of(): " + optional1);

        // 创建可能为null的Optional
        String nullableValue = null;
        Optional<String> optional2 = Optional.ofNullable(nullableValue);
        System.out.println("Optional.ofNullable(null): " + optional2);

        String nonNullValue = "World";
        Optional<String> optional3 = Optional.ofNullable(nonNullValue);
        System.out.println("Optional.ofNullable(value): " + optional3);

        // 创建空Optional
        Optional<String> optional4 = Optional.empty();
        System.out.println("Optional.empty(): " + optional4);

        System.out.println("\n⚠️  注意：Optional.of(null)会抛出NullPointerException");
        System.out.println("           应使用Optional.ofNullable()来处理可能为null的值\n");
    }

    // 获取Optional的值
    public static void demonstrateGetValue() {
        System.out.println("2. 获取Optional的值\n");

        Optional<String> optional = Optional.of("Java");

        // get() - 直接获取值（如果为空会抛异常）
        System.out.println("get(): " + optional.get());

        // orElse() - 提供默认值
        Optional<String> empty = Optional.empty();
        String value1 = empty.orElse("默认值");
        System.out.println("orElse(): " + value1);

        // orElseGet() - 通过Supplier提供默认值
        String value2 = empty.orElseGet(() -> "通过Supplier获取的默认值");
        System.out.println("orElseGet(): " + value2);

        // orElseThrow() - 为空时抛出异常
        try {
            String value3 = empty.orElseThrow(() -> new RuntimeException("值不存在！"));
        } catch (RuntimeException e) {
            System.out.println("orElseThrow(): " + e.getMessage());
        }

        System.out.println("\n💡 最佳实践：");
        System.out.println("- 避免直接使用get()，可能抛出NoSuchElementException");
        System.out.println("- 优先使用orElse()或orElseGet()");
        System.out.println("- orElse()适用于简单默认值，orElseGet()适用于需要计算的默认值\n");
    }

    // Optional的判断和处理
    public static void demonstrateCheckAndHandle() {
        System.out.println("3. Optional的判断和处理\n");

        Optional<String> optional = Optional.of("Spring Boot");
        Optional<String> empty = Optional.empty();

        // isPresent() - 判断是否有值
        System.out.println("isPresent(): " + optional.isPresent());
        System.out.println("isEmpty(): " + optional.isEmpty());

        // ifPresent() - 如果有值则执行操作
        System.out.print("ifPresent(): ");
        optional.ifPresent(value -> System.out.println("值是: " + value));

        // ifPresentOrElse() - 有值执行第一个，无值执行第二个
        System.out.print("有值时: ");
        optional.ifPresentOrElse(
            value -> System.out.println("处理值: " + value),
            () -> System.out.println("值不存在")
        );

        System.out.print("无值时: ");
        empty.ifPresentOrElse(
            value -> System.out.println("处理值: " + value),
            () -> System.out.println("值不存在")
        );
        System.out.println();
    }

    // Optional的转换
    public static void demonstrateTransformation() {
        System.out.println("4. Optional的转换\n");
        System.out.println("作用：对Optional中的值进行转换和过滤");
        System.out.println("Spring应用：链式处理数据、条件过滤等\n");

        Optional<String> optional = Optional.of("java");

        // map() - 转换值
        Optional<String> upperCase = optional.map(String::toUpperCase);
        System.out.println("map转换: " + upperCase.get());

        Optional<Integer> length = optional.map(String::length);
        System.out.println("map获取长度: " + length.get());

        // flatMap() - 转换为Optional
        Optional<String> result = optional.flatMap(s -> Optional.of(s.toUpperCase()));
        System.out.println("flatMap转换: " + result.get());

        // filter() - 过滤值
        Optional<String> filtered1 = optional.filter(s -> s.length() > 3);
        System.out.println("filter(长度>3): " + filtered1);

        Optional<String> filtered2 = optional.filter(s -> s.length() > 10);
        System.out.println("filter(长度>10): " + filtered2);

        // 链式调用
        String finalResult = Optional.of("  Spring Boot  ")
            .map(String::trim)
            .map(String::toUpperCase)
            .filter(s -> s.startsWith("SPRING"))
            .orElse("未找到");
        System.out.println("链式调用结果: " + finalResult);
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("5. 实战案例\n");
        System.out.println("场景：用户服务中查找用户信息\n");

        UserService userService = new UserService();

        // 案例1：查找用户
        System.out.println("查找存在的用户:");
        Optional<User> user1 = userService.findUserById(1);
        user1.ifPresent(user -> System.out.println("  找到用户: " + user.getName()));

        System.out.println("\n查找不存在的用户:");
        Optional<User> user2 = userService.findUserById(999);
        String userName = user2.map(User::getName).orElse("未知用户");
        System.out.println("  用户名: " + userName);

        // 案例2：获取用户邮箱（可能为空）
        System.out.println("\n获取用户邮箱:");
        String email = userService.findUserById(1)
            .flatMap(User::getEmail)
            .orElse("no-email@example.com");
        System.out.println("  邮箱: " + email);

        // 案例3：条件处理
        System.out.println("\n查找管理员用户:");
        userService.findUserById(1)
            .filter(User::isAdmin)
            .ifPresentOrElse(
                user -> System.out.println("  " + user.getName() + " 是管理员"),
                () -> System.out.println("  该用户不是管理员")
            );

        // 案例4：多层Optional处理
        System.out.println("\n获取用户的邮箱域名:");
        String domain = userService.findUserById(1)
            .flatMap(User::getEmail)
            .map(e -> e.substring(e.indexOf("@") + 1))
            .orElse("无域名");
        System.out.println("  域名: " + domain);

        System.out.println("\n💡 Spring中的应用：");
        System.out.println("- Spring Data JPA的findById()返回Optional<T>");
        System.out.println("- Spring Configuration中使用Optional<T>处理可选配置");
        System.out.println("- REST API中使用Optional避免返回null");
        System.out.println("- 减少空指针异常，让代码更安全\n");
    }

    // 用户类
    static class User {
        private int id;
        private String name;
        private String email;
        private boolean admin;

        public User(int id, String name, String email, boolean admin) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.admin = admin;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public boolean isAdmin() { return admin; }

        public Optional<String> getEmail() {
            return Optional.ofNullable(email);
        }
    }

    // 用户服务类
    static class UserService {
        private List<User> users = Arrays.asList(
            new User(1, "张三", "zhangsan@example.com", false),
            new User(2, "李四", null, true),
            new User(3, "王五", "wangwu@example.com", false)
        );

        public Optional<User> findUserById(int id) {
            return users.stream()
                       .filter(user -> user.getId() == id)
                       .findFirst();
        }
    }
}
