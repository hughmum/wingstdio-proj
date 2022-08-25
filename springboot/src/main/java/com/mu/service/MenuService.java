package com.mu.service;

import com.mu.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mu
 * @since 2022-08-09
 */
public interface MenuService extends IService<Menu> {

    List<Menu> findMenus(String name);

    Set<String> getMenusByAccount(String account);
}
