package com.masi.employee.service;

import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.service.dto.LeaveDayReport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Service
public class LeaveReportService {
    public byte[] generateLeaveTrackingExcel(Collection<LeaveDayReport> dataList, int year, WorkspaceType workspaceType) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        ZonedDateTime time = ZonedDateTime.now();
        String nameFile = "Leave Tracking " + year + " " + time.toString();
        Sheet sheet = workbook.createSheet(nameFile);
////////////////////////////////////////////////////////

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle headerStyle0 = createHeaderStyle0(workbook);

// Tạo hàng tiêu đề
        Row headerRow0 = sheet.createRow(0); // Hàng tiêu đề 1
        Row headerRow1 = sheet.createRow(1); // Hàng tiêu đề 1
        Row headerRow2 = sheet.createRow(2); // Hàng tiêu đề 2
        Row headerRow3 = sheet.createRow(3); // Hàng tiêu đề 3

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 21));
        Cell cell0 = headerRow0.createCell(0);
        cell0.setCellStyle(headerStyle0);
        if (workspaceType.equals(WorkspaceType.FACTORY))
            cell0.setCellValue("THEO DÕI PHÉP NĂM " + year + "_KHỐI NHÀ MÁY");
        else
            cell0.setCellValue("THEO DÕI PHÉP NĂM " + year + "_KHỐI VĂN PHÒNG");

        headerRow0.setHeight((short) 1000); // Đặt chiều cao hàng 2 là 1000 điểm


        String[] headers = {
            "Mã NV", "Tên nhân viên", "Ngày vào\nlàm", "Ngày bắt\nđầu tính\nphép",
            "Phép năm\nnay (đến\ntháng\n12/" + year + ")", "Tháng", "", "", "", "", "", "", "", "", "", "", "",
            "Tổng số\nngày phép\nđã nghỉ", "Phép", "Số ngày\nphép còn\nlại", "Hủy phép", "Trả phép"
        };

// Tạo tiêu đề cho hàng 1 và hàng 2
        for (int i = 0; i < headers.length; i++) {
            Cell cell1 = headerRow1.createCell(i);
            cell1.setCellValue(headers[i]);
            cell1.setCellStyle(headerStyle);

            // Tạo ô trong hàng 2 cho tiêu đề, chỉ cần đặt style
            Cell cell2 = headerRow2.createCell(i);
            cell2.setCellStyle(headerStyle);
        }

// Hợp nhất các ô từ cột F2 đến Q2
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 16));


// Đặt kích thước cột
        for (int i = 0; i < headers.length; i++) {
            sheet.setColumnWidth(i, 25 * 256); // Kích thước cột 25 pixels
        }
        sheet.setColumnWidth(0, 10 * 256); // Tùy chỉnh kích thước cột cụ thể (cột 1)
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 13 * 256);
        sheet.setColumnWidth(3, 13 * 256);
        sheet.setColumnWidth(4, 13 * 256);

        headerRow2.setHeight((short) 1000); // Đặt chiều cao hàng 2 là 1000 điểm
        headerRow3.setHeight((short) 500); // Đặt chiều cao hàng 3 là 500 điểm


        // Hợp nhất các ô từ dòng 2 đến dòng 3 cho từng cột
        for (int i = 0; i < headers.length; i++) {
            Cell cell1 = headerRow1.createCell(i);
            cell1.setCellValue(headers[i]);
            cell1.setCellStyle(headerStyle);

            Cell cell2 = headerRow2.createCell(i);
            cell2.setCellValue(""); // Clear the cell in row 2 if needed
            cell2.setCellStyle(headerStyle);

            // Merge dòng 2 và 3, nhưng không hợp nhất nếu cột nằm trong khoảng từ dòng 5 đến dòng 16
            if (i < 5 || i > 16) { // Ví dụ: không hợp nhất từ cột 4 đến 20 (tùy chỉnh theo yêu cầu)
                sheet.addMergedRegion(new CellRangeAddress(1, 2, i, i));
            }
        }

        // Thêm giá trị từ 1 đến 12 vào hàng 3 từ cột F3 đến Q3
        for (int i = 5; i <= 16; i++) {
            Cell cell = headerRow2.createCell(i);
            cell.setCellValue(i - 4); // Giá trị từ 1 đến 12
            cell.setCellStyle(headerStyle);

            sheet.setColumnWidth(i, 7 * 256);

        }

        sheet.setColumnWidth(17, 13 * 256); // Tùy chỉnh kích thước cột cụ thể (cột 1)
        sheet.setColumnWidth(18, 13 * 256);
        sheet.setColumnWidth(19, 13 * 256);
        sheet.setColumnWidth(20, 13 * 256);
        sheet.setColumnWidth(21, 13 * 256);

