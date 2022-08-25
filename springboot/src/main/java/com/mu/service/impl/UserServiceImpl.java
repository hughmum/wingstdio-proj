package com.mu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mu.common.Constants;
import com.mu.controller.dto.DeviceDTO;
import com.mu.controller.dto.UserDTO;
import com.mu.entity.Menu;
import com.mu.entity.User;
import com.mu.exception.ServiceException;
import com.mu.mapper.MenuMapper;
import com.mu.mapper.RoleMapper;
import com.mu.mapper.RoleMenuMapper;
import com.mu.mapper.UserMapper;
import com.mu.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mu.service.MenuService;
import com.mu.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mu
 * @since 2022-07-27
 */
@Service
public class  UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log log = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private ValidationService validationService;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one!=null){
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
//            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
//            userDTO.setToken(token);

            String role = one.getRole();//admin teacher user
            //设置当前用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
//        return list.size()!=0;
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one==null){
            //没有，就先new出来一个
            one = new User();
            //将userdto里面的东西往one里面copy
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }
    //封装一个校验的方法
    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",userDTO.getAccount());
        queryWrapper.eq("password",userDTO.getPassword());
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;
    }

    //封装一个校验的方法
    public User getUserInfoByUserName(DeviceDTO deviceDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",deviceDTO.getOwnerName());
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;
    }

    //找到对应的user并且将其老师信息查询出再放入userdto中
    @Override
    public UserDTO findOne(String account) {
        UserDTO userDTO = new UserDTO();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",account);
        User user = getOne(queryWrapper);
        if(user.getTeacherId()!=null){
            QueryWrapper<User> queryWrapperteacher = new QueryWrapper<>();
            queryWrapperteacher.eq("id",user.getTeacherId());
            User teacher  = getOne(queryWrapperteacher);
            String teacherName = teacher.getUsername();
            userDTO.setTeacherName(teacherName);
        }
        BeanUtil.copyProperties(user,userDTO,true);
        return userDTO;
    }

    @Override
    public User findOneByUserId(Integer id) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;

    }

    @Override
    public User findOneByAccount(String  account) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",account);
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;

    }

    @Override
    public Set<String> getRoleByAccount(String account) {
        Set<String> role = userMapper.getRoleByAccount(account);
        return role;
    }

    @Override
    public List<Map<String, Object>> getMenuListByAccount(String account) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Menu> menuList = menuMapper.selectFatherPermissionsByAccount(account);//获得父权限
        for(Menu menu : menuList){
            //遍历父权限，然后丢进去，然后子权限
            Map<String , Object> map = new HashMap<>();
            map.put("menu",menu);
            //获得子权限
            List<Menu> menuList1 = menuMapper.selectSubPermissionsByFatherId(menu.getId(),account);
            map.put("subMenu",menuList1);
            list.add(map);
        }
        return list;
    }

    @Override
    public void sendEmailCode(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from); //发送人
        mailMessage.setSubject("邮箱验证");
        String code = RandomUtil.randomNumbers(4);
        mailMessage.setTo(email);
        mailMessage.setText("验证码："+code+"有效期5分钟");
        javaMailSender.send(mailMessage);

        //发送成功之后把密码存到数据库
        validationService.saveCode(email,code);
    }

    @Override
    public User findOneByUserName(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",name);
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        return one;

    }

    /**
     * 获取当前角色的菜单列表
     * @param role
     * @return
     */
    private  List<Menu> getRoleMenus(String role){
        Integer roleId = roleMapper.selectByFlag(role);//先根据当前角色的身份查询出关系表中的id，然后再根据id得到这个角色的menu
        //当前角色所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        //查出系统所有菜单
        List<Menu> menus = menuService.findMenus("");
        //new一个最后筛选完成之后的lsit
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if(menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            //removeIf() 移除children里卖弄不在menuIds集合中的元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
