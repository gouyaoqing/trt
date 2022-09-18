package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.DealerMapper;
import com.trt.common.data.model.Dealer;
import com.trt.common.data.service.DealerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DealerServiceImpl implements DealerService {
    @Resource
    private DealerMapper dealerMapper;

    @Override
    public int insert(Dealer dealer) throws BusinessException {
        if(StringUtils.isBlank(dealer.getCode())){
            throw new BusinessException(HttpStatus.BAD_REQUEST,"dealer's code is not null");
        }

        Wrapper wrapper = new QueryChainWrapper(dealerMapper);
//        dealerMapper.selectOne()
        return dealerMapper.insert(dealer);
    }
}
