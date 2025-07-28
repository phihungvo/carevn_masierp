package com.masi.utility.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.utility.domain.NotificationRecipient} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationRecipientDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private UUID notificationId;

    @NotNull(message = "must not be null")
    private UUID recipientId;

    @NotNull(message = "must not be null")
    private Boolean read;

    private ZonedDateTime readAt;

    private NotificationDTO notification;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public ZonedDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(ZonedDateTime readAt) {
        this.readAt = readAt;
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationRecipientDTO)) {
            return false;
        }

        NotificationRecipientDTO notificationRecipientDTO = (NotificationRecipientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationRecipientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationRecipientDTO{" +
            "id='" + getId() + "'" +
            ", notificationId='" + getNotificationId() + "'" +
            ", recipientId='" + getRecipientId() + "'" +
            ", read='" + getRead() + "'" +
            ", readAt='" + getReadAt() + "'" +
            ", notification=" + getNotification() +
            "}";
    }
}
