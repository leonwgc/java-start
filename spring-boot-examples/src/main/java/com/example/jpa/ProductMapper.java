package com.example.jpa;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING) // 交给Spring管理
public interface ProductMapper {
    @Mapping(source = "name", target = "name")
    ProductDto toDto(Product proudct);

    ProductDto toProjectionDto(ProductProject projection);
}

// 四、最常用自定义注解（必须背）
// 表格
// 注解 作用
// @Mapping(source = "a", target = "b") 字段名不一样时映射
// dateFormat = "yyyy-MM-dd" 日期格式化
// constant = "固定值" 赋固定值
// defaultValue = "默认值" 为空时用默认值
// ignore = true 忽略字段
// qualifiedByName 使用自定义方法转换
// expression = "java(xxx)" 直接写 Java 表达式