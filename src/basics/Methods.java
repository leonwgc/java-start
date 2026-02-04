package basics;

/**
 * Java方法学习
 * 学习目标：
 * 1. 理解方法的定义和调用
 * 2. 学习参数传递
 * 3. 理解返回值
 */
public class Methods {
    public static void main(String[] args) {
        System.out.println("=== 方法示例 ===\n");

        // 调用无参数无返回值的方法
        greet();

        // 调用有参数的方法
        System.out.println();
        greetWithName("小明");

        // 调用有返回值的方法
        System.out.println();
        int sum = add(10, 20);
        System.out.println("10 + 20 = " + sum);

        // 调用计算方法
        System.out.println();
        double area = calculateRectangleArea(5.0, 3.0);
        System.out.println("矩形面积: " + area);

        // 调用判断方法
        System.out.println();
        boolean result = isEven(8);
        System.out.println("8是偶数吗？" + result);
    }

    // 无参数无返回值的方法
    public static void greet() {
        System.out.println("你好！欢迎学习Java方法！");
    }

    // 有参数无返回值的方法
    public static void greetWithName(String name) {
        System.out.println("你好，" + name + "！");
        System.out.println("很高兴认识你！");
    }

    // 有参数有返回值的方法
    public static int add(int a, int b) {
        return a + b;
    }

    // 计算矩形面积
    public static double calculateRectangleArea(double length, double width) {
        return length * width;
    }

    // 判断是否为偶数
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }
}
