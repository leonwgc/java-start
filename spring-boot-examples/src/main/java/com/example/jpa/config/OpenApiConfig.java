package com.example.jpa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "JPA 项目 API 文档", version = "1.0", description = "产品接口文档"))
public class OpenApiConfig {
}