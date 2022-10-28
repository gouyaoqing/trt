package com.trt.api.controller.net;

import com.trt.api.service.net.SubGroupCompanyNetService;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.SubGroupCompany;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.SubGroupCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@RestController
@Slf4j
public class InitSubGroupCompanyController {
    @Resource
    private GroupCompanyService groupCompanyService;

    @Resource
    private SubGroupCompanyService subGroupCompanyService;

    @Resource
    private SubGroupCompanyNetService subGroupCompanyNetService;

    @PostMapping("/net/init/sub-group-company")
    public String netInitSubGroupCompany(@RequestParam("token") String token, @RequestParam("start_id") long startId) {
        List<GroupCompany> groupCompanyList = groupCompanyService.findAll();

        groupCompanyList.stream()
                .filter(groupCompany -> groupCompany.getId() >= startId)
                .sorted(Comparator.comparing(GroupCompany::getId))
                .forEach(groupCompany -> {
                    List<SubGroupCompany> subGroupCompanyList = subGroupCompanyNetService.getByGroupCompany(groupCompany, token);
                    subGroupCompanyList.forEach(subGroupCompany -> {
                        subGroupCompany.setGroupCompanyId(groupCompany.getId());
                        subGroupCompanyService.getOrInsert(subGroupCompany);
                    });
                    try {
                        Thread.sleep(Duration.ofSeconds(3).toMillis());
                    } catch (Exception e) {
                        //ignore
                    }

                });
        log.error("done");
        return "success";
    }

    @ApiOperation("手动抓取子公司")
    @PostMapping("/save/sub-group-company")
    public String saveSubGroupCompany(@ApiParam("母公司名称") @RequestParam("group_company_name") String groupCompanyName,
                                      @ApiParam("全国百强") @RequestParam("country_top_100") Boolean countryTop100,
                                      @ApiParam("重点19家") @RequestParam("important_19") Boolean important19,
                                      @ApiParam("省域百强") @RequestParam("county_top_100") Boolean countyTop100,
                                      @ApiParam("网页抓取数据") @RequestBody String response) {
        GroupCompany groupCompany = groupCompanyService.findByName(groupCompanyName).orElseThrow(() -> new IllegalArgumentException("group company 不存在"));
        if (countryTop100 || important19 || countyTop100) {
            groupCompany.setCountryTop100(countryTop100)
                    .setImportant19(important19)
                    .setCountyTop100(countyTop100);
            groupCompanyService.updateBooleanLabel(groupCompany);
        }


        List<SubGroupCompany> subGroupCompanyList = subGroupCompanyNetService.getSubGroupCompanyByJson(response);
        log.info("母公司 {} 解析子公司{}个", groupCompanyName, subGroupCompanyList.size());
        subGroupCompanyList.forEach(subGroupCompany -> {
            subGroupCompany.setGroupCompanyId(groupCompany.getId());
            subGroupCompanyService.getOrInsert(subGroupCompany);
        });

        log.info("done");
        return "母公司 " + groupCompanyName + " 已保存子公司" + subGroupCompanyList.size() + "个";
    }

}
