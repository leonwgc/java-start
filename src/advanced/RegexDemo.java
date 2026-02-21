package advanced;

import java.util.regex.*;
import java.util.*;

// 使用建议：
// 场景 推荐方式
// 一次性验证 Pattern.matches() - 简洁
// 多次验证同一模式 Pattern.compile() + 复用 - 高效 「预编译模式」
// 验证工具类 static final Pattern - 最佳实践

/**
 * 正则表达式学习
 * 学习目标：
 * 1. 理解正则表达式的基本语法
 * 2. 掌握Pattern和Matcher的使用
 * 3. 学习常见的正则表达式模式
 * 4. 理解正则在Spring中的应用
 */
public class RegexDemo {
    public static void main(String[] args) {
        System.out.println("=== 正则表达式学习 ===\n");

        // 示例1：基本匹配
        demonstrateBasicMatching();

        // 示例2：查找和替换
        demonstrateFindAndReplace();

        // 示例3：分组捕获
        demonstrateGroupCapture();

        // 示例4：常用验证
        demonstrateCommonValidations();

        // 示例5：实战案例
        demonstratePracticalExample();
    }

    // 基本匹配
    public static void demonstrateBasicMatching() {
        System.out.println("1. 基本匹配\n");
        System.out.println("作用：判断字符串是否符合某个模式");
        System.out.println("Spring应用：表单验证、@Pattern注解等\n");

        String text = "Hello123";

        // 简单匹配
        boolean hasDigit = text.matches(".*\\d+.*");
        System.out.println("包含数字: " + hasDigit);

        boolean isAllDigits = "12345".matches("\\d+");
        System.out.println("全是数字: " + isAllDigits);

        boolean isEmail = "user@example.com".matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        System.out.println("是邮箱格式: " + isEmail);

        // Pattern和Matcher
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        System.out.println("找到数字: " + matcher.find());
        System.out.println();
    }

