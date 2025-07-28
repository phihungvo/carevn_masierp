package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.InterviewSchedule;
import com.masi.employee.domain.enumeration.InterviewMode;
import com.masi.employee.domain.enumeration.InterviewResult;
import com.masi.employee.domain.enumeration.InterviewProcess;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link InterviewSchedule}, with proper type conversions.
 */
@Service
public class InterviewScheduleRowMapper implements BiFunction<Row, String, InterviewSchedule> {

    private final ColumnConverter converter;

    public InterviewScheduleRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link InterviewSchedule} stored in the database.
     */
    @Override
    public InterviewSchedule apply(Row row, String prefix) {
        InterviewSchedule entity = new InterviewSchedule();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCandidateName(converter.fromRow(row, prefix + "_candidate_name", String.class));
        entity.setInterviewDate(converter.fromRow(row, prefix + "_interview_date", ZonedDateTime.class));
        entity.setCvFile(converter.fromRow(row, prefix + "_cv_file", String.class));
        entity.setInterviewerId(converter.fromRow(row, prefix + "_interviewer_id", UUID.class));
        entity.setRate(converter.fromRow(row, prefix + "_rate", String.class));
        entity.setProcess(converter.fromRow(row, prefix + "_process", InterviewProcess.class));
        entity.setInterviewMode(converter.fromRow(row, prefix + "_interview_mode", InterviewMode.class));
        entity.setInterviewResult(converter.fromRow(row, prefix + "_interview_result", InterviewResult.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setRecruitmentRequestId(converter.fromRow(row, prefix + "_recruitment_request_id", UUID.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPhoneNumber(converter.fromRow(row, prefix + "_phone_number", String.class));
        return entity;
    }
}
