# `throws` 关键字详解

`throws` 是Java异常处理的关键字，用于**方法声明**中,表示该方法可能会抛出某种异常。

## 📌 基本概念

**作用位置**：方法签名中，方法名和参数列表之后

```java
public void methodName() throws ExceptionType {
    // 方法体
}
```

## 🔍 代码示例

```java
// validateAge方法声明
public static void validateAge(int age) throws InvalidAgeException {
    if (age < 18) {
        throw new InvalidAgeException("年龄必须大于等于18岁");
    }
}

// readFile方法声明
public static void readFile(String filename) throws Exception {
    if (filename == null || filename.isEmpty()) {
        throw new Exception("文件名不能为空");
    }
}
```

## 💡 关键要点

1. **声明异常**：告诉调用者"这个方法可能会抛出这些异常，请做好处理准备"
2. **责任转移**：把异常处理的责任转移给调用者
3. **可声明多个**：`throws Exception1, Exception2, Exception3`
4. **受检异常必须处理**：对于checked exception（如IOException），必须用throws声明或try-catch处理

## ⚖️ `throw` vs `throws`

| 关键字 | 位置 | 作用 | 数量 |
|--------|------|------|------|
| **throw** | 方法体内 | 实际抛出异常对象 | 一次抛一个 |
| **throws** | 方法声明 | 声明可能抛出的异常 | 可声明多个 |

### 对比示例

```java
// throws: 声明异常（在方法签名）
public void method() throws IOException, SQLException {
    // throw: 抛出异常（在方法体）
    throw new IOException("文件错误");
}
```

## 📝 调用带throws的方法

调用者有两个选择：

### 选择1：捕获处理

```java
try {
    validateAge(15);
} catch (InvalidAgeException e) {
    System.out.println("异常: " + e.getMessage());
}
```

### 选择2：继续声明throws

```java
public void myMethod() throws InvalidAgeException {
    validateAge(15);  // 不处理，继续向上抛
}
```

## 🎯 实际应用场景

- **文件操作**：`throws IOException`
- **数据库操作**：`throws SQLException`
- **网络操作**：`throws SocketException`
- **自定义业务异常**：`throws CustomBusinessException`

## 💡 总结

throws关键字的设计让调用者清楚地知道需要处理哪些异常情况，这是Java异常处理机制的重要组成部分。它提供了：

- ✅ 明确的异常契约
- ✅ 编译时的异常检查
- ✅ 更好的代码可读性
- ✅ 灵活的异常处理策略

在Spring框架中，throws声明被广泛使用，特别是在Service层和DAO层的方法中！
