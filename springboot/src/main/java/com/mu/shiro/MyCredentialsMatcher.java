package com.mu.shiro;

import com.mu.entity.User;
import com.mu.service.IUserService;
import com.mu.token.JwtToken;
import com.mu.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义一个密码d验证器
 */
@Component
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    @Autowired
    private IUserService userService;


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        if(jwtToken.getPassword()==null){
            return true;
        }
        String inPassword = new String(jwtToken.getPassword());
        //获取数据库查出来的
        String account = String.valueOf(info.getPrincipals());//获取出第一个参数
        String dbPassword = (String) info.getCredentials();
        //比较验证
        User user = userService.findOneByAccount(account);
        return this.equals(inPassword,dbPassword);
    }
}
