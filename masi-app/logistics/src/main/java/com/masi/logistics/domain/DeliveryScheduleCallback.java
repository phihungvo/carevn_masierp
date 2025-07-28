package com.masi.logistics.domain;

import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class DeliveryScheduleCallback implements AfterSaveCallback<DeliverySchedule>, AfterConvertCallback<DeliverySchedule>, BeforeSaveCallback<DeliverySchedule> {
    private final AuditingEntityCallbackHelper auditingEntityCallbackHelper;

    @Override
    public Publisher<DeliverySchedule> onAfterConvert(DeliverySchedule entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<DeliverySchedule> onAfterSave(DeliverySchedule entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<DeliverySchedule> onBeforeSave(DeliverySchedule entity, OutboundRow row, SqlIdentifier table) {
        return auditingEntityCallbackHelper.onBeforeSave(entity, row, table);
    }
}
