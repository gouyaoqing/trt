package com.trt.api.controller;

import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.service.CustomService;
import com.trt.common.data.service.DealerService;
import com.trt.common.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/custom")
public class CustomController {
    @Resource
    private CustomService customService;

    @GetMapping("/business-types")
    public ResponseEntity<ResponseDTO<?>> businessType() {
        return ResponseUtils.success(customService.businessType());
    }
}
