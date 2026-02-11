# Java学习项目

欢迎来到Java学习之旅！这个项目专门为Java初学者设计，包含了从基础到Spring框架的完整学习路径。

## 📋 项目介绍

这是一个系统的Java学习代码库，通过实际代码示例帮助你掌握Java编程。

## 🛠️ 开发环境

- **Java版本**: OpenJDK 17
- **IDE**: VS Code
- **必需插件**: Extension Pack for Java

## 📚 学习路径

### 第一阶段：Java基础 (src/basics)

1. **HelloWorld.java** - 你的第一个Java程序
   - Java程序基本结构
   - main方法的作用
   - 如何输出信息

2. **DataTypes.java** - 数据类型
   - 基本数据类型（byte, short, int, long, float, double, char, boolean）
   - 字符串类型
   - 变量声明和使用

3. **ControlFlow.java** - 控制流
   - if-else条件语句
   - for、while循环
   - switch语句
   - 数组遍历

4. **Methods.java** - 方法
   - 方法定义和调用
   - 参数传递
   - 返回值

5. **JUnitDemo.java** - 单元测试 ⭐新增
   - JUnit 5基础
   - 测试断言和注解
   - 测试生命周期
   - Spring Test的基础

6. **ExceptionHandling.java** - 异常处理
   - try-catch-finally语句
7  - 多重异常捕获
   - 自定义异常
   - throws关键字

### 第二阶段：面向对象编程 (src/oop)

6. **Person.java** - 类和对象
8. **InterfaceDemo.java** - 接口
   - 对象的创建
   - 构造方法
   - 封装（getter/setter）
   - 成员方法

7. **InterfaceDemo.java** - 接口 ⭐新增
   - 接口定义和实现
   - 多接口实现
   - 默认方法和静态方法
   - 接口作为参数

9. **AbstractClassDemo.java** - 抽象类
   - 抽象类和抽象方法
   - 模板方法模式
   - 抽象类 vs 接口

10. **InheritanceDemo.java** - 继承和多态
   - 继承的使用
   - super关键字
   - 方法重写
   - 多态性和类型转换

### 第三阶段：集合框架 (src/collections)

11. **CollectionsDemo.java** - 常用集合
    - ArrayList - 动态数组
    - HashMap - 键值对映射
    - HashSet - 去重集合

### 第四阶段：Java高级特性 (src/advanced)

12. **LambdaDemo.java** - Lambda表达式
    - Lambda语法
    - 函数式接口
    - 方法引用
    - 实际应用场景

13. **StreamDemo.java** - Stream API
    - Stream创建和操作
    - 过滤、映射、排序
    - 聚合操作
    - 收集器使用

14. **AnnotationDemo.java** - 注解
    - 注解概念和作用
    - 自定义注解
    - 元注解
    - 模拟Spring注解

15. **GenericsDemo.java** - 泛型
    - 泛型类和泛型方法
    - 类型安全
    - 泛型通配符
    - 多类型参数

16. **ReflectionDemo.java** - 反射
    - Class对象获取
    - 操作字段和方法
    - 动态创建对象
    - Spring IoC原理

17. **ThreadDemo.java** - 多线程
    - 线程创建方式
    - 线程生命周期
    - 线程同步
    - synchronized关键字

18. **DesignPatternsDemo.java** - 设计模式
    - 单例模式
    - 工厂模式
    - 建造者模式
    - 代理模式（AOP原理）

19. **DateTimeDemo.java** - 日期时间API ⭐新增
    - LocalDate/LocalTime/LocalDateTime
    - 日期格式化和解析
    - 日期计算
    - 时区处理

20. **OptionalDemo.java** - Optional类 ⭐新增
    - Optional的创建和使用
    - 避免空指针异常
    - Optional的转换和过滤
    - Spring Data JPA中的应用

21. **EnumDemo.java** - 枚举类型 ⭐新增
    - 枚举的基本使用
    - 带属性和方法的枚举
    - 枚举的实战应用
    - 订单状态管理示例

22. **RegexDemo.java** - 正则表达式 ⭐新增
    - 正则表达式基础语法
    - Pattern和Matcher使用
    - 常用验证模式
    - 表单验证实战

23. **JsonDemo.java** - JSON处理 ⭐新增
    - Jackson库的使用
    - 对象与JSON互转
    - Jackson注解
    - REST API响应格式

24. **HttpClientDemo.java** - HTTP客户端 ⭐新增
    - Java HttpClient使用
    - GET/POST/PUT/DELETE请求
    - 同步和异步请求
    - API客户端封装

25. **JdbcDemo.java** - JDBC数据库操作 ⭐新增
    - 数据库连接和操作
    - CRUD操作
    - 事务处理
    - PreparedStatement防SQL注入
    - 📖 使用H2内存数据库（[查看说明](H2数据库说明.md)）

