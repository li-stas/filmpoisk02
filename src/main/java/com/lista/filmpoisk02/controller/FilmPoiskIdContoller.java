package com.lista.filmpoisk02.controller;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.Querying;
import com.lista.filmpoisk02.model.services.LookupId;
import com.lista.filmpoisk02.model.services.SiteLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Контроллер  поиска по  IMDBID
 * пример поиска
 * http://localhost:8080/filmpoisk-id?id=tt0119654
 */

@RestController
@EnableAsync
public class FilmPoiskIdContoller  {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskIdContoller.class);

    private static final String template = "FilmPoiskId, %s";
    private final AtomicLong counter = new AtomicLong();

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final SiteLookupService omDbApiLookupService;
    private final LookupId lookupId;
    @Autowired
    public FilmPoiskIdContoller(SpringBootConfiguration config, SiteLookupService omDbApiLookupService, LookupId lookupId) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
        this.lookupId = lookupId;
    }

    @RequestMapping(value = "/filmpoisk-id",   method = RequestMethod.GET,
            produces = { "application/json", "application/xml" })
    public Querying querying(@RequestParam(value = "id", required = false, defaultValue = "tt0119654") String cSeekId) {

        Page oPage01 = lookupId.eval(cSeekId, config, omDbApiLookupService);
        cSeekId = oPage01.getStatus();
        log.info("oPage01:" + oPage01);

        // для варианта формирования чз ХМЛ
        Querying querying = new Querying();
        querying.setId(counter.incrementAndGet());
        querying.setContent(String.format(template, cSeekId));
        return querying;
    }



}
