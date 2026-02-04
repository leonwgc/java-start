# Java Stream API 详解

## 📚 什么是Stream？

**Stream（流）** 是Java 8引入的函数式编程特性，用于对集合数据进行**声明式**处理。

### 核心理解
- **不是数据结构**：Stream不存储元素，只是对数据源的视图
- **函数式编程**：使用Lambda表达式处理数据
- **惰性求值**：中间操作不会立即执行，只有遇到终端操作才真正计算
- **一次性使用**：Stream使用后就关闭了，不能重复使用

---

## 🔄 Stream工作流程

```
数据源 → Stream创建 → 中间操作 → 终端操作 → 结果
List      .stream()    .filter()   .collect()   新List
                       .map()      .forEach()
                       .sorted()   .count()
```

### 示例对比

**传统方式（命令式）**：
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> result = new ArrayList<>();
for (Integer num : numbers) {
    if (num % 2 == 0) {         // 过滤偶数
        result.add(num * num);  // 平方
    }
}
```

**Stream方式（声明式）**：
```java
List<Integer> result = numbers.stream()
    .filter(n -> n % 2 == 0)    // 过滤偶数
    .map(n -> n * n)             // 平方
    .collect(Collectors.toList());
```

✅ **更简洁、更易读、更易维护**

---

## 🎯 Stream操作分类

### 1️⃣ **中间操作（Intermediate Operations）**
返回新Stream，**惰性执行**

| 操作 | 说明 | 示例 |
|------|------|------|
| **filter()** | 过滤 | `.filter(n -> n > 5)` 保留>5的元素 |
| **map()** | 转换/映射 | `.map(s -> s.length())` 字符串→长度 |
| **flatMap()** | 扁平化映射 | 将多个Stream合并 |
| **sorted()** | 排序 | `.sorted()` 或 `.sorted(Comparator)` |
| **distinct()** | 去重 | 去除重复元素 |
| **limit()** | 限制数量 | `.limit(5)` 只取前5个 |
| **skip()** | 跳过元素 | `.skip(3)` 跳过前3个 |
| **peek()** | 调试查看 | `.peek(System.out::println)` |

### 2️⃣ **终端操作（Terminal Operations）**
触发实际计算，**返回结果或副作用**

| 操作 | 说明 | 返回类型 |
|------|------|----------|
| **collect()** | 收集到集合 | List/Set/Map |
| **forEach()** | 遍历 | void |
| **count()** | 计数 | long |
| **reduce()** | 归约（累积） | Optional<T> |
| **max()** | 最大值 | Optional<T> |
| **min()** | 最小值 | Optional<T> |
| **anyMatch()** | 任意匹配 | boolean |
| **allMatch()** | 全部匹配 | boolean |
| **noneMatch()** | 全不匹配 | boolean |
| **findFirst()** | 第一个元素 | Optional<T> |
| **findAny()** | 任意元素 | Optional<T> |

---

## 💡 常用操作详解

### **filter() - 过滤**
```java
// 找出所有偶数
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
```

### **map() - 转换**
```java
// 字符串转大写
List<String> upper = words.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// 提取对象属性
List<String> names = students.stream()
    .map(Student::getName)
    .collect(Collectors.toList());
```

### **sorted() - 排序**
```java
// 自然排序
numbers.stream().sorted()

// 倒序
numbers.stream().sorted(Comparator.reverseOrder())

// 自定义排序（按成绩）
students.stream().sorted(Comparator.comparing(Student::getScore))
```

### **reduce() - 归约**
```java
// 求和
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);
// 或简写为
int sum = numbers.stream()
    .reduce(0, Integer::sum);

// 求最大值
Optional<Integer> max = numbers.stream()
    .reduce(Integer::max);
```

### **collect() - 收集**
```java
// 收集到List
.collect(Collectors.toList())

// 收集到Set
.collect(Collectors.toSet())

// 连接字符串
.collect(Collectors.joining(", "))

// 分组
Map<String, List<Student>> byGender = students.stream()
    .collect(Collectors.groupingBy(Student::getGender));

// 分区（true/false）
Map<Boolean, List<Integer>> partition = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n > 5));
```

---

## 🔥 实战示例

### 示例1：学生成绩分析
```java
List<Student> students = Arrays.asList(
    new Student("张三", 85, "男"),
    new Student("李四", 92, "女"),
    new Student("王五", 78, "男")
);

// 找出最高分学生
students.stream()
    .max(Comparator.comparing(Student::getScore))
    .ifPresent(s -> System.out.println(s.getName()));

// 计算平均分
double avg = students.stream()
    .mapToInt(Student::getScore)
    .average()
    .orElse(0.0);

