package com.masi.sale.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PurchaseDeliveryCallback implements AfterSaveCallback<PurchaseDelivery>, AfterConvertCallback<PurchaseDelivery> {

    @Override
    public Publisher<PurchaseDelivery> onAfterConvert(PurchaseDelivery entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PurchaseDelivery> onAfterSave(PurchaseDelivery entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
