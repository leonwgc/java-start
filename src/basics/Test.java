package basics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringJoiner;

// 枚举类型 with value
enum Weekday {
    SUN(1), MON(2), TUE(3), WED(4), THU(5), FRI(6), SAT(7);

    public final int value;

    private Weekday(int value) {
        this.value = value;
    }

}

// 简单的继承示例
class Animal {
    String name;
    int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class Dog extends Animal implements speack {
    public Dog(String name, int age) {
        super(name, age);
    }

    public void bark() {
        System.out.println(name + " says: Woof!");
    }

    @Override
    public void speak() {
        System.out.println(name + " is speaking.");
    }
}

// interface example
interface speack {
    void speak();
}

public class Test {
    public static void main(String[] args) {
        var str = "hello, world";
        System.out.println("This is a test class.");
        System.out.println(str);
        // StringBuilder和StringJoiner
        var strBuilder = new StringBuilder();
        for (var i : str.toCharArray()) {
            strBuilder.append(i);
        }
        var sj = new StringJoiner("-");
        for (var i : str.toCharArray()) {
            sj.add(String.valueOf(i));
        }
        System.out.println(str.length() + "" + strBuilder.toString());
        System.out.println(sj.toString());

        System.out.println(String.join("-", "aa", "bb", "cc"));

        // Enum
        var wk = Weekday.MON;
        System.out.println(wk.value);
        System.out.println(wk.name());

        // Math
        System.out.println(Math.max(10, 20));
        System.out.println(Math.min(10, 20));
        System.out.println(Math.sqrt(16));
        System.out.println(Math.pow(2, 3));
        System.out.println(Math.abs(-5));
        System.out.println(Math.round(3.14));
        System.out.println(Math.ceil(3.14));
        System.out.println(Math.floor(3.14));

        // Random
        var rd = new Random();
        System.out.println(rd.nextInt(100)); // 0-99
        System.out.println(rd.nextDouble()); // 0.0-1.0
        System.out.println(rd.nextBoolean()); // true/false

        // class inheritance
        var dog = new Dog("Buddy", 3);
        dog.bark();
        dog.speak();

        // 用instanceof不但匹配指定类型，还匹配指定类型的子类
        System.out.println(dog instanceof Dog); // true
        System.out.println(dog instanceof Animal); // true

        // reflection
        // 由于JVM为每个加载的class创建了对应的Class实例，并在实例中保存了该class的所有信息，包括类名、包名、父类、实现的接口、所有方法、字段等，因此，如果获取了某个Class实例，我们就可以通过这个Class实例获取到该实例对应的class的所有信息。
        // 这种通过Class实例获取class信息的方法称为反射（Reflection）。
        Class<?> dogClass = dog.getClass();
        System.out.println("Class Name: " + dogClass.getName());
        System.out.println("Superclass: " + dogClass.getSuperclass().getName());

        var dogClass2 = Dog.class;
        Class<?> dogClass3 = null;
        try {
            dogClass3 = Class.forName("basics.Dog");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(dogClass2.equals(dogClass3));


        // collection
        var list=new ArrayList<>();
        list.add("hello");
        list.add("world");
        for(var item:list){
            System.out.println(item);
        }

        HashMap<String,Number> map=new HashMap<>();
        map.put("leon",1);
        map.put("lisa",2.5);
        for(var entry:map.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

    }
}
