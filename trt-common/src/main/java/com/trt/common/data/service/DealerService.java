package com.trt.common.data.service;

import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.model.Dealer;

import java.util.Optional;

public interface DealerService {
    int getOrInsert(Dealer dealer) throws BusinessException;

    Optional<Dealer> findByCode(String code);

}
