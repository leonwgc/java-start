# Spring @Service 注解说明

## 1. 核心概念

`@Service` 是 Spring Framework 的组件注解之一，用于标记**业务逻辑层（Service Layer）**的类。

```java
@Slf4j
@Service  // ← 告诉 Spring：这是一个服务类，请自动创建 Bean
class UserService {
    // Spring 会自动扫描并创建 UserService 的实例
    // 其他类可以通过依赖注入使用这个 Bean
}
```

## 2. 主要作用

### 2.1 自动组件扫描

Spring 容器启动时会自动扫描带有 `@Service` 注解的类，并将其注册为 Bean。

```java
// 启动类通常包含 @SpringBootApplication 注解
@SpringBootApplication  // 包含了 @ComponentScan
public class CacheApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }
}

// UserService 会被自动发现并注册
@Service
class UserService { }
```

### 2.2 依赖注入

其他组件可以通过构造器、字段或方法注入使用 `@Service` 标记的类。

```java
@RestController
class CacheController {
    private final UserService userService;
    private final ProductService productService;

    // 构造器注入（推荐方式）
    public CacheController(UserService userService,
                          ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }
}
```

### 2.3 启用 AOP 功能

被 `@Service` 标记的类可以使用 Spring 的 AOP 功能，如缓存、事务、日志等。

```java
@Service
class UserService {

    // ✅ 缓存注解在 @Service Bean 上生效
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        // Spring AOP 会在方法执行前检查缓存
        return userDatabase.get(id);
    }

    // ✅ 事务注解在 @Service Bean 上生效
    @Transactional
    public void updateUser(User user) {
        // Spring 会管理事务的提交和回滚
    }
}
```

### 2.4 语义化标识

`@Service` 比通用的 `@Component` 更清晰地表达了类的职责：业务逻辑处理。

## 3. Spring 组件注解体系

| 注解 | 层级 | 用途 | 示例 |
|------|------|------|------|
| **@Component** | 通用层 | 通用组件，其他注解的基础 | 工具类、配置类 |
| **@Service** | 业务层 | 业务逻辑处理 | UserService、OrderService |
| **@Repository** | 持久层 | 数据访问（DAO） | UserRepository、OrderDao |
| **@Controller** | 控制层 | MVC 控制器 | UserController |
| **@RestController** | 控制层 | RESTful API 控制器 | UserRestController |

### 3.1 注解关系图

```
@Component (父注解)
    ├── @Service       (业务逻辑层)
    ├── @Repository    (数据访问层)
    └── @Controller    (表示层)
            └── @RestController (= @Controller + @ResponseBody)
```

### 3.2 功能对比

**本质**：`@Service`、`@Repository`、`@Controller` 都是 `@Component` 的特化版本。

```java
// 查看源码可以看到：
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component  // ← 继承自 @Component
public @interface Service {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
```

**区别**：
- 功能上完全相同，都是让 Spring 自动扫描并创建 Bean
- 语义上不同，表达不同的架构层级
- `@Repository` 额外提供了数据访问异常转换功能

## 4. @Service vs @Bean

| 特性 | @Service | @Bean |
|------|----------|-------|
| **使用位置** | 类上 | 方法上 |
| **创建方式** | Spring 自动扫描 | 手动在配置类中定义 |
| **适用场景** | 自己编写的业务类 | 第三方库类或需要复杂初始化 |
| **灵活性** | 低（只能使用默认构造器） | 高（可以自定义创建逻辑） |
| **代码量** | 少（一行注解） | 多（需要写配置方法） |

### 4.1 使用场景对比

```java
// ✅ 自己编写的业务类 → 用 @Service
@Service
public class UserService {
    // Spring 自动创建实例
    // 使用默认构造器或带参数的构造器（参数会自动注入）
}

// ✅ 第三方库类或需要复杂配置 → 用 @Bean
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // 手动创建和配置第三方类
        ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager();
        manager.setCacheNames(Arrays.asList("users", "products"));
        return manager;
    }
}
```

## 5. 实际应用示例

### 5.1 基本使用（CacheApplication.java）

