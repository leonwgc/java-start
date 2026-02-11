package advanced;

import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * AOP面向切面编程学习
 * 学习目标：
 * 1. 理解AOP的概念和术语
 * 2. 学习动态代理实现AOP
 * 3. 理解Spring AOP的工作原理
 * 4. 掌握AOP的实际应用场景
 */
public class AopDemo {
    public static void main(String[] args) {
        System.out.println("=== AOP面向切面编程学习 ===\n");

        // 示例1：AOP基本概念
        demonstrateAopConcepts();

        // 示例2：动态代理实现日志切面
        demonstrateLoggingAspect();

        // 示例3：事务切面
        demonstrateTransactionAspect();

        // 示例4：性能监控切面
        demonstratePerformanceAspect();

        // 示例5：综合应用
        demonstratePracticalExample();

        System.out.println("\n💡 Spring AOP：");
        System.out.println("- @Aspect定义切面");
        System.out.println("- @Before、@After、@Around定义通知");
        System.out.println("- @Transactional声明式事务");
        System.out.println("- @Cacheable缓存切面");
        System.out.println("- 无侵入式增强业务逻辑\n");
    }

    // AOP基本概念
    public static void demonstrateAopConcepts() {
        System.out.println("1. AOP基本概念\n");
        System.out.println("核心术语：");
        System.out.println("- Aspect（切面）：横切关注点的模块化");
        System.out.println("- JoinPoint（连接点）：程序执行的某个点");
        System.out.println("- Advice（通知）：在连接点执行的动作");
        System.out.println("- Pointcut（切点）：匹配连接点的表达式");
        System.out.println("- Weaving（织入）：将切面应用到目标对象\n");

        System.out.println("通知类型：");
        System.out.println("- Before（前置通知）：方法执行前");
        System.out.println("- After（后置通知）：方法执行后");
        System.out.println("- Around（环绕通知）：方法执行前后");
        System.out.println("- AfterReturning（返回后通知）：成功返回后");
        System.out.println("- AfterThrowing（异常通知）：抛出异常后\n");

        System.out.println("应用场景：");
        System.out.println("✓ 日志记录");
        System.out.println("✓ 事务管理");
        System.out.println("✓ 权限检查");
        System.out.println("✓ 性能监控");
        System.out.println("✓ 缓存处理");
        System.out.println("✓ 异常处理\n");
    }

    // 日志切面
    public static void demonstrateLoggingAspect() {
        System.out.println("2. 日志切面\n");
        System.out.println("场景：自动记录方法调用日志\n");

        // 创建目标对象
        UserService target = new UserServiceImpl();

        // 创建代理对象（应用日志切面）
        UserService proxy = (UserService) LoggingAspect.createProxy(target);

        // 调用方法（自动记录日志）
        proxy.addUser("张三");
        proxy.getUser("张三");

        System.out.println();
    }

    // 事务切面
    public static void demonstrateTransactionAspect() {
        System.out.println("3. 事务切面\n");
        System.out.println("场景：自动管理事务\n");

        OrderService target = new OrderServiceImpl();
        OrderService proxy = (OrderService) TransactionAspect.createProxy(target);

        // 成功场景
        System.out.println("场景1：正常订单");
        proxy.createOrder("ORDER001", 1000.0);

        // 失败场景
        System.out.println("\n场景2：异常订单（金额为负）");
        try {
            proxy.createOrder("ORDER002", -100.0);
        } catch (Exception e) {
            System.out.println("  捕获到异常: " + e.getMessage());
        }

        System.out.println();
    }

    // 性能监控切面
    public static void demonstratePerformanceAspect() {
        System.out.println("4. 性能监控切面\n");
        System.out.println("场景：自动监控方法执行时间\n");

        DataService target = new DataServiceImpl();
        DataService proxy = (DataService) PerformanceAspect.createProxy(target);

        proxy.processData("测试数据");
        proxy.queryData();

        System.out.println();
    }

