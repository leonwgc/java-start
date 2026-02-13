package com.example.interceptor;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Spring Boot 拦截器和过滤器示例
 * 学习目标：
 * 1. 理解Filter和Interceptor的区别
 * 2. 掌握Filter的创建和注册
 * 3. 掌握Interceptor的创建和配置
 * 4. 了解请求处理的完整流程
 * 5. 学会使用拦截器实现通用功能
 *
 * Filter vs Interceptor：
 * ┌───────────────────────────────────┐
 * │ Filter（过滤器）                    │
 * │ - Servlet规范                      │
 * │ - 容器级别                          │
 * │ - 可以拦截所有请求                   │
 * │ - 执行顺序：请求前 → 处理 → 响应后    │
 * └───────────────────────────────────┘
 *         ↓
 * ┌───────────────────────────────────┐
 * │ Interceptor（拦截器）               │
 * │ - Spring框架                       │
 * │ - Spring MVC级别                   │
 * │ - 只拦截Controller请求              │
 * │ - 可以访问Controller方法信息         │
 * │ - 执行顺序：preHandle → Controller →│
 * │   postHandle → afterCompletion     │
 * └───────────────────────────────────┘
 *
 * 应用场景：
 * Filter：
 * - 字符编码转换
 * - 请求日志记录
 * - XSS攻击防御
 * - CORS跨域处理
 *
 * Interceptor：
 * - 登录验证
 * - 权限检查
 * - 操作日志
 * - 性能监控
 */
@SpringBootApplication
@Slf4j
public class InterceptorApplication {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot 拦截器和过滤器示例 ===\n");
        System.out.println("学习内容：");
        System.out.println("1. Filter（过滤器）的使用");
        System.out.println("2. Interceptor（拦截器）的使用");
        System.out.println("3. Filter和Interceptor的区别");
        System.out.println("4. 请求处理流程分析");
        System.out.println("5. 实战示例：日志记录、权限验证、性能监控\n");

        SpringApplication.run(InterceptorApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 测试地址：http://localhost:8080/api/demo");
        System.out.println("\n📝 测试示例：");
        System.out.println("curl http://localhost:8080/api/demo/public");
        System.out.println("curl http://localhost:8080/api/demo/protected");
        System.out.println("curl -H \"Authorization: Bearer valid-token\" http://localhost:8080/api/demo/protected");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }
}

/**
 * 1. 请求日志过滤器
 * 记录每个HTTP请求的信息
 */
@Slf4j
@Component
class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString().substring(0, 8);

        log.info("🔵 [Filter] 请求开始 - ID: {}, Method: {}, URI: {}, RemoteAddr: {}",
                requestId,
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpRequest.getRemoteAddr());

        // 传递请求到下一个过滤器或控制器
        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        log.info("🔵 [Filter] 请求结束 - ID: {}, Status: {}, Duration: {}ms",
                requestId,
                httpResponse.getStatus(),
                duration);
    }
}

/**
 * 2. 字符编码过滤器
 * 确保请求和响应使用UTF-8编码
 */
@Slf4j
@Component
class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.debug("🔵 [EncodingFilter] 设置字符编码为UTF-8");

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        chain.doFilter(request, response);
    }
}

/**
 * 3. 登录验证拦截器
 * 验证用户是否已登录
 */
@Slf4j
@Component
class AuthenticationInterceptor implements HandlerInterceptor {

    /**
     * 在Controller方法执行之前调用
     * 返回true继续执行，返回false中断请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getRequestURI();

        log.info("🟢 [Interceptor-preHandle] 开始验证 - URI: {}", uri);

        // 公开接口，不需要验证
        if (uri.contains("/public") || uri.contains("/guide")) {
            log.info("🟢 [Interceptor-preHandle] 公开接口，跳过验证");
            return true;
        }

        // 检查Authorization header
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            log.warn("🟢 [Interceptor-preHandle] 验证失败 - 缺少Authorization header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权：请提供有效的token\"}");
            return false;
        }

        // 简单的token验证（实际项目中应该验证JWT）
        if (!token.startsWith("Bearer ")) {
            log.warn("🟢 [Interceptor-preHandle] 验证失败 - token格式错误");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权：token格式错误\"}");
            return false;
        }

        log.info("🟢 [Interceptor-preHandle] 验证成功");
        return true;
    }

    /**
     * 在Controller方法执行之后、视图渲染之前调用
     * 可以对ModelAndView进行操作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                          Object handler, ModelAndView modelAndView) throws Exception {
        log.info("🟢 [Interceptor-postHandle] Controller执行完成 - URI: {}", request.getRequestURI());
    }

    /**
     * 在整个请求完成之后调用（视图渲染完成）
     * 无论是否发生异常都会调用
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("🟢 [Interceptor-afterCompletion] 请求处理异常 - URI: {}, Error: {}",
                    request.getRequestURI(), ex.getMessage());
        } else {
            log.info("🟢 [Interceptor-afterCompletion] 请求完成 - URI: {}", request.getRequestURI());
        }
    }
}

/**
 * 4. 性能监控拦截器
 * 监控接口响应时间
 */
@Slf4j
@Component
class PerformanceInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        startTimeThreadLocal.set(System.currentTimeMillis());
        log.debug("🟡 [PerformanceInterceptor] 开始计时");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                               Object handler, Exception ex) throws Exception {
        Long startTime = startTimeThreadLocal.get();
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;

            if (duration > 1000) {
                log.warn("🟡 [PerformanceInterceptor] ⚠️ 慢请求 - URI: {}, Duration: {}ms",
                        request.getRequestURI(), duration);
            } else {
                log.info("🟡 [PerformanceInterceptor] 请求耗时 - URI: {}, Duration: {}ms",
                        request.getRequestURI(), duration);
            }

            startTimeThreadLocal.remove();
        }
    }
}

