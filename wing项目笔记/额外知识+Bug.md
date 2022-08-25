# 额外知识

## 负载均衡

当一台服务器的性能达到极限时，我们可以使用服务器集群来提高网站的整体性能。那么，在服务器集群中，需要有一台服务器充当调度者的角色，用户的所有请求都会首先由它接收，调度者再根据每台服务器的负载情况将请求分配给某一台后端服务器去处理。

### DNS



数据包采用IP地址在网络中传播，而为了方便用户记忆，我们使用域名来访问网站。那么，我们通过域名访问网站之前，首先需要将域名解析成IP地址，这个工作是由DNS完成的，也就是域名服务器。

 我们提交的请求不会直接发送给想要访问的网站，而是首先发给域名服务器，它会帮我们把域名解析成IP地址并返回给我们。我们收到IP之后才会向该IP发起请求。

 那么，DNS服务器有一个天然的优势，如果一个域名指向了多个IP地址，那么每次进行域名解析时，DNS只要选一个IP返回给用户，就能够实现服务器集群的负载均衡。 

## 文件上传

```java
package com.mu.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mu.entity.Files;
import com.mu.mapper.FilesMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FilesController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FilesMapper fileMapper;

    /**
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        //得到上传时候的文件名
        String originalFilename =  file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //先存储到磁盘
        File uploadParentFile = new File(fileUploadPath);
        //如果目录不存在就创建
        if(!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }
        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid+ StrUtil.DOT+type;
        File uploadFile = new File(fileUploadPath+fileUUID);
        //把获取到的文件存储到磁盘目录
        file.transferTo(uploadFile);

        String url = "http://localhost:9090/file/"+fileUUID;
        //存储数据库
        Files saveFile  = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size);
        saveFile.setUrl(url);
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 下载接口
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {//传进来的是一个标示位
        //根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath+fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

}

```

## MD5防止文件重复上传

```java
   /**
     * 通过文件的MD5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5){
        //查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList  = filesMapper.selectList(queryWrapper);
        return filesList.size()==0?null:filesList.get(0);
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        //得到上传时候的文件名
        String originalFilename =  file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid+ StrUtil.DOT+type;

        File uploadFile = new File(fileUploadPath+fileUUID);
        //如果目录不存在就创建
        if(!uploadFile.getParentFile().exists()){
            uploadFile.getParentFile().mkdirs();
        }

        String url;
        //获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        //从数据库查询是否存在相同记录
        Files dbFiles = getFileByMd5(md5);
        if(dbFiles!=null){
            url = dbFiles.getUrl();
        }else{
            //上传文件到磁盘
            file.transferTo(uploadFile);
            //数据库若不存在重复文件，则不删除
            url = "http://localhost:9090/file/"+fileUUID;
        }


        //存储数据库
        Files saveFile  = new Files();

        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        filesMapper.insert(saveFile);
        return url;
    }

```

## 数据库下划线与驼峰命名映射

```yml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
```

## async/await

## promise

