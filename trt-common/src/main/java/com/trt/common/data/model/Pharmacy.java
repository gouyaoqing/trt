package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;

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
     * 所属子连锁店id
     */
    private Long subGroupCompanyId;
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
     * 单体店，连锁店
     */
    private String groupCompanyType;
    /**
     * 院边店
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
    private String storeId;
    /**
     * 三甲医院-北京协和医院 周边重点建筑
     */
    private String aroundSpecial;
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
    /**
     * 创建时间
     */
    private Date createTime;

    private String storeArea;
    /**
     *
     */
    private Integer aroundBusinessCount;
    /**
     * 周边居民区数量
     */
    private Integer aroundCommunityCount;
    /**
     *
     */
    private Integer aroundDistanceCount;
    /**
     * 周边医院数量
     */
    private Integer aroundHospitalCount;

    private Integer aroundIndustryCount;
    /**
     * 周边商业区数量
     */
    private Integer aroundOfficeCount;

}
