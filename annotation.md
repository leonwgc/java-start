# Java 注解（Annotation）详解

## 📚 什么是注解？

**注解（Annotation）** 是Java 5引入的特性，是一种**元数据**（metadata），为代码提供额外的信息，但不直接影响代码的执行。

### 核心理解
- **元数据**：描述数据的数据，注解是描述代码的代码
- **声明式编程**：用注解表达意图，而不是写大量配置代码
- **编译时/运行时处理**：可以在编译期或运行时通过反射读取
- **非侵入式**：不改变代码逻辑，只添加标记信息

---

## 🎯 注解的作用

### 1. **提供信息给编译器**
```java
@Override          // 检查是否正确重写父类方法
@SuppressWarnings  // 抑制编译警告
@Deprecated        // 标记过时的方法
```

### 2. **编译时处理**
```java
@Entity            // Hibernate用于生成SQL
@Getter/@Setter    // Lombok在编译时生成代码
```

### 3. **运行时处理**
```java
@Service           // Spring在运行时扫描并创建Bean
@Autowired         // Spring在运行时注入依赖
@RequestMapping    // Spring MVC在运行时映射URL
```

---

## 🔤 Java内置注解

### 基础注解

**@Override - 检查方法重写**
```java
class Parent {
    public void show() { }
}

class Child extends Parent {
    @Override
    public void show() {  // ✅ 正确重写
        // ...
    }

    // @Override
    // public void shwo() {  // ❌ 编译错误：拼写错误
    // }
}
```

**@Deprecated - 标记过时**
```java
@Deprecated
public void oldMethod() {
    // 这个方法已过时，不建议使用
}

// 调用时会有警告
oldMethod();  // ⚠️ 'oldMethod()' is deprecated
```

**@SuppressWarnings - 抑制警告**
```java
@SuppressWarnings("unchecked")
List<String> list = (List<String>) rawList;

@SuppressWarnings({"unchecked", "deprecation"})
public void someMethod() {
    // 抑制多个警告
}
```

**@FunctionalInterface - 标记函数式接口**
```java
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);  // 只能有一个抽象方法
}
```

---

## 🏗️ 元注解（Meta-Annotation）

用于定义注解的注解

### 1. **@Retention** - 保留策略

决定注解保留到哪个阶段

```java
public enum RetentionPolicy {
    SOURCE,     // 源码中保留，编译时丢弃（如@Override）
    CLASS,      // 编译到.class文件，运行时丢弃（默认值）
    RUNTIME     // 运行时保留，可通过反射读取（如@Service）
}
```

```java
@Retention(RetentionPolicy.SOURCE)   // 仅源码
@interface MyAnnotation1 { }

@Retention(RetentionPolicy.CLASS)    // 编译时
@interface MyAnnotation2 { }

@Retention(RetentionPolicy.RUNTIME)  // 运行时
@interface MyAnnotation3 { }
```

### 2. **@Target** - 使用目标

决定注解可以用在哪里

```java
public enum ElementType {
    TYPE,            // 类、接口、枚举
    FIELD,           // 字段
    METHOD,          // 方法
    PARAMETER,       // 参数
    CONSTRUCTOR,     // 构造器
    LOCAL_VARIABLE,  // 局部变量
    ANNOTATION_TYPE, // 注解
    PACKAGE,         // 包
    TYPE_PARAMETER,  // 类型参数（泛型）
    TYPE_USE         // 任何类型使用
}
```

```java
@Target(ElementType.METHOD)
@interface MethodOnly { }  // 只能用于方法

@Target({ElementType.TYPE, ElementType.METHOD})
@interface TypeOrMethod { }  // 可用于类或方法
```

### 3. **@Documented** - 生成文档

```java
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface ApiDoc {
    String value();
}
// 使用javadoc生成文档时，会包含此注解信息
```

### 4. **@Inherited** - 继承性

```java
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Service { }

@Service
class ParentService { }

// 子类自动继承@Service注解
class ChildService extends ParentService { }
```

---

## 🔧 自定义注解

### 定义注解

```java
// 基本格式
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    // 注解属性（类似方法）
    String value();               // 必填属性
    String name() default "";     // 有默认值的属性
    int age() default 0;
    String[] tags() default {};   // 数组类型
}
```

### 使用自定义注解

```java
// value是特殊属性，单独使用时可以省略名称
@MyAnnotation("快捷方式")
public void method1() { }

// 完整写法
@MyAnnotation(value = "测试", name = "张三", age = 25, tags = {"tag1", "tag2"})
public void method2() { }

// 只有默认值的属性可以不写
@MyAnnotation(value = "测试")
public void method3() { }
```

---

## 🔍 通过反射读取注解

### 检查是否存在注解

```java
Class<?> clazz = UserService.class;

// 检查类上是否有@Service注解
if (clazz.isAnnotationPresent(Service.class)) {
    System.out.println("这是一个Service类");
}
```

### 读取注解信息

