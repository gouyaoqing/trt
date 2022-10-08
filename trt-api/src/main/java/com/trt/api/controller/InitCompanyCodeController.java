package com.trt.api.controller;

import com.trt.common.data.model.Custom;
import com.trt.common.data.service.CustomService;
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
public class InitCompanyCodeController {
    @Resource
    private CustomService customService;

    @PostMapping("/excel/initCustomCode")
    public String initCompanyCode(@RequestParam("excelPath") String excelPath) {
        Map<String, Custom> customMap = customService.findAll().stream().collect(Collectors.toMap(Custom::getName, Function.identity()));

        XSSFWorkbook xssfWorkbook = null;
        int notExist = 0;
        try {
            // 创建工作簿
            xssfWorkbook = new XSSFWorkbook(new FileInputStream(excelPath));
            // 读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);


            for (int i = 3; i < sheet.getLastRowNum(); i++) {
                try {
                    XSSFRow sheetRow = sheet.getRow(i);
                    String companyName = sheetRow.getCell(0).getStringCellValue();
                    Custom custom = customMap.get(companyName);

                    companyName = companyName.replace("（", "(");
                    companyName = companyName.replace("）", ")");
                    companyName = companyName.replace("曾用名：", "");

                    if (custom == null) {
                        custom = customMap.get(companyName);
                    }

                    if (custom == null) {
                        if (companyName.contains("(")) {
                            String[] names = companyName.split("\\(");
                            custom = customMap.get(names[0]);
                            if (custom == null) {
                                custom = customMap.get(names[1].substring(0, names[1].length() - 1));
                            }
                        }
                    }

                    String industry = sheetRow.getCell(12).getStringCellValue();
                    String societyCode = sheetRow.getCell(13).getStringCellValue();
                    String taxesCode = sheetRow.getCell(15).getStringCellValue();
                    String registerCode = sheetRow.getCell(16).getStringCellValue();
                    String organizationCode = sheetRow.getCell(17).getStringCellValue();
                    String address = sheetRow.getCell(21).getStringCellValue();

                    if (custom == null) {
                        if (parse(industry) == null
                                && parse(societyCode) == null
                                && parse(taxesCode) == null
                                && parse(registerCode) == null
                                && parse(organizationCode) == null
                                && parse(address) == null) {
                            continue;
                        }
                        notExist++;
                        System.out.println("不存在 " + companyName);
                        continue;
                    }
                    //update
                    custom.setIndustry(parse(industry))
                            .setSocietyCode(parse(societyCode))
                            .setTaxesCode(parse(taxesCode))
                            .setRegisterCode(parse(registerCode))
                            .setOrganizationCode(parse(organizationCode))
                            .setAddress(parse(address));

                    if (custom.getIndustry() == null
                            && custom.getSocietyCode() == null
                            && custom.getTaxesCode() == null
                            && custom.getRegisterCode() == null
                            && custom.getOrganizationCode() == null
                            && custom.getAddress() == null) {
                        continue;
                    }
                    customService.updateExcelInfo(custom);
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
