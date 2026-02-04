package oop;

/**
 * Java接口学习
 * 学习目标：
 * 1. 理解接口的概念和作用
 * 2. 学习接口的定义和实现
 * 3. 理解接口的多实现特性
 * 4. 掌握接口的默认方法（Java 8+）
 */
public class InterfaceDemo {
    public static void main(String[] args) {
        System.out.println("=== 接口示例 ===\n");

        // 创建不同的动物对象
        Animal dog = new Dog();
        Animal cat = new Cat();

        System.out.println("--- 狗的行为 ---");
        dog.eat();
        dog.sleep();
        dog.makeSound();

        System.out.println("\n--- 猫的行为 ---");
        cat.eat();
        cat.sleep();
        cat.makeSound();

        // 多接口实现
        System.out.println("\n--- 机器人示例（多接口实现）---");
        Robot robot = new Robot();
        robot.work();
        robot.charge();
        robot.displayInfo();

        // 接口作为参数类型
        System.out.println("\n--- 接口作为参数 ---");
        performAnimalAction(dog);
        performAnimalAction(cat);
    }

    // 接口作为方法参数
    public static void performAnimalAction(Animal animal) {
        animal.makeSound();
        animal.eat();
    }
}

// 定义动物接口
interface Animal {
    // 抽象方法（默认public abstract）
    void eat();
    void sleep();
    void makeSound();

    // 默认方法（Java 8+）
    default void breathe() {
        System.out.println("动物在呼吸");
    }

    // 静态方法（Java 8+）
    static void printInfo() {
        System.out.println("这是一个动物接口");
    }
}

// 实现接口：狗
class Dog implements Animal {
    @Override
    public void eat() {
        System.out.println("狗在吃骨头");
    }

    @Override
    public void sleep() {
        System.out.println("狗在睡觉");
    }

    @Override
    public void makeSound() {
        System.out.println("汪汪汪！");
    }
}

// 实现接口：猫
class Cat implements Animal {
    @Override
    public void eat() {
        System.out.println("猫在吃鱼");
    }

    @Override
    public void sleep() {
        System.out.println("猫在睡觉");
    }

    @Override
    public void makeSound() {
        System.out.println("喵喵喵！");
    }
}

// 多接口实现示例
interface Workable {
    void work();
    default void displayInfo() {
        System.out.println("这是一个可工作的对象");
    }
}

interface Chargeable {
    void charge();
}

class Robot implements Workable, Chargeable {
    @Override
    public void work() {
        System.out.println("机器人正在工作");
    }

    @Override
    public void charge() {
        System.out.println("机器人正在充电");
    }
}
