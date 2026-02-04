package oop;

/**
 * Java抽象类学习
 * 学习目标：
 * 1. 理解抽象类的概念
 * 2. 掌握抽象方法的使用
 * 3. 理解抽象类与接口的区别
 * 4. 学习模板方法模式
 */
public class AbstractClassDemo {
    public static void main(String[] args) {
        System.out.println("=== 抽象类示例 ===\n");

        // 创建具体的形状对象
        Shape circle = new Circle(5.0);
        Shape rectangle = new Rectangle(4.0, 6.0);
        Shape triangle = new Triangle(3.0, 4.0);

        System.out.println("--- 圆形 ---");
        circle.display();

        System.out.println("\n--- 矩形 ---");
        rectangle.display();

        System.out.println("\n--- 三角形 ---");
        triangle.display();

        // 模板方法模式示例
        System.out.println("\n=== 模板方法模式 ===\n");

        Beverage tea = new Tea();
        Beverage coffee = new Coffee();

        System.out.println("--- 制作茶 ---");
        tea.prepare();

        System.out.println("\n--- 制作咖啡 ---");
        coffee.prepare();
    }
}

// 抽象类：形状
abstract class Shape {
    protected String color = "无色";

    // 抽象方法：计算面积
    public abstract double calculateArea();

    // 抽象方法：计算周长
    public abstract double calculatePerimeter();

    // 具体方法：显示信息
    public void display() {
        System.out.println("形状类型: " + this.getClass().getSimpleName());
        System.out.println("颜色: " + color);
        System.out.println("面积: " + calculateArea());
        System.out.println("周长: " + calculatePerimeter());
    }

    // 具体方法：设置颜色
    public void setColor(String color) {
        this.color = color;
    }
}

// 具体类：圆形
class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
        this.color = "红色";
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * Math.PI * radius;
    }
}

// 具体类：矩形
class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
        this.color = "蓝色";
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    @Override
    public double calculatePerimeter() {
        return 2 * (width + height);
    }
}

// 具体类：三角形
class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
        this.color = "绿色";
    }

    @Override
    public double calculateArea() {
        return 0.5 * base * height;
    }

    @Override
    public double calculatePerimeter() {
        // 简化计算：假设是等腰三角形
        double side = Math.sqrt((base / 2) * (base / 2) + height * height);
        return base + 2 * side;
    }
}

// 模板方法模式示例
abstract class Beverage {
    // 模板方法：定义算法骨架
    public final void prepare() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
        System.out.println("准备完成！");
    }

    // 具体方法
    private void boilWater() {
        System.out.println("1. 烧水");
    }

    private void pourInCup() {
        System.out.println("3. 倒入杯中");
    }

    // 抽象方法：由子类实现
    protected abstract void brew();
    protected abstract void addCondiments();
}

class Tea extends Beverage {
    @Override
    protected void brew() {
        System.out.println("2. 用热水浸泡茶叶");
    }

    @Override
    protected void addCondiments() {
        System.out.println("4. 加柠檬");
    }
}

class Coffee extends Beverage {
    @Override
    protected void brew() {
        System.out.println("2. 用热水冲泡咖啡粉");
    }

    @Override
    protected void addCondiments() {
        System.out.println("4. 加糖和牛奶");
    }
}
