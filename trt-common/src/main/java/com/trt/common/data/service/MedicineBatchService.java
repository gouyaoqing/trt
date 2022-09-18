package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.MedicineBatch;

public interface MedicineBatchService {
    int getOrInsert(MedicineBatch medicineBatch) throws BusinessException;
}
