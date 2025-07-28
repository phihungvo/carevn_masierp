package com.carevn.masi.sdk.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CallHistoryQuery {
    public static enum Status {
        ANSWERED, NO_ANSWER, MISSED, FAILED, BUSY
    }

    public int limit;
    public int offset;
    private String date_start;
    private String date_end;
    private String type;
    private Status status;
    private String did;

    private String extension;
    private String sort;
    private String callid;

    public JsonNode toJson(ObjectMapper objectMapper) {
        ObjectNode jsonNode = objectMapper.createObjectNode();
        if (date_start != null) {
            jsonNode.put("date_start", date_start);
        }
        if (date_end != null) {
            jsonNode.put("date_end", date_end);
        }
        if (type != null) {
            jsonNode.put("type", type);
        }
        if (status != null) {
            jsonNode.put("status", status.name());
        }
        if (did != null) {
            jsonNode.put("did", did);
        }
        if (extension != null) {
            jsonNode.put("extension", extension);
        }
        if (limit > 0) {
            jsonNode.put("limit", limit);
        }
        if (offset > 0) {
            jsonNode.put("offset", offset);
        }
        if (sort != null) {
            jsonNode.put("sort", sort);
        }
        if (callid != null) {
            jsonNode.put("callid", callid);
        }
        return jsonNode;

    }
}