26. **PropertiesDemo.java** - 配置文件处理 ⭐新增
    - Properties文件读写
    - 配置管理
    - ResourceBundle国际化
    - Spring配置基础

27. **IoCDemo.java** - IoC控制反转 ⭐新增⭐核心
    - 依赖注入原理
    - 模拟Spring IoC容器
    - 解耦合的重要性
    - Spring核心概念

28. **AopDemo.java** - AOP面向切面编程 ⭐新增⭐核心
    - AOP概念和术语
    - 动态代理实现
    - 日志、事务、性能监控切面
    - Spring AOP基础

### 第四阶段补充：单元测试 (src/basics)

**JUnitDemo.java** - JUnit单元测试
    - JUnit 5测试框架
    - 测试注解(@Test、@BeforeEach等)
    - 断言方法(assertEquals、assertTrue等)
    - 参数化测试和测试生命周期

### 第五阶段：文件I/O操作 (src/io)

29. **FileIODemo.java** - 文件操作
    - 文件读写
    - 缓冲流使用
    - NIO文件操作
    - 文件和目录管理

### 第六阶段：准备Spring开发 ⭐新增

**已添加Maven配置**：[pom.xml](pom.xml)
- Maven项目结构
- 依赖管理
- 插件配置
- 准备好进入Spring学习！

## 🚀 如何运行

### 方法1：使用VS Code运行按钮
1. 打开任意`.java`文件
2. 点击右上角的▶️运行按钮
3. 查看输出结果

### 方法2：使用终端（变量、控制流、方法）
- ✅ 面向对象编程（类、接口、抽象类、继承、多态）
- ✅ 集合框架（ArrayList、HashMap、HashSet）
- ✅ 异常处理（try-catch、自定义异常）
- ✅ Lambda和Stream API
- ✅ 文件I/O操作
- ✅ 注解基础
- ✅ 泛型编程
- ✅ 反射机制 ⭐Spring核心
- ✅ 多线程基础
- ✅ 常用设计模式 ⭐Spring大量使用
- ✅ Maven项目管理

**恭喜！你已经具备了学习Spring框架的所有基础知识！**

下一步：
1. ✅ 已完成Java核心基础
2. 🎯 **可以开始学习Spring Boot了！**
3. 后续深入：Spring Data JPA、Spring Security等察输出
3. **修改代码**：尝试修改代码，看看会发生什么
4. **多做练习**：基于示例创建自己的程序
5. **理解原理**：不仅要知道怎么做，还要知道为什么

## 💡 学习技巧

- 每天至少编写一个小程序
- 遇到问题先尝试自己解决
- 阅读代码注释，理解每行代码的作用
- 使用调试功能，逐步执行代码
- 记录学习笔记

## 🔧 常见问题

### JdbcDemo运行错误："Table not found"

**问题**：运行JdbcDemo时出现`Table "USERS" not found`错误

**原因**：H2内存数据库默认在连接关闭后清空数据

**解决**：确保URL包含`DB_CLOSE_DELAY=-1`参数
```java
String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
```

📖 详细说明请查看：[H2数据库说明.md](H2数据库说明.md)

### Maven命令找不到

**问题**：`mvn: command not found`

**解决**：使用javac直接编译
```bash
javac src/advanced/YourDemo.java
cd src && java advanced.YourDemo
```

### 类找不到或无法加载

**问题**：`ClassNotFoundException`

**解决**：检查包路径和编译输出目录
```bash
# 正确的编译和运行方式
javac -d target/classes src/advanced/Demo.java
java -cp target/classes advanced.Demo
```

## 🎯 当前学习进度

你已经掌握：
- ✅ Java基础语法
- ✅ 面向对象编程（OOP）
- ✅ 集合框架
- ✅ Lambda和Stream API
- ✅ 文件I/O操作
- ✅ 注解基础

下一步可以学习：
1. 多线程和并发编程
2. 网络编程（Socket）
### Java命令
```bash
# 查看Java版本
java -version

# 编译所有Java文件
find src -name "*.java" -exec javac {} \;

# 清理编译文件
find src -name "*.class" -delete
```

### Maven命令
```bash
# 编译项目
mvn compile

# 运行测试
mvn test

# 打包项目
mvn package

# 清理编译文件
mvn clean

# 查看依赖树
mvn dependency:tre

# 编译所有Java文件
find src -name "*.java" -exec javac {} \;

# 清理编译文件
find src -name "*.class" -delete
```

## 🔗 学习资源

- [Oracle Java官方文档](https://docs.oracle.com/en/java/)
- [菜鸟教程 - Java](https://www.runoob.com/java/)
- [廖雪峰Java教程](https://www.liaoxuefeng.com/wiki/1252599548343744)

## ✨ 保持学习热情

记住：
- 每个大神都是从新手开始的
- 编程是一门需要实践的技能
- 不要害怕犯错，错误是最好的老师
- 享受编程的乐趣！

祝你学习愉快！🎉
