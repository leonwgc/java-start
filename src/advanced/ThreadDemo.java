package advanced;

/**
 * Java多线程学习
 * 学习目标：
 * 1. 理解线程的概念
 * 2. 掌握创建线程的两种方式
 * 3. 学习线程同步
 * 4. 理解线程安全问题
 */
public class ThreadDemo {
    public static void main(String[] args) {
        System.out.println("=== 多线程学习 ===\n");

        // 示例1：创建和启动线程
        demonstrateThreadCreation();

        // 等待一下
        sleep(3000);

        // 示例2：线程状态和生命周期
        System.out.println("\n" + "=".repeat(50));
        demonstrateThreadLifecycle();

        sleep(2000);

        // 示例3：线程同步问题
        System.out.println("\n" + "=".repeat(50));
        demonstrateThreadSafety();

        sleep(3000);

        // 示例4：使用synchronized解决问题
        System.out.println("\n" + "=".repeat(50));
        demonstrateSynchronized();
    }

    // 创建线程的方式
    public static void demonstrateThreadCreation() {
        System.out.println("1. 创建线程的两种方式\n");

        // 方式1：继承Thread类
        MyThread thread1 = new MyThread("线程A");
        thread1.start();  // 启动线程

        // 方式2：实现Runnable接口（推荐）
        Thread thread2 = new Thread(new MyRunnable("线程B"));
        thread2.start();

        // 方式3：使用Lambda表达式（Java 8+，最简洁）
        Thread thread3 = new Thread(() -> {
            for (int i = 1; i <= 3; i++) {
                System.out.println("线程C - 执行第" + i + "次");
                sleep(500);
            }
        });
        thread3.start();

        System.out.println("主线程继续执行...\n");
    }

    // 线程生命周期
    public static void demonstrateThreadLifecycle() {
        System.out.println("2. 线程状态和生命周期\n");

        Thread thread = new Thread(() -> {
            System.out.println("子线程：开始执行");

            // 执行任务
            for (int i = 1; i <= 3; i++) {
                System.out.println("子线程：正在工作 " + i);
                sleep(500);
            }

            System.out.println("子线程：执行完毕");
        }, "WorkerThread");

        // NEW状态
        System.out.println("创建后状态: " + thread.getState());

        // RUNNABLE状态
        thread.start();
        System.out.println("启动后状态: " + thread.getState());

        // 等待线程结束
        try {
            thread.join();  // 等待thread执行完成
            System.out.println("结束后状态: " + thread.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 线程安全问题演示
    public static void demonstrateThreadSafety() {
        System.out.println("3. 线程安全问题（不加锁）\n");

        UnsafeCounter counter = new UnsafeCounter();

        // 创建多个线程同时操作同一个计数器
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();

        // 等待两个线程执行完
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 理论上应该是2000，但实际可能小于2000
        System.out.println("不安全的计数器最终值: " + counter.getCount());
        System.out.println("预期值: 2000");
        System.out.println("（如果不等于2000，说明出现了线程安全问题）");
    }

    // 使用synchronized解决线程安全
    public static void demonstrateSynchronized() {
        System.out.println("4. 使用synchronized解决线程安全\n");

        SafeCounter counter = new SafeCounter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("安全的计数器最终值: " + counter.getCount());
        System.out.println("预期值: 2000");
        System.out.println("✅ 使用synchronized后，结果正确！");
    }

    // 工具方法：休眠
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// ========== 线程类定义 ==========

// 方式1：继承Thread类
class MyThread extends Thread {
    private String threadName;

    public MyThread(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(threadName + " - 执行第" + i + "次");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// 方式2：实现Runnable接口（推荐）
class MyRunnable implements Runnable {
    private String threadName;

    public MyRunnable(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 3; i++) {
            System.out.println(threadName + " - 执行第" + i + "次");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// 不安全的计数器
class UnsafeCounter {
    private int count = 0;

    public void increment() {
        // 这个操作不是原子性的，可能出现线程安全问题
        count++;
    }

    public int getCount() {
        return count;
    }
}

// 安全的计数器（使用synchronized）
class SafeCounter {
    private int count = 0;

    // synchronized关键字：同一时间只允许一个线程执行
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
