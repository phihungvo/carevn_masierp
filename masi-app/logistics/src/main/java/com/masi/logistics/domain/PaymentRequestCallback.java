package com.masi.logistics.domain;

import com.carevn.masi.utils.SecurityUtils;
import com.masi.logistics.domain.enumeration.IncomingInvoiceStatus;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class PaymentRequestCallback implements AfterSaveCallback<PaymentRequest>, AfterConvertCallback<PaymentRequest>, BeforeSaveCallback<PaymentRequest> {

    @Override
    public Publisher<PaymentRequest> onAfterConvert(PaymentRequest entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PaymentRequest> onAfterSave(PaymentRequest entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PaymentRequest> onBeforeSave(PaymentRequest entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("id", Parameter.from(newId));
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_date", Parameter.from(ZonedDateTime.now()));

                entity.setId(newId);
                entity.setCreatedBy(login.getUserId());
                entity.setCreatedDate(ZonedDateTime.now());
            }
            return Mono.just(entity);
        });
    }
}
