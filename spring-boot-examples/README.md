# Spring Boot 学习示例

## 🎯 项目概述

这是一个循序渐进的Spring Boot实战项目，包含 **11个完整示例**，从基础入门到进阶实战，基于你之前学习的Java基础和Spring核心概念。

## 📚 学习示例

### 📦 示例总览

| 序号 | 示例名称 | 运行命令 | 难度 | 说明 |
|-----|---------|---------|-----|------|
| 1 | QuickStart | `./run-app.sh quickstart` | ⭐ | Spring Boot快速入门 |
| 2 | Restful | `./run-app.sh restful` | ⭐⭐ | RESTful API开发 |
| 3 | JPA | `./run-app.sh jpa` | ⭐⭐ | Spring Data JPA |
| 4 | Validation | `./run-app.sh validation` | ⭐⭐ | 表单验证 |
| 5 | Exception | `./run-app.sh exception` | ⭐⭐⭐ | 异常处理 |
| 6 | Config | `./run-app.sh config` | ⭐⭐ | 配置管理 |
| 7 | Interceptor | `./run-app.sh interceptor` | ⭐⭐⭐ | 拦截器和过滤器 |
| 8 | FileUpload | `./run-app.sh fileupload` | ⭐⭐ | 文件上传下载 |
| 9 | Transaction | `./run-app.sh transaction` | ⭐⭐⭐ | 事务管理 |
| 10 | Cache | `./run-app.sh cache` | ⭐⭐⭐ | 缓存应用 |
| 11 | Scheduled | `./run-app.sh scheduled` | ⭐⭐ | 定时任务 |

---

## 🚀 快速启动

### 方式1：使用启动脚本（推荐）
```bash
cd spring-boot-examples

# 查看所有可用示例
./run-app.sh

# 运行指定示例
./run-app.sh quickstart
```

### 方式2：Maven命令
```bash
mvn spring-boot:run -Dstart-class=com.example.quickstart.QuickStartApplication
```

### 方式3：VS Code调试
1. 打开spring-boot-examples目录
2. 按F5启动调试
3. 选择对应的调试配置

---

## 📖 详细示例说明

### 1️⃣ QuickStartApplication - Spring Boot 快速入门

**位置**: `src/main/java/com/example/quickstart/QuickStartApplication.java`

**学习目标**:
- 理解Spring Boot的核心注解
- 掌握Spring Boot应用的启动方式
- 学会创建简单的REST接口
- 了解Spring Boot的自动配置机制

**核心知识点**:
- `@SpringBootApplication`：组合注解，包含@Configuration、@EnableAutoConfiguration、@ComponentScan
- `@RestController`：REST控制器，自动将返回值转换为JSON
- `@GetMapping`：处理GET请求
- `SpringApplication.run()`：启动Spring Boot应用

**运行方式**:
```bash
cd spring-boot-examples
mvn spring-boot:run -Dstart-class=com.example.quickstart.QuickStartApplication
```

**测试接口**:
```bash
# Hello World
curl http://localhost:8080/hello

# 带参数的问候
curl http://localhost:8080/greet?name=张三

# 返回JSON对象
curl http://localhost:8080/info
```

**关联前面的学习**:
- IoCDemo.java：理解了依赖注入，现在使用Spring Boot的自动装配
- AnnotationDemo.java：学习了自定义注解，现在使用Spring的注解

---

### 2️⃣ RestfulApplication - RESTful API 实战

**位置**: `src/main/java/com/example/restful/RestfulApplication.java`

**学习目标**:
- 理解RESTful架构风格
- 掌握Spring MVC的核心注解
- 学会CRUD操作的实现
- 了解HTTP状态码的使用

**核心知识点**:
- RESTful设计原则：资源导向、统一接口、无状态
- HTTP方法映射：
  * GET：查询资源
  * POST：创建资源
  * PUT：更新资源
  * DELETE：删除资源
- `@PathVariable`：路径参数
- `@RequestParam`：查询参数
- `@RequestBody`：请求体参数
- `ResponseEntity`：响应实体，包含状态码和响应体

**运行方式**:
```bash
mvn spring-boot:run -Dstart-class=com.example.restful.RestfulApplication
```

