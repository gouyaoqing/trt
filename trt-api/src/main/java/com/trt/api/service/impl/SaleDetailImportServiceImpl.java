package com.trt.api.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.trt.api.model.ExcelDemo;
import com.trt.api.model.SaleDetailExcel;
import com.trt.api.service.SaleDetailImportService;
import com.trt.common.data.model.*;
import com.trt.common.data.service.*;
import com.trt.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SaleDetailImportServiceImpl implements SaleDetailImportService {
    @Resource
    private DealerService dealerService;

    @Resource
    private CustomService customService;

    @Resource
    private MedicineService medicineService;

    @Resource
    private MedicineBatchService medicineBatchService;

    @Resource
    private SaleDetailService saleDetailService;

    @Override
    public void importByExcel(String excelPath, String excelName) {
        List<SaleDetailExcel> saleDetailExcels = readExcel(excelPath, excelName);

        saleDetailService.deleteByExcelName(excelName);

        saleDetailExcels.forEach(saleDetailExcel -> {
            try {
                dealerService.getOrInsert(saleDetailExcel.getDealer());

                customService.getOrInsert(saleDetailExcel.getCustom());

                medicineService.getOrInsert(saleDetailExcel.getMedicine());

                saleDetailExcel.getMedicineBatch().setMedicineId(saleDetailExcel.getMedicine().getId());

                medicineBatchService.getOrInsert(saleDetailExcel.getMedicineBatch());

                SaleDetail saleDetail = new SaleDetail();
                saleDetail.setDealerId(saleDetailExcel.getDealer().getId());
                saleDetail.setCustomId(saleDetailExcel.getCustom().getId());
                saleDetail.setMedicineBatchId(saleDetailExcel.getMedicineBatch().getId());
                BeanUtils.copyProperties(saleDetailExcel, saleDetail);

                saleDetailService.insert(saleDetail);
            } catch (Exception e) {
                log.error("insert data error .{}", saleDetailExcel, e);
            }
//            log.error(JSON.toJSONString(saleDetailExcel));

        });
        log.error("done");
    }

    private List<SaleDetailExcel> readExcel(String excelPath, String excelName) {
        Workbook xssfWorkbook = null;
        List<SaleDetailExcel> result = new ArrayList<SaleDetailExcel>();
        try {
            EasyExcel.read(excelPath, ExcelDemo.class, new PageReadListener<ExcelDemo>(dataList -> {
                for (ExcelDemo demoData : dataList) {
                    try {
                        SaleDetailExcel saleDetailExcel = new SaleDetailExcel();
                        result.add(saleDetailExcel);

                        Dealer dealer = new Dealer();
                        dealer.setArea(demoData.getDealerArea())
                                .setProvince(demoData.getDealerProvince())
                                .setCity(demoData.getDealerCity())
                                .setCode(demoData.getDealerCode())
                                .setName(demoData.getDealerName())
                                .setLevel(NumberUtils.parse(demoData.getDealerLevel()));
                        saleDetailExcel.setDealer(dealer);

                        Custom custom = new Custom()
                                .setArea(demoData.getCustomArea())
                                .setProvince(demoData.getCustomProvince())
                                .setCity(demoData.getCustomCity())
                                .setCode(demoData.getCustomCode())
                                .setName(demoData.getCustomName())
                                .setBusinessType(demoData.getBusinessType());
                        saleDetailExcel.setCustom(custom);

                        MedicineBatch medicineBatch = new MedicineBatch();
                        medicineBatch.setLotNumber(demoData.getMedicineLotNum());
                        saleDetailExcel.setMedicineBatch(medicineBatch);

                        Medicine medicine = new Medicine();
                        medicine.setCode(demoData.getMedicineCode())
                                .setName(demoData.getMedicineName())
                                .setSpecification(demoData.getMedicineSpecification())
                                .setInitSpecification(demoData.getMedicineInitSpecification())
                                .setUnit(demoData.getMedicineUnit())
                                .setPackingPcs(demoData.getPackingPcs())
                                .setDepartment(demoData.getDepartment());
                        saleDetailExcel.setMedicine(medicine);

                        saleDetailExcel.setSaleNum(demoData.getSaleNum())
                                .setSalePrice(demoData.getSalePrice())
                                .setSaleAmount(demoData.getSaleAmount())
                                .setSaleDate(demoData.getSaleDate())
                                .setMinUnit(demoData.getMinUnit())
                                .setMinNum(demoData.getMinNum())
                                .setPackageNum(demoData.getPackageNum())
                                .setMinPrice(demoData.getMinPrice())
                                .setSaleAmountDouble(demoData.getSaleAmountDouble())
                                .setExcel(excelName);
                    } catch (Exception e) {
                        log.error("read excel error.{}", demoData, e);
                    }

                }
            })).sheet().doRead();

            // 创建工作簿
//            FileInputStream stream = new FileInputStream(excelPath);
//            xssfWorkbook = StreamingReader.builder()
//                    .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
//                    .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
//                    .open(stream);
////            xssfWorkbook = new XSSFWorkbook(new FileInputStream(excelPath));
//            // 读取第一个工作表
//            Sheet sheet = xssfWorkbook.getSheetAt(0);
//
//            // 获取最后一行的num，即总行数。此处从0开始计数
//            int maxRow = sheet.getLastRowNum();
//            maxRow = 100;
//            for (int row = 1; row <= maxRow; row++) {
//                Row sheetRow = sheet.getRow(row);
//
//            }
        } catch (Exception e) {
            log.error("import excel error", e);
        } finally {
            if (xssfWorkbook != null) {
//                xssfWorkbook.
            }
        }

        return result;
    }
}
