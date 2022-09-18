package com.trt.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.trt.api.model.SaleDetailExcel;
import com.trt.api.service.SaleDetailImportService;
import com.trt.common.data.model.*;
import com.trt.common.data.service.*;
import com.trt.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
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
    public void importByExcel(String excelPath) {
        List<SaleDetailExcel> saleDetailExcels = readExcel(excelPath);

        saleDetailExcels.forEach(saleDetailExcel -> {
            log.error(JSON.toJSONString(saleDetailExcel));
//            dealerService.getOrInsert(saleDetailExcel.getDealer());
//
//            customService.getOrInsert(saleDetailExcel.getCustom());
//
//            medicineService.getOrInsert(saleDetailExcel.getMedicine());
//
//            saleDetailExcel.getMedicineBatch().setMedicineId(saleDetailExcel.getMedicine().getId());
//
//            medicineBatchService.getOrInsert(saleDetailExcel.getMedicineBatch());
//
//            SaleDetail saleDetail = new SaleDetail();
//            saleDetail.setDealerId(saleDetailExcel.getDealer().getId());
//            saleDetail.setCustomId(saleDetailExcel.getCustom().getId());
//            saleDetail.setMedicineBatchId(saleDetailExcel.getMedicineBatch().getId());
//            BeanUtils.copyProperties(saleDetailExcel, saleDetail);
//
//            saleDetailService.insert(saleDetail);
        });

    }

    private List<SaleDetailExcel> readExcel(String excelPath) {
        XSSFWorkbook xssfWorkbook = null;
        List<SaleDetailExcel> result = new ArrayList<SaleDetailExcel>();
        try {
            // 创建工作簿
            xssfWorkbook = new XSSFWorkbook(new FileInputStream(excelPath));
            // 读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);

            // 获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            for (int row = 1; row <= maxRow; row++) {
                XSSFRow sheetRow = sheet.getRow(row);
                SaleDetailExcel saleDetailExcel = new SaleDetailExcel();
                result.add(saleDetailExcel);

                Dealer dealer = new Dealer();
                dealer.setArea(sheetRow.getCell(0).getStringCellValue())
                        .setProvince(sheetRow.getCell(1).getStringCellValue())
                        .setCity(sheetRow.getCell(2).getStringCellValue())
                        .setCode(sheetRow.getCell(3).getStringCellValue())
                        .setName(sheetRow.getCell(4).getStringCellValue())
                        .setLevel(NumberUtils.parse(sheetRow.getCell(5).getStringCellValue()));
                saleDetailExcel.setDealer(dealer);

                Custom custom = new Custom()
                        .setArea(sheetRow.getCell(6).getStringCellValue())
                        .setProvince(sheetRow.getCell(7).getStringCellValue())
                        .setCity(sheetRow.getCell(8).getStringCellValue())
                        .setCode(sheetRow.getCell(9).getStringCellValue())
                        .setName(sheetRow.getCell(10).getStringCellValue());
                saleDetailExcel.setCustom(custom);

                MedicineBatch medicineBatch = new MedicineBatch();
                medicineBatch.setLotNumber(sheetRow.getCell(11).getStringCellValue());
                saleDetailExcel.setMedicineBatch(medicineBatch);

                Medicine medicine = new Medicine();
                medicine.setCode(sheetRow.getCell(12).getStringCellValue())
                        .setName(sheetRow.getCell(13).getStringCellValue())
                        .setSpecification(sheetRow.getCell(14).getStringCellValue())
                        .setInitSpecification(sheetRow.getCell(15).getStringCellValue())
                        .setUnit(sheetRow.getCell(16).getStringCellValue())
                        .setPackingPcs(Double.valueOf(sheetRow.getCell(23).getNumericCellValue()).intValue())
                        .setDepartment(sheetRow.getCell(27).getStringCellValue());
                saleDetailExcel.setMedicine(medicine);

                saleDetailExcel.setSaleNum(Double.valueOf(sheetRow.getCell(17).getNumericCellValue()).intValue())
                        .setSalePrice(sheetRow.getCell(18).getNumericCellValue())
                        .setSaleAmount(sheetRow.getCell(19).getNumericCellValue())
                        .setSaleDate(sheetRow.getCell(20).getDateCellValue())
                        .setMinUnit(sheetRow.getCell(21).getStringCellValue())
                        .setMinNum(Double.valueOf(sheetRow.getCell(22).getNumericCellValue()).intValue())
                        .setPackageNum(sheetRow.getCell(23).getNumericCellValue())
                        .setMinPrice(sheetRow.getCell(24).getNumericCellValue())
                        .setSaleAmountDouble(sheetRow.getCell(25).getNumericCellValue());
            }
        } catch (IOException e) {
            log.error("import excel error", e);
        } finally {
            if (xssfWorkbook != null) {
//                xssfWorkbook.
            }
        }

        return result;
    }
}
