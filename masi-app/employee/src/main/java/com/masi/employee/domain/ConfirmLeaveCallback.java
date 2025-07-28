package com.masi.employee.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ConfirmLeaveCallback implements AfterSaveCallback<ConfirmLeave>, AfterConvertCallback<ConfirmLeave> {

    @Override
    public Publisher<ConfirmLeave> onAfterConvert(ConfirmLeave entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<ConfirmLeave> onAfterSave(ConfirmLeave entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
