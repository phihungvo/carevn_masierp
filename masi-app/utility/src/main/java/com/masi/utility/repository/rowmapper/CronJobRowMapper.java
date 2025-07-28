package com.masi.utility.repository.rowmapper;

import com.masi.utility.domain.CronJob;
import com.masi.utility.domain.enumeration.CronJobStatus;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CronJob}, with proper type conversions.
 */
@Service
public class CronJobRowMapper implements BiFunction<Row, String, CronJob> {

    private final ColumnConverter converter;

    public CronJobRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CronJob} stored in the database.
     */
    @Override
    public CronJob apply(Row row, String prefix) {
        CronJob entity = new CronJob();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAction(converter.fromRow(row, prefix + "_action", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setEveryMinute(converter.fromRow(row, prefix + "_every_minute", Integer.class));
        entity.setAtMinute(converter.fromRow(row, prefix + "_at_minute", Integer.class));
        entity.setAtHour(converter.fromRow(row, prefix + "_at_hour", Integer.class));
        entity.setAtDayOfMonth(converter.fromRow(row, prefix + "_at_day_of_month", Integer.class));
        entity.setAtMonth(converter.fromRow(row, prefix + "_at_month", Integer.class));
        entity.setAtDayOfWeek(converter.fromRow(row, prefix + "_at_day_of_week", Integer.class));
        entity.setEnabled(converter.fromRow(row, prefix + "_enabled", Boolean.class));
        entity.setLastRun(converter.fromRow(row, prefix + "_last_run", ZonedDateTime.class));
        entity.setNextRun(converter.fromRow(row, prefix + "_next_run", ZonedDateTime.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", CronJobStatus.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setUrlDb(converter.fromRow(row, prefix + "_url_db", String.class));
        entity.setUserDb(converter.fromRow(row, prefix + "_user_db", String.class));
        entity.setPwDb(converter.fromRow(row, prefix + "_pw_db", String.class));
        entity.setZoneOffset(converter.fromRow(row, prefix + "_zone_offset", Float.class));
        return entity;
    }
}