```java
/**
 * 用户服务 - 演示缓存操作
 */
@Slf4j
@Service  // ← Spring 自动创建 Bean
class UserService {

    // 模拟数据库
    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();

    /**
     * 查询用户（带缓存）
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        log.info("从数据库查询用户：{}", id);
        simulateSlowService();  // 模拟延迟
        return userDatabase.get(id);
    }

    /**
     * 更新用户（更新缓存）
     */
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        log.info("更新用户：{}", user.getId());
        userDatabase.put(user.getId(), user);
        return user;
    }
}
```

### 5.2 依赖注入使用

```java
/**
 * REST 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
class CacheController {

    // 1. 声明依赖
    private final UserService userService;
    private final ProductService productService;
    private final StatisticsService statisticsService;

    // 2. 构造器注入（推荐方式）
    public CacheController(UserService userService,
                          ProductService productService,
                          StatisticsService statisticsService) {
        this.userService = userService;
        this.productService = productService;
        this.statisticsService = statisticsService;
    }

    // 3. 使用注入的服务
    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);  // ← 调用服务方法
        return Map.of("user", user);
    }
}
```

### 5.3 多个 Service 协作

```java
@Service
class OrderService {

    private final UserService userService;
    private final ProductService productService;

    // Service 之间也可以相互注入
    public OrderService(UserService userService,
                       ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    public Order createOrder(Long userId, Long productId) {
        // 调用其他服务
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);

        // 订单创建逻辑
        return new Order(user, product);
    }
}
```

## 6. Bean 的生命周期和作用域

### 6.1 默认作用域：单例（Singleton）

```java
@Service  // 默认是单例
class UserService {
    // Spring 容器中只有一个 UserService 实例
    // 所有需要注入 UserService 的地方都使用同一个实例
}
```

### 6.2 指定其他作用域

```java
@Service
@Scope("prototype")  // 每次注入都创建新实例
class PrototypeService {
    // 每次请求这个 Bean 时都会创建新实例
}

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class RequestScopedService {
    // 每个 HTTP 请求创建一个实例
}
```

### 6.3 生命周期回调

```java
@Service
class UserService {

    // Bean 初始化后执行
    @PostConstruct
    public void init() {
        log.info("UserService 初始化完成");
        // 初始化资源、加载缓存等
    }

    // Bean 销毁前执行
    @PreDestroy
    public void cleanup() {
        log.info("UserService 即将销毁");
        // 释放资源、关闭连接等
    }
}
```

## 7. 依赖注入的三种方式

### 7.1 构造器注入（推荐 ✅）

```java
@RestController
class UserController {
    private final UserService userService;  // final 保证不可变

    // 构造器注入
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
```

**优点**：
- 保证依赖不可变（final）
- 强制依赖必须提供
- 更容易进行单元测试
- Spring 4.3+ 单构造器可省略 `@Autowired`

### 7.2 字段注入（不推荐 ❌）

```java
@RestController
class UserController {
    @Autowired  // 通过反射直接注入字段
    private UserService userService;
}
```

**缺点**：
- 无法声明为 final
- 难以进行单元测试
- 隐藏了依赖关系
- 容易导致循环依赖

### 7.3 Setter 注入（可选依赖场景）

```java
@RestController
class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
```

**适用场景**：可选依赖、需要重新配置的依赖。

## 8. 常见问题与注意事项

### 8.1 同类内部调用不会触发 AOP

```java
@Service
class UserService {

    @Cacheable("users")
    public User getUserById(Long id) {
        return userDatabase.get(id);
    }

    // ❌ 错误！调用同类方法，缓存不生效
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (Long id : userIds) {
            users.add(this.getUserById(id));  // 直接调用，bypass 了 AOP 代理
        }
        return users;
    }

    // ✅ 正确！从外部调用，缓存生效
    // Controller 调用 getUserById() 会触发缓存
}
```

**原因**：Spring AOP 基于代理模式，同类内部调用不经过代理对象。

**解决方案**：
1. 将方法提取到另一个 Service
2. 自己注入自己（通过 ApplicationContext 获取代理对象）
3. 使用 AspectJ 编译时织入

### 8.2 循环依赖

```java
// ❌ 错误！循环依赖
@Service
class ServiceA {
    private final ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}

@Service
class ServiceB {
    private final ServiceA serviceA;

    public ServiceB(ServiceA serviceA) {  // 循环依赖
        this.serviceA = serviceA;
    }
}
```

