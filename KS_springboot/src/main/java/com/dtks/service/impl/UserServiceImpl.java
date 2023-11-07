package com.dtks.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtks.common.Constants;
import com.dtks.controller.dto.UserDto;
import com.dtks.controller.dto.UserPasswordDTO;
import com.dtks.entity.Menu;
import com.dtks.entity.User;
import com.dtks.exception.ServiceException;
import com.dtks.mapper.RoleMapper;
import com.dtks.mapper.RoleMenuMapper;
import com.dtks.mapper.UserMapper;
import com.dtks.service.IMenuService;
import com.dtks.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dtks.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.serializer.SerializerException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Log LOG=Log.get();
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;
    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if(one!=null){
            BeanUtil.copyProperties(one,userDto,true);
            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);
            String role=one.getRole();
            List<Menu> roleMenus=getRoleMenus(role);
            userDto.setMenus(roleMenus);
            return  userDto;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }

    @Override
    public User register(UserDto userDto) {
        User one=getUserInfo(userDto);
        if(one==null){
            one=new User();
            BeanUtil.copyProperties(userDto,one,true);
            save(one);
        }else {
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update=userMapper.updatePassword(userPasswordDTO);
        if(update<1){
            throw new ServiceException(Constants.CODE_600,"密码错误");
        }
    }

    @Override
    public Object getByUsername(String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User one;
        one=getOne(queryWrapper);
        return one;
    }

    private User getUserInfo(UserDto userDto){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username",userDto.getUsername());
        queryWrapper.eq("password",userDto.getPassword());
        User one;
        try{
            one=getOne(queryWrapper);
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }
    private List<Menu>  getRoleMenus(String roleFlag){
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        List<Integer> menuIds=roleMenuMapper.selectByRoleId(roleId);
        List<Menu> roleMenus=new ArrayList<>();
        List<Menu> menus =menuService.findMenus("");
        for (Menu menu : menus){
        if (menuIds.contains(menu.getId())) {
            roleMenus.add(menu);
        }
        List<Menu> children = menu.getChildren();
        children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }

}
