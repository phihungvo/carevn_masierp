package com.masi.sale.service.mapper;

import com.masi.sale.domain.Material;
import com.masi.sale.domain.Product;
import com.masi.sale.domain.QuotationDetail;
import com.masi.sale.service.dto.MaterialDTO;
import com.masi.sale.service.dto.ProductDTO;
import com.masi.sale.service.dto.QuotationDetailDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QuotationDetail} and its DTO
 * {@link QuotationDetailDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationDetailMapper extends EntityMapper<QuotationDetailDTO, QuotationDetail> {
    @Mapping(target = "material", source = "material", qualifiedByName = "materialId")
    QuotationDetailDTO toDto(QuotationDetail s);

    @Named("materialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MaterialDTO toDtoMaterialId(Material material);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
