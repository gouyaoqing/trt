package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.MedicineMapper;
import com.trt.common.data.model.Medicine;
import com.trt.common.data.service.MedicineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Resource
    private MedicineMapper medicineMapper;

    @Override
    public int getOrInsert(Medicine medicine) throws BusinessException {
        if (StringUtils.isBlank(medicine.getCode())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "medicine's code is not null");
        }

        QueryWrapper<Medicine> wrapper = new QueryWrapper<Medicine>(new Medicine().setCode(medicine.getCode()));
        Medicine dbMedicine = medicineMapper.selectOne(wrapper);
        if (dbMedicine != null) {
            medicine.setId(dbMedicine.getId());
            return 1;
        }

        return medicineMapper.insert(medicine);
    }
}
