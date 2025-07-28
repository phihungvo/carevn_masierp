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
public class ItemAssetTransferCallback implements AfterSaveCallback<ItemAssetTransfer>, AfterConvertCallback<ItemAssetTransfer>, BeforeSaveCallback<ItemAssetTransfer> {

    @Override
    public Publisher<ItemAssetTransfer> onAfterConvert(ItemAssetTransfer entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ItemAssetTransfer> onAfterSave(ItemAssetTransfer entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }


    @Override
    public Publisher<ItemAssetTransfer> onBeforeSave(ItemAssetTransfer entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("created_by", Parameter.from(login.getUserId().toString()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("id", Parameter.from(newId));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("department", Parameter.from(login.getGroupId()));
                row.put("is_deleted", Parameter.from(false));
                entity.setId(newId);
                entity.setCreatedBy(login.getUserId().toString());
                entity.createdAt(ZonedDateTime.now());
                entity.setCompany(login.getCompanyId());
                entity.setDepartment(login.getGroupId());
                entity.setIsDeleted(false);
            }
            else
            {
                if (entity.getDeletedAt() != null || entity.getIsDeleted().equals(false))
                {
                    entity.setUpdatedAt(ZonedDateTime.now());
                    entity.setUpdatedBy(login.getUserId().toString());
                    row.put("updated_by", Parameter.from(login.getUserId().toString()));
                    row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                }
                else
                {
                    entity.deletedAt(ZonedDateTime.now());
                    entity.setDeletedBy(login.getUserId().toString());
                    row.put("deleted_by", Parameter.from(login.getUserId().toString()));
                    row.put("deleted_at", Parameter.from(ZonedDateTime.now()));
                }
            }
            return Mono.just(entity);
        });
    }
}
