package advanced;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Switch表达式详解（Java 14+）
 * 学习目标：
 * 1. 理解传统switch语句的局限性
 * 2. 掌握新的switch表达式语法
 * 3. 学习箭头语法（->）和yield关键字
 * 4. 了解模式匹配（Java 17增强）
 *
 * Switch表达式的优势：
 * - 可以直接返回值（作为表达式使用）
 * - 箭头语法更简洁
 * - 自动break，避免fall-through错误
 * - 编译器检查完整性（穷尽性检查）
 *
 * Spring应用：
 * - 状态机逻辑
 * - 请求路由
 * - 错误代码映射
 * - 配置值转换
 */
public class SwitchExpressionDemo {

    public static void main(String[] args) {
        System.out.println("=== Switch表达式详解 ===\n");

        demonstrateTraditionalSwitch();
        demonstrateSwitchExpression();
        demonstrateArrowSyntax();
        demonstrateYieldKeyword();
        demonstrateMultipleLabels();
        demonstrateEnumSwitch();
        demonstrateRealWorldExamples();
    }

    /**
     * 1. 传统switch语句的问题
     */
    private static void demonstrateTraditionalSwitch() {
        System.out.println("1. 传统switch语句的问题\n");

        int day = 3;
        String dayType;

        // 传统写法：冗长，需要break，容易出错
        switch (day) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                dayType = "工作日";
                break;
            case 6:
            case 7:
                dayType = "周末";
                break;
            default:
                dayType = "无效";
                break;
        }

