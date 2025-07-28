package com.masi.production.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SteamingProcessChecklistCallback
    implements AfterSaveCallback<SteamingProcessChecklist>, AfterConvertCallback<SteamingProcessChecklist> {

    @Override
    public Publisher<SteamingProcessChecklist> onAfterConvert(SteamingProcessChecklist entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<SteamingProcessChecklist> onAfterSave(SteamingProcessChecklist entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
