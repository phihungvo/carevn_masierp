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
import com.masi.sale.domain.enumeration.QuotationStatus;

import reactor.core.publisher.Mono;

@Component
public class QuotationCallback
        implements AfterSaveCallback<Quotation>, AfterConvertCallback<Quotation>, BeforeSaveCallback<Quotation> {

    @Override
    public Publisher<Quotation> onAfterConvert(Quotation entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Quotation> onAfterSave(Quotation entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Quotation> onBeforeSave(Quotation entity, OutboundRow row, SqlIdentifier table) {
        // TODO Auto-generated method stub
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("id", Parameter.from(newId));
                row.put("created_by", Parameter.from(login.getUserId()));
                row.put("created_date", Parameter.from(ZonedDateTime.now()));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("is_deleted", Parameter.from(false));
                row.put("department", Parameter.from(login.getGroupId().toString()));
                row.put("status", Parameter.from(QuotationStatus.NEW));
                entity.setId(newId);
                entity.setCreatedDate(ZonedDateTime.now());
                entity.setCreatedBy(login.getUserId().toString());
                entity.setCompany(String.valueOf(login.getCompanyId()));
                entity.setDepartment(login.getGroupId().toString());
                entity.setStatus(QuotationStatus.NEW);
                entity.setIsDeleted(false);
            }
            return Mono.just(entity);
        });
    }
}
