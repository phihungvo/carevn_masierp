package com.masi.employee.domain;

import com.carevn.masi.utils.SecurityUtils;
import org.hibernate.validator.constraints.UUID;
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
public class UniformReturnCallback implements AfterSaveCallback<UniformReturn>, AfterConvertCallback<UniformReturn>, BeforeSaveCallback<UniformReturn> {

    @Override
    public Publisher<UniformReturn> onAfterConvert(UniformReturn entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<UniformReturn> onAfterSave(UniformReturn entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<UniformReturn> onBeforeSave(UniformReturn entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("create_by", Parameter.from(login.getUserId().toString()));
                row.put("create_at", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                entity.setCreateAt(ZonedDateTime.now());
                entity.setCreateBy(login.getUserId().toString());
                entity.setCompany(login.getCompanyId());
            }

            return Mono.just(entity);
        });
    }
}
