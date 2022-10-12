package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.GroupCompanyMapper;
import com.trt.common.data.mapper.PharmacyMapper;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.PharmacyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
@Service
public class PharmacyServiceImpl implements PharmacyService {
    @Resource
    private PharmacyMapper pharmacyMapper;

    @Resource
    private GroupCompanyService groupCompanyService;

    @Override
    public int getOrInsert(Pharmacy pharmacy) {
        if (StringUtils.isBlank(pharmacy.getName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "pharmacy's name is not null");
        }

        QueryWrapper<Pharmacy> wrapper = new QueryWrapper<Pharmacy>(new Pharmacy().setName(pharmacy.getName()));
        Pharmacy dbPharmacy = pharmacyMapper.selectOne(wrapper);
        if (dbPharmacy != null) {
            pharmacy.setId(dbPharmacy.getId());
            return 1;
        }

        return pharmacyMapper.insert(pharmacy);
    }

    @Override
    public int getOrInsert(Pharmacy pharmacy, GroupCompany groupCompany) {
        if (groupCompany != null && StringUtils.isNotBlank(groupCompany.getName())) {
            groupCompanyService.getOrInsert(groupCompany);
            pharmacy.setGroupCompanyId(groupCompany.getId());
        }

        return getOrInsert(pharmacy);
    }
}
