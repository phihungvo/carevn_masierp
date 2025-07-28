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
public class RequestApprovalCallback implements AfterSaveCallback<RequestApproval>, AfterConvertCallback<RequestApproval>, BeforeSaveCallback<RequestApproval> {

    public Publisher<RequestApproval> onAfterConvert(RequestApproval entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<RequestApproval> onAfterSave(RequestApproval entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
    @Override
    public Publisher<RequestApproval> onBeforeSave(RequestApproval entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                System.out.println("New entity");
                var id = UUID.randomUUID();
                row.put("id", Parameter.from(id));
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_date", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("is_deleted", Parameter.from(false));
                row.put("department", Parameter.from(login.getGroupId().toString()));
                entity.setCreatedBy(String.valueOf(login.getUserId()));
                entity.setCreatedDate(ZonedDateTime.now());
                entity.setCompany(String.valueOf(login.getCompanyId()));
                entity.setIsDeleted(false);
                entity.setDepartment(String.valueOf(login.getGroupId()));
                entity.setId(id);
            } else {
                if (entity.getIsDeleted().equals(false)){
                    row.put("updated_by", Parameter.from(login.getUserId()));
                    row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                    entity.setUpdatedBy(String.valueOf(login.getUserId()));
                    entity.setUpdatedAt(ZonedDateTime.now());
                }
                else{
                    row.put("deleted_by", Parameter.from(login.getUserId()));
                    row.put("deleted_at", Parameter.from(ZonedDateTime.now()));
                    entity.setDeletedBy(String.valueOf(login.getUserId()));
                    entity.setDeletedAt(ZonedDateTime.now());
                }
            }
            return Mono.just(entity);
        });
    }
}
