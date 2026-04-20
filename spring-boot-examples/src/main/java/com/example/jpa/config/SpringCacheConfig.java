package com.example.jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;

import jakarta.annotation.Nonnull;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

// example:
// @Service
// public class ProductService {

//     // 自动多级缓存：本地Caffeine -> Redis -> DB
//     @Cacheable(value = "product", key = "#id")
//     public ProductVO getProduct(Long id) {
//         // 只有缓存没有才会执行这里查数据库
//         return productMapper.selectById(id);
//     }

//     // 更新数据库后自动删除缓存
//     @CacheEvict(value = "product", key = "#product.id")
//     public void updateProduct(ProductVO product) {
//         productMapper.updateById(product);
//     }
// }

@Configuration // 告诉Spring：这是配置类，启动时加载
@EnableCaching // ✅ 开启 Spring Cache 注解功能（必须加！否则 @Cacheable 不生效）
public class SpringCacheConfig {

    private final ObjectMapper objectMapper;

    public SpringCacheConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 一级本地缓存 Caffeine
    private CaffeineCache localCache() {
        return new CaffeineCache(
                "localCache",
                Objects.requireNonNull(Caffeine.newBuilder()
                        .maximumSize(10000) // 最大1万条
                        .expireAfterWrite(1, TimeUnit.MINUTES) // 1分钟过期
                        .build()));
    }

    // 二级 Redis 缓存
    private RedisCacheManager redisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Objects.requireNonNull(Duration.ofMinutes(5)))
                .computePrefixWith(cacheName -> cacheName + ":")
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(Objects.requireNonNull(objectMapper))));

        return RedisCacheManager.builder(Objects.requireNonNull((factory))).cacheDefaults(config).build();
    }

    // 多级缓存：Caffeine + Redis
    @Bean
    public CacheManager cacheManager(@Nonnull RedisConnectionFactory factory) {
        org.springframework.cache.support.SimpleCacheManager localCacheManager = new org.springframework.cache.support.SimpleCacheManager();
        List<org.springframework.cache.Cache> caches = new ArrayList<>();
        caches.add(localCache());
        localCacheManager.setCaches(caches);
        localCacheManager.afterPropertiesSet();

        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        List<CacheManager> cacheManagers = new ArrayList<>();
        cacheManagers.add(localCacheManager);
        cacheManagers.add(redisCacheManager(factory));
        compositeCacheManager.setCacheManagers(cacheManagers);
        compositeCacheManager.afterPropertiesSet();
        return compositeCacheManager;
    }
}