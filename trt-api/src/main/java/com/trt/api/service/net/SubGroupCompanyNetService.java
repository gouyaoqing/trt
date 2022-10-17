package com.trt.api.service.net;

import com.trt.common.data.model.SubGroupCompany;

import java.util.List;

public interface SubGroupCompanyNetService {
    List<SubGroupCompany> getByGroupCompany(String groupCompanyName,String token);
}
