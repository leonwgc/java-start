package com.example.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


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
@Getter
@Setter
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
}
