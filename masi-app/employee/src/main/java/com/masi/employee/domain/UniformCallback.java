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
public class UniformCallback implements AfterSaveCallback<Uniform>, AfterConvertCallback<Uniform>, BeforeSaveCallback<Uniform> {

    @Override
    public Publisher<Uniform> onAfterConvert(Uniform entity, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Uniform> onAfterSave(Uniform entity, OutboundRow outboundRow, SqlIdentifier table) {
        return Mono.just(entity.setIsPersisted());
    }

    @Override
    public Publisher<Uniform> onBeforeSave(Uniform entity, OutboundRow row, SqlIdentifier table) {
        return SecurityUtils.getUserJWTDetail().flatMap(login -> {
            if (entity.isNew()) {
                var newId = UUID.randomUUID();
                row.put("create_by", Parameter.from(login.getUserId().toString()));
                row.put("create_at", Parameter.from(ZonedDateTime.now()));
                row.put("id", Parameter.from(newId));
                row.put("company", Parameter.from(login.getCompanyId()));
                row.put("status", Parameter.from("ENABLE"));
                entity.setStatus("ENABLE");
                entity.setCreateAt(ZonedDateTime.now());
                entity.setId(newId);
                entity.setCreateBy(login.getUserId().toString());
                entity.setCompany(login.getCompanyId());
            } else {
                System.out.println("Update entity");
                row.put("update_by", Parameter.from(login.getUserId().toString()));
                row.put("update_at", Parameter.from(ZonedDateTime.now()));
                entity.setUpdateBy( login.getUserId().toString());
                entity.setUpdateAt(ZonedDateTime.now());
            }


            return Mono.just(entity);
        });
    }
}
