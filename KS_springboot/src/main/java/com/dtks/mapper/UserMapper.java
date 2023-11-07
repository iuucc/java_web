package com.dtks.mapper;

import com.dtks.controller.dto.UserPasswordDTO;
import com.dtks.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jianghui
 * @since 2023-10-03
 */
public interface UserMapper extends BaseMapper<User> {
    @Update("update sys_user set password =#{newpassword} where username=#{username} and password=#{password}")
    int updatePassword(UserPasswordDTO userPasswordDTO);
}
