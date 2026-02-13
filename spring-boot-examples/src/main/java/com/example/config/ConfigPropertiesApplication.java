package com.example.config;

import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring Boot 配置属性示例
 * 学习目标：
 * 1. 掌握@Value注解注入配置值
 * 2. 理解@ConfigurationProperties的使用
 * 3. 学会配置文件的层级结构
 * 4. 掌握不同数据类型的配置注入
 * 5. 了解配置的优先级和覆盖规则
 *
 * 配置注入方式：
 * 1. @Value：适合简单的配置注入
 * 2. @ConfigurationProperties：适合批量配置，支持类型安全和验证
 * 3. Environment：通过环境对象动态获取配置
 *
 * 配置文件支持：
 * - application.properties
 * - application.yml / application.yaml
 * - 支持profile：application-{profile}.yml
 *
 * 应用场景：
 * - 数据库连接配置
 * - 第三方API密钥配置
 * - 业务参数配置
 * - 功能开关配置
 */
@SpringBootApplication
@EnableConfigurationProperties({
    AppProperties.class,
    DatabaseProperties.class,
    SecurityProperties.class
})
public class ConfigPropertiesApplication {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot 配置属性示例 ===\n");
        System.out.println("学习内容：");
        System.out.println("1. @Value注解注入简单配置");
        System.out.println("2. @ConfigurationProperties批量配置注入");
        System.out.println("3. 配置文件的层级结构");
        System.out.println("4. 不同数据类型配置（String、数字、布尔、List、Map）");
        System.out.println("5. 配置的默认值和验证\n");

        SpringApplication.run(ConfigPropertiesApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 测试地址：http://localhost:8080/api/config");
        System.out.println("\n📝 测试示例：");
        System.out.println("curl http://localhost:8080/api/config/app");
        System.out.println("curl http://localhost:8080/api/config/database");
        System.out.println("curl http://localhost:8080/api/config/security");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }

    /**
     * 启动时打印配置信息
     */
    @Bean
    public CommandLineRunner printConfig(
            AppProperties appProperties,
            DatabaseProperties databaseProperties,
            SecurityProperties securityProperties) {

        return args -> {
            System.out.println("\n========== 配置信息 ==========");
            System.out.println("📱 应用配置：");
            System.out.println("  名称：" + appProperties.getName());
            System.out.println("  版本：" + appProperties.getVersion());
            System.out.println("  描述：" + appProperties.getDescription());
            System.out.println("  作者：" + appProperties.getAuthor());

            System.out.println("\n💾 数据库配置：");
            System.out.println("  URL：" + databaseProperties.getUrl());
            System.out.println("  用户名：" + databaseProperties.getUsername());
            System.out.println("  最大连接数：" + databaseProperties.getMaxConnections());
            System.out.println("  超时：" + databaseProperties.getTimeout());

            System.out.println("\n🔐 安全配置：");
            System.out.println("  JWT密钥：" + securityProperties.getJwt().getSecret().substring(0, 10) + "...");
            System.out.println("  JWT过期时间：" + securityProperties.getJwt().getExpiration());
            System.out.println("  启用CORS：" + securityProperties.getCors().isEnabled());
            System.out.println("  允许的源：" + securityProperties.getCors().getAllowedOrigins());
            System.out.println("===============================\n");
        };
    }
}

/**
 * 应用基本配置
 * 使用@ConfigurationProperties注入配置
 * 配置前缀：app
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
class AppProperties {
    private String name = "Spring Boot示例";
    private String version = "1.0.0";
    private String description = "学习Spring Boot配置管理";
    private String author = "Java学习者";
    private List<String> features;
    private Map<String, String> contacts;
}

/**
 * 数据库配置
 * 配置前缀：database
 */
@Data
@Component
@ConfigurationProperties(prefix = "database")
class DatabaseProperties {
    private String url = "jdbc:h2:mem:testdb";
    private String username = "sa";
    private String password = "";
    private String driverClassName = "org.h2.Driver";
    private Integer maxConnections = 10;
    private Integer timeout = 30;
}

/**
 * 安全配置（嵌套配置示例）
 * 配置前缀：security
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
class SecurityProperties {
    private JwtProperties jwt = new JwtProperties();
    private CorsProperties cors = new CorsProperties();

    @Data
    public static class JwtProperties {
        private String secret = "my-secret-key-for-jwt-token-generation";
        private Long expiration = 86400000L; // 24小时（毫秒）
    }

    @Data
    public static class CorsProperties {
        private boolean enabled = true;
        private List<String> allowedOrigins = List.of("http://localhost:3000");
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE");
    }
}

/**
 * 配置控制器 - 展示配置信息
 */