```java
@Service(name = "userService", description = "用户服务")
class UserService {
    @Permission(role = "ADMIN", description = "管理员权限")
    public void deleteUser() { }
}

// 读取类注解
Class<?> clazz = UserService.class;
Service service = clazz.getAnnotation(Service.class);
System.out.println("服务名: " + service.name());
System.out.println("描述: " + service.description());

// 读取方法注解
Method method = clazz.getMethod("deleteUser");
Permission permission = method.getAnnotation(Permission.class);
System.out.println("需要角色: " + permission.role());
```

### 遍历所有注解

```java
// 获取类上的所有注解
Annotation[] annotations = clazz.getAnnotations();
for (Annotation ann : annotations) {
    System.out.println(ann.annotationType().getName());
}

// 获取所有方法的注解
Method[] methods = clazz.getDeclaredMethods();
for (Method method : methods) {
    if (method.isAnnotationPresent(Permission.class)) {
        Permission perm = method.getAnnotation(Permission.class);
        System.out.println(method.getName() + " 需要 " + perm.role() + " 权限");
    }
}
```

---

## 🌟 实战示例

### 示例1：服务注解

```java
// 定义注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Service {
    String name() default "";
    String description() default "";
}

// 使用注解
@Service(name = "userService", description = "用户服务类")
class UserService {
    public void getUser() {
        System.out.println("获取用户");
    }
}

// 读取注解
Class<?> clazz = UserService.class;
if (clazz.isAnnotationPresent(Service.class)) {
    Service service = clazz.getAnnotation(Service.class);
    System.out.println("发现服务: " + service.name());
    System.out.println("说明: " + service.description());
}
```

### 示例2：权限注解

```java
// 定义注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Permission {
    String role();
    String description() default "";
}

// 使用注解
class UserService {
    @Permission(role = "USER")
    public void getUser() { }

    @Permission(role = "ADMIN", description = "仅管理员")
    public void deleteUser() { }
}

// 权限检查（AOP风格）
public void checkPermission(Method method, String userRole) {
    if (method.isAnnotationPresent(Permission.class)) {
        Permission perm = method.getAnnotation(Permission.class);
        if (!userRole.equals(perm.role())) {
            throw new SecurityException("权限不足！需要: " + perm.role());
        }
    }
}
```

### 示例3：模拟Spring注解

```java
// @Component - 组件注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface Component {
    String value() default "";
}

// @Autowired - 自动注入
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Autowired { }

// 使用
@Component("productService")
class ProductService {
    @Autowired
    private ProductRepository repository;

    public void listProducts() {
        repository.findAll();
    }
}

// 简化的容器扫描
public void scanComponents() {
    Class<?> clazz = ProductService.class;

    // 检查是否是组件
    if (clazz.isAnnotationPresent(Component.class)) {
        Component comp = clazz.getAnnotation(Component.class);
        System.out.println("发现组件: " + comp.value());

        // 检查需要注入的字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                System.out.println("需要注入: " + field.getName());
            }
        }
    }
}
```

---

## 🔥 Spring常用注解

### 核心注解

```java
// 1. 组件注解（Bean定义）
@Component      // 通用组件
@Service        // 服务层
@Repository     // 数据访问层
@Controller     // 控制层
@RestController // REST控制层（@Controller + @ResponseBody）

// 2. 依赖注入
@Autowired      // 自动注入（按类型）
@Qualifier      // 指定注入Bean的名称
@Resource       // 按名称注入（JSR-250）
@Value          // 注入配置值

// 3. 配置注解
@Configuration  // 配置类
@Bean           // 定义Bean
@ComponentScan  // 组件扫描
@PropertySource // 属性文件

// 4. Web注解
@RequestMapping // URL映射
@GetMapping     // GET请求
@PostMapping    // POST请求
@PutMapping     // PUT请求
@DeleteMapping  // DELETE请求
@PathVariable   // 路径变量
@RequestParam   // 请求参数
@RequestBody    // 请求体
@ResponseBody   // 响应体
```

### Spring Boot注解

```java
@SpringBootApplication  // 启动类（包含@Configuration, @EnableAutoConfiguration, @ComponentScan）
@EnableAutoConfiguration // 自动配置
@Transactional          // 事务管理
@Async                  // 异步执行
@Scheduled              // 定时任务
@Cacheable              // 缓存
@ConditionalOnProperty  // 条件配置
```

### 完整Spring示例

```java
// 控制层
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
}

// 服务层
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

// 数据访问层
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
}
```

---

## ⚠️ 注解最佳实践

### 1. 选择正确的保留策略

```java
// 编译时检查 → SOURCE
@Retention(RetentionPolicy.SOURCE)
@interface Override { }

// 编译时处理 → CLASS
@Retention(RetentionPolicy.CLASS)
@interface Getter { }

// 运行时处理 → RUNTIME
@Retention(RetentionPolicy.RUNTIME)
@interface Service { }
```

### 2. 明确目标元素

```java
// 只用于方法
@Target(ElementType.METHOD)
@interface Transaction { }

// 可用于多个地方
@Target({ElementType.TYPE, ElementType.METHOD})
@interface Security { }
```

### 3. 提供默认值

