package com.lista.filmpoisk02.controller;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import com.lista.filmpoisk02.model.OmDbApiLookupService;
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
public class FilmPoiskNmContoller implements Queryinterface {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskNmContoller.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

    private static final String template = "FilmPoiskNm, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final OmDbApiLookupService omDbApiLookupService;

    @Autowired
    public FilmPoiskNmContoller(SpringBootConfiguration config, OmDbApiLookupService omDbApiLookupService) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
    }

    @RequestMapping("/filmpoisk-nm")
    public Querying Querying(@RequestParam(value = "name", required = false, defaultValue = "batman") String cName) {
        //http://localhost:8080/filmpoisk?name=Stas
        log.info("config.getApikey()=" + config.getApikey());
        log.info("--> " + "/filmpoisk-nm " + cName);

        String [] aName = cName.split(";");
        int nLen_aUrl = aName.length;

        if (nLen_aUrl > 0) {
            log.info("// Start the clock");
            long start = System.currentTimeMillis();

            Future<Page>[] thr = new Future[nLen_aUrl];

            log.info("  // постановка всех задач в поток");
            for (int i = 0; i < nLen_aUrl; i++) {

                String cUrl01 = "?apikey=" + config.getApikey() + "&t=" + aName[i];
                log.info(String.format("i=%d,aName[i]=%s,cUrl01=%s",i,aName[i],cUrl01));
                thr[i] = omDbApiLookupService.findPage(cUrl01);
            }

            log.info("  // Подождите, пока они все не сделали // Wait until they are all done");
            while (true) {
                int nChk = 0;
                for (int i = 0; i < nLen_aUrl; i++) {
                    nChk += thr[i].isDone() ? 1 : 0;
                }
                if (nChk == nLen_aUrl) {
                    break;
                } else {
                    try {
                        //log.info("  //10 millisecond pause between each check");
                        Thread.sleep(10); //millisecond pause between each check
                    } catch (InterruptedException e) {
                        log.error(e.getMessage() + " " + "//millisecond pause between each check");
                        //e.printStackTrace();
                    }
                }
            }

            log.info("  // Print results, including elapsed time");
            log.info("  Elapsed time: " + (System.currentTimeMillis() - start));

            for (int i = 0; i < nLen_aUrl; i++) {
                try {

                    log.info("  --> " + thr[i].get());

                } catch (InterruptedException e) {
                    log.error(e.getMessage() + " InterruptedException");
                    //e.printStackTrace();
                } catch (ExecutionException e) {
                    log.error(e.getMessage() + "ExecutionException");
                    //e.printStackTrace();
                }
            }

        } else {
            cName = "Строка поиска с ошибкой =" + cName;
            log.info(cName);
        }

        return new Querying(counter.incrementAndGet(), String.format(template, cName));
    }

}
