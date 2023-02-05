package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.MedicineMapper;
import com.trt.common.data.model.Custom;
import com.trt.common.data.model.Medicine;
import com.trt.common.data.model.query.QMedicine;
import com.trt.common.data.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        if ("是".equals(medicine.getHuanCai())) {
            log.error("{} {} 幻彩需要填充", medicine.getCode(), medicine.getName());
            return 0;
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

    @Override
    public List<Medicine> query(QMedicine query, Integer limit) {
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(query.getKeyword())) {
            queryWrapper.like("name", query.getKeyword());
        }

        if (StringUtils.isNotBlank(query.getCategory1())) {
            queryWrapper.eq("category1", query.getCategory1());
        }

        if (StringUtils.isNotBlank(query.getCategory2())) {
            queryWrapper.eq("category2", query.getCategory2());
        }

        if (StringUtils.isNotBlank(query.getHuanCai())) {
            queryWrapper.eq("huan_cai", query.getHuanCai());
        }

        if (StringUtils.isNotBlank(query.getDepartment())) {
            queryWrapper.eq("department", query.getDepartment());
        }

        if (limit != null && limit > 0) {
            queryWrapper.last(" limit " + limit);
        }
        return medicineMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, List<String>> queryCategory() {
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT category1", "category2");

        List<Medicine> medicines = medicineMapper.selectList(queryWrapper);

        return medicines.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(Medicine::getCategory1, Collectors.mapping(Medicine::getCategory2, Collectors.toList())));
    }

    @Override
    public List<String> queryHuanCai() {
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT huan_cai");

        List<Medicine> medicines = medicineMapper.selectList(queryWrapper);
        return medicines.stream().filter(Objects::nonNull).map(Medicine::getHuanCai).collect(Collectors.toList());
    }

    @Override
    public List<String> queryDepartment() {
        QueryWrapper<Medicine> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT department");

        List<Medicine> medicines = medicineMapper.selectList(queryWrapper);
        return medicines.stream().filter(Objects::nonNull).map(Medicine::getDepartment).collect(Collectors.toList());
    }

    @Override
    public Medicine findByCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "medicine's code is not null");
        }

        QueryWrapper<Medicine> wrapper = new QueryWrapper<Medicine>(new Medicine().setCode(code));
        return medicineMapper.selectOne(wrapper);
    }
}
