package com.lista.filmpoisk02.model.services;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface GetDocxStream {
    ResponseEntity<InputStreamResource> download(XWPFDocument docx, String fileName) throws IOException;
}
