# Spring Boot 学习示例

## 🎯 项目概述

这是一个循序渐进的Spring Boot实战项目，基于你之前学习的Java基础和Spring核心概念。

## 📚 学习示例

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
mvn spring-boot:run -Dstart-class=com.example.jpa.JpaApplication
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
# 方式1：Maven插件
mvn spring-boot:run -Dstart-class=<完整类名>

# 方式2：打包后运行
mvn clean package
java -jar target/spring-boot-examples-1.0.0.jar
```

## 📖 学习路径

### 第1步：快速入门 ✅
运行 `QuickStartApplication`，理解Spring Boot基础概念。

### 第2步：RESTful API ✅
运行 `RestfulApplication`，学习HTTP接口开发。

### 第3步：数据库操作 ✅
运行 `JpaApplication`，掌握Spring Data JPA。

### 第4步：实战项目（即将添加）
- Spring Boot + JPA 完整CRUD
- 数据验证（Bean Validation）
- 异常处理（@ControllerAdvice）
- 缓存集成（@Cacheable）
- AOP日志（@Aspect）

## 🔗 与前面学习的关联

| 前置示例 | Spring Boot应用 |
|---------|----------------|
| IoCDemo.java | Spring Boot自动装配机制 |
| AopDemo.java | @Aspect切面编程 |
| JdbcDemo.java | Spring Data JPA |
| HttpClientDemo.java | @RestController服务端 |
| ValidationDemo.java | @Valid参数验证 |
| CacheDemo.java | @Cacheable缓存注解 |
| AsyncDemo.java | @Async异步方法 |
| PropertiesDemo.java | application.yml配置 |
| LoggingDemo.java | Spring Boot日志系统 |

## 💡 学习建议

1. **逐个运行示例**：按顺序运行每个示例，理解基本概念
2. **查看日志输出**：控制台日志展示了详细的操作过程
3. **测试API接口**：使用curl或Postman测试REST接口
4. **查看数据库**：访问H2控制台查看数据变化
5. **修改代码实验**：尝试添加新接口、新字段、新查询方法
6. **对比前置示例**：理解Spring Boot如何简化了之前的手动代码

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

完成这3个示例后，你将掌握：
- ✅ Spring Boot应用的创建和启动
- ✅ RESTful API的设计和实现
- ✅ Spring Data JPA的使用

**接下来可以学习**:
- Spring Boot完整项目实战
- Spring Security安全认证
- Spring Cloud微服务
- 部署和运维

**恭喜你开始Spring Boot实战！** 🚀
