package com.example.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

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
