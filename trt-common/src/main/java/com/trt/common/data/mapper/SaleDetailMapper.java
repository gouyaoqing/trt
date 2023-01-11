package com.trt.common.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trt.common.data.model.SaleDetail;
import com.trt.common.data.model.api.SaleDetailSumResult;
import com.trt.common.data.model.query.QSaleDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SaleDetailMapper extends BaseMapper<SaleDetail> {
    SaleDetailSumResult querySaleDetailTotal(@Param("query") QSaleDetail query);
}
