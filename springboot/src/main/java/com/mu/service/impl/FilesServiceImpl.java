package com.mu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mu.common.Constants;
import com.mu.entity.Files;
import com.mu.entity.User;
import com.mu.exception.ServiceException;
import com.mu.mapper.FilesMapper;
import com.mu.mapper.UserMapper;
import com.mu.service.FilesService;
import com.mu.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {
    //
    public Files getFilesInfo(String st){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url",st);
        Files one ;
        try {
            one = getOne(queryWrapper);//从数据库查询文件信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;
    }
}
