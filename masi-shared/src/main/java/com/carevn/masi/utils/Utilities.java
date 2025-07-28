package com.carevn.masi.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utilities {
    public static Map<String, Object> generateResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (Objects.nonNull(data)) {
            response.put("data", data);
        }
        return response;
    }

    public static Map<String, Object> generateSuccess() {
        return generateResponseWithStatus(true, "", null);
    }

    public static Map<String, Object> generateError(Throwable error) {
        return generateResponseWithStatus(false, error.getMessage(), error);
    }

    private static Map<String, Object> generateResponseWithStatus(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("success", success);
        if (!success) {
            response.put("error", data);
        }
        if (Objects.nonNull(data)) {
            response.put("data", data);
        }
        return response;
    }

    public static Pageable getDefaultSortIfUnsorted(Pageable pageable, String defaultSortField) {
        Sort defaultSort = Sort.by(Sort.Direction.DESC, defaultSortField);
        if (Objects.isNull(pageable)) {
            pageable = PageRequest.of(0, Integer.MAX_VALUE, defaultSort);
        }
        if (pageable.getSort().equals(Sort.unsorted())) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
        }
        return pageable;
    }

}
