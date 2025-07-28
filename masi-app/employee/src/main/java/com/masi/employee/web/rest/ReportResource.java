package com.masi.employee.web.rest;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.UniformRelease;
import com.masi.employee.domain.enumeration.UniformReleaseType;
import com.masi.employee.helper.Xlsx;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.reports.*;
import com.poiji.bind.Poiji;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@AllArgsConstructor
public class ReportResource {
    private final ReportService reportService;
    private final ImportExportReportService importExportReportService;

    @Operation(summary = " Truy cập báo cáo theo dõi NV sắp hết hạn HĐ")
    @GetMapping("/employee-expiring-contract")
    public Mono<ResponseEntity<ApiResponse<EmployeeProfileDTO>>> getEmployeeExpiringContractReport(
        @ParameterObject Pageable pageable) {
        return reportService.getEmployeeExpiredContractSoon(pageable).collectList()
            .zipWith(reportService.countEmployeeExpiredContractSoon())
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    @Operation(summary = " Truy cập báo cáo theo dõi NV sắp hết hạn HĐ")
    @GetMapping("/employee-expiring-contract/excel")
    public Mono<ResponseEntity<InputStreamResource>> getEmployeeExpiringContractReportExcel(
        @ParameterObject Pageable pageable) {
        return reportService.getEmployeeExpiredContractSoon(pageable).collectList()
            .flatMap(data -> {
                try {
                    var tempFile = Files.createTempFile("Expiring_contract", ".xlsx");
                    Xlsx.toXlsx(tempFile.toFile(), EmployeeProfileDTO.class, data);
                    var header = "attachment; filename=recruitment_report.xlsx";
                    return Mono.just(ResponseEntity.ok().header("Content-Disposition", header)
                        .body(new InputStreamResource(Files.newInputStream(tempFile))));
                } catch (IOException e) {
                    return Mono.error(new RuntimeException(e));
                }
            });
    }

    @Operation(summary = "Truy cập báo cáo số lượng NV đăng ký nghỉ chế độ theo khung thời gian")
    @GetMapping("/leave-regime-request")
    public Mono<ResponseEntity<List<RegimeLeaveReport>>> getLeaveRegimeRequestReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate) {
        return reportService.getRegimeLeaveReport(fromDate, toDate).flatMap(data -> {
            return Mono.just(ResponseEntity.ok().body(data));
        });
    }

