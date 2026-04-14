package com.careir.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelUtil {
    private ExcelUtil() {
    }

    public static Object[][] getSheetData(String resourcePath, String sheetName) {
        try (InputStream input = ExcelUtil.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                return new Object[][] {{"demo.user@careir.com", "Password@123"}};
            }

            try (Workbook workbook = new XSSFWorkbook(input)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet not found: " + sheetName);
                }

                DataFormatter formatter = new DataFormatter();
                int rowCount = sheet.getPhysicalNumberOfRows();
                if (rowCount <= 1) {
                    return new Object[0][0];
                }

                Row header = sheet.getRow(0);
                int colCount = header.getPhysicalNumberOfCells();
                List<Object[]> rows = new ArrayList<>();

                for (int i = 1; i < rowCount; i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    Object[] data = new Object[colCount];
                    for (int j = 0; j < colCount; j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        data[j] = formatter.formatCellValue(cell);
                    }
                    rows.add(data);
                }
                return rows.toArray(new Object[0][]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + resourcePath, e);
        }
    }
}
