package com.masi.production.service.exportDto;

import com.carevn.masi.utils.CSV.CSVColumn;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masi.production.service.dto.ManuFactureDTO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class ManuStandardExportDTO {
    private static final Logger log = LoggerFactory.getLogger(ManuStandardExportDTO.class);
    @CSVColumn(name = "Mã")
    private String code;

    @CSVColumn(name = "Đầu cá (Kg)")
    private BigDecimal fishHeadWeight;

    @CSVColumn(name = "Cá tươi (Kg)")
    private BigDecimal freshFishWeight;

    @CSVColumn(name = "Tổng (Kg)")
    private BigDecimal totalWeight;

    @CSVColumn(name = "Bột (Kg)")
    private BigDecimal flourWeight;

    @CSVColumn(name = "Đạm (%)")
    private BigDecimal proteinPercentage;

    @CSVColumn(name = "Định mức")
    private BigDecimal standard;

    @CSVColumn(name = "Kiểm định")
    private String inspection;

    @CSVColumn(name = "Trạng thái")
    private String status;

    public ManuStandardExportDTO(ManuFactureDTO manufactureOrderDTO) throws IOException {
        this.code = manufactureOrderDTO != null && manufactureOrderDTO.getCode() != null ? manufactureOrderDTO.getCode() : "";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode attributes = objectMapper.readTree(manufactureOrderDTO.getAttributes().asArray());

        this.fishHeadWeight = attributes.has("rawMaterial") && attributes.get("rawMaterial").has("fishHead")
                ? new BigDecimal(attributes.get("rawMaterial").get("fishHead").asText()).setScale(3, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        this.freshFishWeight = attributes.has("rawMaterial") && attributes.get("rawMaterial").has("freshFish")
                ? new BigDecimal(attributes.get("rawMaterial").get("freshFish").asText()).setScale(3, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        this.totalWeight = fishHeadWeight.add(freshFishWeight).setScale(3, RoundingMode.FLOOR);

        this.flourWeight = manufactureOrderDTO.getProductPackageDTO() != null && manufactureOrderDTO.getProductPackageDTO().getWeight() != null
                ? BigDecimal.valueOf(manufactureOrderDTO.getProductPackageDTO().getWeight()).setScale(3, RoundingMode.FLOOR)
                : BigDecimal.ZERO;

        this.proteinPercentage = manufactureOrderDTO.getQualityCheckSampleDTO() != null && manufactureOrderDTO.getQualityCheckSampleDTO().getProteinPercentageApply() != null
                ? BigDecimal.valueOf(manufactureOrderDTO.getQualityCheckSampleDTO().getProteinPercentageApply()).setScale(3, RoundingMode.FLOOR)
                : BigDecimal.ZERO;


        if (this.flourWeight.compareTo(BigDecimal.ZERO) == 0) {
            this.standard = this.fishHeadWeight.add(this.freshFishWeight).setScale(3, RoundingMode.FLOOR);
        } else {
            BigDecimal result = this.fishHeadWeight.add(this.freshFishWeight)
                    .divide(this.flourWeight, 10, RoundingMode.CEILING);

            this.standard = result.setScale(3, RoundingMode.FLOOR);
        }


        this.inspection = manufactureOrderDTO.getQualityCheckSampleDTO() != null && manufactureOrderDTO.getQualityCheckSampleDTO().getProteinPercentageApply() != null
                ? "Đã có kết quả"
                : "Chờ kết quả";

        this.status = manufactureOrderDTO.getStatus() != null
                ? (manufactureOrderDTO.getStatus().getVietnameseName() != null
                ? manufactureOrderDTO.getStatus().getVietnameseName()
                : "")
                : "";
    }
}