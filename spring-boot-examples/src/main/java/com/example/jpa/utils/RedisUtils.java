package com.example.jpa.utils;

import org.springframework.data.redis.core.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Resource
    private StringRedisTemplate redisTemplate;

    // ====================== 1.String 字符串（最常用 80%场景）======================
    /** 设置缓存，永不过期 */
    public void set(@NonNull String key, @NonNull String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /** 设置缓存+过期时间 */
    public void set(@NonNull String key, @NonNull String value, long time, @NonNull TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    /** 获取缓存 */
    public String get(@NonNull String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /** 删除 */
    public Boolean del(@NonNull String key) {
        return redisTemplate.delete(key);
    }

    /** 计数器自增（限流、id生成、访问量） */
    public Long incr(@NonNull String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /** 设置过期时间 */
    public Boolean expire(@NonNull String key, long time, @NonNull TimeUnit unit) {
        return redisTemplate.expire(key, time, unit);
    }

    // ====================== 2.Hash 哈希（存对象、购物车、用户信息）======================
    /** Hash设置单个字段 */
    public void hset(@NonNull String key, @NonNull String field, @NonNull String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /** Hash设置多个字段（存整个对象） */
    public void hsetAll(@NonNull String key, @NonNull Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /** Hash获取单个字段 */
    public String hget(@NonNull String key, @NonNull String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    /** Hash获取所有字段 */
    @SuppressWarnings("unchecked")
    public Map<String, String> hgetAll(@NonNull String key) {
        return (Map<String, String>) (Map<?, ?>) redisTemplate.opsForHash().entries(key);
    }

    // ====================== 3.ZSet 有序集合（排行榜、热度、排序）======================
    /** ZSet添加分数 */
    public Boolean zadd(@NonNull String key, @NonNull String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /** 倒序排行（热门榜TOP N） */
    public Set<String> zrevrange(@NonNull String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    // ====================== 4.Set 集合（去重、点赞、关注）======================
    public void sadd(@NonNull String key, @NonNull String... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /** 判断是否存在 */
    public Boolean sismember(@NonNull String key, @NonNull String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    // ====================== 5.List 列表（队列、消息记录、时间线）======================
    public void lpush(@NonNull String key, @NonNull String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public String rpop(@NonNull String key) {
        return redisTemplate.opsForList().rightPop(key);
    }
}