**测试接口**:
```bash
# 获取所有用户
curl http://localhost:8080/api/users

# 获取指定用户
curl http://localhost:8080/api/users/1

# 创建用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"赵六","email":"zhaoliu@example.com","age":27}'

# 更新用户
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"张三（已更新）","email":"zhangsan@example.com","age":26}'

# 删除用户
curl -X DELETE http://localhost:8080/api/users/1

# 搜索用户
curl http://localhost:8080/api/users/search?name=张
```

**关联前面的学习**:
- CollectionsDemo.java：使用了Map存储数据
- LambdaDemo.java：使用Stream API过滤数据
- HttpClientDemo.java：理解了HTTP协议，现在实现HTTP服务端

---

### 3️⃣ JpaApplication - Spring Data JPA 实战

**位置**: `src/main/java/com/example/jpa/JpaApplication.java`

**学习目标**:
- 理解JPA和Hibernate的关系
- 掌握实体类的定义和注解
- 学会使用Spring Data JPA Repository
- 了解JPQL查询语言

**核心知识点**:
- JPA实体类注解：
  * `@Entity`：标记为JPA实体
  * `@Table`：指定表名
  * `@Id`：主键字段
  * `@GeneratedValue`：主键生成策略
  * `@Column`：列属性配置
- Repository接口：
  * 继承`JpaRepository<T, ID>`
  * 方法命名规则：findByXxx、deleteByXxx
  * `@Query`：自定义JPQL查询
- `CommandLineRunner`：应用启动后自动执行

**运行方式**:
```bash
./run-app.sh jpa
```

**查看数据库**:
访问 http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (留空)

**关联前面的学习**:
- JdbcDemo.java：手动编写SQL和JDBC代码
- JPA：自动生成SQL，更加简洁
- DesignPatternsDemo.java中的代理模式：JPA使用动态代理实现Repository
- ReflectionDemo.java：JPA使用反射操作实体类

---

### 4️⃣ ValidationApplication - 表单验证和数据校验

**位置**: `src/main/java/com/example/validation/ValidationApplication.java`

**学习目标**:
- 掌握Jakarta Validation（JSR-380）注解的使用
- 理解@Valid和@Validated的区别
- 学会自定义验证错误响应
- 掌握分组验证和嵌套对象验证

**核心知识点**:
- 验证注解：
  * `@NotNull` / `@NotEmpty` / `@NotBlank`：非空验证
  * `@Size(min, max)`：长度验证
  * `@Min` / `@Max`：数值范围
  * `@Email`：邮箱格式
  * `@Pattern(regexp)`：正则表达式
  * `@Past` / `@Future`：日期验证
- `@Valid`：触发验证
- `@ExceptionHandler`：处理验证异常
- 统一错误响应格式

**运行方式**:
```bash
./run-app.sh validation
```

**测试接口**:
```bash
# 用户注册（正确）
curl -X POST http://localhost:8080/api/validation/register \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","email":"test@example.com","age":25,"password":"123456"}'

# 用户注册（验证失败）
curl -X POST http://localhost:8080/api/validation/register \
  -H "Content-Type: application/json" \
  -d '{"username":"ab","email":"invalid","age":15,"password":"123"}'

# 查看验证规则
curl http://localhost:8080/api/validation/rules
```

**关联前面的学习**:
- ValidationDemo.java：手动验证
- Spring Boot：使用注解自动验证

---

### 5️⃣ ExceptionHandlingApplication - 异常处理和统一响应

**位置**: `src/main/java/com/example/exception/ExceptionHandlingApplication.java`

**学习目标**:
- 理解Spring Boot的异常处理机制
- 掌握@ExceptionHandler的使用
- 学会使用@ControllerAdvice进行全局异常处理
- 掌握自定义业务异常的创建和使用
- 了解HTTP状态码的正确使用

**核心知识点**:
- `@ControllerAdvice`：全局异常处理器
- `@ExceptionHandler`：处理特定异常
- 自定义异常类：
  * ResourceNotFoundException（404）
  * BusinessException（400）
  * ValidationException（400）
  * PermissionDeniedException（403）
- ResponseEntity：包含状态码的响应
- 统一错误响应格式

**运行方式**:
```bash
./run-app.sh exception
```

