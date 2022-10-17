package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.SubGroupCompanyMapper;
import com.trt.common.data.model.SubGroupCompany;
import com.trt.common.data.service.SubGroupCompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
@Service
public class SubGroupCompanyServiceImpl implements SubGroupCompanyService {
    @Resource
    private SubGroupCompanyMapper subGroupCompanyMapper;

    @Override
    public int getOrInsert(SubGroupCompany subGroupCompany) throws BusinessException {
        if (StringUtils.isBlank(subGroupCompany.getName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "sub group company's name is not null");
        }

        QueryWrapper<SubGroupCompany> wrapper = new QueryWrapper<SubGroupCompany>(new SubGroupCompany().setName(subGroupCompany.getName()));
        SubGroupCompany dbSubGroupCompany = subGroupCompanyMapper.selectOne(wrapper);
        if (dbSubGroupCompany != null) {
            subGroupCompany.setId(dbSubGroupCompany.getId());
            return 1;
        }

        return subGroupCompanyMapper.insert(subGroupCompany);
    }
}
