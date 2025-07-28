package com.masi.employee.service;

import com.masi.employee.domain.Employee;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetDTO;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetQuery;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
@Slf4j
public class TimesheetExportService {
    private final PersonalMonthlyTimesheetService personalMonthlyTimesheetService;

    @FunctionalInterface
    public interface ProcessSumHeader {
        void process(PersonalMonthlyTimesheetDTO timesheet, Row row, int cellIndex);
    }

    public static CellStyle createDefaultCellStyle(Workbook workbook, short color) {
        var cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Times New Roman");
        font.setColor(color);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.DOTTED);
        cellStyle.setBorderTop(BorderStyle.DOTTED);
        cellStyle.setBorderLeft(BorderStyle.DOTTED);
        cellStyle.setBorderRight(BorderStyle.DOTTED);
        return cellStyle;
    }

    public static CellStyle createDefaultCellStyle(Workbook workbook) {
        return createDefaultCellStyle(workbook, IndexedColors.BLACK.getIndex());
    }

    private static String getDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> "Thứ 2";
            case TUESDAY -> "Thứ 3";
            case WEDNESDAY -> "Thứ 4";
            case THURSDAY -> "Thứ 5";
            case FRIDAY -> "Thứ 6";
            case SATURDAY -> "Thứ 7";
            case SUNDAY -> "Chủ nhật";
        };
    }

    public static Cell createDefaultCell(Row row, int cellIndex, Object value) {
        var cell = row.createCell(cellIndex);
        var style = createDefaultCellStyle(row.getSheet().getWorkbook());
        style.setAlignment(HorizontalAlignment.LEFT);
        cell.setCellStyle(style);
        if (Objects.isNull(value)) {
            return cell;
        }
        switch (value.getClass().getSimpleName()) {
            case "String" -> cell.setCellValue((String) value);
            case "Integer" -> cell.setCellValue((Integer) value);
            case "Float" -> cell.setCellValue((Float) value);
            case "Double" -> cell.setCellValue((Double) value);
            case "Long" -> cell.setCellValue((Long) value);
            case "LocalDate" -> cell.setCellValue((LocalDate) value);
            default -> cell.setCellValue(value.toString());
        }
        return cell;
    }

    public static Cell createDateCell(Row row, int cellIndex, LocalDate date) {
        var cell = row.createCell(cellIndex);
        var style = createDefaultCellStyle(row.getSheet().getWorkbook());
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setWrapText(true);
        String value = getDayOfWeek(date) + "\n" + date.getDayOfMonth();
        if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            style.setFillForegroundColor(IndexedColors.RED.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    public static Cell createTimeCell(Row row, int cellIndex, String timeNote) {
        if (Objects.isNull(timeNote)) {
            timeNote = "";
        }
        timeNote = timeNote.toUpperCase(Locale.ROOT);
        // Number/KP/OFF/NV/V/P/PO/NB
        // nếu không phải number thì chữ đỏ nền vàng
        var cell = row.createCell(cellIndex);
        if (StringUtils.isNotBlank(timeNote) && "/KP/OFF/NV/V/P/PO/NB".contains(timeNote)) {
            var style = createDefaultCellStyle(row.getSheet().getWorkbook(), IndexedColors.RED.getIndex());
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            cell.setCellStyle(style);
            cell.setCellValue(timeNote);

            return cell;
            // font color red

        }
        var style = createDefaultCellStyle(row.getSheet().getWorkbook());

        String display = timeNote;
        float value = NumberUtils.toFloat(timeNote, -1);
        if (value > -1) {
            display = value % 1 == 0 ? String.valueOf((int) value) : timeNote;

        }
        cell.setCellStyle(style);
        cell.setCellValue(display);
        return cell;

    }

    private String getTimesheetName(WorkspaceType type, TimeKeepingType timeKeepingType) {
        return switch (type) {
            case OFFICE -> "Bảng chấm công văn phòng";
            case FACTORY -> switch (timeKeepingType) {
                case DRIVER -> "Bảng chấm công tài xế";
                case HOUR -> "Bảng chấm công sản xuất";
                case LOADING_UNLOADING -> "Bảng chấm công bốc xếp";
                case MIXING_FLOUR -> "Bảng chấm công trộn bột";
                case OVERTIME -> "Bảng chấm công tăng ca";
            };
        };
    }

    public static Cell createHeaderCell(Row row, int cellIndex, String header) {
        var cell = row.createCell(cellIndex);
        var style = createDefaultCellStyle(row.getSheet().getWorkbook());
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.MEDIUM);
        Font font = row.getSheet().getWorkbook().createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setWrapText(true);
        cell.setCellStyle(style);
        cell.setCellValue(header);
        return cell;
    }

    private static final Map<String, String[]> HEADER_MAP;

    static {
        HEADER_MAP = new HashMap<>();
        HEADER_MAP.put("OFFICE-HOUR", new String[]{"Giờ ca", "Số ngày\nWFH", "Lễ 300%", "Phép năm",
            "Tổng giờ tại \nVP", "Tổng công tại \nVP", "TỔNG CÔNG", "Ký tên"});

        HEADER_MAP.put("FACTORY-HOUR", new String[]{"Giờ ca", "Lễ 300%", "Ngày off hưởng \nnguyên lương", "Phép năm",
            "Tổng giờ tại \nNM", "Tổng công tại \nNM", "Tổng công", "Ngày off trong tháng", "Ký tên"});

        HEADER_MAP.put("FACTORY-OVERTIME", new String[]{"Tổng giờ\ntăng ca/người/tháng", "Ký xác nhận"});

        HEADER_MAP.put("FACTORY-DRIVER", new String[]{"Giờ ca", "Lễ 300%", "Phép năm",
            "Tổng giờ tại \nVP", "Tổng công tại \nVP", "TỔNG CÔNG", "Ký tên"});

        HEADER_MAP.put("FACTORY-LOADING_UNLOADING", new String[]{"Tổng số\n tấn/người/tháng", "Ký xác nhận"});
        HEADER_MAP.put("FACTORY-MIXING_FLOUR", new String[]{"Tổng số\n tấn/người/tháng", "Ký xác nhận"});
    }

    private static final Map<String, ProcessSumHeader> PROCESS_SUM_HEADER_MAP;

    static {
        PROCESS_SUM_HEADER_MAP = new HashMap<>();
        PROCESS_SUM_HEADER_MAP.put("FACTORY-HOUR", (timesheet, row, cellIndex) -> {
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getShiftHours()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getHoliday300()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getOffDay()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getAnnualLeave()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalHoursAtFactory()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWorkAtFactory(), 2));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWork(), 2));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getOffDayInMonth()));
            createTimeCell(row, cellIndex++, "");
        });

        PROCESS_SUM_HEADER_MAP.put("OFFICE-HOUR", (timesheet, row, cellIndex) -> {
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getShiftHours()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWorkFromHome()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getHoliday300()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getAnnualLeave()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalHoursAtFactory()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWorkAtFactory(), 2));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWork(), 2));
            createTimeCell(row, cellIndex++, "");
        });

        PROCESS_SUM_HEADER_MAP.put("FACTORY-OVERTIME", (timesheet, row, cellIndex) -> {
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getShiftHours()));
            createTimeCell(row, cellIndex++, "");
        });

        PROCESS_SUM_HEADER_MAP.put("FACTORY-DRIVER", (timesheet, row, cellIndex) -> {
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getShiftHours()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getHoliday300()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getAnnualLeave()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalHoursAtFactory()));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWorkAtFactory(), 2));
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getTotalWork(), 2));
            createTimeCell(row, cellIndex++, "");
        });

        PROCESS_SUM_HEADER_MAP.put("FACTORY-LOADING_UNLOADING", (timesheet, row, cellIndex) -> {
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getShiftHours()));
            createTimeCell(row, cellIndex++, "");
        });
        PROCESS_SUM_HEADER_MAP.put("FACTORY-MIXING_FLOUR", (timesheet, row, cellIndex) -> {
            createTimeCell(row, cellIndex++, com.carevn.masi.utils.StringUtils.toString(timesheet.getShiftHours()));
            createTimeCell(row, cellIndex++, "");
        });

    }

    private int startSumHeaderData(PersonalMonthlyTimesheetDTO timesheet, Row row, int cellIndex, WorkspaceType type,
                                   TimeKeepingType timeKeepingType) {
        var key = type.name() + "-" + timeKeepingType.name();
        PROCESS_SUM_HEADER_MAP.get(key).process(timesheet, row, cellIndex);
        return cellIndex + 8;
    }

    private int startSumHeader(Row row, int startCellIndex, WorkspaceType type, TimeKeepingType timeKeepingType) {
        var headers = HEADER_MAP.get(type.name() + "-" + timeKeepingType.name());
        for (int i = 0; i < headers.length; i++) {
            createHeaderCell(row, startCellIndex + i, headers[i]);
        }
        return startCellIndex + headers.length;
    }

    private String startParse(List<PersonalMonthlyTimesheetDTO> records, WorkspaceType type,
                              TimeKeepingType timeKeepingType, LocalDate fromDate, LocalDate toDate) throws IOException {
        if (!TimeKeepingType.HOUR.equals(timeKeepingType)) {
            type = WorkspaceType.FACTORY;
        }
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(getTimesheetName(type, timeKeepingType));
        AtomicInteger currentRowIndex = new AtomicInteger(0);

        Row titleRow = sheet.createRow(currentRowIndex.getAndIncrement()); // để tí merge rồi set value
        Row headerRow = sheet.createRow(currentRowIndex.getAndIncrement()); // để s
        int currentCellIndex = 0;
        // et header
        // số thứ tự, tên, đầu tháng - cuối tháng,
        createHeaderCell(headerRow, currentCellIndex++, "STT");
        createHeaderCell(headerRow, currentCellIndex++, "Mã NV");
        createHeaderCell(headerRow, currentCellIndex++, "Họ và tên");
        createHeaderCell(headerRow, currentCellIndex++, "Vị trí");
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            createDateCell(headerRow, currentCellIndex++, date);
        }

        int maxCol = startSumHeader(headerRow, currentCellIndex, type, timeKeepingType);
        int recordsSize = records.size();
        for (int i = 0; i < recordsSize; i++) {
            var timesheet = records.get(i);
            var employeeName = timesheet.getEmployee().getLastName() + " " + timesheet.getEmployee().getFirstName();
            var row = sheet.createRow(currentRowIndex.getAndIncrement());
            currentCellIndex = 0;

            Cell temp = createDefaultCell(row, currentCellIndex++, i + 1);
            temp.getCellStyle().setBorderLeft(BorderStyle.MEDIUM);
            createDefaultCell(row, currentCellIndex++, timesheet.getEmployee().getCode());

            createDefaultCell(row, currentCellIndex++, employeeName);
            if (timesheet.getEmployee().getEmployeeProfile() == null) {
                timesheet.getEmployee().setEmployeeProfile(new EmployeeProfileDTO());
            }
            createDefaultCell(row, currentCellIndex++, timesheet.getEmployee().getEmployeeProfile().getRole());
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                LocalDate finalDate = date;
                var time = timesheet.getTimeKeepings().stream()
                    .filter(timeKeeping -> timeKeeping.getDate().equals(finalDate)).findFirst().orElse(null);
                String timeNote;
                if (time == null) {
                    timeNote = "";
                } else if (time.getNote() == null) {
                    timeNote = time.resolveTimesheetDayValue();
                } else {
                    timeNote = time.getNote();
                }
                Cell cellT = createTimeCell(row, currentCellIndex++, timeNote);
            }
            currentCellIndex = startSumHeaderData(timesheet, row, currentCellIndex, type, timeKeepingType);
        }

        // done
        autoFitColumn(sheet, maxCol);
        // merge title
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, maxCol - 1));
        CellStyle titleStyle = createDefaultCellStyle(workbook);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 20);
        font.setFontName("Times New Roman");

        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleStyle.setWrapText(true);
        String title = String.format("%s\n(Từ %s - %s)", getTimesheetName(type, timeKeepingType),
            fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        titleRow.createCell(0).setCellValue(title);
        titleRow.getCell(0).setCellStyle(titleStyle);
        titleRow.setHeightInPoints(60);
        sheet.createFreezePane(3, 2);
        var file = Files.createTempFile("timesheet", ".xlsx");
        workbook.write(Files.newOutputStream(file));

        workbook.close();
        return file.toFile().getAbsolutePath();
    }

    public static void autoFitColumn(Sheet sheet, int maxCol) {
        for (int i = 0; i < maxCol; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public Mono<String> exportTimesheet(PersonalMonthlyTimesheetQuery query) {
        log.info("Exporting timesheet");

        return personalMonthlyTimesheetService.findAllByQuery(query, null).collectList().handle((timesheets, sink) -> {
            try {
                sink.next(startParse(timesheets, query.getWorkspaceType(), query.getType(),
                        query.getMonth().withDayOfMonth(1),
                        query.getMonth().with(TemporalAdjusters.lastDayOfMonth())));  // Ngày cuối tháng
            } catch (Exception e) {
                sink.error(e);
            }
        });

    }

    public static void main(String[] args) {
        TimesheetExportService timesheetExportService = new TimesheetExportService(null);
        // C:\Users\Laffy\AppData\Local\Temp\timesheet5271169180440992776.bin

        try {
            FileInputStream fis = new FileInputStream(
                "C:\\Users\\Laffy\\AppData\\Local\\Temp\\timesheet15880188461070232591.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<PersonalMonthlyTimesheetDTO> datas = (List<PersonalMonthlyTimesheetDTO>) ois.readObject();
            ois.close();
            var fileName = timesheetExportService.startParse(datas, WorkspaceType.OFFICE, TimeKeepingType.OVERTIME,
                LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 31));
            // open file
            File file = new File(fileName);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("Desktop is not supported. Cannot open file.");
            }
            log.info("File name: {}", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
