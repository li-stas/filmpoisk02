package com.lista.filmpoisk02.model.services;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * //http://localhost:8080/filmpoisk?id=tt3340364
 * получение данных с сайта по ключю поиска
 * входные данные: ключевое слово поиска
 * возварат: объекта Страница с данными
 */

public class LookupId {
    private static final Logger log = LoggerFactory.getLogger(LookupId.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

    public Page eval(String cSeekId, SpringBootConfiguration config,
                     SiteLookupService omDbApiLookupService) {
        String cUrl01;

        Page oPage01 =  new Page(SUCCESS_STATUS, CODE_SUCCESS);
        oPage01.setStatus(ERROR_STATUS);
        oPage01.setCode(AUTH_FAILURE);

        log.info("--> " + "/filmpoisk-id");
        log.info("  config.getApikey()=" + config.getApikey());

        cUrl01 = "?apikey=" + config.getApikey() + "&i=" + cSeekId;
        log.info("cUrl01=" + cUrl01);
        log.info("  // чтение из сайта cUrl01");
        Future<Page> result = omDbApiLookupService.findPage(cUrl01);

        try {

            while (!result.isDone()) {
                Thread.sleep(10); //millisecond pause between each check
            }
            log.info("  // запись в экзепляр класса");
            oPage01 = result.get();

        } catch (InterruptedException e) {
            log.error(e.getMessage() + " InterruptedException", e);
            //e.printStackTrace();
        } catch (ExecutionException e) {
            log.error(e.getMessage() + "ExecutionException", e);
            //e.printStackTrace();
        }

        if (oPage01.getCode() == 102) {
            cSeekId = "  Ошибвка поиска ID=" + cSeekId;
            log.info(cSeekId + "  cPage:" + oPage01.getStatus());
        } else {
            log.info("oPage01:" + oPage01);
        }
        oPage01.setStatus(oPage01.getStatus() +  " " + cSeekId);

        return oPage01;
    }

}
