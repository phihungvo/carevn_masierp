package com.masi.employee.domain;

import com.carevn.masi.utils.SecurityUtils;
import com.carevn.masi.utils.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class WorkspaceCallback implements AfterSaveCallback<Workspace>, AfterConvertCallback<Workspace>, BeforeSaveCallback<Workspace> {

    @Override
    public Publisher<Workspace> onAfterConvert(Workspace entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Workspace> onAfterSave(Workspace entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Workspace> onBeforeSave(Workspace entity, OutboundRow row, SqlIdentifier table) {
        if (entity.isNew()) {
            return SecurityUtils.getUserJWTDetail().map(user -> {
                row.put("company", Parameter.from(user.getCompanyId()));
                row.put("normalized_name", Parameter.from(StringUtils.normalizeName(entity.getName())));
                return entity;
            });
        } else {
            return Mono.just(entity);
        }
    }
}