**测试接口**:
```bash
# 资源未找到异常
curl http://localhost:8080/api/exception/users/999

# 业务异常
curl http://localhost:8080/api/exception/divide/10/0

# 权限异常
curl -X DELETE http://localhost:8080/api/exception/users/1

# 系统异常
curl http://localhost:8080/api/exception/error

# 查看使用指南
curl http://localhost:8080/api/exception/guide
```

**关联前面的学习**:
- ExceptionHandling.java：手动try-catch
- Spring Boot：统一异常处理

---

### 6️⃣ ConfigPropertiesApplication - 配置属性管理

**位置**: `src/main/java/com/example/config/ConfigPropertiesApplication.java`

**学习目标**:
- 掌握@Value注解注入配置值
- 理解@ConfigurationProperties的使用
- 学会配置文件的层级结构
- 掌握不同数据类型的配置注入
- 了解配置的优先级和覆盖规则

**核心知识点**:
- `@Value("${key:defaultValue}")`：简单配置注入
- `@ConfigurationProperties(prefix)`：批量配置注入
- 配置文件：application.yml
- 支持的数据类型：
  * String、数字、布尔
  * List、Map
  * 嵌套对象
- 配置优先级：命令行 > 系统属性 > application.yml

**运行方式**:
```bash
./run-app.sh config
```

**测试接口**:
```bash
# 查看应用配置
curl http://localhost:8080/api/config/app

# 查看数据库配置
curl http://localhost:8080/api/config/database

# 查看安全配置
curl http://localhost:8080/api/config/security

# 查看所有配置
curl http://localhost:8080/api/config/all
```

**关联前面的学习**:
- PropertiesDemo.java：手动读取properties文件
- Spring Boot：自动注入配置

---

### 7️⃣ InterceptorApplication - 拦截器和过滤器

**位置**: `src/main/java/com/example/interceptor/InterceptorApplication.java`

**学习目标**:
- 理解Filter和Interceptor的区别
- 掌握Filter的创建和注册
- 掌握Interceptor的创建和配置
- 了解请求处理的完整流程
- 学会使用拦截器实现通用功能

**核心知识点**:
- Filter（Servlet规范）：
  * 容器级别
  * 可以拦截所有请求
  * 实现`javax.servlet.Filter`
- Interceptor（Spring MVC）：
  * Spring级别
  * 只拦截Controller请求
  * 实现`HandlerInterceptor`
  * preHandle / postHandle / afterCompletion
- WebMvcConfigurer：注册拦截器
- 应用场景：
  * Filter：字符编码、CORS、XSS防御
  * Interceptor：登录验证、权限检查、日志记录

**运行方式**:
```bash
./run-app.sh interceptor
```

**测试接口**:
```bash
# 公开接口（无需token）
curl http://localhost:8080/api/demo/public

# 受保护接口（无token，返回401）
curl http://localhost:8080/api/demo/protected

# 受保护接口（有token，成功）
curl -H "Authorization: Bearer valid-token" \
  http://localhost:8080/api/demo/protected

# 慢接口（查看性能监控日志）
curl http://localhost:8080/api/demo/slow
```

**关联前面的学习**:
- AopDemo.java：面向切面编程
- Interceptor：特定于Web请求的切面

---

### 8️⃣ FileUploadApplication - 文件上传下载

**位置**: `src/main/java/com/example/fileupload/FileUploadApplication.java`

**学习目标**:
- 掌握MultipartFile处理文件上传
- 学会文件的存储和管理
- 实现文件下载功能
- 掌握文件类型验证和大小限制
- 了解文件上传的安全性考虑

**核心知识点**:
- `MultipartFile`：Spring提供的文件上传接口
- Path/Files：Java NIO文件操作API
- Resource：Spring资源抽象
- Content-Disposition：HTTP头控制文件下载
- 文件验证：
  * 文件大小限制（最大10MB）
  * 文件类型验证（白名单）
  * 文件名安全处理（防止路径穿越）
- UUID：生成唯一文件名

**运行方式**:
```bash
./run-app.sh fileupload
```

