package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.SaleDetailMapper;
import com.trt.common.data.model.SaleDetail;
import com.trt.common.data.model.api.SaleDetailSumResult;
import com.trt.common.data.model.query.QSaleDetail;
import com.trt.common.data.service.SaleDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SaleDetailServiceImpl implements SaleDetailService {
    @Resource
    private SaleDetailMapper saleDetailMapper;

    @Override
    public int insert(SaleDetail saleDetail) throws BusinessException {
        if (saleDetail.getDealerId() == null || saleDetail.getCustomId() == null || saleDetail.getMedicineBatchId() == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "sale detail's all id is not null");
        }
        return saleDetailMapper.insert(saleDetail);
    }

    @Override
    public int deleteByExcelName(String excelName) {
        SaleDetail where = new SaleDetail();
        where.setExcel(excelName);
        UpdateWrapper<SaleDetail> updateWrapper = new UpdateWrapper<SaleDetail>(where);
        return saleDetailMapper.delete(updateWrapper);
    }

    @Override
    public int updateCustomId(Long oldCustomId, Long newCustomId) {
        UpdateWrapper<SaleDetail> updateWrapper = new UpdateWrapper<SaleDetail>();
        updateWrapper.eq("custom_id", oldCustomId);
        updateWrapper.set("custom_id", newCustomId);
        return saleDetailMapper.update(null, updateWrapper);
    }

    @Override
    public SaleDetailSumResult querySaleDetailTotal(QSaleDetail query) {
        return null;
    }
}
