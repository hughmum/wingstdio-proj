package com.mu.mapper;

import com.mu.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mu
 * @since 2022-08-08
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from mu.sys_role where flag= #{flag} ;")
    Integer selectByFlag(@Param("flag") String flag);
}
