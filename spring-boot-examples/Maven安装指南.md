# Maven 安装指南

## 问题说明

运行Spring Boot示例需要Maven构建工具。当前系统未检测到Maven。

## ⚠️ macOS 15 用户注意

如果你使用的是macOS 15（预发布版本），Homebrew暂不支持。请使用下面的**方案1：手动安装**。

---

## ✅ 方案1：手动安装Maven（适用于所有macOS版本）

### 一键自动安装

```bash
cd spring-boot-examples
./install-maven-manually.sh
```

这个脚本会自动：
1. 下载Maven 3.9.6
2. 解压到 `~/.local/apache-maven-3.9.6`
3. 配置环境变量
4. 验证安装

安装完成后运行：
```bash
source ~/.zshrc  # 使配置生效
mvn -version     # 验证
./run-app.sh quickstart  # 运行示例
```

### 手动安装步骤（如果脚本失败）

```bash
# 1. 下载Maven
cd ~/Downloads
curl -L -O https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz

# 2. 解压并移动
tar -xzf apache-maven-3.9.6-bin.tar.gz
mkdir -p ~/.local
mv apache-maven-3.9.6 ~/.local/

# 3. 配置环境变量（添加到 ~/.zshrc）
echo 'export MAVEN_HOME="$HOME/.local/apache-maven-3.9.6"' >> ~/.zshrc
echo 'export PATH="$MAVEN_HOME/bin:$PATH"' >> ~/.zshrc

# 4. 使配置生效
source ~/.zshrc

# 5. 验证安装
mvn -version
```

---

## ✅ 方案2：使用Homebrew安装（macOS 14及以下）

### 第1步：安装Homebrew（如果还没有）

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### 第2步：安装Maven

```bash
brew install maven
```

### 第3步：验证安装

```bash
mvn -version
```

---

## 🔧 备选方案1：手动下载Maven

### 第1步：下载Maven

访问：https://maven.apache.org/download.cgi

下载 `apache-maven-3.9.6-bin.tar.gz`

### 第2步：解压并配置

```bash
# 下载（替换为最新版本）
cd ~/Downloads
tar -xzf apache-maven-3.9.6-bin.tar.gz

# 移动到/opt
sudo mv apache-maven-3.9.6 /opt/maven

# 添加到PATH
echo 'export PATH=/opt/maven/bin:$PATH' >> ~/.zshrc
source ~/.zshrc
```

### 第3步：验证安装

```bash
mvn -version
```

---

## 💻 备选方案2：使用IDE运行（无需Maven命令行）

### VS Code

1. 打开 `spring-boot-examples` 文件夹
2. 确保安装了 "Extension Pack for Java"
3. 打开任意Application类（如 `QuickStartApplication.java`）
4. 点击 `main` 方法上方的 ▶️ 运行按钮

VS Code会自动使用内置的Maven处理依赖。

### IntelliJ IDEA

1. File → Open → 选择 `spring-boot-examples` 文件夹
2. 等待Maven依赖自动下载
3. 找到任意Application类
4. 右键 → Run 'XxxApplication.main()'

IDEA会自动处理所有Maven依赖。

---

## 🚀 快速安装脚本

我们提供了自动安装脚本：

```bash
cd spring-boot-examples
./install-maven.sh
```

这个脚本会：
1. 检测是否已安装Maven
2. 检测是否安装Homebrew
3. 使用Homebrew自动安装Maven
4. 验证安装结果

---

## ⚠️ 常见问题

### Q: Homebrew安装很慢怎么办？

A: 更换国内镜像：
```bash
export HOMEBREW_BOTTLE_DOMAIN=https://mirrors.aliyun.com/homebrew/homebrew-bottles
brew install maven
```

### Q: 我不想安装Maven，有其他方式吗？

A: 可以使用IDE（VS Code或IntelliJ IDEA）运行，它们有内置的Maven支持，不需要在命令行安装Maven。

### Q: Maven安装后还是找不到mvn命令

A: 重新加载shell配置：
```bash
source ~/.zshrc
# 或
source ~/.bash_profile
```

### Q: 为什么需要Maven？

A: Maven是Java项目的构建工具，用于：
- 管理项目依赖（Spring Boot、JPA等）
- 编译Java代码
- 打包应用程序
- 运行Spring Boot应用

---

## 📚 学习建议

### 如果你现在没有时间安装Maven

**临时方案**：先回顾前面49个Java基础示例，它们都不需要Maven：

```bash
cd /Users/leonwgc/java-start

# 运行基础示例（不需要Maven）
java -cp target/classes basics.HelloWorld
java -cp target/classes advanced.IoCDemo
java -cp target/classes advanced.AopDemo
```

### 安装Maven后

继续Spring Boot学习：
```bash
cd spring-boot-examples
./run-app.sh quickstart
./run-app.sh restful
./run-app.sh jpa
```

---

## ✅ 推荐执行步骤

**最快的方式（推荐）**：

```bash
# 1. 安装Homebrew（如果没有）
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 2. 安装Maven
brew install maven

# 3. 验证
mvn -version

# 4. 运行Spring Boot示例
cd spring-boot-examples
./run-app.sh quickstart
```

**或者使用IDE（适合不想用命令行的）**：

1. 安装VS Code或IntelliJ IDEA
2. 打开 `spring-boot-examples` 项目
3. 点击运行按钮

---

需要帮助？查看以下文档：
- [SpringBoot学习路径.md](../SpringBoot学习路径.md)
- [快速开始.md](快速开始.md)
