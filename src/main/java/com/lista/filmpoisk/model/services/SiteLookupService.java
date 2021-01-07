package com.lista.filmpoisk.model.services;

import com.lista.filmpoisk.model.Page;

import java.util.concurrent.Future;

public interface SiteLookupService {
    Future<Page> findPage(String cMainUrl, String cUrlTypeSeek, String apikey, String cSeekKey);
}
