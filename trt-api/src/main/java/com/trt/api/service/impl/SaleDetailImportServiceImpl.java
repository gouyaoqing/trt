package com.trt.api.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.trt.api.model.EasyExcelDemo;
import com.trt.api.model.ExcelDemo;
import com.trt.api.model.SaleDetailExcel;
import com.trt.api.service.SaleDetailImportService;
import com.trt.common.data.model.*;
import com.trt.common.data.service.*;
import com.trt.common.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SaleDetailImportServiceImpl implements SaleDetailImportService {
    private static final String ZHI_GONG_DEALER_CODE = "H00000";
    private static final String ZHI_GONG_MEDICINE_BATCH_CODE = "M00000";

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

    @Override
    public void importZhiGongByExcel(String excelPath, String excelName) throws IllegalAccessException {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(custom -> custom.getName().trim(), Function.identity(), BinaryOperator.maxBy(Comparator.comparing(Custom::getId))));
        Dealer dealer = new Dealer()
                .setCode(ZHI_GONG_DEALER_CODE)
                .setName("同仁堂科技公司")
                .setArea("华北区")
                .setCity("北京市")
                .setProvince("北京市")
                .setLevel(0);
        dealerService.getOrInsert(dealer);

        List<SaleDetailExcel> saleDetailData = readZhiGongExcel(excelPath, excelName);

        //check
        String notExistCustom = saleDetailData.stream()
                .map(SaleDetailExcel::getCustom)
                .map(Custom::getName)
                .map(name -> {
                    name = name.replace("（", "(");
                    name = name.replace("）", ")");
                    return name;
                })
                .filter(customName -> !customMap.containsKey(customName))
                .distinct()
                .collect(Collectors.joining(","));


        if (notExistCustom.length() > 0) {
            log.error("客户不存在。" + notExistCustom);
            return;
//            throw new IllegalAccessException("客户不存在" + notExistCustom);
        }


        saleDetailService.deleteByExcelName(excelName);

        List<Medicine> medicines = medicineService.findAll();
        Set<String> notExistNameSpecification = new HashSet<>();

        saleDetailData.forEach(saleDetailExcel -> {
            try {
                Custom custom = customMap.get(saleDetailExcel.getCustom().getName().replace("（", "(").replace("）", ")"));

                if (custom == null) {
                    return;
                }

                Medicine medicine;
                if (StringUtils.isBlank(saleDetailExcel.getMedicine().getCode())) {
                    //匹配药品别名
                    medicine = medicines.stream()
                            .filter(m -> StringUtils.isNotBlank(m.getNameSpecification()) && m.getNameSpecification().equalsIgnoreCase(saleDetailExcel.getMedicine().getName() + saleDetailExcel.getMedicine().getSpecification()))
                            .findFirst().orElse(null);
                } else {
                    //匹配药品code
                    medicine = medicineService.findByCode(saleDetailExcel.getMedicine().getCode());
                    if (medicine != null) {
                        medicine.setNameSpecification(saleDetailExcel.getMedicine().getNameSpecification());
                    }
                }

                if (medicine == null) {
                    if (!notExistNameSpecification.contains(saleDetailExcel.getMedicine().getCode() + " " + saleDetailExcel.getMedicine().getName() + saleDetailExcel.getMedicine().getSpecification())) {
                        log.error(saleDetailExcel.getMedicine().getCode() + " " + saleDetailExcel.getMedicine().getName() + saleDetailExcel.getMedicine().getSpecification() + " is not exist");
                        notExistNameSpecification.add(saleDetailExcel.getMedicine().getCode() + " " + saleDetailExcel.getMedicine().getName() + saleDetailExcel.getMedicine().getSpecification());
                    }
                    return;
                }


                if (StringUtils.isNotBlank(medicine.getNameSpecification()) && medicine.getId() != null) {
                    medicineService.updateNameSpecification(medicine);
                }

                MedicineBatch medicineBatch = new MedicineBatch()
                        .setMedicineId(medicine.getId())
                        .setLotNumber(ZHI_GONG_MEDICINE_BATCH_CODE);

                medicineBatchService.getOrInsert(medicineBatch);

                SaleDetail saleDetail = new SaleDetail();
                BeanUtils.copyProperties(saleDetailExcel, saleDetail);

                saleDetail.setDealerId(dealer.getId());
                saleDetail.setCustomId(custom.getId());
                saleDetail.setMedicineBatchId(medicineBatch.getId());
                saleDetail.setZhiGong(true);
//                saleDetail.setPackageNum(BigDecimal.valueOf((double) saleDetailExcel.getSaleNum() / medicine.getPackingPcs()).setScale(2, RoundingMode.HALF_UP).doubleValue());
                saleDetail.setExcel(excelName);
                saleDetail.setSaleDate(saleDetailExcel.getSaleDate());

                saleDetailService.insert(saleDetail);
            } catch (Exception e) {
                log.error("insert data error .{}", saleDetailExcel, e);
            }

        });
        log.error("产品不存在：" + String.join(",", notExistNameSpecification));
        log.error("done");

    }

    private List<SaleDetailExcel> readZhiGongExcel(String excelPath, String excelName) {
        List<SaleDetailExcel> result = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            EasyExcel.read(excelPath, EasyExcelDemo.class, new PageReadListener<EasyExcelDemo>(dataList -> {
                for (EasyExcelDemo data : dataList) {
                    try {
                        if (StringUtils.isNotBlank(data.getA()) && !data.getA().equals("客户名称")) {
                            SaleDetailExcel saleDetailExcel = new SaleDetailExcel();
                            result.add(saleDetailExcel);

                            Custom custom = new Custom()
                                    .setName(data.getA());
                            saleDetailExcel.setCustom(custom);

                            saleDetailExcel.setMedicineBatch(null);

                            Medicine medicine = new Medicine();
                            medicine.setCode(data.getP());
                            medicine.setNameSpecification(data.getO());
                            medicine.setName(data.getF());
                            medicine.setSpecification(data.getG());

                            saleDetailExcel.setSaleNum(new Double(Double.parseDouble(data.getK())).intValue())
                                    .setSalePrice(Double.parseDouble(data.getI()))
                                    .setSaleAmount(Double.parseDouble(data.getL()))
                                    .setSaleDate(formatter.parse(data.getM()))
                                    .setSaleAmountDouble(Double.parseDouble(data.getL()))
                                    .setMedicine(medicine)
                                    .setExcel(excelName);
                        }
                    } catch (Exception e) {
                        log.error("readZhiGongExcel error", e);
                    }

                }
            })).sheet().doRead();
        } catch (Exception e) {
            log.error("readZhiGongExcel error", e);
        }
        return result;
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
                                .setLevel(NumberUtils.parse(demoData.getDealerLevel()))
                                .setSocietyCode(demoData.getDealerSocietyCode());
                        saleDetailExcel.setDealer(dealer);

                        Custom custom = new Custom()
                                .setArea(demoData.getCustomArea())
                                .setProvince(demoData.getCustomProvince())
                                .setCity(demoData.getCustomCity())
                                .setCode(demoData.getCustomCode())
                                .setName(demoData.getCustomName())
                                .setBusinessType(demoData.getBusinessType())
                                .setSocietyCode(demoData.getCustomSocietyCode());
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
                                .setDepartment(demoData.getDepartment())
                                .setHuanCai(demoData.getHuanCai())
                                .setYuYao300("是".equals(demoData.getYuYao()));
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
