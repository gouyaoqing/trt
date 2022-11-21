package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.GroupCompany;

import java.util.List;
import java.util.Optional;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
public interface GroupCompanyService {
    int getOrInsert(GroupCompany groupCompany) throws BusinessException;

    List<GroupCompany> findAll();

    int updateHongJun(GroupCompany groupCompany);

    Optional<GroupCompany> findByName(String name);

    int updateBooleanLabel(GroupCompany groupCompany);

    int updateCity(GroupCompany groupCompany);
}
