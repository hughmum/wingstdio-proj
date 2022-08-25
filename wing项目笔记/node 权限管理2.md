

在role表中唯一标识是id，但是不能直观表示出含义，所以加一个唯一标识key

枚举类型

```java
public enum RoleEnum {
    admin,teacher,user;
}
```

## 动态菜单

#### 后端

登录时，获得当前用户的菜单列表

usercontroller

```java
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String account = userDTO.getAccount();
        String password= userDTO.getPassword();
        if(StrUtil.isBlank(account)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

```

userservice

```java

    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one!=null){
            BeanUtil.copyProperties(one,userDTO,true);
            //设置token
            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
            userDTO.setToken(token);

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


```

#### 前端

Login.vue中

```vue
  methods:{
    login(){
      this.request.post("/user/login",this.user).then(res=>{
        if(res.code==='200'){
          localStorage.setItem("user",JSON.stringify(res.data))
          localStorage.setItem("menus",JSON.stringify(res.data.menus))
          this.$router.push("/")//跳转到主页面
          this.$message.success("登录成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }
}
```

Aside.vue中动态渲染菜单

```vue
<template>
  <el-menu :default-openeds="opens" style="height: 100% "
           background-color="rgb(48,65,86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px">
      <b style="color: white" v-show="logoTestShow">设备管理系统</b>
    </div>
    <div v-for="item in menus" :key="item.id+''">
        <div v-if="item.path">
          <el-menu-item :index="item.path">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{item.name}}</span>
            </template>
          </el-menu-item>
        </div>
        <div v-else>
          <el-submenu :index="item.id+''">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title">{{item.name}}</span>
            </template>
            <div  v-for="subItem in item.children" :key="subItem.id">
              <el-menu-item :index="subItem.path">
                <i :class="subItem.icon"></i>
                <span slot="title">{{subItem.name}}</span>
              </el-menu-item>
            </div>
          </el-submenu>
        </div>
    </div>

  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTestShow: Boolean,
  },
  data() {
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")):[],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id +''):[],//默认全部展开
    }
  }
}
</script>

<style scoped>

</style>
```

## 动态路由

login vue中

```js
 login(){
      this.request.post("/user/login",this.user).then(res=>{
        if(res.code==='200'){
          localStorage.setItem("user",JSON.stringify(res.data))
          localStorage.setItem("menus",JSON.stringify(res.data.menus))
          //动态设置当前用户的路由
          setRoutes()
          this.$router.push("/")//跳转到主页面
          this.$message.success("登录成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    }
```

router indexjs中

```js
import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [

  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '*',
    name: '404',
    component: () => import('../views/404.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//刷新页面会导致路由重置
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if(storeMenus){
    const manageRoute = {path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children:[]}
    const menus = JSON.parse(storeMenus)
    menus.forEach(item=>{
      if(item.path){
        let itemMenu = {path: item.path.replace("/",""), name: item.name, component: () => import('../views/'+item.pagePath+'.vue')}
        manageRoute.children.push(itemMenu)
      }
      else if(item.children.length){
        item.children.forEach(item=>{
          if(item.path){
            let itemMenu = {path: item.path.replace("/",""), name: item.name, component: () => import('../views/'+item.pagePath+'.vue')}
            manageRoute.children.push(itemMenu)
          }
        })
      }
    })
    //获取当前路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v=>v.name)
    if(!currentRouteNames.includes('Manage')){
      router.addRoute(manageRoute)
    }
  }
}

 setRoutes()

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  next()  // 放行路由
})

export default router

```



## 404Bug

```js
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  }
```

路由守卫

```js
// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  //未找到路由的情况
  if(!to.matched.length){
    //拿到当前用户的menus
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus) {
      next("/404") // 放行路由
    }else{
      //跳回登录
      next("/login")
    }
  }
  //其他情况方形
  next()

})
```

## 把固定的路由写上

比如个人信息

```js
    const manageRoute = {path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children:[
        { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
      ]}
```

## 点击分配菜单没反应

方式一，先显示菜单

