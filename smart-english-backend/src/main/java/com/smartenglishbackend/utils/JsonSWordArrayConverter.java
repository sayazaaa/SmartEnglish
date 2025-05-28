package com.smartenglishbackend.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartenglishbackend.jpaentity.SWord;
import jakarta.persistence.AttributeConverter;

import java.util.List;

public class JsonSWordArrayConverter implements AttributeConverter<List<SWord>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(List<SWord> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }
    @Override
    public List<SWord> convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, new TypeReference<List<SWord>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON解析失败", e);
        }
    }
}
