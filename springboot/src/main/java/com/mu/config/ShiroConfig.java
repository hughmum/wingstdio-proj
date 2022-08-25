package com.mu.config;

import com.mu.Realm.MyRealm;
import com.mu.shiro.JwtFilter;
import com.mu.shiro.MyCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Autowired
    private MyRealm myRealm;
    @Autowired
    private MyCredentialsMatcher myCredentialsMatcher;



    //注入一个安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        myRealm.setCredentialsMatcher(myCredentialsMatcher);
        securityManager.setRealm(myRealm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactorBean(DefaultWebSecurityManager manager,JwtFilter jwtFilter){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(manager);//添加一个安全管理器
        //添加过滤
        Map<String, Filter> map = new HashMap<>();
        map.put("jwt",jwtFilter);
        filterFactoryBean.setFilters(map);
        Map<String,String> filterMap = new LinkedHashMap<>();
        //让swapper不被拦截
        filterMap.put("/swagger-ui.html**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-resources/**", "anon");

        filterMap.put("/**","jwt");//让所有的请求都通过jwt过滤，链式hashpmap，从上往下执行
        filterFactoryBean.setLoginUrl("/login");//自定义的登录界面
        filterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return filterFactoryBean;
    }

    @Bean
    public JwtFilter getJwtFilter(){
        return new JwtFilter();
    }

    ///开启注释
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
}
