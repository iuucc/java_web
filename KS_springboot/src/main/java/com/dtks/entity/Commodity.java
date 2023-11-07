package com.dtks.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2023-10-08
 */
@Getter
@Setter
@TableName("sys_commodity")
@ApiModel(value = "Commodity对象", description = "")
public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String tradename;
    private String description;
    private BigDecimal price;
    private String type;
    private Integer inventory;
    private Boolean enable;
    private String image;
    private Integer userid;
    private String sellername;
}
