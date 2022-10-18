package com.trt.api.service.net;

import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.SubGroupCompany;

import java.util.List;

public interface SubGroupCompanyNetService {
    List<SubGroupCompany> getByGroupCompany(GroupCompany groupCompany, String token);
}
