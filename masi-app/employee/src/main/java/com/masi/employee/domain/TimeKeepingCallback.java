package com.masi.employee.domain;

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
public class TimeKeepingCallback implements AfterSaveCallback<TimeKeeping>, AfterConvertCallback<TimeKeeping>
        , BeforeSaveCallback<TimeKeeping> {

    @Override
    public Publisher<TimeKeeping> onAfterConvert(TimeKeeping entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TimeKeeping> onAfterSave(TimeKeeping entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TimeKeeping> onBeforeSave(TimeKeeping entity, OutboundRow row, SqlIdentifier table) {

        if (entity.getFirstCheckIn() != null && entity.getLastCheckIn() != null && entity.getFirstCheckIn().equals(entity.getLastCheckIn())) {
            row.remove("last_check_in");

        }
        return Mono.just(entity);
    }
}
