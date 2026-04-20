package com.example.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * 产品服务类
 * 业务逻辑层，调用Repository进行数据访问
 */
@Service
class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(String name, Double price, Integer stock) {
        Product product = new Product(name, price, stock);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Cacheable(value = "product", key = "#id")
    public Optional<Product> findById(@NonNull Long id) {
        return productRepository.findById(id);
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
