package com.lista.filmpoisk02.model.services;

import com.lista.filmpoisk02.config.SpringBootConfiguration;
import com.lista.filmpoisk02.model.Page;

public interface LookupId {
    Page eval(String cSeekId, SpringBootConfiguration config,
              SiteLookupService omDbApiLookupService);
}
