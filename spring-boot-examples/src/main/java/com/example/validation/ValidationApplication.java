package com.example.validation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Boot 表单验证示例
 * 学习目标：
 * 1. 掌握Jakarta Validation（JSR-380）注解的使用
 * 2. 理解@Valid和@Validated的区别
 * 3. 学会自定义验证错误响应
 * 4. 掌握分组验证和嵌套对象验证
 *
 * 验证注解说明：
 * - @NotNull：不能为null
 * - @NotEmpty：不能为null且长度大于0（用于字符串、集合）
 * - @NotBlank：不能为null且去除空格后长度大于0（只用于字符串）
 * - @Size：字符串、集合、数组的大小范围
 * - @Min/@Max：数值的最小/最大值
 * - @Email：邮箱格式验证
 * - @Pattern：正则表达式验证
 * - @Past/@Future：日期验证
 *
 * 应用场景：
 * - 用户注册/登录表单验证
 * - API接口参数校验
 * - 数据完整性检查
 * - 业务规则验证
 */
@SpringBootApplication
@RestController
@RequestMapping("/api/validation")
public class ValidationApplication {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot 表单验证示例 ===\n");
        System.out.println("学习内容：");
        System.out.println("1. 基础验证注解（@NotNull, @NotEmpty, @NotBlank等）");
        System.out.println("2. 字符串验证（@Size, @Email, @Pattern等）");
        System.out.println("3. 数值验证（@Min, @Max, @Positive等）");
        System.out.println("4. 日期验证（@Past, @Future等）");
        System.out.println("5. 自定义错误响应\n");

        SpringApplication.run(ValidationApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 测试地址：http://localhost:8080/api/validation");
        System.out.println("\n📝 测试示例：");
        System.out.println("curl -X POST http://localhost:8080/api/validation/register \\");
        System.out.println("  -H \"Content-Type: application/json\" \\");
        System.out.println("  -d '{\"username\":\"zhangsan\",\"email\":\"test@example.com\",\"age\":25,\"password\":\"123456\"}'");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }

    /**
     * 1. 用户注册接口 - 演示基础验证
     * POST /api/validation/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegisterDTO user) {
        return ResponseEntity.ok(new ApiResponse(
            200,
            "注册成功",
            Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "message", "用户信息验证通过！"
            )
        ));
    }

    /**
     * 2. 创建产品接口 - 演示数值和字符串验证
     * POST /api/validation/products
     */
    @PostMapping("/products")
    public ResponseEntity<ApiResponse> createProduct(@Valid @RequestBody ProductDTO product) {
        return ResponseEntity.ok(new ApiResponse(
            200,
            "产品创建成功",
            product
        ));
    }

    /**
     * 3. 预约接口 - 演示日期验证
     * POST /api/validation/appointments
     */
    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse> createAppointment(@Valid @RequestBody AppointmentDTO appointment) {
        return ResponseEntity.ok(new ApiResponse(
            200,
            "预约成功",
            appointment
        ));
    }

    /**
     * 4. 测试接口 - 返回验证规则说明
     * GET /api/validation/rules
     */
    @GetMapping("/rules")
    public ResponseEntity<Map<String, Object>> getValidationRules() {
        Map<String, Object> rules = new HashMap<>();

        rules.put("用户注册规则", Map.of(
            "username", "长度3-20字符，只能包含字母、数字、下划线",
            "email", "必须是有效的邮箱格式",
            "age", "年龄必须在18-100之间",
            "password", "长度至少6个字符"
        ));

        rules.put("产品创建规则", Map.of(
            "name", "产品名称不能为空，长度2-50字符",
            "price", "价格必须大于0",
            "stock", "库存不能为负数",
            "description", "描述长度不超过500字符"
        ));

        rules.put("预约规则", Map.of(
            "name", "姓名不能为空",
            "phone", "必须是有效的手机号码",
            "date", "预约日期必须是未来的日期"
        ));

        return ResponseEntity.ok(rules);
    }

    // ==================== 数据传输对象（DTO） ====================

    /**
     * 用户注册DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserRegisterDTO {
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
        private String username;

        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        @NotNull(message = "年龄不能为空")
        @Min(value = 18, message = "年龄必须大于等于18岁")
        @Max(value = 100, message = "年龄必须小于等于100岁")
        private Integer age;

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, message = "密码长度至少6个字符")
        private String password;
    }

    /**
     * 产品DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ProductDTO {
        @NotBlank(message = "产品名称不能为空")
        @Size(min = 2, max = 50, message = "产品名称长度必须在2-50个字符之间")
        private String name;

        @NotNull(message = "价格不能为空")
        @Positive(message = "价格必须大于0")
        private Double price;

        @NotNull(message = "库存不能为空")
        @Min(value = 0, message = "库存不能为负数")
        private Integer stock;

        @Size(max = 500, message = "描述长度不能超过500个字符")
        private String description;
    }

    /**
     * 预约DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class AppointmentDTO {
        @NotBlank(message = "姓名不能为空")
        private String name;

        @NotBlank(message = "手机号不能为空")
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
        private String phone;

        @NotNull(message = "预约日期不能为空")
        @Future(message = "预约日期必须是未来的日期")
        private LocalDate date;
    }

    /**
     * 统一响应对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ApiResponse {
        private Integer code;
        private String message;
        private Object data;
    }

    // ==================== 全局异常处理 ====================

    /**
     * 处理验证异常
     * 当@Valid验证失败时，会抛出MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse(400, "数据验证失败", errors));
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleOtherExceptions(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse(500, "服务器错误", ex.getMessage()));
    }
}
