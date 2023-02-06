package com.trt.api.model;

import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;
import com.trt.common.data.model.SubGroupCompany;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PharmacyInsertModel {
    private Pharmacy pharmacy;

    private GroupCompany groupCompany;

    private SubGroupCompany subGroupCompany;
}
