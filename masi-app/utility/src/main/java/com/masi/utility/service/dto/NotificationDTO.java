package com.masi.utility.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.utility.utils.PgJsonObjectDeserializer;
import com.masi.utility.utils.PgJsonObjectSerializer;

import io.r2dbc.postgresql.codec.Json;

/**
 * A DTO for the {@link com.masi.utility.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
public class NotificationDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    private String content;

    private String title;

    private String entityName;

    private String entityId;

    private String entityType;

    @NotNull(message = "must not be null")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    private String createdBy;
       @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json data;
    private String category;
    private String sentBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
