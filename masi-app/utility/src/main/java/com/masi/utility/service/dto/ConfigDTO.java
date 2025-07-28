package com.masi.utility.service.dto;

import com.masi.utility.domain.Config;
import com.masi.utility.domain.enumeration.DataType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.masi.utility.domain.Config} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfigDTO implements Serializable {

    private Long id;

    private String key;

    private String description;

    private String value;

    private DataType type;

    private ZonedDateTime updatedAt;

    private String updatedBy;

    private String company;

    public void applyChange(Config configDTO) {
        if (configDTO == null) {
            return;
        }
        if (configDTO.getDescription() != null) {
            this.setDescription(configDTO.getDescription());
        }
        if (configDTO.getValue() != null) {
            this.setValue(configDTO.getValue());
        }

    }

}
