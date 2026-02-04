package oop;

/**
 * 面向对象编程 - 类和对象
 * 学习目标：
 * 1. 理解类和对象的概念
 * 2. 学习构造方法
 * 3. 理解封装
 */
public class Person {
    // 成员变量（属性）
    private String name;
    private int age;
    private String email;

    // 构造方法
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    // Getter方法
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    // Setter方法
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        if (age > 0 && age < 150) {
            this.age = age;
        } else {
            System.out.println("年龄不合法！");
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // 成员方法
    public void introduce() {
        System.out.println("大家好，我叫" + name + "，今年" + age + "岁");
        System.out.println("我的邮箱是：" + email);
    }

    public void celebrateBirthday() {
        age++;
        System.out.println("生日快乐！现在" + age + "岁了！");
    }

    // 主方法 - 测试类
    public static void main(String[] args) {
        System.out.println("=== 创建对象示例 ===\n");

        // 创建对象
        Person person1 = new Person("张三", 25, "zhangsan@example.com");
        Person person2 = new Person("李四", 30, "lisi@example.com");

        // 调用方法
        person1.introduce();
        System.out.println();
        person2.introduce();

        System.out.println("\n=== 修改对象属性 ===\n");
        person1.setAge(26);
        person1.introduce();

        System.out.println();
        person1.celebrateBirthday();
    }
}
