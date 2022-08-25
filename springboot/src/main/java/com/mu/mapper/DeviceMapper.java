package com.mu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.Device;
import com.mu.entity.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    IPage<DeviceDTO> pageAllData(IPage<DeviceDTO> page, @Param("deviceName") String devicename, @Param("start")Integer start,@Param("end") Integer end);
}
