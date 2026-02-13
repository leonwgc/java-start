package advanced;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * Bean Validation（JSR-380）详解
 * 学习目标：
 * 1. 理解Bean Validation的作用和重要性
 * 2. 掌握常用验证注解的使用
 * 3. 学习自定义验证器
 * 4. 了解验证组和级联验证
 *
 * Bean Validation是什么？
 * - Java标准的数据验证规范（JSR-380）
 * - 使用注解声明式验证对象属性
 * - Hibernate Validator是最流行的实现
 * - Spring Boot默认集成，自动启用
 *
 * Spring应用：
 * - REST API请求参数验证
 * - @Valid/@Validated注解自动触发验证
 * - 统一异常处理
 * - 表单数据验证
 */
public class ValidationDemo {

    // 获取验证器工厂和验证器
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static void main(String[] args) {
        System.out.println("=== Bean Validation详解 ===\n");

        demonstrateBasicValidation();
        demonstrateCommonConstraints();
        demonstrateCustomMessages();
        demonstrateNestedValidation();
        demonstrateValidationGroups();
        demonstrateMethodValidation();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. 基础验证
     */
    private static void demonstrateBasicValidation() {
        System.out.println("1. 基础验证\n");

        // 创建有效对象
        User validUser = new User("张三", "zhangsan@example.com", 25);
        Set<ConstraintViolation<User>> violations1 = validator.validate(validUser);

        System.out.println("有效用户: " + validUser.name);
        System.out.println("验证结果: " + (violations1.isEmpty() ? "✓ 通过" : "✗ 失败"));
        System.out.println();

        // 创建无效对象
        User invalidUser = new User("", "invalid-email", 150);
        Set<ConstraintViolation<User>> violations2 = validator.validate(invalidUser);

        System.out.println("无效用户: " + invalidUser.name);
        System.out.println("验证结果: ✗ 失败");
        System.out.println("错误信息:");
        violations2.forEach(v ->
            System.out.println("  • " + v.getPropertyPath() + ": " + v.getMessage())
        );
        System.out.println();
    }

    /**
     * 2. 常用约束注解
     */
    private static void demonstrateCommonConstraints() {
        System.out.println("2. 常用约束注解\n");

        System.out.println("【字符串约束】");
        System.out.println("@NotNull       - 不能为null");
        System.out.println("@NotEmpty      - 不能为null或空");
        System.out.println("@NotBlank      - 不能为null、空或只有空格");
        System.out.println("@Size(min,max) - 长度范围");
        System.out.println("@Pattern       - 正则表达式匹配");
        System.out.println("@Email         - 邮箱格式");
        System.out.println();

        System.out.println("【数值约束】");
        System.out.println("@Min(value)    - 最小值");
        System.out.println("@Max(value)    - 最大值");
        System.out.println("@Positive      - 正数");
        System.out.println("@Negative      - 负数");
        System.out.println("@DecimalMin    - 小数最小值");
        System.out.println("@DecimalMax    - 小数最大值");
        System.out.println();

        System.out.println("【时间约束】");
        System.out.println("@Past          - 过去时间");
        System.out.println("@Future        - 将来时间");
        System.out.println("@PastOrPresent - 过去或现在");
        System.out.println("@FutureOrPresent - 将来或现在");
        System.out.println();

        // 测试Product对象
        Product product = new Product();
        product.name = ""; // 违反@NotBlank
        product.price = -10.0; // 违反@Positive
        product.stock = 1000; // 违反@Max(100)

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        System.out.println("产品验证错误 (" + violations.size() + "个):");
        violations.forEach(v ->
            System.out.println("  • " + v.getPropertyPath() + ": " + v.getMessage())
        );
        System.out.println();
    }

    /**
     * 3. 自定义错误消息
     */
    private static void demonstrateCustomMessages() {
        System.out.println("3. 自定义错误消息\n");

        Employee employee = new Employee();
        employee.name = "A"; // 太短
        employee.salary = 500.0; // 太低
        employee.email = "not-an-email";

        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);

        System.out.println("员工信息验证失败:");
        violations.forEach(v ->
            System.out.println("  ✗ " + v.getMessage())
        );
        System.out.println();
    }

    /**
     * 4. 嵌套对象验证（级联验证）
     */
    private static void demonstrateNestedValidation() {
        System.out.println("4. 嵌套对象验证\n");

        Address address = new Address();
        address.street = ""; // 无效
        address.city = null; // 无效
        address.zipCode = "123"; // 无效格式

        Customer customer = new Customer();
        customer.name = "王五";
        customer.email = "wangwu@example.com";
        customer.address = address; // 嵌套对象有错误

        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        System.out.println("客户信息验证（包含地址）:");
        if (violations.isEmpty()) {
            System.out.println("✓ 全部通过");
        } else {
            System.out.println("✗ 发现错误:");
            violations.forEach(v ->
                System.out.println("  • " + v.getPropertyPath() + ": " + v.getMessage())
            );
        }
        System.out.println();
    }

