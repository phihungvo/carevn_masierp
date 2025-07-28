package com.masi.production.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO implements Serializable {

    private UUID id;
    private String code;
    private String name;
    private UUID uomId;
    private Integer percentProtein;

}
