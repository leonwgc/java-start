#!/bin/bash

# Spring Boot 示例快速启动脚本

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven未安装"
    echo ""
    echo "请选择以下方式之一安装Maven："
    echo ""
    echo "方式1：使用Homebrew安装（推荐）"
    echo "  brew install maven"
    echo ""
    echo "方式2：手动下载安装"
    echo "  1. 访问：https://maven.apache.org/download.cgi"
    echo "  2. 下载并解压到 /opt/maven"
    echo "  3. 添加到PATH："
    echo "     export PATH=/opt/maven/bin:\$PATH"
    echo ""
    exit 1
fi

APP_NAME=$1

if [ -z "$APP_NAME" ]; then
    echo "❌ 请指定要运行的示例"
    echo ""
    echo "用法: ./run-app.sh [示例名称]"
    echo ""
    echo "📚 可用示例："
    echo ""
    echo "基础示例："
    echo "  quickstart  - Spring Boot 快速入门"
    echo "  restful     - RESTful API 实战"
    echo "  jpa         - Spring Data JPA 实战"
    echo ""
    echo "进阶示例："
    echo "  validation  - 表单验证和数据校验"
    echo "  exception   - 异常处理和统一响应"
    echo "  config      - 配置属性管理"
    echo "  interceptor - 拦截器和过滤器"
    echo "  fileupload  - 文件上传下载"
    echo ""
    echo "示例: ./run-app.sh quickstart"
    exit 1
fi

case $APP_NAME in
    quickstart)
        CLASS_NAME="com.example.quickstart.QuickStartApplication"
        DESCRIPTION="Spring Boot 快速入门"
        ;;
    restful)
        CLASS_NAME="com.example.restful.RestfulApplication"
        DESCRIPTION="RESTful API 实战"
        ;;
    jpa)
        CLASS_NAME="com.example.jpa.JpaApplication"
        DESCRIPTION="Spring Data JPA 实战"
        ;;
    validation)
        CLASS_NAME="com.example.validation.ValidationApplication"
        DESCRIPTION="表单验证和数据校验"
        ;;
    exception)
        CLASS_NAME="com.example.exception.ExceptionHandlingApplication"
        DESCRIPTION="异常处理和统一响应"
        ;;
    config)
        CLASS_NAME="com.example.config.ConfigPropertiesApplication"
        DESCRIPTION="配置属性管理"
        ;;
    interceptor)
        CLASS_NAME="com.example.interceptor.InterceptorApplication"
        DESCRIPTION="拦截器和过滤器"
        ;;
    fileupload)
        CLASS_NAME="com.example.fileupload.FileUploadApplication"
        DESCRIPTION="文件上传下载"
        ;;
    *)
        echo "❌ 未知的示例: $APP_NAME"
        echo ""
        echo "📚 可用示例："
        echo ""
        echo "基础示例："
        echo "  quickstart  - Spring Boot 快速入门"
        echo "  restful     - RESTful API 实战"
        echo "  jpa         - Spring Data JPA 实战"
        echo ""
        echo "进阶示例："
        echo "  validation  - 表单验证和数据校验"
        echo "  exception   - 异常处理和统一响应"
        echo "  config      - 配置属性管理"
        echo "  interceptor - 拦截器和过滤器"
        echo "  fileupload  - 文件上传下载"
        exit 1
        ;;
esac

echo "🚀 正在启动: $DESCRIPTION"
echo "📦 主类: $CLASS_NAME"
echo ""

mvn spring-boot:run -Dstart-class=$CLASS_NAME