    // 查找和替换
    public static void demonstrateFindAndReplace() {
        System.out.println("2. 查找和替换\n");

        String text = "我的电话是13812345678，备用电话是13987654321";

        // 查找所有匹配
        Pattern pattern = Pattern.compile("1[3-9]\\d{9}");
        Matcher matcher = pattern.matcher(text);

        System.out.println("查找所有电话号码:");
        while (matcher.find()) {
            System.out.println("  找到: " + matcher.group() + " (位置: " + matcher.start() + "-" + matcher.end() + ")");
        }

        // 替换
        String masked = text.replaceAll("(1[3-9]\\d{9})", "***隐藏***");
        System.out.println("\n替换后: " + masked);

        // 替换第一个匹配
        String maskedFirst = text.replaceFirst("(1[3-9]\\d{9})", "***");
        System.out.println("只替换第一个: " + maskedFirst);

        // 使用Matcher替换（可以自定义逻辑）
        matcher.reset();
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String phone = matcher.group();
            String masked2 = phone.substring(0, 3) + "****" + phone.substring(7);
            matcher.appendReplacement(sb, masked2);
        }
        matcher.appendTail(sb);
        System.out.println("自定义脱敏: " + sb.toString());
        System.out.println();
    }

    // 分组捕获
    public static void demonstrateGroupCapture() {
        System.out.println("3. 分组捕获\n");
        System.out.println("作用：提取字符串中的特定部分");
        System.out.println("Spring应用：解析URL、提取参数等\n");

        // 日期格式提取
        String dateText = "今天是2024-03-15，明天是2024-03-16";
        Pattern datePattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
        Matcher dateMatcher = datePattern.matcher(dateText);

        System.out.println("提取日期:");
        while (dateMatcher.find()) {
            System.out.println("  完整日期: " + dateMatcher.group(0));
            System.out.println("    年: " + dateMatcher.group(1));
            System.out.println("    月: " + dateMatcher.group(2));
            System.out.println("    日: " + dateMatcher.group(3));
        }

        // 邮箱提取
        String email = "联系方式：zhangsan@example.com";
        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})");
        Matcher emailMatcher = emailPattern.matcher(email);

        if (emailMatcher.find()) {
            System.out.println("\n提取邮箱:");
            System.out.println("  完整邮箱: " + emailMatcher.group(0));
            System.out.println("  用户名: " + emailMatcher.group(1));
            System.out.println("  域名: " + emailMatcher.group(2));
            System.out.println("  顶级域: " + emailMatcher.group(3));
        }
        System.out.println();
    }

    // 常用验证
    public static void demonstrateCommonValidations() {
        System.out.println("4. 常用验证模式\n");

        // 测试数据
        String[] testCases = {
                "user@example.com",
                "13812345678",
                "320123199001011234",
                "https://www.example.com",
                "192.168.1.1",
                "abc123!@#"
        };

        System.out.println("验证结果:");
        for (String test : testCases) {
            System.out.println("\n测试: " + test);
            System.out.println("  邮箱: " + Validator.isEmail(test));
            System.out.println("  手机号: " + Validator.isPhone(test));
            System.out.println("  身份证: " + Validator.isIdCard(test));
            System.out.println("  URL: " + Validator.isUrl(test));
            System.out.println("  IP地址: " + Validator.isIpAddress(test));
            System.out.println("  强密码: " + Validator.isStrongPassword(test));
        }
        System.out.println();
    }

    // 实战案例
    public static void demonstratePracticalExample() {
        System.out.println("5. 实战案例 - 用户注册表单验证\n");

        // 模拟用户输入
        UserForm form1 = new UserForm(
                "zhangsan",
                "Zhang123!@#",
                "zhangsan@example.com",
                "13812345678");

        UserForm form2 = new UserForm(
                "ls", // 用户名太短
                "123456", // 密码太弱
                "invalid-email", // 邮箱格式错误
                "12345" // 电话号码格式错误
        );

        System.out.println("表单1验证:");
        validateUserForm(form1);

        System.out.println("\n表单2验证:");
        validateUserForm(form2);

        System.out.println("\n💡 Spring中的应用：");
        System.out.println("- @Pattern注解验证表单输入");
        System.out.println("- @Email注解验证邮箱格式");
        System.out.println("- Spring MVC中的RequestMapping路径匹配");
        System.out.println("- Spring Security中的URL权限匹配");
        System.out.println("- 自定义验证注解中使用正则表达式\n");
    }

    // 验证用户表单
    public static void validateUserForm(UserForm form) {
        List<String> errors = new ArrayList<>();

        // 用户名验证（3-20个字符，字母数字下划线）
        if (!form.username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            errors.add("用户名格式错误（3-20个字符，只能包含字母数字下划线）");
        }

        // 密码验证（至少8位，包含大小写字母、数字、特殊字符）
        if (!Validator.isStrongPassword(form.password)) {
            errors.add("密码强度不够（至少8位，包含大小写字母、数字、特殊字符）");
        }

        // 邮箱验证
        if (!Validator.isEmail(form.email)) {
            errors.add("邮箱格式错误");
        }

        // 手机号验证
        if (!Validator.isPhone(form.phone)) {
            errors.add("手机号格式错误");
        }

        // 输出结果
        if (errors.isEmpty()) {
            System.out.println("✓ 验证通过");
        } else {
            System.out.println("✗ 验证失败:");
            errors.forEach(error -> System.out.println("  - " + error));
        }
    }

    // 使用建议：
    // 场景 推荐方式
    // 一次性验证 Pattern.matches() - 简洁
    // 多次验证同一模式 Pattern.compile() + 复用 - 高效 「预编译模式」
    // 验证工具类 static final Pattern - 最佳实践

    // ========== 辅助类 ==========

    // 验证器类
    static class Validator {
        // 邮箱验证
        private static final Pattern EMAIL_PATTERN = Pattern
                .compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        // 手机号验证（中国大陆）
        private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

        // 身份证验证（18位）
        private static final Pattern ID_CARD_PATTERN = Pattern
                .compile("^[1-9]\\d{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$");

        // URL验证
        private static final Pattern URL_PATTERN = Pattern
                .compile("^(https?://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$");

        // IP地址验证
        private static final Pattern IP_PATTERN = Pattern
                .compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");

        // 强密码验证（至少8位，包含大小写字母、数字、特殊字符）
        private static final Pattern STRONG_PASSWORD_PATTERN = Pattern
                .compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$");

        public static boolean isEmail(String email) {
            return EMAIL_PATTERN.matcher(email).matches();
        }

        public static boolean isPhone(String phone) {
            return PHONE_PATTERN.matcher(phone).matches();
        }

        public static boolean isIdCard(String idCard) {
            return ID_CARD_PATTERN.matcher(idCard).matches();
        }

        public static boolean isUrl(String url) {
            return URL_PATTERN.matcher(url).matches();
        }

        public static boolean isIpAddress(String ip) {
            return IP_PATTERN.matcher(ip).matches();
        }

        public static boolean isStrongPassword(String password) {
            return STRONG_PASSWORD_PATTERN.matcher(password).matches();
        }
    }

    // 用户表单类
    static class UserForm {
        String username;
        String password;
        String email;
        String phone;

        public UserForm(String username, String password, String email, String phone) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.phone = phone;
        }
    }
}
