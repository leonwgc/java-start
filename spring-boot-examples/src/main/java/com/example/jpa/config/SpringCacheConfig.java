package com.example.jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.lang.NonNull;

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

@Configuration
@EnableCaching
public class SpringCacheConfig {

    private CaffeineCache localCache() {
        return new CaffeineCache("localCache",
                Caffeine.newBuilder()
                        .maximumSize(10000)
                        .expireAfterWrite(1, TimeUnit.MINUTES)
                        .build());
    }

    // Redis 缓存使用专用的 redisObjectMapper（包含 @class 类型信息）
    private RedisCacheManager redisCacheManager(
            RedisConnectionFactory factory,
            ObjectMapper redisObjectMapper) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .computePrefixWith(cacheName -> cacheName + ":")
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(redisObjectMapper)));

        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }

    @Bean
    public CacheManager cacheManager(
            @NonNull RedisConnectionFactory factory,
            @Qualifier("redisObjectMapper") ObjectMapper redisObjectMapper) {
        var localCacheManager = new org.springframework.cache.support.SimpleCacheManager();
        localCacheManager.setCaches(List.of(localCache()));
        localCacheManager.afterPropertiesSet();

        var composite = new CompositeCacheManager();
        composite.setCacheManagers(List.of(
                localCacheManager,
                redisCacheManager(factory, redisObjectMapper)));
        composite.afterPropertiesSet();
        return composite;
    }
}