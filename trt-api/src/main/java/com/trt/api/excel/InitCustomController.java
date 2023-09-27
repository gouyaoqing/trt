package com.trt.api.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.trt.api.model.EasyExcelDemo;
import com.trt.api.model.ExcelDemo;
import com.trt.api.model.SaleDetailExcel;
import com.trt.common.data.model.*;
import com.trt.common.data.service.*;
import com.trt.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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

    @Resource
    private SubGroupCompanyService subGroupCompanyService;

    @Resource
    private PharmacyService pharmacyService;

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

    @PostMapping("/excel/import-custom-group-company")
    public String importCustomGroupCompany(@RequestParam("excelPath") String excelPath) {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getName, Function.identity(), (a, b) -> a));
        Map<String, Pharmacy> pharmacyMap = pharmacyService.findAll().stream().collect(Collectors.toMap(Pharmacy::getName, Function.identity(), (a, b) -> a));

        AtomicReference<String> lastGroupCompanyName = new AtomicReference<>();
        AtomicReference<String> lastSubGroupCompanyName = new AtomicReference<>();
        List<Custom> nullGroupCompanyCustoms = new CopyOnWriteArrayList<>();

        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String customName = demoData.getA();
                        String groupCompanyName = StringUtils.isBlank(demoData.getC()) ? null : demoData.getC();
                        String subGroupCompanyName = StringUtils.isBlank(demoData.getE()) ? null : demoData.getE();
                        String businessType = demoData.getB();
                        String bloc = demoData.getG();

//                        if (StringUtils.isBlank(subGroupCompanyName) && StringUtils.isNotBlank(groupCompanyName)) {
//                            subGroupCompanyName = groupCompanyName;
//                        }

                        Custom custom = customMap.get(customName);
                        if (StringUtils.isNotBlank(businessType)) {
                            custom.setBusinessType(businessType);
                        }

                        if (custom == null) {
                            log.error(customName + "不存在");
                            continue;
                        }

//                        custom.setBloc(bloc);

//                        if (pharmacyMap.containsKey(customName)) {
//                            Pharmacy pharmacy = pharmacyMap.get(customName);
//                            if (pharmacy.getGroupCompanyId() != null && pharmacy.getSubGroupCompanyId() != null) {
//                                custom.setGroupCompanyId(pharmacy.getGroupCompanyId());
//                                custom.setSubGroupCompanyId(pharmacy.getSubGroupCompanyId());
//
//                                customService.updateGroupCompanyId(custom);
//                                continue;
//                            }
//                        }
//                        if (groupCompanyName != null) {
//                            if (groupCompanyName.equalsIgnoreCase(lastGroupCompanyName.get())) {
//                                if (!nullGroupCompanyCustoms.isEmpty()) {
//                                    //更新填充空缺的custom
//                                    GroupCompany groupCompany = new GroupCompany().setName(groupCompanyName);
//                                    groupCompanyService.getOrInsert(groupCompany);
//
//                                    SubGroupCompany subGroupCompany = new SubGroupCompany().setName(subGroupCompanyName).setGroupCompanyId(groupCompany.getId());
//                                    subGroupCompanyService.getOrInsert(subGroupCompany);
//
//                                    nullGroupCompanyCustoms.forEach(nullGroupCompanyCustom -> {
//                                        nullGroupCompanyCustom.setGroupCompanyId(groupCompany.getId());
//                                        nullGroupCompanyCustom.setSubGroupCompanyId(subGroupCompany.getId());
//                                        customService.updateGroupCompanyId(nullGroupCompanyCustom);
//                                    });
//                                    nullGroupCompanyCustoms.clear();
//                                }
//                            } else {
//                                nullGroupCompanyCustoms.clear();
//                            }
//
//                            lastGroupCompanyName.set(groupCompanyName);
//                            lastSubGroupCompanyName.set(subGroupCompanyName);
//                        } else {
//                            nullGroupCompanyCustoms.add(custom);
//                        }


//                        if (StringUtils.isNotBlank(groupCompanyName)) {
//                            GroupCompany groupCompany = new GroupCompany().setName(groupCompanyName);
//                            groupCompanyService.getOrInsert(groupCompany);
//                            custom.setGroupCompanyId(groupCompany.getId());
//                        } else {
//                            custom.setGroupCompanyId(null);
//                        }
//
//                        if (StringUtils.isNotBlank(subGroupCompanyName)) {
//                            SubGroupCompany subGroupCompany = new SubGroupCompany().setName(subGroupCompanyName).setGroupCompanyId(custom.getGroupCompanyId());
//                            subGroupCompanyService.getOrInsert(subGroupCompany);
//                            custom.setSubGroupCompanyId(subGroupCompany.getId());
//                        } else {
//                            custom.setSubGroupCompanyId(null);
//                        }

                        customService.updateGroupCompanyId(custom);

//                        log.info("已更改 " + customName + " groupCompanyId=" + groupCompanyId + ",subGroupCompanyId=" + subGroupCompanyId);

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

    @PostMapping("/excel/import-custom-group-company1")
    public String importCustomGroupCompany1(@RequestParam("excelPath") String excelPath) {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getName, Function.identity(), (a, b) -> a));

        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String customName = demoData.getA();
                        String businessType = StringUtils.isBlank(demoData.getB()) || demoData.getB().equalsIgnoreCase("null") ? null : demoData.getB();
                        String businessTypeCha = StringUtils.isBlank(demoData.getC()) || demoData.getC().equalsIgnoreCase("null") ? null : demoData.getC();
                        String groupCompanyName = StringUtils.isBlank(demoData.getD()) || demoData.getD().equalsIgnoreCase("null") ? null : demoData.getD();
                        String subGroupCompanyName = StringUtils.isBlank(demoData.getE()) || demoData.getE().equalsIgnoreCase("null") ? null : demoData.getE();


                        Custom custom = customMap.get(customName);

                        if (custom == null) {
                            log.error(customName + "不存在");
                            continue;
                        }

                        custom.setBusinessType(businessType);
//                        custom.setCategory(businessTypeCha);

//                        if (groupCompanyName != null) {
//                            GroupCompany groupCompany = new GroupCompany().setName(groupCompanyName);
//                            groupCompanyService.getOrInsert(groupCompany);
//                            custom.setGroupCompanyId(groupCompany.getId());
//                        } else {
//                            custom.setGroupCompanyId(null);
//                        }
//
//                        if (subGroupCompanyName != null) {
//                            SubGroupCompany subGroupCompany = new SubGroupCompany().setName(subGroupCompanyName).setGroupCompanyId(custom.getGroupCompanyId());
//                            subGroupCompanyService.getOrInsertOrUpdate(subGroupCompany);
//                            custom.setSubGroupCompanyId(subGroupCompany.getId());
//                        } else {
//                            custom.setSubGroupCompanyId(null);
//                        }


                        customService.updateBusinessTypeById(custom);

//                        log.info("已更改 " + customName + " groupCompanyId=" + groupCompanyId + ",subGroupCompanyId=" + subGroupCompanyId);

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
