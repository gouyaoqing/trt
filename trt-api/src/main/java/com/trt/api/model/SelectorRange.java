package com.trt.api.model;

import lombok.Data;

import java.util.List;

@Data
public class SelectorRange {
    private String label;

    private List<SelectorRange> children;
}
