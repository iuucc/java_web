package com.dtks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dtks.common.Result;
import com.dtks.entity.Commodity;
import com.dtks.entity.User;
import com.dtks.mapper.CommodityMapper;
import com.dtks.mapper.UserMapper;
import com.dtks.service.ICommodityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-08
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {
    @Resource
    private CommodityMapper commodityMapper;
    @Override
    public void updateInventory(Integer id, Integer number) {
        commodityMapper.updateInventory(id,number);
    }
}
