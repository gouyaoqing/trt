package com.trt.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SaleDetail {
    /**
     * id
     */
    private Long id;
    /**
     * 经销商id
     */
    private Long dealerId;
    /**
     * 客户id
     */
    private Long CustomId;
    /**
     * 药品批次id
     */
    private Long medicineBatchId;
    /**
     * 销售日期
     */
    private Date saleDate;
    /**
     * 销售数量
     */
    private Integer saleNum;
    /**
     * 销售单价
     */
    private Double salePrice;
    /**
     * 销售金额
     */
    private Float saleAmount;
    /**
     * 销售金额 两位小数
     */
    private Double saleAmountDouble;
    /**
     * 最小单位
     */
    private String minUnit;
    /**
     * 最小数量
     */
    private Integer minNum;
    /**
     * 件数
     */
    private Double packageNum;
    /**
     * 最小单价
     */
    private Double minPrice;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
