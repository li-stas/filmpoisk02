package com.lista.filmpoisk02;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController implements Queryinterface {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Querying Querying(@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Querying(counter.incrementAndGet(),     String.format(template, name));
    }
}

