package com.masi.logistics.domain;

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
import java.util.UUID;

@Component
public class TransactionInCallback implements AfterSaveCallback<TransactionIn>, AfterConvertCallback<TransactionIn>, BeforeSaveCallback<TransactionIn> {

    @Override
    public Publisher<TransactionIn> onAfterConvert(TransactionIn entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TransactionIn> onAfterSave(TransactionIn entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TransactionIn> onBeforeSave(TransactionIn entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("create_by", Parameter.from(login.getUserId().toString()));
                row.put("create_at", Parameter.from(ZonedDateTime.now()));
                row.put("id", Parameter.from(newId));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("department", Parameter.from(login.getGroupId()));
                entity.setId(newId);
                entity.setCreateBy(login.getUserId().toString());
                entity.setCreateAt(ZonedDateTime.now());
                entity.setCompany(login.getCompanyId());
                entity.setDepartment(login.getGroupId());
            }
            return Mono.just(entity);
        });
    }
}
