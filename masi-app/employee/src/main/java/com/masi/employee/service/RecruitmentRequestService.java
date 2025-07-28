package com.masi.employee.service;

import com.masi.employee.domain.*;
import com.carevn.masi.utils.SecurityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.employee.domain.enumeration.PositionEmployee;
import com.masi.employee.domain.enumeration.RecruitmentStatus;
import com.masi.employee.helper.ObjectComparator;
import com.masi.employee.repository.InterviewScheduleRepository;
import com.masi.employee.repository.RecruitmentRequestLogsRepository;
import com.masi.employee.repository.RecruitmentRequestRepository;
import com.masi.employee.repository.RecruitmentReviewRequestRepository;
import com.masi.employee.service.dto.RecruitmentChangeLogsDTO;
import com.masi.employee.service.dto.RecruitmentRequestDTO;
import com.masi.employee.service.dto.RecruitmentRequestRO;
import com.masi.employee.service.dto.RecruitmentReviewRequestDTO;
import com.masi.employee.service.dto.request.ConsentToApproveRecruitmentRequest;
import com.masi.employee.service.dto.request.RefusalOfApproveRecruitmentRequest;
import com.masi.employee.service.event.NotificationAddedEvent;
import com.masi.employee.service.mapper.RecruitmentRequestMapper;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.masi.employee.service.mapper.RecruitmentReviewRequestMapper;
import com.masi.employee.service.web.client.AuthClient;
import com.masi.employee.service.web.client.FileClient;
import com.masi.employee.web.rest.errors.BadRequestAlertException;

import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;

//import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing
 * {@link com.masi.employee.domain.RecruitmentRequest}.
 */
@Service
@Transactional
@AllArgsConstructor
public class RecruitmentRequestService {

    private static final Logger log = LoggerFactory.getLogger(RecruitmentRequestService.class);

    private final RecruitmentRequestRepository recruitmentRequestRepository;

    private final RecruitmentRequestMapper recruitmentRequestMapper;
    private final RecruitmentReviewRequestMapper recruitmentReviewRequestMapper;
    private final StreamBridge streamBridge;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final RecruitmentReviewRequestRepository recruitmentReviewRequestRepository;
    private final ObjectMapper objectMapper;
    private final AuthClient employeeClient;
    private final FileClient fileClient;
    private final RecruitmentRequestLogsRepository recruitmentRequestLogsRepository;
    // thứ tự duyệt
    private final java.util.List<PositionEmployee> positionEmployeeList = Arrays.asList(
        PositionEmployee.DEPARTMENT_MANAGER,
        PositionEmployee.PERSONNEL_MANAGER,
        PositionEmployee.ACCOUNTING_MANAGER,
        PositionEmployee.DIRECTOR);

