# Spring Boot 学习路径

## 🎯 前置要求

在开始Spring Boot学习之前，你应该已经掌握：

### ✅ 已完成的Java基础学习
- ✅ Java基础语法（49个示例）
- ✅ 面向对象编程
- ✅ 集合框架和Stream API
- ✅ Lambda表达式和函数式编程
- ✅ 注解和反射机制
- ✅ 多线程和并发编程

### ✅ 已完成的Spring核心概念
- ✅ [IoCDemo.java](src/advanced/IoCDemo.java) - IoC控制反转和依赖注入
- ✅ [AopDemo.java](src/advanced/AopDemo.java) - AOP面向切面编程
- ✅ [JdbcDemo.java](src/advanced/JdbcDemo.java) - JDBC数据库操作
- ✅ [ValidationDemo.java](src/advanced/ValidationDemo.java) - Bean Validation

### ✅ 已完成的基础设施层
- ✅ [CacheDemo.java](src/advanced/CacheDemo.java) - 缓存机制
- ✅ [RetryDemo.java](src/advanced/RetryDemo.java) - 重试机制
- ✅ [EventBusDemo.java](src/advanced/EventBusDemo.java) - 事件总线

**恭喜！你已经完全准备好学习Spring Boot了！** 🎉

---

## 🚀 Spring Boot 学习路径

### 📁 项目位置
所有Spring Boot示例都在 `spring-boot-examples/` 目录中。

```
spring-boot-examples/
├── pom.xml                          # Maven配置
├── src/main/
│   ├── java/com/example/
│   │   ├── quickstart/              # 1️⃣ 快速入门
│   │   │   └── QuickStartApplication.java
│   │   ├── restful/                 # 2️⃣ RESTful API
│   │   │   └── RestfulApplication.java
│   │   └── jpa/                     # 3️⃣ Spring Data JPA
│   │       └── JpaApplication.java
│   └── resources/
│       └── application.yml          # Spring Boot配置
├── README.md                        # 详细使用说明
└── run-app.sh                       # 快速启动脚本
```

---

## 📚 学习示例详解

### 1️⃣ QuickStartApplication - Spring Boot 快速入门

**学习目标**：
- 理解Spring Boot的核心注解
- 掌握Spring Boot应用的启动方式
- 学会创建简单的REST接口
- 了解Spring Boot的自动配置机制

**核心知识点**：
- `@SpringBootApplication` = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`
- `@RestController` = `@Controller` + `@ResponseBody`
- `SpringApplication.run()` 启动方式
- 内嵌Tomcat服务器

**运行方式**：
```bash
cd spring-boot-examples
./run-app.sh quickstart
```

**测试接口**：
```bash
# Hello World
curl http://localhost:8080/hello

# 带参数的问候
curl http://localhost:8080/greet?name=张三

# 返回JSON对象
curl http://localhost:8080/info
```

**与前面学习的关联**：
| 前置示例 | Spring Boot应用 |
|---------|----------------|
| [IoCDemo.java](src/advanced/IoCDemo.java) | Spring Boot的自动装配和依赖注入 |
| [AnnotationDemo.java](src/advanced/AnnotationDemo.java) | Spring的注解体系 |

---

### 2️⃣ RestfulApplication - RESTful API 实战

**学习目标**：
- 理解RESTful架构风格
- 掌握Spring MVC的核心注解
- 学会CRUD操作的实现
- 了解HTTP状态码的使用

**核心知识点**：
- RESTful设计原则：资源导向、统一接口、无状态
- HTTP方法映射：GET、POST、PUT、DELETE
- `@PathVariable`：路径参数（/users/{id}）
- `@RequestParam`：查询参数（?name=xxx）
- `@RequestBody`：请求体参数（JSON）
- `ResponseEntity`：响应实体（包含状态码）

**运行方式**：
```bash
cd spring-boot-examples
./run-app.sh restful
```

**测试接口**：
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
curl "http://localhost:8080/api/users/search?name=张"
```

**与前面学习的关联**：
| 前置示例 | Spring Boot应用 |
|---------|----------------|
| [HttpClientDemo.java](src/advanced/HttpClientDemo.java) | 理解HTTP协议，现在实现服务端 |
| [CollectionsDemo.java](src/collections/CollectionsDemo.java) | 使用Map存储数据 |
| [LambdaDemo.java](src/advanced/LambdaDemo.java) | Stream API过滤数据 |

---

### 3️⃣ JpaApplication - Spring Data JPA 实战

**学习目标**：
- 理解JPA和Hibernate的关系
- 掌握实体类的定义和注解
- 学会使用Spring Data JPA Repository
- 了解JPQL查询语言

