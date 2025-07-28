package com.carevn.masi.service.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
public class WorkspaceDTO  implements Serializable {
    private UUID id;
    private String name;
    private String normalizedName;

}
