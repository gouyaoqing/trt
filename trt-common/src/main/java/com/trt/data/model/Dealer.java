package com.trt.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
/**
 *  经销商
 */
public class Dealer {
    /**
     * id
     */
    private Long id;
    /**
     * 经销商代码
     */
    private String code;
    /**
     * 经销商名称
     */
    private String name;
    /**
     * 经销商大区
     */
    private String area;
    /**
     * 经销商省份
     */
    private String province;
    /**
     * 经销商城市
     */
    private String city;
    /**
     * 经销商级别
     */
    private Integer level;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
