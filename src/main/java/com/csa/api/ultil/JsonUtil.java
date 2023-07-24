package com.csa.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static String convertToJson(Object data) {
        String jsonString = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        }
        catch (Exception e) {
            jsonString = null;
        }

        return jsonString;
    }
}