    @Operation(summary = "Xuất excel số lượng NV đăng ký nghỉ chế độ theo khung thời gian")
    @GetMapping("/leave-regime-request/excel")
    public Mono<ResponseEntity<InputStreamResource>> getLeaveRegimeRequestReportExcel(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate) {
        return reportService.getRegimeLeaveReport(fromDate, toDate).flatMap(data -> {
            try {
                var tempFile = Files.createTempFile("leave_regime_report", ".xlsx");
                Xlsx.toXlsx(tempFile.toFile(), RegimeLeaveReport.class, data);
                var header = "attachment; filename=leave_regime_report.xlsx";
                return Mono.just(ResponseEntity.ok().header("Content-Disposition", header)
                    .body(new InputStreamResource(Files.newInputStream(tempFile))));
            } catch (IOException e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }


    @Operation(summary = "Truy cập báo cáo so sánh yêu cầu tuyển dụng và trạng thái tuyển dụng\n")
    @GetMapping("/recruitment-request")
    public Mono<ResponseEntity<ApiResponse<RecruitmentReport>>> getRecruitmentRequestReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
        @ParameterObject Pageable pageable) {

        return reportService.getRecruitmentReport(fromDate, toDate, pageable).flatMap(data -> {
            return Mono.just(ResponseEntity.ok().body(data));
        });
    }

    @Operation(summary = "Xuất excel báo cáo so sánh yêu cầu tuyển dụng và trạng thái tuyển dụng\n")
    @GetMapping("/recruitment-request/excel")
    public Mono<ResponseEntity<InputStreamResource>> getRecruitmentRequestReportExcel(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
        @ParameterObject Pageable pageable) {

        return reportService.getRecruitmentReport(fromDate, toDate, pageable).flatMap(data -> {
            try {
                var tempFile = Files.createTempFile("recruitment_report", ".xlsx");
                Xlsx.toXlsx(tempFile.toFile(), RecruitmentReport.class, data.getData());
                var header = "attachment; filename=recruitment_report.xlsx";
                return Mono.just(ResponseEntity.ok().header("Content-Disposition", header)
                    .body(new InputStreamResource(Files.newInputStream(tempFile))));
            } catch (IOException e) {
                return Mono.error(new RuntimeException(e));
            }
        });
    }


    // Báo cáo này sẽ hiển thị DS NV sắp đến hạn cấp đồng phục mới (trước 1 tháng so
    // với tháng vào làm cty).
    @Operation(summary = "Truy cập báo cáo NV sắp đến hạn cấp đồng phục")
    @GetMapping("/uniform-expiring")
    public Mono<ResponseEntity<ApiResponse<UniformExpiringReport>>> getUniformExpiringReport(
        @ParameterObject Pageable pageable,
        @RequestParam(value = "type", required = false, defaultValue = "BOTH") UniformExpiringReport.Type type) {

        return reportService.getUniformExpiringReport(pageable, type).collectList()
            .zipWith(reportService.countUniformExpiringReport(type))
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    @Operation(summary = "Xuất báo cáo biến động nhấn sự")
    @GetMapping("/human-resource-change")
    public Mono<ResponseEntity<ApiResponse<HumanResourceChangeReport>>> getHumanResourceChangeReport(
        HumanResourceChangeReport.Query query,
        @ParameterObject Pageable pageable
    ) {

        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        query.setPageable(pageable);
        return reportService.getHumanResourceChangeReport(query).collectList()
            .zipWith(reportService.countHumanResourceChangeReport(query))
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    // Truy cập báo cáo nhập xuất tồn đồng phục
    // Báo cáo này bao gồm 3 phần:
    // Báo cáo nhập: hiển thị số lượng từng loại đồng phục được nhập vào kho theo
    // khung thời gian.
    // Báo cáo xuất: hiển thị số lượng từng loại đồng phục được xuất kho theo khung
    // thời gian.
    // Báo cáo tồn: hiển thị số lượng hiện tại của từng loại đồng phục đang còn
    // trong kho.
    @Operation(summary = "Truy cập báo cáo  xuất  đồng phục")
    @GetMapping("/uniform-export")
    public Mono<ResponseEntity<ApiResponse<ExportImportReport>>> getUniformExportReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
        @ParameterObject Pageable pageable,
        @RequestParam(value = "type", required = false) UniformReleaseType type) {
        return reportService.getUniformExportReport(fromDate, toDate, pageable, type).collectList()
            .zipWith(reportService.countUniformExportReport(fromDate, toDate, type))
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    @Operation(summary = "Truy cập báo cáo nhập đồng phục")
    @GetMapping("/uniform-import")
    public Mono<ResponseEntity<ApiResponse<ExportImportReport>>> getUniformImportReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
        @ParameterObject Pageable pageable) {
        return reportService.getUniformImportStockReport(fromDate, toDate, pageable).collectList()
            .zipWith(reportService.countUniformImportStockReport(fromDate, toDate))
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    @Operation(summary = "Truy cập báo cáo tồn đồng phục")
    @GetMapping("/uniform-inventory")
    public Mono<ResponseEntity<ApiResponse<ExportImportReport>>> getUniformInventoryReport(
        @ParameterObject Pageable pageable) {
        return reportService.getUniformInventoryReport(pageable).collectList()
            .zipWith(reportService.countUniformInventoryReport())
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    @Operation(summary = "Truy excel, báo cáo  xuất nhâập tồn đồng phục")
    @GetMapping("/uniform-change/excel")
    public Mono<ResponseEntity<InputStreamResource>> getUniformInventoryReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate
    ) {
        return SecurityUtils.getUserJWTDetail().flatMap(u -> importExportReportService.getReport(fromDate, toDate,u.getCompanyId())
            .flatMap(file -> {
                try {

                    var header = "attachment; filename=uniform_change.xlsx";
                    return Mono.just(ResponseEntity.ok().header("Content-Disposition", header)
                        .body(new InputStreamResource(new FileInputStream(file))));
                } catch (IOException e) {
                    return Mono.error(new RuntimeException(e));
                }
            }));
    }

    @GetMapping("/uniform-change/{id}")
    public Mono<ApiResponse<UniformChangeDetail>> getUniformChange(@PathVariable UUID id,
                                                                   @ParameterObject Pageable pageable,
                                                                   @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
                                                                   @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
                                                                   @RequestParam(value = "type", required = true) UniformChangeDetail.Type type) {
        return reportService.getUniformChangeDetail(fromDate, toDate, id, type, pageable);

    }

    @Operation(summary = "Truy cập báo cáo xuất nhập tồn đồng phục")
    @GetMapping("/all-uniform")
    public Mono<ResponseEntity<ApiResponse<ExportImportReport>>> getAllUniformReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
        @ParameterObject Pageable pageable) {
        return reportService.getAllUniformReport(fromDate, toDate, pageable).collectList()
            .zipWith(reportService.countAllUniformReport(fromDate, toDate))
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }

    @Operation(summary = "Truy cập báo cáo xuất ứng, hoàn ứng")
    @GetMapping("/uniform-support")
    public Mono<ResponseEntity<ApiResponse<ExportImportReport>>> getUniformSupportReport(
        @RequestParam(value = "fromDate", required = false, defaultValue = "2020-01-01") LocalDate fromDate,
        @RequestParam(value = "toDate", required = false, defaultValue = "2050-12-31") LocalDate toDate,
        @ParameterObject Pageable pageable) {
        return reportService.getUniformSupportReport(fromDate, toDate, pageable).collectList()
            .zipWith(reportService.countUniformSupportReport(fromDate, toDate))
            .map(data -> {
                return ResponseEntity.ok().body(new ApiResponse<>(data.getT1(), data.getT2()));
            });
    }
}
