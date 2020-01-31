package com.lista.filmpoisk02.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.filmpoisk02.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * https://www.baeldung.com/spring-type-conversions
 * Мы еще не закончили. Мы также должны сообщить Spring об этом новом конвертере, добавив
 * StringToEmployeeConverter в FormatterRegistry .
 * Это может быть сделано путем реализации WebMvcConfigurer  и переопределения метода addFormatters() :
 * package com.lista.filmpoisk02.config;
 * WebConfig
 */

public class Json2PageConverter implements Converter<String, Page> {
    private static final Logger log = LoggerFactory.getLogger(Json2oPage.class);

    @Override
    public Page convert(String cPage) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapPage;
        try {
            mapPage = mapper.readValue(cPage, new TypeReference<Map<String, Object>>() {
            });
            return new Page((String) mapPage.get("imdbID"), (String) mapPage.get("Title")
                    , Integer.parseInt((String) mapPage.get("Year")), (String) mapPage.get("Production")
                    , (String) mapPage.get("Poster"), "", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
