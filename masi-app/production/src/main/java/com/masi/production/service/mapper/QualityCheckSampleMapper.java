package com.masi.production.service.mapper;

import com.masi.production.domain.QualityCheckSample;
import com.masi.production.domain.SampleDisposal;
import com.masi.production.service.dto.QualityCheckSampleDTO;
import com.masi.production.service.dto.SampleDisposalDTO;
import java.util.Objects;
import java.util.UUID;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link QualityCheckSample} and its DTO {@link QualityCheckSampleDTO}.
 */
@Mapper(componentModel = "spring")
public interface QualityCheckSampleMapper extends EntityMapper<QualityCheckSampleDTO, QualityCheckSample> {
    @Mapping(target = "disposal", source = "disposal", qualifiedByName = "sampleDisposalId")
    QualityCheckSampleDTO toDto(QualityCheckSample s);

    @Named("sampleDisposalId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SampleDisposalDTO toDtoSampleDisposalId(SampleDisposal sampleDisposal);

    default String map(UUID value) {
        return Objects.toString(value, null);
    }
}
