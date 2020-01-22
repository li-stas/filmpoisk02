package com.lista.filmpoisk02;

import com.lista.filmpoisk02.model.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Future;
@EnableAsync
public interface SiteLookupService {
    @Async //будет запущен в отдельном потоке
    public Future<Page> findPage(String cSeekKey);
}
