package com.masi.utility.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NotificationRecipientCallback
    implements AfterSaveCallback<NotificationRecipient>, AfterConvertCallback<NotificationRecipient> {

    @Override
    public Publisher<NotificationRecipient> onAfterConvert(NotificationRecipient entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<NotificationRecipient> onAfterSave(NotificationRecipient entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