**解决方案**：
1. 重新设计，避免循环依赖
2. 使用 `@Lazy` 延迟加载
3. 使用 Setter 注入（不推荐）

### 8.3 Bean 命名冲突

```java
// 情况1：同一类有多个 @Service（错误！）
@Service
class UserService { }

@Service  // ❌ 错误！会冲突
class UserService { }

// 情况2：不同类但想要同名 Bean
@Service("userService")  // 指定 Bean 名称
class UserServiceImpl { }

@Service("userServiceV2")  // 不同名称
class UserServiceV2 { }
```

**注入指定名称的 Bean**：
```java
@RestController
class UserController {
    private final UserService userService;

    public UserController(@Qualifier("userServiceV2") UserService userService) {
        this.userService = userService;
    }
}
```

### 8.4 条件化创建 Bean

```java
@Service
@ConditionalOnProperty(name = "feature.user.enabled", havingValue = "true")
class UserService {
    // 只有配置 feature.user.enabled=true 时才创建此 Bean
}

@Service
@Profile("dev")  // 仅在 dev 环境创建
class MockUserService { }

@Service
@Profile("prod")  // 仅在 prod 环境创建
class RealUserService { }
```

## 9. 最佳实践

### ✅ 推荐做法

1. **使用构造器注入**
```java
@Service
class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

2. **保持单一职责**
```java
// ✅ 好：职责单一
@Service
class UserRegistrationService {
    public void register(User user) { }
}

@Service
class UserAuthenticationService {
    public void authenticate(String username, String password) { }
}
```

3. **业务逻辑放在 Service 层**
```java
@Service
class OrderService {

    // ✅ 业务逻辑在 Service
    public Order createOrder(OrderRequest request) {
        // 验证库存
        validateStock(request);
        // 计算价格
        BigDecimal totalPrice = calculatePrice(request);
        // 创建订单
        return saveOrder(request, totalPrice);
    }
}
```

4. **使用接口隔离**
```java
public interface UserService {
    User getUserById(Long id);
    void updateUser(User user);
}

@Service
class UserServiceImpl implements UserService {
    // 实现接口
}
```

### ❌ 避免做法

1. **不要在 Service 中处理 HTTP 细节**
```java
// ❌ 错误
@Service
class UserService {
    public ResponseEntity<User> getUser(HttpServletRequest request) {
        // Service 不应处理 HTTP
    }
}

// ✅ 正确
@Service
class UserService {
    public User getUser(Long id) {
        // 只处理业务逻辑
    }
}
```

2. **不要使用字段注入**
```java
// ❌ 不推荐
@Service
class UserService {
    @Autowired
    private UserRepository repository;
}

// ✅ 推荐
@Service
class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

3. **不要创建过于庞大的 Service**
```java
// ❌ 错误：God Service
@Service
class UserService {
    public void register() { }
    public void login() { }
    public void logout() { }
    public void resetPassword() { }
    public void sendEmail() { }
    public void uploadAvatar() { }
    // ... 500 行代码
}

// ✅ 正确：拆分职责
@Service
class UserRegistrationService { }

@Service
class UserAuthenticationService { }

@Service
class UserProfileService { }
```

## 10. 总结

| 特性 | 说明 |
|------|------|
| **作用** | 标记业务逻辑层类，让 Spring 自动创建 Bean |
| **位置** | 类上 |
| **继承** | 继承自 `@Component` |
| **作用域** | 默认单例（Singleton） |
| **依赖注入** | 支持构造器、字段、Setter 注入 |
| **AOP 支持** | 支持缓存、事务、日志等 AOP 功能 |
| **适用场景** | 自己编写的业务逻辑类 |

**核心价值**：
1. 自动化 Bean 管理，无需手动创建对象
2. 支持依赖注入，降低组件耦合
3. 启用 AOP 功能，实现横切关注点
4. 语义化标识，代码结构更清晰

**与 `@Bean` 的区别**：
- `@Service` 用于自己的类（类上注解，自动扫描）
- `@Bean` 用于第三方类（方法上注解，手动配置）

通过合理使用 `@Service` 注解，可以构建出结构清晰、易于维护的 Spring 应用！
