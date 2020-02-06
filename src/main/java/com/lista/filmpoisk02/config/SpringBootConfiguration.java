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
    private int timeOutForSingleRequests = 1;

    public int getTimeOutForSingleRequests() {
        return timeOutForSingleRequests;
    }

    public void setTimeOutForSingleRequests(int timeOutForSingleRequests) {
        this.timeOutForSingleRequests = timeOutForSingleRequests;
    }

    public String getApikey() { return apikey; }
    public void setApikey(String apikey) { this.apikey = apikey;  }

    @Override
    public String toString() {
        return "ConfigApi{" +
                "apiKey='" + apikey + '\'' +
                '}';
    }
}