// 按性别分组
Map<String, List<Student>> byGender = students.stream()
    .collect(Collectors.groupingBy(Student::getGender));

// 80分以上的学生名单
List<String> excellent = students.stream()
    .filter(s -> s.getScore() >= 80)
    .map(Student::getName)
    .collect(Collectors.toList());
```

### 示例2：链式操作
```java
// 找出偶数 → 平方 → 排序 → 转List
List<Integer> result = numbers.stream()
    .filter(n -> n % 2 == 0)  // 过滤偶数
    .map(n -> n * n)           // 平方
    .sorted()                  // 排序
    .collect(Collectors.toList());
```

### 示例3：字符串处理
```java
List<String> words = Arrays.asList("apple", "banana", "cherry", "date");

// 长度>5的单词，转大写，排序
List<String> result = words.stream()
    .filter(w -> w.length() > 5)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());

// 连接成一个字符串
String joined = words.stream()
    .collect(Collectors.joining(", "));
// 结果: "apple, banana, cherry, date"
```

---

## ⚠️ 重要概念

### 1. **惰性求值（Lazy Evaluation）**
```java
Stream<Integer> stream = numbers.stream()
    .filter(n -> {
        System.out.println("过滤: " + n);
        return n > 5;
    });  // 这里不会打印任何东西！

// 只有调用终端操作时才执行
stream.collect(Collectors.toList());  // 现在开始打印
```

**原理**：
- 中间操作只是定义了"要做什么"，并不执行
- 终端操作触发时，才开始从数据源读取并处理
- 这样可以优化性能，避免不必要的计算

### 2. **Stream不能重复使用**
```java
Stream<Integer> stream = numbers.stream();
stream.forEach(System.out::println);  // ✅ 正确
stream.forEach(System.out::println);  // ❌ 错误！已关闭

// 报错信息: java.lang.IllegalStateException: stream has already been operated upon or closed
```

**解决方案**：需要重新创建Stream
```java
numbers.stream().forEach(...);  // 第一次使用
numbers.stream().forEach(...);  // 第二次使用，重新创建
```

### 3. **Optional处理空值**
```java
// max/min/findFirst等返回Optional
Optional<Integer> max = numbers.stream().max(Integer::compareTo);

// 安全使用方式1：ifPresent
max.ifPresent(m -> System.out.println("最大值: " + m));

// 安全使用方式2：orElse提供默认值
int value = max.orElse(0);

// 安全使用方式3：orElseGet（懒加载）
int value = max.orElseGet(() -> calculateDefault());

// 安全使用方式4：orElseThrow
int value = max.orElseThrow(() -> new RuntimeException("没有最大值"));
```

---

## 🎓 学习建议

### 记忆技巧
1. **中间操作** = 返回Stream，可以继续链式调用
   - filter、map、sorted、distinct、limit、skip

2. **终端操作** = 返回结果，Stream结束
   - collect、forEach、count、reduce、max/min

3. **记口诀**：
   - "过滤映射排序去重" → filter、map、sorted、distinct
   - "收集遍历计数归约" → collect、forEach、count、reduce

### 实践练习

**练习1：找出长度>5的单词，转大写，排序**
```java
List<String> result = words.stream()
    .filter(w -> w.length() > 5)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

**练习2：统计及格人数**
```java
long count = students.stream()
    .filter(s -> s.getScore() >= 60)
    .count();
```

**练习3：获取前3名学生姓名**
```java
List<String> top3 = students.stream()
    .sorted(Comparator.comparing(Student::getScore).reversed())
    .limit(3)
    .map(Student::getName)
    .collect(Collectors.toList());
```

**练习4：计算总价**
```java
double total = orders.stream()
    .mapToDouble(Order::getPrice)
    .sum();
```

---

## 📊 Stream vs 传统循环

| 特性 | 传统循环 | Stream |
|------|----------|--------|
| 代码量 | 多 | 少 |
| 可读性 | 命令式（怎么做） | 声明式（做什么） |
| 并行化 | 需要手动 | `.parallelStream()` 自动 |
| 性能 | 小数据集更快 | 大数据集更优 |
| 调试 | 容易 | 需要技巧 |
| 灵活性 | 高（可以break） | 受限 |

### 何时使用Stream？
✅ **适合使用Stream**：
- 数据转换/过滤/聚合
- 链式操作多个步骤
- 需要并行处理大数据集
- 追求代码简洁性

❌ **不适合使用Stream**：
- 需要提前退出循环（break）
- 需要访问循环索引
- 简单的单次遍历
- 性能要求极高的小数据集

---

