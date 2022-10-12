package com.trt.api.model;

import lombok.Data;

import java.util.Date;

@Data
public class ExcelDemo {
    private String dealerArea;
    private String dealerProvince;
    private String dealerCity;
    private String dealerCode;
    private String dealerName;
    private String dealerLevel;
    private String customArea;
    private String customProvince;
    private String customCity;
    private String customCode;
    private String customName;
    private String medicineLotNum;
    private String medicineCode;
    private String medicineName;
    private String medicineSpecification;
    private String medicineInitSpecification;
    private String medicineUnit;
    private Integer saleNum;
    private Double salePrice;
    private Double saleAmount;
    private Date saleDate;
    private String minUnit;
    private Integer minNum;
    private Integer packingPcs;
    private Double packageNum;
    private Double minPrice;
    private Double saleAmountDouble;
    private String department;
    private String businessType;
}
