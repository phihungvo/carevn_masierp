package com.masi.production.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AdditiveMaterialChecklistCallback
    implements AfterSaveCallback<AdditiveMaterialChecklist>, AfterConvertCallback<AdditiveMaterialChecklist> {

    @Override
    public Publisher<AdditiveMaterialChecklist> onAfterConvert(AdditiveMaterialChecklist entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<AdditiveMaterialChecklist> onAfterSave(
        AdditiveMaterialChecklist entity,
        OutboundRow outboundRow,
        SqlIdentifier table
    ) {
        return Mono.just(entity.setIsPersisted());
    }
}
