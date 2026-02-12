package advanced;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * 定时任务演示
 * 学习目标：
 * 1. 理解 Java 中的三种定时任务机制
 * 2. 掌握 ScheduledExecutorService 的使用
 * 3. 理解延迟执行和周期性执行的区别
 *
 * Spring应用场景：
 * - Spring @Scheduled 注解底层使用 ScheduledExecutorService
 * - 定时数据同步、缓存刷新、报表生成
 * - 定时清理过期数据、发送提醒邮件
 */
public class ScheduledTaskDemo {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 定时任务学习 ===\n");

        demonstrateTimerTask();
        System.out.println("\n" + "=".repeat(60) + "\n");

        demonstrateScheduledExecutorService();
        System.out.println("\n" + "=".repeat(60) + "\n");

        demonstrateScheduledPatterns();
        System.out.println("\n" + "=".repeat(60) + "\n");

        demonstrateSpringScheduled();
    }

    /**
     * 演示 Timer 和 TimerTask（传统方式）
     * 作用：简单的定时任务调度
     * 缺点：单线程执行，异常会导致整个 Timer 停止
     */
    private static void demonstrateTimerTask() throws InterruptedException {
        System.out.println("1. Timer 和 TimerTask（不推荐）\n");

        Timer timer = new Timer("MyTimer");

        // 延迟 1 秒执行
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("[" + now() + "] 延迟任务执行");
            }
        }, 1000);

        // 延迟 1 秒后，每 500 毫秒执行一次
        timer.schedule(new TimerTask() {
            private int count = 0;

            @Override
            public void run() {
                count++;
                System.out.println("[" + now() + "] 周期任务执行第 " + count + " 次");

                if (count >= 3) {
                    timer.cancel(); // 取消所有任务
                    System.out.println("[" + now() + "] Timer 已取消\n");
                }
            }
        }, 1000, 500);

        Thread.sleep(3000);

        System.out.println("Timer 缺点：");
        System.out.println("- 单线程执行，多个任务会相互阻塞");
        System.out.println("- 任务抛异常会导致整个 Timer 终止");
        System.out.println("- 无法利用多核 CPU");
        System.out.println("- Spring 不推荐使用\n");
    }

    /**
     * 演示 ScheduledExecutorService（推荐）
     * 作用：基于线程池的定时任务调度
     * 优点：多线程、异常隔离、灵活控制
     */
    private static void demonstrateScheduledExecutorService() throws InterruptedException {
        System.out.println("2. ScheduledExecutorService（推荐）\n");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // 1) 延迟执行（schedule）
        scheduler.schedule(() -> {
            System.out.println("[" + now() + "] 延迟 500ms 执行一次");
        }, 500, TimeUnit.MILLISECONDS);

        // 2) 固定速率执行（scheduleAtFixedRate）
        // 任务开始时间固定间隔，不考虑任务执行时长
        ScheduledFuture<?> fixedRate = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("[" + now() + "] Fixed Rate - 每 300ms 开始执行");
            sleep(100); // 模拟任务耗时
        }, 500, 300, TimeUnit.MILLISECONDS);

        // 3) 固定延迟执行（scheduleWithFixedDelay）
        // 上次任务结束后，延迟固定时间再执行下次任务
        ScheduledFuture<?> fixedDelay = scheduler.scheduleWithFixedDelay(() -> {
            System.out.println("[" + now() + "] Fixed Delay - 任务结束后延迟 300ms");
            sleep(100); // 模拟任务耗时
        }, 500, 300, TimeUnit.MILLISECONDS);

        // 运行 2 秒后停止
        Thread.sleep(2000);
        fixedRate.cancel(false);
        fixedDelay.cancel(false);

        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println("\n区别总结：");
        System.out.println("- scheduleAtFixedRate：  按固定频率执行（可能并发）");
        System.out.println("- scheduleWithFixedDelay：任务结束后延迟执行（不会并发）");
        System.out.println("- Spring @Scheduled 默认使用 fixedDelay 模式\n");
    }

    /**
     * 演示常见定时任务模式
     * 作用：模拟 Spring 中的常见定时任务场景
     * Spring应用：对应 @Scheduled 注解的不同配置
     */
    private static void demonstrateScheduledPatterns() throws InterruptedException {
        System.out.println("3. Spring 定时任务模式\n");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        // 模式 1：固定速率（每 N 秒执行）
        // 对应：@Scheduled(fixedRate = 1000)
        System.out.println("✓ 模式 1：固定速率执行");
        ScheduledFuture<?> task1 = scheduler.scheduleAtFixedRate(
            () -> System.out.println("  [" + now() + "] 每秒执行 - 数据同步任务"),
            0, 1, TimeUnit.SECONDS
        );

        // 模式 2：固定延迟（上次完成后延迟 N 秒）
        // 对应：@Scheduled(fixedDelay = 2000)
        System.out.println("✓ 模式 2：固定延迟执行");
        ScheduledFuture<?> task2 = scheduler.scheduleWithFixedDelay(
            () -> {
                System.out.println("  [" + now() + "] 延迟 2 秒执行 - 缓存刷新任务");
                sleep(500); // 模拟耗时操作
            },
            0, 2, TimeUnit.SECONDS
        );

        // 模式 3：初始延迟（首次延迟，后续固定速率）
        // 对应：@Scheduled(initialDelay = 3000, fixedRate = 1000)
        System.out.println("✓ 模式 3：初始延迟执行");
        ScheduledFuture<?> task3 = scheduler.scheduleAtFixedRate(
            () -> System.out.println("  [" + now() + "] 初始延迟 3 秒，然后每秒执行"),
            3, 1, TimeUnit.SECONDS
        );

        // 运行 5 秒
        Thread.sleep(5000);

        task1.cancel(false);
        task2.cancel(false);
        task3.cancel(false);
        scheduler.shutdown();

        System.out.println();
    }

    /**
     * 演示 Spring @Scheduled 注解用法
     * 作用：展示 Spring 中定时任务的配置方式
     * Spring应用：实际项目中的定时任务注解
     */
    private static void demonstrateSpringScheduled() {
        System.out.println("4. Spring @Scheduled 注解对照\n");

        System.out.println("Spring Boot 配置：");
        System.out.println("@EnableScheduling  // 在主类上启用定时任务\n");

        System.out.println("常用注解示例：\n");

        System.out.println("// 1. 固定速率（每 5 秒执行一次）");
        System.out.println("@Scheduled(fixedRate = 5000)");
        System.out.println("public void syncData() {");
        System.out.println("    log.info(\"执行数据同步\");");
        System.out.println("}\n");

        System.out.println("// 2. 固定延迟（上次完成后延迟 10 秒）");
        System.out.println("@Scheduled(fixedDelay = 10000)");
        System.out.println("public void cleanCache() {");
        System.out.println("    log.info(\"清理过期缓存\");");
        System.out.println("}\n");

        System.out.println("// 3. Cron 表达式（每天凌晨 2 点执行）");
        System.out.println("@Scheduled(cron = \"0 0 2 * * ?\")");
        System.out.println("public void generateReport() {");
        System.out.println("    log.info(\"生成每日报表\");");
        System.out.println("}\n");

        System.out.println("// 4. 初始延迟 + 固定速率");
        System.out.println("@Scheduled(initialDelay = 60000, fixedRate = 300000)");
        System.out.println("public void checkHealth() {");
        System.out.println("    log.info(\"健康检查\");");
        System.out.println("}\n");

        System.out.println("Cron 表达式说明：");
        System.out.println("格式：秒 分 时 日 月 星期");
        System.out.println("示例：");
        System.out.println("  \"0 0 12 * * ?\"       - 每天中午 12 点");
        System.out.println("  \"0 15 10 ? * MON-FRI\" - 工作日上午 10:15");
        System.out.println("  \"0 0/5 * * * ?\"      - 每 5 分钟");
        System.out.println("  \"0 0 2 1 * ?\"       - 每月 1 号凌晨 2 点\n");

        System.out.println("注意事项：");
        System.out.println("- @Scheduled 方法必须无参数、无返回值");
        System.out.println("- 默认单线程执行，可配置线程池");
        System.out.println("- 异常不会导致定时任务停止");
        System.out.println("- 建议使用 @Async 实现异步定时任务");
    }

    /**
     * 获取当前时间字符串
     */
    private static String now() {
        return LocalDateTime.now().format(TIME_FORMAT);
    }

    /**
     * 睡眠（忽略中断异常）
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
