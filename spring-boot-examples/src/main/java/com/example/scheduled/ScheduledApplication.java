package com.example.scheduled;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Spring Boot 定时任务学习示例
 * 学习目标：
 * 1. 理解Spring定时任务机制
 * 2. 掌握@Scheduled注解的使用
 * 3. 学习Cron表达式
 * 4. 了解定时任务配置和最佳实践
 *
 * 运行方式：
 * ./run-app.sh scheduled
 *
 * 测试接口：
 * # 查看任务执行日志
 * curl http://localhost:8080/api/tasks/logs
 *
 * # 查看任务状态
 * curl http://localhost:8080/api/tasks/status
 *
 * # 手动触发任务
 * curl -X POST http://localhost:8080/api/tasks/trigger
 *
 * # 启动/停止动态任务
 * curl -X POST http://localhost:8080/api/tasks/dynamic/start
 * curl -X POST http://localhost:8080/api/tasks/dynamic/stop
 */
@Slf4j
@EnableScheduling  // 启用定时任务支持
@SpringBootApplication
public class ScheduledApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledApplication.class, args);
        log.info("===========================================");
        log.info("定时任务示例启动成功！");
        log.info("定时任务将自动执行，观察控制台输出");
        log.info("测试地址：http://localhost:8080");
        log.info("===========================================");
    }
}

/**
 * 定时任务配置
 */
@Configuration
class ScheduledConfig implements SchedulingConfigurer {

    /**
     * 配置任务调度器
     * 默认单线程，这里配置线程池
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // 线程池大小
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.initialize();
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }
}

/**
 * 任务执行日志
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class TaskLog {
    private String taskName;
    private String description;
    private LocalDateTime executeTime;
    private String result;
}

/**
 * 日志服务
 */
@Component
class TaskLogService {
    private final List<TaskLog> logs = new CopyOnWriteArrayList<>();
    private static final int MAX_LOGS = 100;

    public void addLog(String taskName, String description, String result) {
        TaskLog log = new TaskLog(
            taskName,
            description,
            LocalDateTime.now(),
            result
        );
        logs.add(0, log);  // 添加到开头

        // 保持最多100条日志
        if (logs.size() > MAX_LOGS) {
            logs.remove(logs.size() - 1);
        }
    }

    public List<TaskLog> getAllLogs() {
        return new ArrayList<>(logs);
    }

    public void clearLogs() {
        logs.clear();
    }
}

/**
 * 基础定时任务示例
 */
@Slf4j
@Component
class BasicScheduledTasks {

    private final TaskLogService logService;
    private final AtomicInteger fixedRateCounter = new AtomicInteger(0);
    private final AtomicInteger fixedDelayCounter = new AtomicInteger(0);

    public BasicScheduledTasks(TaskLogService logService) {
        this.logService = logService;
    }

    /**
     * 1. fixedRate - 固定频率执行
     * 从上一次任务开始时间计算，每5秒执行一次
     * 即使上一次任务还没完成，时间到了就开始下一次
     */
    @Scheduled(fixedRate = 5000)  // 5秒
    public void taskWithFixedRate() {
        int count = fixedRateCounter.incrementAndGet();
        String msg = String.format("固定频率任务执行 #%d", count);
        log.info(msg);
        logService.addLog("FixedRate", "每5秒执行一次（fixedRate）", msg);
    }

