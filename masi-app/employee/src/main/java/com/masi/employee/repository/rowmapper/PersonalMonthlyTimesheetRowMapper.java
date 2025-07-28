package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.PersonalMonthlyTimesheet;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.domain.enumeration.TimesheetReviewStatus;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PersonalMonthlyTimesheet}, with proper type conversions.
 */
@Service
public class PersonalMonthlyTimesheetRowMapper implements BiFunction<Row, String, PersonalMonthlyTimesheet> {

    private final ColumnConverter converter;

    public PersonalMonthlyTimesheetRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PersonalMonthlyTimesheet} stored in the database.
     */
    @Override
    public PersonalMonthlyTimesheet apply(Row row, String prefix) {
        PersonalMonthlyTimesheet entity = new PersonalMonthlyTimesheet();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setMonth(converter.fromRow(row, prefix + "_month", LocalDate.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", TimesheetReviewStatus.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setReviewId(converter.fromRow(row, prefix + "_review_id", UUID.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setTimeKeepingType(converter.fromRow(row, prefix + "_type", TimeKeepingType.class));
        return entity;
    }
}
