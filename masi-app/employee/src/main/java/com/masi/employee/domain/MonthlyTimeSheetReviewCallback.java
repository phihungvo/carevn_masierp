package com.masi.employee.domain;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MonthlyTimeSheetReviewCallback
    implements AfterSaveCallback<MonthlyTimeSheetReview>, AfterConvertCallback<MonthlyTimeSheetReview> {

    @Override
    public Publisher<MonthlyTimeSheetReview> onAfterConvert(MonthlyTimeSheetReview entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<MonthlyTimeSheetReview> onAfterSave(MonthlyTimeSheetReview entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }
}
