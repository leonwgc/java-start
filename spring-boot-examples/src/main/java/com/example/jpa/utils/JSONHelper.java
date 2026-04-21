package com.example.jpa.utils;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JSONHelper {
    @Autowired
    private ObjectMapper objectMapper;

    public String toJson(Object obj, Consumer<Exception> exceptionHandler) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            exceptionHandler.accept(e);
            return null;
        }
    }

    public <T> T fromJson(String json, Class<T> clazz, Consumer<Exception> exceptionHandler) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            exceptionHandler.accept(e);
            return null;
        }
    }
}
