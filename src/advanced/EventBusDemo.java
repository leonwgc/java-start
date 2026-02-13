package advanced;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 事件总线模式详解
 * 学习目标：
 * 1. 理解事件驱动架构的核心概念
 * 2. 掌握观察者模式和发布-订阅模式
 * 3. 学习事件的同步和异步处理
 * 4. 了解Spring Event的基本原理
 *
 * 事件总线是什么？
 * - 组件间解耦通信的机制
 * - 发布者发布事件，订阅者接收事件
 * - 支持一对多的消息传递
 * - 降低组件间的直接依赖
 *
 * Spring应用：
 * - ApplicationEvent和ApplicationListener
 * - @EventListener注解
 * - 异步事件处理
 * - 事务事件监听
 */
public class EventBusDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 事件总线模式详解 ===\n");

        demonstrateBasicEventBus();
        demonstrateTypedEvents();
        demonstrateAsyncEventBus();
        demonstrateEventWithResult();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. 基础事件总线
     */
    private static void demonstrateBasicEventBus() {
        System.out.println("1. 基础事件总线\n");

        SimpleEventBus eventBus = new SimpleEventBus();

        // 订阅者1
        eventBus.subscribe("user.created", event -> {
            System.out.println("  [订阅者1] 收到事件: " + event);
            System.out.println("  → 发送欢迎邮件");
        });

        // 订阅者2
        eventBus.subscribe("user.created", event -> {
            System.out.println("  [订阅者2] 收到事件: " + event);
            System.out.println("  → 创建用户默认设置");
        });

        // 订阅者3（订阅不同事件）
        eventBus.subscribe("user.deleted", event -> {
            System.out.println("  [订阅者3] 收到事件: " + event);
            System.out.println("  → 清理用户数据");
        });

        System.out.println("发布事件: user.created");
        eventBus.publish("user.created", "userId=123");

        System.out.println("\n发布事件: user.deleted");
        eventBus.publish("user.deleted", "userId=456");

        System.out.println();
    }

    /**
     * 2. 类型安全的事件
     */
    private static void demonstrateTypedEvents() {
        System.out.println("2. 类型安全的事件\n");

        TypedEventBus eventBus = new TypedEventBus();

        // 订阅UserCreatedEvent
        eventBus.subscribe(UserCreatedEvent.class, event -> {
            System.out.println("  [用户服务] 处理用户创建事件");
            System.out.println("    - 用户ID: " + event.getUserId());
            System.out.println("    - 用户名: " + event.getUsername());
            System.out.println("    - 邮箱: " + event.getEmail());
        });

        // 订阅OrderCreatedEvent
        eventBus.subscribe(OrderCreatedEvent.class, event -> {
            System.out.println("  [订单服务] 处理订单创建事件");
            System.out.println("    - 订单ID: " + event.getOrderId());
            System.out.println("    - 金额: ¥" + event.getAmount());
            System.out.println("    - 用户ID: " + event.getUserId());
        });

        System.out.println("发布UserCreatedEvent:");
        eventBus.publish(new UserCreatedEvent("user-123", "张三", "zhangsan@example.com"));

        System.out.println("\n发布OrderCreatedEvent:");
        eventBus.publish(new OrderCreatedEvent("order-456", "user-123", 299.00));

        System.out.println();
    }

    /**
     * 3. 异步事件总线
     */
    private static void demonstrateAsyncEventBus() throws Exception {
        System.out.println("3. 异步事件总线\n");

        AsyncEventBus eventBus = new AsyncEventBus();

        // 快速订阅者
        eventBus.subscribe("data.sync", event -> {
            System.out.println("  [快速订阅者] 收到事件: " + event);
        });

        // 慢速订阅者（模拟耗时操作）
        eventBus.subscribe("data.sync", event -> {
            System.out.println("  [慢速订阅者] 开始处理: " + event);
            try {
                Thread.sleep(2000); // 模拟耗时2秒
                System.out.println("  [慢速订阅者] 处理完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("发布异步事件...");
        long startTime = System.currentTimeMillis();
        
        eventBus.publish("data.sync", "timestamp=" + System.currentTimeMillis());
        
        long publishTime = System.currentTimeMillis() - startTime;
        System.out.println("发布耗时: " + publishTime + "ms（异步，不等待订阅者）\n");

        System.out.println("继续执行其他任务...");
        Thread.sleep(500);
        System.out.println("其他任务完成\n");

        // 等待异步任务完成
        System.out.println("等待所有订阅者处理完成...");
        Thread.sleep(2000);

        eventBus.shutdown();
        System.out.println();
    }

    /**
     * 4. 带返回值的事件（查询模式）
     */
    private static void demonstrateEventWithResult() {
        System.out.println("4. 带返回值的事件（查询模式）\n");

        QueryEventBus eventBus = new QueryEventBus();

        // 注册查询处理器
        eventBus.registerHandler("user.getByName", query -> {
            String username = (String) query;
            System.out.println("  [用户服务] 查询用户: " + username);
            
            // 模拟数据库查询
            if ("admin".equals(username)) {
                return new User("admin", "管理员", "admin@example.com");
            }
            return null;
        });

        // 执行查询
        System.out.println("查询用户: admin");
        User user = (User) eventBus.query("user.getByName", "admin");
        
        if (user != null) {
            System.out.println("  查询结果:");
            System.out.println("    - ID: " + user.getId());
            System.out.println("    - 姓名: " + user.getName());
            System.out.println("    - 邮箱: " + user.getEmail());
        }

        System.out.println("\n查询用户: guest");
        User guest = (User) eventBus.query("user.getByName", "guest");
        System.out.println("  查询结果: " + (guest == null ? "未找到" : guest.getName()));

        System.out.println();
    }

    /**
     * 5. 实际应用场景
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("5. 实际应用场景\n");

        // 场景1: 用户注册流程
        System.out.println("场景1: 用户注册流程（多个服务协同）");
        UserRegistrationProcess registration = new UserRegistrationProcess();
        registration.register("user-789", "李四", "lisi@example.com");

        // 场景2: 订单处理流程
        System.out.println("\n场景2: 订单处理流程（解耦业务逻辑）");
        OrderProcessingSystem orderSystem = new OrderProcessingSystem();
        orderSystem.createOrder("order-999", "user-789", 599.00);

        // 场景3: 系统审计日志
        System.out.println("\n场景3: 系统审计日志");
        AuditSystem auditSystem = new AuditSystem();
        auditSystem.recordAction("user-789", "登录");
        auditSystem.recordAction("user-789", "修改密码");
        auditSystem.recordAction("user-789", "退出登录");

        System.out.println();
    }

    // ==================== 简单事件总线 ====================

    static class SimpleEventBus {
        private final Map<String, List<Consumer<String>>> subscribers = new ConcurrentHashMap<>();

        public void subscribe(String eventType, Consumer<String> listener) {
            subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
        }

        public void publish(String eventType, String eventData) {
            List<Consumer<String>> listeners = subscribers.get(eventType);
            if (listeners != null) {
                listeners.forEach(listener -> listener.accept(eventData));
            }
        }
    }

    // ==================== 类型安全的事件总线 ====================

    static class TypedEventBus {
        private final Map<Class<?>, List<Consumer<Object>>> subscribers = new ConcurrentHashMap<>();

        public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
            subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add((Consumer<Object>) listener);
        }

        public <T> void publish(T event) {
            List<Consumer<Object>> listeners = subscribers.get(event.getClass());
            if (listeners != null) {
                listeners.forEach(listener -> listener.accept(event));
            }
        }
    }

    // ==================== 异步事件总线 ====================

    static class AsyncEventBus {
        private final ExecutorService executor = Executors.newFixedThreadPool(4);
        private final Map<String, List<Consumer<String>>> subscribers = new ConcurrentHashMap<>();

        public void subscribe(String eventType, Consumer<String> listener) {
            subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
        }

        public void publish(String eventType, String eventData) {
            List<Consumer<String>> listeners = subscribers.get(eventType);
            if (listeners != null) {
                listeners.forEach(listener -> 
                    executor.submit(() -> listener.accept(eventData))
                );
            }
        }

        public void shutdown() {
            executor.shutdown();
        }
    }

    // ==================== 查询事件总线 ====================

    interface QueryHandler<Q, R> {
        R handle(Q query);
    }

    static class QueryEventBus {
        private final Map<String, QueryHandler<Object, Object>> handlers = new ConcurrentHashMap<>();

        public void registerHandler(String queryType, QueryHandler<Object, Object> handler) {
            handlers.put(queryType, handler);
        }

        public Object query(String queryType, Object queryData) {
            QueryHandler<Object, Object> handler = handlers.get(queryType);
            if (handler != null) {
                return handler.handle(queryData);
            }
            return null;
        }
    }

    // ==================== 事件对象 ====================

    static class UserCreatedEvent {
        private final String userId;
        private final String username;
        private final String email;
        private final long timestamp;

        public UserCreatedEvent(String userId, String username, String email) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.timestamp = System.currentTimeMillis();
        }

        public String getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public long getTimestamp() { return timestamp; }
    }

    static class OrderCreatedEvent {
        private final String orderId;
        private final String userId;
        private final double amount;
        private final long timestamp;

        public OrderCreatedEvent(String orderId, String userId, double amount) {
            this.orderId = orderId;
            this.userId = userId;
            this.amount = amount;
            this.timestamp = System.currentTimeMillis();
        }

        public String getOrderId() { return orderId; }
        public String getUserId() { return userId; }
        public double getAmount() { return amount; }
        public long getTimestamp() { return timestamp; }
    }

    static class User {
        private final String id;
        private final String name;
        private final String email;

        public User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    // ==================== 实际应用示例 ====================

    /**
     * 用户注册流程
     */
    static class UserRegistrationProcess {
        private final TypedEventBus eventBus = new TypedEventBus();

        public UserRegistrationProcess() {
            // 邮件服务订阅
            eventBus.subscribe(UserCreatedEvent.class, event -> {
                System.out.println("  [邮件服务] 发送欢迎邮件到: " + event.getEmail());
            });

            // 积分服务订阅
            eventBus.subscribe(UserCreatedEvent.class, event -> {
                System.out.println("  [积分服务] 赠送新用户积分: " + event.getUserId());
            });

            // 通知服务订阅
            eventBus.subscribe(UserCreatedEvent.class, event -> {
                System.out.println("  [通知服务] 推送新消息给: " + event.getUsername());
            });
        }

        public void register(String userId, String username, String email) {
            System.out.println("  [用户服务] 创建用户: " + username);
            
            // 发布事件
            eventBus.publish(new UserCreatedEvent(userId, username, email));
            
            System.out.println("  ✅ 用户注册完成");
        }
    }

    /**
     * 订单处理系统
     */
    static class OrderProcessingSystem {
        private final TypedEventBus eventBus = new TypedEventBus();

        public OrderProcessingSystem() {
            // 库存服务
            eventBus.subscribe(OrderCreatedEvent.class, event -> {
                System.out.println("  [库存服务] 扣减库存");
            });

            // 支付服务
            eventBus.subscribe(OrderCreatedEvent.class, event -> {
                System.out.println("  [支付服务] 创建支付单，金额: ¥" + event.getAmount());
            });

            // 物流服务
            eventBus.subscribe(OrderCreatedEvent.class, event -> {
                System.out.println("  [物流服务] 创建物流订单");
            });

            // 消息服务
            eventBus.subscribe(OrderCreatedEvent.class, event -> {
                System.out.println("  [消息服务] 发送订单确认短信");
            });
        }

        public void createOrder(String orderId, String userId, double amount) {
            System.out.println("  [订单服务] 创建订单: " + orderId);
            
            eventBus.publish(new OrderCreatedEvent(orderId, userId, amount));
            
            System.out.println("  ✅ 订单创建完成");
        }
    }

    /**
     * 审计系统
     */
    static class AuditSystem {
        private final SimpleEventBus eventBus = new SimpleEventBus();
        private final List<String> auditLogs = new ArrayList<>();

        public AuditSystem() {
            // 审计日志订阅者
            eventBus.subscribe("audit", log -> {
                auditLogs.add(log);
                System.out.println("  [审计] " + log);
            });

            // 安全监控订阅者
            eventBus.subscribe("audit", log -> {
                if (log.contains("修改密码") || log.contains("删除")) {
                    System.out.println("  [安全监控] 敏感操作告警: " + log);
                }
            });
        }

        public void recordAction(String userId, String action) {
            String log = String.format("用户 %s 执行操作: %s", userId, action);
            eventBus.publish("audit", log);
        }
    }
}
