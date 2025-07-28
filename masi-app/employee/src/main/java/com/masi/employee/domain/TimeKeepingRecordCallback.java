package com.masi.employee.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TimeKeepingRecordCallback implements AfterSaveCallback<TimeKeepingRecord>, AfterConvertCallback<TimeKeepingRecord> {

    @Override
    public Publisher<TimeKeepingRecord> onAfterConvert(TimeKeepingRecord entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TimeKeepingRecord> onAfterSave(TimeKeepingRecord entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
