package advanced;

/**
 * 密封类（Sealed Classes）详解（Java 17+）
 * 学习目标：
 * 1. 理解密封类的作用和使用场景
 * 2. 掌握sealed、permits、non-sealed关键字
 * 3. 学习密封类与模式匹配的结合
 * 4. 了解密封类在领域建模中的应用
 *
 * 密封类是什么？
 * - 允许类或接口限制哪些类可以继承或实现它
 * - 提供比final更细粒度的继承控制
 * - 增强类型安全性和领域模型完整性
 * - 编译器可以进行穷尽性检查
 *
 * Spring应用：
 * - 领域事件建模
 * - 状态模式实现
 * - API响应类型定义
 * - 异常层次结构
 */
public class SealedClassesDemo {

    public static void main(String[] args) {
        System.out.println("=== 密封类详解 ===\n");

        demonstrateBasicSealedClass();
        demonstrateSealedInterface();
        demonstratePatternMatching();
        demonstrateStatePattern();
        demonstrateEventSystem();
        demonstrateAPIResponse();
    }

    /**
     * 1. 基础密封类
     */
    private static void demonstrateBasicSealedClass() {
        System.out.println("1. 基础密封类\n");

        System.out.println("密封类的三种子类形式：");
        System.out.println("- final：不可再被继承");
        System.out.println("- sealed：继续限制继承");
        System.out.println("- non-sealed：重新开放继承\n");

        // 创建不同的图形实例
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        Shape square = new Square(4.0);

        System.out.println("圆形面积: " + circle.area());
        System.out.println("矩形面积: " + rectangle.area());
        System.out.println("正方形面积: " + square.area());
        System.out.println();
    }

    /**
     * 2. 密封接口
     */
    private static void demonstrateSealedInterface() {
        System.out.println("2. 密封接口\n");

        // 创建不同的支付方式
        Payment creditCard = new CreditCardPayment("1234-5678-9012-3456", 1000.0);
        Payment alipay = new AlipayPayment("user@example.com", 500.0);
        Payment wechat = new WeChatPayment("wx_openid_123", 300.0);

        System.out.println("信用卡支付: " + creditCard.process());
        System.out.println("支付宝支付: " + alipay.process());
        System.out.println("微信支付: " + wechat.process());
        System.out.println();
    }

    /**
     * 3. 模式匹配（Java 21+）
     */
    private static void demonstratePatternMatching() {
        System.out.println("3. 密封类与模式匹配\n");

        Shape[] shapes = {
            new Circle(3.0),
            new Rectangle(4.0, 5.0),
            new Square(4.0)
        };

        System.out.println("使用传统instanceof：");
        for (Shape shape : shapes) {
            System.out.println(describeShapeTraditional(shape));
        }

        System.out.println("\n使用模式匹配（Java 21+风格）：");
        for (Shape shape : shapes) {
            System.out.println(describeShapePattern(shape));
        }
        System.out.println();
    }

    /**
     * 4. 状态模式实现
     */
    private static void demonstrateStatePattern() {
        System.out.println("4. 状态模式实现\n");

        System.out.println("订单状态流转：");
        OrderState state1 = new PendingOrder("ORD001", 999.0);
        System.out.println("初始状态: " + state1.getStatusMessage());

        OrderState state2 = new PaidOrder("ORD001", 999.0, "PAY123");
        System.out.println("支付后: " + state2.getStatusMessage());

        OrderState state3 = new ShippedOrder("ORD001", 999.0, "SHIP456");
        System.out.println("发货后: " + state3.getStatusMessage());

        OrderState state4 = new DeliveredOrder("ORD001", 999.0);
        System.out.println("完成后: " + state4.getStatusMessage());
        System.out.println();
    }

    /**
     * 5. 事件系统
     */
    private static void demonstrateEventSystem() {
        System.out.println("5. 事件系统（领域事件）\n");

        // 创建不同类型的领域事件
        DomainEvent[] events = {
            new UserRegisteredEvent("user123", "张三", "zhang@example.com"),
            new OrderPlacedEvent("order456", "user123", 999.0),
            new PaymentCompletedEvent("pay789", "order456", 999.0, "支付宝"),
            new OrderShippedEvent("order456", "SF123456")
        };

        System.out.println("处理领域事件：");
        for (DomainEvent event : events) {
            handleEvent(event);
        }
        System.out.println();
    }

    /**
     * 6. API响应类型
     */
    private static void demonstrateAPIResponse() {
        System.out.println("6. API响应类型定义\n");

        // 模拟不同的API响应
        ApiResponse success = new SuccessResponse(200, "操作成功",
            new UserData("张三", "zhang@example.com"));
        ApiResponse error = new ErrorResponse(400, "请求参数错误", "INVALID_PARAM");
        ApiResponse notFound = new NotFoundResponse(404, "用户不存在", "/api/users/999");

        System.out.println("成功响应: " + formatResponse(success));
        System.out.println("错误响应: " + formatResponse(error));
        System.out.println("404响应: " + formatResponse(notFound));
        System.out.println();
    }

    // ==================== 辅助方法 ====================

