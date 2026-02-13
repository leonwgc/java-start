package com.example.restful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RESTful API 实战
 * 学习目标：
 * 1. 理解RESTful架构风格
 * 2. 掌握Spring MVC的核心注解
 * 3. 学会CRUD操作的实现
 * 4. 了解HTTP状态码的使用
 *
 * RESTful是什么？
 * - REST：表述性状态转移（Representational State Transfer）
 * - 资源导向：一切皆资源
 * - 统一接口：使用标准HTTP方法
 *   * GET：查询资源
 *   * POST：创建资源
 *   * PUT：更新资源（完整更新）
 *   * PATCH：更新资源（部分更新）
 *   * DELETE：删除资源
 * - 无状态：每次请求独立
 *
 * Spring MVC注解：
 * - @RestController：组合注解 = @Controller + @ResponseBody
 * - @RequestMapping：映射请求路径
 * - @GetMapping、@PostMapping、@PutMapping、@DeleteMapping
 * - @PathVariable：路径参数
 * - @RequestParam：查询参数
 * - @RequestBody：请求体参数
 */
@SpringBootApplication
public class RestfulApplication {

    public static void main(String[] args) {
        System.out.println("=== RESTful API 实战 ===\n");
        System.out.println("正在启动用户管理API服务...\n");

        SpringApplication.run(RestfulApplication.class, args);

        System.out.println("\n✅ API服务启动成功！");
        System.out.println("\n📖 可用接口：");
        System.out.println("GET    /api/users        - 获取所有用户");
        System.out.println("GET    /api/users/{id}   - 获取指定用户");
        System.out.println("POST   /api/users        - 创建新用户");
        System.out.println("PUT    /api/users/{id}   - 更新用户");
        System.out.println("DELETE /api/users/{id}   - 删除用户");
        System.out.println("\n💡 使用curl或Postman测试API");
        System.out.println("curl http://localhost:8080/api/users");
        System.out.println("\n按 Ctrl+C 停止应用\n");
    }
}

/**
 * 用户控制器
 * 实现用户资源的CRUD操作
 */
@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserService userService = new UserService();

    /**
     * 1. 获取所有用户
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        System.out.println("📋 查询所有用户，共 " + users.size() + " 条记录");
        return ResponseEntity.ok(users);
    }

    /**
     * 2. 根据ID获取用户
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        System.out.println("🔍 查询用户 ID: " + id);

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            System.out.println("✅ 找到用户: " + user.get().getName());
            return ResponseEntity.ok(user.get());
        } else {
            System.out.println("❌ 用户不存在");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 3. 创建新用户
     * POST /api/users
     * Content-Type: application/json
     * Body: {"name": "张三", "email": "zhangsan@example.com", "age": 25}
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        System.out.println("➕ 创建用户: " + user.getName());

        User createdUser = userService.create(user);
        System.out.println("✅ 用户创建成功，ID: " + createdUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * 4. 更新用户
     * PUT /api/users/{id}
     * Content-Type: application/json
     * Body: {"name": "李四", "email": "lisi@example.com", "age": 30}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        System.out.println("✏️  更新用户 ID: " + id);

        Optional<User> updatedUser = userService.update(id, user);
        if (updatedUser.isPresent()) {
            System.out.println("✅ 用户更新成功");
            return ResponseEntity.ok(updatedUser.get());
        } else {
            System.out.println("❌ 用户不存在");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 5. 删除用户
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        System.out.println("🗑️  删除用户 ID: " + id);

        boolean deleted = userService.delete(id);
        if (deleted) {
            System.out.println("✅ 用户删除成功");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("❌ 用户不存在");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 6. 搜索用户（查询参数示例）
     * GET /api/users/search?name=张
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String name) {
        System.out.println("🔍 搜索用户，关键字: " + name);

        List<User> users = userService.searchByName(name);
        System.out.println("✅ 找到 " + users.size() + " 条记录");

        return ResponseEntity.ok(users);
    }
}

/**
 * 用户实体类
 */
class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;

    public User() {}

    public User(Long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}

/**
 * 用户服务类
 * 模拟数据库操作（使用内存存储）
 */
class UserService {
    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // 初始化测试数据
        create(new User(null, "张三", "zhangsan@example.com", 25));
        create(new User(null, "李四", "lisi@example.com", 30));
        create(new User(null, "王五", "wangwu@example.com", 28));
    }

    public List<User> findAll() {
        return new ArrayList<>(userDatabase.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userDatabase.get(id));
    }

    public User create(User user) {
        Long id = idGenerator.getAndIncrement();
        user.setId(id);
        userDatabase.put(id, user);
        return user;
    }

    public Optional<User> update(Long id, User user) {
        if (!userDatabase.containsKey(id)) {
            return Optional.empty();
        }
        user.setId(id);
        userDatabase.put(id, user);
        return Optional.of(user);
    }

    public boolean delete(Long id) {
        return userDatabase.remove(id) != null;
    }

    public List<User> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        return userDatabase.values().stream()
            .filter(user -> user.getName().contains(name))
            .toList();
    }
}
