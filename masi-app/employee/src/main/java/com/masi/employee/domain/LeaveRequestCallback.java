package com.masi.employee.domain;

import java.time.ZonedDateTime;

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
public class LeaveRequestCallback implements AfterSaveCallback<LeaveRequest>, AfterConvertCallback<LeaveRequest>, BeforeSaveCallback<LeaveRequest> {

    @Override
    public Publisher<LeaveRequest> onAfterConvert(LeaveRequest entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<LeaveRequest> onAfterSave(LeaveRequest entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<LeaveRequest> onBeforeSave(LeaveRequest entity, OutboundRow row, SqlIdentifier table) {
        // TODO Auto-generated method stub
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                System.out.println("New entity");
                row.put("is_active", Parameter.from(true)); // Add is_active
                row.put("created_at", Parameter.from(ZonedDateTime.now())); // Add created_at
            }
            return Mono.just(entity);
        });
    }
}
