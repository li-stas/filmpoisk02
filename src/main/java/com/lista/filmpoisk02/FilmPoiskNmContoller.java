package com.lista.filmpoisk02;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FilmPoiskNmContoller implements Queryinterface {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskNmContoller.class);

    @Autowired // для введения ссылки напрямую в ваш класс:
    SpringBootConfiguration config;

    private static final String template = "FilmPoiskNm, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/filmpoisk-nm")
    public Querying Querying(@RequestParam(value = "name", required = false, defaultValue = "batman") String cName) {
        //http://localhost:8080/filmpoisk?name=Stas
        log.info("config.getApikey()=" + config.getApikey());
        log.info("--> " + "/filmpoisk-nm " + cName);
        String [] aName = cName.split(";");

        if (aName.length > 0) {
            List<String> aNameList = Arrays.asList(aName);
            aNameList.forEach(topic -> log.info(topic));

            log.info("Ставим ключи поиска в потоки");

        } else {
            cName = "Строка поиска с ошибкой =" + cName;
            log.info(cName);
        }

        return new Querying(counter.incrementAndGet(), String.format(template, cName));
    }

}
