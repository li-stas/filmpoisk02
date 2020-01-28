package com.lista.filmpoisk02.controller.getdocx;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.controller.lib.LookupId;
import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.converters.WordRepl;
import com.lista.filmpoisk02.model.converters.WordWorker;
import com.lista.filmpoisk02.model.services.SiteLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Контроллер  поиска по  IMDBID с
 * сохранение информации в формате MS Word (* .docx) с помощью Apache POI. Шаблон файла разработан заранее
 */

@RestController
@EnableAsync
public class GetDocxContoller {
    private static final Logger log = LoggerFactory.getLogger(GetDocxContoller.class);

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final SiteLookupService omDbApiLookupService;
    private final GetFile1 getFile1;

    @Autowired
    public GetDocxContoller(SpringBootConfiguration config, SiteLookupService omDbApiLookupService,
                            GetFile1 getFile1) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
        this.getFile1 = getFile1;
    }

    @RequestMapping("/getdocx")
    public ResponseEntity<InputStreamResource> querying(
            @RequestParam(value = "id", required = false, defaultValue = "tt0119654") String cSeekId) {

        ResponseEntity<InputStreamResource> ret = null;

        Page oPage01 = new LookupId().eval(cSeekId, config, omDbApiLookupService);

        log.info("oPage01:" + oPage01);

        String cFile;
        // генрерация нового д-та
        // cFile = new WordWorker().create(oPage01, "Page01.docx");

        // замены в шаблоне
        cFile = new WordRepl().eval(oPage01, "FilmPoisk.docx");

        try {
            ret = getFile1.downloadFile1(cFile);
        } catch (IOException e) {
            log.error(e.getMessage() + "IOException", e);
        }
        log.info("ret:" + ret);
        return ret;
    }
}
