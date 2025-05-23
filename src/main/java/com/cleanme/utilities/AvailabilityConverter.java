package com.cleanme.utilities;

import com.cleanme.dto.auth.CleanerSetupRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;
import java.util.Map;

@Converter
public class AvailabilityConverter implements AttributeConverter<List<Map<String, CleanerSetupRequest.TimeRange>>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Map<String, CleanerSetupRequest.TimeRange>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert availability to JSON string", e);
        }
    }

    @Override
    public List<Map<String, CleanerSetupRequest.TimeRange>> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON string to availability list", e);
        }
    }
}
