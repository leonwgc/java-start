package advanced;

/**
 * 枚举类型学习
 * 学习目标：
 * 1. 理解枚举的基本概念和使用
 * 2. 掌握枚举的高级特性
 * 3. 学习枚举的实战应用
 * 4. 理解枚举在Spring中的应用
 */
public class EnumDemo {
    public static void main(String[] args) {
        System.out.println("=== 枚举类型学习 ===\n");

        // 示例1：基本枚举
        demonstrateBasicEnum();

        // 示例2：枚举的方法
        demonstrateEnumMethods();

        // 示例3：带属性的枚举
        demonstrateEnumWithFields();

        // 示例4：带方法的枚举
        demonstrateEnumWithMethods();

        // 示例5：枚举的实战应用
        demonstratePracticalExample();
    }

    // 基本枚举
    public static void demonstrateBasicEnum() {
        System.out.println("1. 基本枚举\n");
        System.out.println("作用：定义一组固定的常量");
        System.out.println("Spring应用：定义订单状态、用户角色等\n");

        // 使用枚举
        Day today = Day.MONDAY;
        System.out.println("今天是: " + today);

        // 枚举比较
        if (today == Day.MONDAY) {
            System.out.println("今天是星期一，新的一周开始了！");
        }

        // switch语句中使用枚举
        String message = getDayMessage(today);
        System.out.println(message);
        System.out.println();
    }

    // 获取星期的消息
    public static String getDayMessage(Day day) {
        return switch (day) {
            case MONDAY -> "周一：新的开始";
            case TUESDAY -> "周二：继续努力";
            case WEDNESDAY -> "周三：过半了";
            case THURSDAY -> "周四：快到周末了";
            case FRIDAY -> "周五：TGIF!";
            case SATURDAY, SUNDAY -> "周末：休息日";
        };
    }

    // 枚举的方法
    public static void demonstrateEnumMethods() {
        System.out.println("2. 枚举的常用方法\n");

        // values() - 获取所有枚举值
        System.out.println("所有星期:");
        for (Day day : Day.values()) {
            System.out.println("  " + day);
        }

        // valueOf() - 根据名称获取枚举
        Day friday = Day.valueOf("FRIDAY");
        System.out.println("\nvalueOf获取: " + friday);

        // ordinal() - 获取枚举的索引
        System.out.println("MONDAY的索引: " + Day.MONDAY.ordinal());
        System.out.println("FRIDAY的索引: " + Day.FRIDAY.ordinal());

        // name() - 获取枚举的名称
        System.out.println("枚举名称: " + Day.MONDAY.name());
        System.out.println();
    }

    // 带属性的枚举
    public static void demonstrateEnumWithFields() {
        System.out.println("3. 带属性的枚举\n");
        System.out.println("作用：枚举不仅是常量，还可以包含属性和方法");
        System.out.println("Spring应用：HTTP状态码、响应码定义等\n");

        // 使用带属性的枚举
        System.out.println("HTTP状态码:");
        for (HttpStatus status : HttpStatus.values()) {
            System.out.println(status.getCode() + " - " + status.getMessage());
        }

        // 根据code查找枚举
        HttpStatus status = HttpStatus.fromCode(404);
        System.out.println("\n查找404: " + status.getMessage());
        System.out.println();
    }

