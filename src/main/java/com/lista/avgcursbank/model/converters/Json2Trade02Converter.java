package com.lista.avgcursbank.model.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lista.avgcursbank.model.AO_trade;
import com.lista.avgcursbank.model.Trades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * https://www.baeldung.com/spring-type-conversions
 * Мы еще не закончили. Мы также должны сообщить Spring об этом новом конвертере, добавив
 * StringToEmployeeConverter в FormatterRegistry .
 * Это может быть сделано путем реализации WebMvcConfigurer  и переопределения метода addFormatters() :
 * package com.lista.filmpoisk02.config;
 * WebConfig
 */
@Component
public class Json2Trade02Converter implements Converter<String, Trades> {
    private static final Logger log = LoggerFactory.getLogger(Json2Trade03Converter.class);

    @Override
    public Trades convert(String cTrade) {

        cTrade = cTrade.replace("[","");
        cTrade = cTrade.replace("]",",");
        String[] aTTrade = cTrade.split("},");
        for (int i = 0; i < aTTrade.length; i++) {
            aTTrade[i] += "}";
        }

        Trades oTrades = new Trades(); // список курсов банка
        int j = 0; // для быстрого вход после поиска данных

        for (int i = 0; i < aTTrade.length; i++) {
            cTrade = aTTrade[i];

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> mapTrade;
            try {
                mapTrade = mapper.readValue(cTrade, new TypeReference<Map<String, Object>>() {
                });

                AO_trade oTrade = new AO_trade();

                oTrade.setDate_trade(LocalDate.now());
                oTrade.setId_bank(2);
                oTrade.setName_bank("MinFin");

                oTrade.setRateBuy(new BigDecimal((String) mapTrade.get("bid")));
                oTrade.setRateSell(new BigDecimal((String) mapTrade.get("ask")));

                switch ((String) mapTrade.get("currency")) {
                    case "rub":
                        oTrade.setId_currency(3);
                        oTrade.setName_currency("rub");
                        j++;
                        break;
                    case "eur":
                        oTrade.setId_currency(2);
                        oTrade.setName_currency("eur");
                        j++;
                        break;
                    case "usd":
                        oTrade.setId_currency(1);
                        oTrade.setName_currency("usd");
                        j++;
                        break;
                }

                if (oTrade.getId_currency() != 0) {
                    oTrades.addTrade(oTrade);
                }

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            if (j == 3) {
                break;
            }
        }
        return oTrades;
    }
}

