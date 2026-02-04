package basics;

/**
 * Java异常处理学习
 * 学习目标：
 * 1. 理解异常的概念
 * 2. 掌握try-catch-finally的使用
 * 3. 学习自定义异常
 * 4. 理解throws关键字
 */
public class ExceptionHandling {
    public static void main(String[] args) {
        System.out.println("=== 异常处理基础 ===\n");

        // 示例1：处理算术异常
        handleArithmeticException();

        // 示例2：处理数组越界异常
        handleArrayException();

        // 示例3：多重捕获
        handleMultipleExceptions();

        // 示例4：finally的使用
        demonstrateFinally();

        // 示例5：自定义异常
        try {
            validateAge(15);
        } catch (InvalidAgeException e) {
            System.out.println("捕获到异常: " + e.getMessage());
        }

        // 示例6：方法抛出异常
        try {
            readFile("nonexistent.txt");
        } catch (Exception e) {
            System.out.println("文件读取失败: " + e.getMessage());
        }
    }

    // 处理算术异常
    public static void handleArithmeticException() {
        System.out.println("1. 处理除零异常");
        try {
            int result = 10 / 0;  // 会抛出ArithmeticException
            System.out.println("结果: " + result);
        } catch (ArithmeticException e) {
            System.out.println("错误：不能除以零！");
            System.out.println("异常信息: " + e.getMessage());
        }
        System.out.println("程序继续执行\n");
    }

    // 处理数组越界异常
    public static void handleArrayException() {
        System.out.println("2. 处理数组越界异常");
        int[] numbers = {1, 2, 3};

        try {
            System.out.println("访问索引5: " + numbers[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("错误：数组索引越界！");
            System.out.println("数组长度为: " + numbers.length);
        }
        System.out.println();
    }

    // 多重捕获
    public static void handleMultipleExceptions() {
        System.out.println("3. 多重异常捕获");
        String text = null;

        try {
            // 可能抛出多种异常
            int length = text.length();  // NullPointerException
            int result = 10 / 0;         // ArithmeticException
        } catch (NullPointerException e) {
            System.out.println("错误：对象为空！");
        } catch (ArithmeticException e) {
            System.out.println("错误：算术异常！");
        } catch (Exception e) {
            System.out.println("错误：其他异常 - " + e.getMessage());
        }
        System.out.println();
    }

    // finally的使用
    public static void demonstrateFinally() {
        System.out.println("4. Finally块演示");
        try {
            System.out.println("执行try块");
            int result = 10 / 2;
            System.out.println("结果: " + result);
        } catch (Exception e) {
            System.out.println("执行catch块");
        } finally {
            System.out.println("执行finally块（无论是否有异常都会执行）");
        }
        System.out.println();
    }

    // 使用自定义异常
    public static void validateAge(int age) throws InvalidAgeException {
        System.out.println("5. 自定义异常演示");
        if (age < 18) {
            throw new InvalidAgeException("年龄必须大于等于18岁，当前年龄: " + age);
        }
        System.out.println("年龄验证通过: " + age);
    }

    // 方法声明抛出异常
    public static void readFile(String filename) throws Exception {
        System.out.println("\n6. 方法抛出异常演示");
        if (filename == null || filename.isEmpty()) {
            throw new Exception("文件名不能为空");
        }
        // 模拟文件不存在
        throw new Exception("文件不存在: " + filename);
    }
}

// 自定义异常类
class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}
