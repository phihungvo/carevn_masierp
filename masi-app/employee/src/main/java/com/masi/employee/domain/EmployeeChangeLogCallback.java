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

@Component
public class EmployeeChangeLogCallback implements AfterSaveCallback<EmployeeChangeLog>, AfterConvertCallback<EmployeeChangeLog>, BeforeSaveCallback<EmployeeChangeLog> {

    @Override
    public Publisher<EmployeeChangeLog> onAfterConvert(EmployeeChangeLog entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<EmployeeChangeLog> onAfterSave(EmployeeChangeLog entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<EmployeeChangeLog> onBeforeSave(EmployeeChangeLog entity, OutboundRow row, SqlIdentifier table) {
        if (entity.isNew()) {
            return SecurityUtils.getUserJWTDetail().map(user -> {
                row.put("change_by", Parameter.from(user.getUserId()));
                return entity;
            }).switchIfEmpty(Mono.just(entity));
        }
        return Mono.just(entity);
    }
}
