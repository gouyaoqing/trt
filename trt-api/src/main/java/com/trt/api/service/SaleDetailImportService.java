package com.trt.api.service;

public interface SaleDetailImportService {
    void importByExcel(String excelPath, String excelName);

    void importZhiGongByExcel(String excelPath, String excelName);
}
