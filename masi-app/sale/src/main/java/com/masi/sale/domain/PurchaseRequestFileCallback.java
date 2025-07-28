package com.masi.sale.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PurchaseRequestFileCallback implements AfterSaveCallback<PurchaseRequestFile>, AfterConvertCallback<PurchaseRequestFile> {

    @Override
    public Publisher<PurchaseRequestFile> onAfterConvert(PurchaseRequestFile entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PurchaseRequestFile> onAfterSave(PurchaseRequestFile entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
