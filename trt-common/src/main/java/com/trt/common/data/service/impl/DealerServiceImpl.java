package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.DealerMapper;
import com.trt.common.data.model.Dealer;
import com.trt.common.data.service.DealerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DealerServiceImpl implements DealerService {
    @Resource
    private DealerMapper dealerMapper;

    @Override
    public int getOrInsert(Dealer dealer) throws BusinessException {
        if (StringUtils.isBlank(dealer.getCode())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "dealer's code is not null");
        }

        QueryWrapper<Dealer> wrapper = new QueryWrapper<Dealer>(new Dealer().setCode(dealer.getCode()));
        Dealer dbDealer = dealerMapper.selectOne(wrapper);
        if (dbDealer != null) {
            dealer.setId(dbDealer.getId());

            //填充社会统一代码
            if (StringUtils.isBlank(dealer.getSocietyCode())) {
                UpdateWrapper<Dealer> updateWrapper = new UpdateWrapper();
                updateWrapper.set("society_code", dealer.getSocietyCode());
                updateWrapper.eq("id", dealer.getId());

                dealerMapper.update(dealer, updateWrapper);
            }
            return 1;
        }

        return dealerMapper.insert(dealer);
    }

    @Override
    public Optional<Dealer> findByCode(String code) {
        Dealer whereDealer = new Dealer().setCode(code);
        QueryWrapper<Dealer> wrapper = new QueryWrapper<Dealer>(whereDealer);
        return Optional.ofNullable(dealerMapper.selectOne(wrapper));
    }

    @Override
    public List<Dealer> findByKeyword(String keyword) {
        QueryWrapper<Dealer> wrapper = new QueryWrapper<Dealer>();
        wrapper.like("name", keyword);
        wrapper.last("limit 100");
        return dealerMapper.selectList(wrapper);
    }

}
