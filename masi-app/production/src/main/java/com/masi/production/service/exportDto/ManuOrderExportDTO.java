package com.masi.production.service.exportDto;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.production.service.dto.ManuFactureDTO;
import lombok.Data;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
public class ManuOrderExportDTO {
    @CSVColumn(name = "Mã")
    private String code;

    @CSVColumn(name = "Đơn hàng")
    private String order;

    @CSVColumn(name = "Yêu cầu")
    private String requirementWeight;

    @CSVColumn(name = "Nguyên liệu 1 (Kg)")
    private BigDecimal material1Weight;

    @CSVColumn(name = "Nguyên liệu 2 (Kg)")
    private BigDecimal material2Weight;

    @CSVColumn(name = "Bột (Kg)")
    private BigDecimal flourWeight;

    @CSVColumn(name = "Đạm (%)")
    private BigDecimal proteinRate;

    @CSVColumn(name = "Ngày bắt đầu")
    private String startDate;

    @CSVColumn(name = "Ngày kết thúc")
    private String endDate;

    @CSVColumn(name = "Kiểm định")
    private String inspectionStatus;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public ManuOrderExportDTO(ManuFactureDTO manufactureOrderDTO) throws IOException {
        this.code = manufactureOrderDTO != null && manufactureOrderDTO.getCode() != null ? manufactureOrderDTO.getCode() : "";
        this.order = manufactureOrderDTO.getOrder() != null && manufactureOrderDTO.getOrder().getCode() != null ? manufactureOrderDTO.getOrder().getCode() : "";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode attributes = objectMapper.readTree(manufactureOrderDTO.getAttributes().asArray());

        // Nguyên liệu 1
        this.material1Weight = attributes.has("rawMaterial2") &&
                attributes.get("rawMaterial2").has("items") &&
                !attributes.get("rawMaterial2").get("items").isEmpty() &&
                attributes.get("rawMaterial2").get("items").get(0).has("quantityUse")
                ? BigDecimal.valueOf(attributes.get("rawMaterial2").get("items").get(0).get("quantityUse").asDouble())
                : BigDecimal.ZERO;

        // Nguyên liệu 2
        this.material2Weight = attributes.has("rawMaterial2") &&
                attributes.get("rawMaterial2").has("items") &&
                attributes.get("rawMaterial2").get("items").size() > 1 &&
                attributes.get("rawMaterial2").get("items").get(1).has("quantityUse")
                ? BigDecimal.valueOf(attributes.get("rawMaterial2").get("items").get(1).get("quantityUse").asDouble())
                : BigDecimal.ZERO;

        // Yêu cầu
        this.requirementWeight = attributes.has("percentProtein") && manufactureOrderDTO.getProductionQuantity() != null
                ? BigDecimal.valueOf(attributes.get("percentProtein").asDouble()).toString() + " - " + BigDecimal.valueOf(manufactureOrderDTO.getProductionQuantity()).toString()
                : BigDecimal.ZERO.toString();


        // Bột
        this.flourWeight = manufactureOrderDTO.getProductPackageDTO() != null && manufactureOrderDTO.getProductPackageDTO().getWeight() != null
                ? BigDecimal.valueOf(manufactureOrderDTO.getProductPackageDTO().getWeight())
                : BigDecimal.ZERO;

        // Đạm
        this.proteinRate = manufactureOrderDTO.getQualityCheckSampleDTO() != null && manufactureOrderDTO.getQualityCheckSampleDTO().getProteinPercentageApply() != null
                ? BigDecimal.valueOf(manufactureOrderDTO.getQualityCheckSampleDTO().getProteinPercentageApply())
                : BigDecimal.ZERO;

        this.startDate = manufactureOrderDTO.getFromDate() != null ? manufactureOrderDTO.getFromDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";

        this.endDate = manufactureOrderDTO.getToDate() != null ? manufactureOrderDTO.getToDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";
        this.inspectionStatus = manufactureOrderDTO.getQualityCheckSampleDTO() != null && manufactureOrderDTO.getQualityCheckSampleDTO().getProteinPercentageApply() != null
                ? "Đã có kết quả" : "Chờ kết quả";

        this.status = manufactureOrderDTO.getStatus() != null
                ? manufactureOrderDTO.getStatus().getVietnameseName() != null
                ? manufactureOrderDTO.getStatus().getVietnameseName()
                : ""
                : "";
    }
}
