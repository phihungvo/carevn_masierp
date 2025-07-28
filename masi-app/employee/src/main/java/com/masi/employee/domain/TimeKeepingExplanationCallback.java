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

@Component
public class TimeKeepingExplanationCallback
        implements AfterSaveCallback<TimeKeepingExplanation>, AfterConvertCallback<TimeKeepingExplanation>
        , BeforeSaveCallback<TimeKeepingExplanation> {

    @Override
    public Publisher<TimeKeepingExplanation> onAfterConvert(TimeKeepingExplanation entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TimeKeepingExplanation> onAfterSave(TimeKeepingExplanation entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<TimeKeepingExplanation> onBeforeSave(TimeKeepingExplanation entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("company", Parameter.from(login.getCompanyId())); // Add company_id
                entity.setCompany(String.valueOf(login.getCompanyId()));
//                entity.setDepartment(String.valueOf(login.getGroupId()));
            }
            return Mono.just(entity);
        });
    }
}
