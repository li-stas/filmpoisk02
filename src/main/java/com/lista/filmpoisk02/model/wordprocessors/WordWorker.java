package com.lista.filmpoisk02.model.wordprocessors;

import com.lista.filmpoisk02.model.Page;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * https://javadevblog.com/sozdanie-dokumenta-word-v-formate-docx-s-pomoshh-yu-apache-poi.html
 * https://www.codeflow.site/ru/article/java-microsoft-word-with-apache-poi
 */
public class WordWorker {
    private static final Logger log = LoggerFactory.getLogger(WordWorker.class);
    public String create(Page oPage, String cPathAndFileDocx) {
        try {
            // создаем модель docx документа,
            // к которой будем прикручивать наполнение (колонтитулы, текст)
            XWPFDocument docxModel = new XWPFDocument();
            CTSectPr ctSectPr = docxModel.getDocument().getBody().addNewSectPr();
            // получаем экземпляр XWPFHeaderFooterPolicy для работы с колонтитулами
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(docxModel, ctSectPr);

            // создаем верхний колонтитул Word файла
            CTP ctpHeaderModel = createHeaderModel(
                    "Верхний колонтитул - создано с помощью Apache POI на Java :)"
            );
            // устанавливаем сформированный верхний
            // колонтитул в модель документа Word
            XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeaderModel, docxModel);
            headerFooterPolicy.createHeader(
                    XWPFHeaderFooterPolicy.DEFAULT,
                    new XWPFParagraph[]{headerParagraph}
            );

            // создаем нижний колонтитул docx файла
            CTP ctpFooterModel = createFooterModel("Просто нижний колонтитул");
            // устанавливаем сформированый нижний
            // колонтитул в модель документа Word
            XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooterModel, docxModel);
            headerFooterPolicy.createFooter(
                    XWPFHeaderFooterPolicy.DEFAULT,
                    new XWPFParagraph[]{footerParagraph}
            );

            // создаем обычный параграф, который будет расположен слева,
            // будет синим курсивом со шрифтом 14 размера
            XWPFParagraph bodyParagraph = docxModel.createParagraph();
            bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
            //bodyParagraph.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun paragraphConfig = bodyParagraph.createRun();
            paragraphConfig.setItalic(true);
            paragraphConfig.setFontSize(14);
            // HEX цвет без решетки #
            paragraphConfig.setColor("06357a");
            paragraphConfig.setText("Title:" + oPage.getTitle());

            //createParagraph(docxModel).setText("Title:" + oPage.getTitle());
            createParagraph(docxModel).setText("imdbID:" + oPage.getImdbID());
            createParagraph(docxModel).setText("year:" + oPage.getYear());
            createParagraph(docxModel).setText("production:" + oPage.getProduction());
            createParagraph(docxModel).setText("poster:" + oPage.getPoster());

            // вывод изображения
            new WordAddImgFile().eval(docxModel, oPage);

            // сохраняем модель docx документа в файл
            FileOutputStream outputStream = new FileOutputStream(cPathAndFileDocx);
            docxModel.write(outputStream);
            outputStream.close();

            log.info("Успешно записан в файл " + cPathAndFileDocx);            //System.out.println("Успешно записан в файл " + cPathAndFileDocx);

        } catch (FileNotFoundException e) {
            log.error(e.getMessage() + " " + cPathAndFileDocx);//System.out.println(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());//e.printStackTrace();
        }
        return cPathAndFileDocx;
    }

    private static CTP createFooterModel(String footerContent) {
        // создаем футер или нижний колонтитул
        CTP ctpFooterModel = CTP.Factory.newInstance();
        CTR ctrFooterModel = ctpFooterModel.addNewR();
        CTText cttFooter = ctrFooterModel.addNewT();

        cttFooter.setStringValue(footerContent);
        return ctpFooterModel;
    }

    private static CTP createHeaderModel(String headerContent) {
        // создаем хедер или верхний колонтитул
        CTP ctpHeaderModel = CTP.Factory.newInstance();
        CTR ctrHeaderModel = ctpHeaderModel.addNewR();
        CTText cttHeader = ctrHeaderModel.addNewT();

        cttHeader.setStringValue(headerContent);
        return ctpHeaderModel;
    }

    private static XWPFRun createParagraph(XWPFDocument docxModel) {
        // создаем обычный параграф, который будет расположен слева,
                // будет синим курсивом со шрифтом 14 размера
        XWPFParagraph bodyParagraph = docxModel.createParagraph();
        //bodyParagraph.setAlignment(ParagraphAlignment.RIGHT);
        bodyParagraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun paragraphConfig = bodyParagraph.createRun();
        paragraphConfig.setItalic(true);
        paragraphConfig.setFontSize(14);
        // HEX цвет без решетки #
        paragraphConfig.setColor("06357a");
        return paragraphConfig;
    }
}
