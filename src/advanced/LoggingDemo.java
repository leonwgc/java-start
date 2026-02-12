package advanced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * SLF4J + Logback 日志框架演示
 * 学习目标：
 * 1. 理解日志框架的分层架构（SLF4J 门面 + Logback 实现）
 * 2. 掌握日志级别和格式化输出
 * 3. 理解 MDC（Mapped Diagnostic Context）诊断上下文
 *
 * Spring应用场景：
 * - Spring Boot 默认使用 Logback 作为日志实现
 * - 统一日志规范，避免使用 System.out
 * - 分布式系统中的请求追踪（traceId）
 *
 * 依赖说明：
 * 本项目已配置 SLF4J 2.0.9 + Logback 1.4.14
 * 日志配置文件：src/main/resources/logback.xml（可选）
 */
public class LoggingDemo {

    // 获取当前类的 Logger 实例
    private static final Logger log = LoggerFactory.getLogger(LoggingDemo.class);

    public static void main(String[] args) {
        System.out.println("=== SLF4J + Logback 日志框架学习 ===\n");

        demonstrateLogLevels();
        demonstrateParameterizedLogging();
        demonstrateExceptionLogging();
        demonstrateMDC();
        demonstratePerformance();
    }

    /**
     * 演示日志级别
     * 作用：根据重要程度输出不同级别的日志
     * Spring应用：开发环境 DEBUG，生产环境 INFO/WARN
     */
    private static void demonstrateLogLevels() {
        System.out.println("1. 日志级别（TRACE < DEBUG < INFO < WARN < ERROR）\n");

        log.trace("TRACE - 最详细的追踪信息，通常不启用");
        log.debug("DEBUG - 调试信息，开发环境使用");
        log.info("INFO  - 重要的业务流程信息");
        log.warn("WARN  - 警告信息，不影响运行但需要注意");
        log.error("ERROR - 错误信息，需要立即处理");

        System.out.println("\n说明：");
        System.out.println("- 默认级别通常为 INFO，只会输出 INFO/WARN/ERROR");
        System.out.println("- 可通过 logback.xml 或环境变量调整级别");
        System.out.println("- Spring Boot: logging.level.root=DEBUG\n");
    }

    /**
     * 演示参数化日志
     * 作用：高效的日志消息格式化（避免字符串拼接）
     * Spring应用：记录请求参数、业务数据
     */
    private static void demonstrateParameterizedLogging() {
        System.out.println("2. 参数化日志（性能优化）\n");

        String username = "zhangsan";
        int age = 25;
        String department = "研发部";

        // ❌ 不推荐：字符串拼接（总是执行，浪费性能）
        log.debug("传统方式：用户 " + username + " 年龄 " + age + " 部门 " + department);

        // ✅ 推荐：参数化日志（只在日志级别启用时才格式化）
        log.info("参数化：用户 {} 年龄 {} 部门 {}", username, age, department);

        System.out.println("\n优势：");
        System.out.println("- 如果日志级别未启用，不会执行字符串拼接");
        System.out.println("- 代码更简洁，性能更好");
        System.out.println("- Spring 中大量使用这种方式\n");
    }

    /**
     * 演示异常日志
     * 作用：记录完整的异常堆栈信息
     * Spring应用：全局异常处理器、事务回滚日志
     */
    private static void demonstrateExceptionLogging() {
        System.out.println("3. 异常日志记录\n");

        try {
            int result = 10 / 0; // 触发异常
        } catch (Exception e) {
            // ✅ 正确方式：将异常对象作为最后一个参数
            log.error("计算过程发生异常：操作数 {}, 除数 {}", 10, 0, e);
        }

        System.out.println("\n注意事项：");
        System.out.println("- 异常对象必须作为最后一个参数传入");
        System.out.println("- 会自动打印完整的堆栈跟踪信息");
        System.out.println("- Spring @ControllerAdvice 中常用于全局异常处理\n");
    }

    /**
     * 演示 MDC（Mapped Diagnostic Context）
     * 作用：在日志中添加上下文信息（如请求 ID、用户 ID）
     * Spring应用：分布式追踪、请求链路日志
     */
    private static void demonstrateMDC() {
        System.out.println("4. MDC 诊断上下文\n");

        // 模拟处理 3 个请求
        for (int i = 1; i <= 3; i++) {
            String requestId = "REQ-" + System.currentTimeMillis() + "-" + i;
            String userId = "user" + (100 + i);

            try {
                // 设置 MDC 上下文
                MDC.put("requestId", requestId);
                MDC.put("userId", userId);

                log.info("开始处理请求");
                processOrder(i);
                log.info("请求处理完成");

            } finally {
                // 清理 MDC（避免内存泄漏）
                MDC.clear();
            }
        }

        System.out.println("\n说明：");
        System.out.println("- MDC 数据存储在 ThreadLocal 中");
        System.out.println("- 可在 logback.xml 中配置 %X{requestId} 输出");
        System.out.println("- Spring Cloud Sleuth 使用 MDC 实现分布式追踪\n");
    }

    /**
     * 模拟订单处理
     */
    private static void processOrder(int orderId) {
        log.debug("查询订单信息：orderId = {}", orderId);
        log.debug("验证库存：商品 ID = {}", orderId * 10);
        log.info("订单创建成功：订单号 = ORDER-{}", orderId);
    }

    /**
     * 演示日志性能优化
     * 作用：避免不必要的计算和对象创建
     * Spring应用：高并发场景下的性能优化
     */
    private static void demonstratePerformance() {
        System.out.println("5. 日志性能优化\n");

        // ❌ 不推荐：即使日志未启用，也会执行复杂计算
        log.debug("复杂对象：" + createComplexObject().toString());

        // ✅ 推荐：先判断日志级别
        if (log.isDebugEnabled()) {
            log.debug("复杂对象：{}", createComplexObject());
        }

        System.out.println("性能建议：");
        System.out.println("- 参数化日志自动优化简单类型");
        System.out.println("- 复杂计算需要手动判断日志级别");
        System.out.println("- 生产环境避免大量 DEBUG 日志");
        System.out.println("\nSpring Boot 配置示例：");
        System.out.println("  logging.level.root=INFO");
        System.out.println("  logging.level.com.example.myapp=DEBUG");
        System.out.println("  logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
    }

    /**
     * 模拟创建复杂对象
     */
    private static Object createComplexObject() {
        // 模拟耗时操作
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ComplexData("测试数据", 12345);
    }

    /**
     * 复杂数据类
     */
    static class ComplexData {
        private final String name;
        private final int value;

        public ComplexData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("ComplexData{name='%s', value=%d}", name, value);
        }
    }
}
