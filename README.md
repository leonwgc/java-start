# Java学习项目

欢迎来到Java学习之旅！这个项目专门为Java初学者设计，包含了从基础到Spring框架的完整学习路径。

## 🆕 最新更新（2026-02）

**最新：新增基础设施层模式** ⭐⭐⭐⭐⭐（2026-02-13 下午）：

**基础设施层（为Spring Boot做准备）**：
- 🌐 [SocketDemo.java](src/advanced/SocketDemo.java) - 网络编程（TCP/IP、客户端-服务器模式）
- 💾 [ConnectionPoolDemo.java](src/advanced/ConnectionPoolDemo.java) - 数据库连接池（HikariCP原理）
- 🚀 [CacheDemo.java](src/advanced/CacheDemo.java) - 缓存机制（LRU算法、缓存问题）
- 🔄 [RetryDemo.java](src/advanced/RetryDemo.java) - 重试机制（指数退避、Spring Retry基础）
- 📡 [EventBusDemo.java](src/advanced/EventBusDemo.java) - 事件总线（发布订阅、Spring Event原理）

**Java现代特性（Java 14-17+）**：
- 📦 [RecordsDemo.java](src/advanced/RecordsDemo.java) - 记录类（不可变数据载体）
- 🔀 [SwitchExpressionDemo.java](src/advanced/SwitchExpressionDemo.java) - Switch表达式（箭头语法、yield）
- 📝 [TextBlocksDemo.java](src/advanced/TextBlocksDemo.java) - 文本块（多行字符串、SQL/JSON）
- 🎯 [VarDemo.java](src/advanced/VarDemo.java) - 类型推断（var关键字）
- 🔒 [SealedClassesDemo.java](src/advanced/SealedClassesDemo.java) - 密封类（限制继承、领域建模）

**高级编程技巧**：
- 🎭 [FunctionalInterfacesDemo.java](src/advanced/FunctionalInterfacesDemo.java) - 函数式接口详解（Function、Predicate、Consumer）
- 📊 [CollectionsAdvancedDemo.java](src/collections/CollectionsAdvancedDemo.java) - 集合高级操作（groupingBy、分组统计）
- ⚡ [ConcurrencyDemo.java](src/advanced/ConcurrencyDemo.java) - 并发工具（线程池、原子类、Lock）
- ✅ [ValidationDemo.java](src/advanced/ValidationDemo.java) - Bean Validation（JSR-380验证）

**高级技术示例** ⭐⭐⭐⭐（2026-02-11）：
- 💾 [NioDemo.java](src/advanced/NioDemo.java) - NIO文件操作（Netty/Tomcat底层）
- 📝 [LoggingDemo.java](src/advanced/LoggingDemo.java) - SLF4J+Logback日志框架（**Spring Boot默认**）
- ⏰ [ScheduledTaskDemo.java](src/advanced/ScheduledTaskDemo.java) - 定时任务（@Scheduled底层）
- 🔄 [SerializationDemo.java](src/advanced/SerializationDemo.java) - 序列化机制（Redis Session）

**Spring核心概念示例** ⭐⭐⭐：
- 🔥 [IoCDemo.java](src/advanced/IoCDemo.java) - IoC控制反转和依赖注入（**Spring核心**）
- 🔥 [AopDemo.java](src/advanced/AopDemo.java) - AOP面向切面编程（**Spring核心**）
- ⚙️ [PropertiesDemo.java](src/advanced/PropertiesDemo.java) - 配置管理
- ⚡ [AsyncDemo.java](src/advanced/AsyncDemo.java) - 异步编程CompletableFuture
- 🏗️ [BuilderDemo.java](src/advanced/BuilderDemo.java) - Builder模式和链式调用

**新增实用工具类**：
- Optional、Enum、DateTime、Regex
- JSON处理、HTTP客户端、JDBC数据库

📖 详细说明：[2026-02-13-新增示例总结.md](2026-02-13-新增示例总结.md) | [新增高级示例说明.md](新增高级示例说明.md) | [Spring核心概念学习指南.md](Spring核心概念学习指南.md) | [学习示例完整清单.md](学习示例完整清单.md)

---

