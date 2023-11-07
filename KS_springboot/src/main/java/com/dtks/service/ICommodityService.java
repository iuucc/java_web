package com.dtks.service;

import com.dtks.entity.Commodity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-08
 */
public interface ICommodityService extends IService<Commodity> {

    void updateInventory(Integer id, Integer number);
}
