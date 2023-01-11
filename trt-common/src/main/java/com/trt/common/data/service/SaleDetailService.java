package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.SaleDetail;
import com.trt.common.data.model.api.SaleDetailSumResult;
import com.trt.common.data.model.query.QSaleDetail;

public interface SaleDetailService {
    int insert(SaleDetail saleDetail) throws BusinessException;

    int deleteByExcelName(String excelName);

    int updateCustomId(Long oldCustomId, Long newCustomId);

    SaleDetailSumResult querySaleDetailTotal(QSaleDetail query);
}