    // 综合应用
    public static void demonstratePracticalExample() {
        System.out.println("5. 综合应用 - 多个切面组合\n");
        System.out.println("组合日志、事务、性能监控切面\n");

        PaymentService target = new PaymentServiceImpl();

        // 应用多个切面
        PaymentService proxy1 = (PaymentService) LoggingAspect.createProxy(target);
        PaymentService proxy2 = (PaymentService) TransactionAspect.createProxy(proxy1);
        PaymentService proxy3 = (PaymentService) PerformanceAspect.createProxy(proxy2);

        // 执行业务方法
        proxy3.pay("USER001", 299.99);

        System.out.println("\n💡 对比Spring AOP:");
        System.out.println("");
        System.out.println("@Service");
        System.out.println("@Transactional  // 事务切面");
        System.out.println("public class PaymentService {");
        System.out.println("    @Cacheable     // 缓存切面");
        System.out.println("    public void pay(String userId, double amount) {");
        System.out.println("        // 业务代码");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("\nSpring自动应用所有切面，无需手动创建代理！");
        System.out.println();
    }

    // ========== 业务接口和实现 ==========

    interface UserService {
        void addUser(String name);
        String getUser(String name);
    }

    static class UserServiceImpl implements UserService {
        @Override
        public void addUser(String name) {
            System.out.println("    业务方法: 添加用户 " + name);
        }

        @Override
        public String getUser(String name) {
            System.out.println("    业务方法: 查询用户 " + name);
            return name;
        }
    }

    interface OrderService {
        void createOrder(String orderId, double amount);
    }

    static class OrderServiceImpl implements OrderService {
        @Override
        public void createOrder(String orderId, double amount) {
            if (amount < 0) {
                throw new RuntimeException("金额不能为负");
            }
            System.out.println("    业务方法: 创建订单 " + orderId + ", 金额: " + amount);
        }
    }

    interface DataService {
        void processData(String data);
        String queryData();
    }

    static class DataServiceImpl implements DataService {
        @Override
        public void processData(String data) {
            System.out.println("    业务方法: 处理数据 " + data);
            try { Thread.sleep(100); } catch (Exception e) {}
        }

        @Override
        public String queryData() {
            System.out.println("    业务方法: 查询数据");
            try { Thread.sleep(50); } catch (Exception e) {}
            return "查询结果";
        }
    }

    interface PaymentService {
        void pay(String userId, double amount);
    }

    static class PaymentServiceImpl implements PaymentService {
        @Override
        public void pay(String userId, double amount) {
            System.out.println("    业务方法: 用户 " + userId + " 支付 " + amount + " 元");
            try { Thread.sleep(100); } catch (Exception e) {}
        }
    }

    // ========== 切面实现 ==========

    // 日志切面
    static class LoggingAspect implements InvocationHandler {
        private Object target;

        public LoggingAspect(Object target) {
            this.target = target;
        }

        public static Object createProxy(Object target) {
            return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LoggingAspect(target)
            );
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 前置通知
            System.out.println("  [日志] 调用方法: " + method.getName());
            System.out.println("  [日志] 参数: " + java.util.Arrays.toString(args));

            // 执行目标方法
            Object result = method.invoke(target, args);

            // 后置通知
            System.out.println("  [日志] 返回值: " + result);

            return result;
        }
    }

    // 事务切面
    static class TransactionAspect implements InvocationHandler {
        private Object target;

        public TransactionAspect(Object target) {
            this.target = target;
        }

        public static Object createProxy(Object target) {
            return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionAspect(target)
            );
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("  [事务] 开启事务");

            try {
                // 执行目标方法
                Object result = method.invoke(target, args);

                // 提交事务
                System.out.println("  [事务] 提交事务");
                return result;

            } catch (Exception e) {
                // 回滚事务
                System.out.println("  [事务] 回滚事务");
                throw e;
            }
        }
    }

    // 性能监控切面
    static class PerformanceAspect implements InvocationHandler {
        private Object target;

        public PerformanceAspect(Object target) {
            this.target = target;
        }

        public static Object createProxy(Object target) {
            return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new PerformanceAspect(target)
            );
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 记录开始时间
            long startTime = System.currentTimeMillis();
            System.out.println("  [性能] 开始执行: " + method.getName());

            // 执行目标方法
            Object result = method.invoke(target, args);

            // 计算执行时间
            long endTime = System.currentTimeMillis();
            System.out.println("  [性能] 执行完成，耗时: " + (endTime - startTime) + "ms");

            return result;
        }
    }
}
