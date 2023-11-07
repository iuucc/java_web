package com.dtks.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dtks.common.Constants;
import com.dtks.common.Result;
import com.dtks.controller.dto.UserDto;
import com.dtks.controller.dto.UserPasswordDTO;
import com.dtks.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.dtks.service.IUserService;
import com.dtks.entity.User;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jianghui
 * @since 2023-10-03
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    //新增或者更新
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        String username=userDto.getUsername();
        String password=userDto.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
    }
    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        String username=userDto.getUsername();
        String password=userDto.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(userService.register(userDto));
    }
    //新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id)) ;
    }

    //更新密码
    @PostMapping("/password")
    public Result updataPassword(@RequestBody UserPasswordDTO userPasswordDTO) {
      userService.updatePassword(userPasswordDTO);
      return Result.success();
    }
    //删除所有的选中
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids)) ;
    }
    //查询所有
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list()) ;
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id)) ;
    }

    //根据id查询
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper)) ;
    }
    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(defaultValue = "") String username, @RequestParam(defaultValue = "") String email, @RequestParam(defaultValue = "") String address) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queeryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queeryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queeryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queeryWrapper.like("address", address);
        }
        queeryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queeryWrapper)) ;
    }
    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        //从数据库查询出所有数据
        List<User> list=userService.list();
        //通过工具类创建writer写出到磁盘
        ExcelWriter writer= ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username","用户名");
//        writer.addHeaderAlias("password","密码");
//        writer.addHeaderAlias("nickname","昵称");
//        writer.addHeaderAlias("email","邮箱");
//        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("address","地址");
//        writer.addHeaderAlias("createTime","创建时间");
//        writer.addHeaderAlias("avatarUrl","头像");
        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName= URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename"+fileName+".xlsx");
        ServletOutputStream out=response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws  Exception{
        InputStream inputStream=file.getInputStream();
        ExcelReader reader=ExcelUtil.getReader(inputStream);
        List<User> list=reader.readAll(User.class);
        userService.saveBatch(list);
        return Result.success(true) ;
    }

}

