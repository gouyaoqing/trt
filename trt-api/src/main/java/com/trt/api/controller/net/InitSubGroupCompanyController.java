package com.trt.api.controller.net;

import com.trt.api.service.net.SubGroupCompanyNetService;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.SubGroupCompany;
import com.trt.common.data.service.GroupCompanyService;
import com.trt.common.data.service.SubGroupCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
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

}
