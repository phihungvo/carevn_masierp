package com.masi.employee.domain;

import com.carevn.masi.utils.SecurityUtils;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TimeKeepingViolationCallback implements AfterSaveCallback<TimeKeepingViolation>, AfterConvertCallback<TimeKeepingViolation> {

    @Override
    public Publisher<TimeKeepingViolation> onAfterConvert(TimeKeepingViolation entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TimeKeepingViolation> onAfterSave(TimeKeepingViolation entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
