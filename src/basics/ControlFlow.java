package basics;

/**
 * Java控制流学习
 * 学习目标：
 * 1. 掌握if-else条件语句
 * 2. 掌握for、while循环
 * 3. 理解switch语句
 */
public class ControlFlow {
    public static void main(String[] args) {
        System.out.println("=== If-Else 条件语句 ===");
        int score = 85;

        if (score >= 90) {
            System.out.println("优秀！");
        } else if (score >= 80) {
            System.out.println("良好！");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("需要努力");
        }

        System.out.println("\n=== For 循环 ===");
        // 打印1到5
        for (int i = 1; i <= 5; i++) {
            System.out.println("数字: " + i);
        }

        System.out.println("\n=== While 循环 ===");
        int count = 1;
        while (count <= 3) {
            System.out.println("循环次数: " + count);
            count++;
        }

        System.out.println("\n=== Switch 语句 ===");
        int day = 3;
        String dayName;

        switch (day) {
            case 1:
                dayName = "星期一";
                break;
            case 2:
                dayName = "星期二";
                break;
            case 3:
                dayName = "星期三";
                break;
            case 4:
                dayName = "星期四";
                break;
            case 5:
                dayName = "星期五";
                break;
            case 6:
                dayName = "星期六";
                break;
            case 7:
                dayName = "星期日";
                break;
            default:
                dayName = "无效的日期";
        }

        System.out.println("今天是: " + dayName);

        System.out.println("\n=== 数组遍历 ===");
        String[] fruits = {"苹果", "香蕉", "橙子", "葡萄"};

        // 使用传统for循环
        for (int i = 0; i < fruits.length; i++) {
            System.out.println((i + 1) + ". " + fruits[i]);
        }

        System.out.println("\n使用增强for循环:");
        // 使用增强for循环（for-each）
        for (String fruit : fruits) {
            System.out.println("- " + fruit);
        }
    }
}
