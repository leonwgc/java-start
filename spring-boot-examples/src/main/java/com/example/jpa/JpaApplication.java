package com.example.jpa;

import jakarta.persistence.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.utils.DynamicSqlBuilder;
import com.example.utils.NativeSqlPageHelper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Spring Data JPA 实战
 * 学习目标：
 * 1. 理解JPA和Hibernate的关系
 * 2. 掌握实体类的定义和注解
 * 3. 学会使用Spring Data JPA Repository
 * 4. 了解JPQL查询语言
 *
 * JPA是什么？
 * - JPA：Java Persistence API（Java持久化API）
 * - ORM：对象关系映射（Object-Relational Mapping）
 * - Hibernate：JPA的实现之一（Spring Boot默认使用）
 * - 简化数据库操作，无需编写SQL
 *
 * Spring Data JPA核心：
 * - Repository接口：自动实现CRUD操作
 * - 方法命名规则：findByXxx、deleteByXxx等
 * - @Query注解：自定义JPQL查询
 * - 分页和排序支持
 *
 * 关联前面的学习：
 * - JdbcDemo.java：手动编写SQL
 * - JPA：自动生成SQL，更加简洁
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {JpaApplication.class, NativeSqlPageHelper.class})
public class JpaApplication {

    public static void main(String[] args) {
        System.out.println("=== Spring Data JPA 实战 ===\n");
        System.out.println("正在启动JPA示例应用...\n");

        SpringApplication.run(JpaApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 H2数据库控制台：http://localhost:8080/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("   Username: sa");
        System.out.println("   Password: (留空)\n");
    }

    /**
     * CommandLineRunner：应用启动后自动执行
     * 用于演示JPA的各种操作
     */
    @Bean
    public CommandLineRunner demo(ProductService productService, NativeSqlPageHelper sqlHelper) {
        return args -> {
            System.out.println("\n=== 开始JPA操作演示 ===\n");

            demonstrateCRUD(productService, sqlHelper);
            // demonstrateQuery(productService, sqlHelper);
            // demonstrateUpdate(productService);
        };
    }

    /**
     * 1. CRUD操作演示
     */
    private void demonstrateCRUD(ProductService productService, NativeSqlPageHelper sqlHelper) {
        System.out.println("1. CRUD操作演示\n");

        // 创建产品
        System.out.println("➕ 创建50个产品...");
        String[] names = { "笔记本电脑", "无线鼠标", "机械键盘", "显示器", "耳机",
                "摄像头", "麦克风", "硬盘", "固态硬盘", "内存条" };
        Product laptop = null;
        for (int i = 0; i < 50; i++) {
            String name = names[i % names.length] + "-" + (i + 1);
            double price = Math.round((50 + Math.random() * 9950) * 100.0) / 100.0;
            int stock = (int) (Math.random() * 200) + 1;
            Product p = productService.createProduct(name, price, stock);
            if (i == 0)
                laptop = p;
        }
        System.out.println("✅ 创建了50个产品\n");

        // 查询所有产品
        System.out.println("📋 查询所有产品:");

        // 使用 DynamicSqlBuilder + NativeSqlPageHelper 查询库存最多的10个产品
        System.out.println("🔍 使用SQL工具查询库存最多的10个产品:");
        DynamicSqlBuilder builder = new DynamicSqlBuilder();
        String sql = builder
                .select("id, name, price, stock, created_at, updated_at")
                .from("product")
                .buildSql() + " ORDER BY stock DESC LIMIT 10";
        List<Object[]> top10 = sqlHelper.listQuery(sql, Map.of());
        top10.forEach(row -> System.out.println("  - " + row[1] + "，库存: " + row[3]));
        System.out.println();
        // List<Product> allProducts = productService.findAll();
        // allProducts.forEach(p ->
        // System.out.println(" - " + p.getName() + "，价格: ¥" + p.getPrice() + "，库存: " +
        // p.getStock())
        // );

        System.out.println();

        // 根据ID查询
        // System.out.println("🔍 查询ID为1的产品:");
        // Optional<Product> product = productService.findById(laptop.getId());
        // product.ifPresent(p ->
        // System.out.println(" 找到: " + p.getName() + "，创建时间: " + p.getCreatedAt())
        // );
        // System.out.println();

        // // 删除产品
        // System.out.println("🗑️ 删除产品ID: " + laptop.getId());
        // productService.deleteProduct(laptop.getId());
        // System.out.println("✅ 删除成功");
        // System.out.println("📋 当前产品数量: " + productService.count() + "\n");
    }

    /**
     * 2. 查询方法演示
     */
    private void demonstrateQuery(ProductService productService) {
        System.out.println("2. 查询方法演示\n");

        // 按名称查询
        System.out.println("🔍 查询名称包含'键盘'的产品:");
        List<Product> keyboards = productService.findByNameContaining("键盘");
        keyboards.forEach(p -> System.out.println("  - " + p.getName()));
        System.out.println();

        // 按价格范围查询
        System.out.println("🔍 查询价格在100-500之间的产品:");
        List<Product> priceRange = productService.findByPriceRange(100.0, 500.0);
        priceRange.forEach(p -> System.out.println("  - " + p.getName() + "，价格: ¥" + p.getPrice()));
        System.out.println();

        // 按库存查询
        System.out.println("🔍 查询低库存产品（库存<100）:");
        List<Product> lowStock = productService.findLowStockProducts(100);
        lowStock.forEach(p -> System.out.println("  - " + p.getName() + "，库存: " + p.getStock()));
        System.out.println();

    }

    /**
     * 3. 更新操作演示
     */
    private void demonstrateUpdate(ProductService productService) {
        System.out.println("3. 更新操作演示\n");

        List<Product> products = productService.findAll();
        if (!products.isEmpty()) {
            Product product = products.get(0);
            System.out.println("✏️  更新产品: " + product.getName());
            System.out.println("  原价格: ¥" + product.getPrice());

            product.setPrice(6999.00);
            product.setStock(60);
            productService.updateProduct(product);

            System.out.println("  新价格: ¥" + product.getPrice());
            System.out.println("  新库存: " + product.getStock());
            System.out.println("✅ 更新成功\n");
        }
    }
}

/**
 * 产品实体类
 * 对应数据库表：product
 *
 * JPA注解说明：
 * - @Entity：标记为JPA实体类
 * - @Table：指定表名（可选，默认为类名）
 * - @Id：主键字段
 * - @GeneratedValue：主键生成策略
 * - @Column：列属性配置
 */
@Entity
@Table(name = "product")
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String name, Double price, Integer stock) {
        this();
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // 更新前自动设置更新时间
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

/**
 * 产品Repository接口
 * 继承JpaRepository，自动获得CRUD方法
 *
 * 方法命名规则：
 * - findBy...：查询方法
 * - deleteBy...：删除方法
 * - countBy...：计数方法
 * - existsBy...：存在性检查
 *
 * 支持的关键字：
 * - And、Or：逻辑运算
 * - Between：范围查询
 * - LessThan、GreaterThan：大小比较
 * - Like、Containing：模糊查询
 * - OrderBy：排序
 */
@Repository
interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据名称模糊查询
     * 自动生成SQL: SELECT * FROM product WHERE name LIKE %?%
     */
    List<Product> findByNameContaining(String name);

    /**
     * 按价格范围查询
     * 自动生成SQL: SELECT * FROM product WHERE price BETWEEN ? AND ?
     */
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * 按库存查询并排序
     * 自动生成SQL: SELECT * FROM product WHERE stock < ? ORDER BY stock ASC
     */
    List<Product> findByStockLessThanOrderByStockAsc(Integer stock);

    /**
     * 自定义JPQL查询
     * JPQL：使用实体类名和属性名（不是表名和列名）
     */
    @Query("SELECT p FROM Product p WHERE p.price > :minPrice ORDER BY p.price DESC")
    List<Product> findExpensiveProducts(Double minPrice);

    /**
     * 使用原生SQL查询
     */
    @Query(value = "SELECT * FROM product WHERE stock = 0", nativeQuery = true)
    List<Product> findOutOfStockProducts();
}

/**
 * 产品服务类
 * 业务逻辑层，调用Repository进行数据访问
 */
@Service
class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, Double price, Integer stock) {
        Product product = new Product(name, price, stock);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public long count() {
        return productRepository.count();
    }

    public List<Product> findByNameContaining(String name) {
        return productRepository.findByNameContaining(name);
    }

    public List<Product> findByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> findLowStockProducts(Integer threshold) {
        return productRepository.findByStockLessThanOrderByStockAsc(threshold);
    }
}
