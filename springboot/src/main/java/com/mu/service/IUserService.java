package com.mu.service;

import com.mu.controller.dto.DeviceDTO;
import com.mu.controller.dto.UserDTO;
import com.mu.entity.Role;
import com.mu.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mu
 * @since 2022-07-27
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    User getUserInfoByUserName(DeviceDTO deviceDTO);
    //绑定老师信息
    UserDTO findOne(String account);

    User findOneByUserId(Integer id);

    User findOneByUserName(String name);
    //通过学工号查询用户信息
    User findOneByAccount(String  account);

    Set<String> getRoleByAccount(String account);

    List<Map<String,Object>> getMenuListByAccount(String account);

    void sendEmailCode(String email);
}
