package com.example.jpa.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * JPA Specification 通用动态查询工具类
 * 替代原生SQL，类型安全、防注入、支持动态条件、分页、排序、多表关联
 */
@Component
public class JpaSpecHelper {

    private static final Logger logger = LoggerFactory.getLogger(JpaSpecHelper.class);
    private static final int MAX_RESULT_SIZE = 100000;

    @PersistenceContext
    private EntityManager entityManager;

    // ==================== 分页查询（实体类） ====================
    public <T> Page<T> pageQuery(Class<T> entityClass,
            List<SearchCondition> conditions,
            Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        try {
            Specification<T> spec = buildSpecification(conditions);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(entityClass);
            Root<T> root = query.from(entityClass);

            // 应用查询条件
            if (spec != null) {
                query.where(toPredicate(spec, root, query, cb));
            }

            // 应用排序
            applySort(query, root, cb, pageable.getSort());

            // 查询列表
            List<T> content = entityManager.createQuery(query)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            // 查询总数
            long total = countQuery(entityClass, conditions);

            logger.info("分页查询完成：总条数={}, 当前页条数={}", total, content.size());
            return new PageImpl<>(content, pageable, total);
        } catch (Exception e) {
            logger.error("Specification分页查询异常", e);
            throw new RuntimeException("分页查询失败：" + e.getMessage(), e);
        }
    }

    // ==================== 列表查询（不分页） ====================
    public <T> List<T> listQuery(Class<T> entityClass, List<SearchCondition> conditions) {
        try {
            Specification<T> spec = buildSpecification(conditions);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(entityClass);
            Root<T> root = query.from(entityClass);

            if (spec != null) {
                query.where(toPredicate(spec, root, query, cb));
            }

            List<T> result = entityManager.createQuery(query)
                    .setMaxResults(MAX_RESULT_SIZE)
                    .getResultList();

            logger.info("列表查询完成：返回{}条记录", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Specification列表查询异常", e);
            throw new RuntimeException("列表查询失败：" + e.getMessage(), e);
        }
    }

    // ==================== 查询单个对象 ====================
    public <T> T findOne(Class<T> entityClass, List<SearchCondition> conditions) {
        List<T> list = listQuery(entityClass, conditions);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    // ==================== 更新操作（事务） ====================
    @Transactional(rollbackFor = Exception.class)
    public <T> void update(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("实体不能为空");
        }
        entityManager.merge(entity);
        logger.info("实体更新成功：{}", entity);
    }

    // ==================== 保存/插入 ====================
    @Transactional(rollbackFor = Exception.class)
    public <T> void save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("实体不能为空");
        }
        entityManager.persist(entity);
        logger.info("实体保存成功：{}", entity);
    }

    // ==================== 删除 ====================
    @Transactional(rollbackFor = Exception.class)
    public <T> void delete(Class<T> entityClass, Object id) {
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
            logger.info("实体删除成功，ID：{}", id);
        }
    }

    // ==================== 统计数量 ====================
    public <T> long countQuery(Class<T> entityClass, List<SearchCondition> conditions) {
        try {
            Specification<T> spec = buildSpecification(conditions);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<T> root = query.from(entityClass);
            query.select(cb.count(root));

            if (spec != null) {
                query.where(toPredicate(spec, root, query, cb));
            }

            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception e) {
            logger.error("统计数量异常", e);
            return 0;
        }
    }

    // ==================== 构建 Specification ====================
    private <T> Specification<T> buildSpecification(List<SearchCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return null;
        }

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchCondition condition : conditions) {
                String field = condition.getField();
                Object value = condition.getValue();
                SearchOperator operator = condition.getOperator();

                if (!StringUtils.hasText(field) || value == null) {
                    continue;
                }

                Path<?> path = getPath(root, field);
                predicates.add(buildPredicate(cb, path, operator, value));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // ==================== 支持级联字段（如 user.name） ====================
    private <T> Path<?> getPath(Root<T> root, String field) {
        if (!field.contains(".")) {
            return root.get(field);
        }
        String[] parts = field.split("\\.");
        Path<?> path = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            path = path.get(parts[i]);
        }
        return path;
    }

    // ==================== 构建条件 ====================
    private Predicate buildPredicate(CriteriaBuilder cb, Path<?> path, SearchOperator op, Object value) {
        return switch (op) {
            case EQ -> cb.equal(path, value);
            case NE -> cb.notEqual(path, value);
            case GT -> greaterThan(cb, path, value);
            case LT -> lessThan(cb, path, value);
            case GTE -> greaterThanOrEqualTo(cb, path, value);
            case LTE -> lessThanOrEqualTo(cb, path, value);
            case LIKE -> cb.like(path.as(String.class), "%" + value + "%");
            case LIKE_LEFT -> cb.like(path.as(String.class), value + "%");
            case LIKE_RIGHT -> cb.like(path.as(String.class), "%" + value);
            case IN -> path.in((Collection<?>) value);
            case NOT_IN -> cb.not(path.in((Collection<?>) value));
            case IS_NULL -> cb.isNull(path);
            case IS_NOT_NULL -> cb.isNotNull(path);
        };
    }

    @SuppressWarnings("unchecked")
    private Predicate greaterThan(CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.greaterThan(asComparableExpression(path), asComparableValue(value));
    }

    @SuppressWarnings("unchecked")
    private Predicate lessThan(CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.lessThan(asComparableExpression(path), asComparableValue(value));
    }

    @SuppressWarnings("unchecked")
    private Predicate greaterThanOrEqualTo(CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.greaterThanOrEqualTo(asComparableExpression(path), asComparableValue(value));
    }

    @SuppressWarnings("unchecked")
    private Predicate lessThanOrEqualTo(CriteriaBuilder cb, Path<?> path, Object value) {
        return cb.lessThanOrEqualTo(asComparableExpression(path), asComparableValue(value));
    }

    @SuppressWarnings("rawtypes")
    private Expression<? extends Comparable> asComparableExpression(Path<?> path) {
        return path.as(Comparable.class);
    }

    @SuppressWarnings("rawtypes")
    private Comparable asComparableValue(Object value) {
        if (!(value instanceof Comparable comparableValue)) {
            throw new IllegalArgumentException("比较操作的值必须实现 Comparable 接口");
        }
        return comparableValue;
    }

    @SuppressWarnings("null")
    private <T> Predicate toPredicate(Specification<T> spec,
            Root<T> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb) {
        return spec.toPredicate(root, query, cb);
    }

    // ==================== 应用排序 ====================
    private <T> void applySort(CriteriaQuery<?> query, Root<T> root, CriteriaBuilder cb, Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return;
        }

        List<Order> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            Path<?> path = getPath(root, order.getProperty());
            if (order.isAscending()) {
                orders.add(cb.asc(path));
            } else {
                orders.add(cb.desc(path));
            }
        }
        query.orderBy(orders);
    }

    // ==================== 查询操作枚举 ====================
    public enum SearchOperator {
        EQ, NE, GT, LT, GTE, LTE,
        LIKE, LIKE_LEFT, LIKE_RIGHT,
        IN, NOT_IN, IS_NULL, IS_NOT_NULL
    }

    // ==================== 条件对象 ====================
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCondition {
        private String field; // 字段名，支持级联：user.name
        private SearchOperator operator; // 操作符
        private Object value; // 值
    }
}