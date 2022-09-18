package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.MedicineBatchMapper;
import com.trt.common.data.model.MedicineBatch;
import com.trt.common.data.service.MedicineBatchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MedicineBatchServiceImpl implements MedicineBatchService {
    @Resource
    private MedicineBatchMapper medicineBatchMapper;

    @Override
    public int getOrInsert(MedicineBatch medicineBatch) throws BusinessException {
        if (StringUtils.isBlank(medicineBatch.getLotNumber()) || medicineBatch.getMedicineId() == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "medicine batch's medicine is or lot number is not null");
        }

        QueryWrapper<MedicineBatch> wrapper = new QueryWrapper<MedicineBatch>(new MedicineBatch().setMedicineId(medicineBatch.getMedicineId()).setLotNumber(medicineBatch.getLotNumber()));
        MedicineBatch dbMedicineBatch = medicineBatchMapper.selectOne(wrapper);
        if (dbMedicineBatch != null) {
            medicineBatch.setId(dbMedicineBatch.getId());
            return 1;
        }

        return medicineBatchMapper.insert(medicineBatch);
    }
}
