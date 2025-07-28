package com.masi.employee.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileAttachmentDTO implements Serializable {

    @NotNull(message = "must not be null")
    private UUID id;

    @NotNull(message = "must not be null")
    private String name;

    @NotNull(message = "must not be null")
    private String path;

    @NotNull(message = "must not be null")
    private Long fileSize;

    @NotNull(message = "must not be null")
    private String mimeType;


    @NotNull(message = "must not be null")
    private ZonedDateTime createdAt;

    @NotNull(message = "must not be null")
    private String createdBy;

    private ZonedDateTime updatedAt;

    private String updatedBy;

    private ZonedDateTime deletedAt;

    private String deletedBy;

    private String company;

    private byte[] base64;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileAttachmentDTO fileAttachmentDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileAttachmentDTO.id);
    }


}
