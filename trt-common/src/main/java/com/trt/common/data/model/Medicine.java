package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("medicine")
/**
 * 药品
 */
public class Medicine {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 药品代码
     */
    private String code;
    /**
     * 药品名称
     */
    private String name;
    /**
     * 药品规格
     */
    private String specification;
    /**
     * 原始规格
     */
    private String initSpecification;
    /**
     * 单位
     */
    private String unit;
    /**
     * 药品所属部门
     */
    private String department;
    /**
     * 包装率
     */
    private Integer packingPcs;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 公司级code
     */
    private String companyCode;
    /**
     * 别名
     */
    private String alias;
    /**
     * 分类1
     */
    private String category1;
    /**
     * 分类2
     */
    private String category2;
    /**
     * 描述
     */
    private String description;
    /**
     * 幻彩行动
     */
    private String huanCai;
    /**
     * 御药三百年
     */
    @TableField("yu_yao_300")
    private Boolean yuYao300;

    /**
     * 防疫用药
     */
    private Boolean fangYi;
    /**
     * 名称规格
     */
    private String nameSpecification;
}
