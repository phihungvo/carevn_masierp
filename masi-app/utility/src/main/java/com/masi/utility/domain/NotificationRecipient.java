package com.masi.utility.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
 * A NotificationRecipient.
 */
@Table("notification_recipient")
@JsonIgnoreProperties(value = {"new"})
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRecipient implements Serializable, Persistable<UUID> {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "must not be null")
    @Id
    @Column("id")
    private UUID id;

    @NotNull(message = "must not be null")
    @Column("recipient_id")
    private UUID recipientId;

    @NotNull(message = "must not be null")
    @Column("read")
    private Boolean read=false;

    @Column("read_at")
    private ZonedDateTime readAt;

    @Transient
    private boolean isPersisted;

    @Transient
    @JsonIgnoreProperties(value = {"notificationRecipients"}, allowSetters = true)
    private Notification notification;

    @Column("notification_id")
    private UUID notificationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here


    public NotificationRecipient id(UUID id) {
        this.setId(id);
        return this;
    }


    public NotificationRecipient notificationId(UUID notificationId) {
        this.setNotificationId(notificationId);
        return this;
    }


    public NotificationRecipient recipientId(UUID recipientId) {
        this.setRecipientId(recipientId);
        return this;
    }


    public NotificationRecipient read(Boolean read) {
        this.setRead(read);
        return this;
    }


    public NotificationRecipient readAt(ZonedDateTime readAt) {
        this.setReadAt(readAt);
        return this;
    }


    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public NotificationRecipient setIsPersisted() {
        this.isPersisted = true;
        return this;
    }


    public void setNotification(Notification notification) {
        this.notification = notification;
        this.notificationId = notification != null ? notification.getId() : null;
    }

    public NotificationRecipient notification(Notification notification) {
        this.setNotification(notification);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationRecipient)) {
            return false;
        }
        return getId() != null && getId().equals(((NotificationRecipient) o).getId());
    }

}
