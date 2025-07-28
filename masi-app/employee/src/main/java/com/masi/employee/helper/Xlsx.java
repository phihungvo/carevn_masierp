package com.masi.employee.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Collection;

public class Xlsx {
//    @Target(value = {ElementType.METHOD, ElementType.FIELD})
//    @Retention(value = RetentionPolicy.SOURCE)
//    public @interface ExcelCell {
//        int column();
//
//        int name();
//    }

    private static void createHeaderCell(Row headerRow, int column, String name, int fontSize) {
        Cell cell = headerRow.createCell(column);
        CellStyle cellStyle = headerRow.getSheet().getWorkbook().createCellStyle();
        Font font = headerRow.getSheet().getWorkbook().createFont();
        font.setBold(true);
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) fontSize);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(name);
    }

    private static void createDataCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        CellStyle cellStyle = row.getSheet().getWorkbook().createCellStyle();
        Font font = row.getSheet().getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }
//    @Target(value = {ElementType.FIELD}) // for a list of fields
//    @Retention(value = RetentionPolicy.RUNTIME)
//    public @interface GroupedRow {
//        Class<?> value();
//    }

    public static <T> void toXlsx(File file, Class<T> clazz, Collection<T> data) {
        Workbook workbook = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            workbook = new XSSFWorkbook();
            int rowIndex = 0;
            Sheet sheet = workbook.createSheet("Sheet1");
            var isHasSheetTitle = clazz.isAnnotationPresent(SheetTitle.class);
            if (isHasSheetTitle) {
                Row titleRow = sheet.createRow(rowIndex++);
                SheetTitle sheetTitle = clazz.getAnnotation(SheetTitle.class);
                createHeaderCell(titleRow, 0, sheetTitle.value(), 20);
                rowIndex++;
            }
            Row headerRow = sheet.createRow(rowIndex++);
            for (Field field : clazz.getDeclaredFields()) {
                ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
                if (excelCell != null) {
                    createHeaderCell(headerRow, excelCell.column(), excelCell.name(), 16);
                }
            }
            for (T item : data) {
                Row row = sheet.createRow(rowIndex++);
                for (Field field : clazz.getDeclaredFields()) {
                    ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
                    if (excelCell != null) {
                        field.setAccessible(true);
                        Object value = field.get(item);
                        createDataCell(row, excelCell.column(), value == null ? "" : value.toString());
                    }
                }
            }
            if (isHasSheetTitle) {
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerRow.getLastCellNum() - 1));
                CellStyle cellStyle = sheet.getRow(0).getCell(0).getCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                sheet.getRow(0).getCell(0).setCellStyle(cellStyle);
            }
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