## 📋 项目介绍

这是一个系统的Java学习代码库，包含 **49个精心设计的学习示例**，通过实际代码帮助你：
1. 掌握Java核心语法和现代特性
2. 理解面向对象编程
3. 学会常用设计模式和高级编程技巧
4. **理解Spring框架核心思想**（IoC、AOP）
5. **掌握基础设施层模式**（缓存、重试、事件总线等）

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

12. **CollectionsAdvancedDemo.java** - 集合高级操作 ⭐新增
    - groupingBy、partitioningBy分组分区
    - Collectors高级收集器
    - 比较器链式调用
    - 并发集合和不可变集合

### 第四阶段：Java高级特性 (src/advanced)

#### 核心语言特性

13. **LambdaDemo.java** - Lambda表达式
    - Lambda语法
    - 函数式接口
    - 方法引用
    - 实际应用场景

14. **FunctionalInterfacesDemo.java** - 函数式接口详解 ⭐新增
    - Function、Predicate、Consumer、Supplier
    - BiFunction双参数函数
    - 方法引用和构造器引用
    - 函数组合（andThen、compose）

15. **StreamDemo.java** - Stream API
    - Stream创建和操作
    - 过滤、映射、排序
    - 聚合操作
    - 收集器使用

16. **AnnotationDemo.java** - 注解
    - 注解概念和作用
    - 自定义注解
    - 元注解
    - 模拟Spring注解

17. **GenericsDemo.java** - 泛型
    - 泛型类和泛型方法
    - 类型安全
    - 泛型通配符
    - 多类型参数

18. **ReflectionDemo.java** - 反射
    - Class对象获取
    - 操作字段和方法
    - 动态创建对象
    - Spring IoC原理

19. **ThreadDemo.java** - 多线程
    - 线程创建方式
    - 线程生命周期
    - 线程同步
    - synchronized关键字

20. **ConcurrencyDemo.java** - 并发工具详解 ⭐新增
    - 线程池（ExecutorService、ThreadPoolExecutor）
    - 原子类（AtomicInteger、AtomicReference）
    - 显式锁（ReentrantLock、ReadWriteLock）
    - 同步工具（CountDownLatch、CyclicBarrier、Semaphore）
    - 阻塞队列和ForkJoinPool

21. **DesignPatternsDemo.java** - 设计模式
    - 单例模式
    - 工厂模式
    - 建造者模式
    - 代理模式（AOP原理）

#### 实用工具类

22. **DateTimeDemo.java** - 日期时间API ⭐新增
    - LocalDate/LocalTime/LocalDateTime
    - 日期格式化和解析
    - 日期计算
    - 时区处理

23. **OptionalDemo.java** - Optional类 ⭐新增
    - Optional的创建和使用
    - 避免空指针异常
    - Optional的转换和过滤
    - Spring Data JPA中的应用

24. **EnumDemo.java** - 枚举类型 ⭐新增
    - 枚举的基本使用
    - 带属性和方法的枚举
    - 枚举的实战应用
    - 订单状态管理示例

25. **RegexDemo.java** - 正则表达式 ⭐新增
    - 正则表达式基础语法
    - Pattern和Matcher使用
    - 常用验证模式
    - 表单验证实战

26. **JsonDemo.java** - JSON处理 ⭐新增
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

26. **ValidationDemo.java** - Bean Validation ⭐新增
    - JSR-380验证规范
    - 常用验证注解（@NotNull、@Size、@Email等）
    - 自定义验证器
    - 验证组和级联验证

#### Spring核心概念

27. **PropertiesDemo.java** - 配置文件处理 ⭐新增
    - Properties文件读写
    - 配置管理
    - ResourceBundle国际化
    - Spring配置基础

28. **IoCDemo.java** - IoC控制反转 ⭐新增⭐核心
    - 依赖注入原理
    - 模拟Spring IoC容器
    - 解耦合的重要性
    - Spring核心概念

29. **AopDemo.java** - AOP面向切面编程 ⭐新增⭐核心
    - AOP概念和术语
    - 动态代理实现
    - 日志、事务、性能监控切面
    - Spring AOP基础

