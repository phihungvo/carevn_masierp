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
public class SupplierTypeCallback implements AfterSaveCallback<SupplierType>, AfterConvertCallback<SupplierType>, BeforeSaveCallback<SupplierType> {

    @Override
    public Publisher<SupplierType> onAfterConvert(SupplierType entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SupplierType> onAfterSave(SupplierType entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SupplierType> onBeforeSave(SupplierType entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("id", Parameter.from(newId));
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("is_deleted", Parameter.from(false));
                row.put("department", Parameter.from(login.getGroupId().toString()));
                entity.setId(newId);
                entity.setCreatedBy(String.valueOf(login.getUserId()));
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setCompany(String.valueOf(login.getCompanyId()));
                entity.setIsDeleted(false);
                entity.setDepartment(String.valueOf(login.getGroupId()));
            } else {
                if (entity.getDeletedAt() == null) 
                {
                    row.put("updated_by", Parameter.from(login.getUserId()));
                    row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                    entity.setUpdatedBy(String.valueOf(login.getUserId()));
                    entity.setUpdatedAt(ZonedDateTime.now());
                }
            }
            return Mono.just(entity);
        });
    }

}
