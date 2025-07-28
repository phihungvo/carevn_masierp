package com.masi.employee.service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerDTO {
    private UUID id;
    private String company_name;
    private String customer_code;
    private String email;
}
