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
public class ItemCallback implements AfterSaveCallback<Item>, AfterConvertCallback<Item>, BeforeSaveCallback<Item> {

    @Override
    public Publisher<Item> onAfterConvert(Item entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Item> onAfterSave(Item entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Item> onBeforeSave(Item entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                if(entity.getCreatedBy() == null){
                    row.put("created_by", Parameter.from(login.getUserId().toString()));
                    entity.setCreatedBy(login.getUserId().toString());
                }
                row.put("created_date", Parameter.from(ZonedDateTime.now()));
                row.put("id", Parameter.from(newId));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("department", Parameter.from(login.getGroupId()));
                row.put("is_deleted", Parameter.from(false));
                row.put("is_active", Parameter.from(true));
                entity.setIsActive(true);
                entity.setId(newId);
                entity.setCreatedDate(ZonedDateTime.now());
                entity.setCompany(login.getCompanyId());
                entity.setDepartment(login.getGroupId());
                entity.setIsDeleted(false);
            }
            return Mono.just(entity);
        });
    }
}
