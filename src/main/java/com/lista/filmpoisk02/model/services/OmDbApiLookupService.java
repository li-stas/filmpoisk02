package com.lista.filmpoisk02.model.services;


import com.lista.filmpoisk02.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
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

    // вариант инициализаци без конструктора private RestTemplate restTemplate = new RestTemplate();
    private final RestTemplate restTemplate;
    private final DownloadImage downloadImage;
    private final ConversionService conversionService;

    public OmDbApiLookupService(RestTemplateBuilder restTemplateBuilder, ConversionService conversionService,
                                DownloadImage downloadImage) {
        this.restTemplate = restTemplateBuilder.build();
        this.conversionService = conversionService;
        this.downloadImage = downloadImage;
    }

    /**
     * cUrlKey = "" - построение запроса будет выполнено с использованием
     * UriComponentsBuilder из параметров String cApiKey, String cSeekId
     *
     * @param cApiKey -
     * @param cSeekId -
     * @return
     */
    @Async //будет запущен в отдельном потоке
    public Future<Page> findPage(String cUrlTypeSeek, String cApiKey, String cSeekId) {
        String url = null;
        String cPage = "Error\":";

        // начальный ЮРЛ mainUrl - подстановка чз Map<String, String>
        url = "http://{mainUrl}/";
        // URI (URL) parameters
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("mainUrl", "www.omdbapi.com");
        // Query parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                // Add query parameter
                .queryParam("apikey", cApiKey)
                .queryParam(cUrlTypeSeek, cSeekId);

        url = builder.buildAndExpand(urlParams).toUri().toString();
        log.info("  Looking up URL" + builder.buildAndExpand(urlParams).toUri());

        cPage = restTemplate.getForObject(url, String.class);
        Page oPage = getPage(cPage);

        return new AsyncResult<Page>(oPage);  //AsyncResult требование любого асинхронного сервиса
    }

    private Page getPage(String cPage) {
        Page oPage = new Page(ERROR_STATUS + " " + cPage, AUTH_FAILURE);
        if (!(cPage.contains("Error\":"))) {
            log.info("Call conversionService");
            oPage = conversionService.convert(cPage, Page.class);
            if (oPage != null) {
                oPage.setStatus(SUCCESS_STATUS);
                oPage.setCode(CODE_SUCCESS);
                // получение стрима картики
                if (oPage.getPosterImg() != null) {
                    InputStream streamImg = downloadImage.getStreamImg(oPage.getPoster());
                    downloadImage.saveTofile(oPage.getPoster(), oPage.getPosterImg());
                    oPage.setStreamImg(streamImg);
                }
            }
        }
        return oPage;
    }

}
