package com.mu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mu.common.Constants;
import com.mu.common.Result;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.Device;
import com.mu.entity.Files;
import com.mu.entity.User;
import com.mu.exception.ServiceException;
import com.mu.mapper.DeviceMapper;
import com.mu.service.DeviceService;
import com.mu.service.FilesService;
import com.mu.service.IUserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {

    @Autowired
    private FilesService filesService;


    @Resource
    private DeviceMapper deviceMapper;

    @Autowired
    private IUserService userService;



    //封装一个校验的方法
    public Device getDeviceInfo(DeviceDTO deviceDTO) {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        String name = deviceDTO.getDeviceName();
        queryWrapper.eq("device_name",name);
        Device one;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            System.out.println(one);
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500, "系统错误");//如果有多条数据，返回false
        }
        return one;
    }


    //根据设备id查询出设备
    @Override
    public Device findOneByDeviceId(Integer id) {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Device one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;
    }




    //绑定图片与设备
    //先根据deviceDTO中fileurl数组中的url查询出对应的文件对象，然后将文件对象的deviceid赋值
    @Override
    public void photo(DeviceDTO deviceDTO) {
        Device device = getDeviceInfo(deviceDTO);
        List<String> list = deviceDTO.getFileurl();
        if(list!=null&&list.size()!=0){
            for(int i=0;i<list.size();i++){
                Files file = filesService.getFilesInfo(list.get(i));
                System.out.println("-----------------");
                file.setDeviceId(device.getId());
                //修改了之后还要把file更新到数据库里卖弄
                filesService.saveOrUpdate(file);
                System.out.println(file);
            }
        }
    }

    //设备与教师绑定
    @Override
    public DeviceDTO owner(DeviceDTO deviceDTO) {
        User one ;
        try {
            one = userService.getUserInfoByUserName(deviceDTO);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500, "系统错误");//如果有多条数据，返回false
        }
        deviceDTO.setOwnerId(one.getId());
        return deviceDTO;
    }

    //多表查询所有设备信息
    @Override
    public Result pageAllData(Integer pageNum, Integer pageSize, String devicename, Integer start, Integer end) {
        IPage<DeviceDTO> page = new Page<>(pageNum,pageSize);

//        QueryWrapper<De> objectQueryWrapper = new QueryWrapper<>();
//        if(!"".equals(devicename)){
//            objectQueryWrapper.like("device_name",devicename);
//        }
//        //获取当前用户信息
////        User currentUser = TokenUtils.getCurrentUser();
//
//        objectQueryWrapper.orderByDesc("id");
        Result result =  Result.success(deviceMapper.pageAllData(page,devicename,start,end));
//        return Result.success(deviceService.page(new Page<>(pageNum,pageSize),objectQueryWrapper));
        System.out.println("-------------------------");
        System.out.println(result);
        return result;
    }




}
