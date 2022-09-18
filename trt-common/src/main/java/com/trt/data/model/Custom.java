package com.trt.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors
/**
 * 客户
 */
public class Custom {
    /**
     * id
     */
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
