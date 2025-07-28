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
public class RequestApprovalDetailCallback
    implements AfterSaveCallback<RequestApprovalDetail>, AfterConvertCallback<RequestApprovalDetail>, BeforeSaveCallback<RequestApprovalDetail> {

    @Override
    public Publisher<RequestApprovalDetail> onAfterConvert(RequestApprovalDetail entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<RequestApprovalDetail> onAfterSave(RequestApprovalDetail entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<RequestApprovalDetail> onBeforeSave(RequestApprovalDetail entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_date", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("department", Parameter.from(login.getGroupUId()));
            } else {
                row.put("updated_by", Parameter.from(login.getUserId()));
                row.put("updated_at", Parameter.from(ZonedDateTime.now()));
            }
            return Mono.just(entity);
        });
    }
}
