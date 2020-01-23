package com.lista.filmpoisk02.model.lib;

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
            oPage01.setImdbID((String) mapPage.get("imdbID"));
            oPage01.setTitle((String) mapPage.get("Title"));
            oPage01.setYear(Integer.parseInt((String) mapPage.get("Year")));
            oPage01.setProduction((String) mapPage.get("Production"));
            oPage01.setPoster((String) mapPage.get("Poster"));
        } catch (Exception e) {
            log.error(e.getMessage());//e.printStackTrace();
        }
        return oPage01;
    }
}
