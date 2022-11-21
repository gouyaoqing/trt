package com.trt.api.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.trt.api.model.EasyExcelDemo;
import com.trt.common.data.model.Custom;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.service.CustomService;
import com.trt.common.data.service.GroupCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class GroupCompanyImportController {
    @Resource
    private GroupCompanyService groupCompanyService;

    @PostMapping("/excel/group-cpmpany/city")
    public String initGroupCompany(@RequestParam("excelPath") String excelPath) {
        Map<String, GroupCompany> groupCompanyMap = groupCompanyService.findAll().stream().collect(Collectors.toMap(GroupCompany::getName, Function.identity(), (a, b) -> a));

        AtomicInteger notExist = new AtomicInteger();
        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String name = demoData.getC();

                        if (StringUtils.isBlank(name)) {
                            continue;
                        }
                        GroupCompany groupCompany = groupCompanyMap.get(name);

                        if (groupCompany == null) {
                            log.error("没有 " + name);
                            notExist.getAndDecrement();

                            groupCompany = new GroupCompany();
                            groupCompany.setName(name);
                        }

                        String province = demoData.getA();
                        String city = demoData.getB();

                        if (StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city)) {
                            //update
                            groupCompany.setProvince(province);
                            groupCompany.setCity(city);
                            groupCompany.setCityTop10(true);

                            if (groupCompany.getId() == null) {
                                groupCompanyService.getOrInsert(groupCompany);
                            } else {
                                groupCompanyService.updateCity(groupCompany);
                            }

                        }

                    } catch (Exception e) {
                        log.error("read excel error.{}", demoData, e);
                    }

                }
            })).sheet().doRead();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        log.info("done.not exist=" + notExist.get());
        return "success";
    }

}
