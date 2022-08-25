package com.mu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.common.Constants;
import com.mu.common.Result;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.BorrowRecord;
import com.mu.entity.Device;
import com.mu.entity.User;
import com.mu.exception.ServiceException;
import com.mu.mapper.BorrowRecordMapper;
import com.mu.service.BorrowRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mu.service.DeviceService;
import com.mu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mu
 * @since 2022-08-12
 */
@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Resource
    private IUserService userService;

    @Resource
    private DeviceService deviceService;

    @Override
    public Boolean saveBorrowRecord(BorrowRecord borrowRecord) {

        //如果借用者是老师的话
        String borrowPerson = userService.findOneByUserId(borrowRecord.getBorrowPersonId()).getRole();
        if("teacher".equals(borrowPerson)){
            borrowRecord.setTeacherReview("agree");
            //如果老师就是拥有者，或者说，借用者就是拥有者
            if(borrowRecord.getOwnerId()==borrowRecord.getBorrowPersonId()){
                borrowRecord.setOwnerReview("agree");
            }
        }

        //根据学生id查询出他的身份，如果是学生，就先查出老师id然后绑定，如果是老师，就直接下一步
        User one  = userService.findOneByUserId(borrowRecord.getBorrowPersonId());
        if("user".equals(one.getRole())){
            borrowRecord.setTeacherId(one.getTeacherId());
        }
        //根据管理员名称绑定管理员id
        Integer id = userService.findOneByUserName("admin").getId();
        borrowRecord.setAdministratorId(id);

        //状态更改
//        Device device = deviceService.findOneByDeviceId(borrowRecord.getDeviceId());
//        device.setState("待审核");
//        deviceService.saveOrUpdate(device);

        return saveOrUpdate(borrowRecord);
    }

    @Override
    public Result pageAllData(Integer pageNum, Integer pageSize , Integer examinePersonId) {
        IPage<BorrowRecord> page = new Page<>(pageNum,pageSize);
        Result result =  Result.success(borrowRecordMapper.pageAllData(page,examinePersonId));
        return result;
    }



    @Override
    public Boolean agreeBorrow(BorrowRecord borrowRecord) {

        String borrowPerson = userService.findOneByUserId(borrowRecord.getBorrowPersonId()).getRole();
        String CurrentExaminePerson = userService.findOneByUserId(borrowRecord.getCurrentExamineId()).getRole();
        //如果是老师审核
        if("teacher".equals(CurrentExaminePerson)){
            //如果是学生借用的
            if("user".equals(borrowPerson) && borrowRecord.getTeacherReview()==null){
                borrowRecord.setTeacherReview("agree");
            }
        }
        //如果是拥有者审核
        if(borrowRecord.getCurrentExamineId()==borrowRecord.getOwnerId()){
            borrowRecord.setOwnerReview("agree");
        }
        //如果是管理员审核同意，最终通过
        else if (borrowRecord.getCurrentExamineId()==borrowRecord.getAdministratorId()){
            borrowRecord.setAdministratorReview("agree");
            borrowRecord.setFailed("已批准");
            Device device = deviceService.findOneByDeviceId(borrowRecord.getDeviceId());
            device.setState("已借出");
            deviceService.saveOrUpdate(device);
        }

        return saveOrUpdate(borrowRecord);
    }

    @Override
    public Result pageMyAllData(Integer pageNum, Integer pageSize, Integer borrowPersonId) {
        IPage<BorrowRecord> page = new Page<>(pageNum,pageSize);
        Result result =  Result.success(borrowRecordMapper.pageMyAllData(page,borrowPersonId));
        return result;
    }

    /**
     * 归还设备
     * @param id
     * @return
     */
    @Override
    public Boolean returnDevice(Integer id) {
        BorrowRecord borrowRecord = findOneByBorrowRecordId(id);
        Device device = deviceService.findOneByDeviceId(borrowRecord.getDeviceId());
        device.setState("待借用");
        deviceService.saveOrUpdate(device);
        borrowRecord.setFailed("已归还");
        saveOrUpdate(borrowRecord);
        return true;
    }

    /**
     * 拒绝借用
     * @param borrowRecord
     * @return
     */
    @Override
    public Boolean refuseRequest(BorrowRecord borrowRecord) {
        borrowRecord.setRefusalId(borrowRecord.getCurrentExamineId());
        User user = userService.findOneByUserId(borrowRecord.getRefusalId());
        borrowRecord.setRefusePersonName(user.getUsername());
        //更新状态
        borrowRecord.setFailed("未通过");

        saveOrUpdate(borrowRecord);
        return null;
    }

    /**
     * 根据id查询借用记录
     * @param id
     * @return
     */
    @Override
    public BorrowRecord findOneByBorrowRecordId(Integer id) {
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        BorrowRecord one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;
    }



}
