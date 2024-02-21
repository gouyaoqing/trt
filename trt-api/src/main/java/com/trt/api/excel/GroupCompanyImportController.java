package com.trt.api.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.google.common.primitives.Longs;
import com.trt.api.model.EasyExcelDemo;
import com.trt.common.data.model.Custom;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.SubGroupCompany;
import com.trt.common.data.service.CustomService;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.SubGroupCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class GroupCompanyImportController {
    @Resource
    private GroupCompanyService groupCompanyService;

    @Resource
    private SubGroupCompanyService subGroupCompanyService;

    @Resource
    private CustomService customService;

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

    @PostMapping("/excel/group-cpmpany-and-sub-new")
    public String initGroupCompanyAndSub(@RequestParam("excelPath") String excelPath) {
        List<Custom> customList = customService.findAll();
        Map<String, Custom> nameCustomMap = customList.stream().collect(Collectors.toMap(Custom::getName, Function.identity(), (a, b) -> a));
        Map<String, Custom> societyCodeCustomMap = customList.stream().collect(Collectors.toMap(Custom::getSocietyCode, Function.identity(), (a, b) -> a));

        Map<String, GroupCompany> groupCompanyMap = groupCompanyService.findAll().stream().collect(Collectors.toMap(GroupCompany::getName, Function.identity(), (a, b) -> a));
        Map<String, SubGroupCompany> subGroupCompanyMap = subGroupCompanyService.findAll().stream().collect(Collectors.toMap(SubGroupCompany::getName, Function.identity(), (a, b) -> a));


        AtomicInteger updatedCount = new AtomicInteger();
        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String customName = demoData.getB();
                        String societyCode = demoData.getH();
                        String subGroupCompanyName = demoData.getL();
                        String groupCompanyName = demoData.getM();

                        if (StringUtils.isBlank(customName) || StringUtils.isBlank(societyCode)) {
                            continue;
                        }

                        if (StringUtils.isBlank(groupCompanyName) || groupCompanyName.equals("单体")) {
                            continue;
                        }

                        if (StringUtils.isBlank(subGroupCompanyName) || subGroupCompanyName.equals("单体")) {
                            continue;
                        }

                        Custom custom = societyCodeCustomMap.get(societyCode);
                        if (custom == null) {
                            custom = nameCustomMap.get(customName);
                        }

                        if (custom == null) {
                            log.error("库里客户不存在 " + customName + " " + societyCode);
                            continue;
                        }


                        GroupCompany groupCompany = groupCompanyMap.get(groupCompanyName);
                        if (groupCompany == null) {
                            groupCompany = new GroupCompany();
                            groupCompany.setName(groupCompanyName);
                            groupCompanyService.getOrInsert(groupCompany);
                        }

                        SubGroupCompany subGroupCompany = subGroupCompanyMap.get(subGroupCompanyName);
                        if (subGroupCompany == null) {
                            subGroupCompany = new SubGroupCompany();
                            subGroupCompany.setName(subGroupCompanyName);
                            subGroupCompany.setGroupCompanyId(groupCompany.getId());
                            subGroupCompanyService.getOrInsert(subGroupCompany);
                        }

                        custom.setGroupCompanyIdNew(groupCompany.getId());
                        custom.setSubGroupCompanyIdNew(subGroupCompany.getId());
                        custom.setSocietyCode(societyCode);
                        customService.updateGroupCompanyIdNew(custom);
                        updatedCount.getAndIncrement();
//                            log.info("修改了 " + customName);


                    } catch (Exception e) {
                        log.error("read excel error.{}", demoData, e);
                    }

                }
            })).sheet().doRead();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        log.info("done.updated " + updatedCount.get());
        return "success";
    }

    @PostMapping("/excel/society-code")
    public String initSocietyCode(@RequestParam("excelPath") String excelPath) {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getCode, Function.identity(), (a, b) -> a));

        StringBuffer notExistCode = new StringBuffer("code不存在：");
        AtomicInteger successCount = new AtomicInteger();
        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo demoData : dataList) {
                    try {
                        String customCode = demoData.getA();
                        String customName = demoData.getB();
                        String societyCode = demoData.getC();

                        if (StringUtils.isBlank(customCode)) {
                            continue;
                        }

                        if (!customMap.containsKey(customCode)) {
                            notExistCode.append(customCode);
                            notExistCode.append(",");
                            continue;
                        }

                        successCount.getAndIncrement();
                        Custom custom = customMap.get(customCode);

                        if (!custom.getName().equals(customName)) {
                            log.info("修改了 " + customCode + " 由 " + custom.getName() + " 改为 " + customName);
                            custom.setName(customName);
                            customService.updateNameById(custom);
                        }

                        if (StringUtils.isBlank(custom.getSocietyCode()) || !custom.getSocietyCode().equals(societyCode)) {
                            log.info("修改了 " + customCode + " 由 " + custom.getSocietyCode() + " 改为 " + societyCode);
                            custom.setSocietyCode(societyCode);
                            customService.updateSocietyCodeById(custom);
                        }
                    } catch (Exception e) {
                        log.error("read excel error.{}", demoData, e);
                    }

                }
            })).sheet().doRead();

        } catch (Exception e) {
            log.error("eee", e);
        } finally {
            log.info("done." + notExistCode.toString());
            log.info("成功数" + successCount.get());
        }

        return "success";
    }
}
