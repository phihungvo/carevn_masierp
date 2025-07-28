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
public class SupplierGroupCallback implements AfterSaveCallback<SupplierGroup>, AfterConvertCallback<SupplierGroup>, BeforeSaveCallback<SupplierGroup> {

    @Override
    public Publisher<SupplierGroup> onAfterConvert(SupplierGroup entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SupplierGroup> onAfterSave(SupplierGroup entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SupplierGroup> onBeforeSave(SupplierGroup entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var id = UUID.randomUUID();
                entity.setId(id);
                entity.setCreateAt(ZonedDateTime.now());
                entity.setCreateBy(login.getUserId().toString());
                entity.setCompany(login.getCompanyId());
                row.put("create_at", Parameter.from(ZonedDateTime.now()));
                row.put("create_by", Parameter.from(login.getUserId().toString()));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("id", Parameter.from(id));

            }
            else {
                if (entity.getDeleteAt() == null) {
                    entity.setUpdateAt(ZonedDateTime.now());
                    entity.setUpdateBy(login.getUserId().toString());
                    row.put("update_at", Parameter.from(ZonedDateTime.now()));
                    row.put("update_by", Parameter.from(login.getUserId().toString()));
                }
            }
            return Mono.just(entity);
        });
    }
}
