package com.mu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_device")
public class Device {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String deviceName;
    private String price;
    private Integer ownerId;
    private String place;
    private String state;
}