/**
 * 5. Web MVC 配置
 * 注册拦截器
 */
@Configuration
class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final PerformanceInterceptor performanceInterceptor;

    public WebMvcConfig(AuthenticationInterceptor authenticationInterceptor,
                       PerformanceInterceptor performanceInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.performanceInterceptor = performanceInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册性能监控拦截器（拦截所有请求）
        registry.addInterceptor(performanceInterceptor)
                .addPathPatterns("/api/**");

        // 注册认证拦截器（拦截需要认证的请求）
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/demo/public", "/api/demo/guide");
    }
}

/**
 * 6. 测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/demo")
class DemoController {

    /**
     * 公开接口（不需要认证）
     * GET /api/demo/public
     */
    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        log.info("⚪ [Controller] 处理公开请求");
        return Map.of(
            "message", "这是一个公开接口，不需要认证",
            "timestamp", LocalDateTime.now(),
            "status", "success"
        );
    }

    /**
     * 受保护接口（需要认证）
     * GET /api/demo/protected
     * 测试：curl -H "Authorization: Bearer valid-token" http://localhost:8080/api/demo/protected
     */
    @GetMapping("/protected")
    public Map<String, Object> protectedEndpoint() {
        log.info("⚪ [Controller] 处理受保护请求");
        return Map.of(
            "message", "这是一个受保护接口，需要提供有效的token",
            "timestamp", LocalDateTime.now(),
            "status", "success"
        );
    }

    /**
     * 慢接口（用于测试性能监控）
     * GET /api/demo/slow
     */
    @GetMapping("/slow")
    public Map<String, Object> slowEndpoint() throws InterruptedException {
        log.info("⚪ [Controller] 处理慢请求");
        // 模拟耗时操作
        Thread.sleep(1500);
        return Map.of(
            "message", "这是一个慢接口，耗时1.5秒",
            "timestamp", LocalDateTime.now(),
            "status", "success"
        );
    }

    /**
     * 获取使用指南
     * GET /api/demo/guide
     */
    @GetMapping("/guide")
    public Map<String, Object> getGuide() {
        Map<String, Object> guide = new HashMap<>();

        guide.put("Filter说明", Map.of(
            "定义", "Servlet规范，容器级别的组件",
            "执行时机", "请求到达Servlet之前和响应发送之后",
            "作用", "字符编码、请求日志、XSS防御等",
            "本项目示例", List.of("LoggingFilter - 请求日志", "EncodingFilter - 字符编码")
        ));

        guide.put("Interceptor说明", Map.of(
            "定义", "Spring MVC框架的组件",
            "执行时机", "Controller方法执行前后",
            "作用", "登录验证、权限检查、操作日志、性能监控等",
            "本项目示例", List.of(
                "AuthenticationInterceptor - 登录验证",
                "PerformanceInterceptor - 性能监控"
            )
        ));

        guide.put("执行顺序", List.of(
            "1. LoggingFilter - 记录请求开始",
            "2. EncodingFilter - 设置字符编码",
            "3. PerformanceInterceptor.preHandle - 开始计时",
            "4. AuthenticationInterceptor.preHandle - 验证登录",
            "5. Controller方法执行",
            "6. AuthenticationInterceptor.postHandle",
            "7. PerformanceInterceptor.afterCompletion - 计算耗时",
            "8. AuthenticationInterceptor.afterCompletion",
            "9. LoggingFilter - 记录请求结束"
        ));

        guide.put("测试接口", Map.of(
            "公开接口", "curl http://localhost:8080/api/demo/public",
            "受保护接口（无token）", "curl http://localhost:8080/api/demo/protected",
            "受保护接口（有token）", "curl -H \"Authorization: Bearer valid-token\" http://localhost:8080/api/demo/protected",
            "慢接口", "curl http://localhost:8080/api/demo/slow"
        ));

        return guide;
    }
}
