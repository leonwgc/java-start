package com.example.jpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.example.utils.DynamicSqlBuilder;
import com.example.utils.NativeSqlPageHelper;
import java.util.List;
import java.util.Map;

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
@ComponentScan(basePackageClasses = { JpaApplication.class, NativeSqlPageHelper.class })
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
        // System.out.println("➕ 创建100个产品...");
        // String[] names = { "笔记本电脑", "无线鼠标", "机械键盘", "显示器", "耳机",
        // "摄像头", "麦克风", "硬盘", "固态硬盘", "内存条" };
        // Product laptop = null;
        // for (int i = 0; i < 100; i++) {
        // String name = names[i % names.length] + "-" + (i + 1);
        // double price = Math.round((50 + Math.random() * 9950) * 100.0) / 100.0;
        // int stock = (int) (Math.random() * 200) + 1;
        // Product p = productService.createProduct(name, price, stock);
        // if (i == 0)
        // laptop = p;
        // }
        // System.out.println("✅ 创建了100个产品\n");

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
