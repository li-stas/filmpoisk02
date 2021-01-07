package com.lista.filmpoisk.model.services;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * https://o7planning.org/ru/11765/spring-boot-file-download-example
 * возвращающий объект  ResponseEntity, данный объект обертывает (wrap) объект InputStreamResource
 * (Это данные файла, который скачал пользователь).
 */
@Component
public class GetDocxStreamImp implements GetDocxStream {
    private static final Logger log = LoggerFactory.getLogger(GetDocxStreamImp.class);
    private static final String DIRECTORY = ".";

    private final ServletContext servletContext;
    private final MediaTypeUtils mediaTypeUtils;
    @Autowired
    public GetDocxStreamImp(ServletContext servletContext, MediaTypeUtils mediaTypeUtils) {
        this.servletContext = servletContext;
        this.mediaTypeUtils = mediaTypeUtils;
    }

    public ResponseEntity<InputStreamResource> download(XWPFDocument docx, String fileName) throws IOException {

        MediaType mediaType = mediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        log.info("fileName: " + fileName);
        log.info("mediaType: " + mediaType);

        File file = new File(DIRECTORY + "/" + fileName);

        //Write the Document in file system
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        docx.write(out);
        out.close();
        log.info("create document written successully");
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName().replaceAll("\\s*,\\s*", "_"))
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(out.size()) //
                .body(resource);
    }
}
