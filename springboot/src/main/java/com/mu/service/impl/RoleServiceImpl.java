package com.mu.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mu.entity.Menu;
import com.mu.entity.Role;
import com.mu.entity.RoleMenu;
import com.mu.mapper.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mu.mapper.RoleMenuMapper;
import com.mu.service.MenuService;
import com.mu.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mu
 * @since 2022-08-08
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    //先删后增，先删除已经绑定的
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    @Transactional//同时成功或者失败的注解，假如第一步发生异常，事务回滚
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        //先删除当前角色id所有绑定关系
        roleMenuMapper.deleteByRoleId(roleId);
        //再把前段传过来的菜单id数组绑定到当前角色的id上去
        boolean flag = false;
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
            if (menu.getPid()!=null && !menuIds.contains(menu.getPid()) && !flag){//如果是二级菜单,并且不包含它的父级菜单id
                //补上父级菜单id
                flag = true;
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
