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

echo "========================================="
echo "✅ 所有示例测试完成！"
echo "========================================="