## 🚀 进阶：并行流

### 使用并行流
```java
// 串行流
list.stream().filter(...).map(...)

// 并行流（多线程）
list.parallelStream().filter(...).map(...)
```

### 并行流示例
```java
// 计算1到1000000的平方和（并行）
long sum = LongStream.rangeClosed(1, 1000000)
    .parallel()
    .map(n -> n * n)
    .sum();
```

### ⚠️ 并行流注意事项
1. **线程安全**：确保操作无副作用
2. **数据量**：数据量小时串行更快
3. **计算密集**：只有CPU密集型操作才适合并行
4. **顺序**：并行流不保证处理顺序

**经验法则**：
- 数据量 < 1000：使用串行流
- 数据量 > 10000：考虑并行流
- 操作耗时长：并行流收益大

---

## 🔍 调试技巧

### 使用peek()调试
```java
List<Integer> result = numbers.stream()
    .peek(n -> System.out.println("原始值: " + n))
    .filter(n -> n % 2 == 0)
    .peek(n -> System.out.println("过滤后: " + n))
    .map(n -> n * n)
    .peek(n -> System.out.println("平方后: " + n))
    .collect(Collectors.toList());
```

### 分步验证
```java
// 不要一次写完整链式调用，而是分步验证
List<Integer> filtered = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
System.out.println("过滤后: " + filtered);

List<Integer> mapped = filtered.stream()
    .map(n -> n * n)
    .collect(Collectors.toList());
System.out.println("映射后: " + mapped);
```

---

## 💡 高级技巧

### flatMap扁平化
```java
// 将嵌套List展平
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5, 6)
);

List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
// 结果: [1, 2, 3, 4, 5, 6]
```

### 自定义Collector
```java
// 统计信息
IntSummaryStatistics stats = numbers.stream()
    .mapToInt(Integer::intValue)
    .summaryStatistics();

System.out.println("总数: " + stats.getCount());
System.out.println("总和: " + stats.getSum());
System.out.println("平均: " + stats.getAverage());
System.out.println("最小: " + stats.getMin());
System.out.println("最大: " + stats.getMax());
```

### 多级分组
```java
// 按年级分组，再按性别分组
Map<Integer, Map<String, List<Student>>> grouped = students.stream()
    .collect(Collectors.groupingBy(
        Student::getGrade,
        Collectors.groupingBy(Student::getGender)
    ));
```

---

## 📝 常见错误

### ❌ 错误1：在Lambda中修改外部变量
```java
int sum = 0;  // 外部变量
numbers.stream().forEach(n -> sum += n);  // ❌ 编译错误！
```

✅ **正确做法**：使用reduce
```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

### ❌ 错误2：重复使用Stream
```java
Stream<Integer> stream = numbers.stream();
long count = stream.count();
long sum = stream.sum();  // ❌ 错误！Stream已关闭
```

✅ **正确做法**：重新创建或使用IntStream
```java
IntSummaryStatistics stats = numbers.stream()
    .mapToInt(Integer::intValue)
    .summaryStatistics();
long count = stats.getCount();
long sum = stats.getSum();
```

### ❌ 错误3：忘记终端操作
```java
numbers.stream()
    .filter(n -> n > 5)
    .map(n -> n * n);  // ❌ 没有执行任何操作！
```

✅ **正确做法**：添加终端操作
```java
numbers.stream()
    .filter(n -> n > 5)
    .map(n -> n * n)
    .collect(Collectors.toList());  // ✅ 触发执行
```

---

## 💡 总结

### Stream核心价值
✅ **简洁** - 用声明式代码替代循环
✅ **易读** - 更贴近业务逻辑
✅ **组合** - 链式调用灵活组合操作
✅ **并行** - 轻松支持多线程处理

### 关键要点
1. **中间操作**返回Stream，**终端操作**返回结果
2. Stream是**惰性求值**，只在终端操作时执行
3. Stream**一次性使用**，不能重复
4. 使用**Optional**安全处理可能为空的结果
5. 合理选择**串行/并行**流

### 最佳实践
1. 数据转换/过滤用Stream，简单遍历用for-each
2. 避免在Lambda中修改外部变量
3. 合理使用Optional处理空值
4. 大数据集考虑并行流
5. 使用peek()进行调试

### 学习路径
1. ✅ 掌握基本操作：filter、map、collect
2. ✅ 理解中间操作 vs 终端操作
3. ✅ 学会使用Comparator排序
4. ✅ 掌握Collectors收集器
5. ⏭️ 进阶：flatMap、reduce、并行流

**Stream是Spring框架中处理集合的常用方式，务必熟练掌握！** 🎯
