package com.example.jpa;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.example.jpa.utils.SqlHelper;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackageClasses = { JpaApplication.class, SqlHelper.class })
public class JpaApplication {

    public static void main(String[] args) {

        SpringApplication.run(JpaApplication.class, args);

        System.out.println("\n✅ 应用启动成功！");
        System.out.println("📍 H2数据库控制台：http://localhost:8080/h2-console");
    }

    /**
     * CommandLineRunner：应用启动后自动执行
     * 用于演示JPA的各种操作
     */
    @Bean
    public CommandLineRunner demo(ProductService productService, SqlHelper sqlHelper) {
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
    private void demonstrateCRUD(ProductService productService, SqlHelper sqlHelper) {
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
        System.out.println("🔍 使用SQL工具查询:");
        // SqlBuilder builder = new SqlBuilder();
        // String sql = builder
        // .select("id, name, price, stock, created_at, updated_at")
        // .from("product")
        // .gt("price", 1000)
        // .buildSql();

        // Pageable firstPage = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC,
        // "stock"));
        // Page<Product> page = sqlPageHelper.pageQuery(sql, builder.getParams(),
        // firstPage, Product.class);
        // System.out.println("✅ 价格>1000，按价格降序，第一页结果:");
        // page.getContent().forEach(p -> System.out.println(
        // " - " + p.getName() + "，价格: ¥" + p.getPrice() + "，库存: " + p.getStock()));
        // System.out.println("📄 分页信息: page=" + (page.getNumber() + 1)
        // + ", size=" + page.getSize()
        // + ", total=" + page.getTotalElements());

        System.out.println("单个对象查询:");

        var sqlBuilder = new SqlHelper.SqlBuilder()
                .select("name,price,stock")
                .from("product")
                .eq("id", 401L);
        ProductDto pd = sqlHelper.queryForObject(sqlBuilder.buildSql(), sqlBuilder.getParams(), ProductDto.class);
        System.out.println("✅ 查询ID=401的产品: " + pd.getName() + "，价格: ¥" + pd.getPrice() + "，库存: "
                + pd.getStock());

        System.out.println();
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