**测试接口**:
```bash
# 创建测试文件
echo "Hello, Spring Boot!" > test.txt

# 单文件上传
curl -X POST -F "file=@test.txt" \
  http://localhost:8080/api/files/upload

# 多文件上传
curl -X POST -F "files=@file1.txt" -F "files=@file2.txt" \
  http://localhost:8080/api/files/upload-multiple

# 查看所有文件
curl http://localhost:8080/api/files

# 下载文件
curl -O http://localhost:8080/api/files/download/filename.txt

# 删除文件
curl -X DELETE http://localhost:8080/api/files/filename.txt
```

**关联前面的学习**:
- FileIODemo.java：手动文件读写
- NioDemo.java：NIO文件操作
- Spring Boot：简化文件上传处理

---

### 9️⃣ TransactionApplication - 事务管理

**位置**: `src/main/java/com/example/transaction/TransactionApplication.java`

**学习目标**:
- 理解Spring事务管理机制
- 掌握@Transactional注解的使用
- 学习事务传播行为和隔离级别
- 了解事务回滚规则和最佳实践

**核心知识点**:
- `@Transactional`：声明式事务管理注解
- 事务传播行为：
  * REQUIRED（默认）：有事务加入，无事务创建
  * REQUIRES_NEW：总是创建新事务
  * SUPPORTS、NOT_SUPPORTED、MANDATORY、NEVER、NESTED
- 事务属性：
  * propagation：传播行为
  * isolation：隔离级别
  * timeout：超时设置
  * readOnly：只读事务
  * rollbackFor：指定回滚异常
  * noRollbackFor：指定不回滚异常
- `@Version`：乐观锁版本控制
- 事务边界和ACID特性

**运行方式**:
```bash
./run-app.sh transaction
```

**测试接口**:
```bash
# 查看所有账户
curl http://localhost:8080/api/accounts

# 成功转账（事务提交）
curl -X POST "http://localhost:8080/api/transfer?from=1&to=2&amount=100"

# 失败转账（事务回滚 - 余额不足）
curl -X POST "http://localhost:8080/api/transfer?from=1&to=2&amount=10000"

# 查看交易日志
curl http://localhost:8080/api/logs

# 测试事务传播行为
curl -X POST http://localhost:8080/api/test-propagation

# 测试独立事务（REQUIRES_NEW）
curl -X POST http://localhost:8080/api/test-requires-new
```

**关联前面的学习**:
- JdbcDemo.java：JDBC事务基础
- ConcurrencyDemo.java：并发控制
- JpaApplication：JPA数据库操作

---

### 🔟 CacheApplication - 缓存应用

**位置**: `src/main/java/com/example/cache/CacheApplication.java`

**学习目标**:
- 理解Spring Cache抽象层
- 掌握缓存注解的使用
- 学习缓存配置和管理
- 了解缓存策略和最佳实践

**核心知识点**:
- `@EnableCaching`：启用缓存支持
- `@Cacheable`：查询缓存，不存在则执行方法
- `@CachePut`：更新缓存，总是执行方法
- `@CacheEvict`：删除缓存（单个或全部）
- `@Caching`：组合多个缓存操作
- `CacheManager`：缓存管理器
- SpEL表达式：灵活的缓存Key设计
- 缓存策略：
  * Cache-Aside（旁路缓存）
  * Write-Through（写穿）
  * Write-Behind（写后）

**运行方式**:
```bash
./run-app.sh cache
```

**测试接口**:
```bash
# 第一次查询（慢，2秒延迟）
curl http://localhost:8080/api/users/1
# 响应：{"user":{...},"duration":"2003ms","cached":false}

# 第二次查询（快，从缓存读取）
curl http://localhost:8080/api/users/1
# 响应：{"user":{...},"duration":"5ms","cached":true}

# 更新用户（更新缓存）
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"张三（已更新）","email":"zhangsan@example.com","age":26}'

# 删除用户（清除缓存）
curl -X DELETE http://localhost:8080/api/users/1

# 查看缓存状态
curl http://localhost:8080/api/cache/stats

# 清空所有缓存
curl -X DELETE http://localhost:8080/api/cache/all
```

**关联前面的学习**:
- CacheDemo.java：LRU缓存算法实现
- OptionalDemo.java：处理null值
- ConcurrencyDemo.java：线程安全

