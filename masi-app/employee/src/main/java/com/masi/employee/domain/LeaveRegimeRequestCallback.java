package com.masi.employee.domain;

import java.time.ZonedDateTime;

import org.hibernate.validator.constraints.UUID;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;

import com.carevn.masi.utils.SecurityUtils;

import reactor.core.publisher.Mono;

@Component
public class LeaveRegimeRequestCallback
        implements AfterSaveCallback<LeaveRegimeRequest>, AfterConvertCallback<LeaveRegimeRequest>,
        BeforeSaveCallback<LeaveRegimeRequest> {

    @Override
    public Publisher<LeaveRegimeRequest> onAfterConvert(LeaveRegimeRequest entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<LeaveRegimeRequest> onAfterSave(LeaveRegimeRequest entity, OutboundRow outboundRow,
            SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<LeaveRegimeRequest> onBeforeSave(LeaveRegimeRequest entity, OutboundRow row, SqlIdentifier table) {
        // Using flatMap to asynchronously get the current user login and update the row
        // accordingly
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                System.out.println("New entity");
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("company_id", Parameter.from(login.getCompanyId())); // Add company_id
                row.put("is_deleted", Parameter.from(false)); // Add is_active
                row.put("department", Parameter.from(login.getGroupId().toString())); // Add is_active
                entity.setCreatedBy(login.getUserId());
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setCompanyId(String.valueOf(login.getCompanyId()));
                entity.setIsDeleted(false);
                entity.setDepartment(login.getGroupId().toString());
            } else {
                System.out.println("Update entity");
                row.put("updated_by", Parameter.from(login.getUserId()));
                row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                entity.setUpdatedBy(login.getUserId());
                entity.setUpdatedAt(ZonedDateTime.now());
            }
            return Mono.just(entity);
        });
    }

}
