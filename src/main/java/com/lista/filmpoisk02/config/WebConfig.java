package com.lista.filmpoisk02.config;

import com.lista.filmpoisk02.model.converters.Json2PageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Json2PageConverter());
    }

    /**
     * https://www.javainuse.com/spring/spring-boot-content-negotiation
     * 1. это добавлено
     * 2. class FilmPoiskIdContoller @RequestMapping(val ...
     * ответ констуируется чз set-теры
     * 3. @XmlRootElement !!!
     * public class Querying {
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        log.info("config.contains(\"Using Path Extension\")");
        // установить расширение пути в true
        configurer.favorPathExtension(true).
        // установить параметр предпочтения в false
        favorParameter(false).
        // игнорируем заголовки принятия
        ignoreAcceptHeader(true).
        defaultContentType(MediaType.APPLICATION_JSON).
        mediaType("xml", MediaType.APPLICATION_XML).
        mediaType("json", MediaType.APPLICATION_JSON);
    }
}
/*
 String config;
        //config = "Using Path Parameters";
        config = "Using Path Extension";

        if (config.contains("Using Path Parameters")) {
            log.info("config.contains(\"Using Path Parameters\")");
            // установить расширение пути в false
            configurer.favorPathExtension(false).
                    // параметр запроса (по умолчанию "format") должен использоваться для определения запрошенного типа медиа
                            favorParameter(true).
                    // параметр favor установлен на «mediaType» вместо «format» по умолчанию
                            parameterName("mediaType").
                    // игнорируем заголовки принятия
                            ignoreAcceptHeader(true).
                    defaultContentType(MediaType.APPLICATION_JSON).
                    mediaType("xml", MediaType.APPLICATION_XML).
                    mediaType("json", MediaType.APPLICATION_JSON);
        } else if (config.contains("Using Path Extension")) {
            log.info("config.contains(\"Using Path Extension\")");
            // установить расширение пути в true
            configurer.favorPathExtension(true).
                    // установить параметр предпочтения в false
                            favorParameter(false).
                    // игнорируем заголовки принятия
                            ignoreAcceptHeader(true).
                    defaultContentType(MediaType.APPLICATION_JSON).
                    mediaType("xml", MediaType.APPLICATION_XML).
                    mediaType("json", MediaType.APPLICATION_JSON);
        }
 */