    /**
     * Save a recruitmentRequest.
     *
     * @param recruitmentRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RecruitmentRequestDTO> save(RecruitmentRequestDTO recruitmentRequestDTO) {
        log.debug("Request to save RecruitmentRequest : {}", recruitmentRequestDTO);
        recruitmentRequestDTO.setCreatedDate(ZonedDateTime.now());
        recruitmentRequestDTO.setLastUpdated(ZonedDateTime.now());
        recruitmentRequestDTO.setStatus(RecruitmentStatus.WAITING_APPROVAL);
        recruitmentRequestDTO.setOldStatus(RecruitmentStatus.WAITING_APPROVAL);
        recruitmentRequestDTO.setIsDeleted(false);

        return recruitmentRequestRepository
            .save(recruitmentRequestMapper.toEntity(recruitmentRequestDTO))
            .flatMap(recruitmentRequest -> {
                return this.saveEmployeeRequest(recruitmentRequest)
                    .thenReturn(recruitmentRequest);
            })
            .map(recruitmentRequestMapper::toDto)
            .switchIfEmpty(Mono.error(new BadRequestAlertException(
                "Failed to update RecruitmentRequest: Invalid status", "recruitmentRequest", "invalidStatus")));
    }
//
//    public Mono<Void> saveEmployeeRequest(RecruitmentRequest recruitmentRequest) {
//        ZonedDateTime createdDate = ZonedDateTime.now();
//        return Flux.fromIterable(positionEmployeeList)
//            .flatMap(position -> {
//                RecruitmentReviewRequest employeeRequest = new RecruitmentReviewRequest();
//                employeeRequest.setId(UUID.randomUUID());
//                employeeRequest.setRequestId(recruitmentRequest.getId());
//                employeeRequest.setCreatedDate(createdDate);
//                return employeeClient.getEmployeePosition(String.valueOf(recruitmentRequest.getDepartmentId()), position, recruitmentRequest.getCompany())
//                    .flatMap(employeeDTO -> {
//                        log.info("Employee at position: {} of department: {} is: {}", position,
//                            recruitmentRequest.getDepartmentId(), employeeDTO);
//
//                        employeeRequest.setEmployeeId(UUID.fromString(employeeDTO.get("id").toString()));
//                        employeeRequest.setPosition(position);
//                        String idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";
//                        if (employeeDTO.get("id").equals("") || employeeDTO.get("id") == null)
//                            idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";
//
//                        if (position.equals(positionEmployeeList.get(0))) {
//                            employeeRequest.setResult(true);
//                            Map<String, Object> data = objectMapper.convertValue(recruitmentRequest,
//                                new TypeReference<Map<String, Object>>() {
//                                });
//                            System.out.println("\n\n\n\n " + recruitmentRequest.getEmployeeId() + "\n\n\n\n ");
//                            NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
//                                .content(String.format("Có đơn tuyển dụng yêu cầu xét duyệt"))
//                                .title("Có đơn tuyển dụng yêu cầu xét duyệt")
//                                .createdAt(ZonedDateTime.now())
//                                .createdBy("system")
//                                .data(data)
//                                .sentBy("system")
//                                .action("RecruitmentRequestUpdated")
//                                .entityId(recruitmentRequest.getId().toString())
//                                .entityType("RecruitmentRequest")
//                                .recipients(List.of((idEmployeeTest)))
//                                .id(UUID.randomUUID())
//                                .build();
//                            streamBridge.send("notification-added", notificationAddedEvent,
//                                MediaType.APPLICATION_JSON);
//                        }
//
//                        employeeRequest.setCreatedDate(createdDate);
//                        return recruitmentReviewRequestRepository.save(employeeRequest);
//                    })
//                    .switchIfEmpty(Mono.defer(() -> {
//                        log.error("Error retrieving employee position");
//                        RecruitmentReviewRequest defaultRequest = createDefaultEmployeeRequest(recruitmentRequest.getCompany());
////                                defaultRequest.setResult(position.equals(positionEmployeeList.get(0)));
//                        String idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";
////                                if(employeeDTO.get("id").equals("") || employeeDTO.get("id") == null)
////                                    idEmployeeTest ="999e1416-a92a-459a-add7-611278b61b35";
//
//
//                        if (position.equals(positionEmployeeList.get(0))) {
//                            defaultRequest.setResult(true);
//                            Map<String, Object> data = objectMapper.convertValue(recruitmentRequest,
//                                new TypeReference<Map<String, Object>>() {
//                                });
//                            System.out.println("\n\n\n\n " + recruitmentRequest.getEmployeeId() + "\n\n\n\n ");
//
//                            NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
//                                .content(String.format("Có đơn tuyển dụng yêu cầu xét duyệt"))
//                                .title("Có đơn tuyển dụng yêu cầu xét duyệt")
//                                .createdAt(ZonedDateTime.now())
//                                .createdBy("system")
//                                .data(data)
//                                .sentBy("system")
//                                .action("RecruitmentRequestUpdated")
//                                .entityId(recruitmentRequest.getId().toString())
//                                .entityType("RecruitmentRequest")
//                                .recipients(List.of(idEmployeeTest))
//                                .id(UUID.randomUUID())
//                                .build();
//                            streamBridge.send("notification-added", notificationAddedEvent,
//                                MediaType.APPLICATION_JSON);
//                        }
//
//
//                        defaultRequest.setRequestId(recruitmentRequest.getId());
//                        defaultRequest.setPosition(position);
//                        defaultRequest.setCreatedDate(createdDate);
//                        return recruitmentReviewRequestRepository.save(defaultRequest);
//                    }))
//                    .onErrorResume(e -> {
//                        log.error("Error retrieving employee position", e);
//                        RecruitmentReviewRequest defaultRequest = createDefaultEmployeeRequest(recruitmentRequest.getCompany());
////                                defaultRequest.setResult(position.equals(positionEmployeeList.get(0)));
//                        defaultRequest.setCreatedDate(createdDate);
//                        if (position.equals(positionEmployeeList.get(0))) {
//                            defaultRequest.setResult(true);
//                            Map<String, Object> data = objectMapper.convertValue(recruitmentRequest,
//                                new TypeReference<Map<String, Object>>() {
//                                });
//                            System.out.println("\n\n\n\n " + recruitmentRequest.getEmployeeId() + "\n\n\n\n ");
//                            String idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";
//
//                            NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
//                                .content(String.format("Có đơn tuyển dụng yêu cầu xét duyệt"))
//                                .title("Có đơn tuyển dụng yêu cầu xét duyệt")
//                                .createdAt(ZonedDateTime.now())
//                                .createdBy("system")
//                                .data(data)
//                                .sentBy("system")
//                                .action("RecruitmentRequestUpdated")
//                                .entityId(recruitmentRequest.getId().toString())
//                                .entityType("RecruitmentRequest")
//                                .recipients(List.of(idEmployeeTest))
//                                .id(UUID.randomUUID())
//                                .build();
//                            streamBridge.send("notification-added", notificationAddedEvent,
//                                MediaType.APPLICATION_JSON);
//                        }
//
//
//                        defaultRequest.setRequestId(recruitmentRequest.getId());
//                        defaultRequest.setPosition(position);
//                        defaultRequest.setCreatedDate(createdDate);
//                        return recruitmentReviewRequestRepository.save(defaultRequest);
//                    });
//
//            })
//            .then();
//    }
//
//    // nào có tắt cái này sau
//    private RecruitmentReviewRequest createDefaultEmployeeRequest(String company) {
//
//        RecruitmentReviewRequest defaultRequest = new RecruitmentReviewRequest();
//        defaultRequest.setId(UUID.randomUUID()); // Tạo ID mới cho default request
//        defaultRequest.setResult(false);
//
//        if (company != null || company.equals("KIM_LONG"))
//            defaultRequest.setEmployeeId(UUID.fromString("999e1416-a92a-459a-add7-611278b61b35"));
//        else
//            defaultRequest.setEmployeeId(UUID.fromString("f9def996-2c8d-48b6-bdb5-930969766c48"));
//
//        return defaultRequest;
//    }
//
//    public Mono<Void> createReviews(UUID recruitmentId, List<UUID> empIds, String baseContentBuilder) {
//        ZonedDateTime createdDate = ZonedDateTime.now();
//        return Flux.fromIterable(empIds)
//                .flatMap(id -> {
//                    RecruitmentReviewRequest recruitmentReviewRequest = new RecruitmentReviewRequest();
//                    recruitmentReviewRequest.setId(UUID.randomUUID());
//                    recruitmentReviewRequest.setEmployeeId(id);
//                    recruitmentReviewRequest.setPosition(PositionEmployee.REVIEWER);
//                    recruitmentReviewRequest.setResult(false);
//                    recruitmentReviewRequest.setRequestId(recruitmentId);
//                    recruitmentReviewRequest.setIsDeleted(false);
//                    recruitmentReviewRequest.setCreatedDate(createdDate);
//                    return recruitmentReviewRequestRepository.save(recruitmentReviewRequest);
//                })
//                .then(Mono.defer(() -> {
//                    List<String> list = empIds.stream()
//                            .map(UUID::toString)
//                            .toList();
//                    var content = baseContentBuilder + "yêu cầu xét duyệt";
//                    var title = baseContentBuilder + "yêu cầu xét duyệt";
//                    NotificationAddedEvent notificationAddedEventApprove = NotificationAddedEvent.builder()
//                            .content(content)
//                            .title(title)
//                            .createdAt(ZonedDateTime.now())
//                            .createdBy("system")
////                            .data(data)
//                            .sentBy("system")
//                            .action("RecruitmentRequestUpdated")
//                            .entityId(recruitmentId.toString())
//                            .entityType("RecruitmentRequest")
//                            .recipients(list)
//                            .id(UUID.randomUUID())
//                            .build();
//
//                    streamBridge.send("notification-added", notificationAddedEventApprove, org.springframework.http.MediaType.APPLICATION_JSON);
//
//                    return Mono.empty();
//                }));
//    }
//
//
//    public Mono<RecruitmentRequestDTO> partialUpdate(RecruitmentRequestDTO recruitmentRequestDTO, List<UUID> empIds) {
//        log.debug("Request to partially update RecruitmentRequest : {}", recruitmentRequestDTO);
//
//        return recruitmentRequestRepository
//                .findById(recruitmentRequestDTO.getId())
//                .flatMap(existingRecruitmentRequest -> {
//                    if (existingRecruitmentRequest.getNumberAdjourn() == null) {
//                        existingRecruitmentRequest.setNumberAdjourn(0);
//                    }
//                    var oldObj = RecruitmentRequest.builder()
//                            .deadline(existingRecruitmentRequest.getDeadline())
////                    .numberAdjourn(existingRecruitmentRequest.getNumberAdjourn())
//                            .build();
//                    recruitmentRequestDTO.applyUpdate(existingRecruitmentRequest);
//                    existingRecruitmentRequest.setIsPersisted();
//                    existingRecruitmentRequest.setLastUpdated(ZonedDateTime.now());
//
//                    Flux<String> listRecruitmentRequest = recruitmentReviewRequestRepository.findByRequestIdAndResults(recruitmentRequestDTO.getId(), true)
//                            .map(recruitmentReviewRequestFull -> {
//                                return recruitmentReviewRequestFull.getEmployeeId().toString();
//                            });
//
//                    return listRecruitmentRequest
//                            .collectList()
//                            .flatMap(list -> {
//
//                                StringBuilder baseContentBuilder = new StringBuilder("Yêu cầu tuyển dụng vị trí ")
//                                        .append(recruitmentRequestDTO.getPosition())
//                                        .append(", chức vụ ")
//                                        .append(recruitmentRequestDTO.getJobTitle())
//                                        .append(" với mục đích ")
//                                        .append(recruitmentRequestDTO.getRecruitmentPurposes());
//
//                                String titleOwner = baseContentBuilder + " do bạn tạo đã được";
//                                String contentOwner = baseContentBuilder + "do bạn tạo đã được";
//                                String titleApprove = "Yêu cầu tuyển dụng do bạn duyệt đã được";
//                                String contentApprove = "Yêu cầu tuyển dụng do bạn duyệt đã được";
//
//                                RecruitmentRequestLogs recruitmentChangeLog = null;
//                                if (recruitmentRequestDTO.isUpdate()) {
//                                    titleOwner += " cập nhật";
//                                    contentOwner += " cập nhật";
//                                    titleApprove += " cập nhật";
//                                    contentApprove += " cập nhật";
//                                } else if (recruitmentRequestDTO.isAdjourn()) {
//                                    titleOwner += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
//                                    contentOwner += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
//                                    titleApprove += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
//                                    contentApprove += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
//
//                                    existingRecruitmentRequest.setNumberAdjourn(existingRecruitmentRequest.getNumberAdjourn() + 1);
//                                    existingRecruitmentRequest.setStatus(RecruitmentStatus.WAITING_RENEW);
//
//                                    var newObject = RecruitmentRequest.builder()
//                                            .deadline(existingRecruitmentRequest.getDeadline())
////                                .numberAdjourn(existingRecruitmentRequest.getNumberAdjourn())
//                                            .build();
//
//                                    Optional<Json> change = ObjectComparator.compareObjectsToPostgresJson(oldObj, newObject);
//                                    recruitmentChangeLog = RecruitmentRequestLogs.builder()
//                                            .id(UUID.randomUUID())
//                                            .change(change.orElseThrow())
//                                            .changeBy("System")
//                                            .recruitmentRequestId(recruitmentRequestDTO.getId())
//                                            .changeDate(ZonedDateTime.now())
//                                            .build();
//
//                                }
//
////                                list.add(existingRecruitmentRequest.getCreatedBy());
//
//                                Map<String, Object> data = objectMapper.convertValue(recruitmentRequestDTO,
//                                        new TypeReference<Map<String, Object>>() {
//                                        });
//                                NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
//                                        .content(titleOwner)
//                                        .title(contentOwner)
//                                        .createdAt(ZonedDateTime.now())
//                                        .createdBy("system")
//                                        .data(data)
//                                        .sentBy("system")
//                                        .action("RecruitmentRequestUpdated")
//                                        .entityId(existingRecruitmentRequest.getId().toString())
//                                        .entityType("RecruitmentRequest")
//                                        .recipients(Collections.singleton(existingRecruitmentRequest.getCreatedBy()))
//                                        .id(UUID.randomUUID())
//                                        .build();
//                                streamBridge.send("notification-added", notificationAddedEvent,
//                                        org.springframework.http.MediaType.APPLICATION_JSON);
//
//                                if (list.isEmpty() && recruitmentRequestDTO.isUpdate()) {
//                                    NotificationAddedEvent notificationAddedEventApprove = NotificationAddedEvent.builder()
//                                            .content(titleApprove)
//                                            .title(contentApprove)
//                                            .createdAt(ZonedDateTime.now())
//                                            .createdBy("system")
//                                            .data(data)
//                                            .sentBy("system")
//                                            .action("RecruitmentRequestUpdated")
//                                            .entityId(existingRecruitmentRequest.getId().toString())
//                                            .entityType("RecruitmentRequest")
//                                            .recipients(list)
//                                            .id(UUID.randomUUID())
//                                            .build();
//                                    streamBridge.send("notification-added", notificationAddedEventApprove,
//                                            org.springframework.http.MediaType.APPLICATION_JSON);
//                                }
//
//                                if (recruitmentChangeLog != null) {
//
//                                    return recruitmentRequestLogsRepository.save(recruitmentChangeLog)
//                                            .then(recruitmentReviewRequestRepository.approveConsentRecruitments(recruitmentRequestDTO.getId()))
//                                            .then(recruitmentRequestRepository.save(existingRecruitmentRequest))
//                                            .flatMap(recruitmentRequest -> {
//                                                return createReviews(recruitmentRequestDTO.getId(), empIds, baseContentBuilder.toString())
//                                                        .thenReturn(recruitmentRequest);
//                                            });
//
//                                }
//
//                                return recruitmentRequestRepository.save(existingRecruitmentRequest);
//                            });
//
//                })
//                .map(recruitmentRequestMapper::toDto);
//    }
//
//    public Mono<Integer> EmployeeConsentApprove(UUID recruitmentRequestId, String approvalSign) {
//        return recruitmentReviewRequestRepository.findByRequestIdAndResultAndIsDeletedIsFalse(recruitmentRequestId, true)
//                .flatMap(employeeRequest -> {
//                    int currentIndex = positionEmployeeList.indexOf(employeeRequest.getPosition());
//
//                    if (currentIndex == -1 || currentIndex == positionEmployeeList.size() - 1) {
//                        return recruitmentReviewRequestRepository
//                                .approveConsentRecruitment(employeeRequest.getId(), approvalSign)
//                                .then(recruitmentReviewRequestRepository.approveConsentRecruitments(recruitmentRequestId)
//                                        .then(Mono.just(0))
//                                );
//                    }
//
//                    return recruitmentReviewRequestRepository
//                            .approveConsentRecruitment(employeeRequest.getId(), approvalSign)
//                            .then(recruitmentReviewRequestRepository.findByRequestIdAndPosition(
//                                    recruitmentRequestId,
//                                    String.valueOf(positionEmployeeList.get(currentIndex + 1))))
//                            .flatMap(nextEmployeeRequest -> {
//
//                                System.out.println("\n\n\n\n " + employeeRequest.getEmployeeId() + "\n\n\n\n ");
//
//                                Map<String, Object> data = objectMapper.convertValue(employeeRequest,
//                                        new TypeReference<Map<String, Object>>() {
//                                        });
//                                NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
//                                        .content(String.format("Có đơn tuyển dụng yêu cầu xét duyệt"))
//                                        .title("Có đơn tuyển dụng yêu cầu xét duyệt")
//                                        .createdAt(ZonedDateTime.now())
//                                        .createdBy("system")
//                                        .data(data)
//                                        .sentBy("system")
//                                        .action("RecruitmentRequestUpdated")
//                                        .entityId(recruitmentRequestId.toString())
//                                        .entityType("RecruitmentRequest")
//                                        .recipients(java.util.List.of(String.valueOf(nextEmployeeRequest.getEmployeeId())))
//                                        .id(UUID.randomUUID())
//                                        .build();
//                                streamBridge.send("notification-added", notificationAddedEvent,
//                                        org.springframework.http.MediaType.APPLICATION_JSON);
//
//
//                                return recruitmentReviewRequestRepository
//                                        .updateStatus(nextEmployeeRequest.getId(), true)
//                                        .thenReturn(1);
//                            })
//                            .onErrorReturn(3);
//                })
//                .switchIfEmpty(Mono.just(3));
//    }

        // nào có tắt cái này sau
    private RecruitmentReviewRequest createDefaultEmployeeRequest(String company) {

        RecruitmentReviewRequest defaultRequest = new RecruitmentReviewRequest();
        defaultRequest.setId(UUID.randomUUID()); // Tạo ID mới cho default request
        defaultRequest.setResult(false);

        if (company != null || company.equals("KIM_LONG"))
            defaultRequest.setEmployeeId(UUID.fromString("999e1416-a92a-459a-add7-611278b61b35"));
        else
            defaultRequest.setEmployeeId(UUID.fromString("f9def996-2c8d-48b6-bdb5-930969766c48"));

        return defaultRequest;
    }

    private void sendNotification(NotificationAddedEvent notificationAddedEvent) {
        streamBridge.send("notification-added", notificationAddedEvent, MediaType.APPLICATION_JSON);
    }

    public Mono<Void> saveEmployeeRequest(RecruitmentRequest recruitmentRequest) {
        ZonedDateTime createdDate = ZonedDateTime.now();
        return Flux.fromIterable(positionEmployeeList)
                .flatMap(position -> {
                    RecruitmentReviewRequest employeeRequest = new RecruitmentReviewRequest();
                    employeeRequest.setId(UUID.randomUUID());
                    employeeRequest.setRequestId(recruitmentRequest.getId());
                    employeeRequest.setCreatedDate(createdDate);
                    return employeeClient.getEmployeePosition(String.valueOf(recruitmentRequest.getDepartmentId()), position, recruitmentRequest.getCompany())
                            .flatMap(employeeDTO -> {
                                log.info("Employee at position: {} of department: {} is: {}", position,
                                        recruitmentRequest.getDepartmentId(), employeeDTO);

                                employeeRequest.setEmployeeId(UUID.fromString(employeeDTO.get("id").toString()));
                                employeeRequest.setPosition(position);
                                String idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";
                                if (employeeDTO.get("id").equals("") || employeeDTO.get("id") == null)
                                    idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";

                                if (position.equals(positionEmployeeList.get(0))) {
                                    employeeRequest.setResult(true);
                                    Map<String, Object> data = objectMapper.convertValue(recruitmentRequest,
                                            new TypeReference<Map<String, Object>>() {});
                                    NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
                                            .content("Có đơn tuyển dụng yêu cầu xét duyệt")
                                            .title("Có đơn tuyển dụng yêu cầu xét duyệt")
                                            .createdAt(ZonedDateTime.now())
                                            .createdBy("system")
                                            .data(data)
                                            .sentBy("system")
                                            .action("RecruitmentRequestUpdated")
                                            .entityId(recruitmentRequest.getId().toString())
                                            .entityType("RecruitmentRequest")
                                            .recipients(List.of(idEmployeeTest))
                                            .id(UUID.randomUUID())
                                            .build();
                                    Mono.fromRunnable(() -> sendNotification(notificationAddedEvent)).subscribe();
                                }

                                employeeRequest.setCreatedDate(createdDate);
                                return recruitmentReviewRequestRepository.save(employeeRequest);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                log.error("Error retrieving employee position");
                                RecruitmentReviewRequest defaultRequest = createDefaultEmployeeRequest(recruitmentRequest.getCompany());
                                String idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";

                                if (position.equals(positionEmployeeList.get(0))) {
                                    defaultRequest.setResult(true);
                                    Map<String, Object> data = objectMapper.convertValue(recruitmentRequest,
                                            new TypeReference<Map<String, Object>>() {});
                                    NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
                                            .content("Có đơn tuyển dụng yêu cầu xét duyệt")
                                            .title("Có đơn tuyển dụng yêu cầu xét duyệt")
                                            .createdAt(ZonedDateTime.now())
                                            .createdBy("system")
                                            .data(data)
                                            .sentBy("system")
                                            .action("RecruitmentRequestUpdated")
                                            .entityId(recruitmentRequest.getId().toString())
                                            .entityType("RecruitmentRequest")
                                            .recipients(List.of(idEmployeeTest))
                                            .id(UUID.randomUUID())
                                            .build();
                                    Mono.fromRunnable(() -> sendNotification(notificationAddedEvent)).subscribe();
                                }

                                defaultRequest.setRequestId(recruitmentRequest.getId());
                                defaultRequest.setPosition(position);
                                defaultRequest.setCreatedDate(createdDate);
                                return recruitmentReviewRequestRepository.save(defaultRequest);
                            }))
                            .onErrorResume(e -> {
                                log.error("Error retrieving employee position", e);
                                RecruitmentReviewRequest defaultRequest = createDefaultEmployeeRequest(recruitmentRequest.getCompany());
                                defaultRequest.setCreatedDate(createdDate);
                                if (position.equals(positionEmployeeList.get(0))) {
                                    defaultRequest.setResult(true);
                                    Map<String, Object> data = objectMapper.convertValue(recruitmentRequest,
                                            new TypeReference<Map<String, Object>>() {});
                                    String idEmployeeTest = "999e1416-a92a-459a-add7-611278b61b35";
                                    NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
                                            .content("Có đơn tuyển dụng yêu cầu xét duyệt")
                                            .title("Có đơn tuyển dụng yêu cầu xét duyệt")
                                            .createdAt(ZonedDateTime.now())
                                            .createdBy("system")
                                            .data(data)
                                            .sentBy("system")
                                            .action("RecruitmentRequestUpdated")
                                            .entityId(recruitmentRequest.getId().toString())
                                            .entityType("RecruitmentRequest")
                                            .recipients(List.of(idEmployeeTest))
                                            .id(UUID.randomUUID())
                                            .build();
                                    Mono.fromRunnable(() -> sendNotification(notificationAddedEvent)).subscribe();
                                }

                                defaultRequest.setRequestId(recruitmentRequest.getId());
                                defaultRequest.setPosition(position);
                                defaultRequest.setCreatedDate(createdDate);
                                return recruitmentReviewRequestRepository.save(defaultRequest);
                            });
                })
                .then();
    }

    public Mono<Void> createReviews(UUID recruitmentId, List<UUID> empIds, String baseContentBuilder) {
        ZonedDateTime createdDate = ZonedDateTime.now();
        return Flux.fromIterable(empIds)
                .flatMap(id -> {
                    RecruitmentReviewRequest recruitmentReviewRequest = new RecruitmentReviewRequest();
                    recruitmentReviewRequest.setId(UUID.randomUUID());
                    recruitmentReviewRequest.setEmployeeId(id);
                    recruitmentReviewRequest.setPosition(PositionEmployee.REVIEWER);
                    recruitmentReviewRequest.setResult(false);
                    recruitmentReviewRequest.setRequestId(recruitmentId);
                    recruitmentReviewRequest.setIsDeleted(false);
                    recruitmentReviewRequest.setCreatedDate(createdDate);
                    return recruitmentReviewRequestRepository.save(recruitmentReviewRequest);
                })
                .then(Mono.defer(() -> {
                    List<String> list = empIds.stream()
                            .map(UUID::toString)
                            .toList();
                    var content = baseContentBuilder + "yêu cầu xét duyệt";
                    var title = baseContentBuilder + "yêu cầu xét duyệt";
                    NotificationAddedEvent notificationAddedEventApprove = NotificationAddedEvent.builder()
                            .content(content)
                            .title(title)
                            .createdAt(ZonedDateTime.now())
                            .createdBy("system")
                            .sentBy("system")
                            .action("RecruitmentRequestUpdated")
                            .entityId(recruitmentId.toString())
                            .entityType("RecruitmentRequest")
                            .recipients(list)
                            .id(UUID.randomUUID())
                            .build();

                    Mono.fromRunnable(() -> sendNotification(notificationAddedEventApprove)).subscribe();

                    return Mono.empty();
                }));
    }

    public Mono<RecruitmentRequestDTO> partialUpdate(RecruitmentRequestDTO recruitmentRequestDTO, List<UUID> empIds) {
        log.debug("Request to partially update RecruitmentRequest : {}", recruitmentRequestDTO);

        return recruitmentRequestRepository
                .findById(recruitmentRequestDTO.getId())
                .flatMap(existingRecruitmentRequest -> {
                    if (existingRecruitmentRequest.getNumberAdjourn() == null) {
                        existingRecruitmentRequest.setNumberAdjourn(0);
                    }
                    var oldObj = RecruitmentRequest.builder()
                            .deadline(existingRecruitmentRequest.getDeadline())
                            .build();
                    recruitmentRequestDTO.applyUpdate(existingRecruitmentRequest);
                    existingRecruitmentRequest.setIsPersisted();
                    existingRecruitmentRequest.setLastUpdated(ZonedDateTime.now());

                    Flux<String> listRecruitmentRequest = recruitmentReviewRequestRepository.findByRequestIdAndResults(recruitmentRequestDTO.getId(), true)
                            .map(recruitmentReviewRequestFull -> recruitmentReviewRequestFull.getEmployeeId().toString());

                    return listRecruitmentRequest
                            .collectList()
                            .flatMap(list -> {
                                StringBuilder baseContentBuilder = new StringBuilder("Yêu cầu tuyển dụng vị trí ")
                                        .append(recruitmentRequestDTO.getPosition())
                                        .append(", chức vụ ")
                                        .append(recruitmentRequestDTO.getJobTitle())
                                        .append(" với mục đích ")
                                        .append(recruitmentRequestDTO.getRecruitmentPurposes());

                                String titleOwner = baseContentBuilder + " do bạn tạo đã được";
                                String contentOwner = baseContentBuilder + "do bạn tạo đã được";
                                String titleApprove = "Yêu cầu tuyển dụng do bạn duyệt đã được";
                                String contentApprove = "Yêu cầu tuyển dụng do bạn duyệt đã được";

                                RecruitmentRequestLogs recruitmentChangeLog = null;
                                if (recruitmentRequestDTO.isUpdate()) {
                                    titleOwner += " cập nhật";
                                    contentOwner += " cập nhật";
                                    titleApprove += " cập nhật";
                                    contentApprove += " cập nhật";
                                } else if (recruitmentRequestDTO.isAdjourn()) {
                                    titleOwner += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
                                    contentOwner += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
                                    titleApprove += " gia hạn đến " + recruitmentRequestDTO.getDeadline();
                                    contentApprove += " gia hạn đến " + recruitmentRequestDTO.getDeadline();

                                    existingRecruitmentRequest.setNumberAdjourn(existingRecruitmentRequest.getNumberAdjourn() + 1);
                                    existingRecruitmentRequest.setStatus(RecruitmentStatus.WAITING_RENEW);

                                    var newObject = RecruitmentRequest.builder()
                                            .deadline(existingRecruitmentRequest.getDeadline())
                                            .build();

                                    Optional<Json> change = ObjectComparator.compareObjectsToPostgresJson(oldObj, newObject);
                                    recruitmentChangeLog = RecruitmentRequestLogs.builder()
                                            .id(UUID.randomUUID())
                                            .change(change.orElseThrow())
                                            .changeBy("System")
                                            .recruitmentRequestId(recruitmentRequestDTO.getId())
                                            .changeDate(ZonedDateTime.now())
                                            .build();
                                }

                                Map<String, Object> data = objectMapper.convertValue(recruitmentRequestDTO,
                                        new TypeReference<Map<String, Object>>() {});
                                NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
                                        .content(titleOwner)
                                        .title(contentOwner)
                                        .createdAt(ZonedDateTime.now())
                                        .createdBy("system")
                                        .data(data)
                                        .sentBy("system")
                                        .action("RecruitmentRequestUpdated")
                                        .entityId(existingRecruitmentRequest.getId().toString())
                                        .entityType("RecruitmentRequest")
                                        .recipients(Collections.singleton(existingRecruitmentRequest.getCreatedBy()))
                                        .id(UUID.randomUUID())
                                        .build();
                                Mono.fromRunnable(() -> sendNotification(notificationAddedEvent)).subscribe();

                                if (list.isEmpty() && recruitmentRequestDTO.isUpdate()) {
                                    NotificationAddedEvent notificationAddedEventApprove = NotificationAddedEvent.builder()
                                            .content(titleApprove)
                                            .title(contentApprove)
                                            .createdAt(ZonedDateTime.now())
                                            .createdBy("system")
                                            .data(data)
                                            .sentBy("system")
                                            .action("RecruitmentRequestUpdated")
                                            .entityId(existingRecruitmentRequest.getId().toString())
                                            .entityType("RecruitmentRequest")
                                            .recipients(list)
                                            .id(UUID.randomUUID())
                                            .build();
                                    Mono.fromRunnable(() -> sendNotification(notificationAddedEventApprove)).subscribe();
                                }

                                if (recruitmentChangeLog != null) {
                                    return recruitmentRequestLogsRepository.save(recruitmentChangeLog)
                                            .then(recruitmentReviewRequestRepository.approveConsentRecruitments(recruitmentRequestDTO.getId()))
                                            .then(recruitmentRequestRepository.save(existingRecruitmentRequest))
                                            .flatMap(recruitmentRequest -> createReviews(recruitmentRequestDTO.getId(), empIds, baseContentBuilder.toString())
                                                    .thenReturn(recruitmentRequest));
                                }

                                return recruitmentRequestRepository.save(existingRecruitmentRequest);
                            });
                })
                .map(recruitmentRequestMapper::toDto);
    }

    public Mono<Integer> EmployeeConsentApprove(UUID recruitmentRequestId, String approvalSign) {
        return recruitmentReviewRequestRepository.findByRequestIdAndResultAndIsDeletedIsFalse(recruitmentRequestId, true)
                .flatMap(employeeRequest -> {
                    int currentIndex = positionEmployeeList.indexOf(employeeRequest.getPosition());

                    if (currentIndex == -1 || currentIndex == positionEmployeeList.size() - 1) {
                        return recruitmentReviewRequestRepository
                                .approveConsentRecruitment(employeeRequest.getId(), approvalSign)
                                .then(recruitmentReviewRequestRepository.approveConsentRecruitments(recruitmentRequestId)
                                        .then(Mono.just(0)));
                    }

                    return recruitmentReviewRequestRepository
                            .approveConsentRecruitment(employeeRequest.getId(), approvalSign)
                            .then(recruitmentReviewRequestRepository.findByRequestIdAndPosition(
                                    recruitmentRequestId,
                                    String.valueOf(positionEmployeeList.get(currentIndex + 1))))
                            .flatMap(nextEmployeeRequest -> {
                                Map<String, Object> data = objectMapper.convertValue(employeeRequest,
                                        new TypeReference<Map<String, Object>>() {});
                                NotificationAddedEvent notificationAddedEvent = NotificationAddedEvent.builder()
                                        .content("Có đơn tuyển dụng yêu cầu xét duyệt")
                                        .title("Có đơn tuyển dụng yêu cầu xét duyệt")
                                        .createdAt(ZonedDateTime.now())
                                        .createdBy("system")
                                        .data(data)
                                        .sentBy("system")
                                        .action("RecruitmentRequestUpdated")
                                        .entityId(recruitmentRequestId.toString())
                                        .entityType("RecruitmentRequest")
                                        .recipients(List.of(String.valueOf(nextEmployeeRequest.getEmployeeId())))
                                        .id(UUID.randomUUID())
                                        .build();
                                Mono.fromRunnable(() -> sendNotification(notificationAddedEvent)).subscribe();

                                return recruitmentReviewRequestRepository
                                        .updateStatus(nextEmployeeRequest.getId(), true)
                                        .thenReturn(1);
                            })
                            .onErrorReturn(3);
                })
                .switchIfEmpty(Mono.just(3));
    }
    /**
     * Update a recruitmentRequest.
     *
     * @param recruitmentRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RecruitmentRequestDTO> update(RecruitmentRequestDTO recruitmentRequestDTO) {
        log.debug("Request to update RecruitmentRequest : {}", recruitmentRequestDTO);
        return recruitmentRequestRepository
            .save(recruitmentRequestMapper.toEntity(recruitmentRequestDTO).setIsPersisted())
            .map(recruitmentRequestMapper::toDto);
    }



    /**
     * Partially update a recruitmentRequest.
     *
     * @param recruitmentRequestDTO the entity to update partially.
     * @return the persisted entity.
     */



//    public Mono<Void> writeChangeLog(RecruitmentRequest reOld, RecruitmentRequestDTO reNew) {
//
//    }

