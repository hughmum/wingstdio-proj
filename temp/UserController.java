package com.mu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.entity.User;
import com.mu.mapper.UserMapper;
import com.mu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //新增和修改
    @PostMapping
    public boolean save(@RequestBody User user){
        //@RequestBody User user将json数据映射成user对象
        //插入返回一个数据返回更改的条数
        //新增或者更新
        return userService.saveUser(user);
    }

    //查询所有数据
    @GetMapping("/")
    public List<User> findAll(){
//        List<User> all = userMapper.findAll();
        return userService.list();
    }

    @DeleteMapping("{id}")
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return userService.removeByIds(ids);
    }

    //分页查询
    //接口路径：/user/page?pageNum=1&pageSize=10
    //映射url的值
    //limit第一个参数(pageNum-1)*pageSize
//    @GetMapping("/page")
//    public Map<String,Object> findPage(@RequestParam Integer pageNum,
//                                       @RequestParam Integer pageSize,
//                                       @RequestParam String username,
//                                       @RequestParam String email){
//        pageNum = (pageNum -1)*pageSize;
//        username = "%" +username +"%";
//        email = "%" +email +"%";
//        Integer total = userMapper.selectTotal(username,email);//查询总条数
//        List<User> data= userMapper.selectPage(pageNum,pageSize,username,email);
//        Map<String ,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }
    //分页查询mybatisplus方式
    @GetMapping("/page")
    public  IPage<User> findPage(@RequestParam Integer pageNum,
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
        objectQueryWrapper.orderByDesc("id");
        IPage<User> userPage = userService.page(page,objectQueryWrapper);
        return userPage;
    }


}
