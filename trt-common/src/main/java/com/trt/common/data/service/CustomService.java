package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Custom;

import java.util.List;

public interface CustomService {
    int getOrInsert(Custom custom) throws BusinessException;

    List<Custom> findAll();

    int updateExcelInfo(Custom custom);

    int updateBusinessTypeById(Custom custom);

    int updateGroupCompanyId(Custom custom);

    int updateGroupCompanyIdNew(Custom custom);

    List<String> businessType();

    int updateNameById(Custom custom);

    int updateSocietyCodeById(Custom custom);

}
