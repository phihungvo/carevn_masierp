package com.masi.logistics.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
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


  
    
}
