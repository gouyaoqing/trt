package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 连锁店
 *
 * @author gouyaoqing
 * @since 2022-10-12
 */
@Data
@Accessors(chain = true)
@TableName("group_company")
public class GroupCompany {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 公司名称
     */
    private String name;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否是红军联盟
     */
    private Boolean hongJun;
    /**
     * 全国百强
     */
    @TableField("country_top_100")
    private Boolean countryTop100;
    /**
     * 重点19家
     */
    @TableField("important_19")
    private Boolean important19;
    /**
     * 县域百强
     */
    @TableField("county_top_100")
    private Boolean countyTop100;

}
