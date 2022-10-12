package com.trt.common.data.model;

import com.baomidou.mybatisplus.annotation.IdType;
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
}
