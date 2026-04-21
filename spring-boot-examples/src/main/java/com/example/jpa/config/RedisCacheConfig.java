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

@Configuration
@EnableCaching
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