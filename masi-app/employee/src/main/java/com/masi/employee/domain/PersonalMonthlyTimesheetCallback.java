package com.masi.employee.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PersonalMonthlyTimesheetCallback
    implements AfterSaveCallback<PersonalMonthlyTimesheet>, AfterConvertCallback<PersonalMonthlyTimesheet> {

    @Override
    public Publisher<PersonalMonthlyTimesheet> onAfterConvert(PersonalMonthlyTimesheet entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<PersonalMonthlyTimesheet> onAfterSave(PersonalMonthlyTimesheet entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
