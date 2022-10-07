package com.trt.api.exceldeal;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BaiQiangXian {
    public static void main(String[] args) {
        XSSFWorkbook xssfWorkbook = null;
        try {
            // 创建工作簿
            xssfWorkbook = new XSSFWorkbook(new FileInputStream("/Users/gouyaoqing/private/trt/县百强1006的副本.xlsx"));
            // 读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            List<String> baiQiang = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                //获取百强县
                baiQiang.add(sheet.getRow(i).getCell(6).getStringCellValue());
            }

            for (int i = 1; i <= 100; i++) {
                try {
                    XSSFRow sheetRow = sheet.getRow(i);
                    String company = sheetRow.getCell(0).getStringCellValue();
                    String companySheng = sheetRow.getCell(1).getStringCellValue();
                    String companyCity = sheetRow.getCell(2).getStringCellValue();
                    String companyXian = sheetRow.getCell(3).getStringCellValue();
                    boolean isBaiQiang = false;

                    if (companyXian.equals("-") || companyXian.endsWith("区")) {
                        isBaiQiang = baiQiang.stream().anyMatch(baiqiang -> baiqiang.contains(companyCity));
                    } else {
                        isBaiQiang = baiQiang.stream().anyMatch(baiqiang -> baiqiang.contains(companyXian));
                    }


                    //insert
                    sheetRow.createCell(5).setCellValue(isBaiQiang ? "是" : "否");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xssfWorkbook != null) {
                try {
                    OutputStream outputStream = new FileOutputStream("/Users/gouyaoqing/private/trt/县百强1006的副本.xlsx");
                    xssfWorkbook.write(outputStream);
//                    xssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}