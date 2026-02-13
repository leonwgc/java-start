package advanced;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;
import java.util.*;

/**
 * 并发工具详解（java.util.concurrent）
 * 学习目标：
 * 1. 掌握线程池（ExecutorService）的使用
 * 2. 学习原子类（Atomic*）和Lock锁
 * 3. 理解CountDownLatch、CyclicBarrier等同步工具
 * 4. 了解并发集合和阻塞队列
 *
 * 并发工具包括：
 * - 线程池框架（Executors、ThreadPoolExecutor）
 * - 原子操作类（AtomicInteger、AtomicReference等）
 * - 显式锁（ReentrantLock、ReadWriteLock）
 * - 同步辅助类（CountDownLatch、Semaphore等）
 *
 * Spring应用：
 * - @Async异步执行底层
 * - 定时任务线程池
 * - 缓存并发访问
 * - 分布式锁的本地实现
 */
public class ConcurrencyDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 并发工具详解 ===\n");

        demonstrateThreadPool();
        demonstrateAtomicClasses();
        demonstrateReentrantLock();
        demonstrateReadWriteLock();
        demonstrateCountDownLatch();
        demonstrateCyclicBarrier();
        demonstrateSemaphore();
        demonstrateBlockingQueue();
        demonstrateForkJoinPool();
    }

    /**
     * 1. 线程池（ExecutorService）
     */
    private static void demonstrateThreadPool() throws Exception {
        System.out.println("1. 线程池（ExecutorService）\n");

        // 创建固定大小线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("提交5个任务到3个线程的线程池:");
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("  任务" + taskId + " 在线程 " +
                    Thread.currentThread().getName() + " 执行");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // ThreadPoolExecutor详细配置
        System.out.println("\n自定义线程池配置:");
        ThreadPoolExecutor customPool = new ThreadPoolExecutor(
            2,                      // 核心线程数
            4,                      // 最大线程数
            60L,                    // 空闲线程存活时间
            TimeUnit.SECONDS,       // 时间单位
            new LinkedBlockingQueue<>(10),  // 任务队列
            Executors.defaultThreadFactory(), // 线程工厂
            new ThreadPoolExecutor.AbortPolicy()  // 拒绝策略
        );

        System.out.println("  核心线程数: " + customPool.getCorePoolSize());
        System.out.println("  最大线程数: " + customPool.getMaximumPoolSize());
        System.out.println("  队列容量: 10");

        customPool.shutdown();
        System.out.println();
    }

    /**
     * 2. 原子类（Atomic*）
     */
    private static void demonstrateAtomicClasses() throws Exception {
        System.out.println("2. 原子类（线程安全的计数器）\n");

        // AtomicInteger - 原子整数
        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(10);

        // 10个线程同时对计数器加1
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet();  // 原子操作：加1
                }
                latch.countDown();
            });
        }

        latch.await();
        System.out.println("AtomicInteger计数结果: " + counter.get() + " (预期: 10000)");

        // AtomicLong - 原子长整数
        AtomicLong atomicLong = new AtomicLong(100);
        System.out.println("AtomicLong初始值: " + atomicLong.get());
        atomicLong.addAndGet(50);
        System.out.println("加50后: " + atomicLong.get());
        System.out.println("先获取再自增: " + atomicLong.getAndIncrement());
        System.out.println("当前值: " + atomicLong.get());

        // AtomicReference - 原子引用
        AtomicReference<String> atomicRef = new AtomicReference<>("初始值");
        System.out.println("\nAtomicReference: " + atomicRef.get());
        atomicRef.compareAndSet("初始值", "新值");  // CAS操作
        System.out.println("CAS更新后: " + atomicRef.get());

        executor.shutdown();
        System.out.println();
    }

    /**
     * 3. ReentrantLock（可重入锁）
     */
    private static void demonstrateReentrantLock() throws Exception {
        System.out.println("3. ReentrantLock（可重入锁）\n");

        ReentrantLock lock = new ReentrantLock();
        int[] sharedResource = {0};

        ExecutorService executor = Executors.newFixedThreadPool(3);
        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            final int threadId = i + 1;
            executor.submit(() -> {
                lock.lock();  // 获取锁
                try {
                    System.out.println("  线程" + threadId + " 获得锁");
                    sharedResource[0]++;
                    Thread.sleep(100);
                    System.out.println("  线程" + threadId + " 修改资源为: " + sharedResource[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();  // 释放锁
                    System.out.println("  线程" + threadId + " 释放锁");
                }
                latch.countDown();
            });
        }

        latch.await();
        System.out.println("最终值: " + sharedResource[0]);

        // tryLock - 尝试获取锁
        System.out.println("\ntryLock示例:");
        if (lock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                System.out.println("  成功获取锁");
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("  获取锁失败");
        }

        executor.shutdown();
        System.out.println();
    }

    /**
     * 4. ReadWriteLock（读写锁）
     */
    private static void demonstrateReadWriteLock() throws Exception {
        System.out.println("4. ReadWriteLock（读写锁）\n");

        ReadWriteLock rwLock = new ReentrantReadWriteLock();
        Map<String, String> cache = new HashMap<>();

        // 写操作
        Runnable writer = () -> {
            rwLock.writeLock().lock();
            try {
                String key = "key-" + System.currentTimeMillis();
                cache.put(key, "value");
                System.out.println("  [写] 添加: " + key);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.writeLock().unlock();
            }
        };

        // 读操作
        Runnable reader = () -> {
            rwLock.readLock().lock();
            try {
                System.out.println("  [读] 缓存大小: " + cache.size());
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rwLock.readLock().unlock();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 1个写线程，4个读线程
        executor.submit(writer);
        for (int i = 0; i < 4; i++) {
            executor.submit(reader);
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println();
    }

    /**
     * 5. CountDownLatch（倒计数门栓）
     */
    private static void demonstrateCountDownLatch() throws Exception {
        System.out.println("5. CountDownLatch（等待多个线程完成）\n");

        int taskCount = 3;
        CountDownLatch latch = new CountDownLatch(taskCount);

        ExecutorService executor = Executors.newFixedThreadPool(taskCount);

        System.out.println("主线程等待" + taskCount + "个任务完成...");

        for (int i = 1; i <= taskCount; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    Thread.sleep(taskId * 500);
                    System.out.println("  任务" + taskId + " 完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();  // 计数减1
                }
            });
        }

        latch.await();  // 等待计数变为0
        System.out.println("所有任务完成，主线程继续执行");

        executor.shutdown();
        System.out.println();
    }

    /**
     * 6. CyclicBarrier（循环栅栏）
     */
    private static void demonstrateCyclicBarrier() throws Exception {
        System.out.println("6. CyclicBarrier（等待所有线程到达屏障）\n");

        int parties = 3;
        CyclicBarrier barrier = new CyclicBarrier(parties, () -> {
            System.out.println("  >>> 所有线程到达屏障，开始下一阶段\n");
        });

        ExecutorService executor = Executors.newFixedThreadPool(parties);

        for (int i = 1; i <= parties; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    System.out.println("线程" + threadId + " 准备阶段1...");
                    Thread.sleep(threadId * 300);
                    System.out.println("线程" + threadId + " 完成阶段1，等待其他线程...");
                    barrier.await();  // 等待所有线程

                    System.out.println("线程" + threadId + " 准备阶段2...");
                    Thread.sleep(threadId * 200);
                    System.out.println("线程" + threadId + " 完成阶段2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    /**
     * 7. Semaphore（信号量）
     */
    private static void demonstrateSemaphore() throws Exception {
        System.out.println("7. Semaphore（限制并发访问数量）\n");

        // 只允许2个线程同时访问
        Semaphore semaphore = new Semaphore(2);

        ExecutorService executor = Executors.newFixedThreadPool(5);

        System.out.println("模拟数据库连接池（最多2个并发）:");

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    System.out.println("  任务" + taskId + " 尝试获取许可...");
                    semaphore.acquire();  // 获取许可
                    System.out.println("  任务" + taskId + " 获得许可，开始执行");
                    Thread.sleep(1000);
                    System.out.println("  任务" + taskId + " 执行完成，释放许可");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();  // 释放许可
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println();
    }

    /**
     * 8. BlockingQueue（阻塞队列）
     */
    private static void demonstrateBlockingQueue() throws Exception {
        System.out.println("8. BlockingQueue（生产者-消费者模式）\n");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

        // 生产者
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String item = "产品" + i;
                    queue.put(item);  // 队列满时会阻塞
                    System.out.println("  [生产] " + item);
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 消费者
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    String item = queue.take();  // 队列空时会阻塞
                    System.out.println("  [消费] " + item);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("\n其他阻塞队列类型:");
        System.out.println("  - ArrayBlockingQueue: 有界阻塞队列");
        System.out.println("  - LinkedBlockingQueue: 可选有界阻塞队列");
        System.out.println("  - PriorityBlockingQueue: 优先级阻塞队列");
        System.out.println("  - DelayQueue: 延迟队列");
        System.out.println("  - SynchronousQueue: 同步队列");
        System.out.println();
    }

    /**
     * 9. ForkJoinPool（分治任务）
     */
    private static void demonstrateForkJoinPool() throws Exception {
        System.out.println("9. ForkJoinPool（分治并行计算）\n");

        // 计算1到10000的和
        ForkJoinPool pool = ForkJoinPool.commonPool();
        SumTask task = new SumTask(1, 10000);

        long result = pool.invoke(task);
        System.out.println("1到10000的和: " + result);
        System.out.println("验证: " + (10000 * 10001 / 2));

        System.out.println("\nForkJoinPool特点:");
        System.out.println("  - 工作窃取算法（Work-Stealing）");
        System.out.println("  - 适合递归分治任务");
        System.out.println("  - Stream并行流底层使用");
        System.out.println();
    }

    // ==================== 辅助类 ====================

    /**
     * ForkJoin任务：计算区间和
     */
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 1000;  // 阈值
        private int start;
        private int end;

        public SumTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            // 任务足够小，直接计算
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
                return sum;
            }

            // 任务太大，拆分成两个子任务
            int mid = (start + end) / 2;
            SumTask leftTask = new SumTask(start, mid);
            SumTask rightTask = new SumTask(mid + 1, end);

            // 异步执行子任务
            leftTask.fork();
            rightTask.fork();

            // 等待子任务完成并合并结果
            return leftTask.join() + rightTask.join();
        }
    }
}
