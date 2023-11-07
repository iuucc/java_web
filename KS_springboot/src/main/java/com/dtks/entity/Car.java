package com.dtks.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author jianghui
 * @since 2023-10-09
 */
@Getter
@Setter
@TableName("sys_car")
@ApiModel(value = "Car对象", description = "")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer carnumber;

    private Integer fileid;

    private Integer amount;

    private LocalDateTime time;

    private Integer userId;

    private String userAddress;

    private Integer userPhone;

    private Integer total;

    private String fileName;
    private String sellername;
    private String filepiture;

}
