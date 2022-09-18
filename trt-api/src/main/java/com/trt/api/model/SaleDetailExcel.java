package com.trt.api.model;

import com.trt.common.data.model.Custom;
import com.trt.common.data.model.Dealer;
import com.trt.common.data.model.Medicine;
import com.trt.common.data.model.MedicineBatch;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SaleDetailExcel {
    /**
     * 经销商信息
     */
    private Dealer dealer;
    /**
     * 客户信息
     */
    private Custom custom;
    /**
     * 药品信息
     */
    private Medicine medicine;
    /**
     * 药品批次信息
     */
    private MedicineBatch medicineBatch;
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
    private Double saleAmount;
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
}
