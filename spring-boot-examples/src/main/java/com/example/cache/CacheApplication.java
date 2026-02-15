package com.example.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.*;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring Boot 缓存应用学习示例
 * 学习目标：
 * 1. 理解Spring Cache抽象层
 * 2. 掌握缓存注解的使用（@Cacheable、@CachePut、@CacheEvict）
 * 3. 学习缓存配置和管理
 * 4. 了解缓存策略和最佳实践
 *
 * 运行方式：
 * ./run-app.sh cache
 *
 * 测试接口：
 * # 第一次查询（慢，会缓存）
 * curl http://localhost:8080/api/users/1
 *
 * # 第二次查询（快，从缓存读取）
 * curl http://localhost:8080/api/users/1
 *
 * # 更新用户（更新缓存）
 * curl -X PUT http://localhost:8080/api/users/1 \
 *   -H "Content-Type: application/json" \
 *   -d '{"id":1,"name":"张三（已更新）","email":"zhangsan@example.com"}'
 *
 * # 删除用户（清除缓存）
 * curl -X DELETE http://localhost:8080/api/users/1
 *
 * # 查看缓存状态
 * curl http://localhost:8080/api/cache/stats
 */
@Slf4j
@SpringBootApplication
@EnableCaching  // 启用缓存支持
public class CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
        log.info("===========================================");
        log.info("缓存应用示例启动成功！");
        log.info("测试地址：http://localhost:8080");
        log.info("===========================================");
    }

    /**
     * 配置缓存管理器
     * 这里使用简单的ConcurrentMapCacheManager
     * 生产环境建议使用Redis、Caffeine等
     */
    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(
            "users",      // 用户缓存
            "products",   // 产品缓存
            "statistics"  // 统计缓存
        );
        log.info("缓存管理器初始化完成");
        return cacheManager;
    }

    /**
     * 初始化测试数据
     */
    @Bean
    CommandLineRunner initData(UserService userService, ProductService productService) {
        return args -> {
            // 创建测试用户
            User user1 = new User();
            user1.setId(1L);
            user1.setName("张三");
            user1.setEmail("zhangsan@example.com");
            user1.setAge(25);
            userService.createUser(user1);

            User user2 = new User();
            user2.setId(2L);
            user2.setName("李四");
            user2.setEmail("lisi@example.com");
            user2.setAge(30);
            userService.createUser(user2);

            User user3 = new User();
            user3.setId(3L);
            user3.setName("王五");
            user3.setEmail("wangwu@example.com");
            user3.setAge(28);
            userService.createUser(user3);

            // 创建测试产品
            Product product1 = new Product();
            product1.setId(1L);
            product1.setName("MacBook Pro");
            product1.setPrice(15999.00);
            product1.setStock(50);
            productService.createProduct(product1);

            Product product2 = new Product();
            product2.setId(2L);
            product2.setName("iPhone 15");
            product2.setPrice(5999.00);
            product2.setStock(100);
            productService.createProduct(product2);

            Product product3 = new Product();
            product3.setId(3L);
            product3.setName("AirPods Pro");
            product3.setPrice(1999.00);
            product3.setStock(200);
            productService.createProduct(product3);

            log.info("初始化测试数据完成");
        };
    }
}

/**
 * 用户实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime updatedAt = LocalDateTime.now();
}

/**
 * 产品实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
class Product {
    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private LocalDateTime updatedAt = LocalDateTime.now();
}

/**
 * 用户服务 - 演示缓存操作
 */
@Slf4j
@Service
class UserService {

    // 模拟数据库
    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();

