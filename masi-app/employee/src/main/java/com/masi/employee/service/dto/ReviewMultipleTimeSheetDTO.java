package com.masi.employee.service.dto;

import java.util.UUID;

import jakarta.persistence.Lob;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReviewMultipleTimeSheetDTO {
    private String note;
    private String signatureFile;
    private UUID[] ids;
}
