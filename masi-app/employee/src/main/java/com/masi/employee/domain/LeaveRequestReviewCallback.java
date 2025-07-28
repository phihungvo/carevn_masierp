package com.masi.employee.domain;

import java.time.ZonedDateTime;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.conversion.MutableAggregateChange;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;

import com.carevn.masi.utils.SecurityUtils;

import reactor.core.publisher.Mono;

@Component
public class LeaveRequestReviewCallback implements AfterSaveCallback<LeaveRequestReview>, AfterConvertCallback<LeaveRequestReview>, BeforeSaveCallback<LeaveRequestReview> {

    @Override
    public Publisher<LeaveRequestReview> onAfterConvert(LeaveRequestReview entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<LeaveRequestReview> onAfterSave(LeaveRequestReview entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<LeaveRequestReview> onBeforeSave(LeaveRequestReview entity, OutboundRow row, SqlIdentifier table) {
        // TODO Auto-generated method stub
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                System.out.println("New entity");
                row.put("created_at", Parameter.from(ZonedDateTime.now()));
                row.put("is_active", Parameter.from(true)); // Add is_active
            }
            return Mono.just(entity);
        });
    }
}
