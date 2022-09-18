package com.trt.api.controller;

import com.trt.common.data.model.Dealer;
import com.trt.common.data.service.DealerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SaleDetailController {
    @Resource
    private DealerService dealerService;

    @PostMapping("/sale-detal/excel")
    public String importSaleDetailByExcel() {
        Dealer dealer = new Dealer();
        dealer.setCode("aaa");
        dealer.setName("bbb");
        dealer.setLevel(1);
        dealer.setArea("ccc");
        dealer.setProvince("ddd");
        dealer.setCity("eee");
        dealerService.insert(dealer);
        return "success";
    }
}