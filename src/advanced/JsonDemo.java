package advanced;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.*;

import java.util.*;
import java.time.LocalDateTime;

/**
 * JSON处理学习 - Jackson库
 * 学习目标：
 * 1. 理解JSON序列化和反序列化
 * 2. 掌握Jackson的基本使用
 * 3. 学习Jackson注解
 * 4. 理解JSON在Spring REST API中的应用
 */
public class JsonDemo {
    public static void main(String[] args) {
        System.out.println("=== JSON处理学习 ===\n");

        // 示例1：对象转JSON
        demonstrateObjectToJson();

        // 示例2：JSON转对象
        demonstrateJsonToObject();

        // 示例3：集合的JSON处理
        demonstrateCollectionJson();

        // 示例4：Jackson注解
        demonstrateJacksonAnnotations();

        // 示例5：实战案例
        demonstratePracticalExample();
    }

    // 对象转JSON
    public static void demonstrateObjectToJson() {
        System.out.println("1. 对象转JSON（序列化）\n");
        System.out.println("作用：将Java对象转换为JSON字符串");
        System.out.println("Spring应用：REST API返回JSON响应\n");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // 简单对象
            User user = new User(1, "张三", "zhangsan@example.com", 25);
            String json = mapper.writeValueAsString(user);
            System.out.println("用户对象转JSON:");
            System.out.println(json);

            // 带日期的对象
            Order order = new Order(1001, "已支付", 299.99);
            String orderJson = mapper.writeValueAsString(order);
            System.out.println("\n订单对象转JSON:");
            System.out.println(orderJson);

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    // JSON转对象
    public static void demonstrateJsonToObject() {
        System.out.println("2. JSON转对象（反序列化）\n");
        System.out.println("作用：将JSON字符串解析为Java对象");
        System.out.println("Spring应用：接收前端POST请求的JSON数据\n");

        try {
            ObjectMapper mapper = new ObjectMapper();

            // JSON字符串转对象
            String json = "{\"id\":1,\"name\":\"李四\",\"email\":\"lisi@example.com\",\"age\":30}";
            User user = mapper.readValue(json, User.class);
            System.out.println("JSON转用户对象:");
            System.out.println("  ID: " + user.getId());
            System.out.println("  姓名: " + user.getName());
            System.out.println("  邮箱: " + user.getEmail());
            System.out.println("  年龄: " + user.getAge());

            // 处理嵌套对象
            String orderJson = "{\"id\":1002,\"status\":\"已发货\",\"amount\":599.99,\"user\":{\"id\":1,\"name\":\"张三\",\"email\":\"zhangsan@example.com\",\"age\":25}}";
            Order order = mapper.readValue(orderJson, Order.class);
            System.out.println("\nJSON转订单对象:");
            System.out.println("  订单ID: " + order.getId());
            System.out.println("  状态: " + order.getStatus());
            System.out.println("  金额: " + order.getAmount());
            if (order.getUser() != null) {
                System.out.println("  用户: " + order.getUser().getName());
            }

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    // 集合的JSON处理
    public static void demonstrateCollectionJson() {
        System.out.println("3. 集合的JSON处理\n");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // List转JSON
            List<User> users = Arrays.asList(
                new User(1, "张三", "zhangsan@example.com", 25),
                new User(2, "李四", "lisi@example.com", 30),
                new User(3, "王五", "wangwu@example.com", 28)
            );
            String listJson = mapper.writeValueAsString(users);
            System.out.println("List转JSON:");
            System.out.println(listJson);

            // JSON转List
            String json = "[{\"id\":1,\"name\":\"赵六\",\"email\":\"zhaoliu@example.com\",\"age\":35}]";
            List<User> userList = mapper.readValue(json, new TypeReference<List<User>>(){});
            System.out.println("\nJSON转List:");
            userList.forEach(u -> System.out.println("  " + u.getName()));

            // Map转JSON
            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("message", "操作成功");
            data.put("data", users);
            String mapJson = mapper.writeValueAsString(data);
            System.out.println("\nMap转JSON:");
            System.out.println(mapJson);

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    // Jackson注解
    public static void demonstrateJacksonAnnotations() {
        System.out.println("4. Jackson注解\n");
        System.out.println("作用：控制JSON序列化和反序列化的行为");
        System.out.println("Spring应用：自定义JSON格式、字段过滤等\n");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // 使用注解的对象
            Product product = new Product(
                "P001",
                "笔记本电脑",
                "高性能笔记本",
                8999.99,
                "admin123"
            );

            String json = mapper.writeValueAsString(product);
            System.out.println("带注解的对象转JSON:");
            System.out.println(json);
            System.out.println("注意：password字段被@JsonIgnore忽略了");

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("5. 实战案例 - 构建REST API响应\n");

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // 成功响应
            ApiResponse<User> successResponse = ApiResponse.success(
                new User(1, "张三", "zhangsan@example.com", 25),
                "查询成功"
            );
            System.out.println("成功响应:");
            System.out.println(mapper.writeValueAsString(successResponse));

            // 错误响应
            ApiResponse<Void> errorResponse = ApiResponse.error(404, "用户不存在");
            System.out.println("\n错误响应:");
            System.out.println(mapper.writeValueAsString(errorResponse));

            // 分页响应
            List<User> users = Arrays.asList(
                new User(1, "张三", "zhangsan@example.com", 25),
                new User(2, "李四", "lisi@example.com", 30)
            );
            PageResponse<User> pageResponse = new PageResponse<>(users, 1, 10, 100);
            System.out.println("\n分页响应:");
            System.out.println(mapper.writeValueAsString(pageResponse));

            System.out.println("\n💡 Spring中的应用：");
            System.out.println("- @RestController自动将返回值序列化为JSON");
            System.out.println("- @RequestBody自动将JSON请求体反序列化为对象");
            System.out.println("- @JsonProperty自定义字段名称");
            System.out.println("- @JsonIgnore忽略敏感字段");
            System.out.println("- @JsonFormat格式化日期时间");

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
        System.out.println();
    }

    // ========== 数据类 ==========

    // 用户类
    static class User {
        private int id;
        private String name;
        private String email;
        private int age;

        public User() {}

        public User(int id, String name, String email, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }

    // 订单类
    static class Order {
        private int id;
        private String status;
        private double amount;
        private User user;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createTime = LocalDateTime.now();

        public Order() {}

        public Order(int id, String status, double amount) {
            this.id = id;
            this.status = status;
            this.amount = amount;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
        public LocalDateTime getCreateTime() { return createTime; }
        public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    }

    // 产品类（带注解）
    static class Product {
        @JsonProperty("product_id")
        private String id;

        private String name;

        @JsonProperty("desc")
        private String description;

        private double price;

        @JsonIgnore
        private String password;

        public Product() {}

        public Product(String id, String name, String description, double price, String password) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.password = password;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // API响应类
    static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;

        public static <T> ApiResponse<T> success(T data, String message) {
            ApiResponse<T> response = new ApiResponse<>();
            response.code = 200;
            response.message = message;
            response.data = data;
            return response;
        }

        public static <T> ApiResponse<T> error(int code, String message) {
            ApiResponse<T> response = new ApiResponse<>();
            response.code = code;
            response.message = message;
            return response;
        }

        public int getCode() { return code; }
        public void setCode(int code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }

    // 分页响应类
    static class PageResponse<T> {
        private List<T> data;
        private int page;
        private int pageSize;
        private long total;

        public PageResponse(List<T> data, int page, int pageSize, long total) {
            this.data = data;
            this.page = page;
            this.pageSize = pageSize;
            this.total = total;
        }

        public List<T> getData() { return data; }
        public void setData(List<T> data) { this.data = data; }
        public int getPage() { return page; }
        public void setPage(int page) { this.page = page; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
    }
}