30. **AsyncDemo.java** - 异步编程 ⭐新增
    - CompletableFuture基础
    - 异步任务链和组合
    - 异步异常处理
    - Spring @Async基础

31. **BuilderDemo.java** - Builder模式 ⭐新增
    - Builder模式设计
    - 链式调用实现
    - SQL查询构建器
    - Lombok @Builder原理

#### 高级技术示例

32. **NioDemo.java** - NIO文件操作 ⭐新增
    - Path和Files工具类
    - FileChannel和ByteBuffer
    - 目录遍历和文件查找
    - WatchService文件监听
    - Spring Boot文件上传底层

33. **LoggingDemo.java** - SLF4J+Logback日志 ⭐新增⭐重要
    - 日志级别和参数化日志
    - 异常日志记录
    - MDC诊断上下文
    - Spring Boot默认日志框架

34. **ScheduledTaskDemo.java** - 定时任务 ⭐新增
    - Timer vs ScheduledExecutorService
    - fixedRate vs fixedDelay
    - 定时任务模式
    - Spring @Scheduled注解基础

35. **SerializationDemo.java** - 序列化机制 ⭐新增
    - Serializable接口
    - transient和serialVersionUID
    - 自定义序列化
    - Redis Session序列化基础

#### Java现代特性（Java 14-17+）

36. **RecordsDemo.java** - 记录类 ⭐新增
    - Records定义和使用（Java 14+）
    - 紧凑构造器和自定义方法
    - Records vs 传统类
    - DTO和不可变对象最佳实践

37. **SwitchExpressionDemo.java** - Switch表达式 ⭐新增
    - 箭头语法（->）和yield关键字
    - Switch作为表达式返回值
    - 穷尽性检查
    - 状态机和路由应用

