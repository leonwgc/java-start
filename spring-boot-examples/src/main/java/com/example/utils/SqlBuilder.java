package com.example.utils;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Data
public class SqlBuilder {

    private final StringBuilder select = new StringBuilder();
    private final StringBuilder from = new StringBuilder();
    private final StringBuilder where = new StringBuilder();
    private final Map<String, Object> params = new HashMap<>();

    // ==================== 基础语句 ====================
    public SqlBuilder select(String columns) {
        select.append(columns);
        return this;
    }

    public SqlBuilder from(String table) {
        from.append(table);
        return this;
    }

    // ==================== 常用条件 ====================
    public SqlBuilder eq(String column, Object value) {
        addCondition(column, "=", value);
        return this;
    }

    public SqlBuilder ne(String column, Object value) {
        addCondition(column, "!=", value);
        return this;
    }

    public SqlBuilder like(String column, String value) {
        addLikeCondition(column, value, "%", "%");
        return this;
    }

    public SqlBuilder likeLeft(String column, String value) {
        addLikeCondition(column, value, "%", "");
        return this;
    }

    public SqlBuilder likeRight(String column, String value) {
        addLikeCondition(column, value, "", "%");
        return this;
    }

    public SqlBuilder gt(String column, Comparable<?> value) {
        addCondition(column, ">", value);
        return this;
    }

    public SqlBuilder ge(String column, Comparable<?> value) {
        addCondition(column, ">=", value);
        return this;
    }

    public SqlBuilder lt(String column, Comparable<?> value) {
        addCondition(column, "<", value);
        return this;
    }

    public SqlBuilder le(String column, Comparable<?> value) {
        addCondition(column, "<=", value);
        return this;
    }

    public SqlBuilder in(String column, Iterable<?> value) {
        if (value != null && value.iterator().hasNext()) {
            and(column + " IN (:" + column + ")");
            params.put(column, value);
        }
        return this;
    }

    // ==================== 新增你要的 ====================
    public SqlBuilder between(String column, Comparable<?> start, Comparable<?> end) {
        if (start != null && end != null) {
            and(column + " BETWEEN :" + column + "Start AND :" + column + "End");
            params.put(column + "Start", start);
            params.put(column + "End", end);
        }
        return this;
    }

    public SqlBuilder isNull(String column) {
        and(column + " IS NULL");
        return this;
    }

    public SqlBuilder isNotNull(String column) {
        and(column + " IS NOT NULL");
        return this;
    }

    // 自定义 SQL 片段（万能兜底）
    public SqlBuilder sql(String sqlFragment) {
        and(sqlFragment);
        return this;
    }

    // ==================== OR 拼接 ====================
    // 用法：.or( b -> b.like("name", "a").eq("age",18) )
    public SqlBuilder or(Consumer<SqlBuilder> consumer) {
        SqlBuilder orBuilder = new SqlBuilder();
        consumer.accept(orBuilder);
        String orSql = orBuilder.getWhere().toString().trim();
        if (!orSql.isEmpty()) {
            if (orSql.startsWith("AND "))
                orSql = orSql.substring(4);
            and("(" + orSql + ")");
            params.putAll(orBuilder.getParams());
        }
        return this;
    }

    private void addCondition(String column, String operator, Object value) {
        if (value == null) {
            return;
        }
        and(column + " " + operator + " :" + column);
        params.put(column, value);
    }

    private void addLikeCondition(String column, String value, String prefix, String suffix) {
        if (value == null) {
            return;
        }
        and(column + " LIKE :" + column);
        params.put(column, prefix + value + suffix);
    }

    // ==================== 内部拼接 ====================
    private void and(String condition) {
        if (where.length() == 0) {
            where.append(" WHERE ").append(condition);
        } else {
            where.append(" AND ").append(condition);
        }
    }

    // 构建完整 SQL
    public String buildSql() {
        return "SELECT " + select.toString().trim() +
                " FROM " + from.toString().trim() +
                where.toString();
    }

    // 获取参数
    public Map<String, Object> getParams() {
        return params;
    }
}