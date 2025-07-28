package com.masi.employee.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masi.employee.domain.EmbedFile;
import com.masi.employee.domain.Employee;
import com.masi.employee.domain.ProcessLeaveRegimeRequest;
import com.masi.employee.domain.enumeration.LeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.domain.enumeration.ProcessLeaveRegimeRequestStatus;
import com.masi.employee.domain.enumeration.ReviewStatus;

import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRegimeRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@Log4j2
public class LeaveRegimeRequestDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private LeaveType leaveType;

    @NotNull(message = "must not be null")
    private ZonedDateTime lastWorkDate;

    @NotNull(message = "must not be null")
    private ZonedDateTime returnWorkDate;

    private ZonedDateTime fromTime;

    private ZonedDateTime toTime;

    private UUID substituteId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = Employee.class)
    private ArrayList<EmployeeDTO> approvers;

    private String companyId;

    @NotNull(message = "must not be null")
    private UUID employeeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = Employee.class)
    private EmployeeDTO substitute;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = Employee.class)
    private EmployeeDTO employee;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = ProcessLeaveRegimeRequest.class)
    private List<ProcessLeaveRegimeRequestDTO> processLeaveRegimeRequests;

    private LeaveRequestDayType leaveRequestDayType = LeaveRequestDayType.FULL_DAY;

    private LeaveRegimeRequestStatus status;

    private Collection<EmbedFile> embedFiles;
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json files;

    private UUID leaveRequestId;

    private String fileId;

    private String fileName;

    private String department;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime deletedAt;

    private Boolean isDeleted;

    private UUID createdBy;

    private UUID updatedBy;

    private UUID deletedBy;
    private Float totalDayOff = 0f;
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveRegimeRequestDTO leaveRegimeRequestDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveRegimeRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore

    public static final String leaveRequestRegimeVietnamese = "Đăng ký nghỉ chế độ: ";

    @JsonIgnore
    public Json getFilesJson() {
        if (this.embedFiles == null) {
            return Json.of("[]");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String jsonStr = "{}";
        try {
            jsonStr = ow.writeValueAsString(embedFiles);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Json.of(jsonStr);
    }

    public LeaveRequestDTO toLeaveRequestDTO() {

        var leaveRequestDTO = new LeaveRequestDTO();
        var leaveRequestId = UUID.randomUUID();

        leaveRequestDTO.setId(leaveRequestId);
        leaveRequestDTO.setCreatedAt(ZonedDateTime.now());
        leaveRequestDTO.setLastUpdated(ZonedDateTime.now());
        leaveRequestDTO.setIsActive(true);
        leaveRequestDTO.setLeaveRequestType(leaveType);
        leaveRequestDTO.setStatus(LeaveRequestStatus.APPROVED);
        leaveRequestDTO.setLeaveRequestDayType(this.leaveRequestDayType == null ? LeaveRequestDayType.FULL_DAY : this.leaveRequestDayType);
        if (LeaveRequestDayType.HALF_DAY.equals(this.getLeaveRequestDayType())) {
            leaveRequestDTO.setFromTime(this.fromTime);
            leaveRequestDTO.setToTime(this.toTime);
        }
//        leaveRequestDTO.setFromDate(this.lastWorkDate.plusDays(1).toLocalDate());
        log.info("zoned lastWorkDate: {}", this.lastWorkDate.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")));
        log.info("lastWorkDate: {}", this.lastWorkDate.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate());
        leaveRequestDTO.setFromDate(this.lastWorkDate.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate());
        leaveRequestDTO.setToDate(this.returnWorkDate.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate());

        log.info("zoned returnWorkDate: {}", this.returnWorkDate.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")));
        log.info("returnWorkDate: {}", this.returnWorkDate.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate());
        leaveRequestDTO.setCreatedAt(ZonedDateTime.now());
        leaveRequestDTO.setEmployeeId(this.employeeId);
        leaveRequestDTO.setSubstituteId(this.substituteId);
        leaveRequestDTO.setReason(leaveRequestRegimeVietnamese + LeaveType.VIETNAMESE_MAP.get(leaveType));
        leaveRequestDTO.setFileId(this.fileId);
        leaveRequestDTO.setFileAttachmentName(this.fileName);
        Set<UUID> reviewerIds = new HashSet<>();

        for (var item : processLeaveRegimeRequests) {
            var leaveRequestReviewDTO = new LeaveRequestReviewDTO();
            leaveRequestReviewDTO.setId(UUID.randomUUID());
            leaveRequestReviewDTO.setLeaveRequestId(leaveRequestId);
            leaveRequestReviewDTO.setIsActive(true);
            leaveRequestReviewDTO.setReviewerId(item.getApproverId());
            leaveRequestReviewDTO.setStatus(ReviewStatus.APPROVED);
            leaveRequestReviewDTO.setCreatedAt(ZonedDateTime.now());
            leaveRequestReviewDTO.setFileId(item.getFileId());
            leaveRequestReviewDTO.setFileName(item.getFileName());
            leaveRequestReviewDTO.setIsActive(true);
            reviewerIds.add(item.getApproverId());
            leaveRequestDTO.addReview(leaveRequestReviewDTO);
        }
        leaveRequestDTO.setReviewerIds(reviewerIds);
        return leaveRequestDTO;
    }

    public ArrayList<ProcessLeaveRegimeRequestDTO> toProcessLeaveRegimeRequest(ArrayList<UUID> approverIds) {
        var listDto = new ArrayList<ProcessLeaveRegimeRequestDTO>();
        for (var item : approverIds) {
            ProcessLeaveRegimeRequestDTO processLeaveRegimeRequestDTO = new ProcessLeaveRegimeRequestDTO();
            processLeaveRegimeRequestDTO.setId(UUID.randomUUID());
            processLeaveRegimeRequestDTO.setLeaveRegimeRequestId(this.id);
            processLeaveRegimeRequestDTO.setEmployeeId(this.getEmployeeId());
            processLeaveRegimeRequestDTO.setStatus(ProcessLeaveRegimeRequestStatus.WAITING_APPROVAL);
            processLeaveRegimeRequestDTO.setApproverId(item);
            processLeaveRegimeRequestDTO.setIsDeleted(false);
            processLeaveRegimeRequestDTO.setCreatedBy(this.createdBy);
            processLeaveRegimeRequestDTO.setCreatedAt(ZonedDateTime.now());
            listDto.add(processLeaveRegimeRequestDTO);
        }
        return listDto;
    }

    public long calculateLeaveDays() {
        if (lastWorkDate == null || returnWorkDate == null) {
            throw new IllegalArgumentException("Both lastWorkDate and returnWorkDate must not be null");
        }
        var days = ChronoUnit.DAYS.between(lastWorkDate, returnWorkDate);
        System.out.println("days: " + days);
        return days;
    }

}
