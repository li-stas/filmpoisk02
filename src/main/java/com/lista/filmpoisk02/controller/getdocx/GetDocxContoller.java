package com.lista.filmpoisk02.controller.getdocx;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.controller.lib.LookupId;
import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.converters.WordRepl;
import com.lista.filmpoisk02.model.converters.WordWorker;
import com.lista.filmpoisk02.model.services.OmDbApiLookupService;

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


@RestController
@EnableAsync
public class GetDocxContoller {
    private static final Logger log = LoggerFactory.getLogger(GetDocxContoller.class);

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final OmDbApiLookupService omDbApiLookupService;
    private final GetFile1 getFile1;

    @Autowired
    public GetDocxContoller(SpringBootConfiguration config, OmDbApiLookupService omDbApiLookupService,
                            GetFile1 getFile1) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
        this.getFile1 = getFile1;
    }

    @RequestMapping("/getdocx")
    public ResponseEntity<InputStreamResource> Querying(
            @RequestParam(value = "id", required = false, defaultValue = "tt0119654") String cSeekId) {

        ResponseEntity<InputStreamResource> ret = null;

        Page oPage01 = new LookupId().Eval(cSeekId, config, omDbApiLookupService);
        cSeekId = oPage01.getStatus();
        log.info("oPage01:" + oPage01);

        String cFile;
        cFile = new WordWorker().Create(oPage01, "Page01.docx");

        cFile = new WordRepl().Eval(oPage01, "FilmPoisk.docx");
        try {
            ret = getFile1.downloadFile1(cFile);
        } catch (IOException e) {
            log.error(e.getMessage() + "IOException");
            //e.printStackTrace();
        }
        return ret;
    }
}
