package com.lista.filmpoisk02.model.converters;

import com.lista.filmpoisk02.model.Page;
import org.springframework.core.convert.ConversionService;

/**
 * конвертация String cPage -> оPage чз конвертор
 * начальными данными в оPage
 */
public class Json2Page {
    public Page eval(String cPage, Page oPage, ConversionService conversionService) {
        Page oPage01 = conversionService.convert(cPage, Page.class);
        oPage01.setStatus(oPage.getStatus());
        oPage01.setCode(oPage.getCode());
        return oPage01;
    }
}

