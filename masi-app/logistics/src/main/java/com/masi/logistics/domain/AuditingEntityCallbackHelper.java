package com.masi.logistics.domain;

import com.carevn.masi.utils.SecurityUtils;
import jakarta.persistence.Column;
import org.reactivestreams.Publisher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
public class AuditingEntityCallbackHelper {
    public <T, E extends AbstractAuditingEntity<T>> Publisher<E> onBeforeSave(E entity, OutboundRow row, SqlIdentifier table) {
        return onBeforeSave(entity, row, table, true);
    }
    public <T, E extends AbstractAuditingEntity<T>> Publisher<E> onBeforeSave(E entity, OutboundRow row, SqlIdentifier table,boolean withDepartment) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                if(withDepartment){
                    row.put("department", Parameter.from(login.getGroupUId()));
                }
                entity.setCreatedBy(login.getUserId().toString());
                entity.setCreatedAt(ZonedDateTime.now());
            } else {
                row.put("updated_by", Parameter.from(login.getUserId()));
                row.put("updated_at", Parameter.from(ZonedDateTime.now()));
            }
            return Mono.just(entity);
        });
    }
}
