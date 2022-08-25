软件版本

- jdk 1.8
- mysql 5.7+
- nodejs
- navicat
- idea



vue-cli  npm install -g @vue/cli

淘宝 `npm config set registry https://registry.npmjs.org`

创建vue项目 `vue create vue`

运行vue项目：

```
cd vue
npm run serve
```

## vue项目结构

```
1、build：构建脚本目录
1）build.js ==> 生产环境构建脚本；
2）check-versions.js ==> 检查npm，node.js版本；
3）utils.js ==> 构建相关工具方法；
4）vue-loader.conf.js ==> 配置了css加载器以及编译css之后自动添加 前缀；
5）webpack.base.conf.js ==> webpack基本配置；
6）webpack.dev.conf.js ==> webpack开发环境配置；
7）webpack.prod.conf.js ==> webpack生产环境配置；
2、config：项目配置
1）dev.env.js ==> 开发环境变量；
2）index.js ==> 项目配置文件；
3）prod.env.js ==> 生产环境变量；
3、node_modules：npm 加载的项目依赖模块
4、src：这里是我们要开发的目录，基本上要做的事情都在这个目录里。里面包含了几个目录及文件：
1）assets：资源目录，放置一些图片或者公共js、公共css。但是因为它们属于代码目录下，所以可以用 webpack 来操作和处理。意思就是你可以使用一些预处理比如 Sass/SCSS 或者 Stylus。
2）components：用来存放自定义组件的目录，目前里面会有一个示例组件。
3）router：前端路由目录，我们需要配置的路由路径写在index.js里面；
4）App.vue：根组件；这是 Vue 应用的根节点组件，往下看可以了解更多关注 Vue 组件的信息。
5）main.js：应用的入口文件。主要是引入vue框架，根组件及路由设置，并且定义vue实例，即初始化 Vue 应用并且制定将应用挂载到index.html 文件中的哪个 HTML 元素上。通常还会做一些注册全局组件或者添额外的 Vue 库的操作。
5、static：静态资源目录，如图片、字体等。不会被webpack构建
6、index.html：首页入口文件，可以添加一些 meta 信息等。 这是应用的模板文件，Vue 应用会通过这个 HTML 页面来运行，也可以通过 lodash 这种模板语法在这个文件里插值。 注意：这个不是负责管理页面最终展示的模板，而是管理 Vue 应用之外的静态 HTML 文件，一般只有在用到一些高级功能的时候才会修改这个文件。
7、package.json：npm包配置文件，定义了项目的npm脚本，依赖包等信息
8、README.md：项目的说明文档，markdown 格式
9、.xxxx文件：这些是一些配置文件，包括语法配置，git配置等
1）.babelrc:babel编译参数
2）.editorconfig:编辑器相关的配置，代码格式
3）.eslintignore : 配置需要或略的路径，一般build、config、dist、test等目录都会配置忽略
4）.eslintrc.js : 配置代码格式风格检查规则
5）.gitignore:git上传需要忽略的文件配置
6）.postcssrc.js ：css转换工 
```

1调整整体布局以及细节样式

main.js

```html
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/gloable.css'

Vue.config.productionTip = false

Vue.use(ElementUI,{size:"small"});
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')

```

Home.vue

```vue
<template>
  <div style="height: 100%">
    <el-container style="height: 100%;">
      <el-aside width="200px" style="background-color: rgb(238, 241, 246);height: 100%;overflow: hidden">
        <el-menu :default-openeds="['1', '3']" style="height: 100%">
          <el-submenu index="1">
            <template slot="title"><i class="el-icon-message"></i>导航一</template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="1-1">选项1</el-menu-item>
              <el-menu-item index="1-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="1-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="1-4">
              <template slot="title">选项4</template>
              <el-menu-item index="1-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-menu"></i>导航二</template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="2-1">选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title"><i class="el-icon-setting"></i>导航三</template>
            <el-menu-item-group>
              <template slot="title">分组一</template>
              <el-menu-item index="3-1">选项1</el-menu-item>
              <el-menu-item index="3-2">选项2</el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group title="分组2">
              <el-menu-item index="3-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="3-4">
              <template slot="title">选项4</template>
              <el-menu-item index="3-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px;border-bottom:1px solid #ccc; line-height: 60px">
          <el-dropdown>
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>查看</el-dropdown-item>
              <el-dropdown-item>新增</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <span>王小虎</span>
        </el-header>

        <el-main>
          <el-table :data="tableData">
            <el-table-column prop="date" label="日期" width="140">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="address" label="地址">
            </el-table-column>
          </el-table>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>


export default {
  name: 'HomeView',
  data(){
    const item = {
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
    };
    return {
      tableData: Array(10).fill(item),
      msg:"沐"
    }
  }
}
</script>

```

App.vue

```vue
<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
#app{
  height: 100%;
}
</style>
```

gloable.css

```css
html,body,div{
    margin:0;
    padding:0;
}
html,body{
    height: 100%;
}

```

在main.js中引入`import './assets/gloable.css'`

home.vue

