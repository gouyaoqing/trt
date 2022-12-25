package com.trt.common.data.model.query;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QPharmacy {
    private String keyword;

    private String province;

    private String city;

    private String district;
}
