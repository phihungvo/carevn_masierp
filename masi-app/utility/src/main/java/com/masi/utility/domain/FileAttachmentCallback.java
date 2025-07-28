package com.masi.utility.domain;

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
public class FileAttachmentCallback implements AfterSaveCallback<FileAttachment>, AfterConvertCallback<FileAttachment>, BeforeSaveCallback<FileAttachment> {

    @Override
    public Publisher<FileAttachment> onAfterConvert(FileAttachment entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<FileAttachment> onAfterSave(FileAttachment entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<FileAttachment> onBeforeSave(FileAttachment entity, OutboundRow row, SqlIdentifier table) {
        Mono<FileAttachment> promise;
        if (entity.isNew()) {
            promise = SecurityUtils.getUserJWTDetail().map(user -> {
                System.out.println("user: " + user.getCompanyId());
                row.put("company", Parameter.from(user.getCompanyId()));
                entity.setCompany(user.getCompanyId());
                row.put("created_by", Parameter.from(user.getUserId()));
                entity.setCreatedBy(user.getUserId()!=null?user.getUserId().toString():null);
                return entity;
            });
        } else {
            promise = SecurityUtils.getUserJWTDetail().map(user -> {
                row.put("updated_by", Parameter.from(user.getUserId()));
                row.put("updated_at", Parameter.from(ZonedDateTime.now()));
                return entity;
            });
        }
        return promise;
    }
}
