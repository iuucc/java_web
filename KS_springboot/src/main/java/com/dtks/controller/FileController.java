package com.dtks.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.common.Result;
import com.dtks.entity.Files;
import com.dtks.entity.User;
import com.dtks.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("file")
public class FileController {
    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Resource
    private FileMapper fileMapper;
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size=file.getSize();
        //定义一个文件的唯一标识码
        String uuid= IdUtil.fastSimpleUUID();
        String fileUUID=uuid+ StrUtil.DOT+type;
        //存储到硬盘
        File uploadFile=new File(fileUploadPath+fileUUID);
        File ParentFile=uploadFile.getParentFile();
        //判断文件目录是否存在，不存在则重新创建一个
        if(!ParentFile.exists()){
            ParentFile.mkdirs();
        }

        String url;
        file.transferTo(uploadFile);
        String md5=SecureUtil.md5(uploadFile);
        Files dbFiles = getFileByMd5(md5);
            if(dbFiles!=null){
                url=dbFiles.getUrl();
                uploadFile.delete();
            }else{
                url="http://localhost:9090/file/"+fileUUID;
            }
        Files saveFile=new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);
        return url;
    }
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        //根据文件的唯一标识获取文件
        File uploadFile=new File(fileUploadPath+fileUUID);
        //设置输出流格式
        ServletOutputStream os=response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");
        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }
    private Files getFileByMd5(String md5){
        QueryWrapper<Files> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }
    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files=fileMapper.selectById(id);
        files.setIsDelete(true);
        return Result.success(fileMapper.updateById(files)) ;
    }

    //删除所有的选中
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        QueryWrapper<Files> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files=fileMapper.selectList(queryWrapper);
        for(Files file:files){
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success() ;
    }


    //新增或者更新
    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        return Result.success(fileMapper.updateById(files));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String name) {
        QueryWrapper<Files> queeryWrapper = new QueryWrapper<>();
        queeryWrapper.eq("is_delete",false);
        queeryWrapper.orderByDesc("id");
        if (!"".equals(name)) {
            queeryWrapper.like("name", name);
        }
        return Result.success(fileMapper.selectPage(new Page<>(pageNum,pageSize),queeryWrapper)) ;
    }
}