    /**
     * 2. fixedDelay - 固定延迟执行
     * 从上一次任务完成时间计算，延迟8秒后执行
     * 上一次任务完成后，等待8秒再开始下一次
     */
    @Scheduled(fixedDelay = 8000)  // 8秒
    public void taskWithFixedDelay() {
        int count = fixedDelayCounter.incrementAndGet();
        String msg = String.format("固定延迟任务执行 #%d", count);
        log.info(msg);
        logService.addLog("FixedDelay", "上次执行完成后延迟8秒（fixedDelay）", msg);

        // 模拟任务耗时
        try {
            Thread.sleep(2000);  // 任务本身耗时2秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 3. initialDelay - 初始延迟
     * 应用启动后延迟10秒首次执行，之后每10秒执行一次
     */
    @Scheduled(initialDelay = 10000, fixedRate = 10000)
    public void taskWithInitialDelay() {
        String msg = "初始延迟任务执行（启动10秒后首次执行）";
        log.info(msg);
        logService.addLog("InitialDelay", "初始延迟10秒，之后每10秒执行", msg);
    }
}

/**
 * Cron表达式定时任务示例
 */
@Slf4j
@Component
class CronScheduledTasks {

    private final TaskLogService logService;

    public CronScheduledTasks(TaskLogService logService) {
        this.logService = logService;
    }

    /**
     * 4. Cron表达式 - 每分钟执行
     * Cron: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 * * * * ?")  // 每分钟的0秒执行
    public void taskEveryMinute() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String msg = String.format("每分钟任务执行 at %s", time);
        log.info(msg);
        logService.addLog("EveryMinute", "每分钟执行（Cron: 0 * * * * ?）", msg);
    }

    /**
     * 5. Cron表达式 - 每10秒执行
     */
    @Scheduled(cron = "0/10 * * * * ?")  // 每10秒执行
    public void taskEvery10Seconds() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String msg = String.format("每10秒任务执行 at %s", time);
        log.info(msg);
        logService.addLog("Every10Seconds", "每10秒执行（Cron: 0/10 * * * * ?）", msg);
    }

    /**
     * 6. Cron表达式 - 工作日上午9点执行
     * 用于生产环境的定时报表、数据清理等
     */
    @Scheduled(cron = "0 0 9 * * MON-FRI")  // 周一到周五上午9点
    public void taskOnWeekdayMorning() {
        String msg = "工作日早上9点任务执行";
        log.info(msg);
        logService.addLog("WeekdayMorning", "周一至周五上午9:00（Cron: 0 0 9 * * MON-FRI）", msg);
    }

    /**
     * 7. Cron表达式 - 每月1号凌晨执行
     * 用于月度报表、账单生成等
     */
    @Scheduled(cron = "0 0 0 1 * ?")  // 每月1号00:00:00
    public void taskFirstDayOfMonth() {
        String msg = "每月1号凌晨任务执行";
        log.info(msg);
        logService.addLog("FirstDayOfMonth", "每月1号凌晨（Cron: 0 0 0 1 * ?）", msg);
    }
}

/**
 * 业务定时任务示例
 */
@Slf4j
@Component
class BusinessScheduledTasks {

    private final TaskLogService logService;
    private final AtomicInteger healthCheckCounter = new AtomicInteger(0);

    public BusinessScheduledTasks(TaskLogService logService) {
        this.logService = logService;
    }

    /**
     * 8. 健康检查任务 - 每30秒执行
     */
    @Scheduled(fixedRate = 30000)
    public void healthCheck() {
        int count = healthCheckCounter.incrementAndGet();
        String msg = String.format("系统健康检查 #%d - 状态：正常", count);
        log.info(msg);
        logService.addLog("HealthCheck", "每30秒检查系统状态", msg);
    }

