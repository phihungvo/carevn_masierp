package com.carevn.masi.service.mapper;

import com.carevn.masi.domain.Company;
import com.carevn.masi.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {
    CompanyDTO toDto(Company s);
}
