package collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Java集合框架学习
 * 学习目标：
 * 1. 掌握ArrayList的使用
 * 2. 掌握HashMap的使用
 * 3. 掌握HashSet的使用
 */
public class CollectionsDemo {
    public static void main(String[] args) {
        // ArrayList 示例
        System.out.println("=== ArrayList 示例 ===");
        ArrayList<String> list = new ArrayList<>();

        // 添加元素
        list.add("Java");
        list.add("Python");
        list.add("JavaScript");
        list.add("Go");

        System.out.println("编程语言列表: " + list);
        System.out.println("列表大小: " + list.size());
        System.out.println("第一个元素: " + list.get(0));

        // 遍历ArrayList
        System.out.println("\n遍历列表:");
        for (String lang : list) {
            System.out.println("- " + lang);
        }

        // HashMap 示例
        System.out.println("\n=== HashMap 示例 ===");
        HashMap<String, Integer> scores = new HashMap<>();

        // 添加键值对
        scores.put("张三", 95);
        scores.put("李四", 87);
        scores.put("王五", 92);
        scores.put("赵六", 88);

        System.out.println("学生成绩: " + scores);
        System.out.println("张三的成绩: " + scores.get("张三"));

        // 遍历HashMap
        System.out.println("\n所有学生成绩:");
        for (String name : scores.keySet()) {
            System.out.println(name + ": " + scores.get(name) + "分");
        }

        // HashSet 示例
        System.out.println("\n=== HashSet 示例 ===");
        HashSet<String> set = new HashSet<>();

        // 添加元素（自动去重）
        set.add("苹果");
        set.add("香蕉");
        set.add("橙子");
        set.add("苹果");  // 重复元素不会被添加

        System.out.println("水果集合: " + set);
        System.out.println("集合大小: " + set.size());
        System.out.println("是否包含香蕉: " + set.contains("香蕉"));

        // 遍历HashSet
        System.out.println("\n遍历集合:");
        for (String fruit : set) {
            System.out.println("- " + fruit);
        }
    }
}