    /**
     * 9. 数据同步任务 - 每5分钟执行
     */
    @Scheduled(cron = "0 0/5 * * * ?")  // 每5分钟
    public void dataSyncTask() {
        String msg = "执行数据同步任务";
        log.info(msg);
        logService.addLog("DataSync", "每5分钟同步数据（Cron: 0 0/5 * * * ?）", msg);

        // 模拟数据同步
        try {
            Thread.sleep(3000);
            log.info("数据同步完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 10. 缓存清理任务 - 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")  // 每天凌晨2点
    public void cacheCleanupTask() {
        String msg = "执行缓存清理任务";
        log.info(msg);
        logService.addLog("CacheCleanup", "每天凌晨2点清理缓存（Cron: 0 0 2 * * ?）", msg);
    }

    /**
     * 11. 报表生成任务 - 每周一上午8点执行
     */
    @Scheduled(cron = "0 0 8 ? * MON")  // 每周一上午8点
    public void weeklyReportTask() {
        String msg = "生成周报表";
        log.info(msg);
        logService.addLog("WeeklyReport", "每周一上午8点生成报表（Cron: 0 0 8 ? * MON）", msg);
    }
}

/**
 * 动态定时任务示例
 */
@Slf4j
@Component
class DynamicScheduledTask {

    private final TaskScheduler taskScheduler;
    private final TaskLogService logService;
    private ScheduledFuture<?> future;
    private final AtomicInteger dynamicTaskCounter = new AtomicInteger(0);

    public DynamicScheduledTask(TaskScheduler taskScheduler, TaskLogService logService) {
        this.taskScheduler = taskScheduler;
        this.logService = logService;
    }

    /**
     * 启动动态任务
     */
    public void startDynamicTask() {
        if (future != null && !future.isCancelled()) {
            log.warn("动态任务已在运行中");
            return;
        }

        // 使用Cron表达式创建动态任务：每15秒执行
        future = taskScheduler.schedule(
            this::executeDynamicTask,
            new CronTrigger("0/15 * * * * ?")
        );

        log.info("动态任务已启动（每15秒执行）");
        logService.addLog("DynamicTask", "动态任务已启动", "每15秒执行");
    }

    /**
     * 停止动态任务
     */
    public void stopDynamicTask() {
        if (future != null && !future.isCancelled()) {
            future.cancel(false);
            log.info("动态任务已停止");
            logService.addLog("DynamicTask", "动态任务已停止", "任务取消");
        } else {
            log.warn("动态任务未运行");
        }
    }

    /**
     * 动态任务执行逻辑
     */
    private void executeDynamicTask() {
        int count = dynamicTaskCounter.incrementAndGet();
        String msg = String.format("动态任务执行 #%d", count);
        log.info(msg);
        logService.addLog("DynamicTask", "动态任务（每15秒）", msg);
    }

    /**
     * 检查任务状态
     */
    public boolean isRunning() {
        return future != null && !future.isCancelled();
    }
}

/**
 * REST控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/tasks")
class ScheduledTaskController {

    private final TaskLogService logService;
    private final DynamicScheduledTask dynamicTask;

    public ScheduledTaskController(TaskLogService logService, DynamicScheduledTask dynamicTask) {
        this.logService = logService;
        this.dynamicTask = dynamicTask;
    }

    /**
     * 获取所有任务执行日志
     */
    @GetMapping("/logs")
    public List<TaskLog> getLogs() {
        return logService.getAllLogs();
    }

    /**
     * 清空日志
     */
    @PostMapping("/logs/clear")
    public String clearLogs() {
        logService.clearLogs();
        return "日志已清空";
    }

    /**
     * 获取任务状态
     */
    @GetMapping("/status")
    public java.util.Map<String, Object> getStatus() {
        java.util.Map<String, Object> status = new java.util.HashMap<>();
        status.put("dynamicTaskRunning", dynamicTask.isRunning());
        status.put("totalLogs", logService.getAllLogs().size());
        status.put("currentTime", LocalDateTime.now());
        return status;
    }

    /**
     * 手动触发任务（用于测试）
     */
    @PostMapping("/trigger")
    public String triggerTask() {
        logService.addLog("Manual", "手动触发测试任务", "执行成功");
        return "任务已手动触发";
    }

    /**
     * 启动动态任务
     */
    @PostMapping("/dynamic/start")
    public String startDynamicTask() {
        dynamicTask.startDynamicTask();
        return "动态任务已启动";
    }

    /**
     * 停止动态任务
     */
    @PostMapping("/dynamic/stop")
    public String stopDynamicTask() {
        dynamicTask.stopDynamicTask();
        return "动态任务已停止";
    }

    /**
     * 帮助文档
     */
    @GetMapping("/help")
    public String help() {
        return """
            === Spring Boot 定时任务示例 ===

            1. 查看任务执行日志
               GET http://localhost:8080/api/tasks/logs

            2. 查看任务状态
               GET http://localhost:8080/api/tasks/status

            3. 手动触发任务
               POST http://localhost:8080/api/tasks/trigger

            4. 启动动态任务
               POST http://localhost:8080/api/tasks/dynamic/start

            5. 停止动态任务
               POST http://localhost:8080/api/tasks/dynamic/stop

            6. 清空日志
               POST http://localhost:8080/api/tasks/logs/clear

            核心知识点：
            - @EnableScheduling：启用定时任务
            - @Scheduled：定义定时任务
            - fixedRate：固定频率（从开始时间算）
            - fixedDelay：固定延迟（从结束时间算）
            - initialDelay：初始延迟
            - cron：Cron表达式
            - TaskScheduler：任务调度器

            Cron表达式格式：
            秒 分 时 日 月 周 [年]
            * : 所有值
            ? : 不指定值（日和周互斥）
            - : 范围（1-5）
            , : 列举（1,3,5）
            / : 步长（0/15表示从0开始每15秒）
            L : 最后（月的最后一天）
            W : 工作日
            # : 第几个（6#3表示第3个周六）

            常用Cron示例：
            - 0 0 2 * * ?        每天凌晨2点
            - 0 0/5 * * * ?      每5分钟
            - 0 0 9 * * MON-FRI  工作日上午9点
            - 0 0 0 1 * ?        每月1号凌晨
            - 0 0 8 ? * MON      每周一上午8点

            注意事项：
            1. 默认单线程执行，需配置线程池
            2. 长时间任务考虑使用fixedDelay
            3. Cron表达式要仔细测试
            4. 避免在定时任务中进行耗时操作
            5. 生产环境建议使用分布式任务调度（如XXL-JOB）
            """;
    }
}
