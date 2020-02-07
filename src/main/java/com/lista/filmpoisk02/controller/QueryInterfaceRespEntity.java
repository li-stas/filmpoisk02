package com.lista.filmpoisk02.controller;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface QueryInterfaceRespEntity {
    ResponseEntity<InputStreamResource> querying(String cSeekId);
}
