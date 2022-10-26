package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.CustomMapper;
import com.trt.common.data.model.Custom;
import com.trt.common.data.service.CustomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomServiceImpl implements CustomService {
    @Resource
    private CustomMapper customMapper;

    @Override
    public int getOrInsert(Custom custom) throws BusinessException {
        if (StringUtils.isBlank(custom.getCode())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "custom's code is not null");
        }

        QueryWrapper<Custom> wrapper = new QueryWrapper<Custom>(new Custom().setCode(custom.getCode()));
        Custom dbCustom = customMapper.selectOne(wrapper);
        if (dbCustom != null) {
            custom.setId(dbCustom.getId());

            if (custom.getBusinessType() != null) {
                UpdateWrapper<Custom> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", custom.getId());
                updateWrapper.set("business_type", custom.getBusinessType());
                customMapper.update(dbCustom, updateWrapper);
            }
            return 1;
        }

        return customMapper.insert(custom);
    }

    @Override
    public List<Custom> findAll() {
        QueryWrapper<Custom> wrapper = new QueryWrapper<>();
        return customMapper.selectList(wrapper);
    }

    @Override
    public int updateExcelInfo(Custom custom) {
        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("industry", custom.getIndustry());
        wrapper.set("society_code", custom.getSocietyCode());
        wrapper.set("register_code", custom.getRegisterCode());
        wrapper.set("organization_code", custom.getOrganizationCode());
        wrapper.set("address", custom.getAddress());
        wrapper.set("taxes_code", custom.getTaxesCode());

        return customMapper.update(custom, wrapper);
    }

    @Override
    public int updateBusinessTypeById(Custom custom) {
        if (custom.getId() == null) {
            return 0;
        }
        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("business_type", custom.getBusinessType());

        return customMapper.update(custom, wrapper);
    }

    @Override
    public int updateGroupCompanyId(Custom custom) {
        if (custom.getId() == null) {
            return 0;
        }
        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("group_company_id", custom.getGroupCompanyId());

        return customMapper.update(custom, wrapper);
    }
}
