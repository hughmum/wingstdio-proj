package com.mu.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mu.common.Result;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.Device;
import com.mu.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @PostMapping
    public Result save(@RequestBody DeviceDTO deviceDTO) {
        //将devicedto中的多个图片与本设备的id对应上
//        System.out.println("------------"+deviceDTO.getFileurl());
        System.out.println(deviceDTO);
        Device device = new Device();
        if(deviceDTO.getOwnerName()!=""){
            deviceDTO = deviceService.owner(deviceDTO);//将设备拥有者与老师绑定
        }
        //通过deviceDto查询出对应的device
        BeanUtil.copyProperties(deviceDTO,device,true);
        System.out.println(device);
        Result result = Result.success(deviceService.saveOrUpdate(device));
        deviceService.photo(deviceDTO);
        return result;
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(deviceService.removeById(id));
    }

    @GetMapping("/devicename/{devicename}")
    public Result findOne(@PathVariable String devicename) {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_name",devicename);
        return Result.success(deviceService.getOne(queryWrapper));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(deviceService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(deviceService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(deviceService.getById(id));
    }



    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String devicename,
                           @RequestParam(defaultValue = "") Integer start,
                           @RequestParam(defaultValue = "") Integer end){

//        IPage<DeviceDTO> page = new Page<>(pageNum,pageSize);
//        QueryWrapper<Device> objectQueryWrapper = new QueryWrapper<>();
//        if(!"".equals(devicename)){
//            objectQueryWrapper.like("device_name",devicename);
//        }
//        //获取当前用户信息
//        User currentUser = TokenUtils.getCurrentUser();
////        System.out.println("===>"+currentUser.getAccount());
//
//        objectQueryWrapper.orderByDesc("id");
//        return Result.success(deviceService.page(new Page<>(pageNum,pageSize),objectQueryWrapper));
        Result result = deviceService.pageAllData(pageNum,pageSize,devicename,start,end);
        return result;
    }



}
