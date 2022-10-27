package com.trt.common.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.trt.common.data.exception.BusinessException;
import com.trt.common.data.mapper.GroupCompanyMapper;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.service.GroupCompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
@Service
public class GroupCompanyServiceImpl implements GroupCompanyService {
    @Resource
    private GroupCompanyMapper groupCompanyMapper;

    @Override
    public int getOrInsert(GroupCompany groupCompany) throws BusinessException {
        if (StringUtils.isBlank(groupCompany.getName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "group company's name is not null");
        }

        QueryWrapper<GroupCompany> wrapper = new QueryWrapper<GroupCompany>(new GroupCompany().setName(groupCompany.getName()));
        GroupCompany dbGroupCompany = groupCompanyMapper.selectOne(wrapper);
        if (dbGroupCompany != null) {
            groupCompany.setId(dbGroupCompany.getId());
            return 1;
        }

        return groupCompanyMapper.insert(groupCompany);
    }

    @Override
    public List<GroupCompany> findAll() {
        QueryWrapper<GroupCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        return groupCompanyMapper.selectList(queryWrapper);
    }

    @Override
    public int updateHongJun(GroupCompany groupCompany) {
        if (groupCompany.getId() == null) {
            return 0;
        }
        UpdateWrapper<GroupCompany> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", groupCompany.getId());
        updateWrapper.set("hong_jun", groupCompany.getHongJun());
        return groupCompanyMapper.update(groupCompany, updateWrapper);
    }

    @Override
    public Optional<GroupCompany> findByName(String name) {
        return Optional.ofNullable(name)
                .map(groupCompanyName -> {
                    QueryWrapper<GroupCompany> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("name", groupCompanyName);
                    return queryWrapper;
                })
                .map(queryWrapper -> groupCompanyMapper.selectOne(queryWrapper));
    }

    @Override
    public int updateBooleanLabel(GroupCompany groupCompany) {
        if (groupCompany == null || groupCompany.getId() == null) {
            return 0;
        }

        UpdateWrapper<GroupCompany> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", groupCompany.getId());
        updateWrapper.set("country_top_100", groupCompany.getCountryTop100());
        updateWrapper.set("important_19", groupCompany.getImportant19());
        updateWrapper.set("county_top_100", groupCompany.getCountyTop100());

        return groupCompanyMapper.update(groupCompany, updateWrapper);

    }
}
