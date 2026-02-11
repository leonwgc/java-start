package advanced;

import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Properties配置文件学习
 * 学习目标：
 * 1. 理解Properties文件格式
 * 2. 掌握配置文件的读写操作
 * 3. 学习ResourceBundle的使用
 * 4. 理解Spring配置管理的基础
 */
public class PropertiesDemo {
    public static void main(String[] args) {
        System.out.println("=== Properties配置文件学习 ===\n");

        try {
            // 示例1：基本Properties操作
            demonstrateBasicProperties();

            // 示例2：读写Properties文件
            demonstrateFileProperties();

            // 示例3：ResourceBundle国际化
            demonstrateResourceBundle();

            // 示例4：配置管理实战
            demonstrateConfigManager();

        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n💡 Spring中的应用：");
        System.out.println("- application.properties配置文件");
        System.out.println("- @Value注解注入配置值");
        System.out.println("- @ConfigurationProperties绑定配置");
        System.out.println("- Spring Profiles环境配置");
        System.out.println("- 国际化消息资源管理\n");
    }

    // 基本Properties操作
    public static void demonstrateBasicProperties() {
        System.out.println("1. 基本Properties操作\n");
        System.out.println("作用：键值对形式的配置管理");
        System.out.println("Spring应用：application.properties核心机制\n");

        Properties props = new Properties();

        // 设置属性
        props.setProperty("app.name", "Java学习项目");
        props.setProperty("app.version", "1.0.0");
        props.setProperty("server.port", "8080");
        props.setProperty("database.url", "jdbc:mysql://localhost:3306/mydb");

        // 读取属性
        System.out.println("应用名称: " + props.getProperty("app.name"));
        System.out.println("应用版本: " + props.getProperty("app.version"));
        System.out.println("服务器端口: " + props.getProperty("server.port"));

        // 使用默认值
        String timeout = props.getProperty("connection.timeout", "30");
        System.out.println("连接超时(默认值): " + timeout + "秒");

        // 遍历所有属性
        System.out.println("\n所有配置项:");
        props.forEach((key, value) ->
            System.out.println("  " + key + " = " + value));

        System.out.println();
    }

    // 读写Properties文件
    public static void demonstrateFileProperties() throws IOException {
        System.out.println("2. 读写Properties文件\n");
        System.out.println("作用：持久化配置到文件");
        System.out.println("Spring应用：读取application.properties\n");

        String configFile = "app.properties";

        // 写入配置文件
        Properties writeProps = new Properties();
        writeProps.setProperty("spring.application.name", "demo-app");
        writeProps.setProperty("spring.datasource.url", "jdbc:h2:mem:testdb");
        writeProps.setProperty("spring.datasource.username", "sa");
        writeProps.setProperty("spring.jpa.show-sql", "true");
        writeProps.setProperty("spring.jpa.hibernate.ddl-auto", "create");

        try (FileWriter writer = new FileWriter(configFile)) {
            writeProps.store(writer, "Spring Boot Application Configuration");
            System.out.println("✓ 配置已保存到: " + configFile);
        }

        // 读取配置文件
        Properties readProps = new Properties();
        try (FileReader reader = new FileReader(configFile)) {
            readProps.load(reader);
            System.out.println("\n从文件读取的配置:");
            readProps.forEach((key, value) ->
                System.out.println("  " + key + " = " + value));
        }

        // 清理：删除临时文件
        new File(configFile).delete();
        System.out.println();
    }

    // ResourceBundle国际化
    public static void demonstrateResourceBundle() {
        System.out.println("3. ResourceBundle国际化\n");
        System.out.println("作用：支持多语言配置");
        System.out.println("Spring应用：MessageSource国际化\n");

        // 模拟不同语言环境
        System.out.println("示例：国际化消息");
        System.out.println("中文: 欢迎使用Java学习系统");
        System.out.println("英文: Welcome to Java Learning System");
        System.out.println("日文: Javaラーニングシステムへようこそ");

        System.out.println("\n在Spring Boot中的使用:");
        System.out.println("1. 创建messages_zh_CN.properties");
        System.out.println("2. 创建messages_en_US.properties");
        System.out.println("3. 使用@Autowired MessageSource");
        System.out.println("4. 调用getMessage()获取本地化消息");
        System.out.println();
    }

    // 配置管理实战
    public static void demonstrateConfigManager() {
        System.out.println("4. 配置管理实战\n");
        System.out.println("场景：应用配置管理器\n");

        // 创建配置管理器
        AppConfig config = new AppConfig();
        config.loadDefaults();

        // 使用配置
        System.out.println("应用配置:");
        System.out.println("  应用名: " + config.getAppName());
        System.out.println("  环境: " + config.getEnvironment());
        System.out.println("  端口: " + config.getServerPort());
        System.out.println("  调试模式: " + config.isDebugEnabled());

        // 数据库配置
        System.out.println("\n数据库配置:");
        System.out.println("  URL: " + config.getDatabaseUrl());
        System.out.println("  用户名: " + config.getDatabaseUsername());
        System.out.println("  最大连接数: " + config.getMaxConnections());

        // 修改配置
        System.out.println("\n修改配置:");
        config.setDebugEnabled(true);
        config.setServerPort(9090);
        System.out.println("  新端口: " + config.getServerPort());
        System.out.println("  调试模式: " + config.isDebugEnabled());

        System.out.println("\n💡 对比Spring Boot:");
        System.out.println("Spring Boot中使用@ConfigurationProperties自动绑定:");
        System.out.println("");
        System.out.println("@ConfigurationProperties(prefix = \"app\")");
        System.out.println("public class AppConfig {");
        System.out.println("    private String name;");
        System.out.println("    private int serverPort;");
        System.out.println("    // 自动从application.properties读取");
        System.out.println("}");
        System.out.println();
    }

    // ========== 配置管理器类 ==========

    /**
     * 应用配置管理器
     * 模拟Spring Boot的配置管理机制
     */
    static class AppConfig {
        private Properties properties = new Properties();

        // 加载默认配置
        public void loadDefaults() {
            // 应用配置
            properties.setProperty("app.name", "Java学习项目");
            properties.setProperty("app.environment", "development");
            properties.setProperty("app.version", "1.0.0");

            // 服务器配置
            properties.setProperty("server.port", "8080");
            properties.setProperty("server.context-path", "/api");

            // 数据库配置
            properties.setProperty("database.url", "jdbc:mysql://localhost:3306/mydb");
            properties.setProperty("database.username", "root");
            properties.setProperty("database.password", "");
            properties.setProperty("database.max-connections", "10");

            // 功能开关
            properties.setProperty("debug.enabled", "false");
            properties.setProperty("cache.enabled", "true");
        }

        // 应用配置
        public String getAppName() {
            return properties.getProperty("app.name");
        }

        public String getEnvironment() {
            return properties.getProperty("app.environment");
        }

        // 服务器配置
        public int getServerPort() {
            return Integer.parseInt(properties.getProperty("server.port", "8080"));
        }

        public void setServerPort(int port) {
            properties.setProperty("server.port", String.valueOf(port));
        }

        // 数据库配置
        public String getDatabaseUrl() {
            return properties.getProperty("database.url");
        }

        public String getDatabaseUsername() {
            return properties.getProperty("database.username");
        }

        public int getMaxConnections() {
            return Integer.parseInt(properties.getProperty("database.max-connections", "10"));
        }

        // 功能开关
        public boolean isDebugEnabled() {
            return Boolean.parseBoolean(properties.getProperty("debug.enabled", "false"));
        }

        public void setDebugEnabled(boolean enabled) {
            properties.setProperty("debug.enabled", String.valueOf(enabled));
        }

        public boolean isCacheEnabled() {
            return Boolean.parseBoolean(properties.getProperty("cache.enabled", "true"));
        }
    }
}