---

### 1️⃣1️⃣ ScheduledApplication - 定时任务

**位置**: `src/main/java/com/example/scheduled/ScheduledApplication.java`

**学习目标**:
- 理解Spring定时任务机制
- 掌握@Scheduled注解的使用
- 学习Cron表达式
- 了解定时任务配置和最佳实践

**核心知识点**:
- `@EnableScheduling`：启用定时任务支持
- `@Scheduled`：定义定时任务
- fixedRate：固定频率（从开始时间算）
- fixedDelay：固定延迟（从结束时间算）
- initialDelay：初始延迟
- cron：Cron表达式
- `TaskScheduler`：任务调度器
- 线程池配置：多任务并发执行
- 动态任务：运行时启动/停止任务
- Cron表达式格式：秒 分 时 日 月 周 [年]

**运行方式**:
```bash
./run-app.sh scheduled
```

**测试接口**:
```bash
# 查看任务执行日志
curl http://localhost:8080/api/tasks/logs

# 查看任务状态
curl http://localhost:8080/api/tasks/status

# 手动触发任务
curl -X POST http://localhost:8080/api/tasks/trigger

# 启动动态任务
curl -X POST http://localhost:8080/api/tasks/dynamic/start

# 停止动态任务
curl -X POST http://localhost:8080/api/tasks/dynamic/stop

# 清空日志
curl -X POST http://localhost:8080/api/tasks/logs/clear
```

**常用Cron表达式**:
```
0 0 2 * * ?          每天凌晨2点
0 0/5 * * * ?        每5分钟
0 0 9 * * MON-FRI    工作日上午9点
0 0 0 1 * ?          每月1号凌晨
0 0 8 ? * MON        每周一上午8点
```

**关联前面的学习**:
- ScheduledTaskDemo.java：Java定时任务基础
- ThreadDemo.java：多线程基础
- AsyncDemo.java：异步编程

---

## 🛠️ 运行要求

### 环境要求
- Java 17+
- Maven 3.6+

### 依赖安装
```bash
cd spring-boot-examples
mvn clean install
```

### 启动应用
```bash
# 方式1：启动脚本（推荐）
./run-app.sh <示例名称>

# 方式2：Maven插件
mvn spring-boot:run -Dstart-class=<完整类名>

# 方式3：打包后运行
mvn clean package
java -jar target/spring-boot-examples-1.0.0.jar
```

## 📖 学习路径

### 第1周：基础示例（必学）

#### Day 1-2：快速入门 ✅
- 运行 `./run-app.sh quickstart`
- 理解Spring Boot基础概念
- 学习@SpringBootApplication、@RestController等注解
- 测试3个基础REST接口

#### Day 3-4：RESTful API ✅
- 运行 `./run-app.sh restful`
- 学习RESTful设计原则
- 掌握CRUD操作实现
- 理解HTTP方法和状态码

#### Day 5-7：数据库操作 ✅
- 运行 `./run-app.sh jpa`
- 掌握JPA实体类定义
- 学习Spring Data JPA Repository
- 访问H2控制台查看数据

### 第2周：进阶示例（重要）

#### Day 1-2：表单验证 ⭐
- 运行 `./run-app.sh validation`
- 掌握验证注解使用
- 学习@Valid触发验证
- 处理验证异常

#### Day 3-4：异常处理 ⭐
- 运行 `./run-app.sh exception`
- 理解异常处理机制
- 掌握@ControllerAdvice全局异常处理
- 自定义业务异常
- 统一错误响应格式

#### Day 5：配置管理 ⭐
- 运行 `./run-app.sh config`
- 学习@Value和@ConfigurationProperties
- 掌握配置文件结构
- 理解配置优先级

#### Day 6：拦截器和过滤器 ⭐
- 运行 `./run-app.sh interceptor`
- 理解Filter和Interceptor区别
- 实现登录验证
- 性能监控

#### Day 7：文件上传下载 ⭐
- 运行 `./run-app.sh fileupload`
- 掌握MultipartFile使用
- 实现文件上传下载
- 文件验证和安全处理

#### Day 8：事务管理 ⭐⭐⭐
- 运行 `./run-app.sh transaction`
- 理解ACID特性和事务边界
- 掌握@Transactional使用
- 学习事务传播行为
- 测试事务回滚场景

