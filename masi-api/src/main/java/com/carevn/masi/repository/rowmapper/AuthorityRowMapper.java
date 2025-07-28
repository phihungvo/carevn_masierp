package com.carevn.masi.repository.rowmapper;

import com.carevn.masi.domain.Authority;
import io.r2dbc.spi.Row;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class AuthorityRowMapper implements BiFunction<Row, String, Authority> {
    private final ColumnConverter converter;

    public AuthorityRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    @Override
    public Authority apply(Row row, String prefix) {
        Authority entity = new Authority();
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setAction(converter.fromRow(row, prefix + "_action", String.class));
        entity.setResource(converter.fromRow(row, prefix + "_resource", String.class));
        return entity;
    }
}
