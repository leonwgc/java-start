package basics;

/**
 * Java数据类型学习
 * 学习目标：
 * 1. 了解Java的基本数据类型
 * 2. 学习变量的声明和初始化
 * 3. 理解不同数据类型的使用场景
 */
public class DataTypes {
    public static void main(String[] args) {
        // 整数类型
        byte myByte = 100;              // 8位，范围：-128 到 127
        short myShort = 1000;           // 16位，范围：-32768 到 32767
        int myInt = 100000;             // 32位，最常用的整数类型
        long myLong = 1000000000L;      // 64位，大数字需要加L后缀

        System.out.println("=== 整数类型 ===");
        System.out.println("byte: " + myByte);
        System.out.println("short: " + myShort);
        System.out.println("int: " + myInt);
        System.out.println("long: " + myLong);

        // 浮点类型
        float myFloat = 3.14f;          // 32位，需要加f后缀
        double myDouble = 3.141592653;  // 64位，默认的浮点类型

        System.out.println("\n=== 浮点类型 ===");
        System.out.println("float: " + myFloat);
        System.out.println("double: " + myDouble);

        // 字符类型
        char myChar = 'A';              // 16位Unicode字符
        char chineseChar = '中';

        System.out.println("\n=== 字符类型 ===");
        System.out.println("char: " + myChar);
        System.out.println("中文字符: " + chineseChar);

        // 布尔类型
        boolean isJavaFun = true;
        boolean isFishTasty = false;

        System.out.println("\n=== 布尔类型 ===");
        System.out.println("Java好玩吗？" + isJavaFun);
        System.out.println("鱼好吃吗？" + isFishTasty);

        // 字符串（引用类型）
        String greeting = "Hello, Java!";
        String name = "Java学习者";

        System.out.println("\n=== 字符串类型 ===");
        System.out.println(greeting);
        System.out.println("你好，" + name);
    }
}
