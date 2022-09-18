package com.trt.common.utils;

import org.apache.commons.lang3.StringUtils;

public class NumberUtils {
    public static Integer parse(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        String str1 = str.substring(0, 1);
        switch (str1) {
            case "一":
                return 1;
            case "二":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            case "七":
                return 7;
            case "八":
                return 8;
            case "九":
                return 9;
            case "十":
                return 10;
            default:
                return 0;
        }
    }
}
