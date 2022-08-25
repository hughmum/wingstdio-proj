package com.mu;

import cn.hutool.crypto.digest.mac.MacEngine;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mu.controller.dto.DeviceDTO;
import com.mu.entity.Device;
import com.mu.entity.Menu;
import com.mu.mapper.DeviceMapper;
import com.mu.mapper.MenuMapper;
import com.mu.service.IUserService;
import com.mu.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringbootApplicationTests {

	@Autowired
	private IUserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuMapper menuMapper;


	@Test
	void contextLoads() {
	}


	@Test
	void test(){
		System.out.println("----------------------------------");
		System.out.println(userService.getRoleByAccount("2021"));

	}
}
