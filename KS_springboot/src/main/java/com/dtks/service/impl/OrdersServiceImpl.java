package com.dtks.service.impl;

import com.dtks.entity.Orders;
import com.dtks.mapper.OrdersMapper;
import com.dtks.service.IOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-09
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Resource
    private OrdersMapper ordersMapper;
    @Override
    public void updataPaid(Integer id, String paid, String formattedDate) {
        ordersMapper.updataPaid(id,paid,formattedDate);
    }

    @Override
    public void updataDelivery(Integer id, String name, String formattedDate) {
        ordersMapper.updataDelivery(id,name,formattedDate);
    }

    @Override
    public void removeByOrderNumber(Integer id) {
            ordersMapper.deleteByOrderNumber(id);
    }

    @Override
    public void updataReceipttime(Integer id, String name, String formattedDate) {
   ordersMapper.updataReceipttime(id,name,formattedDate);
    }
}
