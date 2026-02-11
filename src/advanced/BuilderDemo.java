package advanced;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Builder模式和链式调用学习
 * 学习目标：
 * 1. 理解Builder模式的设计思想
 * 2. 掌握链式调用的实现方式
 * 3. 学习Lombok @Builder的底层原理
 * 4. 理解流式API的设计理念
 */
public class BuilderDemo {
    public static void main(String[] args) {
        System.out.println("=== Builder模式和链式调用学习 ===\n");

        // 示例1：传统构造器的问题
        demonstrateTraditionalWay();

        // 示例2：Builder模式解决方案
        demonstrateBuilderPattern();

        // 示例3：链式调用
        demonstrateFluentAPI();

        // 示例4：Stream API的链式调用
        demonstrateStreamChaining();

        // 示例5：实战案例
        demonstratePracticalExample();

        System.out.println("\n💡 Spring和Lombok中的应用：");
        System.out.println("- Lombok @Builder自动生成构建器");
        System.out.println("- Spring Security配置链式调用");
        System.out.println("- ResponseEntity.ok().body()链式构建");
        System.out.println("- QueryDSL的链式查询");
        System.out.println("- MyBatis-Plus的LambdaQueryWrapper\n");
    }

    // 传统构造器的问题
    public static void demonstrateTraditionalWay() {
        System.out.println("1. 传统构造器的问题\n");
        System.out.println("问题：");
        System.out.println("- 参数过多时难以记忆顺序");
        System.out.println("- 可选参数需要多个构造器重载");
        System.out.println("- 代码可读性差\n");

        // 传统方式：参数顺序容易搞混
        User user1 = new User(
            "张三",
            "zhangsan@example.com",
            "13812345678",
            25,
            "北京市",
            "会员"
        );
        System.out.println("传统构造: " + user1);

        // 问题：参数顺序混乱
        // new User("张三", "13812345678", "zhangsan@example.com", ...) // ❌ 容易搞错

        System.out.println("\n❌ 缺点：参数多了容易搞混，不知道每个参数是什么");
        System.out.println();
    }

    // Builder模式
    public static void demonstrateBuilderPattern() {
        System.out.println("2. Builder模式解决方案\n");
        System.out.println("优势：");
        System.out.println("- 参数清晰明确");
        System.out.println("- 支持可选参数");
        System.out.println("- 代码可读性强\n");

        // Builder模式：清晰明确
        UserDTO user = UserDTO.builder()
            .name("李四")
            .email("lisi@example.com")
            .phone("13987654321")
            .age(30)
            .address("上海市")
            .memberLevel("VIP会员")
            .build();

        System.out.println("Builder构造: " + user);

        // 可选参数：只设置必需字段
        UserDTO simpleUser = UserDTO.builder()
            .name("王五")
            .email("wangwu@example.com")
            .build();

        System.out.println("简化构造: " + simpleUser);
        System.out.println("\n✓ 优点：参数意图清晰，顺序任意，可选参数灵活");
        System.out.println();
    }

    // 链式调用
    public static void demonstrateFluentAPI() {
        System.out.println("3. 链式调用（Fluent API）\n");
        System.out.println("作用：使代码更流畅、更易读\n");

        // 配置类的链式调用
        DatabaseConfig config = new DatabaseConfig()
            .setHost("localhost")
            .setPort(3306)
            .setDatabase("mydb")
            .setUsername("root")
            .setPassword("password")
            .setPoolSize(20)
            .setAutoCommit(true);

        System.out.println("数据库配置: " + config);

        // HTTP请求构建器
        HttpRequest request = new HttpRequest()
            .setMethod("POST")
            .setUrl("https://api.example.com/users")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer token123")
            .setBody("{\"name\":\"张三\"}")
            .setTimeout(30000);

        System.out.println("\nHTTP请求: " + request);
        System.out.println();
    }

