package com.masi.sale.domain;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback;
import org.springframework.data.r2dbc.mapping.event.AfterSaveCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.r2dbc.core.Parameter;
import org.springframework.stereotype.Component;

import com.carevn.masi.utils.SecurityUtils;

import reactor.core.publisher.Mono;

@Component
public class QuotationDetailCallback implements AfterSaveCallback<QuotationDetail>,
        AfterConvertCallback<QuotationDetail>, BeforeSaveCallback<QuotationDetail> {

    @Override
    public Publisher<QuotationDetail> onAfterConvert(QuotationDetail entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<QuotationDetail> onAfterSave(QuotationDetail entity, OutboundRow outboundRow,
            SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<QuotationDetail> onBeforeSave(QuotationDetail entity, OutboundRow row, SqlIdentifier table) {
        // TODO Auto-generated method stub
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("id", Parameter.from(newId));
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_date", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("is_deleted", Parameter.from(false));
                entity.setId(newId);
                entity.setCreatedDate(ZonedDateTime.now());
                entity.setCreatedBy(login.getUserId().toString());
                entity.setCompany(String.valueOf(login.getCompanyId()));
                entity.setIsDeleted(false);
            }
            return Mono.just(entity);
        });
    }
}