```vue
<template>
  <div style="height: 100%">
    <el-container style="height: 100%;">
      <el-aside :width="sideWidth+'px'" style="background-color: rgb(238, 241, 246);height: 100%;overflow: hidden ;box-shadow: 2px 0 6px rgb(0 21 41 /35%)">
        <el-menu :default-openeds="['1', '3']" style="height: 100% "
            background-color="rgb(48,65,86)"
                 text-color="#fff"
                 active-text-color="#ffd04b"
                 :collapse-transition="false"
                 :collapse="isCollapse"
        >
          <div style="height: 60px;line-height: 60px;text-align: center">
            <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top: 5px;margin-right: 5px">
            <b style="color: white" v-show="logoTestShow">设备管理系统</b>
          </div>
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-message"></i>
            <span slot="title">导航一</span>
            </template>
            <el-menu-item-group title="分组2">
              <el-menu-item index="1-3">选项3</el-menu-item>
            </el-menu-item-group>
            <el-submenu index="1-4">
              <template slot="title">选项4</template>
              <el-menu-item index="1-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-message"></i>
              <span slot="title">导航二</span>
            </template>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-submenu index="3">
            <template slot="title">
              <i class="el-icon-message"></i>
              <span slot="title">导航三</span>
            </template>
            <el-submenu index="3-4">
              <template slot="title">选项4</template>
              <el-menu-item index="3-4-1">选项4-1</el-menu-item>
            </el-submenu>
          </el-submenu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header style=" font-size: 12px;border-bottom:1px solid #ccc; line-height: 60px;display: flex">
          <div style="flex:1;font-size:20px">
            <span :class="collapseBtnClass" style="cursor:pointer" @click="collapse"></span>
          </div>
          <el-dropdown style="width: 70px;cursor:pointer">
            <span>王小虎</span><i class="el-icon-arrow-down" style="margin-left:5px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-header>

        <el-main>
          <div style="margin-bottom:30px ">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>用户管理</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div style="padding: 10px 0">
              <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search"></el-input>
              <el-input style="width: 200px"  placeholder="请输入邮箱" suffix-icon="el-icon-message"></el-input>
              <el-input style="width: 200px"  placeholder="请输入地址" suffix-icon="el-icon-position"></el-input><el-button class="ml-5" type="primary">搜索</el-button>
          </div>
          <div style="margin: 10px 0">
            <el-button type="primary">新增<i class="el-icon-circle-plus-outline"></i></el-button>
            <el-button type="danger">批量删除<i class="el-icon-remove-outline"></i></el-button>
            <el-button type="primary">导入<i class="el-icon-bottom"></i></el-button>
            <el-button type="primary">导出<i class="el-icon-top"></i></el-button>
          </div>
          <el-table :data="tableData" border stripe :header-cell-class-name="headerBg">
            <el-table-column prop="date" label="日期" width="140">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="120">
            </el-table-column>
            <el-table-column prop="address" label="地址">
            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button type="success">编辑<i class="el-icon-edit"></i></el-button>
                  <el-button type="danger">删除<i class="el-icon-remove-outline"></i></el-button>
                </template>
            </el-table-column>
          </el-table>
          <el-pagination
              :page-sizes="[5, 10, 15, 20]"
              :page-size="10"
              layout="total, sizes, prev, pager, next, jumper"
              :total="400">
          </el-pagination>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>


export default {
  name: 'HomeView',
  data(){
    const item = {
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
    };
    return {
      tableData: Array(10).fill(item),
      msg:"沐",
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      sideWidth:200,
      logoTestShow:true,
      headerBg:'headerBg'
    }
  },
  methods:{
    collapse() {//点击收缩按钮触发
        this.isCollapse=!this.isCollapse;
        if(this.isCollapse){
            this.sideWidth=64;
            this.collapseBtnClass='el-icon-s-unfold'
            this.logoTestShow=false
        }else{
          this.sideWidth=200;
          this.collapseBtnClass='el-icon-s-fold'
          this.logoTestShow=true
        }
    }
  }
}
</script>
<style>
.headerBg{
  background:#eee !important;
}
</style>
```

mybatis查询流程

1. pom引入mybatis依赖
2. 写个entity
3. mapper
4. 在启动类里写restcontroller

代理对象

