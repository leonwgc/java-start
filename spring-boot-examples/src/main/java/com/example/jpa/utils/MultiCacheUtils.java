package com.example.jpa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.Resource;
import org.springframework.lang.NonNull;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

// ProductVO product = multiCacheUtils.get(
//     "product:100",
//     1800,    // Redis 30分钟
//     ProductVO.class,
//     () -> productMapper.selectById(100)
// );

/**
 * 多级缓存工具类
 * 缓存层级：Caffeine本地缓存 → Redis缓存 → 数据库
 */
@Component
public class MultiCacheUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    @Qualifier("objectMapper")
    private ObjectMapper objectMapper;

    /**
     * Caffeine本地缓存配置
     */
    private final Cache<String, String> localCache = Caffeine.newBuilder()
            .maximumSize(10000) // 最大缓存数量
            .expireAfterWrite(1, TimeUnit.MINUTES) // 写入后1分钟过期
            .softValues() // 内存不足时自动回收（优化内存）
            .build();

    /**
     * 通用缓存获取方法
     *
     * @param key                缓存key
     * @param redisExpireSeconds Redis过期时间（秒）
     * @param clazz              返回类型
     * @param dbSupplier         数据库查询逻辑
     * @return 缓存数据
     */
    public <T> T get(@NonNull String key, long redisExpireSeconds, @NonNull Class<T> clazz,
            @NonNull Supplier<T> dbSupplier) {
        // 1. 先查本地缓存 Caffeine
        String localVal = localCache.getIfPresent(key);
        if (localVal != null) {
            try {
                return objectMapper.readValue(localVal, clazz);
            } catch (Exception ignored) {
                // 反序列化失败，删除脏缓存
                localCache.invalidate(key);
            }
        }

        // 2. 再查 Redis
        String redisVal = stringRedisTemplate.opsForValue().get(key);
        if (redisVal != null) {
            // 回写到本地缓存
            localCache.put(key, redisVal);
            try {
                return objectMapper.readValue(redisVal, clazz);
            } catch (Exception ignored) {
                // Redis 脏数据清理
                stringRedisTemplate.delete(key);
            }
        }

        // 3. 最后查数据库
        T dbData = dbSupplier.get();
        if (dbData == null) {
            return null;
        }

        // 4. 数据回填到 Redis + 本地缓存
        try {
            String json = objectMapper.writeValueAsString(dbData);
            // 写入Redis
            stringRedisTemplate.opsForValue().set(key, json, redisExpireSeconds, TimeUnit.SECONDS);
            // 写入本地缓存
            localCache.put(key, json);
        } catch (JsonProcessingException ignored) {
            // 序列化失败不影响业务返回
        }

        return dbData;
    }

    /**
     * 清理缓存（同时清理本地+Redis）
     */
    public void delete(@NonNull String key) {
        localCache.invalidate(key);
        stringRedisTemplate.delete(key);
    }
}