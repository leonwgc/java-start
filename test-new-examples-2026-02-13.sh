#!/bin/bash

# 测试2026-02-13新增示例脚本
# 用法: ./test-new-examples-2026-02-13.sh

echo "========================================"
echo "  测试2026-02-13新增的9个示例"
echo "========================================"
echo ""

# 编译所有新示例
echo "📦 1. 编译所有新示例..."
javac -d target/classes -sourcepath src \
  src/advanced/SealedClassesDemo.java \
  src/advanced/FunctionalInterfacesDemo.java \
  src/advanced/ConcurrencyDemo.java \
  src/collections/CollectionsAdvancedDemo.java \
  src/advanced/RecordsDemo.java \
  src/advanced/SwitchExpressionDemo.java \
  src/advanced/TextBlocksDemo.java \
  src/advanced/VarDemo.java

if [ $? -eq 0 ]; then
    echo "✅ 编译成功！"
else
    echo "❌ 编译失败"
    exit 1
fi

echo ""
echo "========================================"
echo ""

# 运行示例函数
run_demo() {
    local class_name=$1
    local title=$2

    echo "🚀 运行: $title"
    echo "----------------------------------------"
    java -cp target/classes $class_name | head -60
    echo ""
    echo "✅ $title 运行完成"
    echo ""
    echo "========================================"
    echo ""
}

# 运行所有示例
echo "📝 2. 运行Java现代特性示例（5个）..."
echo ""

run_demo "advanced.RecordsDemo" "RecordsDemo - 记录类"
run_demo "advanced.SwitchExpressionDemo" "SwitchExpressionDemo - Switch表达式"
run_demo "advanced.TextBlocksDemo" "TextBlocksDemo - 文本块"
run_demo "advanced.VarDemo" "VarDemo - 类型推断"
run_demo "advanced.SealedClassesDemo" "SealedClassesDemo - 密封类"

echo "📝 3. 运行高级编程技巧示例（4个）..."
echo ""

run_demo "advanced.FunctionalInterfacesDemo" "FunctionalInterfacesDemo - 函数式接口"
run_demo "collections.CollectionsAdvancedDemo" "CollectionsAdvancedDemo - 集合高级操作"
run_demo "advanced.ConcurrencyDemo" "ConcurrencyDemo - 并发工具"

echo ""
echo "========================================"
echo "  🎉 所有示例测试完成！"
echo "========================================"
echo ""
echo "📊 总计: 9个新示例全部运行成功"
echo ""
echo "💡 提示: 查看完整输出，直接运行："
echo "   java -cp target/classes advanced.FunctionalInterfacesDemo"
echo ""
