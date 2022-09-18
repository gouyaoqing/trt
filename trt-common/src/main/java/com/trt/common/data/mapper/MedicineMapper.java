package com.trt.common.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trt.common.data.model.Medicine;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MedicineMapper extends BaseMapper<Medicine> {
}