```js
   selectMenu(role) {

      this.roleId = role.id
      this.roleFlag = role.flag
      //请求菜单数据
      this.request.get("/menu").then(res=>{
        console.log(res)
        this.menuData=res.data
        //把后台返回的菜单数据处理成id数组
        this.expends = this.menuData.map(v => v.id);
      })
      //请求已选中的数据
      this.request.get("/role/roleMenu/"+this.roleId).then(res=>{
        this.menuDialogVis = true;

        this.checks = res.data;
        //后端查询出所有菜单的id，然后跟我们本角色选中的id所比较
        this.request.get("/menu/ids").then(r =>{
          const ids  = r.data
          ids.forEach(id =>{
            if(!this.checks.includes(id)){
              //可能会报错
              this.$refs.tree.setChecked(id,false)//当当前的菜单项不是选中的就设为false
            }
          })
        })
      })
    },
  }
```

方式二 处理未来元素的渲染问题

```js
       this.request.get("/menu/ids").then(r =>{
          const ids  = r.data
          ids.forEach(id =>{
            if(!this.checks.includes(id)){
              //可能会报错
              this.$nextTick(()=>{
                this.$refs.tree.setChecked(id,false)//当当前的菜单项不是选中的就设为false
              })
            }
          })
          this.menuDialogVis = true;
        })
```

## 解决分配菜单，退出重新登陆后，点击新路由出现404情况

退出时重置路由

store indexjs中

```js
        logout() {
            //清空缓存
            localStorage.removeItem("user")
            localStorage.removeItem("menus")
            router.push("/login")
            //重置路由
            resetRouter()
        }
```

router中

新建一个重置方法

```js
//重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}
```

## 整合shiro

导入shiro和jwt依赖

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
    <version>1.4.1</version>
</dependency>	

```





[(192条消息) Shiro权限控制+整合shiro_Armymans的博客-CSDN博客_shiro权限控制](https://blog.csdn.net/qq_43652509/article/details/88074832?ops_request_misc=%7B%22request%5Fid%22%3A%22166074989316782414979899%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=166074989316782414979899&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~baidu_landing_v2~default-1-88074832-null-null.142^v41^pc_search_integral,185^v2^control&utm_term=shiro整合&spm=1018.2226.3001.4187)

[选课功能（二）_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1xU4y1G7GR?p=20&vd_source=49d04a66fc1c216a8dc4017809d8abd5)

第一步：

shiroConfig

```java
@Configuration
public class ShiroConfig {

    //注入一个安全管理器
    public DefaultWebSecurityManager getDefaultWebSecurityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm();
    }

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactorBean(){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager();//添加一个安全管理器
    }
}

```

第二步

Realm

```java
@Component
public class MyRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}

```

测试

第三步

验证token，自定义token

```java
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //先使用系统自带的token
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        User user = userService.findOneByAccount(username);
        if (user==null){
            return null;
        }
        return new SimpleAuthenticationInfo(username,user.getPassword(),getName());
    }
```

自定义token 

要继承源码token中的两个接口

```java
@Data
@Component
public class JwtToken implements HostAuthenticationToken, RememberMeAuthenticationToken {
    private String token;
    private char[] password;
    private boolean rememberMe;
    private String host;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public JwtToken() {
        this.rememberMe = false;
    }

    public JwtToken(String token, char[] password) {
        this(token, (char[])password, false, (String)null);
    }

    public JwtToken(String token, String password) {
        this(token, (char[])(password != null ? password.toCharArray() : null), false, (String)null);
    }

    public JwtToken(String token, char[] password, String host) {
        this(token, password, false, host);
    }

    public JwtToken(String token, String password, String host) {
        this(token, password != null ? password.toCharArray() : null, false, host);
    }

    public JwtToken(String token, char[] password, boolean rememberMe) {
        this(token, (char[])password, rememberMe, (String)null);
    }

    public JwtToken(String token, String password, boolean rememberMe) {
        this(token, (char[])(password != null ? password.toCharArray() : null), rememberMe, (String)null);
    }

    public JwtToken(String token, char[] password, boolean rememberMe, String host) {
        this.rememberMe = false;
        this.token = token;
        this.password = password;
        this.rememberMe = rememberMe;
        this.host = host;
    }

    public JwtToken(String token, String password, boolean rememberMe, String host) {
        this(token, password != null ? password.toCharArray() : null, rememberMe, host);
    }
}

