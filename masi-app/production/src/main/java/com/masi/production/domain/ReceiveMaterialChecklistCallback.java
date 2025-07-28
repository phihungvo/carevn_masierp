package com.masi.production.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReceiveMaterialChecklistCallback
    implements AfterSaveCallback<ReceiveMaterialChecklist>, AfterConvertCallback<ReceiveMaterialChecklist> {

    @Override
    public Publisher<ReceiveMaterialChecklist> onAfterConvert(ReceiveMaterialChecklist entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ReceiveMaterialChecklist> onAfterSave(ReceiveMaterialChecklist entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
