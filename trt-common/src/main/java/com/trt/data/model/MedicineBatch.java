package com.trt.data.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors
/**
 * 药品批号
 */
public class MedicineBatch {
    /**
     * id
     */
    private Long id;
    /**
     * 药品id
     */
    private Long medicineId;
    /**
     * 药品批号
     */
    private String lotNumber;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
}
