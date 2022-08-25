package com.mu.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.BorrowRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mu
 * @since 2022-08-12
 */
@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    IPage<BorrowRecord> pageAllData(IPage<BorrowRecord> page , @Param("examinePersonId") Integer examinePersonId);

    IPage<BorrowRecord> pageMyAllData(IPage<BorrowRecord> page,  @Param("borrowPersonId") Integer borrowPersonId);
}
