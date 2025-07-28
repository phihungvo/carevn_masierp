package com.masi.utility.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.masi.utility.domain.HolidayConfig;
import com.masi.utility.domain.enumeration.CalenderType;
import com.masi.utility.domain.enumeration.HolidayType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.masi.utility.domain.HolidayConfig} entity.
 */
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HolidayConfigDTO implements Serializable {

    private UUID id;

    private String name;

    private HolidayType type = HolidayType.FIXED;

    private CalenderType calenderType = CalenderType.SOLAR;

    private LocalDate date;

    private String description;

    private ZonedDateTime createdAt = ZonedDateTime.now();

    private ZonedDateTime updatedAt;

    public LocalDate getSolarDate() {
        //toDo: convert date to solar
        if (calenderType == CalenderType.SOLAR) {
            return date;
        }
        return date;
    }


    public void applyChanges(HolidayConfig holidayConfigDTO) {
        if (holidayConfigDTO == null) {
            return;
        }
        if (holidayConfigDTO.getName() != null) {
            this.setName(holidayConfigDTO.getName());
        }
        if (holidayConfigDTO.getDescription() != null) {
            this.setDescription(holidayConfigDTO.getDescription());
        }
        updatedAt = ZonedDateTime.now();
    }


}
