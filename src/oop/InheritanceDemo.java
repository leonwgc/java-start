package oop;

/**
 * Java继承和多态学习
 * 学习目标：
 * 1. 理解继承的概念
 * 2. 掌握super关键字的使用
 * 3. 理解方法重写（Override）
 * 4. 掌握多态性
 */
public class InheritanceDemo {
    public static void main(String[] args) {
        System.out.println("=== 继承示例 ===\n");

        // 创建父类对象
        Employee emp = new Employee("张三", 5000);
        emp.displayInfo();
        emp.work();

        System.out.println("\n--- 分隔线 ---\n");

        // 创建子类对象
        Manager manager = new Manager("李四", 10000, "技术部");
        manager.displayInfo();
        manager.work();
        manager.holdMeeting();

        System.out.println("\n--- 分隔线 ---\n");

        Developer developer = new Developer("王五", 8000, "Java");
        developer.displayInfo();
        developer.work();
        developer.code();

        // 多态演示
        System.out.println("\n=== 多态示例 ===\n");
        demonstratePolymorphism();

        // 类型转换
        System.out.println("\n=== 类型转换 ===\n");
        demonstrateTypeCasting();
    }

    // 多态演示
    public static void demonstratePolymorphism() {
        // 父类引用指向子类对象
        Employee emp1 = new Manager("赵六", 12000, "销售部");
        Employee emp2 = new Developer("孙七", 9000, "Python");
        Employee emp3 = new Employee("周八", 6000);

        // 多态：同一个方法调用，不同的对象有不同的行为
        Employee[] employees = {emp1, emp2, emp3};

        for (Employee emp : employees) {
            System.out.println("员工: " + emp.name);
            emp.work();  // 调用各自重写的方法
            System.out.println("月薪: " + emp.calculateBonus());
            System.out.println();
        }
    }

    // 类型转换演示
    public static void demonstrateTypeCasting() {
        Employee emp = new Manager("刘九", 15000, "人事部");

        // 向下转型（需要显式转换）
        if (emp instanceof Manager) {
            Manager mgr = (Manager) emp;
            System.out.println("转换成功！");
            mgr.holdMeeting();
        }

        // instanceof 运算符
        System.out.println("\nemp instanceof Employee: " + (emp instanceof Employee));
        System.out.println("emp instanceof Manager: " + (emp instanceof Manager));
        System.out.println("emp instanceof Developer: " + (emp instanceof Developer));
    }
}

// 父类：员工
class Employee {
    protected String name;
    protected double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public void displayInfo() {
        System.out.println("姓名: " + name);
        System.out.println("基本工资: " + salary);
    }

    public void work() {
        System.out.println(name + " 正在工作");
    }

    public double calculateBonus() {
        return salary * 0.1;  // 10%奖金
    }
}

// 子类：经理
class Manager extends Employee {
    private String department;

    public Manager(String name, double salary, String department) {
        super(name, salary);  // 调用父类构造方法
        this.department = department;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();  // 调用父类方法
        System.out.println("职位: 经理");
        System.out.println("部门: " + department);
    }

    @Override
    public void work() {
        System.out.println(name + " 正在管理 " + department);
    }

    @Override
    public double calculateBonus() {
        return salary * 0.2;  // 经理奖金20%
    }

    public void holdMeeting() {
        System.out.println(name + " 正在主持会议");
    }
}

// 子类：开发人员
class Developer extends Employee {
    private String programmingLanguage;

    public Developer(String name, double salary, String language) {
        super(name, salary);
        this.programmingLanguage = language;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("职位: 开发人员");
        System.out.println("编程语言: " + programmingLanguage);
    }

    @Override
    public void work() {
        System.out.println(name + " 正在用 " + programmingLanguage + " 编程");
    }

    @Override
    public double calculateBonus() {
        return salary * 0.15;  // 开发人员奖金15%
    }

    public void code() {
        System.out.println(name + " 正在写代码");
    }
}
