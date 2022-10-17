package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 子连锁公司
 */
@Data
@Accessors(chain = true)
@TableName("sub_group_company")
public class SubGroupCompany {
    /**
     * id
     */
    private Long id;
    /**
     * 连锁公司id
     */
    private Long groupCompanyId;
    /**
     * 子连锁公司名称
     */
    private String name;
    /**
     * 核心省份
     */
    private String province;
    /**
     * 核心城市
     */
    private String city;
    /**
     * 门店数
     */
    private Integer storeNum;
    /**
     * 市场潜力(万元)
     */
    private Integer totalMarketPotential;
    /**
     * 门店数排名
     */
    private Integer storeNumRank;
    /**
     * 市场潜力排名
     */
    private Integer marketPotentialRank;

}
