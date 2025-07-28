package com.masi.employee.domain;

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
import com.masi.employee.domain.enumeration.UniformOrderStatus;

import reactor.core.publisher.Mono;

@Component
public class UniformStockCallback implements AfterSaveCallback<UniformStock>, AfterConvertCallback<UniformStock>,
        BeforeSaveCallback<UniformStock> {

    @Override
    public Publisher<UniformStock> onAfterConvert(UniformStock entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<UniformStock> onAfterSave(UniformStock entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<UniformStock> onBeforeSave(UniformStock entity, OutboundRow row, SqlIdentifier table) {
        // TODO Auto-generated method stub
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.getId() == null) {
                if (entity.isNew()) {
                    var newId = UUID.randomUUID();
                    row.put("create_by", Parameter.from(login.getUserId().toString()));
                    row.put("create_at", Parameter.from(ZonedDateTime.now()));
                    row.put("id", Parameter.from(newId));
                    row.put("company", Parameter.from(login.getCompanyId()));
                    row.put("department", Parameter.from(login.getGroupId()));
                    entity.setCreateAt(ZonedDateTime.now());
                    entity.setId(newId);
                    entity.setCreateBy(login.getUserId().toString());
                    entity.setCompany(login.getCompanyId());
                    entity.setDepartment(login.getGroupId());
                }
            }

            return Mono.just(entity);
        });
    }
}
