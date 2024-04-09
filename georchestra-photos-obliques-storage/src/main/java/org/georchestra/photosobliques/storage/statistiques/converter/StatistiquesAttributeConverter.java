package org.georchestra.photosobliques.storage.statistiques.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.georchestra.photosobliques.core.bean.statistiques.StatistiquesData;

@Converter
@Slf4j
public class StatistiquesAttributeConverter implements AttributeConverter<StatistiquesData, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(StatistiquesData address) {
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert Address into JSON");
            return null;
        }
    }

    @Override
    public StatistiquesData convertToEntityAttribute(String value) {
        try {
            return objectMapper.readValue(value, StatistiquesData.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into Address");
            return null;
        }
    }
}