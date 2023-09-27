package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.SubGroupCompany;

import java.util.List;

public interface SubGroupCompanyService {
    int getOrInsert(SubGroupCompany subGroupCompany) throws BusinessException;

    List<SubGroupCompany> findAll();

    int getOrInsertOrUpdate(SubGroupCompany subGroupCompany) throws BusinessException;
}
