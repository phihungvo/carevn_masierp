package com.carevn.masi.utils.CSV;

import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CSVUtils {

    public static <T> Mono<byte[]> convertListToCSVV(List<T> records) {
        return Mono.fromCallable(() -> {
            if (records == null || records.isEmpty()) {
                return new byte[0];
            }

            StringWriter stringWriter = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(stringWriter);
            Class<?> clazz = records.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();
            String[] header = new String[fields.length];
            int index = 0;
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(CSVColumn.class);
                if (annotation != null) {
                    CSVColumn csvColumn = (CSVColumn) annotation;
                    header[index++] = csvColumn.name();
                }
            }

            String[] filteredHeader = new String[index];
            System.arraycopy(header, 0, filteredHeader, 0, index);
            csvWriter.writeNext(filteredHeader);

            for (T record : records) {
                String[] row = new String[index];
                int rowIndex = 0;
                for (Field field : fields) {
                    Annotation annotation = field.getAnnotation(CSVColumn.class);
                    if (annotation != null) {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(record);
                            row[rowIndex++] = value != null ? value.toString() : "";
                        } catch (IllegalAccessException e) {
                            row[rowIndex++] = "";
                        }
                    }
                }
                csvWriter.writeNext(row);
            }
            try {
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringWriter.toString().getBytes(StandardCharsets.UTF_8);
        });
    }

    public static <T> Mono<byte[]> convertListToExcel(List<T> records) {
        return Mono.fromCallable(() -> {
            if (records == null || records.isEmpty()) {
                return new byte[0];
            }
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet 1");
            Class<?> clazz = records.get(0).getClass();
            Field[] fields = clazz.getDeclaredFields();
            Row headerRow = sheet.createRow(0);
            int headerIndex = 0;
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(CSVColumn.class);
                if (annotation != null) {
                    CSVColumn csvColumn = (CSVColumn) annotation;
                    Cell cell = headerRow.createCell(headerIndex++);
                    cell.setCellValue(csvColumn.name());
                }
            }
            int rowIndex = 1;
            for (T record : records) {
                Row dataRow = sheet.createRow(rowIndex++);
                int cellIndex = 0;
                for (Field field : fields) {
                    Annotation annotation = field.getAnnotation(CSVColumn.class);
                    if (annotation != null) {
                        field.setAccessible(true);
                        try {
                            Object value = field.get(record);
                            Cell cell = dataRow.createCell(cellIndex++);
                            cell.setCellValue(value != null ? value.toString() : "");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                workbook.write(baos);
                workbook.close();
                return baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return new byte[0];
            }
        });
    }
}
