package com.example.jpa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * SQL原生查询辅助类，支持分页、排序、参数绑定
 * 功能：1. 原生SQL分页查询（实体对象、Object数组）
 * 2. 原生SQL列表查询（支持不分页）
 * 3. 单个对象查询
 * 4. 更新/删除操作
 * 5. SQL注入防护、参数验证、日志记录、异常处理
 */
@Component
public class SqlHelper {

    private static final Logger logger = LoggerFactory.getLogger(SqlHelper.class);
    private static final Pattern VALID_FIELD_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_.]*$");
    private static final int DEFAULT_QUERY_TIMEOUT = 30000; // 30秒
    private static final int MAX_RESULT_SIZE = 100000; // 最大结果集数量

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 返回实体对象的分页查询
     *
     * @param sql         原生SQL语句
     * @param params      参数Map
     * @param pageable    分页参数
     * @param resultClass 结果类型
     * @return 分页对象
     */
    public <T> Page<T> pageQuery(String sql, Map<String, Object> params,
            Pageable pageable, Class<T> resultClass) {
        validateInput(sql, pageable);
        try {
            String sqlWithSort = applySort(sql, pageable.getSort());
            String countSql = buildCountSql(sqlWithSort);

            logger.debug("执行分页查询: {}", sqlWithSort);

            Query query = entityManager.createNativeQuery(sqlWithSort, resultClass);
            Query countQuery = entityManager.createNativeQuery(countSql);

            setQueryTimeout(query);
            setQueryTimeout(countQuery);

            setParams(query, params);
            setParams(countQuery, params);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            long total = ((Number) countQuery.getSingleResult()).longValue();
            List<T> content = castList(query.getResultList());

            logger.info("分页查询完成: 总记录数={}, 当前页数据数={}", total, content.size());
            return new PageImpl<>(Objects.requireNonNull(content), pageable, total);
        } catch (Exception e) {
            logger.error("分页查询异常: {}", sql, e);
            throw new RuntimeException("分页查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 返回Object数组的分页查询（多表关联查询）
     *
     * @param sql      原生SQL语句
     * @param params   参数Map
     * @param pageable 分页参数
     * @return 分页对象
     */
    public Page<Object[]> pageQuery(String sql, Map<String, Object> params,
            Pageable pageable) {
        validateInput(sql, pageable);
        try {
            String sqlWithSort = applySort(sql, pageable.getSort());
            String countSql = buildCountSql(sqlWithSort);

            logger.debug("执行Object[]分页查询: {}", sqlWithSort);

            Query query = entityManager.createNativeQuery(sqlWithSort);
            Query countQuery = entityManager.createNativeQuery(countSql);

            setQueryTimeout(query);
            setQueryTimeout(countQuery);

            setParams(query, params);
            setParams(countQuery, params);

            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            long total = ((Number) countQuery.getSingleResult()).longValue();
            List<Object[]> content = castObjectArrayList(query.getResultList());

            logger.info("Object[]分页查询完成: 总记录数={}, 当前页数据数={}", total, content.size());
            return new PageImpl<>(Objects.requireNonNull(content), pageable, total);
        } catch (Exception e) {
            logger.error("Object[]分页查询异常: {}", sql, e);
            throw new RuntimeException("分页查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 返回实体对象列表（不分页）
     *
     * @param sql         原生SQL语句
     * @param params      参数Map
     * @param resultClass 结果类型
     * @return 对象列表
     */
    public <T> List<T> listQuery(String sql, Map<String, Object> params,
            Class<T> resultClass) {
        validateInput(sql);
        try {
            logger.debug("执行列表查询: {}", sql);
            Query query = entityManager.createNativeQuery(sql, resultClass);
            setQueryTimeout(query);
            query.setMaxResults(MAX_RESULT_SIZE);
            setParams(query, params);
            List<T> result = castList(query.getResultList());
            logger.info("列表查询完成: 返回{}条记录", result.size());
            return result;
        } catch (Exception e) {
            logger.error("列表查询异常: {}", sql, e);
            throw new RuntimeException("列表查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 返回Object数组列表（不分页）
     *
     * @param sql    原生SQL语句
     * @param params 参数Map
     * @return Object数组列表
     */
    public List<Object[]> listQuery(String sql, Map<String, Object> params) {
        validateInput(sql);
        try {
            logger.debug("执行Object[]列表查询: {}", sql);
            Query query = entityManager.createNativeQuery(sql);
            setQueryTimeout(query);
            query.setMaxResults(MAX_RESULT_SIZE);
            setParams(query, params);
            List<Object[]> result = castObjectArrayList(query.getResultList());
            logger.info("Object[]列表查询完成: 返回{}条记录", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Object[]列表查询异常: {}", sql, e);
            throw new RuntimeException("列表查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询单个对象
     *
     * @param sql         原生SQL语句
     * @param params      参数Map
     * @param resultClass 结果类型
     * @return 单个对象，不存在返回null
     */
    public <T> T queryForObject(String sql, Map<String, Object> params, Class<T> resultClass) {
        validateInput(sql);
        try {
            logger.debug("执行单个对象查询: {}", sql);
            Query query = entityManager.createNativeQuery(sql, resultClass);
            setQueryTimeout(query);
            setParams(query, params);
            query.setMaxResults(1);
            List<T> results = castList(query.getResultList());
            T result = results.isEmpty() ? null : results.get(0);
            logger.info("单个对象查询完成: 结果={}", result != null ? "已找到" : "未找到");
            return result;
        } catch (Exception e) {
            logger.error("单个对象查询异常: {}", sql, e);
            throw new RuntimeException("单个对象查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询单个Object数组
     *
     * @param sql    原生SQL语句
     * @param params 参数Map
     * @return Object数组，不存在返回null
     */
    public Object[] queryForArray(String sql, Map<String, Object> params) {
        validateInput(sql);
        try {
            logger.debug("执行单个数组查询: {}", sql);
            Query query = entityManager.createNativeQuery(sql);
            setQueryTimeout(query);
            setParams(query, params);
            query.setMaxResults(1);
            List<Object[]> results = castObjectArrayList(query.getResultList());
            Object[] result = results.isEmpty() ? null : results.get(0);
            logger.info("单个数组查询完成: 结果={}", result != null ? "已找到" : "未找到");
            return result;
        } catch (Exception e) {
            logger.error("单个数组查询异常: {}", sql, e);
            throw new RuntimeException("单个数组查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行更新或删除操作
     *
     * @param sql    更新/删除SQL语句
     * @param params 参数Map
     * @return 影响的行数
     */
    @Transactional(rollbackFor = Exception.class)
    public int executeUpdate(String sql, Map<String, Object> params) {
        validateInput(sql);
        if (sql.trim().toUpperCase().startsWith("SELECT")) {
            throw new IllegalArgumentException("不支持SELECT语句，请使用查询方法");
        }
        try {
            logger.debug("执行更新操作: {}", sql);
            Query query = entityManager.createNativeQuery(sql);
            setQueryTimeout(query);
            setParams(query, params);
            int result = query.executeUpdate();
            logger.info("更新操作完成: 影响{}行", result);
            return result;
        } catch (Exception e) {
            logger.error("更新操作异常: {}", sql, e);
            throw new RuntimeException("更新操作失败: " + e.getMessage(), e);
        }
    }

    /**
     * 拼接排序条件（防SQL注入）
     *
     * @param sql  原生SQL语句
     * @param sort 排序对象
     * @return 拼接排序后的SQL
     */
    private String applySort(String sql, Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return sql;
        }

        StringBuilder orderClause = new StringBuilder();
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            // SQL注入防护：验证字段名有效性
            if (!isValidFieldName(property)) {
                logger.warn("检测到非法的排序字段: {}", property);
                throw new IllegalArgumentException("非法的排序字段: " + property);
            }

            if (!orderClause.isEmpty()) {
                orderClause.append(", ");
            }
            orderClause.append(property)
                    .append(" ")
                    .append(order.getDirection().name());
        }
        return sql + " ORDER BY " + orderClause;
    }

    /**
     * 自动生成COUNT SQL（优化版）
     *
     * @param sql 原生SQL语句
     * @return COUNT SQL
     */
    private String buildCountSql(String sql) {
        String lowerSql = sql.toLowerCase().trim();
        String workSql = sql;

        // 移除ORDER BY子句
        int orderPos = lowerSql.lastIndexOf("order by");
        if (orderPos > 0) {
            workSql = sql.substring(0, orderPos);
        }

        // 移除LIMIT/OFFSET子句
        int limitPos = lowerSql.lastIndexOf("limit");
        if (limitPos > 0) {
            workSql = workSql.substring(0, limitPos);
        }

        // 如果SQL中没有GROUP BY或DISTINCT，直接count；否则使用子查询
        if (!lowerSql.contains("group by") && !lowerSql.contains("distinct")) {
            // 简单SQL直接替换SELECT子句
            String countSql = workSql.replaceFirst("(?i)select\\s+.*?\\s+from", "SELECT COUNT(*) FROM");
            if (countSql.equals(workSql)) {
                // 替换失败则使用子查询
                return "SELECT COUNT(*) FROM (" + workSql + ") AS t";
            }
            return countSql;
        }

        return "SELECT COUNT(*) FROM (" + workSql + ") AS t";
    }

    /**
     * 验证字段名的有效性（防止SQL注入）
     *
     * @param fieldName 字段名
     * @return 是否有效
     */
    private boolean isValidFieldName(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty()) {
            return false;
        }
        return VALID_FIELD_PATTERN.matcher(fieldName).matches();
    }

    /**
     * 验证输入参数
     *
     * @param sql      SQL语句
     * @param pageable 分页对象
     */
    private void validateInput(String sql, Pageable pageable) {
        validateInput(sql);
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        if (pageable.getPageSize() < 1) {
            throw new IllegalArgumentException("每页数量不能小于1");
        }
    }

    /**
     * 验证输入参数
     *
     * @param sql SQL语句
     */
    private void validateInput(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL语句不能为空");
        }
    }

    /**
     * 设置查询超时时间
     *
     * @param query 查询对象
     */
    private void setQueryTimeout(Query query) {
        try {
            query.setHint("jakarta.persistence.query.timeout", DEFAULT_QUERY_TIMEOUT);
        } catch (Exception e) {
            logger.warn("设置查询超时失败", e);
        }
    }

    /**
     * 类型安全的列表转换
     *
     * @param rawResults 原始结果列表
     * @return 转换后的列表
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> List<T> castList(List rawResults) {
        return (List<T>) rawResults;
    }

    /**
     * Object数组列表转换
     *
     * @param rawResults 原始结果列表
     * @return Object数组列表
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<Object[]> castObjectArrayList(List rawResults) {
        return (List<Object[]>) rawResults;
    }

    /**
     * 设置SQL参数
     *
     * @param query  查询对象
     * @param params 参数Map
     */
    private void setParams(Query query, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return;
        }

        params.forEach((key, value) -> {
            try {
                query.setParameter(key, value);
            } catch (IllegalArgumentException e) {
                logger.warn("参数绑定失败: key={}, value={}", key, value);
            }
        });
    }
}