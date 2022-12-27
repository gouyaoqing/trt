package com.trt.api.controller;

import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.model.query.QMedicine;
import com.trt.common.data.service.MedicineService;
import com.trt.common.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MedicineController {
    @Resource
    private MedicineService medicineService;

    @GetMapping("/medicines")
    public ResponseEntity<ResponseDTO<?>> query(@RequestParam("keyword") String keyword,
                                                @RequestParam("category1") String category1,
                                                @RequestParam("category2") String category2,
                                                @RequestParam("huan_cai") String huanCai,
                                                @RequestParam("yu_yao_300") Boolean yuYao300,
                                                @RequestParam("limit") Integer limit) {
        return ResponseUtils.success(medicineService.query(new QMedicine().setKeyword(keyword)
                .setCategory1(category1)
                .setCategory2(category2)
                .setHuanCai(huanCai)
                .setYuYao300(yuYao300), limit));
    }

    @GetMapping("/medicine/categories")
    public ResponseEntity<ResponseDTO<?>> queryCategory() {
        return ResponseUtils.success(medicineService.queryCategory());
    }

    @GetMapping("/medicine/huan-cai")
    public ResponseEntity<ResponseDTO<?>> queryHuanCai() {
        return ResponseUtils.success(medicineService.queryHuanCai());
    }
}
