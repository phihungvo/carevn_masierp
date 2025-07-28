package com.masi.logistics.domain;

import com.masi.logistics.domain.enumeration.ContractStatus;
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
public class SupplierContractCallback implements AfterSaveCallback<SupplierContract>, AfterConvertCallback<SupplierContract>, BeforeSaveCallback<SupplierContract> {
    private final AuditingEntityCallbackHelper auditingEntityCallbackHelper;

    @Override
    public Publisher<SupplierContract> onAfterConvert(SupplierContract entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SupplierContract> onAfterSave(SupplierContract entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SupplierContract> onBeforeSave(SupplierContract entity, OutboundRow row, SqlIdentifier table) {
         if (entity.isNew()){
             row.put("is_deleted", Parameter.from(false));
         }
        return auditingEntityCallbackHelper.onBeforeSave(entity, row, table);

    }
}
