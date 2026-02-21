package basics;

import java.util.List;
import java.util.stream.Collectors;

class Person {
    String name;
    int age;
    String city;

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() {
        return this.name;
    }

    public String getCity() {
        return this.city;
    }

    public static Person buildPerson(String name, int age, String city) {
        return new Person(name, age, city);
    }
}

public class Lamdas {
    public static void main(String[] args) {
        // lambda + stream API 示例
        var list = List.of(
                Person.buildPerson("Alice", 30, "Shanghai"),
                Person.buildPerson("Bob", 25, "Beijing"),
                Person.buildPerson("Charlie", 35, "Shanghai"),
                Person.buildPerson("David", 28, "Beijing"),
                Person.buildPerson("Eve", 22, "Shanghai"),
                Person.buildPerson("Frank", 40, "Beijing"));

        // 建议：
        // 如果不需要修改结果列表 → 用 .toList()（更简洁安全）
        // 如果需要修改结果列表 → 用 .collect(Collectors.toList())
        // 如果需要确保兼容性（Java 8-15）→ 用 .collect(Collectors.toList())
        var filtered = list.stream().filter((p -> p.age > 30)).map(Person::getName)
                .collect(Collectors.toList());

        for (var name : filtered) {
            System.out.println(name);
        }

        // groupingBy 示例

        // 方法引用写法
        // Person::getCity， 它是lambda 表达式的简写形式

        // 等同于 lambda 写法
        // p -> p.getCity()

        var filteredGroup = list.stream().filter((p -> p.age > 30)).collect(Collectors.groupingBy(Person::getCity));
        for (var entry : filteredGroup.entrySet()) {
            System.out.println("City: " + entry.getKey());
            for (var person : entry.getValue()) {
                System.out.println("  " + person.getName());
            }
        }

        // lambda + thread 示例
        new Thread() {
            @Override
            public void run() {
                System.out.println("Hello from a thread!");
            }
        }.start();

        new Thread(() -> System.out.println("Hello from a lambda thread!")).start();

        // Optional 示例
        var optionalPerson = list.stream().filter(p -> p.name.equals("Alice")).findFirst();
        optionalPerson.ifPresent(p -> System.out.println("Found: " + p.getName()));
    }
}