    // 带方法的枚举
    public static void demonstrateEnumWithMethods() {
        System.out.println("4. 带方法的枚举\n");
        System.out.println("作用：枚举可以实现接口，包含抽象方法");
        System.out.println("Spring应用：策略模式、计算逻辑等\n");

        // 使用带方法的枚举
        System.out.println("计算器操作:");
        int a = 10, b = 5;
        for (Operation op : Operation.values()) {
            System.out.println(a + " " + op + " " + b + " = " + op.apply(a, b));
        }
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("5. 实战案例 - 订单管理系统\n");

        // 创建订单
        Order order1 = new Order(1001, OrderStatus.PENDING);
        System.out.println("订单创建: " + order1);

        // 订单状态流转
        System.out.println("\n订单状态流转:");
        order1.updateStatus(OrderStatus.PAID);
        order1.updateStatus(OrderStatus.SHIPPED);
        order1.updateStatus(OrderStatus.DELIVERED);

        // 订单权限检查
        System.out.println("\n权限检查:");
        Order order2 = new Order(1002, OrderStatus.PENDING);
        System.out.println("待支付订单可以取消吗? " + order2.canCancel());

        Order order3 = new Order(1003, OrderStatus.DELIVERED);
        System.out.println("已送达订单可以取消吗? " + order3.canCancel());

        // 统计订单
        System.out.println("\n订单统计:");
        System.out.println("进行中的状态: " + OrderStatus.SHIPPED.getCategory());
        System.out.println("已完成的状态: " + OrderStatus.COMPLETED.getCategory());

        System.out.println("\n💡 Spring中的应用：");
        System.out.println("- 定义业务状态（订单状态、支付状态等）");
        System.out.println("- 定义用户角色和权限");
        System.out.println("- 定义配置选项");
        System.out.println("- JPA中使用@Enumerated注解映射枚举到数据库");
        System.out.println("- REST API中使用枚举作为请求参数\n");
    }

    // ========== 枚举定义 ==========

    // 基本枚举 - 星期
    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    // 带属性的枚举 - HTTP状态码
    enum HttpStatus {
        OK(200, "成功"),
        CREATED(201, "已创建"),
        BAD_REQUEST(400, "错误的请求"),
        UNAUTHORIZED(401, "未授权"),
        FORBIDDEN(403, "禁止访问"),
        NOT_FOUND(404, "未找到"),
        INTERNAL_SERVER_ERROR(500, "服务器内部错误");

        private final int code;
        private final String message;

        HttpStatus(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        // 根据code查找枚举
        /**
         * 根据HTTP状态码获取对应的HttpStatus枚举实例。
         *
         * <p>该方法遍历枚举中的所有值，查找与给定状态码匹配的枚举实例。
         * values()是枚举类型自动生成的静态方法,返回包含所有枚举常量的数组。
         *
         * @param code HTTP状态码(如200, 404, 500等)
         * @return 对应的HttpStatus枚举实例
         * @throws IllegalArgumentException 如果提供的状态码无效(不存在对应的枚举值)
         */
        public static HttpStatus fromCode(int code) {
            for (HttpStatus status : values()) {
                if (status.code == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid status code: " + code);
        }
    }

    // 带方法的枚举 - 计算器操作
    enum Operation {
        PLUS("+") {
            public double apply(double a, double b) { return a + b; }
        },
        MINUS("-") {
            public double apply(double a, double b) { return a - b; }
        },
        MULTIPLY("*") {
            public double apply(double a, double b) { return a * b; }
        },
        DIVIDE("/") {
            public double apply(double a, double b) {
                if (b == 0) throw new ArithmeticException("除数不能为0");
                return a / b;
            }
        };

        private final String symbol;

        Operation(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }

        public abstract double apply(double a, double b);
    }

    // 订单状态枚举
    enum OrderStatus {
        PENDING("待支付", "待处理"),
        PAID("已支付", "待处理"),
        PROCESSING("处理中", "进行中"),
        SHIPPED("已发货", "进行中"),
        DELIVERED("已送达", "进行中"),
        COMPLETED("已完成", "已完成"),
        CANCELLED("已取消", "已取消"),
        REFUNDED("已退款", "已取消");

        private final String description;
        private final String category;

        OrderStatus(String description, String category) {
            this.description = description;
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        // 判断是否可以取消
        public boolean canCancel() {
            return this == PENDING || this == PAID;
        }

        // 判断是否是最终状态
        public boolean isFinalState() {
            return this == COMPLETED || this == CANCELLED || this == REFUNDED;
        }
    }

    // 订单类
    static class Order {
        private int orderId;
        private OrderStatus status;

        public Order(int orderId, OrderStatus status) {
            this.orderId = orderId;
            this.status = status;
        }

        public void updateStatus(OrderStatus newStatus) {
            System.out.println("订单" + orderId + ": " + status.getDescription()
                             + " -> " + newStatus.getDescription());
            this.status = newStatus;
        }

        public boolean canCancel() {
            return status.canCancel();
        }

        @Override
        public String toString() {
            return "Order{orderId=" + orderId + ", status=" + status.getDescription() + "}";
        }
    }
}
