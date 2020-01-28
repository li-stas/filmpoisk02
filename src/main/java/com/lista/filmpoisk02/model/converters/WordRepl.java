package com.lista.filmpoisk02.model.converters;

import com.lista.filmpoisk02.model.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordRepl {
    private static final Logger log = LoggerFactory.getLogger(WordRepl.class);
    public String eval(Page oPage, String cPathAndFileDotx) {
        String cFile = cPathAndFileDotx;

        try {
            WordReplaceTextInFormFields wrtiff =  new WordReplaceTextInFormFields();
            XWPFDocument document = new XWPFDocument(new FileInputStream(cPathAndFileDotx));

            WordReplaceTextInFormFields.replaceFormFieldText(document, "title", oPage.getTitle());
            WordReplaceTextInFormFields.replaceFormFieldText(document, "imdbID", oPage.getImdbID());
            WordReplaceTextInFormFields.replaceFormFieldText(document, "year", String.format("%d",oPage.getYear()));
            WordReplaceTextInFormFields.replaceFormFieldText(document, "production", oPage.getProduction());
            WordReplaceTextInFormFields.replaceFormFieldText(document, "poster", oPage.getPoster());

            new WordAddImgFile().eval(document, oPage);

            cFile = new StringBuilder().append(oPage.getTitle()).append("(").append(oPage.getImdbID()).append(")").append(".docx").toString();
            FileOutputStream out = new FileOutputStream(cFile);
            document.write(out);
            out.close();

            log.info("Успешно записан в файл ->" + cFile);
            //System.out.println("Успешно записан в файл");
        } catch (InvalidFormatException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return cFile;
    }


}
