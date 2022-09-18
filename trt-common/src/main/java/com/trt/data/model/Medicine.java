package com.trt.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
/**
 * 药品
 */
public class Medicine {
    /**
     * id
     */
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
}
