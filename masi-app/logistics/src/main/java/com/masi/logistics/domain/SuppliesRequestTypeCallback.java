package com.masi.logistics.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SuppliesRequestTypeCallback implements AfterSaveCallback<SuppliesRequestType>, AfterConvertCallback<SuppliesRequestType> {

    @Override
    public Publisher<SuppliesRequestType> onAfterConvert(SuppliesRequestType entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SuppliesRequestType> onAfterSave(SuppliesRequestType entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
