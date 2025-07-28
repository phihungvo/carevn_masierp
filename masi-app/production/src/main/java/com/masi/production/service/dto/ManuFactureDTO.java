package com.masi.production.service.dto;

import com.carevn.masi.utils.JsonMapperService;
import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.production.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ManuFactureDTO {

    // Case 1: Order
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderDTO order;
    private UUID orderId;


    // Case 2: Standard
    private UUID productionStandardId;
    private ProductionStandardDTO productionStandards;

    // Item Used in Production
    private UUID itemId;
    private UUID orderItemId;

    // Shared
    private UUID id;
    private String code;
    private String name;
    private StatusEntity status;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Float productionQuantity;
    private UUID createdBy;
    private ZonedDateTime createdAt;
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attributes;
    private String typePage;
    private ZonedDateTime lastUpdated;



    // Save json
    private List<MaterialManuFactureDTO> materialManuFacture;
    private AdditivesDTO additives;
    private ProductionManufactureDTO productionManufacture;
    private ProductPackageDTO productPackageDTO;
    private QualityCheckSampleDTO qualityCheckSampleDTO;
    private ProductMaintainDTO productMaintainDTO;
    private ProductRoutingDTO productRoutingDTO;


    public Json mergeJson() {
        Json mergedJson = null;
        try {

            Json materialJson = materialManuFacture != null
                    ? JsonMapperService.convertListToJson(materialManuFacture, MaterialManuFactureDTO.class)
                    : null;

            Json additivesJson = additives != null
                    ? JsonMapperService.convertObjectToJson(additives, AdditivesDTO.class)
                    : null;

            Json productionJson = productionManufacture != null
                    ? JsonMapperService.convertObjectToJson(productionManufacture, ProductionManufactureDTO.class)
                    : null;

            Json packagingJson = productPackageDTO != null
                    ? JsonMapperService.convertObjectToJson(productPackageDTO, ProductPackageDTO.class)
                    : null;

            Json qualityJson = qualityCheckSampleDTO != null
                    ? JsonMapperService.convertObjectToJson(qualityCheckSampleDTO, QualityCheckSampleDTO.class)
                    : null;

            Json batchJson = productMaintainDTO != null
                    ? JsonMapperService.convertObjectToJson(productMaintainDTO, ProductMaintainDTO.class)
                    : null;

            Json inventoryJson = productRoutingDTO != null
                    ? JsonMapperService.convertObjectToJson(productRoutingDTO, ProductRoutingDTO.class)
                    : null;

            // Merge từng JSON (theo thứ tự yêu cầu)
            if (materialJson != null) mergedJson = materialJson;
            if (additivesJson != null) mergedJson = JsonMapperService.mergeJson(mergedJson, additivesJson);
            if (productionJson != null) mergedJson = JsonMapperService.mergeJson(mergedJson, productionJson);
            if (packagingJson != null) mergedJson = JsonMapperService.mergeJson(mergedJson, packagingJson);
            if (qualityJson != null) mergedJson = JsonMapperService.mergeJson(mergedJson, qualityJson);
            if (batchJson != null) mergedJson = JsonMapperService.mergeJson(mergedJson, batchJson);
            if (inventoryJson != null) mergedJson = JsonMapperService.mergeJson(mergedJson, inventoryJson);
            if(attributes != null) mergedJson = JsonMapperService.mergeJson(mergedJson, attributes);

        } catch (Exception ignore) {

        }

        return mergedJson; // Trả về JSON đã merge
    }


    public UUID getId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        return id;
    }

    public void setId(UUID id) {
        if (id == null) {
            id = UUID.randomUUID();
        }
        this.id = id;
    }
}