![image-20220724101705275](http://rf0acvi1v.hb-bkt.clouddn.com/image-20220724101705275.png)

![image-20220724101944199](http://rf0acvi1v.hb-bkt.clouddn.com/image-20220724101944199.png)

#### 分页查询

传统方法 fetch请求分页查询数据

```vue
  create(){
    //请求 分页查询数据
    //将字符串转成一个json
    fetch("http://localhost:9090/user/page?pageNum=1&pageSize=2").then(res=>res.json()).then(res=>{
      
    })
  },
```

## 前端跨域问题

跨域：指的是浏览器不能执行其他网站的脚本。它是由浏览器的同源策略造成的，是浏览器对javascript施加的安全限制。

例如：a页面想获取b页面资源，如果a、b页面的协议、域名、端口、子域名不同，所进行的访问行动都是跨域的，而浏览器为了安全问题一般都限制了跨域访问，也就是不允许跨域请求资源。注意：跨域限制访问，其实是浏览器的限制。理解这一点很重要！！！


- 其实无论哪种方案，最终目的都是修改响应头，向响应头中添加浏览器所要求的数据，进而实现跨域。

[(182条消息) SpringBoot解决跨域的5种方式_wh柒八九的博客-CSDN博客_springboot跨域](https://blog.csdn.net/qq_31960623/article/details/118254754?ops_request_misc=%7B%22request%5Fid%22%3A%22165866035516782388046195%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=165866035516782388046195&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-118254754-null-null.142^v33^pc_rank_34,185^v2^control&utm_term=springboot跨域&spm=1018.2226.3001.4187)

```java
package com.mu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 当前跨域请求最大有效时长。这里默认1天
    private static final long MAX_AGE = 24 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080"); // 1 设置访问源地址
        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
        corsConfiguration.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
}

```

## 忽略某个字段，不展示给前端，比如密码

```java
    @JsonIgnore
    private String password;
```

## mybatis-plus依赖

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>

```

## @MapperScan与@Mapper的作用

使Mybatis知道需要对哪些接口提供代理实现。

声明：不同版本mybatis代理类生成机制可能存在差异，以下结论`@MapperScan`基于`org.mybatis:mybatis-spring:2.0.6`，`@Mapper`基于`org.mybatis:mybatis:3.5.9`

在目标接口上使用@Mapper注解进行标识

需保证该接口源码必须在当前项目下

即：该接口必须是当前项目的源码接口，不能是其它依赖类库中的，否者@Mapper不会生效。

注：有人可能会以为使用@ComponentScan将其它依赖类库中的mapper导入就可以了。其实是不可以的，@ComponentScan扫描的是被@Component等注解标注了的类并注册进Spring容器，与Mybatis扫描生成代理类的识别机制是两个概念。

注：如果需要让Mybatis对其它依赖类库中的mapper接口也生成代理类，那么请使用@MapperScan


@MapperScan指定范围下的所有接口，是所有接口，不论是mapper接口、还是service接口、或者是其它什么接口，只要接口是在@MapperScan指定的范围内，Mybatis都会对该接口进行对应的代理实现（，并将代理实现类注册进容器中）。

##### mybatisplusconfig.java

```java
package com.mu.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.mu.mapper")
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}

```

sql没有打印原因是已经被mybatisplus环境所管理了，要换一个配置

mybatis-plus默认使用实体类为表名，在实体类上+tableName注解解决

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value="sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String address;

    @TableField(value="avatar_url")//数据库名和实体类名绑定
    private String avatar;

}

```



usercontroller

```java
package com.mu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.entity.User;
import com.mu.mapper.UserMapper;
import com.mu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        IPage<User> userPage = userService.page(page,objectQueryWrapper);
        return userPage;
    }


}

```

## 新增和编辑



```vue
   handleEdit(row){
        this.form = JSON.parse(JSON.stringify(row))
        this.dialogFormVisible=true
    },

    save(){
        request.post("/user",this.form).then(res=>{
          if(res){
            this.$message.success("保存成功")
            this.dialogFormVisible=false
            this.load()
          }else{
            this.$message.error("保存失败")
          }
        })
    },
```

## 批量删除

delete请求中不能拿传递数组，可以改为post请求

## 代码生成器

```java
package com.mu.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }

    private static void generate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mu?serverTimezone=GMT%2b8", "root", "1234")
                .globalConfig(builder -> {
                    builder.author("mu") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("E:\\university\\Code\\system\\springboot\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.mu") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "E:\\university\\Code\\system\\springboot\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();//加一个mapper注解
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符,针对接口路径
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("sys_user") // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}

```

controller.java.vm模板文件

```java
package ${package.Controller};


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mu.entity.User;
import com.mu.mapper.UserMapper;
import com.mu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.management.DescriptorKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import $!{package.Service}.$!{table.serviceName};
import ${package.Entity}.${entity};

#if(${restControllerStyle})
import org.springframework.web.bind.annotation.RestController;
#else
import org.springframework.stereotype.Controller;
#end
#if(${superControllerClassPackage})
import ${superControllerClassPackage};
#end

/**
 * <p>
 * $!{table.comment} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
#if(${restControllerStyle})
@RestController
#else
@Controller
#end
@RequestMapping("#if(${package.ModuleName})/${package.ModuleName}#end/#if(${controllerMappingHyphenStyle})${controllerMappingHyphen}#else${table.entityPath}#end")
#if(${kotlin})
class ${table.controllerName}#if(${superControllerClass}) : ${superControllerClass}()#end

#else
#if(${superControllerClass})
public class ${table.controllerName} extends ${superControllerClass} {
#else
public class ${table.controllerName} {
#end

##    相当于导入userservice这个包
    @Resource
    private ${table.serviceName} ${table.entityPath}Service;

    @PostMapping
    public Boolean save(@RequestBody ${entity} ${table.entityPath}) {
        return ${table.entityPath}Service.saveOrUpdate(${table.entityPath});
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return ${table.entityPath}Service.removeById(id);
    }

    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return ${table.entityPath}Service.removeByIds(ids);
    }

    @GetMapping
    public List<${entity}> findAll() {
        return ${table.entityPath}Service.list();
    }

    @GetMapping("/{id}")
    public ${entity} findOne(@PathVariable Integer id) {
        return ${table.entityPath}Service.getById(id);
    }

    @GetMapping("/page")
    public Page<${entity}> findPage(@RequestParam Integer pageNum,
    @RequestParam Integer pageSize) {
        return ${table.entityPath}Service.page(new Page<>(pageNum, pageSize));
    }


}

#end
```

## 路由

ajax请求的配置由于放在mainjs中，是全局变量，可以不用import 直接this.request请求

将home改名Manage.vue

通过manage管理所有的vue界面，在切换的时候，只有主体会变，而头部和侧边栏是不变的，所以把manage作为一个web的框架，把里面的视图作为一个子路由，当点击的时候只需要切换子路由就行了，

安装vuex

`npm install vuex@3.6.2 --save`

STOREjs

```
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    state: {
        currentPathName: ''
    },
    mutations: {
        setPath (state) {
            state.currentPathName = localStorage.getItem("currentPathName")
        }
    }
})

export default store
```

## 动态获取请求头

路由和组件的数据监听显示页签

## 导入导出

依赖

导入时如果表头是中文

```java
        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarUrl(row.get(6).toString());
            users.add(user);
        }
```

### 方法二 Alias注解

```java
 @Alias("用户名")
    private String username;



    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        System.out.println(list);
        userService.saveBatch(list);//批量插入到数据库
        return true;
    }
```

vue中

加冒号的，说明后面的是一个变量或者[表达式](https://so.csdn.net/so/search?q=表达式&spm=1001.2101.3001.7020)；没加冒号的后面就是对应的字符串字面量！

## 登录界面login

```vue
<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
      <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        <div style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small"  autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small"  autocomplete="off">注册</el-button>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      user:{}
    }
  }
}
</script>

<style>
  .wrapper{
    height: 100vh;
    background-image: linear-gradient(to bottom right,#FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>
```

路由中加入

```js

const routes = [
  {
    path: '/',
    name: 'Manage',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children:[
      { path: 'home', name: '主页', component: () => import('../views/Home.vue')},
      { path: 'user', name: '用户', component: () => import('../views/User.vue')},
    ]
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  }
]
```

## login.vue

```vue
<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
      <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
        <div style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small"  autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small"  autocomplete="off">注册</el-button>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      user:{}
    }
  }
}
</script>

<style>
  .wrapper{
    /*窗口高度*/
    height: 100vh;
    /*渐变的背景色*/
    background-image: linear-gradient(to bottom right,#FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>
```

可以新建userdto 传输用户数据的一个类，将接受参数的类和实体类分隔开

![image-20220730154359699](C:/Users/Administrator/AppData/Roaming/Typora/typora-user-images/image-20220730154359699.png)

requestbody是将json转为java对象

```java
@Data
public class UserDTO {
    private String account;
    private String password;
}


    @PostMapping
    public Boolean login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }





```

[(186条消息) mybatis plus 条件构造器queryWrapper学习_bird_tp的博客-CSDN博客_querywrapper](https://blog.csdn.net/bird_tp/article/details/105587582?ops_request_misc=%7B%22request%5Fid%22%3A%22165916768216782248515837%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=165916768216782248515837&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-2-105587582-null-null.142^v35^pc_rank_34&utm_term=QueryWrapper是什么&spm=1018.2226.3001.4187)

```js
methods:{
    login(){
      this.request("/user/login").then(res=>{
        if(!res){
          this.$message.error("用户名或密码错误")
        }else{
          this.$router.push("/")//跳转到主页面
        }
      })
    }
  }
}
```

## 400错误

前端传的参数跟后台对不上

```js
  methods:{
    login(){
      this.request.post("/user/login",this.user).then(res=>{
        if(!res){
          this.$message.error("用户名或密码错误")
        }else{
          this.$router.push("/")//跳转到主页面
        }
      })
    }
  }
}
```

## 改造接口 统一包装类

返回同样一个表示位，如果不统一，前端会处理不一样的请求类型

```java
//包装类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;//  标志请求是否成功
    private String msg;//原因
    private Object data;

    //如果请求成功，返回成功信息
    //没有数据的成功
    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }
    //有数据的成功
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }
    //错误
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }

    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}
```

## 自定义异常

```java
@ControllerAdvice
public class MyExceptionHandler {
    /**
     * 如果抛出的是service异常，调用该方法
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se){
        return Result.error(se.getCode(),se.getMessage());
    }

}

```

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

## 在登录失败的时候报系统错误而不是密码错误原因

```java
@Service
public class  UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log log = Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",userDTO.getAccount());
        queryWrapper.eq("password",userDTO.getPassword());
//        List<User> list =  list(queryWrapper);//万一查出来多个，也让他登录
        try {
            User one = getOne(queryWrapper);//从数据库查询用户信息
            if(one!=null){
                BeanUtil.copyProperties(one,userDTO,true);
                return userDTO;
            }else{
                throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
            }
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
//        return list.size()!=0;
    }
}

```

![image-20220731103157893](http://rf0acvi1v.hb-bkt.clouddn.com/image-20220731103157893.png)

在上面throw的exception被catch了，然后再throw的被全局补货到了

```java
@Service
public class  UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log log = Log.get();

    @Override
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",userDTO.getAccount());
        queryWrapper.eq("password",userDTO.getPassword());
//        List<User> list =  list(queryWrapper);//万一查出来多个，也让他登录
        User one ;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
            //数据库查询，可以视为系统异常
        } catch (Exception e) {
            log.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");//如果有多条数据，返回false
        }
        if(one!=null){
            BeanUtil.copyProperties(one,userDTO,true);
             return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
//        return list.size()!=0;
    }
}


```

## 登录时，使用数据

```
  methods:{
    login(){
      this.request.post("/user/login",this.user).then(res=>{
        if(res.code==='200'){
          localStorage.setItem("user",JSON.stringify(res.data))//存储用户信息到浏览器
          this.$router.push("/")//跳转到主页面
          this.$message.success("登录成功")
        }else{
          this.$message.error(res.msg)
        }
      })
    }
  }







data(){
    return {
      // paths: []
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{}
    }

  },
```

## 退出时，去掉数据

```js
    logout(){
      this.$router.push("/login")
      localStorage.removeItem("user")
      this.$message.success("退出")
    },
```

## 注册后端代码

```java
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String account = userDTO.getAccount();
        String password= userDTO.getPassword();
        if(StrUtil.isBlank(account)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }

        return Result.success(userService.register(userDTO));
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

```

## JWT

[(186条消息) SpringBoot集成JWT实现token验证_苍穹帝的博客-CSDN博客_springboot集成jwt](https://blog.csdn.net/gjtao1130/article/details/111658060?ops_request_misc=&request_id=&biz_id=102&utm_term=springboot继承JWT苍穹&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduweb~default-1-111658060.142^v35^pc_rank_34&spm=1018.2226.3001.4187)

`Jwt`全称是：`json web token`。它将用户信息加密到`token`里，服务器不保存任何用户信息。服务器通过使用保存的[密钥](https://so.csdn.net/so/search?q=密钥&spm=1001.2101.3001.7020)验证`token`的正确性，只要正确即通过验证。

优点

![image-20220731220959200](http://rf0acvi1v.hb-bkt.clouddn.com/image-20220731220959200.png)



## token工具类

```java

public class TokenUtils {
    /**
     * 生成token
     */
    public static String getToken(String userId,String sign){
        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面
                .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥

    }
}

```

## request.js改造

```js
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")):null
    if(user){
        config.headers['token'] = user.token;  // 设置请求头
    }
    return config
}, error => {
    return Promise.reject(error)
});
```



JwtInterceptor

```java
package com.mu.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mu.common.Constants;
import com.mu.entity.User;
import com.mu.exception.ServiceException;
import com.mu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;


public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取
        String token = request.getHeader("token");
        //如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        //执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"无token,请重新登录");
        }
        //获取token中的数据
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败");
        }
        //根据token中的userid查询数据库
        User user = userService.getById(userId);
        if(user==null){
            throw new ServiceException(Constants.CODE_401,"用户不存在");
        }
        //用户密码加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);//验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败,请重新登录");
        }
        return true ;
    }
}

```

InterceptorConfig

```java
package com.mu.config;

import com.mu.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements   WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")//拦截所有请求，通过判断token合法，来决定
                .excludePathPatterns("/user/login","/user/register","/**/export","/**/import");
    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
```

## jwt继承完毕，如果想在后台获取用户信息如何获取

首先把tokenutils注册位spring的一个bean，通过resouce方式，把userService引进来，再在后台启动的时候，赋值给一个静态对象，通过静态方法获取数据必须引用静态对象

## 装配注解

[(186条消息) @Autowired和@Resource注解的区别和联系（十分详细，不看后悔）_莫小兮丶的博客-CSDN博客_autowired和resource注解的区别](https://blog.csdn.net/qq_45590494/article/details/114444371?ops_request_misc=%7B%22request%5Fid%22%3A%22165936271616781818796111%22%2C%22scm%22%3A%2220140713.130102334.pc%5Fall.%22%7D&request_id=165936271616781818796111&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~pc_rank_v37-5-114444371-null-null.142^v37^pc_rank_v37&utm_term=%40Resource注解&spm=1018.2226.3001.4187)

## @PostConstruct

[(186条消息) @PostConstruct注解_那些很冒险的梦丶的博客-CSDN博客_postconstruct](https://blog.csdn.net/qq360694660/article/details/82877222?ops_request_misc=%7B%22request%5Fid%22%3A%22165936376116780366567844%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=165936376116780366567844&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-82877222-null-null.142^v37^pc_rank_v37&utm_term=%40PostConstruct&spm=1018.2226.3001.4187)

@PostConstruct**是java自己的注解**，被用来修饰一个非静态的void方法，会在服务器加载servlet时运行。并且只会执行一次，postconbstruct在构造函数之后执行，init方法之前执行

### 顺序

Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)



## 修改个人信息头像后不用刷新，同步修改右上角小头像

子传父，父传子，person先触发父级获取用户信息，再传给header组件

person.vue

```vue
save(){
      this.request.post("/user",this.form).then(res=>{
        if(res.code==='200'){
          this.$message.success("保存成功")
          //触发父级，通过父级来获取后台数据。然后再传给header
          //触发父级更新User的方法
          this.$emit("refreshUser")
        }else{
          this.$message.error("保存失败")
        }
      })
    },
```

manage.vue

```vue
        <el-header style=" border-bottom:1px solid #ccc; ">
          <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user"></Header>
        </el-header>

        <el-main>
           <router-view @refreshUser="getUser"/>
        </el-main>

    return {

      msg:"沐",
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      sideWidth:200,
      logoTestShow:true,
      user: {}

    }

created() {
    this.getUser()
  },
  methods:{
    collapse() {//点击收缩按钮触发
        this.isCollapse=!this.isCollapse;
        if(this.isCollapse){
            this.sideWidth=64;
            this.collapseBtnClass='el-icon-s-unfold'
            this.logoTestShow=false
        }else{
          this.sideWidth=200;
          this.collapseBtnClass='el-icon-s-fold'
          this.logoTestShow=true
        }
    },
    getUser(){
      let account  = localStorage.getItem("user")?JSON.parse(localStorage.getItem("user")).account:""
      //从后台获取数据
     this.request.get("/user/account/"+account).then(res=>{
        this.user = res.data
     })
    },
```



header.vue

```vue
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    user: Object
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　//需要监听的数据
    }
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },
  data(){
    return {
      // paths: []
    }

  },
```

新增设备 前端上传多张图片，后端接受并将文件所属设备id同时绑定

Device.VUE

```vue
<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search" v-model="devicename"></el-input>
      <el-input style="width: 200px"  placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email"></el-input>
      <!--              <el-input style="width: 200px"  placeholder="请输入地址" suffix-icon="el-icon-position"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='再想想'
          icon="el-icon-info"
          icon-color="red"
          title="确定删除吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="deviceName" label="名称" width="140"></el-table-column>
      <el-table-column prop="price" label="价格" width="120"></el-table-column>
      <el-table-column prop="ownerId" label="拥有者"></el-table-column>
      <el-table-column prop="place" label="地址"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.deviceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="拥有者">-->
<!--          <el-input v-model="form.email" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-upload
              class="upload-demo"
              action="http://localhost:9090/file/upload"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              list-type="picture"
              :on-success="uploadSuccess1">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.place" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Device",
  data(){
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:6,
      form:{},
      fileList:[],//作为图片url的数组
      devicename:"",
      dialogFormVisible:false,
      multipleSelection:[],
      headerBg:'headerBg',
      dialogVisible: false
    }
  },
  created(){
    //请求 分页查询数据
    //将字符串转成一个json
    this.load();
    this.form.fileList= this.fileList
  },
  methods:{
    load(){
      this.request.get("/device/page/",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          devicename:this.devicename,
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total = res.data.total

      })
      // fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email).then(res=>res.json()).then(res=>{
      //   console.log(res);
      //   this.tableData = res.data;
      //   this.total=res.total;
      // })
    },
    uploadSuccess1(res){
      console.log(res)
      this.fileList.push(res)  //这里直接push会报错，但是改的话就不能把图片存成数组形式了，我还没有找到好的办法，所以只能先这样把功能实现了
      console.log(this.fileList)
      this.form.fileurl = this.fileList
      console.log(this.form.fileurl)
    },
    save(){
      console.log(this.form)
      this.request.post("/device",this.form).then(res=>{
        if(res.data){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    reset(){
      this.devicename=""
      this.load()
    },
    //修改数据
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible=true
    },
    del(id){
      this.request.delete("/device/"+id).then(res=>{
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    delBatch(){
      let ids = this.multipleSelection.map(v=>v.id) //将数据扁平化处理，由对象转化为数组
      this.request.post("/device/del/batch",ids).then(res=>{
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    handleSizeChange(pageSize){
      console.log(pageSize);
      this.pageSize=pageSize;
      //当切换页码的时候，重新请求一下，由于参数已经更i性能，那就在加载时候直接变化
      this.load();
    },
    handleCurrentChange(pageNum){
      console.log(pageNum);
      this.pageNum=pageNum;
      this.load();
    },
    //新增数据
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    }
  }
}
</script>

<style scoped>

</style>
```

上传成功方法绑定uploadSuccess1,此方法接受res参数为url，每上传一个就push添加一个字符串，然后更新表格数据，最后传递表格数据

在devicecontroller中 通过dto先得到device的信息，然后在数据库中更新数据，因为只有数据库中存在才能够与file关联。在return前，进入photo方法，将dto中的url集合一个个接受并且存放，存放方法就是先通过url获取file对象，然后将deviceid赋值

## 返回devicedto携带file对象以及url数组

[(187条消息) Mybatis一对多查询的两种姿势，你值得拥有（收藏就完事了）_臭小子帅的博客-CSDN博客_mybatis 一对多 子查询](https://blog.csdn.net/shuai8624/article/details/116563491)

mabatis。xml中

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mu.mapper.DeviceMapper">
<!--    返回结果集合，id 是唯一标识，type 是结果类型-->
    <resultMap id="DeviceDTOMap" type="com.mu.controller.dto.DeviceDTO">
        <id column="id" jdbcType="INTEGER"  property="id"/>
        <result column="device_name" jdbcType="VARCHAR" property="deviceName"/>
        <result column="price" jdbcType="VARCHAR" property="price"/>
        <result column="owner_id" jdbcType="INTEGER" property="ownerId"/>
        <result column="place" jdbcType="VARCHAR" property="place"/>
        <!--实现连表查询 property 是实体对象，id 是传递的参数，select 是查询语句-->
        <collection property="filesList"  javaType="ArrayList" ofType="com.mu.entity.Files" select="selectList" column="id">
<!--            <id column="id" property="id" jdbcType="INTEGER"/>-->
<!--            <result column="device_id" property="deviceId" jdbcType="INTEGER"/>-->
<!--            <result column="url" property="url" jdbcType="VARCHAR"/>-->
        </collection>
    </resultMap>

    <select id="pageAll" resultMap="DeviceDTOMap">
        select
        *
        from
        mu.sys_device
        left join mu.sys_file on mu.sys_file.device_id = mu.sys_device.id
        <where>
            <if test="deviceName!=null">
                device_name like concat('%',#{deviceName},'%')
            </if>
        </where>
    </select>

<!--    子查询-->
<!--    子查询的resultMap-->
    <resultMap id="filesMap" type="com.mu.entity.Files">
    </resultMap>

    <select id="selectList" resultMap="filesMap">
        select * from mu.sys_file where mu.sys_file.device_id=#{id}
    </select>


</mapper>

```

## 消除左连接的笛卡尔积，重复数据

[(187条消息) SQL left join 左表合并去重技巧总结_ZhaoYingChao88的博客-CSDN博客_sql左连接去重](https://blog.csdn.net/ZYC88888/article/details/83002882)

```sql
    <select  id="pageAllData" resultMap="DeviceDTOMap">
        select
        DISTINCT mu.sys_device.*
        from
        mu.sys_device
        left join mu.sys_file on mu.sys_file.device_id = mu.sys_device.id
        <where>
            <if test="deviceName!=null">
                device_name like concat('%',#{deviceName},'%')
            </if>
        </where>
    </select>

```

将设备与用户绑定

controller

```java
    @PostMapping
    public Result save(@RequestBody DeviceDTO deviceDTO) {
        //将devicedto中的多个图片与本设备的id对应上
//        System.out.println("------------"+deviceDTO.getFileurl());
        System.out.println(deviceDTO);
        Device device = new Device();
        deviceDTO = deviceService.owner(deviceDTO);//将设备拥有者与老师绑定
        //通过deviceDto查询出对应的device
        BeanUtil.copyProperties(deviceDTO,device,true);
        System.out.println(device);
        Result result = Result.success(deviceService.saveOrUpdate(device));
        deviceService.photo(deviceDTO);
        return result;
    }
```

serviceimpl

```java
    //设备与教师绑定
    @Override
    public DeviceDTO owner(DeviceDTO deviceDTO) {
        User one ;
        try {
            one = userService.getUserInfoByUserName(deviceDTO);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new ServiceException(Constants.CODE_500, "系统错误");//如果有多条数据，返回false
        }
        deviceDTO.setOwnerId(one.getId());
        return deviceDTO;
    }
```

## 一对一查询返回dto的xml语句

```
       <!--一对一查询-->
        <association property="ownerName" javaType="string" select="selectTheOwnerName" column="owner_id">

        </association>
        
        
    <select id="selectTheOwnerName" resultType="string">
        select mu.sys_user.username from mu.sys_user where  mu.sys_user.id = #{ownerId}
    </select>



```

## 一对多上传图片

```vue
<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px"  placeholder="请输入名称" suffix-icon="el-icon-search" v-model="devicename"></el-input>
      <el-input style="width: 200px"  placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email"></el-input>
      <!--              <el-input style="width: 200px"  placeholder="请输入地址" suffix-icon="el-icon-position"></el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button  type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='再想想'
          icon="el-icon-info"
          icon-color="red"
          title="确定删除吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="deviceName" label="名称" width="140"></el-table-column>
      <el-table-column prop="price" label="价格" width="120"></el-table-column>
      <el-table-column prop="ownerName" label="拥有者"></el-table-column>
      <el-table-column label="图片" align="center" width="265px">
        <template slot-scope="scope">
        <span v-for="(item,index) in scope.row.filesList" :key="index">
            <el-popover placement="left" trigger="click" width="300">
                <img :src="item.url" width="100%" />
                <img
                    slot="reference"
                    :src="item.url"
                    :alt="item"
                    style="max-height: 70px;max-width: 70px; padding: 5px"
                />
            </el-popover>
        </span>
        </template>
      </el-table-column>
      <el-table-column prop="place" label="地址"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='再想想'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="60px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.deviceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="form.price" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="拥有者">
          <el-input v-model="form.ownerName" autocomplete="off"></el-input>
        </el-form-item>
<!--        <el-form-item label="拥有者">-->
<!--          <el-input v-model="form.email" autocomplete="off"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-upload
              class="upload-demo"
              action="http://localhost:9090/file/upload"
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              :file-list="fileList"
              list-type="picture"
              :on-success="uploadSuccess1">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.place" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Device",
  data(){
    return {
      tableData: [],
      total:0,
      pageNum:1,
      pageSize:6,
      form:{},
      fileList:[],//作为图片url的数组
      devicename:"",

      dialogFormVisible:false,
      multipleSelection:[],
      headerBg:'headerBg',
      dialogVisible: false
    }
  },
  created(){
    //请求 分页查询数据
    //将字符串转成一个json
    this.load();
    this.form.fileList= this.fileList
  },
  methods:{
    load(){
      this.request.get("/device/page/",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          devicename:this.devicename,
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data.records
        this.total = res.data.total

      })
      // fetch("http://localhost:9090/user/page?pageNum="+this.pageNum+"&pageSize="+this.pageSize+"&username="+this.username+"&email="+this.email).then(res=>res.json()).then(res=>{
      //   console.log(res);
      //   this.tableData = res.data;
      //   this.total=res.total;
      // })
    },
    uploadSuccess1(res){
      console.log(res)
      this.fileList.push(res)  //这里直接push会报错，但是改的话就不能把图片存成数组形式了，我还没有找到好的办法，所以只能先这样把功能实现了
      console.log(this.fileList)
      this.form.fileurl = this.fileList
      console.log(this.form.fileurl)
    },
    save(){
      console.log(this.form)
      this.request.post("/device",this.form).then(res=>{
        if(res.data){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    reset(){
      this.devicename=""
      this.load()
    },
    //修改数据
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible=true
    },
    del(id){
      this.request.delete("/device/"+id).then(res=>{
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    delBatch(){
      let ids = this.multipleSelection.map(v=>v.id) //将数据扁平化处理，由对象转化为数组
      this.request.post("/device/del/batch",ids).then(res=>{
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val;
    },
    handleSizeChange(pageSize){
      console.log(pageSize);
      this.pageSize=pageSize;
      //当切换页码的时候，重新请求一下，由于参数已经更i性能，那就在加载时候直接变化
      this.load();
    },
    handleCurrentChange(pageNum){
      console.log(pageNum);
      this.pageNum=pageNum;
      this.load();
    },
    //新增数据
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    }
  }
}
</script>

<style scoped>

</style>
```

```java
    @PostMapping
    public Result save(@RequestBody DeviceDTO deviceDTO) {
        //将devicedto中的多个图片与本设备的id对应上
//        System.out.println("------------"+deviceDTO.getFileurl());
        System.out.println(deviceDTO);
        Device device = new Device();
        deviceDTO = deviceService.owner(deviceDTO);//将设备拥有者与老师绑定
        //通过deviceDto查询出对应的device
        BeanUtil.copyProperties(deviceDTO,device,true);
        System.out.println(device);
        Result result = Result.success(deviceService.saveOrUpdate(device));
        deviceService.photo(deviceDTO);
        return result;
    }

```



```java
    //绑定图片与设备
    //先根据deviceDTO中fileurl数组中的url查询出对应的文件对象，然后将文件对象的deviceid赋值
    @Override
    public void photo(DeviceDTO deviceDTO) {
        Device device = getDeviceInfo(deviceDTO);
        List<String> list = deviceDTO.getFileurl();
        if(list!=null&&list.size()!=0){
            for(int i=0;i<list.size();i++){
                Files file = filesService.getFilesInfo(list.get(i));
                System.out.println("-----------------");
                file.setDeviceId(device.getId());
                //修改了之后还要把file更新到数据库里卖弄
                filesService.saveOrUpdate(file);
                System.out.println(file);
            }
        }
    }
```

xml数据库查询

```xml
<!--    返回结果集合，id 是唯一标识，type 是结果类型-->
    <resultMap id="DeviceDTOMap" type="com.mu.controller.dto.DeviceDTO">
        <id column="id" jdbcType="INTEGER"  property="id"/>
        <result column="device_name" jdbcType="VARCHAR" property="deviceName"/>
        <result column="price" jdbcType="VARCHAR" property="price"/>
        <result column="owner_id" jdbcType="INTEGER" property="ownerId"/>
        <result column="place" jdbcType="VARCHAR" property="place"/>
       <!--一对一查询-->
        <association property="ownerName" javaType="string" select="selectTheOwnerName" column="owner_id">

        </association>
        <!--实现连表查询 property 是实体对象，id 是传递的参数，select 是查询语句-->
        <collection property="filesList"  javaType="ArrayList" ofType="com.mu.entity.Files" select="selectAllList" column="id">
<!--            <id column="id" property="id" jdbcType="INTEGER"/>-->
<!--            <result column="device_id" property="deviceId" jdbcType="INTEGER"/>-->
<!--            <result column="url" property="url" jdbcType="VARCHAR"/>-->
        </collection>
    </resultMap>

    <select  id="pageAllData" resultMap="DeviceDTOMap">
        select
        DISTINCT mu.sys_device.*
        from
        mu.sys_device
        left join mu.sys_file on mu.sys_file.device_id = mu.sys_device.id
        <where>
            <if test="deviceName!=null">
                device_name like concat('%',#{deviceName},'%')
            </if>
        </where>
    </select>

<!--    子查询-->
<!--    子查询的resultMap-->
    <resultMap id="filesMap" type="com.mu.entity.Files">
    </resultMap>

    <select id="selectAllList" resultMap="filesMap">
        select * from mu.sys_file where mu.sys_file.device_id=#{id}
    </select>
```

## 重写分页查询

```java
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String devicename){

//        IPage<DeviceDTO> page = new Page<>(pageNum,pageSize);
//        QueryWrapper<Device> objectQueryWrapper = new QueryWrapper<>();
//        if(!"".equals(devicename)){
//            objectQueryWrapper.like("device_name",devicename);
//        }
//        //获取当前用户信息
//        User currentUser = TokenUtils.getCurrentUser();
////        System.out.println("===>"+currentUser.getAccount());
//
//        objectQueryWrapper.orderByDesc("id");
//        return Result.success(deviceService.page(new Page<>(pageNum,pageSize),objectQueryWrapper));
        Result result = deviceService.pageAllData(pageNum,pageSize,devicename);
        return result;
    }




    //多表查询所有设备信息
    @Override
    public Result pageAllData(Integer pageNum, Integer pageSize, String devicename) {
        IPage<DeviceDTO> page = new Page<>(pageNum,pageSize);

//        QueryWrapper<De> objectQueryWrapper = new QueryWrapper<>();
//        if(!"".equals(devicename)){
//            objectQueryWrapper.like("device_name",devicename);
//        }
//        //获取当前用户信息
////        User currentUser = TokenUtils.getCurrentUser();
//
//        objectQueryWrapper.orderByDesc("id");
        Result result =  Result.success(deviceMapper.pageAllData(page,devicename));
//        return Result.success(deviceService.page(new Page<>(pageNum,pageSize),objectQueryWrapper));
        System.out.println("-------------------------");
        System.out.println(result);
        return result;
    }



@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    IPage<DeviceDTO> pageAllData(IPage<DeviceDTO> page, @Param("deviceName") String devicename);
}


  <resultMap id="DeviceDTOMap" type="com.mu.controller.dto.DeviceDTO">
        <id column="id" jdbcType="INTEGER"  property="id"/>
        <result column="device_name" jdbcType="VARCHAR" property="deviceName"/>
        <result column="price" jdbcType="VARCHAR" property="price"/>
        <result column="owner_id" jdbcType="INTEGER" property="ownerId"/>
        <result column="place" jdbcType="VARCHAR" property="place"/>
       <!--一对一查询-->
        <association property="ownerName" javaType="string" select="selectTheOwnerName" column="owner_id">

        </association>
        <!--实现连表查询 property 是实体对象，id 是传递的参数，select 是查询语句-->
        <collection property="filesList"  javaType="ArrayList" ofType="com.mu.entity.Files" select="selectAllList" column="id">
<!--            <id column="id" property="id" jdbcType="INTEGER"/>-->
<!--            <result column="device_id" property="deviceId" jdbcType="INTEGER"/>-->
<!--            <result column="url" property="url" jdbcType="VARCHAR"/>-->
        </collection>
    </resultMap>

    <select  id="pageAllData" resultMap="DeviceDTOMap">
        select
        DISTINCT mu.sys_device.*
        from
        mu.sys_device
        left join mu.sys_file on mu.sys_file.device_id = mu.sys_device.id
        <where>
            <if test="deviceName!=null">
                device_name like concat('%',#{deviceName},'%')
            </if>
        </where>
    </select>

<!--    子查询-->
<!--    子查询的resultMap-->
    <resultMap id="filesMap" type="com.mu.entity.Files">
    </resultMap>

    <select id="selectAllList" resultMap="filesMap">
        select * from mu.sys_file where mu.sys_file.device_id=#{id}
    </select>

    <select id="selectTheOwnerName" resultType="string">
        select mu.sys_user.username from mu.sys_user where  mu.sys_user.id = #{ownerId}
    </select>



```





## 借用模块

## 权限管理，菜单管理

树形列表管理，菜单的父子级关系

menu类中，添加一个孩子字段

```java
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String path;

    private String icon;

    private String description;

    @TableField(exist = false)
    private List<Menu> children;

    private Integer pid;

}

```

controller代码 

```java
//通过findAll查询
    @GetMapping
    public Result findAll() {
        //查询所有数据
        List<Menu> list = menuService.list();
        //找出pid为null的一级菜单
        List<Menu> parentNode = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for(Menu menu : parentNode){
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return Result.success(parentNode);
    }

```



角色管理界面分配权限

```vue
    selectMenu(roleId) {
      this.menuDialogVis = true;

      //请求菜单数据
      this.request.get("/menu").then(res=>{
        console.log(res)
        this.menuData=res.data
      })
    },
```

## 设置图标

可以新建一个表，类似于字典，存放图标的名字，然后直接使用

```java
    @GetMapping("/icons")
    public Result getIcons() {
        //根据类型查询
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }
```

## 把当前角色和菜单的关系保存到后台

这个表要设置两个主键，因为roleid可能是重复的，绑定多个菜单，所以要设置成联合主键

rolecontroller

```java
    /**
     * 绑定角色和菜单关系
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

    /**
     * 请求返回已经选中的选项数据
     * @param roleId
     * @return
     */
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success(roleService.getRoleMenu(roleId));
    }
```

roleservice

```java
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    //先删后增，先删除已经绑定的
    @Resource
    private RoleMenuMapper roleMenuMapper;

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

```

