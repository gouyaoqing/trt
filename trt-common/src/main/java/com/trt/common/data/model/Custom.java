package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("custom")
/**
 * 客户
 */
public class Custom {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 客户代码
     */
    private String code;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 客户大区
     */
    private String area;
    /**
     * 客户省份
     */
    private String province;
    /**
     * 客户城市
     */
    private String city;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