```

要用自定义token，要在realm中重写一个方法，来判断是不是自定义的JwtToken

```java
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
```

赋值jwt工具类

```java

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    public static String createJWT(String username, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("1sf12sds21ie1inecs078j");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(username)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    /**
     * 解密
     *
     * @param jwt
     */
    public static Claims parseJWT(String jwt) {
        //This line will throw an exception if
        // it is not a signed JWS (as expected)
        try {


            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary("1sf12sds21ie1inecs078j"))
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        }catch (ExpiredJwtException e){
            return null;
        }
//        System.out.println("ID: " + claims.getId());
//        System.out.println("Subject: " + claims.getSubject());
//        System.out.println("Issuer: " + claims.getIssuer());
//        System.out.println("Expiration: " + claims.getExpiration());

    }
    //判断过期
    public static boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public static void main(String[] args) {

        //加密
        String jwt = createJWT("zhangsan", "13dsdda", "afrefsa", 1000 * 60 * 60 * 24 * 7);
        System.out.println(jwt);

        //解密
        parseJWT(jwt);
    }
}
```

自定义的加密方式可用可不用，最好自定义一个密码验证器

StringUtil

```java
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class StringUtil {
    /**
     * 生成随机字符串
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * MD5加密
     */
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}

```

自定义验证器

重写里面在doc方法

```java
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
        String inPassword = new String(jwtToken.getPassword());
        //获取数据库查出来的
        String account = String.valueOf(info.getPrincipals());//获取出第一个参数
        String dbPassword = (String) info.getCredentials();
        //比较验证
        User user = userService.findOneByAccount(account);
        return this.equals(inPassword,dbPassword);
    }
}

```

自定义过滤器



```java
import com.mu.common.Constants;
import com.mu.common.Result;
import com.mu.utils.JwtUtil;
import com.mu.token.JwtToken;
import io.jsonwebtoken.Claims;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter extends AuthenticatingFilter {
    @Override
    //创建token
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");//每次从前端请求的时候，除了登录以外,其他所有的请求头部都需要添加这样一个属性，值为token
        if(StringUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            return true;
        } else {
            // 校验jwt
            Claims claim = JwtUtil.parseJWT(jwt);
            if(claim == null || JwtUtil.isTokenExpired(claim.getExpiration())) {
                HttpServletResponse response = (HttpServletResponse)servletResponse;
                response.setContentType("application/plain;charset=utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(new Result(Constants.CODE_401,"身份已过期，请重新登录",null)));
                return false;
//                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }

            // 执行登录
            return executeLogin(servletRequest, servletResponse);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Throwable throwable = e.getCause() == null ? e : e.getCause();

        Result result = Result.error(Constants.CODE_500,e.getMessage());
        String json = JSON.toJSONString(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }

    //跨域处理
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}

```

​	在请求时有这种情况，就是如果有token，jwt，但因为传过来只有账号信息没有密码信息，这样直接给他通过，因为有token就代表已经有身份了

在mycredentialsMatcher中

```java
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
```



controller中

```java
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
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
        return new Result(Constants.CODE_200,"登录成功",null);
    }
```

### 获取用户的所有角色以及权限

myreal中授权

```java
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
```

config中添加注解配置

```java
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
```

controller中注解

```java
    @RequiresPermissions("user:dfsd")
    @GetMapping("/test")
    public String test1() {
        return "你好";
    }

```

## 前后端交互

store js

```js
import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";
Vue.use(Vuex)

// const store = new Vuex.Store({
//     state: {
//         currentPathName: ''
//     },
//     mutations: {
//         setPath (state) {
//             state.currentPathName = localStorage.getItem("currentPathName")
//         },
//         logout() {
//             //清空缓存
//             localStorage.removeItem("user")
//             localStorage.removeItem("menus")
//             router.push("/login")
//             //重置路由
//             resetRouter()
//         }
//     }
// })
//
// export default store

export default new Vuex.Store({
    state: {
        token: localStorage.getItem("token"),
        userInfo: JSON.parse(localStorage.getItem("userInfo")),
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
            localStorage.setItem("token", token)
        },
        SET_USERINFO: (state, userInfo) => {
            state.userInfo = userInfo
            localStorage.setItem("userInfo", JSON.stringify(userInfo))
        },
        REMOVE_INFO: (state) => {
            state.token = ''
            state.userInfo = {}
            localStorage.setItem("token", '')
            localStorage.setItem("userInfo", JSON.stringify(''))
        }
    },
    getters: {
        // get
        getUser: state => {
            return state.userInfo
        },
        getToken: state => {
            if (state.token == null) {
                return ''
            } else {
                return state.token
            }
        },

    },
    actions: {},
    modules: {}
})

```

router

```js
import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
  {
    path: '/',
    name: 'Manage',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue')
      },
      {
        path: '/user',
        name: 'User',
        component: () => import('../views/User.vue')
      },
      {
        path: '/person',
        name: 'Person',
        component: () => import('../views/Person.vue')
      },
      {
        path: '/borrow',
        name: 'Borrow',
        component: () => import('../views/Borrow.vue')
      },
      {
        path: '/examine',
        name: 'Examine',
        component: () => import('../views/Examine.vue')
      },
      {
        path: '/class',
        name: 'Class',
        component: () => import('../views/Class.vue')
      },
      {
        path: '/role',
        name: 'Role',
        component: () => import('../views/Role.vue')
      },
      {
        path: '/menu',
        name: 'Menu',
        component: () => import('../views/Menu.vue')
      },
      {
        path: '/device',
        name: 'Device',
        component: () => import('../views/Device.vue')
      },
      {
        path: '/myborrow',
        name: 'MyBorrow',
        component: () => import('../views/MyBorrow.vue')
      },
    ]
  }

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router



