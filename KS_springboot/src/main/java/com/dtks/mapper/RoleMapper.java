package com.dtks.mapper;

import com.dtks.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jianghui
 * @since 2023-10-06
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select id from sys_role where flag=#{flag} ")
    Integer selectByFlag(@Param("flag") String flag);
}
