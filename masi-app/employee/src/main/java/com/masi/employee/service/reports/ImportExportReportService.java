package com.masi.employee.service.reports;

import com.masi.employee.domain.UniformFormDetail;
import com.masi.employee.repository.UniformFormDetailRepository;
import com.masi.employee.service.LogisticClient;
import com.masi.employee.service.TimesheetExportService;
import io.github.subtlelib.poi.api.sheet.SheetContext;
import io.github.subtlelib.poi.api.style.AdditiveStyle;
import io.github.subtlelib.poi.api.style.Style;
import io.github.subtlelib.poi.api.style.StyleConfiguration;
import io.github.subtlelib.poi.api.workbook.WorkbookContext;
import io.github.subtlelib.poi.impl.style.defaults.DefaultStyleConfiguration;
import io.github.subtlelib.poi.impl.style.defaults.FontStyle;
import io.github.subtlelib.poi.impl.workbook.WorkbookContextFactory;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImportExportReportService {
    private final UniformFormDetailRepository uniformFormDetailRepository;
    private static final String aggregatePath = "C:\\Users\\Laffy\\Downloads\\aggregates.bin";
    private static final String changePath = "C:\\Users\\Laffy\\Downloads\\changes.bin";
    private final LogisticClient logisticClient;

    public static class UniformStockReport {
        public UniformStockAggregate start;
        public Collection<UniformStockChange> importChange;
        public Collection<UniformStockChange> exportChange;
        public UniformStockAggregate end;
    }

//    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
//    public void getReport() {
//        Mono<List<UniformStockAggregate>> stockAggregate = uniformFormDetailRepository.getUniformStockAggregate().collect(Collectors.toList());
//        ZonedDateTime start = ZonedDateTime.of(2024, 8, 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
//        ZonedDateTime end = ZonedDateTime.of(2024, 9, 30, 0, 0, 0, 0, ZonedDateTime.now().getZone());
//        Mono<List<UniformStockChange>> stockChanges = uniformFormDetailRepository.getUniformStockChange(start, end).collect(Collectors.toList());
//        Mono.zip(stockAggregate, stockChanges).flatMap(tuple -> {
//            try {
//                File aggregates = Files.createFile(Path.of(aggregatePath)).toFile();
//                File changes = Files.createFile(Path.of(changePath)).toFile();
//                ObjectOutputStream aggregateStream = new ObjectOutputStream(Files.newOutputStream(aggregates.toPath()));
//                ObjectOutputStream changeStream = new ObjectOutputStream(Files.newOutputStream(changes.toPath()));
//                aggregateStream.writeObject(tuple.getT1());
//                changeStream.writeObject(tuple.getT2());
//                aggregateStream.close();
//                changeStream.close();
//            } catch (IOException e) {
//                return Mono.error(new RuntimeException(e));
//            }
//            return Mono.empty();
//        }).then().subscribe();
//
//
//    }

    public Mono<String> getReport(LocalDate from, LocalDate to, String company) {
        Mono<List<UniformStockAggregate>> stockAggregate = uniformFormDetailRepository.getUniformStockAggregate(company).collect(Collectors.toList());
        ZonedDateTime start = from.atStartOfDay().atZone(ZonedDateTime.now().getZone());
        ZonedDateTime end = to.atStartOfDay().atZone(ZonedDateTime.now().getZone()).plusDays(1);
        Mono<List<UniformStockChange>> stockChanges = uniformFormDetailRepository.getUniformStockChange(start, end, company).collect(Collectors.toList());
        return Mono.zip(stockAggregate, stockChanges).flatMap(tuple -> {
            var reports = makeReport(tuple.getT1(), tuple.getT2(), start, end);

            return reports
                .flatMap(rs -> {
                    Map<UUID, LinkedList<UniformStockAggregate>> mapUnit = new HashMap<>();
                    rs.forEach(report -> {
                        if (report.start.getUnitId() != null) {
                            mapUnit.putIfAbsent(report.start.getUnitId(), new LinkedList<>());
                            mapUnit.get(report.start.getUnitId()).add(report.start);
                        }
                    });
                    java.util.List<UUID> listUnitId = mapUnit.keySet().stream().toList();
                    return logisticClient.getUomByListIds(listUnitId)
                        .map(uom -> {
                            var list = mapUnit.get(uom.getId());
                            list.forEach(aggregate -> {
                                aggregate.setUnitName(uom.getName());
                            });
                            return uom;
                        }).collectList().then(Mono.just(rs));
                })
                .map(rs -> exportExcelV2(rs, from, to));
        });


    }

    private Mono<List<UniformStockReport>> makeReport(List<UniformStockAggregate> stockAggregate, List<UniformStockChange> stockChanges, ZonedDateTime start, ZonedDateTime end) {
        return Flux.fromIterable(stockAggregate).flatMap(aggregate -> {
            UniformStockReport report = new UniformStockReport();
            var uniformChanges = stockChanges.stream().filter(change -> change.getId().equals(aggregate.getId())).toList();
            report.exportChange = uniformChanges
                .stream()
                .filter(change -> change.isOut() && change.getDate().isAfter(start) && change.getDate().isBefore(end))
                .collect(Collectors.toList());// lấy ra các lần xuất kho trong kỳ
            report.importChange =
                uniformChanges
                    .stream()
                    .filter(change -> change.isIn() && change.getDate().isAfter(start) && change.getDate().isBefore(end))
                    .collect(Collectors.toList()); // lấy ra các lần nhập kho trong kỳ
            report.start = aggregate
                .toBuilder()
                .build()
                .rollback(uniformChanges);  // khôi phục trạng thái kho về thời điểm đầu kỳ
            report.end = aggregate
                .toBuilder()
                .build()
                .rollback(uniformChanges.stream().filter(change -> change.getDate().isAfter(end)).collect(Collectors.toList()))
                .rollback(uniformChanges.stream().filter(change -> change.getDate().isAfter(end)).collect(Collectors.toList())); // khôi phục trạng thái kho về thời điểm cuối kỳ
            return Mono.just(report);
        }).collectList();
    }

    private String exportExcelV2(List<UniformStockReport> reports, LocalDate from, LocalDate to) {
        WorkbookContext workbookCtx = WorkbookContextFactory.useXlsx().createWorkbook();
        SheetContext sheetCtx = workbookCtx.createSheet("Báo cáo nhập xuất kho");
        sheetCtx.setHeaderStyle((workbookContext, cellStyle) -> {
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            var nativeWorkbook = workbookContext.toNativeWorkbook();
            Font font = nativeWorkbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 12);
            cellStyle.setFont(font);

        });
        sheetCtx.nextRow()
            .setRowHeight(30)
            .mergeCells(13)
            .header("BÁO CÁO NHẬP XUẤT KHO \n( TỪ " + from.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " ĐẾN " + to.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " )")
        ;


        sheetCtx.nextRow()
            .header("STT")//0
            .header("MÃ HÀNG") //1
            .header("TÊN HÀNG HÓA") //2
            .header("ĐVT") //3
            .header("ĐƠN GIÁ")  //4
            .mergeCells(2)
            .header("TỒN ĐẦU KỲ") //5
            .mergeCells(2)

            .header("NHẬP TRONG KỲ")//7
            .mergeCells(2)

            .header("XUẤT TRONG KỲ") //9
            .mergeCells(2)

            .header("TỒN CUỐI KỲ");//11
        var subHeaderRow = sheetCtx.nextRow();

        subHeaderRow
            .cellAt(5)
            .text("SL")
            .text("TRỊ GIÁ")
            .text("SL")
            .text("TRỊ GIÁ")
            .text("SL")
            .text("TRỊ GIÁ")
            .text("SL")
            .text("TRỊ GIÁ");
        ;
        for (int i = 0; i < reports.size(); i++) {
            var report = reports.get(i);
            var row = sheetCtx.nextRow()
                .number(i + 1)
                .text(report.start.getCode())
                .text(report.start.getName())
                .text(report.start.getUnitName())
                .number(report.start.getBasePrice())
                .number(report.start.getStock())
                .number(report.start.getValuation())
                .number(report.importChange.stream().mapToInt(UniformStockChange::getQuantity).sum())
                .number(report.importChange.stream().mapToDouble(change -> change.getQuantity() * report.start.getBasePrice()).sum())
                .number(report.exportChange.stream().mapToInt(UniformStockChange::getQuantity).sum())
                .number(report.exportChange.stream().mapToDouble(change -> change.getQuantity() * report.start.getBasePrice()).sum())
                .number(report.end.getStock())
                .number(report.end.getValuation());

        }
        var workbook = workbookCtx.toNativeWorkbook();
        for (int i = 0; i < 13; i++) {
            workbook.getSheetAt(0).autoSizeColumn(i, true);
        }
        try {
            var tempFile = Files.createTempFile("baocao", ".xlsx");
            var nativeBytes = workbookCtx.toNativeBytes();
            Files.write(tempFile, nativeBytes);
            return tempFile.toFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        // read report from file
        ObjectInputStream aggregateStream = new ObjectInputStream(Files.newInputStream(Path.of(aggregatePath)));
        ObjectInputStream changeStream = new ObjectInputStream(Files.newInputStream(Path.of(changePath)));
        List<UniformStockAggregate> stockAggregates = (List<UniformStockAggregate>) aggregateStream.readObject();
        List<UniformStockChange> stockChanges = (List<UniformStockChange>) changeStream.readObject();
        aggregateStream.close();
        changeStream.close();
        ZonedDateTime start = ZonedDateTime.of(2024, 8, 1, 0, 0, 0, 0, ZonedDateTime.now().getZone());
        ZonedDateTime end = ZonedDateTime.of(2024, 9, 30, 0, 0, 0, 0, ZonedDateTime.now().getZone());
        var reportsService = new ImportExportReportService(null, null);
        var reports = reportsService.makeReport(stockAggregates, stockChanges, start, end).block();
        assert reports != null;
        var file = reportsService.exportExcelV2(reports, LocalDate.of(2024, 8, 1), LocalDate.of(2024, 9, 30));
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File(file));
        } else {
            System.out.println("Desktop is not supported. Cannot open file.");
        }
    }
}
