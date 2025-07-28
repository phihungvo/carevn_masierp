package com.masi.utility.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class NotificationAddedEvent {
    private UUID id;
    private String content;
    private String title;
    private String entityName;
    private String entityId;
    private String entityType;
    private ZonedDateTime createdAt;
    private String createdBy;
    private Collection<String> recipients;
    private Map<String,Object> data;
    private String action;
    private String category;
    private String sentBy;

    public Map<String, Object> getData() {
        data.put("action", action==null?"":action);
        data.values().removeIf(Objects::isNull);
        return data;
    }
}
