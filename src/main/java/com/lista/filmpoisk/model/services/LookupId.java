package com.lista.filmpoisk.model.services;

import com.lista.avgcursbank.config.SpringBootConfiguration;
import com.lista.filmpoisk.model.Page;

public interface LookupId {
    Page eval(String cSeekId, SpringBootConfiguration config,
              SiteLookupService omDbApiLookupService);
}
