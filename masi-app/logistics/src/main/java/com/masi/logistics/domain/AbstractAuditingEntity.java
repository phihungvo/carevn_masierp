package com.masi.logistics.domain;

import com.carevn.masi.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import reactor.core.publisher.Mono;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data

@JsonIgnoreProperties(value = {"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"}, allowGetters = true)
public abstract class AbstractAuditingEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7239833055232266240L;

    public abstract T getId();


    @Column(name = "created_by", nullable = false, length = 100, updatable = false)
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_by", length = 100)
    protected String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected ZonedDateTime updatedAt = ZonedDateTime.now();

    @Column(name = "deleted_by", length = 100)
    protected String deletedBy;

    @Column(name = "deleted_at")
    protected ZonedDateTime deletedAt = null;

    @Column(name = "company")
    protected String company;

    @Column(name = "department")
    protected String department;

    public abstract boolean isNew();

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void delete(String deletedBy) {
        this.deletedBy = deletedBy;
        this.deletedAt = ZonedDateTime.now();
    }

    public Mono<Void> deleteAsync() {
        return SecurityUtils.getUserJWTDetail().flatMap(user -> {
                this.deletedBy = user.getUserId().toString();
                this.deletedAt = ZonedDateTime.now();
                return Mono.empty();
            })
            .doOnError(e -> {
                this.deletedBy = "system";
                this.deletedAt = ZonedDateTime.now();
            }).then();
    }

}
