package com.trt.api.controller;

import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.service.DealerService;
import com.trt.common.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dealer")
public class DealerController {
    @Resource
    private DealerService dealerService;

    @GetMapping("/keyword")
    public ResponseEntity<ResponseDTO<?>> importSaleDetailByExcel(@RequestParam("keyword") String keyword) {
        return ResponseUtils.success(dealerService.findByKeyword(keyword));
    }
}