38. **TextBlocksDemo.java** - 文本块 ⭐新增
    - 三重引号（"""）语法（Java 15+）
    - 多行字符串和自动缩进
    - SQL、JSON、HTML等应用
    - 格式化方法和转义序列

39. **VarDemo.java** - 类型推断 ⭐新增
    - var关键字使用（Java 10+）
    - 局部变量类型推断规则
    - var最佳实践和限制
    - 简化复杂泛型声明

40. **SealedClassesDemo.java** - 密封类 ⭐新增
    - sealed、permits、non-sealed关键字（Java 17+）
    - 限制继承层次结构
    - 模式匹配和领域建模
    - 状态模式、事件系统、API响应类型

#### 基础设施层模式

41. **SocketDemo.java** - 网络编程 ⭐新增
    - TCP/IP Socket通信
    - 客户端-服务器模式
    - 多客户端并发处理
    - HTTP连接和心跳检测

42. **ConnectionPoolDemo.java** - 数据库连接池 ⭐新增
    - 连接池原理和实现
 6  - 自定义连接池（SimpleConnectionPool）
    - 并发连接管理
    - HikariCP配置（Spring Boot默认）

43. **CacheDemo.java** - 缓存机制 ⭐新增
    - LRU缓存算法实现
    - TTL过期缓存
    - 缓存策略（LRU、LFU、FIFO）
    - 缓存问题（穿透、击穿、雪崩）

44. **RetryDemo.java** - 重试机制 ⭐新增
    - 固定延迟重试策略
    - 指数退避重试算法
    - 条件重试（异常类型判断）
    - HTTP请求、数据库操作重试

45. **EventBusDemo.java** - 事件总线 ⭐新增
    - 发布-订阅模式
    - 类型安全的事件系统
    - 同步和异步事件处理
    - Spring Event基础原理

### 第四阶段补充：单元测试 (src/basics)

**JUnitDemo.java** - JUnit单元测试
    - JUnit 5测试框架
    - 测试注解(@Test、@BeforeEach等)
    - 断言方法(assertEquals、assertTrue等)
    - 参数化测试和测试生命周期

### 第五阶段：文件I/O操作 (src/io)

41. **FileIODemo.java** - 文件操作
    - 文件读写
    - 缓冲流使用
    - NIO文件操作
    - 文件和目录管理

### 第六阶段：Spring Boot实战 🎯

**Spring Boot项目**：[spring-boot-examples/](spring-boot-examples/)

现在你可以开始Spring Boot实战了！我们已经为你准备了完整的学习路径：

#### Spring Boot学习示例

47. **QuickStartApplication** - Spring Boot快速入门 ⭐新增
    - Spring Boot核心注解
    - 自动配置机制
    - REST接口开发
    - 内嵌Tomcat服务器

48. **RestfulApplication** - RESTful API实战 ⭐新增
    - RESTful架构风格
    - CRUD操作实现
    - HTTP状态码使用
    - 路径参数和查询参数

49. **JpaApplication** - Spring Data JPA实战 ⭐新增
    - JPA实体类定义
    - Repository接口使用
    - JPQL查询语言
    - H2内存数据库

📖 **完整学习路径**：[SpringBoot学习路径.md](SpringBoot学习路径.md)

**快速启动**：
```bash
cd spring-boot-examples

# 快速入门
./run-app.sh quickstart

# RESTful API
./run-app.sh restful

# Spring Data JPA
./run-app.sh jpa
```

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
- ✅ Java基础语法（变量、控制流、方法、异常处理）
- ✅ 面向对象编程（类、继承、接口、抽象类、多态）
- ✅ 集合框架（ArrayList、HashMap、HashSet、高级操作）
- ✅ Lambda表达式和Stream API
- 基础设施层** ⭐⭐⭐⭐：
- ✅ Socket网络编程
- ✅ 数据库连接池
- ✅ 缓存机制（LRU、TTL）
- ✅ 重试机制（指数退避）
- ✅ 事件总线（发布订阅）

**🎊 恭喜！你已经完全准备好开始Spring Boot实战了！**

## 🚀 现在开始Spring Boot学习

### Spring Boot实战项目：[spring-boot-examples/](spring-boot-examples/)

我们已经为你准备了3个循序渐进的Spring Boot示例：

1. **QuickStartApplication** - Spring Boot快速入门
   - 理解核心注解和自动配置
   - 创建简单的REST接口
   - 掌握应用启动方式

2. **RestfulApplication** - RESTful API实战
   - 实现完整的CRUD操作
   - 学习HTTP方法和状态码
   - 处理路径参数和请求体

3. **JpaApplication** - Spring Data JPA实战
   - 使用JPA操作数据库
   - 理解ORM映射关系
   - 掌握Repository查询方法

📖 **完整学习路径**：[SpringBoot学习路径.md](SpringBoot学习路径.md)

### 快速开始

```bash
cd spring-boot-examples

# 1. 快速入门
./run-app.sh quickstart
# 访问: http://localhost:8080/hello

# 2. RESTful API
./run-app.sh restful
# 测试: curl http://localhost:8080/api/users

# 3. Spring Data JPA
./run-app.sh jpa
# 控制台: http://localhost:8080/h2-console
```

### 学习建议

**第1天**：运行QuickStartApplication，理解Spring Boot基础
**第2天**：运行RestfulApplication，学习RESTful API开发
**第3天**：运行JpaApplication，掌握数据库操作
**第4天**：综合实战，创建自己的用户管理系统

### 下一步深入学习

**Java现代特性** ⭐⭐⭐⭐⭐：
- ✅ Records记录类（Java 14+）
- ✅ Switch表达式（Java 14+）
- ✅ 文本块（Java 15+）
- ✅ var类型推断（Java 10+）
- ✅ 密封类（Java 17+）

**恭喜！你已经具备了学习Spring Boot的所有基础知识！** 🎉

下一步学习建议：

### 1️⃣ 立即开始Spring Boot（推荐）
你现在已经完全掌握了：
- IoC/DI容器原理
- AOP切面编程思想
- 配置管理基础
- 异步编程模式
- Builder模式设计

**可以直接开始学习：**
- Spring Boot快速入门
- Spring Data JPA
- Spring MVC RESTful API
- Spring Security

### 2️⃣ 深入进阶（可选）
如果想继续深入Java基础：
- 并发编程（ExecutorService、锁机制）
- JVM原理和性能调优
- 网络编程（Socket、Netty）
- 微服务架构（Spring Cloud）

### 3️⃣ 推荐学习路径
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
