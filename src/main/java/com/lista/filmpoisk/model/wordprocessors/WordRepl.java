package com.lista.filmpoisk.model.wordprocessors;

import com.lista.filmpoisk.model.Page;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WordRepl {
    private static final Logger log = LoggerFactory.getLogger(WordRepl.class);

    public String saveToFile(Page oPage, String cPathAndFileDotx, String cFileParam) {
        String cFile = cFileParam;
        if (cFile.isEmpty()) {
            cFile = new StringBuilder().append(oPage.getTitle()).append("(").append(oPage.getImdbID())
                    .append(")").append(".docx").toString();
        }
        try {
            XWPFDocument document = getXWPFDocument(oPage, cPathAndFileDotx);

            FileOutputStream out = new FileOutputStream(cFile);
            document.write(out);
            out.close();

            log.info("Успешно записан в файл ->" + cFile);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return cFile;
    }

    public XWPFDocument getXWPFDocument(Page oPage, String cPathAndFileDotx)  {
        //WordReplaceTextInFormFields wrtiff =  new WordReplaceTextInFormFields();
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(new FileInputStream(cPathAndFileDotx));
            WordReplaceTextInFormFields.replace(document, "title", oPage.getTitle());
            WordReplaceTextInFormFields.replace(document, "imdbID", oPage.getImdbID());
            WordReplaceTextInFormFields.replace(document, "year", String.format("%d",oPage.getYear()));
            WordReplaceTextInFormFields.replace(document, "production", oPage.getProduction());
            WordReplaceTextInFormFields.replace(document, "poster", oPage.getPoster());

            new WordAddImgFile().eval(document, oPage);

        } catch (InvalidFormatException | IOException e) {
            log.error(e.getMessage(), e);
        }

        return document;
    }


}
