package com.masi.production.service.dto;

import com.carevn.masi.utils.PgJsonObjectDeserializer;
import com.carevn.masi.utils.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.masi.production.domain.enumeration.StatusEntity;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class AdditivesDTO {
    private UUID id; // Mã định danh duy nhất cho phụ gia (nếu cần)
    private LocalDate inspectionDate; // Ngày kiểm tra
    private ZonedDateTime inspectionTime; // Thời gian kiểm tra
    private UUID inspectorId; // Người thực hiện
    private StatusEntity status; // Trạng thái
    @JsonSerialize(using = PgJsonObjectSerializer.class)
    @JsonDeserialize(using = PgJsonObjectDeserializer.class)
    private Json attributes;
    private String batchNumber; // Số phiếu cân
    private String sodiumMaterial;
    private String sodiumMaterialUom;

    private String sodiumCarbonateBatch; // Số lô Natri cacbonat
    private String sodiumCarbonateWeight; // Khối lượng Natri cacbonat
    private String sodiumCarbonateUom;


    private String sodiumBicarbonateWeight; // Khối lượng Natri bicarbonat
    private String sodiumBicarbonateBatch; // Số lô Natri bicarbonat
    private String sodiumBicarbonateBatchUom;

    private String bhtWeight; // Khối lượng BHT
    private String bhtBatch; // Số lô BHT
    private String bhtUom; // Số lô BHT

    private String notes; // Ghi chú
}

