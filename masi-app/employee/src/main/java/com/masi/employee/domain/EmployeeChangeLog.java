package com.masi.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import io.r2dbc.postgresql.codec.Json;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A EmployeeChangeLog.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("employee_change_log")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmployeeChangeLog implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private UUID id;

    @Column("change_date")
    private ZonedDateTime changeDate;

    @Column("change")
    private Json change;

    @Column("change_by")
    private String changeBy;
    @Column("employee_id")
    private UUID employeeId;

    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EmployeeChangeLog id(UUID id) {
        this.setId(id);
        return this;
    }

    public EmployeeChangeLog changeDate(ZonedDateTime changeDate) {
        this.setChangeDate(changeDate);
        return this;
    }


    public EmployeeChangeLog changeBy(String changeBy) {
        this.setChangeBy(changeBy);
        return this;
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public EmployeeChangeLog setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

}
