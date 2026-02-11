package advanced;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * IoC控制反转和依赖注入学习
 * 学习目标：
 * 1. 理解IoC（控制反转）的概念
 * 2. 理解DI（依赖注入）的实现
 * 3. 模拟Spring IoC容器的工作原理
 * 4. 理解为什么需要IoC容器
 */
public class IoCDemo {
    public static void main(String[] args) {
        System.out.println("=== IoC控制反转和依赖注入学习 ===\n");

        // 示例1：没有IoC的传统方式
        demonstrateTraditionalWay();

        // 示例2：使用依赖注入
        demonstrateDependencyInjection();

        // 示例3：简单IoC容器实现
        demonstrateSimpleIoCContainer();

        // 示例4：实战案例
        demonstratePracticalExample();

        System.out.println("\n💡 Spring IoC容器：");
        System.out.println("- ApplicationContext是核心IoC容器");
        System.out.println("- @Component、@Service、@Repository注册Bean");
        System.out.println("- @Autowired自动注入依赖");
        System.out.println("- Bean的生命周期由容器管理");
        System.out.println("- 解耦合，易测试，易维护\n");
    }

    // 传统方式（紧耦合）
    public static void demonstrateTraditionalWay() {
        System.out.println("1. 传统方式（紧耦合）\n");
        System.out.println("问题：");
        System.out.println("- 类之间高度耦合");
        System.out.println("- 难以测试");
        System.out.println("- 难以替换实现\n");

        // 传统方式：UserService直接创建UserRepository
        TraditionalUserService service = new TraditionalUserService();
        service.register("张三", "zhangsan@example.com");

        System.out.println("\n❌ 缺点：UserService紧密依赖UserRepositoryImpl");
        System.out.println("   如果要切换到数据库实现，需要修改UserService代码");
        System.out.println();
    }

    // 使用依赖注入（解耦）
    public static void demonstrateDependencyInjection() {
        System.out.println("2. 使用依赖注入（解耦）\n");
        System.out.println("优势：");
        System.out.println("- 依赖通过构造器注入");
        System.out.println("- 面向接口编程");
        System.out.println("- 易于替换实现\n");

        // 依赖注入方式：外部传入依赖
        UserRepository repository = new UserRepositoryImpl();
        ModernUserService service = new ModernUserService(repository);
        service.register("李四", "lisi@example.com");

        System.out.println("\n✓ 优点：可以轻松切换实现");
        System.out.println("  例如：使用数据库实现");
        UserRepository dbRepository = new DatabaseUserRepository();
        ModernUserService dbService = new ModernUserService(dbRepository);
        dbService.register("王五", "wangwu@example.com");
        System.out.println();
    }

    // 简单IoC容器
    public static void demonstrateSimpleIoCContainer() {
        System.out.println("3. 简单IoC容器实现\n");
        System.out.println("核心功能：");
        System.out.println("- 管理对象的创建");
        System.out.println("- 自动注入依赖");
        System.out.println("- 单例管理\n");

        // 创建IoC容器
        SimpleIoCContainer container = new SimpleIoCContainer();

        // 注册Bean（模拟@Component注解）
        container.registerBean("userRepository", UserRepositoryImpl.class);
        container.registerBean("userService", ModernUserService.class);

        // 获取Bean（模拟@Autowired注入）
        ModernUserService service = container.getBean("userService", ModernUserService.class);
        service.register("赵六", "zhaoliu@example.com");

        System.out.println("\n✓ IoC容器自动管理对象和依赖");
        System.out.println("  类似Spring的@Component和@Autowired");
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("4. 实战案例 - 完整的业务场景\n");
        System.out.println("场景：用户注册系统\n");

        // 使用IoC容器
        SimpleIoCContainer container = new SimpleIoCContainer();

        // 注册所有组件
        container.registerBean("emailService", EmailService.class);
        container.registerBean("userRepository", DatabaseUserRepository.class);
        container.registerBean("userService", CompleteUserService.class);

        // 获取服务并使用
        CompleteUserService userService = container.getBean("userService", CompleteUserService.class);

        System.out.println("注册新用户:");
        boolean success = userService.registerUser("周七", "zhouqi@example.com");
        System.out.println("注册结果: " + (success ? "成功" : "失败"));

        System.out.println("\n查找用户:");
        userService.findUser("周七");

        System.out.println("\n💡 对比Spring Boot:");
        System.out.println("");
        System.out.println("@Service");
        System.out.println("public class UserService {");
        System.out.println("    @Autowired");
        System.out.println("    private UserRepository repository;");
        System.out.println("    @Autowired");
        System.out.println("    private EmailService emailService;");
        System.out.println("}");
        System.out.println("\nSpring自动完成对象创建和依赖注入！");
        System.out.println();
    }