    /**
     * 1. @Cacheable - 查询缓存
     * 如果缓存中存在，直接返回缓存数据
     * 如果缓存中不存在，执行方法并将结果放入缓存
     *
     * value/cacheNames: 缓存名称
     * key: 缓存key，支持SpEL表达式
     * condition: 缓存条件
     * unless: 排除条件
     */
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        log.info("从数据库查询用户：{}", id);
        // 模拟数据库查询延迟
        simulateSlowService();
        return userDatabase.get(id);
    }

    /**
     * 2. @Cacheable with condition
     * 只缓存年龄大于18的用户
     */
    @Cacheable(value = "users", key = "#id", condition = "#result != null && #result.age > 18")
    public User getUserByIdWithCondition(Long id) {
        log.info("条件缓存查询用户：{}", id);
        simulateSlowService();
        return userDatabase.get(id);
    }

    /**
     * 3. @CachePut - 更新缓存
     * 总是执行方法，并将结果更新到缓存
     * 用于更新操作，既更新数据库又更新缓存
     */
    @CachePut(value = "users", key = "#user.id")
    public User updateUser(User user) {
        log.info("更新用户：{}", user.getId());
        user.setUpdatedAt(LocalDateTime.now());
        userDatabase.put(user.getId(), user);
        return user;
    }

    /**
     * 4. @CacheEvict - 删除缓存
     * 执行方法后删除指定的缓存
     *
     * allEntries: 是否删除所有缓存
     * beforeInvocation: 是否在方法执行前删除
     */
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        log.info("删除用户：{}", id);
        userDatabase.remove(id);
    }

    /**
     * 5. @CacheEvict - 清空所有缓存
     */
    @CacheEvict(value = "users", allEntries = true)
    public void clearAllUsersCache() {
        log.info("清空所有用户缓存");
    }

    /**
     * 6. @Caching - 组合多个缓存操作
     */
    @Caching(
        cacheable = {
            @Cacheable(value = "users", key = "#id")
        },
        put = {
            @CachePut(value = "users", key = "#result.email")
        }
    )
    public User getUserWithMultipleCache(Long id) {
        log.info("多级缓存查询：{}", id);
        simulateSlowService();
        return userDatabase.get(id);
    }

    /**
     * 获取所有用户（不缓存）
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userDatabase.values());
    }

    /**
     * 创建用户
     */
    public User createUser(User user) {
        userDatabase.put(user.getId(), user);
        return user;
    }

    /**
     * 模拟慢速服务（数据库查询延迟）
     */
    private void simulateSlowService() {
        try {
            Thread.sleep(2000);  // 模拟2秒延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}

/**
 * 产品服务 - 演示缓存过期策略
 */
@Slf4j
@Service
class ProductService {

    private final Map<Long, Product> productDatabase = new ConcurrentHashMap<>();

    /**
     * 查询产品（缓存）
     */
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        log.info("从数据库查询产品：{}", id);
        try {
            Thread.sleep(1000);  // 模拟延迟
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return productDatabase.get(id);
    }

    /**
     * 更新产品库存（更新缓存）
     */
    @CachePut(value = "products", key = "#id")
    public Product updateStock(Long id, Integer quantity) {
        log.info("更新产品库存：{}, 数量：{}", id, quantity);
        Product product = productDatabase.get(id);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            product.setUpdatedAt(LocalDateTime.now());
        }
        return product;
    }

    /**
     * 批量删除产品缓存
     */
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductsCache() {
        log.info("清空所有产品缓存");
    }

    /**
     * 获取所有产品
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(productDatabase.values());
    }

    /**
     * 创建产品
     */
    public Product createProduct(Product product) {
        productDatabase.put(product.getId(), product);
        return product;
    }
}

/**
 * 统计服务 - 演示缓存统计
 */
@Slf4j
@Service
class StatisticsService {

    /**
     * 获取用户统计（短期缓存）
     */
    @Cacheable(value = "statistics", key = "'user-count'")
    public Map<String, Object> getUserStatistics() {
        log.info("计算用户统计...");
        // 模拟复杂计算
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 1000);
        stats.put("activeUsers", 750);
        stats.put("timestamp", LocalDateTime.now());
        return stats;
    }

    /**
     * 清除统计缓存
     */
    @CacheEvict(value = "statistics", allEntries = true)
    public void clearStatisticsCache() {
        log.info("清空统计缓存");
    }
}

