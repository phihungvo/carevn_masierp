package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.RecruitmentRequest;
import com.masi.employee.domain.enumeration.ContractType;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.domain.enumeration.Position;
import com.masi.employee.domain.enumeration.RecruitmentStatus;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link RecruitmentRequest}, with proper type
 * conversions.
 */
@Service
public class RecruitmentRequestRowMapper implements BiFunction<Row, String, RecruitmentRequest> {

    private final ColumnConverter converter;

    public RecruitmentRequestRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link RecruitmentRequest} stored in the database.
     */
    @Override
    public RecruitmentRequest apply(Row row, String prefix) {
        RecruitmentRequest entity = new RecruitmentRequest();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setDepartmentId(converter.fromRow(row, prefix + "_department_id", UUID.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", Position.class));
        entity.setJobTitle(converter.fromRow(row, prefix + "_job_title", String.class));
        entity.setWage(converter.fromRow(row, prefix + "_wage", Float.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", Integer.class));
        entity.setStartDate(converter.fromRow(row, prefix + "_start_date", LocalDate.class));
        entity.setRecruitmentPurposes(converter.fromRow(row, prefix + "_recruitment_purposes", String.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", UUID.class));
        entity.setRequestNotes(converter.fromRow(row, prefix + "_request_notes", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", Gender.class));
        entity.setApprovalSignContentType(converter.fromRow(row, prefix + "_approval_sign_content_type", String.class));
        entity.setApprovalSign(converter.fromRow(row, prefix + "_approval_sign", byte[].class));
        entity.setRejectNote(converter.fromRow(row, prefix + "_reject_note", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", RecruitmentStatus.class));
        entity.setContractType(converter.fromRow(row, prefix + "_contract_type", ContractType.class));
        entity.setLastUpdated(converter.fromRow(row, prefix + "_last_updated", ZonedDateTime.class));
        entity.setCreatedDate(converter.fromRow(row, prefix + "_created_date", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setSalaryUnit(converter.fromRow(row, prefix + "_salary_unit", String.class));
        entity.setReplaceForId(converter.fromRow(row, prefix + "_replace_for_id", UUID.class));
        entity.setDeadline(converter.fromRow(row, prefix + "_deadline", LocalDate.class));
        entity.setNumberAdjourn(converter.fromRow(row, prefix + "_number_adjourn", Integer.class));
        entity.setOldStatus(converter.fromRow(row, prefix + "_old_status", RecruitmentStatus.class));
        return entity;
    }
}
