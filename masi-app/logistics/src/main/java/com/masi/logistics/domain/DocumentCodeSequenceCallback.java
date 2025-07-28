package com.masi.logistics.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DocumentCodeSequenceCallback implements AfterSaveCallback<DocumentCodeSequence>, AfterConvertCallback<DocumentCodeSequence> {

    @Override
    public Publisher<DocumentCodeSequence> onAfterConvert(DocumentCodeSequence entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<DocumentCodeSequence> onAfterSave(DocumentCodeSequence entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