    /**
     * 5. 验证组（Validation Groups）
     */
    private static void demonstrateValidationGroups() {
        System.out.println("5. 验证组\n");

        System.out.println("验证组的作用：");
        System.out.println("• 不同场景使用不同验证规则");
        System.out.println("• 例如：创建用户时需要密码，更新用户时不需要");
        System.out.println("• 使用接口标记验证组");
        System.out.println();

        OrderRequest request = new OrderRequest();
        request.orderId = null; // Create时不需要
        request.productName = "商品A";
        request.quantity = 5;

        // 只验证Create组
        System.out.println("验证Create组（orderId可以为空）:");
        Set<ConstraintViolation<OrderRequest>> createViolations =
            validator.validate(request, ValidationGroups.Create.class);
        System.out.println(createViolations.isEmpty() ? "✓ 通过" : "✗ 失败");
        System.out.println();

        // 验证Update组
        System.out.println("验证Update组（orderId不能为空）:");
        Set<ConstraintViolation<OrderRequest>> updateViolations =
            validator.validate(request, ValidationGroups.Update.class);
        if (!updateViolations.isEmpty()) {
            updateViolations.forEach(v ->
                System.out.println("  • " + v.getPropertyPath() + ": " + v.getMessage())
            );
        }
        System.out.println();
    }

    /**
     * 6. 方法参数验证
     */
    private static void demonstrateMethodValidation() {
        System.out.println("6. 方法参数验证\n");

        System.out.println("方法级别验证：");
        System.out.println("• 验证方法参数");
        System.out.println("• 验证方法返回值");
        System.out.println("• 需要使用ExecutableValidator");
        System.out.println();

        System.out.println("Spring Boot中自动启用：");
        System.out.println("• 在Controller方法参数上使用@Valid");
        System.out.println("• 在Service类上使用@Validated");
        System.out.println("• 配合@MethodValidationPostProcessor");
        System.out.println();
    }

    /**
     * 7. 实际应用示例
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("7. 实际应用示例\n");

        System.out.println("【示例1】REST API请求验证:");
        System.out.println("""
                @PostMapping("/users")
                public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
                    // @Valid自动触发验证
                    // 验证失败会抛出MethodArgumentNotValidException
                    return ResponseEntity.ok(userService.create(request));
                }
                """);

        System.out.println("【示例2】表单提交验证:");
        System.out.println("""
                @PostMapping("/register")
                public String register(@Valid RegisterForm form, BindingResult result) {
                    if (result.hasErrors()) {
                        // 处理验证错误
                        return "register";
                    }
                    return "success";
                }
                """);

        System.out.println("【示例3】统一异常处理:");
        System.out.println("""
                @ExceptionHandler(MethodArgumentNotValidException.class)
                public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex) {
                    Map<String, String> errors = new HashMap<>();
                    ex.getBindingResult().getFieldErrors().forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                    );
                    return ResponseEntity.badRequest().body(new ErrorResponse(errors));
                }
                """);

        System.out.println("\nSpring Boot优势：");
        System.out.println("✓ 无需手动调用validator.validate()");
        System.out.println("✓ 自动绑定验证错误到BindingResult");
        System.out.println("✓ 统一的异常处理机制");
        System.out.println("✓ 支持国际化错误消息");
        System.out.println();
    }

    // ==================== 数据类定义 ====================

    /**
     * 用户类 - 基础验证示例
     */
    static class User {
        @NotBlank(message = "姓名不能为空")
        @Size(min = 2, max = 50, message = "姓名长度必须在2-50之间")
        private String name;

        @NotNull(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        private String email;

        @Min(value = 18, message = "年龄必须大于等于18")
        @Max(value = 100, message = "年龄必须小于等于100")
        private Integer age;

        public User(String name, String email, Integer age) {
            this.name = name;
            this.email = email;
            this.age = age;
        }
    }

    /**
     * 产品类 - 常用约束示例
     */
    static class Product {
        @NotBlank
        private String name;

        @NotNull
        @Positive
        private Double price;

        @Min(0)
        @Max(100)
        private Integer stock;

        @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "产品编号格式错误")
        private String productCode;
    }

    /**
     * 员工类 - 自定义消息示例
     */
    static class Employee {
        @NotBlank(message = "员工姓名不能为空")
        @Size(min = 2, max = 30, message = "员工姓名长度必须在2-30个字符之间")
        private String name;

        @DecimalMin(value = "3000.0", message = "薪资不能低于3000元")
        @DecimalMax(value = "100000.0", message = "薪资不能超过100000元")
        private Double salary;

        @Email(message = "请输入有效的邮箱地址")
        private String email;

        @Past(message = "入职日期必须是过去的时间")
        private LocalDate hireDate;
    }

    /**
     * 地址类 - 嵌套验证示例
     */
    static class Address {
        @NotBlank
        private String street;

        @NotBlank
        private String city;

        @Pattern(regexp = "\\d{6}", message = "邮编必须是6位数字")
        private String zipCode;
    }

    /**
     * 客户类 - 使用@Valid级联验证
     */
    static class Customer {
        @NotBlank
        private String name;

        @Email
        private String email;

        @Valid  // 级联验证嵌套对象
        @NotNull
        private Address address;
    }

    /**
     * 订单请求 - 验证组示例
     */
    static class OrderRequest {
        @NotNull(groups = ValidationGroups.Update.class, message = "更新时订单ID不能为空")
        private String orderId;

        @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
        private String productName;

        @Min(value = 1, groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
        private Integer quantity;
    }

    /**
     * 验证组接口定义
     */
    interface ValidationGroups {
        interface Create {}
        interface Update {}
    }
}
