package advanced;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * 数据库连接池详解
 * 学习目标：
 * 1. 理解数据库连接池的作用和原理
 * 2. 掌握自定义连接池实现
 * 3. 学习连接池的核心参数配置
 * 4. 了解连接池的最佳实践
 *
 * 连接池是什么？
 * - 预先创建并管理数据库连接的容器
 * - 避免频繁创建和销毁连接的开销
 * - 提高并发性能和资源利用率
 * - 提供连接复用、超时控制、连接验证等功能
 *
 * Spring应用：
 * - Spring Boot默认使用HikariCP
 * - @Configuration配置数据源
 * - Spring Data JPA连接管理
 * - 微服务数据库访问优化
 */
public class ConnectionPoolDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 数据库连接池详解 ===\n");

        demonstrateWithoutPool();
        demonstrateSimpleConnectionPool();
        demonstrateConnectionPoolBehavior();
        demonstrateBestPractices();
    }

    /**
     * 1. 不使用连接池的问题
     */
    private static void demonstrateWithoutPool() throws Exception {
        System.out.println("1. 不使用连接池的问题\n");

        String url = "jdbc:h2:mem:testdb";
        String user = "sa";
        String password = "";

        System.out.println("模拟频繁创建和关闭连接:");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 5; i++) {
            // 每次都创建新连接
            Connection conn = DriverManager.getConnection(url, user, password);
            // 执行简单查询
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            rs.next();
            System.out.println("  执行查询 #" + (i + 1) + ": " + rs.getInt(1));
            // 关闭连接
            rs.close();
            stmt.close();
            conn.close();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\n总耗时: " + (endTime - startTime) + "ms");
        System.out.println("问题: 频繁创建和销毁连接，性能低下\n");
    }

    /**
     * 2. 简单连接池实现
     */
    private static void demonstrateSimpleConnectionPool() throws Exception {
        System.out.println("2. 简单连接池实现\n");

        SimpleConnectionPool pool = new SimpleConnectionPool(
            "jdbc:h2:mem:testdb", "sa", "", 5);

        System.out.println("连接池配置:");
        System.out.println("  - 最大连接数: 5");
        System.out.println("  - 初始连接数: 3\n");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 5; i++) {
            // 从连接池获取连接
            Connection conn = pool.getConnection();
            System.out.println("  获取连接 #" + (i + 1) + " - 当前可用: " + 
                pool.getAvailableCount());

            // 执行查询
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + (i + 1));
            rs.next();
            System.out.println("  执行查询 #" + (i + 1) + ": " + rs.getInt(1));
            rs.close();
            stmt.close();

            // 归还连接
            pool.releaseConnection(conn);
            System.out.println("  归还连接 #" + (i + 1) + " - 当前可用: " + 
                pool.getAvailableCount());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\n使用连接池总耗时: " + (endTime - startTime) + "ms");
        System.out.println("优势: 连接复用，性能提升明显\n");

        pool.shutdown();
    }

    /**
     * 3. 连接池行为演示
     */
    private static void demonstrateConnectionPoolBehavior() throws Exception {
        System.out.println("3. 连接池行为演示\n");

        SimpleConnectionPool pool = new SimpleConnectionPool(
            "jdbc:h2:mem:testdb", "sa", "", 3);

        System.out.println("场景1: 并发获取连接");
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    System.out.println("  [任务" + taskId + "] 尝试获取连接...");
                    Connection conn = pool.getConnection();
                    System.out.println("  [任务" + taskId + "] 获得连接，执行查询");
                    
                    Thread.sleep(500); // 模拟查询耗时
                    
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT " + taskId);
                    rs.next();
                    System.out.println("  [任务" + taskId + "] 查询结果: " + rs.getInt(1));
                    rs.close();
                    stmt.close();
                    
                    pool.releaseConnection(conn);
                    System.out.println("  [任务" + taskId + "] 归还连接");
                } catch (Exception e) {
                    System.out.println("  [任务" + taskId + "] 错误: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        System.out.println("\n场景2: 连接池满时的等待");
        System.out.println("  连接池最大连接数: 3");
        System.out.println("  当所有连接被占用时，新请求需要等待");
        System.out.println("  （已在上面的并发场景中演示）\n");

        pool.shutdown();
    }

    /**
     * 4. 最佳实践
     */
    private static void demonstrateBestPractices() {
        System.out.println("4. 连接池最佳实践\n");

        System.out.println("✅ 核心参数配置：");
        System.out.println("  1. maximumPoolSize（最大连接数）");
        System.out.println("     - 根据并发量和数据库负载设置");
        System.out.println("     - 建议: CPU核心数 * 2 + 磁盘数量");
        System.out.println("     - HikariCP默认: 10");

        System.out.println("\n  2. minimumIdle（最小空闲连接）");
        System.out.println("     - 保持一定数量的空闲连接");
        System.out.println("     - 避免突发流量时创建连接延迟");
        System.out.println("     - HikariCP默认: 与maximumPoolSize相同");

        System.out.println("\n  3. connectionTimeout（连接超时）");
        System.out.println("     - 获取连接的最大等待时间");
        System.out.println("     - 默认: 30秒");
        System.out.println("     - 建议: 根据业务响应时间设置");

        System.out.println("\n  4. idleTimeout（空闲超时）");
        System.out.println("     - 空闲连接的最大存活时间");
        System.out.println("     - 默认: 10分钟");
        System.out.println("     - 避免长时间占用数据库连接");

        System.out.println("\n  5. maxLifetime（最大生命周期）");
        System.out.println("     - 连接的最大存活时间");
        System.out.println("     - 默认: 30分钟");
        System.out.println("     - 防止数据库端主动关闭连接");

        System.out.println("\n✅ 使用建议：");
        System.out.println("  1. 始终在finally块中归还连接");
        System.out.println("  2. 使用try-with-resources自动管理连接");
        System.out.println("  3. 避免长时间持有连接");
        System.out.println("  4. 定期验证连接有效性");
        System.out.println("  5. 监控连接池指标（活跃连接数、等待时间等）");

        System.out.println("\n✅ Spring Boot配置示例：");
        System.out.println("  spring.datasource.hikari.maximum-pool-size=20");
        System.out.println("  spring.datasource.hikari.minimum-idle=5");
        System.out.println("  spring.datasource.hikari.connection-timeout=30000");
        System.out.println("  spring.datasource.hikari.idle-timeout=600000");
        System.out.println("  spring.datasource.hikari.max-lifetime=1800000");

        System.out.println("\n✅ 常见连接池选择：");
        System.out.println("  1. HikariCP - Spring Boot默认，性能最优");
        System.out.println("  2. Druid - 阿里开源，功能丰富，监控强大");
        System.out.println("  3. C3P0 - 老牌连接池，稳定性高");
        System.out.println("  4. DBCP2 - Apache项目，功能完善");
        System.out.println();
    }

    // ==================== 简单连接池实现 ====================

    /**
     * 简单连接池实现（教学用途）
     */
    static class SimpleConnectionPool {
        private final String url;
        private final String user;
        private final String password;
        private final int maxSize;
        
        private final Queue<Connection> availableConnections;
        private final Set<Connection> usedConnections;
        private int connectionCount;

        public SimpleConnectionPool(String url, String user, String password, int maxSize) 
                throws SQLException {
            this.url = url;
            this.user = user;
            this.password = password;
            this.maxSize = maxSize;
            this.availableConnections = new LinkedList<>();
            this.usedConnections = new HashSet<>();
            this.connectionCount = 0;

            // 初始化一些连接
            for (int i = 0; i < Math.min(3, maxSize); i++) {
                availableConnections.add(createConnection());
            }
        }

        /**
         * 获取连接
         */
        public synchronized Connection getConnection() throws SQLException {
            // 如果有可用连接，直接返回
            if (!availableConnections.isEmpty()) {
                Connection conn = availableConnections.poll();
                usedConnections.add(conn);
                return conn;
            }

            // 如果未达到最大连接数，创建新连接
            if (connectionCount < maxSize) {
                Connection conn = createConnection();
                usedConnections.add(conn);
                return conn;
            }

            // 等待其他线程归还连接
            try {
                wait(5000); // 最多等待5秒
                if (!availableConnections.isEmpty()) {
                    Connection conn = availableConnections.poll();
                    usedConnections.add(conn);
                    return conn;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            throw new SQLException("无法获取连接：连接池已满且等待超时");
        }

        /**
         * 归还连接
         */
        public synchronized void releaseConnection(Connection conn) {
            if (usedConnections.remove(conn)) {
                availableConnections.offer(conn);
                notify(); // 通知等待的线程
            }
        }

        /**
         * 获取可用连接数
         */
        public synchronized int getAvailableCount() {
            return availableConnections.size();
        }

        /**
         * 关闭连接池
         */
        public synchronized void shutdown() throws SQLException {
            // 关闭所有连接
            for (Connection conn : availableConnections) {
                conn.close();
            }
            for (Connection conn : usedConnections) {
                conn.close();
            }
            availableConnections.clear();
            usedConnections.clear();
            connectionCount = 0;
        }

        /**
         * 创建新连接
         */
        private Connection createConnection() throws SQLException {
            connectionCount++;
            return DriverManager.getConnection(url, user, password);
        }
    }
}
