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
public class ProcessLeaveRegimeRequestCallback
        implements AfterSaveCallback<ProcessLeaveRegimeRequest>, AfterConvertCallback<ProcessLeaveRegimeRequest>,
        BeforeSaveCallback<ProcessLeaveRegimeRequest> {

    @Override
    public Publisher<ProcessLeaveRegimeRequest> onAfterConvert(ProcessLeaveRegimeRequest entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ProcessLeaveRegimeRequest> onAfterSave(
            ProcessLeaveRegimeRequest entity,
            OutboundRow outboundRow,
            SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ProcessLeaveRegimeRequest> onBeforeSave(ProcessLeaveRegimeRequest entity, OutboundRow row,
            SqlIdentifier table) {
        // TODO Auto-generated method stub
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                System.out.println("New entity");
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("is_deleted", Parameter.from(false)); // Add is_active
                entity.setCreatedBy(login.getUserId());
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setIsDeleted(false);
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
