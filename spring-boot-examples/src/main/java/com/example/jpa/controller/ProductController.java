package com.example.jpa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.jpa.common.ErrorCodeEnum;
import com.example.jpa.entity.Product;
import com.example.jpa.exception.BusinessException;
import com.example.jpa.service.ProductService;

@RestController
@RequestMapping("/product")
@Tag(name = "产品模块")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "根据ID查询产品", description = "先走Redis缓存→数据库")
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable @Valid @Min(400L) Long id) {
        Product p = productService.findById(id);
        if (p == null) {
            throw new BusinessException(ErrorCodeEnum.DATA_NOT_EXIST);
        }
        return p;
    }
}