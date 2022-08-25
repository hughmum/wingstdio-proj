package com.mu.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.common.Constants;
import com.mu.common.Result;
import com.mu.config.AuthAccess;
import com.mu.controller.dto.EmailCodeDTO;
import com.mu.controller.dto.UserDTO;
import com.mu.entity.User;
import com.mu.entity.Validation;
import com.mu.exception.ServiceException;
import com.mu.service.MenuService;
import com.mu.service.ValidationService;
import com.mu.token.JwtToken;
import com.mu.utils.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mu.service.IUserService;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mu
 * @since 2022-07-27
 */

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {



    @Resource
    private IUserService userService;


    @Resource
    private ValidationService validationService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
//        String account = userDTO.getAccount();
//        String password= userDTO.getPassword();
//        if(StrUtil.isBlank(account)||StrUtil.isBlank(password)){
//            return Result.error(Constants.CODE_400,"参数错误");
//        }
//        UserDTO dto = userService.login(userDTO);
//        return Result.success(dto);
        if(userDTO.getPassword() == null || userDTO.getAccount() == null){
            return Result.error(Constants.CODE_500,"用户名或密码错误");
        }
        Subject subject = SecurityUtils.getSubject();
        String jwt = JwtUtil.createJWT(userDTO.getAccount(),"back","user",1000*60*30);
        JwtToken jwtToken = new JwtToken(jwt,userDTO.getPassword());
        try{
            subject.login(jwtToken);
        }catch (UnknownAccountException e){
            return  Result.error(Constants.CODE_401,"账号不存在");
        }catch (IncorrectCredentialsException e){
            return  Result.error(Constants.CODE_401,"密码错误");
        }
        User backUser = userService.findOneByAccount(userDTO.getAccount());
        backUser.setPassword(null);
        Map<String,Object> map = new HashMap<>();
        map.put("user",backUser);
        map.put("token",jwt);
        return new Result(Constants.CODE_200,"登录成功",map);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String account = userDTO.getAccount();
        String password= userDTO.getPassword();
        if(StrUtil.isBlank(account)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }

        return Result.success(userService.register(userDTO));
    }


    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    @RequiresRoles(value = {"teacher","admin"},logical = Logical.OR)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @GetMapping("/account/{account}")
    public Result findOne(@PathVariable String account) {
        UserDTO userDTO = userService.findOne(account);
        return Result.success(userDTO);
    }



    @RequiresRoles(value = {"teacher","admin"},logical = Logical.OR)
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(userService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    @AuthAccess
    @GetMapping("/email/{email}")
    public Result sendEmailCode(@PathVariable String email) {
        if(StrUtil.isBlank(email)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }
        userService.sendEmailCode(email);
        return Result.success();
    }

    @AuthAccess
    @PutMapping("/reset")
    public Result reset(@RequestBody EmailCodeDTO emailCodeDTO) {
        if(StrUtil.isBlank(emailCodeDTO.getEmail())){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }

        //查询与数据库中的验证码是否一样
        QueryWrapper<Validation> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("email",emailCodeDTO.getEmail());
        queryWrapper1.eq("code",emailCodeDTO.getCode());
        Validation validation = validationService.getOne(queryWrapper1);
        if(validation == null){
            throw new ServiceException("-1","验证码错误");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",emailCodeDTO.getEmail());
        User one = userService.getOne(queryWrapper);
        String code = RandomUtil.randomNumbers(4);
        one.setPassword(code);
        userService.updateById(one);
        return Result.success(code);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }


    @RequiresRoles(value = {"teacher","admin"},logical = Logical.OR)
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email){

        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            objectQueryWrapper.like("username",username);
        }
        if(!"".equals(email)){
            objectQueryWrapper.like("email",email);
        }
        //获取当前用户信息
//        User currentUser = TokenUtils.getCurrentUser();
//        System.out.println("===>"+currentUser.getAccount());

        objectQueryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum,pageSize),objectQueryWrapper));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        //从数据库查询所有数据
        List<User> list = userService.list();
        //通过工具类创建writer写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+"/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname","身份" );
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
//        writer.addHeaderAlias("avatarUrl", "头像");

        //一次性写出list内的对象到excel使用默认样式
        writer.write(list,true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        //获取输出流
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    /**
     * 导入
     *
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setAccount(row.get(2).toString());
//            user.setEmail(row.get(3).toString());
//            user.setPhone(row.get(4).toString());
//            user.setAddress(row.get(5).toString());
//            user.setAvatarUrl(row.get(6).toString());
//            users.add(user);
//        }

        userService.saveBatch(list);
        return Result.success(true);
    }


    //班级管理
    //分页查询
    @GetMapping("/class/page")
    public Result classfindPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email){

        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        if(!"".equals(username)){
            objectQueryWrapper.like("username",username);
        }
        if(!"".equals(email)){
            objectQueryWrapper.like("email",email);
        }
        //获取当前用户信息
//        User currentUser = TokenUtils.getCurrentUser();
//        Integer id = currentUser.getId();
        Integer id = 1;

        objectQueryWrapper.eq("teacher_id",id);

        objectQueryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum,pageSize),objectQueryWrapper));
    }

    //测试shiro
    @RequiresPermissions("user:dfsd")
    @GetMapping("/test")
    public String test1() {
        return "你好";
    }

    @RequestMapping(value = "/test2",method = RequestMethod.GET)
    public String test2() {
        return "wo好";
    }

    @GetMapping("/getMenuList")
    public String getMenuList(String account){
        if(account == null){
            return JSON.toJSONString(Result.error(Constants.CODE_401,"未登录"));
        }
        List<Map<String, Object>> list = userService.getMenuListByAccount(account);
        return JSON.toJSONString(Result.success(list));
    }

}