[(186条消息) Promise面试题汇总_CUG-GZ的博客-CSDN博客_promise面试题](https://blog.csdn.net/qq_42033567/article/details/108129645?ops_request_misc=%7B%22request%5Fid%22%3A%22165958639716782388080291%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=165958639716782388080291&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-2-108129645-null-null.142^v39^pc_rank_v37&utm_term=promise&spm=1018.2226.3001.4187)

## 多表查询

### 笛卡儿积

## 自连接

### 查询学生及其所属老师的名字

```sql
select a.username , b.username  from mu.sys_user a ,mu.sys_user b where a.teacher_id = b.id;
```

```
Result result = deviceService.pageAll(pageNum,pageSize,devicename);
return result;
```

## Mybatisplus中service和mapper

[关于mybatis-plus中Service和Mapper的分析 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/114451975)

## Echarts 数据可视化

`npm i echarts -S`

折线图

柱状图

饼图

```vue
<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px ; height: 400px"></div>
      </el-col>
      <el-col :span="12">
        <div id="pai" style="width: 500px ; height: 400px"></div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
      return {

      }
  },
  mounted() { //页面元素渲染之后再触发
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;

    option = {
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'line'
        } ,
        {
          data: [150, 230, 224, 218, 135, 147, 260],
          type: 'bar'
        }
      ]
    };
    myChart.setOption(option);

    //饼图
    var paioption = {
      legend: {
        top: 'bottom'
      },
      toolbox: {
        show: true,
        feature: {
          mark: { show: true },
          dataView: { show: true, readOnly: false },
          restore: { show: true },
          saveAsImage: { show: true }
        }
      },
      series: [
        {
          name: 'Nightingale Chart',
          type: 'pie',
          radius: [50, 130],
          center: ['50%', '50%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 8
          },
          data: [
            { value: 40, name: 'rose 1' },
            { value: 38, name: 'rose 2' },
            { value: 32, name: 'rose 3' },
            { value: 30, name: 'rose 4' },
            { value: 28, name: 'rose 5' },
            { value: 26, name: 'rose 6' },
            { value: 22, name: 'rose 7' },
            { value: 18, name: 'rose 8' }
          ]
        }
      ]
    };
    var paichartDom = document.getElementById('pai');
    var paimyChart = echarts.init(paichartDom);
    paimyChart.setOption(paioption)
  }
}
</script>

<style scoped>

</style>
```

## echarts传入动态数据

vue

```vue
  mounted() { //页面元素渲染之后再触发
    var option = {
      xAxis: {
        type: 'category',
        data: []
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        } ,
        {
          data: [],
          type: 'bar'
        }
      ]
    };

var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    //请求数据
    this.request.get("/echarts/example").then(res => {
      //填空
      option.xAxis.data = res.data.x
      option.series[0].data = res.data.y
      option.series[1].data = res.data.y
      //必须再数据准备完毕之后set
      myChart.setOption(option);
    })
```

EchartsController

```java
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @GetMapping("/example")
    public Result get() {
        Map<String,Object> map = new HashMap<>();//两个轴，用map来表示
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y",CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }
}
```

## echarts查数据库数据

```
//vue
    this.request.get("/echarts/members").then(res => {
      //填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data
      //必须再数据准备完毕之后set
      myChart.setOption(option);


      paioption.series[0].data=[
        {name:"第一季度",value:res.data[0]},
        {name:"第二季度",value:res.data[1]},
        {name:"第三季度",value:res.data[2]},
        {name:"第四季度",value:res.data[3]},
      ]
      paimyChart.setOption(paioption)
    })
    
        @GetMapping("/members")
    public Result members(){
        List<User> list = userService.list();
        int q1 = 0;//第一季度
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;
        for(User user : list){
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1:q1+=1;break;
                case Q2:q2+=1;break;
                case Q3:q3+=1;break;
                case Q4:q4+=1;break;
                default:break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
    
```

## queryWrapper使用方法

[(187条消息) QueryWrapper方法解释_juanjuanya.的博客-CSDN博客_querywrapper小于等于](https://blog.csdn.net/m0_60536467/article/details/123501984?ops_request_misc=%7B%22request%5Fid%22%3A%22166003255016782388092217%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=166003255016782388092217&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_click~default-1-123501984-null-null.142^v39^pc_rank_v37,185^v2^control&utm_term=queryWrapper.orderByDesc&spm=1018.2226.3001.4449)

# BUG

## [mybatis](https://so.csdn.net/so/search?q=mybatis&spm=1001.2101.3001.7020) 异常：Parameter 'id' not found. Available parameters are

刚在controller重写了分页查询方法，之前写的新增方法报错，后来发现是由于重写了selectAll和PageAll方法，将方法名改下就可以

#### 修改后代码

```xml
<select id="pageAllData" resultMap="DeviceDTOMap">
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

```

```java
        Result result = deviceService.pageAllData(pageNum,pageSize,devicename);
        return result;
```

## java.lang.NullPointerException: null

```java
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
```

## SQLFeatureNotSupportedException: null

mp和druid不兼容版本

[(190条消息) 解决SQLFeatureNotSupportedException异常_乐hh的博客-CSDN博客_sqlfeaturenotsupportedexception](https://blog.csdn.net/lqbz456/article/details/103388759?ops_request_misc=%7B%22request%5Fid%22%3A%22166053789316782388089696%22%2C%22scm%22%3A%2220140713.130102334..%22%7D&request_id=166053789316782388089696&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-1-103388759-null-null.142^v40^pc_search_integral,185^v2^control&utm_term=.SQLFeatureNotSupportedException%3A null&spm=1018.2226.3001.4187)

## 循环依赖问题

[spring：我是如何解决循环依赖的？ - 腾讯云开发者社区-腾讯云 (tencent.com)](https://cloud.tencent.com/developer/article/1769948)

## Ambiguous @ExceptionHandler method mapped for [class 报错

原因

```JAVA
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



}

```

中exceptionHandler中重复异常



![image-20220822113557643](https://cdn.jsdelivr.net/gh/hughmum/blogImage@main/img/image-20220822113557643.png)
