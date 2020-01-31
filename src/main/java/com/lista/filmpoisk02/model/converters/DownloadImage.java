package com.lista.filmpoisk02.model.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadImage {
    private static final Logger log = LoggerFactory.getLogger(DownloadImage.class);

    public synchronized void  saveTofile(String cUrl, String cImageFile)  {
        if (!(Files.exists(Paths.get(cImageFile)))) {
            InputStream in = getStreamImg(cUrl);
            try {
                Files.deleteIfExists(Paths.get(cImageFile));
                Files.copy(in, Paths.get(cImageFile));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
    /**
     * получени стрима для картики
     * @param cUrl
     * @return
     */
    public InputStream getStreamImg(String cUrl) {
        InputStream in = null;
        try {
            in = new URL(cUrl).openStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return in;
    }
}
