package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.SubGroupCompany;

public interface SubGroupCompanyService {
    int getOrInsert(SubGroupCompany subGroupCompany) throws BusinessException;
}
