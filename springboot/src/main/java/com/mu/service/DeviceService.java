package com.mu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mu.common.Result;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.Device;
import com.mu.entity.User;

public interface DeviceService extends IService<Device> {
    Device getDeviceInfo(DeviceDTO deviceDTO);

    void photo(DeviceDTO deviceDTO);

    Result pageAllData(Integer pageNum, Integer pageSize, String devicename,Integer start,Integer end);

    DeviceDTO owner(DeviceDTO deviceDTO);

    Device findOneByDeviceId(Integer id);

}
