package com.lista.filmpoisk02;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FilmPoiskIdContoller implements Queryinterface {
    private static final Logger log = LoggerFactory.getLogger(FilmPoiskIdContoller.class);
    private static final String template = "FilmPoiskId, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final  SpringBootConfiguration config; // для введения ссылки напрямую в ваш класс:

    @Autowired
    public FilmPoiskIdContoller(SpringBootConfiguration config) {
        this.config = config;
    }

    @RequestMapping("/filmpoisk-id")
    public Querying Querying(@RequestParam(value = "id", required = false, defaultValue = "tt3340364") String cSeekId) {
        //http://localhost:8080/filmpoisk?id=tt3340364
        log.info("--> " + "/filmpoisk-id");
        log.info("config.getApikey()=" + config.getApikey());
         

        String cUrlMain = "http://www.omdbapi.com/?apikey=";        
        String cUrl01 = cUrlMain  + config.getApikey() + "&i=" + cSeekId;
        log.info("cUrl01=" + cUrl01);

        log.info("// чтение из сайта cUrl01");
        RestTemplate restTemplate = new RestTemplate();
        String cPage = restTemplate.getForObject(cUrl01, String.class);

        if (cPage.contains("Error\":")) {
            cSeekId = "Ошибвка поиска ID=" + cSeekId;
            log.info(cSeekId + " cPage:" + cPage);
        } else {
            log.info("cPage:" + cPage);
            log.info("// запись в экзепляр класса");

            Page oPage01 = new Json2oPage().eval(cPage);

            log.info("oPage01:" + oPage01);

            new WordWorker().Create(oPage01, "Page01.docx");

            new WordRepl().Eval(oPage01, "FilmPoisk.docx");

        }

        return new Querying(counter.incrementAndGet(), String.format(template, cSeekId));
    }



}
