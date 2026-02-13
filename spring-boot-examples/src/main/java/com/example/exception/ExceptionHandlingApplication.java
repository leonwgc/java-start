package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot 异常处理示例
 * 学习目标：
 * 1. 理解Spring Boot的异常处理机制
 * 2. 掌握@ExceptionHandler的使用
 * 3. 学会使用@ControllerAdvice进行全局异常处理
 * 4. 掌握自定义业务异常的创建和使用
 * 5. 了解如何返回统一的错误响应格式
 *
 * 异常处理层次：
 * 1. Controller级别：@ExceptionHandler在Controller内部处理
 * 2. 全局级别：@ControllerAdvice统一处理所有Controller的异常
 * 3. 自定义异常：业务异常继承RuntimeException
 *
 * 应用场景：
 * - API接口统一错误响应
 * - 业务异常处理（资源未找到、权限不足等）
 * - 参数验证异常处理
 * - 系统异常处理
 */
@SpringBootApplication
@RestController
@RequestMapping("/api/exception")
public class ExceptionHandlingApplication {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot 异常处理示例 ===\n");
        System.out.println("学习内容：");
        System.out.println("1. 自定义业务异常");
        System.out.println("2. @ExceptionHandler注解使用");
        System.out.println("3. @ControllerAdvice全局异常处理");
        System.out.println("4. 统一错误响应格式");
        System.out.println("5. HTTP状态码的正确使用\n");

        SpringApplication.run(ExceptionHandlingApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 测试地址：http://localhost:8080/api/exception");
        System.out.println("\n📝 测试示例：");
        System.out.println("# 测试资源未找到异常");
        System.out.println("curl http://localhost:8080/api/exception/users/999");
        System.out.println("\n# 测试业务异常");
        System.out.println("curl http://localhost:8080/api/exception/divide/10/0");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }

    // ==================== 模拟数据 ====================

    private static final Map<Long, User> users = new HashMap<>();

    static {
        users.put(1L, new User(1L, "张三", "zhangsan@example.com"));
        users.put(2L, new User(2L, "李四", "lisi@example.com"));
        users.put(3L, new User(3L, "王五", "wangwu@example.com"));
    }

    // ==================== 接口示例 ====================

    /**
     * 1. 正常接口 - 获取所有用户
     * GET /api/exception/users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse(
            200,
            "success",
            "获取成功",
            users.values()
        ));
    }

    /**
     * 2. 资源未找到异常示例
     * GET /api/exception/users/{id}
     * 测试：curl http://localhost:8080/api/exception/users/999
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        User user = users.get(id);
        if (user == null) {
            // 抛出自定义的资源未找到异常
            throw new ResourceNotFoundException("用户", "id", id);
        }
        return ResponseEntity.ok(new ApiResponse(
            200,
            "success",
            "获取成功",
            user
        ));
    }

    /**
     * 3. 业务异常示例 - 除法运算
     * GET /api/exception/divide/{a}/{b}
     * 测试：curl http://localhost:8080/api/exception/divide/10/0
     */
    @GetMapping("/divide/{a}/{b}")
    public ResponseEntity<ApiResponse> divide(
            @PathVariable Integer a,
            @PathVariable Integer b) {

        if (b == 0) {
            // 抛出自定义的业务异常
            throw new BusinessException("除数不能为0");
        }

        int result = a / b;
        return ResponseEntity.ok(new ApiResponse(
            200,
            "success",
            "计算成功",
            Map.of("result", result, "expression", a + " / " + b + " = " + result)
        ));
    }

    /**
     * 4. 验证异常示例
     * POST /api/exception/users
     * 测试：curl -X POST http://localhost:8080/api/exception/users \
     *       -H "Content-Type: application/json" \
     *       -d '{"name":"","email":"invalid"}'
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new ValidationException("用户名不能为空");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("邮箱格式不正确");
        }

        Long newId = users.size() + 1L;
        user.setId(newId);
        users.put(newId, user);

        return ResponseEntity.ok(new ApiResponse(
            200,
            "success",
            "用户创建成功",
            user
        ));
    }

    /**
     * 5. 系统异常示例
     * GET /api/exception/error
     * 测试：curl http://localhost:8080/api/exception/error
     */
    @GetMapping("/error")
    public ResponseEntity<ApiResponse> triggerError() {
        // 模拟系统异常
        throw new RuntimeException("这是一个模拟的系统异常");
    }

