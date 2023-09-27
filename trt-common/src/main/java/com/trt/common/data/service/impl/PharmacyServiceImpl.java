package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.GroupCompanyMapper;
import com.trt.common.data.mapper.PharmacyMapper;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;
import com.trt.common.data.model.SubGroupCompany;
import com.trt.common.data.model.query.QPharmacy;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.PharmacyService;
import com.trt.common.data.service.SubGroupCompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

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

    @Resource
    private SubGroupCompanyService subGroupCompanyService;

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
    public int getOrInsert(Pharmacy pharmacy, GroupCompany groupCompany, SubGroupCompany subGroupCompany) {
        if (groupCompany != null && StringUtils.isNotBlank(groupCompany.getName())) {
            groupCompanyService.getOrInsert(groupCompany);
            pharmacy.setGroupCompanyId(groupCompany.getId());
        }

        if (subGroupCompany != null && groupCompany != null && groupCompany.getId() != null && StringUtils.isNotBlank(subGroupCompany.getName())) {
            subGroupCompany.setGroupCompanyId(groupCompany.getId());
            subGroupCompanyService.getOrInsert(subGroupCompany);
            pharmacy.setSubGroupCompanyId(subGroupCompany.getId());
        }

        return getOrInsert(pharmacy);
    }

    @Override
    public List<Pharmacy> query(QPharmacy query, Integer limit) {
        if (query == null) {
            return Collections.emptyList();
        }

        QueryWrapper<Pharmacy> queryWrapper = new QueryWrapper<Pharmacy>();
        if (StringUtils.isNotBlank(query.getKeyword())) {
            queryWrapper.like("name", query.getKeyword());
        }

        if (StringUtils.isNotBlank(query.getProvince())) {
            queryWrapper.eq("province", query.getProvince());
        }

        if (StringUtils.isNotBlank(query.getCity())) {
            queryWrapper.eq("city", query.getCity());
        }

        if (StringUtils.isNotBlank(query.getDistrict())) {
            queryWrapper.eq("district", query.getDistrict());
        }

        if (StringUtils.isNotBlank(query.getStreet())) {
            queryWrapper.eq("street", query.getStreet());
        }

        if (limit != null && limit != 0) {
            queryWrapper.last(" limit " + limit);
        }

        return pharmacyMapper.selectList(queryWrapper);
    }

    @Override
    public List<Pharmacy> findAll() {
        QueryWrapper<Pharmacy> queryWrapper = new QueryWrapper<>();
        return pharmacyMapper.selectList(queryWrapper);
    }
}
