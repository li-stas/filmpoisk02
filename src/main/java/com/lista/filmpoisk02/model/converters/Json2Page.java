package com.lista.filmpoisk02.model.converters;

import com.lista.filmpoisk02.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class Json2Page {

    private final ConversionService conversionService;
    @Autowired
    public Json2Page(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public Page eval(String cPage, Page oPage) {
        Page oPage01 = conversionService.convert(cPage, Page.class);
        oPage01.setStatus(oPage.getStatus());
        oPage01.setCode(oPage.getCode());
        return oPage01;
    }
}

