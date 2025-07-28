package com.masi.employee.service.reports;

import com.carevn.masi.dto.ApiResponse;
import com.carevn.masi.dto.UserJWTDetail;
import com.carevn.masi.utils.SecurityUtils;
import com.masi.employee.domain.*;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.Position;
import com.masi.employee.domain.enumeration.UniformReleaseType;
import com.masi.employee.repository.*;
import com.masi.employee.service.dto.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactivefeign.utils.Pair;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ReportService {
    private final EmployeeProfileRepository employeeProfileRepository;

    private final LeaveRegimeRequestRepository leaveRegimeRequestRepository;
    private final RecruitmentRequestRepository recruitmentRequestRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final UniformFormDetailRepository uniformFormDetailRepository;
    private final UniformStockRepository uniformStockRepository;

    public Flux<EmployeeProfileDTO> getEmployeeExpiredContractSoon(Pageable pageable) {
        LocalDate date = LocalDate.now().plusMonths(1);
        LocalDate today = LocalDate.now().minusDays(2);
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> employeeProfileRepository
                .findAllByContractEndDateBeforeAndContractEndDateAfterAndIsDeletedIsFalseAndCompany(
                    date, today,
                    user.getCompanyId(), pageable))
            .map(EmployeeProfile::toBriefDTO);
    }

    public Mono<Long> countEmployeeExpiredContractSoon() {
        LocalDate date = LocalDate.now().plusMonths(1);
        LocalDate today = LocalDate.now().minusDays(2);
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> employeeProfileRepository
                .countAllByContractEndDateBeforeAndContractEndDateAfterAndIsDeletedIsFalseAndCompany(
                    date,
                    today, user.getCompanyId()));
    }

    public Flux<HumanResourceChangeReport> getHumanResourceChangeReport(
        HumanResourceChangeReport.Query query) {

        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> {

                if (StringUtils.isBlank(query.getCompany())) {
                    query.setCompany(user.getCompanyId());
                }
                return employeeProfileRepository
                    .getHumanResourceChangeReport(query);
            });
    }

    public Mono<Long> countHumanResourceChangeReport(HumanResourceChangeReport.Query query) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                if (StringUtils.isBlank(query.getCompany())) {
                    query.setCompany(user.getCompanyId());
                }
                return employeeProfileRepository
                    .countHumanResourceChangeReport(query);
            });
    }

    public Mono<List<RegimeLeaveReport>> getRegimeLeaveReport(LocalDate startDate, LocalDate endDate) {
        Map<LeaveType, Integer> leaveTypeMap = LeaveType.getLeaveTypes().stream()
            .collect(Collectors.toMap(leaveType -> leaveType, leaveType -> 0));
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> leaveRegimeRequestRepository
                .findAllByLastWorkDateAfterAndReturnWorkDateBeforeAndIsDeletedIsFalseAndCompanyId(
                    // utc
                    startDate.atStartOfDay(ZoneOffset.UTC),
                    endDate.plusDays(1).atStartOfDay(ZoneOffset.UTC),
                    String.valueOf(user.getCompanyId()))
                .map(leaveRegimeRequest -> {
                    leaveTypeMap.put(leaveRegimeRequest.getLeaveType(),
                        leaveTypeMap.get(leaveRegimeRequest
                            .getLeaveType()) + 1);
                    return leaveRegimeRequest;
                })
                .collectList()
                .map(leaveRegimeRequests -> leaveTypeMap.entrySet().stream()
                    .map(entry -> {
                        RegimeLeaveReport regimeLeaveReport = new RegimeLeaveReport();
                        regimeLeaveReport.setLeaveType(entry.getKey());
                        regimeLeaveReport.setCount(entry.getValue());
                        regimeLeaveReport.setLeaveTypeDisplay(
                            LeaveType.VIETNAMESE_MAP.get(
                                entry.getKey()));
                        return regimeLeaveReport;
                    })
                    .sorted(Comparator.comparing(r -> LeaveType.VIETNAMESE_MAP.get(r.getLeaveType())))
                    .collect(Collectors.toList())));
    }

    public Mono<ApiResponse<RecruitmentReport>> getRecruitmentReport(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // tổng số yêu cầu tuyển dụng trên từng vị trí, chuyên ngành so sánh với số
        // lượng đã tuyển và đang được tuyển hiện tại
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                RecruitmentRequestRO ro = new RecruitmentRequestRO();
                ro.setFromDate(startDate);
                ro.setToDate(endDate);
                ro.setCompany(user.getCompanyId());
                return recruitmentRequestRepository
//                    .findAllByFilter(ro, null)
                    .getReport(startDate, endDate, user.getCompanyId())
                    .collectList()
                    .zipWith(recruitmentRequestRepository.getRecruitmentQuantity(startDate, endDate, user.getCompanyId()).collectList()
                        .map(q -> {
                            Map<String, Integer> quantityMap = new HashMap<>();
                            q.forEach(recruitmentQuantityQuery -> {
                                quantityMap.put(recruitmentQuantityQuery.getPosition(), recruitmentQuantityQuery.getTotalRequest());
                            });
                            return quantityMap;
                        })
                    )
//                    .flatMap(this::joinWithInterviewSchedule)
                    .map(tuple -> {
                        var recruitmentRequests = tuple.getT1();
                        var quantityMap = tuple.getT2();
                        Map<String, List<Integer>> positionMap = new HashMap<>();
                        recruitmentRequests.forEach(recruitmentRequest -> {
                            var position = recruitmentRequest.getPosition();
                            positionMap.putIfAbsent(position,
                                Arrays.asList(0, 0, 0));
                            // 1 cho số lượng đã tuyển => đã phỏng vấn và
                            // pass
                            // 2 cho số lượng đang được tuyển => just count
                            var item = positionMap.get(position);
                            if (InterviewResult.PASS.equals(recruitmentRequest.getResult())) {
                                item.set(1, item.get(1) + 1);
                            }
                            item.set(2, item.get(2) + 1);
                        });
                        return positionMap.entrySet().stream().map(entry -> {
                                RecruitmentReport recruitmentReport = new RecruitmentReport();
                                recruitmentReport.setPosition(entry.getKey());
                                recruitmentReport.setTotalRequest(
                                    quantityMap.getOrDefault(entry.getKey(), 0));
                                recruitmentReport.setTotalRecruited(
                                    entry.getValue().get(1));
                                recruitmentReport.setTotalRecruiting(
                                    entry.getValue().get(2));
                                return recruitmentReport;
                            })
                            .skip(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .collect(Collectors.toList());
                    }).zipWith(recruitmentRequestRepository.countReport(startDate, endDate, user.getCompanyId()))
                    .flatMap(data -> {
                        return Mono.just(new ApiResponse<>(data.getT1(), data.getT2()));
                    });
            });
    }


    public Flux<UniformExpiringReport> getUniformExpiringReport(Pageable pageable, UniformExpiringReport.Type type) {
        var currentDate = LocalDate.now();
        int nextMonth = currentDate.plusMonths(1).getMonthValue();
        int limit = pageable.getPageSize();
        long skip = pageable.getOffset();
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> {
                return switch (type) {
                    case ALLOCATED ->
                        employeeProfileRepository.getUniformExpiringReportAllocated(limit, skip, user.getCompanyId());
                    case UNALLOCATED -> employeeProfileRepository.getUniformExpiringReportUnallocated(limit, skip, user.getCompanyId());
                    default -> employeeProfileRepository.getUniformExpiringReportBoth(nextMonth, limit, skip);
                };
            });

    }

    public Mono<Long> countUniformExpiringReport(UniformExpiringReport.Type type) {
        var currentDate = LocalDate.now();
        int nextMonth = currentDate.plusMonths(1).getMonthValue();
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> {
                return switch (type) {
                    case ALLOCATED -> employeeProfileRepository.countUniformExpiringReportAllocated(user.getCompanyId());
                    case UNALLOCATED -> employeeProfileRepository.countUniformExpiringReportUnallocated(user.getCompanyId());
                    default -> employeeProfileRepository.countUniformExpiringReportBoth(nextMonth);
                };
            });
    }

    // báo cáo nhập xuất tồn đồng phục
    // Báo cáo này bao gồm 3 phần:
    // Báo cáo nhập: hiển thị số lượng từng loại đồng phục được nhập vào kho theo
    // khung thời gian.
    // Báo cáo xuất: hiển thị số lượng từng loại đồng phục được xuất kho theo khung
    // thời gian.
    // Báo cáo tồn: hiển thị số lượng hiện tại của từng loại đồng phục đang còn
    // trong kho.

    // 1 - Báo cáo xuất

    public Flux<ExportImportReport> getUniformExportReport(LocalDate fromDate, LocalDate toDate,
                                                           Pageable pageable, UniformReleaseType type) {
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> uniformFormDetailRepository.exportStockReport(fromDate, toDate,
                user.getCompanyId(), pageable.getPageSize(), pageable.getOffset(),
                type));
    }

    public Mono<Long> countUniformExportReport(LocalDate fromDate, LocalDate toDate, UniformReleaseType type) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> uniformFormDetailRepository.countExportStockReport(fromDate, toDate,
                user.getCompanyId(), type));
    }

    public Flux<ExportImportReport> getAllUniformReport(LocalDate fromDate, LocalDate toDate,
                                                        Pageable pageable) {
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> uniformFormDetailRepository.getSupercalifragilisticexpialidociousUniformReport(fromDate, toDate,
                user.getCompanyId(), pageable.getPageSize(), pageable.getOffset()));
    }

    public Mono<Long> countAllUniformReport(LocalDate fromDate, LocalDate toDate) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> uniformFormDetailRepository.countSupercalifragilisticexpialidociousUniformReport(fromDate, toDate,
                user.getCompanyId()));
    }

    // 2 - Báo cáo nhập
    // public Flux<UniformReport> getUniformImportReport(LocalDate fromDate,
    // LocalDate toDate) {
    // return queryByField("uniform_order_id", fromDate, toDate);
    // }

    // 3 - Báo cáo tồn
    public Flux<ExportImportReport> getUniformInventoryReport(Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> uniformStockRepository.findAllStock(limit, offset,
                user.getCompanyId()));

    }

    public Mono<Long> countUniformInventoryReport() {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> uniformStockRepository.countAllStock(user.getCompanyId()));
    }


    // báo cáo xuất ứng, hoàn ứng
    // Báo cáo này sẽ hiển thị tổng số đồng phục được tạm ứng và hoàn ứng theo từng
    // loại đồng phục và theo khung thời gian.
    // 1 - Báo cáo xuất ứng
    public Flux<ExportImportReport> getUniformSupportReport(LocalDate fromDate, LocalDate toDate,
                                                            Pageable pageable) {
        Map<Uniform, UniformSupportReport> uniformMap = new HashMap<>();
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();

        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> uniformFormDetailRepository.exportImportReport(fromDate, toDate,
                user.getCompanyId(), limit, offset));
    }

    public Mono<Long> countUniformSupportReport(LocalDate fromDate, LocalDate toDate) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> uniformFormDetailRepository.countExportImportReport(fromDate, toDate,
                user.getCompanyId()));
    }

    public Flux<ExportImportReport> getUniformImportStockReport(LocalDate fromDate, LocalDate toDate,
                                                                Pageable pageable) {
        if (pageable == null) {
            pageable = Pageable.unpaged();
        }
        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(user -> uniformFormDetailRepository.importStockReport(fromDate, toDate,
                user.getCompanyId(), limit, offset));
    }

    public Mono<Long> countUniformImportStockReport(LocalDate fromDate, LocalDate toDate) {
        return SecurityUtils.getUserJWTDetail()
            .flatMap(user -> uniformFormDetailRepository.countImportStockReport(fromDate, toDate,
                user.getCompanyId()));
    }

    public Mono<String> exportOrdersToExcel(List<UniformOrderDTO> uniformOrders) {
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/uniform-order/";
            // check if folder exists
            var folder = new File(path);
            if (!folder.exists()) {
                var s = folder.mkdirs();
            }
            // Sheet 1: Thông tin đơn hàng
            var orderSheet = workbook.createSheet("Đơn hàng đồng phục");
            var orderDetailSheet = workbook.createSheet("Chi tiết đơn hàng đồng phục");
            //var orderHeader = createUniformImportStockReportTitle(orderSheet);

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"Mã đơn hàng", "Ngày đặt hàng", "Tên đơn hàng", "Tổng số lượng", "Đã nhập kho", "Chưa nhập kho", "Nhà cung cấp", "Tổng tiền", "Người tạo đơn", "Mã nhân viên", "Ngày tạo đơn" };
            String[] detailHeader = {"Mã đơn hàng", "Tên đồng phục", "Mã đồng phục", "Số lượng", "Giá"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            Row headerDetailRow = orderDetailSheet.createRow(0);
            for (int i = 0; i < detailHeader.length; i++) {
                Cell cell = headerDetailRow.createCell(i);
                cell.setCellValue(detailHeader[i]);
            }
            var headerData = Flux.fromIterable(uniformOrders).collectList();
            AtomicInteger rowNum = new AtomicInteger(1);
            AtomicInteger rowNum1 = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            return headerData.flatMap(data -> {
                data.forEach(order -> {
                    var quantity = order.getQuantity() != null ? order.getQuantity() : 0;
                    var imported = quantity - (order.getRemainQuantity() != null ? order.getRemainQuantity() : 0);
                    var remain = order.getRemainQuantity() != null ? order.getRemainQuantity() : 0;
                    Row row = orderSheet.createRow(rowNum.getAndIncrement());
                    row.createCell(0).setCellValue(order.getCode());
                    row.createCell(1).setCellValue(order.getDate().plusHours(7).format(formatter));
                    row.createCell(2).setCellValue(order.getName());
                    row.createCell(3).setCellValue(quantity);
                    row.createCell(4).setCellValue(imported);
                    row.createCell(5).setCellValue(remain);
                    row.createCell(6).setCellValue(order.getSupplierName());
                    row.createCell(7).setCellValue(order.getTotalActualPrice());
                    row.createCell(8).setCellValue(order.getCreateByDTO().getFullName());
                    row.createCell(9).setCellValue(order.getCreateByDTO().getEmployeeCode());
                    row.createCell(10).setCellValue(order.getCreateAt().plusHours(7).format(formatter));
                    order.getUniformFormDetails().forEach(detail -> {
                        Row detailRow = orderDetailSheet.createRow(rowNum1.getAndIncrement());
                        detailRow.createCell(0).setCellValue(order.getCode());
                        detailRow.createCell(1).setCellValue(detail.getUniform().getName());
                        detailRow.createCell(2).setCellValue(detail.getUniform().getCode());
                        detailRow.createCell(3).setCellValue(detail.getQuantity());
                        detailRow.createCell(4).setCellValue(detail.getActualPrice());
                    });
                });
                for(int i = 0; i < headers.length; i++) {
                    orderSheet.autoSizeColumn(i);
                }

                for(int i = 0; i < detailHeader.length; i++) {
                    orderDetailSheet.autoSizeColumn(i);
                }

                var uuid = UUID.randomUUID().toString();

                try (FileOutputStream out = new FileOutputStream(path + uuid + ".xlsx")) {
                    workbook.write(out);
                    workbook.close();
                    return Mono.just(path + uuid + ".xlsx");
                } catch (IOException e) {
                    return Mono.error(e);
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<String> exportUniformReleaseToExcel(List<UniformReleaseDTO> uniformReleaseDTOS) {
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/uniform-release/";
            // check if folder exists
            var folder = new File(path);
            if (!folder.exists()) {
                var s = folder.mkdirs();
            }
            // Sheet 1: Thông tin đơn hàng
            var orderSheet = workbook.createSheet("Phiếu xuất đồng phục");
            var orderDetailSheet = workbook.createSheet("Chi tiết phiếu xuất đồng phục");
            //var orderHeader = createUniformImportStockReportTitle(orderSheet);

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"Mã phiếu xuất", "Ngày xuất hàng", "Nhân viên", "Tổng số lượng", "Loại xuất", "Chi phí"};
            String[] detailHeader = {"Mã đơn hàng", "Tên đồng phục", "Mã đồng phục", "Số lượng", "Giá bán"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            Row headerDetailRow = orderDetailSheet.createRow(0);
            for (int i = 0; i < detailHeader.length; i++) {
                Cell cell = headerDetailRow.createCell(i);
                cell.setCellValue(detailHeader[i]);
            }
            var headerData = Flux.fromIterable(uniformReleaseDTOS).collectList();
            AtomicInteger rowNum1 = new AtomicInteger(1);
            AtomicInteger rowNum = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            return headerData.flatMap(data -> {
                data.forEach(order -> {

                    Row row = orderSheet.createRow(rowNum.getAndIncrement());
                    row.createCell(0).setCellValue(order.getCode() != null ? order.getCode() : "");
                    row.createCell(1).setCellValue(order.getDate() != null ? order.getDate().format(formatter) : "");
                    if (order.getEmployee() != null) {
                        row.createCell(2).setCellValue(order.getEmployee().getLastName() + " " + order.getEmployee().getFirstName());
                    } else {
                        row.createCell(2).setCellValue("");
                    }
                    row.createCell(3).setCellValue(order.getQuantity() != null ? order.getQuantity() : 0);
                    row.createCell(4).setCellValue(order.getType() != null ? order.getType().getVietnameseName() : "");
                    row.createCell(5).setCellValue(order.getCost() != null ? order.getCost() : 0f);
                    //row.createCell(6).setCellValue(order.getIsReturned() ? "Đã hoàn trả" : "Chưa hoàn trả");

                    order.getUniformFormDetails().forEach(detail -> {
                        Row detailRow = orderDetailSheet.createRow(rowNum1.getAndIncrement());
                        detailRow.createCell(0).setCellValue(order.getCode() != null ? order.getCode() : "");
                        detailRow.createCell(1).setCellValue(detail.getUniform() != null ? detail.getUniform().getName() : "");
                        detailRow.createCell(2).setCellValue(detail.getUniform() != null ? detail.getUniform().getCode() : "");
                        detailRow.createCell(3).setCellValue(detail.getQuantity() != null ? detail.getQuantity() : 0);
                        detailRow.createCell(4).setCellValue(detail.getActualPrice() != null ? detail.getActualPrice() : 0f);

                    });
                });
                for(int i = 0; i < headers.length; i++) {
                    orderSheet.autoSizeColumn(i);
                }

                for(int i = 0; i < detailHeader.length; i++) {
                    orderDetailSheet.autoSizeColumn(i);
                }
                var uuid = UUID.randomUUID().toString();

                try (FileOutputStream out = new FileOutputStream(path + uuid + ".xlsx")) {
                    workbook.write(out);
                    workbook.close();
                    return Mono.just(path + uuid + ".xlsx");
                } catch (IOException e) {

                    return Mono.error(e);
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<String> exportUniformOrderStock(List<UniformOrderStockDTO> uniformOrderStockDTOS){
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/uniform-order-stock/";
            // check if folder exists
            var folder = new File(path);
            if (!folder.exists()) {
                var s = folder.mkdirs();
            }
            // Sheet 1: Thông tin đơn hàng
            var orderSheet = workbook.createSheet("Danh sách phiếu nhập");
            var orderDetailSheet = workbook.createSheet("Chi tiết phiếu nhập");
            //var orderHeader = createUniformImportStockReportTitle(orderSheet);

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"Mã phiếu nhập", "Mã đơn hàng", "Ngày nhập hàng", "Tổng số lượng", "Tên kho", "Mã kho"};
            String[] detailHeader = {"Mã phiếu nhập", "Tên đồng phục", "Mã đồng phục", "Số lượng"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            Row headerDetailRow = orderDetailSheet.createRow(0);
            for (int i = 0; i < detailHeader.length; i++) {
                Cell cell = headerDetailRow.createCell(i);
                cell.setCellValue(detailHeader[i]);
            }
            var headerData = Flux.fromIterable(uniformOrderStockDTOS).collectList();
            AtomicInteger rowNum = new AtomicInteger(1);
            AtomicInteger rowNum1 = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

            return headerData.flatMap(data -> {
                data.forEach(order -> {

                    Row row = orderSheet.createRow(rowNum.getAndIncrement());
                    row.createCell(0).setCellValue(order.getCode());
                    row.createCell(1).setCellValue(order.getUniformOrderDTO().getCode());
                    row.createCell(2).setCellValue(order.getCreateAt().format(formatter));
                    row.createCell(3).setCellValue(order.getTotalQuantity());
                    row.createCell(4).setCellValue(order.getWareHouseDTO().getName());
                    row.createCell(5).setCellValue(order.getWareHouseDTO().getCode());

                    order.getUniformFormDetail().forEach(detail -> {
                        Row detailRow = orderDetailSheet.createRow(rowNum1.getAndIncrement());
                        detailRow.createCell(0).setCellValue(order.getCode());
                        detailRow.createCell(1).setCellValue(detail.getUniform().getName());
                        detailRow.createCell(2).setCellValue(detail.getUniform().getCode());
                        detailRow.createCell(3).setCellValue(detail.getQuantity());
                    });
                });
                for(int i = 0; i < headers.length; i++) {
                    orderSheet.autoSizeColumn(i);
                }

                for(int i = 0; i < detailHeader.length; i++) {
                    orderDetailSheet.autoSizeColumn(i);
                }
                var uuid = UUID.randomUUID().toString();

                try (FileOutputStream out = new FileOutputStream(path + uuid + ".xlsx")) {
                    workbook.write(out);
                    workbook.close();
                    return Mono.just(path + uuid + ".xlsx");
                } catch (IOException e) {

                    return Mono.error(e);
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<String> getUniformExport(List<UniformDTO> uniforms){
        try {
            Workbook workbook = new XSSFWorkbook();
            var path = "uploaded-files/uniform/";
            // check if folder exists
            var folder = new File(path);
            if (!folder.exists()) {
                var s = folder.mkdirs();
            }
            // Sheet 1: Thông tin đơn hàng
            var orderSheet = workbook.createSheet("Danh sách đồng phục");
//            var orderDetailSheet = workbook.createSheet("Chi tiết phiếu nhập");
            //var orderHeader = createUniformImportStockReportTitle(orderSheet);

            Row headerRow = orderSheet.createRow(0);
            String[] headers = {"Mã đồng phục", "Tên đồng phục", "Ngày tạo", "Đơn vị", "Người tạo", "Mã nhân viên", "Trạng thái"};
            //String[] detailHeader = {"Mã phiếu nhập", "Tên đồng phục", "Mã đồng phục", "Số lượng"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
//            Row headerDetailRow = orderDetailSheet.createRow(0);
//            for (int i = 0; i < detailHeader.length; i++) {
//                Cell cell = headerDetailRow.createCell(i);
//                cell.setCellValue(detailHeader[i]);
//            }
            var headerData = Flux.fromIterable(uniforms).collectList();
            AtomicInteger rowNum = new AtomicInteger(1);
            //AtomicInteger rowNum1 = new AtomicInteger(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            return headerData.flatMap(data -> {
                data.forEach(uniform -> {
                    var createdByName = "SYSTEM";
                    var createdByCode = "SYSTEM";
                    if(uniform.getCreateByDTO() != null){
                        createdByName = uniform.getCreateByDTO().getFullName();
                        createdByCode = uniform.getCreateByDTO().getEmployeeCode();
                    }

                    Row row = orderSheet.createRow(rowNum.getAndIncrement());
                    row.createCell(0).setCellValue(uniform.getCode());
                    row.createCell(1).setCellValue(uniform.getName());
                    row.createCell(2).setCellValue(uniform.getCreateAt().format(formatter));
                    row.createCell(3).setCellValue(uniform.getUomDTO().getName());
                    row.createCell(4).setCellValue(createdByName);
                    row.createCell(5).setCellValue(createdByCode);
                    row.createCell(6).setCellValue(Objects.equals(uniform.getStatus(), "ENABLE") ? "Đang sử dụng" : "Ngừng sử dụng");

//                    order.getUniformFormDetail().forEach(detail -> {
//                        Row detailRow = orderDetailSheet.createRow(rowNum1.getAndIncrement());
//                        detailRow.createCell(0).setCellValue(order.getCode());
//                        detailRow.createCell(1).setCellValue(detail.getUniform().getName());
//                        detailRow.createCell(2).setCellValue(detail.getUniform().getCode());
//                        detailRow.createCell(3).setCellValue(detail.getQuantity());
//                    });
                });
                for(int i = 0; i < headers.length; i++) {
                    orderSheet.autoSizeColumn(i);
                }

//                for(int i = 0; i < detailHeader.length; i++) {
//                    orderDetailSheet.autoSizeColumn(i);
//                }
                var uuid = UUID.randomUUID().toString();

                try (FileOutputStream out = new FileOutputStream(path + uuid + ".xlsx")) {
                    workbook.write(out);
                    workbook.close();
                    return Mono.just(path + uuid + ".xlsx");
                } catch (IOException e) {

                    return Mono.error(e);
                }
            });
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    public Mono<ApiResponse<UniformChangeDetail>> getUniformChangeDetail(LocalDate fromDate, LocalDate toDate, UUID uniform, UniformChangeDetail.Type type, Pageable pageable) {
        Pageable notNullPageable = pageable == null ? Pageable.unpaged() : pageable;
        Mono<String> companyMono = SecurityUtils.getUserJWTDetail().map(UserJWTDetail::getCompanyId);


        return switch (type) {
            case STOCKED ->
                companyMono.flatMap(company -> uniformFormDetailRepository.getUniformOrderReportByUniform(fromDate, toDate, uniform, company, notNullPageable.getPageSize(), notNullPageable.getOffset())
                    .collectList()
                    .zipWith(uniformFormDetailRepository.countUniformOrderReportByUniform(fromDate, toDate, uniform, company))
                    .map(data -> {
                        var uniformOrders = data.getT1();
                        var total = data.getT2();
                        return new ApiResponse<>(uniformOrders, total);
                    }));
            case RELEASE ->
                companyMono.flatMap(company -> uniformFormDetailRepository.getUniformReleaseReportByUniform(fromDate, toDate, uniform, company, notNullPageable.getPageSize(), notNullPageable.getOffset())
                    .collectList()
                    .zipWith(uniformFormDetailRepository.countUniformReleaseReportByUniform(fromDate, toDate, uniform, company))
                    .map(data -> {
                        var uniformReleases = data.getT1();
                        var total = data.getT2();
                        return new ApiResponse<>(uniformReleases, total);
                    }));
        };
    }
}