    /**
     * 6. 权限异常示例
     * DELETE /api/exception/users/{id}
     * 测试：curl -X DELETE http://localhost:8080/api/exception/users/1
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        // 模拟权限检查
        throw new PermissionDeniedException("您没有权限删除用户");
    }

    /**
     * 7. 获取异常处理说明
     * GET /api/exception/guide
     */
    @GetMapping("/guide")
    public ResponseEntity<Map<String, Object>> getGuide() {
        Map<String, Object> guide = new HashMap<>();

        guide.put("异常类型说明", Map.of(
            "ResourceNotFoundException", "资源未找到（404）",
            "BusinessException", "业务异常（400）",
            "ValidationException", "验证异常（400）",
            "PermissionDeniedException", "权限不足（403）",
            "RuntimeException", "系统异常（500）"
        ));

        guide.put("测试接口", Map.of(
            "资源未找到", "GET /api/exception/users/999",
            "业务异常", "GET /api/exception/divide/10/0",
            "验证异常", "POST /api/exception/users (空name)",
            "权限异常", "DELETE /api/exception/users/1",
            "系统异常", "GET /api/exception/error"
        ));

        guide.put("响应格式", Map.of(
            "code", "HTTP状态码",
            "status", "状态（success/error）",
            "message", "提示信息",
            "timestamp", "时间戳",
            "data", "数据（可选）"
        ));

        return ResponseEntity.ok(guide);
    }

    // ==================== 数据模型 ====================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
        private String email;
    }

    /**
     * 统一响应对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ApiResponse {
        private Integer code;
        private String status;
        private String message;
        private Object data;
        private LocalDateTime timestamp;

        public ApiResponse(Integer code, String status, String message, Object data) {
            this.code = code;
            this.status = status;
            this.message = message;
            this.data = data;
            this.timestamp = LocalDateTime.now();
        }
    }

    // ==================== 自定义异常 ====================

    /**
     * 资源未找到异常
     */
    static class ResourceNotFoundException extends RuntimeException {
        private final String resourceName;
        private final String fieldName;
        private final Object fieldValue;

        public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
            super(String.format("%s未找到: %s = %s", resourceName, fieldName, fieldValue));
            this.resourceName = resourceName;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public String getResourceName() { return resourceName; }
        public String getFieldName() { return fieldName; }
        public Object getFieldValue() { return fieldValue; }
    }

    /**
     * 业务异常
     */
    static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }

    /**
     * 验证异常
     */
    static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * 权限不足异常
     */
    static class PermissionDeniedException extends RuntimeException {
        public PermissionDeniedException(String message) {
            super(message);
        }
    }

    // ==================== 全局异常处理器 ====================

    /**
     * 全局异常处理器
     * 使用@ControllerAdvice可以统一处理所有Controller抛出的异常
     */
    @ControllerAdvice
    static class GlobalExceptionHandler {

        /**
         * 处理资源未找到异常
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(
                    404,
                    "error",
                    ex.getMessage(),
                    Map.of(
                        "resource", ex.getResourceName(),
                        "field", ex.getFieldName(),
                        "value", ex.getFieldValue()
                    )
                ));
        }

        /**
         * 处理业务异常
         */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(
                    400,
                    "error",
                    ex.getMessage(),
                    null
                ));
        }

        /**
         * 处理验证异常
         */
        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ApiResponse> handleValidationException(ValidationException ex) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(
                    400,
                    "error",
                    ex.getMessage(),
                    null
                ));
        }

        /**
         * 处理权限不足异常
         */
        @ExceptionHandler(PermissionDeniedException.class)
        public ResponseEntity<ApiResponse> handlePermissionDenied(PermissionDeniedException ex) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(
                    403,
                    "error",
                    ex.getMessage(),
                    null
                ));
        }

        /**
         * 处理所有其他异常
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse> handleAllExceptions(Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(
                    500,
                    "error",
                    "服务器内部错误：" + ex.getMessage(),
                    null
                ));
        }
    }
}
