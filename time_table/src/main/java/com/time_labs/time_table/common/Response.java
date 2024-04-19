package com.time_labs.time_table.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Response {
    public static Object createResponse(String message, Object data) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();
        response.put("status", message);
        response.put("data", Objects.requireNonNullElse(data, ""));
        return mapper.convertValue(response, Object.class);
    }
}