**核心知识点**：
- JPA实体类注解：`@Entity`、`@Table`、`@Id`、`@Column`
- Repository接口：继承`JpaRepository<T, ID>`
- 方法命名规则：`findByXxx`、`deleteByXxx`、`countByXxx`
- `@Query`注解：自定义JPQL查询
- `CommandLineRunner`：应用启动后执行

**运行方式**：
```bash
cd spring-boot-examples
./run-app.sh jpa
```

**查看数据库**：
访问 http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (留空)

**与前面学习的关联**：
| 前置示例 | Spring Boot应用 |
|---------|----------------|
| [JdbcDemo.java](src/advanced/JdbcDemo.java) | 手动JDBC → 自动ORM |
| [DesignPatternsDemo.java](src/advanced/DesignPatternsDemo.java) | JPA使用动态代理 |
| [ReflectionDemo.java](src/advanced/ReflectionDemo.java) | JPA使用反射 |

---

## 🎓 学习建议

### 第1天：快速入门
1. 运行 `QuickStartApplication`
2. 理解 `@SpringBootApplication` 注解
3. 测试3个接口，观察JSON响应
4. 修改返回内容，重启应用查看效果

### 第2天：RESTful API
1. 运行 `RestfulApplication`
2. 使用curl测试所有CRUD接口
3. 观察控制台日志输出
4. 尝试添加新接口（例如：分页查询）

### 第3天：数据库操作
1. 运行 `JpaApplication`
2. 访问H2控制台查看数据
3. 观察JPA自动生成的SQL
4. 尝试添加新查询方法

### 第4天：综合实战
1. 结合三个示例的知识
2. 创建一个完整的用户管理系统
3. 添加数据验证（@Valid）
4. 添加异常处理（@ControllerAdvice）

---

## 🔗 知识关联图

```
Java基础（49个示例）
  ↓
Spring核心概念（IoC、AOP）
  ↓
Spring Boot快速入门 ← 你在这里
  ↓
RESTful API开发
  ↓
Spring Data JPA
  ↓
实战项目（用户管理系统）
  ↓
高级特性（缓存、安全、消息队列）
  ↓
微服务架构（Spring Cloud）
```

---

## 💡 常见问题

### Q1: 为什么要学Spring Boot？
**A**: Spring Boot简化了Spring开发：
- 自动配置：无需复杂的XML配置
- 内嵌服务器：无需部署WAR文件
- 生产就绪：自带监控、健康检查
- 快速开发：约定优于配置

### Q2: Spring Boot和Spring有什么区别？
**A**:
- Spring：框架本身，需要大量配置
- Spring Boot：基于Spring，自动配置，开箱即用

### Q3: 我需要掌握Spring才能学Spring Boot吗？
**A**: 不需要！但是你需要理解Spring的核心概念（IoC和AOP），这些你已经在前面的示例中学习过了。

### Q4: 如何调试Spring Boot应用？
**A**:
1. 查看控制台日志
2. 使用IDE的断点调试
3. 使用Spring Boot Actuator监控
4. 查看H2数据库内容

### Q5: 端口8080被占用怎么办？
**A**: 修改 `application.yml` 中的端口配置：
```yaml
server:
  port: 8081
```

---

## 📖 推荐资源

### 官方文档
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [Spring Data JPA文档](https://spring.io/projects/spring-data-jpa)
- [Spring MVC文档](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)

### 学习路径
1. ✅ 完成3个基础示例
2. 📚 阅读Spring Boot官方指南
3. 💻 创建自己的实战项目
4. 🚀 学习Spring Cloud微服务

---

## 🎉 学习成果检查

完成Spring Boot学习后，你应该能够：

- [ ] 创建并启动Spring Boot应用
- [ ] 开发RESTful API接口
- [ ] 使用Spring Data JPA操作数据库
- [ ] 理解Spring Boot的自动配置机制
- [ ] 配置application.yml
- [ ] 使用依赖注入
- [ ] 处理HTTP请求和响应
- [ ] 实现CRUD完整功能

**完成这些后，你就可以开始开发真实的Spring Boot项目了！** 🎊

---

## 🚀 下一步学习

### 中级主题
- Spring Boot异常处理（@ControllerAdvice）
- 数据验证（@Valid、@Validated）
- 缓存集成（@Cacheable）
- AOP日志（@Aspect）
- 异步处理（@Async）
- 定时任务（@Scheduled）

### 高级主题
- Spring Security安全认证
- Spring Cloud微服务
- 消息队列（RabbitMQ、Kafka）
- Redis集成
- Docker部署
- Kubernetes编排

**继续加油，成为Spring Boot开发高手！** 💪
