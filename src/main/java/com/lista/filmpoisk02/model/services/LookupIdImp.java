package com.lista.filmpoisk02.model.services;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * //http://localhost:8080/filmpoisk?id=tt3340364
 * получение данных с сайта по ключю поиска
 * входные данные: ключевое слово поиска
 * возварат: объекта Страница с данными
 */
@Service
public class LookupIdImp implements LookupId{
    private static final Logger log = LoggerFactory.getLogger(LookupIdImp.class);
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    private static final int PAUSE_CHCHECK = 10;

    public Page eval(String cSeekId, SpringBootConfiguration config,
                     SiteLookupService omDbApiLookupService) {

        Page oPage01 = getNewPage();
        String cUrlTypeSeek = "i"; // ID - фильма
        Future<Page> result = omDbApiLookupService.findPage(cUrlTypeSeek, config.getApikey(), cSeekId);

        try {
            int timeOut = waitIsDone(result, config);
            if (timeOut > 0) {
                log.info("  // запись в экзепляр класса время чтения, с "
                        + ((config.getTimeOutForSingleRequests() * 1000) - timeOut)/1000 );
                oPage01 = result.get();
            } else {
                log.info("Время ожидания превышено timeOutForSingleRequests, с" + config.getTimeOutForSingleRequests() );
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
        int timeOut = config.getTimeOutForSingleRequests() * 1000; // значение в секундах
        while (!result.isDone()) {
            Thread.sleep(PAUSE_CHCHECK); //millisecond pause between each check
            timeOut -= PAUSE_CHCHECK;
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

}
