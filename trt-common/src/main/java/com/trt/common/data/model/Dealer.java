package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("dealer")
/**
 *  经销商
 */
public class Dealer implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 经销商代码
     */
    @NotBlank
    private String code;
    /**
     * 经销商名称
     */
    @NotBlank
    private String name;
    /**
     * 经销商大区
     */
    @NotBlank
    private String area;
    /**
     * 经销商省份
     */
    @NotBlank
    private String province;
    /**
     * 经销商城市
     */
    @NotBlank
    private String city;
    /**
     * 经销商级别
     */
    @NotBlank
    private Integer level;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 统一社会信用代码
     */
    private String societyCode;
}
