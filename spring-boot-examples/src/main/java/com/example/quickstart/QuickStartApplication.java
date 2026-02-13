package com.example.quickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot 快速入门
 * 学习目标：
 * 1. 理解Spring Boot的核心注解
 * 2. 掌握Spring Boot应用的启动方式
 * 3. 学会创建简单的REST接口
 * 4. 了解Spring Boot的自动配置机制
 *
 * Spring Boot是什么？
 * - 简化Spring应用开发的框架
 * - 约定优于配置（Convention over Configuration）
 * - 内嵌服务器，无需部署WAR文件
 * - 自动配置，开箱即用
 * - 生产级别的监控和管理功能
 *
 * 核心注解说明：
 * - @SpringBootApplication：组合注解，包含以下三个注解
 * * @Configuration：标记配置类
 * * @EnableAutoConfiguration：启用自动配置
 * * @ComponentScan：组件扫描
 * - @RestController：REST控制器，返回JSON数据
 * - @GetMapping：处理GET请求
 */
@SpringBootApplication
@RestController
public class QuickStartApplication {

    /**
     * 应用启动入口
     * main方法启动Spring Boot应用
     */
    public static void main(String[] args) {
        System.out.println("=== Spring Boot 快速入门 ===\n");
        System.out.println("正在启动Spring Boot应用...\n");

        SpringApplication.run(QuickStartApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 访问地址：http://localhost:8080");
        System.out.println("📍 测试接口：http://localhost:8080/hello");
        System.out.println("📍 问候接口：http://localhost:8080/hello?name=张三");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }

    /**
     * 1. 简单的Hello World接口
     * GET http://localhost:8080/hello
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot! 你好，Spring Boot！";
    }

    /**
     * 2. 带参数的问候接口
     * GET http://localhost:8080/hello?name=张三
     */
    @GetMapping("/greet")
    public String greet(String name) {
        return "你好，" + (name != null ? name : "访客") + "！";
    }

    /**
     * 3. 返回JSON对象
     * GET http://localhost:8080/info
     */
    @GetMapping("/info")
    public AppInfo getInfo() {
        return new AppInfo(
                "Spring Boot 学习示例",
                "1.0.0",
                "这是一个Spring Boot快速入门示例");
    }

    record AppInfo(String name, String version, String description) {
    }

    /**
     * 应用信息对象
     * Spring Boot会自动将对象转换为JSON
     */
    // static class AppInfo {
    // private String name;
    // private String version;
    // private String description;

    // public AppInfo(String name, String version, String description) {
    // this.name = name;
    // this.version = version;
    // this.description = description;
    // }

    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }

    // public String getVersion() { return version; }
    // public void setVersion(String version) { this.version = version; }

    // public String getDescription() { return description; }
    // public void setDescription(String description) { this.description =
    // description; }
    // }
}
