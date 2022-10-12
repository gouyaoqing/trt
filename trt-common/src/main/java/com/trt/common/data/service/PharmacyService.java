package com.trt.common.data.service;

import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
public interface PharmacyService {
    int getOrInsert(Pharmacy pharmacy);

    int getOrInsert(Pharmacy pharmacy, GroupCompany groupCompany);
}