```

login vue

```js
    login(){

      this.request.post("/user/login",this.user).then(res=>{
        if(res.code==='200'){
          this.$store.commit("SET_TOKEN",res.data.token)
          this.$store.commit("SET_USERINFO",res.data.user)
          // localStorage.setItem("user",JSON.stringify(res.data))
          // localStorage.setItem("menus",JSON.stringify(res.data.menus))
          // //动态设置当前用户的路由
          // setRoutes()
          this.$router.replace("/")//跳转到主页面
          this.$message.success("登录成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    }
```

controller

```java
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
```

userService中查询菜单列表

```JAVA
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
```

controller接口权限最终划分

```java
    @RequiresRoles({"teacher","admin"})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

```





### jwt和jjwt区别

[(195条消息) JWT 和 JJWT，别再傻傻分不清了！_程序IT圈的博客-CSDN博客](https://blog.csdn.net/itcodexy/article/details/120929034)

### shiro中的Realm

[(195条消息) Shiro 中的 Realm_夏洛克卷的博客-CSDN博客_shiro的realm](https://blog.csdn.net/zx48822821/article/details/80503742?ops_request_misc=%7B%22request%5Fid%22%3A%22166080644616782391873228%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=166080644616782391873228&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-1-80503742-null-null.142^v41^pc_search_integral,185^v2^control&utm_term=Realm是什么&spm=1018.2226.3001.4187)



## 全局异常+aop

1.定义自定义异常

myexception

```java
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    /**
     * 如果抛出的是service异常，调用该方法
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handler(ServiceException se){
        return Result.error(se.getCode(),se.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public Result handler(UnauthorizedException e){
        return Result.error(Constants.CODE_401,"无访问权限");
    }

    @ExceptionHandler(ExpiredCredentialsException.class)
    @ResponseBody
    public Result handler(ExpiredCredentialsException e){
        return Result.error(Constants.CODE_401,"登录已过期，请重新登录");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public Result handler(UnauthenticatedException e){
        log.error("运行时异常",e);
        return Result.error(Constants.CODE_401,"未登录");
    }

}

```

描述 

serviceExcepition

```java

@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code,String msg){
        super(msg);//继承
        this.code = code;
    }
}

```

aop切面

aspect.ServiceLogAspect

```java
@Aspect
@Component
public class ServiceLogAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(com.mu.aspect.ServiceLogAspect.class);

    @Pointcut("execution(* com.mu.service.*.*(..))")//任意类，任意方法，任意返回值
    public void pointCut(){

    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null){
            return;
        }
        HttpServletRequest request =attributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." +joinPoint.getSignature().getName();
        LOGGER.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }
}

```





### .UnauthenticatedException: This subject is anonymous - it do异常

说明请求的时候没有携带token

方法一 axiosjs中

```js
// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // let user = localStorage.getItem("userInfo")?JSON.parse(localStorage.getItem("userInfo")):null
    // if(user){
    //     config.headers['token'] = user.token;  // 设置请求头
    // }
    if (window.location.pathname!="/user/login") {
        config.headers['Authorization'] = localStorage.getItem("token");
    }

    return config
}, error => {
    return Promise.reject(error)
});
```

方法二 请求中携带请求头

```js
          this.request.get("/user/getMenuList?account="+account,
              {
                  headers: {
                    "Authorization": this.$store.getters.getToken
                  }
              }).then(res=>{
                this.menuList = res.data
          })
      },
```



# 借用模块

navciat中默认中文报1064

![image-20220812220649605](http://rf0acvi1v.hb-bkt.clouddn.com/image-20220812220649605.png)

在中文两边加上英文单引号或者双引号



## 审核模块中的自己需要审核的显示

```xml
    <!--1.查询所有记录-->
    <select id="pageAllData" resultMap="BorrowRecordMap">
        select
        DISTINCT mu.borrow_record.*
        from mu.borrow_record
        left join mu.sys_file on mu.sys_file.device_id = mu.borrow_record.device_id
        where (mu.borrow_record.teacher_id = #{examinePersonId} and  mu.borrow_record.teacher_review IS NULL)
           or (mu.borrow_record.owner_id =#{examinePersonId} and mu.borrow_record.teacher_review = 'agree' and mu.borrow_record.owner_review IS NULL)
           or(mu.borrow_record.administrator_id=#{examinePersonId} and mu.borrow_record.teacher_review = 'agree' and mu.borrow_record.owner_review = 'agree' and  mu.borrow_record.administrator_review IS NULL)

    </select>
```

## 邮箱验证，找回密码

[(197条消息) Springboot+Vue实现邮箱验证功能（邮箱登录+忘记密码）_程序员青戈的博客-CSDN博客](https://blog.csdn.net/xqnode/article/details/124634370)

email依赖

yml配置

```
  mail:
    #    protocol: smtps
    protocol: smtps
    # 配置 SMTP 服务器地址
    host: smtp.163.com
    # 发送者邮箱
    #    username: xqnode@163.com
    username: muyubingri@163.com
    # 配置密码，注意不是真正的密码，而是刚刚申请到的授权码
    password: VOCBWIOYHZBDMBCL
    # 端口号465或587
    #    port: 465
    port: 465
    # 默认的邮件编码为UTF-8
    default-encoding: UTF-8
```



前端js

```js
    getEmailCode() {

      if(!this.form.email){
          this.$message.success("请输入邮箱账号")
      }
      if(!/^\w+((.\w+)|(-\w+))@[A-Za-z0-9]+((.|-)[A-Za-z0-9]+).[A-Za-z0-9]+$/.test(this.form.email)) {
        this.$message.warning("请输入正确的邮箱账号")
      }
      console.log(this.form.email)
      //发送邮箱验证码
      this.request.get("/user/email/" + this.form.email).then(res=>{
        if(res.code==='200'){
          this.$message.success("发送成功");
        }
        else{
          this.$message.error(res.msg)
        }
      })
    },
```



service

```java
    @Override
    public void sendEmailCode(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from); //发送人
        mailMessage.setSubject("邮箱验证");
        String code = RandomUtil.randomNumbers(4);
        mailMessage.setTo(email);
        mailMessage.setText("验证码："+code+"有效期5分钟");
        javaMailSender.send(mailMessage);
        Date now = new Date();

        //发送成功之后把密码存到数据库
        validationService.saveCode(email,code, DateUtil.offsetMinute(now,5));
    }
```



新建validation表

```java
@Service
public class ValidationServiceImpl extends ServiceImpl<ValidationMapper, Validation> implements ValidationService {

    @Override
    public void saveCode(String email, String code, DateTime offsetMinute) {
        Validation validation = new Validation();
        validation.setEmail(email);
        validation.setCode(code);
        validation.setTime(offsetMinute);

        UpdateWrapper<Validation> validationUpdateWrapper = new UpdateWrapper<>();
        validationUpdateWrapper.eq("email",email);
        remove(validationUpdateWrapper);
        save(validation);
    }
}

```



### 自定义注解

config包中

不用token也可以访问

```java
import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {

}
```



# 技巧

#### 不知道用什么变量接收时

```java
JwtUtil.parseJWT(jwt).var  回车
```

## 收缩菜单后，文字不消失问题

```js
<style scoped>
/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span{
  visibility: hidden;
}
</style>
```



