package com.masi.sale.domain;

import com.carevn.masi.utils.SecurityUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PurchaseRequestCallback implements AfterSaveCallback<PurchaseRequest>, AfterConvertCallback<PurchaseRequest>, BeforeSaveCallback<PurchaseRequest> {

    @Override
    public Publisher<PurchaseRequest> onAfterConvert(PurchaseRequest entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PurchaseRequest> onAfterSave(PurchaseRequest entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }


    @Override
    public Publisher<PurchaseRequest> onBeforeSave(PurchaseRequest entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().map(user -> {
            if (entity.isNew()) {
                row.put("created_by", Parameter.from(user.getUserId()));
                row.put("company", Parameter.from(user.getCompanyId()));
            } else {
                row.put("updated_by", Parameter.from(user.getUserId()));
            }
            return entity;
        });
    }
}
