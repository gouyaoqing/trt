package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Medicine;

import java.util.List;

public interface MedicineService {
    int getOrInsert(Medicine medicine) throws BusinessException;

    List<Medicine> findAll();

    int updateSameField(Medicine medicine);

    List<Medicine> query(String keyword, Integer limit);
}