    private static String describeShapeTraditional(Shape shape) {
        if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            return "圆形，半径=" + c.radius + "，面积=" + c.area();
        } else if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            return "矩形，宽=" + r.width + "，高=" + r.height + "，面积=" + r.area();
        } else if (shape instanceof Square) {
            Square s = (Square) shape;
            return "正方形，边长=" + s.side + "，面积=" + s.area();
        }
        return "未知图形";
    }

    private static String describeShapePattern(Shape shape) {
        // Java 21+的模式匹配写法（这里用传统方式模拟）
        if (shape instanceof Circle c) {
            return "圆形，半径=" + c.radius + "，面积=" + c.area();
        } else if (shape instanceof Rectangle r) {
            return "矩形，宽=" + r.width + "，高=" + r.height + "，面积=" + r.area();
        } else if (shape instanceof Square s) {
            return "正方形，边长=" + s.side + "，面积=" + s.area();
        }
        return "未知图形";
    }

    private static void handleEvent(DomainEvent event) {
        if (event instanceof UserRegisteredEvent e) {
            System.out.println("  [用户注册] " + e.username + " (" + e.email + ")");
        } else if (event instanceof OrderPlacedEvent e) {
            System.out.println("  [订单创建] 订单号=" + e.orderId + "，金额=" + e.amount);
        } else if (event instanceof PaymentCompletedEvent e) {
            System.out.println("  [支付完成] 订单号=" + e.orderId + "，方式=" + e.paymentMethod);
        } else if (event instanceof OrderShippedEvent e) {
            System.out.println("  [订单发货] 订单号=" + e.orderId + "，快递单号=" + e.trackingNumber);
        }
    }

    private static String formatResponse(ApiResponse response) {
        if (response instanceof SuccessResponse s) {
            return "成功 [" + s.code + "] " + s.message + ", 数据: " + s.data;
        } else if (response instanceof ErrorResponse e) {
            return "错误 [" + e.code + "] " + e.message + ", 错误码: " + e.errorCode;
        } else if (response instanceof NotFoundResponse n) {
            return "未找到 [" + n.code + "] " + n.message + ", 路径: " + n.path;
        }
        return "未知响应";
    }

    // ==================== 1. 基础密封类：图形示例 ====================

    /**
     * 密封类：限制只能由Circle、Rectangle继承
     */
    static sealed abstract class Shape permits Circle, Rectangle {
        abstract double area();
    }

    /**
     * final类：不可再被继承
     */
    static final class Circle extends Shape {
        final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        double area() {
            return Math.PI * radius * radius;
        }
    }

    /**
     * sealed类：继续限制继承（只允许Square继承）
     */
    static sealed class Rectangle extends Shape permits Square {
        final double width;
        final double height;

        Rectangle(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        double area() {
            return width * height;
        }
    }

    /**
     * non-sealed类：重新开放继承
     * 注意：这里为了演示用final，实际可以是non-sealed
     */
    static final class Square extends Rectangle {
        final double side;

        Square(double side) {
            super(side, side);
            this.side = side;
        }
    }

    // ==================== 2. 密封接口：支付方式 ====================

    /**
     * 密封接口：限制支付方式的实现类
     */
    sealed interface Payment permits CreditCardPayment, AlipayPayment, WeChatPayment {
        String process();
    }

    record CreditCardPayment(String cardNumber, double amount) implements Payment {
        @Override
        public String process() {
            return "信用卡支付 " + amount + " 元，卡号: " + cardNumber.substring(0, 4) + "****";
        }
    }

    record AlipayPayment(String account, double amount) implements Payment {
        @Override
        public String process() {
            return "支付宝支付 " + amount + " 元，账号: " + account;
        }
    }

    record WeChatPayment(String openId, double amount) implements Payment {
        @Override
        public String process() {
            return "微信支付 " + amount + " 元，OpenID: " + openId;
        }
    }

    // ==================== 4. 状态模式 ====================

    /**
     * 订单状态（密封接口）
     */
    sealed interface OrderState
        permits PendingOrder, PaidOrder, ShippedOrder, DeliveredOrder {
        String getStatusMessage();
    }

    record PendingOrder(String orderId, double amount) implements OrderState {
        @Override
        public String getStatusMessage() {
            return "订单 " + orderId + " 待支付，金额: " + amount;
        }
    }

    record PaidOrder(String orderId, double amount, String paymentId) implements OrderState {
        @Override
        public String getStatusMessage() {
            return "订单 " + orderId + " 已支付，支付单号: " + paymentId;
        }
    }

    record ShippedOrder(String orderId, double amount, String trackingNumber) implements OrderState {
        @Override
        public String getStatusMessage() {
            return "订单 " + orderId + " 已发货，快递单号: " + trackingNumber;
        }
    }

    record DeliveredOrder(String orderId, double amount) implements OrderState {
        @Override
        public String getStatusMessage() {
            return "订单 " + orderId + " 已完成";
        }
    }

    // ==================== 5. 领域事件系统 ====================

    /**
     * 领域事件基类（密封接口）
     */
    sealed interface DomainEvent
        permits UserRegisteredEvent, OrderPlacedEvent, PaymentCompletedEvent, OrderShippedEvent {
    }

    record UserRegisteredEvent(String userId, String username, String email)
        implements DomainEvent {}

    record OrderPlacedEvent(String orderId, String userId, double amount)
        implements DomainEvent {}

    record PaymentCompletedEvent(String paymentId, String orderId, double amount, String paymentMethod)
        implements DomainEvent {}

    record OrderShippedEvent(String orderId, String trackingNumber)
        implements DomainEvent {}

    // ==================== 6. API响应类型 ====================

    /**
     * API响应（密封接口）
     */
    sealed interface ApiResponse
        permits SuccessResponse, ErrorResponse, NotFoundResponse {
    }

    record SuccessResponse(int code, String message, Object data)
        implements ApiResponse {}

    record ErrorResponse(int code, String message, String errorCode)
        implements ApiResponse {}

    record NotFoundResponse(int code, String message, String path)
        implements ApiResponse {}

    // 用户数据类
    record UserData(String name, String email) {}
}
