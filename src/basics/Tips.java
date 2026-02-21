package basics;

import java.util.Optional;
import java.util.Objects;

public class Tips {
    public static void main(String[] args) {
        // ## instanceof示例

        // bad
        Object obj = "Hello, World!";
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println("字符串长度: " + str.length());
        }

        // good , Java 16引入了模式匹配的instanceof，可以直接在if语句中声明变量，避免了冗余的类型转换
        if (obj instanceof String str) {
            System.out.println("字符串长度: " + str.length());
        }

        // ## switch 表达式
        int a = 6;
        String today = switch (a) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Invalid day";
        };
        System.out.println("Today is: " + today);

        // Optional示例

        // bad
        String name = null;
        if (name != null) {
            System.out.println("Name length: " + name.length());
        } else {
            System.out.println("Name is null");
        }
        // good
        String optionalName = Optional.ofNullable(name).orElse("unknown");
        System.out.println("Name length: " + optionalName.length());

        // ## Objects 工具类示例

        // 1. null 安全的相等性比较
        String str1 = null;
        String str2 = null;
        // bad - 可能抛出 NullPointerException
        // boolean isEqual = str1.equals(str2);

        // good - null 安全
        boolean isEqual = Objects.equals(str1, str2);
        System.out.println("Are equal: " + isEqual); // true

        // 2. null 检查
        String value = null;
        if (Objects.isNull(value)) {
            System.out.println("Value is null");
        }

        String value2 = "test";
        if (Objects.nonNull(value2)) {
            System.out.println("Value2 is not null: " + value2);
        }

        // 3. requireNonNull - 验证参数非 null（常用于构造函数/方法参数验证）
        try {
            String required = Objects.requireNonNull(null, "参数不能为 null");
        } catch (NullPointerException e) {
            System.out.println("捕获异常: " + e.getMessage());
        }

        // 4. null 安全的 toString
        Object obj2 = null;
        // bad
        // String s = obj2.toString(); // NullPointerException

        // good
        String s1 = Objects.toString(obj2, "默认值");
        System.out.println("ToString result: " + s1);

        // 5. hash - 生成多个值的 hashCode（常用于实现 hashCode() 方法）
        String field1 = "test";
        Integer field2 = 123;
        int hashCode = Objects.hash(field1, field2);
        System.out.println("Hash code: " + hashCode);

        // 6. 默认值处理
        String input = null;
        String result = Objects.requireNonNullElse(input, "默认值");
        System.out.println("Result: " + result);

    }
}
