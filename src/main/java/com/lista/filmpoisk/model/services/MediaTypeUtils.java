package com.lista.filmpoisk.model.services;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

@Component
public class MediaTypeUtils {

    // abc.zip
    // abc.pdf,..
    public MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
        // application/pdf
        // application/xml
        // image/gif, ...
        String mineType = servletContext.getMimeType(fileName);
        try {
            return MediaType.parseMediaType(mineType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
