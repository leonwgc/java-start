package advanced;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * 重试机制详解
 * 学习目标：
 * 1. 理解重试机制的作用和应用场景
 * 2. 掌握固定延迟、指数退避等重试策略
 * 3. 学习重试次数限制和超时控制
 * 4. 了解Spring Retry的基本原理
 *
 * 重试机制是什么？
 * - 当操作失败时自动重新尝试
 * - 提高系统的容错性和可用性
 * - 常用于网络请求、远程调用、数据库操作等
 * - 需要合理设置重试次数和延迟策略
 *
 * Spring应用：
 * - @Retryable注解
 * - Spring Retry框架
 * - 微服务间调用重试
 * - 消息队列消费失败重试
 */
public class RetryDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 重试机制详解 ===\n");

        demonstrateBasicRetry();
        demonstrateFixedDelayRetry();
        demonstrateExponentialBackoff();
        demonstrateRetryWithPredicate();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. 基础重试机制
     */
    private static void demonstrateBasicRetry() {
        System.out.println("1. 基础重试机制\n");

        int maxAttempts = 3;
        int attemptCount = 0;
        boolean success = false;

        System.out.println("最大重试次数: " + maxAttempts);
        System.out.println("开始执行...\n");

        while (attemptCount < maxAttempts && !success) {
            attemptCount++;
            try {
                System.out.println("  尝试 #" + attemptCount);

                // 模拟可能失败的操作
                if (attemptCount < 3) {
                    throw new RuntimeException("操作失败");
                }

                System.out.println("  ✅ 操作成功！");
                success = true;

            } catch (Exception e) {
                System.out.println("  ❌ 失败: " + e.getMessage());

                if (attemptCount >= maxAttempts) {
                    System.out.println("  ⚠️  已达到最大重试次数，放弃操作");
                }
            }
        }
        System.out.println();
    }

    /**
     * 2. 固定延迟重试
     */
    private static void demonstrateFixedDelayRetry() throws Exception {
        System.out.println("2. 固定延迟重试\n");

        RetryExecutor<String> retryExecutor = new RetryExecutor<>(
            3,                    // 最大重试次数
            1000,                 // 固定延迟1秒
            RetryStrategy.FIXED   // 固定延迟策略
        );

        System.out.println("策略: 固定延迟1秒");
        System.out.println("最大重试次数: 3\n");

        int[] attemptCounter = {0};

        try {
            String result = retryExecutor.execute(() -> {
                attemptCounter[0]++;
                System.out.println("  [" + new Date() + "] 执行尝试 #" + attemptCounter[0]);

                if (attemptCounter[0] < 2) {
                    throw new RuntimeException("模拟失败");
                }

                return "成功结果";
            });

            System.out.println("\n✅ 最终结果: " + result);
        } catch (Exception e) {
            System.out.println("\n❌ 所有尝试都失败了");
        }

        System.out.println();
    }

    /**
     * 3. 指数退避重试
     */
    private static void demonstrateExponentialBackoff() throws Exception {
        System.out.println("3. 指数退避重试\n");

        RetryExecutor<String> retryExecutor = new RetryExecutor<>(
            4,                           // 最大重试次数
            500,                         // 初始延迟500ms
            RetryStrategy.EXPONENTIAL    // 指数退避策略
        );

        System.out.println("策略: 指数退避（延迟翻倍）");
        System.out.println("初始延迟: 500ms");
        System.out.println("延迟序列: 500ms, 1000ms, 2000ms, 4000ms\n");

        int[] attemptCounter = {0};
        long startTime = System.currentTimeMillis();

        try {
            String result = retryExecutor.execute(() -> {
                attemptCounter[0]++;
                long elapsed = System.currentTimeMillis() - startTime;
                System.out.println("  [" + elapsed + "ms] 执行尝试 #" + attemptCounter[0]);

                if (attemptCounter[0] < 3) {
                    throw new RuntimeException("模拟失败");
                }

                return "成功结果";
            });

            System.out.println("\n✅ 最终结果: " + result);
        } catch (Exception e) {
            System.out.println("\n❌ 所有尝试都失败了");
        }

        System.out.println("\n指数退避优势: 避免对失败服务造成过大压力\n");
    }

    /**
     * 4. 条件重试（根据异常类型）
     */
    private static void demonstrateRetryWithPredicate() throws Exception {
        System.out.println("4. 条件重试（只重试特定异常）\n");

        System.out.println("策略: 只重试 RetryableException");
        System.out.println("不重试: NonRetryableException\n");

        // 场景1: 可重试的异常
        System.out.println("场景1: 抛出可重试异常");
        try {
            retryOnlyRetryableException(() -> {
                System.out.println("  尝试操作...");
                throw new RetryableException("临时错误（可重试）");
            });
        } catch (Exception e) {
            System.out.println("  ❌ 最终失败: " + e.getMessage());
        }

        // 场景2: 不可重试的异常
        System.out.println("\n场景2: 抛出不可重试异常");
        try {
            retryOnlyRetryableException(() -> {
                System.out.println("  尝试操作...");
                throw new NonRetryableException("致命错误（不可重试）");
            });
        } catch (Exception e) {
            System.out.println("  ❌ 立即失败: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * 5. 实际应用场景
     */
    private static void demonstrateRealWorldExamples() throws Exception {
        System.out.println("5. 实际应用场景\n");

        // 场景1: HTTP请求重试
        System.out.println("场景1: HTTP请求重试");
        HttpClientWithRetry httpClient = new HttpClientWithRetry();
        try {
            String response = httpClient.get("https://api.example.com/data");
            System.out.println("  响应: " + response);
        } catch (Exception e) {
            System.out.println("  请求失败: " + e.getMessage());
        }

        // 场景2: 数据库操作重试
        System.out.println("\n场景2: 数据库操作重试（死锁重试）");
        DatabaseService dbService = new DatabaseService();
        try {
            boolean result = dbService.updateWithRetry("UPDATE users SET ...");
            System.out.println("  更新" + (result ? "成功" : "失败"));
        } catch (Exception e) {
            System.out.println("  更新失败: " + e.getMessage());
        }

        // 场景3: 消息发送重试
        System.out.println("\n场景3: 消息发送重试");
        MessageSender sender = new MessageSender();
        try {
            sender.sendWithRetry("user-123", "Hello, World!");
        } catch (Exception e) {
            System.out.println("  发送失败: " + e.getMessage());
        }

        System.out.println();
    }

    // ==================== 重试执行器 ====================

    enum RetryStrategy {
        FIXED,          // 固定延迟
        EXPONENTIAL     // 指数退避
    }

    static class RetryExecutor<T> {
        private final int maxAttempts;
        private final long initialDelay;
        private final RetryStrategy strategy;

        public RetryExecutor(int maxAttempts, long initialDelay, RetryStrategy strategy) {
            this.maxAttempts = maxAttempts;
            this.initialDelay = initialDelay;
            this.strategy = strategy;
        }

        public T execute(Supplier<T> operation) throws Exception {
            Exception lastException = null;

            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    return operation.get();
                } catch (Exception e) {
                    lastException = e;
                    System.out.println("    ❌ 失败: " + e.getMessage());

                    if (attempt < maxAttempts) {
                        long delay = calculateDelay(attempt);
                        System.out.println("    ⏱️  等待 " + delay + "ms 后重试...");
                        Thread.sleep(delay);
                    }
                }
            }

            throw lastException;
        }

        private long calculateDelay(int attempt) {
            switch (strategy) {
                case FIXED:
                    return initialDelay;
                case EXPONENTIAL:
                    return initialDelay * (long) Math.pow(2, attempt - 1);
                default:
                    return initialDelay;
            }
        }
    }

    // ==================== 自定义异常 ====================

    static class RetryableException extends RuntimeException {
        public RetryableException(String message) {
            super(message);
        }
    }

    static class NonRetryableException extends RuntimeException {
        public NonRetryableException(String message) {
            super(message);
        }
    }

    // ==================== 条件重试 ====================

    private static void retryOnlyRetryableException(Runnable operation) throws Exception {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                operation.run();
                return; // 成功
            } catch (RetryableException e) {
                System.out.println("  ❌ 尝试 #" + attempt + " 失败（可重试）: " + e.getMessage());
                if (attempt < maxAttempts) {
                    System.out.println("  🔄 准备重试...");
                    Thread.sleep(500);
                }
            } catch (NonRetryableException e) {
                System.out.println("  ❌ 不可重试的异常，立即终止");
                throw e;
            }
        }

        throw new Exception("达到最大重试次数");
    }

    // ==================== 实际应用示例 ====================

    /**
     * HTTP客户端（带重试）
     */
    static class HttpClientWithRetry {
        private int requestCount = 0;

        public String get(String url) throws Exception {
            RetryExecutor<String> retryExecutor = new RetryExecutor<>(
                3, 1000, RetryStrategy.EXPONENTIAL
            );

            return retryExecutor.execute(() -> {
                requestCount++;
                System.out.println("  [请求#" + requestCount + "] GET " + url);

                // 模拟前2次失败
                if (requestCount < 3) {
                    throw new RuntimeException("网络超时");
                }

                return "{\"status\": \"success\"}";
            });
        }
    }

    /**
     * 数据库服务（死锁重试）
     */
    static class DatabaseService {
        private int updateCount = 0;

        public boolean updateWithRetry(String sql) throws Exception {
            RetryExecutor<Boolean> retryExecutor = new RetryExecutor<>(
                3, 500, RetryStrategy.FIXED
            );

            return retryExecutor.execute(() -> {
                updateCount++;
                System.out.println("  [更新#" + updateCount + "] " + sql);

                // 模拟死锁
                if (updateCount < 2) {
                    throw new RuntimeException("Deadlock detected");
                }

                System.out.println("  ✅ 更新成功");
                return true;
            });
        }
    }

    /**
     * 消息发送器（带重试）
     */
    static class MessageSender {
        private int sendCount = 0;

        public void sendWithRetry(String userId, String message) throws Exception {
            RetryExecutor<Void> retryExecutor = new RetryExecutor<>(
                3, 1000, RetryStrategy.EXPONENTIAL
            );

            retryExecutor.execute(() -> {
                sendCount++;
                System.out.println("  [发送#" + sendCount + "] to=" + userId + ", msg=" + message);

                if (sendCount < 2) {
                    throw new RuntimeException("消息队列连接失败");
                }

                System.out.println("  ✅ 发送成功");
                return null;
            });
        }
    }
}
