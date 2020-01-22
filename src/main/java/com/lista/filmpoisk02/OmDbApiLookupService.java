package com.lista.filmpoisk02;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * 2. сервис запросов к GitHub для поиска страниц.
 */
@Service //делая его кандидатом для сканирования компонентов в Spring для обнаружения и помещения его в
public class OmDbApiLookupService   { //implements SiteLookupService
    private static final Logger logger = LoggerFactory.getLogger(OmDbApiLookupService.class);

    //private RestTemplate restTemplate = new RestTemplate();
    private RestTemplate restTemplate;

    public OmDbApiLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async //будет запущен в отдельном потоке
    public Future<Page> findPage(String cUrlKey)  {

        //System.out.println("Looking up " + user);
        logger.info("Looking up " + cUrlKey);

        String url = String.format("http://www.omdbapi.com/%s", cUrlKey);
        String cPage = restTemplate.getForObject( url, String.class);

        Page oPage01 = new Json2oPage().eval(cPage);

        //Thread.sleep(1000L);
        return new AsyncResult<Page>(oPage01);  //AsyncResult требование любого асинхронного сервиса
    }
}
