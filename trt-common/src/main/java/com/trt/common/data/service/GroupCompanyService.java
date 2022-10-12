package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.GroupCompany;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
public interface GroupCompanyService {
    int getOrInsert(GroupCompany groupCompany) throws BusinessException;
}
