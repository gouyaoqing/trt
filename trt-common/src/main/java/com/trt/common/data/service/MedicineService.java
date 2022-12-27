package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Medicine;
import com.trt.common.data.model.query.QMedicine;

import java.util.List;
import java.util.Map;

public interface MedicineService {
    int getOrInsert(Medicine medicine) throws BusinessException;

    List<Medicine> findAll();

    int updateSameField(Medicine medicine);

    List<Medicine> query(QMedicine query, Integer limit);

    Map<String, List<String>> queryCategory();

    List<String> queryHuanCai();
}
