package com.masi.logistics.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class PaymentDetailCallback implements AfterSaveCallback<PaymentDetail>, AfterConvertCallback<PaymentDetail>, BeforeSaveCallback<PaymentDetail> {

    private final AuditingEntityCallbackHelper auditingEntityCallbackHelper;

    public PaymentDetailCallback(AuditingEntityCallbackHelper auditingEntityCallbackHelper) {
        this.auditingEntityCallbackHelper = auditingEntityCallbackHelper;
    }

    @Override
    public Publisher<PaymentDetail> onAfterConvert(PaymentDetail entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PaymentDetail> onAfterSave(PaymentDetail entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PaymentDetail> onBeforeSave(PaymentDetail entity, OutboundRow row, SqlIdentifier table) {
        if (entity.isNew())
        {
            var newId = UUID.randomUUID();
            entity.setId(newId);
            row.put("id", Parameter.from(newId));
        }
        return auditingEntityCallbackHelper.onBeforeSave(entity, row, table, false);
    }
}
