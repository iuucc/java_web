package com.dtks.service;

import com.dtks.controller.dto.UserDto;
import com.dtks.controller.dto.UserPasswordDTO;
import com.dtks.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jianghui
 * @since 2023-10-03
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);

    User register(UserDto userDto);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    Object getByUsername(String username);
}
