package com.lista.filmpoisk.model.wordprocessors;

import com.lista.filmpoisk.model.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class WordAddImgFile {
    private static final Logger log = LoggerFactory.getLogger(WordAddImgFile.class);
    public void eval(XWPFDocument document, Page oPage) throws InvalidFormatException, IOException {
        if (oPage.getPosterImg() != null) {
            XWPFParagraph title = document.createParagraph();
            XWPFRun run = title.createRun();
            run.setText("Fig.1 poster:");
            run.setBold(true);
            title.setAlignment(ParagraphAlignment.CENTER);

            String cImageFile = oPage.getPosterImg();
            InputStream is = oPage.getStreamImg();

            log.info("InputStream is = oPage.getStreamImg();");

            run.addBreak();
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, cImageFile, Units.toEMU(200), Units.toEMU(200)); // 200x200 pixels
        }
    }
}
