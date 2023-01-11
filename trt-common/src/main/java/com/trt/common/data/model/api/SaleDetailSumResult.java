package com.trt.common.data.model.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SaleDetailSumResult {
    private Double saleAmount;
    private Double packageNum;
}
