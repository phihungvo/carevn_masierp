package com.masi.utility.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class StandardWorkScheduleConfigCallback
    implements AfterSaveCallback<StandardWorkScheduleConfig>, AfterConvertCallback<StandardWorkScheduleConfig> {

    @Override
    public Publisher<StandardWorkScheduleConfig> onAfterConvert(StandardWorkScheduleConfig entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<StandardWorkScheduleConfig> onAfterSave(
        StandardWorkScheduleConfig entity,
        OutboundRow outboundRow,
        SqlIdentifier table
    ) {
        return Mono.just(entity.setIsPersisted());
    }
}