    /**
     * Get all the recruitmentRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    @Transactional(readOnly = true)
    public Flux<RecruitmentRequestDTO> findAllByFilter(Pageable pageable, RecruitmentRequestRO moRO) {
        log.debug("Request to query RecruitmentRequests");

        return SecurityUtils.getUserJWTDetail()
            .flatMapMany(login -> {
                moRO.setCompany(String.valueOf(login.getCompanyId()));
                UUID idEmployee = login.getUserId();
                return recruitmentReviewRequestRepository.findListCheckEmployee(idEmployee)
                    .map(RecruitmentReviewRequest::getRequestId)
                    .collectList()
                    .flatMapMany(ids -> {
                        return recruitmentRequestRepository.findAllByFilter(moRO, pageable)
                            .map(recruitmentRequest -> {
                                RecruitmentRequestDTO recruitmentRequestDTO = recruitmentRequest.toDto();
                                if (ids.contains(recruitmentRequest.getId())) {
                                    recruitmentRequestDTO.setCheckApprove(true);
                                }
                                return recruitmentRequestDTO;
                            });
                    });
            });
    }


    /**
     * Returns the number of recruitmentRequests available.
     *
     * @return the number of entities in the database.
     */
    @Transactional(readOnly = true)
    public Mono<Long> countAllByFilter(RecruitmentRequestRO moRO) {
        return recruitmentRequestRepository.countAllByFilter(moRO);
    }

