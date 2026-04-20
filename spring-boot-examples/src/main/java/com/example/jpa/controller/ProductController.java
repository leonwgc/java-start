package com.example.jpa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.jpa.common.ErrorCodeEnum;
import com.example.jpa.entity.Product;
import com.example.jpa.exception.BusinessException;
import com.example.jpa.service.ProductService;

@RestController
@RequestMapping("/product")
@Tag(name = "产品模块")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "根据ID查询产品", description = "先走本地Caffeine缓存→Redis缓存→数据库")
    @GetMapping("/{id}")
    public Product hello(@PathVariable Long id) {
        return productService.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodeEnum.DATA_NOT_EXIST));
    }
}