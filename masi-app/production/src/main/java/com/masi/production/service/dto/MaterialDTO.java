package com.masi.production.service.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class MaterialDTO {
    private UUID id;
    private String name;
    private String note;
    private String name_en;
    private UUID itemId;

}
