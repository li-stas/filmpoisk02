package com.lista.filmpoisk02.model.services;


import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.converters.Json2Page;
import com.lista.filmpoisk02.model.converters.Json2oPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 2. сервис запросов к GitHub для поиска страниц.
 */
@Service //делая его кандидатом для сканирования компонентов в Spring для обнаружения и помещения его в
public class OmDbApiLookupService implements SiteLookupService { //implements SiteLookupService
    private static final Logger log = LoggerFactory.getLogger(OmDbApiLookupService.class);

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

    //@Autowired
    private final ConversionService conversionService;

    private final RestTemplate restTemplate; // private RestTemplate restTemplate = new RestTemplate();

    public OmDbApiLookupService(RestTemplateBuilder restTemplateBuilder, ConversionService conversionService) {
        this.restTemplate = restTemplateBuilder.build();
        this.conversionService = conversionService;
    }

    /**
     *  cUrlKey = "" - построение запроса будет выполнено с использованием
     *  UriComponentsBuilder из параметров String cApiKey, String cSeekId
     * @param cUrlKey
     * @param cApiKey
     * @param cSeekId
     * @return
     */
    @Async //будет запущен в отдельном потоке
    public Future<Page> findPage(String cUrlKey, String cApiKey, String cSeekId)  {

        cUrlKey = ""; // режим builder.buildAndExpand
        String url = null;
        String cPage = "Error\":";

        if (cUrlKey.isEmpty()) {
            // начальный ЮРЛ mainUrl - подстановка чз Map<String, String>
            url = "http://{mainUrl}/";

            // URI (URL) parameters
            Map<String, String> urlParams = new HashMap<String, String>();
            urlParams.put("mainUrl", "www.omdbapi.com");

            // Query parameters
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    // Add query parameter
                    .queryParam("apikey", cApiKey)
                    .queryParam("i", cSeekId);

            log.info("  Looking up URL" + builder.buildAndExpand(urlParams).toUri());

            url = builder.buildAndExpand(urlParams).toUri().toString();

        } else {
            log.info("  Looking up " + cUrlKey);//System.out.println("Looking up " + user);
            url = String.format("http://www.omdbapi.com/%s", cUrlKey);
        }

        cPage = restTemplate.getForObject(url, String.class);

        Page oPage01 = getPage(cPage);
        //Thread.sleep(1000L);
        return new AsyncResult<Page>(oPage01);  //AsyncResult требование любого асинхронного сервиса
    }

    private Page getPage(String cPage) {
        Page oPage01;
        if (cPage.contains("Error\":")) {
            oPage01 = new Page(ERROR_STATUS + " " + cPage, AUTH_FAILURE);
        } else {
            oPage01 = new Page(SUCCESS_STATUS, CODE_SUCCESS);
            if (false) {
                log.info("Call String -> oPage");
                oPage01 = new Json2oPage().eval(cPage, oPage01);
            } else {
                log.info("Call conversionService");
                oPage01 = new Json2Page().eval(cPage, oPage01, conversionService);
            }

        }
        return oPage01;
    }

}
