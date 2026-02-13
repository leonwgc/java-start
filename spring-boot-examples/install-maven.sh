#!/bin/bash

# Maven 快速安装脚本（macOS）

echo "=== Maven 安装脚本 ==="
echo ""

# 检查是否已安装Maven
if command -v mvn &> /dev/null; then
    echo "✅ Maven已安装"
    mvn -version
    exit 0
fi

# 检查是否安装了Homebrew
if ! command -v brew &> /dev/null; then
    echo "❌ 未检测到Homebrew"
    echo ""
    echo "Homebrew是macOS的包管理器，建议先安装它："
    echo ""
    echo "运行以下命令安装Homebrew："
    echo '/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"'
    echo ""
    echo "或者手动下载Maven："
    echo "1. 访问：https://maven.apache.org/download.cgi"
    echo "2. 下载 apache-maven-3.9.6-bin.tar.gz"
    echo "3. 解压到 /opt/maven"
    echo "4. 添加到PATH：export PATH=/opt/maven/bin:\$PATH"
    echo ""
    exit 1
fi

# 使用Homebrew安装Maven
echo "📦 使用Homebrew安装Maven..."
echo ""
brew install maven

# 验证安装
if command -v mvn &> /dev/null; then
    echo ""
    echo "✅ Maven安装成功！"
    echo ""
    mvn -version
    echo ""
    echo "现在可以运行Spring Boot示例了："
    echo "  ./run-app.sh quickstart"
else
    echo ""
    echo "❌ Maven安装失败"
    echo "请手动安装或查看错误信息"
fi
