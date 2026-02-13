# Maven 介绍

## 🔧 什么是Maven？

**Maven** 是Java项目的自动化构建工具，由Apache软件基金会开发。它解决了Java开发中的依赖管理和项目构建问题。

简单来说：**Maven = 依赖管理器 + 构建工具**

---

## 🎯 为什么需要Maven？

### 问题1：依赖管理噩梦

**没有Maven时**：
```bash
# 需要手动下载每个JAR包
下载 spring-boot-starter-web.jar
下载 spring-boot-starter-data-jpa.jar
下载 hibernate-core.jar
下载 jackson-databind.jar
... 下载几十个JAR包
```

**有Maven后**：
```xml
<!-- pom.xml中只需要写 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
Maven会自动下载这个依赖以及它所需的所有其他依赖（传递依赖）。

### 问题2：项目构建复杂

**没有Maven时**：
```bash
# 手动编译
javac -cp lib1.jar:lib2.jar:lib3.jar src/**/*.java -d target/
# 手动打包
jar cvf myapp.jar -C target/ .
# 手动复制资源文件...
```

**有Maven后**：
```bash
mvn clean package  # 一条命令完成所有
```

---

## 📦 Maven核心概念

### 1. POM（Project Object Model）

项目对象模型，配置文件 `pom.xml` 是Maven的核心。

**示例**：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>

    <!-- 项目坐标 -->
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>

    <!-- 依赖 -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>3.2.2</version>
        </dependency>
    </dependencies>
</project>
```

### 2. 依赖坐标（Coordinates）

每个依赖由三部分唯一标识：
- **groupId**：组织ID（如 `org.springframework.boot`）
- **artifactId**：项目ID（如 `spring-boot-starter-web`）
- **version**：版本号（如 `3.2.2`）

### 3. Maven仓库（Repository）

- **本地仓库**：`~/.m2/repository/` - 下载的依赖存放位置
- **中央仓库**：https://repo.maven.apache.org/maven2/ - Maven官方仓库
- **私有仓库**：公司内部仓库（可选）

### 4. 生命周期（Lifecycle）

Maven定义了标准的构建生命周期：

```
clean → validate → compile → test → package → verify → install → deploy
```

**常用命令**：
```bash
mvn clean           # 清理编译文件
mvn compile         # 编译源代码
mvn test           # 运行测试
mvn package        # 打包（JAR/WAR）
mvn install        # 安装到本地仓库
mvn deploy         # 部署到远程仓库
```

---

## 🚀 Maven在Spring Boot项目中的作用

### 1. 依赖管理

```xml
<!-- 只需声明starter依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

这一个依赖会自动引入：
- Spring MVC
- Tomcat服务器
- Jackson JSON库
- 以及其他30+个相关依赖

### 2. 运行Spring Boot应用

```bash
# Maven插件直接运行
mvn spring-boot:run

# 或指定主类
mvn spring-boot:run -Dstart-class=com.example.QuickStartApplication
```

### 3. 打包部署

```bash
# 打包成可执行JAR
mvn clean package

# 生成的文件
target/spring-boot-examples-1.0.0.jar

# 运行
java -jar target/spring-boot-examples-1.0.0.jar
```

---

## 📁 Maven标准项目结构

```
my-project/
├── pom.xml                    # Maven配置文件
├── src/
│   ├── main/
│   │   ├── java/              # Java源代码
│   │   │   └── com/example/
│   │   └── resources/         # 资源文件（配置、静态文件）
│   │       └── application.yml
│   └── test/
│       ├── java/              # 测试代码
│       └── resources/         # 测试资源
└── target/                    # 编译输出目录（自动生成）
    ├── classes/
    └── my-project-1.0.0.jar
```

---

## 🔍 实际使用示例

### 查看你的Spring Boot项目的pom.xml

```bash
cat spring-boot-examples/pom.xml
```

你会看到：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.2</version>
</parent>

<dependencies>
    <!-- Web开发 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 数据库 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- H2数据库 -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
</dependencies>
```

---

## 💡 Maven常用命令

```bash
# 清理并编译
mvn clean compile

# 运行测试
mvn test

# 打包（不运行测试）
mvn package -DskipTests

# 查看依赖树
mvn dependency:tree

# 下载所有依赖（离线准备）
mvn dependency:go-offline

# 更新依赖版本
mvn versions:display-dependency-updates

# Spring Boot运行
mvn spring-boot:run
```

---

## 🌐 Maven中央仓库

访问 https://mvnrepository.com/ 可以搜索和查找依赖。

例如搜索"spring boot web"，会找到：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.2.2</version>
</dependency>
```

---

## ⚡ Maven vs Gradle

| 特性 | Maven | Gradle |
|-----|-------|--------|
| 配置文件 | pom.xml (XML) | build.gradle (Groovy/Kotlin) |
| 学习曲线 | 简单 | 较陡 |
| 构建速度 | 较慢 | 更快 |
| 社区支持 | 非常成熟 | 快速增长 |
| Spring Boot | 默认选择 | 同样支持 |

**建议**：初学者使用Maven，熟练后可以尝试Gradle。

---

## 🎓 总结

**Maven是Java开发的必备工具**，它：

✅ **自动管理依赖** - 无需手动下载JAR包
✅ **标准化构建** - 统一的项目结构和构建流程
✅ **简化部署** - 一键打包可执行JAR
✅ **插件丰富** - Spring Boot、代码检查、文档生成等
✅ **跨平台** - Windows、macOS、Linux都能用

---

## 🚀 现在试试吧

你已经成功安装了Maven，现在可以：

```bash
cd spring-boot-examples

# 查看Maven版本
mvn -version

# 下载依赖（首次运行会下载很多依赖）
mvn dependency:resolve

# 运行Spring Boot示例
./run-app.sh quickstart
```

第一次运行会下载很多依赖，可能需要几分钟，之后就会很快了！

---

## 📚 延伸阅读

- [Maven官方文档](https://maven.apache.org/guides/index.html)
- [Spring Boot Maven插件](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/)
- [Maven中央仓库](https://mvnrepository.com/)
- [pom.xml详解](../pom.md)
