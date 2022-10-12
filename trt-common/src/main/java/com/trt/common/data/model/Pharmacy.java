package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 药店
 *
 * @author gouyaoqing
 * @since 2022-10-12
 */
@Data
@Accessors(chain = true)
@TableName("pharmacy")
public class Pharmacy {
    /**
     * id
     */
    private Long id;
    /**
     * 所属连锁店id
     */
    private Long groupCompanyId;
    /**
     * 药店名称
     */
    @NotBlank
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 连锁店、单体店
     */
    private String type;
    /**
     * 所属区域
     */
    private String area;
    /**
     * 城市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 商品数量
     */
    private Long popNum;
    /**
     * 省
     */
    private String province;
    /**
     * 药店通的药店id
     */
    private String storyId;
    /**
     * 三甲医院-北京协和医院 不知道是啥
     */
    private String special;
    /**
     * 级别
     */
    private String level;
    /**
     * 电话
     */
    private String telephone;
    /**
     * 街道
     */
    private String street;
}
