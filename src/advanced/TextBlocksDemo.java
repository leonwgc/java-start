package advanced;

/**
 * 文本块（Text Blocks）详解（Java 15+）
 * 学习目标：
 * 1. 理解文本块的作用和优势
 * 2. 掌握文本块的语法和格式规则
 * 3. 学习转义序列和格式化方法
 * 4. 了解文本块的实际应用场景
 *
 * 文本块是什么？
 * - 使用三重引号（"""）定义的多行字符串字面量
 * - 自动处理换行和缩进
 * - 提高多行文本的可读性
 * - 特别适合JSON、SQL、HTML等多行内容
 *
 * Spring应用：
 * - SQL查询语句
 * - JSON/XML配置
 * - HTML模板
 * - 错误消息和帮助文本
 */
public class TextBlocksDemo {

    public static void main(String[] args) {
        System.out.println("=== 文本块详解 ===\n");

        demonstrateTraditionalString();
        demonstrateBasicTextBlock();
        demonstrateIndentation();
        demonstrateEscapeSequences();
        demonstrateFormatting();
        demonstrateSQLQueries();
        demonstrateJSONHandling();
        demonstrateHTMLTemplates();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. 传统字符串的问题
     */
    private static void demonstrateTraditionalString() {
        System.out.println("1. 传统字符串的问题\n");

        // 传统方式：需要大量转义和拼接
        String json = "{\n" +
                      "  \"name\": \"张三\",\n" +
                      "  \"age\": 25,\n" +
                      "  \"city\": \"北京\"\n" +
                      "}";

        System.out.println("传统JSON字符串（需要\\n和+）:");
        System.out.println(json);
        System.out.println();

        System.out.println("问题：");
        System.out.println("  ✗ 需要手动添加\\n换行");
        System.out.println("  ✗ 需要转义引号\\\"");
        System.out.println("  ✗ 需要字符串拼接+");
        System.out.println("  ✗ 代码难以阅读和维护");
        System.out.println();
    }

    /**
     * 2. 文本块基础用法
     */
    private static void demonstrateBasicTextBlock() {
        System.out.println("2. 文本块基础用法\n");

        // 使用文本块：简洁清晰
        String json = """
                {
                  "name": "李四",
                  "age": 30,
                  "city": "上海"
                }
                """;

        System.out.println("文本块JSON字符串（无需转义）:");
        System.out.println(json);

        System.out.println("优势：");
        System.out.println("  ✓ 自动处理换行");
        System.out.println("  ✓ 不需要转义双引号（大多数情况）");
        System.out.println("  ✓ 不需要字符串拼接");
        System.out.println("  ✓ 所见即所得");
        System.out.println();

        // 文本块语法规则
        System.out.println("文本块语法规则：");
        System.out.println("1. 使用三重双引号\"\"\"开始");
        System.out.println("2. 开始引号后必须换行");
        System.out.println("3. 使用三重双引号\"\"\"结束");
        System.out.println("4. 结束引号位置决定缩进");
        System.out.println();
    }

    /**
     * 3. 缩进处理
     */
    private static void demonstrateIndentation() {
        System.out.println("3. 缩进处理\n");

        // 示例1：结束引号在行首 - 保留所有缩进
        String text1 = """
                第一行
                  第二行（2空格缩进）
                    第三行（4空格缩进）
                """;

        System.out.println("【示例1】结束引号在行首:");
        System.out.println(text1);
        System.out.println("实际内容: " + text1.replace("\n", "\\n"));
        System.out.println();

        // 示例2：结束引号缩进 - 自动去除公共缩进
        String text2 = """
                    第一行
                      第二行（2空格缩进）
                        第三行（4空格缩进）
                    """;

        System.out.println("【示例2】结束引号缩进:");
        System.out.println(text2);
        System.out.println();

        // 使用indent()方法调整缩进
        String text3 = """
                Java
                Python
                Go
                """.indent(4);

        System.out.println("【示例3】使用indent()添加缩进:");
        System.out.println(text3);
    }

    /**
     * 4. 转义序列
     */
    private static void demonstrateEscapeSequences() {
        System.out.println("4. 转义序列\n");

        // 保留换行
        String text1 = """
                第一行
                第二行
                第三行
                """;
        System.out.println("自动换行:");
        System.out.println(text1);

        // 取消换行：使用 \
        String text2 = """
                这是一个很长的句子，\
                但我们不想在中间换行，\
                所以使用反斜杠连接。
                """;
        System.out.println("使用\\取消换行:");
        System.out.println(text2);
        System.out.println();

        // 保留行尾空格：使用 \s
        String text3 = """
                第一行\s\s
                第二行\s\s
                """;
        System.out.println("使用\\s保留行尾空格:");
        System.out.println("'" + text3 + "'");
        System.out.println();

        // 包含三重引号：需要转义
        String text4 = """
                使用三重引号：\"\"\"
                定义文本块
                """;
        System.out.println("转义三重引号:");
        System.out.println(text4);
    }

    /**
     * 5. 格式化方法
     */
    private static void demonstrateFormatting() {
        System.out.println("5. 格式化方法\n");

        String name = "王五";
        int age = 28;
        String city = "深圳";

        // 使用formatted()方法
        String text1 = """
                姓名：%s
                年龄：%d
                城市：%s
                """.formatted(name, age, city);

        System.out.println("使用formatted()方法:");
        System.out.println(text1);

        // 使用String.format()
        String text2 = String.format("""
                用户信息
                --------
                姓名：%s
                年龄：%d岁
                城市：%s
                """, name, age, city);

        System.out.println("使用String.format():");
        System.out.println(text2);

        // 使用stripIndent()去除缩进
        String text3 = """
                    Line 1
                    Line 2
                    Line 3
                    """.stripIndent();

        System.out.println("stripIndent()去除缩进:");
        System.out.println("'" + text3 + "'");
        System.out.println();
    }

    /**
     * 6. SQL查询语句
     */
    private static void demonstrateSQLQueries() {
        System.out.println("6. SQL查询语句\n");

        // 传统方式
        String oldSql = "SELECT u.id, u.name, u.email, o.order_id, o.amount\n" +
                       "FROM users u\n" +
                       "LEFT JOIN orders o ON u.id = o.user_id\n" +
                       "WHERE u.status = 'ACTIVE'\n" +
                       "AND o.created_at >= '2024-01-01'\n" +
                       "ORDER BY o.created_at DESC";

        // 使用文本块
        String newSql = """
                SELECT u.id, u.name, u.email, o.order_id, o.amount
                FROM users u
                LEFT JOIN orders o ON u.id = o.user_id
                WHERE u.status = 'ACTIVE'
                  AND o.created_at >= '2024-01-01'
                ORDER BY o.created_at DESC
                """;

        System.out.println("文本块SQL查询:");
        System.out.println(newSql);

        // 参数化查询
        String userId = "12345";
        String parameterizedSql = """
                SELECT *
                FROM users
                WHERE id = '%s'
                  AND status = 'ACTIVE'
                """.formatted(userId);

        System.out.println("参数化SQL:");
        System.out.println(parameterizedSql);
    }

    /**
     * 7. JSON处理
     */
    private static void demonstrateJSONHandling() {
        System.out.println("7. JSON处理\n");

        String name = "赵六";
        int age = 35;
        String[] hobbies = {"阅读", "游泳", "编程"};

        String json = """
                {
                  "name": "%s",
                  "age": %d,
                  "hobbies": ["%s", "%s", "%s"],
                  "address": {
                    "city": "杭州",
                    "district": "西湖区"
                  },
                  "active": true
                }
                """.formatted(name, age, hobbies[0], hobbies[1], hobbies[2]);

        System.out.println("JSON对象:");
        System.out.println(json);

        // REST API响应模拟
        String apiResponse = """
                {
                  "code": 200,
                  "message": "success",
                  "data": {
                    "users": [
                      {"id": 1, "name": "用户1"},
                      {"id": 2, "name": "用户2"}
                    ],
                    "total": 2
                  }
                }
                """;

        System.out.println("API响应示例:");
        System.out.println(apiResponse);
    }

    /**
     * 8. HTML模板
     */
    private static void demonstrateHTMLTemplates() {
        System.out.println("8. HTML模板\n");

        String title = "欢迎页面";
        String username = "用户123";

        String html = """
                <!DOCTYPE html>
                <html lang="zh-CN">
                <head>
                    <meta charset="UTF-8">
                    <title>%s</title>
                </head>
                <body>
                    <h1>欢迎, %s!</h1>
                    <div class="content">
                        <p>这是使用文本块生成的HTML页面。</p>
                        <ul>
                            <li>特性1：简洁清晰</li>
                            <li>特性2：易于维护</li>
                            <li>特性3：所见即所得</li>
                        </ul>
                    </div>
                </body>
                </html>
                """.formatted(title, username);

        System.out.println("HTML模板:");
        System.out.println(html);
    }

    /**
     * 9. 实际应用示例
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("9. 实际应用示例\n");

        // 示例1: MyBatis XML映射
        System.out.println("【示例1】MyBatis XML映射:");
        String mybatisXml = """
                <mapper namespace="com.example.UserMapper">
                    <select id="findUserById" resultType="User">
                        SELECT id, name, email
                        FROM users
                        WHERE id = #{id}
                    </select>
                </mapper>
                """;
        System.out.println(mybatisXml);

        // 示例2: 错误消息
        System.out.println("【示例2】多行错误消息:");
        String errorMessage = """
                操作失败！

                错误详情：
                - 数据库连接超时
                - 重试次数已达上限

                建议操作：
                1. 检查网络连接
                2. 确认数据库服务状态
                3. 联系系统管理员
                """;
        System.out.println(errorMessage);

        // 示例3: RESTful API文档
        System.out.println("【示例3】API文档:");
        String apiDoc = """
                API: POST /api/users

                Description: 创建新用户

                Request Body:
                {
                  "name": "string",
                  "email": "string",
                  "age": "integer"
                }

                Response: 201 Created
                {
                  "id": "string",
                  "name": "string",
                  "createdAt": "datetime"
                }
                """;
        System.out.println(apiDoc);

        System.out.println("\nSpring Boot应用场景：");
        System.out.println("1. JdbcTemplate中的SQL查询");
        System.out.println("2. @Query注解中的JPQL语句");
        System.out.println("3. 测试用例中的JSON mock数据");
        System.out.println("4. 异常消息和日志输出");
        System.out.println("5. 邮件模板和通知内容");
        System.out.println();
    }
}
