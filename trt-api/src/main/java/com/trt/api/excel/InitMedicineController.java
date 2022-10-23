package com.trt.api.excel;

import com.trt.common.data.model.Medicine;
import com.trt.common.data.service.MedicineService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class InitMedicineController {
    @Resource
    private MedicineService medicineService;

    @PostMapping("/excel/init-medicine")
    public String initMedicine(@RequestParam("excelPath") String excelPath) {
        Map<Long, Medicine> medicineMap = medicineService.findAll().stream().collect(Collectors.toMap(Medicine::getId, Function.identity()));

        XSSFWorkbook xssfWorkbook = null;
        int notExist = 0;
        try {
            // 创建工作簿
            xssfWorkbook = new XSSFWorkbook(new FileInputStream(excelPath));
            // 读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);


            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                try {
                    XSSFRow sheetRow = sheet.getRow(i);
                    String idStr = sheetRow.getCell(0).getStringCellValue();
                    Medicine medicine = medicineMap.get(Long.parseLong(idStr));

                    if (medicine == null) {
                        log.error("没有 " + idStr);
                        notExist++;
                        continue;
                    }

                    String companyCode = sheetRow.getCell(8).getStringCellValue();
                    String alias = sheetRow.getCell(9).getStringCellValue();
                    String category1 = sheetRow.getCell(10).getStringCellValue();
                    String category2 = sheetRow.getCell(11).getStringCellValue();
                    String description = sheetRow.getCell(12).getStringCellValue();
                    String huanCai = sheetRow.getCell(13).getStringCellValue();
                    String yuYao300 = sheetRow.getCell(14).getStringCellValue();

                    //update
                    medicine.setCompanyCode(companyCode)
                            .setAlias(alias)
                            .setCategory1(category1)
                            .setCategory2(category2)
                            .setDescription(description)
                            .setHuanCai(huanCai)
                            .setYuYao300(StringUtils.isNotBlank(yuYao300));

                    medicineService.updateSameField(medicine);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xssfWorkbook != null) {
                try {
                    OutputStream outputStream = new FileOutputStream(excelPath);
                    xssfWorkbook.write(outputStream);
//                    xssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("done.not exist=" + notExist);
        return "success";
    }

    private String parse(String value) {
        if (value == null || value.equals("-")) {
            return null;
        }
        return value;
    }
}
