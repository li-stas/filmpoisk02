package com.lista.filmpoisk02.controller;


import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.Querying;
import com.lista.filmpoisk02.model.services.SiteLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Контроллер  поиска по одному и нескольким наименованим фильма
 * http://localhost:8080/filmpoisk?name=batmam - одниночный поиск
 * http://localhost:8080/filmpoisk-nm?name=batman;cat;fire;cat;batman - множественный поиск *
 * слова разделяются ";"
 *
 */

@RestController
@EnableAsync
public class FilmPoiskNmContoller  {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskNmContoller.class);

    private static final String template = "FilmPoiskNm, %s";
    private final AtomicLong counter = new AtomicLong();

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final SiteLookupService omDbApiLookupService;
    @Autowired
    public FilmPoiskNmContoller(SpringBootConfiguration config, SiteLookupService omDbApiLookupService) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
    }

    @RequestMapping("/filmpoisk-nm")
    public Querying querying(@RequestParam(value = "name", required = false, defaultValue = "batman") String cName) {

        log.info("config.getApikey()=" + config.getApikey());
        log.info("--> " + "/filmpoisk-nm " + cName);

        // разбор строки пареметра
        String [] aName = cName.split(";");
        int nLen_aUrl = aName.length;

        if (nLen_aUrl > 0) {
            log.info("// Start the clock");
            long start = System.currentTimeMillis();

            log.info("  // постановка всех задач в поток");
            Future<Page>[] thr = getFutures(aName, nLen_aUrl);

            log.info("  // Подождите, пока они все не сделают // Wait until they are all done");
            waitingIsDone(thr, nLen_aUrl);

            log.info("  Elapsed time: " + (System.currentTimeMillis() - start));
            printResult(thr, nLen_aUrl);

        } else {
            cName = "Строка поиска с ошибкой =" + cName;
            log.info(cName);
        }

        return new Querying(counter.incrementAndGet(), String.format(template, cName));
    }

    private void printResult(Future<Page>[] thr, int nLen_aUrl) {
        for (int i = 0; i < nLen_aUrl; i++) {
            try {
                log.info("  --> " + thr[i].get());
            } catch (InterruptedException e) {
                log.error(e.getMessage() + " InterruptedException", e);
            } catch (ExecutionException e) {
                log.error(e.getMessage() + "ExecutionException", e);
            }
        }
    }

    private void waitingIsDone(Future<Page>[] thr, int nLen_aUrl) {
        while (true) {
            int nChk = 0;
            for (int i = 0; i < nLen_aUrl; i++) {
                nChk += thr[i].isDone() ? 1 : 0;
            }
            if (nChk == nLen_aUrl) {
                break;
            } else {
                try {
                    Thread.sleep(10); //millisecond pause between each check
                } catch (InterruptedException e) {
                    log.error(e.getMessage() + " " + "//millisecond pause between each check", e);

                }
            }
        }
    }

    private Future<Page>[] getFutures(String[] aName, int nLen_aUrl) {
        Future<Page>[] thr = new Future[nLen_aUrl];
        for (int i = 0; i < nLen_aUrl; i++) {
            String cUrlParam = "t";
            String cSeekKey = aName[i];
            thr[i] = omDbApiLookupService.findPage(cUrlParam, config.getApikey(), cSeekKey);
        }
        return thr;
    }

}
