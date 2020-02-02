package com.lista.filmpoisk02.model.services;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.Debug;
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
    private static final int PAUSEBETWEENEACHCHECK = 10;

    public Page eval(String cSeekId, SpringBootConfiguration config,
                     SiteLookupService omDbApiLookupService) {
        String cUrl01;
        Page oPage01 = getNewPage();

        cUrl01 = get_cUrl01(cSeekId, config);

        Future<Page> result = omDbApiLookupService.findPage(cUrl01, config.getApikey(), cSeekId);

        try {
            int timeOut = waitIsDone(result, config);
            if (timeOut > 0) {
                log.info("  // запись в экзепляр класса время чтения, с "
                        + ((config.getTimeoutforsinglerequests() * 1000) - timeOut)/1000 );
                oPage01 = result.get();
            } else {
                log.info("Время ожидания превышено Timeoutforsinglerequests, с" + config.getTimeoutforsinglerequests() );
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage() + " InterruptedException", e);
        } catch (ExecutionException e) {
            log.error(e.getMessage() + "ExecutionException", e);
        }
        checkStatusPage(cSeekId, oPage01);
        return oPage01;
    }

    private Page getNewPage() {
        Page oPage01 = new Page(SUCCESS_STATUS, CODE_SUCCESS);
        oPage01.setStatus(ERROR_STATUS);
        oPage01.setCode(AUTH_FAILURE);
        return oPage01;
    }

    private int waitIsDone(Future<Page> result, SpringBootConfiguration config) throws InterruptedException {
        int timeOut = config.getTimeoutforsinglerequests() * 1000; // значение в секундах
        while (!result.isDone()) {
            Thread.sleep(PAUSEBETWEENEACHCHECK); //millisecond pause between each check
            timeOut -= PAUSEBETWEENEACHCHECK;
            if (timeOut <= 0) {
                break;
            }
        }
        return timeOut;
    }

    private void checkStatusPage(String cSeekId, Page oPage01) {
        if (oPage01.getCode() == 102) {
            cSeekId = "  Ошибвка поиска ID=" + cSeekId;
            log.info(cSeekId + "  cPage:" + oPage01.getStatus());
        } else {
            log.info("oPage01:" + oPage01);
        }

        oPage01.setStatus(oPage01.getStatus() + " " + cSeekId);
    }

    private String get_cUrl01(String cSeekId, SpringBootConfiguration config) {
        String cUrl01;
        if (Debug.FALSE) {
            log.info("--> " + "/filmpoisk-id");
            log.info("  config.getApikey()=" + config.getApikey());

            cUrl01 = new StringBuilder().append("?apikey=").append(config.getApikey()).
                    append("&i=").append(cSeekId).toString();

            log.info("cUrl01=" + cUrl01);
            log.info("  // чтение из сайта cUrl01");
        } else {
             cUrl01 = "";
        }
        return cUrl01;
    }

}
