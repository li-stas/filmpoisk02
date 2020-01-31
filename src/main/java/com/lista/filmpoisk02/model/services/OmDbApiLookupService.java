package com.lista.filmpoisk02.model.services;


import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.converters.Json2Page;
import com.lista.filmpoisk02.model.converters.Json2oPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * 2. сервис запросов к GitHub для поиска страниц.
 */
@Service //делая его кандидатом для сканирования компонентов в Spring для обнаружения и помещения его в
public class OmDbApiLookupService implements SiteLookupService { //implements SiteLookupService
    private static final Logger logger = LoggerFactory.getLogger(OmDbApiLookupService.class);

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

    @Async //будет запущен в отдельном потоке
    public Future<Page> findPage(String cUrlKey)  {

        logger.info("  Looking up " + cUrlKey);//System.out.println("Looking up " + user);

        String url = String.format("http://www.omdbapi.com/%s", cUrlKey);
        String cPage = restTemplate.getForObject(url, String.class);

        Page oPage01;

        if (cPage.contains("Error\":")) {
            oPage01 = new Page(ERROR_STATUS + " " + cPage, AUTH_FAILURE);
        } else {
            oPage01 = new Page(SUCCESS_STATUS, CODE_SUCCESS);
            if (false) {
                oPage01 = new Json2oPage().eval(cPage, oPage01);
            } else {
                oPage01 = new Json2Page(conversionService).eval(cPage, oPage01);
            }

        }
        //Thread.sleep(1000L);
        return new AsyncResult<Page>(oPage01);  //AsyncResult требование любого асинхронного сервиса
    }
}
