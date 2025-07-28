package com.masi.employee.service.dto;

import com.carevn.masi.dto.EmbedFile;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.employee.domain.Documentary;
import com.masi.employee.domain.enumeration.DocumentaryGroup;
import com.masi.employee.domain.enumeration.DocumentaryStatus;
import com.masi.employee.domain.enumeration.DocumentaryType;
import io.r2dbc.postgresql.codec.Json;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.employee.domain.Documentary} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentaryDTO implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "must not be null")
    private String documentNumber;

    @NotNull(message = "must not be null")
    private LocalDate dateStart;

    @NotNull(message = "must not be null")
    private DocumentaryGroup group;

    @NotNull(message = "must not be null")
    private DocumentaryType type;

    @NotNull(message = "must not be null")
    private String content;

    @NotNull(message = "must not be null")
    private UUID signer;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = EmployeeProfileDTO.class)
    private EmployeeProfileDTO employeeProfileSigner;

    @NotNull(message = "must not be null")
    private String recipient;

    @NotNull(message = "must not be null")
    private String archiveLocation;

    private UUID senderOrReceiver;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = EmployeeProfileDTO.class)
    private EmployeeProfileDTO employeeProfileSender;


    private String attachmentsData;

    private String attachmentsContentFile;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = FileAttachmentDTO.class)
    private FileAttachmentDTO attachmentsFile;
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Json attachments;

    public Json getAttachments() {
        if (attachments != null) {
            return attachments;
        }
        if (embedFiles != null) {
            return com.carevn.masi.dto.EmbedFile.fromList(embedFiles);
        }
        return Json.of("[]");
    }


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(allOf = EmbedFile.class)
    private Collection<EmbedFile> embedFiles;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String approvalSignFile;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = FileAttachmentDTO.class)
    private FileAttachmentDTO signFile;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String rejectNote;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String idGroup;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String idCompany;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DocumentaryStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String department;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = String.class)
    private String updatedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(allOf = String.class)
    private String deletedBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentaryDTO documentaryDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore


    public void applyUpdate(Documentary existingDocumentary) {
        existingDocumentary.setDocumentNumber(this.documentNumber);
        existingDocumentary.setDateStart(this.dateStart);
        existingDocumentary.setGroup(this.group);
        existingDocumentary.setType(this.type);
        existingDocumentary.setContent(this.content);
        existingDocumentary.setSigner(this.signer);
        existingDocumentary.setRecipient(this.recipient);
        existingDocumentary.setArchiveLocation(this.archiveLocation);
        existingDocumentary.setSenderOrReceiver(this.senderOrReceiver);
        existingDocumentary.setAttachmentsContentFile(this.attachmentsContentFile);
        existingDocumentary.setAttachments(this.getAttachments());
    }
}
