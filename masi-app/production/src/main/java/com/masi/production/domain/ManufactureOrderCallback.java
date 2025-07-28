package com.masi.production.domain;

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

@Component
public class ManufactureOrderCallback implements AfterSaveCallback<ManufactureOrder>, AfterConvertCallback<ManufactureOrder>, BeforeSaveCallback<ManufactureOrder> {

    @Override
    public Publisher<ManufactureOrder> onAfterConvert(ManufactureOrder entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ManufactureOrder> onAfterSave(ManufactureOrder entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ManufactureOrder> onBeforeSave(ManufactureOrder entity, OutboundRow row, SqlIdentifier table) {
        // Using flatMap to asynchronously get the current user login and update the row
        // accordingly
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                System.out.println("New entity");
//                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId())); // Add company_id
//                row.put("is_deleted", Parameter.from(false)); // Add is_active
//                row.put("department", Parameter.from(login.getGroupId().toString())); // Add is_active
//                entity.setCreatedBy(String.valueOf(login.getUserId()));
                entity.setCreatedAt(ZonedDateTime.now());
                entity.setCompany(String.valueOf(login.getCompanyId()));
//                entity.setIsDeleted(false);
//                entity.setDepartment(String.valueOf(login.getGroupId()));
            } else {
//                System.out.println("Update entity");
                row.put("last_updated", Parameter.from(ZonedDateTime.now()));
                entity.setLastUpdated(ZonedDateTime.now());
                entity.setCompany(login.getCompanyId());
                row.put("company", Parameter.from(login.getCompanyId()));
            }
            return Mono.just(entity);
        });
    }
}
