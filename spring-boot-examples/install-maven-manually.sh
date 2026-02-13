#!/bin/bash

# Maven 手动安装脚本（适用于macOS 15或Homebrew不可用的情况）

echo "=== Maven 手动安装脚本 ==="
echo ""

# 检查是否已安装Maven
if command -v mvn &> /dev/null; then
    echo "✅ Maven已安装"
    mvn -version
    exit 0
fi

MAVEN_VERSION="3.9.6"
INSTALL_DIR="$HOME/.local"
MAVEN_HOME="$INSTALL_DIR/apache-maven-${MAVEN_VERSION}"
FILENAME="apache-maven-${MAVEN_VERSION}-bin.tar.gz"

# 多个下载镜像（按优先级）
MIRRORS=(
    "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/${FILENAME}"
    "https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/${MAVEN_VERSION}/binaries/${FILENAME}"
    "https://mirrors.aliyun.com/apache/maven/maven-3/${MAVEN_VERSION}/binaries/${FILENAME}"
    "https://dlcdn.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/${FILENAME}"
)

echo "📦 准备安装Maven ${MAVEN_VERSION}"
echo "安装位置: $MAVEN_HOME"
echo ""

# 创建安装目录
mkdir -p "$INSTALL_DIR"
cd "$INSTALL_DIR"

# 下载Maven（尝试多个镜像）
echo "⬇️  正在下载Maven..."
DOWNLOAD_SUCCESS=false

for MAVEN_URL in "${MIRRORS[@]}"; do
    echo "尝试: $(echo $MAVEN_URL | sed 's|/binaries/.*||' | sed 's|https://||')"

    if command -v curl &> /dev/null; then
        if curl -f -L -o "$FILENAME" "$MAVEN_URL" 2>/dev/null; then
            DOWNLOAD_SUCCESS=true
            break
        fi
    elif command -v wget &> /dev/null; then
        if wget -O "$FILENAME" "$MAVEN_URL" 2>/dev/null; then
            DOWNLOAD_SUCCESS=true
            break
        fi
    fi
done

if [ "$DOWNLOAD_SUCCESS" = false ]; then
    echo "❌ 所有镜像下载失败"
    echo ""
    echo "请手动下载Maven："
    echo "1. 访问: https://maven.apache.org/download.cgi"
    echo "2. 下载 apache-maven-3.9.6-bin.tar.gz"
    echo "3. 将文件放到: $INSTALL_DIR"
    echo "4. 再次运行此脚本"
    exit 1
fi

echo "✅ 下载完成"

# 解压
echo "📦 正在解压..."
tar -xzf "apache-maven-${MAVEN_VERSION}-bin.tar.gz"
rm "apache-maven-${MAVEN_VERSION}-bin.tar.gz"

# 配置环境变量
echo ""
echo "⚙️  配置环境变量..."

# 检测shell类型
if [ "$SHELL" = "/bin/zsh" ] || [ -n "$ZSH_VERSION" ]; then
    SHELL_RC="$HOME/.zshrc"
elif [ "$SHELL" = "/bin/bash" ]; then
    SHELL_RC="$HOME/.bash_profile"
else
    SHELL_RC="$HOME/.profile"
fi

# 添加Maven到PATH
echo "" >> "$SHELL_RC"
echo "# Maven配置 (自动添加于 $(date))" >> "$SHELL_RC"
echo "export MAVEN_HOME=\"$MAVEN_HOME\"" >> "$SHELL_RC"
echo "export PATH=\"\$MAVEN_HOME/bin:\$PATH\"" >> "$SHELL_RC"

# 立即生效
export MAVEN_HOME="$MAVEN_HOME"
export PATH="$MAVEN_HOME/bin:$PATH"

# 验证安装
echo ""
if command -v mvn &> /dev/null; then
    echo "✅ Maven安装成功！"
    echo ""
    mvn -version
    echo ""
    echo "📝 配置已添加到: $SHELL_RC"
    echo ""
    echo "运行以下命令使配置立即生效："
    echo "  source $SHELL_RC"
    echo ""
    echo "或者关闭并重新打开终端"
    echo ""
    echo "然后运行Spring Boot示例："
    echo "  cd $(pwd | sed 's|/.local.*|/java-start/spring-boot-examples|')"
    echo "  ./run-app.sh quickstart"
else
    echo "❌ 安装失败"
    echo "请手动添加Maven到PATH："
    echo "export PATH=\"$MAVEN_HOME/bin:\$PATH\""
fi
