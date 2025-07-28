package com.masi.utility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.r2dbc.postgresql.codec.Json;
import jakarta.validation.constraints.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Notification.
 */
@Table("notification")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification implements Serializable, Persistable<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @Column("content")
    private String content;

    @Column("title")
    private String title;

    @Column("entity_name")
    private String entityName;

    @Column("entity_id")
    private String entityId;

    @Column("entity_type")
    private String entityType;

    @NotNull(message = "must not be null")
    @Column("created_at")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    @Column("created_by")
    private String createdBy;

    @Transient
    private boolean isPersisted;

    @Column("data")
    private Json  data;

    @Column("category")
    private String category;


    @Column("sent_by")
    private String sentBy;


    

    @Transient
    @JsonIgnoreProperties(value = { "notification" }, allowSetters = true)
    private Set<NotificationRecipient> notificationRecipients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public Notification id(UUID id) {
        this.setId(id);
        return this;
    }



    public Notification content(String content) {
        this.setContent(content);
        return this;
    }

    public Notification title(String title) {
        this.setTitle(title);
        return this;
    }

    public Notification entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }



    public Notification entityId(String entityId) {
        this.setEntityId(entityId);
        return this;
    }



    public Notification entityType(String entityType) {
        this.setEntityType(entityType);
        return this;
    }



    public Notification createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }


    public Notification createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public Notification setIsPersisted() {
        this.isPersisted = true;
        return this;
    }



    public void setNotificationRecipients(Set<NotificationRecipient> notificationRecipients) {
        if (this.notificationRecipients != null) {
            this.notificationRecipients.forEach(i -> i.setNotification(null));
        }
        if (notificationRecipients != null) {
            notificationRecipients.forEach(i -> i.setNotification(this));
        }
        this.notificationRecipients = notificationRecipients;
    }

    public void notificationRecipients(Set<NotificationRecipient> notificationRecipients) {
        this.setNotificationRecipients(notificationRecipients);
    }

    public void addNotificationRecipient(NotificationRecipient notificationRecipient) {
        this.notificationRecipients.add(notificationRecipient);
        notificationRecipient.setNotification(this);
    }

    public void removeNotificationRecipient(NotificationRecipient notificationRecipient) {
        this.notificationRecipients.remove(notificationRecipient);
        notificationRecipient.setNotification(null);
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

}