    // ========== 传统方式（紧耦合）==========

    static class TraditionalUserService {
        // 直接创建依赖，紧耦合
        private UserRepositoryImpl repository = new UserRepositoryImpl();

        public void register(String name, String email) {
            System.out.println("传统方式注册:");
            repository.save(name, email);
        }
    }

    // ========== 现代方式（依赖注入）==========

    // 仓储接口
    interface UserRepository {
        void save(String name, String email);
        String findByName(String name);
    }

    // 内存实现
    static class UserRepositoryImpl implements UserRepository {
        private Map<String, String> users = new HashMap<>();

        @Override
        public void save(String name, String email) {
            users.put(name, email);
            System.out.println("  保存到内存: " + name + " - " + email);
        }

        @Override
        public String findByName(String name) {
            return users.get(name);
        }
    }

    // 数据库实现
    static class DatabaseUserRepository implements UserRepository {
        @Override
        public void save(String name, String email) {
            System.out.println("  保存到数据库: " + name + " - " + email);
        }

        @Override
        public String findByName(String name) {
            return "从数据库查询: " + name;
        }
    }

    // 使用依赖注入的服务类
    static class ModernUserService {
        private final UserRepository repository;

        // 构造器注入（推荐方式）
        public ModernUserService(UserRepository repository) {
            this.repository = repository;
        }

        public void register(String name, String email) {
            System.out.println("依赖注入方式注册:");
            repository.save(name, email);
        }
    }

    // ========== 简单IoC容器 ==========

    static class SimpleIoCContainer {
        // Bean定义存储
        private Map<String, Class<?>> beanDefinitions = new HashMap<>();
        // 单例Bean存储
        private Map<String, Object> singletonBeans = new HashMap<>();

        // 注册Bean定义
        public void registerBean(String name, Class<?> clazz) {
            beanDefinitions.put(name, clazz);
            System.out.println("✓ 注册Bean: " + name + " -> " + clazz.getSimpleName());
        }

        // 获取Bean实例
        @SuppressWarnings("unchecked")
        public <T> T getBean(String name, Class<T> requiredType) {
            // 先检查单例缓存
            if (singletonBeans.containsKey(name)) {
                return (T) singletonBeans.get(name);
            }

            // 创建新实例
            Class<?> clazz = beanDefinitions.get(name);
            if (clazz == null) {
                throw new RuntimeException("Bean未找到: " + name);
            }

            try {
                Object instance;

                // 尝试获取无参构造器
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                } catch (NoSuchMethodException e) {
                    // 如果没有无参构造器，尝试有参构造器
                    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                    if (constructors.length == 0) {
                        throw new RuntimeException("没有可用的构造器");
                    }

                    Constructor<?> constructor = constructors[0];
                    Class<?>[] paramTypes = constructor.getParameterTypes();
                    Object[] params = new Object[paramTypes.length];

                    for (int i = 0; i < paramTypes.length; i++) {
                        // 查找匹配类型的Bean
                        params[i] = findBeanByType(paramTypes[i]);
                    }

                    constructor.setAccessible(true);
                    instance = constructor.newInstance(params);
                }

                // 缓存单例
                singletonBeans.put(name, instance);
                return (T) instance;

            } catch (Exception e) {
                throw new RuntimeException("创建Bean失败: " + name, e);
            }
        }

        // 根据类型查找Bean
        private Object findBeanByType(Class<?> type) {
            for (Map.Entry<String, Class<?>> entry : beanDefinitions.entrySet()) {
                if (type.isAssignableFrom(entry.getValue())) {
                    return getBean(entry.getKey(), entry.getValue());
                }
            }
            throw new RuntimeException("未找到类型的Bean: " + type);
        }
    }

    // ========== 完整示例 ==========

    // 邮件服务
    static class EmailService {
        public void sendWelcomeEmail(String email) {
            System.out.println("  发送欢迎邮件到: " + email);
        }
    }

    // 完整的用户服务（依赖多个组件）
    static class CompleteUserService {
        private final UserRepository repository;
        private final EmailService emailService;

        // 多个依赖注入
        public CompleteUserService(UserRepository repository, EmailService emailService) {
            this.repository = repository;
            this.emailService = emailService;
        }

        public boolean registerUser(String name, String email) {
            repository.save(name, email);
            emailService.sendWelcomeEmail(email);
            return true;
        }

        public void findUser(String name) {
            String result = repository.findByName(name);
            System.out.println("  查询结果: " + result);
        }
    }
}
