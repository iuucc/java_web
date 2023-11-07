package com.dtks.service;

import com.dtks.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-06
 */
public interface IMenuService extends IService<Menu> {



    List<Menu> findMenus(String name);
}
