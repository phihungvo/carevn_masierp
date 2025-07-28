package com.carevn.masi.dto;

import com.nimbusds.jose.shaded.gson.JsonObject;
import io.r2dbc.postgresql.codec.Json;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class EmbedFile {
    private UUID id;
    private String fileName;

    public Json toJson() {
        String json = String.format("{\"id\": \"%s\", \"fileName\": \"%s\"}", id, fileName);
        return Json.of(json);
    }

    public static Json fromList(Collection<EmbedFile> files) {
        if(files==null || files.isEmpty()){
            return Json.of("[]");
        }
        StringBuilder json = new StringBuilder("[");
        for (EmbedFile file : files) {
            json.append(file.toJson().asString()).append(",");
        }
        json.deleteCharAt(json.length() - 1);
        json.append("]");
        return Json.of(json.toString());
    }
}


