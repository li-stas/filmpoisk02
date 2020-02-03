package com.lista.filmpoisk02.model.services;

import com.lista.filmpoisk02.model.Page;

import java.util.concurrent.Future;

public interface SiteLookupService {

    public Future<Page> findPage(String cUrlTypeSeek, String apikey, String cSeekKey);
}
