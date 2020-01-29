package com.lista.filmpoisk02.controller.getdocx;


import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.controller.lib.LookupId;
import com.lista.filmpoisk02.model.Page;
import com.lista.filmpoisk02.model.converters.WordRepl;
import com.lista.filmpoisk02.model.services.SiteLookupService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Контроллер  поиска по  IMDBID с
 * сохранение информации в формате MS Word (* .docx) с помощью Apache POI. Шаблон файла разработан заранее
 */

@RestController
@EnableAsync
public class GetDocxStreamContoller {
    private static final Logger log = LoggerFactory.getLogger(GetDocxContoller.class);

    private final SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:
    private final SiteLookupService omDbApiLookupService;
    private final GetStream1 getStream1;

    @Autowired
    public GetDocxStreamContoller(SpringBootConfiguration config, SiteLookupService omDbApiLookupService,
                                  GetStream1 getStream1) {
        this.config = config;
        this.omDbApiLookupService = omDbApiLookupService;
        this.getStream1 = getStream1;
    }

    @RequestMapping("/getdocx02")
    public ResponseEntity<InputStreamResource> querying(
            @RequestParam(value = "id", required = false, defaultValue = "tt0119654") String cSeekId) {

        ResponseEntity<InputStreamResource> ret = null;
        XWPFDocument docx;
        String cFile;

        Page oPage = new LookupId().eval(cSeekId, config, omDbApiLookupService);

        log.info("oPage:" + oPage);


        // замены в шаблоне c сохранением в файл
        docx = new WordRepl().getXWPFDocument(oPage, "FilmPoisk.docx");
        cFile = new StringBuilder().append(oPage.getTitle()).append("(").append(oPage.getImdbID())
                .append(")").append(".docx").toString();
        try {
            ret = getStream1.download(docx, cFile);
        } catch (IOException e) {
            log.error(e.getMessage() + "IOException", e);
        }
        return ret;
    }
}
