package com.masi.logistics.service.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class DepartmentDTO {
    private UUID id;
    private String code;
    private String name;
    private String company;

}
