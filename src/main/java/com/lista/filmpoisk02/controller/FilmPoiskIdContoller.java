package com.lista.filmpoisk02.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import com.lista.filmpoisk02.model.Json2oPage;
import com.lista.filmpoisk02.model.services.OmDbApiLookupService;
import com.lista.filmpoisk02.model.Querying;

import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.converters.WordRepl;
import com.lista.filmpoisk02.model.converters.WordWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@EnableAsync
public class FilmPoiskIdContoller implements Queryinterface {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskIdContoller.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

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
        String cUrl01;
        //http://localhost:8080/filmpoisk?id=tt3340364
        log.info("--> " + "/filmpoisk-id");
        log.info("  config.getApikey()=" + config.getApikey());

        if (true) {
            cUrl01 = "?apikey=" + config.getApikey() + "&i=" + cSeekId;
            log.info("cUrl01=" + cUrl01);
            log.info("  // чтение из сайта cUrl01");
            Future<Page> result = omDbApiLookupService.findPage(cUrl01);
            try {

                while (!result.isDone()) {
                    Thread.sleep(10); //millisecond pause between each check
                }
                log.info("  // запись в экзепляр класса");
                Page oPage01 = result.get();

                if (oPage01.getCode() == 102) {
                    cSeekId = "  Ошибвка поиска ID=" + cSeekId;
                    log.info(cSeekId + "  cPage:" + oPage01.getStatus());
                } else {
                    log.info("oPage01:" + oPage01);

                    new WordWorker().Create(oPage01, "Page01.docx");

                    new WordRepl().Eval(oPage01, "FilmPoisk.docx");
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage() + " InterruptedException");
                //e.printStackTrace();
            } catch (ExecutionException e) {
                log.error(e.getMessage() + "ExecutionException");
                //e.printStackTrace();
            }
        } else {

            String cUrlMain;
            cUrlMain = "http://www.omdbapi.com/?apikey=";
            cUrl01 = cUrlMain  + config.getApikey() + "&i=" + cSeekId;
            log.info("cUrl01=" + cUrl01);
            log.info("  // чтение из сайта cUrl01");
            RestTemplate restTemplate = new RestTemplate();
            String cPage = restTemplate.getForObject(cUrl01, String.class);

            Page oPage01;
            if (cPage.contains("Error\":")) {
                oPage01 = new Page(cPage, AUTH_FAILURE);
                cSeekId = "  Ошибвка поиска ID=" + cSeekId;
                log.info(cSeekId + "  cPage:" + oPage01.getStatus());
            } else {
                log.info("cPage:" + cPage);
                log.info("// запись в экзепляр класса");

                oPage01 = new Page(SUCCESS_STATUS, CODE_SUCCESS);
                oPage01 = new Json2oPage().eval(cPage, oPage01);

                log.info("oPage01:" + oPage01);

                new WordWorker().Create(oPage01, "Page01.docx");

                new WordRepl().Eval(oPage01, "FilmPoisk.docx");

            }

        }



        return new Querying(counter.incrementAndGet(), String.format(template, cSeekId));
    }



}