    // Stream链式调用
    public static void demonstrateStreamChaining() {
        System.out.println("4. Stream API的链式调用\n");
        System.out.println("特点：多个操作连贯执行，代码简洁\n");

        List<Product> products = Arrays.asList(
            new Product("手机", 2999, "电子", 4.5),
            new Product("电脑", 5999, "电子", 4.8),
            new Product("图书", 49, "书籍", 4.2),
            new Product("键盘", 299, "电子", 4.6),
            new Product("鼠标", 99, "电子", 4.3)
        );

        System.out.println("原始数据 (" + products.size() + "件商品)");

        // 链式操作：过滤 -> 排序 -> 映射 -> 收集
        List<String> result = products.stream()
            .filter(p -> p.category.equals("电子"))     // 过滤电子类
            .filter(p -> p.price > 200)                 // 价格>200
            .sorted((a, b) -> Double.compare(b.rating, a.rating))  // 按评分降序
            .map(p -> p.name + "(¥" + p.price + ", ⭐" + p.rating + ")")
            .collect(Collectors.toList());

        System.out.println("\n处理结果:");
        result.forEach(item -> System.out.println("  " + item));
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("5. 实战案例 - SQL查询构建器\n");
        System.out.println("场景：动态构建SQL查询（类似MyBatis-Plus）\n");

        // 构建查询
        QueryBuilder query = new QueryBuilder()
            .select("id", "name", "email", "created_at")
            .from("users")
            .where("status", "=", "active")
            .where("age", ">=", 18)
            .orderBy("created_at", "DESC")
            .limit(10);

        System.out.println("生成的SQL:");
        System.out.println(query.build());

        // 复杂查询
        QueryBuilder complexQuery = new QueryBuilder()
            .select("u.id", "u.name", "COUNT(o.id) as order_count")
            .from("users u")
            .join("orders o", "u.id = o.user_id")
            .where("o.status", "=", "completed")
            .groupBy("u.id", "u.name")
            .having("COUNT(o.id)", ">", 5)
            .orderBy("order_count", "DESC")
            .limit(20);

        System.out.println("\n复杂查询SQL:");
        System.out.println(complexQuery.build());

        System.out.println("\n💡 对比Lombok和MyBatis-Plus:");
        System.out.println("");
        System.out.println("// Lombok @Builder");
        System.out.println("@Data");
        System.out.println("@Builder");
        System.out.println("public class User {");
        System.out.println("    private String name;");
        System.out.println("    private String email;");
        System.out.println("}");
        System.out.println("");
        System.out.println("// MyBatis-Plus链式查询");
        System.out.println("List<User> users = new LambdaQueryChainWrapper<>(userMapper)");
        System.out.println("    .eq(User::getStatus, \"active\")");
        System.out.println("    .ge(User::getAge, 18)");
        System.out.println("    .orderByDesc(User::getCreatedAt)");
        System.out.println("    .list();");
        System.out.println();
    }

    // ========== 传统方式类 ==========

    static class User {
        String name, email, phone, address, memberLevel;
        int age;

        public User(String name, String email, String phone, int age, String address, String memberLevel) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.age = age;
            this.address = address;
            this.memberLevel = memberLevel;
        }

