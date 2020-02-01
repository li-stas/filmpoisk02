package com.lista.filmpoisk02.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * считывание конфигурационных данных
 * https://www.theserverside.com/video/How-applicationproperties-simplifies-Spring-config
 *
 */

@ConfigurationProperties(prefix="ini.api")
@Component
public class SpringBootConfiguration {
    private String apikey = "######";

    public String getApikey() { return apikey; }

    public void setApikey(String apikey) { this.apikey = apikey;  }

    @Override
    public String toString() {
        return "ConfigApi{" +
                "apiKey='" + apikey + '\'' +
                '}';
    }
}
/*
@ConfigurationProperties(prefix="spring.boot.config.example")
@Component
public class SpringBootConfiguration {

    private String company;
    private int suite;
    private boolean active;

    public String getCompany() { return company; }
    public void setCompany(String c) { company = c; }
    public int getSuite() { return suite; }
    public void setSuite(int s) { suite = s; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
}*/