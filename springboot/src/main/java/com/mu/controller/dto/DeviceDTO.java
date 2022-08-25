package com.mu.controller.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mu.entity.Files;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDTO {
    private Integer id;
    private String deviceName;
    private String price;
    private Integer ownerId;
    private String ownerName;
    private String place;
    private List<String> fileurl;
    private List<Files> filesList;
    private String state;

}
