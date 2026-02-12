#!/bin/bash
# 测试所有新增的学习示例

echo "========================================="
echo "测试新增的Java学习示例"
echo "========================================="
echo ""

cd src

echo "1️⃣  测试 OptionalDemo.java"
echo "-------------------------------------------"
java advanced.OptionalDemo
echo ""
echo ""

echo "2️⃣  测试 EnumDemo.java"
echo "-------------------------------------------"
java advanced.EnumDemo
echo ""
echo ""

echo "3️⃣  测试 DateTimeDemo.java"
echo "-------------------------------------------"
java advanced.DateTimeDemo
echo ""
echo ""

echo "4️⃣  测试 RegexDemo.java"
echo "-------------------------------------------"
java advanced.RegexDemo
echo ""
echo ""

echo "5️⃣  测试 PropertiesDemo.java - 配置管理"
echo "-------------------------------------------"
java advanced.PropertiesDemo
echo ""
echo ""

echo "6️⃣  测试 IoCDemo.java - IoC控制反转 ⭐核心"
echo "-------------------------------------------"
java advanced.IoCDemo
echo ""
echo ""

echo "7️⃣  测试 AopDemo.java - AOP面向切面编程 ⭐核心"
echo "-------------------------------------------"
java advanced.AopDemo
echo ""
echo ""

echo "8️⃣  测试 NioDemo.java - NIO文件操作"
echo "-------------------------------------------"
java advanced.NioDemo
echo ""
echo ""

echo "9️⃣  测试 LoggingDemo.java - SLF4J+Logback日志 ⭐重要"
echo "-------------------------------------------"
# 需要添加日志依赖到 classpath
java -cp ".:$HOME/.m2/repository/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:$HOME/.m2/repository/ch/qos/logback/logback-classic/1.4.14/logback-classic-1.4.14.jar:$HOME/.m2/repository/ch/qos/logback/logback-core/1.4.14/logback-core-1.4.14.jar" advanced.LoggingDemo
echo ""
echo ""

echo "🔟 测试 ScheduledTaskDemo.java - 定时任务"
echo "-------------------------------------------"
java advanced.ScheduledTaskDemo
echo ""
echo ""

echo "1️⃣1️⃣ 测试 SerializationDemo.java - 序列化机制"
echo "-------------------------------------------"
java -cp ".:$HOME/.m2/repository/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:$HOME/.m2/repository/ch/qos/logback/logback-classic/1.4.14/logback-classic-1.4.14.jar:$HOME/.m2/repository/ch/qos/logback/logback-core/1.4.14/logback-core-1.4.14.jar" advanced.SerializationDemo
echo ""
echo ""

echo "========================================="
echo "✅ 所有示例测试完成！"
echo ""
echo "💡 重点关注："
echo "   - IoCDemo & AopDemo：Spring框架核心！"
echo "   - LoggingDemo：Spring Boot默认日志框架"
echo "   - ScheduledTaskDemo：@Scheduled底层原理"
echo "========================================="
