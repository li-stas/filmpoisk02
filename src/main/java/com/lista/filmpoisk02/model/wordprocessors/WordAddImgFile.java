package com.lista.filmpoisk02.model.wordprocessors;

import com.lista.filmpoisk02.model.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileInputStream;
import java.io.IOException;

public class WordAddImgFile {
    public void eval(XWPFDocument document, Page oPage) throws InvalidFormatException, IOException {
        if (oPage.getPosterImg() != null) {
            XWPFParagraph title = document.createParagraph();
            XWPFRun run = title.createRun();
            run.setText("Fig.1 poster:");
            run.setBold(true);
            title.setAlignment(ParagraphAlignment.CENTER);
            String imgFile =  oPage.getPosterImg();
            FileInputStream is = new FileInputStream(imgFile);
            run.addBreak();
            run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(200), Units.toEMU(200)); // 200x200 pixels
        }
    }
}
