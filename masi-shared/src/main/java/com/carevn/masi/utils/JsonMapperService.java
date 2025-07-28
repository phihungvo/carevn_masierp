package com.carevn.masi.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.r2dbc.postgresql.codec.Json;

import java.io.IOException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;

public class JsonMapperService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static{
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> Json convertObjectToJson(T object) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return Json.of(jsonNode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting object to JSON");
        }
    }
    public static <T> Json convertObjectToJson(T object, Class<T> clazz) {
        try {
            String className = clazz.getSimpleName();
            String jsonString = objectMapper.writeValueAsString(object);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.set(className, jsonNode); // Set "class_name" : {[]}

            return Json.of(objectMapper.writeValueAsString(objectNode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting object to JSON");
        }
    }

    public static <T> Json convertListToJson(List<T> objectList, Class<T> clazz) {
        try {
            String className = clazz.getSimpleName();
            String jsonString = objectMapper.writeValueAsString(objectList);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.set(className, jsonNode); // Set "class_name" : {[]}

            return Json.of(objectMapper.writeValueAsString(objectNode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting list to JSON");
        }
    }

    public static <T> T convertJsonToObject(Json json, TypeReference<T> typeReference) {
        try {
            String jsonString = json.toString();
            return objectMapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting JSON to object");
        }
    }

    public static Json mergeJson(Json json1, Json json2) {
        try {
            // Sử dụng json1.asString() để đảm bảo chuỗi JSON hợp lệ
            if (json1 == null) {
                return json2; // Nếu json1 là null, trả về json2
            }
            if (json2 == null) {
                return json1; // Nếu json2 là null, trả về json1
            }

            JsonNode node1 = objectMapper.readTree(json1.asString());
            JsonNode node2 = objectMapper.readTree(json2.asString());

            // Thực hiện merge hai JsonNode
            JsonNode mergedNode = merge(node1, node2);

            // Trả về Json hợp lệ
            return Json.of(objectMapper.writeValueAsString(mergedNode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error merging JSON objects");
        }
    }

    // Hàm đệ quy merge hai JsonNode
    private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode valueToUpdate = updateNode.get(fieldName);
            JsonNode existingValue = mainNode.get(fieldName);

            if (existingValue != null && existingValue.isObject() && valueToUpdate.isObject()) {
                // Đệ quy nếu cả hai trường đều là object
                merge(existingValue, valueToUpdate);
            } else {
                if (mainNode instanceof ObjectNode) {
                    // Cập nhật giá trị
                    ((ObjectNode) mainNode).set(fieldName, valueToUpdate);
                }
            }
        }

        return mainNode;
    }

    public static <T> List<T> convertJsonToList(Json json, Class<T> clazz) {
        return convertJsonToList(json, clazz, null);
    }
    public static <T> List<T> convertJsonToList(Json json, Class<T> clazz, String key) {
        try {
            var text = json.asString();

            JsonNode jsonNode = objectMapper.readTree(text);
            if(!StringUtils.isBlank(key)){
                jsonNode = jsonNode.get(key);
            }

            return objectMapper.readValue(jsonNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T convertJsonToObject(Json json, Class<T> clazz, String key) {
        try {
            String text = json.asString();
            JsonNode jsonNode = objectMapper.readTree(text);
            if (!StringUtils.isBlank(key)) {
                jsonNode = jsonNode.get(key);
                if (jsonNode == null) {
                    return null; // Trả về null nếu key không tồn tại
                }
            }
            return objectMapper.treeToValue(jsonNode, clazz);
        } catch (Exception e) {
            return null; // Trả về null khi có lỗi
        }
    }

    public static Json deleteObjectJson(Json json, String key) {
        try {
            String text = json.asString();
            JsonNode jsonNode = objectMapper.readTree(text);
            if (!StringUtils.isBlank(key) && jsonNode.has(key)) {
                ((ObjectNode) jsonNode).remove(key);
            }
            String updatedJsonString = objectMapper.writeValueAsString(jsonNode);
            return Json.of(updatedJsonString);
        } catch (Exception e) {
            return null;
        }
    }





}


