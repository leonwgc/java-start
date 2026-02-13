package advanced;

import java.util.*;
import java.util.concurrent.*;

/**
 * 缓存机制详解
 * 学习目标：
 * 1. 理解缓存的作用和原理
 * 2. 掌握LRU、LFU等缓存淘汰策略
 * 3. 学习缓存的实际应用场景
 * 4. 了解分布式缓存的基本概念
 *
 * 缓存是什么？
 * - 将数据存储在快速访问的存储介质中
 * - 减少数据库查询，提升系统性能
 * - 常用策略：LRU（最近最少使用）、LFU（最不经常使用）
 * - 需要考虑缓存穿透、缓存雪崩、缓存击穿等问题
 *
 * Spring应用：
 * - @Cacheable、@CacheEvict等注解
 * - Spring Cache抽象
 * - Redis分布式缓存
 * - 本地缓存+分布式缓存多级缓存
 */
public class CacheDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 缓存机制详解 ===\n");

        demonstrateLRUCache();
        demonstrateCacheWithExpiration();
        demonstrateCacheStrategies();
        demonstrateCacheProblems();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. LRU缓存实现
     */
    private static void demonstrateLRUCache() {
        System.out.println("1. LRU（最近最少使用）缓存\n");

        LRUCache<String, String> cache = new LRUCache<>(3);

        System.out.println("缓存容量: 3");
        System.out.println("\n添加元素:");
        cache.put("A", "Value A");
        System.out.println("  添加 A -> " + cache);

        cache.put("B", "Value B");
        System.out.println("  添加 B -> " + cache);

        cache.put("C", "Value C");
        System.out.println("  添加 C -> " + cache);

        System.out.println("\n访问元素 A（A变为最近使用）:");
        cache.get("A");
        System.out.println("  " + cache);

        System.out.println("\n添加元素 D（B最久未使用，将被淘汰）:");
        cache.put("D", "Value D");
        System.out.println("  " + cache);

        System.out.println("\nLRU策略: 淘汰最久未被访问的数据\n");
    }

    /**
     * 2. 带过期时间的缓存
     */
    private static void demonstrateCacheWithExpiration() throws Exception {
        System.out.println("2. 带过期时间的缓存\n");

        CacheWithExpiration<String, String> cache = new CacheWithExpiration<>();

        System.out.println("添加缓存项（TTL=2秒）:");
        cache.put("user:1", "张三", 2000);
        cache.put("user:2", "李四", 2000);

        System.out.println("  立即获取 user:1 -> " + cache.get("user:1"));
        System.out.println("  立即获取 user:2 -> " + cache.get("user:2"));

        System.out.println("\n等待1秒...");
        Thread.sleep(1000);
        System.out.println("  1秒后获取 user:1 -> " + cache.get("user:1"));

        System.out.println("\n等待2秒...");
        Thread.sleep(2000);
        System.out.println("  3秒后获取 user:1 -> " + cache.get("user:1") + " (已过期)");
        System.out.println("  3秒后获取 user:2 -> " + cache.get("user:2") + " (已过期)");

        System.out.println("\n过期时间确保缓存数据的时效性\n");
    }

    /**
     * 3. 缓存策略对比
     */
    private static void demonstrateCacheStrategies() {
        System.out.println("3. 常见缓存策略\n");

        System.out.println("📌 1. LRU (Least Recently Used - 最近最少使用)");
        System.out.println("   原理: 淘汰最久未被访问的数据");
        System.out.println("   实现: LinkedHashMap（保持访问顺序）");
        System.out.println("   适用: 热点数据访问场景");
        System.out.println("   Java: LinkedHashMap(capacity, 0.75f, true)");

        System.out.println("\n📌 2. LFU (Least Frequently Used - 最不经常使用)");
        System.out.println("   原理: 淘汰访问频率最低的数据");
        System.out.println("   实现: 维护访问计数器");
        System.out.println("   适用: 需要考虑访问频率的场景");
        System.out.println("   特点: 能更好识别热点数据");

        System.out.println("\n📌 3. FIFO (First In First Out - 先进先出)");
        System.out.println("   原理: 按照数据进入的顺序淘汰");
        System.out.println("   实现: 队列");
        System.out.println("   适用: 简单缓存场景");
        System.out.println("   特点: 实现简单但可能淘汰热点数据");

        System.out.println("\n📌 4. TTL (Time To Live - 生存时间)");
        System.out.println("   原理: 设置数据过期时间");
        System.out.println("   实现: 存储时间戳，定期清理");
        System.out.println("   适用: 数据有时效性的场景");
        System.out.println("   Redis: EXPIRE key seconds");

        System.out.println("\n📌 5. Write-Through（写穿）");
        System.out.println("   原理: 更新缓存的同时更新数据库");
        System.out.println("   优点: 缓存和数据库数据一致");
        System.out.println("   缺点: 写操作延迟较高");

        System.out.println("\n📌 6. Write-Back（写回）");
        System.out.println("   原理: 只更新缓存，异步写入数据库");
        System.out.println("   优点: 写操作快");
        System.out.println("   缺点: 可能丢失未持久化的数据");
        System.out.println();
    }

    /**
     * 4. 缓存常见问题
     */
    private static void demonstrateCacheProblems() {
        System.out.println("4. 缓存常见问题及解决方案\n");

        System.out.println("❌ 问题1: 缓存穿透（查询不存在的数据）");
        System.out.println("   现象: 大量请求查询不存在的key，直达数据库");
        System.out.println("   危害: 数据库压力巨大");
        System.out.println("   解决:");
        System.out.println("     1. 布隆过滤器（Bloom Filter）");
        System.out.println("     2. 缓存空值（设置短过期时间）");
        System.out.println("     3. 参数校验");

        System.out.println("\n❌ 问题2: 缓存击穿（热点key过期）");
        System.out.println("   现象: 热点key突然过期，大量请求直达数据库");
        System.out.println("   危害: 数据库瞬时压力巨大");
        System.out.println("   解决:");
        System.out.println("     1. 热点数据永不过期");
        System.out.println("     2. 互斥锁（只允许一个线程查询数据库）");
        System.out.println("     3. 提前更新（后台线程定期刷新）");

        System.out.println("\n❌ 问题3: 缓存雪崩（大量key同时过期）");
        System.out.println("   现象: 大量key同时过期，瞬间压力全部到数据库");
        System.out.println("   危害: 数据库可能崩溃");
        System.out.println("   解决:");
        System.out.println("     1. 过期时间随机化（加上随机值）");
        System.out.println("     2. 多级缓存（本地缓存+Redis）");
        System.out.println("     3. 限流降级");
        System.out.println("     4. Redis高可用（集群、哨兵）");

        System.out.println("\n❌ 问题4: 缓存一致性");
        System.out.println("   现象: 缓存和数据库数据不一致");
        System.out.println("   解决:");
        System.out.println("     1. 先更新数据库，再删除缓存（推荐）");
        System.out.println("     2. 延迟双删（删除-更新-延迟删除）");
        System.out.println("     3. 设置合理的过期时间");
        System.out.println("     4. 使用消息队列保证最终一致性");
        System.out.println();
    }

    /**
     * 5. 实际应用场景
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("5. 实际应用场景\n");

        // 场景1: 用户信息缓存
        System.out.println("场景1: 用户信息缓存");
        UserCacheService userCache = new UserCacheService();

        System.out.println("  第1次查询（从数据库）:");
        User user1 = userCache.getUserById("user123");
        System.out.println("    " + user1);

        System.out.println("  第2次查询（从缓存）:");
        User user2 = userCache.getUserById("user123");
        System.out.println("    " + user2);

        // 场景2: 接口限流缓存
        System.out.println("\n场景2: 接口限流（基于缓存）");
        RateLimiterCache limiter = new RateLimiterCache(3, 5000); // 5秒内最多3次

        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.allowRequest("api:user:list", "user123");
            System.out.println("  请求#" + i + ": " + (allowed ? "✅ 允许" : "❌ 限流"));
        }

        // 场景3: 配置缓存
        System.out.println("\n场景3: 系统配置缓存");
        ConfigCache configCache = new ConfigCache();

        System.out.println("  获取配置（第1次，从数据库）:");
        String config1 = configCache.getConfig("system.timeout");
        System.out.println("    system.timeout = " + config1);

        System.out.println("  获取配置（第2次，从缓存）:");
        String config2 = configCache.getConfig("system.timeout");
        System.out.println("    system.timeout = " + config2);

        System.out.println("\n  刷新配置（清空缓存）:");
        configCache.refreshConfig("system.timeout");
        System.out.println("  再次获取（从数据库）:");
        String config3 = configCache.getConfig("system.timeout");
        System.out.println("    system.timeout = " + config3);
        System.out.println();
    }

    // ==================== LRU缓存实现 ====================

    /**
     * LRU缓存实现（基于LinkedHashMap）
     */
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75f, true); // true表示按访问顺序
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // ==================== 带过期时间的缓存 ====================

    static class CacheWithExpiration<K, V> {
        private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();

        static class CacheEntry<V> {
            V value;
            long expirationTime;

            CacheEntry(V value, long ttl) {
                this.value = value;
                this.expirationTime = System.currentTimeMillis() + ttl;
            }

            boolean isExpired() {
                return System.currentTimeMillis() > expirationTime;
            }
        }

        public void put(K key, V value, long ttl) {
            cache.put(key, new CacheEntry<>(value, ttl));
        }

        public V get(K key) {
            CacheEntry<V> entry = cache.get(key);
            if (entry == null) {
                return null;
            }
            if (entry.isExpired()) {
                cache.remove(key);
                return null;
            }
            return entry.value;
        }
    }

    // ==================== 实际应用示例 ====================

    static class User {
        String id;
        String name;

        User(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{id='" + id + "', name='" + name + "'}";
        }
    }

    /**
     * 用户缓存服务
     */
    static class UserCacheService {
        private final Map<String, User> cache = new ConcurrentHashMap<>();

        public User getUserById(String userId) {
            // 先查缓存
            User user = cache.get(userId);
            if (user != null) {
                System.out.println("    [缓存命中]");
                return user;
            }

            // 缓存未命中，查询数据库
            System.out.println("    [查询数据库]");
            user = queryFromDatabase(userId);

            // 写入缓存
            if (user != null) {
                cache.put(userId, user);
            }

            return user;
        }

        private User queryFromDatabase(String userId) {
            // 模拟数据库查询
            return new User(userId, "用户_" + userId);
        }
    }

    /**
     * 限流器缓存
     */
    static class RateLimiterCache {
        private final Map<String, Queue<Long>> requestTimestamps = new ConcurrentHashMap<>();
        private final int maxRequests;
        private final long windowMs;

        public RateLimiterCache(int maxRequests, long windowMs) {
            this.maxRequests = maxRequests;
            this.windowMs = windowMs;
        }

        public synchronized boolean allowRequest(String api, String userId) {
            String key = api + ":" + userId;
            Queue<Long> timestamps = requestTimestamps.computeIfAbsent(key,
                k -> new LinkedList<>());

            long now = System.currentTimeMillis();

            // 移除过期的时间戳
            while (!timestamps.isEmpty() && now - timestamps.peek() > windowMs) {
                timestamps.poll();
            }

            // 检查是否超过限制
            if (timestamps.size() >= maxRequests) {
                return false;
            }

            timestamps.offer(now);
            return true;
        }
    }

    /**
     * 配置缓存服务
     */
    static class ConfigCache {
        private final Map<String, String> cache = new ConcurrentHashMap<>();

        public String getConfig(String key) {
            String value = cache.get(key);
            if (value != null) {
                System.out.println("    [缓存命中]");
                return value;
            }

            System.out.println("    [查询数据库]");
            value = queryConfigFromDB(key);
            cache.put(key, value);
            return value;
        }

        public void refreshConfig(String key) {
            cache.remove(key);
            System.out.println("    [缓存已清除]");
        }

        private String queryConfigFromDB(String key) {
            // 模拟数据库查询
            return "30000"; // 30秒超时
        }
    }
}