#### Day 9：缓存应用 ⭐⭐⭐
- 运行 `./run-app.sh cache`
- 理解缓存的作用和使用场景
- 掌握@Cacheable、@CachePut、@CacheEvict
- 对比有无缓存的性能差异
- 学习缓存管理策略

#### Day 10：定时任务 ⭐
- 运行 `./run-app.sh scheduled`
- 理解fixedRate和fixedDelay区别
- 学习Cron表达式语法
- 观察不同类型任务执行
- 实现动态任务启停

### 第3周：综合实战

结合所有学到的知识，开发一个完整的小项目，例如：
- 📝 博客系统：文章CRUD + 标签 + 分类 + 评论 + 缓存 + 定时发布
- 📋 任务管理系统：任务CRUD + 优先级 + 状态管理 + 定时提醒
- 📚 图书管理系统：图书CRUD + 借阅管理 + 事务处理
- 👥 用户管理系统：用户CRUD + 角色权限 + 登录认证 + 数据缓存

### 学习建议

1. **按顺序学习**：从QuickStart开始，逐步深入
2. **动手实践**：每个示例都要运行和测试
3. **查看日志**：理解程序执行流程
4. **修改代码**：尝试添加新功能，加深理解
5. **对比学习**：对比基础示例和进阶示例的区别
6. **断点调试**：使用F5调试，观察变量值
7. **查看文档**：每个示例的main方法都有详细注释

## 🔗 与前面学习的关联

| 前置示例 | Spring Boot应用 | 进步点 |
|---------|----------------|--------|
| IoCDemo.java | Spring Boot自动装配机制 | 无需手动配置Bean |
| AopDemo.java | @Aspect切面编程 | 内置AOP支持 |
| JdbcDemo.java | Spring Data JPA | 自动生成SQL |
| HttpClientDemo.java | @RestController服务端 | 简化HTTP服务开发 |
| ValidationDemo.java | @Valid参数验证 | 注解式验证 |
| CacheDemo.java | @Cacheable缓存注解 | 声明式缓存 |
| AsyncDemo.java | @Async异步方法 | 自动线程管理 |
| PropertiesDemo.java | @ConfigurationProperties | 类型安全的配置 |
| LoggingDemo.java | Spring Boot日志系统 | 统一日志管理 |
| AnnotationDemo.java | Spring注解体系 | 理解注解原理 |
| ReflectionDemo.java | JPA实体操作 | 理解反射应用 |
| DesignPatternsDemo.java | Spring设计模式 | 单例、工厂、代理等 |
| FileIODemo.java | MultipartFile文件上传 | 简化文件处理 |
| NioDemo.java | 文件存储服务 | Path/Files API |
| ExceptionHandling.java | @ControllerAdvice | 全局异常处理 |

## 💡 核心收获

完成所有8个示例后，你将掌握：

### 基础能力
- ✅ Spring Boot应用的创建和启动
- ✅ RESTful API的设计和实现
- ✅ Spring Data JPA的使用
- ✅ Maven项目管理

### 进阶能力
- ✅ 表单验证和数据校验
- ✅ 统一异常处理机制
- ✅ 配置属性的管理方式
- ✅ 拦截器和过滤器的使用
- ✅ 文件上传下载功能

## 🔧 常见问题

### 端口已被占用
修改 `src/main/resources/application.yml`：
```yaml
server:
  port: 8081  # 改为其他端口
```

### H2控制台无法访问
确保配置正确：
```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
```

