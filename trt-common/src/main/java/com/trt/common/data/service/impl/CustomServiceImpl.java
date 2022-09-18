package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.CustomMapper;
import com.trt.common.data.model.Custom;
import com.trt.common.data.service.CustomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
            return 1;
        }

        return customMapper.insert(custom);
    }
}
