package com.masi.production.domain;

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

@Component
public class SampleDisposalCallback implements AfterSaveCallback<SampleDisposal>, AfterConvertCallback<SampleDisposal>, BeforeSaveCallback<SampleDisposal> {

    @Override
    public Publisher<SampleDisposal> onAfterConvert(SampleDisposal entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SampleDisposal> onAfterSave(SampleDisposal entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SampleDisposal> onBeforeSave(SampleDisposal entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                entity.setCreatedAt(ZonedDateTime.now());
            } else {
                row.put("last_updated", Parameter.from(ZonedDateTime.now()));
                entity.setLastUpdated(ZonedDateTime.now());
            }
            return Mono.just(entity);
        });
    }
}
