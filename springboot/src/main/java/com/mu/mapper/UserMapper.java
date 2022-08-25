package com.mu.mapper;

import com.mu.entity.Role;
import com.mu.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mu
 * @since 2022-07-27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Set<String> getRoleByAccount(@Param("account") String account);
}
