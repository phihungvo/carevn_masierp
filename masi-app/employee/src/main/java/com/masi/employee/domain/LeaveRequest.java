package com.masi.employee.domain;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.enumeration.LeaveRequestDayType;
import com.masi.employee.domain.enumeration.LeaveRequestStatus;
import com.masi.employee.domain.enumeration.LeaveType;
import com.masi.employee.service.dto.LeaveRequestDTO;
import com.masi.employee.service.dto.LeaveRequestReviewDTO;
import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A LeaveRequest.
 */
@Data
@Table("leave_request")
@JsonIgnoreProperties(value = {"new"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRequest implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("status")
    private LeaveRequestStatus status;

    @NotNull(message = "must not be null")
    @Column("from_date")
    private LocalDate fromDate;

    @NotNull(message = "must not be null")
    @Column("to_date")
    private LocalDate toDate;

    @Column("files")
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json files;

    @Column("from_time")
    private ZonedDateTime fromTime;

    @Column("to_time")
    private ZonedDateTime toTime;

    @Column("reason")
    private String reason;

    @NotNull(message = "must not be null")
    @Column("leave_request_type")
    private LeaveType leaveRequestType;

    @Column("file_attachment")
    private byte[] fileAttachment;

    @Column("file_id")
    private String fileId;

    @Column("file_attachment_content_type")
    private String fileAttachmentContentType;

    @Column("file_attachment_name")
    private String fileAttachmentName;

    @NotNull(message = "must not be null")
    @Column("leave_request_day_type")
    private LeaveRequestDayType leaveRequestDayType;

    @NotNull(message = "must not be null")
    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private ZonedDateTime createdAt;

    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Transient
    private boolean isPersisted;

    @Transient
    private Employee employee;

    @Transient
    private Employee substitute;

    @Transient
    @JsonIgnoreProperties(value = {"leaveRequest"}, allowSetters = true)
    private Set<LeaveRequestReview> reviews = new HashSet<>();

    @Column("employee_id")
    @NotNull(message = "must not be null")
    private UUID employeeId;

    @Column("substitute_id")
    private UUID substituteId;
    @Column("total_day_off")
    private Float totalDayOff = 0f;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LeaveRequest id(UUID id) {
        this.setId(id);
        return this;
    }

    public LeaveRequest status(LeaveRequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public LeaveRequest fromDate(LocalDate fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public LeaveRequest toDate(LocalDate toDate) {
        this.setToDate(toDate);
        return this;
    }

    public LeaveRequest fromTime(ZonedDateTime fromTime) {
        this.setFromTime(fromTime);
        return this;
    }

    public LeaveRequest toTime(ZonedDateTime toTime) {
        this.setToTime(toTime);
        return this;
    }

    public LeaveRequest reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public LeaveRequest leaveRequestType(LeaveType leaveRequestType) {
        this.setLeaveRequestType(leaveRequestType);
        return this;
    }

    public LeaveRequest fileAttachment(byte[] fileAttachment) {
        this.setFileAttachment(fileAttachment);
        return this;
    }

    public LeaveRequest fileAttachmentContentType(String fileAttachmentContentType) {
        this.fileAttachmentContentType = fileAttachmentContentType;
        return this;
    }

    public LeaveRequest fileAttachmentName(String fileAttachmentName) {
        this.setFileAttachmentName(fileAttachmentName);
        return this;
    }

    public LeaveRequest leaveRequestDayType(LeaveRequestDayType leaveRequestDayType) {
        this.setLeaveRequestDayType(leaveRequestDayType);
        return this;
    }

    public LeaveRequest isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public LeaveRequest createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public LeaveRequest lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public LeaveRequest setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public LeaveRequest employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public LeaveRequest substitute(Employee employee) {
        this.setSubstitute(employee);
        return this;
    }

    public LeaveRequest reviews(Set<LeaveRequestReview> leaveRequestReviews) {
        this.setReviews(leaveRequestReviews);
        return this;
    }

    public LeaveRequest addReviews(LeaveRequestReview leaveRequestReview) {
        this.reviews.add(leaveRequestReview);
        leaveRequestReview.setLeaveRequest(this);
        return this;
    }

    public LeaveRequest removeReviews(LeaveRequestReview leaveRequestReview) {
        this.reviews.remove(leaveRequestReview);
        leaveRequestReview.setLeaveRequest(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void partialUpdate(LeaveRequestDTO leaveRequestDTO) {
        this.status = leaveRequestDTO.getStatus();
        this.reason = leaveRequestDTO.getReason();
        this.lastUpdated = ZonedDateTime.now();
        this.isPersisted = true;
    }

    public LeaveRequestDTO toDto() {
        LeaveRequestDTO leaveRequestDTO = new LeaveRequestDTO();
        leaveRequestDTO.setId(this.id);
        leaveRequestDTO.setStatus(this.status);
        leaveRequestDTO.setFromDate(this.fromDate);
        leaveRequestDTO.setToDate(this.toDate);
        leaveRequestDTO.setReason(this.reason);
        leaveRequestDTO.setLeaveRequestType(this.leaveRequestType);
        leaveRequestDTO.setFileAttachment(this.fileAttachment);
        leaveRequestDTO.setFileAttachmentContentType(this.fileAttachmentContentType);
        leaveRequestDTO.setLeaveRequestDayType(this.leaveRequestDayType);
        leaveRequestDTO.setIsActive(this.isActive);
        leaveRequestDTO.setEmployeeId(this.employeeId);
        leaveRequestDTO.setSubstituteId(this.substituteId);
        leaveRequestDTO.setFromTime(fromTime);
        leaveRequestDTO.setToTime(toTime);
        leaveRequestDTO.setFileAttachmentName(fileAttachmentName);
        leaveRequestDTO.setFileId(fileId);
        leaveRequestDTO.setFiles(files);
        leaveRequestDTO.setTotalDayOff(totalDayOff);

        if (Objects.nonNull(this.fromDate)) {
            leaveRequestDTO.setZonedFromDate(this.fromDate.atStartOfDay(ZoneOffset.UTC).toInstant().atZone(ZoneId.of("UTC")));
        }

        if (Objects.nonNull(this.toDate)) {
            leaveRequestDTO.setZonedToDate(this.toDate.atStartOfDay(ZoneOffset.UTC).toInstant().atZone(ZoneId.of("UTC")));
        }
        if (Objects.nonNull(this.substitute)) leaveRequestDTO.setSubstitute(this.substitute.toDto());
        if (Objects.nonNull(this.employee)) leaveRequestDTO.setEmployee(this.employee.toDto());
        if (this.reviews != null) {
            leaveRequestDTO.setReviews(this.reviews.stream().map(entity -> {
                entity.setLeaveRequest(null); // add this for prevent infinite loop
                return entity.toDto();
            }).collect(Collectors.toSet()));
        }
        return leaveRequestDTO;
    }

}
