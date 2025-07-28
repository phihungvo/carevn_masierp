package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.CallCenter;
import com.masi.employee.domain.enumeration.GroupCs;
import com.masi.employee.domain.enumeration.StatusEntity;
import com.masi.employee.domain.enumeration.TypeCS;
import com.masi.employee.domain.enumeration.TypePageCS;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CallCenter}, with proper type conversions.
 */
@Service
public class CallCenterRowMapper implements BiFunction<Row, String, CallCenter> {

    private final ColumnConverter converter;

    public CallCenterRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CallCenter} stored in the database.
     */
    @Override
    public CallCenter apply(Row row, String prefix) {
        CallCenter entity = new CallCenter();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setReceptionDate(converter.fromRow(row, prefix + "_reception_date", ZonedDateTime.class));
        entity.setGroupCS(converter.fromRow(row, prefix + "_group_cs", GroupCs.class));
        entity.setPhoneOfCaller(converter.fromRow(row, prefix + "_phone_of_caller", String.class));
        entity.setPhoneOfName(converter.fromRow(row, prefix + "_phone_of_name", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", StatusEntity.class));
        entity.setTypeCS(converter.fromRow(row, prefix + "_type_cs", TypeCS.class));
        entity.setCustomerId(converter.fromRow(row, prefix + "_customer_id", UUID.class));
        entity.setEmployeeCreatedId(converter.fromRow(row, prefix + "_employee_created_id", UUID.class));

        entity.setAttribute(converter.fromRow(row, prefix + "_attribute", Json.class));
        entity.setAttachment(converter.fromRow(row, prefix + "_attachment", Json.class));

        entity.setProblemContent(converter.fromRow(row, prefix + "_problem_content", String.class));
        entity.setResolutionContent(converter.fromRow(row, prefix + "_resolution_content", String.class));
        entity.setResponseContent(converter.fromRow(row, prefix + "_response_content", String.class));
        entity.setEmployeeAssignId(converter.fromRow(row, prefix + "_employee_assign_id", UUID.class));
        entity.setEmployeeCloseId(converter.fromRow(row, prefix + "_employee_close_id", UUID.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setUpdatedBy(converter.fromRow(row, prefix + "_updated_by", String.class));
        entity.setDeletedAt(converter.fromRow(row, prefix + "_deleted_at", ZonedDateTime.class));
        entity.setDeletedBy(converter.fromRow(row, prefix + "_deleted_by", String.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));
        entity.setTypePageCs(converter.fromRow(row, prefix + "_type_page_cs", TypePageCS.class));
        entity.setEmployeeAssignDate(converter.fromRow(row, prefix + "_employee_assign_date", ZonedDateTime.class));
        entity.setEmployeeCloseDate(converter.fromRow(row, prefix + "_employee_close_date", ZonedDateTime.class));
        entity.setConfirmDate(converter.fromRow(row, prefix + "_confirm_date", ZonedDateTime.class));
        entity.setSourceCs(converter.fromRow(row, prefix + "_source_cs", String.class));




        return entity;
    }
}
