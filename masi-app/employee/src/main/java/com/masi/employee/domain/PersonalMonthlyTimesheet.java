package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import com.masi.employee.domain.enumeration.WorkspaceType;
import com.masi.employee.service.dto.PersonalMonthlyTimesheetDTO;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PersonalMonthlyTimesheet.
 */
@Table("personal_monthly_timesheet")
@JsonIgnoreProperties(value = {"new"})
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class PersonalMonthlyTimesheet implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("month")
    private LocalDate month;

    @NotNull(message = "must not be null")
    @Column("status")
    private TimesheetReviewStatus status;

    @NotNull(message = "must not be null")
    @Column("created_date")
    private ZonedDateTime createdDate;

    @NotNull(message = "must not be null")
    @Column("last_updated")
    private ZonedDateTime lastUpdated;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"violation", "personalMonthlyTimesheet"}, allowSetters = true)
    private Set<TimeKeeping> timeKeepings = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = {"personalMonthlyTimesheets"}, allowSetters = true)
    private Employee employee;

    @Column("employee_id")
    private UUID employeeId;

    @Transient
    private MonthlyTimeSheetReview review;

    @Column("review_id")
    private UUID reviewId;

    @Column("type")
    private TimeKeepingType timeKeepingType = TimeKeepingType.HOUR;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public PersonalMonthlyTimesheet id(UUID id) {
        this.setId(id);
        return this;
    }


    public PersonalMonthlyTimesheet month(LocalDate month) {
        this.setMonth(month);
        return this;
    }


    public PersonalMonthlyTimesheet status(TimesheetReviewStatus status) {
        this.setStatus(status);
        return this;
    }


    public PersonalMonthlyTimesheet createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }


    public PersonalMonthlyTimesheet lastUpdated(ZonedDateTime lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public PersonalMonthlyTimesheet timeKeepingType(TimeKeepingType timeKeepingType) {
        this.setTimeKeepingType(timeKeepingType);
        return this;
    }



    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PersonalMonthlyTimesheet setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setTimeKeepings(Set<TimeKeeping> timeKeepings) {
        if (this.timeKeepings != null) {
            this.timeKeepings.forEach(i -> i.setPersonalMonthlyTimesheet(null));
        }
        if (timeKeepings != null) {
            timeKeepings.forEach(i -> i.setPersonalMonthlyTimesheet(this));
        }
        this.timeKeepings = timeKeepings;
    }

    public PersonalMonthlyTimesheet timeKeepings(Set<TimeKeeping> timeKeepings) {
        this.setTimeKeepings(timeKeepings);
        return this;
    }

    public PersonalMonthlyTimesheet addTimeKeepings(TimeKeeping timeKeeping) {
        this.timeKeepings.add(timeKeeping);
        timeKeeping.setPersonalMonthlyTimesheet(this);
        return this;
    }

    public PersonalMonthlyTimesheet removeTimeKeepings(TimeKeeping timeKeeping) {
        this.timeKeepings.remove(timeKeeping);
        timeKeeping.setPersonalMonthlyTimesheet(null);
        return this;
    }


    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public PersonalMonthlyTimesheet employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }


    public PersonalMonthlyTimesheet employeeId(UUID employee) {
        this.setEmployeeId(employee);
        return this;
    }


    public void setReview(MonthlyTimeSheetReview monthlyTimeSheetReview) {
        this.review = monthlyTimeSheetReview;
        this.reviewId = monthlyTimeSheetReview != null ? monthlyTimeSheetReview.getId() : null;
    }

    public PersonalMonthlyTimesheet review(MonthlyTimeSheetReview monthlyTimeSheetReview) {
        this.setReview(monthlyTimeSheetReview);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalMonthlyTimesheet)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonalMonthlyTimesheet) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalMonthlyTimesheet{" + "id=" + getId() + ", month='" + getMonth() + "'" + ", status='" + getStatus() + "'" + ", createdDate='" + getCreatedDate() + "'" + ", lastUpdated='" + getLastUpdated() + "'" + "}";
    }

    public PersonalMonthlyTimesheetDTO toDto() {
        PersonalMonthlyTimesheetDTO dto = new PersonalMonthlyTimesheetDTO();
        dto.setId(id);
        dto.setMonth(month);
        dto.setStatus(status);
        dto.setCreatedDate(createdDate);
        dto.setLastUpdated(lastUpdated);
        if (employee != null) {
            dto.setEmployee(employee.toDto());
        }

        if (getReview() != null) {
            dto.setReview(review.toDto());
        }
        return dto;
    }
}
