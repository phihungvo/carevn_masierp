package com.masi.logistics.domain;

import com.carevn.masi.utils.SecurityUtils;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class InventoriesStorageCallback implements AfterSaveCallback<InventoriesStorage>, AfterConvertCallback<InventoriesStorage>, BeforeSaveCallback<InventoriesStorage> {

    private static final Logger log = LoggerFactory.getLogger(InventoriesStorageCallback.class);

    @Override
    public Publisher<InventoriesStorage> onAfterConvert(InventoriesStorage entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<InventoriesStorage> onAfterSave(InventoriesStorage entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<InventoriesStorage> onBeforeSave(InventoriesStorage entity, OutboundRow row, SqlIdentifier table) {
        log.debug("onBeforeSave");
//        if (1 == 1)
//            return Mono.just(entity);

        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId())); // Add company_id
                row.put("is_deleted", Parameter.from(false)); // Add is_active
//                row.put("department", Parameter.from(login.getGroupId().toString())); // Add is_active
                entity.setCreatedBy(String.valueOf(login.getUserId()));
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setCompany(String.valueOf(login.getCompanyId()));
                entity.setIsDeleted(false);
//                entity.setDepartment(String.valueOf(login.getGroupId()));
            } else {
                row.put("updated_by", Parameter.from(login.getUserId()));
                row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                entity.setUpdatedBy(String.valueOf(login.getUserId()));
                entity.setUpdatedAt(ZonedDateTime.now());
                entity.setIsPersisted();
            }
            return Mono.just(entity);
        });
    }
}
