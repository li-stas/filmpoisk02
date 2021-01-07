package com.lista.filmpoisk.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.filmpoisk.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * https://www.baeldung.com/spring-type-conversions
 * Мы еще не закончили. Мы также должны сообщить Spring об этом новом конвертере, добавив
 * StringToEmployeeConverter в FormatterRegistry .
 * Это может быть сделано путем реализации WebMvcConfigurer  и переопределения метода addFormatters() :
 * package com.lista.filmpoisk02.config;
 * WebConfig
 */
@Component
public class Json2PageConverter implements Converter<String, Page> {
    private static final Logger log = LoggerFactory.getLogger(Json2PageConverter.class);
    @Override
    public Page convert(String cPage) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mapPage;
        try {
            mapPage = mapper.readValue(cPage, new TypeReference<Map<String, Object>>() {
            });
            Page oPage01 = new Page();
            oPage01.setImdbID((String) mapPage.get("imdbID"));
            oPage01.setTitle((String) mapPage.get("Title"));
            oPage01.setYear(Integer.parseInt((String) mapPage.get("Year")));
            oPage01.setProduction((String) mapPage.get("Production"));
            oPage01.setPoster((String) mapPage.get("Poster"));
            return oPage01;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
