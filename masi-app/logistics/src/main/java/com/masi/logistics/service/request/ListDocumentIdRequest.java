package com.masi.logistics.service.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ListDocumentIdRequest {
    List<UUID> documentIds;
    private UUID warehouseId;
}
