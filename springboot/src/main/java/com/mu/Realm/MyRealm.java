package com.mu.Realm;

import com.mu.entity.Role;
import com.mu.entity.User;
import com.mu.service.IUserService;
import com.mu.service.MenuService;
import com.mu.service.impl.UserServiceImpl;
import com.mu.token.JwtToken;
import com.mu.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Autowired
    private MenuService menuService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String account = (String) principalCollection.iterator().next();
        Set<String> role = userService.getRoleByAccount(account);
        Set<String> menus = menuService.getMenusByAccount(account);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(role);
        info.setStringPermissions(menus);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) authenticationToken;
        String jwt = (String) jwtToken.getPrincipal();//得到token，要根据token获得用户名
        //解析
        Claims claims = JwtUtil.parseJWT(jwt);
        String account = claims.getId();
        User user = userService.findOneByAccount(account);
        if (user==null){
            return null;
        }
        return new SimpleAuthenticationInfo(account,user.getPassword(),getName());
    }
}
