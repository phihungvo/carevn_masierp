package com.masi.employee.domain;

import com.carevn.masi.utils.SecurityUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Component
public class CallCenterCallback implements AfterSaveCallback<CallCenter>, AfterConvertCallback<CallCenter> , BeforeSaveCallback<CallCenter> {

    @Override
    public Publisher<CallCenter> onAfterConvert(CallCenter entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<CallCenter> onAfterSave(CallCenter entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
    @Override
    public Publisher<CallCenter> onBeforeSave(CallCenter entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId())); // Add company_id
                row.put("is_deleted", Parameter.from(false)); // Add is_active
                row.put("department", Parameter.from(login.getGroupId().toString())); // Add is_active
                row.put("employee_assign_id", Parameter.from(login.getUserId())); // Add employee_assign_id
                row.put("employee_assign_date", Parameter.from(ZonedDateTime.now())); // Add employee_assign_date
                entity.setCreatedBy(String.valueOf(login.getUserId()));
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setCompany(String.valueOf(login.getCompanyId()));
                entity.setIsDeleted(false);
                entity.setDepartment(String.valueOf(login.getGroupId()));
                entity.setEmployeeAssignId((login.getUserId()));
                entity.setEmployeeAssignDate(ZonedDateTime.now());
            } else {
                row.put("updated_by", Parameter.from(login.getUserId()));
                row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                entity.setUpdatedBy(String.valueOf(login.getUserId()));
                entity.setUpdatedAt(ZonedDateTime.now());
                entity.setIsPersisted();
            }
            return Mono.just(entity);
        });
    }
}
