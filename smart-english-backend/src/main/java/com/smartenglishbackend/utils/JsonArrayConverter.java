package com.smartenglishbackend.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class JsonArrayConverter implements AttributeConverter<List<Object>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Object> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    @Override
    public List<Object> convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ?
                    new ArrayList<>() :
                    objectMapper.readValue(dbData, new TypeReference<List<Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }
}