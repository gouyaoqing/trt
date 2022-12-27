package com.trt.api.controller;

import com.trt.api.model.SelectorRange;
import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.model.query.QMedicine;
import com.trt.common.data.service.MedicineService;
import com.trt.common.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MedicineController {
    @Resource
    private MedicineService medicineService;

    @GetMapping("/medicines")
    public ResponseEntity<ResponseDTO<?>> query(@RequestParam("keyword") String keyword,
                                                @RequestParam("category1") String category1,
                                                @RequestParam("category2") String category2,
                                                @RequestParam("huan_cai") String huanCai,
                                                @RequestParam("department") String department,
                                                @RequestParam("limit") Integer limit) {
        return ResponseUtils.success(medicineService.query(new QMedicine().setKeyword(keyword)
                .setCategory1(category1)
                .setCategory2(category2)
                .setHuanCai(huanCai)
                .setDepartment(department), limit));
    }

    @GetMapping("/medicine/categories")
    public ResponseEntity<ResponseDTO<?>> queryCategory() {
        Map<String, List<String>> categoryMap = medicineService.queryCategory();

        return ResponseUtils.success(categoryMap.entrySet().stream()
                .map(entry -> new SelectorRange()
                        .setLabel(entry.getKey())
                        .setChildren(entry.getValue().stream().map(childStr -> new SelectorRange().setLabel(childStr)).collect(Collectors.toList())))
                .collect(Collectors.toList()));
    }

    @GetMapping("/medicine/huan-cai")
    public ResponseEntity<ResponseDTO<?>> queryHuanCai() {
        return ResponseUtils.success(medicineService.queryHuanCai());
    }

    @GetMapping("/medicine/departments")
    public ResponseEntity<ResponseDTO<?>> queryDepartment() {
        return ResponseUtils.success(medicineService.queryDepartment());
    }
}
