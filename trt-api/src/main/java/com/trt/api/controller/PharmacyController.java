package com.trt.api.controller;

import com.trt.common.data.model.api.ResponseDTO;
import com.trt.common.data.model.query.QPharmacy;
import com.trt.common.data.service.PharmacyService;
import com.trt.common.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class PharmacyController {
    @Resource
    private PharmacyService pharmacyService;

    @GetMapping("/pharmacies")
    public ResponseEntity<ResponseDTO<?>> query(@RequestParam("keyword") String keyword,
                                                @RequestParam("province") String province,
                                                @RequestParam("city") String city,
                                                @RequestParam("street") String street,
                                                @RequestParam("district") String district,
                                                @RequestParam("limit") Integer limit) {
        return ResponseUtils.success(pharmacyService.query(new QPharmacy().setKeyword(keyword)
                        .setProvince(province)
                        .setCity(city)
                        .setDistrict(district)
                        .setStreet(street),
                limit));
    }
}
