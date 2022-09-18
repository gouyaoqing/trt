package com.trt.api.controller;

import com.trt.api.service.SaleDetailImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SaleDetailController {
    @Resource
    private SaleDetailImportService saleDetailImportService;

    @PostMapping("/sale-detal/excel")
    public String importSaleDetailByExcel(@RequestParam("excelPath") String excelPath, @RequestParam("excelName") String excelName) {
        saleDetailImportService.importByExcel(excelPath, excelName);
        return "success";
    }
}