        System.out.println("传统switch结果: " + dayType);
        System.out.println("问题：");
        System.out.println("  ✗ 必须手动break，容易遗漏");
        System.out.println("  ✗ 不能直接返回值");
        System.out.println("  ✗ 代码冗长");
        System.out.println();
    }

    /**
     * 2. 新的switch表达式
     */
    private static void demonstrateSwitchExpression() {
        System.out.println("2. 新的switch表达式\n");

        int day = 3;

        // 新写法：简洁，直接返回值
        String dayType = switch (day) {
            case 1, 2, 3, 4, 5 -> "工作日";
            case 6, 7 -> "周末";
            default -> "无效";
        };

        System.out.println("Switch表达式结果: " + dayType);
        System.out.println("优势：");
        System.out.println("  ✓ 自动break，不会fall-through");
        System.out.println("  ✓ 可以直接赋值");
        System.out.println("  ✓ 代码简洁");
        System.out.println();
    }

    /**
     * 3. 箭头语法（->）详解
     */
    private static void demonstrateArrowSyntax() {
        System.out.println("3. 箭头语法详解\n");

        String month = "一月";

        // 箭头右边可以是单个表达式
        int days = switch (month) {
            case "一月", "三月", "五月", "七月", "八月", "十月", "十二月" -> 31;
            case "四月", "六月", "九月", "十一月" -> 30;
            case "二月" -> 28;
            default -> 0;
        };

        System.out.println(month + "有 " + days + " 天");

        // 箭头右边也可以是代码块
        String season = switch (month) {
            case "一月", "二月", "十二月" -> {
                System.out.println("  → 正在计算季节...");
                yield "冬季";  // 使用yield返回值
            }
            case "三月", "四月", "五月" -> {
                System.out.println("  → 正在计算季节...");
                yield "春季";
            }
            case "六月", "七月", "八月" -> {
                System.out.println("  → 正在计算季节...");
                yield "夏季";
            }
            default -> {
                System.out.println("  → 正在计算季节...");
                yield "秋季";
            }
        };

        System.out.println(month + "属于" + season);
        System.out.println();
    }

    /**
     * 4. yield关键字详解
     */
    private static void demonstrateYieldKeyword() {
        System.out.println("4. yield关键字详解\n");

        int score = 85;

        // 当需要复杂逻辑时，使用代码块和yield
        String grade = switch (score / 10) {
            case 10, 9 -> {
                System.out.println("  → 优秀成绩！获得额外奖励");
                yield "A";
            }
            case 8 -> {
                System.out.println("  → 良好成绩");
                yield "B";
            }
            case 7 -> {
                System.out.println("  → 中等成绩");
                yield "C";
            }
            case 6 -> {
                System.out.println("  → 及格成绩");
                yield "D";
            }
            default -> {
                System.out.println("  → 不及格，需要补考");
                yield "F";
            }
        };

        System.out.println("分数 " + score + " 对应等级: " + grade);
        System.out.println();

        System.out.println("yield vs return:");
        System.out.println("  • yield - 从switch表达式返回值");
        System.out.println("  • return - 从方法返回值");
        System.out.println();
    }

    /**
     * 5. 多个标签（Multiple Labels）
     */
    private static void demonstrateMultipleLabels() {
        System.out.println("5. 多个标签用法\n");

        String fruit = "苹果";

        String color = switch (fruit) {
            case "苹果", "草莓", "樱桃" -> "红色";
            case "香蕉", "柠檬" -> "黄色";
            case "葡萄", "蓝莓" -> "紫色";
            case "橙子" -> "橙色";
            default -> "未知颜色";
        };

        System.out.println(fruit + "的颜色是: " + color);
        System.out.println();
    }

    /**
     * 6. 枚举类型的switch（穷尽性检查）
     */
    private static void demonstrateEnumSwitch() {
        System.out.println("6. 枚举类型的switch\n");

        DayOfWeek today = DayOfWeek.FRIDAY;

        // 对于枚举，编译器会检查是否覆盖所有情况
        String activity = switch (today) {
            case MONDAY -> "周会";
            case TUESDAY, WEDNESDAY, THURSDAY -> "正常工作";
            case FRIDAY -> "周报总结";
            case SATURDAY, SUNDAY -> "休息";
        };

        System.out.println(today + "的安排: " + activity);
        System.out.println();

        // 如果没有default，编译器会确保覆盖所有枚举值
        System.out.println("枚举switch的特点：");
        System.out.println("  ✓ 编译器检查完整性");
        System.out.println("  ✓ 不需要default（如果覆盖所有情况）");
        System.out.println("  ✓ 添加新枚举值时会产生编译错误");
        System.out.println();
    }

    /**
     * 7. 实际应用示例
     */
    private static void demonstrateRealWorldExamples() {
        System.out.println("7. 实际应用示例\n");

        // 示例1: HTTP状态码处理
        System.out.println("【示例1】HTTP状态码处理:");
        int statusCode = 404;
        String message = getHttpStatusMessage(statusCode);
        System.out.println("状态码 " + statusCode + ": " + message);
        System.out.println();

        // 示例2: 订单状态机
        System.out.println("【示例2】订单状态处理:");
        Order order = new Order("ORD001", OrderStatus.PAID);
        String nextAction = getOrderNextAction(order);
        System.out.println("订单 " + order.id() + " [" + order.status() + "] → " + nextAction);
        System.out.println();

        // 示例3: 操作系统检测
        // System.out.println("【示例3】操作系统适配:");
        // String os = System.getProperty("os.name").toLowerCase();
        // String command = getOSCommand(os);
        // System.out.println("当前系统: " + os);
        // System.out.println("清屏命令: " + command);
        // System.out.println();

        System.out.println("Spring Boot应用场景：");
        System.out.println("1. Controller中的请求路由");
        System.out.println("2. Service中的业务状态判断");
        System.out.println("3. Exception Handler中的错误代码映射");
        System.out.println("4. 配置类中的环境判断");
        System.out.println();
    }

    // ==================== 辅助方法 ====================

    /**
     * HTTP状态码处理示例
     */
    private static String getHttpStatusMessage(int statusCode) {
        return switch (statusCode) {
            case 200 -> "成功";
            case 201 -> "已创建";
            case 204 -> "无内容";
            case 400 -> "错误请求";
            case 401 -> "未授权";
            case 403 -> "禁止访问";
            case 404 -> "未找到";
            case 500 -> "服务器错误";
            default -> "未知状态码";
        };
    }

    /**
     * 订单状态处理示例
     */
    private static String getOrderNextAction(Order order) {
        return switch (order.status()) {
            case CREATED -> "等待支付";
            case PAID -> "开始处理订单";
            case PROCESSING -> "准备发货";
            case SHIPPED -> "等待收货确认";
            case COMPLETED -> "订单完成";
            case CANCELLED -> "订单已取消";
        };
    }

    /**
     * 操作系统命令适配示例
     */
    // private static String getOSCommand(String os) {
    //     return switch (os) {
    //         // java 21 and above support pattern matching in switch
    //         case String s when s.contains("win") -> "cls";
    //         case String s when s.contains("mac") -> "clear";
    //         case String s when s.contains("nix") || s.contains("nux") -> "clear";
    //         default -> "unknown";
    //     };
    // }

    // ==================== 辅助类 ====================

    /**
     * 订单状态枚举
     */
    enum OrderStatus {
        CREATED,      // 已创建
        PAID,         // 已支付
        PROCESSING,   // 处理中
        SHIPPED,      // 已发货
        COMPLETED,    // 已完成
        CANCELLED     // 已取消
    }

    /**
     * 订单记录
     */
    record Order(String id, OrderStatus status) {}
}
