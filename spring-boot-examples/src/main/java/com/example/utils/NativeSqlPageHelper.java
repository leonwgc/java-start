package com.example.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Map;

@Component
public class NativeSqlPageHelper {

    @PersistenceContext
    private EntityManager entityManager;

    // 返回实体分页
    public <T> Page<T> pageQuery(String sql, Map<String, Object> params,
                                 Pageable pageable, Class<T> resultClass) {
        String sqlWithSort = applySort(sql, pageable.getSort());
        String countSql = buildCountSql(sqlWithSort);

        Query query = entityManager.createNativeQuery(sqlWithSort, resultClass);
        Query countQuery = entityManager.createNativeQuery(countSql);

        setParams(query, params);
        setParams(countQuery, params);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(query.getResultList(), pageable, total);
    }

    // 返回 Object[] 分页（多表DTO用）
    public Page<Object[]> pageQuery(String sql, Map<String, Object> params,
                                    Pageable pageable) {
        String sqlWithSort = applySort(sql, pageable.getSort());
        String countSql = buildCountSql(sqlWithSort);

        Query query = entityManager.createNativeQuery(sqlWithSort);
        Query countQuery = entityManager.createNativeQuery(countSql);

        setParams(query, params);
        setParams(countQuery, params);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        long total = ((Number) countQuery.getSingleResult()).longValue();
        return new PageImpl<>(query.getResultList(), pageable, total);
    }

    // 拼接排序
    private String applySort(String sql, Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return sql;
        }

        StringBuilder orderClause = new StringBuilder();
        for (Sort.Order order : sort) {
            if (!orderClause.isEmpty()) {
                orderClause.append(", ");
            }
            orderClause.append(order.getProperty())
                       .append(" ")
                       .append(order.getDirection().name());
        }
        return sql + " ORDER BY " + orderClause;
    }

    // 自动生成 count SQL
    private String buildCountSql(String sql) {
        String lowerSql = sql.toLowerCase();
        int orderPos = lowerSql.lastIndexOf("order by");
        if (orderPos > 0) {
            sql = sql.substring(0, orderPos);
        }
        return "SELECT COUNT(*) FROM (" + sql + ") AS t";
    }

    // 返回列表（不分页）
    public <T> java.util.List<T> listQuery(String sql, Map<String, Object> params,
                                           Class<T> resultClass) {
        Query query = entityManager.createNativeQuery(sql, resultClass);
        setParams(query, params);
        return query.getResultList();
    }

    // 返回 Object[] 列表（不分页）
    public java.util.List<Object[]> listQuery(String sql, Map<String, Object> params) {
        Query query = entityManager.createNativeQuery(sql);
        setParams(query, params);
        return query.getResultList();
    }

    // 设置参数
    private void setParams(Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            params.forEach(query::setParameter);
        }
    }
}