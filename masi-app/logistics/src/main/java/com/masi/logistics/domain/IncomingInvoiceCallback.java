package com.masi.logistics.domain;

import com.masi.logistics.domain.enumeration.IncomingInvoiceStatus;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class IncomingInvoiceCallback implements AfterSaveCallback<IncomingInvoice>, AfterConvertCallback<IncomingInvoice>, BeforeSaveCallback<IncomingInvoice> {
    private final AuditingEntityCallbackHelper auditingEntityCallbackHelper;

    @Override
    public Publisher<IncomingInvoice> onAfterConvert(IncomingInvoice entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<IncomingInvoice> onAfterSave(IncomingInvoice entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<IncomingInvoice> onBeforeSave(IncomingInvoice entity, OutboundRow row, SqlIdentifier table) {
        if (entity.isNew()){
            if (entity.getNeedApproval() == null) {
                entity.setNeedApproval(true);
                row.put("need_approval", Parameter.from(true));
            }
            if (entity.getStatus() == null) {
                entity.setStatus(IncomingInvoiceStatus.NEW);
                row.put("status", Parameter.from(IncomingInvoiceStatus.NEW));
            }
        }
        return auditingEntityCallbackHelper.onBeforeSave(entity, row, table, false);
    }
}
