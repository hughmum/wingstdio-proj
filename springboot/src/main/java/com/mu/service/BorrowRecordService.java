package com.mu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.common.Result;
import com.mu.entity.BorrowRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mu
 * @since 2022-08-12
 */
public interface BorrowRecordService extends IService<BorrowRecord> {

    Boolean saveBorrowRecord(BorrowRecord borrowRecord);
    //查询所有需要自己审核的记录
    Result pageAllData(Integer pageNum, Integer pageSize,Integer examinePersonId);

    Boolean agreeBorrow(BorrowRecord borrowRecord);
    //查询所有自己借用的记录
    Result pageMyAllData(Integer pageNum, Integer pageSize, Integer borrowPersonId);
    //归还设备
    Boolean returnDevice(Integer id);

    BorrowRecord findOneByBorrowRecordId(Integer id);

    Boolean refuseRequest(BorrowRecord borrowRecord);
}
