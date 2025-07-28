package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.HolidayConfig;
import com.masi.utility.domain.enumeration.CalenderType;
import com.masi.utility.domain.enumeration.HolidayType;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link HolidayConfig}, with proper type conversions.
 */
@Service
public class HolidayConfigRowMapper implements BiFunction<Row, String, HolidayConfig> {

    private final ColumnConverter converter;

    public HolidayConfigRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link HolidayConfig} stored in the database.
     */
    @Override
    public HolidayConfig apply(Row row, String prefix) {
        HolidayConfig entity = new HolidayConfig();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", HolidayType.class));
        entity.setCalenderType(converter.fromRow(row, prefix + "_calender_type", CalenderType.class));
        entity.setDate(converter.fromRow(row, prefix + "_date", LocalDate.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        return entity;
    }
}
