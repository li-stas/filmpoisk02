package com.lista.filmpoisk02.model.converters;

import com.lista.filmpoisk02.model.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordRepl {
    private static final Logger log = LoggerFactory.getLogger(WordRepl.class);
    public String Eval(Page oPage, String cPathAndFileDotx) {
        String cFile = cPathAndFileDotx;

        try {
            WordReplaceTextInFormFields wrtiff =  new WordReplaceTextInFormFields();
            XWPFDocument document = new XWPFDocument(new FileInputStream(cPathAndFileDotx));

            WordReplaceTextInFormFields.replaceFormFieldText(document, "title", oPage.getTitle());
            WordReplaceTextInFormFields.replaceFormFieldText(document, "imdbID", oPage.getImdbID());
            WordReplaceTextInFormFields.replaceFormFieldText(document, "year", String.format("%d",oPage.getYear()));
            WordReplaceTextInFormFields.replaceFormFieldText(document, "production", oPage.getProduction());
            WordReplaceTextInFormFields.replaceFormFieldText(document, "poster", oPage.getPoster());


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

            cFile = oPage.getTitle()  + "(" + oPage.getImdbID() + ")" + ".docx";
            FileOutputStream out = new FileOutputStream(cFile);
            document.write(out);
            out.close();
            //document.close();
            log.info("Успешно записан в файл ->" + cFile);
            //System.out.println("Успешно записан в файл");
        } catch (InvalidFormatException | IOException e) {
            log.error(e.getMessage());
            //e.printStackTrace();
        }
        return cFile;
    }
}