```java
@interface Config {
    String name();                  // 必填
    String description() default ""; // 可选
    int timeout() default 30;        // 可选
}
```

### 4. 使用value作为主属性

```java
@interface Path {
    String value();  // 主属性
}

// 简洁用法
@Path("/users")
public void getUsers() { }

// 等价于
@Path(value = "/users")
public void getUsers() { }
```

### 5. 检查注解存在性

```java
// ❌ 不安全
Permission perm = method.getAnnotation(Permission.class);
String role = perm.role();  // 可能NullPointerException

// ✅ 安全
if (method.isAnnotationPresent(Permission.class)) {
    Permission perm = method.getAnnotation(Permission.class);
    String role = perm.role();
}
```

---

## 🎓 注解处理流程

### 编译时处理（如Lombok）

```
源码 → 注解处理器 → 生成代码 → 编译
```

```java
@Getter
@Setter
public class User {
    private String name;
}

// Lombok在编译时生成：
public class User {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

### 运行时处理（如Spring）

```
启动 → 扫描类 → 读取注解 → 创建Bean → 注入依赖
```

```java
@Service
public class UserService {
    @Autowired
    private UserRepository repository;
}

// Spring启动时：
// 1. 扫描到@Service注解
// 2. 创建UserService实例
// 3. 发现@Autowired注解
// 4. 查找UserRepository Bean并注入
```

---

## 💡 注解 vs 配置文件

| 特性 | 注解 | XML配置 |
|------|------|---------|
| 代码量 | 少 | 多 |
| 可读性 | 高（代码就是配置） | 低（需要跳转查看） |
| 灵活性 | 低（修改需要重新编译） | 高（可动态修改） |
| 类型安全 | 强（编译检查） | 弱（运行时解析） |
| 维护成本 | 低 | 高 |
| 现代趋势 | ✅ 主流（Spring Boot） | ❌ 逐渐被取代 |

---

## 🔍 常见错误

### ❌ 错误1：忘记设置RUNTIME

```java
@Retention(RetentionPolicy.CLASS)  // ❌ 运行时无法读取
@interface MyAnnotation { }
```

✅ **正确做法**：
```java
@Retention(RetentionPolicy.RUNTIME)  // ✅ 运行时可读
@interface MyAnnotation { }
```

### ❌ 错误2：注解用错位置

```java
@Target(ElementType.METHOD)
@interface MethodOnly { }

@MethodOnly  // ❌ 编译错误！只能用于方法
class MyClass { }
```

✅ **正确做法**：
```java
class MyClass {
    @MethodOnly  // ✅ 用在方法上
    public void method() { }
}
```

### ❌ 错误3：未检查注解存在性

```java
Permission perm = method.getAnnotation(Permission.class);
String role = perm.role();  // ❌ 可能NullPointerException
```

✅ **正确做法**：
```java
if (method.isAnnotationPresent(Permission.class)) {
    Permission perm = method.getAnnotation(Permission.class);
    String role = perm.role();  // ✅ 安全
}
```

### ❌ 错误4：混淆注解和注释

```java
// 这是注释（Comment）- 给人看的
/* 多行注释 */

// 这是注解（Annotation）- 给程序看的
@Override
@Service
```

---

## 💡 总结

### 核心要点
1. **注解是元数据**：描述代码的代码，不影响执行
2. **三种保留策略**：SOURCE（编译检查）、CLASS（编译时处理）、RUNTIME（运行时处理）
3. **通过反射读取**：运行时处理注解需要RUNTIME策略
4. **Spring大量使用**：@Service、@Autowired等都是注解

### 元注解记忆
- **@Retention**：保留到哪（SOURCE/CLASS/RUNTIME）
- **@Target**：用在哪（TYPE/FIELD/METHOD等）
- **@Documented**：生成javadoc文档
- **@Inherited**：子类可继承

### 注解使用场景

| 场景 | 示例 | 保留策略 |
|------|------|----------|
| 编译检查 | @Override, @Deprecated | SOURCE |
| 代码生成 | @Getter, @Setter (Lombok) | CLASS |
| 依赖注入 | @Autowired, @Component (Spring) | RUNTIME |
| URL映射 | @RequestMapping (Spring MVC) | RUNTIME |
| 权限控制 | @PreAuthorize (Spring Security) | RUNTIME |
| 事务管理 | @Transactional | RUNTIME |

### 为什么Spring大量使用注解？

✅ **简化配置**：无需XML，代码即配置
✅ **类型安全**：编译时检查
✅ **易于维护**：代码和配置在一起
✅ **提高开发效率**：注解驱动开发
✅ **自动化处理**：框架自动扫描和处理

### 学习路径
1. ✅ 掌握内置注解：@Override、@Deprecated
2. ✅ 理解元注解：@Retention、@Target
3. ✅ 学会自定义注解
4. ✅ 通过反射读取注解
5. ⏭️ Spring注解体系
6. ⏭️ 注解驱动开发

**注解是现代Java开发的基础，Spring框架的核心就是基于注解的依赖注入和AOP！** 🎯
