package basics;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit 5单元测试学习
 * 学习目标：
 * 1. 理解单元测试的重要性
 * 2. 掌握JUnit 5的基本使用
 * 3. 学习常用的断言方法
 * 4. 理解测试在Spring中的应用
 *
 * 注意：这是一个可运行的演示类，也是一个测试类
 * - 作为普通类运行：展示测试概念和用法
 * - 作为测试类运行：执行实际的单元测试
 */
public class JUnitDemo {

    public static void main(String[] args) {
        System.out.println("=== JUnit 5单元测试学习 ===\n");

        System.out.println("1. 什么是单元测试\n");
        System.out.println("作用：验证代码的正确性，发现bug");
        System.out.println("Spring应用：@SpringBootTest、@WebMvcTest等\n");

        System.out.println("2. 测试示例\n");
        demonstrateTestConcepts();

        System.out.println("\n3. 运行测试\n");
        System.out.println("方式1：在IDE中右键点击类名 -> Run 'JUnitDemo'");
        System.out.println("方式2：使用Maven命令：mvn test");
        System.out.println("方式3：使用Gradle命令：gradle test\n");

        System.out.println("💡 Spring中的应用：");
        System.out.println("- @SpringBootTest: 完整的Spring上下文测试");
        System.out.println("- @WebMvcTest: Controller层测试");
        System.out.println("- @DataJpaTest: Repository层测试");
        System.out.println("- @MockBean: 模拟依赖对象");
        System.out.println("- 确保代码质量，便于重构\n");
    }

    // 演示测试概念
    public static void demonstrateTestConcepts() {
        Calculator calc = new Calculator();

        System.out.println("被测试的类: Calculator");
        System.out.println("测试用例:");

        // 测试加法
        int result1 = calc.add(2, 3);
        System.out.println("  add(2, 3) = " + result1 + " (期望: 5) " +
                         (result1 == 5 ? "✓" : "✗"));

        // 测试减法
        int result2 = calc.subtract(5, 3);
        System.out.println("  subtract(5, 3) = " + result2 + " (期望: 2) " +
                         (result2 == 2 ? "✓" : "✗"));

        // 测试乘法
        int result3 = calc.multiply(4, 5);
        System.out.println("  multiply(4, 5) = " + result3 + " (期望: 20) " +
                         (result3 == 20 ? "✓" : "✗"));

        // 测试除法
        double result4 = calc.divide(10, 2);
        System.out.println("  divide(10, 2) = " + result4 + " (期望: 5.0) " +
                         (result4 == 5.0 ? "✓" : "✗"));

        // 测试异常
        try {
            calc.divide(10, 0);
            System.out.println("  divide(10, 0) = 应该抛出异常 ✗");
        } catch (IllegalArgumentException e) {
            System.out.println("  divide(10, 0) = 抛出异常: " + e.getMessage() + " ✓");
        }
    }

    // ========== JUnit测试方法 ==========

    private Calculator calculator;

    @BeforeAll
    static void initAll() {
        System.out.println("[生命周期] @BeforeAll - 所有测试开始前执行一次");
    }

    @BeforeEach
    void init() {
        calculator = new Calculator();
        System.out.println("[生命周期] @BeforeEach - 每个测试前执行");
    }

    @AfterEach
    void cleanup() {
        System.out.println("[生命周期] @AfterEach - 每个测试后执行");
    }

    @AfterAll
    static void cleanupAll() {
        System.out.println("[生命周期] @AfterAll - 所有测试结束后执行一次");
    }

    @Test
    @DisplayName("测试加法功能")
    void testAdd() {
        assertEquals(5, calculator.add(2, 3), "2 + 3 应该等于 5");
        assertEquals(0, calculator.add(-1, 1), "-1 + 1 应该等于 0");
        assertEquals(-5, calculator.add(-2, -3), "-2 + -3 应该等于 -5");
    }

