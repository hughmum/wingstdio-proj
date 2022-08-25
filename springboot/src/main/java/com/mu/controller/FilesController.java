package com.mu.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.List;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FilesController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @Resource
    private FilesMapper filesMapper;

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
            url = "http://"+serverIp+":9090/file/"+fileUUID;
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

}
