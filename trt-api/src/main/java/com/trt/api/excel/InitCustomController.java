package com.trt.api.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.trt.api.model.EasyExcelDemo;
import com.trt.api.model.ExcelDemo;
import com.trt.api.model.SaleDetailExcel;
import com.trt.common.data.model.*;
import com.trt.common.data.service.CustomService;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.MedicineService;
import com.trt.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class InitCustomController {
    @Resource
    private CustomService customService;

    @Resource
    private GroupCompanyService groupCompanyService;

    @PostMapping("/excel/init-custom")
    public String initMedicine(@RequestParam("excelPath") String excelPath) {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getName, Function.identity(), (a, b) -> a));

        AtomicInteger notExist = new AtomicInteger();
        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String name = demoData.getB();

                        if (StringUtils.isBlank(name)) {
                            continue;
                        }
                        Custom custom = customMap.get(name);

                        if (custom == null) {
                            log.error("没有 " + name);
                            notExist.getAndDecrement();
                            continue;
                        }

                        String businessType = demoData.getC();

                        if (StringUtils.isNotBlank(businessType)) {
                            //update
                            custom.setBusinessType(businessType);

                            customService.updateBusinessTypeById(custom);
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

    @PostMapping("/excel/init-custom-group-company")
    public String initCustomGroupCompany(@RequestParam("excelPath") String excelPath) {
        Map<Long, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getId, Function.identity()));
        Map<String, GroupCompany> groupCompanyMap = groupCompanyService.findAll().stream().collect(Collectors.toMap(GroupCompany::getName, Function.identity()));

        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String groupCompanyName = demoData.getO();
                        String hongJun = demoData.getT();
                        String customIdStr = demoData.getA();

                        if (StringUtils.isBlank(groupCompanyName) && StringUtils.isBlank(hongJun)) {
                            continue;
                        }

                        if (StringUtils.isNotBlank(groupCompanyName)) {
                            //deal group company
                            GroupCompany groupCompany = new GroupCompany().setName(groupCompanyName).setHongJun("红军联盟".equals(hongJun));
                            GroupCompany dbGroupCompany = groupCompanyMap.get(groupCompanyName);
                            if (dbGroupCompany == null) {
                                groupCompanyService.getOrInsert(groupCompany);
                            } else if (groupCompany.getHongJun()) {
                                groupCompanyService.updateHongJun(groupCompany.setId(dbGroupCompany.getId()));
                            }

                            //deal custom
                            if (StringUtils.isNotBlank(customIdStr)) {
                                Custom custom = customMap.get(Long.parseLong(customIdStr));
                                if (custom == null) {
                                    log.error("custom code {} is not exist.", customIdStr);
                                } else {
                                    customService.updateGroupCompanyId(custom.setGroupCompanyId(groupCompany.getId()));
                                }
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
        log.info("done.");
        return "success";
    }

}
