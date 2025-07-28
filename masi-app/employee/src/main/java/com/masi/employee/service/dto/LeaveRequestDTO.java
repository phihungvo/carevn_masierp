package com.masi.employee.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.EmbedFile;
import com.masi.employee.domain.LeaveRequest;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import io.r2dbc.postgresql.codec.Json;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A DTO for the {@link com.masi.employee.domain.LeaveRequest} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRequestDTO implements Serializable {

    private UUID id;

    @NotNull(message = "must not be null")
    private LeaveRequestStatus status;

    @NotNull(message = "must not be null")
    private LocalDate fromDate;

    // caculated fields
    private ZonedDateTime zonedFromDate;


    @NotNull(message = "must not be null")
    private LocalDate toDate;

    private ZonedDateTime zonedToDate;

    private ZonedDateTime fromTime;

    private ZonedDateTime toTime;

    private String reason;

    @NotNull(message = "must not be null")
    private LeaveType leaveRequestType;

    @Lob
    private byte[] fileAttachment;

    private String fileId;

    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json files = Json.of("[]");

    private Float totalDayOff;

    public Float getTotalDayOff() {
        if (totalDayOff == null) {
            return 0f;
        }
        return totalDayOff;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<EmbedFile> uploadFiles;

    private String fileAttachmentContentType;

    private String fileAttachmentName;

    @NotNull(message = "must not be null")
    private LeaveRequestDayType leaveRequestDayType;

    private Boolean isActive;

    private EmployeeDTO employee;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastUpdated;

    private UUID employeeId;

    private Set<LeaveRequestReviewDTO> reviews;

    @NotNull(message = "must not be null")
    @NotEmpty(message = "must not be empty")
    private Set<UUID> reviewerIds;

    private EmployeeDTO substitute;

    private UUID substituteId;

    @JsonIgnore
    public Json getFilesJson() {
        if (this.uploadFiles == null) {
            return Json.of("[]");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String jsonStr = "{}";
        try {
            jsonStr = ow.writeValueAsString(uploadFiles);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Json.of(jsonStr);
    }

    public LeaveRequest toEntity() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(this.id);
        leaveRequest.setStatus(this.status);
        leaveRequest.setFromDate(this.fromDate);
        leaveRequest.setToDate(this.toDate);
        leaveRequest.setReason(this.reason);
        leaveRequest.setLeaveRequestType(this.leaveRequestType);
        leaveRequest.setFileAttachment(this.fileAttachment);
        leaveRequest.setFileAttachmentContentType(this.fileAttachmentContentType);
        leaveRequest.setLeaveRequestDayType(this.leaveRequestDayType);
        leaveRequest.setEmployeeId(this.employeeId);
        leaveRequest.setSubstituteId(this.substituteId);
        leaveRequest.setFileAttachmentName(fileAttachmentName);
        leaveRequest.setTotalDayOff(this.totalDayOff);
        if (LeaveRequestDayType.HALF_DAY.equals(this.getLeaveRequestDayType())) {
            leaveRequest.setFromTime(this.fromTime);
            leaveRequest.setToTime(this.toTime);
        }
        leaveRequest.setFileId(this.fileId);
        return leaveRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveRequestDTO)) {
            return false;
        }

        LeaveRequestDTO leaveRequestDTO = (LeaveRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveRequestDTO [id=" + id + ", status=" + status + ", fromDate=" + fromDate + ", toDate=" + toDate + ", fromTime=" + fromTime + ", toTime=" + toTime + ", reason=" + reason + ", leaveRequestType=" + leaveRequestType + ", fileAttachment=" + Arrays.toString(fileAttachment) + ", fileAttachmentContentType=" + fileAttachmentContentType + ", leaveRequestDayType=" + leaveRequestDayType + ", isActive=" + isActive + ", employee=" + employee + ", createdAt=" + createdAt + ", lastUpdated=" + lastUpdated + ", employeeId=" + employeeId + ", reviews=" + reviews + ", reviewerIds=" + reviewerIds + ", substitute=" + substitute + ", substituteId=" + substituteId + "]";
    }

    public void addReview(LeaveRequestReviewDTO review) {
        if (Objects.isNull(this.reviews)) {
            this.reviews = new HashSet<>();
        }
        this.reviews.add(review);
    }


}
