package com.masi.employee.repository.rowmapper;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.enumeration.ContractType;
import com.masi.employee.domain.enumeration.EmployeeStatus;
import com.masi.employee.domain.enumeration.Gender;
import com.masi.employee.domain.enumeration.Position;
import io.r2dbc.spi.Row;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmployeeProfile}, with proper type conversions.
 */
@Service
public class EmployeeProfileRowMapper implements BiFunction<Row, String, EmployeeProfile> {

    private final ColumnConverter converter;

    public EmployeeProfileRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     *
     * @return the {@link EmployeeProfile} stored in the database.
     */
    @Override
    public EmployeeProfile apply(Row row, String prefix) {
        EmployeeProfile entity = new EmployeeProfile();
        entity.setId(converter.fromRow(row, prefix + "_id", UUID.class));
        entity.setEmployeeCode(converter.fromRow(row, prefix + "_employee_code", String.class));
        entity.setFullName(converter.fromRow(row, prefix + "_full_name", String.class));
        entity.setGender(converter.fromRow(row, prefix + "_gender", Gender.class));
        entity.setWorkspaceId(converter.fromRow(row, prefix + "_workspace_id", UUID.class));
        entity.setCitizenId(converter.fromRow(row, prefix + "_citizen_id", String.class));
        entity.setCitizenIssueDate(converter.fromRow(row, prefix + "_citizen_issue_date", LocalDate.class));
        entity.setCitizenIssuePlace(converter.fromRow(row, prefix + "_citizen_issue_place", String.class));
        entity.setResidenceAddress(converter.fromRow(row, prefix + "_residence_address", String.class));
        entity.setTemporaryAddress(converter.fromRow(row, prefix + "_temporary_address", String.class));
        entity.setBirthday(converter.fromRow(row, prefix + "_birthday", LocalDate.class));
        entity.setPhone(converter.fromRow(row, prefix + "_phone", String.class));
        entity.setTaxCode(converter.fromRow(row, prefix + "_tax_code", String.class));
        entity.setStartWorkDate(converter.fromRow(row, prefix + "_start_work_date", LocalDate.class));
        entity.setRole(converter.fromRow(row, prefix + "_role", String.class));
        entity.setPosition(converter.fromRow(row, prefix + "_position", Position.class));
        entity.setBankCode(converter.fromRow(row, prefix + "_bank_code", String.class));
        entity.setBankNumber(converter.fromRow(row, prefix + "_bank_number", String.class));
        entity.setContractType(converter.fromRow(row, prefix + "_contract_type", ContractType.class));
        entity.setContractTerm(converter.fromRow(row, prefix + "_contract_term", String.class));
        entity.setContractNumber(converter.fromRow(row, prefix + "_contract_number", String.class));
        entity.setContractDate(converter.fromRow(row, prefix + "_contract_date", LocalDate.class));
        entity.setContractEndDate(converter.fromRow(row, prefix + "_contract_end_date", LocalDate.class));
        entity.setLevel(converter.fromRow(row, prefix + "_level", String.class));
        entity.setParkingCard(converter.fromRow(row, prefix + "_parking_card", String.class));
        entity.setInsuranceCard(converter.fromRow(row, prefix + "_insurance_card", String.class));
        entity.setReferrerId(converter.fromRow(row, prefix + "_referrer_id", UUID.class));
        entity.setReferrerDate(converter.fromRow(row, prefix + "_referrer_date", LocalDate.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setNote(converter.fromRow(row, prefix + "_note", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", EmployeeStatus.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setUpdatedAt(converter.fromRow(row, prefix + "_updated_at", ZonedDateTime.class));
        entity.setIsDeleted(converter.fromRow(row, prefix + "_is_deleted", Boolean.class));
        entity.setIsActive(converter.fromRow(row, prefix + "_is_active", Boolean.class));
        entity.setCompany(converter.fromRow(row, prefix + "_company", String.class));
        entity.setDepartment(converter.fromRow(row, prefix + "_department", String.class));

        entity.setProbationDateFrom(converter.fromRow(row, prefix + "_probation_date_from", LocalDate.class));
        entity.setProbationDateTo(converter.fromRow(row, prefix + "_probation_date_to", LocalDate.class));
        entity.setOfficialWorkType(converter.fromRow(row, prefix + "_official_work_type", String.class));
        entity.setOfficialWorkTypeDuration(converter.fromRow(row, prefix + "_official_work_type_duration", Float.class));
        entity.setInsurancePaymentLevel(converter.fromRow(row, prefix + "_insurance_payment_level", Float.class));
        entity.setPin(converter.fromRow(row, prefix + "_pin", String.class));
        entity.setAccountStatus(converter.fromRow(row, prefix + "_account_status", String.class));
        return entity;
    }
}
