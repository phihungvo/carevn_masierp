package com.masi.employee.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class EmbedFile {
    @NotNull
    private UUID id;
    @NotNull
    private String fileName;
}
