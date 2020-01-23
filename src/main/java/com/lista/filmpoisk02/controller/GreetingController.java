package com.lista.filmpoisk02.controller;

import com.lista.filmpoisk02.model.Querying;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Контроллер выдаюций приметвие
 * http://localhost:8080/greeting -> "Hello, World!"
 * http://localhost:8080/greeting?name=Kate -> "Hello, Kate!"
 */

@RestController
public class GreetingController implements Queryinterface {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Querying querying(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Querying(counter.incrementAndGet(),     String.format(template, name));
    }
}

