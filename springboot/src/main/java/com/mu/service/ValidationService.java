package com.mu.service;

import cn.hutool.core.date.DateTime;
import com.mu.entity.Validation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mu
 * @since 2022-08-22
 */
public interface ValidationService extends IService<Validation> {

    void saveCode(String email, String code);
}
