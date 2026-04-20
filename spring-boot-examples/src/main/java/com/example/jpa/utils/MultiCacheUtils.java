package com.example.jpa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.lang.NonNull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

// ProductVO product = multiCacheUtils.get(
//     "product:100",
//     1800,    // Redis 30分钟
//     ProductVO.class,
//     () -> productMapper.selectById(100)
// );

@Component
public class MultiCacheUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Cache<String, String> localCache = Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @PostConstruct
    public void init() {
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public <T> T get(@NonNull String key, long redisExpireSeconds, @NonNull Class<T> clazz,
            @NonNull Supplier<T> dbSupplier) {
        // 1. 本地缓存
        String localVal = localCache.getIfPresent(key);
        if (localVal != null) {
            try {
                return objectMapper.readValue(localVal, clazz);
            } catch (Exception ignored) {
            }
        }

        // 2. Redis
        String redisVal = stringRedisTemplate.opsForValue().get(key);
        if (redisVal != null) {
            localCache.put(key, redisVal);
            try {
                return objectMapper.readValue(redisVal, clazz);
            } catch (Exception ignored) {
            }
        }

        // 3. DB
        T dbData = dbSupplier.get();
        if (dbData == null) {
            return null;
        }

        // 4. 回填缓存
        try {
            String json = Objects.requireNonNull(objectMapper.writeValueAsString(dbData));
            stringRedisTemplate.opsForValue().set(key, json, redisExpireSeconds, TimeUnit.SECONDS);
            localCache.put(key, json);
        } catch (JsonProcessingException ignored) {
        }

        return dbData;
    }

    public void delete(@NonNull String key) {
        stringRedisTemplate.delete(key);
        localCache.invalidate(key);
    }
}