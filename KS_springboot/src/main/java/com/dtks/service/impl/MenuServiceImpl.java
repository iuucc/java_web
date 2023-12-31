package com.dtks.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtks.entity.Menu;
import com.dtks.mapper.MenuMapper;
import com.dtks.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-06
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> findMenus(String name) {
        QueryWrapper<Menu> queeryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(name)){
            queeryWrapper.like("name",name);
        }
        queeryWrapper.like("name",name);
        //查出所有数据
        List<Menu> list = list(queeryWrapper);
        List<Menu> parentNodes=list.stream().filter(menu -> menu.getPid()==null).collect(Collectors.toList());
        for (Menu menu : parentNodes) {
            menu.setChildren(list.stream().filter(m->menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
