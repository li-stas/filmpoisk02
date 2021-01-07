package com.lista.filmpoisk.model.services;

import java.io.InputStream;

public interface DownloadImage {
    void  saveTofile(String cUrl, String cImageFile);
    InputStream getStreamImg(String cUrl);
}
