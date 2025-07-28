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
public class WarehouseCallback implements AfterConvertCallback<Warehouse>, AfterSaveCallback<Warehouse>, BeforeSaveCallback<Warehouse> {

    @Override
    public Publisher<Warehouse> onAfterConvert(Warehouse entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Warehouse> onAfterSave(Warehouse entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Warehouse> onBeforeSave(Warehouse entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("create_by", Parameter.from(login.getUserId().toString()));
                row.put("create_at", Parameter.from(ZonedDateTime.now()));
                row.put("id", Parameter.from(newId));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("active", Parameter.from(true));
                entity.setCreateAt(ZonedDateTime.now());
                entity.setId(newId);
                entity.setActive(true);
                entity.setCreateBy(login.getUserId().toString());
                entity.setCompany(login.getCompanyId());
            }
            return Mono.just(entity);
        });
    }
}