### Maven依赖下载慢
配置国内镜像（在 `~/.m2/settings.xml`）：
```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

## 🎉 下一步学习

完成所有示例后，建议的学习方向：

### 1. Spring Boot高级特性
- ⭐ Spring Boot Actuator（监控和健康检查）
- ⭐ Spring Boot Test（单元测试和集成测试）
- ⭐ Spring Boot Profiles（多环境配置）
- ⭐ Spring Boot DevTools（开发工具）

### 2. 数据库进阶
- ⭐ MyBatis / MyBatis-Plus（SQL映射框架）
- ⭐ 数据库连接池（HikariCP、Druid）
- ⭐ 分页查询（PageHelper、JPA Pagination）
- ⭐ 数据库迁移（Flyway、Liquibase）

### 3. 安全和认证
- ⭐ Spring Security（安全框架）
- ⭐ JWT Token认证
- ⭐ OAuth2 / SSO单点登录
- ⭐ 权限管理（RBAC）

### 4. 微服务架构
- ⭐ Spring Cloud（微服务全家桶）
- ⭐ 服务注册与发现（Eureka、Nacos）
- ⭐ 配置中心（Config Server、Apollo）
- ⭐ API网关（Gateway、Zuul）
- ⭐ 服务调用（OpenFeign、RestTemplate）
- ⭐ 熔断降级（Resilience4j、Sentinel）

### 5. 缓存和消息
- ⭐ Redis集成（缓存、分布式锁）
- ⭐ 消息队列（RabbitMQ、Kafka、RocketMQ）
- ⭐ 定时任务（@Scheduled、Quartz、XXL-Job）

### 6. 部署和运维
- ⭐ Docker容器化
- ⭐ Kubernetes编排
- ⭐ CI/CD持续集成
- ⭐ 日志收集（ELK、Loki）
- ⭐ 监控告警（Prometheus、Grafana）

### 7. 综合项目实战
推荐项目：
- 📝 **博客系统**：文章管理、评论、标签、搜索
- 🛒 **电商系统**：商品、订单、购物车、支付
- 📚 **在线教育**：课程、视频、作业、考试
- 👥 **社交平台**：用户、动态、关注、私信
- 🏢 **企业OA**：考勤、审批、通知、权限

## 📚 推荐资源

### 官方文档
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Spring Framework文档](https://spring.io/projects/spring-framework)
- [Spring Data JPA文档](https://spring.io/projects/spring-data-jpa)
- [Maven官方文档](https://maven.apache.org/)

### 学习网站
- Spring官方教程：https://spring.io/guides
- Baeldung Spring教程：https://www.baeldung.com/
- 掘金Spring专栏
- CSDN Spring Boot专题

### 推荐书籍
- 《Spring Boot实战》
- 《Spring微服务实战》
- 《深入理解Spring Cloud与微服务构建》

## 🙏 致谢

感谢你完成Spring Boot学习示例！从Java基础到Spring Boot实战，你已经掌握了：

- ✅ 49个Java基础示例
- ✅ Spring核心概念（IoC、AOP）
- ✅ 8个Spring Boot实战示例

**你现在已经具备了Java后端开发的基础能力！**

继续加油，向更高的目标前进！🚀

---

## 📝 附录

### 常用命令速查

```bash
# 查看所有示例
./run-app.sh

# 运行示例
./run-app.sh <示例名称>

# 编译项目
mvn clean compile

# 打包项目
mvn clean package

# 清理项目
mvn clean

# 查看依赖树
mvn dependency:tree

# 运行测试
mvn test
```

### 常用端口

| 端口 | 服务 |
|-----|------|
| 8080 | Spring Boot应用 |
| 8080/h2-console | H2数据库控制台 |
| 8080/actuator | Spring Boot Actuator |

### 项目结构

```
spring-boot-examples/
├── src/main/java/com/example/
│   ├── quickstart/          # 快速入门
│   ├── restful/             # RESTful API
│   ├── jpa/                 # Spring Data JPA
│   ├── validation/          # 表单验证
│   ├── exception/           # 异常处理
│   ├── config/              # 配置管理
│   ├── interceptor/         # 拦截器和过滤器
│   └── fileupload/          # 文件上传下载
├── src/main/resources/
│   └── application.yml      # 配置文件
├── .vscode/
│   ├── launch.json          # 调试配置
│   └── settings.json        # VS Code设置
├── pom.xml                  # Maven配置
├── run-app.sh               # 启动脚本
├── README.md                # 本文件
├── 快速开始.md               # 快速开始指南
├── 新增示例说明.md          # 新增示例详细说明
└── Maven介绍.md             # Maven工具介绍
```

---

**恭喜你完成Spring Boot实战学习！** 🎉

希望这些示例能帮助你更好地理解和掌握Spring Boot开发。

继续努力，成为优秀的Java开发工程师！💪
