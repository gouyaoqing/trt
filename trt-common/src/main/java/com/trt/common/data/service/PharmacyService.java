package com.trt.common.data.service;

import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;
import com.trt.common.data.model.query.QPharmacy;

import java.util.List;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
public interface PharmacyService {
    int getOrInsert(Pharmacy pharmacy);

    int getOrInsert(Pharmacy pharmacy, GroupCompany groupCompany);

    List<Pharmacy> query(QPharmacy query,Integer limit);
}
