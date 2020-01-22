package com.lista.filmpoisk02.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.lista.filmpoisk02.controller.lib.LookupId;
import com.lista.filmpoisk02.model.services.OmDbApiLookupService;
import com.lista.filmpoisk02.model.Querying;

import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.config.SpringBootConfiguration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAsync
public class FilmPoiskIdContoller implements Queryinterface {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskIdContoller.class);

    private static final String template = "FilmPoiskId, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final OmDbApiLookupService omDbApiLookupService;

    @Autowired
    public FilmPoiskIdContoller(SpringBootConfiguration config, OmDbApiLookupService omDbApiLookupService) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
    }

    @RequestMapping("/filmpoisk-id")
    public Querying Querying(@RequestParam(value = "id", required = false, defaultValue = "tt0119654") String cSeekId) {

        Page oPage01 = new LookupId().Eval(cSeekId, config, omDbApiLookupService);
        cSeekId = oPage01.getStatus();
        log.info("oPage01:" + oPage01);

        return new Querying(counter.incrementAndGet(), String.format(template, cSeekId));
    }


}
