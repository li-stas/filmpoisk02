package com.lista.filmpoisk.model.wordprocessors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import java.util.Objects;


/**
 * https://myht.ru/question/49678959-zamenit-tekst-shablony-vnutri-docx-apache-poi-d
 */

public class WordReplaceTextInFormFields {

   public static void replace(XWPFDocument document, String ffname, String text) {
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
                        foundformfield = Objects.equals(ffname, ((SimpleValue) obj).getStringValue());
                    } else if ("end".equals(((SimpleValue)obj).getStringValue())) {
                        if (foundformfield) {
                            return;
                        }
                        foundformfield = false;
                    }
                }
                if (foundformfield && run.getCTR().getTList().size() > 0) {
                    run.getCTR().getTList().get(0).setStringValue(text);
                }
            }
        }
    }

}