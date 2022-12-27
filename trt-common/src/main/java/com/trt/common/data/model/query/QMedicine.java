package com.trt.common.data.model.query;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class QMedicine {
    private String keyword;

    private String category1;

    private String category2;

    private String huanCai;

    private String department;
}
