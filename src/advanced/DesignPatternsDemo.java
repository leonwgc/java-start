package advanced;

/**
 * Java设计模式学习
 * 学习目标：
 * 1. 理解设计模式的概念
 * 2. 掌握单例模式
 * 3. 掌握工厂模式
 * 4. 了解常见设计模式在Spring中的应用
 */
public class DesignPatternsDemo {
    public static void main(String[] args) {
        System.out.println("=== 设计模式学习 ===\n");

        // 示例1：单例模式
        demonstrateSingleton();

        // 示例2：工厂模式
        demonstrateFactory();

        // 示例3：建造者模式
        demonstrateBuilder();

        // 示例4：代理模式
        demonstrateProxy();
    }

    // 单例模式
    public static void demonstrateSingleton() {
        System.out.println("1. 单例模式（Singleton Pattern）\n");
        System.out.println("作用：确保一个类只有一个实例");
        System.out.println("Spring应用：Spring Bean默认是单例的\n");

        // 饿汉式单例
        DatabaseConnection conn1 = DatabaseConnection.getInstance();
        DatabaseConnection conn2 = DatabaseConnection.getInstance();

        System.out.println("两次获取的是同一个实例吗？" + (conn1 == conn2));
        conn1.connect();

        // 懒汉式单例（线程安全）
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();

        System.out.println("Logger单例: " + (logger1 == logger2));
        logger1.log("这是一条日志");
        System.out.println();
    }

    // 工厂模式
    public static void demonstrateFactory() {
        System.out.println("2. 工厂模式（Factory Pattern）\n");
        System.out.println("作用：创建对象时不暴露创建逻辑");
        System.out.println("Spring应用：BeanFactory、FactoryBean\n");

        // 简单工厂
        Animal dog = AnimalFactory.createAnimal("dog");
        Animal cat = AnimalFactory.createAnimal("cat");

        dog.speak();
        cat.speak();

        // 抽象工厂
        PaymentFactory alipayFactory = new AlipayFactory();
        Payment alipay = alipayFactory.createPayment();
        alipay.pay(100);

        PaymentFactory wechatFactory = new WechatFactory();
        Payment wechat = wechatFactory.createPayment();
        wechat.pay(200);
        System.out.println();
    }

    // 建造者模式
    public static void demonstrateBuilder() {
        System.out.println("3. 建造者模式（Builder Pattern）\n");
        System.out.println("作用：构建复杂对象");
        System.out.println("应用：StringBuilder、HttpClient构建等\n");

        // 使用建造者模式创建对象
        User user = new User.Builder()
            .setName("张三")
            .setAge(25)
            .setEmail("zhangsan@example.com")
            .setPhone("13800138000")
            .build();

        System.out.println(user);
        System.out.println();
    }

    // 代理模式
    public static void demonstrateProxy() {
        System.out.println("4. 代理模式（Proxy Pattern）\n");
        System.out.println("作用：为对象提供代理以控制对它的访问");
        System.out.println("Spring应用：AOP、@Transactional等\n");

        // 使用代理
        UserService userService = new UserServiceProxy(new UserServiceImpl());
        userService.save("用户数据");
        System.out.println();
        userService.delete(123);

        System.out.println("\n这就是Spring AOP的实现原理！");
    }
}

// ========== 单例模式 ==========

// 饿汉式单例（线程安全，推荐）
class DatabaseConnection {
    // 类加载时就创建实例
    private static final DatabaseConnection instance = new DatabaseConnection();

    // 私有构造器
    private DatabaseConnection() {
        System.out.println("创建数据库连接...");
    }

    // 公共获取方法
    public static DatabaseConnection getInstance() {
        return instance;
    }

    public void connect() {
        System.out.println("连接到数据库");
    }
}

// 懒汉式单例（双重检查锁，线程安全）
class Logger {
    private static volatile Logger instance;

    private Logger() {
        System.out.println("初始化Logger...");
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

// ========== 工厂模式 ==========

// 产品接口
interface Animal {
    void speak();
}

// 具体产品
class Dog implements Animal {
    public void speak() {
        System.out.println("汪汪汪！");
    }
}

class Cat implements Animal {
    public void speak() {
        System.out.println("喵喵喵！");
    }
}

// 简单工厂
class AnimalFactory {
    public static Animal createAnimal(String type) {
        if ("dog".equals(type)) {
            return new Dog();
        } else if ("cat".equals(type)) {
            return new Cat();
        }
        throw new IllegalArgumentException("未知类型: " + type);
    }
}

// 抽象工厂模式
interface Payment {
    void pay(double amount);
}

interface PaymentFactory {
    Payment createPayment();
}

class AlipayPayment implements Payment {
    public void pay(double amount) {
        System.out.println("使用支付宝支付: ￥" + amount);
    }
}

class WechatPayment implements Payment {
    public void pay(double amount) {
        System.out.println("使用微信支付: ￥" + amount);
    }
}

class AlipayFactory implements PaymentFactory {
    public Payment createPayment() {
        return new AlipayPayment();
    }
}

class WechatFactory implements PaymentFactory {
    public Payment createPayment() {
        return new WechatPayment();
    }
}

// ========== 建造者模式 ==========

class User {
    private String name;
    private int age;
    private String email;
    private String phone;

    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.phone = builder.phone;
    }

    // 建造者类
    public static class Builder {
        private String name;
        private int age;
        private String email;
        private String phone;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age +
               ", email='" + email + "', phone='" + phone + "'}";
    }
}

// ========== 代理模式 ==========

// 接口
interface UserService {
    void save(String data);
    void delete(int id);
}

// 真实实现
class UserServiceImpl implements UserService {
    public void save(String data) {
        System.out.println("  [核心业务] 保存数据: " + data);
    }

    public void delete(int id) {
        System.out.println("  [核心业务] 删除ID为 " + id + " 的数据");
    }
}

// 代理类（增强功能）
class UserServiceProxy implements UserService {
    private UserService target;

    public UserServiceProxy(UserService target) {
        this.target = target;
    }

    public void save(String data) {
        System.out.println("[代理] 开始事务");
        System.out.println("[代理] 权限检查");

        target.save(data);  // 调用真实对象

        System.out.println("[代理] 提交事务");
        System.out.println("[代理] 记录日志");
    }

    public void delete(int id) {
        System.out.println("[代理] 开始事务");
        System.out.println("[代理] 权限检查");

        target.delete(id);

        System.out.println("[代理] 提交事务");
        System.out.println("[代理] 记录日志");
    }
}
