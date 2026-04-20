package com.example.jpa.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
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

    // 一级本地缓存 Caffeine：1分钟过期
    private CaffeineCache localCache() {
        return new CaffeineCache(
                "localCache",
                Objects.requireNonNull(Caffeine.newBuilder()
                        .maximumSize(10000) // 最大1万条缓存，超过就LRU淘汰
                        .expireAfterWrite(1, TimeUnit.MINUTES) // 写入后1分钟过期
                        .build()));
    }

    // 二级Redis缓存：5分钟过期
    private RedisCacheManager redisCacheManager(@NonNull RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Objects.requireNonNull(java.time.Duration.ofMinutes(5)))
                .computePrefixWith(cacheName -> cacheName + ":") // 缓存key前缀，默认是 cacheName::key，这里改成 cacheName:key
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(
                                new GenericJackson2JsonRedisSerializer(Objects.requireNonNull(objectMapper()))));

        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }

    // 组合多级缓存：先本地 -> 再Redis
    @Bean
    public CacheManager cacheManager(@NonNull RedisConnectionFactory factory) {
        // 创建本地缓存管理器
        org.springframework.cache.support.SimpleCacheManager localCacheManager = new org.springframework.cache.support.SimpleCacheManager();
        List<org.springframework.cache.Cache> caches = new ArrayList<>();
        caches.add(localCache());
        localCacheManager.setCaches(caches);
        localCacheManager.afterPropertiesSet();

        // 创建组合缓存管理器
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        List<CacheManager> cacheManagers = new ArrayList<>();
        cacheManagers.add(localCacheManager);
        cacheManagers.add(redisCacheManager(factory));
        compositeCacheManager.setCacheManagers(cacheManagers);
        compositeCacheManager.afterPropertiesSet();
        return compositeCacheManager;
    }

    // Jackson序列化，SpringBoot3标准，无fastjson
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, 
                JsonTypeInfo.As.PROPERTY);
        return mapper;
    }
}