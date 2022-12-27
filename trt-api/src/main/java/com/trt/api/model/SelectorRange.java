package com.trt.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SelectorRange {
    private String label;

    private List<SelectorRange> children;
}
