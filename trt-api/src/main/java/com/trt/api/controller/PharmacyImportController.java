package com.trt.api.controller;

import com.trt.api.service.PharmacyCompanyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PharmacyImportController {
    @Resource
    private PharmacyCompanyService pharmacyCompanyService;

    @PostMapping("/pharmacy/import")
    public String importPharmacyByNet(@RequestParam("city_num") Integer cityNum, @RequestParam("pageNum") Integer pageNum, @RequestParam("token") String token) {
        pharmacyCompanyService.importPharmacyByNet(cityNum, pageNum, token);
        return "success";
    }
}
