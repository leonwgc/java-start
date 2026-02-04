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

5. **ExceptionHandling.java** - 异常处理 ⭐新增
   - try-catch-finally语句
   - 多重异常捕获
   - 自定义异常
   - throws关键字

### 第二阶段：面向对象编程 (src/oop)

6. **Person.java** - 类和对象
   - 类的定义
   - 对象的创建
   - 构造方法
   - 封装（getter/setter）
   - 成员方法

7. **InterfaceDemo.java** - 接口 ⭐新增
   - 接口定义和实现
   - 多接口实现
   - 默认方法和静态方法
   - 接口作为参数

8. **AbstractClassDemo.java** - 抽象类 ⭐新增
   - 抽象类和抽象方法
   - 模板方法模式
   - 抽象类 vs 接口

9. **InheritanceDemo.java** - 继承和多态 ⭐新增
   - 继承的使用
   - super关键字
   - 方法重写
   - 多态性和类型转换

### 第三阶段：集合框架 (src/collections)

10. **CollectionsDemo.java** - 常用集合
    - ArrayList - 动态数组
    - HashMap - 键值对映射
    - HashSet - 去重集合

### 第四阶段：Java高级特性 (src/advanced) ⭐新增

11. **LambdaDemo.java** - Lambda表达式
    - Lambda语法
    - 函数式接口
    - 方法引用
    - 实际应用场景

12. **StreamDemo.java** - Stream API
    - Stream创建和操作
    - 过滤、映射、排序
    - 聚合操作
    - 收集器使用

13. **AnnotationDemo.java** - 注解
    - 注解概念和作用
    - 自定义注解
    - 元注解
    - 模拟Spring注解

### 第五阶段：文件I/O操作 (src/io) ⭐新增

14. **FileIODemo.java** - 文件操作
    - 文件读写
    - 缓冲流使用
    - NIO文件操作
    - 文件和目录管理

### 第六阶段：Spring框架 (src/spring)

（即将添加，敬请期待！）

## 🚀 如何运行

### 方法1：使用VS Code运行按钮
1. 打开任意`.java`文件
2. 点击右上角的▶️运行按钮
3. 查看输出结果

### 方法2：使用终端
```bash
# 编译Java文件
javac src/basics/HelloWorld.java

# 运行Java程序
java -cp src basics.HelloWorld
```

## 📖 学习建议

1. **循序渐进**：按照学习路径顺序学习
2. **动手实践**：运行每个示例程序，观察输出
3. **修改代码**：尝试修改代码，看看会发生什么
4. **多做练习**：基于示例创建自己的程序
5. **理解原理**：不仅要知道怎么做，还要知道为什么

## 💡 学习技巧

- 每天至少编写一个小程序
- 遇到问题先尝试自己解决
- 阅读代码注释，理解每行代码的作用
- 使用调试功能，逐步执行代码
- 记录学习笔记

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
3. 数据库操作（JDBC）
4. Maven/Gradle构建工具
5. **开始学习Spring框架** 🎯

## 📝 常用命令

```bash
# 查看Java版本
java -version

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
