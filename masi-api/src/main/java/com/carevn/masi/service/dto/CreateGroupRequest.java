package com.carevn.masi.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class CreateGroupRequest {
    @JsonIgnore
    private UUID id = UUID.randomUUID();
    private String name = "";

    private String description;

    private Collection<String> authorities;
}
