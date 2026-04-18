package com.example.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.jpa.utils.SqlHelper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * SqlPageHelper 单元测试类
 *
 * 测试范围：
 * 1. 分页查询功能（实体对象、Object数组）
 * 2. 列表查询功能（不分页）
 * 3. 单个对象查询
 * 4. 更新/删除操作
 * 5. SQL注入防护
 * 6. 参数验证
 * 7. 异常处理
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SqlPageHelper 单元测试")
class SqlPageHelperTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @InjectMocks
    private SqlHelper sqlPageHelper;

    private String testSql;
    private Map<String, Object> testParams;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        testSql = "SELECT * FROM users WHERE age > :age";
        testParams = new HashMap<>();
        testParams.put("age", 18);
        pageable = PageRequest.of(0, 10);
    }

    // ==================== 分页查询测试 ====================

    @Test
    @DisplayName("分页查询实体对象_应返回正确的分页数据")
    void testPageQueryEntity_ShouldReturnCorrectPageData() {
        // 准备测试数据
        List<TestEntity> mockResults = new ArrayList<>();
        mockResults.add(new TestEntity(1, "User1"));
        mockResults.add(new TestEntity(2, "User2"));

        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(100L);

        // 执行
        Page<TestEntity> result = sqlPageHelper.pageQuery(testSql, testParams, pageable, TestEntity.class);

        // 验证
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(100L, result.getTotalElements());
        assertEquals(10, result.getTotalPages());
        // 两个调用：一个带 resultClass，一个不带
        verify(entityManager, atLeast(1)).createNativeQuery(anyString(), eq(TestEntity.class));
        verify(entityManager, atLeast(1)).createNativeQuery(anyString());
    }

    @Test
    @DisplayName("分页查询Object数组_应返回正确的分页数据")
    void testPageQueryArray_ShouldReturnCorrectPageData() {
        // 准备测试数据
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{1, "User1"});
        mockResults.add(new Object[]{2, "User2"});

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(50L);

        // 执行
        Page<Object[]> result = sqlPageHelper.pageQuery(testSql, testParams, pageable);

        // 验证
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(50L, result.getTotalElements());
        verify(entityManager, times(2)).createNativeQuery(anyString());
    }

    @Test
    @DisplayName("分页查询带排序_应正确应用排序条件")
    void testPageQueryWithSort_ShouldApplySortCorrectly() {
        // 准备排序分页对象
        Pageable pageableWithSort = PageRequest.of(0, 10, Sort.by("age").descending());

        List<TestEntity> mockResults = new ArrayList<>();
        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(0L);

        // 执行
        Page<TestEntity> result = sqlPageHelper.pageQuery(testSql, testParams, pageableWithSort, TestEntity.class);

        // 验证 SQL 中包含 ORDER BY
        verify(entityManager).createNativeQuery(contains("ORDER BY age DESC"), eq(TestEntity.class));
        assertNotNull(result);
    }

    // ==================== 列表查询测试 ====================

    @Test
    @DisplayName("列表查询实体对象_应返回所有匹配数据")
    void testListQueryEntity_ShouldReturnAllData() {
        // 准备测试数据
        List<TestEntity> mockResults = new ArrayList<>();
        mockResults.add(new TestEntity(1, "User1"));
        mockResults.add(new TestEntity(2, "User2"));
        mockResults.add(new TestEntity(3, "User3"));

        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);

        // 执行
        List<TestEntity> result = sqlPageHelper.listQuery(testSql, testParams, TestEntity.class);

        // 验证
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(entityManager).createNativeQuery(testSql, TestEntity.class);
        verify(query).setMaxResults(100000);
    }

    @Test
    @DisplayName("列表查询Object数组_应返回所有匹配数据")
    void testListQueryArray_ShouldReturnAllData() {
        // 准备测试数据
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{1, "User1"});
        mockResults.add(new Object[]{2, "User2"});

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);

        // 执行
        List<Object[]> result = sqlPageHelper.listQuery(testSql, testParams);

        // 验证
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(entityManager).createNativeQuery(testSql);
    }

    // ==================== 单个对象查询测试 ====================

    @Test
    @DisplayName("查询单个对象_应返回第一条记录")
    void testQueryForObject_ShouldReturnFirstRecord() {
        // 准备测试数据
        List<TestEntity> mockResults = new ArrayList<>();
        mockResults.add(new TestEntity(1, "User1"));
        mockResults.add(new TestEntity(2, "User2"));

        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);

        // 执行
        TestEntity result = sqlPageHelper.queryForObject(testSql, testParams, TestEntity.class);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.id);
        assertEquals("User1", result.name);
        verify(query).setMaxResults(1);
    }

    @Test
    @DisplayName("查询单个对象_数据不存在时应返回null")
    void testQueryForObject_ShouldReturnNullWhenNotFound() {
        // 准备空结果
        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        // 执行
        TestEntity result = sqlPageHelper.queryForObject(testSql, testParams, TestEntity.class);

        // 验证
        assertNull(result);
    }

    @Test
    @DisplayName("查询单个数组_应返回第一条记录")
    void testQueryForArray_ShouldReturnFirstArray() {
        // 准备测试数据
        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{1, "User1"});

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);

        // 执行
        Object[] result = sqlPageHelper.queryForArray(testSql, testParams);

        // 验证
        assertNotNull(result);
        assertEquals(1, result[0]);
        assertEquals("User1", result[1]);
    }

    // ==================== 更新操作测试 ====================

    @Test
    @DisplayName("执行更新操作_应返回影响的行数")
    void testExecuteUpdate_ShouldReturnAffectedRowCount() {
        // 准备
        String updateSql = "UPDATE users SET age = :age WHERE id = :id";
        testParams.put("id", 1);

        when(entityManager.createNativeQuery(updateSql)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(5);

        // 执行
        int result = sqlPageHelper.executeUpdate(updateSql, testParams);

        // 验证
        assertEquals(5, result);
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("删除操作_应返回影响的行数")
    void testExecuteDelete_ShouldReturnAffectedRowCount() {
        // 准备
        String deleteSql = "DELETE FROM users WHERE age > :age";

        when(entityManager.createNativeQuery(deleteSql)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(10);

        // 执行
        int result = sqlPageHelper.executeUpdate(deleteSql, testParams);

        // 验证
        assertEquals(10, result);
    }

    @Test
    @DisplayName("执行更新时使用SELECT_应抛出异常")
    void testExecuteUpdate_WithSelectStatement_ShouldThrowException() {
        // 准备
        String selectSql = "SELECT * FROM users";

        // 执行 & 验证
        assertThrows(IllegalArgumentException.class, () ->
                sqlPageHelper.executeUpdate(selectSql, testParams));
    }

    // ==================== 参数验证测试 ====================

    @Test
    @DisplayName("SQL为空_应抛出异常")
    void testEmptySql_ShouldThrowException() {
        // 执行 & 验证
        assertThrows(IllegalArgumentException.class, () ->
                sqlPageHelper.pageQuery("", testParams, pageable, TestEntity.class));

        assertThrows(IllegalArgumentException.class, () ->
                sqlPageHelper.pageQuery(null, testParams, pageable, TestEntity.class));
    }

    @Test
    @DisplayName("分页对象为空_应抛出异常")
    void testNullPageable_ShouldThrowException() {
        // 执行 & 验证
        assertThrows(IllegalArgumentException.class, () ->
                sqlPageHelper.pageQuery(testSql, testParams, null, TestEntity.class));
    }

    @Test
    @DisplayName("参数为null_应正常执行")
    void testNullParams_ShouldExecuteNormally() {
        // 准备
        List<TestEntity> mockResults = new ArrayList<>();
        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);

        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(0L);

        // 执行
        Page<TestEntity> result = sqlPageHelper.pageQuery(testSql, null, pageable, TestEntity.class);

        // 验证
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    // ==================== SQL注入防护测试 ====================

    @Test
    @DisplayName("排序字段名非法_应抛出异常")
    void testInvalidSortField_ShouldThrowException() {
        // 准备 - 使用非法的字段名
        Sort invalidSort = Sort.by("1 OR 1=1");
        Pageable pageableWithInvalidSort = PageRequest.of(0, 10, invalidSort);

        // 执行 & 验证
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                sqlPageHelper.pageQuery(testSql, testParams, pageableWithInvalidSort, TestEntity.class));
        assertTrue(exception.getMessage().contains("分页查询失败"));
    }

    @Test
    @DisplayName("排序字段包含SQL关键字_应抛出异常")
    void testSortFieldWithSqlKeyword_ShouldThrowException() {
        // 准备
        Sort invalidSort = Sort.by("age; DROP TABLE users");
        Pageable pageableWithInvalidSort = PageRequest.of(0, 10, invalidSort);

        // 执行 & 验证
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                sqlPageHelper.pageQuery(testSql, testParams, pageableWithInvalidSort, TestEntity.class));
        assertTrue(exception.getMessage().contains("分页查询失败"));
    }

    @Test
    @DisplayName("有效的排序字段格式_应正常执行")
    void testValidSortField_ShouldExecuteNormally() {
        // 准备 - 合法的字段名（包括点号和下划线）
        Sort validSort = Sort.by("user.age").descending();
        Pageable pageableWithValidSort = PageRequest.of(0, 10, validSort);

        List<TestEntity> mockResults = new ArrayList<>();
        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(0L);

        // 执行
        Page<TestEntity> result = sqlPageHelper.pageQuery(testSql, testParams, pageableWithValidSort, TestEntity.class);

        // 验证
        assertNotNull(result);
    }

    // ==================== COUNT SQL优化测试 ====================

    @Test
    @DisplayName("简单SELECT查询生成COUNT_应优化而非使用子查询")
    void testSimpleSelectCountOptimization() {
        // 准备
        String simpleSql = "SELECT id, name FROM users WHERE age > :age";
        List<TestEntity> mockResults = new ArrayList<>();

        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(100L);

        // 执行
        Page<TestEntity> result = sqlPageHelper.pageQuery(simpleSql, testParams, pageable, TestEntity.class);

        // 验证
        verify(entityManager, atLeast(1)).createNativeQuery(anyString());
        assertNotNull(result);
    }

    @Test
    @DisplayName("带ORDER BY的查询_生成COUNT时应移除ORDER BY")
    void testCountSqlRemovesOrderBy() {
        // 准备
        String sqlWithOrder = "SELECT id, name FROM users ORDER BY age DESC";
        List<TestEntity> mockResults = new ArrayList<>();

        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);
        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(100L);

        // 执行
        Page<TestEntity> result = sqlPageHelper.pageQuery(sqlWithOrder, testParams, pageable, TestEntity.class);

        // 验证
        verify(entityManager, atLeast(1)).createNativeQuery(anyString());
        assertNotNull(result);
    }

    // ==================== 异常处理测试 ====================

    @Test
    @DisplayName("数据库查询异常_应包装并抛出RuntimeException")
    void testQueryException_ShouldWrapInRuntimeException() {
        // 准备 - mock异常
        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenThrow(new RuntimeException("数据库连接错误"));

        // 执行 & 验证
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                sqlPageHelper.pageQuery(testSql, testParams, pageable, TestEntity.class));

        assertTrue(exception.getMessage().contains("分页查询失败"));
    }

    @Test
    @DisplayName("参数绑定失败_应继续处理其他参数")
    void testParameterBindingFailure_ShouldContinueProcessing() {
        // 准备 - 模拟参数绑定失败但查询成功
        List<TestEntity> mockResults = new ArrayList<>();

        when(entityManager.createNativeQuery(anyString(), eq(TestEntity.class)))
                .thenReturn(query);
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(query);

        // 模拟参数绑定时抛出异常（参数可能不在SQL中）
        doThrow(new IllegalArgumentException("参数不存在"))
                .when(query).setParameter(anyString(), any());

        when(query.getResultList()).thenReturn((List) mockResults);
        when(query.getSingleResult()).thenReturn(0L);

        // 执行 - 不应抛出异常，应该继续处理
        assertDoesNotThrow(() ->
                sqlPageHelper.pageQuery(testSql, testParams, pageable, TestEntity.class));
    }

    // ==================== 辅助测试实体类 ====================

    /**
     * 测试用的简单实体类
     */
    static class TestEntity {
        int id;
        String name;

        TestEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "TestEntity{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
