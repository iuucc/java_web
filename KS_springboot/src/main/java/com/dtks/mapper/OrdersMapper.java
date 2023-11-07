package com.dtks.mapper;

import com.dtks.controller.dto.UserPasswordDTO;
import com.dtks.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jianghui
 * @since 2023-10-09
 */
public interface OrdersMapper extends BaseMapper<Orders> {
    @Update("update sys_orders set paid =#{paid} , paytime=#{formattedDate} where order_number=#{id} ")
    int updataPaid(Integer id,String paid,String formattedDate);

    @Update("update sys_orders set delivery =#{name} , deliverytime=#{formattedDate} where order_number=#{id} ")
    int updataDelivery(Integer id,String name,String formattedDate);

    @Update("update sys_orders set delivery =#{name} , receipttime=#{formattedDate} where order_number=#{id} ")
    int updataReceipttime(Integer id,String name,String formattedDate);

    @Delete("DELETE from sys_orders where order_number = #{id}")
    int deleteByOrderNumber(Integer id);
}
