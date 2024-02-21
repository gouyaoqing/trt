package com.trt.api.controller;

import com.google.common.collect.Lists;
import com.trt.common.data.model.Custom;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;
import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.service.CustomService;
import com.trt.common.data.service.DealerService;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.PharmacyService;
import com.trt.common.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/custom")
@Slf4j
public class CustomController {
    @Resource
    private CustomService customService;

    @Resource
    private GroupCompanyService groupCompanyService;

    @Resource
    private PharmacyService pharmacyService;

    @GetMapping("/business-types")
    public ResponseEntity<ResponseDTO<?>> businessType() {
        return ResponseUtils.success(customService.businessType());
    }

    @GetMapping("/same-name")
    public ResponseEntity<ResponseDTO<?>> sameName() {
        List<Custom> customs = customService.findAll();
        Map<Long, GroupCompany> groupCompanyMap = groupCompanyService.findAll().stream().collect(Collectors.toMap(GroupCompany::getId, Function.identity()));
        int length = 5;

        Map<String, List<Custom>> sameNameMap = new HashMap<>();

        for (Custom custom : customs) {
            String subName = custom.getName().substring(0, length);
            if (sameNameMap.containsKey(subName)) {
                sameNameMap.get(subName).add(custom);
            } else {
                sameNameMap.put(subName, Lists.newArrayList(custom));
            }
        }

        StringBuffer logStr = new StringBuffer();
        for (Map.Entry<String, List<Custom>> entry : sameNameMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                logStr.append(entry.getKey() + "\n ");
                for (Custom custom : entry.getValue()) {
                    GroupCompany groupCompany = custom.getGroupCompanyId() == null || custom.getGroupCompanyId() <= 0 ? null : groupCompanyMap.get(custom.getGroupCompanyId());

                    logStr.append("\t" + custom.getName() + "\t\t" + (groupCompany == null ? "" : groupCompany.getName()) + "\n");
                }
//                logStr.append(entry.getKey() + "\n");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/gouyaoqing/private/trt/2024/same-name"))) {
            writer.write(logStr.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseUtils.success(customService.businessType());
    }

    @GetMapping("/same-name-pharmacy")
    public ResponseEntity<ResponseDTO<?>> sameNamePharmacy() {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getName, Function.identity(), (a, b) -> a));
        Map<String, Pharmacy> pharmacyMap = pharmacyService.findAll().stream().collect(Collectors.toMap(Pharmacy::getName, Function.identity(), (a, b) -> a));

        long count = 0;
        for (Map.Entry<String, Custom> entry : customMap.entrySet()) {
            if (entry.getKey() != null && pharmacyMap.containsKey(entry.getKey())) {
                Pharmacy pharmacy = pharmacyMap.get(entry.getKey());
                if ((pharmacy.getGroupCompanyId() != null && pharmacy.getGroupCompanyId() > 0L)
                        | (pharmacy.getSubGroupCompanyId() != null && pharmacy.getSubGroupCompanyId() > 0L)) {
                    Custom custom = entry.getValue();
                    custom.setGroupCompanyIdNew(pharmacy.getGroupCompanyId());
                    custom.setSubGroupCompanyIdNew(pharmacy.getSubGroupCompanyId());
                    custom.setSocietyCode(null);

                    customService.updateGroupCompanyIdNew(custom);
                    log.info("修改 " + entry.getKey() + "为 " + pharmacy.getGroupCompanyId() + " " + pharmacy.getSubGroupCompanyId());
                    count++;
                }
            }
        }

        return ResponseUtils.success(count);
    }
}
