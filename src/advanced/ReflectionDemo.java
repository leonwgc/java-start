package advanced;

import java.lang.reflect.*;

/**
 * Java反射学习
 * 学习目标：
 * 1. 理解反射的概念和作用
 * 2. 掌握获取Class对象的方法
 * 3. 学习操作类的属性和方法
 * 4. 理解反射在Spring中的应用
 */
public class ReflectionDemo {
    public static void main(String[] args) {
        System.out.println("=== 反射学习 ===\n");

        try {
            // 示例1：获取Class对象
            demonstrateGetClass();

            // 示例2：获取类信息
            demonstrateClassInfo();

            // 示例3：操作字段
            demonstrateFieldAccess();

            // 示例4：调用方法
            demonstrateMethodInvoke();

            // 示例5：创建实例
            demonstrateCreateInstance();

            // 示例6：注解读取
            demonstrateAnnotationReading();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取Class对象的三种方式
    public static void demonstrateGetClass() throws ClassNotFoundException {
        System.out.println("1. 获取Class对象的三种方式\n");

        // 方式1：通过对象的getClass()方法
        Student student = new Student("张三", 20);
        Class<?> clazz1 = student.getClass();
        System.out.println("方式1: " + clazz1.getName());

        // 方式2：通过类名.class
        Class<?> clazz2 = Student.class;
        System.out.println("方式2: " + clazz2.getName());

        // 方式3：通过Class.forName()（最常用）
        Class<?> clazz3 = Class.forName("advanced.Student");
        System.out.println("方式3: " + clazz3.getName());

        // 三种方式获取的是同一个Class对象
        System.out.println("\n是否相同: " + (clazz1 == clazz2 && clazz2 == clazz3));
        System.out.println();
    }

    // 获取类的信息
    public static void demonstrateClassInfo() throws ClassNotFoundException {
        System.out.println("2. 获取类的详细信息\n");

        Class<?> clazz = Class.forName("advanced.Student");

        // 获取类名
        System.out.println("类名: " + clazz.getName());
        System.out.println("简单类名: " + clazz.getSimpleName());

        // 获取所有字段
        System.out.println("\n所有字段:");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  - " + field.getName() + " (" + field.getType().getSimpleName() + ")");
        }

        // 获取所有方法
        System.out.println("\n所有方法:");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  - " + method.getName());
        }

        // 获取构造方法
        System.out.println("\n构造方法:");
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("  - " + constructor);
        }
        System.out.println();
    }

    // 操作字段
    public static void demonstrateFieldAccess() throws Exception {
        System.out.println("3. 通过反射访问和修改字段\n");

        Student student = new Student("李四", 22);
        Class<?> clazz = student.getClass();

        // 获取private字段
        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");

        // 设置可访问（破坏封装）
        nameField.setAccessible(true);
        ageField.setAccessible(true);

        // 读取字段值
        String name = (String) nameField.get(student);
        int age = (int) ageField.get(student);
        System.out.println("原始值 - 姓名: " + name + ", 年龄: " + age);

        // 修改字段值
        nameField.set(student, "王五");
        ageField.set(student, 25);

        System.out.println("修改后 - 姓名: " + student.getName() + ", 年龄: " + student.getAge());
        System.out.println();
    }

    // 调用方法
    public static void demonstrateMethodInvoke() throws Exception {
        System.out.println("4. 通过反射调用方法\n");

        Student student = new Student("赵六", 21);
        Class<?> clazz = student.getClass();

        // 获取public方法
        Method introduceMethod = clazz.getMethod("introduce");
        introduceMethod.invoke(student);

        // 获取private方法
        Method secretMethod = clazz.getDeclaredMethod("secretMethod");
        secretMethod.setAccessible(true);
        secretMethod.invoke(student);

        // 调用带参数的方法
        Method setNameMethod = clazz.getMethod("setName", String.class);
        setNameMethod.invoke(student, "孙七");
        System.out.println("\n修改后的名字: " + student.getName());
        System.out.println();
    }

    // 创建实例
    public static void demonstrateCreateInstance() throws Exception {
        System.out.println("5. 通过反射创建对象\n");

        Class<?> clazz = Class.forName("advanced.Student");

        // 方式1：使用无参构造器
        Student student1 = (Student) clazz.getDeclaredConstructor().newInstance();
        student1.setName("钱八");
        student1.setAge(23);
        System.out.println("方式1创建: " + student1.getName());

        // 方式2：使用有参构造器
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class);
        Student student2 = (Student) constructor.newInstance("周九", 24);
        System.out.println("方式2创建: " + student2.getName());

        System.out.println("\n这就是Spring IoC容器创建Bean的原理！");
        System.out.println();
    }

    // 读取注解
    public static void demonstrateAnnotationReading() throws Exception {
        System.out.println("6. 通过反射读取注解\n");

        Class<?> clazz = Class.forName("advanced.Student");

        // 读取类上的注解
        if (clazz.isAnnotationPresent(Entity.class)) {
            Entity entity = clazz.getAnnotation(Entity.class);
            System.out.println("Entity注解 - 表名: " + entity.tableName());
        }

        // 读取字段上的注解
        Field nameField = clazz.getDeclaredField("name");
        if (nameField.isAnnotationPresent(Column.class)) {
            Column column = nameField.getAnnotation(Column.class);
            System.out.println("name字段 - 列名: " + column.name());
        }

        System.out.println("\n这就是Spring如何读取@Autowired、@Service等注解的原理！");
    }
}

// ========== 测试类定义 ==========

@Entity(tableName = "t_student")
class Student {
    @Column(name = "student_name")
    private String name;

    @Column(name = "student_age")
    private int age;

    // 无参构造器（反射需要）
    public Student() {
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public void introduce() {
        System.out.println("我是 " + name + "，今年 " + age + " 岁");
    }

    private void secretMethod() {
        System.out.println("这是一个私有方法，通过反射被调用了！");
    }
}

// 模拟JPA注解
@interface Entity {
    String tableName();
}

@interface Column {
    String name();
}
