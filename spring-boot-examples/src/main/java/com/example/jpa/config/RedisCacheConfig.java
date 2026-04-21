package com.example.jpa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

// Spring Cache 配置类，使用 Redis 作为缓存存储, 有个问题就是会 加上 @class 到序列化后的 JSON 中，导致 Redis 中存储的值不是纯净的 JSON 字符串，而是包含类型信息的 JSON，反序列化时会丢失类型信息，变成 LinkedHashMap。
// 使用 RedisUtils 直接操作 Redis 时，我们使用了 JSONHelper 来处理 JSON 序列化和反序列化，JSONHelper 内部使用了 Spring 注入的 ObjectMapper，这个 ObjectMapper 没有启用默认类型信息，所以不会在 JSON 中添加 @class 字段，保证了 Redis 中存储的值是纯净的 JSON 字符串，反序列化时也能正确还原为 Product 对象。
@Configuration
@EnableCaching
@Deprecated(since = "2024-06", forRemoval = true) // 这个类已经废弃，直接使用 RedisUtils 操作 Redis 就行了，不需要通过 Spring Cache 抽象了
public class RedisCacheConfig {

        @Value("${spring.cache.redis.time-to-live:300000}")
        private long timeToLive; // 毫秒

        @Value("${spring.cache.redis.key-prefix:cache:}")
        private String keyPrefix;

        @Bean
        CacheManager cacheManager(
                        RedisConnectionFactory factory, ObjectMapper springRedisObjectMapper) {

                GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer(
                                springRedisObjectMapper);

                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMillis(timeToLive))
                                .computePrefixWith(cacheName -> keyPrefix + cacheName + ":")
                                .serializeKeysWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                                valueSerializer));

                return RedisCacheManager.builder(factory).cacheDefaults(config).build();
        }

}