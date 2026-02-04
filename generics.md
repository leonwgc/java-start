# Java 泛型（Generics）详解

## 📚 什么是泛型？

**泛型**是Java 5引入的特性，允许在定义类、接口和方法时使用**类型参数**（Type Parameter），实现**类型安全**和**代码复用**。

### 核心理解
- **参数化类型**：把类型作为参数传递（就像方法参数一样）
- **编译时类型检查**：在编译期发现类型错误，而不是运行时
- **类型擦除**：编译后泛型信息被移除，保证向后兼容
- **避免类型转换**：自动类型推断，无需强制转换

---

## 🎯 为什么需要泛型？

### 没有泛型的问题

```java
// 没有泛型：类型不安全
List list = new ArrayList();
list.add("Hello");
list.add(100);        // 可以添加任何类型
list.add(true);

// 取出时需要类型转换
String str = (String) list.get(1);  // ❌ 运行时崩溃！ClassCastException
```

**问题**：
1. ❌ 编译时无法发现类型错误
2. ❌ 需要手动类型转换，麻烦且易错
3. ❌ 运行时才发现错误，代价高

### 使用泛型的好处

```java
// 使用泛型：类型安全
List<String> stringList = new ArrayList<>();
stringList.add("Hello");
stringList.add(100);      // ❌ 编译错误！类型不匹配

String str = stringList.get(0);  // ✅ 不需要类型转换
```

**优点**：
1. ✅ **类型安全**：编译时检查类型错误
2. ✅ **消除类型转换**：自动推断类型
3. ✅ **代码复用**：同一代码适用多种类型
4. ✅ **更好的可读性**：明确表达意图

---

## 🔤 泛型符号约定

| 符号 | 含义 | 示例 |
|------|------|------|
| **T** | Type（类型） | `class Box<T>` |
| **E** | Element（元素） | `List<E>` |
| **K** | Key（键） | `Map<K, V>` |
| **V** | Value（值） | `Map<K, V>` |
| **N** | Number（数字） | `Box<N extends Number>` |
| **?** | 通配符（未知类型） | `List<?>` |

---

## 📦 泛型类（Generic Class）

### 定义泛型类

```java
// 泛型类：Box可以存储任何类型
class Box<T> {
    private T content;

    public Box(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }

    public void set(T content) {
        this.content = content;
    }
}
```

### 使用泛型类

```java
// 创建不同类型的Box
Box<String> stringBox = new Box<>("Hello");
Box<Integer> intBox = new Box<>(123);
Box<Person> personBox = new Box<>(new Person("张三", 25));

// 类型安全的获取
String str = stringBox.get();      // 不需要转换
Integer num = intBox.get();        // 不需要转换
Person person = personBox.get();   // 不需要转换
```

### 多类型参数

```java
// 键值对泛型类
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}

// 使用
Pair<String, Integer> pair = new Pair<>("Age", 25);
Pair<String, String> pair2 = new Pair<>("Name", "张三");
```

---

## 🔧 泛型方法（Generic Method）

### 定义泛型方法

```java
// 泛型方法语法：<T> 在返回类型之前
public static <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.print(element + " ");
    }
    System.out.println();
}
```

### 使用泛型方法

```java
// 调用时自动类型推断
printArray(new String[]{"Java", "Python", "Go"});
printArray(new Integer[]{1, 2, 3, 4, 5});
printArray(new Double[]{1.1, 2.2, 3.3});
```

### 泛型方法的类型推断

```java
// 返回第一个元素
public static <T> T getFirst(T[] array) {
    if (array == null || array.length == 0) {
        return null;
    }
    return array[0];
}

// 自动推断类型
String[] names = {"Alice", "Bob"};
String first = getFirst(names);  // 推断为 String

Integer[] numbers = {1, 2, 3};
Integer firstNum = getFirst(numbers);  // 推断为 Integer
```

---

## 🔒 泛型限定（Bounded Type）

### 上界限定（extends）

```java
// T 必须是 Comparable 的子类
public static <T extends Comparable<T>> T getMax(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// 使用
Integer max = getMax(10, 20);          // ✅ Integer实现了Comparable
String maxStr = getMax("A", "Z");      // ✅ String实现了Comparable
// Person max = getMax(p1, p2);        // ❌ 如果Person没实现Comparable会报错
```

### 多重限定

```java
// T 必须同时实现 Comparable 和 Serializable
public static <T extends Comparable<T> & Serializable> T process(T value) {
    // 可以使用两个接口的方法
    return value;
}
```

---

## 🌟 泛型通配符（Wildcard）

### 1. **无界通配符 `<?>`**
接受任何类型

