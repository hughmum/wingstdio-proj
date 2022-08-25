package com.mu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.mu.service.BorrowRecordService;
import com.mu.entity.BorrowRecord;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mu
 * @since 2022-08-12
 */
@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Resource
    private BorrowRecordService borrowRecordService;

    @PostMapping
    public Result save(@RequestBody BorrowRecord borrowRecord) {
        return Result.success(borrowRecordService.saveBorrowRecord(borrowRecord));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(borrowRecordService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(borrowRecordService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(borrowRecordService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(borrowRecordService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize) {
        return Result.success(borrowRecordService.page(new Page<>(pageNum, pageSize)));
    }

    //查询自己的记录
    @GetMapping("/mypage")
    public Result findMyPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize,@RequestParam Integer borrowPersonId) {
        Result result = borrowRecordService.pageMyAllData(pageNum, pageSize,borrowPersonId);
        return result;
    }

    //归还设备
    @GetMapping("/ret/{id}")
    public Result returnDevice(@PathVariable Integer id) {
        return Result.success(borrowRecordService.returnDevice(id));
    }


}

