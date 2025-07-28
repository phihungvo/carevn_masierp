package com.masi.logistics.domain;

import com.carevn.masi.utils.SecurityUtils;
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
public class VatRateCallback implements AfterSaveCallback<VatRate>, AfterConvertCallback<VatRate>, BeforeSaveCallback<VatRate> {

    @Override
    public Publisher<VatRate> onAfterConvert(VatRate entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<VatRate> onAfterSave(VatRate entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
    @Override
    public Publisher<VatRate> onBeforeSave(VatRate entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("created_by", Parameter.from(login.getUserId().toString()));
                row.put("id", Parameter.from(newId));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("department", Parameter.from(login.getGroupId()));
                entity.setId(newId);
                entity.setCreatedBy(login.getUserId().toString());
                entity.setCompany(login.getCompanyId());
                entity.setDepartment(login.getGroupId());
            }
            return Mono.just(entity);
        });
    }
}
