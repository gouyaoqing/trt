package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Medicine;

public interface MedicineService {
    int getOrInsert(Medicine medicine) throws BusinessException;
}