```java
// 可以接受任何类型的List
public static void printList(List<?> list) {
    for (Object obj : list) {
        System.out.println(obj);
    }
}

// 使用
printList(Arrays.asList(1, 2, 3));           // List<Integer>
printList(Arrays.asList("A", "B", "C"));     // List<String>
printList(Arrays.asList(true, false));       // List<Boolean>
```

**限制**：只能读取（作为Object），不能添加

```java
List<?> list = new ArrayList<String>();
Object obj = list.get(0);    // ✅ 可以读取为Object
// list.add("Hello");        // ❌ 不能添加
```

### 2. **上界通配符 `<? extends T>`**
只接受T或T的子类

```java
// 只接受Number及其子类（Integer、Double等）
public static double sumList(List<? extends Number> list) {
    double sum = 0;
    for (Number num : list) {
        sum += num.doubleValue();
    }
    return sum;
}

// 使用
sumList(Arrays.asList(1, 2, 3));           // ✅ List<Integer>
sumList(Arrays.asList(1.1, 2.2, 3.3));     // ✅ List<Double>
// sumList(Arrays.asList("A", "B"));       // ❌ String不是Number子类
```

**特性**：
- ✅ 可以读取（作为T类型）
- ❌ 不能添加（除了null）

```java
List<? extends Number> list = new ArrayList<Integer>();
Number num = list.get(0);    // ✅ 可以读取
// list.add(10);             // ❌ 不能添加
```

### 3. **下界通配符 `<? super T>`**
只接受T或T的父类

```java
// 接受Integer及其父类（Number、Object）
public static void addIntegers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

// 使用
List<Integer> intList = new ArrayList<>();
addIntegers(intList);        // ✅ List<Integer>

List<Number> numList = new ArrayList<>();
addIntegers(numList);        // ✅ List<Number>

List<Object> objList = new ArrayList<>();
addIntegers(objList);        // ✅ List<Object>

// List<Double> doubleList = new ArrayList<>();
// addIntegers(doubleList);  // ❌ Double不是Integer的父类
```

**特性**：
- ✅ 可以添加T及其子类
- ❌ 只能读取为Object

```java
List<? super Integer> list = new ArrayList<Number>();
list.add(10);              // ✅ 可以添加Integer
list.add(20);              // ✅ 可以添加Integer
Object obj = list.get(0);  // ⚠️ 只能读取为Object
```

---

## 📝 PECS原则

**Producer Extends, Consumer Super**

### Producer（生产者）- 使用 extends
```java
// 从集合中读取数据 → 生产者 → extends
public static void processNumbers(List<? extends Number> numbers) {
    for (Number num : numbers) {          // ✅ 读取
        System.out.println(num.doubleValue());
    }
    // numbers.add(10);                   // ❌ 不能写入
}
```

### Consumer（消费者）- 使用 super
```java
// 向集合中写入数据 → 消费者 → super
public static void addNumbers(List<? super Integer> numbers) {
    numbers.add(1);                       // ✅ 写入
    numbers.add(2);
    // Integer num = numbers.get(0);     // ❌ 不能精确读取
}
```

### 记忆口诀
- **读取用 extends**（只读不写）
- **写入用 super**（只写不读）
- **既读又写不用通配符**（用具体类型）

---

## ⚠️ 类型擦除（Type Erasure）

### 什么是类型擦除？

Java泛型是**编译时特性**，编译后类型参数会被**擦除**，替换为原始类型（Raw Type）。

```java
// 编译前
List<String> list = new ArrayList<>();
list.add("Hello");

// 编译后（类型擦除）
List list = new ArrayList();
list.add("Hello");
```

### 类型擦除规则

```java
// 无限定类型参数 → Object
class Box<T> { }
// 编译后变成
class Box {
    private Object content;  // T → Object
}

// 有限定类型参数 → 第一个边界
class Box<T extends Number> { }
// 编译后变成
class Box {
    private Number content;  // T → Number
}
```

### 类型擦除的影响

**1. 不能创建泛型数组**
```java
// List<String>[] array = new List<String>[10];  // ❌ 编译错误
List<?>[] array = new List<?>[10];               // ✅ 使用通配符
```

**2. 不能使用基本类型**
```java
// List<int> list = new ArrayList<>();  // ❌ 错误
List<Integer> list = new ArrayList<>();  // ✅ 使用包装类
```

