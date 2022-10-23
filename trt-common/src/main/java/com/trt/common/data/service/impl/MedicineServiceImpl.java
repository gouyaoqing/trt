package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.MedicineMapper;
import com.trt.common.data.model.Custom;
import com.trt.common.data.model.Medicine;
import com.trt.common.data.service.MedicineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<Medicine> findAll() {
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
        return medicineMapper.selectList(queryWrapper);
    }

    @Override
    public int updateSameField(Medicine medicine) {
        UpdateWrapper<Medicine> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", medicine.getId());
        wrapper.set("company_code", medicine.getCompanyCode());
        wrapper.set("alias", medicine.getAlias());
        wrapper.set("category1", medicine.getCategory1());
        wrapper.set("category2", medicine.getCategory2());
        wrapper.set("description", medicine.getDescription());
        wrapper.set("huan_cai", medicine.getHuanCai());
        wrapper.set("yu_yao_300", medicine.getYuYao300());

        return medicineMapper.update(medicine, wrapper);
    }
}
