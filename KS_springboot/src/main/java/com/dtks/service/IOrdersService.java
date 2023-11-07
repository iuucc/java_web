package com.dtks.service;

import com.dtks.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-09
 */
public interface IOrdersService extends IService<Orders> {

    void updataPaid(Integer id, String paid, String formattedDate);

    void updataDelivery(Integer id, String name, String formattedDate);

    void removeByOrderNumber(Integer id);

    void updataReceipttime(Integer id, String name, String formattedDate);
}
