package advanced;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * CompletableFuture异步编程学习
 * 学习目标：
 * 1. 理解异步编程的概念
 * 2. 掌握CompletableFuture的使用
 * 3. 学习异步任务的组合
 * 4. 理解Spring异步处理的基础
 */
public class AsyncDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("=== CompletableFuture异步编程学习 ===\n");

        // 示例1：基本异步任务
        demonstrateBasicAsync();

        // 示例2：异步任务链
        demonstrateAsyncChain();

        // 示例3：组合多个异步任务
        demonstrateCombineAsync();

        // 示例4：异常处理
        demonstrateExceptionHandling();

        // 示例5：实战案例
        demonstratePracticalExample();

        System.out.println("\n💡 Spring异步处理：");
        System.out.println("- @Async注解实现异步方法");
        System.out.println("- @EnableAsync启用异步支持");
        System.out.println("- CompletableFuture作为返回值");
        System.out.println("- 配置ThreadPoolTaskExecutor线程池");
        System.out.println("- 异步提升系统响应速度\n");
    }

    // 基本异步任务
    public static void demonstrateBasicAsync() throws Exception {
        System.out.println("1. 基本异步任务\n");
        System.out.println("作用：在后台线程执行任务，不阻塞主线程");
        System.out.println("Spring应用：@Async异步方法调用\n");

        // supplyAsync - 有返回值的异步任务
        System.out.println("主线程: 开始执行");
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  异步任务1: 开始处理（线程: " + Thread.currentThread().getName() + "）");
            sleep(1000);
            return "任务1完成";
        });

        System.out.println("主线程: 继续做其他事情");

        // 获取异步结果
        String result1 = future1.get();
        System.out.println("主线程: 收到结果 - " + result1);

        // runAsync - 无返回值的异步任务
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println("  异步任务2: 发送邮件通知");
            sleep(500);
            System.out.println("  异步任务2: 邮件发送完成");
        });

        future2.get(); // 等待完成
        System.out.println();
    }

    // 异步任务链
    public static void demonstrateAsyncChain() throws Exception {
        System.out.println("2. 异步任务链\n");
        System.out.println("作用：多个异步任务按顺序执行");
        System.out.println("Spring应用：多步骤异步处理流程\n");

        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            System.out.println("  步骤1: 查询用户信息");
            sleep(500);
            return "User:张三";
        })
        .thenApply(user -> {
            System.out.println("  步骤2: 查询订单信息 - " + user);
            sleep(500);
            return user + ", Orders:5";
        })
        .thenApply(data -> {
            System.out.println("  步骤3: 计算总金额 - " + data);
            sleep(500);
            return data + ", Total:2999元";
        })
        .thenApply(finalData -> {
            System.out.println("  步骤4: 生成报告");
            return "报告: " + finalData;
        });

        System.out.println("最终结果: " + result.get());
        System.out.println();
    }

    // 组合多个异步任务
    public static void demonstrateCombineAsync() throws Exception {
        System.out.println("3. 组合多个异步任务\n");
        System.out.println("作用：并行执行多个任务，等待所有完成");
        System.out.println("Spring应用：并行调用多个服务\n");

        // 模拟调用多个服务
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("  查询用户服务（耗时1秒）");
            sleep(1000);
            return "用户: 张三";
        });

        CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("  查询订单服务（耗时1.5秒）");
            sleep(1500);
            return "订单: 5个";
        });

        CompletableFuture<String> paymentFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("  查询支付服务（耗时800毫秒）");
            sleep(800);
            return "支付: 已验证";
        });

        long startTime = System.currentTimeMillis();

        // 等待所有任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
            userFuture, orderFuture, paymentFuture
        );

        allFutures.get(); // 阻塞直到所有完成

        long endTime = System.currentTimeMillis();

        // 获取所有结果
        System.out.println("\n所有服务调用完成:");
        System.out.println("  " + userFuture.get());
        System.out.println("  " + orderFuture.get());
        System.out.println("  " + paymentFuture.get());
        System.out.println("  总耗时: " + (endTime - startTime) + "ms");
        System.out.println("  （如果串行执行需要3.3秒，并行只需1.5秒！）");
        System.out.println();
    }

    // 异常处理
    public static void demonstrateExceptionHandling() throws Exception {
        System.out.println("4. 异常处理\n");
        System.out.println("作用：优雅处理异步任务中的异常\n");

        // exceptionally - 捕获异常并返回默认值
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("  任务: 尝试连接数据库");
            if (Math.random() > 0.5) {
                throw new RuntimeException("连接失败");
            }
            return "连接成功";
        }).exceptionally(ex -> {
            System.out.println("  异常处理: " + ex.getMessage());
            return "使用缓存数据";
        });

        System.out.println("结果: " + future1.get());

        // handle - 同时处理成功和失败
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("\n  任务: 处理支付");
            throw new RuntimeException("余额不足");
        }).handle((result, ex) -> {
            if (ex != null) {
                System.out.println("  错误处理: " + ex.getMessage());
                return "支付失败，请充值";
            }
            return "支付成功: " + result;
        });

        System.out.println("结果: " + future2.get());
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() throws Exception {
        System.out.println("5. 实战案例 - 电商订单处理\n");
        System.out.println("场景：用户下单后的异步处理流程\n");

        OrderService orderService = new OrderService();

        String orderId = "ORDER-" + System.currentTimeMillis();
        System.out.println("用户下单: " + orderId);

        // 异步处理订单
        CompletableFuture<OrderResult> orderResult = orderService.processOrderAsync(orderId);

        System.out.println("主线程: 立即返回订单创建成功，后台异步处理");
        System.out.println("主线程: 用户可以继续浏览其他商品\n");

        // 等待异步处理完成
        OrderResult result = orderResult.get();
        System.out.println("\n订单处理结果:");
        System.out.println("  " + result);

        System.out.println("\n💡 对比Spring Boot异步处理:");
        System.out.println("");
        System.out.println("@Service");
        System.out.println("public class OrderService {");
        System.out.println("    @Async  // Spring自动异步执行");
        System.out.println("    public CompletableFuture<OrderResult> processOrder(String orderId) {");
        System.out.println("        // 1. 扣减库存");
        System.out.println("        // 2. 创建支付单");
        System.out.println("        // 3. 发送通知");
        System.out.println("        return CompletableFuture.completedFuture(result);");
        System.out.println("    }");
        System.out.println("}");
        System.out.println("\n只需@Async注解，Spring自动处理异步！");
        System.out.println();
    }

    // ========== 辅助类 ==========

    // 订单服务
    static class OrderService {
        public CompletableFuture<OrderResult> processOrderAsync(String orderId) {
            return CompletableFuture.supplyAsync(() -> {
                System.out.println("  异步步骤1: 验证库存");
                sleep(500);

                return "库存充足";
            })
            .thenApply(stock -> {
                System.out.println("  异步步骤2: 扣减库存 - " + stock);
                sleep(500);
                return "库存已扣减";
            })
            .thenApply(inventory -> {
                System.out.println("  异步步骤3: 创建支付单");
                sleep(500);
                return "支付单已创建";
            })
            .thenApply(payment -> {
                System.out.println("  异步步骤4: 发送短信通知");
                sleep(300);
                return "通知已发送";
            })
            .thenApply(notification -> {
                System.out.println("  异步步骤5: 更新订单状态");
                sleep(200);
                return new OrderResult(orderId, "SUCCESS", "订单处理完成");
            })
            .exceptionally(ex -> {
                System.out.println("  错误: " + ex.getMessage());
                return new OrderResult(orderId, "FAILED", "订单处理失败");
            });
        }
    }

    // 订单结果
    static class OrderResult {
        private String orderId;
        private String status;
        private String message;

        public OrderResult(String orderId, String status, String message) {
            this.orderId = orderId;
            this.status = status;
            this.message = message;
        }

        @Override
        public String toString() {
            return "订单ID: " + orderId + ", 状态: " + status + ", 消息: " + message;
        }
    }

    // 辅助方法
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
