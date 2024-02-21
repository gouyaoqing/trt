package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.CustomMapper;
import com.trt.common.data.model.Custom;
import com.trt.common.data.model.Dealer;
import com.trt.common.data.model.Medicine;
import com.trt.common.data.service.CustomService;
import com.trt.common.data.service.SaleDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomServiceImpl implements CustomService {
    @Resource
    private CustomMapper customMapper;

    @Resource
    private SaleDetailService saleDetailService;

    @Override
    public int getOrInsert(Custom custom) throws BusinessException {
        if (StringUtils.isBlank(custom.getCode())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "custom's code is not null");
        }

        QueryWrapper<Custom> wrapper = new QueryWrapper<Custom>(new Custom().setCode(custom.getCode()));
        Custom dbCodeCustom = customMapper.selectOne(wrapper);
        if (dbCodeCustom != null) {
            custom.setId(dbCodeCustom.getId());

            //填充社会统一代码
            if (StringUtils.isBlank(custom.getSocietyCode())) {
                UpdateWrapper<Custom> updateWrapper = new UpdateWrapper();
                updateWrapper.set("society_code", custom.getSocietyCode());
                updateWrapper.eq("id", custom.getId());

                customMapper.update(custom, updateWrapper);
            }

            if (!custom.getName().equals(dbCodeCustom.getName())) {

                //咱就是说，根据code查出来的名字不一样了，先看看新名字存不存在
                QueryWrapper<Custom> nameWrapper = new QueryWrapper<Custom>(new Custom().setName(custom.getName()));
                Custom newNameCustom = customMapper.selectOne(nameWrapper);
                if (newNameCustom != null) {
                    //如果相同名字的custom存在那就是说，合二为一，删一个改一个，留code的
                    log.error("新custom " + custom.getCode() + "_" + custom.getName() + " 已存在的code " + dbCodeCustom.getCode() + "_" + dbCodeCustom.getName() + " 已存在的name" + newNameCustom.getCode() + "_" + newNameCustom.getName());

                    //先把销售数据改了
                    int count = saleDetailService.updateCustomId(newNameCustom.getId(), dbCodeCustom.getId());
                    log.error("修改了销售数据" + count + "条");
                    //先把相同名字的删掉
                    UpdateWrapper<Custom> updateDeleteWrapper = new UpdateWrapper<Custom>();
                    updateDeleteWrapper.eq("id", newNameCustom.getId());
                    updateDeleteWrapper.set("name", newNameCustom.getName() + "_delete");
                    updateDeleteWrapper.set("code", newNameCustom.getCode() + "_delete");
                    customMapper.update(null, updateDeleteWrapper);

                    //再改code那个为新的名字
                    UpdateWrapper<Custom> updateWrapper = new UpdateWrapper<Custom>();
                    updateWrapper.eq("id", dbCodeCustom.getId());
                    updateWrapper.set("name", custom.getName());
                    customMapper.update(null, updateWrapper);
                } else {
                    log.error("custom {} 改为 {}", dbCodeCustom.getName(), custom.getName());
                    UpdateWrapper<Custom> updateWrapper = new UpdateWrapper<Custom>();
                    updateWrapper.eq("id", dbCodeCustom.getId());
                    updateWrapper.set("name", custom.getName());
                    customMapper.update(null, updateWrapper);
                }

            }

//            if (custom.getBusinessType() != null) {
//                UpdateWrapper<Custom> updateWrapper = new UpdateWrapper();
//                updateWrapper.eq("id", custom.getId());
//                updateWrapper.set("business_type", custom.getBusinessType());
//                customMapper.update(dbCustom, updateWrapper);
//            }
            return 1;
        } else {
            Custom newNameCustom = customMapper.selectOne(new QueryWrapper<Custom>(new Custom().setName(custom.getName())));

            if (newNameCustom != null) {
                UpdateWrapper<Custom> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", newNameCustom.getId());
                updateWrapper.set("code", custom.getCode());
                customMapper.update(newNameCustom, updateWrapper);
                custom.setId(newNameCustom.getId());
                return 1;
            } else {
                return customMapper.insert(custom);
            }
        }


    }

    @Override
    public List<Custom> findAll() {
        QueryWrapper<Custom> wrapper = new QueryWrapper<>();
        return customMapper.selectList(wrapper);
    }

    @Override
    public int updateExcelInfo(Custom custom) {
        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("industry", custom.getIndustry());
        wrapper.set("society_code", custom.getSocietyCode());
        wrapper.set("register_code", custom.getRegisterCode());
        wrapper.set("organization_code", custom.getOrganizationCode());
        wrapper.set("address", custom.getAddress());
        wrapper.set("taxes_code", custom.getTaxesCode());

        return customMapper.update(custom, wrapper);
    }

    @Override
    public int updateBusinessTypeById(Custom custom) {
        if (custom.getId() == null) {
            return 0;
        }
        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("business_type", custom.getBusinessType());

        return customMapper.update(custom, wrapper);
    }

    @Override
    public int updateGroupCompanyId(Custom custom) {
        if (custom.getId() == null) {
            return 0;
        }

        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("group_company_id", custom.getGroupCompanyId());
        wrapper.set("sub_group_company_id", custom.getSubGroupCompanyId());
        if (custom.getBusinessType() != null) {
            wrapper.set("business_type", custom.getBusinessType());
        }

        if (custom.getCategory() != null) {
            wrapper.set("category", custom.getCategory());
        }

        return customMapper.update(custom, wrapper);
    }

    @Override
    public int updateGroupCompanyIdNew(Custom custom) {
        if (custom.getId() == null) {
            return 0;
        }

        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("group_company_id_new", custom.getGroupCompanyIdNew());
        wrapper.set("sub_group_company_id_new", custom.getSubGroupCompanyIdNew());

        if (custom.getSocietyCode() != null) {
            wrapper.set("society_code", custom.getSocietyCode());
        }

        return customMapper.update(custom, wrapper);
    }

    @Override
    public List<String> businessType() {
        QueryWrapper<Custom> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT business_type");

        List<Custom> customs = customMapper.selectList(queryWrapper);
        return customs.stream().filter(Objects::nonNull).map(Custom::getBusinessType).collect(Collectors.toList());
    }

    @Override
    public int updateNameById(Custom custom) {
        if (custom == null || custom.getId() == null) {
            return 0;
        }

        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("name", custom.getName());

        return customMapper.update(custom, wrapper);
    }

    @Override
    public int updateSocietyCodeById(Custom custom) {
        if (custom == null || custom.getId() == null) {
            return 0;
        }

        UpdateWrapper<Custom> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", custom.getId());
        wrapper.set("society_code", custom.getSocietyCode());

        return customMapper.update(custom, wrapper);
    }
}
