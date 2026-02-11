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

echo "========================================="
echo "✅ 所有示例测试完成！"
echo ""
echo "💡 重点关注：IoCDemo 和 AopDemo"
echo "   这是理解Spring框架的核心！"
echo "========================================="
