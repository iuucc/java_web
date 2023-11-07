package com.dtks.mapper;

import com.dtks.controller.dto.UserPasswordDTO;
import com.dtks.entity.Commodity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jianghui
 * @since 2023-10-08
 */
public interface CommodityMapper extends BaseMapper<Commodity> {
    @Update("update sys_commodity set inventory =#{number} where id=#{id}")
    int updateInventory(Integer id, Integer number);
}