@RestController
@RequestMapping("/api/config")
class ConfigController {

    // 方式1：使用@Value注入简单配置
    @Value("${server.port:8080}")
    private Integer serverPort;

    @Value("${spring.application.name:demo}")
    private String applicationName;

    // 方式2：注入@ConfigurationProperties对象
    private final AppProperties appProperties;
    private final DatabaseProperties databaseProperties;
    private final SecurityProperties securityProperties;

    public ConfigController(
            AppProperties appProperties,
            DatabaseProperties databaseProperties,
            SecurityProperties securityProperties) {
        this.appProperties = appProperties;
        this.databaseProperties = databaseProperties;
        this.securityProperties = securityProperties;
    }

    /**
     * 1. 获取应用配置
     * GET /api/config/app
     */
    @GetMapping("/app")
    public Map<String, Object> getAppConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("name", appProperties.getName());
        config.put("version", appProperties.getVersion());
        config.put("description", appProperties.getDescription());
        config.put("author", appProperties.getAuthor());
        config.put("features", appProperties.getFeatures());
        config.put("contacts", appProperties.getContacts());
        return config;
    }

    /**
     * 2. 获取数据库配置
     * GET /api/config/database
     */
    @GetMapping("/database")
    public Map<String, Object> getDatabaseConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("url", databaseProperties.getUrl());
        config.put("username", databaseProperties.getUsername());
        config.put("password", "******"); // 密码不暴露
        config.put("driverClassName", databaseProperties.getDriverClassName());
        config.put("maxConnections", databaseProperties.getMaxConnections());
        config.put("timeout", databaseProperties.getTimeout());
        return config;
    }

    /**
     * 3. 获取安全配置
     * GET /api/config/security
     */
    @GetMapping("/security")
    public Map<String, Object> getSecurityConfig() {
        Map<String, Object> config = new HashMap<>();

        Map<String, Object> jwt = new HashMap<>();
        jwt.put("secret", securityProperties.getJwt().getSecret().substring(0, 10) + "...");
        jwt.put("expiration", securityProperties.getJwt().getExpiration());

        Map<String, Object> cors = new HashMap<>();
        cors.put("enabled", securityProperties.getCors().isEnabled());
        cors.put("allowedOrigins", securityProperties.getCors().getAllowedOrigins());
        cors.put("allowedMethods", securityProperties.getCors().getAllowedMethods());

        config.put("jwt", jwt);
        config.put("cors", cors);

        return config;
    }

    /**
     * 4. 获取服务器配置（@Value方式）
     * GET /api/config/server
     */
    @GetMapping("/server")
    public Map<String, Object> getServerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("port", serverPort);
        config.put("applicationName", applicationName);
        return config;
    }

    /**
     * 5. 获取所有配置
     * GET /api/config/all
     */
    @GetMapping("/all")
    public Map<String, Object> getAllConfig() {
        Map<String, Object> allConfig = new HashMap<>();
        allConfig.put("app", getAppConfig());
        allConfig.put("database", getDatabaseConfig());
        allConfig.put("security", getSecurityConfig());
        allConfig.put("server", getServerConfig());
        return allConfig;
    }

    /**
     * 6. 配置说明文档
     * GET /api/config/guide
     */
    @GetMapping("/guide")
    public Map<String, Object> getGuide() {
        Map<String, Object> guide = new HashMap<>();

        guide.put("配置注入方式", Map.of(
            "@Value", "适合简单配置，语法：@Value(\"${key:defaultValue}\")",
            "@ConfigurationProperties", "适合批量配置，支持类型安全和嵌套对象",
            "Environment", "通过环境对象动态获取配置"
        ));

        guide.put("配置文件位置", List.of(
            "src/main/resources/application.yml",
            "src/main/resources/application-{profile}.yml",
            "外部配置文件：--spring.config.location"
        ));

        guide.put("配置示例", Map.of(
            "简单值", "app.name=MyApp",
            "数字", "database.max-connections=10",
            "布尔", "security.cors.enabled=true",
            "列表", "app.features[0]=feature1",
            "Map", "app.contacts.email=test@example.com",
            "嵌套对象", "security.jwt.secret=xxx"
        ));

        guide.put("配置优先级", List.of(
            "1. 命令行参数",
            "2. Java系统属性",
            "3. 操作系统环境变量",
            "4. application-{profile}.yml",
            "5. application.yml",
            "6. @PropertySource",
            "7. 默认值"
        ));

        return guide;
    }
}
