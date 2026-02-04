# 抽象类 vs 接口 vs 子类 - 使用场景指南

## 🎯 快速决策树

```
需要多继承？
├─ 是 → 使用接口（一个类可以实现多个接口）
└─ 否 → 继续判断
      │
      有共同的代码实现？
      ├─ 是 → 使用抽象类（可以共享代码）
      └─ 否 → 使用接口（只定义规范）

需要完整实现所有功能？
└─ 是 → 使用普通子类（具体类）
```

---

## 📊 三者对比

| 特性 | 接口 (Interface) | 抽象类 (Abstract Class) | 子类 (Class) |
|------|-----------------|----------------------|-------------|
| **多继承** | ✅ 可以实现多个 | ❌ 只能继承一个 | ❌ 只能继承一个 |
| **抽象方法** | ✅ 全部抽象 | ⚡ 可以有抽象和具体方法 | ❌ 不能有抽象方法 |
| **构造方法** | ❌ 不能有 | ✅ 可以有 | ✅ 必须有 |
| **成员变量** | ❌ 只能常量 | ✅ 可以有普通变量 | ✅ 可以有普通变量 |
| **实例化** | ❌ 不能 | ❌ 不能 | ✅ 可以 |
| **默认方法** | ✅ Java 8+ | ✅ 可以 | ✅ 可以 |

---

## 🔍 使用场景详解

### 1️⃣ 使用**接口** - 定义规范/契约

**何时使用：**
- ✅ 定义"能做什么"（能力/行为）
- ✅ 需要多个不相关的类实现相同行为
- ✅ 为了多态和松耦合
- ✅ Spring中的依赖注入

**典型场景：**
```java
// 场景：不同类型的对象都可以"飞"
interface Flyable {
    void fly();
}

class Bird implements Flyable {
    public void fly() { /* 鸟用翅膀飞 */ }
}

class Airplane implements Flyable {
    public void fly() { /* 飞机用引擎飞 */ }
}

class Superman implements Flyable {
    public void fly() { /* 超人用超能力飞 */ }
}
```

**实际例子：**
- `List`, `Set`, `Map` - 定义集合的行为
- `Runnable`, `Callable` - 定义可执行的任务
- `Comparable`, `Comparator` - 定义比较规则
- Spring中的 `Service`, `Repository` 接口

---

### 2️⃣ 使用**抽象类** - 共享代码 + 定义框架

**何时使用：**
- ✅ 有共同的代码需要复用
- ✅ 定义模板方法（算法骨架）
- ✅ 父子类关系明确（"is-a"关系）
- ✅ 需要成员变量保存状态

**典型场景：**
```java
// 场景：所有形状都有颜色，都能计算面积
abstract class Shape {
    protected String color;  // 共享的成员变量

    // 具体方法：所有子类共享
    public void setColor(String color) {
        this.color = color;
    }

    // 抽象方法：子类必须实现
    public abstract double getArea();
}

class Circle extends Shape {
    private double radius;

    public double getArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private double width, height;

    public double getArea() {
        return width * height;
    }
}
```

**实际例子：**
- `HttpServlet` - Web开发基类
- `InputStream`, `OutputStream` - IO流基类
- `AbstractList`, `AbstractMap` - 集合框架基类

---

### 3️⃣ 使用**普通子类** - 完整具体实现

**何时使用：**
- ✅ 实现所有功能，可以直接使用
- ✅ 不需要被进一步继承
- ✅ 业务逻辑的具体实现

**典型场景：**
```java
// 具体的业务类
class UserService {
    private UserRepository repository;

    public User getUser(Long id) {
        return repository.findById(id);
    }

    public void saveUser(User user) {
        repository.save(user);
    }
}

// 具体的实体类
class User {
    private String name;
    private int age;

    // getter/setter...
}
```

**实际例子：**
- `ArrayList`, `HashMap` - 具体集合实现
- `String`, `Integer` - 具体数据类型
- 各种Service、Controller、Entity类

---

## 💡 实战决策指南

### 场景1：设计支付系统
```java
// 接口：定义支付能力
interface Payment {
    void pay(double amount);
}

// 抽象类：共享支付流程
abstract class AbstractPayment implements Payment {
    protected String paymentId;

    // 模板方法：定义支付流程
    public final void pay(double amount) {
        validateAmount(amount);
        doPayment(amount);  // 具体支付方式
        logPayment(amount);
    }

    protected abstract void doPayment(double amount);

    private void validateAmount(double amount) {
        // 共享验证逻辑
    }

    private void logPayment(double amount) {
        // 共享日志逻辑
    }
}

// 具体类：实现具体支付方式
class AlipayPayment extends AbstractPayment {
    protected void doPayment(double amount) {
        // 支付宝支付逻辑
    }
}

class WechatPayment extends AbstractPayment {
    protected void doPayment(double amount) {
        // 微信支付逻辑
    }
}
```

### 场景2：动物系统
```java
// 接口：定义能力
interface Swimmable { void swim(); }
interface Flyable { void fly(); }

// 抽象类：共享属性和行为
abstract class Animal {
    protected String name;
    protected int age;

    public abstract void makeSound();

    public void eat() {
        System.out.println(name + " is eating");
    }
}

// 具体类：多重能力
class Duck extends Animal implements Swimmable, Flyable {
    public void makeSound() { System.out.println("Quack!"); }
    public void swim() { System.out.println("Duck swimming"); }
    public void fly() { System.out.println("Duck flying"); }
}

class Fish extends Animal implements Swimmable {
    public void makeSound() { System.out.println("Blub!"); }
    public void swim() { System.out.println("Fish swimming"); }
}
```

---

## 🎓 选择原则总结

1. **接口优先原则**：
   - 面向接口编程（Spring推荐）
   - 降低耦合度
   - 便于测试和扩展

2. **抽象类用于代码复用**：
   - 有共同实现逻辑
   - 模板方法模式

3. **具体类用于实现**：
   - 完整的业务逻辑
   - 可直接实例化使用

4. **组合优于继承**：
   - 能用接口就不用继承
   - 能用组合就不用继承

---

## 🚀 Spring框架中的应用

```java
// 接口：定义服务契约
public interface UserService {
    User findById(Long id);
    void save(User user);
}

// 实现类：具体业务逻辑
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public User findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }
}
```

**为什么Spring推荐接口 + 实现？**
- ✅ 依赖注入更灵活
- ✅ 便于切换实现（如测试时mock）
- ✅ AOP代理更容易
- ✅ 解耦，便于维护

---

## 📝 记忆口诀

**接口定义规范，抽象类复用代码，具体类完成实现！**

### 三句话总结：
1. **接口（Interface）**：能做什么 - 定义行为契约
2. **抽象类（Abstract）**：是什么 - 共享代码和状态
3. **具体类（Class）**：怎么做 - 完整的实现

---

## 🎯 实战练习建议

1. 查看项目代码中的 `InterfaceDemo.java`、`AbstractClassDemo.java`、`InheritanceDemo.java`
2. 运行示例，理解不同场景的使用
3. 尝试自己设计一个包含三者的小系统（如图书管理系统）
4. 思考：如果只能用一种，会遇到什么问题？

继续学习Spring框架时，你会发现这些概念无处不在！
