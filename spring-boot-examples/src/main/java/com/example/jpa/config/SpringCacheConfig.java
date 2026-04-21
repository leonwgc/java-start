package com.example.jpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

import java.time.Duration;

@Configuration
@EnableCaching
public class SpringCacheConfig {

        @Value("${spring.cache.redis.time-to-live:300000}")
        private long timeToLive; // 毫秒

        @Value("${spring.cache.redis.key-prefix:cache:}")
        private String keyPrefix;

        // Spring Redis 缓存使用专用的 springRedisObjectMapper（包含 @class 类型信息）
        @Bean
        public CacheManager cacheManager(
                        @NonNull RedisConnectionFactory factory,
                        @Qualifier("springRedisObjectMapper") ObjectMapper redisObjectMapper) {
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMillis(timeToLive))
                                .computePrefixWith(cacheName -> keyPrefix + cacheName + ":")
                                .serializeKeysWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                                new GenericJackson2JsonRedisSerializer(redisObjectMapper)));

                return RedisCacheManager.builder(factory).cacheDefaults(config).build();
        }
}