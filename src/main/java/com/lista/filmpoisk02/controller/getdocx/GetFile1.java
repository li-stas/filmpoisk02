package com.lista.filmpoisk02.controller.getdocx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * https://o7planning.org/ru/11765/spring-boot-file-download-example
 * возвращающий объект  ResponseEntity, данный объект обертывает (wrap) объект InputStreamResource
 * (Это данные файла, который скачал пользователь).
 */
@Component
public class GetFile1 {
    private static final Logger log = LoggerFactory.getLogger(GetFile1.class);
    private static final String DIRECTORY = ".";
    private static final String DEFAULT_FILE_NAME = "FilmPoisk.docx";

    private ServletContext servletContext;
   @Autowired
    public GetFile1(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ResponseEntity<InputStreamResource> downloadFile1(String fileName) throws IOException {

        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        log.info("fileName: " + fileName);
        log.info("mediaType: " + mediaType);

        File file = new File(DIRECTORY + "/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);
    }
}
