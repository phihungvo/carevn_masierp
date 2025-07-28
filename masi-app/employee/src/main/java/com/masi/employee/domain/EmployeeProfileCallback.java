package com.masi.employee.domain;

import com.carevn.masi.utils.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EmployeeProfileCallback implements AfterSaveCallback<EmployeeProfile>, AfterConvertCallback<EmployeeProfile>, BeforeSaveCallback<EmployeeProfile> {

    @Override
    public Publisher<EmployeeProfile> onAfterConvert(EmployeeProfile entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<EmployeeProfile> onAfterSave(EmployeeProfile entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public @NotNull Publisher<EmployeeProfile> onBeforeSave(EmployeeProfile entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("company", Parameter.from(login.getCompanyId()));
            }
            return Mono.just(entity);
        }).thenReturn(entity);
    }
}
