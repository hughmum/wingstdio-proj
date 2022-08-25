package com.mu.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mu.entity.Validation;
import com.mu.mapper.ValidationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mu.service.ValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mu
 * @since 2022-08-22
 */
@Service
public class ValidationServiceImpl extends ServiceImpl<ValidationMapper, Validation> implements ValidationService {

    @Override
    public void saveCode(String email, String code) {
        Validation validation = new Validation();
        validation.setEmail(email);
        validation.setCode(code);

        UpdateWrapper<Validation> validationUpdateWrapper = new UpdateWrapper<>();
        validationUpdateWrapper.eq("email",email);
        remove(validationUpdateWrapper);
        save(validation);
    }
}
