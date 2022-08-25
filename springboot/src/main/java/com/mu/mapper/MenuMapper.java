package com.mu.mapper;

import com.mu.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mu
 * @since 2022-08-09
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    Set<String> selectMenusByAccount(String account);

    List<Menu> selectFatherPermissionsByAccount(String account);

    List<Menu> selectSubPermissionsByFatherId(@Param("fatherId") Integer fatherId, @Param("account")String account);

}
