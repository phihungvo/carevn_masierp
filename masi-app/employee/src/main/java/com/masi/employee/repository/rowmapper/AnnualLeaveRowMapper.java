package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.AnnualLeave;
import com.masi.employee.domain.enumeration.WorkPlace;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AnnualLeave}, with proper type conversions.
 */
@Service
public class AnnualLeaveRowMapper implements BiFunction<Row, String, AnnualLeave> {

    private final ColumnConverter converter;

    public AnnualLeaveRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AnnualLeave} stored in the database.
     */
    @Override
    public AnnualLeave apply(Row row, String prefix) {
        AnnualLeave entity = new AnnualLeave();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setLeaveAfterProbation(converter.fromRow(row, prefix + "_leave_after_probation", Integer.class));
        entity.setLeavePerYear(converter.fromRow(row, prefix + "_leave_per_year", Integer.class));
        entity.setCarryForwardMonth(converter.fromRow(row, prefix + "_carry_forward_month", Integer.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setWorkPlace(converter.fromRow(row, prefix + "_work_place", WorkPlace.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        return entity;
    }
}
