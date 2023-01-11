package com.trt.api.controller;

import com.trt.api.service.SaleDetailImportService;
import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.model.query.QSaleDetail;
import com.trt.common.data.service.SaleDetailService;
import com.trt.common.utils.ResponseUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SaleDetailController {
    @Resource
    private SaleDetailImportService saleDetailImportService;

    @Resource
    private SaleDetailService saleDetailService;

    @PostMapping("/sale-detal/excel")
    public String importSaleDetailByExcel(@RequestParam("excelPath") String excelPath, @RequestParam("excelName") String excelName) {
        saleDetailImportService.importByExcel(excelPath, excelName);
        return "success";
    }

    @ApiOperation(value = "导入销售数据-直供终端")
    @PostMapping("/sale-detal/zhi-gong/excel")
    public String importSaleDetailZhiGongByExcel(@RequestParam("excelPath") String excelPath, @RequestParam("excelName") String excelName) throws Exception {
        saleDetailImportService.importZhiGongByExcel(excelPath, excelName);
        return "success";
    }

    @PostMapping("/sale-detal")
    public ResponseEntity<ResponseDTO<?>> querySaleDetail(@RequestBody QSaleDetail query) {
        return ResponseUtils.success(saleDetailService.querySaleDetailTotal(query));
    }
}
