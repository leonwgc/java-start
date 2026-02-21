package advanced;

import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * Java注解学习
 * 学习目标：
 * 1. 理解注解的概念
 * 2. 学习自定义注解
 * 3. 掌握元注解
 * 4. 理解注解在Spring中的应用
 */
public class AnnotationDemo {
    public static void main(String[] args) {
        System.out.println("=== Java注解学习 ===\n");

        // 示例1：使用自定义注解
        demonstrateCustomAnnotation();

        // 示例2：通过反射读取注解
        demonstrateReflection();

        // 示例3：模拟Spring风格的注解
        demonstrateSpringStyleAnnotation();
    }

    // 使用自定义注解
    public static void demonstrateCustomAnnotation() {
        System.out.println("1. 自定义注解示例\n");

        AnnotatedUserService userService = new AnnotatedUserService();
        userService.getUser();
        userService.saveUser();
        userService.deleteUser();
        System.out.println();
    }

    // 通过反射读取注解
    public static void demonstrateReflection() {
        System.out.println("2. 通过反射读取注解信息\n");

        Class<AnnotatedUserService> clazz = AnnotatedUserService.class;

        // 读取类上的注解
        if (clazz.isAnnotationPresent(Service.class)) {
            Service service = clazz.getAnnotation(Service.class);
            System.out.println("类注解 @Service:");
            System.out.println("  name: " + service.name());
            System.out.println("  description: " + service.description());
        }

        // 读取方法上的注解
        System.out.println("\n方法注解:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Permission.class)) {
                Permission permission = method.getAnnotation(Permission.class);
                System.out.println("\n方法: " + method.getName());
                System.out.println("  角色: " + permission.role());
                System.out.println("  描述: " + permission.description());
            }
        }
        System.out.println();
    }

    // 模拟Spring风格的注解
    public static void demonstrateSpringStyleAnnotation() {
        System.out.println("3. Spring风格注解示例\n");

        // 扫描带有@Component注解的类
        System.out.println("扫描组件:");
        ProductService productService = new ProductService();
        Class<?> clazz = productService.getClass();

        if (clazz.isAnnotationPresent(Component.class)) {
            Component component = clazz.getAnnotation(Component.class);
            System.out.println("发现组件: " + component.value());
        }

        // 检查@Autowired字段
        System.out.println("\n检查@Autowired字段:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                System.out.println("需要注入的字段: " + field.getName() +
                                 " (" + field.getType().getSimpleName() + ")");
            }
        }

        System.out.println("\n这些注解概念在Spring框架中被广泛使用！");
    }
}

// ========== 自定义注解定义 ==========

/**
 * 元注解说明：
 * @Retention: 注解保留策略
 *   - SOURCE: 仅在源码中保留，编译时丢弃
 *   - CLASS: 编译到class文件，运行时丢弃
 *   - RUNTIME: 运行时保留，可通过反射读取
 *
 * @Target: 注解可以用在哪里
 *   - TYPE: 类、接口、枚举
 *   - FIELD: 字段
 *   - METHOD: 方法
 *   - PARAMETER: 参数
 *   - CONSTRUCTOR: 构造器
 */

// 服务注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Service {
    String name() default "";
    String description() default "";
}

// 权限注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Permission {
    String role();
    String description() default "";
}

// 模拟Spring的@Component注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Component {
    String value() default "";
}

// 模拟Spring的@Autowired注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Autowired {
}

// ========== 使用注解的类 ==========

@Service(name = "userService", description = "用户服务类")
class AnnotatedUserService {

    @Permission(role = "USER", description = "所有用户可访问")
    public void getUser() {
        System.out.println("获取用户信息");
    }

    @Permission(role = "ADMIN", description = "仅管理员可访问")
    public void saveUser() {
        System.out.println("保存用户信息");
    }

    @Permission(role = "ADMIN", description = "仅管理员可访问")
    public void deleteUser() {
        System.out.println("删除用户信息");
    }
}

@Component("productService")
class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public void listProducts() {
        System.out.println("列出所有商品");
    }
}

class ProductRepository {
    public void findAll() {
        System.out.println("从数据库查询商品");
    }
}