////////////////////////////////////////////////////////////////////////

        // TODO: Nạp dữ liệu nhân viên vào từ List<Employee> dataList


// Tạo style cho dữ liệu
        CellStyle dataStyle = createDataStyle(workbook);

// Nạp dữ liệu nhân viên vào từ List<Employee> dataList
        int rowNum = 3;
        for (LeaveDayReport employee : dataList) {
            LeaveDayReport emp = this.generateLeaveDayReport(employee,year);
            Row row = sheet.createRow(rowNum++);

            Cell cellData0 = row.createCell(0);
            cellData0.setCellValue(String.valueOf(emp.getEmployeeId()));
            cellData0.setCellStyle(dataStyle);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(emp.getFullName());
            cell1.setCellStyle(dataStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(emp.getJoinDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            cell2.setCellStyle(dataStyle);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(emp.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            cell3.setCellStyle(dataStyle);
            Cell cell4 = row.createCell(4);

//            if(emp.getStartDate().isBefore(LocalDate.now()))
//                cell4.setCellValue(0);
//            else
//                cell4.setCellFormula("MIN(17, MAX(1, DATEDIF(D" + rowNum + ", DATE(2025, 1, 1), \"m\") + INT(DATEDIF(C" + rowNum + ", TODAY(), \"m\") / 12)))");
            cell4.setCellFormula("IFERROR(DATEDIF(D" + rowNum + ",DATE(2025,1,1),\"m\"), 0)");


            cell4.setCellStyle(dataStyle);

            double[] monthlyLeaves = emp.getMonthlyLeaves();
            for (int i = 0; i < monthlyLeaves.length; i++) {
                Cell cell = row.createCell(5 + i);
                if (monthlyLeaves[i] == 0)
                    cell.setCellValue(""); // Cột F3 -> Q3
                else
                    cell.setCellValue(monthlyLeaves[i]);
                cell.setCellStyle(dataStyle);
            }

            Cell cell17 = row.createCell(17);
            cell17.setCellFormula("SUM(F" + rowNum + ":Q" + rowNum + ")");
            cell17.setCellStyle(dataStyle);

            Cell cell18 = row.createCell(18);
//            cell18.setCellFormula("MROUND((E" + rowNum + ")/12*MONTH(TODAY()),0.5)");
            cell18.setCellFormula("IFERROR(MROUND((TODAY()-D"+rowNum+")/30,0.5), 0)");
            cell18.setCellStyle(dataStyle);

            Cell cell19 = row.createCell(19);
            cell19.setCellFormula("IF((S" + rowNum + " - R" + rowNum + ") < 0, 0, S" + rowNum + " - R" + rowNum + ")");
            cell19.setCellStyle(dataStyle);

            Cell cell20 = row.createCell(20);
            cell20.setCellValue("");
            cell20.setCellStyle(dataStyle);

            Cell cell21 = row.createCell(21);
            cell21.setCellValue("");
            cell21.setCellStyle(dataStyle);
        }


////////////////////////////////////////////////////////////////////////


// Thêm dòng tổng
        Row totalRow = sheet.createRow(rowNum);

// Tính tổng cho cột A (0) - Đã có cột này trong dữ liệu nếu cần tổng
        Cell totalCellA = totalRow.createCell(0);
        totalCellA.setCellFormula("COUNTA(A4:A" + rowNum + ")");
        totalCellA.setCellStyle(headerStyle);

// Tính tổng cho các cột từ E đến Y (4 đến 24)
        for (int i = 1; i <= 3; i++) {
            Cell cell = totalRow.createCell(i);
            cell.setCellStyle(headerStyle);
        }
// Cột E -> 4, Cột Y -> 24
        for (int i = 4; i <= 21; i++) {
            String columnLetter = CellReference.convertNumToColString(i);
            String formula = "SUM(" + columnLetter + "4:" + columnLetter + rowNum + ")";
            Cell cell = totalRow.createCell(i);
            cell.setCellFormula(formula);
            cell.setCellStyle(headerStyle);
        }

// Tính tổng cho các cột cụ thể nếu có
        Cell totalCell17 = totalRow.createCell(17);
        totalCell17.setCellFormula("SUM(R4:R" + rowNum + ")");
        totalCell17.setCellStyle(headerStyle);

////////////////////////////////////////////////////////////////////////
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }

    private static CellStyle createHeaderStyle0(Workbook workbook) {
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 20); // Cỡ chữ 12 điểm
        font.setBold(true);

        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        headerStyle.setBorderBottom(BorderStyle.MEDIUM); // Border dưới dày
        headerStyle.setBorderTop(BorderStyle.MEDIUM);    // Border trên dày
        headerStyle.setBorderRight(BorderStyle.MEDIUM);  // Border phải dày
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);   // Border trái dày
        headerStyle.setWrapText(true);

        return headerStyle;
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13); // Cỡ chữ 12 điểm
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        headerStyle.setBorderBottom(BorderStyle.DASHED); // Border dưới dày
        headerStyle.setBorderTop(BorderStyle.DASHED);    // Border trên dày
        headerStyle.setBorderRight(BorderStyle.DASHED);  // Border phải dày
        headerStyle.setBorderLeft(BorderStyle.DASHED);   // Border trái dày
        headerStyle.setWrapText(true);

        return headerStyle;
    }


    private static CellStyle createHeaderStyle(Workbook workbook) {
        XSSFCellStyle headerStyle = (XSSFCellStyle) workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13); // Cỡ chữ 12 điểm
        font.setBold(true);

        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // Tạo màu tùy chỉnh bằng XSSFColor với mã màu #bdd6ee
        XSSFColor myColor = new XSSFColor(new java.awt.Color(189, 214, 238), new DefaultIndexedColorMap());
        headerStyle.setFillForegroundColor(myColor);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setWrapText(true);

        return headerStyle;
    }

