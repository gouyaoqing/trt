package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Custom;

public interface CustomService {
    int getOrInsert(Custom custom) throws BusinessException;
}