    /**
     * Get one recruitmentRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RecruitmentRequestDTO> findOne(UUID id) {
        log.debug("Request to get RecruitmentRequest : {}", id);
        return recruitmentRequestRepository.findByIdAndNotDelete(id).map(RecruitmentRequest::toDto)
            .zipWith(
                interviewScheduleRepository.findByRecruitmentRequest(id).map(InterviewSchedule::toDto)
                    .collectList(),
                (recruitmentRequestDTO, interviewScheduleDTOS) -> {
                    recruitmentRequestDTO.setInterviewSchedules(interviewScheduleDTOS);
                    return recruitmentRequestDTO;
                });
    }

    /**
     * Delete the recruitmentRequest by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete RecruitmentRequest : {}", id);
        return recruitmentRequestRepository.findById(id)
            .flatMap(existing -> {
                if (!existing.getStatus().equals(RecruitmentStatus.WAITING_APPROVAL)) {
                    return Mono.error(
                        new BadRequestAlertException("Failed to delete RecruitmentRequest: Invalid status",
                            "recruitmentRequest", "invalidStatus"));
                }
                existing.setIsDeleted(true);
                existing.setLastUpdated(ZonedDateTime.now());
                existing.setIsPersisted();
                return recruitmentRequestRepository.save(existing);
            })
            .then();
    }

    public Mono<Integer> approveConsentRecruitment(UUID id, ConsentToApproveRecruitmentRequest consentToReview) {
        log.debug("Request to Recruitment : {}", id);

        return recruitmentRequestRepository.findById(id)
            .flatMap(recruitmentRequest -> {
                if (recruitmentRequest.getNumberAdjourn() == 0 || RecruitmentStatus.WAITING_APPROVAL.equals(recruitmentRequest.getStatus())) {
                    // xet duyet binh thuong
                    return checkIdEmployeeApprove(recruitmentRequest.getId())
                        .flatMap(isApproved -> {
                            if (!isApproved) {
                                return Mono.error(new BadRequestAlertException(
                                    "Employee is not authorized to approve this recruitment request",
                                    "recruitmentRequest", "unauthorized"));
                            }

                            if (recruitmentRequest.getStatus().equals(RecruitmentStatus.WAITING_APPROVAL)) {
                                var approvalSign = consentToReview.getApprovalSignFile();
                                return EmployeeConsentApprove(recruitmentRequest.getId(), approvalSign)
                                    .flatMap(check -> {
                                        if (check == 0) {

                                            recruitmentRequest.setStatus(RecruitmentStatus.APPROVED);

                                            return recruitmentRequestRepository.save(recruitmentRequest)
                                                .thenReturn(1); // Trả về 1 khi phê duyệt thành công
                                        } else {
                                            return Mono.just(3); // Trả về 3 nếu EmployeeConsentApprove không trả về 1
                                        }
                                    })
                                    .onErrorResume(e -> Mono.just(3)); // Xử lý lỗi và trả về 3
                            }

                            return Mono.error(new BadRequestAlertException(
                                "Cannot approve recruitment request with status: " + recruitmentRequest.getStatus(),
                                "recruitmentRequest", "invalidStatus"));
                        });
                } else {
                    return SecurityUtils.getUserJWTDetail()
                        .flatMap(login -> recruitmentReviewRequestRepository.updateResultById(login.getUserId(), id, consentToReview.approvalSignFile, true)
                            .flatMap(updatedRows -> {
                                if (updatedRows > 0) {
                                    return autoUpdateRecruitment(recruitmentRequest.getId())
                                        .thenReturn(2); // Trả về 2 nếu có thành công trong quá trình autoUpdateRecruitment
                                } else {
                                    return Mono.just(4); // Trả về 4 nếu không có dòng nào được cập nhật
                                }
                            })
                            .onErrorResume(e -> Mono.just(4)) // Xử lý lỗi và trả về 4
                        );
                }
            });
    }


    public Mono<Void> autoUpdateRecruitment(UUID idRecruitment) {
        return recruitmentReviewRequestRepository.findAllByRequestIdAndResultAndIsDeletedIsFalse(idRecruitment, false)
            .collectList()
            .flatMap(reviews -> {
                if (reviews.isEmpty()) {
                    return recruitmentRequestRepository.findById(idRecruitment).flatMap(recruitmentRequest -> {
                        if (recruitmentRequest.getStatus().equals(RecruitmentStatus.WAITING_RENEW)) {
                            if (recruitmentRequest.getOldStatus() != null) {
                                recruitmentRequest.setStatus(recruitmentRequest.getOldStatus());
                                if (recruitmentRequest.getStatus() == RecruitmentStatus.WAITING_APPROVAL) {
                                    return this.recoverLastedReview(idRecruitment).then(recruitmentRequestRepository.save(recruitmentRequest.setIsPersisted()).then());
                                }

                            } else {
                                recruitmentRequest.status(RecruitmentStatus.APPROVED);
                            }
                        } else {
                            recruitmentRequest.status(RecruitmentStatus.APPROVED);
                        }
                        return recruitmentRequestRepository.save(recruitmentRequest.setIsPersisted()).then();
                    });
                } else {
                    return Mono.empty();

                }
            });
    }

    public Mono<Void> recoverLastedReview(UUID idRecruitment) {
        return recruitmentReviewRequestRepository.findFirstByRequestIdOrderByCreatedDateDesc(idRecruitment)
            .flatMap(review -> {
                return recruitmentReviewRequestRepository
                    .deleteByRequestId(idRecruitment)
                    .then(recruitmentReviewRequestRepository.findFirstByRequestIdAndCreatedDateBefore(idRecruitment, review.getCreatedDate())
                        .flatMap(reviewBefore -> {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            String createDate = reviewBefore.getCreatedDate().format(formatter);
                            return recruitmentReviewRequestRepository.recoverWhereCreateDateEqual(idRecruitment, createDate)
                                .then();
                        }));
            });
    }




    public Mono<Boolean> checkIdEmployeeApprove(UUID recruitmentRequestId) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> recruitmentReviewRequestRepository
            .findByRequestIdAndResultAndIsDeletedIsFalse(recruitmentRequestId, true)
            .map(employeeRequest -> employeeRequest.getEmployeeId().toString().equals(login.getUserId().toString()))
            .defaultIfEmpty(false));
    }

    public byte[] saveBase64AsFileByte(String base64Data) throws IOException {
        String base64String = base64Data.contains(",") ? base64Data.split(",")[1] : base64Data;
        return Base64.getDecoder().decode(base64String);
    }

    public Mono<Integer> refusalRecruitment(UUID id, RefusalOfApproveRecruitmentRequest refusalOfReview) {
        log.debug("Request to reject recruitment request : {}", id);

        return recruitmentRequestRepository.findById(id)
            .flatMap(recruitmentRequest -> checkIdEmployeeApprove(recruitmentRequest.getId())
                .flatMap(isApproved -> {
//                    if (!isApproved) {
//                        return Mono.error(new BadRequestAlertException(
//                            "Employee is not authorized to reject this recruitment request",
//                            "recruitmentRequest", "unauthorized"));
//                    }

                    recruitmentRequest.setLastUpdated(ZonedDateTime.now());
                    recruitmentRequest.setStatus(RecruitmentStatus.REJECTED);
                    recruitmentRequest.setIsPersisted();

                    // Update the employee request status in the database
                    return recruitmentReviewRequestRepository
                            .approveRefusalRecruitment(recruitmentRequest.getId(),
                                    refusalOfReview.getRejectNote())
                            .then(recruitmentReviewRequestRepository
                                    .approveRefusalRecruitmentAll(recruitmentRequest.getId(), false))
                            .then(recruitmentRequestRepository.save(recruitmentRequest))
                            .thenReturn(1);

//                    if (recruitmentRequest.getStatus().equals(RecruitmentStatus.WAITING_APPROVAL)) {
//                        recruitmentRequest.setLastUpdated(ZonedDateTime.now());
//                        recruitmentRequest.setStatus(RecruitmentStatus.REJECTED);
//                        recruitmentRequest.setIsPersisted();
//
//                        // Update the employee request status in the database
//                        return recruitmentReviewRequestRepository
//                            .approveRefusalRecruitment(recruitmentRequest.getId(),
//                                refusalOfReview.getRejectNote())
//                            .then(recruitmentReviewRequestRepository
//                                .approveRefusalRecruitmentAll(recruitmentRequest.getId(), false))
//                            .then(recruitmentRequestRepository.save(recruitmentRequest))
//                            .thenReturn(1);
//                    }
//
//                    return Mono.error(new BadRequestAlertException(
//                        "Cannot refuse recruitment request with status: " + recruitmentRequest.getStatus(),
//                        "recruitmentRequest",
//                        "invalidStatus"));
                }));
    }

    private UUID tryParseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception e) {
            return UUID.randomUUID();
        }
    }

    public Mono<java.util.List<RecruitmentReviewRequestDTO>> findRecruimentReviewRequest(UUID recruitmentRequestId) {
        return recruitmentReviewRequestRepository.findByRequestId(recruitmentRequestId)
            .map(RecruitmentReviewRequest::toDto) // Convert entity to DTO
            .collectList().flatMap(recruitmentReviewRequestDTOS -> {
                Map<UUID, RecruitmentReviewRequestDTO> recruitmentReviewRequestDTOMap = new HashMap<>();
                recruitmentReviewRequestDTOS.forEach(recruitmentReviewRequestDTO -> {
                    recruitmentReviewRequestDTOMap.put(tryParseUUID(recruitmentReviewRequestDTO.getApprovalSignFile()), recruitmentReviewRequestDTO);
                });
                return fileClient.getFileAttachmentsByListIds(new ArrayList<>(recruitmentReviewRequestDTOMap.keySet()))
                    .map(fileDTO -> {
                        RecruitmentReviewRequestDTO recruitmentReviewRequestDTO = recruitmentReviewRequestDTOMap.get(fileDTO.getId());
                        recruitmentReviewRequestDTO.setApprovalSignFileAttachment(fileDTO);
                        return recruitmentReviewRequestDTO;
                    }).then(Mono.just(recruitmentReviewRequestDTOS));
            });
    }

    public Flux<RecruitmentReviewRequestDTO> getRequestIdByListIds(List<UUID> requestIds) {
        if (requestIds.isEmpty()) {
            return Flux.empty();
        }
        return recruitmentReviewRequestRepository.findByRequestIds(requestIds)
            .map(RecruitmentReviewRequest::toBriefDto); // Convert entity to DTO
    }

    public Mono<Long> countAllChangeLogsByRecruitmentId(UUID id) {
        return recruitmentRequestLogsRepository.countAllByRecruitmentRequestId(id);
    }

    public Flux<RecruitmentChangeLogsDTO> findAllChangeLogsByRecruitmentId(UUID id, Pageable pageable) {
        return recruitmentRequestLogsRepository.findAllByRecruitmentRequestIdOrderByChangeDateAsc(id, pageable)
            .map(RecruitmentRequestLogs::toDto);
    }

    ;

}
