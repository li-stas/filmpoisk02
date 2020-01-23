package com.lista.filmpoisk02.model.converters;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;


/**
 * https://myht.ru/question/49678959-zamenit-tekst-shablony-vnutri-docx-apache-poi-d
 */

public class WordReplaceTextInFormFields {

   public static void replaceFormFieldText(XWPFDocument document, String ffname, String text) {
        boolean foundformfield = false;
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                XmlCursor cursor = run.getCTR().newCursor();
                cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//w:fldChar/@w:fldCharType");
                while(cursor.hasNextSelection()) {
                    cursor.toNextSelection();
                    XmlObject obj = cursor.getObject();
                    if ("begin".equals(((SimpleValue)obj).getStringValue())) {
                        cursor.toParent();
                        obj = cursor.getObject();
                        obj = obj.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//w:ffData/w:name/@w:val")[0];
                        if (ffname.equals(((SimpleValue)obj).getStringValue())) {
                            foundformfield = true;
                        } else {
                            foundformfield = false;
                        }
                    } else if ("end".equals(((SimpleValue)obj).getStringValue())) {
                        if (foundformfield) return;
                        foundformfield = false;
                    }
                }
                if (foundformfield && run.getCTR().getTList().size() > 0) {
                    run.getCTR().getTList().get(0).setStringValue(text);
                    //System.out.println(run.getCTR());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        XWPFDocument document = new XWPFDocument(new FileInputStream("FilmPoisk.dotx"));

        replaceFormFieldText(document, "title", "Black Cat, White Cat");
        replaceFormFieldText(document, "imdbID", "a625f1bf");
        replaceFormFieldText(document, "year", String.format("%d", 1998));
        replaceFormFieldText(document, "production", "October Films");
        replaceFormFieldText(document, "poster", "https://m.media-amazon.com/images/M/MV5BMmExZTZhN2QtMzg5Mi00Y2M5LTlmMWYtNTUzMzUwMGM2OGQ3XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg");

        FileOutputStream out = new FileOutputStream("Black Cat, White Cat (" + "a625f1bf" + ").docx");
        document.write(out);
        out.close();
        //document.close();
    }
}