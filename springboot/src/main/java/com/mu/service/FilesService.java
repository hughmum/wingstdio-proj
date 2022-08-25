package com.mu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mu.entity.Files;
import com.mu.entity.User;

public interface FilesService extends IService<Files> {
    public Files getFilesInfo(String st);
}
