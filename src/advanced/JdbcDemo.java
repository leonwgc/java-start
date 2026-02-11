package advanced;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC数据库操作学习
 * 学习目标：
 * 1. 理解JDBC的基本概念
 * 2. 掌握数据库连接和操作
 * 3. 学习CRUD操作
 * 4. 理解JDBC在Spring Data中的基础作用
 */
public class JdbcDemo {
    // H2内存数据库URL (DB_CLOSE_DELAY=-1 保持数据库在内存中直到JVM关闭)
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        System.out.println("=== JDBC数据库操作学习 ===\n");

        try {
            // 示例1：数据库连接
            demonstrateDatabaseConnection();

            // 示例2：创建表
            demonstrateCreateTable();

            // 示例3：插入数据
            demonstrateInsert();

            // 示例4：查询数据
            demonstrateQuery();

            // 示例5：更新和删除
            demonstrateUpdateAndDelete();

            // 示例6：事务处理
            demonstrateTransaction();

            // 示例7：PreparedStatement
            demonstratePreparedStatement();

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n💡 Spring中的应用：");
        System.out.println("- Spring Data JPA自动管理JDBC连接");
        System.out.println("- @Transactional注解管理事务");
        System.out.println("- JdbcTemplate简化JDBC操作");
        System.out.println("- 通过Repository接口操作数据库");
    }

    // 数据库连接
    public static void demonstrateDatabaseConnection() throws SQLException {
        System.out.println("1. 数据库连接\n");
        System.out.println("作用：建立与数据库的连接");
        System.out.println("Spring应用：DataSource配置、连接池管理\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✓ 数据库连接成功");
            System.out.println("  数据库: " + conn.getMetaData().getDatabaseProductName());
            System.out.println("  版本: " + conn.getMetaData().getDatabaseProductVersion());
        }
        System.out.println();
    }

    // 创建表
    public static void demonstrateCreateTable() throws SQLException {
        System.out.println("2. 创建表\n");

        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE, " +
                    "age INT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✓ 表创建成功: users");
            System.out.println("  字段: id, name, email, age, created_at");
        }
        System.out.println();
    }

    // 插入数据
    public static void demonstrateInsert() throws SQLException {
        System.out.println("3. 插入数据 (INSERT)\n");
        System.out.println("作用：向数据库表中添加新记录");
        System.out.println("Spring应用：Repository.save()方法\n");

        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 插入多条记录
            String[][] users = {
                {"张三", "zhangsan@example.com", "25"},
                {"李四", "lisi@example.com", "30"},
                {"王五", "wangwu@example.com", "28"}
            };

            for (String[] user : users) {
                pstmt.setString(1, user[0]);
                pstmt.setString(2, user[1]);
                pstmt.setInt(3, Integer.parseInt(user[2]));
                int rows = pstmt.executeUpdate();
                System.out.println("✓ 插入成功: " + user[0] + " (影响 " + rows + " 行)");
            }
        }
        System.out.println();
    }

    // 查询数据
    public static void demonstrateQuery() throws SQLException {
        System.out.println("4. 查询数据 (SELECT)\n");
        System.out.println("作用：从数据库中检索数据");
        System.out.println("Spring应用：Repository.findAll(), findById()等\n");

        String sql = "SELECT id, name, email, age FROM users";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("查询结果:");
            System.out.println("ID\t姓名\t\t邮箱\t\t\t\t年龄");
            System.out.println("------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int age = rs.getInt("age");

                System.out.printf("%d\t%s\t\t%s\t\t%d%n", id, name, email, age);
            }
        }
        System.out.println();
    }

    // 更新和删除
    public static void demonstrateUpdateAndDelete() throws SQLException {
        System.out.println("5. 更新和删除数据\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 更新数据
            String updateSql = "UPDATE users SET age = ? WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, 26);
                pstmt.setString(2, "张三");
                int rows = pstmt.executeUpdate();
                System.out.println("✓ 更新成功: 张三的年龄 (影响 " + rows + " 行)");
            }

            // 条件查询
            String querySql = "SELECT name, age FROM users WHERE age > ?";
            try (PreparedStatement pstmt = conn.prepareStatement(querySql)) {
                pstmt.setInt(1, 27);
                ResultSet rs = pstmt.executeQuery();
                System.out.println("\n年龄大于27的用户:");
                while (rs.next()) {
                    System.out.println("  " + rs.getString("name") + " - " + rs.getInt("age") + "岁");
                }
            }

            // 删除数据
            String deleteSql = "DELETE FROM users WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setString(1, "王五");
                int rows = pstmt.executeUpdate();
                System.out.println("\n✓ 删除成功: 王五 (影响 " + rows + " 行)");
            }

            // 查询剩余记录数
            String countSql = "SELECT COUNT(*) as total FROM users";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(countSql)) {
                if (rs.next()) {
                    System.out.println("剩余用户数: " + rs.getInt("total"));
                }
            }
        }
        System.out.println();
    }

    // 事务处理
    public static void demonstrateTransaction() throws SQLException {
        System.out.println("6. 事务处理\n");
        System.out.println("作用：保证一组操作要么全部成功，要么全部失败");
        System.out.println("Spring应用：@Transactional注解\n");

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 关闭自动提交
            conn.setAutoCommit(false);
            System.out.println("开始事务...");

            // 操作1：插入用户
            String sql1 = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql1)) {
                pstmt.setString(1, "赵六");
                pstmt.setString(2, "zhaoliu@example.com");
                pstmt.setInt(3, 32);
                pstmt.executeUpdate();
                System.out.println("  ✓ 插入用户: 赵六");
            }

            // 操作2：更新统计（模拟）
            String sql2 = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                pstmt.setString(1, "钱七");
                pstmt.setString(2, "qianqi@example.com");
                pstmt.setInt(3, 29);
                pstmt.executeUpdate();
                System.out.println("  ✓ 插入用户: 钱七");
            }

            // 提交事务
            conn.commit();
            System.out.println("✓ 事务提交成功");

        } catch (SQLException e) {
            System.out.println("✗ 事务失败，回滚...");
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("✓ 事务已回滚");
                } catch (SQLException ex) {
                    System.out.println("回滚失败: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        System.out.println();
    }

    // PreparedStatement防SQL注入
    public static void demonstratePreparedStatement() throws SQLException {
        System.out.println("7. PreparedStatement防SQL注入\n");
        System.out.println("作用：防止SQL注入攻击，提高性能");
        System.out.println("Spring应用：JPA自动使用PreparedStatement\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // 安全的查询（使用PreparedStatement）
            String safeSql = "SELECT * FROM users WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(safeSql)) {
                pstmt.setString(1, "张三");
                ResultSet rs = pstmt.executeQuery();

                System.out.println("✓ 安全查询 (PreparedStatement):");
                while (rs.next()) {
                    System.out.println("  找到用户: " + rs.getString("name"));
                }
            }

            // 演示SQL注入防护
            String maliciousInput = "张三' OR '1'='1";
            System.out.println("\n尝试SQL注入: \"" + maliciousInput + "\"");
            try (PreparedStatement pstmt = conn.prepareStatement(safeSql)) {
                pstmt.setString(1, maliciousInput);
                ResultSet rs = pstmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    count++;
                }
                System.out.println("✓ PreparedStatement阻止了SQL注入，找到 " + count + " 条记录");
            }

            System.out.println("\n💡 重要提示：");
            System.out.println("- 永远使用PreparedStatement，不要用String拼接SQL");
            System.out.println("- PreparedStatement会自动转义特殊字符");
            System.out.println("- Spring Data JPA自动使用PreparedStatement");
        }
        System.out.println();
    }
}
