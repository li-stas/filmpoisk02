package com.lista.filmpoisk02.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.filmpoisk02.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Json2oPage {
    private static final Logger log = LoggerFactory.getLogger(Json2oPage.class);

    public Page eval(String cPage, Page oPage01) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> mapPage = mapper.readValue(cPage, new TypeReference<Map<String, Object>>() {
            });
            Json2PageConverter.extractFromMapPage(mapPage, oPage01);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return oPage01;
    }

}
