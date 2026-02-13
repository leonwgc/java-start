#!/bin/bash
echo "=== 编译基础设施层示例 ==="
javac -d target/classes -encoding UTF-8 \
  src/advanced/SocketDemo.java \
  src/advanced/ConnectionPoolDemo.java \
  src/advanced/CacheDemo.java \
  src/advanced/RetryDemo.java \
  src/advanced/EventBusDemo.java

echo -e "\n=== 运行示例 ===\n"

echo "1. SocketDemo - 网络编程"
java -cp target/classes advanced.SocketDemo
echo -e "\n----------------------------------------\n"

echo "2. ConnectionPoolDemo - 数据库连接池"
java -cp target/classes advanced.ConnectionPoolDemo
echo -e "\n----------------------------------------\n"

echo "3. CacheDemo - 缓存机制"
java -cp target/classes advanced.CacheDemo
echo -e "\n----------------------------------------\n"

echo "4. RetryDemo - 重试机制"
java -cp target/classes advanced.RetryDemo
echo -e "\n----------------------------------------\n"

echo "5. EventBusDemo - 事件总线"
java -cp target/classes advanced.EventBusDemo
echo -e "\n----------------------------------------\n"

echo "✅ 所有示例运行完成！"
