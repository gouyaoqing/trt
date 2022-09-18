package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Dealer;

public interface DealerService {
    int insert(Dealer dealer) throws BusinessException;
}
