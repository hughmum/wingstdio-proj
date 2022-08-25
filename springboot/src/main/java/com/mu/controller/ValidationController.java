package com.mu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.common.Result;
import com.mu.service.ValidationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.mu.entity.Validation;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mu
 * @since 2022-08-22
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {

    @Resource
    private ValidationService validationService;

    @PostMapping
    public Result save(@RequestBody Validation validation) {
        return Result.success(validationService.saveOrUpdate(validation));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(validationService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(validationService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(validationService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(validationService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize) {
        return Result.success(validationService.page(new Page<>(pageNum, pageSize)));
    }


}