/**
 * REST控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
class CacheController {

    private final UserService userService;
    private final ProductService productService;
    private final StatisticsService statisticsService;
    private final CacheManager cacheManager;

    public CacheController(UserService userService,
                          ProductService productService,
                          StatisticsService statisticsService,
                          CacheManager cacheManager) {
        this.userService = userService;
        this.productService = productService;
        this.statisticsService = statisticsService;
        this.cacheManager = cacheManager;
    }

    /**
     * 获取用户（演示缓存效果）
     */
    @GetMapping("/users/{id}")
    public Map<String, Object> getUser(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        User user = userService.getUserById(id);
        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("duration", duration + "ms");
        response.put("cached", duration < 100);  // 如果很快，说明是缓存
        return response;
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 更新用户（更新缓存）
     */
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    /**
     * 删除用户（清除缓存）
     */
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "用户删除成功，缓存已清除";
    }

    /**
     * 清空所有用户缓存
     */
    @DeleteMapping("/cache/users")
    public String clearUsersCache() {
        userService.clearAllUsersCache();
        return "用户缓存已清空";
    }

    /**
     * 获取产品
     */
    @GetMapping("/products/{id}")
    public Map<String, Object> getProduct(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        Product product = productService.getProductById(id);
        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> response = new HashMap<>();
        response.put("product", product);
        response.put("duration", duration + "ms");
        response.put("cached", duration < 100);
        return response;
    }

    /**
     * 更新产品库存
     */
    @PutMapping("/products/{id}/stock")
    public Product updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.updateStock(id, quantity);
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        long startTime = System.currentTimeMillis();
        Map<String, Object> stats = statisticsService.getUserStatistics();
        long duration = System.currentTimeMillis() - startTime;

        stats.put("queryDuration", duration + "ms");
        return stats;
    }

    /**
     * 查看缓存状态
     */
    @GetMapping("/cache/stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();

        // 获取所有缓存名称
        Collection<String> cacheNames = cacheManager.getCacheNames();
        stats.put("cacheNames", cacheNames);

        // 获取每个缓存的详情
        Map<String, Object> cacheDetails = new HashMap<>();
        for (String cacheName : cacheNames) {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                // ConcurrentMapCache可以获取native cache
                Object nativeCache = cache.getNativeCache();
                if (nativeCache instanceof ConcurrentHashMap) {
                    ConcurrentHashMap<?, ?> map = (ConcurrentHashMap<?, ?>) nativeCache;
                    Map<String, Object> detail = new HashMap<>();
                    detail.put("size", map.size());
                    detail.put("keys", map.keySet());
                    cacheDetails.put(cacheName, detail);
                }
            }
        }
        stats.put("cacheDetails", cacheDetails);

        return stats;
    }

    /**
     * 清空所有缓存
     */
    @DeleteMapping("/cache/all")
    public String clearAllCache() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
        return "所有缓存已清空";
    }

    /**
     * 帮助文档
     */
    @GetMapping("/help")
    public String help() {
        return """
            === Spring Boot 缓存应用示例 ===

            1. 查询用户（第一次慢，第二次快）
               GET http://localhost:8080/api/users/1

            2. 更新用户（更新缓存）
               PUT http://localhost:8080/api/users/1
               Body: {"name":"新名字","email":"new@example.com","age":30}

            3. 删除用户（清除缓存）
               DELETE http://localhost:8080/api/users/1

            4. 查看缓存状态
               GET http://localhost:8080/api/cache/stats

            5. 清空所有缓存
               DELETE http://localhost:8080/api/cache/all

            6. 获取统计信息（缓存3秒）
               GET http://localhost:8080/api/statistics

            核心知识点：
            - @EnableCaching：启用缓存支持
            - @Cacheable：查询缓存，不存在则执行方法
            - @CachePut：更新缓存，总是执行方法
            - @CacheEvict：删除缓存
            - @Caching：组合多个缓存操作
            - CacheManager：缓存管理器

            缓存策略：
            - Write-Through：写穿（更新时同时更新缓存）
            - Write-Behind：写后（异步更新缓存）
            - Cache-Aside：旁路（查询时加载缓存）

            注意事项：
            1. 同一类内部调用不会触发缓存（AOP限制）
            2. 缓存key要设计合理，避免冲突
            3. 注意缓存一致性问题
            4. 生产环境使用Redis等分布式缓存
            5. 设置合理的过期时间
            """;
    }
}