        @Override
        public String toString() {
            return name + ", " + email + ", " + phone + ", " + age + "岁";
        }
    }

    // ========== Builder模式类 ==========

    static class UserDTO {
        private String name;
        private String email;
        private String phone;
        private Integer age;
        private String address;
        private String memberLevel;

        // 私有构造器
        private UserDTO() {}

        // 静态Builder类
        public static class Builder {
            private UserDTO user = new UserDTO();

            public Builder name(String name) {
                user.name = name;
                return this;
            }

            public Builder email(String email) {
                user.email = email;
                return this;
            }

            public Builder phone(String phone) {
                user.phone = phone;
                return this;
            }

            public Builder age(Integer age) {
                user.age = age;
                return this;
            }

            public Builder address(String address) {
                user.address = address;
                return this;
            }

            public Builder memberLevel(String level) {
                user.memberLevel = level;
                return this;
            }

            public UserDTO build() {
                // 可以在这里添加验证逻辑
                if (user.name == null || user.email == null) {
                    throw new IllegalStateException("姓名和邮箱是必需的");
                }
                return user;
            }
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public String toString() {
            return "UserDTO{name='" + name + "', email='" + email +
                   "', phone='" + phone + "', age=" + age +
                   ", address='" + address + "', level='" + memberLevel + "'}";
        }
    }

    // ========== 链式调用类 ==========

    static class DatabaseConfig {
        private String host, database, username, password;
        private int port, poolSize;
        private boolean autoCommit;

        public DatabaseConfig setHost(String host) {
            this.host = host;
            return this;
        }

        public DatabaseConfig setPort(int port) {
            this.port = port;
            return this;
        }

        public DatabaseConfig setDatabase(String database) {
            this.database = database;
            return this;
        }

        public DatabaseConfig setUsername(String username) {
            this.username = username;
            return this;
        }

        public DatabaseConfig setPassword(String password) {
            this.password = password;
            return this;
        }

        public DatabaseConfig setPoolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public DatabaseConfig setAutoCommit(boolean autoCommit) {
            this.autoCommit = autoCommit;
            return this;
        }

        @Override
        public String toString() {
            return "jdbc:mysql://" + host + ":" + port + "/" + database +
                   " (pool=" + poolSize + ", autoCommit=" + autoCommit + ")";
        }
    }

    static class HttpRequest {
        private String method, url, body;
        private Map<String, String> headers = new HashMap<>();
        private int timeout;

        public HttpRequest setMethod(String method) {
            this.method = method;
            return this;
        }

        public HttpRequest setUrl(String url) {
            this.url = url;
            return this;
        }

        public HttpRequest addHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public HttpRequest setBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        @Override
        public String toString() {
            return method + " " + url + " (headers=" + headers.size() + ", timeout=" + timeout + "ms)";
        }
    }

    // ========== 辅助类 ==========

    static class Product {
        String name, category;
        double price, rating;

        Product(String name, double price, String category, double rating) {
            this.name = name;
            this.price = price;
            this.category = category;
            this.rating = rating;
        }
    }

    // SQL查询构建器
    static class QueryBuilder {
        private List<String> selectFields = new ArrayList<>();
        private String fromTable;
        private List<String> joins = new ArrayList<>();
        private List<String> whereConditions = new ArrayList<>();
        private List<String> groupByFields = new ArrayList<>();
        private List<String> havingConditions = new ArrayList<>();
        private List<String> orderByFields = new ArrayList<>();
        private Integer limitValue;

        public QueryBuilder select(String... fields) {
            selectFields.addAll(Arrays.asList(fields));
            return this;
        }

        public QueryBuilder from(String table) {
            this.fromTable = table;
            return this;
        }

        public QueryBuilder join(String table, String condition) {
            joins.add("LEFT JOIN " + table + " ON " + condition);
            return this;
        }

        public QueryBuilder where(String field, String operator, Object value) {
            String valueStr = value instanceof String ? "'" + value + "'" : value.toString();
            whereConditions.add(field + " " + operator + " " + valueStr);
            return this;
        }

        public QueryBuilder groupBy(String... fields) {
            groupByFields.addAll(Arrays.asList(fields));
            return this;
        }

        public QueryBuilder having(String field, String operator, Object value) {
            havingConditions.add(field + " " + operator + " " + value);
            return this;
        }

        public QueryBuilder orderBy(String field, String direction) {
            orderByFields.add(field + " " + direction);
            return this;
        }

        public QueryBuilder limit(int limit) {
            this.limitValue = limit;
            return this;
        }

        public String build() {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT ").append(String.join(", ", selectFields));
            sql.append("\nFROM ").append(fromTable);

            if (!joins.isEmpty()) {
                sql.append("\n").append(String.join("\n", joins));
            }

            if (!whereConditions.isEmpty()) {
                sql.append("\nWHERE ").append(String.join(" AND ", whereConditions));
            }

            if (!groupByFields.isEmpty()) {
                sql.append("\nGROUP BY ").append(String.join(", ", groupByFields));
            }

            if (!havingConditions.isEmpty()) {
                sql.append("\nHAVING ").append(String.join(" AND ", havingConditions));
            }

            if (!orderByFields.isEmpty()) {
                sql.append("\nORDER BY ").append(String.join(", ", orderByFields));
            }

            if (limitValue != null) {
                sql.append("\nLIMIT ").append(limitValue);
            }

            return sql.toString();
        }
    }
}