**3. 不能实例化类型参数**
```java
class Box<T> {
    // private T instance = new T();  // ❌ 错误

    // ✅ 通过反射创建
    private T instance;
    public Box(Class<T> type) {
        try {
            instance = type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

**4. 不能用于静态字段**
```java
class Box<T> {
    // private static T value;  // ❌ 错误
    private static Object value; // ✅ 只能用Object
}
```

---

## 🔥 实战示例

### 示例1：自定义ArrayList

```java
public class MyList<E> {
    private Object[] elements;
    private int size = 0;

    public MyList(int capacity) {
        elements = new Object[capacity];
    }

    public void add(E element) {
        elements[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elements[index];
    }

    public int size() {
        return size;
    }
}

// 使用
MyList<String> list = new MyList<>(10);
list.add("Java");
list.add("Python");
String lang = list.get(0);  // 不需要类型转换
```

### 示例2：泛型缓存

```java
public class Cache<K, V> {
    private Map<K, V> cache = new HashMap<>();

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.get(key);
    }

    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}

// 使用
Cache<String, User> userCache = new Cache<>();
userCache.put("user1", new User("张三"));
User user = userCache.get("user1");
```

### 示例3：泛型工具类

```java
public class ArrayUtils {
    // 交换数组中两个元素
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // 查找元素
    public static <T> int indexOf(T[] array, T target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }
}

// 使用
String[] names = {"Alice", "Bob", "Charlie"};
ArrayUtils.swap(names, 0, 2);
int index = ArrayUtils.indexOf(names, "Bob");
```

---

## 🎓 常见错误

### ❌ 错误1：试图创建泛型数组
```java
List<String>[] array = new List<String>[10];  // ❌ 编译错误
```
✅ **正确做法**：
```java
@SuppressWarnings("unchecked")
List<String>[] array = (List<String>[]) new List<?>[10];
```

### ❌ 错误2：在静态方法中使用类的类型参数
```java
class Box<T> {
    // public static void show(T value) { }  // ❌ 错误
}
```
✅ **正确做法**：静态方法使用自己的类型参数
```java
class Box<T> {
    public static <E> void show(E value) { }  // ✅ 正确
}
```

### ❌ 错误3：不理解通配符限制
```java
List<? extends Number> list = new ArrayList<Integer>();
list.add(10);  // ❌ 编译错误！不能添加
```
✅ **理解**：`extends` 只能读，不能写

---

## 💡 最佳实践

### 1. 优先使用泛型而非原始类型
```java
// ❌ 不推荐
List list = new ArrayList();

// ✅ 推荐
List<String> list = new ArrayList<>();
```

### 2. 消除未检查警告
```java
// 添加 @SuppressWarnings 注解
@SuppressWarnings("unchecked")
List<String>[] array = (List<String>[]) new List<?>[10];
```

### 3. API设计使用通配符
```java
// ✅ 更灵活
public void addAll(List<? extends E> list) { }

// ❌ 不够灵活
public void addAll(List<E> list) { }
```

### 4. 返回值不用通配符
```java
// ❌ 不推荐
public List<? extends Number> getNumbers() { }

// ✅ 推荐
public List<Number> getNumbers() { }
```

---

## 📊 泛型在集合框架中的应用

```java
// List<E>
List<String> list = new ArrayList<>();

// Set<E>
Set<Integer> set = new HashSet<>();

// Map<K, V>
Map<String, User> userMap = new HashMap<>();

// Queue<E>
Queue<Task> taskQueue = new LinkedList<>();

// Stream<T>
Stream<String> stream = list.stream();
```

---

## 💡 总结

### 核心要点
1. **类型安全**：编译时检查，避免运行时错误
2. **消除转换**：自动类型推断，无需强制转换
3. **代码复用**：一套代码适用多种类型
4. **类型擦除**：编译后移除泛型信息，保证向后兼容

### 泛型使用指南

| 场景 | 使用 | 示例 |
|------|------|------|
| 创建容器类 | 泛型类 | `class Box<T>` |
| 工具方法 | 泛型方法 | `<T> void sort(List<T>)` |
| 只读数据 | `<? extends T>` | `List<? extends Number>` |
| 只写数据 | `<? super T>` | `List<? super Integer>` |
| 任意类型 | `<?>` | `List<?>` |

### 记忆口诀
- **T for Type**（类型参数）
- **E for Element**（集合元素）
- **K/V for Key/Value**（键值对）
- **Producer Extends**（读数据用extends）
- **Consumer Super**（写数据用super）

### 泛型与Spring
- Spring框架大量使用泛型
- `BeanFactory<T>` - 泛型Bean工厂
- `@Autowired List<Service>` - 注入泛型集合
- `ResponseEntity<T>` - 泛型响应
- `Optional<T>` - 泛型容器

**泛型是Java集合框架和Spring的基础，务必掌握！** 🎯
