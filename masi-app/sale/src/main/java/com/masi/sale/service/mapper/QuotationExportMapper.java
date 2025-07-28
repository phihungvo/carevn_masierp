package com.masi.sale.service.mapper;

import com.masi.sale.domain.Quotation;
import com.masi.sale.domain.QuotationExport;
import com.masi.sale.service.dto.QuotationDTO;
import com.masi.sale.service.dto.QuotationExportDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuotationExport} and its DTO {@link QuotationExportDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationExportMapper extends EntityMapper<QuotationExportDTO, QuotationExport> {
    @Mapping(target = "quotation", source = "quotation", qualifiedByName = "quotationId")
    QuotationExportDTO toDto(QuotationExport s);

    @Named("quotationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuotationDTO toDtoQuotationId(Quotation quotation);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