/*    private LeaveDayReport generateLeaveDayReport(LeaveDayReport leaveDayReport) {
        LocalDate joinDate = leaveDayReport.getJoinDate();
        leaveDayReport.setStartDate(startDate);


        leaveDayReport.getMonthlyLeaves()

        return leaveDayReport;
    }*/

    public LeaveDayReport generateLeaveDayReport(LeaveDayReport leaveDayReport, int year) {
        LocalDate joinDate = leaveDayReport.getJoinDate();
        LocalDate startOfYear = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate thresholdDate = startOfYear.minusMonths(2);

        LocalDate calculatedStartDate;

        if (joinDate.isBefore(thresholdDate)) {
            calculatedStartDate = startOfYear;
        } else {
            calculatedStartDate = joinDate.plusMonths(2);
            if (joinDate.getDayOfMonth() <= 10) {
                calculatedStartDate = calculatedStartDate.withDayOfMonth(1);
            } else {
                calculatedStartDate = calculatedStartDate.plusMonths(1).withDayOfMonth(1);
            }
        }

        leaveDayReport.setStartDate(calculatedStartDate);

        double[] monthlyLeaves = new double[12]; // Khởi tạo mảng 12 tháng
        for (int i = 0; i < 12; i++) {
            monthlyLeaves[i] = 0.0;
        }
        for (LeaveDayReport.LeaveDayReportItem item : leaveDayReport.getLeaveDayReportItems()) {
            Integer month = item.month(); // Lấy tháng
            Float value = item.value();   // Lấy giá trị

            // Kiểm tra giá trị của tháng và giá trị có null không
            if (month != null && value != null) {
                if (month >= 1 && month <= 12) {
                    monthlyLeaves[month - 1] = value; // Cập nhật giá trị tương ứng với tháng
                }
            }
        }

        leaveDayReport.setMonthlyLeaves(monthlyLeaves);

        return leaveDayReport;
    }

}
