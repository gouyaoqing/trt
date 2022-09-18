package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
}
