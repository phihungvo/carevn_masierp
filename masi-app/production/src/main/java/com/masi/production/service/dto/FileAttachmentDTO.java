package com.masi.production.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;


@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileAttachmentDTO implements Serializable {

    private UUID id;

    private String name;

    private String path;

    private Long fileSize;

    private String mimeType;

    private String company;

    private String type;

    public void copyForm(FileAttachmentDTO fileAttachmentDTO) {
        this.id = fileAttachmentDTO.id;
        this.name = fileAttachmentDTO.name;
        this.path = fileAttachmentDTO.path;
        this.fileSize = fileAttachmentDTO.fileSize;
        this.mimeType = fileAttachmentDTO.mimeType;
    }

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
