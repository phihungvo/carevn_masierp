package com.masi.employee.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ObjectComparator {
    @Data
    public static class FieldDiff {
        private String fieldName;
        private String oldValue;
        private String newValue;
    }

    public static List<FieldDiff> compareObjects(Object oldObj, Object newObj) {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        JsonNode oldNode = mapper.convertValue(oldObj, JsonNode.class);
        JsonNode newNode = mapper.convertValue(newObj, JsonNode.class);
        Iterable<String> oldFieldNames = oldNode::fieldNames;
        List<FieldDiff> diffs = new java.util.ArrayList<>();
        for (String fieldName : oldFieldNames) {
            if (Objects.isNull(oldNode.get(fieldName)) && Objects.isNull(newNode.get(fieldName))) {
                continue;
            }
            String oldField = String.valueOf(oldNode.get(fieldName)).replaceAll("\"", "");
            String newField = String.valueOf(newNode.get(fieldName)).replaceAll("\"", "");
            if (!oldField.equals(newField)) {
                FieldDiff diff = new FieldDiff();
                diff.setFieldName(fieldName);
                diff.setOldValue(oldField);
                diff.setNewValue(newField);
                diffs.add(diff);
            }
        }
        return diffs;
    }

    public static Optional<Json> compareObjectsToPostgresJson(Object oldObj, Object newObj) {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        List<FieldDiff> diffs = compareObjects(oldObj, newObj);
        if (CollectionUtils.isEmpty(diffs)) {
            return Optional.empty();
        }
        try {
            return Optional.of(Json.of(writer.writeValueAsString(diffs)));

        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
