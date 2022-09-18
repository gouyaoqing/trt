package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.SaleDetail;

public interface SaleDetailService {
    int insert(SaleDetail saleDetail) throws BusinessException;
}