    @Test
    @DisplayName("测试减法功能")
    void testSubtract() {
        assertEquals(2, calculator.subtract(5, 3));
        assertEquals(-2, calculator.subtract(3, 5));
        assertEquals(0, calculator.subtract(5, 5));
    }

    @Test
    @DisplayName("测试乘法功能")
    void testMultiply() {
        assertEquals(20, calculator.multiply(4, 5));
        assertEquals(0, calculator.multiply(0, 5));
        assertEquals(-15, calculator.multiply(-3, 5));
    }

    @Test
    @DisplayName("测试除法功能")
    void testDivide() {
        assertEquals(5.0, calculator.divide(10, 2), 0.001);
        assertEquals(2.5, calculator.divide(5, 2), 0.001);
    }

    @Test
    @DisplayName("测试除以零抛出异常")
    void testDivideByZero() {
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.divide(10, 0),
            "除以零应该抛出IllegalArgumentException"
        );

        assertTrue(exception.getMessage().contains("除数不能为0"));
    }

    @Test
    @DisplayName("测试分组断言")
    void testGroupedAssertions() {
        assertAll("calculator",
            () -> assertEquals(5, calculator.add(2, 3)),
            () -> assertEquals(2, calculator.subtract(5, 3)),
            () -> assertEquals(20, calculator.multiply(4, 5))
        );
    }

    @Test
    @DisplayName("测试条件断言")
    void testConditionalAssertions() {
        int result = calculator.add(2, 3);
        assertTrue(result > 0, "结果应该大于0");
        assertFalse(result < 0, "结果不应该小于0");
        assertNotNull(calculator, "calculator不应该为null");
    }

    @Test
    @Disabled("这个测试暂时禁用")
    void testDisabled() {
        // 这个测试不会运行
        fail("这个测试被禁用了");
    }

    @RepeatedTest(3)
    @DisplayName("重复测试示例")
    void repeatedTest() {
        assertEquals(5, calculator.add(2, 3));
    }

    @ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("参数化测试示例")
    void testWithParameters(int number) {
        assertTrue(number > 0 && number <= 5);
    }

    // ========== 测试数据和工具类 ==========

    // 被测试的计算器类
    static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }

        public int subtract(int a, int b) {
            return a - b;
        }

        public int multiply(int a, int b) {
            return a * b;
        }

        public double divide(int a, int b) {
            if (b == 0) {
                throw new IllegalArgumentException("除数不能为0");
            }
            return (double) a / b;
        }
    }

    // 用户服务测试示例
    @Nested
    @DisplayName("用户服务测试")
    class UserServiceTest {
        private UserService userService;

        @BeforeEach
        void setup() {
            userService = new UserService();
        }

        @Test
        @DisplayName("测试创建用户")
        void testCreateUser() {
            User user = userService.createUser("张三", "zhangsan@example.com");
            assertNotNull(user);
            assertEquals("张三", user.getName());
            assertEquals("zhangsan@example.com", user.getEmail());
        }

        @Test
        @DisplayName("测试查找用户")
        void testFindUser() {
            User created = userService.createUser("李四", "lisi@example.com");
            User found = userService.findById(created.getId());
            assertEquals(created.getId(), found.getId());
        }

        @Test
        @DisplayName("测试用户列表")
        void testGetAllUsers() {
            userService.createUser("张三", "zhangsan@example.com");
            userService.createUser("李四", "lisi@example.com");

            List<User> users = userService.getAllUsers();
            assertEquals(2, users.size());
        }
    }

    // 简单的用户类
    static class User {
        private int id;
        private String name;
        private String email;

        public User(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    // 简单的用户服务
    static class UserService {
        private List<User> users = new ArrayList<>();
        private int nextId = 1;

        public User createUser(String name, String email) {
            User user = new User(nextId++, name, email);
            users.add(user);
            return user;
        }

        public User findById(int id) {
            return users.stream()
                       .filter(u -> u.getId() == id)
                       .findFirst()
                       .orElse(null);
        }

        public List<User> getAllUsers() {
            return new ArrayList<>(users);
        }
    }
}
