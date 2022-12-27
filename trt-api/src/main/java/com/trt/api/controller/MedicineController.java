package com.trt.api.controller;

import com.trt.common.data.model.api.ResponseDTO;
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
                                                @RequestParam("limit") Integer limit) {
        return ResponseUtils.success(medicineService.query(keyword, limit));
    }
}
