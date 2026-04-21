package com.example.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.jpa.entity.Product;
import com.example.jpa.projection.ProductProject;
import com.example.jpa.repository.ProductRepository;
import com.example.jpa.utils.JSONHelper;
import com.example.jpa.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 产品服务类
 * 业务逻辑层，调用Repository进行数据访问
 */
@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("objectMapper")
    ObjectMapper objectMapper;

    @Autowired
    JSONHelper jsonHelper;

    public Product createProduct(String name, Double price, Integer stock) {
        Product product = new Product(name, price, stock);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * 先查 Redis -> 没有再查 DB -> 查到后存入 Redis
     */
    public Product findById(@NonNull Long id) {
        String key = "product:" + id;

        // 2. 先从 Redis 获取
        String productJson = redisUtils.get(key);
        if (productJson != null) {

            return jsonHelper.fromJson(productJson, Product.class, e -> {
                log.error("【Redis 反序列化失败】key: {}, json: {}", key, productJson, e);
                // 反序列化失败，删除缓存，避免下次继续失败
                redisUtils.del(key);
            });

        }

        // 3. Redis 没有，查数据库
        log.info("【从数据库查询】产品 ID: {}", id);
        Product product = productRepository.findById(id).orElse(null);

        // 4. 查到了，存入 Redis（设置过期时间 30 分钟）
        if (product != null) {
            try {
                var str = jsonHelper.toJson(product, e -> {
                    log.error("【Redis 序列化失败】key: {}, product: {}", key, product, e);
                });
                if (StringUtils.hasText(str)) {
                    redisUtils.set(key, str, 30, TimeUnit.MINUTES);
                }
            } catch (Exception e) {
                log.error("【Redis 序列化失败】key: {}, product: {}", key, product, e);
            }
        }

        return product;
    }

    @NonNull
    public Product updateProduct(@NonNull Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(@NonNull Long id) {
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

    public ProductProject findProjectedById(Long id) {
        return productRepository.findProjectedById(id);
    }

}
