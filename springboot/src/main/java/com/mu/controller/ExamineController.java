package com.mu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.common.Result;
import com.mu.entity.BorrowRecord;
import com.mu.service.BorrowRecordService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mu
 * @since 2022-08-12
 */
@RequiresRoles(value = {"teacher","admin"},logical = Logical.OR)
@RestController
@RequestMapping("/examine")
public class ExamineController {

    @Resource
    private BorrowRecordService borrowRecordService;

    @PostMapping
    public Result save(@RequestBody BorrowRecord borrowRecord) {
        return Result.success(borrowRecordService.saveBorrowRecord(borrowRecord));
    }

    @PostMapping("/agree")
    public Result agree(@RequestBody BorrowRecord borrowRecord) {
        return Result.success(borrowRecordService.agreeBorrow(borrowRecord));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(borrowRecordService.removeById(id));
    }


    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(borrowRecordService.removeByIds(ids));
    }

    @PostMapping("/refuse")
    public Result refuseRequest(@RequestBody BorrowRecord borrowRecord){
        return Result.success(borrowRecordService.refuseRequest(borrowRecord));
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
    @RequestParam Integer pageSize,@RequestParam Integer examinePersonId) {
        //穿用户的id进来，根据身份判断，并查询出自己当前需要审核的记录
        Result result = borrowRecordService.pageAllData(pageNum, pageSize,examinePersonId);
        return result;
    }


}

