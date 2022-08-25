package com.mu.controller.dto;


import cn.hutool.crypto.digest.mac.MacEngine;
import com.mu.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接收前端登录请求的参数
 */
@Data
public class UserDTO {
    private Integer id;
    private String account;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String avatarUrl;
    private String address;
    private String token;
    private String teacherName;
    private String role;
    private List<Menu> menus;
}
